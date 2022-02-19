<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" import="java.io.*,java.util.*,java.text.*" %>
<jsp:include page="../common/header.jsp" flush="true"/>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
   
<main>
	<div class="container">
	
		<div class="show">
			<div class="show-tr">
				<div class="show-th">物件ID:</div>
				<div class="show-td">
					<div class="show-con">
						${rsdc[0]}
					</div>
				</div>
			</div>
			
			<div class="show-tr">
				<div class="show-th">大家:</div>
				<div class="show-td">
					<div class="show-con">
						${landnm}
					</div>
				</div>
			</div>
			
			<div class="show-tr">
				<div class="show-th">物件名:</div>
				<div class="show-td">
					<div class="show-con">
						${rsdc[3]}
					</div>
				</div>
			</div>
			
			<div class="show-tr">
				<div class="show-th">物件名フリガナ:</div>
				<div class="show-td">
					<div class="show-con">
						${rsdc[4]}
					</div>
				</div>
			</div>
			
			<div class="show-tr">
				<div class="show-th">郵便番号:</div>
				<div class="show-td">
					<div class="show-con">
						${rsdc[5]}
					</div>
				</div>
			</div>
			
			<div class="show-tr">
				<div class="show-th">住所:</div>
				<div class="show-td">
					<div class="show-con">
						${rsdc[6]}
						${rsdc[7]}
						${rsdc[8]}
					</div>
				</div>
			</div>
			
			<div class="show-tr">
				<div class="show-th">家賃:</div>
				<div class="show-td">
					<div class="show-con">
						<c:if test="${rsdc[9] != ''}">
							${rsdc[9]} 円
						</c:if>
					</div>
				</div>
			</div>
			
			<div class="show-tr">
				<div class="show-th">管理費:</div>
				<div class="show-td">
					<div class="show-con">
						<c:if test="${rsdc[10] != ''}">
							${rsdc[10]} 円
						</c:if>
					</div>
				</div>
			</div>
			
			<div class="show-tr">
				<div class="show-th">敷金:</div>
				<div class="show-td">
					<div class="show-con">
						<c:if test="${rsdc[11] != ''}">
							${rsdc[11]} 円
						</c:if>
					</div>
				</div>
			</div>
			
			<div class="show-tr">
				<div class="show-th">礼金:</div>
				<div class="show-td">
					<div class="show-con">
						<c:if test="${rsdc[12] != ''}">
							${rsdc[12]} 円
						</c:if>
					</div>
				</div>
			</div>
			
			<div class="show-tr">
				<div class="show-th">仲介料:</div>
				<div class="show-td">
					<div class="show-con">
						<c:if test="${rsdc[13] != ''}">
							${rsdc[13]} 円
						</c:if>
					</div>
				</div>
			</div>
			
			<div class="show-tr">
				<div class="show-th">鍵代金:</div>
				<div class="show-td">
					<div class="show-con">
						<c:if test="${rsdc[14] != ''}">
							${rsdc[14]} 円
						</c:if>
					</div>
				</div>
			</div>
			
			<c:if test="${!empty adebs}">
				<c:forEach var="adeb" items="${adebs}">
					<div class="show-tr">
						<div class="show-th">${adeb.itemnm}:</div>
						<div class="show-td">
							<div class="show-con">
								<c:if test="${adeb.adebvl != ''}">
									${adeb.adebvl} 円
								</c:if>
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
							<c:when test="${rsdc[15] == 't'}">有効</c:when>
							<c:when test="${rsdc[15] == 'f'}">無効</c:when>
						</c:choose>
					</div>
				</div>
			</div>
			
			<div class="show-tr">
				<div class="show-th">コメント:</div>
				<div class="show-td">
					<div class="show-con">
						${rsdc[16]}
					</div>
				</div>
			</div>
			
			<div class="show-tr">
				<div class="show-th">登録日時:</div>
				<div class="show-td">
					<div class="show-con">
						${rsdc[17].replaceAll("([0-9]+)-([0-9]+)-([0-9]+) ([0-9]+):([0-9]+):([0-9]+)","$1年$2月$3日 $4時$5分$6秒")}
					</div>
				</div>
			</div>
			
			<div class="show-tr">
				<div class="show-th">更新日時:</div>
				<div class="show-td">
					<div class="show-con">
						${rsdc[18].replaceAll("([0-9]+)-([0-9]+)-([0-9]+) ([0-9]+):([0-9]+):([0-9]+)","$1年$2月$3日 $4時$5分$6秒")}
					</div>
				</div>
			</div>
		</div>
		
		<div class="show_buttons">
			<form action="${pageContext.request.contextPath}/delete/residence?id=${rsdc[0]}" method="post">
				<input type="submit" id="submit_btn" class="button" value="削除">
			</form>
			<a href="${pageContext.request.contextPath}/edit/residence?id=${rsdc[0]}" class="button">編集</a>
			<a href="${pageContext.request.contextPath}/index/residence" class="button">戻る</a>
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
