<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/webpage/include/taglib.jsp"%>
<html>
<head>
<title>称谓标签</title>
<meta name="decorator" content="default" />
<script type="text/javascript">
	var validateForm;
	function doSubmit(){//回调函数，在编辑和保存动作时，供openDialog调用提交表单。
	    if(validateForm.form()){
    		loading("正在提交，请稍候...");
			$("#inputForm").submit();
	    	return true;
    	}
    	return false;
    }
	$(document).ready(function(){
		validateForm = $("#inputForm").validate();
	});
</script>
<!-- 内容上传 引用-->
</head>
<body>
	<form:form id="inputForm" modelAttribute="appellation" action="${ctx}/crm/appellation/save" method="post" class="form-horizontal">
		<form:hidden path="appellationId" />
		<sys:message content="${message}" />
		<table
			class="table table-bordered  table-condensed dataTables-example dataTable no-footer">
			<tbody>
				<tr>
					<td class="active"><label class="pull-right"><font color="red">*</font>称谓名称:</label></td>
					<td><form:input path="name" htmlEscape="false" maxlength="50" class="form-control required" /></td>
				</tr>
			</tbody>
		</table>
	</form:form>
</body>
</html>