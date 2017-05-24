<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/webpage/include/taglib.jsp"%>
<%@ page import="com.training.modules.sys.utils.ParametersFactory"%>
<%
	String uploadURL = ParametersFactory.getMtmyParamValues("uploader_url");
%>
<html>
<head>
	<title>供应商联系人管理列表</title>
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
			alert("123456");
		    /* if($("#tabIcon").val() == null || $("#tabIcon").val() == ""){
			   top.layer.alert('图片不可为空！', {icon: 0, title:'提醒'});
			   return false;
		    }
		    if($("#tabSelectIcon").val() == null || $("#tabSelectIcon").val() == ""){
				   top.layer.alert('图片不可为空！', {icon: 0, title:'提醒'});
				   return false;
			    }
	    	if(validateForm.form()){
	    		loading("正在提交，请稍候...");
				$("#inputForm").submit();
		    	return true;
	    	} */
	    	return false;
	    }
		$(document).ready(function() {
			validateForm = $("#inputForm").validate();
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
						<form id="inputForm"  action="${ctx}/ec/goodsSupplierContacts/save">
							<input class="form-control" id="goodsSupplierContactsId" name="goodsSupplierContactsId" type="hidden" value="${goodsSupplierContacts.goodsSupplierContactsId}"/>
							<input class="form-control" id="goodsSupplierId" name="goodsSupplierId" type="hidden" value="${goodsSupplierContacts.goodsSupplierId}"/>
							<table id="contentTable" class="table table-striped table-bordered  table-hover table-condensed  dataTables-example dataTable no-footer">
								<tr>
									<td><label class="pull-right"><font color="red">*</font>姓名：</label></td>
									<td>
										<input class="form-control required" id="name" name="name" type="text" value="${goodsSupplierContacts.name}" style="width: 300px"/>
									</td>
								</tr>
								<tr>
									<td><label class="pull-right"><font color="red">*</font>手机号：</label></td>
									<td>
										<input class="form-control required" id="mobile" name="mobile" type="text" value="${goodsSupplierContacts.mobile}" style="width: 300px"/>
										<span id="sp" style="display: none">请输入正确的手机号码</span>
									</td>
								</tr>
								<tr>
									<td><label class="pull-right"><font color="red">*</font>联系人状态：</label></td>
									<td>
										<select class="form-control required" id="status" name="status">
											<c:if test="${goodsSupplierContacts.status != 1}">
												<option value="0" selected="selected">可用</option>
												<option value="1">不可用</option>
											</c:if>
											<c:if test="${goodsSupplierContacts.status == 1}">
												<option value="0">可用</option>
												<option value="1" selected="selected">不可用</option>
											</c:if>
											
										</select>
									</td>
								</tr>
							</table>
						</form>
					</div>
				</div>
			</div>
		</div>
	</div>
	<div class="loading"></div>
	<script type="text/javascript">
		$(document).ready(function() {
			$("#file_img_upload1").uploadify({
				'buttonText' : '请选择图片',
				'width' : 110,
				'method' : 'post',
				'swf' : '${ctxStatic}/train/uploadify/uploadify.swf',
				'uploader' : '<%=uploadURL%>',
				'fileObjName' : 'file_img_upload1',//<input type="file"/>的name
				'queueID' : 'img1',//与下面HTML的div.id对应
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
						$("#tabIcon").val(jsonData.file_url);
						$("#img1").attr('src',jsonData.file_url); 
					}
				}
			});
			
			$("#file_img_upload2").uploadify({
				'buttonText' : '请选择图片',
				'width' : 110,
				'method' : 'post',
				'swf' : '${ctxStatic}/train/uploadify/uploadify.swf',
				'uploader' : '<%=uploadURL%>',
				'fileObjName' : 'file_img_upload2',//<input type="file"/>的name
				'queueID' : 'img2',//与下面HTML的div.id对应
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
						$("#tabSelectIcon").val(jsonData.file_url);
						$("#img2").attr('src',jsonData.file_url); 
					}
				}
			});
		});
		
		
	</script>
</body>
</html>