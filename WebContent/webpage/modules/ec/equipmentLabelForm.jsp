<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/webpage/include/taglib.jsp"%>
<%@ page import="com.training.modules.sys.utils.ParametersFactory"%>
<%
	String uploadURL = ParametersFactory.getMtmyParamValues("uploader_url");
%>
<html>
<head>
<title>设备标签管理</title>
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

	$(document).ready(function(){
		select($("#TYPE").val());
		
		$("#name").focus();
		validateForm = $("#inputForm").validate({
			rules:{
				no:{remote:"${ctx}/ec/equipmentLabel/checkNO?oldNo=" + encodeURIComponent('${equipmentLabel.no}')},
				name:{remote:"${ctx}/ec/equipmentLabel/checkName?oldName=" + encodeURIComponent('${equipmentLabel.name}')}
				},
			messages:{
				no:{remote:"编号已存在"},
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
				if (element.is(":checkbox")|| element.is(":radio")|| element.parent().is(".input-append")) {
					error.appendTo(element.parent().parent());
				} else {
					error.insertAfter(element);
				}
			}
		});
	
		//在ready函数中预先调用一次远程校验函数，是一个无奈的回避案。(刘高峰）
		//否则打开修改对话框，不做任何更改直接submit,这时再触发远程校验，耗时较长，
		//submit函数在等待远程校验结果然后再提交，而layer对话框不会阻塞会直接关闭同时会销毁表单，因此submit没有提交就被销毁了导致提交表单失败。
		$("#inputForm").validate().element($("#name"));
		$("#inputForm").validate().element($("#no"));
		$("#inputForm").validate().element($("#name"));
		
	});
		
	function select(obj){
		$("#shopId").val("");	
		$("#shopName").val("");		
		if(obj == 1 || obj == 0){
			$("#shop").show();
		}else if(obj == 2){
			$("#shop").hide();
		}
	}
</script>
</head>
<body>
	<form:form id="inputForm" modelAttribute="equipmentLabel" action="${ctx}/ec/equipmentLabel/save" method="post" class="form-horizontal">
		<form:hidden path="equipmentLabelId" />
		<input id="TYPE" value="${equipmentLabel.type}" type="hidden"/>
		<sys:message content="${message}" />
		<table class="table table-bordered  table-condensed dataTables-example dataTable no-footer">
			<tbody>
				<c:if test="${equipmentLabel.flag == 1}">
				<tr>
					<td class="active" style="width:100px;">
						<label class="pull-right"><font color="red">*</font>标签类型:</label>
					</td>
					<td>
						<select class="form-control required" id="type" name="type" onchange="select(this.value)">
							<option value=1 <c:if test="${equipment.type == 1}">selected</c:if>>特殊</option>
							<option value=2 <c:if test="${equipment.type == 2}">selected</c:if>>通用</option>
						</select>
					</td>
				</tr>
				</c:if>
				<tr>
					<td class="active">
						<label class="pull-right"><font color="red">*</font>标签名称:</label>
					</td>
					<td>
						<input id="oldName" name="oldName" type="hidden" value="${equipmentLabel.name}">
						<form:input path="name" htmlEscape="false" maxlength="50" class="form-control required" />
					</td>
				</tr>
				<tr>
					<td class="active">
						<label class="pull-right"><font color="red">*</font>标签编号:</label>
					</td>
					<td>
						<input id="oldNo" name="oldNo" type="hidden" value="${equipmentLabel.no}">
						<form:input path="no" htmlEscape="false" maxlength="32" class="form-control required" />
					</td>
				</tr>
				<tr>
					<td class="active">
						<label class="pull-right">标签描述:</label>
					</td>
					<td>
						<form:textarea path="description" htmlEscape="false" rows="3" maxlength="200" class="form-control"/>
					</td>
				</tr>
			</tbody>
		</table>
	</form:form>
</body>
</html>