<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/webpage/include/taglib.jsp"%>
<%@ page import="com.training.modules.sys.utils.ParametersFactory"%>
<%
	String uploadURL = ParametersFactory.getMtmyParamValues("uploader_url");
%>
<html>
<head>
	<title>支付信息</title>
	<meta name="decorator" content="default"/>
	<!-- 内容上传 引用-->
</head>
<body>
	<%-- <form:form id="inputForm" modelAttribute="fzxMenu" action="" method="post" class="form-horizontal"> --%>
		<table class="table table-bordered  table-condensed dataTables-example dataTable no-footer">
		   <tbody>
		   		
		   	  <tr>
		         <td  class="width-15 active"><label class="pull-right">流水号：</label></td>
		         <td  class="width-35" >${transferpay.serialnumber}
		         <%-- <form:input path="name" htmlEscape="false" maxlength="20" class="required form-control "/> --%>
		         </td>
		      </tr>
		      
		      <tr>
		         <td  class="width-15 active"><label class="pull-right">开户银行:</label></td>
		         <td  class="width-35" >
		         	${ transferpay.openbank}
		        <%--  <form:input path="name" htmlEscape="false" maxlength="20" class="required form-control "/> --%>
		         </td>
		      </tr>
		      <tr>
		         <td  class="width-15 active"><label class="pull-right">银行账号:</label></td>
		         <td  class="width-35" >
		         	<%-- <form:input path="enname" htmlEscape="false" maxlength="20" class="required form-control "/> --%>
		         	${transferpay.bankaccount }
		         </td>
		      </tr>
		      <tr>
		         <td  class="width-15 active"><label class="pull-right">开户名称:</label></td>
		         <td class="width-35" >
		         	<%-- <form:input path="href" htmlEscape="false" maxlength="2000" class="form-control "/> --%>
		         	${transferpay.openname}
	         	</td>
		      </tr>
		      <tr>
		         <td  class="width-15 active"><label class="pull-right">还款凭证:</label></td>
		         <td class="width-35" >
		         	<%-- <form:input path="href" htmlEscape="false" maxlength="2000" class="form-control "/> --%>
		         	<img src="${transferpay.proof}">
	         	</td>
		      </tr>
		      <tr>
		         <td  class="width-15 active"><label class="pull-right">还款人:</label></td>
		         <td class="width-35" >
		         	<%-- <form:input path="href" htmlEscape="false" maxlength="2000" class="form-control "/> --%>
		         	<img src="${transferpay.user_name}">
	         	</td>
		      </tr>
		    </tbody>
		  </table>
	<%-- </form:form> --%>
</body>
</html>