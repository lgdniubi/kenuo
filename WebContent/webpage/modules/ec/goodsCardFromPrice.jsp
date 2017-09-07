<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/webpage/include/taglib.jsp"%>

<html>
<head>
	<title>添加套卡商品价格</title>
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
<body>
	<form:form id="inputForm" modelAttribute="goodsCard" method="post" class="form-horizontal">
		<table class="table table-bordered  table-condensed dataTables-example dataTable no-footer">
			<tbody>
				<tr>
					<td class="width-15 active">
						<label class="pull-right">商品序号:</label>
					</td>
					<td class="width-35">
						<input id="goodsId" name="goodsId" maxlength="50" disabled="disabled" value="${goodsCard.goodsId }" class="form-control"/>
					</td>
				</tr>
				<c:if test="${goodsCard.isReal == 2}">
					<tr>
						<td class="width-15 active">
							<label class="pull-right"><font color="red">*</font>次(个)数:</label>
						</td>
						<td class="width-35">
							<input id="goodsNum" htmlEscape="false" maxlength="3" class="form-control required" value="${goodsCard.goodsNum }"
								onkeyup="this.value=this.value.replace(/[^\d.]/g,'')" onpaste="this.value=this.value.replace(/[^\d.]/g,'')" onfocus="if(value == '0') value=''" onblur="if(this.value == '')this.value='0';"/>
						</td>
					</tr>
					<tr>
						<td class="width-15 active">
							<label class="pull-right"><font color="red">*</font>市场价:</label>
						</td>
						<td class="width-35">
							<input id="marketPrice" htmlEscape="false" maxlength="50" class="form-control required" value="${goodsCard.marketPrice }"
								onkeyup="this.value=this.value.replace(/[^\d.]/g,'')" onpaste="this.value=this.value.replace(/[^\d.]/g,'')" onfocus="if(value == '0.0')value=''" onblur="if(this.value == '')this.value='0.0';"/>
						</td>
					</tr>
					<tr>
						<td class="width-15 active">
							<label class="pull-right"><font color="red">*</font>优惠价:</label>
						</td>
						<td class="width-35">
							<input id="price" htmlEscape="false" maxlength="50" class="form-control required" value="${goodsCard.price }"
								onkeyup="this.value=this.value.replace(/[^\d.]/g,'')" onpaste="this.value=this.value.replace(/[^\d.]/g,'')" onfocus="if(value == '0.0')value=''" onblur="if(this.value == '')this.value='0.0';"/>
						</td>
					</tr>
				</c:if>
				<c:if test="${goodsCard.isReal == 3}">
					<tr>
						<td class="width-15 active">
							<label class="pull-right"><font color="red">*</font>数量:</label>
						</td>
						<td class="width-35">
							<input id="goodsNum" htmlEscape="false" maxlength="3" class="form-control required" value="${goodsCard.goodsNum }"
								onkeyup="this.value=this.value.replace(/[^\d.]/g,'')" onpaste="this.value=this.value.replace(/[^\d.]/g,'')" onfocus="if(value == '0') value=''" onblur="if(this.value == '')this.value='0';"/>
						</td>
					</tr>
				</c:if>
			</tbody>
		</table>
	</form:form>
</body>
</html>