<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/webpage/include/taglib.jsp"%>

<html>
<head>
<title>创建活动</title>
<meta name="decorator" content="default" />
<script type="text/javascript" src="${ctxStatic}/My97DatePicker/WdatePicker.js"></script>

<style type="text/css">
#one {
	width: 200px;
	height: 180px;
	float: left
}

#two {
	width: 50px;
	height: 180px;
	float: left
}

#three {
	width: 200px;
	height: 180px;
	float: left
}

.fabtn {
	width: 50px;
	height: 30px;
	margin-top: 10px;
	cursor: pointer;
}
</style>

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
					<form:form id="inputForm" modelAttribute="pdWareHouse" action="${ctx}/ec/wareHouse/save" method="post" class="form-horizontal">
					<form:hidden path="wareHouseId"/>
						<table class="table table-bordered  table-condensed dataTables-example dataTable no-footer">
							<tr>
								<td><label class="pull-right" ><font color="red">*</font>所属商家：</label></td>
								<td>
									<div style="width:249px;float:left">
										<sys:treeselect id="company" name="company.id" value="${pdWareHouse.company.id}" 
										labelName="company.name" labelValue="${pdWareHouse.company.name}"
										title="公司" url="/sys/franchisee/treeData" cssClass="form-control required"/>
									</div>
								</td>
							</tr>
							<tr>
								<td><label class="pull-right" ><font color="red">*</font>库房名称：</label></td>
								<td>
									<form:input path="name" htmlEscape="false" maxlength="50" style="width:250px;" class="form-control required" />
								</td>
							</tr>
							<tr>
								<td><label class="pull-right"><font color="red">*</font>库房管理员：</label></td>
								<td>
									<form:input path="governor" htmlEscape="false" maxlength="50" style="width:250px;" class="form-control required" />
								</td>
							</tr>
							<tr>
								<td><label class="pull-right"><font color="red">*</font>联系方式：</label></td>
								<td>
									<form:input path="phone" htmlEscape="false" maxlength="11" style="width:250px;" class="form-control required digits" />
								</td>
							</tr>
							<tr>
								<td><label class="pull-right"><font color="red">*</font>库房地址：</label></td>
								<td>
									<div style="width:249px;float:left">
										<sys:treeselect id="areaId" name="area.id"
										value="${pdWareHouse.areaId }" labelName="area.name"
										labelValue="${pdWareHouse.areaName }" title="区域"
										url="/sys/area/treeData" cssClass="form-control required" notAllowSelectParent="true" />	
									</div>						
								</td>
							</tr>
							<tr>
								<td><label class="pull-right"><font color="red">*</font>详细地址：</label></td>
								<td>
									<form:input path="address" htmlEscape="address" maxlength="50" style="width:250px;" class="form-control required" />	
								</td>
							</tr>
							<tr>
								<td><label class="pull-right">邮政编码：</label></td>
								<td>
									<form:input path="postalcode" htmlEscape="postalcode" maxlength="8" style="width:250px;" class="form-control digits" />	
								</td>
							</tr>
							<tr>
								<td><label class="pull-right"><font color="red">*</font>备注：</label></td>
								<td>
									<textarea class="form-control required" maxlength="150" name="remarks" style="width:250px;">${pdWareHouse.remarks}</textarea>
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