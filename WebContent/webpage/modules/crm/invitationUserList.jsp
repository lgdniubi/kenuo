<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/webpage/include/taglib.jsp"%>
<html>
<head>
<title>邀请明细列表</title>
<meta charset="utf-8">
<meta name="decorator" content="default"/>
<meta name="viewport" content="width=device-width, initial-scale=1.0, minimum-scale=1.0, maximum-scale=1.0,user-scalable=no">
		<link href="${ctxStatic}/layer-v2.0/layer/skin/layui.css" type="text/css" rel="stylesheet">
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
				
						<div class="nav">
						<div class="form-group">
							<p>
								<span>${detail.name}</span>的客户档案--请注意保密
							</p>
						</div>
				   <ul class="layui-tab-title">
					<li role="presentation" class="layui-this"><a
								href="${ctx}/crm/user/userDetail?userId=${userId}">基本资料</a></li>
					<li role="presentation"><a
								href="${ctx}/crm/physical/skin?userId=${userId}">身体状况</a></li>
					<li role="presentation"><a
								href="${ctx}/crm/schedule/list?userId=${userId}">护理时间表</a></li>
					<li role="presentation"><a
								href="${ctx}/crm/orders/list?userId=${userId}">客户订单</a></li>
					<li role="presentation"><a
								href="${ctx}/crm/coustomerService/list?userId=${userId}">售后</a></li>
					<li role="presentation"><a
								href="${ctx}/crm/consign/list?userId=${userId}">物品寄存</a></li>
					<li role="presentation"><a
								href="${ctx}/crm/goodsUsage/list?userId=${userId}">产品使用记录</a></li>
					<li role="presentation"><a
								href="${ctx}/crm/coin/list?userId=${userId}">云币明细</a></li>
					<li role="presentation"><a
								href="${ctx}/crm/user/account?userId=${userId}">账户总览</a></li>
					<li role="presentation"><a
								href="${ctx}/crm/invitation?userId=${userId}">邀请明细</a></li>
					<li role="presentation"><a
								href="${ctx}/crm/returnRecord?userId=${userId}">回访记录</a></li>
					<li role="presentation"><a 
								href="crm/store/list?mobile=${userDetail.mobile}&stamp='1';">投诉咨询</a></li>
				  </ul>
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