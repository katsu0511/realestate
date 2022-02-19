<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<jsp:include page="../common/header.jsp" flush="true"/>

<main>
	<div class="container">
		
		<div class="top">
			<a href="${pageContext.request.contextPath}/show/company" class="management">自社情報管理</a>
			<a href="${pageContext.request.contextPath}/index/responsible_person" class="management">担当者一覧</a>
			<a href="${pageContext.request.contextPath}/index/landlord" class="management">大家一覧</a>
			<a href="${pageContext.request.contextPath}/index/residence" class="management">物件一覧</a>
			<a href="${pageContext.request.contextPath}/index/room" class="management">部屋一覧</a>
			<a href="${pageContext.request.contextPath}/index/tenant" class="management">借り主一覧</a>
			<a href="${pageContext.request.contextPath}/index/undecided_payment" class="management">入金消込</a>
			<a href="${pageContext.request.contextPath}/index/additional_item" class="management">追加項目一覧</a>
			<a href="${pageContext.request.contextPath}/index/pdf_export" class="management">各種PDF出力</a>
		</div>
		
	</div>
</main>

<jsp:include page="../common/footer.jsp" flush="true"/>
