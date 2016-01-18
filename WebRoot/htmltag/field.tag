<div class="form-group">
	@if(required == "true"){
    <label for="${id}">${label}<tp:required/></label>
    <input id="${id}" class="form-control" name="${name}" placeholder="${desc!}" required ${valid!}>
    @}else{
    <label for="${id}">${label}</label>
    <input id="${id}" class="form-control" name="${name}" placeholder="${desc!}" ${valid!} }>
    @}
    
</div>