<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/webpage/include/taglib.jsp"%>
<html>
<head>
	<title>分销参数详情</title>
	<meta name="decorator" content="default"/>
	<script type="text/javascript">
		var validateForm;
		function doSubmit(){//回调函数，在编辑和保存动作时，供openDialog调用提交表单。
		  if(validateForm.form()){
			  $("#inputForm").submit();
			  return true;
		  }
		  return false;
		}
		//页面加载事件
		$(document).ready(function() {
			
			//表单验证
			validateForm = $("#inputForm").validate({
				submitHandler: function(form){
					loading('正在提交，请稍等...');
					form.submit();
				},
				errorContainer: "#messageBox",
				errorPlacement: function(error, element) {
					$("#messageBox").text("输入有误，请先更正。");
					if (element.is(":checkbox")||element.is(":radio")||element.parent().is(".input-append")){
						error.appendTo(element.parent().parent());
					} else {
						error.insertAfter(element);
					}
				}
			});
		});
		
	</script>
</head>
<body>
	<form:form id="inputForm" modelAttribute="mtmyRuleParam" action="${ctx}/ec/saleParam/save" method="post" class="form-horizontal">
		<form:hidden path="id"/>
		<sys:message content="${message}" />
		<table class="table table-bordered  table-condensed dataTables-example dataTable no-footer">
			<tr>
				<td class="active">
					<label class="pull-right"><font color="red">*</font>参数Key:</label>
				</td>
				<td>
					<input type="text" id="paramKey" name="paramKey" value="${mtmyRuleParam.paramKey }" class="form-control required" style="width: 400px;">
				</td>
			</tr>
			<tr>
				<td class="active">
					<label class="pull-right"><font color="red">*</font>参数值:</label>
				</td>
				<td>
					<form:input path="paramValue" htmlEscape="false" maxlength="50" class="form-control required" />
				</td>
			</tr>
			<tr>
				<td class="active">
					<label class="pull-right"><font color="red">*</font>参数说明:</label>
				</td>
				<td>
					<form:textarea path="paramExplain" htmlEscape="false" rows="3" maxlength="200" class="form-control required"/>
				</td>
			</tr>
			<tr>
				<td class="active">
					<label class="pull-right">排序:</label>
				</td>
				<td>
					<form:input path="sort" htmlEscape="false" maxlength="11" class="form-control" />
				</td>
			</tr>
			<tr>
				<td class="active">
					<label class="pull-right">备注:</label>
				</td>
				<td>
					<form:textarea path="remarks" htmlEscape="false" rows="3" maxlength="200" class="form-control"/>
				</td>
			</tr>
		</table>
	</form:form>
	<div class="loading"></div>
</body>
</html>