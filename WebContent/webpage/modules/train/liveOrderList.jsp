<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/webpage/include/taglib.jsp"%>

<html>
<head>
<title>直播订单列表</title>
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
				<h5>直播订单列表</h5>
			</div>
			<sys:message content="${message}" />
			<!-- 查询条件 -->
			<div class="ibox-content">
				<div class="clearfix">
					<form:form id="searchForm" modelAttribute="TrainLiveOrder" action="${ctx}/train/live/liveOrderList" method="post" class="form-inline">
						<input id="pageNo" name="pageNo" type="hidden" value="${page.pageNo}"/>
						<input id="pageSize" name="pageSize" type="hidden" value="${page.pageSize}"/>
					</form:form>
				</div>
				<p></p>
				<table id="contentTable"
					class="table table-striped table-bordered  table-hover table-condensed  dataTables-example dataTable no-footer">
					<thead>
						<tr>
							<th style="text-align: center;">订单ID</th>
							<th style="text-align: center;">直播申请ID</th>
							<th style="text-align: center;">用户名</th>
							<th style="text-align: center;">直播规格ID</th>
							<th style="text-align: center;">天数</th>
							<th style="text-align: center;">实付金额</th>
							<th style="text-align: center;">类型</th>
							<th style="text-align: center;">添加时间</th>
							<th style="text-align: center;">支付时间</th>
							<th style="text-align: center;">有效时间</th>
							<th style="text-align: center;">支付状态</th>
						</tr>
					</thead>
					<tbody style="text-align: center;">
						<c:forEach items="${page.list}" var="order">
							<tr>
								<td>${order.trainLiveOrderId}</td>
								<td>${order.auditId}</td>
								<td>${order.userName}</td>
								<td>${order.specId}</td>
								<td>${order.specNum}</td>
								<td>${order.specPrice}</td>
								<td>
									<c:if test="${order.type == 1}">直播</c:if>
									<c:if test="${order.type == 2}">回放</c:if>
								</td>
								<td><fmt:formatDate value="${order.createDate}" pattern="yyyy-MM-dd HH:mm:ss"/></td>
								<td><fmt:formatDate value="${order.payDate}" pattern="yyyy-MM-dd HH:mm:ss"/></td>
								<td><fmt:formatDate value="${order.validDate}" pattern="yyyy-MM-dd HH:mm:ss"/></td>
								<td>
									<c:if test="${order.orderStatus == 1}">取消订单</c:if>
									<c:if test="${order.orderStatus == 2}">待支付</c:if>
									<c:if test="${order.orderStatus == 3}">已付款</c:if>
									<c:if test="${order.orderStatus == 4}">已退款</c:if>
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