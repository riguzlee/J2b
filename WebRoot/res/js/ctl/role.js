$(document).ready(function () {
	var grid_selector = "#jqGrid";
	var pager_selector = "#jqGridPager";

	//resize to fit page size
	$(window).on('resize.jqGrid', function () {
		$(grid_selector).jqGrid( 'setGridWidth', $(".page-content").width() );
	})
	//resize on sidebar collapse/expand
	var parent_column = $(grid_selector).closest('[class*="col-"]');
	$(document).on('settings.ace.jqGrid' , function(ev, event_name, collapsed) {
		if( event_name === 'sidebar_collapsed' || event_name === 'main_container_fixed' ) {
			//setTimeout is for webkit only to give time for DOM changes and then redraw!!!
			setTimeout(function() {
				$(grid_selector).jqGrid( 'setGridWidth', parent_column.width() );
			}, 0);
		}
	})


	$("#jqGrid").jqGrid({
		url: 'system/roles/list',
		mtype: "GET",
		datatype: "json",
		caption: "角色列表",
		
		colNames: ['角色名称', '角色标识', '备注'],
		colModel: [
		           { name: 'NAME', width: 75 },
		           { name: 'IDENT', width: 150 },
		           { name: 'REMARK', width: 150 }
		           ],
		           page: 1,                  //当前页          page=1
		           height:250,               //高度
		           viewrecords:true,         //显示总记录数 
		           rowNum: 10,               //每页显示记录数  rows=10
		           autowidth: true,          //自动匹配宽度 
		           multiselect: true,        //可多选，为true则出现复选框
		           multiselectWidth: 25,     //设置多选列宽度		           
		           sortable:true,            //可以排序:参数将传递给后台 
		           sortname: 'NAME',         //排序字段名 sidx=NAME
		           sortorder: "desc",        //排序方式   sord=desc
		           pager: "#jqGridPager",
		           loadComplete : function() {
		        	   var table = this;
		        	   setTimeout(function(){
		        		   styleCheckbox(table);

		        		   updateActionIcons(table);
		        		   updatePagerIcons(table);
		        		   enableTooltips(table);
		        	   }, 0);
		           },
	});
	$(window).triggerHandler('resize.jqGrid');
});