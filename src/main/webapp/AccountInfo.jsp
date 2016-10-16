<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>User Index</title>
<meta name="viewport" content="width=device-width, initial-scale=1.0">
<script type="text/javascript" src="/js/jquery-3.0.0.min.js"></script>
<link href="/css/fonts/material-design-icons/material.css"
	rel="stylesheet" />
<script src="/js/materialize.min.js"></script>
<link rel="stylesheet" href="/css/materialize.min.css">
<link rel="stylesheet" href="/css/styles.css">
<script type="text/javascript" src="/js/script.js"></script>
<style>
#home-button {
	position: relative;
	top: 10px;
}
</style>
</head>
<body>
	<div class="navbar-fixed top-nav-bar">
		<nav class='blue darken-2'>
			<div class="nav-wrapper">
				<div class='top-bar-text'>
					<a href='/webapps7/index'><span class='page-title'>Portfolio
							Gorilla</span></a> <span class='page-description'> Dragging your
						money through economy hardship </span>
				</div>
				<ul class="right user-options-block">
					<li><a class="dropdown-button" href="#!"
						data-activates="user-dropdown"><i class="material-icons left"
							id='user-icon'>&#xE853;</i> <c:out
								value='${sessionScope.userName}' /><i
							class="material-icons right" id='dropdown-arrow'>arrow_drop_down</i></a></li>
				</ul>
				<ul id="user-dropdown" class="dropdown-content">
					<li><a href="/webapps7/account/view">Edit profile</a></li>
					<li class="divider"></li>
					<li><a href="/webapps7/logout">Logout</a></li>
				</ul>
			</div>
		</nav>
	</div>
	<div class="navbar-fixed lower-nav-bar">
		<nav class='white'>
			<div class='nav-bar-buttons right'>
				<a class="waves-effect btn-flat nav-bar-button" id='home-button'
					href='/webapps7/index'><i class=" material-icons left">home</i>
					<span class='nav-bar-button-label'>Home</span> </a>
			</div>
		</nav>
	</div>
	<div class='card account-card'>
		<div class='card-content'>
			<div class='row'>
				<div class='col s2 offset-s1 bold'>Username:</div>
				<div class='col s9'>
					<c:out value='${sessionScope.userName}' />
				</div>
			</div>
			<form id='acc-update-form' action='/webapps7/account/update'
				method='post'>
				<div class='row'>
					<div class="input-field col s10 offset-s1">
						<input id="email-input" maxlength='255' name="email" type="email"
							class="required-input validate" placeholder=""
							value='${user.email }'> <label id='email-label'
							for="email-input" data-error="Wrong email format">Email</label>
					</div>
				</div>
				<div class='row'>
					<div class="input-field col s10 offset-s1">
						<input id="pw-input" maxlength='255' name="password"
							type="password" class="required-input" placeholder=" "> <label
							id='pw-label' for="pw-input" data-error="">New password</label>
					</div>
				</div>
				<div class='row'>
					<div class="input-field col s10 offset-s1">
						<input id="pw-confirm-input" maxlength='255' name='passConfirm'
							type="password" class="required-input" placeholder=" "> <label
							id='pw-confirm-label' for="pw-confirm-input" data-error="">Confirm</label>
					</div>
				</div>
			</form>
			<div class='row'>
				<div class="col s10 offset-s1 center">
					<a class="waves-effect waves-light btn" id='update-button'>Update</a>
				</div>
			</div>
		</div>
	</div>

	<div id="update-modal" class="modal">
		<div class="modal-content">
			<div class="row">
				<div class="col s12">
					<h5>Update Profile</h5>
				</div>
			</div>
			<div class="row ">
				<div class="col s12">Are you sure you want to update your
					account information?</div>
			</div>
		</div>
		<div class="modal-footer">
			<a id='update-submit' data-id=""
				class="modal-action modal-close btn-flat">Update</a> <a
				class="modal-action modal-close btn-flat">Cancel</a>
		</div>
	</div>

	<script>
		$('#update-button').click(
				function() {
					if ($('#pw-confirm-input').val() == ''
							|| $('#pw-input').val() == '') {
						$('#pw-confirm-input').addClass('invalid');
						$('#pw-input').addClass('invalid');
						$('#pw-confirm-label').attr('data-error',
								'Password and confirmation cannot be empty.');
						return;
					}
					if ($('#pw-confirm-input').val() != $('#pw-input').val()) {
						$('#pw-confirm-input').addClass('invalid');
						$('#pw-confirm-label').attr('data-error',
								'Password and confirmation are not identical.')
						return;
					} else {
						$('#update-modal').openModal();
					}
				})

		$('#update-submit').click(function() {
			$('#acc-update-form').submit();
		})
	</script>
</body>
</html>