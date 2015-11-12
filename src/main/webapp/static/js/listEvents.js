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
		initControls();
	});
	$('.editButton').click(function(){
		$('.ajaxForm').clearForm();
		initControls();
		var selections = $table.bootstrapTable('getSelections');
		loadItem(selections[0].ID);
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
					$.getJSON(deleteUrl + "/" + this.id, function(json){
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
		$(".editButton").prop('disabled', !single);
		$(".deleteButton").prop('disabled', !$table.bootstrapTable('getSelections').length);

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
	params['limit']=p.limit;
	params['offset']=p.offset;
	return params;
}

function getIdSelections() {
	return $.map($table.bootstrapTable('getSelections'), function (row) {
		return row.ID
	});
}
