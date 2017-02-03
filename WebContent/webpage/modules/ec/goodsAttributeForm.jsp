<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/webpage/include/taglib.jsp"%>
<html>
<head>
	<title>商品属性管理</title>
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
	<form:form id="inputForm" modelAttribute="goodsAttribute" action="${ctx}/ec/goodsattribute/save" method="post" class="form-horizontal">
		<form:hidden path="id"/>
		<sys:message content="${message}" />
		<table class="table table-bordered  table-condensed dataTables-example dataTable no-footer">
			<tbody>
				<tr>
					<td class="width-15 active">
						<label class="pull-right"><font color="red">*</font>属性名称:</label>
					</td>
					<td class="width-35">
						<form:input path="attrName" htmlEscape="false" maxlength="50" class="form-control required"/>
					</td>
				</tr>
				<tr>
					<td class="width-15 active">
						<label class="pull-right"><font color="red">*</font>所属商品类型:</label>
					</td>
					<td class="width-35">
						<select class="form-control" style="width: 35%" id="typeId" name="typeId">
							<c:forEach items="${goodsTypeList}" var="goodsType">
								<option ${(goodsType.id == goodsAttribute.goodsType.id)?'selected="selected"':''} value="${goodsType.id}">${goodsType.name}</option>
							</c:forEach>
						</select>
					</td>
				</tr>
				<tr>
					<td class="width-15 active">
						<label class="pull-right"><font color="red">*</font>能否进行检索:</label>
					</td>
					<td class="width-35">
						<input type="radio" name="attrIndex" value="0" ${(goodsAttribute.attrIndex =='0')?'checked':''} />不需要检索
						<input type="radio" name="attrIndex" value="1" ${(goodsAttribute.attrIndex =='1')?'checked':''}/>关键字检索
					</td>
				</tr>
				<tr>
					<td class="width-15 active">
						<label class="pull-right"><font color="red">*</font>该属性值的录入方式</label>
					</td>
					<td class="width-35">
						<input type="radio" id="attrInputType1" name="attrInputType" value="0" ${(goodsAttribute.attrInputType =='0')?'checked':''}/>手工录入
						<input type="radio" id="attrInputType2" name="attrInputType" value="1" ${(goodsAttribute.attrInputType =='1')?'checked':''}/>从下面的列表中选择（一行代表一个可选值）
						<input type="radio" id="attrInputType3" name="attrInputType" value="2" ${(goodsAttribute.attrInputType =='2')?'checked':''}/>多行文本框
					</td>
				</tr>
				<tr>
					<td class="width-15 active">
						<label class="pull-right">可选值列表:</label>
					</td>
					<td class="width-35" >
						<textarea id="attrValues" name="attrValues" style="resize: none;width: 200px;height: 100px;max-width: 200px;max-height: 100px;">${goodsAttribute.attrValues }</textarea>
						录入方式为手工或者多行文本时，此输入框不需填写。
					</td>
				</tr>
				<tr>
					<td class="width-15 active">
						<label class="pull-right">排序:</label>
					</td>
					<td class="width-35">
						<form:input path="sort" htmlEscape="false" maxlength="50" class="form-control required" />
					</td>
				</tr>
			</tbody>
		</table>
	</form:form>
</body>
</html>