<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/webpage/include/taglib.jsp"%>
<html>
<head>
	<title>开闭店操作日志</title>
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
		                <form:form id="searchForm" action="${ctx}/sys/office/shopLogs" style="width: 100%;" method="post" class="navbar-form navbar-left searcharea">
							<input id="officeId" name="officeId" value="${officeId}" type="hidden">
		               		<!-- 分页必要字段 -->
							<input id="pageNo" name="pageNo" type="hidden" value="${page.pageNo}"/>
							<input id="pageSize" name="pageSize" type="hidden" value="${page.pageSize}"/>
		                </form:form>
		                <table id="contentTable" class="table table-striped table-bordered  table-hover table-condensed  dataTables-example dataTable no-footer">
								<tr>
									<th style="text-align: center;">日志类型</th>
									<th style="text-align: center;">修改内容</th>
									<th style="text-align: center;">修改人</th>
									<th style="text-align: center;">系统记录时间</th>
									<th style="text-align: center;">人工修正时间</th>
									<th style="text-align: center;">最终时间</th>
								</tr>
								<c:forEach items="${page.list}" var="officeLog">
									<tr>
										<td align="center">
											<c:if test="${officeLog.type == 0}">创建</c:if>	
											<c:if test="${officeLog.type == 1}">删除</c:if>	
											<c:if test="${officeLog.type == 2}">开店</c:if>	
											<c:if test="${officeLog.type == 3}">关店</c:if>									
										</td>
										<td align="center">${officeLog.content}</td>
										<td align="center">${officeLog.updateBy.name}</td>
										<td align="center"><fmt:formatDate value="${officeLog.updateDate}" pattern="yyyy-MM-dd HH:mm:ss"/></td>
										<td align="center"><fmt:formatDate value="${officeLog.realtime}" pattern="yyyy-MM-dd HH:mm:ss"/></td>
										<td align="center"><fmt:formatDate value="${officeLog.finalTime}" pattern="yyyy-MM-dd HH:mm:ss"/></td>
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