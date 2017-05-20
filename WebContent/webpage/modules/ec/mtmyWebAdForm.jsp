<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/webpage/include/taglib.jsp"%>
<%@ page import="com.training.modules.sys.utils.ParametersFactory"%>
<%
	String uploadURL = ParametersFactory.getMtmyParamValues("uploader_url");
%>
<html>
<head>
	<title>首页广告图</title>
	<meta name="decorator" content="default"/>
	<link rel="stylesheet" href="${ctxStatic}/train/css/exam.css">
	
	<!-- 图片上传 引用-->
	<link rel="stylesheet" type="text/css" href="${ctxStatic}/ec/css/custom_uploadImg.css">
	<link rel="stylesheet" type="text/css" href="${ctxStatic}/train/uploadify/uploadify.css">
	<script type="text/javascript" src="${ctxStatic}/train/uploadify/lang-cn.js"></script>
	<script type="text/javascript" src="${ctxStatic}/train/uploadify/jquery.uploadify.min.js"></script>
	
	<script type="text/javascript">
	$(document).ready(function() {
		validateForm = $("#inputForm").validate();
	});
	
		var validateForm;
		function doSubmit(){//回调函数，在编辑和保存动作时，供openDialog调用提交表单。
			 if($("#headImg").val() == null || $("#headImg").val() == ""){
				   top.layer.alert('头图不可为空！', {icon: 0, title:'提醒'});
				   return false;
			    }
		
		    if($("#suboriginalImg").val() == null || $("#suboriginalImg").val() == ""){
			   top.layer.alert('原始图不可为空！', {icon: 0, title:'提醒'});
			   return false;
		    }
	    	if(validateForm.form()){
	    		loading("正在提交，请稍候...");
				$("#inputForm").submit();
		    	return true;
	    	}
	    	return false;
	    }
		
	</script>
</head>
<body class="gray-bg">
	<div class="wrapper wrapper-content">
		<div class="ibox">
			<sys:message content="${message}"/>
	    	<div class="ibox-content">
				<div class="tab-content" id="tab-content">
	                <div class="tab-inner">
						<form id="inputForm" action="${ctx}/ec/webAd/save">
							<input class="form-control" id="mtmyWebAdId" name="mtmyWebAdId" type="hidden" value="${mtmyWebAd.mtmyWebAdId}"/>
							<input id="categoryId" type="hidden" name="categoryId" value="${mtmyWebAd.categoryId}">
							<input id="positionType" type="hidden" name="positionType" value="${mtmyWebAd.positionType}">
							<table id="contentTable" class="table table-striped table-bordered  table-hover table-condensed  dataTables-example dataTable no-footer">
								<tr>
									<td><label class="pull-right"><font color="red">*</font>名称：</label></td>
									<td>
										<input class="form-control required" id="name" name="name" type="text" value="${mtmyWebAd.name }" style="width: 300px"/>
									</td>
								</tr>
								<tr>
									<td><label class="pull-right"><font color="red">*</font>方向:</label></td>
									<td>
										<select class="form-control required" id="align" name="align">
											<option value='0' <c:if test="${mtmyWebAd.align == '0'}">selected</c:if>>居中</option>
											<option value='1' <c:if test="${mtmyWebAd.align == '1'}">selected</c:if>>左边</option>
											<option value='2' <c:if test="${mtmyWebAd.align == '2'}">selected</c:if>>右上</option>
											<option value='3' <c:if test="${mtmyWebAd.align == '3'}">selected</c:if>>右下</option>
										</select>
									</td>
								</tr>
								<tr>
									<td><label class="pull-right"><font color="red">*</font>原始图：</label></td>
									<td>
										<img id="img2" src="${mtmyWebAd.suboriginalImg }" alt="" style="width: 200px;height: 100px;" />
										<input class="form-control" id="suboriginalImg" name="suboriginalImg" type="hidden" value="${mtmyWebAd.suboriginalImg }"/><!-- 图片隐藏文本框 -->
										<input type="file" name="file_img_upload2" id="file_img_upload2">
										<div id="file_img_queue2"></div>
									</td>
								</tr>
								<tr>
									<td><label class="pull-right"><font color="red">*</font>头图：</label></td>
									<td>
										<img id="img1" src="${mtmyWebAd.headImg }" alt="" style="width: 200px;height: 100px;" />
										<input class="form-control" id="headImg" name="headImg" type="hidden" value="${mtmyWebAd.headImg }"/><!-- 图片隐藏文本框 -->
										<input type="file" name="file_img_upload1" id="file_img_upload1">
										<div id="file_img_queue1"></div>
									</td>
								</tr>
								<tr>
									<td><label class="pull-right"><font color="red">*</font>类型：</label></td>
									<td>
										<select class="form-control required" id="dataType" name="dataType" >
											<option value='1' <c:if test="${mtmyWebAd.dataType == '1'}">selected</c:if>>动态</option>
											<option value='2' <c:if test="${mtmyWebAd.dataType == '2'}">selected</c:if>>静态</option>
										</select>
									</td>
								</tr>
								<tr id = "urls">
									<td><label class="pull-right"><font color="red">*</font>链接：</label></td>
									<td>
										<input class="form-control required" id="redirectUrl" name="redirectUrl" type="text" value="${mtmyWebAd.redirectUrl }" style="width: 300px"/>
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
						$("#headImg").val(jsonData.file_url);
						$("#img1").attr('src',jsonData.file_url); 
						$("#img1").attr('style',"width: 200px;height: 100px;border:1px solid black;"); 
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
						$("#suboriginalImg").val(jsonData.file_url);
						$("#img2").attr('src',jsonData.file_url); 
						$("#img2").attr('style',"width: 200px;height: 100px;border:1px solid black;"); 
					}
				}
			});
		});
	</script>
</body>
</html>