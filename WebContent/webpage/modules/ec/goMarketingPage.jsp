<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/webpage/include/taglib.jsp"%>
<html>
	<head>
		<title>客服营销工具页面</title>
		<meta name="decorator" content="default"/>
		<script type="text/javascript">
		</script>
	</head>
	<body>
		<div style="margin-top: 50px;margin-left: 50px;">
			<a id="alink" style="font-size: 30px;font-weight: bold;" target="_blank" href="${ctx}/ec/marketing/goMarketSystem">进入营销工具系统</a>
		</div>
		
		<script>
			window.onload = function() {
				document.getElementById("alink").click();
			}
		</script>
	</body>
</html>
