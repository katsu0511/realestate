<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<jsp:include page="../common/header.jsp" flush="true"/>

<main>
	<div class="container">
		
		<div class="top">
			<a href="${pageContext.request.contextPath}/export/rental_statement" class="management">賃貸精算書</a>
			<a href="${pageContext.request.contextPath}/export/cancellation_statement" class="management">解約精算書</a>
			<a href="${pageContext.request.contextPath}/export/landlord_statement" class="management">大家精算書</a>
		</div>
		<div class="show_buttons">
			<a href="${pageContext.request.contextPath}/top" class="button">戻る</a>
		</div>
		
	</div>
</main>

<jsp:include page="../common/footer.jsp" flush="true"/>
