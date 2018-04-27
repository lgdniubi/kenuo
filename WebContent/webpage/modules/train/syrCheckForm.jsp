<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/webpage/include/taglib.jsp"%>
<%-- <%@ include file="/webpage/include/icheck.jsp"%> --%>
<%-- <%@ page import="com.training.modules.sys.utils.ParametersFactory"%> --%>
<html>
<head>
	<title>手艺人审核</title>
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
	<form:form id="inputForm" modelAttribute="userCheck" action="" method="post" class="form-horizontal">
		<sys:message content="${message}"/>
		<table class="table table-bordered  table-condensed dataTables-example dataTable no-footer">
			<tbody>
			    <tr>
			    	<td align="center" class="active" style="height:1px;border-top:2px solid #555555;" colspan="6"><label class="pull-left">申请人基本信息:</label></td>
				</tr>
			    <tr>
			         <td width="100px" class="active"><label class="pull-left">电话:</label></td>
			         <td colspan="5">${userCheck.mobile}</td>
				</tr>
			    <tr>
			         <td class="active"><label class="pull-left">姓名:</label></td>
			         <td colspan="5">${userCheck.name}</td>
				</tr>
			    <tr>
			         <td class="active"><label class="pull-left">昵称:</label></td>
			         <td colspan="5">${userCheck.nickname}</td>
				</tr>
			    <tr>
			         <td class="active"><label class="pull-left">申请时间:</label></td>
			         <td colspan="5"><fmt:formatDate value="${userCheck.createDate}" pattern="yyyy-MM-dd HH:mm:ss"/></td>
				</tr>
			    <tr>
			         <td class="active"><label class="pull-left">当前会员类型:</label></td>
			         <td colspan="5">
			         	<c:if test="${userCheck.applyType eq 'pt'}">普通员工</c:if>
						<c:if test="${userCheck.applyType eq 'syr'}">手艺人</c:if>
						<c:if test="${userCheck.applyType eq 'qy'}">企业</c:if>
			         </td>
				</tr>
			    <tr>
			         <td class="active"><label class="pull-left">申请会员类型:</label></td>
			         <td  >
			         	<c:if test="${userCheck.auditType eq 'syr'}">手艺人</c:if>
						<c:if test="${userCheck.auditType eq 'qy'}">企业</c:if>
					</td>
				</tr>
				<tr>
			    	<td align="center" class="active" style="height:1px;border-top:2px solid #555555;" colspan="6"><label class="pull-left">申请资料:</label></td>
				</tr>
			    <tr>
			         <td class="active"><label class="pull-right">专长:</label></td>
			         <td>${userCheck.speciality}</td>
				</tr>
			    <tr>
			         <td class="active"><label class="pull-right">工作经验:</label></td>
			         <td>${userCheck.startDate}</td>
				</tr>
			    <tr>
			         <td class="active"><label class="pull-right">目前收入:</label></td>
			         <td>${userCheck.income}</td>
				</tr>
			    <tr>
			         <td class="active"><label class="pull-right">期望工作城市:</label></td>
			         <td>${userCheck.city}</td>
				</tr>
			    <tr>
			         <td class="active"><label class="pull-right">手机:</label></td>
			         <td>${userCheck.mobile}</td>
				</tr>
				<c:if test="${userCheck.status == 1}">
				    <tr>
				         <td class="active"><label class="pull-right">不通过原因:</label></td>
				         <td>${userCheck.remarks}</td>
					</tr>
			    </c:if>
			</tbody>
		</table>  
	</form:form> 
</body>
</html>