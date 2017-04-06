<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/webpage/include/taglib.jsp"%>
<html>
<head>
	<title>会员管理</title>
	<meta name="decorator" content="default"/>
    <meta name="viewport" content="width=device-width, initial-scale=1.0, minimum-scale=1.0, maximum-scale=1.0,user-scalable=no">
		<link href="${ctxStatic}/layer-v2.0/layer/skin/layui.css" type="text/css" rel="stylesheet">
	
	<script type="text/javascript">
// 	    function edit(){
// 	    	$("userid").removeAttr("disabled"); 
// 	    	$("sex").attr("disabled",false);
// 	    	$("userid").removeAttr("disabled")
// 	    }
	    function newSearch(){
	    	$("#searchForm").submit();
			return true;
	    }
	    $('#edit').click(function() {
			if($('#fieldset').is(':disabled')) {
				$('#fieldset').removeAttr('disabled');
			}
		});
	</script>
	<style type="text/css">
		.modal-content{
			margin:0 auto;
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
			<sys:message content="${message}"/>
			<!-- 查询条件 -->
	    	<div class="ibox-content">
				<div class="clearfix">
					<div class="nav" >
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
							<li role="presentation" class="layui-this"><a
										href="${ctx}/crm/orders/list?userId=${userId}">客户订单</a></li>
							<li role="presentation"><a
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
								href="${ctx}/crm/store/list?mobile=${userDetail.mobile}&stamp=1">投诉咨询</a></li>
						  </ul>
					</div>
					<!-- 工具栏 -->
				</div>
			</div>
			<!-- 查询条件 -->
	    	<div class="ibox-content">
	    	<div class="clearfix">
					<div class="row">
						<div class="col-sm-12">
							<form id="searchForm" 
								action="${ctx}/crm/orders/list" method="post" class="form-inline">
								<input id="pageNo" name="pageNo" type="hidden"
									value="${page.pageNo}" />
								<input id="pageSize" name="pageSize" type="hidden"
									value="${page.pageSize}" />
								<table:sortColumn id="orderBy" name="orderBy"
									value="${page.orderBy}" callback="sortOrRefresh();" />
								<!-- 支持排序 -->
								<input id="userId" name="userId" type="hidden" value="${userDetail.userId}" />
								<div class="form-group">
									<span>选择订单状态：</span> 
									<div class="input-group">
									  <select name="orderstatus" class=" form-control">
										<option value="0">请选择</option>
										<option value="0">全部</option>
										<option value="-2">取消订单</option>
										<option value="-1">待付款</option>
										<option value="1">待发货</option>
										<option value="2">待收货</option>
										<option value="3">已退款</option>
										<option value="4">已完成</option>
									  </select>
									</div>
									<span>请选择性质：</span> 
									<div class="input-group">
										<select name="distinction" class=" form-control">
											<option value="0">电商</option>
											<option value="1">售前卖</option>
											<option value="2">售后卖</option>
											<option value="3">老带新</option>
										 </select>
									</div>
									<input name="keyword" maxlength="50" 
									class=" form-control input-sm" style="width:280px" 
									placeholder="订单号，手机号码或者用户名"/>
									<div class="pull-right">
										<button
											class="btn btn-primary btn-rounded btn-outline btn-sm "
											onclick="newSearch()">
											<i class="fa fa-search"></i> 查询
										</button>
									</div>
								</div>
							</form>
						</div>
					</div>
				</div>
				<table id="contentTable" style="margin-top:20px"class="table table-striped table-bordered  table-hover table-condensed  dataTables-example dataTable no-footer">
					<thead>
						<tr>
						     <th style="text-align: center;">订单编号</th>
						     <th style="text-align: center;">订单区分</th>
						     <th style="text-align: center;">用户名</th>
						     <th style="text-align: center;">下单时间</th>
						     <th style="text-align: center;">支付时间</th>
						     <th style="text-align: center;">支付方式</th>
						     <th style="text-align: center;">实付总额</th>
						     <th style="text-align: center;">订单欠款</th>
						     <th style="text-align: center;">订单状态</th>
						     <th style="text-align: center;">评价状态</th>
						     <th style="text-align: center;">订单性质</th>
						     <th style="text-align: center;">操作</th>
						</tr>
					</thead>
					<tbody style="text-align: center;">
						<c:forEach items="${page.list}" var="order">
							<tr>
							  	<td>${order.orderid}</td>
							  	<c:if test="${order.isReal=='0'}">
							  	<td>实物订单</td>
							  	</c:if>
							  	<c:if test="${order.isReal=='1'}">
							  	<td>虚拟订单</td>
							  	</c:if>
								<td>${order.username}</td>	
								<td><fmt:formatDate value="${order.addtime }" pattern="yyyy-MM-dd "/></td>
							    <td><fmt:formatDate value="${order.paytime }" pattern="yyyy-MM-dd "/></td>	
							     <td>${order.payname }</td>	
							  	<td>${order.totalamount}</td>						
							    <td>${order.orderArrearage}</td>	
							    <td>
							  		    <c:if test="${empty order.orderstatus}">
							  			无数据
								  		</c:if>
									    <c:if test="${order.orderstatus=='-2'}">
									  		已取消
									  	</c:if>
									    <c:if test="${order.orderstatus=='-1'}">
									  		待付款
									  	</c:if>
									  	<c:if test="${order.orderstatus=='1'}">
									  		待发货
									  	</c:if>
									  	<c:if test="${order.orderstatus=='2'}">
									  		待收货
									  	</c:if>
									  	<c:if test="${order.orderstatus=='3'}">
									  		已退款(退货并退款使用)
									  	</c:if>
									  	<c:if test="${order.orderstatus=='4'}">
									  		已完成
									   	</c:if>
							    </td>
							    <td>
								  	<c:if test="${empty order.isComment}">
								  		无数据
								  	</c:if>
								    <c:if test="${order.isComment=='0'}">
								  		未评价
								  	</c:if>
								  	<c:if test="${order.isComment=='1'}">
								  		已评价
								  	</c:if>
							  	</td>
							  	<td>
								  	<c:if test="${empty order.distinction}">
								  		无数据
								  	</c:if>
								    <c:if test="${order.distinction==0}">
								  		电商
								  	</c:if>
								  	<c:if test="${order.distinction==1}">
								  		售前卖
								  	</c:if>
								  	<c:if test="${order.distinction==2}">
								  		售后卖
								  	</c:if>
								  	<c:if test="${order.distinction==3}">
								  		老带新
								  	</c:if>
							  	</td>
							  	<td>
			 						<a href="#" onclick="openDialogView('查看订单', '${ctx}/crm/orders/orderform?orderid=${order.orderid}&isReal=${order.isReal}&type=view','1100px','650px')" class="btn btn-info btn-xs" ><i class="fa fa-search-plus"></i> 订单详情</a>
					  			</td>
							</tr>
						</c:forEach>
					</tbody>
					<tfoot>
 						<tr>
                            <td colspan="20">
                                <!-- 分页代码 --> 
                                <div class="tfoot">
                                </div>
                               	<table:page page="${page}"></table:page>
                            </td>	
                        </tr>
                    </tfoot>
				</table>
			</div>	 
		</div>
	</div>
</body>
</html>