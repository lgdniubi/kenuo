<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/webpage/include/taglib.jsp"%>
<%@ page import="com.training.modules.sys.utils.ParametersFactory"%>
<%
	String uploadURL = ParametersFactory.getMtmyParamValues("uploader_url");
%>
<html>
<head>
	<title>明星技师</title>
	<meta name="decorator" content="default"/>
	<link rel="stylesheet" href="${ctxStatic}/train/css/exam.css">
	
	<!-- 图片上传 引用-->
	<link rel="stylesheet" type="text/css" href="${ctxStatic}/ec/css/custom_uploadImg.css">
	<link rel="stylesheet" type="text/css" href="${ctxStatic}/train/uploadify/uploadify.css">
	<script type="text/javascript" src="${ctxStatic}/train/uploadify/lang-cn.js"></script>
	<script type="text/javascript" src="${ctxStatic}/train/uploadify/jquery.uploadify.min.js"></script>
	
	<script type="text/javascript">
		var validateForm;
		function doSubmit(){//回调函数，在编辑和保存动作时，供openDialog调用提交表单。
	    	if(validateForm.form()){
	    		loading("正在提交，请稍候...");
				$("#inputForm").submit();
		    	return true;
	    	}
	    	return false;
	    };
	    
		$(document).ready(function() {
			
			
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
				}
			);
		});
	</script>
</head>
<body class="gray-bg">
	<div class="wrapper wrapper-content">
		<div class="ibox">
			<sys:message content="${message}"/>
	    	<div class="ibox-content">
				<div class="tab-content" id="tab-content">
	                <div class="tab-inner">
						<form:form id="inputForm" modelAttribute="starBeautyMapping" action="${ctx}/ec/starBeauty/saveStarBeautyMapping">
							<form:hidden path="mappingId"/>
							<form:hidden path="starId"/>
							<form:hidden path="userId"/>
							<form:hidden path="officeIds"/>
							<table id="contentTable" class="table table-striped table-bordered  table-hover table-condensed  dataTables-example dataTable no-footer">
								<tr>
						  			<td><label class="pull-right"><font color="red">*</font>姓名：</label></td>
						       		<td><form:input path="starBeautyName" style="width: 300px;height:30px;" class="form-control required"/></td>
								</tr>
								<tr>
									<td><label class="pull-right"><font color="red">*</font>图片：</label></td>
									<td>
										<img id="img" src="${starBeautyMapping.starBeautyPhoto}" alt="" style="width: 200px;height: 100px;"/>
										<input class="form-control" id="starBeautyPhoto" name="starBeautyPhoto" type="hidden" value="${starBeautyMapping.starBeautyPhoto}"/><!-- 图片隐藏文本框 -->
										<input type="file" name="file_img_upload" id="file_img_upload" onclick="fileClick()">
										<div id="file_img_queue"></div>
									</td>
								</tr>
								<tr>
									<td><label class="pull-right"><font color="red">*</font>排序：</label></td>
						       		<td><form:input path="sort" style="width: 300px;height:30px;" class="form-control required"/></td>
								</tr>
								<tr>
									<td><label class="pull-right">备注：</label></td>
									<td>
										<form:textarea path="remarks" htmlEscape="false" rows="3" maxlength="200" class="form-control"/>
									</td>
								</tr>
							</table>
						</form:form>
					</div>
				</div>
			</div>
		</div>
	</div>
	<div class="loading"></div>
	<script type="text/javascript">
		$(document).ready(function() {
			$("#file_img_upload").uploadify({
				'buttonText' : '请选择图片',
				'width' : 140,
				'method' : 'post',
				'swf' : '${ctxStatic}/train/uploadify/uploadify.swf',
				'uploader' : '<%=uploadURL%>',
				'fileObjName' : 'file_img_upload',//<input type="file"/>的name
				'queueID' : 'img',//与下面HTML的div.id对应
				'method' : 'post',
				'fileTypeDesc': '支持的格式：*.BMP;*.JPG;*.PNG;*.GIF;',
				'fileTypeExts' : '*.BMP;*.JPG;*.PNG;*.GIF;', //控制可上传文件的扩展名，启用本项时需同时声明fileDesc 
				'fileSizeLimit' : '10MB',//上传文件的大小限制
				'multi' : false,//设置为true时可以上传多个文件
				'auto' : true,//点击上传按钮才上传(false)
				'onFallback' : function(){
					//没有兼容的FLASH时触发
					alert("您未安装FLASH控件，无法上传图片！请安装FLASH控件后再试。");
				},
				'onUploadSuccess' : function(file, data, response) { 
					var jsonData = $.parseJSON(data);//text 转 json
					if(jsonData.result == '200'){
						$("#starBeautyPhoto").val(jsonData.file_url);
						$("#img").attr('src',jsonData.file_url); 
					}
				}
			});
		});
	</script>
</body>
</html>