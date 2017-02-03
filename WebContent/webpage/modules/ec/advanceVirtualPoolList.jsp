<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/webpage/include/taglib.jsp"%>
<%@ include file="/webpage/include/icheck.jsp"%>
<html>
<head>
<title>用户管理</title>
<meta name="decorator" content="default" />
<link rel="stylesheet" type="text/css" href="${ctxStatic}/common/training.css">
</head>
<body>
	<div class="wrapper wrapper-content">
		<div class="ibox">
			<!-- 标题 -->
			<div class="ibox-title">
				<h5>预付款虚拟池</h5>
			</div>
			<!-- 主体内容 -->
			<div class="ibox-content">
				<sys:message content="${message}" />
				<!-- 查询条件 -->
				<div class="row">
					<div class="col-sm-12">
						<form:form id="searchForm" modelAttribute="advanceVirtualPool" action="${ctx}/ec/advanceVirtualPool/list" method="post" class="form-inline">
							<input id="pageNo" name="pageNo" type="hidden" value="${page.pageNo}" />
							<input id="pageSize" name="pageSize" type="hidden" value="${page.pageSize}" />
							<table:sortColumn id="orderBy" name="orderBy" value="${page.orderBy}" callback="sortOrRefresh();" />
							<!-- 支持排序 -->
						</form:form>
						<br />
					</div>
				</div>
				<div class="row">
					<div class="col-sm-12">
						<div class="pull-left">
							<shiro:hasPermission name="ec:advanceVirtualPool:export">
								<!-- 导出按钮 -->
								<table:exportExcel url="${ctx}/ec/advanceVirtualPool/export"></table:exportExcel>
							</shiro:hasPermission>
						</div>
					</div>
				</div>
				<!-- 主要内容展示 -->
				<table id="contentTable" class="table table-striped table-bordered table-hover table-condensed dataTables-example dataTable">
					<thead>
						<tr>
							
							<th style="text-align: center;">订单号</th>
							<th style="text-align: center;">虚拟订单号</th>
							<th style="text-align: center;">商品id</th>
							<th style="text-align: center;">商品名称</th>
							<th style="text-align: center;">商品规格</th>
							<th style="text-align: center;">商品编号</th>
							<th style="text-align: center;">购买数量</th>
							<th style="text-align: center;">应付款</th>
							<th style="text-align: center;">实付款</th>
							<th style="text-align: center;">欠款</th>
							<th style="text-align: center;">预计成交次数</th>
							<th style="text-align: center;">购买次数</th>
							<th style="text-align: center;">消耗次数</th>
							<th style="text-align: center;">实际消耗业绩</th>
						</tr>
					</thead>
					<tbody>
						<c:forEach items="${page.list}" var="advanceVirtualPool">
							<tr>
								
								<td style="text-align: center;">${advanceVirtualPool.orderId}</td>
								<td style="text-align: center;">${advanceVirtualPool.recId}</td>
								<td style="text-align: center;">${advanceVirtualPool.goodsId}</td>
								<td style="text-align: center;">${advanceVirtualPool.goodsName}</td>
								<td style="text-align: center;">${advanceVirtualPool.specKeyName}</td>
								<td style="text-align: center;">${advanceVirtualPool.goodsSn}</td>
								<td style="text-align: center;">${advanceVirtualPool.goodsNum}</td>
								<td style="text-align: center;">${advanceVirtualPool.orderAmount}</td>
								<td style="text-align: center;">${advanceVirtualPool.totalAmount}</td>
								<td style="text-align: center;">${advanceVirtualPool.orderArrearage}</td>
								<td style="text-align: center;">${advanceVirtualPool.serviceTimes}</td>
								<td style="text-align: center;">${advanceVirtualPool.payTimes}</td>
								<td style="text-align: center;">${advanceVirtualPool.useTimes}</td>
								<td style="text-align: center;">${advanceVirtualPool.actualUseFee}</td>
							</tr>
						</c:forEach>
					</tbody>
				</table>
				<!-- 分页table -->
				<table:page page="${page}"></table:page>
			</div>
		</div>
	</div>
</body>
</html>