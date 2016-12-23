<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/webpage/include/taglib.jsp"%>

<html>
<head>
	<title>退货录入</title>
	<meta name="decorator" content="default"/>
	<!-- 内容上传 引用-->

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
	
			
			$("#reason").focus();
			validateForm = $("#inputForm").validate({
				rules: {
					returnmoney:{
						number:true,
						min:0
						},
					bankno:{
						digits:true
					}
				},
				messages: {
					returnmoney:{
						number:"输入合法的价格",
						min:"价格最小为0"
						},
						bankno:{
							digits:"输入合法的银行卡号"
						}
					
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
		
			//在ready函数中预先调用一次远程校验函数，是一个无奈的回避案。(刘高峰）
			//否则打开修改对话框，不做任何更改直接submit,这时再触发远程校验，耗时较长，
			//submit函数在等待远程校验结果然后再提交，而layer对话框不会阻塞会直接关闭同时会销毁表单，因此submit没有提交就被销毁了导致提交表单失败。
			$("#inputForm").validate().element($("#reason"));
			
// 			laydate({
// 	            elem: '#userinfo.birthday', //目标元素。由于laydate.js封装了一个轻量级的选择器引擎，因此elem还允许你传入class、tag但必须按照这种方式 '#id .class'
// 	            event: 'focus' //响应事件。如果没有传入event，则按照默认的click
// 	        });
			
			
		});

		
</script>
</head>
<body>
	<form:form id="inputForm" modelAttribute="returnGoods" action="${ctx}/ec/returngoods/save" method="post" class="form-horizontal">
		<sys:message content="${message}"/>
		<table class="table table-bordered  table-condensed dataTables-example dataTable no-footer">
		   <tbody>
		     
		      <tr>
		         
		         <td class="active"><label class="pull-right">订单号:</label></td>
		         <td>
					 <form:input path="orderid" htmlEscape="false" maxlength="50" class="form-control" readonly="true"/></td>
					 
		     	 </tr>
		     	 <tr>
		      	
		      	 <td class="active"><label class="pull-right">用户名:</label></td>
		         <td><form:input path="username" htmlEscape="false" maxlength="64" class="form-control" readonly="true"/></td>
		      	
		      	</tr>
		      	
		      	<tr>
		      	<td class="active"><label class="pull-right">支付金额:</label></td>
		         <td><form:input path="orderamount" htmlEscape="false" maxlength="10" class="form-control" readonly="true"/></td>
		      	
		      	</tr>
		      	<tr>
		      	<td class="active" ><label class="pull-right">订单状态:</label></td>
		      	<td>
					<form:select path="orderstatus"  class="form-control">
						<c:if test="${returnGoods.orderstatus==5}">
							<form:option value="5">申请退款</form:option>
						</c:if>
						<c:if test="${returnGoods.orderstatus==3}">
							<form:option value="3">已退款</form:option>
						</c:if>
					</form:select>
		      	</td>
		      	</tr>
		     	<tr>
		         <td class="active"><label class="pull-right"><font color="red">*</font>退款原因:</label></td>
		         <td><form:textarea path="reason" htmlEscape="false" rows="3" maxlength="200" class="form-control required"/></td>
		      </tr>
		  		<tr>
		      	<td class="active" ><label class="pull-right">入库状态:</label></td>
		      	<td>
		      		<form:select path="storagestatus"  class="form-control">
			      		 	<form:option value="1">未入库</form:option>
			      		 	<form:option value="2">已入库</form:option> 	
		      		</form:select>
		      	</td>
		      	</tr>
		      	<tr>
		      	<td class="active"><label class="pull-right"><font color="red">*</font>退款金额:</label></td>
		         <td><form:input path="returnmoney" htmlEscape="false" maxlength="10" class="form-control required"/></td>
		      	
		      	</tr>
		       
		      <tr>
		      	 <td class="active"><label class="pull-right"><font color="red">*</font>退款人姓名:</label></td>
		         <td><form:input path="retusername" htmlEscape="false" maxlength="10" class="form-control required"/></td>
		      
		      </tr>
		      <tr>
		         <td class="active"><label class="pull-right"><font color="red">*</font>退款银行:</label></td>
		         <td><form:input path="bankname" htmlEscape="false" maxlength="20" class="form-control required"/></td>
		      </tr>
		      <tr>
		      	 <td class="active"><label class="pull-right"><font color="red">*</font>银行卡号:</label></td>
		         <td><form:input path="bankno" htmlEscape="false" maxlength="25" class="form-control required"/></td>
		      </tr>
		      
		       <tr>
		         <td class="active"><label class="pull-right">客服备注:</label></td>
		         <td><form:textarea path="remark" htmlEscape="false" rows="3" maxlength="200" class="form-control"/></td>
		      </tr>
		      
		   
		  </tbody>
		  </table>   
		  
	</form:form>
</body>
</html>