<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/webpage/include/taglib.jsp"%>

<html>
<head>
<title>交易记录列表</title>
<meta name="decorator" content="default" />

<script type="text/javascript">
	function page(n, s) {
		$("#pageNo").val(n);
		$("#pageSize").val(s);
		$("#searchForm").submit();
		return false;
	}
</script>
</head>
<body class="gray-bg">
	<div class="wrapper wrapper-content">
		<div class="ibox">
			<div class="ibox-title">
				<h5>交易记录</h5>
			</div>
			<!-- 查询条件 -->
			<div class="ibox-content">
				<div class="clearfix">
					<form:form id="searchForm" modelAttribute="tradingLog"
						action="${ctx}/ec/tradinglog/tradinglist" method="post"
						class="form-inline">
						<input id="pageNo" name="pageNo" type="hidden"
							value="${page.pageNo}" />
						<input id="pageSize" name="pageSize" type="hidden"
							value="${page.pageSize}" />

						<div class="form-group">

							<label>用户名：</label>
							<form:input path="userName" htmlEscape="false" maxlength="50"
								style="width:200px;" class="form-control" />

						</div>
					</form:form>
					<!-- 工具栏 -->
					<div class="row">
						<div class="col-sm-12">
							<!-- 							<div class="pull-left"> -->
							<!-- 							</div> -->
							<div class="pull-right">
								<button class="btn btn-primary btn-rounded btn-outline btn-sm "
									onclick="search()">
									<i class="fa fa-search"></i> 查询
								</button>
								<button class="btn btn-primary btn-rounded btn-outline btn-sm "
									onclick="reset()">
									<i class="fa fa-refresh"></i> 重置
								</button>
							</div>
						</div>
					</div>
				</div>
				<table id="contentTable"
					class="table table-striped table-bordered  table-hover table-condensed  dataTables-example dataTable no-footer">
					<thead>
						<tr>
							<th style="text-align: center;">编号</th>
							<th style="text-align: center;">用户名</th>
							<th style="text-align: center;">金额</th>
							<th style="text-align: center;">收入/支出</th>
							<th style="text-align: center;">途径</th>
							<th style="text-align: center;">订单号</th>
							<th style="text-align: center;">交易日期</th>
						</tr>
					</thead>
					<tbody style="text-align: center;">
						<c:forEach items="${page.list}" var="list" varStatus="status">
							<tr>
								<td>${list.id}</td>
								<td>${list.userName}</td>
								<td>${list.money}</td>
								<c:if test="${list.status==1}">
									<td>收入</td>
								</c:if>
								<c:if test="${list.status==2}">
									<td>支出</td>
								</c:if>				
								<td>${list.type}</td>
								<td>${list.orderId}</td>
								<td><fmt:formatDate value="${list.tradingTime}"
										pattern="yyyy-MM-dd HH:mm:ss" /></td>

							</tr>
						</c:forEach>
					</tbody>
					<tfoot>
						<tr>
							<td colspan="20">
								<!-- 分页代码 -->
								<div class="tfoot">
									<table:page page="${page}"></table:page>
								</div>

							</td>
						</tr>
					</tfoot>
				</table>
			</div>
		</div>
	</div>


</body>
</html>