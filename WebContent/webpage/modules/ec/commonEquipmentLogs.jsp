<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/webpage/include/taglib.jsp"%>
<html>
<head>
	<title>通用设备操作日志</title>
	<meta name="decorator" content="default"/>
	<link rel="stylesheet" href="${ctxStatic}/train/css/exam.css">
</head>
<body class="gray-bg">
	<div class="wrapper wrapper-content">
		<div class="ibox">
			<sys:message content="${message}"/>
	    	<div class="ibox-content">
				<div class="tab-content" id="tab-content">
	                <div class="tab-inner">
		                <form:form id="searchForm" action="${ctx}/specEquipment/editLog" style="width: 100%;" modelAttribute="goods" method="post" class="navbar-form navbar-left searcharea">
							<input id="comEquipmentId" name="comEquipmentId" value="${comEquipmentId}" type="hidden">
		               		<!-- 分页必要字段 -->
							<input id="pageNo" name="pageNo" type="hidden" value="${page.pageNo}"/>
							<input id="pageSize" name="pageSize" type="hidden" value="${page.pageSize}"/>
		                </form:form>
		                <table id="contentTable" class="table table-striped table-bordered  table-hover table-condensed  dataTables-example dataTable no-footer">
								<tr>
									<th style="text-align: center;">操作人</th>
									<th style="text-align: center;">操作时间</th>
									<th style="text-align: center;">相关内容</th>
								</tr>
								<c:forEach items="${page.list}" var="equipmentLogs">
									<tr>
										<td align="center">${equipmentLogs.createBy.name}</td>
										<td align="center"><fmt:formatDate value="${equipmentLogs.createDate}" pattern="yyyy-MM-dd HH:mm:ss"/></td>
										<td align="center">${equipmentLogs.content}</td>
								</c:forEach>
						</table>
						<!-- 分页代码 -->
						<table:page page="${page}"></table:page>
					</div>
				</div>
			</div>
		</div>
	</div>
	<div class="loading"></div>
</body>
</html>