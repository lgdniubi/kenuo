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
	    function save(){
	    	$("#inputForm").submit();
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
						<div class="form-group">
						<p><span>${userDetail.name}</span>的客户档案--请注意保密</p>
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
							<li role="presentation" class="layui-this"><a
										href="${ctx}/crm/coin/list?userId=${userId}">云币明细</a></li>
							<li role="presentation"><a
										href="${ctx}/crm/user/account?userId=${userId}">账户总览</a></li>
							<li role="presentation"><a
										href="${ctx}/crm/invitation?userId=${userId}">邀请明细</a></li>
							<li role="presentation"><a
										href="${ctx}/crm/returnRecord?userId=${userId}">回访记录</a></li>
							<li role="presentation"><a href="#">投诉咨询</a></li>
						  </ul>
					</div>
					<!-- 工具栏 -->
				</div>
			</div>
			<sys:message content="${message}"/>
			<!-- 查询条件 -->
	    	<div class="ibox-content">
	    	
			<table id="contentTable" class="table table-striped table-bordered  table-hover table-condensed  dataTables-example dataTable no-footer">
				<thead>
						<tr>
						     <th style="text-align: center;">订单单号</th>
						     <th style="text-align: center;">商品名称</th>
						     <th style="text-align: center;">单价</th>
						     <th style="text-align: center;">数量</th>
						     <th style="text-align: center;">返币比率</th>
						     <th style="text-align: center;">返币金额</th>
						     <th style="text-align: center;">返币日期</th>
						     <th style="text-align: center;">结算状态</th>
						     <th style="text-align: center;">备注</th>
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
							    <td><c:if test="${empty order.orderstatus}">
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
							  		已退款(退货并退款使用)
							  	</c:if>
							  	<c:if test="${order.orderstatus=='3'}">
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
							  		>已评价
							  	</c:if></td>
							  	<td>
<%-- 							  		<shiro:hasPermission name="ec:mtmyuser:lookOrderByUser"> --%>
							  			<a class="btn btn-info btn-xs">取消</a>
							  			<a class="btn btn-info btn-xs">售后</a>
							  			<a class="btn btn-info btn-xs">详情</a>
<%-- 						  			</shiro:hasPermission> --%>
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