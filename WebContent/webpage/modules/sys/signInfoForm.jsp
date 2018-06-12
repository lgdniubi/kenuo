<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/webpage/include/taglib.jsp"%>
<%@ page import="com.training.modules.sys.utils.ParametersFactory"%>
<%
	String uploadURL = ParametersFactory.getMtmyParamValues("uploader_url");
%>
<html>
<head>
	<title>签约信息</title>
	<meta name="decorator" content="default"/>
	<!-- 日期控件 -->
	<script type="text/javascript" src="${ctxStatic}/My97DatePicker/WdatePicker.js"></script>
	 <!-- 图片上传 引用-->
	<link rel="stylesheet" type="text/css" href="${ctxStatic}/ec/css/custom_uploadImg.css">
	<link rel="stylesheet" type="text/css" href="${ctxStatic}/train/uploadify/uploadify.css">
	<script type="text/javascript" src="${ctxStatic}/train/uploadify/lang-cn.js"></script>
	<script type="text/javascript" src="${ctxStatic}/train/uploadify/jquery.uploadify.min.js"></script>
	
	<link rel="stylesheet" href="${ctxStatic}/train/css/exam.css">
	<!-- 富文本框 -->
	<link rel="stylesheet" href="${ctxStatic}/kindEditor/themes/default/default.css" />
	<script src="${ctxStatic}/kindEditor/kindeditor-all.js" type="text/javascript"></script>
	
	<!-- 富文本框中新增的东西：上传照片，上传代码，商品分类，商品选择 -->
	<script src="${ctxStatic}/kindEditor/themes/editJs/editJs.js" type="text/javascript"></script>
	
	<!-- 富文本框上传图片样式引用 -->
	<link rel="stylesheet" type="text/css" href="${ctxStatic}/kindEditor/themes/editCss/edit.css">
	<style>
	</style>
	<script type="text/javascript">
		var newUploadURL = '<%=uploadURL%>';
		var validateForm;
		function saveSign(){//回调函数，在编辑和保存动作时，供openDialog调用提交表单。
			//if(validateForm.form()){
				$("#inputForm").submit();
				//return true;
			//}
		  return false;
		}
		$(document).ready(function() {
			$("#sign_username").focus();
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
		
			$("#file_sign_fonturl_upload").uploadify({
				'buttonText' : ' 请选择图片',
				'method' : 'post',
				'swf' : '${ctxStatic}/train/uploadify/uploadify.swf',
				'uploader' : '<%=uploadURL%>',
				'fileObjName' : 'file_sign_fonturl_upload',//<input type="file"/>的name
				'queueID' : 'file_sign_fonturl_queue',//与下面HTML的div.id对应
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
						$("#sign_fonturl").val(jsonData.file_url);
						$("#officesign_fonturlImgsrc").attr('src',jsonData.file_url); 
					}
				}
			});	
			$("#file_sign_backurl_upload").uploadify({
				'buttonText' : ' 请选择图片',
				'method' : 'post',
				'swf' : '${ctxStatic}/train/uploadify/uploadify.swf',
				'uploader' : '<%=uploadURL%>',
				'fileObjName' : 'file_sign_backurl_upload',//<input type="file"/>的name
				'queueID' : 'file_sign_backurl_queue',//与下面HTML的div.id对应
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
						$("#sign_backurl").val(jsonData.file_url);
						$("#officesign_backurlImgsrc").attr('src',jsonData.file_url); 
					}
				}
			});	
			$("#file_cargo_backurl_upload").uploadify({
				'buttonText' : ' 请选择图片',
				'method' : 'post',
				'swf' : '${ctxStatic}/train/uploadify/uploadify.swf',
				'uploader' : '<%=uploadURL%>',
				'fileObjName' : 'file_cargo_backurl_upload',//<input type="file"/>的name
				'queueID' : 'file_cargo_backurl_queue',//与下面HTML的div.id对应
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
						$("#cargo_backurl").val(jsonData.file_url);
						$("#officecargo_backurlImgsrc").attr('src',jsonData.file_url); 
					}
				}
			});	
			$("#file_cargo_fonturl_upload").uploadify({
				'buttonText' : ' 请选择图片',
				'method' : 'post',
				'swf' : '${ctxStatic}/train/uploadify/uploadify.swf',
				'uploader' : '<%=uploadURL%>',
				'fileObjName' : 'file_cargo_fonturl_upload',//<input type="file"/>的name
				'queueID' : 'file_cargo_fonturl_queue',//与下面HTML的div.id对应
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
						$("#cargo_fonturl").val(jsonData.file_url);
						$("#officecargo_fonturlImgsrc").attr('src',jsonData.file_url); 
					}
				}
			});	
			
			$("#file_audit_fonturl_upload").uploadify({
				'buttonText' : ' 请选择图片',
				'method' : 'post',
				'swf' : '${ctxStatic}/train/uploadify/uploadify.swf',
				'uploader' : '<%=uploadURL%>',
				'fileObjName' : 'file_audit_fonturl_upload',//<input type="file"/>的name
				'queueID' : 'file_audit_fonturl_queue',//与下面HTML的div.id对应
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
						$("#audit_fonturl").val(jsonData.file_url);
						$("#officeaudit_fonturlImgsrc").attr('src',jsonData.file_url); 
					}
				}
			});	
			$("#file_audit_backurl_upload").uploadify({
				'buttonText' : ' 请选择图片',
				'method' : 'post',
				'swf' : '${ctxStatic}/train/uploadify/uploadify.swf',
				'uploader' : '<%=uploadURL%>',
				'fileObjName' : 'file_audit_backurl_upload',//<input type="file"/>的name
				'queueID' : 'file_audit_backurl_queue',//与下面HTML的div.id对应
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
						$("#audit_backurl").val(jsonData.file_url);
						$("#officeaudit_backurlImgsrc").attr('src',jsonData.file_url); 
					}
				}
			});	
			$("#file_proxy_fonturl_upload").uploadify({
				'buttonText' : ' 请选择图片',
				'method' : 'post',
				'swf' : '${ctxStatic}/train/uploadify/uploadify.swf',
				'uploader' : '<%=uploadURL%>',
				'fileObjName' : 'file_proxy_fonturl_upload',//<input type="file"/>的name
				'queueID' : 'file_proxy_fonturl_queue',//与下面HTML的div.id对应
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
						$("#proxy_fonturl").val(jsonData.file_url);
						$("#officeproxy_fonturlImgsrc").attr('src',jsonData.file_url); 
					}
				}
			});	
			$("#file_proxy_backurl_upload").uploadify({
				'buttonText' : ' 请选择图片',
				'method' : 'post',
				'swf' : '${ctxStatic}/train/uploadify/uploadify.swf',
				'uploader' : '<%=uploadURL%>',
				'fileObjName' : 'file_proxy_backurl_upload',//<input type="file"/>的name
				'queueID' : 'file_proxy_backurl_queue',//与下面HTML的div.id对应
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
						$("#proxy_backurl").val(jsonData.file_url);
						$("#officeproxy_backurlImgsrc").attr('src',jsonData.file_url); 
					}
				}
			});	
			$("#file_pay_fonturl_upload").uploadify({
				'buttonText' : ' 请选择图片',
				'method' : 'post',
				'swf' : '${ctxStatic}/train/uploadify/uploadify.swf',
				'uploader' : '<%=uploadURL%>',
				'fileObjName' : 'file_pay_fonturl_upload',//<input type="file"/>的name
				'queueID' : 'file_pay_fonturl_queue',//与下面HTML的div.id对应
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
						$("#pay_fonturl").val(jsonData.file_url);
						$("#officepay_fonturlImgsrc").attr('src',jsonData.file_url); 
					}
				}
			});	
			$("#file_pay_backurl_upload").uploadify({
				'buttonText' : ' 请选择图片',
				'method' : 'post',
				'swf' : '${ctxStatic}/train/uploadify/uploadify.swf',
				'uploader' : '<%=uploadURL%>',
				'fileObjName' : 'file_pay_backurl_upload',//<input type="file"/>的name
				'queueID' : 'file_pay_backurl_queue',//与下面HTML的div.id对应
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
						$("#pay_backurl").val(jsonData.file_url);
						$("#officepay_backurlImgsrc").attr('src',jsonData.file_url); 
					}
				}
			});	
		});
	
		
			
			
			
	
			
			
	</script>
