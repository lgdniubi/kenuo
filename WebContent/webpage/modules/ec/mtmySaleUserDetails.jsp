<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/webpage/include/taglib.jsp"%>
<html>
<head>
	<title>查看记录</title>
	<meta name="decorator" content="default"/>
</head>
<body class="gray-bg">
	<div class="wrapper wrapper-content">
		<div class="ibox">
			<sys:message content="${message}"/>
	    	<div class="ibox-content">
				<div class="tab-content" id="tab-content">
	                <div class="tab-inner">
	                	<form:form id="searchForm" modelAttribute="mtmySaleRelieve" action="${ctx}/ec/mtmySale/userDetails" method="post" class="form-inline">
							<input id="pageNo" name="pageNo" type="hidden" value="${page.pageNo}" />
							<input id="pageSize" name="pageSize" type="hidden" value="${page.pageSize}" />
							<table:sortColumn id="orderBy" name="orderBy" value="${page.orderBy}" callback="sortOrRefresh();" />
							<!-- 支持排序 -->
							<input id="users.nickname" name="users.nickname" type="hidden" value="${mtmySaleRelieveLog.users.nickname}" />
							<input id="rebateUser" name="rebateUser" type="hidden" value="${mtmySaleRelieveLog.rebateUser}" />
							<input id="receiveUser" name="receiveUser" type="hidden" value="${mtmySaleRelieveLog.receiveUser}" />
						</form:form>
						<!-- 主要内容展示 -->
						<table id="contentTable" class="table table-striped table-bordered table-hover table-condensed dataTables-example dataTable">
							<thead>
								<tr>
									<th colspan="5">${mtmySaleRelieveLog.users.nickname}</th>
								</tr>
								<tr>
									<th>时间</th>
									<th>实付金额</th>
									<th>贡献余额</th>
									<th>贡献云币</th>
									<th>操作</th>
								</tr>
							</thead>
							<tbody>
								<c:forEach items="${page.list}" var="mtmySaleRelieveLog">
									<tr>
										<td><fmt:formatDate value="${mtmySaleRelieveLog.createDate}" pattern="yyyy-MM-dd HH:mm:ss"/></td>
										<td><fmt:formatNumber type="number" value="${mtmySaleRelieveLog.orders.orderamount}" maxFractionDigits="2"/></td>
										<td><fmt:formatNumber type="number" value="${mtmySaleRelieveLog.balanceAmount}" maxFractionDigits="2"/></td>
										<td><fmt:formatNumber type="number" value="${mtmySaleRelieveLog.integralAmount}" maxFractionDigits="2"/></td>
										<td>
											<a href="#" onclick="openDialogView('查看订单详情', '${ctx}/ec/orders/goodslist?orderid=${mtmySaleRelieveLog.orders.orderid}','750px', '600px')" class="btn btn-info btn-xs" ><i class="fa fa-search-plus"></i> 订单详情</a>
										</td>
									</tr>
								</c:forEach>
							</tbody>
						</table>
						<!-- 分页table -->
						<table:page page="${page}"></table:page>
					</div>
				</div>
			</div>
		</div>
	</div>
</body>
</html>