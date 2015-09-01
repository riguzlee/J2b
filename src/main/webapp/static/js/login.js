$(function() {
	console.log('binding ajax submit events...');
	$('.loginButton').click(function(){
		var formData = $('.loginForm').serialize();
		var url = $('.loginForm').attr('action');
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
					$.scojs_message('登录成功！', $.scojs_message.TYPE_OK);
					$(window.location).attr('href', '/');
				}
				else
					$.scojs_message('登录失败:' + data.data, $.scojs_message.TYPE_ERROR);
			}
		});
	});
});