<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/webpage/include/taglib.jsp"%>
<html>
<head>
	<title>活动商品</title>
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
		                <form:form id="searchForm" action="${ctx}/ec/action/actionGoods" style="width: 100%;" modelAttribute="goods" method="post" class="navbar-form navbar-left searcharea">
		               		<input id="actionId" name="actionId" type="hidden" value="${actionId}"/>
		               		<!-- 分页必要字段 -->
							<input id="pageNo" name="pageNo" type="hidden" value="${page.pageNo}"/>
							<input id="pageSize" name="pageSize" type="hidden" value="${page.pageSize}"/>
		                </form:form>
		                <table id="contentTable" class="table table-striped table-bordered  table-hover table-condensed  dataTables-example dataTable no-footer">
								<tr>
									<th style="text-align: center;">商品ID</th>
									<th style="text-align: center;">商品名称</th>
									<th style="text-align: center;">限购数量</th>
									<th style="text-align: center;">移除人</th>
									<th style="text-align: center;">移除时间</th>
								</tr>
								<c:forEach items="${page.list}" var="goods">
									<tr>
										<td align="center">${goods.goodsId}</td>
										<td align="center">${goods.goodsName}</td>
										<td align="center">${goods.limitNum}</td>
										<td align="center">${goods.createBy.name}</td>
										<td align="center"><fmt:formatDate value="${goods.createDate}" pattern="yyyy-MM-dd HH:mm:ss"/></td>
									</tr>
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