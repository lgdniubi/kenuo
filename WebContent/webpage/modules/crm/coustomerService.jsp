<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/webpage/include/taglib.jsp"%>
<html>
<head>
<title>会员管理</title>
<meta name="decorator" content="default" />
<meta name="viewport"
	content="width=device-width, initial-scale=1.0, minimum-scale=1.0, maximum-scale=1.0,user-scalable=no">
<link href="${ctxStatic}/layer-v2.0/layer/skin/layui.css"
	type="text/css" rel="stylesheet">

<script type="text/javascript">
	// 	    function edit(){
	// 	    	$("userid").removeAttr("disabled"); 
	// 	    	$("sex").attr("disabled",false);
	// 	    	$("userid").removeAttr("disabled")
	// 	    }
	function save() {
		$("#inputForm").submit();
		return true;
	}
	$('#edit').click(function() {
		if ($('#fieldset').is(':disabled')) {
			$('#fieldset').removeAttr('disabled');
		}
	});
	
	$(document).ready(function() {
		var start = {
			    elem: '#begtime',
			    format: 'YYYY-MM-DD',
			    event: 'focus',
			    max: $("#endtime").val(),   //最大日期
			    istime: false,				//是否显示时间
			    isclear: true,				//是否显示清除
			    istoday: true,				//是否显示今天
			    issure: true,				//是否显示确定
			    festival: true,				//是否显示节日
			    choose: function(datas){
			         end.min = datas; 		//开始日选好后，重置结束日的最小日期
			         end.start = datas 		//将结束日的初始值设定为开始日
			    }
			};
		var end = {
			    elem: '#endtime',
			    format: 'YYYY-MM-DD',
			    event: 'focus',
			    min: $("#begtime").val(),
			    istime: false,
			    isclear: true,
			    istoday: true,
			    issure: true,
			    festival: true,
			    choose: function(datas){
			        start.max = datas; //结束日选好后，重置开始日的最大日期
			    }
			};
			laydate(start);
			laydate(end);
    });

	$(document).ready(function() {
		$("#btnExport").click(function(){
			top.layer.confirm('确认要导出Excel吗?', {icon: 3, title:'系统提示'}, function(){
				//导出excel
				$("#exportForm").submit();
				top.layer.close();
			});
		});
	});
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
								<span>${userDetail.nickname}</span>的客户档案--请注意保密
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
							<li role="presentation" class="layui-this"><a
								href="${ctx}/crm/coustomerService/list?userId=${userId}">售后</a></li>
							<li role="presentation"><a
								href="${ctx}/crm/consign/list?userId=${userId}">物品寄存</a></li>
							<li role="presentation"><a
								href="${ctx}/crm/goodsUsage/list?userId=${userId}">产品使用记录</a></li>
							<li role="presentation"><a
								href="${ctx}/crm/user/account?userId=${userId}">账户总览</a></li>
							<li role="presentation"><a
								href="${ctx}/crm/invitation/list?userId=${userId}">邀请明细</a></li>
							<li role="presentation"><a
								href="${ctx}/crm/returnRecord?userId=${userId}">回访记录</a></li>
							<li role="presentation">
							<shiro:hasPermission name="crm:store:list">	
							<a onclick='top.openTab("${ctx}/crm/store/list?mobile=${userDetail.mobile}&stamp=1","会员投诉", false)'
								>投诉咨询</a>
							</shiro:hasPermission>
							</li>
						</ul>
					</div>
				</div>
			</div>
			<!-- 查询条件 -->
			<div class="ibox-content">
				<div class="clearfix">
					<form:form id="searchForm" modelAttribute="returnedGoods"
						action="${ctx}/crm/coustomerService/list" method="post" class="form-inline">
						<input id="pageNo" name="pageNo" type="hidden"
							value="${page.pageNo}" />
						<input id="pageSize" name="pageSize" type="hidden"
							value="${page.pageSize}" />
						<input id="userId" name="userId" type="hidden"
							value="${userDetail.userId}" />	
						<table:sortColumn id="orderBy" name="orderBy"
							value="${page.orderBy}" callback="sortOrRefresh();" />
						<!-- 支持排序 -->
						<div class="form-group">
							<label>创建时间：</label> <input id="begtime" name="begtime"
								type="text" maxlength="20"
								class="laydate-icon form-control layer-date input-sm"
								value="<fmt:formatDate value="${returnedGoods.begtime}" pattern="yyyy-MM-dd"/>"
								placeholder="开始时间" readonly="readonly" style="width: 185px;" />
							— <input id="endtime" name="endtime" type="text" maxlength="20"
								class=" laydate-icon form-control layer-date input-sm"
								value="<fmt:formatDate value="${returnedGoods.endtime}" pattern="yyyy-MM-dd"/>"
								placeholder="结束时间" readonly="readonly" style="width: 185px;" />
							<label>入库状态：</label>
							<form:select path="isStorage" class="form-control"
								style="width:185px;">
								<form:option value=" ">全部</form:option>
								<form:option value="0">未入库</form:option>
								<form:option value="1">已入库</form:option>
							</form:select>
							<label>退货状态：</label>
							<form:select path="returnStatus" class="form-control"
								style="width:185px;">
								<form:option value=" ">全部</form:option>
								<form:option value="-10">拒绝退货</form:option>
								<form:option value="11">申请退货</form:option>
								<form:option value="12">同意退货</form:option>
								<form:option value="13">退货中</form:option>
								<form:option value="14">退货完成</form:option>
								<form:option value="15">退款中</form:option>
								<form:option value="16">已退款</form:option>
								<form:option value="-20">拒绝换货</form:option>
								<form:option value="21">申请换货</form:option>
								<form:option value="22">同意换货</form:option>
								<form:option value="23">换货退货中</form:option>
								<form:option value="24">换货退货完成</form:option>
								<form:option value="25">换货中</form:option>
								<form:option value="26">换货完成</form:option>
							</form:select>
							<form:input path="keyword" htmlEscape="false" style="width:275px" class=" form-control input-sm" 
							placeholder="请输入订单号，用户名或者手机号"/>
							<p></p>
						</div>
					</form:form>
					<!-- 工具栏 -->
					<div class="row">
						<div class="col-sm-12">
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
					<p></p>
				</div>
					<div>
					<!-- 工具栏 -->
