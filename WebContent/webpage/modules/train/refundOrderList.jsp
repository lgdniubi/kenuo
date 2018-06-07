<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/webpage/include/taglib.jsp"%>
<%@ include file="/webpage/include/icheck.jsp"%>
<html>
<head>
    <meta charset="utf-8">
    <meta name="decorator" content="default"/>
    <meta name="viewport" content="width=device-width, initial-scale=1.0, minimum-scale=1.0, maximum-scale=1.0,user-scalable=no">
     <link rel="stylesheet" href="${ctxStatic}/train/css/exam.css">
      <script>
	   /*  $(document).ready(function () {
	    	if($('#nowcateId').val()!=''){
	       		document.getElementById("cateId").value=$('#nowcateId').val();
	    	}
	    }); */
	   function search(){//查询，页码清零
			$("#pageNo").val(0);
			$("#searchForm").submit();
	   }
		function resetnew(){//重置，页码清零
			$("#pageNo").val(0);
			$("#orderId").val("");
			$("#searchForm").submit();
	 	 }
		function page(n,s){//翻页
			$("#pageNo").val(n);
			$("#pageSize").val(s);
			$("#searchForm").submit();
		}
		function makesure(){
			
		}
    </script>
    <title>文章管理</title>
</head>
<body>
	<div class="wrapper-content">
        <div class="ibox">
            <div class="ibox-title">
                <h5>信用账单管理</h5>
            </div>
            <sys:message content="${message}"/>
            <div class="ibox-content">
                <div class=" clearfix">
                    <form:form class="navbar-form navbar-left searcharea"  id="searchForm" modelAttribute="exercise" action="${ctx}/train/refundOrder/list" method="post">
                    	<!-- 分页隐藏文本框 -->
						<input id="pageNo" name="pageNo" type="hidden" value="${page.pageNo}"/>
						<input id="pageSize" name="pageSize" type="hidden" value="${page.pageSize}"/>
                        <div class="form-group">
                            <label>账单编号：<input id="orderId" name="orderId" type="text" value="${refundOrder.orderId}" class="form-control" placeholder="搜索账单编号"></label> 
                           
                        </div>
                        <div class="pull-right">
							<button  class="btn btn-primary btn-rounded btn-outline btn-sm " onclick="search()" ><i class="fa fa-search"></i> 查询</button>
							<button  class="btn btn-primary btn-rounded btn-outline btn-sm " onclick="resetnew()" ><i class="fa fa-refresh"></i> 重置</button>
						</div>
                    </form:form>
                </div>
                <table id="treeTable" class="table table-bordered table-hover table-striped">
	                 <thead> 
	                   	 <tr>
                   			<!-- <th style="text-align: center;"><label for="i-checks"><input type="checkbox" name="" id="i-checks" class="i-checks"></label></th> -->
                   			<th style="text-align: center;">账单编号</th>
                   			<!-- <th style="text-align: center;">临时订单号</th> -->
                   			<th style="text-align: center;">账单类型</th>
                   			<th style="text-align: center;">账单月份</th>
                   			<th style="text-align: center;">欠款金额</th>
                   			<th style="text-align: center;">欠款商家</th>
                   			<th style="text-align: center;">欠款机构</th>
                   			<th style="text-align: center;">实付金额</th>
                   			<th style="text-align: center;">账单状态</th>
                   			<th style="text-align: center;">支付类型</th>
                   			<th style="text-align: center;">支付用户</th>
                   			<th style="text-align: center;">渠道</th>
                   			<th style="text-align: center;">创建时间</th>
                   			<th style="text-align: center;">支付时间</th>
                   			<th style="text-align: center;">操作</th>
                   		</tr>
	                 </thead>
                     <tbody>
                       	<c:forEach items="${page.list}" var="refund">
							<tr style="text-align: center;">
                                <td style="text-align: center;">
                               		${refund.orderId}
                               	</td>
                               	<%-- <td style="text-align: center;">
                               		${refund.tempOrderId}
                               	</td> --%>
                               	<td style="text-align: center;">
                               		<c:if test="${refund.orderType eq '1'}">线上</c:if>
                               		<c:if test="${refund.orderType eq '2'}">线下</c:if>
                               	</td>
                               	<td style="text-align: center;">
                               		${refund.billmonth}
                               	</td>
                                <td style="text-align: center;">
                               		${refund.arrearagePrice}
                               	</td>
                               	<td style="text-align: center;">
                               		${refund.franchiseeName}
                               	</td>
                               	<td style="text-align: center;">
                               		${refund.arrearageOfficeName}
                               	</td>
                               	<td style="text-align: center;">
                               		${refund.amount}
                               	</td>
                               	<td style="text-align: center;">
                               		<c:if test="${refund.orderStatus eq '1'}">待支付</c:if>
                               		<c:if test="${refund.orderStatus eq '2'}">待审核</c:if>
                               		<c:if test="${refund.orderStatus eq '3'}">已入账</c:if>
                               	</td>
                               	<td style="text-align: center;">
                               		${refund.payCode}
                               	</td>
                               	<td style="text-align: center;">
                               		${refund.userName}
                               	</td>
                               	<td style="text-align: center;">
                               		<c:if test="${refund.channelFlag eq 'wx'}">微信</c:if>
                               		<c:if test="${refund.channelFlag eq 'alipay'}">支付宝</c:if>
                               		<c:if test="${refund.channelFlag eq 'zz'}">转账</c:if>
                               	</td>
                               	<td style="text-align: center;">
                               		${refund.addTime}
                               	</td>
                               	<td style="text-align: center;">
                               		${refund.payTime}
                               	</td>
								<td style="text-align: center;">
									<shiro:hasPermission name="train:refundOrder:makeSureInAccount">
									<c:if test="${refund.orderType eq '2'}">
										<c:if test="${refund.orderStatus eq '2'}">
											<a href="${ctx}/train/refundOrder/makeSureInAccount?office_id=${refund.arrearageOffice}&order_id=${refund.orderId}&amount=${refund.amount}&user_id=${refund.user_id}"  onclick="return confirmx('确定已入账？', this.href)" class="btn btn-success btn-xs" ><i class="fa fa-edit"></i>确认入账</a>
										</c:if>
									</c:if>
									</shiro:hasPermission>
									<shiro:hasPermission name="train:articlelist:deleteOne">
										<a class="btn btn-success btn-xs"  onclick="openDialogView('对账单','${ctx}/train/refundOrder/queryStatementOfRefund?office_id=${refund.arrearageOffice}&billmonth=${refund.billmonth }', '800px', '500px')"><i class="fa fa-edit"></i>对账单详情</a>
									</shiro:hasPermission>
									<a class="btn btn-success btn-xs"  onclick="openDialogView('详细信息','${ctx}/train/refundOrder/queryRefundOrderDetail?order_id=${refund.orderId}', '800px', '500px')"><i class="fa fa-edit"></i>详细信息</a>
									
								</td>
							</tr>
						</c:forEach>
                     </tbody>
                     <tfoot>
                        <tr>
                            <td colspan="20">
                                <div class="tfoot">
                                </div>
                                <!-- 分页代码 --> 
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