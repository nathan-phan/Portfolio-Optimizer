<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>

<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>Portfolio Report</title>
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
					<a href='/webapps7/index'><span class='page-title'>Portfolio
							Gorilla</span> </a> <span class='page-description'> Dragging your
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
				<a class="waves-effect btn-flat nav-bar-button" id='opt-back-button'
					href='/webapps7/portfolio/view?id=${param.id}'><i
					class="material-icons left">&#xE5CB;</i> <span
					class='nav-bar-button-label'>Back</span> </a>
			</div>
		</nav>
	</div>
	<div class='optimizer-container'>
		<div class='row'>
			<div class='col s12 center'>
				<h5>Portfolio Optimizer</h5>
			</div>
		</div>
		<div class='card portfolio-report-card'>
			<div class='card-content'>
				<c:choose>
					<c:when test='${empty stockArray}'>
					Portfolio has no stock.
					</c:when>
					<c:otherwise>
						<div class='row'>
							<div class='col s12 bold opt-header'>Weekly Expected
								Returns</div>
						</div>
						<div class='row'>
							<div class='col s3 bold'>&nbsp;</div>
							<div class='col s2 bold'>Weight</div>
							<div class='col s7 bold'>Stock Weekly Return</div>
						</div>
						<c:forEach items='${stockArray }' var='stock'>
							<div class='row ex-return-row'>
								<div class='col s3 bold'>
									<span class='blue white-text'><c:out
											value='${stock.symbol}' /></span>
									<c:out value='${stock.name}' />
								</div>
								<div class='col s2'>
									<fmt:formatNumber type="number" minFractionDigits="3"
										maxFractionDigits="3" value="${stock.weight}" />
								</div>
								<div class='col s7'>
									<span
										class=' <c:if test="${stock.expectedReturn lt 0}">red</c:if> '><fmt:formatNumber
											type="number" minFractionDigits="7" maxFractionDigits="7"
											value='${stock.expectedReturn}' /></span>
									<c:if test="${stock.expectedReturn lt 0}">
										<span class='bold'>(Bad stock to keep)</span>
									</c:if>
								</div>
							</div>
						</c:forEach>
						<div class='row'>
							<div class='col s3 bold'>Portfolio Overall</div>
							<div class='col s2'>1</div>
							<div class='col s7'>
								<fmt:formatNumber type="number" minFractionDigits="7"
									maxFractionDigits="7" value='${expectedReturn}' />
							</div>
						</div>
						<br>
						<div class='row'>
							<div class='col s12 bold opt-header'>Portfolio Covariances</div>
						</div>

						<div class='row bold'>
							<div class='col s1'>&nbsp;</div>
							<c:forEach items='${stockArray }' var='stock'>
								<div class='col s1'>${stock.symbol}</div>
							</c:forEach>
						</div>
						<c:forEach items='${stockArray }' var='stock' varStatus='loop'>
							<div class='row ex-return-row'>
								<div class='col s1 bold'>${stock.symbol}</div>
								<c:forEach items='${covMatrix[loop.index]}' var='covVal'
									varStatus='loop2'>
									<div class='col s1 bold'>
									<span class=''></span>
										<fmt:formatNumber type="number" minFractionDigits="7"
											maxFractionDigits="7" value="${covVal}" />
									</div>
								</c:forEach>
							</div>
						</c:forEach>
					</c:otherwise>
				</c:choose>
			</div>
		</div>
	</div>

</body>
</html>