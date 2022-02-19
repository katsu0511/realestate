<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" import="java.io.*,java.util.*,java.text.*" %>
<jsp:include page="../common/header.jsp" flush="true"/>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<main>
	<div class="container">
		
		<c:choose>
			<c:when test="${rppns.isEmpty()}">
				<div class="no-display">登録されている担当者情報がありません。</div>
				<div class="add-button">
					<a href="${pageContext.request.contextPath}/register/responsible_person" class="button">新規追加</a>
				</div>
			</c:when>
			<c:otherwise>
				<div class="display-title">
					<h2>担当者情報一覧</h2>
				</div>
				<div class="add-button">
					<a href="${pageContext.request.contextPath}/register/responsible_person" class="button">新規追加</a>
				</div>
				<div class="search">
					<h3>表示切り替え:</h3>
					<select id="active_search">
						<option value="">全て表示</option>
						<option value="有効">有効のみ表示</option>
					</select>
				</div>
				<div class="display">
					<div class="display-thead">
						<div class="display-th" style="width:20%;">担当者ID</div>
						<div class="display-th" style="width:30%;">担当者名</div>
						<div class="display-th" style="width:30%;">担当者名フリガナ</div>
						<div class="display-th" style="width:20%;">表示</div>
					</div>
				<c:forEach var="rppn" items="${rppns}">
					<div class="display-tbody visible">
						<div class="display-td" style="width:20%;">
							<a href="${pageContext.request.contextPath}/show/responsible_person?id=${rppn.rppsid}" class="display-con">
								${rppn.rppsid}
							</a>
						</div>
						<div class="display-td" style="width:30%;">
							${rppn.rppsnm}
						</div>
						<div class="display-td" style="width:30%;">
							${rppn.rppsnf}
						</div>
						<div class="display-td active_flag" style="width:20%;">
							<c:choose>
								<c:when test="${rppn.actvfg == 't'}">有効</c:when>
								<c:when test="${rppn.actvfg == 'f'}">無効</c:when>
							</c:choose>
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

<script type="text/javascript">
$(function() {
	const activeSearch = $('#active_search');
	const display_tbody = $('.display-tbody');
	const displayTbody = display_tbody.toArray();
	activeSearch.change(function() {
		display_tbody.css('display','flex');
		if (activeSearch.val() == '') {
			return false;
		} else {
			$.each(displayTbody,function() {
				var active_flag = $(this).children('.active_flag').text();
				var activeFlag = $.trim(active_flag);
				if (activeFlag == '無効') {
					$(this).css('display','none');
				}
			});
		}
	});
});
</script>

<jsp:include page="../common/footer.jsp" flush="true"/>
