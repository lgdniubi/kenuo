<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/webpage/include/taglib.jsp"%>
<%@ page import="com.training.modules.sys.utils.ParametersFactory"%>
<%
	String uploadURL = ParametersFactory.getMtmyParamValues("uploader_url");
%>
<html>
<head>
	<title>banner图</title>
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
		    if($("#adPic").val() == null || $("#adPic").val() == ""){
			   top.layer.alert('banner图不可为空！', {icon: 0, title:'提醒'});
			   return false;
		    }
	    	if(validateForm.form()){
	    		loading("正在提交，请稍候...");
				$("#inputForm").submit();
		    	return true;
	    	}
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
						<form id="inputForm" action="${ctx}/trains/banner/save">
							<input class="form-control" id="adId" name="adId" type="hidden" value="${trainsBanner.adId }"/>
							<table id="contentTable" class="table table-striped table-bordered  table-hover table-condensed  dataTables-example dataTable no-footer">
								<tr>
									<td><label class="pull-right"><font color="red">*</font>banner图名称：</label></td>
									<td>
										<input class="form-control required" id="adName" name="adName" type="text" value="${trainsBanner.adName }" style="width: 300px"/>
									</td>
								</tr>
								<tr>
									<td><label class="pull-right"><font color="red">*</font>banner图：</label></td>
									<td>
										<img id="img" src="${trainsBanner.adPic }" alt="" style="width: 200px;height: 100px;"/>
										<input class="form-control" id="adPic" name="adPic" type="hidden" value="${trainsBanner.adPic }"/><!-- 图片隐藏文本框 -->
										<input type="file" name="file_img_upload" id="file_img_upload">
										<div id="file_img_queue"></div>
									</td>
								</tr>
								<tr>
									<td><label class="pull-right"><font color="red">*</font>类型：</label></td>
									<td>
										<select class="form-control" id="adType" name="adType">
											<c:if test="${trainsBanner.adType == 1 }">
												<option value="0">首页</option>
												<option value="1" selected="selected">提问</option>
											</c:if>
											<c:if test="${trainsBanner.adType != 1 }">
												<option value="0">首页</option>
												<option value="1">提问</option>
											</c:if>
										</select>
									</td>
								</tr>
								<tr>
									<td><label class="pull-right">链接：</label></td>
									<td>
										<input class="form-control" id="adUrl" name="adUrl" type="text" value="${trainsBanner.adUrl }" style="width: 300px"/>
									</td>
								</tr>
								<tr>
									<td><label class="pull-right"><font color="red">*</font>排序：</label></td>
									<td>
										<input class="form-control required" id="sort" name="sort" type="text" value="${trainsBanner.sort }" onkeyup="this.value=this.value.replace(/\D/g,'')"/>
									</td>
								</tr>
								<tr>
									<td><label class="pull-right"><font color="red">*</font>备注：</label></td>
									<td>
										<textarea id="remark" name="remark" cols="35" rows="4" class="form-control required">${trainsBanner.remark }</textarea>
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
			$("#file_img_upload").uploadify({
				'buttonText' : '请选择banner图片',
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
						$("#adPic").val(jsonData.file_url);
						$("#img").attr('src',jsonData.file_url); 
					}
				}
			});
		});
	</script>
</body>
</html>