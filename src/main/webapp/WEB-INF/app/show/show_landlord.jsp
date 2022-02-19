<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" import="java.io.*,java.util.*,java.text.*" %>
<jsp:include page="../common/header.jsp" flush="true"/>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<main>
	<div class="container">
	
		<div class="show">
			<div class="show-tr">
				<div class="show-th">大家ID:</div>
				<div class="show-td">
					<div class="show-con">
						${land[0]}
					</div>
				</div>
			</div>
			
			<div class="show-tr">
				<div class="show-th">大家名:</div>
				<div class="show-td">
					<div class="show-con">
						${land[2]}
					</div>
				</div>
			</div>
			
			<div class="show-tr">
				<div class="show-th">大家名フリガナ:</div>
				<div class="show-td">
					<div class="show-con">
						${land[3]}
					</div>
				</div>
			</div>
			
			<div class="show-tr">
				<div class="show-th">郵便番号:</div>
				<div class="show-td">
					<div class="show-con">
						${land[4]}
					</div>
				</div>
			</div>
			
			<div class="show-tr">
				<div class="show-th">住所:</div>
				<div class="show-td">
					<div class="show-con">
						${land[5]}
						${land[6]}
						${land[7]}
						${land[8]}
					</div>
				</div>
			</div>
			
			<div class="show-tr">
				<div class="show-th">電話番号1:</div>
				<div class="show-td">
					<div class="show-con">
						${land[9]}
					</div>
				</div>
			</div>
			
			<div class="show-tr">
				<div class="show-th">電話番号2:</div>
				<div class="show-td">
					<div class="show-con">
						${land[10]}
					</div>
				</div>
			</div>
			
			<div class="show-tr">
				<div class="show-th">メールアドレス:</div>
				<div class="show-td">
					<div class="show-con">
						${land[11]}
					</div>
				</div>
			</div>
			
			<div class="show-tr">
				<div class="show-th">銀行名:</div>
				<div class="show-td">
					<div class="show-con">
						<c:if test="${land[12] != ''}">
							${land[12]}銀行
						</c:if>
					</div>
				</div>
			</div>
			
			<div class="show-tr">
				<div class="show-th">銀行支店名:</div>
				<div class="show-td">
					<div class="show-con">
						${land[13]}
					</div>
				</div>
			</div>
			
			<div class="show-tr">
				<div class="show-th">口座区分:</div>
				<div class="show-td">
					<div class="show-con">
						${land[14]}
					</div>
				</div>
			</div>
			
			<div class="show-tr">
				<div class="show-th">口座番号:</div>
				<div class="show-td">
					<div class="show-con">
						${land[15]}
					</div>
				</div>
			</div>
			
			<div class="show-tr">
				<div class="show-th">口座名義:</div>
				<div class="show-td">
					<div class="show-con">
						${land[16]}
					</div>
				</div>
			</div>
			
			<div class="show-tr">
				<div class="show-th">有効フラグ:</div>
				<div class="show-td">
					<div class="show-con">
						<c:choose>
							<c:when test="${land[17] == 't'}">有効</c:when>
							<c:when test="${land[17] == 'f'}">無効</c:when>
						</c:choose>
					</div>
				</div>
			</div>
			
			<div class="show-tr">
				<div class="show-th">コメント:</div>
				<div class="show-td">
					<div class="show-con">
						${land[18]}
					</div>
				</div>
			</div>
			
			<div class="show-tr">
				<div class="show-th">登録日時:</div>
				<div class="show-td">
					<div class="show-con">
						${land[19].replaceAll("([0-9]+)-([0-9]+)-([0-9]+) ([0-9]+):([0-9]+):([0-9]+)","$1年$2月$3日 $4時$5分$6秒")}
					</div>
				</div>
			</div>
			
			<div class="show-tr">
				<div class="show-th">更新日時:</div>
				<div class="show-td">
					<div class="show-con">
						${land[20].replaceAll("([0-9]+)-([0-9]+)-([0-9]+) ([0-9]+):([0-9]+):([0-9]+)","$1年$2月$3日 $4時$5分$6秒")}
					</div>
				</div>
			</div>
		</div>
		
		<div class="show_buttons">
			<form action="${pageContext.request.contextPath}/delete/landlord?id=${land[0]}" method="post">
				<input type="submit" id="submit_btn" class="button" value="削除">
			</form>
			<a href="${pageContext.request.contextPath}/edit/landlord?id=${land[0]}" class="button">編集</a>
			<a href="${pageContext.request.contextPath}/index/landlord" class="button">戻る</a>
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
