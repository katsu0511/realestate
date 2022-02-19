<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" import="java.io.*,java.util.*,java.text.*" %>
<jsp:include page="../common/header.jsp" flush="true"/>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<main>
	<div class="container">
		<h2>部屋情報登録</h2>
		<form name="ROOM" action="${pageContext.request.contextPath}/register/room" method="POST">
		
			<input type="hidden" id="context_path" value="${pageContext.request.contextPath}">
		
			<c:if test="${errorMessage != null && errorMessage != ''}">
				<div class="error-message" style="margin-bottom:20px;">
					${errorMessage}
				</div>
			</c:if>
		
			<div class="tr">
				<label for="rsd_id" class="th">物件:<span>*</span></label>
				<div class="td">
					<select id="rsd_id" class="short check_target" name="rsd_id" required>
						<option value="">選択してください</option>
						<c:forEach var="rsdc" items="${rsdcs}">
							<option value="${rsdc.rsdcid}" <c:if test="${room[1] == rsdc.rsdcid}"> selected </c:if>>${rsdc.rsdcnm}</option>
						</c:forEach>
					</select>
					<p id="rsd_id_error1" class="error">物件を選択してください</p>
				</div>
			</div>
			
			<div class="tr">
				<label for="room_id" class="th">部屋ID:</label>
				<div class="td">
					<input type="text" id="room_id" class="short back-gray" name="room_id" value="${room[0]}" readonly>
				</div>
			</div>
			
			<div class="tr">
				<label for="room_nu" class="th">部屋番号:<span>*</span></label>
				<div class="td">
					<input type="text" id="room_nu" class="short check_target" name="room_nu" value="${room[2]}" required>
					号室
					<p id="room_nu_error1" class="error">部屋番号を入力してください</p>
					<c:if test="${errorMessage != null && errorMessage != ''}">
						<p style="color:red;">${room[2]}号室はすでに登録されています</p>
					</c:if>
				</div>
			</div>
			
			<div class="tr">
				<label for="rent" class="th">家賃:<span>*</span></label>
				<div class="td">
					<input type="text" id="rent" class="short check_target" name="rent" value="${room[3]}" maxlength="7" oninput="value = value.replace(/[^0-9]+/i,'');" required>
					円
					<p id="rent_error1" class="error">家賃を入力してください</p>
				</div>
			</div>
			
			<div class="tr">
				<label for="mng_fee" class="th">管理費:<span>*</span></label>
				<div class="td">
					<input type="text" id="mng_fee" class="short check_target" name="mng_fee" value="${room[4]}" maxlength="7" oninput="value = value.replace(/[^0-9]+/i,'');" required>
					円
					<p id="mng_fee_error1" class="error">管理費を入力してください</p>
				</div>
			</div>
			
			<div class="tr">
				<label for="deposit" class="th">敷金:<span>*</span></label>
				<div class="td">
					<input type="text" id="deposit" class="short check_target" name="deposit" value="${room[5]}" maxlength="7" oninput="value = value.replace(/[^0-9]+/i,'');" required>
					円
					<p id="deposit_error1" class="error">敷金を入力してください</p>
				</div>
			</div>
			
			<div class="tr">
				<label for="honorarium" class="th">礼金:<span>*</span></label>
				<div class="td">
					<input type="text" id="honorarium" class="short check_target" name="honorarium" value="${room[6]}" maxlength="7" oninput="value = value.replace(/[^0-9]+/i,'');" required>
					円
					<p id="honorarium_error1" class="error">礼金を入力してください</p>
				</div>
			</div>
			
			<div class="tr">
				<label for="brokerage" class="th">仲介料:<span>*</span></label>
				<div class="td">
					<input type="text" id="brokerage" class="short check_target" name="brokerage" value="${room[7]}" maxlength="7" oninput="value = value.replace(/[^0-9]+/i,'');" required>
					円
					<p id="brokerage_error1" class="error">仲介料を入力してください</p>
				</div>
			</div>
			
			<div class="tr">
				<label for="key_fee" class="th">鍵代金:<span>*</span></label>
				<div class="td">
					<input type="text" id="key_fee" class="short check_target" name="key_fee" value="${room[8]}" maxlength="7" oninput="value = value.replace(/[^0-9]+/i,'');" required>
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
								<input type="text" id="${adit.itemid}" class="short check_target" name="${adit.itemid}" value="${adit.adebvl}"
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
				<label for="comment" class="th">コメント:</label>
				<div class="td">
					<textarea id="comment" name="comment">${room[9]}</textarea>
				</div>
			</div>
			
			<div class="register_buttons">
				<input type="submit" class="submit_btn button" name="submit" value="登録終了">
				<input type="submit" class="submit_btn button" name="submit" value="引き続き部屋登録">
				<a href="${pageContext.request.contextPath}/index/room" class="button">戻る</a>
			</div>
			
		</form>
	</div>
