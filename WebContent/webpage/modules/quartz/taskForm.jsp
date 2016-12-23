<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/webpage/include/taglib.jsp"%>

<html>
<head>
	<title>定时任务管理</title>
	<meta name="decorator" content="default"/>
	<link rel="stylesheet" href="${ctxStatic}/ec/css/loading.css">
	<script type="text/javascript">
		var validateForm;
		function doSubmit(){//回调函数，在编辑和保存动作时，供openDialog调用提交表单。
		  if(validateForm.form()){
			  $("#inputForm").submit();
			  return true;
		  }
		  return false;
		}
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
		
		//立即执行定时任务
		function checkcron(cron){
			$(".loading").show();//打开展示层
			$.ajax({
				type : "POST",   
				url : "${ctx}/quartz/task/checkcron?cron="+cron,
				dataType: 'json',
				success: function(data) {
					$(".loading").hide(); //关闭加载层
					if("OK" == data.STATUS){
						alert(data.MESSAGE);
					}else if("ERROR" == data.STATUS){
						alert(data.MESSAGE);
						$("#cronExpression").focus();
					}
				}
			});   
		}
		
	</script>
</head>
<body>
	<form:form id="inputForm" modelAttribute="task" action="${ctx}/quartz/task/save" method="post" class="form-horizontal">
		<form:hidden path="jobId"/>
		<sys:message content="${message}" />
		<table class="table table-bordered  table-condensed dataTables-example dataTable no-footer">
			<tbody>
				<tr>
					<td class="width-15 active">
						<label class="pull-right"><font color="red">*</font>任务名称:</label>
					</td>
					<td class="width-35">
						<form:input path="jobName" htmlEscape="false" maxlength="50" class="form-control required"/>
					</td>
					<td class="width-15 active">
						<label class="pull-right"><font color="red">*</font>任务分组:</label>
					</td>
					<td class="width-35">
						<form:input path="jobGroup" htmlEscape="false" maxlength="50" class="form-control required"/>
					</td>
				</tr>
				<tr>
					<td class="width-15 active">
						<label class="pull-right"><font color="red">*</font>spring bean:</label>
					</td>
					<td class="width-35">
						<form:input path="springId" htmlEscape="false" maxlength="50" class="form-control required"/>
					</td>
					<td class="width-15 active">
						<label class="pull-right"><font color="red">*</font>任务方法:</label>
					</td>
					<td class="width-35">
						<form:input path="methodName" htmlEscape="false" maxlength="50" class="form-control required"/>
					</td>
				</tr>
				<tr>
					<td class="width-15 active">
						<label class="pull-right"><font color="red">*</font>时间表达式:</label>
					</td>
					<td class="width-35">
						<form:input path="cronExpression" htmlEscape="false" maxlength="50" onblur="checkcron(this.value)" class="form-control required"/>
					</td>
					<td class="width-15 active">
						<label class="pull-right">包名+类名:</label>
					</td>
					<td class="width-35">
						<form:input path="beanClass" htmlEscape="false" maxlength="150" class="form-control"/>
					</td>
				</tr>
				<%-- <tr>
					<td class="width-15 active">
						<label class="pull-right"><font color="red">*</font>是否并发:</label>
					</td>
					<td class="width-35">
						<form:select path="isConcurrent" class="form-control required">
							<form:option value="0">开启</form:option>
							<form:option value="1">关闭</form:option>
						</form:select>
					</td>
					<td class="width-15 active">
						<label class="pull-right"><font color="red">*</font>任务状态:</label>
					</td>
					<td class="width-35">
						<form:select path="jobStatus" class="form-control required">
							<form:option value="0">开启</form:option>
							<form:option value="1">关闭</form:option>
						</form:select>
					</td>
					<td class="width-15 active">
						<label class="pull-right"><font color="red">*</font>spring bean:</label>
					</td>
					<td class="width-35">
						<form:input path="springId" htmlEscape="false" maxlength="50" class="form-control required"/>
					</td>
				</tr> --%>
				<tr>
					<td class="width-15 active">
						<label class="pull-right"><font color="red">*</font>描述:</label>
					</td>
					<td class="width-35" colspan="3">
						<form:textarea path="description" htmlEscape="false" maxlength="50" class="form-control" />
					</td>
				</tr>
			</tbody>
		</table>
	</form:form>
	<div class="loading"></div>
</body>
</html>