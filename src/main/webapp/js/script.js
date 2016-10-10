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

function withdrawMoney(amount, id){
	$.ajax({
		url:"/webapps7/withdraw?" + $.param({
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

function deletePortfolio(id) {
	$.ajax({
		url:"/webapps7/portfolio/delete?" + $.param({
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

function buyStock(){
	$('#buy-share-input').val(
			$('#buy-share-input').val().trim());
	$('#buy-symbol-input').val(
			$('#buy-share-input').val().trim().toUpperCase());
	var number = $('#buy-share-input').val();
	if (isNaN(number) || parseInt(Number(number)) != number
			|| parseInt(Number(number)) < 0
			|| parseInt(Number(number)) > 2000000
			|| isNaN(parseInt(number, 10))) {
		$('#buy-share-input').addClass('invalid');
		$('#buy-share-label').attr('data-error',
				"Invalid shares value");
		return;
	} else {
		$.ajax({
			url : "/webapps7/stock/buy",
			data : $('#add-stock-form').serialize(),
			async : false,
			cache : false,
			type : "POST",
			success : function(response) {
				if (response.status == 'failed') {
					$('#buy-symbol-input').addClass(
							'invalid');
					$('#buy-symbol-label').attr(
							'data-error',
							response.errorMessage);
					return;
				} else {
					location.reload();
				}
			}
		});
	}
}