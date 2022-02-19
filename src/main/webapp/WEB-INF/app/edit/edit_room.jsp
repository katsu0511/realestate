<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" import="java.io.*,java.util.*,java.text.*" %>
<jsp:include page="../common/header.jsp" flush="true"/>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<main>
	<div class="container">
		<h2>部屋情報編集</h2>
		<form name="ROOM" action="${pageContext.request.contextPath}/edit/room" method="POST">
		
			<div class="tr">
				<label for="room_id" class="th">部屋ID:</label>
				<div class="td">
					<input type="text" id="room_id" class="short back-gray" name="room_id" value="${room[0]}" readonly>
				</div>
			</div>
		
			<div class="tr">
				<label for="rsdc_name" class="th">物件名:</label>
				<div class="td">
					<input type="text" id="rsdc_name" class="short back-gray" name="rsdc_name" value="${rsdcnm}" readonly>
				</div>
			</div>
			
			<div class="tr">
				<label for="room_nu" class="th">部屋番号:</label>
				<div class="td">
					<input type="text" id="room_nu" class="short back-gray" name="room_nu" value="${room[3]}号室" readonly>
				</div>
			</div>
			
			<div class="tr">
				<label for="rent" class="th">家賃:<span>*</span></label>
				<div class="td">
					<input type="text" id="rent" class="short check_target" name="rent" value="${room[5]}" maxlength="7" oninput="value = value.replace(/[^0-9]+/i,'');" required>
					円
					<p id="rent_error1" class="error">家賃を入力してください</p>
				</div>
			</div>
			
			<div class="tr">
				<label for="mng_fee" class="th">管理費:<span>*</span></label>
				<div class="td">
					<input type="text" id="mng_fee" class="short check_target" name="mng_fee" value="${room[6]}" maxlength="7" oninput="value = value.replace(/[^0-9]+/i,'');" required>
					円
					<p id="mng_fee_error1" class="error">管理費を入力してください</p>
				</div>
			</div>
			
			<div class="tr">
				<label for="deposit" class="th">敷金:<span>*</span></label>
				<div class="td">
					<input type="text" id="deposit" class="short check_target" name="deposit" value="${room[7]}" maxlength="7" oninput="value = value.replace(/[^0-9]+/i,'');" required>
					円
					<p id="deposit_error1" class="error">敷金を入力してください</p>
				</div>
			</div>
			
			<div class="tr">
				<label for="honorarium" class="th">礼金:<span>*</span></label>
				<div class="td">
					<input type="text" id="honorarium" class="short check_target" name="honorarium" value="${room[8]}" maxlength="7" oninput="value = value.replace(/[^0-9]+/i,'');" required>
					円
					<p id="honorarium_error1" class="error">礼金を入力してください</p>
				</div>
			</div>
			
			<div class="tr">
				<label for="brokerage" class="th">仲介料:<span>*</span></label>
				<div class="td">
					<input type="text" id="brokerage" class="short check_target" name="brokerage" value="${room[9]}" maxlength="7" oninput="value = value.replace(/[^0-9]+/i,'');" required>
					円
					<p id="brokerage_error1" class="error">仲介料を入力してください</p>
				</div>
			</div>
			
			<div class="tr">
				<label for="key_fee" class="th">鍵代金:<span>*</span></label>
				<div class="td">
					<input type="text" id="key_fee" class="short check_target" name="key_fee" value="${room[10]}" maxlength="7" oninput="value = value.replace(/[^0-9]+/i,'');" required>
					円
					<p id="key_fee_error1" class="error">鍵代金を入力してください</p>
				</div>
			</div>
			
			<div id="add_items">
				<c:if test="${!empty adits}">
					<c:forEach var="adit" items="${adits}">
						<div class="tr">
							<label for="${adit.itemid}" class="th">${adit.itemnm}:<span>*</span></label>
							<div class="td">
								<input type="text" id="${adit.itemid}" class="short check_target" name="${adit.itemid}" value="${adit.adehvl}"
									<c:if test="${!empty adit.spclng}">pattern="[0-9]{${adit.spclng}}"</c:if>
									<c:if test="${!empty adit.maxlng}">maxlength="${adit.maxlng}"</c:if>
									oninput="value = value.replace(/[^0-9]+/i,'');" required
								>
								円
								<c:if test="${!empty adit.commnt}"><p>${adit.commnt}</p></c:if>
								<p id="${adit.itemid}_error1" class="error">${adit.itemnm}を入力してください</p>
								<c:if test="${!empty adit.spclng}"><p id="${adit.itemid}_error2" class="error">${adit.itemnm}の桁数が間違っています</p></c:if>
							</div>
						</div>
					</c:forEach>
				</c:if>
			</div>
			
			<div class="tr">
				<label for="actv_flg" class="th">有効フラグ:</label>
				<div class="td">
					<select id="actv_flg" name="actv_flg">
						<option value="true" <c:if test="${room[11] == 't'}"> selected </c:if>>有効</option>
						<option value="false" <c:if test="${room[11] == 'f'}"> selected </c:if>>無効</option>
					</select>
				</div>
			</div>

			<div class="tr">
				<label for="comment" class="th">コメント:</label>
				<div class="td">
					<textarea id="comment" name="comment">${room[12]}</textarea>
				</div>
			</div>
			
			<div class="register_buttons">
				<input type="submit" id="submit" class="button" name="submit" value="保存">
				<a href="${pageContext.request.contextPath}/show/room?id=${room[0]}" class="button">戻る</a>
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
