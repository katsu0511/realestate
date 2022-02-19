<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" import="java.io.*,java.util.*,java.text.*" %>
<jsp:include page="../common/header.jsp" flush="true"/>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<main>
	<div class="container">
	
		<div class="show">
			<div class="show-tr">
				<div class="show-th">追加項目番ID:</div>
				<div class="show-td">
					<div class="show-con">
						${adit[0]}
					</div>
				</div>
			</div>
			
			<div class="show-tr">
				<div class="show-th">追加項目表示名:</div>
				<div class="show-td">
					<div class="show-con">
						${adit[2]}
					</div>
				</div>
			</div>
			
			<div class="show-tr">
				<div class="show-th">追加項目ID名:</div>
				<div class="show-td">
					<div class="show-con">
						${adit[3]}
					</div>
				</div>
			</div>
			
			<div class="show-tr">
				<div class="show-th">最大桁数:</div>
				<div class="show-td">
					<div class="show-con">
						${adit[4]}
					</div>
				</div>
			</div>
			
			<div class="show-tr">
				<div class="show-th">桁数指定:</div>
				<div class="show-td">
					<div class="show-con">
						${adit[5]}
					</div>
				</div>
			</div>
			
			<div class="show-tr">
				<div class="show-th">コメント:</div>
				<div class="show-td">
					<div class="show-con">
						${adit[6]}
					</div>
				</div>
			</div>
			
			<div class="show-tr">
				<div class="show-th">登録日時:</div>
				<div class="show-td">
					<div class="show-con">
						${adit[7].replaceAll("([0-9]+)-([0-9]+)-([0-9]+) ([0-9]+):([0-9]+):([0-9]+)","$1年$2月$3日 $4時$5分$6秒")}
					</div>
				</div>
			</div>
			
			<div class="show-tr">
				<div class="show-th">更新日時:</div>
				<div class="show-td">
					<div class="show-con">
						${adit[8].replaceAll("([0-9]+)-([0-9]+)-([0-9]+) ([0-9]+):([0-9]+):([0-9]+)","$1年$2月$3日 $4時$5分$6秒")}
					</div>
				</div>
			</div>
		</div>
		
		<div class="show_buttons">
			<form action="${pageContext.request.contextPath}/delete/additional_item?id=${adit[0]}" method="post">
				<input type="submit" id="submit_btn" class="button" value="削除">
			</form>
			<a href="${pageContext.request.contextPath}/edit/additional_item?id=${adit[0]}" class="button">編集</a>
			<a href="${pageContext.request.contextPath}/index/additional_item" class="button">戻る</a>
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
