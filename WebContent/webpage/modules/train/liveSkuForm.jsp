<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/webpage/include/taglib.jsp"%>

<html>
<head>
<title>配置列表</title>
<meta name="decorator" content="default" />

<script type="text/javascript">
	function page(n, s) {
		$("#pageNo").val(n);
		$("#pageSize").val(s);
		$("#searchForm").submit();
		return false;
	}
	
	
	
	
</script>
</head>




<body class="gray-bg">
	<div class="wrapper wrapper-content">
		<div class="ibox">
			<div class="ibox-title">
				<h5>配置列表</h5>
			</div>
			<sys:message content="${message}" />
			<!-- 查询条件 -->
			<div class="ibox-content">
				<div class="clearfix">
					<form:form id="searchForm" modelAttribute="TrainLiveSku" action="${ctx}/train/live/liveSkuform" method="post" class="form-inline">
						<input id="pageNo" name="pageNo" type="hidden" value="${page.pageNo}"/>
						<input id="pageSize" name="pageSize" type="hidden" value="${page.pageSize}"/>
					</form:form>
				</div>
				<p></p>
				<table id="contentTable"
					class="table table-striped table-bordered  table-hover table-condensed  dataTables-example dataTable no-footer">
					<thead>
						<tr>
							<th style="text-align: center;">编号</th>
							<th style="text-align: center;">价格</th>
							<th style="text-align: center;">有效期</th>
							<th style="text-align: center;">类型</th>
						</tr>
					</thead>
					<tbody style="text-align: center;">
						<c:forEach items="${page.list}" var="sku">
							<tr>
								<td>${sku.trainLiveSkuId}</td>
								<td>${sku.price}</td>
								<td>${sku.num}</td>
								<td>
									<c:if test="${sku.type == 1}">直播</c:if>
									<c:if test="${sku.type == 2}">回放</c:if>
								</td>
							</tr>
						</c:forEach>
					</tbody>
					<tfoot>
						<tr>
							<td colspan="20">
								<!-- 分页代码 -->
								<div class="tfoot">
									<table:page page="${page}"></table:page>
								</div>
							</td>
						</tr>
					</tfoot>
				</table>
			</div>
		</div>
	</div>
</body>
</html>