<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/webpage/include/taglib.jsp"%>
<%@ page import="com.training.modules.sys.utils.ParametersFactory"%>
<%
	String uploadURL = ParametersFactory.getMtmyParamValues("uploader_url");
%>
<html>
<head>
	<title>添加分类</title>
	<meta name="decorator" content="default"/>
	 <!-- 图片上传 引用-->
	<link rel="stylesheet" type="text/css" href="${ctxStatic}/ec/css/custom_uploadImg.css">
	<link rel="stylesheet" type="text/css" href="${ctxStatic}/train/uploadify/uploadify.css">
	<script type="text/javascript" src="${ctxStatic}/train/uploadify/lang-cn.js"></script>
	<script type="text/javascript" src="${ctxStatic}/train/uploadify/jquery.uploadify.min.js"></script>
	<link rel="stylesheet" href="${ctxStatic}/train/css/exam.css">
	<script type="text/javascript">
		var validateForm;
		function doSubmit(){//回调函数，在编辑和保存动作时，供openDialog调用提交表单。
		  if(validateForm.form()){
			  $("#inputForm").submit();
			  return true;
		  }
		  return false;
		}
		
		$(document).ready(function(){
			//当改变版本的时候获取英文名称的页面代码
			htmlvalue = $("#enname").html();
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
			
			$("#file_iconUrl_upload").uploadify({
				'buttonText' : ' 请选择图片',
				'method' : 'post',
				'swf' : '${ctxStatic}/train/uploadify/uploadify.swf',
				'uploader' : '<%=uploadURL%>',
				'fileObjName' : 'file_iconUrl_upload',//<input type="file"/>的name
				'queueID' : 'file_iconUrl_queue',//与下面HTML的div.id对应
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
						$("#iconUrl").val(jsonData.file_url);
						$("#iconUrlImgsrc").attr('src',jsonData.file_url); 
					}
				}
			});
			
		});
		
	</script>
</head>
<body>
	<form:form id="inputForm" modelAttribute="handbookType" action="${ctx}/train/handbook/save" method="post" class="form-horizontal">
		<form:hidden path="id"/>
		<form:hidden path="type"/>
		<c:if test="${handbookType.type eq '3'}">
			<form:hidden path="isShop"/>
		</c:if>
<%-- 		<input type="hidden" name="type" value="${handbookType.type }" /> --%>
		<table class="table table-bordered  table-condensed dataTables-example dataTable no-footer">
		   <tbody>
		      <tr>
		         <td  class="width-15 active"><label class="pull-right"><font color="red">*</font>分类名称:</label></td>
		         <td  class="width-35" ><form:input path="name" htmlEscape="false" maxlength="10" class="form-control "/></td>
		      </tr>
		      <tr>
		         <td  class="width-15 active"><label class="pull-right"><font color="red">*</font> 排序:</label></td>
		         <td  class="width-35" >
		         	<form:input path="sort" htmlEscape="false" maxlength="10" class="form-control "/>
				 </td>
		      </tr>
		      <c:if test="${handbookType.type eq '2'}">
		      <tr>
		      	<td  class="width-15 active"><label class="pull-right"><font color="red">*</font>图标:</label></td>
      			<td class="width-35" >
		        	<img id="iconUrlImgsrc" src="${handbookType.iconUrl }" alt="" style="width: 200px;height: 100px;"/>
					<!--<input type="hidden" id="iconUrl" name="iconUrl" value=""> 图片隐藏文本框 -->
					<form:hidden path="iconUrl"/>
					<p>&nbsp;</p>
		            <div class="upload">
						<input type="file" name="file_iconUrl_upload" class="required" id="file_iconUrl_upload">
					</div>
					<div id="file_iconUrl_queue"></div>
	        	</td>
		      </tr>
		      </c:if>
		    </tbody>
		  </table>
	</form:form>
</body>
</html>