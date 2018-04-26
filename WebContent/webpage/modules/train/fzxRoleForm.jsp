<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/webpage/include/taglib.jsp"%>
<%@ page import="com.training.modules.sys.utils.ParametersFactory"%>
<%
	String uploadURL = ParametersFactory.getMtmyParamValues("uploader_url");
%>
<html>
<head>
	<title>角色管理</title>
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
				rules: {  // 英文名称校验修改为下拉选，不用再进行验证,如需进行校验，将此注释打开即可
					name: {
						required:true,
						nameMethod :true
					} ,
					enname:{
						enameMethod:true
					}
				},
				messages:{
					name:{nameMethod:"此版本的角色名称已存在",required:"角色名称不能为空"}
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
		});
		//自定义校验英文名称
		jQuery.validator.addMethod("enameMethod", function(value, element) {
	        var url = "${ctx}/train/fzxRole/checkEnname?oldEnname=${fzxRole.enname}";
	        var enameFlag = true;
	        $.ajax({
	            type: "post",
	            url: url,
	            async : false,
	            data: {modeid:$("#modeid").val(),enname:$("#enname").val()},
	            dataType: "json",
	            success: function(data){
	            	enameFlag = data;
	            }
	        });
	        return enameFlag;
	    }, "此版本的英文名称已存在");
		//自定义校验英文名称
		jQuery.validator.addMethod("nameMethod", function(value, element) {
	        var url = "${ctx}/train/fzxRole/checkName";
	        var nameFlag = true;
	        $.ajax({
	            type: "post",
	            url: url,
	            async : false,
	            data: {
	            	oldEnname: function() { return $("#name").val(); },
			        modeid: function() {  return $("#modeid").val(); },
					oldModeid: function() {  return $("#oldModeid").val(); },
			        oldName: function() {  return $("#oldName").val(); }
					},
	            dataType: "json",
	            success: function(data){
	            	nameFlag = data;
	            }
	        });
	        return nameFlag;
	    }, "此版本的角色名称已存在");
	</script>
</head>
<body>
	<input type="hidden" value="${fzxRole.modeid}" id="oldModeid"/>
	<input type="hidden" value="${fzxRole.name}" id="oldName"/>
	<form:form id="inputForm" modelAttribute="fzxRole" action="${ctx}/train/fzxRole/save" method="post" class="form-horizontal">
		<form:hidden path="roleId"/>
		<table class="table table-bordered  table-condensed dataTables-example dataTable no-footer">
		   <tbody>
		      <tr>
		         <td  class="width-15 active"><label class="pull-right"><font color="red">*</font> 名称:</label></td>
		         <td  class="width-35" ><form:input path="name" htmlEscape="false" maxlength="20" class="form-control "/></td>
		      </tr>
		      <tr>
		         <td  class="width-15 active"><label class="pull-right"><font color="red">*</font> 英文名称:</label></td>
		         <td  class="width-35" >
		         	<!-- 将英文名称改为下拉选 -->
		         	<form:select path="enname"  class="form-control">
						<form:options items="${fns:getDictList('fzx_role_type')}" itemLabel="label" itemValue="value" htmlEscape="false"/>
					</form:select>
		         	<%-- <form:input path="enname" htmlEscape="false" maxlength="20" class="required form-control "/>
		         	<input type="hidden" id="oldEnname" name="oldEnname" value="${fzxRole.enname }"> --%>
		         </td>
		      </tr>
		       <tr>
		         <td  class="width-15 active"><label class="pull-right"><font color="red">*</font> 版本类型:</label></td>
		         <td  class="width-35" >
		         	<form:select path="modeid"  class="form-control">
						<form:options items="${modList}" itemLabel="modName" itemValue="id" htmlEscape="false"/>
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