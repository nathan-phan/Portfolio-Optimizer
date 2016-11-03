<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>

<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>Portfolio Index</title>
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
				<form id='stock-search-form'>
					<div class="input-field">
						<input id="viewer-search-input" name='symbol'
							class="blue darken-1 lighten-1" type="search"
							placeholder="Search stock" required> <label for="search"><i
							class="material-icons prefix">search</i></label>
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
					<li><a href="/webapps7/account/view">Edit profile</a></li>
					<li class="divider"></li>
					<li><a href="/webapps7/logout">Logout</a></li>
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
						href="#history-content">History</a></li>
				</ul>
			</div>
			<div class='nav-bar-buttons right'>
				<a class="waves-effect btn-flat nav-bar-button" id='home-button'
					href='/webapps7/index'><i class=" material-icons left">home</i>
					<span class='nav-bar-button-label'>Home</span> </a> <a
					class="waves-effect btn-flat nav-bar-button modal-trigger"
					id='deposit-button' href="#deposit-modal"><i
					class=" material-icons left">add</i> <span
					class='nav-bar-button-label'>Deposit</span> </a> <a
					class="waves-effect btn-flat  nav-bar-button modal-trigger"
					href='#withdraw-modal' id='withdraw-button'><i
					class=" material-icons left">remove</i> <span
					class='nav-bar-button-label'>Withdraw</span> </a> <a
					class="waves-effect btn-flat nav-bar-button" href='/webapps7/portfolio/report?id=${portfolio.id}' id='report-button'><i
					class=" material-icons left">assessment</i> <span
					class='nav-bar-button-label'>Report</span> </a> <a
					class="waves-effect btn-flat  nav-bar-button" id='optimizer-button'><i
					class=" material-icons left">assignment</i> <span
					class='nav-bar-button-label'>Optimizer</span> </a>
			</div>
		</nav>
	</div>
	<div id='current-stock-content'>
		<div id='left-container'>
			<c:choose>
				<c:when test="${size eq 0}">
					<div class='card empty-portfolio-card'>
						<div class='card-content'>Looks like this portfolio is
							empty.</div>
					</div>
				</c:when>
				<c:otherwise>
					<ul class="collapsible popout stock-collection"
						data-collapsible="accordion">
						<c:forEach items="${portfolio.stocks}" var="entry">
							<li>
								<div class="collapsible-header valign-wrapper">
									<span class="title entry-name"><c:out
											value='${entry.key.name}' /> <span class='entry-symbol'>(<c:out
												value='${entry.key.symbol}' /></span>)</span> - <span class='red'><c:out
											value='${entry.key.exchange}' /></span> -
									<c:if test='${entry.key.currency ne "USD"}'>
										<span class='bold'>${entry.key.currency}${entry.key.foreignPrice}</span> - 
												</c:if>
									<span class='bold'><fmt:formatNumber
											value="${entry.key.price}" type="currency" /></span>
									<div class="secondary-content">${entry.value}</div>
								</div>
								<div class="collapsible-body">
									<div class='row'>
										<div class='col s4 stock-info-header'>Current price</div>
										<div class='stock-price col s8'>
											<c:if test='${entry.key.currency ne "USD"}'>
												<span class='bold'>${entry.key.currency}${entry.key.foreignPrice}</span> - 
                        </c:if>
											<span class='bold'><fmt:formatNumber
													value="${entry.key.price }" type="currency" /></span> <a
												href='#sell-stock-modal' data-shares="${entry.value}"
												data-symbol='<c:out value='${entry.key.symbol}'/>'
												class="waves-effect btn blue modal-trigger right sell-button">
												<span>Sell stock</span>
											</a>
										</div>
									</div>
									<div class='row prev-closing-row'>
										<div class='col s4 stock-info-header'>Previous closing
											price</div>
										<div class='stock-closing-price col s8'>
											<c:if test='${entry.key.currency ne "USD"}'>
												<span class='bold'>${entry.key.currency}${entry.key.foreignPreviousClosingPrice}</span> - 
                        </c:if>
											<span class='bold'><fmt:formatNumber
													value="${entry.key.previousClosingPrice}" type="currency" /></span>
										</div>
									</div>
									<div class='row'>
										<div class='col s4 stock-info-header'>Price change</div>
										<div
											class='stock-price-change col s8 ${entry.key.change lt 0? "
											red-text bold":"green-text bold" }'>
											<c:if test='${entry.key.currency ne "USD"}'>${entry.key.foreignChange}${entry.key.currency} &nbsp;&nbsp; </c:if>${entry.key.change}USD</div>
									</div>
									<div class='row'>
										<div class='col s4 stock-info-header'>Percent change</div>
										<div
											class='stock-change-percent col s8 ${entry.key.change lt 0? "
											red-text bold":"green-text bold" }'>${entry.key.changePercent}%</div>
									</div>

									<div class='row'>
										<div class='col s4 stock-info-header'>Total current
											market value</div>
										<div class='col s8 bold'>
											<fmt:formatNumber value="${entry.key.price * entry.value }"
												type="currency" />
										</div>
									</div>
								</div>
							</li>
						</c:forEach>
					</ul>

				</c:otherwise>
			</c:choose>
		</div>
		<div id='right-container'>
			<div class='card portfolio-overview-card'>
				<div class='card-content'>
					<div class="row">
						<div class="col s12">
							<h5 class='card-header'>Portfolio Overview</h5>
						</div>
						<div class="col s6">
							<span class='bold'>Balance </span>: &nbsp;
							<fmt:formatNumber value="${portfolio.balance}" type="currency" />
						</div>
						<div class="col s6">
							<a id='add-stock-button' href='#add-stock-modal'
								class="waves-effect btn red modal-trigger right"> <span>Add
									stock</span></a>
						</div>
						<div class="col s12">
							<span class='bold'>Oversea Stock Value: </span> &nbsp;
							<fmt:formatNumber value="${overseaValue}" type="currency" />
						</div>
						<div class="col s12">
							<span class='bold'>Domestic Stocks Value: </span> &nbsp;
							<fmt:formatNumber value="${domesticValue}" type="currency" />
						</div>
						<div class="col s12">
							<span class='bold'>Total Stocks Value: </span> &nbsp;
							<fmt:formatNumber value="${totalValue}" type="currency" />
						</div>
						<div class="col s12 violation-block display-none">
							<span class='bold'>Violation</span>:
							<ul class='bold'>
								<li id='violation-1' class='display-none red-text'>Minimum
									7 stocks, maximum 10. (${size} stocks available in portfolio)</li>
								<li id='violation-2' class='display-none red-text'>70% of
									the portfolio value should be in domestic stocks and 30% in
									overseas stocks. (Domestic value is <fmt:formatNumber
										type="number" maxFractionDigits="2" minFractionDigits="2"
										value="${domesticPercent}" />%)
								</li>
								<li id='violation-3' class='display-none red-text'>No more
									than 10% cash in the portfolio at any time (Currently portfolio
									has <fmt:formatNumber type="number" maxFractionDigits="2"
										minFractionDigits="2" value="${cashPercent}" />% in cash)
								</li>
							</ul>
						</div>
					</div>
				</div>
			</div>
			<div
				class='card guidelines-card <c:if test="${size ne 0}">display-none</c:if>'>
				<div class='card-content'>
					<div class="row">
						<div class="col s12">
							<h5 class='card-header'>Portfolio Guidelines</h5>
						</div>
						<div class="col s12">
							<ul>
								<li>Minimum 7 stocks, maximum 10.</li>
								<li>70% of the portfolio value should be in domestic stocks
									and 30% in overseas stocks.</li>
								<li>No more than 10% cash in the portfolio at any time</li>
							</ul>
						</div>
					</div>
				</div>
			</div>
			<div class='card allowed-stock-card'>
				<div class='card-content'>
					<div class="row">
						<div class="col s12">
							<h5 class='card-header'>Allowed Stocks</h5>
						</div>
						<div class="col s12 allowed-stocks-header-row">
							<span class='allowed-stocks-header'>Domestic Stocks</span>
						</div>
						<div class="col s12">
							<c:set var="domestics"
								value="MMM,AXP,AAPL,BA,CAT,CVX,CSCO,KO,DIS,DD,XOM,GE,GS,HD,IBM,INTC,JNJ,JPM,MCD,MRK,MSFT,NKE,PFE,PG,TRV,UTX,UNH,VZ,V,WMT" />
							<c:forTokens var="stock" items="${domestics}" delims=",">
								<div class='col s2 allowed-stock-entry'>
									<c:out value='${stock}' />
								</div>
							</c:forTokens>
						</div>
						<div class="col s12 allowed-stocks-header-row">
							<span class='allowed-stocks-header'>Oversea Stocks</span>
						</div>
						<div class="col s12">
							<c:set var="overseas" value="${overseas}" />
							<c:forTokens var="stock" items="${overseas}" delims=",">
								<div class='col s3 allowed-stock-entry'>
									<c:out value='${stock}' />
								</div>
							</c:forTokens>
						</div>
					</div>
				</div>
			</div>
		</div>
	</div>
	<div id='history-content'>
		<div class='card history-card'>
			<div class='card-content'>
				<c:choose>
					<c:when test="${empty records}"> There is no transaction history for this portfolio.
	     </c:when>
					<c:otherwise>
						<table class='history-table'>
							<thead>
								<tr>
									<th>Type</th>
									<th>Amount</th>
									<th class='center'>Stock</th>
									<th class='center'>Shares</th>
									<th>Price</th>
									<th>Balance</th>
									<th>Time</th>
								</tr>
							</thead>
							<tbody>
								<c:forEach items="${records}" var="record">
									<tr>
										<td>${record.type}</td>
										<td><fmt:formatNumber value="${record.amount}"
												type="currency" /></td>
										<td class='center'><c:out value='${record.symbol}' /></td>
										<td class='center'>${record.shares eq 0? '' : record.shares }</td>
										<td><fmt:formatNumber value="${record.price}"
												type="currency" /></td>
										<td><fmt:formatNumber value="${record.balance}"
												type="currency" /></td>
										<td><c:out value='${record.time }' /></td>
									</tr>
								</c:forEach>
							</tbody>
							<c:forEach items="${records}" var="record">

							</c:forEach>
						</table>
					</c:otherwise>
				</c:choose>
			</div>
		</div>
	</div>

	<div id="deposit-modal" class="modal">
		<div class="modal-content">
			<div class="row">
				<div class="col s12">
					<h5>Deposit Money</h5>
				</div>
			</div>
			<div class="row ">
				<div class="col s12">
					<form id='deposit-form' onSubmit="return false;">
						<div class="row">
							<div class="col s12">Please enter the money amount you wish
								to deposit</div>
							<br> <br>
							<div class="input-field col s12">
								<input id="deposit-input" name="amount" type="text"
									class="required-input" placeholder="Enter an amount"> <label
									id='deposit-label' for="deposit-input" data-error="">Amount</label>
							</div>
						</div>
					</form>
				</div>
			</div>
			<div class="row deposit-warning-row display-none red-text">
				<div class="col s12 ">Warning: Adding money will cause the
					portfolio to have more than 10% cash.</div>
			</div>
		</div>
		<div class="modal-footer">
			<a id='deposit-submit' class="modal-action btn-flat">Deposit</a> <a
				class="modal-action modal-close btn-flat">Cancel</a>
		</div>
	</div>

	<div id="withdraw-modal" class="modal">
		<div class="modal-content">
			<div class="row">
				<div class="col s12">
					<h5>Withdraw Money</h5>
				</div>
			</div>
			<div class="row ">
				<div class="col s12">
					<form id='deposit-form' onSubmit="return false;">
						<div class="row">
							<div class="col s12">Please enter the money amount you wish
								to withdraw</div>
							<br> <br>
							<div class="input-field col s12">
								<input id="withdraw-input" name="amount" type="text"
									class="required-input" placeholder="Enter an amount"> <label
									id='withdraw-label' for="deposit-input" data-error="">Amount</label>
							</div>
						</div>
					</form>
				</div>
			</div>
		</div>
		<div class="modal-footer">
			<a id='withdraw-submit' class="modal-action btn-flat">Withdraw</a> <a
				class="modal-action modal-close btn-flat">Cancel</a>
		</div>
	</div>

	<div id="add-stock-modal" class="modal">
		<div class="modal-content">
			<div class="row">
				<div class="col s12">
					<h5>Add Stocks</h5>
				</div>
			</div>
			<div class="row ">
				<div class="col s12">
					<form id='add-stock-form' onSubmit="return false;">
						<div class="row">
							<div class="col s12">Please enter the stock symbol and
								shares you wish to purchase</div>
							<br> <br>
							<div class="input-field col s6">
								<input id="buy-symbol-input" name="symbol" type="text"
									class="required-input" placeholder="Enter a symbol"> <label
									id='buy-symbol-label' for="buy-symbol-input" data-error="">Symbol</label>
							</div>
							<div class="input-field col s6">
								<input id="buy-share-input" name="shares" type="text"
									class="required-input" placeholder="Enter a number"> <label
									id='buy-share-label' for="buy-share-input" data-error="">Shares</label>
							</div>
						</div>
						<input type='hidden' name='id' value='${param.id }'>
					</form>
				</div>
			</div>
		</div>
		<div class="modal-footer">
			<a id='add-stock-submit' class="modal-action btn-flat">Add</a> <a
				class="modal-action modal-close btn-flat">Cancel</a>
		</div>
	</div>

	<div id="sell-stock-modal" class="modal">
		<div class="modal-content">
			<div class="row">
				<div class="col s12">
					<h5>Sell Stocks</h5>
				</div>
			</div>
			<div class="row ">
				<div class="col s12">
					<form id='sell-stock-form' onSubmit="return false;">
						<div class="row">
							<div class="col s12">Please enter the shares number you
								wish to sell</div>
							<br> <br>
							<div class="input-field col s12">
								<input id="sell-share-input" name="shares" type="text"
									class="required-input" placeholder="Enter a number"> <label
									id='sell-share-label' for="sell-share-input" data-error="">Shares</label>
							</div>
						</div>
						<input type='hidden' name='id' value='${param.id}'> <input
							type='hidden' name='available' id="available-share" value=''>
						<input type='hidden' name='symbol' id="sell-symbol-input" value=''>
					</form>
				</div>
			</div>
		</div>
		<div class="modal-footer">
			<a id='sell-stock-submit' class="modal-action btn-flat">Sell</a> <a
				class="modal-action modal-close btn-flat">Cancel</a>
		</div>
	</div>

	<div id="buy-stock-confirm-modal" class="modal">
		<div class="modal-content">
			<div class="row">
				<div class="col s12">
					<h5>Buy Stock Confirmation</h5>
				</div>
			</div>
			<div class="row ">
				<div class="col s12">
					<div class="row">
						<div class="col s12">
							Are you willing to buy <span id='confirm-buy-shares'></span>
							shares of <span id='confirm-buy-symbol'></span> for $<span
								id='confirm-buy-cost'></span>?
						</div>
					</div>
				</div>
			</div>
			<div class="row buy-warning-row display-none red-text">
				<div class="col s12 "></div>
			</div>
		</div>
		<div class="modal-footer">
			<a id='buy-stock-confirm-submit'
				class="modal-action modal-close btn-flat">Confirm</a> <a
				class="modal-action modal-close btn-flat">Cancel</a>
		</div>
	</div>

	<div id="sell-stock-confirm-modal" class="modal">
		<div class="modal-content">
			<div class="row">
				<div class="col s12">
					<h5>Sell Stock Confirmation</h5>
				</div>
			</div>
			<div class="row ">
				<div class="col s12">
					<div class="row">
						<div class="col s12">
							Are you willing to sell <span id='confirm-sell-shares'></span>
							shares of <span id='confirm-sell-symbol'></span> for $<span
								id='confirm-sell-cost'></span>?
						</div>
					</div>
				</div>
			</div>
			<div class="row buy-warning-row display-none red-text">
				<div class="col s12 "></div>
			</div>
		</div>
		<div class="modal-footer">
			<a id='sell-stock-confirm-submit'
				class="modal-action modal-close btn-flat">Confirm</a> <a
				class="modal-action modal-close btn-flat">Cancel</a>
		</div>
	</div>

	<div id="stock-preview-modal" class="modal">
		<div class="modal-content"></div>
		<div class="modal-footer">
			<a class="modal-action modal-close btn-flat">Close</a>
		</div>
	</div>
	
	<div id="error-modal" class="modal">
		<div class="modal-content"></div>
		<div class="modal-footer">
			<a class="modal-action modal-close btn-flat">Close</a>
		</div>
	</div>

	<script>
		$(function() {
			$('.modal-trigger').leanModal({
				dismissible : false
			});

			if (Number('${size}') < 7 || Number('${size}') > 10) {
				$('.violation-block').removeClass('display-none');
				$('#violation-1').removeClass('display-none');
			}

			if (Number('${cashPercent}') > 10) {
				$('.violation-block').removeClass('display-none');
				$('#violation-3').removeClass('display-none');
			}

			if ('${size}' != '0' && Number('${domesticPercent}') != 70) {
				$('.violation-block').removeClass('display-none');
				$('#violation-2').removeClass('display-none');
			}

			$('.sell-button').click(function() {
				$('#sell-symbol-input').val($(this).attr('data-symbol'));
				$('#available-share').val($(this).attr('data-shares'));
			})

			$('#deposit-submit').click(function() {
				var id = '${param.id}';
				depositSubmit(id)
			});

			$('#withdraw-submit').click(function() {
				var id = '${param.id}';
				withdrawSubmit(id, '${portfolio.balance}');
			});

			$('#add-stock-submit').click(function() {
				$('#confirm-buy-symbol').text($('#buy-symbol-input').val());
				$('#confirm-buy-shares').text($('#buy-share-input').val());
				buyStock('${usdinr}', '${usdsgd}');
			});

			$('#buy-stock-confirm-submit').click(function() {
				confirmBuyStock();
			})
			
			$('#sell-stock-confirm-submit').click(function() {
		        confirmSellStock();
		      })

			$('#sell-stock-submit').click(function() {
				$('#confirm-sell-symbol').text($('#sell-symbol-input').val());
				$('#confirm-sell-shares').text($('#sell-share-input').val());
				sellStock('${usdinr}', '${usdsgd}');
			})
			
			$('#stock-search-form').submit(function(e){
				e.preventDefault();
				previewStockInfo();
			});
		});
	</script>
</body>
</html>