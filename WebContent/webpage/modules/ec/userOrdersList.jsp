<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/webpage/include/taglib.jsp"%>
<html>
<head>
	<title>个人订单详情</title>
	<meta name="decorator" content="default"/>
    <meta name="viewport" content="width=device-width, initial-scale=1.0, minimum-scale=1.0, maximum-scale=1.0,user-scalable=no">
    <script type="text/javascript">
    function page(n,s){
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
			<div class="ibox-title">
				<h5>个人订单详情</h5>
			</div>
			<sys:message content="${message}"/>
			<!-- 查询条件 -->
	    	<div class="ibox-content">
				<div class="clearfix">
					<form id="searchForm" modelAttribute="" action="${ctx}/ec/mtmyuser/lookOrderByUser" method="post" class="navbar-form navbar-left searcharea">
						<!-- 翻页隐藏文本框 -->
						<input id="pageNo" name="pageNo" type="hidden" value="${page.pageNo}"/>
						<input id="pageSize" name="pageSize" type="hidden" value="${page.pageSize}"/>
						<input type="hidden" name="userid" value="${userid }">
						<table:sortColumn id="orderBy" name="orderBy" value="${page.orderBy}" callback="sortOrRefresh();"/><!-- 支持排序 -->
					</form>
				</div>
				<table id="contentTable" class="table table-striped table-bordered  table-hover table-condensed  dataTables-example dataTable no-footer">
					<thead>
						<tr>
							 <th style="text-align: center;">订单号</th>
						     <th style="text-align: center;">姓名</th>
						     <th style="text-align: center;">订单状态</th>
						     <th style="text-align: center;">支付金额</th>
						     <th style="text-align: center;">创建时间</th>
						     <th style="text-align: center;">操作</th>
						</tr>
					</thead>
					<tbody style="text-align: center;">
						<c:forEach items="${page.list}" var="orders">
							<tr>
								<td>${orders.orderid}</td>
								<td>${orders.username}</td>	
								<td>		
									${fns:getDictLabel(orders.orderstatus, 'order_status', '')}
								</td>
								<td>${orders.orderamount}</td>
								<td><fmt:formatDate value="${orders.addtime}" pattern="yyyy-MM-dd HH:mm:ss" /></td>
							    <td>
							    	<a href="#" onclick="openDialogView('查看订单', '${ctx}/ec/orders/orderform?orderid=${orders.orderid}&isReal=${orders.isReal}&type=view','1100px','650px')" class="btn btn-info btn-xs" ><i class="fa fa-search-plus"></i> 订单详情</a>
							    	<%-- <a href="#" onclick="openDialogView('查看订单详情', '${ctx}/ec/mtmyuser/userOrdersFrom?orderid=${orders.orderid}','800px','700px')" class="btn btn-info btn-xs" ><i class="fa fa-search-plus"></i> 查看详情</a> --%>
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
	<div class="loading"></div>
</body>
</html>