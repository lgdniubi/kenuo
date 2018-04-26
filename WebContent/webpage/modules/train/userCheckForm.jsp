<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/webpage/include/taglib.jsp"%>
<%-- <%@ include file="/webpage/include/icheck.jsp"%> --%>
<%-- <%@ page import="com.training.modules.sys.utils.ParametersFactory"%> --%>
<html>
<head>
	<title>企业审核</title>
	<meta name="decorator" content="default"/>

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
			         	<c:if test="${userCheck.applyType eq 'pt'}">普通员工</c:if>
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
			         <td height="100px" colspan="3"><img id="photosrc" src="${userCheck.icardone}" alt="images" style="width: 200px;height: 100px;"/></td>
			         <td  colspan="3"><img id="photosrc" src="${userCheck.icardtwo}" alt="images" style="width: 200px;height: 100px;"/></td>
				</tr>
			    <tr>
			         <td height="100px" class="active"><label class="pull-left">营业执照:</label></td>
			    	 <td colspan="5"><img id="photosrc" src="${userCheck.charterUrl}" alt="images" style="width: 200px;height: 100px;"/></td>
			    </tr>
			    <tr>
			         <td class="active" colspan="6"><label class="pull-left">企业介绍:</label></td>
			    </tr>
			    <tr>  
			         <td height="100px"></td>
			         <td colspan="5">${userCheck.intro}</td>
				</tr>
				<c:if test="${userCheck.status == 1 || userCheck.status == 2}">
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
</body>
</html>