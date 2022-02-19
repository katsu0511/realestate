<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" isELIgnored="false" %>
<jsp:include page="../common/header.jsp" flush="true"/>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<main>
	<div class="container">
		<h2>ログイン</h2>
	
		<form action="${pageContext.request.contextPath}/login" method="POST">
		
			<div class="login-item">
				<label for="userName" class="login-label">
					ユーザー名:
				</label>
				<div class="login-input">
					<input type="text" id="userName" class="login-info" name="userName" autocomplete="off">
				</div>
			</div>
				
			<div class="login-item">
				<label for="password" class="login-label">
					パスワード:
				</label>
				<div class="login-input">
					<input type="password" id="password" class="login-info" name="password" autocomplete="off">
				</div>
			</div>
			
			<c:if test="${errorMessage != null && errorMessage != ''}">
				<div class="error-message">
					${errorMessage}
				</div>
			</c:if>
				
			<div class="login-item">
				<div class="login-input">
					<input type="submit" class="button" name="loginBtn" value="ログイン">
				</div>
			</div>
		
		</form>
		
		<div class="add-button">
			<a href="${pageContext.request.contextPath}/register/company">新規自社登録はこちら</a>
		</div>
	</div>
</main>

<jsp:include page="../common/footer.jsp" flush="true"/>
