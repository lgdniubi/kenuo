<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/webpage/include/taglib.jsp"%>
<%@ page import="com.training.modules.sys.utils.ParametersFactory"%>
<%
	String uploadURL = ParametersFactory.getMtmyParamValues("uploader_url");
%>
<html>
<head>
	<title>商品分类管理</title>
	<meta name="decorator" content="default"/>
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
			//分类图片上传
			$("#file_image_upload").uploadify({
				'buttonText' : '请选择图片',
				'method' : 'post',
				'swf' : '${ctxStatic}/train/uploadify/uploadify.swf',
				'uploader' : '<%=uploadURL%>',
				'fileObjName' : 'file_image_upload',//<input type="file"/>的name
				'queueID' : 'file_image_queue',//与下面HTML的div.id对应
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
						$("#image").val(jsonData.file_url);
						$("#imagesrc").attr('src',jsonData.file_url); 
					}
				}
			});
			
			//表单验证
			$("#name").focus();
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
	<form:form id="inputForm" modelAttribute="goodsCategory" action="${ctx}/ec/goodscategory/save" method="post" class="form-horizontal">
		<form:hidden path="id" />
		<sys:message content="${message}" />
		<table class="table table-bordered  table-condensed dataTables-example dataTable no-footer">
			<tbody>
				<tr>
					<td class="width-15 active"><label class="pull-right"><font color="red">*</font>上级分类:</label></td>
					<td class="width-35">
						<form:input path="parent.name" htmlEscape="false" maxlength="50" class="form-control" readonly="true" />
						<input type="hidden" id="parent.id" name="parent.id" value="${goodsCategory.parent.id}">
						<input type="hidden" id="parentIds" name="parentIds" value="${goodsCategory.parent.parentIds}">
					</td>
					<td class="width-15 active"><label class="pull-right"><font color="red">*</font>分类编码:</label>
					</td>
					<td class="width-35">
						<form:input path="code" htmlEscape="false" maxlength="50" class="form-control required" readonly="true" />
					</td>
				</tr>
				<tr>
					<td class="width-15 active">
						<label class="pull-right"><font color="red">*</font>分类名称:</label>
					</td>
					<td class="width-35">
						<form:input path="name" htmlEscape="false" maxlength="50" class="form-control required" />
					</td>
					<td class="width-15 active"><label class="pull-right"><font color="red">*</font>短分类名称:</label></td>
					<td class="width-35">
						<form:input path="mobileName" htmlEscape="false" maxlength="50" class="form-control required" />
					</td>
				</tr>
				<tr>
					<td class="width-15 active"><label class="pull-right">等级:</label></td>
					<td class="width-35">
						<form:select path="level" class="form-control">
							<form:options items="${fns:getDictList('goods_category_level')}" itemLabel="label" itemValue="value" htmlEscape="false" cssClass="form-control"/>
						</form:select>
					</td>
					<td class="width-15 active"><label class="pull-right">是否热门:</label></td>
					<td class="width-35">
						<form:select path="isHot" class="form-control">
							<form:options items="${fns:getDictList('goods_yes_no')}" itemLabel="label" itemValue="value" htmlEscape="false" cssClass="form-control"/>
						</form:select>
					</td>
				</tr>
				<tr>
					<td class="width-15 active"><label class="pull-right"><font color="red">*</font>分类类型:</label></td>
					<td class="width-35">
						<form:select path="type" class="form-control">
							<form:options items="${fns:getDictList('goods_category_type')}" itemLabel="label" itemValue="value" htmlEscape="false" cssClass="form-control"/>
						</form:select>
					</td>
					<td class="width-15 active">
						<label class="pull-right"><font color="red">*</font>排序:</label>
					</td>
					<td class="width-35" colspan="3">
						<form:input path="sort" htmlEscape="false" maxlength="50" class="form-control required" />
					</td>
				</tr>
				<c:if test="${empty goodsCategory.parent.name}">
					<tr>
						<td class="width-15 active"><label class="pull-right"><font color="red">*</font>位置分类:</label></td>
						<td class="width-35">
							<form:select path="positionType" class="form-control">
								<form:options items="${fns:getDictList('position_type')}" itemLabel="label" itemValue="value" htmlEscape="false" cssClass="form-control"/>
							</form:select>
						</td>
					</tr>
				</c:if>
				<tr>
					<td class="width-15 active"><label class="pull-right">分类展示<br/>（照片）:</label></td>
					<td class="width-35" colspan="3">
						<img id="imagesrc" src="${goodsCategory.image}" alt="images" style="width: 200px;height: 100px;"/>
						<input type="hidden" id="image" name="image" value="${goodsCategory.image}">
						<c:if test="${opflag != 'VIEW'}">
							<div class="upload">
								<input type="file" name="file_image_upload" id="file_image_upload">
							</div>
							<div id="file_image_queue"></div>
						</c:if>
					</td>
				</tr>
			</tbody>
		</table>
	</form:form>
</body>
</html>