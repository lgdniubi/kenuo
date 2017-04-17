<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/webpage/include/taglib.jsp"%>
<%@ page import="com.training.modules.sys.utils.ParametersFactory"%>
<html>
<head>
	<title>添加直播分类</title>
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
	<form:form class="form-horizontal" id="inputForm" modelAttribute="trainLiveCategory" action="${ctx}/train/category/saveCategory" method="post">
		<input type="hidden" id="trainLiveCategoryId" name="trainLiveCategoryId" value="${trainLiveCategory.trainLiveCategoryId}">
		<input type="hidden" id="parentId" name="parentId" value="${trainLiveCategory.parentId}">
		<sys:message content="${message}"/>
		<div class="modal-body">
			<div class="form-inline">
				<div class="form-group">
					<span style="color: red;">*</span>
					<span>名称：</span> 
					<input type="text" id="name" name="name" maxlength="10" value="${trainLiveCategory.name}" class="input-medium form-control required" style="width: 70%;">
				</div>
			</div>
			<div class="form-inline">
				<div class="form-group input-item">
					<span style="color: red;">*</span>
					<span>排序：</span> 
					<input type="text" id="sort" name="sort" maxlength="200" value="${trainLiveCategory.sort}" class="input-medium form-control required" style="width: 70%;">
				</div>
			</div>
		</div>
	</form:form>
</body>
</html>