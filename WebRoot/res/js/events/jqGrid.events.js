/*
 * For more about jqGrid, ref:http://www.helloweba.com/view-blog-164.html
 */
$(function() {
	console.log('binding jqGrid events...');
	$('.grid-search').click(function(){
		console.log('searching...');
		var searchFormData = $(".search-form").serializeJson();
		console.log(searchFormData);
		$("#jqGrid").jqGrid('setGridParam',{ 
            postData:searchFormData, //发送数据 
            page:1 
        }).trigger("reloadGrid"); //重新载入 
	});
});