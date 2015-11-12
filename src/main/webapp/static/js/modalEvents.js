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

