<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/webpage/include/taglib.jsp"%>
<%@ include file="/webpage/include/icheck.jsp"%>
<html>
	<head>
		<title>部门管理</title>
		<meta name="decorator" content="default" />
		<%-- <link rel="stylesheet" type="text/css" href="${ctxStatic}/common/training.css"> --%>
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
					name: {
						remote:{
							type: "post",
							async: false,
							url:"${ctx}/train/department/checkName?oldName=" + encodeURIComponent("${department.name}")+"&companyId=${department.office.id}"
						}
					}
				},
				messages: {
					name: {remote: "部门已存在"}
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
			$("#inputForm").validate().element($("#name"));
		})
		</script>
		
	</head>
	<body>
		<form:form id="inputForm" modelAttribute="department" action="${ctx}/train/department/save" method="post" class="form-horizontal" >
			<form:hidden path="dId"/>
			<sys:message content="${message}"/>
			<table class="table table-bordered  table-condensed dataTables-example dataTable no-footer">
			   <tbody>
			      <tr>
					<td class="width-15 active">
						<label class="pull-right"><font color="red">*</font>部门名称:</label>
					</td>
					<td class="width-35"><input id="oldName" name="oldName" type="hidden" value="${department.name}">
					<form:input path="name" htmlEscape="false" maxlength="50" class="form-control required"/></td>
				  </tr>
			      <tr>
			          <td  class="width-15 active"><label class="pull-right"><font color="red">*</font>排序:</label></td>
		         	  <td  class="width-35" ><form:input path="sort" htmlEscape="false" maxlength="50" class="required digits form-control "/>
					  <span class="help-inline">排列顺序，升序。</span></td>
				  </tr>
			      <tr>
					 <td class="width-15 active"><label class="pull-right">备注:</label></td>
			         <td class="width-35"><form:textarea path="remarks" htmlEscape="false" rows="3" maxlength="200" class="form-control "/></td>
			      </tr>
				</tbody>
				</table>
			<%-- <form:hidden path=""/> --%>
		</form:form>
	</body>
</html>