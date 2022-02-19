<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" import="java.io.*,java.util.*,java.text.*" %>
<jsp:include page="../common/header.jsp" flush="true"/>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<main>
	<div class="container">
		<h2>賃貸精算書</h2>
		<form name="REST" action="${pageContext.request.contextPath}/export/rental_statement" target="_blank" method="POST">
		
			<div class="tr">
				<label for="tnnt_id" class="th">借り主:<span>*</span></label>
				<div class="td">
					<select id="tnnt_id" class="short check_target" name="tnnt_id" required>
						<option value="">選択してください</option>
						<c:forEach var="tenant" items="${tenants}">
							<option value="${tenant.tnntid}">${tenant.tnntnm}</option>
						</c:forEach>
					</select>
					<p id="tnnt_id_error1" class="error">借り主を選択してください</p>
				</div>
			</div>
			
			<div class="tr">
				<label for="st_date" class="th">契約期間:<span>*</span></label>
				<div class="td">
					<input type="date" id="st_date" class="mini check_target" name="st_date" required>
					〜
					<input type="date" id="en_date" class="mini check_target" name="en_date" required>
					<p id="st_date_error1" class="error">契約期間を入力してください</p>
					<p id="en_date_error1" class="error">契約期間を入力してください</p>
					<p id="date_error" class="error">契約期間を正しく入力してください</p>
				</div>
			</div>
			
			<div class="register_buttons">
				<input type="submit" id="submit" class="button" value="賃貸精算書発行">
				<a href="${pageContext.request.contextPath}/index/pdf_export" class="button">戻る</a>
			</div>
			
		</form>
	</div>
</main>

<script type="text/javascript">
$(function() {
	const submit = $('#submit');
	const stDate = $('#st_date');
	const enDate = $('#en_date');
	submit.click(function() {
		const elmList = document.REST.getElementsByClassName("check_target");
		var has_error = false;
		for (var elm of elmList) {
			var elmErr1 = document.getElementById(elm.id + "_error1");
			if (elmErr1) {
				elmErr1.style.display = "none";
			}
			if (elm.validity.valueMissing) {
				elmErr1.style.display = "block";
			}
		}
		
		if (stDate.val() != '' && enDate.val() != '' && stDate.val() > enDate.val()) {
			$('#date_error').css('display','block');
			return false;
		} else {
			$('#date_error').css('display','none');
		}
	});
});
</script>
	
<jsp:include page="../common/footer.jsp" flush="true"/>
