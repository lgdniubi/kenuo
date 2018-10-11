<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/webpage/include/taglib.jsp"%>
<%-- <%@ include file="/webpage/include/icheck.jsp"%> --%>
<%-- <%@ page import="com.training.modules.sys.utils.ParametersFactory"%> --%>
<html>
<head>
	<title>对账单详情</title>
	<meta name="decorator" content="default"/>
	<!-- 引入layui.css -->
	<link href="${ctxStatic}/layer-v2.0/layer/skin/layui.css" type="text/css" rel="stylesheet">
	<style>
		body{padding-bottom: 80px;}
		.fixed-footer{position: fixed;left: 0;bottom: 0;width: 100%;box-sizing: border-box;z-index: 9999;background:#fff;border-top: 1px solid #dcdcdc;line-height: 35px;padding: 20px;text-align: right;}
		.fixed-footer .btns{display: inline-block;line-height: 35px;width: 85px;text-align: center;font-size: 14px;color: #fff;margin-left: 20px;border-radius: 6px;cursor: pointer}
		.yes-btn{background: #4d90fe;}
		.cancel-btn{background: #999;}
		
		
		/*{margin: 0;padding:0;} */
		/*查看凭证，页面样式 */
		#jqPhoto{position: fixed;left: 0;top: 0;width: 100%;height: 100%; z-index: 99999;background-color: rgba(0, 0, 0,.6);display: none;}
		#jqPhoto #colorBtn{position: absolute;right: 23px;top: 16px;width: 17px;height: 15px;background: url(${ctxStatic}/train/images/close.png) center no-repeat;background-size: 15px;cursor: pointer;z-index: 999;opacity: .8;}
		#jqPhotoPage{position: absolute;left:0;top:0;width: 100%;height: 100%;border:none;overflow: hidden;}
		
	</style>
	<!-- 放大图片js -->
	<script type="text/javascript" src="${ctxStatic}/train/imgZoom/jquery.imgZoom.js"></script>
	
	<script type="text/javascript">
		function auditPass(){
			var msg = "确认入账？";
			top.layer.confirm(msg, {icon: 3, title:'系统提示'}, function(index){
				top.layer.close(index);
				$("#status").val('3');
			    $("#inputForm").submit();
			});
		    
		} 
		function auditNoPass(){
			var amount =  $("#amount").val();
			var orderId =  $("#orderId").val();
			var officeId =  $("#officeId").val();
			var URL = "${ctx}/train/refundOrder/toAuditRefundOrder?office_id="+officeId+"&order_id="+orderId+"&amount="+amount;
			openDialog('审核',URL, '300px', '300px');
		} 
	</script>
</head>
<body>
	<div class="ibox-content">
		<div class="clearfix">
			<div class="nav">
				<ul class="layui-tab-title">
					<li  class="layui-this">
						<a href="${ctx}/train/refundOrder/queryRefundOrderDetail?opflag=0&order_id=${refundOrder.orderId}">账单详情</a>
					</li>
					<li >
						<a href="${ctx}/train/refundOrder/queryStatementOfRefund?order_id=${refundOrder.orderId }">对账单信息</a>
					</li>
					
				</ul>
			</div>
		</div>
	</div>
	<form:form id="inputForm" modelAttribute="refundOrder" action="${ctx}/train/refundOrder/makeSureInAccount" method="post" class="form-horizontal">
		<sys:message content="${message}"/>
		<input type="hidden" name="order_id" id="orderId" value="${refundOrder.orderId}"/>
		<input type="hidden" name="office_id" id="officeId" value="${refundOrder.arrearageOffice}"/>
		<input type="hidden" name="amount" id="amount" value="${refundOrder.amount}"/>
		<input type="hidden" name="status" id="status" value="3"/>
		<table class="table table-bordered  table-condensed dataTables-example dataTable no-footer">
			<tbody>
			    <tr>
			    	<td align="center" class="active" style="height:1px;border-top:2px solid #555555;" colspan="3"><label class="pull-left">还款信息:</label></td>
			         <td style="height:1px;border-top:2px solid #555555; color: red;font-weight:bold" >
<%-- 			         	<c:if test="${refundOrder.orderStatus eq '1'}">待支付</c:if> --%>
			         	<c:if test="${refundOrder.orderStatus eq '2'}">待审核</c:if>
			         	<c:if test="${refundOrder.orderStatus eq '3'}">已入账</c:if>
			         	<c:if test="${refundOrder.orderStatus eq '4'}">已驳回</c:if>
			         	<c:if test="${refundOrder.orderStatus eq '5'}">已取消</c:if>
			         </td>
				</tr>
				<tr>
			         <td class="active"><label class="pull-right">还款商家:</label></td>
			         <td >${refundOrder.franchiseeName}</td>
			         <td class="active"><label class="pull-right">还款机构:</label></td>
			         <td >${refundOrder.arrearageOfficeName}</td>
				</tr>
				<tr>
			         <td class="active"><label class="pull-right">支付类型:</label></td>
			         <td >
			         <c:if test="${refundOrder.orderType eq '1'}">线上</c:if>
			         <c:if test="${refundOrder.orderType eq '2'}">线下</c:if>
			         </td>
			         <td class="active"><label class="pull-right">支付方式:</label></td>
			         <td >
			         <c:if test="${refundOrder.payCode eq 'wx'}">微信</c:if>
			         <c:if test="${refundOrder.payCode eq 'alipay'}">支付宝</c:if>
			         <c:if test="${refundOrder.payCode eq 'zz'}">转账</c:if>
			         </td>
				</tr>
				<tr>
			         <td class="active"><label class="pull-right">账单金额:</label></td>
			         <td >${refundOrder.arrearagePrice}</td>
			         <td class="active"><label class="pull-right">售后金额:</label></td>
			         <td >${refundOrder.aftersalesPrice}</td>
				</tr>
			    <tr>
			         <td width="100px" class="active"><label class="pull-right">实付金额:</label></td>
			         <td colspan="3">${refundOrder.amount}</td>
		        </tr>
		        <tr>
			    	<td align="center" class="active" style="height:1px;border-top:2px solid #555555;" colspan="4"><label class="pull-left">账单详情:</label></td>
				</tr>
			    <tr>
			         <td width="100px" class="active"><label class="pull-right">还款单号:</label></td>
			         <td >${refundOrder.orderId}</td>
			         <td class="active"><label class="pull-right">创建时间:</label></td>
			         <td >${refundOrder.addTime }</td>
		         </tr>
		         <tr>
			         <td class="active"><label class="pull-right">付款人:</label></td>
			         <td >${refundOrder.userName }</td>
			         <td class="active">
			         <label class="pull-right">
			         	<c:if test="${refundOrder.orderType eq '1'}">支付时间:</c:if>
			         	<c:if test="${refundOrder.orderType eq '2'}">提交时间:</c:if>
			         </label></td>
			         <td >${refundOrder.payTime }</td>
		         </tr>
		         <tr>
			         <td class="active"><label class="pull-right">手机号:</label></td>
			         <td colspan="3">${refundOrder.userMobile }</td>
				</tr>
			</tbody>
		</table>
		<c:if test="${refundOrder.orderType eq '2'}">
		<table class="table table-bordered  table-condensed dataTables-example dataTable no-footer">
			<tbody>
			    <tr>
			    	<td align="center" class="active" style="height:1px;border-top:2px solid #555555;" colspan="5"><label class="pull-left">付款账户:</label></td>
				</tr>
				<tr>
					 <th class="active" align="center">交易流水号:</td>
			         <th class="active" align="center">开户银行:</td>
			         <th class="active" align="center">银行账号:</td>
			         <th class="active" align="center">持卡人姓名:</td>
			         <th class="active" align="center">凭证:</td>
				</tr>
				<c:forEach items="${refundOrder.bankList}" var="bank">
				<tr>
			         <td align="center">${bank.serialnumber }</td>
			         <td align="center">${bank.openbank }</td>
			         <td align="center">${bank.bankaccount}</td>
			         <td align="center">${bank.openname }</td>
			         <td align="center"><span class="jq-photo" data-link='${ctx}/train/refundOrder/proof?id=${bank.id}'><a style="color: #1c84c6">查看</a></span></td>
				</tr>
				</c:forEach>
			</tbody>
			<tbody>
				<tr>
			         <td class="active"><label class="pull-right">说明:</label></td>
			         <td colspan="4">${refundOrder.explains }</td>
				</tr>
			<c:if test="${refundOrder.orderStatus eq '4'}">
				<tr>
			         <td class="active"><label class="pull-right">驳回原因:</label></td>
			         <td colspan="4">${refundOrder.remarks }</td>
				</tr>
			</c:if>
			</tbody>
		</table> 
		</c:if>
		<%-- <c:if test="${refundOrder.orderType eq '1'}">
		<table class="table table-bordered  table-condensed dataTables-example dataTable no-footer">
			<tbody>
			    <tr>
			    	<td align="center" class="active" style="height:1px;border-top:2px solid #555555;" colspan="5"><label class="pull-left">付款账户:</label></td>
				</tr>
				<tr>
			         <th class="active" align="center">支付宝账号:</td>
			         <th class="active" align="center">姓名:</td>
			         <th class="active" align="center">电话:</td>
				</tr>
				<c:forEach items="${refundOrder.bankList}" var="bank">
				<tr>
			         <td align="center">${bank.bankaccount}</td>
			         <td align="center">${bank.openbank }</td>
			         <td align="center">${bank.openname }</td>
				</tr>
				</c:forEach>
			</tbody>
			<tbody>
				<tr>
			         <td class="active"><label class="pull-right">说明:</label></td>
			         <td colspan="4">${refundOrder.explains }</td>
				</tr>
			<c:if test="${refundOrder.orderStatus eq '4'}">
				<tr>
			         <td class="active"><label class="pull-right">驳回原因:</label></td>
			         <td colspan="4">${refundOrder.remarks }</td>
				</tr>
			</c:if>
			</tbody>
		</table> 
		</c:if> --%>
		
		<c:if test="${opflag eq '1'}">
			<table id="treeTable" class="table table-bordered table-hover table-striped">
				<thead>
					<tr>
				    	<td align="center" style="height:1px;border-top:2px solid #555555;" colspan="3"><label class="pull-left">日志:</label></td>
					</tr>
					<tr>
						<th style="text-align: center;">操作时间</th>
						<th style="text-align: center;">操作人</th>
						<th style="text-align: center;">操作描述</th>
					</tr>
				</thead>
				<tbody>
					<c:forEach items="${log}" var="state">
						<tr style="text-align: center;">
							<td style="text-align: center;">${state.createTime}</td>
							<td style="text-align: center;">${state.createUsername }</td>
							<td style="text-align: center;">${state.description }</td>
						</tr>
					</c:forEach>
				</tbody>
			</table>
		</c:if>
		
		<c:if test="${opflag eq '0' and refundOrder.orderStatus eq '2'}">
			<div class="fixed-footer">
				<span class="btns yes-btn" onclick="auditPass()">确认入账</span>
				 <c:if test="${refundOrder.orderType eq '2'}">
				<span class="btns cancel-btn" onclick="auditNoPass()">审核驳回</span>
				 </c:if>
			</div> 
	   </c:if>
	</form:form> 
	<!-- 放在页面任何地方，查看凭证 -->
	<div id="jqPhoto">
		<div id="colorBtn"></div>
		<iframe src="" id="jqPhotoPage"></iframe>
	</div>
	<script type="text/javascript">
		//点击放大图片
		$(".imgZoom").imgZoom();
		$(function() {
			$('.jq-photo').click(function(){
				var url = $(this).attr('data-link')
				$('#jqPhotoPage').attr({'src':url})
				$('#jqPhoto').fadeIn()
			})
			$('#colorBtn').click(function(){
				$('#jqPhoto').fadeOut()
			})
		})
	</script>
</body>
</html>