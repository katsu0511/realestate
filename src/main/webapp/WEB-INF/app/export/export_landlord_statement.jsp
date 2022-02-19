<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" import="java.io.*,java.util.*,java.text.*" %>
<jsp:include page="../common/header.jsp" flush="true"/>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<main>
	<div class="container">
		<h2>大家精算書</h2>
		<form name="LAST" action="${pageContext.request.contextPath}/export/landlord_statement" target="_blank" method="POST">
		
			<div class="tr">
				<label for="land_id" class="th">大家:<span>*</span></label>
				<div class="td">
					<select id="land_id" class="short check_target" name="land_id" required>
						<option value="">選択してください</option>
						<c:forEach var="land" items="${lands}">
							<option value="${land.landid}">${land.landnm}</option>
						</c:forEach>
					</select>
					<p id="land_id_error1" class="error">大家を選択してください</p>
				</div>
			</div>
			
			<div class="tr">
				<label for="pay_date" class="th">支払予定日:<span>*</span></label>
				<div class="td">
					<input type="date" id="pay_date" class="check_target" name="pay_date" required>
					<p id="pay_date_error1" class="error">支払予定日を入力してください</p>
				</div>
			</div>
			
			<div class="register_buttons">
				<input type="submit" id="submit" class="button" value="大家精算書発行">
				<a href="${pageContext.request.contextPath}/index/pdf_export" class="button">戻る</a>
			</div>
			
		</form>
	</div>
</main>

<script type="text/javascript">
$(function() {
	const submit = $('#submit');
	submit.click(function() {
		const elmList = document.LAST.getElementsByClassName("check_target");
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
	});
});
</script>
	
<jsp:include page="../common/footer.jsp" flush="true"/>
