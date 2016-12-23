<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/webpage/include/taglib.jsp"%>
<html>
<head>
	<title>查看明细</title>
	<meta name="decorator" content="default"/>
</head>
<body class="gray-bg">
	<div class="wrapper wrapper-content">
		<div class="ibox">
			<sys:message content="${message}"/>
	    	<div class="ibox-content">
				<div class="tab-content" id="tab-content">
	                <div class="tab-inner">
	                	<form:form id="searchForm" action="${ctx}/ec/mtmySale/findUserAllOrder" method="post" class="form-inline">
							<input id="pageNo" name="pageNo" type="hidden" value="${page.pageNo}" />
							<input id="pageSize" name="pageSize" type="hidden" value="${page.pageSize}" />
							<table:sortColumn id="orderBy" name="orderBy" value="${page.orderBy}" callback="sortOrRefresh();" />
							<!-- 支持排序 -->
							<input id="rabateDate" name="rabateDate" type="hidden" value="${newmtmySaleRelieveLog.rabateDate}"/>
							<input id="receiveUser" name="receiveUser" type="hidden" value="${newmtmySaleRelieveLog.receiveUser }" />
						</form:form>
						<!-- 主要内容展示 -->
						<table id="contentTable" class="table table-striped table-bordered table-hover table-condensed dataTables-example dataTable">
							<thead>
								<tr>
									<th>时间</th>
									<th>姓名</th>
									<th>级别</th>
									<th>贡献可用余额</th>
									<th>贡献结算中余额</th>
									<th>贡献可用云币</th>
									<th>贡献结算中云币</th>
									<th>操作</th>
								</tr>
							</thead>
							<tbody>
								<c:forEach items="${page.list}" var="mtmySaleRelieveLog">
									<tr>
										<td><fmt:formatDate value="${mtmySaleRelieveLog.createDate}" pattern="yyyy-MM-dd HH:mm:ss"/></td>
										<td>${mtmySaleRelieveLog.users.nickname}</td>
										<td>${mtmySaleRelieveLog.users.layer}</td>
										<c:if test="${newmtmySaleRelieveLog.rebateFlag == 0}">
											<td>0</td>
											<td><fmt:formatNumber type="number" value="${mtmySaleRelieveLog.balanceAmount}" maxFractionDigits="2"/></td>
											<td>0</td>
											<td><fmt:formatNumber type="number" value="${mtmySaleRelieveLog.integralAmount}" maxFractionDigits="2"/></td>
										</c:if>
										<c:if test="${newmtmySaleRelieveLog.rebateFlag == 1}">
											<td><fmt:formatNumber type="number" value="${mtmySaleRelieveLog.balanceAmount}" maxFractionDigits="2"/></td>
											<td>0</td>
											<td><fmt:formatNumber type="number" value="${mtmySaleRelieveLog.integralAmount}" maxFractionDigits="2"/></td>
											<td>0</td>
										</c:if>
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