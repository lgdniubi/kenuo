<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/webpage/include/taglib.jsp"%>
<%@ include file="/webpage/include/icheck.jsp"%>
<html>
<head>
	<title>新增团购项目</title>
	<meta name="decorator" content="default"/>
	<link rel="stylesheet" href="${ctxStatic}/ec/css/loading.css">
	<script type="text/javascript">
		var validateForm;
		function doSubmit(){//回调函数，在编辑和保存动作时，供openDialog调用提交表单。
			if(validateForm.form()){
        	  loading('正在提交，请稍等...');
		      $("#inputForm").submit();
	     	  return true;
		  	}
		  return false;
		}
		
		$(document).ready(function(){
			validateForm = $("#inputForm").validate({
				rules: {
					
				},
				messages:{
					
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
		
		//根据商品id 查询商品信息
		function selectgood(){
			var val=$("#goodsId").val();
			if(val!=0){
				$(".loading").show();//打开展示层
				$.ajax({
					 type:"get",
					 dataType:"json",
					 url:"${ctx}/ec/groupActivity/Getgood?id="+val,
					 success:function(date){
						$(".loading").hide();//隐藏展示层
						if(date != null){
							if(date.isReal == 0 || date.isReal == 1){
								$("#mytable").empty("");
								$("#goodsGoodsId").val(date.goodsId);
								$("#goodsIsReal").val(date.isReal);
								$("#gooodsGoodsName").val(date.goodsName);
								var str = "<tr><td>规格名称</td><td>市场价</td><td>团购价</td></tr>";
								$.each(date.goodsSpecPricesList,function(index,item){
									str = str + "<tr><td>"+item.specKeyValue+"</td><td>"+item.marketPrice+"</td>"+
												"<td><input id='specKey_"+item.specKey+"' name='specKey_"+item.specKey+"' value='0' class='form-control required'><input id='specKey' name='specKey' value='"+item.specKey+"' class='form-control' type='hidden'></td>"+
												"</tr>";
			           			});
								$("#mytable").append(str);
							}else{
								top.layer.alert('目前只支持实物和虚拟', {icon: 0, title:'提醒'});
							}
						}else{
							top.layer.alert('未查询到该商品', {icon: 0, title:'提醒'});
						}  
					 },
					 error:function(XMLHttpRequest,textStatus,errorThrown) {
					    $(".loading").hide();//隐藏展示层
						top.layer.alert('未查询到该商品', {icon: 0, title:'提醒'});
					 }
					 
					});
			}
			
		}
	</script>
</head>
	<body>
		<form:form id="inputForm" modelAttribute="mtmyGroupActivityGoods" action="${ctx}/ec/groupActivity/saveGoods" method="post" class="form-horizontal">
		<form:hidden path="mtmyGroupActivity.id"/>
		<form:hidden path="gId"/>
			<table class="table table-bordered  table-condensed dataTables-example dataTable no-footer">
				<tbody>
					<tr>
						<td class="width-15 active"><label class="pull-right"><font color="red">*</font>商品ID:</label></td>
						<td class="width-35">
							<input id="goodsId" class="form-control" value="${mtmyGroupActivityGoods.goods.goodsId }">
							<input id="goodsGoodsId" name="goods.goodsId" value="${mtmyGroupActivityGoods.goods.goodsId }" type="hidden">
							<c:if test="${mtmyGroupActivityGoods.gId == 0}">
								<a href="#" onclick="selectgood()" class="btn btn-primary btn-xs"><i class="fa fa-plus"></i>查询</a>
							</c:if>
						</td>
						<td class="width-15 active"><label class="pull-right">商品类型:</label></td>
						<td class="width-35">
							<select id="goodsIsReal" disabled="disabled" class="form-control">
								<option value=''></option>
								<option value='0' ${(mtmyGroupActivityGoods.goods.isReal == '0')?'selected="selected"':''}>实物</option>
								<option value='1' ${(mtmyGroupActivityGoods.goods.isReal == '1')?'selected="selected"':''}>虚拟</option>
							</select>
						</td>
					</tr>
					<tr>
						<td class="active"><label class="pull-right"><font color="red">*</font>商品名称:</label></td>
						<td class="width-35">
							<input id="gooodsGoodsName" value="${mtmyGroupActivityGoods.goods.goodsName }" readonly="readonly" class="form-control">
						</td>
						<td class="active"><label class="pull-right"><font color="red">*</font>市场价:</label></td>
						<td class="width-35">
							<form:input path="groupActivityMarketPrice" cssClass="form-control required"/>
						</td>
					</tr>
					<tr>
						<td class="active"><label class="pull-right"><font color="red">*</font>团购价:</label></td>
						<td class="width-35">
							<form:input path="groupActivityPrice" cssClass="form-control required"/>
						</td>
						<td class="active"><label class="pull-right"><font color="red">*</font>预约金:</label></td>
						<td class="width-35">
							<form:input path="groupActivityAdvancePrice" cssClass="form-control required"/>
						</td>
					</tr>
					<tr>
						<td class="active"><label class="pull-right"><font color="red">*</font>限购数量:</label></td>
						<td class="width-35">
							<form:input path="purchaseCount" cssClass="form-control required"/>
						</td>
						<td class="active"><label class="pull-right"><font color="red">*</font>排序:</label></td>
						<td class="width-35">
							<form:input path="sort" cssClass="form-control required"/>
						</td>
					</tr>
					<tr>
						<td class="active"><label class="pull-right">规格项:</label></td>
						<td colspan="3">
							<table id="mytable" class="table table-striped table-bordered  table-hover table-condensed  dataTables-example dataTable no-footer">
								<c:if test="${mtmyGroupActivityGoods.gId != 0}">
									<tr>
										<td>规格名称</td>
										<td>市场价</td>
										<td>团购价</td>
									</tr>
									<c:forEach items="${mtmyGroupActivityGoods.goods.goodsSpecPricesList}" var="goodsSpecPricesList">
										<tr>
											<td>
												${goodsSpecPricesList.specKeyValue}
											</td>
											<td>
												${goodsSpecPricesList.marketPrice}
											</td>
											<td>
												<input id="specKey_${goodsSpecPricesList.specKey}" name="specKey_${goodsSpecPricesList.specKey}" value="${goodsSpecPricesList.groupActivityPrice}" class="form-control required">
												<input id="specKey" name="specKey" value="${goodsSpecPricesList.specKey}" class="form-control" type="hidden">
											</td>
										</tr>
									</c:forEach>
								</c:if>
							</table>
						</td>
					</tr>
					<tr>
						<td class="active"><label class="pull-right">除外日期:</label></td>
						<td class="width-35">
							<form:select path="exceptDate" class="form-control">
								<form:option value=""></form:option>
								<form:option value="法定节假日和周末通用">法定节假日和周末通用</form:option>
								<form:option value="仅限周一到周五使用">仅限周一到周五使用</form:option>
								<form:option value="仅限周末使用">仅限周末使用</form:option>
							</form:select>
						</td>
						<td class="active"><label class="pull-right"><font color="red">*</font>使用范围:</label></td>
						<td class="width-35">
							<form:input path="rangeOfApplication" cssClass="form-control required"/>
						</td>
					</tr>
					<tr>
						<td class="active"><label class="pull-right">其他提示:</label></td>
						<td class="width-35" colspan="3">
							<form:textarea path="otherTips" rows="10" cols="60"/>
						</td>
					</tr>
				</tbody>
			</table>   
		</form:form>
		<div class="loading"></div> 
	</body>
</html>