<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/webpage/include/taglib.jsp"%>
<%@ include file="/webpage/include/icheck.jsp"%>
<html>
<head>
    <meta charset="utf-8">
    <meta name="decorator" content="default"/>
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
    <title>信用机构列表</title>
</head>
<body>
	<div class="wrapper-content">
        <div class="ibox">
            <div class="ibox-title">
                <h5>信用机构列表</h5>
            </div>
            <sys:message content="${message}"/>
            <div class="ibox-content">
                <div class=" clearfix">
                    <form:form class="navbar-form navbar-left searcharea"  id="searchForm" modelAttribute="officeAcount" action="${ctx}/sys/officeCredit/list" method="post">
                    	<!-- 分页隐藏文本框 -->
						<input id="pageNo" name="pageNo" type="hidden" value="${page.pageNo}"/>
						<input id="pageSize" name="pageSize" type="hidden" value="${page.pageSize}"/>
                        <%-- <div class="form-group">
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
                           
                        </div> --%>
                        <div class="pull-right">
							<button  class="btn btn-primary btn-rounded btn-outline btn-sm " onclick="search()" ><i class="fa fa-search"></i> 查询</button>
							<button  class="btn btn-primary btn-rounded btn-outline btn-sm " onclick="resetnew()" ><i class="fa fa-refresh"></i> 重置</button>
						</div>
                    </form:form>
                </div>
                
                <table id="contentTable" class="table table-bordered table-hover table-striped">
	                 <thead> 
	                   	 <tr>
                   			<th style="text-align: center;">信用机构</th>
                   			<th style="text-align: center;">所属商家</th>
                   			<th style="text-align: center;">总额度</th>
                   			<th style="text-align: center;">欠款</th>
                   			<th style="text-align: center;">操作</th>
                   		</tr>
	                 </thead>
                     <tbody>
                       	<c:forEach items="${page.list}" var="acount">
							<tr style="text-align: center;">
                                <td style="text-align: center;">
                               		${acount.officeName}
                               	</td>
                               	<td style="text-align: center;">
                               		${acount.companyName}
                               	</td>
                               	<td style="text-align: center;">
                               		${acount.creditLimit}
                               	</td>
                               	<td style="text-align: center;">
                               		${acount.creditLimit - acount.usedLimit}
                               	</td>
								<td style="text-align: center;">
									<shiro:hasPermission name="sys:office:editCredit">
										<button class="btn btn-white btn-sm " data-toggle="tooltip" data-placement="left" onclick='top.openTab("${ctx}/sys/officeCredit/toEditCredit?office_id=${acount.officeId}","额度管理", false)'>额度管理</button>	
										<button class="btn btn-white btn-sm " data-toggle="tooltip" data-placement="left" onclick='top.openTab("${ctx}/sys/officeCredit/creditLogList?officeId=${acount.officeId}","额度日志", false)'>额度日志</button>	
									</shiro:hasPermission>
									 <shiro:hasPermission name="sys:office:refundList">
										<button class="btn btn-white btn-sm " data-toggle="tooltip" data-placement="left" onclick='top.openTab("${ctx}/sys/officeCredit/refundList?arrearageOffice=${acount.officeId}","还款记录", false)'>还款记录</button>	
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