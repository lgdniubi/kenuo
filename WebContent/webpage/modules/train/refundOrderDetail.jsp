<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/webpage/include/taglib.jsp"%>
<%-- <%@ include file="/webpage/include/icheck.jsp"%> --%>
<%-- <%@ page import="com.training.modules.sys.utils.ParametersFactory"%> --%>
<html>
<head>
	<title>对账单详情</title>
	<meta name="decorator" content="default"/>

	<!-- 放大图片js -->
	<script type="text/javascript" src="${ctxStatic}/train/imgZoom/jquery.imgZoom.js"></script>
	
	<script type="text/javascript">
		/* var validateForm;
		function doSubmit(){//回调函数，在编辑和保存动作时，供openDialog调用提交表单。
			if(validateForm.form()){
        	  loading('正在提交，请稍等...');
		      $("#inputForm").submit();
	     	  return true;
		  	}
		  return false;
		} */
	</script>
</head>
<body>
	<form:form id="inputForm" modelAttribute="userCheck" action="${ctx}/train/userCheck/save" method="post" class="form-horizontal">
		<sys:message content="${message}"/>
		<table class="table table-bordered  table-condensed dataTables-example dataTable no-footer">
			<tbody>
			    <tr>
			    	<td align="center" class="active" style="height:1px;border-top:2px solid #555555;" colspan="3"><label class="pull-left">还款信息:</label></td>
			         <td >
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
			         <td align="center">${bank.bankaccount}</td>
			         <td align="center">${bank.openbank }</td>
			         <td align="center">${bank.openname }</td>
			         <td align="center">查看</td>
				</tr>
				</c:forEach>
			</tbody>
			<tbody>
				<tr>
			         <td class="active"><label class="pull-right">说明:</label></td>
			         <td colspan="4">${refundOrder.remarks }</td>
				</tr>
				<tr>
			         <td class="active"><label class="pull-right">驳回原因:</label></td>
			         <td colspan="4">${refundOrder.remarks }</td>
				</tr>
			</tbody>
		</table>  
	</form:form> 
	<script type="text/javascript">
		//点击放大图片
		$(".imgZoom").imgZoom();
	</script>
</body>
</html>