<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/webpage/include/taglib.jsp"%>
<html>
<head>
<title>邀请明细列表</title>
<meta charset="utf-8">
<meta name="decorator" content="default"/>
<meta name="viewport" content="width=device-width, initial-scale=1.0, minimum-scale=1.0, maximum-scale=1.0,user-scalable=no">
<script>
	function page(n, s) {
		$("#pageNo").val(n);
		$("#pageSize").val(s);
		$("#searchForm").submit();
		return false;
	}
</script>
</head>
<body>
	<div class="wrapper wrapper-content">
		<div class="ibox">
			<div class="ibox-title">
				<h5>邀请明细列表</h5>
			</div>
			<sys:message content="${message}" />
			<!-- 查询条件 -->
			<div class="ibox-content">
				<div class="clearfix">
					<form:form id="searchForm" modelAttribute="invitationUser"
						action="${ctx}/ec/invitation/invitationlist" method="post"
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
							<th style="text-align: center;">发起人</th>
							<th style="text-align: center;">邀请人数</th>
							<th style="text-align: center;">消费人数</th>
							<th style="text-align: center;">总消费金额</th>
							<th style="text-align: center;">总获得提成</th>
							<th style="text-align: center;">账户余额</th>
							<th style="text-align: center;">操作</th>
						</tr>
					</thead>
					<tbody style="text-align: center;">
						<c:forEach items="${page.list}" var="list" varStatus="status">
							<tr>
								<td>${status.index+1}</td>
								<td>${list.userName}</td>
								<td>${list.sendNum}</td>
								<td>${list.consume}</td>
								<td>${list.consumePrice}</td>
								<td>${list.tiPrice}</td>
								<td>${list.price}</td>
								<td>
									<shiro:hasPermission name="ec:invitation:view">
										<a href="#"
											onclick="openDialogView('查看受邀人','${ctx}/ec/invitation/form?invitationCode=${list.invitationCode}&userName=${list.userName}&sendNum=${list.sendNum}&consume=${list.consume}&consumePrice=${list.consumePrice}&tiPrice=${list.tiPrice}&price=${list.price}','900px','600px')"
											class="btn btn-info btn-xs"><i class="fa fa-search-plus"></i>查看详情</a>
									</shiro:hasPermission>
								</td>
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