function statusFormatter(value, row) {
	if(value == 0)
		return '<div class="silk-user_green"/>' + "正常";
	if(value == 1)
		return  "未激活";
	if(value == 2)
		return '<div class="silk-user_delete"/>' + "冻结";
	return '<i class="silk-error"/>' + '错误';
}
function powerFormatter(value,row){
	var className='lockPower power-';
	if(80<=value){
		className+='5';
	}else if(60<=value){
		className+='4';
	}else if(40<=value){
		className+='3';
	}else if(20<=value){
		className+='2';
	}else if(0<=value){
		className+='1';
	}//<div class="'+className+'"/>
	var rstr='<span>'+value+'%</span>';
	return rstr;
}
function lockStatusFormatter(value,row){
	if(value==0)
		return '未效验';
	if(value==100)
		return '不存在';
	if(value==110)
		return '离线';
	if(value==111)
		return '正常';
	return '错误';
}

/**
 * 门锁操作日志 操作行为格式化 定义各个编码所代表的行为
 * @param value
 * @param row
 * @returns {String}
 */
function operationFormatter(value,row){
	if(value==1){
		return '开锁';
	}
	return '未定义行为代码,请到系统管理员到：formatter.js中operationFormatter 定义行为代码';
}
/**
 * web操作记录 操作代码格式化<br>
 * 用于web操作行为记录 在formatter.js中webOperationFormatter 定义operation编号含义 model<br>
 * 操作编码(WebLog.java与formatter.js同步)：<br/>
 * 
 * 第一二位 不足两位前面空位：<br>
 * 新增：1<br>
 * 删除：2 <br>
 * 修改：3 <br>
 * 取消（记录未删除,业务上数据还会使用,仅存在状态改变）：4 <br>
 * 查看一条：5 <br>
 * 查看多条：6 <br>
 * 第三四位(不足两位前面补0)： <br>
 * "用户"：0<br>
 * "网关":1<br>
 * "门锁":2<br>
 * "单位":3<br>
 * "角色":4<br>
 * "告警":5<br>
 * @param value
 * @param row
 * @returns {String}
 */
function webOperationFormatter(value,row){
	/*if(value==1){
		return 'web操作行为1';
	}
	if(value==405){
		return '取消告警';
	}*/
	if(value>=100){
	var ope=new Array("XXX","新增","删除","修改","取消","查看一条","查看多条");
	var objs=new Array("用户","网关","门锁","单位","角色","告警");
	var operationNum=Number(value);
	var op=parseInt(operationNum/100);
	var obj=operationNum%100;
	var  rs=ope[op]+objs[obj];
	if(!rs){
		rs='未定义行为代码:'+value;
	}
	return rs;
	}
	return '未定义行为代码:'+value+',请到系统管理员到：formatter.js中webOperationFormatter 定义行为代码';
}

/**
 * 门锁操作日志 操作结果编码 格式化
 * @param value
 * @param row
 * @returns {String}
 */
function operation_resFormatter(value,row){
	if(value==1){
		return '成功';
	}
	return '失败';
}
/**
 * 告警类型
 * @param value
 * @param row
 * @returns {String}
 */
function alarmTFormatter(value,row){
	if(value==1){
		return '网关电量过低';
	}
	if(value==2){
		return '门锁电量过低';
	}
	if(value==3){ 
		return '门锁打开次数超限';
	}
	if(value==4){
		return '门锁虚掩';
	}
	if(value==5){
		return '强行传入';
	}
	
	return '未定义类型';
}
function alarmStateFormatter(value,row){
	if(value==1)
		return '告警中';
		return '已取消';
}
