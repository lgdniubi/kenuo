<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/webpage/include/taglib.jsp"%>
<%@ page import="com.training.modules.sys.utils.ParametersFactory"%>
<%
	String uploadURL = ParametersFactory.getMtmyParamValues("uploader_url");
%>
<html>
<head>
	<title>商品品牌管理</title>
	<meta name="decorator" content="default"/>
	<link rel="stylesheet" href="${ctxStatic}/ec/css/loading.css">
	<!-- 内容上传 引用-->
	<link rel="stylesheet" type="text/css" href="${ctxStatic}/train/uploadify/uploadify.css">
	<script type="text/javascript" src="${ctxStatic}/train/uploadify/lang-cn.js"></script>
	<script type="text/javascript" src="${ctxStatic}/train/uploadify/jquery.uploadify.min.js"></script>
	
	<script type="text/javascript">
		var validateForm;
		function doSubmit(){//回调函数，在编辑和保存动作时，供openDialog调用提交表单。
		  if(validateForm.form()){
			  $("#inputForm").submit();
			  return true;
		  }
		  return false;
		}
		$(document).ready(function() {
			
			//税务登记上传
			$("#file_logo_upload").uploadify({
				'buttonText' : '请选择图片',
				'method' : 'post',
				'swf' : '${ctxStatic}/train/uploadify/uploadify.swf',
				'uploader' : '<%=uploadURL%>',
				'fileObjName' : 'file_logo_upload',//<input type="file"/>的name
				'queueID' : 'file_logo_queue',//与下面HTML的div.id对应
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
						$("#logo").val(jsonData.file_url);
						$("#logosrc").attr('src',jsonData.file_url); 
					}
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
	<form:form id="inputForm" modelAttribute="goodsBrand" action="${ctx}/ec/goodsbrand/save" method="post" class="form-horizontal">
		<form:hidden path="id"/>
		<sys:message content="${message}" />
		<table class="table table-bordered  table-condensed dataTables-example dataTable no-footer">
			<tbody>
				<tr>
					<td class="width-15 active"><label class="pull-right"><font color="red">*</font>品牌名称:</label>
					</td>
					<td class="width-35">
						<form:input path="name" htmlEscape="false" maxlength="50" class="form-control required"/>
					</td>
				</tr>
				<tr>
					<td class="width-15 active">
						<label class="pull-right"><font color="red">*</font>品牌网址:</label>
					</td>
					<td class="width-35">
						<form:input path="url" htmlEscape="false" maxlength="50" class="form-control required" />
					</td>
				</tr>
				<tr>
					<td class="width-15 active">
						<label class="pull-right"><font color="red">*</font>所属分类:</label>
					</td>
					<td class="width-35">
						<sys:treeselect id="goodsCategoryId" name="goodsCategoryId" value="${goodsBrand.goodsCategoryId}" 
							labelName="goodsCategory.name" labelValue="${goodsBrand.goodsCategory.name }" 
				     		title="商品分类" url="/ec/goodscategory/treeData" cssClass="form-control required" allowClear="true" notAllowSelectParent="true"/>
					</td>
				</tr>
				<tr>
					<td class="width-15 active">
						<label class="pull-right">品牌产地:</label>
					</td>
					<td class="width-35">
						<form:input path="area" htmlEscape="false" maxlength="50" class="form-control required" />
					</td>
				</tr>
				<tr>
					<td class="width-15 active"><label class="pull-right">品牌logo<br/>（照片）:</label></td>
					<td class="width-35" colspan="3">
						<img id="logosrc" src="${goodsBrand.logo}" alt="images" style="width: 200px;height: 100px;"/>
						<input type="hidden" id="logo" name="logo" value="${goodsBrand.logo}">
						<c:if test="${opflag != 'VIEW'}">
							<div class="upload">
								<input type="file" name="file_logo_upload" id="file_logo_upload">
							</div>
							<div id="file_logo_queue"></div>
						</c:if>
					</td>
				</tr>
				<tr>
					<td class="width-15 active">
						<label class="pull-right"><font color="red">*</font>排序:</label>
					</td>
					<td class="width-35">
						<form:input path="sort" htmlEscape="false" maxlength="50" class="form-control required" />
					</td>
				</tr>
				<tr>
					<td class="width-15 active">
						<label class="pull-right">品牌描述:</label>
					</td>
					<td class="width-35">
						<form:textarea path="brandDesc" htmlEscape="false" maxlength="50" class="form-control" />
					</td>
				</tr>
			</tbody>
		</table>
	</form:form>
	<div class="loading"></div>
</body>
</html>