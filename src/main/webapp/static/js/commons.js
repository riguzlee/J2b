function formatDate(date){
	return date.getFullYear() + '/' + (date.getMonth() + 1) + '/' + date.getDate();
}

function loadRoles(selectId){
	var url="/system/getAllRolesButSuperAdmin";
	$.getJSON(url, function(json){
		if(json.error != 0){
			alert('获取角色数据失败:' + json.data);
			return;
		}
		var roles = json.data;
		$.each(roles, function(index, item){
			$(selectId).append("<option value='"+item.id+"'>"+item.name+"</option>"); 
		});
	});
}

function loadCompanies(selectId,rfunction){
	var curl="/company/getAllCompanies";
	$.getJSON(curl, function(json){
		if(json.error != 0){
			alert('获取数据失败:' + json.data);
			return;
		}
		var hotels = json.data;   	
		$.each(hotels, function(index, item){
			$(selectId).append("<option value='"+item.id+"'>"+item.name+"</option>"); 
		});
		if(rfunction)
		rfunction($(selectId));//数据加载完成回调
	});
}