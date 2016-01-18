function lockUser(lock){
	var selections = $table.bootstrapTable('getSelections');
	var lockMsg = "冻结";
	if(!lock)
		lockMsg = "解除冻结";
	var confirm = $.scojs_confirm({
		content: "是否确认要" + lockMsg + selections.length + '个用户？',
		action: function() {
			var lockUrl = "system/users/lock";
			if(!lock)
				lockUrl = "system/users/unlock";
			var locked = 0;
			$.ajaxSettings.async = false; 
			$.each(selections, function(){					
				$.getJSON(lockUrl + "/" + this.ID, function(json){
					if(json.error != 0){
						console.log(lockMsg+  '失败'+$.scojs_message.TYPE_ERROR);
						$.scojs_message(lockMsg+'失败！', $.scojs_message.TYPE_ERROR);							
						return;
					}else{
						locked += 1;
					}
				});
			});
			$.scojs_message(lockMsg+'完成，共' + lockMsg + '了' + locked + '个用户', $.scojs_message.TYPE_OK);
			$table.bootstrapTable('refresh');
		}
	});
	confirm.show();
}
function grantRole(){
	var $table = $('.dataTable');
	var selections = $table.bootstrapTable('getSelections');
	if(selections.length == 1){
		//$("input[name='user.USER_ID']").val(users);
	}
	var users = "";
	var userNames = "";
	$.each(selections, function(){
		users += this.ID + ",";
		userNames += this.LOGIN_NAME + " ";
	});
	$("#usersToGrant").text(userNames);
	$("input[name='user.USER_ID']").val(users);
	$("#grantRoleDialog").modal("show");
}
function statusFormatter(value,row){
	if(value=='W')
		return '待激活';
	if(value=='N')
		return '正常';
	if(value=='L')
		return '已冻结';
	return "-";
}