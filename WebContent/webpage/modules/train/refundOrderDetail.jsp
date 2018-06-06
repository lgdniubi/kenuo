<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/webpage/include/taglib.jsp"%>
<%-- <%@ include file="/webpage/include/icheck.jsp"%> --%>
<%-- <%@ page import="com.training.modules.sys.utils.ParametersFactory"%> --%>
<html>
<head>
	<title>企业审核</title>
	<meta name="decorator" content="default"/>

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
			    	<td align="center" class="active" style="height:1px;border-top:2px solid #555555;" colspan="6"><label class="pull-left">企业信息:</label></td>
				</tr>
			    <tr>
			         <td width="100px" class="active"><label class="pull-right">订单号:</label></td>
			         <td colspan="5">${refundOrder.orderId}</td>
		         </tr>
		         <tr>
			         <td class="active"><label class="pull-right">临时订单号:</label></td>
			         <td colspan="5">${refundOrder.tempOrderId }</td>
		         </tr>
		         <tr>
			         <td class="active"><label class="pull-right">订单类型:</label></td>
			         <td colspan="5">
			         <c:if test="${refundOrder.orderType eq '1'}">线上</c:if>
			         <c:if test="${refundOrder.orderType eq '2'}">线下</c:if>
			         </td>
				</tr>
			    <tr>
			         <td class="active"><label class="pull-right">欠款金额:</label></td>
			         <td colspan="5">${refundOrder.arrearagePrice}</td>
				</tr>
				<tr>
			         <td class="active"><label class="pull-right">欠款机构商家:</label></td>
			         <td colspan="5">${refundOrder.franchiseeName}</td>
				</tr>
			    <tr>
			         <td class="active"><label class="pull-right">欠款机构:</label></td>
			         <td colspan="5">${refundOrder.arrearageOfficeName}</td>
				</tr>
			    <tr>
			         <td class="active"><label class="pull-right">实付金额:</label></td>
			         <td colspan="5">${refundOrder.amount}</td>
				</tr>
				
				<tr>
			         <td class="active"><label class="pull-right">订单状态:</label></td>
			         <td colspan="5">
			         	<c:if test="${refundOrder.orderStatus eq '1'}">待支付</c:if>
			         	<c:if test="${refundOrder.orderStatus eq '2'}">待审核</c:if>
			         	<c:if test="${refundOrder.orderStatus eq '3'}">已入账</c:if>
			         </td>
				</tr>
				<tr>
			         <td class="active"><label class="pull-right">支付类型:</label></td>
			         <td colspan="5">
			         <c:if test="${refundOrder.payCode eq 'wx'}">微信</c:if>
			         <c:if test="${refundOrder.payCode eq 'alipay'}">支付宝</c:if>
			         <c:if test="${refundOrder.payCode eq 'zz'}">转账</c:if>
			         </td>
				</tr>
				<tr>
			         <td class="active"><label class="pull-right">用户名称:</label></td>
			         <td colspan="5">${refundOrder.userName}</td>
				</tr>
				<tr>
			         <td class="active"><label class="pull-right">用户手机:</label></td>
			         <td colspan="5">${refundOrder.userMobile }</td>
				</tr>
				<tr>
			         <td class="active"><label class="pull-right">渠道标识:</label></td>
			         <td colspan="5">${refundOrder.channelFlag }</td>
				</tr>
				<tr>
			         <td class="active"><label class="pull-right">账单月份:</label></td>
			         <td colspan="5">${refundOrder.billmonth }</td>
				</tr>
				<tr>
			         <td class="active"><label class="pull-right">支付时间:</label></td>
			         <td colspan="5">${refundOrder.payTime }</td>
				</tr>
				<tr>
			         <td class="active"><label class="pull-right">创建时间:</label></td>
			         <td colspan="5">${refundOrder.addTime }</td>
				</tr>
				<c:if test="${refundOrder.orderType eq '2'}">
					<tr>
				    	<td align="center" class="active" style="height:1px;border-top:2px solid #555555;" colspan="6"><label class="pull-left">线下支付信息:</label></td>
					</tr>
				    <tr>
				         <td class="active"><label class="pull-right">流水号:</label></td>
				         <td colspan="5">${refundOrder.serialnumber }</td>
			         </tr>
			         <tr>
				         <td class="active"><label class="pull-right">银行账号:</label></td>
				         <td colspan="5">${refundOrder.bankaccount}</td>
			         </tr>
			         <tr>
				         <td class="active"><label class="pull-right">开户行:</label></td>
				         <td colspan="5">${refundOrder.openbank }</td>
					</tr>
					<tr>
				         <td class="active"><label class="pull-right">开户人:</label></td>
				         <td colspan="5">${refundOrder.openname }</td>
					</tr>
				    <tr>
				         <td class="active"><label class="pull-right">凭证:</label></td>
				         <td colspan="5">
				         <c:if test="${refundOrder.proof}">
				         <img id="photosrc" src="${refundOrder.proof }" alt="images" style="width: 200px;height: 100px;"/>
				         </c:if>
				         </td>
					</tr>
				    <tr>
				         <td class="active"><label class="pull-right">说明:</label></td>
				         <td colspan="5">${refundOrder.explains }</td>
					</tr>
			    </c:if>
			</tbody>
		</table>  
	</form:form> 
</body>
</html>