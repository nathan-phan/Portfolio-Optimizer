<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<div class='row'>
	<div class='col s12'>
		<span class="title entry-name"><c:out value='${stock.name}' />
			<span class='entry-symbol'>(<c:out value='${stock.symbol}' /></span>)</span>
	</div>
</div>
<div class='row'>
	<div class='col s4 stock-info-header'>Current price</div>
	<div class='stock-price col s8'>
		<c:if test='${stock.currency ne "USD"}'>
			<span class='bold'>${stock.currency}${stock.foreignPrice}</span> - 
                        </c:if>
		<span class='bold'><fmt:formatNumber value="${stock.price }"
				type="currency" /></span>
	</div>
</div>
<div class='row prev-closing-row'>
	<div class='col s4 stock-info-header'>Previous closing price</div>
	<div class='stock-closing-price col s8'>
		<c:if test='${stock.currency ne "USD"}'>
			<span class='bold'>${stock.currency}${stock.foreignPreviousClosingPrice}</span> - 
                        </c:if>
		<span class='bold'><fmt:formatNumber
				value="${stock.previousClosingPrice}" type="currency" /></span>
	</div>
</div>
<div class='row'>
	<div class='col s4 stock-info-header'>Price change</div>
	<div
		class='stock-price-change col s8 ${stock.change lt 0? "
                      red-text bold":"green-text bold" }'>
		<c:if test='${stock.currency ne "USD"}'>${stock.foreignChange}${stock.currency} &nbsp;&nbsp; </c:if>${stock.change}USD</div>
</div>
<div class='row'>
	<div class='col s4 stock-info-header'>Percent change</div>
	<div
		class='stock-change-percent col s8 ${stock.change lt 0? "
                      red-text bold":"green-text bold" }'>${stock.changePercent}%</div>
</div>

