<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" import="java.io.*,java.util.*,java.text.*" %>
<jsp:include page="../common/header.jsp" flush="true"/>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<main>
	<div class="container">
		
		<c:choose>
			<c:when test="${payments.isEmpty()}">
				<div class="no-display">登録されている確定済み入金消込情報がありません。</div>
				<div style="text-align:center;margin-top:30px;">
					<a href="${pageContext.request.contextPath}/index/undecided_payment">未確定の入金消込情報を表示する</a>
				</div>
			</c:when>
			<c:otherwise>
				<div class="display-title">
					<h2>確定済み入金消込情報一覧</h2>
				</div>
				<div class="index-shift">
					<div class="date-search">
						<h3>日付けで絞り込み:</h3>
						<div class="date-input">
							<input type="date" id="date1" class="date" name="date1">
							〜
							<input type="date" id="date2" class="date" name="date2">
							<input type="button" id="apply" value="適用">
						</div>
					</div>
					<a href="${pageContext.request.contextPath}/index/undecided_payment">未確定の入金消込情報を表示する</a>
				</div>
				<div class="display">
					<div class="display-thead">
						<div class="display-th" style="width:15%;">支払日</div>
						<div class="display-th" style="width:20%;">借主名</div>
						<div class="display-th" style="width:25%;">物件名</div>
						<div class="display-th" style="width:10%;">部屋番号</div>
						<div class="display-th" style="width:10%;">請求</div>
						<div class="display-th" style="width:10%;">入金</div>
						<div class="display-th" style="width:10%;">差額</div>
					</div>
				<c:forEach var="payment" items="${payments}">
					<div class="display-tbody visible">
						<div class="display-td pay-date" style="width:15%;">
							<a href="${pageContext.request.contextPath}/show/decided_payment?id=${payment.pymtnu}" class="display-con">
								${payment.paydat}
							</a>
						</div>
						<div class="display-td" style="width:20%;">
							${payment.tnntnm}
						</div>
						<div class="display-td" style="width:25%;">
							${payment.rsdcnm}
						</div>
						<div class="display-td" style="width:10%;">
							${payment.roomnu}
						</div>
						<div class="display-td" style="width:10%;">
							${payment.payamt}円
						</div>
						<div class="display-td" style="width:10%;">
							${payment.rcvamt}円
						</div>
						<div class="display-td" style="width:10%;">
							${payment.balanc}円
						</div>
					</div>
		    	</c:forEach>
				</div>
				<div class="add-button">
					<input type="button" id="submit" class="button" value="エクスポート">
					<a href="${pageContext.request.contextPath}/index/undecided_payment" class="button">戻る</a>
				</div>
			</c:otherwise>
		</c:choose>
		
	</div>
</main>

<script src="https://cdnjs.cloudflare.com/ajax/libs/xlsx/0.14.2/xlsx.full.min.js"></script>
<script src="https://cdnjs.cloudflare.com/ajax/libs/FileSaver.js/1.3.8/FileSaver.min.js"></script>
<script type="text/javascript" src="${pageContext.request.contextPath}/js/export-xlsx.js"></script>
<script type="text/javascript">
$(function() {
	const date1 = $('#date1');
	const date2 = $('#date2');
	const apply = $('#apply');
	const display_tbody = $('.display-tbody');
	const displayTbody = display_tbody.toArray();
	apply.click(function() {
		if (!date1.val() == '' && !date2.val() == '' && date1.val() <= date2.val()) {
			display_tbody.addClass('visible');
			$.each(displayTbody,function() {
				var pay_date = $(this).children('.pay-date').text();
				var payDate = $.trim(pay_date);
				if (payDate < date1.val() || payDate > date2.val()) {
					$(this).removeClass('visible');
				}
			});
		}
	});
});
</script>
	
<jsp:include page="../common/footer.jsp" flush="true"/>
