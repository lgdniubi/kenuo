<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/webpage/include/taglib.jsp"%>
<html>
<head>
	<title>操作日志</title>
	<meta name="decorator" content="default"/>
</head>
<body class="gray-bg">
	<div class="wrapper wrapper-content">
		<div class="ibox">
			<sys:message content="${message}"/>
	    	<div class="ibox-content">
				<div class="tab-content" id="tab-content">
	                <div class="tab-inner">
	                	<form:form id="searchForm" modelAttribute="userOperatorLog" class="form-inline">
							<input id="pageNo" name="pageNo" type="hidden" value="${page.pageNo}" />
							<input id="pageSize" name="pageSize" type="hidden" value="${page.pageSize}" />
							<table:sortColumn id="orderBy" name="orderBy" value="${page.orderBy}" callback="sortOrRefresh();" />
							<!-- 支持排序 -->
						</form:form>
						<!-- 主要内容展示 -->
						<table id="contentTable" class="table table-striped table-bordered table-hover table-condensed dataTables-example dataTable">
							<thead>
								<tr>
									<th style="text-align: center;width: 120px" class="col-md-1">操作日期</th>
									<th style="text-align: center;" class="col-md-1">操作人</th>
									<th style="text-align: center;" class="col-md-3">操作记录</th>
								</tr>
							</thead>
							<tbody>
								<c:forEach items="${page.list}" var="log">
									<tr style="text-align: center;">
										<td class="col-md-1">
											<p>
												<fmt:formatDate value="${log.createTime}" pattern="yyyy-MM-dd  HH:mm" />
											</p>
										</td>
										<td class="col-md-1">
											<p>${log.creatorName}</p>
										</td>
										<td class="col-md-3">
											<p>${log.content}</p>
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