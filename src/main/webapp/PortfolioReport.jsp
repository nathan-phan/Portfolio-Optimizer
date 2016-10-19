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
				<a class="waves-effect btn-flat nav-bar-button" id='home-button'
					href='/webapps7/index'><i class=" material-icons left">home</i>
					<span class='nav-bar-button-label'>Home</span> </a> <a
					class="waves-effect btn-flat  nav-bar-button" href='/webapps7/portfolio/report/download?id=${param.id}' id='download-button'><i
					class=" material-icons left">get_app</i> <span
					class='nav-bar-button-label'>Download CSV</span> </a>
			</div>
		</nav>
	</div>
	<div class='report-container'>
		<div class='row'>
			<div class='col s12 report-port-name center'>
				<c:out value='${report.name}' />
			</div>
		</div>
		<div class='card portfolio-report-card'>
			<div class='card-content'>
				<div class='row'>
					<div class='col s2 bold'>Balance:</div>
					<div class='col s10'>
						<fmt:formatNumber value="${report.balance}" type="currency" />
					</div>
				</div>
				<div class='row'>
					<div class='col s2 bold'>Oversea Stock Value:</div>
					<div class='col s10'>
						<fmt:formatNumber value="${report.overseaValue}" type="currency" />
					</div>
				</div>
				<div class='row'>
					<div class='col s2 bold'>Domestic Stock Value:</div>
					<div class='col s10'>
						<fmt:formatNumber value="${report.domesticValue}" type="currency" />
					</div>
				</div>
				<div class='row'>
					<div class='col s2 bold'>Total Stock Value:</div>
					<div class='col s10'>
						<fmt:formatNumber value="${report.totalStockValue}"
							type="currency" />
					</div>
				</div>
				<div class='row'>
					<div class='col s2 bold'>Oversea Percent:</div>
					<div class='col s10'>
						<fmt:formatNumber type="number" maxFractionDigits="2"
							minFractionDigits="2" value="${report.overseaPercent}" />
						%
					</div>
				</div>
				<div class='row'>
					<div class='col s2 bold'>Domestic Percent:</div>
					<div class='col s10'>
						<fmt:formatNumber type="number" maxFractionDigits="2"
							minFractionDigits="2" value="${report.domesticPercent}" />
						%
					</div>
				</div>
				<div class='row'>
					<div class='col s2 bold'>Cash Percent:</div>
					<div class='col s10'>
						<fmt:formatNumber type="number" maxFractionDigits="2"
							minFractionDigits="2" value="${report.cashPercent}" />
						%
					</div>
				</div>
				<div class='row'>
					<div class='col s12 bold'>&nbsp;</div>
				</div>
				<div class='row bold'>Transaction history:</div>
				<table id='port-money-table' class='bordered'>
					<thead>
						<tr>
							<td class='bold'>Transaction Type</td>
							<td class='bold'>Amount</td>
							<td class='bold'>Time</td>
							<td class='bold'>Balance</td>
						</tr>
					</thead>
					<tbody>
						<c:forEach items='${report.history}' var='entry'>
							<tr>
								<td>${entry.type}</td>
								<td><fmt:formatNumber value="${entry.amount}"
										type="currency" /></td>
								<td><c:out value='${entry.time }' /></td>
								<td><fmt:formatNumber value="${entry.balance}"
										type="currency" /></td>
							</tr>
						</c:forEach>
					</tbody>
				</table>
			</div>
		</div>

		<c:forEach items='${report.stockReports }' var='entry'>
			<div class='row'>
				<div class='col s12 report-stock-name'>
					<c:out value='${entry.name}' />
				</div>
			</div>
			<div class='card stock-report-card'>
				<div class='card-content'>
					<div class='row'>
						<div class='col s2 bold'>Symbol:</div>
						<div class='col s10'>${entry.symbol}</div>
					</div>
					<div class='row'>
						<div class='col s2 bold'>Shares:</div>
						<div class='col s10'>
						${entry.shares}
						</div>
					</div>
					<div class='row'>
						<div class='col s2 bold'>Price:</div>
						<div class='col s10'>
							<c:if test='${entry.currency ne "USD"}'>
								<span>${entry.currency}${entry.foreignPrice}</span> - 
                        </c:if>
							<span class='bold'><fmt:formatNumber
									value="${entry.price }" type="currency" /></span>
						</div>
					</div>
					<div class='row'>
						<div class='col s2 bold'>Total Value:</div>
						<div class='col s10'>
							<fmt:formatNumber value="${entry.value}" type="currency" />
						</div>
					</div>
					<div class='row'>
						<div class='col s2 bold'>Total Net Profit:</div>
						<div class='col s10'>
							<fmt:formatNumber value="${entry.profit}" type="currency" />
						</div>
					</div>
					<div class='row'>
						<div class='col s12 bold'>&nbsp;</div>
					</div>
					<div class='row bold'>Purchase history:</div>
					<table class='bordered'>
						<thead>
							<tr>
								<td class='bold col-25'>Date</td>
								<td class='bold col-25'>Price</td>
								<td class='bold col-25'>Shares</td>
								<td class='bold col-25'>&nbsp;</td>
							</tr>
						</thead>
						<tbody>
							<c:forEach items='${entry.buyHistory}' var='buyEntry'>
								<tr>
									<td class='col-25'>${buyEntry.date}</td>
									<td class='col-25'><fmt:formatNumber value="${buyEntry.price}"
											type="currency" /></td>
									<td class='col-25'><c:out value='${buyEntry.shares }' /></td>
									<td class='col-25'>&nbsp;</td>
								</tr>
							</c:forEach>
						</tbody>
					</table>
					<div class='row'>
						<div class='col s12 bold'>&nbsp;</div>
					</div>
					<c:if test="${not empty  entry.sellHistory}">
						<div class='row bold'>Sell history:</div>
						<table class='bordered'>
							<thead>
								<tr>
									<td class='bold col-25'>Date</td>
									<td class='bold col-25'>Price</td>
									<td class='bold col-25'>Shares</td>
									<td class='bold col-25'>Net Gain</td>
								</tr>
							</thead>
							<tbody>
								<c:forEach items='${entry.sellHistory}' var='sellEntry'>
									<tr>
										<td class='col-25'>${sellEntry.date}</td>
										<td class='col-25'><fmt:formatNumber value="${sellEntry.price}"
												type="currency" /></td>
										<td class='col-25'><c:out value='${sellEntry.shares }' /></td>
										<td class='col-25'><fmt:formatNumber value="${sellEntry.net}"
												type="currency" /></td>
									</tr>
								</c:forEach>
							</tbody>
						</table>
					</c:if>
				</div>
			</div>
		</c:forEach>
	</div>

</body>
</html>