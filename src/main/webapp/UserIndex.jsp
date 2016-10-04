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
</head>
<body>
	<div class="navbar-fixed top-nav-bar">
		<nav class='blue darken-2'>
			<div class="nav-wrapper">
				<div class='top-bar-text'>
					<span class='page-title'>Portfolio Gorilla</span> <span
						class='page-description'> Dragging your money through
						economy hardship </span>
				</div>
				<form>
					<div class="input-field">
						<input id="viewer-search-input" class="blue darken-1 lighten-1"
							type="search" placeholder="Search stock" required> <label
							for="search"><i class="material-icons prefix">search</i></label>
					</div>
				</form>
				<ul class="right user-options-block">
					<li><a class="dropdown-button" href="#!"
						data-activates="user-dropdown"><i class="material-icons left"
							id='user-icon'>&#xE853;</i> <c:out
								value='${sessionScope.userName}' /><i
							class="material-icons right" id='dropdown-arrow'>arrow_drop_down</i></a></li>
				</ul>
				<ul id="user-dropdown" class="dropdown-content">
					<li><a href="#!">Edit profile</a></li>
					<li class="divider"></li>
					<li><a href="#!">Logout</a></li>
				</ul>
			</div>
		</nav>
	</div>
	<div class="navbar-fixed lower-nav-bar">
		<nav class='white'>
			<div class='nav-bar-buttons right'>
				<a id='add-portfolio-button' href='#add-port-modal'
					class="waves-effect btn red nav-bar-button modal-trigger"> <span
					class='nav-bar-button-label'>New Portfolio</span>
				</a>
			</div>
		</nav>
	</div>
	<div id='current-portfolio-content'>
		<c:choose>
			<c:when test="${empty ports }">
				<div class='card user-portfolio-card'>
					<div class='card-content'>User has no portfolio right now.</div>
				</div>
			</c:when>
			<c:otherwise>
				<ul class="collection portfolio-collection z-depth-1">
					<c:forEach items="${ports}" var='port'>
						<li class="collection-item portfolio-entry hand">
							<div class='row'>
								<div class='col s9 port-title'>
									<c:out value="${port.name }" />
								</div>
								<div class='col s3'>
									<a href="#!" class="port-entry-icons"><i
										class="material-icons">delete</i></a> <a href="#!"
										class="port-entry-icons"><i class="material-icons">edit</i></a>
								</div>
							</div>
							<div class='row'>
								<div class='col s9 port-stock'>
									<span class='stock-label'>Stocks: </span>
									<c:choose>
										<c:when test='${empty port.stocks }'>
										None
										</c:when>
										<c:otherwise>
											<c:forEach items="${port.stocks}" var="stock">
												<c:out value='${stock.symbol}' />
											</c:forEach>
										</c:otherwise>
									</c:choose>
								</div>
								<div class='col s3 port-balance'>
									<span class='entry-balance'><fmt:formatNumber value="${port.balance}" type="currency"/></span><span class='balance-label'>Balance: &nbsp;</span> 
								</div>
							</div>
					</c:forEach>
				</ul>

			</c:otherwise>
		</c:choose>

	</div>

	<!-- BEGINNING OF ADD PORTFOLIO MODAL -->

	<div id="add-port-modal" class="modal">
		<div class="modal-content">
			<div class="row">
				<div class="col s12">
					<h5>Add New Portfolio</h5>
				</div>
			</div>
			<div class="row ">
				<div class="col s12">
					<form id='add-port-form'>
						<div class="row">
							<div class="input-field col s12">
								<input type="hidden" value="${sessionScope.userName}"
									name="userName"> <input id="add-port-name-input"
									length=255 maxlength=255 name="portfolioName" type="text"
									class="required-input" placeholder=""> <label
									id='add-port-name-label' for="add-port-name-input"
									data-error="">Portfolio Name</label>
							</div>
						</div>
					</form>
				</div>
			</div>
		</div>
		<div class="modal-footer">
			<a id='add-submit' class="modal-action btn-flat">Add</a> <a
				class="modal-action modal-close btn-flat">Cancel</a>
		</div>
	</div>

	<!-- END OF ADD PORTFOLIO MODAL -->
	<script>
		$('.modal-trigger').leanModal({
			dismissible : false
		})

		$('#add-submit').click(
				function() {
					$('#add-port-name-input').val(
							$('#add-port-name-input').val().trim());
					addNewPortfolio();
				})
	</script>
</body>
</html>