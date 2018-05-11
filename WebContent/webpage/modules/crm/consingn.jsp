<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/webpage/include/taglib.jsp"%>
<html>
<head>
<title>会员管理</title>
<meta name="decorator" content="default" />
<meta name="viewport"
	content="width=device-width, initial-scale=1.0, minimum-scale=1.0, maximum-scale=1.0,user-scalable=no">
	<link href="${ctxStatic}/layer-v2.0/layer/skin/layui.css" type="text/css" rel="stylesheet">

<script type="text/javascript">
	function edit() {
		$("userid").removeAttr("disabled");
		$("sex").attr("disabled", false);
		$("userid").removeAttr("disabled")
	}
	function save() {
		$("#inputForm").submit();
		return true;
	}
</script>
<style type="text/css">
.modal-content {
	margin: 0 auto;
	width: 400px;
}
</style>
</head>
<body class="gray-bg">
	<div class="wrapper wrapper-content">
		<div class="ibox">
			<sys:message content="${message}" />
			<!-- 查询条件 -->
			<div class="ibox-content">
				<div class="clearfix">
					<div class="nav">
						<!-- 翻页隐藏文本框 -->
						<div class="text-danger" style="margin:8px">
							<p class="text-primary">
								<span >${userDetail.nickname}</span>的客户档案--请注意保密
							</p>
						</div>
						 <ul class="layui-tab-title">
							<li role="presentation">
								<a href="${ctx}/crm/user/userDetail?userId=${userId}&franchiseeId=${franchiseeId}">基本资料</a>
							</li>
							<li role="presentation">
								<a href="${ctx}/crm/physical/skin?userId=${userId}&franchiseeId=${franchiseeId}">身体状况</a>
							</li>
							<li role="presentation">
								<a href="${ctx}/crm/schedule/list?userId=${userId}&franchiseeId=${franchiseeId}">护理时间表</a>
							</li>
							<li role="presentation">
								<a href="${ctx}/crm/orders/list?userId=${userId}&franchiseeId=${franchiseeId}">客户订单</a>
							</li>
							<li role="presentation">
								<a href="${ctx}/crm/coustomerService/list?userId=${userId}&franchiseeId=${franchiseeId}">售后</a>
							</li>
							<li role="presentation" class="layui-this">
								<a href="${ctx}/crm/consign/list?userId=${userId}&franchiseeId=${franchiseeId}">物品寄存</a>
							</li>
							<li role="presentation">
								<a href="${ctx}/crm/goodsUsage/list?userId=${userId}&franchiseeId=${franchiseeId}">产品使用记录</a>
							</li>
							<li role="presentation">
								<a href="${ctx}/crm/user/account?userId=${userId}&franchiseeId=${franchiseeId}">账户总览</a>
							</li>
							<li role="presentation">
								<a href="${ctx}/crm/invitation/list?userId=${userId}&franchiseeId=${franchiseeId}">邀请明细</a>
							</li>
							<li role="presentation">
								<shiro:hasPermission name="crm:store:list">	
									<a href="${ctx}/crm/store/questionCrmList?mobile=${userDetail.mobile}&userId=${userId}&franchiseeId=${franchiseeId}&stamp=1">投诉咨询</a>
								</shiro:hasPermission>
							</li>
						  </ul>
					</div>
				</div>
				<div>
				<form id="searchForm" action="${ctx}/crm/consign/list" method="post" class="form-inline">
					<input id="pageNo" name="pageNo" type="hidden" value="${page.pageNo}" />
					<input id="pageSize" name="pageSize" type="hidden" value="${page.pageSize}" />
					<table:sortColumn id="orderBy" name="orderBy" value="${page.orderBy}" callback="sortOrRefresh();" />
					<!-- 支持排序 -->
					<input id="userId" name="userId" type="hidden" value="${userDetail.userId}" />
					<input id="franchiseeId" name="franchiseeId" type="hidden" value="${franchiseeId}" />
			   </form>	
				
					<!-- 工具栏 -->
					<div class="row">
						<div class="col-sm-12">
							<div class="pull-left">
								<shiro:hasPermission name="crm:consign:exe">
									<table:addRow url="${ctx}/crm/consign/add?userId=${userId}&franchiseeId=${franchiseeId}" title="用户寄存记录"></table:addRow>
									<!-- 增加按钮 -->
								</shiro:hasPermission>	
							</div>
						</div>
					</div>
				</div>
				<table id="contentTable"
					class="table table-striped table-bordered  table-hover table-condensed  dataTables-example dataTable no-footer">
					<thead>
						<tr>
							<!-- 							 <th style="text-align: center;">编号</th> -->
							<th style="text-align: center;">店铺名称</th>
							<th style="text-align: center;">产品编号</th>
							<th style="text-align: center;">产品名称</th>
							<th style="text-align: center;">购买数量</th>
							<th style="text-align: center;">寄存数量</th>
							<th style="text-align: center;">取走数量</th>
							<th style="text-align: center;">寄存时间</th>
							<th style="text-align: center;">备注</th>
							<th style="text-align: center;">操作</th>
						</tr>
					</thead>
					<tbody style="text-align: center;">
						<c:forEach items="${page.list}" var="item">
							<tr>
								<td>${item.officeName}</td>
								<td>${item.goodsNo}</td>
								<td>${item.goodsName}</td>
								<td>${item.purchaseNum}</td>
								<td>${item.consignNum}</td>
								<td>${item.takenNum}</td>
								<td><fmt:formatDate value="${item.createDate}" pattern="yyyy-MM-dd " /></td>
								<td>${item.remark}</td>
								<td>
									<shiro:hasPermission name="crm:consign:exe">
										<a href="#" onclick="openDialog('查看寄存详情', '${ctx}/crm/consign/update?consignId=${item.consignId}&userId=${userId}&franchiseeId=${franchiseeId}','800px', '500px')" class="btn btn-success btn-xs"><i class="fa fa-search-plus"></i>修改</a>
									</shiro:hasPermission>
								</td>
							</tr>
						</c:forEach>
					</tbody>
					<tfoot>
						<tr>
							<td colspan="20">
								<!-- 分页代码 -->
								<div class="tfoot"></div> <table:page page="${page}"></table:page>
							</td>
						</tr>
					</tfoot>
				</table>
			</div>
		</div>
	</div>
	<div class="wrapper wrapper-content">
		<div class="ibox">
			<!--用户联系信息 -->
			<div class="ibox-content">
				<div class="clearfix">
					<shiro:hasPermission name="crm:userInfo:edit">
						<a href="#" onclick="openDialogView('操作日志', '${ctx}/crm/user/logDetail?userId=${userId}&franchiseeId=${franchiseeId}&operatorType=4','850px','650px')" class="btn btn-info btn-xs" ><i class="fa fa-search-plus"></i> 操作日志</a>
					</shiro:hasPermission>
				</div>
			</div>
		</div>
	</div>
</body>
</html>