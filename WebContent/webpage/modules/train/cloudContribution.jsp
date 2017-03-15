<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/webpage/include/taglib.jsp"%>

<html>
<head>
<title>云币贡献榜</title>
<meta name="decorator" content="default" />

<script type="text/javascript">
	/* function page(n, s) {
		$("#pageNo").val(n);
		$("#pageSize").val(s);
		$("#searchForm").submit();
		return false;
	}
	 */
</script>
</head>




<body class="gray-bg">
	<div class="wrapper wrapper-content">
		<div class="ibox">
			<div class="ibox-title">
				<h5>云币贡献榜</h5>
			</div>
			<sys:message content="${message}" />
			<!-- 查询条件 -->
			<div class="ibox-content">
				<div class="clearfix">
					<form:form id="searchForm" modelAttribute="trainLiveAudit" action="${ctx}/train/live/cloudContribution" method="post" class="form-inline">
						<input id="pageNo" name="pageNo" type="hidden" value="${page.pageNo}"/>
						<input id="pageSize" name="pageSize" type="hidden" value="${page.pageSize}"/>
						<input id="auditId" name="auditId" type="hidden" value="${trainLiveRewardRecord.auditId}">
					</form:form>
				</div>
				<p></p>
				<table id="contentTable"
					class="table table-striped table-bordered  table-hover table-condensed  dataTables-example dataTable no-footer">
					<thead>
						<tr>
							<th style="text-align: center;">排名</th>
							<th style="text-align: center;">姓名</th>
							<th style="text-align: center;">手机号</th>
							<th style="text-align: center;">归属机构</th>
							<th style="text-align: center;">职位</th>
							<th style="text-align: center;">贡献云币数量</th>
						</tr>
					</thead>
					<tbody style="text-align: center;">
						<c:forEach items="${page.list}" var="live" varStatus="status">
							<tr>
								<td>${(page.pageNo-1)*(page.pageSize)+(status.index+1)}</td>
								<td>${live.name}</td>
								<td>${live.phone}</td>
								<td>${live.organization}</td>
								<td>${live.position}</td>
								<td>${live.num}</td>
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