<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/webpage/include/taglib.jsp"%>
<html>
<head>
	<title>订单操作日志</title>
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
		                <table id="contentTable" 
		                	class="table table-striped table-bordered  table-hover table-condensed  dataTables-example dataTable no-footer">
								<tr>
									<th style="text-align: center;">时间</th>
									<th style="text-align: center;">操作人</th>
									<th style="text-align: center;">标题</th>
									<th style="text-align: center;">操作内容</th>
									<th style="text-align: center;">操作渠道</th>
									<th style="text-align: center;">操作平台</th>
								</tr>
								<c:forEach items="${page.list}" var="ordersLog">
									<tr>
										<td align="center"><fmt:formatDate value="${ordersLog.createDate}" pattern="yyyy-MM-dd HH:mm:ss"/></td>
										<td align="center">${ordersLog.createBy.name}</td>
										<td align="center">${ordersLog.title}</td>
										<td align="center">${ordersLog.content}</td>
										<td align="center">
											<c:if test="${ordersLog.channelFlag== 'wap'}">
												wap端
											</c:if>
											<c:if test="${ordersLog.channelFlag== 'ios'}">
												苹果手机
											</c:if>
											<c:if test="${ordersLog.channelFlag=='android'}">
												安卓手机
											</c:if>
											<c:if test="${ordersLog.channelFlag=='bm'}">
												后台管理
											</c:if>
										</td>
										<td align="center">
											<c:if test="${ordersLog.platformFlag== 'mtmy'}">
												每天美耶
											</c:if>
											<c:if test="${ordersLog.platformFlag== 'fzx'}">
												妃子校
											</c:if>
										</td>
									</tr>
								</c:forEach>
						</table>
					</div>
				</div>
			</div>
		</div>
	</div>
	<div class="loading"></div>
</body>
</html>