</head>
<body>
	<div class="ibox-content">
	<div>
	<label class="pull-left"><a href="${ctx}/sys/office/form?id=${office.id}">基础信息</a>-----</label>
	<label class="pull-left"><a href="#">签约信息</a></label>
	</div>
		<form:form id="inputForm" modelAttribute="office" action="${ctx}/sys/office/saveSignInfo" method="post" class="form-horizontal">
			<form:hidden path="id"/>
			<input type="hidden" name="create_user" value="${user.id}"/>
			<input type="hidden" name="payInfos[0].create_user" value="${user.id}"/>
			<input type="hidden" name="payInfos[1].create_user" value="${user.id}"/>
			<input type="hidden" name="payInfos[2].create_user" value="${user.id}"/>
			<input type="hidden" name="office_id" value="${office.id}"/>
			<input type="hidden" name="franchisee_id" value="${office.franchisee.id}"/>
			<input type="hidden" name="office_pid" value="${office.parent.id}"/>
			<input type="hidden" name="office_pids" value="${office.parentIds}"/>
			<input type="hidden" name="office_name" value="${office.name}"/>
			<sys:message content="${message}"/>
				<table class="table table-bordered  table-condensed dataTables-example dataTable no-footer">
				   <tbody>
						<tr><td colspan="4" class="active"><label class="pull-left">签约信息</label></td></tr>
						<tr><td colspan="4" class=""><label class="pull-left">管理员</label></td></tr>
						<tr>
							<td  class="width-15 active"><label class="pull-right"><font color="red"></font>姓名:</label></td>
				         	<td class="width-35"><sys:treeselect id="sign_username" name="sign_userid" value="${infoVo.sign_userid}" labelName="sign_username" labelValue="${infoVo.sign_username}"
								title="姓名" url="/sys/user/treeData?officeId=${office.franchisee.id}"  cssClass="form-control required"/></td>
				         	<td class="width-35" rowspan="4">
				         		<img id="officesign_fonturlImgsrc" src="${infoVo.sign_fonturl}" alt="" style="width: 200px;height: 100px;"/>
								<input type="hidden" id="sign_fonturl" name="sign_fonturl" value="${infoVo.sign_fonturl}"><!-- 图片隐藏文本框 -->
								<p>&nbsp;</p>
			                   	<div class="upload">
									<input type="file" name="file_sign_fonturl_upload" id="file_sign_fonturl_upload">
								</div>
								<div id="file_sign_fonturl_queue"></div>
				         	</td>
				         	<td class="width-35" rowspan="4">
				         		<img id="officesign_backurlImgsrc" src="${infoVo.sign_backurl}" alt="" style="width: 200px;height: 100px;"/>
								<input type="hidden" id="sign_backurl" name="sign_backurl" value="${infoVo.sign_backurl}"><!-- 图片隐藏文本框 -->
								<p>&nbsp;</p>
			                   	<div class="upload">
									<input type="file" name="file_sign_backurl_upload" id="file_sign_backurl_upload">
								</div>
								<div id="file_sign_backurl_queue"></div>
				         	</td>
						</tr>
						<tr>
							<td  class="width-15 active"><label class="pull-right"><font color="red"></font>身份证:</label></td>
				         	<td class="width-35"><input value="${infoVo.sign_idcard}" name="sign_idcard" class="form-control required"></td>
						</tr>
						<tr>
							<td  class="width-15 active"><label class="pull-right"><font color="red"></font>联系电话:</label></td>
				         	<td class="width-35"><input value="${infoVo.sign_mobile}" name="sign_mobile" class="form-control required"></td>
						</tr>
						<tr>
							<td  class="width-15 active"><label class="pull-right"><font color="red"></font>e-mail:</label></td>
				         	<td class="width-35"><input value="${infoVo.sign_email}" name="sign_email" class="form-control required"></td>
						</tr>
						
						
						<tr><td colspan="4" class=""><label class="pull-left">报货人</label></td></tr>
						<tr>
							<td  class="width-15 active"><label class="pull-right"><font color="red"></font>姓名:</label></td>
				         	<td class="width-35"><sys:treeselect id="cargo" name="cargo_userid" value="${infoVo.cargo_userid}" labelName="cargo_username" labelValue="${infoVo.cargo_username}"
								title="姓名" url="/sys/user/treeDataCompany?officeId=${office.franchisee.id}"  cssClass="form-control required required"/></td>
				         	<td class="width-35" rowspan="4">
				         		<img id="officecargo_fonturlImgsrc" src="${infoVo.cargo_fonturl}" alt="" style="width: 200px;height: 100px;"/>
								<input type="hidden" id="cargo_fonturl" name="cargo_fonturl" value="${infoVo.cargo_fonturl}"><!-- 图片隐藏文本框 -->
								<p>&nbsp;</p>
			                   	<div class="upload">
									<input type="file" name="file_cargo_fonturl_upload" id="file_cargo_fonturl_upload">
								</div>
								<div id="file_cargo_fonturl_queue"></div>
				         	</td>
				         	<td class="width-35" rowspan="4">
				         		<img id="officecargo_backurlImgsrc" src="${infoVo.cargo_backurl}" alt="" style="width: 200px;height: 100px;"/>
								<input type="hidden" id="cargo_backurl" name="cargo_backurl" value="${infoVo.cargo_backurl}"><!-- 图片隐藏文本框 -->
								<p>&nbsp;</p>
			                   	<div class="upload">
									<input type="file" name="file_cargo_backurl_upload" id="file_cargo_backurl_upload">
								</div>
								<div id="file_cargo_backurl_queue"></div>
				         	</td>
						</tr>
						<tr>
							<td  class="width-15 active"><label class="pull-right"><font color="red"></font>身份证:</label></td>
				         	<td class="width-35"><input value="${infoVo.cargo_idcard}" name="cargo_idcard" class="form-control required"></td>
						</tr>
						<tr>
							<td  class="width-15 active"><label class="pull-right"><font color="red"></font>联系电话:</label></td>
				         	<td class="width-35"><input value="${infoVo.cargo_mobile}" name="cargo_mobile" class="form-control required"></td>
						</tr>
						<tr>
							<td  class="width-15 active"><label class="pull-right"><font color="red"></font>e-mail:</label></td>
				         	<td class="width-35"><input value="${infoVo.cargo_email}" name="cargo_email" class="form-control required"></td>
						</tr>
						
						
						<tr><td colspan="4" class=""><label class="pull-left">审核人</label></td></tr>
						<tr>
							<td  class="width-15 active"><label class="pull-right"><font color="red"></font>姓名:</label></td>
				         	<td class="width-35"><sys:treeselect id="audit" name="audit_userid" value="${infoVo.audit_userid}" labelName="audit_username" labelValue="${infoVo.audit_username}"
								title="姓名" url="/sys/user/treeDataCompany?officeId=${office.franchisee.id}"  cssClass="form-control required required"/></td>
				         	<td class="width-35" rowspan="4">
				         		<img id="officeaudit_fonturlImgsrc" src="${infoVo.audit_fonturl}" alt="" style="width: 200px;height: 100px;"/>
								<input type="hidden" id="audit_fonturl" name="audit_fonturl" value="${infoVo.audit_fonturl}"><!-- 图片隐藏文本框 -->
								<p>&nbsp;</p>
			                   	<div class="upload">
									<input type="file" name="file_audit_fonturl_upload" id="file_audit_fonturl_upload">
								</div>
								<div id="file_audit_fonturl_queue"></div>
				         	</td>
				         	<td class="width-35" rowspan="4">
				         		<img id="officeaudit_backurlImgsrc" src="${infoVo.audit_backurl}" alt="" style="width: 200px;height: 100px;"/>
								<input type="hidden" id="audit_backurl" name="audit_backurl" value="${infoVo.audit_backurl}"><!-- 图片隐藏文本框 -->
								<p>&nbsp;</p>
			                   	<div class="upload">
									<input type="file" name="file_audit_backurl_upload" id="file_audit_backurl_upload">
								</div>
								<div id="file_audit_backurl_queue"></div>
				         	</td>
						</tr>
						<tr>
							<td  class="width-15 active"><label class="pull-right"><font color="red"></font>身份证:</label></td>
				         	<td class="width-35"><input value="${infoVo.audit_idcard}" name="audit_idcard" class="form-control required"></td>
						</tr>
						<tr>
							<td  class="width-15 active"><label class="pull-right"><font color="red"></font>联系电话:</label></td>
				         	<td class="width-35"><input value="${infoVo.audit_mobile}" name="audit_mobile" class="form-control required"></td>
						</tr>
						<tr>
							<td  class="width-15 active"><label class="pull-right"><font color="red"></font>e-mail:</label></td>
				         	<td class="width-35"><input value="${infoVo.audit_email}" name="audit_email" class="form-control required"></td>
						</tr>
						
						
						<tr><td colspan="4" class=""><label class="pull-left">付款人</label></td></tr>
						<tr>
							<td  class="width-15 active"><label class="pull-right"><font color="red"></font>姓名:</label></td>
				         	<td class="width-35"><sys:treeselect id="proxy" name="proxy_userid" value="${infoVo.proxy_userid}" labelName="proxy_username" labelValue="${infoVo.proxy_username}"
								title="姓名" url="/sys/user/treeDataCompany?officeId=${office.franchisee.id}"  cssClass="form-control required required"/></td>
				         	<td class="width-35" rowspan="4">
				         		<img id="officeproxy_fonturlImgsrc" src="${infoVo.proxy_fonturl}" alt="" style="width: 200px;height: 100px;"/>
								<input type="hidden" id="proxy_fonturl" name="proxy_fonturl" value="${infoVo.proxy_fonturl}"><!-- 图片隐藏文本框 -->
								<p>&nbsp;</p>
			                   	<div class="upload">
									<input type="file" name="file_proxy_fonturl_upload" id="file_proxy_fonturl_upload">
								</div>
								<div id="file_proxy_fonturl_queue"></div>
				         	</td>
				         	<td class="width-35" rowspan="4">
				         		<img id="officeproxy_backurlImgsrc" src="${infoVo.proxy_backurl}" alt="" style="width: 200px;height: 100px;"/>
								<input type="hidden" id="proxy_backurl" name="proxy_backurl" value="${infoVo.proxy_backurl}"><!-- 图片隐藏文本框 -->
								<p>&nbsp;</p>
			                   	<div class="upload">
									<input type="file" name="file_proxy_backurl_upload" id="file_proxy_backurl_upload">
								</div>
								<div id="file_proxy_backurl_queue"></div>
				         	</td>
						</tr>
						<tr>
							<td  class="width-15 active"><label class="pull-right"><font color="red"></font>身份证:</label></td>
				         	<td class="width-35"><input value="${infoVo.proxy_idcard}" name="proxy_idcard" class="form-control required"></td>
						</tr>
						<tr>
							<td  class="width-15 active"><label class="pull-right"><font color="red"></font>联系电话:</label></td>
				         	<td class="width-35"><input value="${infoVo.proxy_mobile}" name="proxy_mobile" class="form-control required"></td>
						</tr>
						<tr>
							<td  class="width-15 active"><label class="pull-right"><font color="red"></font>e-mail:</label></td>
				         	<td class="width-35"><input value="${infoVo.proxy_email}" name="proxy_email" class="form-control required"></td>
						</tr>
						
						
						<tr><td colspan="4" class=""><label class="pull-left">付款账户</label></td></tr>
						<tr>
							<td  class="width-15 active"><label class="pull-right"><font color="red"></font>开户银行</label></td>
				         	<td class="width-35"><input value="${payInfos[0].pay_name}" name="payInfos[0].pay_name" class="form-control required"></td>
				         	<td class="width-35" rowspan="3">
				         		<img id="officepay_fonturlImgsrc" src="${payInfos[0].pay_fonturl}" alt="" style="width: 200px;height: 100px;"/>
								<input type="hidden" id="pay_fonturl" name="payInfos[0].pay_fonturl" value="${payInfos[0].pay_fonturl}"><!-- 图片隐藏文本框 -->
								<p>&nbsp;</p>
			                   	<div class="upload">
									<input type="file" name="file_pay_fonturl_upload" id="file_pay_fonturl_upload">
								</div>
								<div id="file_pay_fonturl_queue"></div>
				         	</td>
				         	<td class="width-35" rowspan="3">
				         		<img id="officepay_backurlImgsrc" src="${payInfos[0].pay_backurl}" alt="" style="width: 200px;height: 100px;"/>
								<input type="hidden" id="pay_backurl" name="payInfos[0].pay_backurl" value="${payInfos[0].pay_backurl}"><!-- 图片隐藏文本框 -->
								<p>&nbsp;</p>
			                   	<div class="upload">
									<input type="file" name="file_pay_backurl_upload" id="file_pay_backurl_upload">
								</div>
								<div id="file_pay_backurl_queue"></div>
				         	</td>
						</tr>
							<input value="0" name="payInfos[0].pay_type" type="hidden">
							<input value="1" name="payInfos[1].pay_type" type="hidden">
							<input value="2" name="payInfos[2].pay_type" type="hidden">
							<input value="支付宝" name="payInfos[1].pay_name" type="hidden">
							<input value="微信" name="payInfos[2].pay_name" type="hidden">
						<tr>
							<td  class="width-15 active"><label class="pull-right"><font color="red"></font>账户名称:</label></td>
				         	<td class="width-35"><input value="${payInfos[0].pay_username}" name="payInfos[0].pay_username" class="form-control required"></td>
						</tr>
						<tr>
							<td  class="width-15 active"><label class="pull-right"><font color="red"></font>银行账号:</label></td>
				         	<td class="width-35"><input value="${payInfos[0].pay_account}" name="payInfos[0].pay_account" class="form-control required"></td>
						</tr>
						<tr>
						<td colspan="2" class=""><label class="pull-left">支付宝</label></td>
						<td colspan="2" class=""><label class="pull-left">微信</label></td>
						</tr>
						<tr>
							<td  class="width-15 active"><label class="pull-right"><font color="red"></font>账号</label></td>
				         	<td class="width-35"><input value="${payInfos[2].pay_account}" name="payInfos[2].pay_account" class="form-control required"></td>
				         	<td class="width-35">账号</td>
				         	<td class="width-35"><input value="${payInfos[1].pay_account}" name="payInfos[1].pay_account" class="form-control required"></td>
						</tr>
						<tr>
							<td  class="width-15 active"><label class="pull-right"><font color="red"></font>姓名:</label></td>
				         	<td class="width-35"><input value="${payInfos[2].pay_username}" name="payInfos[2].pay_username" class="form-control required"></td>
							<td  class="width-15 active"><label class="pull-right"><font color="red"></font>姓名:</label></td>
				         	<td class="width-35"><input value="${payInfos[1].pay_username}" name="payInfos[1].pay_username" class="form-control required"></td>
						</tr>
						<tr>
							<td  class="width-15 active"><label class="pull-right"><font color="red"></font>电话:</label></td>
				         	<td class="width-35"><input value="${payInfos[2].pay_mobile}" name="payInfos[2].pay_mobile" class="form-control required"></td>
							<td  class="width-15 active"><label class="pull-right"><font color="red"></font>电话:</label></td>
				         	<td class="width-35"><input value="${payInfos[1].pay_mobile}" name="payInfos[1].pay_mobile" class="form-control required"></td>
						</tr>
						
			      </tbody>
			     
		      </table>
		      <input type="button" value="保存签约信息" onclick="saveSign()"/>
			  <a href="${ctx}/sys/office/form?id=${office.id}">上一步</a>
		</form:form>
		<div class="loading"></div>
	</div>
</body>
</html>