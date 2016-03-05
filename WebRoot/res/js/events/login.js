$(function() {
	console.log('binding ajax submit events...');
	$('#loginBtn').click(function(){
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
					jbAlert('登录成功！', '提示');
					$(window.location).attr('href', '/');
				}
				else{
					jbAlert('登录失败:' + data.data, '提示');
					$.scojs_message('登录失败:' + data.data, $.scojs_message.TYPE_ERROR);
					$(".randpic").click();
				}
			}
		});
	});
	
	$('#sendResetBtn').click(function(){
		var formData = $('.resetForm').serialize();
		var url = $('.resetForm').attr('action');
		$.ajax({
			type:'post',
			url:url,
			data:formData,
			cache:false,
			dataType:'json',
			success:function(data){
				var error = data.error;
				if(error == '0'){
					$.scojs_message('发送成功！', $.scojs_message.TYPE_OK);
					$(window.location).attr('href', '/anon/reset');
				}
				else
					$.scojs_message('发送失败:' + data.data, $.scojs_message.TYPE_ERROR);
			}
		});
	});
	
	$('#resetBtn').click(function(){
		var formData = $('.resetForm').serialize();
		var url = $('.resetForm').attr('action');
		$.ajax({
			type:'post',
			url:url,
			data:formData,
			cache:false,
			dataType:'json',
			success:function(data){
				var error = data.error;
				if(error == '0'){
					alert('重置密码成功！');
					$(window.location).attr('href', '/');
				}
				else
					$.scojs_message('重置密码失败:' + data.data, $.scojs_message.TYPE_ERROR);
			}
		});
	});
});