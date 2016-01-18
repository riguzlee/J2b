<div class="form-group">
	<label for="${id}" class="col-sm-2 col-xs-4">${label}
	@if(required == "true"){
		<tp:required/>
	@}
	</label>
    <div class="col-sm-10 col-xs-8">
    	<input id="${id}" class="form-control ${class!}" name="${name}" placeholder="${desc!}"
    	@if(required == "true"){ 
    		required 
    	@}
    	${valid!} ${type!}>
    </div>
</div>