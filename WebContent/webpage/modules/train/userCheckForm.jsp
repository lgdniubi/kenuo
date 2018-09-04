<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/webpage/include/taglib.jsp"%>
<%-- <%@ include file="/webpage/include/icheck.jsp"%> --%>
<%-- <%@ page import="com.training.modules.sys.utils.ParametersFactory"%> --%>
<html>
<head>
	<title>企业审核</title>
	<meta name="decorator" content="default"/>
	<!-- 放大图片js -->
	<script type="text/javascript" src="${ctxStatic}/train/imgZoom/jquery.mousewheel.js"></script>
	<script type="text/javascript" src="${ctxStatic}/train/imgZoom/jquery.imgZoom.js"></script>
	<script type="text/javascript" src="${ctxStatic}/train/imgZoom/jquery.drag.js"></script>
	
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
	<form:form id="inputForm" modelAttribute="userCheck" action="${ctx}/train/userCheck/save" method="post" class="form-horizontal">
		<sys:message content="${message}"/>
		<table class="table table-bordered  table-condensed dataTables-example dataTable no-footer">
			<tbody>
			    <tr>
			    	<td align="center" class="active" style="height:1px;border-top:2px solid #555555;" colspan="6"><label class="pull-left">申请人基本信息:</label></td>
				</tr>
			    <tr>
			         <td width="100px" class="active"><label class="pull-right">电话:</label></td>
			         <td>${userCheck.mobile}</td>
			         <td class="active"><label class="pull-right">姓名:</label></td>
			         <td>${userCheck.name}</td>
			         <td class="active"><label class="pull-right">昵称:</label></td>
			         <td>${userCheck.nickname}</td>
				</tr>
			    <tr>
			         <td class="active"><label class="pull-right">申请时间:</label></td>
			         <td colspan="5"><fmt:formatDate value="${userCheck.createDate}" pattern="yyyy-MM-dd HH:mm:ss"/></td>
				</tr>
			    <tr>
			         <td class="active"><label class="pull-right">当前会员类型:</label></td>
			         <td colspan="5">
			         	<c:if test="${userCheck.applyType eq 'pt'}">普通会员</c:if>
						<c:if test="${userCheck.applyType eq 'syr'}">手艺人</c:if>
						<c:if test="${userCheck.applyType eq 'qy'}">企业</c:if>
					 </td>
				</tr>
			    <tr>
			         <td class="active"><label class="pull-right">申请会员类型:</label></td>
			         <td colspan="5">
					 	<c:if test="${userCheck.auditType eq 'syr'}">手艺人</c:if>
						<c:if test="${userCheck.auditType eq 'qy'}">企业</c:if>
					 </td>
				</tr>
				<tr>
			    	<td align="center" class="active" style="height:1px;border-top:2px solid #555555;" colspan="6"><label class="pull-left">申请资料:</label></td>
				</tr>
			    <tr>
			         <td class="active"><label class="pull-right">企业名称:</label></td>
			         <td>${userCheck.companyName}</td>
			         <td class="active"><label class="pull-right">企业简称:</label></td>
			         <td>${userCheck.shortName}</td>
			         <td class="active"><label class="pull-right">执照编号:</label></td>
			         <td>${userCheck.charterCard}</td>
				</tr>
			    <tr>
			         <td class="active"><label class="pull-right">企业类型:</label></td>
			         <td>
			         	<c:if test="${userCheck.addr.type == 1}">个体户</c:if>
			         	<c:if test="${userCheck.addr.type == 2}">合伙企业</c:if>
			         	<c:if test="${userCheck.addr.type == 3}">个人独资企业</c:if>
			         	<c:if test="${userCheck.addr.type == 4}">公司</c:if>
			         </td>
			         <td class="active"><label class="pull-right">成立日期:</label></td>
			         <td><fmt:formatDate value="${userCheck.addr.setDate}" pattern="yyyy/MM/dd  HH:mm:ss" /></td>
			         <td class="active"><label class="pull-right">所在地区:</label></td>
			         <td>${userCheck.addr.provinceName}${userCheck.addr.cityName}${userCheck.addr.districtName}</td>
				</tr>
			    <tr>
			         <td class="active"><label class="pull-right">详细地址:</label></td>
			         <td>${userCheck.address}</td>
			         <td class="active"><label class="pull-right">企业法人:</label></td>
			         <td>${userCheck.legalPerson}</td>
			         <td class="active"><label class="pull-right">法人手机:</label></td>
			         <td>${userCheck.legalMobile}</td>
				</tr>
			    <tr>
			         <td class="active"><label class="pull-left">身份证号:</label></td>
			         <td colspan="5">${userCheck.legalCard}</td>
			    </tr>
			    <tr>
			         <td class="active" colspan="6"><label class="pull-left">法人身份证:</label></td>
			    </tr>
			    <tr>
			         <td height="100px" colspan="3"><img id="photosrc" src="${userCheck.icardone}" class='imgZoom' alt="images" style="width: 200px;height: 100px;"/></td>
			         <td  colspan="3"><img id="photosrc" src="${userCheck.icardtwo}" alt="images" class='imgZoom' style="width: 200px;height: 100px;"/></td>
				</tr>
			    <tr>
			    	  <td class="active" colspan="6"><label class="pull-left">账户信息</label></td>
				</tr>
				<c:forEach var="bank" items="${userCheck.bankAccount}">
					<tr>
				         <td class="active"><label class="pull-left">账户名称:</label></td>
				         <td >${bank.accountname}</td>
				         <td class="active"><label class="pull-left">开户银行:</label></td>
				         <td >${bank.openbank}</td>
				         <td class="active"><label class="pull-left">银行账户:</label></td>
				         <td >${bank.bankaccount}</td>
				    </tr>
				    <tr>
				         <td class="active"><label class="pull-left">开户地址:</label></td>
				         <td >${bank.openaddress}</td>
				         <td class="active"><label class="pull-left">详细地址:</label></td>
				         <td >${bank.detailedaddress}</td>
				    </tr>
				    <tr>
					         <td class=""><label class="pull-right">正面:</label></td>
					         <td ><img id="photosrc" src="${bank.cardup}" alt="images" class='imgZoom' style="width: 200px;height: 100px;"/></td>
					         <td class=""><label class="pull-right">反面:</label></td>
					         <td><img id="photosrc" src="${bank.carddown}" alt="images" class='imgZoom' style="width: 200px;height: 100px;"/></td>
						</tr>
				    <tr>
			    </c:forEach>
			         <td height="100px" class="active"><label class="pull-left">营业执照:</label></td>
			    	 <td colspan="5"><img id="photosrc" src="${userCheck.charterUrl}" class='imgZoom' alt="images" style="width: 200px;height: 100px;"/></td>
			    </tr>
			    <tr>
			         <td class="active" colspan="6"><label class="pull-left">企业介绍:</label></td>
			    </tr>
			    <tr>  
			         <td height="100px"></td>
			         <td colspan="5">${userCheck.intro}</td>
				</tr>
				<c:if test="${userCheck.status == 1}">
				    <tr>
				         <td class="active" colspan="6"><label class="pull-left">不通过原因:</label></td>
				    </tr>
				    <tr>  
				         <td height="100px" colspan="6">${userCheck.remarks}</td>
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