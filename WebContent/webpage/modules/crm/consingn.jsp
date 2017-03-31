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
			<div class="ibox-title">
				<div class="text">
					<h5>首页&nbsp;&nbsp;</h5>
				</div>
				<div class="text active">
					<h5>&nbsp;&nbsp;客户管理&nbsp;&nbsp;</h5>
				</div>
				<div class="text">
					<h5>&nbsp;&nbsp;订单管理&nbsp;&nbsp;</h5>
				</div>
				<div class="text">
					<h5>&nbsp;&nbsp;综合报表&nbsp;&nbsp;</h5>
				</div>
			</div>
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
							<li role="presentation"><a
										href="${ctx}/crm/user/userDetail?userId=${userId}">基本资料</a></li>
							<li role="presentation"><a
										href="${ctx}/crm/physical/skin?userId=${userId}">身体状况</a></li>
							<li role="presentation"><a
										href="${ctx}/crm/schedule/list?userId=${userId}">护理时间表</a></li>
							<li role="presentation"><a
										href="${ctx}/crm/orders/list?userId=${userId}">客户订单</a></li>
							<li role="presentation"><a
										href="${ctx}/crm/coustomerService/list?userId=${userId}">售后</a></li>
							<li role="presentation" class="layui-this"><a
										href="${ctx}/crm/consign/list?userId=${userId}">物品寄存</a></li>
							<li role="presentation"><a
										href="${ctx}/crm/goodsUsage/list?userId=${userId}">产品使用记录</a></li>
							<li role="presentation"><a
										href="${ctx}/crm/account?userId=${userId}">账户总览</a></li>
							<li role="presentation"><a
										href="${ctx}/crm/invitation?userId=${userId}">邀请明细</a></li>
							<li role="presentation"><a 
								href="crm/store/list?mobile=${userDetail.mobile}&stamp='1';">投诉咨询</a></li>
						  </ul>
					</div>
				</div>
				<div>
					<!-- 工具栏 -->
					<div class="row">
						<div class="col-sm-12">
							<div class="pull-left">
								<table:addRow url="${ctx}/crm/consign/add?userId=${userId}"
									title="用户寄存记录"></table:addRow>
								<!-- 增加按钮 -->
							</div>
						</div>
					</div>
					<!-- 工具栏 -->
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
								<td><fmt:formatDate value="${item.createDate}"
										pattern="yyyy-MM-dd " /></td>
								<td>${item.remark}</td>
								<td>
									<a href="#"
									onclick="openDialog('查看寄存详情', '${ctx}/crm/consign/update?consignId=${item.consignId}&userId=${userId}','800px', '500px')"
									class="btn btn-success btn-xs"><i class="fa fa-search-plus"></i>修改</a>
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
					<div class="row" style="margin: 10px">
						<div class="col-md-8">
							<h3 style="color: blue">操作日志</h3>
						</div>
					</div>
					<table class="table table-bordered table-condensed  dataTables-example dataTable no-footer">
						<tr>
							<th style="text-align: center;" class="col-md-1">操作日期</th>
							<th style="text-align: center;" class="col-md-1">操作人</th>
							<th style="text-align: center;" class="col-md-3">操作记录</th>
						</tr>
						<c:forEach items="${logList}" var="log">
							<tr style="text-align: center;">
								<td class="col-md-1">
									<p>
										<fmt:formatDate value="${log.createTime}" pattern="yyyy-MM-dd  HH:mm" />
									</p>
								</td>
								<td class="col-md-1">
									<p>${log.creatorName}</p>
								</td>
								<td class="col-md-3">
									<p>${log.content}</p>
								</td>
							</tr>
						</c:forEach>
					</table>
				</div>
			</div>
		</div>
	</div>
</body>
</html>