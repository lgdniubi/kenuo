<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/webpage/include/taglib.jsp"%>
<%@ page import="com.training.modules.sys.utils.ParametersFactory"%>
<%
	String uploadURL = ParametersFactory.getMtmyParamValues("uploader_url");
%>
<html>
<head>
	<title>商家管理</title>
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
			//税务登记上传
			$("#file_taxationUrl_upload").uploadify({
				'buttonText' : '请选择图片',
				'method' : 'post',
				'swf' : '${ctxStatic}/train/uploadify/uploadify.swf',
				'uploader' : '<%=uploadURL%>',
				'fileObjName' : 'file_taxationUrl_upload',//<input type="file"/>的name
				'queueID' : 'file_taxationUrl_queue',//与下面HTML的div.id对应
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
						$("#taxationUrl").val(jsonData.file_url);
						$("#taxationUrlsrc").attr('src',jsonData.file_url); 
					}
				}
			});
			
			//营业执照图片上传
			$("#file_charterUrl_upload").uploadify({
				'buttonText' : '请选择图片',
				'method' : 'post',
				'swf' : '${ctxStatic}/train/uploadify/uploadify.swf',
				'uploader' : '<%=uploadURL%>',
				'fileObjName' : 'file_charterUrl_upload',//<input type="file"/>的name
				'queueID' : 'file_charterUrl_queue',//与下面HTML的div.id对应
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
						$("#charterUrl").val(jsonData.file_url);
						$("#charterUrlsrc").attr('src',jsonData.file_url); 
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
	<form:form id="inputForm" modelAttribute="franchisee" action="${ctx}/sys/franchisee/save" method="post" class="form-horizontal">
		<form:hidden path="id" />
		<sys:message content="${message}" />
		<table class="table table-bordered  table-condensed dataTables-example dataTable no-footer">
			<tbody>
				<tr>
					<td class="width-15 active"><label class="pull-right"><font color="red">*</font>上级机构:</label></td>
					<td class="width-35">
						<form:input path="parent.name" htmlEscape="false" maxlength="50" class="form-control" readonly="true" />
						<input type="hidden" id="parent.id" name="parent.id" value="${franchisee.parent.id}">
						<input type="hidden" id="parentIds" name="parentIds" value="${franchisee.parent.parentIds}">
					</td>
					<td class="width-15 active"><label class="pull-right"><font color="red">*</font>加盟商编码:</label>
					</td>
					<td class="width-35">
						<form:input path="code" htmlEscape="false" maxlength="50" class="form-control required" readonly="true" />
					</td>
				</tr>
				<tr>
					<td class="width-15 active">
						<label class="pull-right"><font color="red">*</font>供应商名称:</label>
					</td>
					<td class="width-35"><form:input path="name" htmlEscape="false" maxlength="50" class="form-control required" />
					</td>
					<td class="width-15 active"><label class="pull-right"><font color="red">*</font>机构类型:</label></td>
					<td class="width-35">
						<form:select path="type" class="form-control required">
							<form:options items="${fns:getDictList('sys_office_type')}" itemLabel="label" itemValue="value" htmlEscape="false" />
						</form:select>
					</td>
				</tr>
				<tr>
					<td class="width-15 active">
						<label class="pull-right"><font color="red">*</font>归属区域:</label>
					</td>
					<td class="width-35">
						<sys:treeselect id="area" name="area.id"
							value="${franchisee.area.id}" labelName="area.name"
							labelValue="${franchisee.area.name}" title="区域"
							url="/sys/area/treeData" cssClass="form-control required" />
					</td>
					<td class="width-15 active"><label class="pull-right">邮政编码:</label></td>
					<td class="width-35">
						<form:input path="zipcode" htmlEscape="false" maxlength="50" class="form-control digits"/>
					</td>
				</tr>
				<tr>
					<td class="width-15 active" class="active"><label class="pull-right"><font color="red">*</font>详细地址:</label></td>
					<td class="width-35" colspan="3"><form:textarea path="address" htmlEscape="false" rows="3" maxlength="200" class="form-control required" /></td>
				</tr>
				<tr>
					<td class="width-15 active"><label class="pull-right"><font color="red">*</font>加盟法人:</label></td>
					<td class="width-35"><form:input path="legalName" htmlEscape="false" maxlength="50" class="form-control required" /></td>
					<td class="width-15 active"><label class="pull-right"><font color="red">*</font>加盟商联系人:</label></td>
					<td class="width-35"><form:input path="contacts" htmlEscape="false" maxlength="50" cssClass="form-control required" /></td>
				</tr>
				<tr>
					<td class="width-15 active"><label class="pull-right"><font color="red">*</font>手机号码:</label></td>
					<td class="width-35"><form:input path="mobile" htmlEscape="false" maxlength="50" cssClass="form-control required digits" /></td>
					<td class="width-15 active"><label class="pull-right">固定电话:</label></td>
					<td class="width-35"><form:input path="tel" htmlEscape="false" maxlength="50" cssClass="form-control" /></td>
				</tr>
				<tr>
					<td class="width-15 active"><label class="pull-right">营业执照<br/>（照片）:</label></td>
					<td class="width-35">
						<img id="charterUrlsrc" src="${franchisee.charterUrl}" alt="images" style="width: 200px;height: 100px;"/>
						<input type="hidden" id="charterUrl" name="charterUrl" value="${franchisee.charterUrl}">
						<c:if test="${opflag == 'UPDATE' || opflag == 'ADD'}">
							<div class="upload">
								<input type="file" name="file_charterUrl_upload" id="file_charterUrl_upload">
							</div>
							<div id="file_charterUrl_queue"></div>
						</c:if>
					</td>
					<td class="width-15 active"><label class="pull-right">税务登记<br/>（照片）:</label></td>
					<td class="width-35">
						<img id="taxationUrlsrc" src="${franchisee.taxationUrl}" alt="images" style="width: 200px;height: 100px;"/>
						<input type="hidden" id="taxationUrl" name="taxationUrl" value="${franchisee.taxationUrl}">
						<c:if test="${opflag == 'UPDATE' || opflag == 'ADD'}">
							<div class="upload">
								<input type="file" name="file_taxationUrl_upload" id="file_taxationUrl_upload">
							</div>
							<div id="file_taxationUrl_queue"></div>
						</c:if>
					</td>
				</tr>
				<tr>
					<td class="width-15 active"><label class="pull-right"><font color="red">*</font>开户行:</label></td>
					<td class="width-35"><form:input path="bankBeneficiary" htmlEscape="false" maxlength="50" cssClass="form-control required" /></td>
					<td class="width-15 active"><label class="pull-right"><font color="red">*</font>持卡人姓名:</label></td>
					<td class="width-35"><form:input path="bankName" htmlEscape="false" maxlength="50" cssClass="form-control required" /></td>
				</tr>
				<tr>
					<td class="width-15 active"><label class="pull-right"><font color="red">*</font>银行卡号:</label></td>
					<td class="width-35"><form:input path="bankCode" htmlEscape="false" maxlength="50" cssClass="form-control digits required" /></td>
					<td class="width-15 active"><label class="pull-right">备注:</label></td>
					<td class="width-35"><form:textarea path="remarks" htmlEscape="false" rows="3" maxlength="200" class="form-control" /></td>
				</tr>
			</tbody>
		</table>
	</form:form>
</body>
</html>