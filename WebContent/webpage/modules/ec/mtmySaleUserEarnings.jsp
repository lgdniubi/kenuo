<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/webpage/include/taglib.jsp"%>
<html>
<head>
	<title>收益明细</title>
	<meta name="decorator" content="default"/>
</head>
<body class="gray-bg">
	<div class="wrapper wrapper-content">
		<div class="ibox">
			<sys:message content="${message}"/>
	    	<div class="ibox-content">
				<div class="tab-content" id="tab-content">
	                <div class="tab-inner">
	                	<form:form id="searchForm" action="${ctx}/ec/mtmySale/userEarnings" method="post" class="form-inline">
							<input id="pageNo" name="pageNo" type="hidden" value="${page.pageNo}" />
							<input id="pageSize" name="pageSize" type="hidden" value="${page.pageSize}" />
							<table:sortColumn id="orderBy" name="orderBy" value="${page.orderBy}" callback="sortOrRefresh();" />
							<!-- 支持排序 -->
							<input id="receiveUser" name="receiveUser" type="hidden" value="${newMtmySaleRelieveLog.receiveUser }" />
						</form:form>
						<!-- 主要内容展示 -->
						<table id="contentTable" class="table table-striped table-bordered table-hover table-condensed dataTables-example dataTable">
							<thead>
								<tr>
									<th>时间</th>
									<th>可用金额</th>
									<th>结算中余额</th>
									<th>可用云币</th>
									<th>结算中云币</th>
									<th>操作</th>
								</tr>
							</thead>
							<tbody>
								<c:forEach items="${page.list}" var="mtmySaleRelieveLog">
									<tr><!-- ${mtmySaleRelieveLog.rabateDate} -->
										<td>${fn:substring(mtmySaleRelieveLog.rabateDate, 0, 4)}-${fn:substring(mtmySaleRelieveLog.rabateDate, 4, 6)}-${fn:substring(mtmySaleRelieveLog.rabateDate, 6, 8)}</td>
										<c:if test="${mtmySaleRelieveLog.rebateFlag == 0}">
											<td>0</td>
											<td><fmt:formatNumber type="number" value="${mtmySaleRelieveLog.balanceAmount}" maxFractionDigits="2"/></td>
											<td>0</td>
											<td><fmt:formatNumber type="number" value="${mtmySaleRelieveLog.integralAmount}" maxFractionDigits="2"/></td>
										</c:if>
										<c:if test="${mtmySaleRelieveLog.rebateFlag == 1}">
											<td><fmt:formatNumber type="number" value="${mtmySaleRelieveLog.balanceAmount}" maxFractionDigits="2"/></td>
											<td>0</td>
											<td><fmt:formatNumber type="number" value="${mtmySaleRelieveLog.integralAmount}" maxFractionDigits="2"/></td>
											<td>0</td>
										</c:if>
										<td>
											<a href="#" onclick="openDialogView('查看明细', '${ctx}/ec/mtmySale/findUserAllOrder?rabateDate=${mtmySaleRelieveLog.rabateDate}&receiveUser=${newMtmySaleRelieveLog.receiveUser }&rebateFlag=${mtmySaleRelieveLog.rebateFlag }','750px', '600px')" class="btn btn-info btn-xs" ><i class="fa fa-search-plus"></i> 查看明细</a>
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