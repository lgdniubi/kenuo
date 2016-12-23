<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/webpage/include/taglib.jsp"%>
<%@ page import="com.training.modules.sys.utils.ParametersFactory"%>
<%
	String uploadURL = ParametersFactory.getMtmyParamValues("uploader_url");
%>
<html>
<head>
	<title>商品库存管理</title>
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
	<form:form id="inputForm" modelAttribute="goods" action="${ctx}/ec/goods/savespecstocks" method="post" class="form-horizontal">
		<form:hidden path="goodsId"/>
		<form:hidden path="actionId"/>
		<sys:message content="${message}" />
		<font style="color: red;">
			注意：<br>1：在原库存的基础上进行累计
				 <br>2：正整数增加库存
				 <br>3：负整数减少库存
		</font>
		<table class="table table-bordered  table-condensed dataTables-example dataTable no-footer">
			<tbody>
				<c:forEach items="${gspList}" var="goodsSpecPrice">
					<tr>
						<td class="width-15 active">
							<label class="pull-right">${goodsSpecPrice.specKeyValue}:</label>
						</td>
						<td class="width-35">
							<input type="number" id="item[${goodsSpecPrice.specKey}][store_count]" name="item[${goodsSpecPrice.specKey}][store_count]" 
								onkeyup="this.value=this.value.replace(/^(\-?)\d+(\d+)?$,'')" value="0" class="required"/>原库存：<font style="color: red;">${goodsSpecPrice.storeCount}</font>
						</td>
					</tr>
				</c:forEach>
			</tbody>
		</table>
	</form:form>
	<div class="loading"></div>
</body>
</html>