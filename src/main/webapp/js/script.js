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

function depositMoney(amount, id){
	$.ajax({
		url:"/webapps7/deposit?" + $.param({
			"amount": amount,
			"id": id
		}),
		type:"POST",
		async: false,
		cache: false,
		success: function(response){
			if(response.status == 'failed'){
				displayToast(
						response.errorMessage,
						5000);
				return;
			} else {
				location.reload();
			}
		}
	})
}