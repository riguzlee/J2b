var $table = $('.dataTable');
$(function() {
	console.log('binding toolbar events...');

	$ok = $('.searchButton');
	$ok.click(function () {
		var params = {};	
		params['offset']=0;
		$table.bootstrapTable('refresh',{query: params});
	});
	$table.on('check.bs.table uncheck.bs.table ' + 'check-all.bs.table uncheck-all.bs.table', function () {
		var single = false;
		if($table.bootstrapTable('getSelections').length == 1)
			single = true;
		$("#edit-role").prop('disabled', !single);
		$("#allot-user-role").prop('disabled', !single);
		$("#delete-role").prop('disabled', !$table.bootstrapTable('getSelections').length);
		var selectrows=$table.bootstrapTable('getSelections');
		var n=0;
		for(var i=0;i<selectrows.length;i++){
			var alarm_state=selectrows[i].alarm_state;
			if(alarm_state && alarm_state==1){
				n+=1;
			}
		}
		$("#edit-alarm").prop('disabled', !n);

		// save your data, here just save the current page
		selections = getIdSelections();
		// push or splice the selections if you want to save all data selections
	});
	$('.addButton').click(function(){
		$('.ajaxForm').clearForm();
		initControls();
	});
	$('.editButton').click(function(){
		$('.ajaxForm').clearForm();
		initControls();
		var selections = $table.bootstrapTable('getSelections');
		loadItem(selections[0].id);
	});

	$('.cancelAlarmButton').click(function(){
		var selections = $table.bootstrapTable('getSelections');
		var sum=0;
		for(var i=0;i<selections.length;i++){
			var alarm_state=selections[i].alarm_state;
			if(alarm_state && alarm_state==1){
				sum+=1;
			}
		}
		var confirm = $.scojs_confirm({
			content: "确定关闭" + sum + '个报警？',
			action: function() {
				var url=$('.cancelAlarmButton').attr('cancel-action');
				var cancelN=0;
				$.ajaxSettings.async = false; 
				$.each(selections, function(){	
					if(this.alarm_state==1){
						$.getJSON(url + "/" + this.id, function(json){
						if(json.error != 0){
							$.scojs_message('解除失败！', $.scojs_message.TYPE_ERROR);
							return;
						}
						else{
							cancelN += 1;
						}
						});
					}
				});
				$.scojs_message('解除完成，共解除了' + cancelN + '个警告', $.scojs_message.TYPE_OK);
				$table.bootstrapTable('refresh');
			}
		});
		confirm.show();
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
							//$.scojs_message('删除失败！', $.scojs_message.TYPE_ERROR);
							$.scojs_message(json.data,$.scojs_message.TYPE_Ok);	
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
$("input").blur(function(){
	var str=$(this).val();
	console.log('除去前后空格前：|'+str+'|');
	str=$.trim(str);
	console.log('除去前后空格后：|'+str+'|');
	$(this).val(str);
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
function logQueryParams(p){
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

function responseHandler(res) {
	return res.rows;
}
function getIdSelections() {
	return $.map($table.bootstrapTable('getSelections'), function (row) {
		return row.id
	});
}
$(function(){
	$(".selectNowVCompanies").one("click",function(){
		var n=$(this).find("option").length;
		if(n==0){
		loadCompanies(this);
		}
	});
	$(".selectModelList").one("click",function(){
		loadModelList(this);
	});
});
/**
 * 获取指定网关下可用的位置序号
 */
function loadCanUseSerial_Number(select,gate_string,selectedValue){
	console.log('loadCanUseSerial_Number ...'+gate_string+' ,'+selectedValue);
	//getCanUseSerial_NumberByGate_String
	if(select && gate_string){
	var url=url="/gate/getCanUseSerial_NumberByGate_String?gate_string="+gate_string;
	$.ajaxSettings.async = false;
	$.getJSON(url, function(json){
    	var options=json.data;
    	var selectobj=$(select);
    
    	for(var i=0;i<options.length;i++){
    		var option=options[i];
    		selectobj.append("<option  value='"+option+"'>"+option+"</option>"); 
    	}
    	if(selectedValue){
    		selectobj.find("option[value='"+selectedValue+"']").select();
    	}else{
    		selectobj.find("option:eq(0)").attr("selected",true);
    		var v=selectobj.find("option:eq(0)").val();
    		selectobj.val(v);
    	}
    	selectobj.change();
    });
	}
}
/**
 *  获取单位下网关
 * @param select 加载到哪个下拉框
 * @param companyName 单位id
 * @param selectedValue 选中值
 */
function loadGateByCompanyID(select,company_Id,selectedValue){
	console.log('loadGateByCompanyID ...company_Id='+company_Id+' ,selectedValue='+selectedValue);
	if(select && company_Id){
		var url=url="/gate/getGatesAllByCompanyId?company_id="+company_Id;
		$.getJSON(url, function(json){
	    	var options=json.data;
	    	var selectobj=$(select);
	    	for(var i=0;i<options.length;i++){
	    		var option=options[i];
	    		selectobj.append("<option  value='"+option.gate_string+"'>"+option.code+"</option>"); 
	    	}
	    	if(selectedValue){
	    		selectobj.find("option[value='"+selectedValue+"']").attr("selected",true);
	    	}else{
	    		selectobj.find("option:eq(0)").attr("selected",true);
	    		var v=selectobj.find("option:eq(0)").val();
	    		selectobj.val(v);
	    	}
	    	selectobj.change();
	    });
	}
}

/**
 * 获取web操作记录对象列表
 * @param selectobj
 */
function loadModelList(select){
	var url=url="/webLog/getWebModels";
	
	  $.getJSON(url, function(json){
	    	var options=json.data;
	    	var selectobj=$(select);
	    	selectobj.empty();
	    	selectobj.append("<option value=''>所有可见</option>"); 
	    	for(var i=0;i<options.length;i++){
	    		var option=options[i];
	    		selectobj.append("<option  value='"+option+"'>"+option+"</option>"); 
	    	}
	    	
	    });
}

