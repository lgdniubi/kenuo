<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/webpage/include/taglib.jsp"%>
<%@ page import="com.training.modules.sys.utils.ParametersFactory"%>
<%
	String uploadURL = ParametersFactory.getMtmyParamValues("uploader_url");
%>
<html>
<head>
 	<meta name="decorator" content="default"/>
    <link rel="stylesheet" href="${ctxStatic}/train/css/exam.css">
    <script src="${ctxStatic}/train/js/jquery-1.11.3.js"></script>
    <script type="text/javascript">
    	$(document).ready(function(){
    		var pictures = "${feedback.messageImg}";
    		if(pictures != null && pictures != ""){
				var arr = pictures.split(",");
    			for(var i=0;i<arr.length;i++){
    	        	   if(arr[i] != ''){
    	        		   $("#photo").append("<a class='img' href="+arr[i]+"><img width='150px' height='150px' style='padding:3px' src='"+arr[i]+"'></a>");
    	        	   }
    			}
    		}
    	});
		<%-- $(document).ready(function(){
			$("#file_img_upload").uploadify({
				'buttonText' : '请选择图片',
				'width' : 110,
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
						$("#pictures").val(jsonData.file_url);
						$("#img").attr('src',jsonData.file_url); 
						$("#img").attr('style',"width: 200px;height: 100px;border:1px solid black;"); 
					}
				}
			});  
		});--%>
    </script>
    <title>反馈详情</title>
</head>
<body>
   <div class="wrapper wrapper-content">
		<div class="ibox">
	    	<div class="ibox-content">
				<div class="tab-content" id="tab-content">
	                <div class="tab-inner">
						<table id="contentTable" class="table table-striped table-bordered  table-hover table-condensed  dataTables-example dataTable no-footer">
							<tr style="text-align: center;">
								<td><label>用户昵称</label></td>
								<td>
									${feedback.users.nickname}
								</td>
							</tr>
							<tr style="text-align: center;">
								<td><label>用户姓名</label></td>
								<td>
									${feedback.users.name}
								</td>
							</tr>
							<tr style="text-align: center;">
								<td><label>联系电话</label></td>
								<td>
									${feedback.mobile}
								</td>
							</tr>
							<tr style="text-align: center;">
								<td><label>反馈类型</label></td>
								<td>
									<c:if test="${feedback.msgType == 0}">购物商品</c:if>
			               			<c:if test="${feedback.msgType == 1}">功能异常</c:if>
			               			<c:if test="${feedback.msgType == 2}">新功能建议</c:if>
			               			<c:if test="${feedback.msgType == 3}">门店体验</c:if>
			               			<c:if test="${feedback.msgType == 4}">其他</c:if>
								</td>
							</tr>
							<tr style="text-align: center;">
								<td><label>反馈描述</label></td>
								<td>
									${feedback.msgContent}
								</td>
							</tr>
							<tr style="text-align: center;">
								<td><label>反馈图片</label></td>
								<td style="text-align: center;">
									<div id="photo"></div>
								</td>
							</tr>
						</table>
					</div>
				</div>
			</div>
		</div>
	</div>
</body>
</html>