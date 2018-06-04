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
                    <form:form class="navbar-form navbar-left searcharea"  id="searchForm" modelAttribute="exercise" action="${ctx}/train/contractInfo/list" method="post">
						<input id="pageNo" name="pageNo" type="hidden" value="${page.pageNo}"/>
						<input id="pageSize" name="pageSize" type="hidden" value="${page.pageSize}"/>
                        <div class="form-group">
                            <label>所属机构：<input id="office_id" name="office_id" type="text" value="${contractInfo.office_id}" class="form-control"></label> 
                            <label>状态：
                            	<select name="status">
                            		<option value="0" <c:if test="${contractInfo.status eq '0'}">selected="selected"</c:if> >未签约</option>
                            		<option value="1" <c:if test="${contractInfo.status eq '1'}">selected="selected"</c:if> >已签约</option>
                            	</select>
                            </label>
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
                   			<th style="text-align: center;">所属商家</th>
                   			<th style="text-align: center;">所属机构</th>
                   			<th style="text-align: center;">状态</th>
                   			<th style="text-align: center;">签约人</th>
                   			<th style="text-align: center;">创建时间</th>
                   			<th style="text-align: center;">操作</th>
                   		</tr>
	                 </thead>
                     <tbody>
                       	<c:forEach items="${page.list}" var="info">
							<tr style="text-align: center;">
                                <td style="text-align: center;">
                               		${info.franchiseeName}
                               	</td>
                               	<td style="text-align: center;">
                               		${info.office_name}
                               	</td>
                               	<td style="text-align: center;">
                               		<c:if test="${info.status eq '0'}">未签约</c:if>
                               		<c:if test="${info.status eq '1'}">已签约</c:if>
                               	</td>
                                <td style="text-align: center;">
                               		${info.sign_username}
                               	</td>
                               	<td style="text-align: center;">
                               		${info.create_time}
                               	</td>
								<td style="text-align: center;">
									<shiro:hasPermission name="train:refundOrder:makeSureInAccount">
									<c:if test="${info.status eq '2'}">
										<a href="${ctx}/train/refundOrder/makeSureInAccount?order_id=${info.office_id}"  onclick="return confirmx('确定已入账？', this.href)" class="btn btn-success btn-xs" ><i class="fa fa-edit"></i>确认入账</a>
									</c:if>
									</shiro:hasPermission>
									<shiro:hasPermission name="train:articlelist:deleteOne">
										<a class="btn btn-success btn-xs"  onclick="openDialogView('对账单','${ctx}/train/refundOrder/queryStatementOfRefund?order_id=${info.office_id}', '800px', '500px')"><i class="fa fa-edit"></i>对账单详情</a>
									</shiro:hasPermission>
									<c:if test="${info.status eq '2'}">
										<a class="btn btn-success btn-xs"  onclick="openDialogView('支付信息','${ctx}/train/refundOrder/queryTransferpay?order_id=${info.office_id}', '800px', '500px')"><i class="fa fa-edit"></i>支付信息</a>
									</c:if>
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