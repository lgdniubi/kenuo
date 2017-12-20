<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/webpage/include/taglib.jsp"%>
<html>
	<head>
		<title>跳转到客服反馈与建议页面</title>
		<meta name="decorator" content="default"/>
		<script type="text/javascript">
		</script>
	</head>
	<body>
		<div style="margin-top: 50px;margin-left: 50px;">
			<a id="alink" style="font-size: 30px;font-weight: bold;" target="_blank" href="${kefuFeedbackSuggestUrl }">进入到客服反馈与建议页面</a>
		</div>
		
		<script>
			window.onload = function() {
				document.getElementById("alink").click();
			}
		</script>
	</body>
</html>
