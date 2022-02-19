<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" import="java.io.*,java.util.*,java.text.*" %>
<jsp:include page="../common/header.jsp" flush="true"/>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<main>
	<div class="container">
	
		<input type="hidden" id="context_path" value="${pageContext.request.contextPath}">
		
		<c:choose>
			<c:when test="${payments.isEmpty()}">
				<div class="no-display">未確定の入金消込情報がありません。</div>
				<div class="add-button">
					<a href="${pageContext.request.contextPath}/import/payment" class="button">インポート</a>
					<a href="${pageContext.request.contextPath}/top" class="button">戻る</a>
				</div>
				<div style="text-align:center;">
					<a href="${pageContext.request.contextPath}/index/decided_payment">確定した入金消込情報を表示する</a>
				</div>
			</c:when>
			<c:otherwise>
				<div class="display-title">
					<h2>未確定入金消込情報一覧</h2>
				</div>
				<div class="add-button">
					<a href="${pageContext.request.contextPath}/import/payment" class="button">インポート</a>
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
					<a href="${pageContext.request.contextPath}/index/decided_payment">確定した入金消込情報を表示する</a>
				</div>
				<div class="display">
					<div class="display-thead">
						<div class="display-th" style="width:10%;">日付</div>
						<div class="display-th" style="width:20%;">名称</div>
						<div class="display-th" style="width:10%;">入金</div>
						<div class="display-th" style="width:15%;">物件</div>
						<div class="display-th" style="width:5%;">部屋</div>
						<div class="display-th" style="width:10%;">借主名</div>
						<div class="display-th" style="width:10%;">請求</div>
						<div class="display-th" style="width:10%;">差額</div>
						<div class="display-th" style="width:10%;">登録</div>
					</div>
				<c:forEach var="payment" items="${payments}">
					<div class="display-tbody visible">
						<div class="display-td pay-date" style="width:10%;">
							<a href="${pageContext.request.contextPath}/show/undecided_payment?id=${payment.pymtnu}" class="display-con">
								${payment.paydat}
							</a>
						</div>
						<div class="display-td payee" style="width:20%;">
							${payment.payenm}
						</div>
						<div class="display-td receive" style="width:10%;">
							${payment.rcvamt} 円
						</div>
						<div class="display-td pad-fix" style="width:15%;">
							<input type="text" class="rsdc_id pay-input back-gray" readonly>
						</div>
						<div class="display-td pad-fix" style="width:5%;">
							<input type="text" class="room_id pay-input back-gray" readonly>
						</div>
						<div class="display-td pad-fix" style="width:10%;">
							<select class="pay-select" required>
								<option value="">選択してください</option>
								<c:if test="${payment.tnnts != null}">
									<c:set var="tnnts" value="${payment.tnnts.split(',')}" />
									<c:forEach var="tnnt" items="${tnnts}">
										<c:set var="tn" value="${tnnt.split(' ')}" />
										<option value="${tn[0]}">${tnnt}</option>
									</c:forEach>
								</c:if>
							</select>
						</div>
						<div class="display-td pad-fix" style="width:10%;">
							<input type="text" class="payamt pay-input pay-sh" maxlength="7" oninput="value = value.replace(/[^0-9]+/i,'');" required> 円
						</div>
						<div class="display-td pad-fix" style="width:10%;">
							<input type="text" class="balance pay-input pay-sh back-gray" readonly required> 円
						</div>
						<div class="display-td pad-fix" style="width:10%;">
							<form action="${pageContext.request.contextPath}/register/decided_payment" method="POST" class="decide-form">
								<input type="submit" class="register-button" value="登録">
								<input type="hidden" name="pay_nu" value="${payment.pymtnu}">
								<input type="hidden" class="rsdc_id_form" name="rsdc_id">
								<input type="hidden" class="room_id_form" name="room_id">
								<input type="hidden" class="tnnt_form" name="tnnt_id">
								<input type="hidden" class="payamt_form" name="payamt">
								<input type="hidden" class="balance" name="balance">
							</form>
						</div>
					</div>
		    	</c:forEach>
				</div>
				<div class="show_buttons">
					<a href="${pageContext.request.contextPath}/top" class="button">戻る</a>
				</div>
			</c:otherwise>
		</c:choose>
		
	</div>
</main>

<script type="text/javascript">
$(function() {
	const date1 = $('#date1');
	const date2 = $('#date2');
	const apply = $('#apply');
	const display_tbody = $('.display-tbody');
	const displayTbody = display_tbody.toArray();
	apply.click(function() {
		if (!date1.val() == '' && !date2.val() == '' && date1.val() <= date2.val()) {
			display_tbody.css('display','flex');
			$.each(displayTbody,function() {
				var pay_date = $(this).children('.pay-date').text();
				var payDate = $.trim(pay_date);
				if (payDate < date1.val() || payDate > date2.val()) {
					$(this).css('display','none');
				}
			});
		}
	});
	
	$('.pay-select').change(function() {
		var tnntForm = $(this).parent().parent().find('.tnnt_form');
		var receive = $(this).parent().parent().find('.receive');
		var rsdc = $(this).parent().parent().find('.rsdc_id');
		var rsdcForm = $(this).parent().parent().find('.rsdc_id_form');
		var room = $(this).parent().parent().find('.room_id');
		var roomForm = $(this).parent().parent().find('.room_id_form');
		var payamt = $(this).parent().parent().find('.payamt');
		var payamtForm = $(this).parent().parent().find('.payamt_form');
		var balance = $(this).parent().parent().find('.balance');
		rsdc.val('');
		room.val('');
		payamt.val('');
		balance.val('');
		tnntForm.val('');
		rsdcForm.val('');
		roomForm.val('');
		payamtForm.val('');
		if ($(this).val() != '') {
			var req = new XMLHttpRequest();
			const contextPath = $('#context_path');
			var url = contextPath.val() + '/cite/tenant_for_payment?id=' + $(this).val();
			req.open('GET',url);
			req.send();
			req.onreadystatechange = function() {
				if (req.readyState === 4 && req.status === 200) {
					let info = (req.responseText)?JSON.parse(req.responseText):null;
					if (info) {
						rsdc.val(info.rsdcnm);
						room.val(info.roomnu);
						tnntForm.val(info.tnntid);
						rsdcForm.val(info.rsdcid);
						roomForm.val(info.roomid);
					}
				}
			}
		}
	});
	
	$('.payamt').keyup(function() {
		var receive = $.trim($(this).parent().parent().find('.receive').text());
		var balance = $(this).parent().parent().find('.balance').val('');
		var payamtForm = $(this).parent().parent().find('.payamt_form');
		var payamt = $(this).val();
		if (payamt == '') {
			payamtForm.val('');
			balance.val('');
		} else {
			var receiveInt = parseInt(receive);
			var payamtInt = parseInt(payamt);
			var balanceInt = payamtInt - receiveInt;
			balance.val(balanceInt);
			payamtForm.val(payamt);
		}
	});
	
	const register_button = $('.register-button');
	register_button.click(function() {
		var pay_select = $(this).parent().parent().parent().find('.pay-select').val();
		var payamt = $(this).parent().parent().parent().find('.payamt').val();
		var balance = $(this).parent().parent().parent().find('.balance').val();
		if (pay_select == '') {
			alert('借主名を選択してください');
			return false;
		} else if (payamt == '') {
			alert('請求額を入力してください');
			return false;
		} else if (balance != '0') {
			alert('差額は0円である必要があります');
			return false;
		} else {
			if (!confirm('確定しますか？')) {
				return false;
			}
		}
	});
});
</script>
	
<jsp:include page="../common/footer.jsp" flush="true"/>
