<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/webpage/include/taglib.jsp"%>
<html>
<head>
<title>A级用户详情</title>
<meta name="decorator" content="default" />
<link rel="stylesheet" type="text/css"
	href="${ctxStatic}/common/training.css">
		<link href="${ctxStatic}/layer-v2.0/layer/skin/layui.css" type="text/css" rel="stylesheet">
<script type="text/javascript">
		function nowReset(){
			$("#pageNo").val(0);
			$("#searchForm div.form-group input").val("");
			$("#searchForm div.form-group select").val("");
			$("#searchForm").submit();
		}
		function move(userId,parentId){
			$("#userId").val(userId);
			$("#parentId").val(parentId);
			$('#modal').modal("show");
		}
	</script>
<style type="text/css">
.modal-content {
	margin: 0 auto;
	width: 350px;
}
</style>
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
				<h5>A级用户详情</h5>
			</div>
			<!-- 主体内容 -->
			<div class="ibox-content">
				<sys:message content="${message}" />
				<table id="contentTable"
					class="table table-striped table-bordered table-hover table-condensed dataTables-example dataTable">
					<thead>
						<tr>
							<th colspan="8">用户基本信息</th>
						</tr>
						<tr>
							<th>级别</th>
							<th>妃子校用户归属店铺</th>
							<th>妃子校用户名</th>
							<th>每天美耶用户名</th>
							<th>B级用户</th>
							<th>剩余B级用户</th>
							<th>AB级用户</th>
							<th>剩余AB级用户</th>
						</tr>
					</thead>
					<tbody>
						<tr>
							<td>A</td>
							<td>${mtmySaleRelieve.office.name }</td>
							<td>${mtmySaleRelieve.user.name }</td>
							<td>${mtmySaleRelieve.users.nickname }</td>
							<td>${mtmySaleRelieve.ABnum }</td>
							<td><c:if
									test="${mtmyRuleParam.paramValue - mtmySaleRelieve.ABnum < 0}">
									0
								</c:if> <c:if
									test="${mtmyRuleParam.paramValue - mtmySaleRelieve.ABnum >= 0}">
									${mtmyRuleParam.paramValue - mtmySaleRelieve.ABnum }
								</c:if></td>
							<td>${mtmySaleRelieve.ACnum }</td>
							<td><c:if
									test="${ACmtmyRuleParam.paramValue - mtmySaleRelieve.ACnum < 0}">
									0
								</c:if> <c:if
									test="${ACmtmyRuleParam.paramValue - mtmySaleRelieve.ACnum >= 0}">
									${ACmtmyRuleParam.paramValue - mtmySaleRelieve.ACnum }
								</c:if></td>
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
				<!-- 查询条件 -->
				<div class="row">
					<div class="col-sm-12">
						<form:form id="searchForm" modelAttribute="mtmySaleRelieve"
							action="${ctx}/ec/mtmySale/userFrom" method="post"
							class="form-inline">
							<input id="pageNo" name="pageNo" type="hidden"
								value="${page.pageNo}" />
							<input id="pageSize" name="pageSize" type="hidden"
								value="${page.pageSize}" />
							<table:sortColumn id="orderBy" name="orderBy"
								value="${page.orderBy}" callback="sortOrRefresh();" />
							<input id="users.userid" name="users.userid" type="hidden"
								value="${mtmySaleRelieve.users.userid}" />
							<!-- 支持排序 -->
						</form:form>
					</div>
				</div>
				<p></p>
				<!-- 主要内容展示 -->
				<table id="contentTable"
					class="table table-striped table-bordered table-hover table-condensed dataTables-example dataTable">
					<thead>
						<tr>
							<th>级别</th>
							<th>被邀请人姓名</th>
							<th>被邀请人手机号</th>
							<th>邀请人数</th>
							<th>邀请人</th>
							<th>邀请人级别</th>
							<th>邀请人手机号</th>
							<th class="sort-column r.create_date">邀请成功时间</th>
							<th class="sort-column l.balanceAmount">贡献余额</th>
							<th class="sort-column l.integralAmount">贡献云币</th>
							<th>操作</th>
						</tr>
					</thead>
					<tbody>
						<c:forEach items="${page.list}" var="mtmySaleRelieve">
							<tr>
								<td>${mtmySaleRelieve.users.layer}</td>
								<td>${mtmySaleRelieve.users.nickname}</td>
								<td>${mtmySaleRelieve.users.mobile}</td>
								<td>${mtmySaleRelieve.lowerNum}</td>
								<td>
									<!-- 每天美耶用户: -->${mtmySaleRelieve.newusers.nickname } <%-- <c:if test="${mtmySaleRelieve.user.name != '' && mtmySaleRelieve.user.name != null}">
										<br>妃子校用户:${mtmySaleRelieve.user.name }
									</c:if> --%>
								</td>
								<td>${mtmySaleRelieve.newusers.layer }</td>
								<td>${mtmySaleRelieve.newusers.mobile }</td>
								<td><fmt:formatDate value="${mtmySaleRelieve.createDate}"
										pattern="yyyy-MM-dd HH:mm:ss" /></td>
								<td><fmt:formatNumber type="number"
										value="${mtmySaleRelieve.mtmySaleRelieveLog.balanceAmount}"
										maxFractionDigits="2" /></td>
								<td><fmt:formatNumber type="number"
										value="${mtmySaleRelieve.mtmySaleRelieveLog.integralAmount}"
										maxFractionDigits="2" /></td>
								<td><a href="#"
									onclick='top.openTab("${ctx}/ec/mtmySale/findChild?users.userid=${mtmySaleRelieve.users.userid}&moveType=${mtmySaleRelieve.users.layer }","用户详情", false)'
									class="btn btn-info btn-xs"><i class="fa fa-search-plus"></i>
										查看用户详情</a> <a href="#"
									onclick="openDialogView('查看记录', '${ctx}/ec/mtmySale/userDetails?users.nickname=${mtmySaleRelieve.users.nickname}&rebateUser=${mtmySaleRelieve.users.userid}&receiveUser=${mtmySaleRelieve.parentId}','750px', '600px')"
									class="btn btn-info btn-xs"><i class="fa fa-search-plus"></i>
										查看记录</a> <a href="#"
									onclick="move(${mtmySaleRelieve.users.userid},${mtmySaleRelieve.parentId})"
									class="btn btn-info btn-xs btn-danger"><i
										class="fa fa-share"></i> 转移关系</a></td>
							</tr>
						</c:forEach>
					</tbody>
				</table>
				<!-- 分页table -->
				<table:page page="${page}"></table:page>
			</div>
		</div>
	</div>
	<div class="modal fade bs-example-modal-lg in" id="modal" tabindex="-1"
		role="dialog" aria-labelledby="myLargeModalLabel"
		style="display: none; padding-right: 17px;">
		<form id="" class="modal-dialog modal-lg"
			action="${ctx}/ec/mtmySale/move">
			<input name="parentId" id="parentId" type="hidden"> <input
				name="userId" id="userId" type="hidden"> <input
				name="moveType" id="moveType" value="AFrom" type="hidden">
			<div class="modal-content">
				<div class="modal-header">
					<span>选择转移后用户上级</span>
					<button type="button" class="close" data-dismiss="modal"
						aria-label="Close">
						<span aria-hidden="true">×</span>
					</button>
				</div>
				<div class="modal-body">
					<table>
						<c:forEach items="${SNmtmyRuleParam }" var="SNmtmyRuleParam"
							varStatus="status">
							<tr>
								<td><input type="radio" id="newParentId" name="newParentId"
									${(status.index == '0')?'checked="checked"':''}
									value="${SNmtmyRuleParam.paramValue }">&nbsp;&nbsp;${SNmtmyRuleParam.paramExplain }


								
								<td>
							</tr>
						</c:forEach>
					</table>
				</div>
				<div class="modal-footer">
					<input type="submit" class="btn btn-success" value="保 存"> <a
						href="#" class="btn btn-primary" data-dismiss="modal">关 闭</a>
				</div>
			</div>
		</form>
	</div>
</body>
</html>