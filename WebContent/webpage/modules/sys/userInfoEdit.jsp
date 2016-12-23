<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/webpage/include/taglib.jsp"%>
<%@ page import="com.training.modules.sys.utils.ParametersFactory"%>
<%
	String uploadURL = ParametersFactory.getMtmyParamValues("uploader_url");
%>
<html>
<head>
	<title>个人信息</title>
	<meta name="decorator" content="default"/>
	<!-- 内容上传 引用-->
	<link rel="stylesheet" type="text/css" href="${ctxStatic}/train/uploadify/uploadify.css">
	<script type="text/javascript" src="${ctxStatic}/train/uploadify/lang-cn.js"></script>
	<script type="text/javascript" src="${ctxStatic}/train/uploadify/jquery.uploadify.min.js"></script>
	
	<script type="text/javascript">
		$(document).ready(function() {
			//课程封面上传
			$("#file_user_upload").uploadify({
				'buttonText' : '请选择头像',
				'method' : 'post',
				'swf' : '${ctxStatic}/train/uploadify/uploadify.swf',
				'uploader' : '<%=uploadURL%>',
				'fileObjName' : 'file_user_upload',//<input type="file"/>的name
				'queueID' : 'file_user_queue',//与下面HTML的div.id对应
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
						$("#photo").val(jsonData.file_url);
						$("#userphoto").attr('src',jsonData.file_url); 
					}
				}
			});
		});
	</script>
	
</head>
<body>
	<form:form id="inputForm" modelAttribute="user" action="${ctx}/sys/user/infoEdit"  method="post" class="form-horizontal form-group">
		<div class="control-group">
		</div>
		<div class="control-group">
			<label class="col-sm-3 control-label">姓名:</label>
			<div class="controls">
				<form:input path="name" htmlEscape="false" maxlength="10"  class="form-control  max-width-250 required" />
			</div>
		</div>
		<div class="control-group">
			<label class="col-sm-3 control-label">邮箱:</label>
			<div class="controls">
				<form:input path="email" htmlEscape="false" maxlength="20" class="form-control  max-width-250 email"/>
			</div>
		</div>
		<div class="control-group">
			<label class="col-sm-3 control-label">电话:</label>
			<div class="controls">
				<form:input path="phone" htmlEscape="false" class="form-control  max-width-250 digits" maxlength="15"/>
			</div>
		</div>
		<%-- 注释手机号码，不能修改
		<div class="control-group">
			<label class="col-sm-3 control-label">手机:</label>
			<div class="controls">
				<form:input path="mobile" class="form-control  max-width-250 required digits" htmlEscape="false" maxlength="11"/>
			</div>
		</div> 
		--%>
		<div class="control-group">
			<label class="col-sm-3 control-label">头像:</label>
			<div class="controls">
				<input type="hidden" id="photo" name="photo" value="${user.photo }">
				<img src="${user.photo}" id="userphoto" name="userphoto" style="width: 250px;height: 120px;" alt="用户头像">
				<!-- 封面上传 -->
				<div class="upload">
					<input type="file" name="file_user_upload" id="file_user_upload">
				</div>
				<div id="file_user_queue"></div> <!--上传队列展示区-->
			</div>
		</div>
		
		<div class="control-group">
			<label class="col-sm-3 control-label">备注:</label>
			<div class="controls">
				<form:textarea path="remarks" htmlEscape="false" rows="3" maxlength="200" style="resize: none;" class="form-control  max-width-250 "/>
			</div>
		</div>
	</form:form>
</body>
</html>