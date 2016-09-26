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
</head>
<body>
	<div class="navbar-fixed top-nav-bar">
		<nav class='blue darken-2'>
			<div class="nav-wrapper">
				<div class='top-bar-text'>
					<span class='page-title'>Portfolio Optimizer</span> <span
						class='page-description'> A CS490 final project </span>
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
							id='user-icon'>&#xE853;</i> <c:out value='${user.userName}' /><i
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
			<div class='tabs-container manual-index-tabs'>
				<ul class="tabs">
					<li class="tab col s4" id='current-stock-tab'><a
						href="#current-stock-content">Current Stocks</a></li>
					<li class="tab  col s4" id='history-tab'><a
						href="#history-content">Purchase history</a></li>
				</ul>
			</div>
			<div class='nav-bar-buttons right'>
				<a class="waves-effect btn-flat  nav-bar-button" id='glossary-icon'><i
					class=" material-icons left">add</i> <span
					class='nav-bar-button-label'>Buy</span> </a> <a
					class="waves-effect btn-flat  nav-bar-button" id='glossary-icon'><i
					class=" material-icons left">remove</i> <span
					class='nav-bar-button-label'>Sell</span> </a> <a
					class="waves-effect btn-flat  nav-bar-button" id='glossary-icon'><i
					class=" material-icons left">file_download</i> <span
					class='nav-bar-button-label'>Export</span> </a> <a
					class="waves-effect btn-flat  nav-bar-button" id='glossary-icon'><i
					class=" material-icons left">assignment</i> <span
					class='nav-bar-button-label'>Optimizer</span> </a>
			</div>
		</nav>
	</div>
	<div id='current-stock-content'>
		<div class='card user-stocks-card'>
			<div class='card-content'>
				<c:choose>
					<c:when test="${empty user.userStocks }">
			     User has no stock right now.
			   </c:when>
					<c:otherwise>
						<ul class="stock-collection collection">
							<c:forEach items="${user.userStocks}" var='entry'
								varStatus="loop">
								<li class="collection-item" data-symbol='${entry.key.symbol}'><span
									class="title entry-name"><c:out
											value='${entry.key.name}' /> <span class='entry-symbol'>(<c:out
												value='${entry.key.symbol}' />)
									</span></span> <br> <c:out value='${entry.key.quote.price}' />
									<div class="secondary-content">${entry.value}</div></li>
							</c:forEach>
						</ul>
					</c:otherwise>
				</c:choose>
				<div class='row balance-row'>
					<div class='balance-label col s12'>
						Current balance: &nbsp; <span id='balance'><fmt:formatNumber
								value="${user.balance}" type="currency" /></span>
					</div>
				</div>
			</div>
		</div>
		<c:if test='${not empty user.userStocks }'>
			<div class='display-none card stock-preview-card'>
				<div class='card-content'></div>
			</div>
		</c:if>
	</div>
	<script>
		$(function() {
			$('.stock-collection .collection-item').click(
					function() {
						$('.stock-preview-card').removeClass('display-none');
						var symbol = $(this).attr('data-symbol');
						$.ajax({
							url : '/webapps7/stock?symbol=' + symbol,
							type : 'GET',
							dataType : 'text/html',
							async : false,
							cache : false,
							complete : function(data) {
								$('.stock-preview-card .card-content').html(
										data.responseText);
							}
						})
					})
		})
	</script>
</body>
</html>