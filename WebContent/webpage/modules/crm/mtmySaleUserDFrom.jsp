<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/webpage/include/taglib.jsp"%>
<html>
<head>
<title>D级用户详情</title>
<meta name="decorator" content="default" />
<link rel="stylesheet" type="text/css"
	href="${ctxStatic}/common/training.css">
		<link href="${ctxStatic}/layer-v2.0/layer/skin/layui.css" type="text/css" rel="stylesheet">
</head>
<body>
	<div class="wrapper wrapper-content">
		<div class="ibox">
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
					<li role="presentation"><a
						href="${ctx}/crm/consign/list?userId=${userId}">物品寄存</a></li>
					<li role="presentation"><a
						href="${ctx}/crm/goodsUsage/list?userId=${userId}">产品使用记录</a></li>
					<li role="presentation"><a
						href="${ctx}/crm/user/account?userId=${userId}">账户总览</a></li>
					<li role="presentation" class="layui-this"><a
						href="${ctx}/crm/invitation/list?userId=${userId}">邀请明细</a></li>
					<li role="presentation">
							<shiro:hasPermission name="crm:store:list">	
							<a onclick='top.openTab("${ctx}/crm/store/list?mobile=${userDetail.mobile}&stamp=1","投诉咨询", false)'
								>投诉咨询</a>
							</shiro:hasPermission>
					</li>
				</ul>
			</div>
			<!-- 标题 -->
			<div class="ibox-title">
				<h5>D级用户详情</h5>
			</div>
			<!-- 主体内容 -->
			<div class="ibox-content">
				<sys:message content="${message}" />
				<table id="contentTable"
					class="table table-striped table-bordered table-hover table-condensed dataTables-example dataTable">
					<thead>
						<tr>
							<th colspan="9">用户基本信息</th>
						</tr>
						<tr>
							<th>级别</th>
							<th>用户名</th>
							<th>注册手机号</th>
							<th>邀请人</th>
							<th>邀请人级别</th>
							<th>邀请人手机号</th>
							<th>受邀成功时间</th>
						</tr>
					</thead>
					<tbody>
						<tr>
							<td>${mtmySaleRelieve.users.layer }</td>
							<td>${mtmySaleRelieve.users.nickname }</td>
							<td>${mtmySaleRelieve.users.mobile }</td>
							<td>
								<!-- 每天美耶用户: -->${mtmySaleRelieve.newusers.nickname} <%-- <c:if test="${mtmySaleRelieve.user.name != null && mtmySaleRelieve.user.name != ''}">
									<br>妃子校用户:${mtmySaleRelieve.user.name}
								</c:if> --%>
							</td>
							<td>${mtmySaleRelieve.newusers.layer}</td>
							<td>${mtmySaleRelieve.newusers.mobile }</td>
							<td><fmt:formatDate value="${mtmySaleRelieve.createDate}"
									pattern="yyyy-MM-dd HH:mm:ss" /></td>
						</tr>
					</tbody>
				</table>
				<table id="contentTable"
					class="table table-striped table-bordered table-hover table-condensed dataTables-example dataTable">
					<thead>
						<tr>
							<th colspan="7">用户收益信息</th>
						</tr>
						<tr>
							<th>总金额</th>
							<th>可用余额</th>
							<th>结算中余额</th>
							<th>总云币</th>
							<th>可用云币</th>
							<th>结算中云币</th>
							<th>操作</th>
						</tr>
					</thead>
					<tbody>
						<tr>
							<td><fmt:formatNumber type="number"
									value="${mtmySaleRelieve.users.usermoney + mtmySaleRelieve.mtmySaleRelieveLog.balanceAmount }"
									maxFractionDigits="2" /></td>
							<td><fmt:formatNumber type="number"
									value="${mtmySaleRelieve.users.usermoney }"
									maxFractionDigits="2" /></td>
							<td><fmt:formatNumber type="number"
									value="${mtmySaleRelieve.mtmySaleRelieveLog.balanceAmount }"
									maxFractionDigits="2" /></td>
							<td><fmt:formatNumber type="number"
									value="${mtmySaleRelieve.users.paypoints + mtmySaleRelieve.mtmySaleRelieveLog.integralAmount }"
									maxFractionDigits="2" /></td>
							<td><fmt:formatNumber type="number"
									value="${mtmySaleRelieve.users.paypoints }"
									maxFractionDigits="2" /></td>
							<td><fmt:formatNumber type="number"
									value="${mtmySaleRelieve.mtmySaleRelieveLog.integralAmount }"
									maxFractionDigits="2" /></td>
							<td><a href="#"
								onclick="openDialog('收益明细', '${ctx}/ec/mtmySale/userEarnings?receiveUser=${mtmySaleRelieve.users.userid}','750px', '600px')"
								class="btn btn-info btn-xs"><i class="fa fa-search-plus"></i>
									收益明细</a> <%-- <a href="#" onclick="openDialog('消费明细', '${ctx}/ec/mtmySale/userDetails?users.nickname=${mtmySaleRelieve.users.nickname}&rebateUser=${mtmySaleRelieve.users.userid}&receiveUser=${mtmySaleRelieve.parentId}','650px', '500px')" class="btn btn-info btn-xs" ><i class="fa fa-search-plus"></i> 消费明细</a> --%>
							</td>
						</tr>
					</tbody>
				</table>
			</div>
		</div>
	</div>
</body>
</html>