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
			$("#orderStatus").val("");
			$("#orderType").val("");
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
    <title>还款记录</title>
</head>
<body>
	<div class="wrapper-content">
        <div class="ibox">
            <div class="ibox-title">
                <h5>还款记录</h5>
            </div>
            <sys:message content="${message}"/>
            <div class="ibox-content">
                <div class=" clearfix">
                    <form:form class="navbar-form navbar-left searcharea"  id="searchForm" modelAttribute="refundOrder" action="${ctx}/sys/officeCredit/refundList" method="post">
                    	<!-- 分页隐藏文本框 -->
						<input id="pageNo" name="pageNo" type="hidden" value="${page.pageNo}"/>
						<input id="pageSize" name="pageSize" type="hidden" value="${page.pageSize}"/>
						<form:hidden path="arrearageOffice"/>
                        <div class="form-group">
                            <label>还款状态：</label> 
                            	<form:select class="form-control "  path="orderStatus">
									<form:option value=''>全部</form:option>
									<form:option value='2'>待审核</form:option>
									<form:option value='3'>已入账</form:option>
									<form:option value='4'>已驳回</form:option>
									<form:option value='5'>已取消</form:option>
								</form:select>
                            <label>支付类型：</label> 
                            	<form:select class="form-control "  path="orderType">
									<form:option value=''>全部</form:option>
									<form:option value='1'>线上支付</form:option>
									<form:option value='2'>线下支付</form:option>
								</form:select>
                           
                        </div>
                        <div class="pull-right">
							<button  class="btn btn-primary btn-rounded btn-outline btn-sm " onclick="search()" ><i class="fa fa-search"></i> 查询</button>
							<button  class="btn btn-primary btn-rounded btn-outline btn-sm " onclick="resetnew()" ><i class="fa fa-refresh"></i> 重置</button>
						</div>
                    </form:form>
                </div>
                <table id="contentTable" class="table table-bordered table-hover table-striped">
	                 <thead> 
	                   	 <tr>
                   			<th style="text-align: center;">还款单号</th>
                   			<th style="text-align: center;">实付金额</th>
                   			<th style="text-align: center;">支付人</th>
                   			<th style="text-align: center;">支付类型</th>
                   			<th style="text-align: center;">还款状态</th>
                   			<th style="text-align: center;">账单时间</th>
                   			<th style="text-align: center;">操作</th>
                   		</tr>
	                 </thead>
                     <tbody>
                       	<c:forEach items="${page.list}" var="refund">
							<tr style="text-align: center;">
                                <td style="text-align: center;">
                               		${refund.orderId}
                               	</td>
                               	<td style="text-align: center;">
                               		${refund.amount}
                               	</td>
                               	<td style="text-align: center;">
                               		${refund.userName}
                               	</td>
                               	<td style="text-align: center;">
                               		<c:if test="${refund.orderType eq '1'}">线上支付</c:if>
                               		<c:if test="${refund.orderType eq '2'}">线下支付</c:if>
                               	</td>
                               	<td style="text-align: center;">
                               		<c:if test="${refund.orderStatus eq '2'}">待审核</c:if>
                               		<c:if test="${refund.orderStatus eq '3'}">已入账</c:if>
                               		<c:if test="${refund.orderStatus eq '4'}">已驳回</c:if>
                               		<c:if test="${refund.orderStatus eq '5'}">已取消</c:if>
                               	</td>
                               	<td style="text-align: center;">
                               		${refund.addTime}
                               	</td>
								<td style="text-align: center;">
									 <shiro:hasPermission name="train:articlelist:deleteOne">
										<c:if test="${refund.orderStatus ne '2'}">
<%-- 										<a class="btn btn-success btn-xs"  onclick="openDialogView('对账单','${ctx}/train/refundOrder/queryStatementOfRefund?order_id=${refund.orderId }', '800px', '500px')"><i class="fa fa-edit"></i>对账单详情</a> --%>
												详情
										</c:if>
									</shiro:hasPermission>
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