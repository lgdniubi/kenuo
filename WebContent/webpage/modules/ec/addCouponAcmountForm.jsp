<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/webpage/include/taglib.jsp"%>

<html>
<head>
<title>添加红包</title>
<meta name="decorator" content="default" />
<!-- 内容上传 引用-->
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

	function ondeit(o){
		var val=$(o).val();
		if(val=="0.0"){
			$(o).val("");
		}
		if(val=="0"){
			$(o).val("");
		}
		
	}
	
	function bldeit(o){
		var val=$(o).val();
		if(val==""){
			$(o).val(0);
		}
	}
	

	$(document).ready(function() {
		


			
				validateForm = $("#inputForm").validate({
					rules : {
						

					},
					messages:{
							
					},

					submitHandler : function(form) {
						//loading('正在提交，请稍等...');
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
					<form:form id="inputForm" modelAttribute="couponAcmountMapping" action="#" method="post" class="form-horizontal">
						<form:hidden path="amountId"/>
						<form:hidden path="couponId"/>
							<table class="table  table-bordered  table-hovertable-condensed   dataTables-example dataTable no-footer">
								<tr>
									<td><label class="pull-right"><font color="red">*</font>面值名称：</label></td>
									<td><input id="amountName" name="amountName" value="${couponAcmountMapping.amountName}"  style="width:200px;" maxlength="50" class="form-control required" /></td>
								</tr>
								<tr>
									<td><label class="pull-right"><font color="red">*</font>满减金额：</label></td>
									<td><input id="baseAmount" name="baseAmount" value="${couponAcmountMapping.baseAmount}" onclick="ondeit(this)" onBlur="bldeit(this)" style="width:200px;" maxlength="50" class="form-control required" /></td>
								</tr>
								<tr>	
									<td><label class="pull-right"><font color="red">*</font>红包金额：</label></td>
									<td><input id="couponMoney" name="couponMoney" value="${couponAcmountMapping.couponMoney}" onclick="ondeit(this)" onBlur="bldeit(this)"  style="width:200px;" maxlength="50" class="form-control required" /></td>
								</tr>
								<tr>
									<td><label class="pull-right"><font color="red">*</font>发放数量：</label></td>
									<td><input id="totalNumber" name="totalNumber" value="${couponAcmountMapping.totalNumber}" onclick="ondeit(this)" onBlur="bldeit(this)"  style="width:200px;" maxlength="50" class="form-control required" /></td>
								</tr>
								
							</table>
						
						</form:form>
						
						
				</div>
				
			</div>
		</div>
	</div>
	
</body>
</html>