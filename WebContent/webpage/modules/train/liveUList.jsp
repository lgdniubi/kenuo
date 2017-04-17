<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/webpage/include/taglib.jsp"%>

<html>
<head>
<title>会员列表</title>
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
			<sys:message content="${message}" />
			<!-- 查询条件 -->
			<div class="ibox-content">
				<p></p>
				<table id="contentTable"
					class="table table-striped table-bordered  table-hover table-condensed  dataTables-example dataTable no-footer">
					<thead>
						<tr>
							<th style="text-align: center;">编号</th>
							<th style="text-align: center;">会员姓名</th>
							<th style="text-align: center;">所属机构</th>
							<th style="text-align: center;">手机号</th>
							<th style="text-align: center;">职位</th>
							<th style="text-align: center;">购买金额</th>
							<th style="text-align: center;">支付方式</th>
							<th style="text-align: center;">备注</th>
						</tr>
					</thead>
					<tbody style="text-align: center;">
						<c:forEach items="${page.list}" var="list">
							<tr>
								<td>${list.id}</td>
								<td>${list.name}</td>
								<td>${list.organization }</td>
								<td>${list.mobile}</td>
								<td>${list.position}</td>
								<td>${list.money}</td>
								<td>${list.payment}</td>
								<td>${list.remak}</td>
							</tr>	
						</c:forEach>
					</tbody>
					<tfoot>
						<tr>
							<td colspan="20">
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