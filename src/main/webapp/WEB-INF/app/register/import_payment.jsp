<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" import="java.io.*,java.util.*,java.text.*" %>
<jsp:include page="../common/header.jsp" flush="true"/>

<main>
	<div class="container">
		<h2>支払情報インポート</h2>
		<form name="PYMT" action="${pageContext.request.contextPath}/import/payment" method="POST" enctype="multipart/form-data">
		
			<div class="tr">
				<label for="payment" class="th">支払情報csv:<span>*</span></label>
				<div class="td">
					<input type="file" id="payment" name="payment" required>
					<p>※支払情報のcsvファイルを読み込みます</p>
					<p id="payment_error" class="error">csvファイルをアップロードしてください</p>
				</div>
			</div>
			
			<div class="register_buttons">
				<input type="submit" id="submit" class="button" value="インポート">
				<a href="${pageContext.request.contextPath}/index/undecided_payment" class="button">戻る</a>
			</div>
			
		</form>
	</div>
</main>

<script type="text/javascript">
$(function() {
	const submit = $('#submit');
	const payment = $('#payment');
	const payment_error = $('#payment_error');
	
	submit.click(function() {
		if (payment.val() == '') {
			payment_error.css('display','block');
		} else {
			payment_error.css('display','none');
		}
		
		if (payment.val() == '') {
			payment_error.focus();
		} else {
			if (!confirm('送信しますか？')) {
				return false;
			}
		}
	});
});
</script>
	
<jsp:include page="../common/footer.jsp" flush="true"/>
