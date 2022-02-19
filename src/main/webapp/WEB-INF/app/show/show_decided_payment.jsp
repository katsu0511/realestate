<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" import="java.io.*,java.util.*,java.text.*" %>
<jsp:include page="../common/header.jsp" flush="true"/>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<main>
	<div class="container">
	
		<div class="show">
			<div class="show-tr">
				<div class="show-th">消込番号:</div>
				<div class="show-td">
					<div class="show-con">
						${depy[0]}
					</div>
				</div>
			</div>
			
			<div class="show-tr">
				<div class="show-th">支払い日:</div>
				<div class="show-td">
					<div class="show-con">
						${depy[2].replaceAll("([0-9]+)-([0-9]+)-([0-9]+)","$1年$2月$3日")}
					</div>
				</div>
			</div>
			
			<div class="show-tr">
				<div class="show-th">借り主:</div>
				<div class="show-td">
					<div class="show-con">
						${tnntnm}
					</div>
				</div>
			</div>
			
			<div class="show-tr">
				<div class="show-th">契約中の物件:</div>
				<div class="show-td">
					<div class="show-con">
						${rsdcnm}
					</div>
				</div>
			</div>
			
			<div class="show-tr">
				<div class="show-th">契約中の部屋:</div>
				<div class="show-td">
					<div class="show-con">
						${roomnu}号室
					</div>
				</div>
			</div>
			
			<div class="show-tr">
				<div class="show-th">支払い額:</div>
				<div class="show-td">
					<div class="show-con">
						${depy[6]}円
					</div>
				</div>
			</div>
			
			<div class="show-tr">
				<div class="show-th">入金額:</div>
				<div class="show-td">
					<div class="show-con">
						${depy[7]}円
					</div>
				</div>
			</div>
			
			<div class="show-tr">
				<div class="show-th">差額:</div>
				<div class="show-td">
					<div class="show-con">
						${depy[8]}円
					</div>
				</div>
			</div>
			
			<div class="show-tr">
				<div class="show-th">登録日時:</div>
				<div class="show-td">
					<div class="show-con">
						${depy[9].replaceAll("([0-9]+)-([0-9]+)-([0-9]+) ([0-9]+):([0-9]+):([0-9]+)","$1年$2月$3日 $4時$5分$6秒")}
					</div>
				</div>
			</div>
		</div>
		
		<div class="show_buttons">
			<form action="${pageContext.request.contextPath}/delete/decided_payment?id=${depy[0]}" method="post">
				<input type="submit" id="delete_btn" class="button" value="削除">
			</form>
			<a href="${pageContext.request.contextPath}/index/decided_payment" class="button">戻る</a>
		</div>
		
	</div>
</main>

<script type="text/javascript">
$(function() {
	$('#delete_btn').click(function() {
		if (!confirm('削除しますか？')) {
			return false;
		}
	});
});
</script>

<jsp:include page="../common/footer.jsp" flush="true"/>
