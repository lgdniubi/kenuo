<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/webpage/include/taglib.jsp"%>

<html>
<head>
	<title>商品规格价格修改</title>
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
			$("#price").focus();
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
<body> <%-- action="${ctx}/ec/goods/goodsBySpecSave" --%>
	<form:form id="inputForm" modelAttribute="goodsSpecPrice" method="post" class="form-horizontal">
		<table class="table table-bordered  table-condensed dataTables-example dataTable no-footer">
			<tbody>
				<tr>
					<td class="width-15 active">
						<label class="pull-right">规格名称:</label>
					</td>
					<td class="width-35">
						<input id="specKeyValue" name="specKeyValue" maxlength="50" disabled="disabled" value="${goodsSpecPrice.specKeyValue }" class="form-control"/>
					</td>
				</tr>
				<tr>
					<td class="width-15 active">
						<label class="pull-right"><font color="red">*</font>优惠价格:</label>
					</td>
					<td class="width-35">
						<input id="price" htmlEscape="false" maxlength="50" class="form-control required" value="${goodsSpecPrice.price }"
							onkeyup="this.value=this.value.replace(/[^\d.]/g,'')" onpaste="this.value=this.value.replace(/[^\d.]/g,'')" onfocus="if(value == '0')value=''" onblur="if(this.value == '')this.value='0';"/>
					</td>
				</tr>
				<tr>
					<td class="width-15 active">
						<label class="pull-right"><font color="red">*</font>市场价格:</label>
					</td>
					<td class="width-35">
						<input id="marketPrice" htmlEscape="false" maxlength="50" class="form-control required" value="${goodsSpecPrice.marketPrice }"
							onkeyup="this.value=this.value.replace(/[^\d.]/g,'')" onpaste="this.value=this.value.replace(/[^\d.]/g,'')" onfocus="if(value == '0')value=''" onblur="if(this.value == '')this.value='0';"/>
					</td>
				</tr>
				<tr>
					<td class="width-15 active">
						<label class="pull-right">库存:</label>
					</td>
					<td class="width-35">
						<input id="storeCount" htmlEscape="false" disabled="disabled" value="${goodsSpecPrice.storeCount }" maxlength="50" class="form-control"/>
					</td>
				</tr>
				<tr>
					<td class="width-15 active">
						<label class="pull-right">规格条形码:</label>
					</td>
					<td class="width-35">
						<form:input path="barCode" htmlEscape="false" maxlength="50" class="form-control"/>
					</td>
				</tr>
				<tr>
					<td class="width-15 active">
						<label class="pull-right">规格编码:</label>
					</td>
					<td class="width-35">
						<form:input path="goodsNo" htmlEscape="false" maxlength="50" class="form-control"/>
					</td>
				</tr>
				<tr>
					<td class="width-15 active">
						<label class="pull-right">商品重量（克）</label>
					</td>
					<td class="width-35">
						<form:input path="goodsWeight" htmlEscape="false" maxlength="50" class="form-control"/>
					</td>
				</tr>
				<tr>
					<td class="width-15 active">
						<label class="pull-right">服务次数:</label>
					</td>
					<td class="width-35">
						<input id="serviceTimes" htmlEscape="false" maxlength="3" class="form-control" value="${goodsSpecPrice.serviceTimes }"
							onkeyup="this.value=this.value.replace(/[^\d.]/g,'')" onpaste="this.value=this.value.replace(/[^\d.]/g,'')" onfocus="if(value == '0') value=''" onblur="if(this.value == '')this.value='0';"/>
					</td>
				</tr>
				<tr>
					<td class="width-15 active">
						<label class="pull-right">截止日期（月）:</label>
					</td>
					<td class="width-35">
						<input id="expiringDate" htmlEscape="false" maxlength="2" class="form-control" value="${goodsSpecPrice.expiringDate }"
							onkeyup="this.value=this.value.replace(/[^\d.]/g,'')" onpaste="this.value=this.value.replace(/[^\d.]/g,'')" onfocus="if(value == '0')value=''" onblur="if(this.value == '')this.value='0';"/>
					</td>
				</tr>
			</tbody>
		</table>
	</form:form>
</body>
</html>