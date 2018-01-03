<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/webpage/include/taglib.jsp"%>
<%@ page import="com.training.modules.sys.utils.ParametersFactory"%>
<%
	String uploadURL = ParametersFactory.getMtmyParamValues("uploader_url");
%>
<html>
<head>
	<title>菜单管理</title>
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
		$(document).ready(function(){
			validateForm = $("#inputForm").validate({
				rules: {
					enname:{
						remote:{
							type: "post",
							async: false,
							url: "${ctx}/train/fzxMenu/checkEnname?oldEnname=" + encodeURIComponent('${fzxMenu.enname}')
						}
					}
				},
				messages:{
					enname:{remote:"英文名称已存在"}
				},
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
			
			$("#file_icon_upload").uploadify({
				'buttonText' : '请选择封面',
				'method' : 'post',
				'swf' : '${ctxStatic}/train/uploadify/uploadify.swf',
				'uploader' : '<%=uploadURL%>',
				'fileObjName' : 'file_icon_upload',//<input type="file"/>的name
				'queueID' : 'file_icon_queue',//与下面HTML的div.id对应
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
						$("#icon").val(jsonData.file_url);
						$("#iconsrc").attr('src',jsonData.file_url); 
					}
				}
			});
		});
	</script>
</head>
<body>
	<form:form id="inputForm" modelAttribute="fzxMenu" action="${ctx}/train/fzxMenu/save" method="post" class="form-horizontal">
		<c:if test="${not empty fzxMenu.menuId }">
		<form:hidden path="menuId"/>
		</c:if>
		<table class="table table-bordered  table-condensed dataTables-example dataTable no-footer">
		   <tbody>
		   		
		   	  <tr>
		         <td  class="width-15 active"><label class="pull-right">上级菜单:</label></td>
		         <td  class="width-35" >
		         	<%-- <c:if test="${empty fzxMenu.parent.menuId }"> --%>
		         	<%-- <form:hidden path="parent.menuId"/> --%>
		         	<%-- </c:if> --%>
		         	<%-- <form:input path="parent.name" htmlEscape="false" maxlength="20" class="required form-control "/> --%>
		         	<sys:treeselect id="fzxMenu" name="parent.menuId" value="${fzxMenu.parent.menuId}" labelName="parent.name" labelValue="${fzxMenu.parent.name}"
					title="菜单" url="/train/fzxMenu/treeData" extId="${fzxMenu.menuId}" cssClass="form-control required"/>
		         </td>
		      </tr>
		      
		      <tr>
		         <td  class="width-15 active"><label class="pull-right"><font color="red">*</font> 名称:</label></td>
		         <td  class="width-35" ><form:input path="name" htmlEscape="false" maxlength="20" class="required form-control "/></td>
		      </tr>
		      <tr>
		         <td  class="width-15 active"><label class="pull-right"><font color="red">*</font> 英文名称:</label></td>
		         <td  class="width-35" ><form:input path="enname" htmlEscape="false" maxlength="20" class="required form-control "/></td>
		      </tr>
		      <tr>
		         <td  class="width-15 active"><label class="pull-right">链接:</label></td>
		         <td class="width-35" ><form:input path="href" htmlEscape="false" maxlength="2000" class="form-control "/>
		      </tr>
		      <tr>
		         <td  class="width-15 active"><label class="pull-right">图标:</label></td>
		         <td class="width-35" >
					<img id="iconsrc" src="${fzxMenu.icon }" alt="images" style="width: 200px;height: 100px;"/>
					<input type="hidden" id="icon" name="icon" value="${fzxMenu.icon }">
					<div class="upload">
						<input type="file" name="file_icon_upload" id="file_icon_upload">
					</div>
					<div id="file_icon_queue"></div>
		         </td>
		      </tr>
		       <tr>
		         <td  class="width-15 active"><label class="pull-right"><font color="red">*</font> 排序:</label></td>
		         <td  class="width-35" ><form:input path="sort" htmlEscape="false" maxlength="5" class="required form-control digits"/></td>
		      </tr>
		      <tr>
		         <td  class="width-15 active"><label class="pull-right"><font color="red">*</font> 是否显示:</label></td>
		         <td class="width-35" >
		         	<form:select path="isShow" cssClass="form-control required">
		         		<form:option value="0">是</form:option>
		         		<form:option value="1">否</form:option>
		         	</form:select>
		         </td>
		      </tr>
		      <tr>
		         <td  class="width-15 active"><label class="pull-right">备注:</label></td>
		         <td class="width-35" ><form:textarea path="remarks" htmlEscape="false" rows="3" maxlength="200" class="form-control "/></td>
		      </tr>
		    </tbody>
		  </table>
	</form:form>
</body>
</html>