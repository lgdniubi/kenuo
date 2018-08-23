<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/webpage/include/taglib.jsp"%>
<html>
<head>
	<title>权益日志列表</title>
	<meta name="decorator" content="default"/>
</head>
<body>
	<div class="ibox-content">
		<div class="warpper-content">
			<div class="ibox">
				<div class="ibox-title">
					<h5>权益日志列表</h5>
				</div>
				<div class="ibox-content">
					<div class="searcharea clearfix">
						<form:form id="searchForm"  action="${ctx}/train/userCheck/log" method="post" class="navbar-form navbar-left searcharea">
							<input id="pageNo" name="pageNo" type="hidden" value="${page.pageNo}" />
							<input id="pageSize" name="pageSize" type="hidden" value="${page.pageSize}" />
						</form:form>
					</div>
					<table id="treeTable" class="table table-striped table-bordered table-hover table-condensed dataTables-example dataTable">
						<thead>
							<tr>
								<th width="120" style="text-align: center;">序号</th>
								<th width="230" style="text-align: center;">版本类型</th>
								<th width="120" style="text-align: center;">支付类型</th>
								<th width="230" style="text-align: center">折扣</th>
								<th width="230" style="text-align: center;">开始时间</th>
								<th width="230" style="text-align: center;">到期时间</th>
								<th width="230" style="text-align: center;">操作人</th>
								<th width="300" style="text-align: center;">操作日期</th>
							</tr>
						</thead>
						<tbody>
							<c:forEach items="${page.list}" var="log">
								<tr id="${log.id}" >
									<td>${log.id }</td>
									<td>
										<c:choose>
											<c:when test="${log.modid eq '3' }">手艺人免费</c:when>
											<c:when test="${log.modid eq '4' }">手艺人收费</c:when>
											<c:when test="${log.modid eq '5' }">企业标准</c:when>
											<c:when test="${log.modid eq '6' }">企业高级</c:when>
											<c:when test="${log.modid eq '7' }">企业旗舰</c:when>
										</c:choose>
									</td>
									<td>
										<c:choose>
											<c:when test="${log.paytype eq '0'}">线下</c:when>
											<c:when test="${log.paytype eq '1' }">线上</c:when>
										</c:choose>
									</td>
									<td>${log.discount}</td>
									<td><fmt:formatDate value="${log.authStartDate}" pattern="yyyy-MM-dd"/> </td>
									<td><fmt:formatDate value="${log.authEndDate}" pattern="yyyy-MM-dd"/> </td>
									<td>${log.createBy.name}</td>
									<td><fmt:formatDate value="${log.createDate}" pattern="yyyy-MM-dd"/> </td>
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
</body>
</html>