<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/webpage/include/taglib.jsp"%>

<html>
<head>
	<meta name="decorator" content="default"/>
</head>
<body class="gray-bg">
	<div class="wrapper wrapper-content">
		<div class="ibox">
			<div class="ibox-title">
				<h5>订单列表</h5>
			</div>
			 <sys:message content="${message}"/>
			 	<!-- 查询条件 -->
				<div class="ibox-content">
				<div class="clearfix">
				<form:form id="searchForm" modelAttribute="orders" action="${ctx}/ec/orders/returnedOrdersList" method="post" class="form-inline">
					<input id="pageNo" name="pageNo" type="hidden" value="${page.pageNo}"/>
					<input id="pageSize" name="pageSize" type="hidden" value="${page.pageSize}"/>
					<input id="returnedId" name="returnedId" value="${returnedId}" type="hidden">
					<table:sortColumn id="orderBy" name="orderBy" value="${page.orderBy}" callback="sortOrRefresh();"/><!-- 支持排序 -->
				</form:form>
				</div>
				<p></p>
				<table id="contentTable" class="table table-striped table-bordered  table-hover table-condensed  dataTables-example dataTable no-footer">
					<thead>
						<tr>
							<th style="text-align: center;">订单号</th>
							<th style="text-align: center;">订单区分</th>
							<th style="text-align: center;">用户名</th>
							<th style="text-align: center;">订单状态</th>
							<th style="text-align: center;">取消类型</th>
							<th style="text-align: center;">应付金额</th>
							<th style="text-align: center;">商品种类</th>
							<th style="text-align: center;">支付方式</th>
							<th style="text-align: center;">创建类型</th>
							<th style="text-align: center;">订单类型</th>
							<th style="text-align: center;">创建时间</th>
							<th style="text-align: center;">操作</th>
						</tr>
					</thead>
					<tbody style="text-align: center;">
					<c:forEach items="${page.list}" var="orders">
						<tr>
							<td>${orders.orderid}</td>
							<td>
								<c:if test="${orders.isReal==0}">实物订单</c:if>
								<c:if test="${orders.isReal==1}">虚拟订单</c:if>
								<c:if test="${orders.isReal==2}">套卡订单</c:if>
								<c:if test="${orders.isReal==3}">通用卡订单</c:if>
								<input type="hidden" id="isReal" value="${orders.isReal==0}" />
							</td>
							<td>${orders.username}</td>		
							<td>	
								<c:if test="${orders.orderstatus==-2}">
									取消订单
								</c:if>
								<c:if test="${orders.orderstatus==-1}">
									待付款
								</c:if>
								<c:if test="${orders.orderstatus==1}">
									待发货
								</c:if>
								<c:if test="${orders.orderstatus==2}">
									待收货
								</c:if>
								<c:if test="${orders.orderstatus==3}">
									已退款
								</c:if>
								<c:if test="${orders.orderstatus==4}">
									已完成
								</c:if>
								<c:if test="${orders.orderstatus==5}">
									申请退款
								</c:if>
							</td>
							<td>	
								${orders.cancelType}
							</td>
							<td>${orders.orderamount}</td>
							<td>${orders.goodsnum}</td>
							<td>${orders.payname}</td>
							<td>
								<c:if test="${orders.channelFlag== 'wap'}">
									wap端
								</c:if>
								<c:if test="${orders.channelFlag== 'ios'}">
									苹果手机
								</c:if>
								<c:if test="${orders.channelFlag=='android'}">
									安卓手机
								</c:if>
								<c:if test="${orders.channelFlag=='bm'}">
									后台管理
								</c:if>
							</td>
							<td>
								<c:if test="${orders.delFlag == 0}">
									正常
								</c:if>
								<c:if test="${orders.delFlag == 1}">
									用户删除
								</c:if>
							</td>
							<td><fmt:formatDate value="${orders.addtime}" pattern="yyyy-MM-dd HH:mm:ss" /> </td>
							<td>
		 						<c:if test="${orders.isReal == 0 || orders.isReal == 1}">
			 						<a href="#" onclick="openDialogView('查看订单', '${ctx}/ec/orders/orderform?orderid=${orders.orderid}&isReal=${orders.isReal}&type=view','1100px','650px')" class="btn btn-info btn-xs" ><i class="fa fa-search-plus"></i> 订单详情</a>
		 						</c:if>
		 						<c:if test="${orders.isReal == 2 || orders.isReal == 3}">
		 					 		<a href="#" onclick="openDialogView('查看订单', '${ctx}/ec/orders/cardOrdersForm?orderid=${orders.orderid}&isReal=${orders.isReal}&type=view','1100px','650px')" class="btn btn-info btn-xs" ><i class="fa fa-search-plus"></i> 订单详情</a>
		 						</c:if>
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