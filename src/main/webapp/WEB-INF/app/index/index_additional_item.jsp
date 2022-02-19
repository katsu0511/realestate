<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" import="java.io.*,java.util.*,java.text.*" %>
<jsp:include page="../common/header.jsp" flush="true"/>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<main>
	<div class="container">
		
		<c:choose>
			<c:when test="${items.isEmpty()}">
				<div class="no-display">登録されている追加項目がありません。</div>
				<div class="add-button">
					<a href="${pageContext.request.contextPath}/register/additional_item" class="button">新規追加</a>
				</div>
			</c:when>
			<c:otherwise>
				<div class="display-title">
					<h2>追加項目一覧</h2>
				</div>
				<div class="add-button">
					<a href="${pageContext.request.contextPath}/register/additional_item" class="button">新規追加</a>
				</div>
				<div class="display">
					<div class="display-thead">
						<div class="display-th" style="width:30%;">追加項目ID</div>
						<div class="display-th" style="width:35%;">追加項目表示名</div>
						<div class="display-th" style="width:35%;">追加項目ID名</div>
					</div>
				<c:forEach var="item" items="${items}">
					<div class="display-tbody visible">
						<div class="display-td" style="width:30%;">
							<a href="${pageContext.request.contextPath}/show/additional_item?id=${item.aditid}" class="display-con">
								${item.aditid}
							</a>
						</div>
						<div class="display-td" style="width:35%;">
							${item.itemnm}
						</div>
						<div class="display-td" style="width:35%;">
							${item.itemid}
						</div>
					</div>
		    	</c:forEach>
				</div>
			</c:otherwise>
		</c:choose>
		
		<div class="show_buttons">
			<a href="${pageContext.request.contextPath}/top" class="button">戻る</a>
		</div>
		
	</div>
</main>
	
<jsp:include page="../common/footer.jsp" flush="true"/>
