<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html lang="ja">
<head>
	<meta http-equiv="Content-Type" content="text/html" charset="UTF-8">
	<link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/css/style.css">
	<script src="https://code.jquery.com/jquery-3.3.1.js"></script>
	<title>不動産管理システム</title>
</head>
<body>

	<header>
		<div class="container">
			<h1>不動産管理システム</h1>
			<c:if test="${sessionScope.userName != null && sessionScope.password != null}">
				<form class="header-btn" action="${pageContext.request.contextPath}/logout" method="POST">
					<c:if test="${pageContext.request.servletPath != '/WEB-INF/app/top/top.jsp'}">
						<a href="${pageContext.request.contextPath}/top" class="button">ホーム</a>
					</c:if>
					<button type="submit" class="button">ログアウト</button>
				</form>
			</c:if>
		</div>
	</header>