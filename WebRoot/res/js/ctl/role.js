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
		caption: "jqGrid with inline editing",
		viewrecords : true,
		altRows: true,
		colNames: ['Order ID', 'Customer ID'],
		colModel: [
		           { label: 'OrderID', name: 'NAME', width: 75 },
		           { label: 'Customer ID', name: 'IDENT', width: 150 }
		           ],
		           page: 1,
		           height:250,
		           rowNum: 10,
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