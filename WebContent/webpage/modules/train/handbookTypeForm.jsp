<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/webpage/include/taglib.jsp"%>
<%@ page import="com.training.modules.sys.utils.ParametersFactory"%>
<html>
<head>
	<title>添加分类</title>
	<meta name="decorator" content="default"/>
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
			
			
		});
		
	</script>
</head>
<body>
	<form:form id="inputForm" modelAttribute="handbookType" action="${ctx}/train/handbook/save" method="post" class="form-horizontal">
		<form:hidden path="id"/>
		<input type="hidden" name="type" value="0" />
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
		      
		    </tbody>
		  </table>
	</form:form>
</body>
</html>