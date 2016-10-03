function addNewPortfolio(){
	$.ajax({
		url:"/webapps7/portfolio/add",
		type:"POST",
		data : $('#add-port-form').serialize(),
		async: false,
		cache: false,
		dataType : 'json',
		success: function(response){
			if(response.status == "failed") {
				$('#add-port-name-input').addClass(
				'invalid');
				$('#add-port-name-label').attr(
						'data-error',
						response.errorMessage);
				return;
			} else {
				location.reload();
				return;
			}
		}
	})
}