</main>

<script type="text/javascript">
$(function() {
	const submit = $('.submit_btn');
	submit.click(function() {
		const elmList = document.ROOM.getElementsByClassName("check_target");
		var has_error = false;
		for (var elm of elmList) {
			var elmErr1 = document.getElementById(elm.id + "_error1");
			var elmErr2 = document.getElementById(elm.id + "_error2");
			if (elmErr1) {
				elmErr1.style.display = "none";
			}
			if (elmErr2) {
				elmErr2.style.display = "none";
			}
			if (elm.validity.valueMissing) {
				elmErr1.style.display = "block";
				has_error = true;
			}
			if (elm.validity.patternMismatch) {
				elmErr2.style.display = "block";
				has_error = true;
			}
		}
		if (!has_error) {
			if (!confirm('送信しますか？')) {
				return false;
			}
		}
	});
	
	const rsdId = $('#rsd_id');
	const roomId = $('#room_id');
	const rent = $('#rent');
	const mngFee = $('#mng_fee');
	const deposit = $('#deposit');
	const honorarium = $('#honorarium');
	const brokerage = $('#brokerage');
	const keyFee = $('#key_fee');
	const contextPath = $('#context_path');
	rsdId.change(function() {
		roomId.val('');
		rent.val('');
		mngFee.val('');
		deposit.val('');
		honorarium.val('');
		brokerage.val('');
		keyFee.val('');
		if (rsdId.val() != '') {
			var req = new XMLHttpRequest();
			var url = contextPath.val() + '/cite/residence?id=' + rsdId.val();
			req.open('GET',url);
			req.send();
			req.onreadystatechange = function() {
				if (req.readyState === 4 && req.status === 200) {
					let info = (req.responseText)?JSON.parse(req.responseText):null;
					if (info) {
						roomId.val(info.roomid);
						rent.val(info.rentfe);
						mngFee.val(info.mngfee);
						deposit.val(info.depsit);
						honorarium.val(info.hnrrum);
						brokerage.val(info.brkrag);
						keyFee.val(info.keyfee);
					}
				}
			}
		}
	});
	
	rsdId.change(function() {
		$('#add_items').empty();
		if (rsdId.val() != '') {
			var req = new XMLHttpRequest();
			var url = contextPath.val() + '/cite/residence_for_adits?id=' + rsdId.val();
			req.open('GET',url);
			req.send();
			req.onreadystatechange = function() {
				if (req.readyState === 4 && req.status === 200) {
					let info = (req.responseText)?JSON.parse(req.responseText):null;
					if (info.adits != '[]') {
						var array1 = info.adits.slice(2).slice(0, -2).split('}, {');
						var array2 = [];
						var array3 = [];
						for (let i = 0; i < array1.length; i++) {
							array2 = array1[i].split(', ');
							array3.push(array2);
							array2 = [];
						}
						
						let adits = [];
						let adit = {};
						for (let i1 = 0; i1 < array3.length; i1++) {
							for (let i2 = 0; i2 < array3[i1].length; i2++) {
								var key = array3[i1][i2].trim().substring(0, 6);
								var value = array3[i1][i2].trim().substring(7, array3[i1][i2].length);
								adit[key] = value;
							}
							adits.push(adit);
							adit = {};
						}
						
						for (let i = 0; i < adits.length; i++) {
							var pattern = adits[i].spclng == '' ? '' : 'pattern="[0-9]{' + adits[i].spclng + '}" ';
							var maxLength = adits[i].maxlng == '' ? '' : 'maxlength="' + adits[i].maxlng + '" ';
							var comment = adits[i].commnt == '' ? '' : '<p>' + adits[i].commnt + '</p>';
							var error2 = adits[i].spclng == '' ? '' : '<p id="' + adits[i].itemid + '_error2" class="error">' + adits[i].itemnm + 'の桁数が間違っています</p>';
							
							$('#add_items').append(
								'<div class="tr">' +
									'<label for="' + adits[i].itemid + '" class="th">' + adits[i].itemnm + ':<span>*</span></label>' +
									'<div class="td">' +
										'<input type="text" id="' + adits[i].itemid + '" class="short check_target" name="' + adits[i].itemid + '" value="' + adits[i].adebvl + '" ' + pattern + maxLength + 'oninput="value = value.replace(/[^0-9]+/i,\'\');" required> 円' +
										comment +
										'<p id="' + adits[i].itemid + '_error1" class="error">' + adits[i].itemnm + 'を入力してください</p>' +
										error2 +
									'</div>' +
								'</div>'
							);
						}
					}
				}
			}
		}
	});
});
</script>
	
<jsp:include page="../common/footer.jsp" flush="true"/>
