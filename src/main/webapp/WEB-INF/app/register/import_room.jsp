<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" import="java.io.*,java.util.*,java.text.*" %>
<jsp:include page="../common/header.jsp" flush="true"/>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<main>
	<div class="container">
		<h2>部屋情報インポート</h2>
		<form name="ROOM" action="${pageContext.request.contextPath}/import/room" method="POST" enctype="multipart/form-data">
		
			<div class="tr">
				<label for="rsd_id" class="th">物件ID:<span>*</span></label>
				<div class="td">
					<select id="rsd_id" class="short check_target" name="rsd_id" required>
						<option value="">選択してください</option>
						<c:forEach var="rsdc" items="${rsdcs}">
							<option value="${rsdc.rsdcid}">${rsdc.rsdcnm}</option>
						</c:forEach>
					</select>
					<p id="rsd_id_error1" class="error">物件を選択してください</p>
				</div>
			</div>
		
			<div class="tr">
				<label for="room" class="th">部屋情報csv:<span>*</span></label>
				<div class="td">
					<input type="file" id="room" class="check_target" name="room" required>
					<p>※部屋情報のcsvファイルを読み込みます</p>
					<p id="room_error1" class="error">csvファイルをアップロードしてください</p>
				</div>
			</div>
			
			<div class="register_buttons">
				<input type="submit" id="submit" class="button" value="インポート">
				<a href="${pageContext.request.contextPath}/index/room" class="button">戻る</a>
			</div>
			
		</form>
	</div>
</main>

<script type="text/javascript">
$(function() {
	const submit = $('#submit');
	submit.click(function() {
		const elmList = document.ROOM.getElementsByClassName("check_target");
		var has_error = false;
		for (var elm of elmList) {
			var elmErr1 = document.getElementById(elm.id + "_error1");
			if (elmErr1) {
				elmErr1.style.display = "none";
			}
			if (elm.validity.valueMissing) {
				elmErr1.style.display = "block";
				has_error = true;
			}
		}
		if (!has_error) {
			if (!confirm('送信しますか？')) {
				return false;
			}
		}
	});
});
</script>
	
<jsp:include page="../common/footer.jsp" flush="true"/>
