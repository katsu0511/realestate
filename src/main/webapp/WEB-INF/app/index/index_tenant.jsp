<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" import="java.io.*,java.util.*,java.text.*" %>
<jsp:include page="../common/header.jsp" flush="true"/>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<main>
	<div class="container">
		
		<c:choose>
			<c:when test="${tenants.isEmpty()}">
				<div class="no-display">登録されている借り主情報がありません。</div>
				<div class="add-button">
					<a href="${pageContext.request.contextPath}/register/tenant" class="button">新規追加</a>
				</div>
			</c:when>
			<c:otherwise>
				<div class="display-title">
					<h2>借り主情報一覧</h2>
				</div>
				<div class="add-button">
					<a href="${pageContext.request.contextPath}/register/tenant" class="button">新規追加</a>
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
						<div class="display-th" style="width:15%;">借り主ID</div>
						<div class="display-th" style="width:25%;">借り主名</div>
						<div class="display-th" style="width:25%;">契約中の物件</div>
						<div class="display-th" style="width:10%;">契約中の部屋</div>
						<div class="display-th" style="width:15%;">契約日</div>
						<div class="display-th" style="width:10%;">表示</div>
					</div>
				<c:forEach var="tenant" items="${tenants}">
					<div class="display-tbody visible">
						<div class="display-td" style="width:15%;">
							<a href="${pageContext.request.contextPath}/show/tenant?id=${tenant.tnntid}" class="display-con">
								${tenant.tnntid}
							</a>
						</div>
						<div class="display-td" style="width:25%;">
							${tenant.tnntnm}
						</div>
						<div class="display-td" style="width:25%;">
							${tenant.rsdcnm}
						</div>
						<div class="display-td" style="width:10%;">
							<c:if test="${tenant.roomnu != null}">
								${tenant.roomnu} 号室
							</c:if>
						</div>
						<div class="display-td" style="width:15%;">
							${tenant.contdt}
						</div>
						<div class="display-td active_flag" style="width:10%;">
							<c:choose>
								<c:when test="${tenant.actvfg == 't'}">有効</c:when>
								<c:when test="${tenant.actvfg == 'f'}">無効</c:when>
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