<!-- 					<div class="row"> -->
<!-- 						<div class="col-sm-12"> -->
<!-- 							<div class="pull-left"> -->
<%-- <%-- 								<shiro:hasPermission name="crm:returnedGoods:export"> --%> 
<!-- 								导出按钮 -->
<%-- 								<form id="exportForm" action ="${ctx}/crm/coustomerService/export" method="post"> --%>
<%-- 								  <input type="hidden" name="userId" value="${userDetail.userId}">	 --%>
<!-- 									<button id ="btnExport" class="btn btn-danger btn-xs">导出</button> -->
<%-- <%-- 								</shiro:hasPermission> --%>
<!-- 								</form> -->
<!-- 							</div> -->
<!-- 						</div> -->
<!-- 					</div> -->
<!-- 					工具栏 -->
				</div>
				<table id="contentTable"
					class="table table-striped table-bordered  table-hover table-condensed  
					dataTables-example dataTable no-footer">
					<thead>
						<tr>
							<th style="text-align: center;">退货单号</th>
							<th style="text-align: center;">用户名</th>
							<th style="text-align: center;">退货状态</th>
							<th style="text-align: center;">支付金额</th>
							<th style="text-align: center;">申请服务</th>
							<th style="text-align: center;">申请时间</th>
							<th style="text-align: center;">退货原因</th>
							<th style="text-align: center;">入库状态</th>
							<th style="text-align: center;">退款金额</th>
							<th style="text-align: center;">客服备注</th>
							<th style="text-align: center;">操作</th>
						</tr>
					</thead>
					<tbody>
						<c:forEach items="${page.list}" var="returnGoods">
							<tr>
								<td style="text-align: center;">${returnGoods.orderId}</td>
								<td style="text-align: center;">${returnGoods.userName}</td>
								<td style="text-align: center;"><c:if
										test="${returnGoods.returnStatus==-10}">
										拒绝退货
									</c:if> <c:if test="${returnGoods.returnStatus==-20}">
										拒绝换货
									</c:if> <c:if test="${returnGoods.returnStatus==11}">
										申请退货
									</c:if> <c:if test="${returnGoods.returnStatus==12}">
										同意退货
									</c:if> <c:if test="${returnGoods.returnStatus==13}">
										退货中
									</c:if> <c:if test="${returnGoods.returnStatus==14}">
										退货完成
									</c:if> <c:if test="${returnGoods.returnStatus==15}">
										退款中
									</c:if> <c:if test="${returnGoods.returnStatus==16}">
										已退款
									</c:if> <c:if test="${returnGoods.returnStatus==21}">
										申请换货
									</c:if> <c:if test="${returnGoods.returnStatus==22}">
										同意换货
									</c:if> <c:if test="${returnGoods.returnStatus==23}">
										换货退货中
									</c:if> <c:if test="${returnGoods.returnStatus==24}">
										换货退货完成
									</c:if> <c:if test="${returnGoods.returnStatus==25}">
										换货中
									</c:if> <c:if test="${returnGoods.returnStatus==26}">
										换货完成
									</c:if></td>
								<td style="text-align: center;">${returnGoods.orderAmount}</td>
								<td style="text-align: center;"><c:if
										test="${returnGoods.applyType==0}">
										退货并退款
									</c:if> <c:if test="${returnGoods.applyType==1}">
										仅换货
									</c:if></td>
								<td style="text-align: center;"><fmt:formatDate
										value="${returnGoods.applyDate}" pattern="yyyy-MM-dd HH:mm:ss" />
								</td>
								<td style="text-align: center;">${returnGoods.returnReason}</td>
								<td style="text-align: center;"><c:if
										test="${returnGoods.isStorage==0}">
										未入库
									</c:if> <c:if test="${returnGoods.isStorage==1}">
										已入库
									</c:if></td>
								<td style="text-align: center;">${returnGoods.returnAmount}</td>
								<td style="text-align: center;">${returnGoods.remarks}</td>
								<td style="text-align: center;">
									<!-- 查看详情 --> <shiro:hasPermission name="ec:returned:view">
										<a href="#"
											onclick="openDialogView('售后详情', '${ctx}/ec/returned/returnform?id=${returnGoods.id}','850px','650px')"
											class="btn btn-info btn-xs"><i class="fa fa-search-plus"></i>
											查看详情</a>
									</shiro:hasPermission> <!-- 审核 --> 

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