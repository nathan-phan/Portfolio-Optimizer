<html>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>Login</title>
<meta name="viewport" content="width=device-width, initial-scale=1.0">
<script type="text/javascript" src="/js/jquery-3.0.0.min.js"></script>
<link href="/css/fonts/material-design-icons/material.css"
	rel="stylesheet" />
<link href="https://fonts.googleapis.com/css?family=Grand+Hotel|Fugaz+One"
	rel="stylesheet">
<script src="/js/materialize.min.js"></script>
<link rel="stylesheet" href="/css/materialize.min.css">
<link rel="stylesheet" href="/css/styles.css">
</head>
<body class='blue login-body'>
	<div class='container'>
		<div class='row'>

			<!--  BEGINNING OF CENTER CARD  -->

			<div class='col s4 offset-s4 card login-card'>
				<div class='card-content'>

					<!--  BEGINNING OF LOGIN FORM - DEFAULT FORM INPUT -->
					<div class='login-app-title-row center'>Portfolio Gorilla</div>
					<div class='sub-title-row center'>a cs490 project</div>
					<form id='login-form' name="loginForm">
						<div class='input-rows'>
							<div class='row'>
								<div class="input-field col s10 offset-s1" id="username">
									<input type="text" name="username" id='username-input'
										placeholder='' required class="validate required-input">
									<label class='active' for="username-input"
										id='username-label-login' data-error="">Username</label>
								</div>
							</div>
							<div class='row'>
								<div class="input-field col s10 offset-s1" id="password">
									<input type="password" id='password-input' name="password"
										class="validate required-input" placeholder='' required>
									<label for="password" data-error="">Password</label>
								</div>
							</div>
						</div>
						<div class='row'>
							<button
								class=" col s10 offset-s1 waves-effect waves-light btn blue darken-1"
								id="login-button" type="submit">Log in</button>
						</div>
						<div class='row'>
							<a class="col s5 offset-s1 hand" id='pwd-forgot'>Forgot
								password?</a> <a class="col s5 offset-s1 hand" id='register'>Register
								an account</a>
						</div>
					</form>

					<!--  END OF LOGIN FORM -->
					<!--  BEGINNING OF PASSWORD RETRIVE FORM -->

					<form class="display-none" id='forgot-form' name="form1">
						<div class='input-rows'>
							<div class='row'>
								<a class='col s12 hand' id='back-button-forgot'><i
									class="material-icons left">&#xE314;</i>Back</a>
							</div>
							<div class='row'>
								<div class='col s10 offset-s1'>Please submit your username
									and email to have your password emailed to you.</div>
							</div>
							<div class='row'>
								<div class="input-field col s10 offset-s1">
									<input id="username-forgot" name="username" type="text"
										class="validate required-input" placeholder='' required>
									<label for="username-forgot" data-error=""
										id='forgot-pass-label'>Username</label>
								</div>
							</div>
							<div class='row'>
								<div class="input-field col s10 offset-s1">
									<input id="email-forgot" name="email" type="email"
										class="validate required-input" placeholder='' required>
									<label for="email-forgot" data-error=""
										id='forgot-email-label'>Email</label>
								</div>
							</div>
							<div class='row'>
								<button type='submit'
									class=" col s10 offset-s1 waves-effect waves-light btn blue darken-1"
									id="reset-password">Reset Password</button>
							</div>
						</div>
					</form>

					<!--  END OF PASSWORD RETRIEVE FORM  -->
					<!--  BEGINNING OF ACCOUNT REQUEST FORM  -->

					<form class="display-none" name="RegistForm" id='register-form'>
						<div class='input-rows'>
							<div class='row'>
								<a class='col s12 hand' id='back-button-request'><i
									class="material-icons left">&#xE314;</i>Back</a>
							</div>

							<div class='row'>
								<div class="input-field col s10 offset-s1">
									<input type="text" class="validate required-input"
										maxlength="25" id="username-reg" name="username" value=''
										placeholder='' required> <label for="username-reg"
										id='username-reg-label' class='active' data-error="">Username</label>
								</div>
							</div>
							<div class='row'>
								<div class="input-field col s10 offset-s1">
									<input type="email" id="email-reg" maxlength="255"
										class="validate required-input" name="email" required value=''
										placeholder='' required> <label for="email-reg"
										id='email-label'
										data-error="Please enter a proper email address">Email</label>
								</div>
							</div>
						</div>
						<div class='row'>
							<div class="input-field col s10 offset-s1">
								<input type="password" id="password-reg" maxlength="255"
									class="validate required-input" name="password" placeholder=''
									required> <label for="password-reg" id='pwd-reg-label'
									data-error="">Password</label>
							</div>
						</div>
						<div class='row'>
							<div class="input-field col s10 offset-s1">
								<input type="password" id="confirm-pass"
									class="validate required-input" name="password2" placeholder=''
									required> <label for="confirm-pass"
									id='confirm-pwd-label' data-error="">Confirm Password</label>
							</div>
						</div>
						<div class='row'>
							<button type='submit'
								class=" col s10 offset-s1 waves-effect waves-light btn blue darken-1"
								id="request-acc">Request Account</button>
						</div>
					</form>

					<!--  END OF ACCOUNT REQUEST FORM  -->

				</div>
			</div>

			<!--  END OF CENTER CARD -->

		</div>

		<!-- BEGINNING OF LOGIN TOAST -->

		<div class='custom-toast grey darken-4 white-text valign-wrapper'>
			<div class='toast-text'></div>
			<div class='toast-dismiss hand'>
				<i class="material-icons">&#xE14C;</i>
			</div>
		</div>


		<!-- END OF LOGIN TOAST -->

	</div>
	<script src="/js/login.js"></script>
	<script>
	$('.toast-dismiss').click(function() {
        $(this).parent().stop().animate({
          top : '-500px'
        });
      })
	</script>
</body>
</html>
