var $table = $('.dataTable');
$(function() {
	console.log('binding toolbar events...');

	$ok = $('.searchButton');
	$ok.click(function () {
		var params = {};	
		params['offset']=0;
		$table.bootstrapTable('refresh',{query: params});
	});
	$('.addButton').click(function(){
		$('.ajaxForm').clearForm();
		
		try {  
			if(typeof(eval("clearControls"))=="function"){
				clearControls();
			}
		}catch(e){
			console.log("no clearControls function defined!");
		}  
	});
	$('.editButton').click(function(){
		$('.ajaxForm').clearForm();
		try {  
			if(typeof(eval("initControls"))=="function"){
				initControls();
			}
		}catch(e){
			console.log("no reload function defined!");
		} 
		var selections = $table.bootstrapTable('getSelections');
		if(selections.length != 1){
		$(".singleSelect").prop('disabled', true);
		}else{
		loadItem(selections[0].ID);
		}
	});

	$('.deleteButton').click(function(){
		var selections = $table.bootstrapTable('getSelections');
		var confirm = $.scojs_confirm({
			content: "是否确认要删除" + selections.length + '个项目？',
			action: function() {
				var deleteUrl = $('.deleteButton').attr('delete-action');
				var deleted = 0;
				$.ajaxSettings.async = false; 
				$.each(selections, function(){					
					$.getJSON(deleteUrl + "/" + this.ID, function(json){
						if(json.error != 0){
							console.log('删除失败'+$.scojs_message.TYPE_ERROR);
							$.scojs_message('删除失败！', $.scojs_message.TYPE_ERROR);							
							return;
						}else{
							deleted += 1;
						}
					});
				});
				$.scojs_message('删除完成，共删除了' + deleted + '个项目', $.scojs_message.TYPE_OK);
				$table.bootstrapTable('refresh');
			}
		});
		confirm.show();
	});
	
	$table.on('check.bs.table uncheck.bs.table ' + 'check-all.bs.table uncheck-all.bs.table', function () {
		var single = false;
		if($table.bootstrapTable('getSelections').length == 1)
			single = true;
		$(".singleSelect").prop('disabled', !single);
		$(".multSelect").prop('disabled', !$table.bootstrapTable('getSelections').length);

		// save your data, here just save the current page
		selections = getIdSelections();
		// push or splice the selections if you want to save all data selections
	});
	
});

function queryParams(p) {
	var params = {};
	$('#toolbar').find('input[name]').each(function () {
		params[$(this).attr('name')] = $(this).val();
	});
	$('#toolbar').find('select[name]').each(function () {
		params[$(this).attr('name')] = $(this).val();
	});
	
	params['limit']=p.limit;
	params['offset']=p.offset;
	return params;
}


function getFormAction(){
	
}
function validateAndSubmit(formId){
	console.log(formId);
	var formData = $(formId).serialize();
	var url = '';
	var id = $("input[name$='_ID']").val();
	console.log(id);
	if(id == null || id == '')
		url = $(formId).attr('add-action');
	else
		url = $(formId).attr('update-action') + "/" + id;
	
	var action = $(formId).attr('action');
	if(action != null && action != undefined)
		url = action;
	$(formId).validate({}).form();
	if (!$(formId).valid())
		return;
	$.ajax({
		type:'post',
		url:url,
		data:formData,
		cache:false,
		dataType:'json',
		success:function(data){
			var error = data.error;
			if(error == '0'){
				$('.modal').modal('hide')
				$.scojs_message('操作成功！', $.scojs_message.TYPE_OK);
				var $table = $('.dataTable');
				$table.bootstrapTable('refresh');
				try {  
					if(typeof(eval("reloadData"))=="function"){
						reloadData();
					}
				}catch(e){
					console.log("no reload function defined!");
				}  
				setTimeout(function(){
					var selections = $table.bootstrapTable('getSelections');
					if(selections.length != 1){
						$(".singleSelect").prop('disabled', true);
						}
				},2000);
				
			}
			else
				$.scojs_message(data.data, $.scojs_message.TYPE_ERROR);
		}
	});
}