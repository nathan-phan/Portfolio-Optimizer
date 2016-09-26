<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<div class='row'>
	<div class='col s12 stock-symbol'>
		<c:out value='${stock.symbol}'></c:out>
	</div>
</div>
<div class='row'>
	<div class='col s12 stock-name'>
		<c:out value='${stock.name }'></c:out>
	</div>
</div>
<div class='row'>
	<div class='col s6 stock-info-header'>Current price</div>
	<div class='stock-price col s6'><fmt:formatNumber value="${stock.quote.price }" 
            type="currency"/></div>
</div>
<div class='row'>
	<div class='col s6 stock-info-header'>Previous closing price</div>
	<div class='stock-closing-price col s6'><fmt:formatNumber value="${stock.quote.previousClose}" 
            type="currency"/></div>
</div>
<div class='row'>
	<div class='col s6 stock-info-header'>Price change</div>
	<div class='stock-price-change col s6 ${stock.quote.change lt 0? 'red-text':'green-text' }'>${stock.quote.change}</div>
</div>
<div class='row'>
	<div class='col s6 stock-info-header'>Percent change</div>
	<div class='stock-change-percent col s6 ${stock.quote.change lt 0? 'red-text':'green-text' }'>${stock.quote.changeInPercent}%</div>

</div>
<div class='row'>
	<div class='col s6 stock-info-header'>Money spent</div>
	<div class='stock-cost col s6'>stock cost here</div>

</div>
<div class='row'>
	<div class='col s6 stock-info-header'>Net change</div>
	<div class='stock-money-made-cost col s6'>stock profit here</div>
</div>



