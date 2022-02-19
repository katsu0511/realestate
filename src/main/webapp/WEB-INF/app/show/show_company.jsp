<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" import="java.io.*,java.util.*,java.text.*" %>
<jsp:include page="../common/header.jsp" flush="true"/>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>

<main>
	<div class="container">
	
		<div class="show">
			<div class="show-tr">
				<div class="show-th">自社ID:</div>
				<div class="show-td">
					<div class="show-con">
						${cmpn[0]}
					</div>
				</div>
			</div>
			
			<div class="show-tr">
				<div class="show-th">自社名:</div>
				<div class="show-td">
					<div class="show-con">
						${cmpn[1]}
					</div>
				</div>
			</div>
			
			<div class="show-tr">
				<div class="show-th">自社名フリガナ:</div>
				<div class="show-td">
					<div class="show-con">
						${cmpn[2]}
					</div>
				</div>
			</div>
			
			<div class="show-tr">
				<div class="show-th">代表者名:</div>
				<div class="show-td">
					<div class="show-con">
						${cmpn[3]}
					</div>
				</div>
			</div>
			
			<div class="show-tr">
				<div class="show-th">本支店名:</div>
				<div class="show-td">
					<div class="show-con">
						${cmpn[4]}
					</div>
				</div>
			</div>
			
			<div class="show-tr">
				<div class="show-th">ユーザー名:</div>
				<div class="show-td">
					<div class="show-con">
						${cmpn[5]}
					</div>
				</div>
			</div>
			
			<div class="show-tr">
				<div class="show-th">パスワード:</div>
				<div class="show-td">
					<div class="show-con">
						<c:set var="pass">${cmpn[6]}</c:set>
						<c:forEach begin="1" end="${fn:length(pass)}"> ● </c:forEach>
					</div>
				</div>
			</div>
			
			<div class="show-tr">
				<div class="show-th">郵便番号:</div>
				<div class="show-td">
					<div class="show-con">
						${cmpn[7]}
					</div>
				</div>
			</div>
			
			<div class="show-tr">
				<div class="show-th">住所:</div>
				<div class="show-td">
					<div class="show-con">
						${cmpn[8]}
						${cmpn[9]}
						${cmpn[10]}
						${cmpn[11]}
					</div>
				</div>
			</div>
			
			<div class="show-tr">
				<div class="show-th">電話番号1:</div>
				<div class="show-td">
					<div class="show-con">
						${cmpn[12]}
					</div>
				</div>
			</div>
			
			<div class="show-tr">
				<div class="show-th">電話番号2:</div>
				<div class="show-td">
					<div class="show-con">
						${cmpn[13]}
					</div>
				</div>
			</div>
			
			<div class="show-tr">
				<div class="show-th">銀行名:</div>
				<div class="show-td">
					<div class="show-con">
						<c:if test="${cmpn[14] != ''}">
							${cmpn[14]}銀行
						</c:if>
					</div>
				</div>
			</div>
			
			<div class="show-tr">
				<div class="show-th">銀行支店名:</div>
				<div class="show-td">
					<div class="show-con">
						${cmpn[15]}
					</div>
				</div>
			</div>
			
			<div class="show-tr">
				<div class="show-th">口座区分:</div>
				<div class="show-td">
					<div class="show-con">
						${cmpn[16]}
					</div>
				</div>
			</div>
			
			<div class="show-tr">
				<div class="show-th">口座番号:</div>
				<div class="show-td">
					<div class="show-con">
						${cmpn[17]}
					</div>
				</div>
			</div>
			
			<div class="show-tr">
				<div class="show-th">口座名義:</div>
				<div class="show-td">
					<div class="show-con">
						${cmpn[18]}
					</div>
				</div>
			</div>
			
			<div class="show-tr">
				<div class="show-th">コメント:</div>
				<div class="show-td">
					<div class="show-con">
						${cmpn[19]}
					</div>
				</div>
			</div>
			
			<div class="show-tr">
				<div class="show-th">登録日時:</div>
				<div class="show-td">
					<div class="show-con">
						${cmpn[20].replaceAll("([0-9]+)-([0-9]+)-([0-9]+) ([0-9]+):([0-9]+):([0-9]+)","$1年$2月$3日 $4時$5分$6秒")}
					</div>
				</div>
			</div>
			
			<div class="show-tr">
				<div class="show-th">更新日時:</div>
				<div class="show-td">
					<div class="show-con">
						${cmpn[21].replaceAll("([0-9]+)-([0-9]+)-([0-9]+) ([0-9]+):([0-9]+):([0-9]+)","$1年$2月$3日 $4時$5分$6秒")}
					</div>
				</div>
			</div>
		</div>
		
		<div class="show_buttons">
			<form action="${pageContext.request.contextPath}/delete/company" method="post">
				<input type="submit" id="delete_btn" class="button" value="削除">
			</form>
			<a href="${pageContext.request.contextPath}/edit/company" class="button">編集</a>
			<a href="${pageContext.request.contextPath}/top" class="button">戻る</a>
		</div>
		
	</div>
</main>

<script type="text/javascript">
$(function() {
	$('#delete_btn').click(function() {
		if (!confirm('削除しますか？')) {
			return false;
		} else {
			if (!confirm('本当に削除しますか？')) {
				return false;
			}
		}
	});
});
</script>

<jsp:include page="../common/footer.jsp" flush="true"/>
