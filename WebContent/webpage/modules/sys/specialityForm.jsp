<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/webpage/include/taglib.jsp"%>
<%@ page import="com.training.modules.sys.utils.ParametersFactory"%>
<%
	String uploadURL = ParametersFactory.getMtmyParamValues("uploader_url");
%>
<html>
<head>
<title>用户管理</title>
<meta name="decorator" content="default" />
<!-- 内容上传 引用-->


<script type="text/javascript">
	var validateForm;
	function doSubmit() {//回调函数，在编辑和保存动作时，供openDialog调用提交表单。
		if (validateForm.form()) {
			$("#inputForm").submit();
			return true;
		}

		return false;
	}

	$(document).ready(
			function() {
				$("#name").focus();
				validateForm = $("#inputForm").validate({
					rules:{
						name:{remote:"${ctx}/sys/speciality/checkName?oldName=" + encodeURIComponent('${speciality.name}')}
						},
					messages:{
						name:{remote:"名称已存在"}
					},
					submitHandler : function(form) {
						//alert("提交")
						loading('正在提交，请稍等...');
						form.submit();
					},
					errorContainer : "#messageBox",
					errorPlacement : function(error, element) {
						$("#messageBox").text("输入有误，请先更正。");
						if (element.is(":checkbox")
								|| element.is(":radio")
								|| element.parent().is(
										".input-append")) {
							error.appendTo(element.parent()
									.parent());
						} else {
							error.insertAfter(element);
						}
					}
				});

				//在ready函数中预先调用一次远程校验函数，是一个无奈的回避案。(刘高峰）
				//否则打开修改对话框，不做任何更改直接submit,这时再触发远程校验，耗时较长，
				//submit函数在等待远程校验结果然后再提交，而layer对话框不会阻塞会直接关闭同时会销毁表单，因此submit没有提交就被销毁了导致提交表单失败。
				$("#inputForm").validate().element($("#name"));

			});
</script>
</head>
<body>
	<form:form id="inputForm" modelAttribute="speciality"
		action="${ctx}/sys/speciality/save" method="post"
		class="form-horizontal">
		<form:hidden path="id" />
		<sys:message content="${message}" />
		<table
			class="table table-bordered  table-condensed dataTables-example dataTable no-footer">
			<tbody>
				<tr>
					<td class="active"><label class="pull-right"><font
							color="red">*</font>特长名称:</label></td>
					<td><form:input path="name" htmlEscape="false" maxlength="50"
							class="form-control required" /></td>
				</tr>

			</tbody>
		</table>
	</form:form>
</body>
</html>