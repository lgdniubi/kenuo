<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/webpage/include/taglib.jsp"%>
<html>
<head>
	<title>用户管理</title>
	<meta name="decorator" content="default"/>
	<script type="text/javascript" language="javascript">
		//导出下载模板	
		function importFileTemplate(){
			window.location.href= '${ctx}/sys/user/import/templateDelete';
		}
		
		var validateForm;
		function doSubmit(){//回调函数，在编辑和保存动作时，供openDialog调用提交表单。
		  if(validateForm.form()){
			  $("#inputForm").submit();
			  return true;
		  }
		  return false;
		}
		
		$(document).ready(function() {
			//表单提交
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
	<form id="inputForm" action="${ctx}/sys/user/importUserDelete" method="post" enctype="multipart/form-data" style="padding-left:20px;"><br/>
		<input id="uploadFile" accept=".xls,.xlsx" name="file" type="file" style="width:330px"/>
		导入文件不能超过5M，仅允许导入“xls”或“xlsx”格式文件！
	</form>
	<input class="btn" style="margin-left: 150px;margin-top: 7px;" type="button" value="下载模板" onclick="importFileTemplate()">
</body>
</html>