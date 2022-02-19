<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" import="java.io.*,java.util.*,java.text.*" %>
<jsp:include page="../common/header.jsp" flush="true"/>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
   
<main>
	<div class="container">
	
		<div class="show">
			<div class="show-tr">
				<div class="show-th">担当者ID:</div>
				<div class="show-td">
					<div class="show-con">
						${rppn[0]}
					</div>
				</div>
			</div>
			
			<div class="show-tr">
				<div class="show-th">担当者名:</div>
				<div class="show-td">
					<div class="show-con">
						${rppn[2]}
					</div>
				</div>
			</div>
			
			<div class="show-tr">
				<div class="show-th">担当者名フリガナ:</div>
				<div class="show-td">
					<div class="show-con">
						${rppn[3]}
					</div>
				</div>
			</div>
			
			<div class="show-tr">
				<div class="show-th">電話番号1:</div>
				<div class="show-td">
					<div class="show-con">
						${rppn[4]}
					</div>
				</div>
			</div>
			
			<div class="show-tr">
				<div class="show-th">電話番号2:</div>
				<div class="show-td">
					<div class="show-con">
						${rppn[5]}
					</div>
				</div>
			</div>
			
			<div class="show-tr">
				<div class="show-th">メールアドレス:</div>
				<div class="show-td">
					<div class="show-con">
						${rppn[6]}
					</div>
				</div>
			</div>
			
			<div class="show-tr">
				<div class="show-th">有効フラグ:</div>
				<div class="show-td">
					<div class="show-con">
						<c:choose>
							<c:when test="${rppn[7] == 't'}">有効</c:when>
							<c:when test="${rppn[7] == 'f'}">無効</c:when>
						</c:choose>
					</div>
				</div>
			</div>
			
			<div class="show-tr">
				<div class="show-th">コメント:</div>
				<div class="show-td">
					<div class="show-con">
						${rppn[8]}
					</div>
				</div>
			</div>
			
			<div class="show-tr">
				<div class="show-th">登録日時:</div>
				<div class="show-td">
					<div class="show-con">
						${rppn[9].replaceAll("([0-9]+)-([0-9]+)-([0-9]+) ([0-9]+):([0-9]+):([0-9]+)","$1年$2月$3日 $4時$5分$6秒")}
					</div>
				</div>
			</div>
			
			<div class="show-tr">
				<div class="show-th">更新日時:</div>
				<div class="show-td">
					<div class="show-con">
						${rppn[10].replaceAll("([0-9]+)-([0-9]+)-([0-9]+) ([0-9]+):([0-9]+):([0-9]+)","$1年$2月$3日 $4時$5分$6秒")}
					</div>
				</div>
			</div>
		</div>
		
		<div class="show_buttons">
			<form action="${pageContext.request.contextPath}/delete/responsible_person?id=${rppn[0]}" method="post">
				<input type="submit" id="submit_btn" class="button" value="削除">
			</form>
			<a href="${pageContext.request.contextPath}/edit/responsible_person?id=${rppn[0]}" class="button">編集</a>
			<a href="${pageContext.request.contextPath}/index/responsible_person" class="button">戻る</a>
		</div>
		
	</div>
</main>

<script type="text/javascript">
$(function() {
	const submit = $('#submit_btn');
	submit.click(function() {
		if (!confirm('削除しますか？')) {
			return false;
		}
	});
});
</script>

<jsp:include page="../common/footer.jsp" flush="true"/>
