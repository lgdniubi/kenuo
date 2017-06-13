<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/webpage/include/taglib.jsp"%>
<%@ include file="/webpage/include/icheck.jsp"%>
<html>
<head>
<title>静态主页预览</title>
<meta name="decorator" content="default" />
<link rel="stylesheet" type="text/css" href="${ctxStatic}/common/training.css">
<script type="text/javascript">
	$(document).ready(function() {
		$("#contentTable").load('${goal}').getResponse().setHeader("Access-Control-Allow-Origin", "*");	
	});
 
</script>
</head>
<body>
	<div class="wrapper wrapper-content">
		<div class="ibox">
			<!-- 标题 -->
			<div class="ibox-title">
				<h5>静态主页预览</h5>
			</div>
			<!-- 主体内容 -->
			<div class="ibox-content">
				<!-- 主要内容展示 -->
				<table id="contentTable" class="table table-striped table-bordered table-hover table-condensed dataTables-example dataTable">
				<%-- <%@include file=${goal} %>  --%>
				${goal}
				</table>
			</div>
		</div>
	</div>
</body>
</html>