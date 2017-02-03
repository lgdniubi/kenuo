<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/webpage/include/taglib.jsp"%>

<html>
<head>
<title>创建活动</title>
<meta name="decorator" content="default" />


<script type="text/javascript">
	var validateForm;
	
	function doSubmit() {//回调函数，在编辑和保存动作时，供openDialog调用提交表单。
		if (validateForm.form()) {
			
			$("#inputForm").submit();
			
			return true;
		}

		return false;
	}

	$(document).ready(function() {
		
				
				validateForm = $("#inputForm").validate({
					rules : {
						

									},
						messages : {
							

							},

							submitHandler : function(form) {
							//	loading('正在提交，请稍等...');
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
				//$("#inputForm").validate().element($("#reason"));

// 				laydate({
// 					elem : '#expirationDate', //目标元素。由于laydate.js封装了一个轻量级的选择器引擎，因此elem还允许你传入class、tag但必须按照这种方式 '#id .class'
// 					event : 'focus' //响应事件。如果没有传入event，则按照默认的click
// 				});

			});
</script>
</head>
<body>
	<div class="wrapper wrapper-content">
		<div class="ibox">
			<div class="ibox-content">
				<div class="clearfix">
					<form:form id="inputForm" modelAttribute="courierCompany" action="${ctx}/ec/courierCompany/save" method="post" class="form-horizontal">
					<form:hidden path="id"/>
						<table class="table table-bordered  table-condensed dataTables-example dataTable no-footer">
							<tr>
								<td><label class="pull-right" ><font color="red">*</font>物流公司名称：</label></td>
								<td>
									<form:input path="courierName" htmlEscape="false" maxlength="64" style="width:200px;" class="form-control required" />
								</td>
							</tr>
							<tr>
								<td><label class="pull-right" ><font color="red">*</font>物流公司编号：</label></td>
								<td>
									<form:input path="courierNo" htmlEscape="false" maxlength="64" style="width:200px;" class="form-control required" />
								</td>
							</tr>
							<tr>
								<td><label class="pull-right" ><font color="red">*</font>物流接口：</label></td>
								<td>
									<form:input path="courierPort" htmlEscape="false" maxlength="160" style="width:200px;" class="form-control required" />
								</td>
							</tr>
							
							<tr>
								<td><label class="pull-right" >备　　　　注：</label></td>
								<td>
									<form:textarea path="remark"  htmlEscape="false" rows="3" maxlength="200" class="form-control" ></form:textarea>
								</td>
							</tr>
						</table>
					
					</form:form>
				</div>
				
			</div>
		</div>
	</div>
	
</body>
</html>