function checkUserNameRequirements(name) {
	if (name.trim() == "") {
		return "This field cannot be empty";
	}
	var regExp = /^[A-Za-z0-9\-\_\.]+$/;
	if (!name.match(regExp)) {
		return "Allowed characters: [A-Z] [0-9] [-] [_] [.]";
	}
	return "ok";
}

setTimeout(function() {
	$('#username-input').focus(), 10
});

$('#pwd-forgot').click(function() {
	$('#login-form').slideUp();
	$('#forgot-form').show();
	setTimeout(function() {
		$('#username-forgot').focus(), 10
	});
})

$('#back-button-forgot').click(function() {
	$('#forgot-form').slideUp();
	$('#login-form').slideDown();
	setTimeout(function() {
		$('#username-input').focus(), 10
	});
})

$('#back-button-request').click(function() {
	$('.login-card').animate({
		top : '150px'
	})
	$('#register-form')[0].reset();
	$('#register-form').slideUp();
	$('#login-form').slideDown();
	setTimeout(function() {
		$('#username-input').focus(), 10
	});
})

$('#register').click(function() {
	$('.login-card').animate({
		top : '100px'
	})
	$('#login-form').slideUp();
	$('#register-form').show();
	setTimeout(function() {
		$('#username-reg').focus(), 10
	});
})


function displayToast(content, duration) {
	$('.custom-toast .toast-text').text(content);
	$('.custom-toast').animate({
		top : '50px'
	}).delay(duration).animate({
		top : '-500px'
	})
}


$('#register-form').submit(
		function(e) {
			e.preventDefault();
			$('#username-reg').val($('#username-reg').val().trim());
			$('#email-reg').val($('#email-reg').val().trim());
			var idCheck = checkUserNameRequirements($('#username-reg')
					.val());
			if (idCheck != "ok") {
				$('#username-reg').addClass('invalid');
				$('#username-reg-label').attr('data-error', idCheck);
				return;
			}
			if ($('#password-reg').val() != $('#confirm-pass').val()) {
				$('#confirm-pass').addClass('invalid');
				$('#confirm-pwd-label')
				.attr('data-error',
				"Password and password confirmation are not identical");
				return;
			}

			$
			.ajax({
				url : "/webapps7/register",
				type : "POST",
				data : $('#register-form').serialize(),
				async : false,
				cache : false,
				dataType : 'json',
				success : function(data) {
					if (data.status == 'failed') {
						if (data.errorMessage == "Username already exists in database") {
							$('#username-reg').addClass(
							'invalid');
							$('#username-reg-label').attr(
									'data-error',
									data.errorMessage);
							return;
						}
						$('#register-form input').addClass(
						'invalid');
						displayToast(
								data.errorMessage,
								5000)
					} else {
						$('#back-button-request').click();
						displayToast(
								data.successMessage,
								5000)
								return;
					}

				}
			})

		});

$('#login-form')
.submit(
		function(e) {
			e.preventDefault();
			$('#username-input').val($('#username-input').val().trim());
			if ($('#username-input').val() == '') {
				$('#username-label-login').attr('data-error',
				'Username cannot be empty');
				$('#username-input').addClass('invalid');
			}
			$
			.ajax({
				url : '/webapps7/login',
				type : 'POST',
				data : $('#login-form').serialize(),
				async : false,
				cache : false,
				dataType : 'json',
				success : function(data) {
					if (data.status == 'success') {
						alert(data.successMessage)
					} else {
						$('#username-input').addClass('invalid');
						$('#username-label-login').attr(
								'data-error',
								data.errorMessage);
					}
					return;
				}
			});
		});

$('#forgot-form')
.submit(
		function(e) {
			e.preventDefault();
			$('#username-forgot').val(
					$('#username-forgot').val().trim());
			if ($('#username-forgot').val() == "") {
				$('#forgot-pass-label').attr('data-error',
						'Username cannot be empty');
				$('#username-forgot').addClass('invalid');
				setTimeout(function() {
					$('#username-forgot').focus(), 10
				});
				return;
			}
			$('#email-forgot').val(
					$('#email-forgot').val().trim());
			if ($('#email-forgot').val() == "") {
				$('#forgot-email-label').attr('data-error',
						'Email cannot be empty');
				$('#email-forgot').addClass('invalid');
				setTimeout(function() {
					$('#email-forgot').focus(), 10
				});
				return;
			}
			$
					.ajax({
						url : "/webapps7/forgotpass",
						type : "POST",
						data : $('#forgot-form').serialize(),
						async : false,
						cache : false,
						dataType : 'json',
						success : function(data) {
							if (data.status == 'failed') {
								$('#forgot-pass-label')
										.attr('data-error',
												data.errorMessage);
								$('#username-forgot').addClass(
										'invalid');
								return;
							} else {
								$('#back-button-forgot').click();
								displayToast(
										"Your password has been reset per request and an email with a temporary new password was sent to your recorded email account.",
										5000)
							}

						}
					})

		});