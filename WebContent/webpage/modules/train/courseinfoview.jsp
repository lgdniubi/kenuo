<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/webpage/include/taglib.jsp"%>
<html>
<head>
	<title>课程信息修改</title>
	<meta name="decorator" content="default"/>
	<link rel="stylesheet" href="${ctxStatic}/train/css/addcourse.css">
	<script type="text/javascript" src="${ctxStatic}/jquery-validation/1.14.0/jquery.validate.js"></script>
	<script type="text/javascript" src="${ctxStatic}/jquery-validation/1.14.0/localization/messages_zh.min.js"></script>
	<script type="text/javascript" language="javascript">
		
		//课程类型选择事件
		function lessontypechange(v){
			$("#lessontype").val(v);
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
			//课程类型回填
			var lessontype = $("#lessontype").val();
			$("#s1 option").each(function(){
				$this = $(this).val();
				if($this == lessontype){
					$(this).attr("selected",true);
				}
			});
			
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
	<div class="modal-body">
		<div class="form-inline">
			<div class="form-group">
				<span style="color: red;">*</span>
				<span>课程名称：</span> ${trainLessons.name}
			</div>
		</div>
		<div class="form-inline">
			<div class="form-group input-item">
				<span style="color: red;">*</span>
				<span>课程ID：${trainLessons.lessonId}</span> 
			</div>
		</div>
		<div class="form-inline">
			<div class="form-group input-item">
				<span style="color: red;">*</span>
				<span>课程分类ID：${trainLessons.trainCategorys.categoryId}</span> 
			</div>
		</div>
	</div>
</body>
</html>