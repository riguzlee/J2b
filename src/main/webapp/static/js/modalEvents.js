$(function() {
	console.log('binding ajax submit events...');
	$('.ajaxSubmit').click(function(){
    
		var thisForm=$(this).parent().parent().children(".tip").children("form");
		console.log("modalEvents------>thisForm--ID:"+thisForm.attr("id"))
		var formData = thisForm.serialize();
		var url = '';
		if(thisForm.attr("id")=="user_role_ajaxForm"){
			url=thisForm.attr('add-action');
		}
		else{
		var id = $("input[name$='.id']").val();
		if(id == null || id == '')
			url = thisForm.attr('add-action');
		else
			url = thisForm.attr('update-action') + "/" + id;
		}
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
				}else
					alert('错误：' + data.data);
			}
		});
		
	});
});


function validateAndSubmit(formId){
	console.log(formId);
	var formData = $(formId).serialize();
	var url = '';
	var id = $("input[name$='.id']").val();
	if(id == null || id == '')
		url = $(formId).attr('add-action');
	else
		url = $(formId).attr('update-action') + "/" + id;
	$(formId).validate({
	}).form();
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
			}
			else
				alert('错误：' + data.data);
		}
	});
}

