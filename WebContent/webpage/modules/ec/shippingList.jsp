<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/webpage/include/taglib.jsp"%>
<html>
<head>
	<title>快递信息</title>
	<meta name="decorator" content="default"/>
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
			
			
			var start = {
				    elem: '#shippingtime',
				    format: 'YYYY-MM-DD hh:mm:ss',
				    event: 'focus',
				    istime: true,				//是否显示时间
				    isclear: true,				//是否显示清除
				    istoday: true,				//是否显示今天
				    issure: true,				//是否显示确定
				    festival: true				//是否显示节日

				};
		
			
				validateForm = $("#inputForm").validate({
					rules:{
					

					},
					messages:{
						
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
				laydate(start);
	    });
</script>
</head>




<body>
	<form:form id="inputForm" modelAttribute="orders" action="${ctx}/ec/orders/UpdateShipping" method="post" class="form-horizontal">
		<table id="contentTable" class="table table-striped table-bordered table-hover table-condensed dataTables-example dataTable">
			<form:hidden path="orderid"/>
			<tbody>
				<tr>
			        <td class="active"><label class="pull-right">发货时间:</label></td>
			         <td><input id="shippingtime" name="shippingtime" type="text" maxlength="20" class="laydate-icon form-control layer-date input-sm required"
							value="<fmt:formatDate value="${orders.shippingtime}" pattern="yyyy-MM-dd HH:mm:ss"/>" />
					</td>
			      </tr>
			      <tr>
			      	 <td class="active"><label class="pull-right">快递单号:</label></td>
			         <td><form:input path="shippingcode" htmlEscape="false" maxlength="30" class="form-control required" /></td>
			      
			      </tr>
			      <tr>
			         <td class="active"><label class="pull-right">快递公司:</label></td>
			         <td><form:input path="shippingname" htmlEscape="false" maxlength="30" class="form-control required" /></td>
			      </tr>
			</tbody>
		</table>
	
	</form:form>
	<div class="ibox-content">
		<h4>物流信息：</h4>
		<hr>
		<c:forEach items="${list}" var="list" >
			<p>${list.uploadTime}</p>
			<p>${list.processInfo}</p>
			<hr>
		</c:forEach>
		
	</div>
	
</body>

</html>