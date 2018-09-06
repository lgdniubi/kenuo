<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/webpage/include/taglib.jsp"%>
<%-- <%@ include file="/webpage/include/icheck.jsp"%> --%>
<%-- <%@ page import="com.training.modules.sys.utils.ParametersFactory"%> --%>
<html>
<head>
	<title>手艺人审核</title>
	<meta name="decorator" content="default"/>
	<!-- 放大图片js -->
	<script type="text/javascript" src="${ctxStatic}/train/imgZoom/jquery.imgZoom.js"></script>
	
	<script type="text/javascript">
		var validateForm;
		function doSubmit(){//回调函数，在编辑和保存动作时，供openDialog调用提交表单。
			if(validateForm.form()){
        	  loading('正在提交，请稍等...');
		      $("#inputForm").submit();
	     	  return true;
		  	}
		  return false;
		}
	</script>
</head>
<body>
	<form:form id="inputForm" modelAttribute="userCheck" action="" method="post" class="form-horizontal">
		<sys:message content="${message}"/>
		<table class="table table-bordered  table-condensed dataTables-example dataTable no-footer">
			<tbody>
			    <tr>
			    	<td align="center" class="active" style="height:1px;border-top:2px solid #555555;" colspan="6"><label class="pull-left">基本资料:</label></td>
				</tr>
			    <tr>
			         <td ><label class="pull-right">姓名:</label></td>
			         <td colspan="1">${userCheck.name}</td>
			         <td width="100px" ><label class="pull-right">电话:</label></td>
			         <td colspan="1">${userCheck.mobile}</td>
				</tr>
			    <tr>
			         <td ><label class="pull-right">昵称:</label></td>
			         <td colspan="1">${userCheck.nickname}</td>
			         <td class=""><label class="pull-right">身份证:</label></td>
			         <td colspan="1">${userCheck.addr.idcard}</td>
				</tr>
			    <tr>
			    	 <td class=""><label class="pull-right">E-mail:</label></td>
			         <td  colspan="1"> ${userCheck.addr.email}</td>
			         <td ><label class="pull-right">目前收入:</label></td>
			         <td colspan="1">${userCheck.income}</td>
				</tr>
			    <tr>
			         <td ><label class="pull-right">工作经验:</label></td>
			         <td colspan="1">${userCheck.startDate}</td>
			         <td class=""><label class="pull-right">所在地区:</label></td>
			         <td colspan="1">${userCheck.addr.provinceName}${userCheck.addr.cityName}${userCheck.addr.districtName}</td>
				</tr>
			    <tr>
			         <td ><label class="pull-right">专长:</label></td>
			         <td colspan="5">${userCheck.speciality}</td>
				</tr>
			   
			    <tr>
			         <td class=""><label class="pull-right">详细地址:</label></td>
			         <td colspan="5">${userCheck.addr.syrAddress}</td>
				</tr>
				</tbody>
				<tbody>
				<c:forEach var="bank" items="${userCheck.bankAccount}">
				    <tr>
				         <td class="active" colspan="4"><label class="pull-left">银行卡:</label></td>
					</tr>
				    <tr>
				         <td class=""><label class="pull-right">账户名称:</label></td>
				         <td colspan="3">${bank.accountname}</td>
					</tr>
				    <tr>
				         <td class=""><label class="pull-right">开户银行:</label></td>
				         <td>${bank.openbank}</td>
				         <td class=""><label class="pull-right">银行账户:</label></td>
				         <td>${bank.bankaccount}</td>
					</tr>
				    <tr>
				         <td class=""><label class="pull-right">开户地址:</label></td>
				         <td>${bank.openaddress}</td>
				         <td class=""><label class="pull-right">详细地址:</label></td>
				         <td>${bank.detailedaddress}</td>
					</tr>
				    <tr>
				         <td class=""><label class="pull-right">银行卡正面:</label></td>
				         <td ><img id="photosrc" src="${bank.cardup}"  class='imgZoom' alt="images" style="width: 200px;height: 100px;"/></td>
				         <td class=""><label class="pull-right">银行卡反面:</label></td>
				         <td><img id="photosrc" src="${bank.carddown}" class='imgZoom' alt="images" style="width: 200px;height: 100px;"/></td>
					</tr>
				</c:forEach>
				<c:forEach var="pay" items="${userCheck.payAccount}">
				<c:if test="${pay.payType ==1 }">
				    <tr>
				         <td class="active" colspan="4"><label class="pull-left">支付宝:</label></td>
					</tr>
				    <tr>
				         <td class=""><label class="pull-right">账号:</label></td>
				         <td>${pay.no}</td>
				         <td class=""><label class="pull-right">姓名:${pay.name}</label></td>
				         <td class=""><label class="pull-right">手机号:${pay.mobile}</label></td>
					</tr>
				</c:if>
				<c:if test="${pay.payType ==2}">
				    <tr>
				         <td class="active" colspan="4"><label class="pull-left">微信:</label></td>
					</tr>
				    <tr>
				         <td class=""><label class="pull-right">账号:</label></td>
				         <td>${pay.no}</td>
				         <td class=""><label class="pull-right">姓名:${pay.name}</label></td>
				         <td class=""><label class="pull-right">手机号:${pay.mobile}</label></td>
					</tr>
				</c:if>
				</c:forEach>
			   
				<c:if test="${userCheck.status == 1}">
				    <tr>
				         <td class="active"><label class="pull-right">不通过原因:</label></td>
				         <td >${userCheck.remarks}</td>
					</tr>
			    </c:if>
			</tbody>
		</table>  
	</form:form> 
	<script type="text/javascript">
		//点击放大图片
		$(".imgZoom").imgZoom();
	</script>
</body>
</html>