<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/webpage/include/taglib.jsp"%>
<%@ page import="com.training.modules.sys.utils.ParametersFactory"%>
<html>
<head>
	<title>修改配置</title>
	<meta name="decorator" content="default"/>
	<link rel="stylesheet" href="${ctxStatic}/train/css/addcourse.css">
	<script>
		var validateForm;
		function doSubmit(){//回调函数，在编辑和保存动作时，供openDialog调用提交表单。
		  if(validateForm.form()){
			  $("#inputForm").submit();
			  return true;
		  }
		  return false;
		}
		
		$(document).ready(function() {
			var parentId = $("#parentvalue").val();
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
	</script>
</head>
<body>
	<form:form class="form-horizontal" id="inputForm" modelAttribute="trainLiveSku" action="${ctx}/train/live/saveSku" method="post">
		<sys:message content="${message}"/>
		<div class="modal-body">
			<div class="form-inline">
				<div class="form-group">
					<span style="color: red;">*</span>
					<span>价格：</span> 
					<input type="text" id="price" name="price" maxlength="10" value="${trainLiveSku.price}" class="input-medium form-control required" style="width: 70%;">
				</div>
			</div>
			<div class="form-inline">
				<div class="form-group input-item">
					<span style="color: red;">*</span>
					<span>有效期：</span> 
					<select id="num" name="num" class="input-medium form-control required">
						<option value=1 <c:if test="${trainLiveSku.num == 1}">selected</c:if>>1</option>
						<option value=5 <c:if test="${trainLiveSku.num == 5}">selected</c:if>>5</option>
						<option value=10 <c:if test="${trainLiveSku.num == 10}">selected</c:if>>10</option>
						<option value=15 <c:if test="${trainLiveSku.num == 15}">selected</c:if>>15</option>
						<option value=20 <c:if test="${trainLiveSku.num == 20}">selected</c:if>>20</option>
						<option value=30 <c:if test="${trainLiveSku.num == 30}">selected</c:if>>30</option>
						<option value=60 <c:if test="${trainLiveSku.num == 60}">selected</c:if>>60</option>
						<option value=90 <c:if test="${trainLiveSku.num == 90}">selected</c:if>>90</option>
						<option value=120 <c:if test="${trainLiveSku.num == 120}">selected</c:if>>120</option>
						<option value=180 <c:if test="${trainLiveSku.num == 180}">selected</c:if>>180</option>
						<option value=270 <c:if test="${trainLiveSku.num == 270}">selected</c:if>>270</option>
						<option value=365 <c:if test="${trainLiveSku.num == 365}">selected</c:if>>365</option>
					</select>
				</div>
			</div>
		</div>
	</form:form>
</body>
</html>