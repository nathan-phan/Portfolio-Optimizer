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

function buyStock(rate1, rate2){
	$('#buy-share-input').val(
			$('#buy-share-input').val().trim());
	$('#buy-symbol-input').val(
			$('#buy-symbol-input').val().trim().toUpperCase());
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
		$('#add-stock-modal').closeModal();
		$.ajax({
			url:"/webapps7/stock/checkprice?" + $.param({
				symbol: $('#buy-symbol-input').val()
			}),
			cache: false,
			type: "GET",
			success: function(response){
				if(response.status=='failed'){
					alert(response.errorMessage); 
					return;
				} else {
					var price = response.price;
					if($('#buy-symbol-input').val().toUpperCase().indexOf('NSE') >= 0){
						var converted = (Number($('#buy-share-input').val())*price/Number(rate1)).toFixed(2);
						$('#confirm-buy-cost').text(converted)
					}
					else if($('#buy-symbol-input').val().toUpperCase().indexOf('.SI') >= 0){
						var converted = (Number($('#buy-share-input').val())*price/Number(rate2)).toFixed(2);
						$('#confirm-buy-cost').text(converted)
					} else {
						var shares = Number($('#buy-share-input').val());
						var total = shares*price;
						$('#confirm-buy-cost').text(total.toFixed(2));
					}
				}
			}
		})
		$('#buy-stock-confirm-modal').openModal();
	}
}

function confirmBuyStock(){
	$.ajax({
		url : "/webapps7/stock/buy",
		data : $('#add-stock-form').serialize(),
		async : false,
		cache : false,
		type : "POST",
		success : function(response) {
			if (response.status == 'failed') {
				$('#buy-stock-confirm-modal').closeModal();
				$('#error-modal .modal-content').text(response.errorMessage);
				$('#error-modal').openModal();
				return;
			} else {
				location.reload();
			}
		}
	});
}

function sellStock(rate1, rate2){
	$('#sell-share-input').val(
			$('#sell-share-input').val().trim());
	var number = $('#sell-share-input').val();
	if (isNaN(number) || parseInt(Number(number)) != number
			|| parseInt(Number(number)) < 0
			|| parseInt(Number(number)) > 2000000
			|| isNaN(parseInt(number, 10))) {
		$('#sell-share-input').addClass('invalid');
		$('#sell-share-label').attr('data-error',
		"Invalid shares value");
		return;
	}
	else if (Number($("#available-share").val()) < number) {
		$('#sell-share-input').addClass('invalid');
		$('#sell-share-label').attr('data-error',
		"Number exceeds available shares");
		return;
	}
	else {
		$('#sell-stock-modal').closeModal();
		$.ajax({
			url:"/webapps7/stock/checkprice?" + $.param({
				symbol: $('#sell-symbol-input').val()
			}),
			cache: false,
			type: "GET",
			success: function(response){
				if(response.status=='failed'){
					alert(response.errorMessage); 
					return;
				} else {
					var price = response.price;
					if($('#sell-symbol-input').val().toUpperCase().indexOf('NSE') >= 0){
						var converted = (Number($('#sell-share-input').val())*price/Number(rate1)).toFixed(2);
						$('#confirm-sell-cost').text(converted)
					}
					else if($('#sell-symbol-input').val().toUpperCase().indexOf('.SI') >= 0){
						var converted = (Number($('#sell-share-input').val())*price/Number(rate2)).toFixed(2);
						$('#confirm-sell-cost').text(converted)
					}
					else {
						$('#confirm-sell-cost').text((Number($('#sell-share-input').val())*price).toFixed(2));
					}

				}
			}
		})
		$('#sell-stock-confirm-modal').openModal();
	}
}

function confirmSellStock(){
	$.ajax({
		url : "/webapps7/stock/sell",
		data : $('#sell-stock-form').serialize(),
		async : false,
		cache : false,
		type : "POST",
		success : function(response) {
			if (response.status == 'failed') {
				$('#sell-stock-confirm-modal').closeModal();
				$('#error-modal .modal-content').text(response.errorMessage);
				$('#error-modal').openModal();
				return;
			} else {
				location.reload();
			}
		}
	});
}

function withdrawSubmit(id,balance){
	$('#withdraw-input').val(
			$('#withdraw-input').val().trim());
	var amount = $('#withdraw-input').val();
	var regex = /^\d+(?:\.\d{0,2})?$/;
	if (!amount.match(regex)) {
		$('#withdraw-input').addClass('invalid');
		$('#withdraw-label').attr('data-error',
		"Invalid money value");
		return;
	}
	var amountFloat = parseFloat(amount);
	var balanceFloat = parseFloat(balance);
	if (amountFloat > balanceFloat) {
		$('#withdraw-input').addClass('invalid');
		$('#withdraw-label').attr('data-error',
		"Withdraw amount exceeds balance");
		return;
	}
	amount = '-' + amount;
	withdrawMoney(amount, id);
	$('#withdraw-modal').closeModal();
}

function depositSubmit(id){
	$('#deposit-input').val(
			$('#deposit-input').val().trim());
	var amount = $('#deposit-input').val();
	var regex = /^\d+(?:\.\d{0,2})?$/;
	if (!amount.match(regex)) {
		$('#deposit-input').addClass('invalid');
		$('#deposit-label').attr('data-error',
		"Invalid money value");
		return;
	} else {
		depositMoney(amount, id);
		$('#deposit-modal').closeModal();
	}
}

function editPortfolioName(id, name){
	$.ajax({
		url: "/webapps7/portfolio/update?" + $.param({
			id: id,
			name: name
		}),
		cache: false,
		type:"POST",
		dataType: 'json',
		success: function(response){
			if(response.status == 'failed') {
				$('#edit-port-name-input').addClass('invalid');
				$('#edit-port-name-label').attr('data-error',response.errorMessage);
				return;
			} else {
				$('.portfolio-entry[data-id=' + id +'] .port-title').text(name);
				$('#edit-port-modal').closeModal();
			}
		}
	})
}

function previewStockInfo(){
	$.ajax({
		url : "/webapps7/stock/view?symbol=" + $('#viewer-search-input').val().trim(),
		type:"GET",
		cache: false,
		success: function(response) {
			$('#stock-preview-modal .modal-content').html(response);
			$('#stock-preview-modal').openModal();
		}
	})
}