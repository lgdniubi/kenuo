<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/webpage/include/taglib.jsp"%>
<html>
<head>
	<title>编辑信用额度</title>
	<meta name="decorator" content="default"/>
	
	
	<%-- <link rel="stylesheet" href="${ctxStatic}/train/css/exam.css"> --%>
	<script type="text/javascript">
		function doSubmit(){//回调函数，在编辑和保存动作时，供openDialog调用提交表单。
			var v = $("#creditLimit").val();
			if(!v){
				alert("请输入数字");
				return;
			}
			if(isNaN(v)){
				alert("请输入数字");
				return;
			}
			$("#inputForm").submit();
			return true;
		}
		
	</script>
</head>
<body>
	<div class="ibox-content">
		<form:form id="inputForm" modelAttribute="office" action="${ctx}/sys/office/updateOfficeCreditLimit" method="post" class="form-horizontal">
			<input type="hidden" value="${officeAcount.officeId }" name="officeId">
			<sys:message content="${message}"/>
			<table class="table table-bordered  table-condensed dataTables-example dataTable no-footer">
		   <tbody>
		      <tr>
		         <td  class="width-15" class="active"><label class="pull-right"><font color="red">*</font>信用额度</label></td>
		         <td class="width-35"><input id="creditLimit" name="creditLimit" type="text" value="${officeAcount.creditLimit}" class="form-control required">
		      </tr>
			</tbody>
			</table>
		</form:form>
	</div>
	
	
</body>
</html>