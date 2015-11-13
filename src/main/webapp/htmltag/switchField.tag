<div class="form-group">
	<label for="${id}" class="col-sm-2 col-xs-4">${label}
	@if(required == "true"){
		<tp:required/>
	@}
	</label>
    <div class="col-sm-10 col-xs-8">
    	<input id="${id}" type="checkbox" class="switch" name="${name}" 
	    	data-on-text="${on}" 
	    	data-off-text="${off}" 
	    	data-off-color="warning" 
	    	@if(checked == "true"){
				checked
			@}
	    	${valid!}>
    </div>
</div>