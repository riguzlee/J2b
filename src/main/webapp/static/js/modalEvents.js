$(function() {
	console.log('binding ajax submit events...');
	$('.ajaxSubmit').click(function(){
		var formData = $('.ajaxForm').serialize();
		var url = '';
		var id = $("input[name$='.id']").val();
		if(id == null || id == '')
			url = $('.ajaxForm').attr('add-action');
		else
			url = $('.ajaxForm').attr('update-action') + "/" + id;
		console.log('Posting..' + url);
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
					alert('错误：' + error);
			}
		});
	});
});