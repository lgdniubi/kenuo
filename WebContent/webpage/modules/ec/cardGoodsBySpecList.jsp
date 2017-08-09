<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/webpage/include/taglib.jsp"%>

<html>
<head>
	<title>商品规格价格列表</title>
	<meta name="decorator" content="default"/>
	<link rel="stylesheet" href="${ctxStatic}/ec/css/loading.css">
	<link rel="stylesheet" href="${ctxStatic}/ec/css/base.css">
	<script type="text/javascript">
		$(document).ready(function() {
			var suitCardSons = $("#suitCardSons").val();
			$(suitCardSons).appendTo($("#treeTable"));
		});
	</script>
</head>
<body>
	<div class="ibox-content">
		<sys:message content="${message}"/>
		<div class="warpper-content">
			<div class="ibox">
				<div class="ibox-title">
					<h5>商品规格价格</h5>
				</div>
				<input type="hidden" id="suitCardSons" value="${suitCardSons}" />
				<div class="ibox-content">
					<table id="treeTable" class="table table-striped table-bordered table-hover table-condensed dataTables-example dataTable">
					</table>
				</div>
			</div>
		</div>
	</div>
</body>
</html>