<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" import="java.io.*,java.util.*,java.text.*" %>
<jsp:include page="../common/header.jsp" flush="true"/>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
   
<main>
	<div class="container">
	
		<div class="show">
			<div class="show-tr">
				<div class="show-th">部屋ID:</div>
				<div class="show-td">
					<div class="show-con">
						${room[0]}
					</div>
				</div>
			</div>
			
			<div class="show-tr">
				<div class="show-th">物件:</div>
				<div class="show-td">
					<div class="show-con">
						${rsdcnm}
					</div>
				</div>
			</div>
			
			<div class="show-tr">
				<div class="show-th">部屋番号:</div>
				<div class="show-td">
					<div class="show-con">
						${room[3]} 号室
					</div>
				</div>
			</div>
			
			<div class="show-tr">
				<div class="show-th">契約有無:</div>
				<div class="show-td">
					<div class="show-con">
						<c:choose>
							<c:when test="${room[4] == 't'}">契約済み</c:when>
							<c:when test="${room[4] == 'f'}">未契約</c:when>
						</c:choose>
					</div>
				</div>
			</div>
			
			<div class="show-tr">
				<div class="show-th">家賃:</div>
				<div class="show-td">
					<div class="show-con">
						<c:if test="${room[5] != ''}">
							${room[5]} 円
						</c:if>
					</div>
				</div>
			</div>
			
			<div class="show-tr">
				<div class="show-th">管理費:</div>
				<div class="show-td">
					<div class="show-con">
						<c:if test="${room[6] != ''}">
							${room[6]} 円
						</c:if>
					</div>
				</div>
			</div>
			
			<div class="show-tr">
				<div class="show-th">敷金:</div>
				<div class="show-td">
					<div class="show-con">
						<c:if test="${room[7] != ''}">
							${room[7]} 円
						</c:if>
					</div>
				</div>
			</div>
			
			<div class="show-tr">
				<div class="show-th">礼金:</div>
				<div class="show-td">
					<div class="show-con">
						<c:if test="${room[8] != ''}">
							${room[8]} 円
						</c:if>
					</div>
				</div>
			</div>
			
			<div class="show-tr">
				<div class="show-th">仲介料:</div>
				<div class="show-td">
					<div class="show-con">
						<c:if test="${room[9] != ''}">
							${room[9]} 円
						</c:if>
					</div>
				</div>
			</div>
			
			<div class="show-tr">
				<div class="show-th">鍵代金:</div>
				<div class="show-td">
					<div class="show-con">
						<c:if test="${room[10] != ''}">
							${room[10]} 円
						</c:if>
					</div>
				</div>
			</div>
			
			<c:if test="${!empty adehs}">
				<c:forEach var="adeh" items="${adehs}">
					<div class="show-tr">
						<div class="show-th">${adeh.itemnm}:</div>
						<div class="show-td">
							<div class="show-con">
								${adeh.adehvl} 円
							</div>
						</div>
					</div>
				</c:forEach>
			</c:if>
			
			<div class="show-tr">
				<div class="show-th">有効フラグ:</div>
				<div class="show-td">
					<div class="show-con">
						<c:choose>
							<c:when test="${room[11] == 't'}">有効</c:when>
							<c:when test="${room[11] == 'f'}">無効</c:when>
						</c:choose>
					</div>
				</div>
			</div>
			
			<div class="show-tr">
				<div class="show-th">コメント:</div>
				<div class="show-td">
					<div class="show-con">
						${room[12]}
					</div>
				</div>
			</div>
			
			<div class="show-tr">
				<div class="show-th">登録日時:</div>
				<div class="show-td">
					<div class="show-con">
						${room[13].replaceAll("([0-9]+)-([0-9]+)-([0-9]+) ([0-9]+):([0-9]+):([0-9]+)","$1年$2月$3日 $4時$5分$6秒")}
					</div>
				</div>
			</div>
			
			<div class="show-tr">
				<div class="show-th">更新日時:</div>
				<div class="show-td">
					<div class="show-con">
						${room[14].replaceAll("([0-9]+)-([0-9]+)-([0-9]+) ([0-9]+):([0-9]+):([0-9]+)","$1年$2月$3日 $4時$5分$6秒")}
					</div>
				</div>
			</div>
		</div>
		
		<div class="show_buttons">
			<form action="${pageContext.request.contextPath}/delete/room?id=${room[0]}" method="post">
				<input type="submit" id="submit_btn" class="button" value="削除">
			</form>
			<a href="${pageContext.request.contextPath}/edit/room?id=${room[0]}" class="button">編集</a>
			<a href="${pageContext.request.contextPath}/index/room" class="button">戻る</a>
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
