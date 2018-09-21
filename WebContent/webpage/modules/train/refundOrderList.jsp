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
			$("#orderStatus").val("");
			$("#orderType").val("");
			$("#companyName").val("");
			$("#companyId").val("");
			$("#officeName").val("");
			$("#officeId").val("");
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
                    <form:form class="navbar-form navbar-left searcharea"  id="searchForm" modelAttribute="refundOrder" action="${ctx}/train/refundOrder/list" method="post">
                    	<!-- 分页隐藏文本框 -->
						<input id="pageNo" name="pageNo" type="hidden" value="${page.pageNo}"/>
						<input id="pageSize" name="pageSize" type="hidden" value="${page.pageSize}"/>
                        <div class="form-group">
                            <label>还款单编号：<input id="orderId" name="orderId" type="text" value="${refundOrder.orderId}" class="form-control" placeholder="搜索账单编号"></label> 
                            <label>账单状态：</label> 
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
                           <span>还款商家：</span>
								<sys:treeselect id="company" name="franchiseeId" value="${refundOrder.franchiseeId}" labelName="franchiseeName" 
									labelValue="${refundOrder.franchiseeName}" title="公司" 
									url="/sys/franchisee/treeData" cssClass=" form-control input-sm" allowClear="true" />
                           <span>还款机构：</span>
								<sys:treeselect id="office" name="arrearageOffice" value="${refundOrder.arrearageOffice}" labelName="arrearageOfficeName" labelValue="${refundOrder.arrearageOfficeName}" title="部门"
									url="/sys/office/treeData?type=2" cssClass=" form-control input-sm" allowClear="true"
									notAllowSelectRoot="false" notAllowSelectParent="false" />
                        </div>
                        <div class="pull-right">
							<button  class="btn btn-primary btn-rounded btn-outline btn-sm " onclick="search()" ><i class="fa fa-search"></i> 查询</button>
							<button  class="btn btn-primary btn-rounded btn-outline btn-sm " onclick="resetnew()" ><i class="fa fa-refresh"></i> 重置</button>
						</div>
                    </form:form>
                </div>
                <div class="row">
					<div class="col-sm-12">
						<div class="pull-left">
							<shiro:hasPermission name="train:refundOrder:audit">
							<table:audit url="${ctx}/train/refundOrder/audit" id="contentTable"></table:audit>
							</shiro:hasPermission>
							<!-- 导出按钮 -->
							<shiro:hasPermission name="train:refundOrder:export">
							<table:exportExcel url="${ctx}/train/refundOrder/export"></table:exportExcel>
							</shiro:hasPermission>
						</div>
					</div>
				</div>
                <table id="contentTable" class="table table-bordered table-hover table-striped">
	                 <thead> 
	                   	 <tr>
                   			<!-- <th style="text-align: center;"><label for="i-checks"><input type="checkbox" name="" id="i-checks" class="i-checks"></label></th> -->
                   			<th style="text-align: center;"><input type="checkbox" class="i-checks"></th>
                   			<th style="text-align: center;">还款单号</th>
                   			<th style="text-align: center;">还款商家</th>
                   			<th style="text-align: center;">还款机构</th>
                   			<th style="text-align: center;">实付金额</th>
                   			<th style="text-align: center;">支付途径</th>
                   			<th style="text-align: center;">账单状态</th>
                   			<th style="text-align: center;">账单时间</th>
                   			<th style="text-align: center;">操作</th>
                   		</tr>
	                 </thead>
                     <tbody>
                       	<c:forEach items="${page.list}" var="refund">
							<tr style="text-align: center;">
								<td><input type="checkbox" id="${refund.orderId}" class="i-checks" <c:if test="${refund.orderStatus ne '2'}">disabled = true</c:if>></td>
                                <td style="text-align: center;">
                               		${refund.orderId}
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
                               		<c:if test="${refund.orderType eq '1'}">线上支付</c:if>
                               		<c:if test="${refund.orderType eq '2'}">线下支付</c:if>
                               	</td>
                               	<td style="text-align: center;">
<%--                                		<c:if test="${refund.orderStatus eq '1'}">待支付</c:if> --%>
                               		<c:if test="${refund.orderStatus eq '2'}">待审核</c:if>
                               		<c:if test="${refund.orderStatus eq '3'}">已入账</c:if>
                               		<c:if test="${refund.orderStatus eq '4'}">已驳回</c:if>
                               		<c:if test="${refund.orderStatus eq '5'}">已取消</c:if>
                               	</td>
                               	<td style="text-align: center;">
                               		${refund.addTime}
                               	</td>
								<td style="text-align: center;">
									<shiro:hasPermission name="train:refundOrder:makeSureInAccount">
										<c:if test="${refund.orderStatus eq '2'}">
											<%-- <a href="${ctx}/train/refundOrder/makeSureInAccount?office_id=${refund.arrearageOffice}&order_id=${refund.orderId}&amount=${refund.amount}&billmonth=${refund.billmonth}"  onclick="return confirmx('确定已入账？', this.href)" class="btn btn-success btn-xs" ><i class="fa fa-edit"></i>确认入账</a> 
											<a class="btn btn-success btn-xs"  onclick="openDialog('审核','${ctx}/train/refundOrder/toAuditRefundOrder?office_id=${refund.arrearageOffice}&order_id=${refund.orderId}&amount=${refund.amount}&opflag=0', '700px', '300px')"><i class="fa fa-edit"></i>审核</a>--%>
											<button class="btn btn-success btn-xs " data-toggle="tooltip" data-placement="left" onclick='top.openTab("${ctx}/train/refundOrder/queryRefundOrderDetail?order_id=${refund.orderId }&opflag=0","审核", false)'>审核</button>	
										</c:if>
									</shiro:hasPermission>
									 <shiro:hasPermission name="train:articlelist:deleteOne">
										<c:if test="${refund.orderStatus ne '2'}">
										<button class="btn btn-success btn-xs " data-toggle="tooltip" data-placement="left" onclick='top.openTab("${ctx}/train/refundOrder/queryRefundOrderDetail?order_id=${refund.orderId }&opflag=1","详情", false)'>详情</button>	
										
										</c:if>
									</shiro:hasPermission>
									<%-- 	<a class="btn btn-success btn-xs"  onclick="openDialogView('账单日志','${ctx}/train/refundOrder/queryRefundOrderLogList?order_id=${refund.orderId}', '800px', '500px')"><i class="fa fa-edit"></i>账单日志</a>
								<button class="btn btn-success btn-xs " data-toggle="tooltip" data-placement="left" onclick='top.openTab("${ctx}/train/refundOrder/queryRefundOrderDetail?order_id=${refund.orderId}","详细信息", false)'>详细信息</button>	queryStatementOfRefund --%>
									
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