<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/webpage/include/taglib.jsp"%>

<html>
<head>
	<title>商品规格管理</title>
	<meta name="decorator" content="default"/>
	<script type="text/javascript">
		
		//规格项改变事件
		function changeItemsVal(method,specItemId){
			$(".loading").show();//打开展示层
			
			var specId = $("#id").val();
			var itemsvalue = $("#newitems").val();
			
			$.ajax({
				type : "POST",   
				url : "${ctx}/ec/goodsspec/updatespecitems?METHOD="+method+"&SPECITEMID="+specItemId+"&SPECID="+specId+"&ITEMSVALUE="+itemsvalue,
				dataType : 'json',            	//服务器返回的格式,可以是json或xml或text等  
				success : function(data, status) {
					$(".loading").hide(); //关闭加载层
					if(data.STATUS == 'OK'){
						if("DELETE" == method){
							//删除
							$("#items_"+specItemId).remove();
						}else if("ADD" == method){
							//添加
							$("#itemlistdiv").append("<div id='items_"+data.ITEMID+"' style='padding:5px 0'><input type='text' id='item_"+data.ITEMID+"' disabled='disabled' value='"+itemsvalue+"'> <img width='20' height='20' style='margin-left:5px;' onclick=\"changeItemsVal('DELETE','"+data.ITEMID+"')\" src='/kenuo/static/ec/images/minus.png'></div>");
							$("#newitems").val("");
						}
						alert(data.MESSAGE);
					}else{
						alert(data.MESSAGE);
					}
				},
				error:function(data, status, e){ //服务器响应失败时的处理函数 
					$(".loading").hide(); //关闭加载层
					alert(data.MESSAGE);
		        } 
			});
		}
	
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
	<form:form id="inputForm" modelAttribute="goodsSpec" action="${ctx}/ec/goodsspec/save" method="post" class="form-horizontal">
		<form:hidden path="id"/>
		<sys:message content="${message}" />
		<table class="table table-bordered  table-condensed dataTables-example dataTable no-footer">
			<tbody>
				<tr>
					<td class="width-15 active">
						<label class="pull-right"><font color="red">*</font>规格名称:</label>
					</td>
					<td class="width-35">
						<form:input path="name" htmlEscape="false" maxlength="50" class="form-control required"/>
					</td>
				</tr>
				<tr>
					<td class="width-15 active">
						<label class="pull-right"><font color="red">*</font>所属商品类型:</label>
					</td>
					<td class="width-35">
						<select class="form-control" style="width: 35%" id="typeId" name="typeId">
							<c:forEach items="${goodsTypeList}" var="goodsType">
								<option ${(goodsType.id == goodsSpec.goodsType.id)?'selected="selected"':''} value="${goodsType.id}">${goodsType.name}</option>
							</c:forEach>
						</select>
					</td>
				</tr>
				<tr>
					<td class="width-15 active">
						<label class="pull-right"><font color="red">*</font>可选值列表:</label>
					</td>
					<td class="width-35">
						<c:if test="${opflag == 'ADD' }">
							<textarea id="specItem" name="specItem" style="resize: none;width: 200px;height: 100px;max-width: 200px;max-height: 100px;" class="required">${goodsSpec.specItemValues}</textarea>
							一行为一个规格项
						</c:if>
						<c:if test="${opflag == 'UPDATE' }">
							<div id="itemlistdiv">
								<c:forEach items="${goodsSpec.specItemList }" var="goodsSpecItem">
									<div id="items_${goodsSpecItem.specItemId }" style="padding:5px 0">
										<input type="text" id="item_${goodsSpecItem.specItemId }" disabled="disabled" value="${goodsSpecItem.item }">
										<c:if test="${opflag == 'UPDATE' }">
											<img width="20" height="20" style="margin-left:5px;" onclick="changeItemsVal('DELETE','${goodsSpecItem.specItemId}')" src="/kenuo/static/ec/images/minus.png">
										</c:if>
									</div>
								</c:forEach>
							</div>
							<input type="text" id="newitems" value="" style="margin:5px 0">
							<img width="20" height="20" style="margin-left:5px;" onclick="changeItemsVal('ADD','')" src="/kenuo/static/ec/images/plus.png">
						</c:if>
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
	<div class="loading"></div>
</body>
</html>