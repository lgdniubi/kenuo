<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/webpage/include/taglib.jsp"%>
<%@ include file="/webpage/include/icheck.jsp"%>
<html>
<head>
    <meta charset="utf-8">
    <meta name="decorator" content="default"/>
    <meta name="viewport" content="width=device-width, initial-scale=1.0, minimum-scale=1.0, maximum-scale=1.0,user-scalable=no">
     <link rel="stylesheet" href="${ctxStatic}/train/css/exam.css">
     <!-- 引入layui.css -->
	<link href="${ctxStatic}/layer-v2.0/layer/skin/layui.css" type="text/css" rel="stylesheet">
    <title>对账单列表</title>
</head>
<body>
	<div class="ibox-content">
		<div class="clearfix">
			<div class="nav">
				<ul class="layui-tab-title">
					<li >
						<a href="${ctx}/train/refundOrder/queryRefundOrderDetail?opflag=0&order_id=${order_id}">账单详情</a>
					</li>
					<li  class="layui-this">
						<a href="${ctx}/train/refundOrder/queryStatementOfRefund?order_id=${order_id}">对账单信息</a>
					</li>
					
				</ul>
			</div>
		</div>
	</div>
	<div class="wrapper-content">
        <div class="ibox">
            <div class="ibox-title">
                <h5>对账单</h5>
            </div>
            <sys:message content="${message}"/>
            <div class="ibox-content">
                <table id="treeTable" class="table table-bordered table-hover table-striped">
	                 <thead> 
	                   	 <tr>
                   			<th style="text-align: center;">订单编号</th>
                   			<th style="text-align: center;">使用额度</th>
                   			<th style="text-align: center;">来源</th>
                   			<th style="text-align: center;">类型</th>
                   			<th style="text-align: center;">创建时间</th>
                   		</tr>
	                 </thead>
                     <tbody>
                       	<c:forEach items="${statements}" var="state">
							<tr style="text-align: center;">
                                <td style="text-align: center;">
                               		${state.orderId}
                               	</td>
                               	<td style="text-align: center;">
                               		${state.usedLimit}
                               	</td>
                               	<td style="text-align: center;">
                               		<c:if test="${state.from eq '0'}">订单</c:if>
                               		<c:if test="${state.from eq '1'}">取消订单</c:if>
                               		<c:if test="${state.from eq '2'}">售后</c:if>
                               	</td>
                               	<td style="text-align: center;">
                               		<c:if test="${state.type eq '0'}">收入</c:if>
                               		<c:if test="${state.type eq '1'}">支出</c:if>
                               	</td>
                               	<td style="text-align: center;">
                               		${state.createTime}
                               	</td>
							</tr>
						</c:forEach>
                     </tbody>
                </table>
	        </div>
	    </div>
    </div>
</body>
</html>