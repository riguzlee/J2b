var $table = $('.dataTable');
$(function() {
	console.log('binding toolbar events...');
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
});

function clearModalDialog(){
	clearForm('.editForm');
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
	ajaxPost(url, formData, function(data){
		jbAlert('操作成功!');
		closeModal();
		reloadGrid();
	});	
}