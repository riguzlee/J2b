$(function(){
	console.log("form validate enabled.");

	jQuery.validator.addMethod("gateMac", function(value, element) {
		console.log("form validate gateMac....."+value);
	    return this.optional(element) || /^[0-9A-F]{12}$/.test(value);
	}, "网关物理地址（串号）必须是12位十六进制数字母大写");   
	
	$(".ajaxForm").validate();
})