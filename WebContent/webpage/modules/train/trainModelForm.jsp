<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/webpage/include/taglib.jsp"%>
<html>
	<head>
		<title>添加版本管理</title>
		<meta name="decorator" content="default" />
		<script type="text/javascript">
		var validateForm;
		function doSubmit(){//回调函数，在编辑和保存动作时，供openDialog调用提交表单。
		  if(validateForm.form()){
			  $("#inputForm").submit();
			  return true;
		  }
	
		  return false;
		}
		
		$(document).ready(function(){
			$("#name").focus();
			
			validateForm= $("#inputForm").validate({
				rules: {
					modEname: {
						remote:{
							type: "post",
							async: false,
							url:"${ctx}/train/model/checkModEname?oldName=" + encodeURIComponent("${trainModel.modEname}")
/* 							url:"${ctx}/train/model/checkModEname?oldName=" + encodeURIComponent("${trainModel.modEname}") */
						}
					}
				},
				messages: {
					modEname: {remote: "此版本英文名称已存在"}
				},
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
		})
		</script>
		
	</head>
	<body>
		<form:form id="inputForm" modelAttribute="trainModel" action="${ctx}/train/model/savemodel" method="post" class="form-horizontal" >
			<form:hidden path="id" />
			<sys:message content="${message}"/>
			<table class="table table-bordered  table-condensed dataTables-example dataTable no-footer">
			   <tbody>
			      <tr>
					<td class="width-15 active">
						<label class="pull-right"><font color="red">*</font>用户类型：</label>
					</td>
					<td class="width-35">
						<form:select path="modType" id="modType" class="form-control required">
							<form:options items="${fns:getDictList('model_type')}" itemLabel="label" itemValue="value" htmlEscape="false"/>
						</form:select>
					</td>
				  </tr>
			      <tr>
					<td class="width-15 active">
						<label class="pull-right"><font color="red">*</font>版本名称：</label>
					</td>
					<td class="width-35">
					<form:input path="modName" htmlEscape="false" class="form-control required"/></td>
				  </tr>
			      <tr>
					<td class="width-15 active">
						<label class="pull-right"><font color="red">*</font>英文名称：</label>
					</td>
					<td class="width-35"><input id="oldName" name="oldName" type="hidden" value="${trainModel.modEname}">
					<form:input path="modEname" htmlEscape="false" maxlength="50" class="form-control required"/></td>
				  </tr>
			      <tr>
					 <td class="width-15 active"><label class="pull-right">备注:</label></td>
			         <td class="width-35"><form:textarea path="remark" htmlEscape="false" rows="3" maxlength="50" class="form-control" placeholder="请输入备注（最多50个字）"/></td>
			      </tr>
				</tbody>
				</table>
		</form:form>
	</body>
</html>