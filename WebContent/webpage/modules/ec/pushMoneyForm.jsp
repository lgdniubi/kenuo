<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/webpage/include/taglib.jsp"%>

<html>
<head>
	<title>退货录入</title>
	<meta name="decorator" content="default"/>
	<!-- 内容上传 引用-->

<script type="text/javascript">
		var validateForm;
		var flag=$("#flag").val();//标识:记录业务员的营业额+增减值>=0
		function doSubmit(){//回调函数，在编辑和保存动作时，供openDialog调用提交表单。
		  if(validateForm.form()){
			  if(flag){
				  top.layer.alert('营业额必须大于等于0', {icon: 0, title:'提醒'});
				  return;
			  }
			  var returnAmount=$("#returnAmount").val();
			  var departmentId=$("#departmentIds").val();
			  var turnover = 0;//部门营业额
			  var pushMoneys = "";//当前营业额
			  departmentId = departmentId.split(",");
			  for(var i = 0; i < departmentId.length-1; i++){
				  $("[name='Amount"+departmentId[i]+"']").each(function(){
					  turnover += parseFloat($(this).val());
					  pushMoneys += $(this).val() + ",";
		    	  });
				  if(parseFloat(turnover) - parseFloat(returnAmount) != 0){
					  top.layer.alert('请注意输入的增减值!', {icon: 0, title:'提醒'});
					  return;
				  }
				  turnover = 0;
			  }
			  $("#pushMoneys").val(pushMoneys);
			  $("#inputForm").submit();
			  return true;
		  }
		  return false;
		}
		/* //校验输入营业额+增减值+数据库的增减值>=0
		function returnChangeAmount(obj,pushmoney,added){
			var amount = $(obj).val();
			if(parseFloat(pushmoney) + parseFloat(added) + parseFloat(amount) < 0){
				top.layer.alert('营业额必须大于等于0', {icon: 0, title:'提醒'});
				$("#flag").val(true);
				return;
			}
			$("#flag").val(false);
		} */
		$(document).ready(function(){
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
<div class="wrapper wrapper-content">
	<div class="ibox">
		<div class="ibox-content">
			<div class="clearfix">
				<form:form id="inputForm" modelAttribute="orderPushmoneyRecord" action="${ctx}/ec/returned/saveOrderPushmoneyRecord" method="post" class="form-horizontal">
					<input type="hidden"  id="departmentIds" name="departmentIds" value="${departmentIds}"/>
					<input type="hidden" id="returnAmount" name="returnAmount" value="${returnAmount}"/>
					<!-- 存储需要字段 -->
					<input type="hidden" id="orderId" name="orderId" value="${orderId}"/>
					<input type="hidden" id="returnedId" name="returnedId" value="${returnedId}"/>
					
					<table class="table table-bordered  table-condensed dataTables-example dataTable no-footer">
						<thead>
							<tr>
								<th style="text-align: center;">时间</th>
								<th style="text-align: center;">类型</th>
								<th style="text-align: center;">售后金额</th>
								<th style="text-align: center;">业务员</th>
								<th style="text-align: center;">部门</th>
								<th style="text-align: center;">手机号</th>
								<th style="text-align: center;">已增减营业额</th>
								<th style="text-align: center;">增减营业额占比</th>
								<th style="text-align: center;">收益归属店</th>
								<th style="text-align: center;">剩余总营业额</th>
								<th style="text-align: center;">增减值</th>
							</tr>
						</thead>
						<tbody>
							${userTurnover}
						</tbody>						
					</table>
					<p><font color="red">备注：<br/>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;正数是增加营业额，如：填写90，代表营业额增加90;<br/>
						&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;负数是减少营业额，如：填写-90，代表营业额扣减90</font></p>
				</form:form>
			</div>
		</div>
	</div>
</div>
</body>
</html>