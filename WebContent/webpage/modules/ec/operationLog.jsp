<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/webpage/include/taglib.jsp"%>
<%@ include file="/webpage/include/icheck.jsp"%>
<html>
<head>
	<title>订单业务员操作日志</title>
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
	                <form:form id="searchForm" modelAttribute="orders" action="${ctx}/ec/orders/operationLog" method="post" class="form-inline">
						<input id="pageNo" name="pageNo" type="hidden" value="${page.pageNo}"/>
						<input id="pageSize" name="pageSize" type="hidden" value="${page.pageSize}"/>
						<input id="turnOverDetailsId" name="turnOverDetailsId" type="hidden" value="${turnOverDetailsId}"/>
					</form:form>
						<table class="table table-bordered  table-condensed dataTables-example dataTable no-footer">
							<tr>
								<th style="text-align: center;">操作时间</th>
								<th style="text-align: center;">操作人</th>
								<th style="text-align: center;">业务员</th>
								<th style="text-align: center;">部门</th>
								<th style="text-align: center;">手机号</th>
								<th style="text-align: center;">营业额</th>
							</tr>
							<c:forEach items="${page.list}" var="orderPushmoneyRecord">
								<tr>
									<td align="center"><fmt:formatDate value="${orderPushmoneyRecord.createDate}" pattern="yyyy-MM-dd HH:mm:ss"/></td>
									<td align="center">${orderPushmoneyRecord.createBy.name}</td>
									<td align="center">${orderPushmoneyRecord.pushmoneyUserName}</td>
									<td align="center">${orderPushmoneyRecord.departmentName}</td>
									<td align="center">${orderPushmoneyRecord.pushmoneyUserMobile}</td>
									<td align="center">${orderPushmoneyRecord.pushMoney}</td>
								</tr>
							</c:forEach>
						</table>
						 <table:page page="${page}"></table:page>
						</div>
					</div>
				</div>
			</div>
		</div>
	<div class="loading" id="loading"></div>
</body>
</html>