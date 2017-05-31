<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/webpage/include/taglib.jsp"%>
<%@ page import="com.training.modules.sys.utils.ParametersFactory"%>
<html>
<head>
	<title>添加广告图分类</title>
	<meta name="decorator" content="default"/>
	<link rel="stylesheet" href="${ctxStatic}/train/css/addcourse.css">
	<script>
		var validateForm;
		function doSubmit(){//回调函数，在编辑和保存动作时，供openDialog调用提交表单。
		  if(validateForm.form()){
			  if(!/^[1-9]*[1-9][0-9]*$/.test($(sort).val())){
					top.layer.alert('排序必须为正整数!', {icon: 0, title:'提醒'});
					return;
				}
			  $("#inputForm").submit();
			  return true;
		  }
		  return false;
		}
		
		$(document).ready(function() {
			var parentId = $("#parentvalue").val();
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
	<form:form class="form-horizontal" id="inputForm" modelAttribute="mtmyWebAdCategory" action="${ctx}/ec/adCategory/saveCategory" method="post">
		<input type="hidden" id="mtmyWebAdCategoryId" name="mtmyWebAdCategoryId" value="${mtmyWebAdCategory.mtmyWebAdCategoryId}">
		<input type="hidden" id="parentId" name="parentId" value="${mtmyWebAdCategory.parentId}">
		<sys:message content="${message}"/>
		<table id="contentTable" class="table table-striped table-bordered  table-hover table-condensed  dataTables-example dataTable no-footer">
			<tr>
				<td><label class="pull-right"><font color="red">*</font>名称：</label></td>
				<td>
					<input class="form-control required" id="name" name="name" type="text" value="${mtmyWebAdCategory.name }" style="width: 300px"/>
				</td>
			</tr>
			<c:if test="${mtmyWebAdCategory.parentId != '0' && mtmyWebAdCategory.parentId != null}">
				<tr>
					<td><label class="pull-right"><font color="red">*</font>位置类型:</label></td>
					<td>
						<select class="form-control required" id="positionType" name="positionType" style="width:300px">
							<option value='1' <c:if test="${mtmyWebAdCategory.positionType == '1'}">selected</c:if>>首页</option>
							<option value='2' <c:if test="${mtmyWebAdCategory.positionType == '2'}">selected</c:if>>商城</option>
							<option value='3' <c:if test="${mtmyWebAdCategory.positionType == '3'}">selected</c:if>>生活美容</option>
						</select>
					</td>
				</tr>
			</c:if>
			<tr>
				<td><label class="pull-right"><font color="red">*</font>排序：</label></td>
				<td>
					<input class="form-control required" id="sort" name="sort" type="text" value="${mtmyWebAdCategory.sort }" style="width: 300px"/>
				</td>
			</tr>
			<tr>
				<td><label class="pull-right">备注:</label></td>
		        <td colspan="3"><form:textarea path="remarks" htmlEscape="false" rows="3" maxlength="200" class="form-control" style="width:300px;"/></td>
			</tr>
		</table>
	</form:form>
</body>
</html>