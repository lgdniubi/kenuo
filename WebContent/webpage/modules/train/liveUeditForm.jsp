<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/webpage/include/taglib.jsp"%>

<html>
<head>
<title>添加直播会员</title>
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
		
	
		var actiontime = {
				elem : '#validityDate',
				format : 'YYYY-MM-DD hh:mm:ss',
				event : 'focus',
				//min : '',
				istime : true,
				isclear : false,
				istoday : false,
				issure : true,
				festival : true,
				choose : function(datas) {
					var time=datas; 
					//end.max=time;
					//actiontime.max = datas; //结束日选好后，重置开始日的最大日期
				}
			};
		
		laydate(actiontime);
		
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
					<form:form id="inputForm" modelAttribute="trainLiveUser" action="${ctx}/train/liveUser/save" method="post" class="form-horizontal">
					<form:hidden path="id"/>
						<table class="table table-bordered  table-condensed dataTables-example dataTable no-footer">
							<tr>
								<td><label class="pull-right" ><font color="red">*</font>手机号码：</label></td>
								<td>
									<form:input path="mobile" htmlEscape="false" maxlength="11" readonly="true"  class="form-control required" style="width:200px" />
								</td>
							</tr>
							<tr>
								<td><label class="pull-right" ><font color="red">*</font>会员姓名：</label></td>
								<td>
									<form:input path="name" htmlEscape="false" maxlength="64" readonly="true"  style="width:200px;" class="form-control" />
								</td>
							</tr>
							<tr>
								<td><label class="pull-right" ><font color="red">*</font>直播申请编号：</label></td>
								<td>
									<form:input path="auditId" htmlEscape="false" maxlength="10" style="width:200px;" class="form-control required" />
								</td>
							</tr>
							<tr>
								<td><label class="pull-right" ><font color="red">*</font>有效期：</label></td>
								<td>
									<input id="validityDate" name="validityDate" type="text" maxlength="20" class="laydate-icon form-control layer-date input-sm required"
									value="<fmt:formatDate value="${trainLiveUser.validityDate}" pattern="yyyy-MM-dd HH:mm:ss"/>" style="width: 200px;" placeholder="有效期" readonly="readonly" />
								</td>
							</tr>
							<tr>
								<td><label class="pull-right" ><font color="red">*</font>购买金额：</label></td>
								<td>
									<form:input path="money" htmlEscape="false" maxlength="10" style="width:200px;" class="form-control required" />
								</td>
							</tr>
							<tr>
								<td><label class="pull-right" ><font color="red">*</font>支付方式：</label></td>
								<td>
									<form:input path="payment" htmlEscape="false" maxlength="64" style="width:200px;" class="form-control required" />
								</td>
							</tr>
							<tr>
								<td><label class="pull-right" >备　　　　注：</label></td>
								<td>
									<form:textarea path="remak"  htmlEscape="false" rows="3" maxlength="200" class="form-control" ></form:textarea>
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