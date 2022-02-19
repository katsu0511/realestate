<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" import="java.io.*,java.util.*,java.text.*" %>
<jsp:include page="../common/header.jsp" flush="true"/>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
   
<main>
	<div class="container">
	
		<div class="show">
			<div class="show-tr">
				<div class="show-th">借り主ID:</div>
				<div class="show-td">
					<div class="show-con">
						${tenant[0]}
					</div>
				</div>
			</div>
			
			<div class="show-tr">
				<div class="show-th">担当者:</div>
				<div class="show-td">
					<div class="show-con">
						${rppsnm}
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
						<c:if test="${roomnu != ''}">
							${roomnu} 号室
						</c:if>
					</div>
				</div>
			</div>
			
			<div class="show-tr">
				<div class="show-th">契約日:</div>
				<div class="show-td">
					<div class="show-con">
						${tenant[5].replaceAll("([0-9]+)-([0-9]+)-([0-9]+)","$1年$2月$3日")}
					</div>
				</div>
			</div>
			
			<div class="show-tr">
				<div class="show-th">借り主名:</div>
				<div class="show-td">
					<div class="show-con">
						${tenant[6]}
					</div>
				</div>
			</div>
			
			<div class="show-tr">
				<div class="show-th">借り主名フリガナ:</div>
				<div class="show-td">
					<div class="show-con">
						${tenant[7]}
					</div>
				</div>
			</div>
			
			<div class="show-tr">
				<div class="show-th">郵便番号:</div>
				<div class="show-td">
					<div class="show-con">
						${tenant[8]}
					</div>
				</div>
			</div>
			
			<div class="show-tr">
				<div class="show-th">住所:</div>
				<div class="show-td">
					<div class="show-con">
						${tenant[9]}
						${tenant[10]}
						${tenant[11]}
						${tenant[12]}
					</div>
				</div>
			</div>
			
			<div class="show-tr">
				<div class="show-th">電話番号1:</div>
				<div class="show-td">
					<div class="show-con">
						${tenant[13]}
					</div>
				</div>
			</div>
			
			<div class="show-tr">
				<div class="show-th">電話番号2:</div>
				<div class="show-td">
					<div class="show-con">
						${tenant[14]}
					</div>
				</div>
			</div>
			
			<div class="show-tr">
				<div class="show-th">メールアドレス:</div>
				<div class="show-td">
					<div class="show-con">
						${tenant[15]}
					</div>
				</div>
			</div>
			
			<div class="show-tr">
				<div class="show-th">生年月日:</div>
				<div class="show-td">
					<div class="show-con">
						${tenant[16].replaceAll("([0-9]+)-([0-9]+)-([0-9]+)","$1年$2月$3日")}
					</div>
				</div>
			</div>
			
			<div class="show-tr">
				<div class="show-th">銀行名:</div>
				<div class="show-td">
					<div class="show-con">
						<c:if test="${tenant[17] != ''}">
							${tenant[17]}銀行
						</c:if>
					</div>
				</div>
			</div>
			
			<div class="show-tr">
				<div class="show-th">銀行支店名:</div>
				<div class="show-td">
					<div class="show-con">
						${tenant[18]}
					</div>
				</div>
			</div>
			
			<div class="show-tr">
				<div class="show-th">口座区分:</div>
				<div class="show-td">
					<div class="show-con">
						${tenant[19]}
					</div>
				</div>
			</div>
			
			<div class="show-tr">
				<div class="show-th">口座番号:</div>
				<div class="show-td">
					<div class="show-con">
						${tenant[20]}
					</div>
				</div>
			</div>
			
			<div class="show-tr">
				<div class="show-th">口座名義:</div>
				<div class="show-td">
					<div class="show-con">
						${tenant[21]}
					</div>
				</div>
			</div>
			
			<div class="show-tr">
				<div class="show-th">勤務先名:</div>
				<div class="show-td">
					<div class="show-con">
						${tenant[22]}
					</div>
				</div>
			</div>
			
			<div class="show-tr">
				<div class="show-th">勤務先名フリガナ:</div>
				<div class="show-td">
					<div class="show-con">
						${tenant[23]}
					</div>
				</div>
			</div>
			
			<div class="show-tr">
				<div class="show-th">勤務先郵便番号:</div>
				<div class="show-td">
					<div class="show-con">
						${tenant[24]}
					</div>
				</div>
			</div>
			
			<div class="show-tr">
				<div class="show-th">勤務先住所:</div>
				<div class="show-td">
					<div class="show-con">
						${tenant[25]}
						${tenant[26]}
						${tenant[27]}
						${tenant[28]}
					</div>
				</div>
			</div>
			
			<div class="show-tr">
				<div class="show-th">有効フラグ:</div>
				<div class="show-td">
					<div class="show-con">
						<c:choose>
							<c:when test="${tenant[29] == 't'}">有効</c:when>
							<c:when test="${tenant[29] == 'f'}">無効</c:when>
						</c:choose>
					</div>
				</div>
			</div>
			
			<div class="show-tr">
				<div class="show-th">コメント:</div>
				<div class="show-td">
					<div class="show-con">
						${tenant[30]}
					</div>
				</div>
			</div>
			
			<div class="show-tr">
				<div class="show-th">登録日時:</div>
				<div class="show-td">
					<div class="show-con">
						${tenant[31].replaceAll("([0-9]+)-([0-9]+)-([0-9]+) ([0-9]+):([0-9]+):([0-9]+)","$1年$2月$3日 $4時$5分$6秒")}
					</div>
				</div>
			</div>
			
			<div class="show-tr">
				<div class="show-th">更新日時:</div>
				<div class="show-td">
					<div class="show-con">
						${tenant[32].replaceAll("([0-9]+)-([0-9]+)-([0-9]+) ([0-9]+):([0-9]+):([0-9]+)","$1年$2月$3日 $4時$5分$6秒")}
					</div>
				</div>
			</div>
		</div>
		
		<div class="show_buttons">
			<form action="${pageContext.request.contextPath}/delete/tenant?id=${tenant[0]}" method="post">
				<input type="submit" id="submit_btn" class="button" value="削除">
			</form>
			<a href="${pageContext.request.contextPath}/edit/tenant?id=${tenant[0]}" class="button">編集</a>
			<a href="${pageContext.request.contextPath}/index/tenant" class="button">戻る</a>
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
