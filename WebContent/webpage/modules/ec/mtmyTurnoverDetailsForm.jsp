<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/webpage/include/taglib.jsp"%>

<html>
<head>
	<title>退货录入</title>
	<meta name="decorator" content="default"/>
	<!-- 内容上传 引用-->

<script type="text/javascript">
		var validateForm;
		var flag=false;//记录业务员的营业额+增减值>=0
		function doSubmit(){//回调函数，在编辑和保存动作时，供openDialog调用提交表单。
		  if(validateForm.form()){
			  if(flag){
				  top.layer.alert('营业额必须大于等于0', {icon: 0, title:'提醒'});
				  return;
			  }
			  var returnAmount=$("#returnAmount").val();
			  var amounts = 0;//店铺营业额
			  var oldAmount = 0;//店铺旧的营业额
			  var added = 0;//数据库查询的之前编辑营业额的数据
			  $("[name='added']").each(function(){
				  added += parseFloat($(this).val());
	    	  });
			  $("[name='amounts']").each(function(){
				  amounts += parseFloat($(this).val());
	    	  });
			  if(parseFloat(amounts) + parseFloat(added) + parseFloat(returnAmount) != 0){
				  top.layer.alert('请注意输入的增减值!', {icon: 0, title:'提醒'});
				  return;
			  }
			  $("#inputForm").submit();
			  return true;
		  }
		  return false;
		}
		/* //校验输入营业额+增减值+数据库的增减值>=0
		function returnChangeAmount(obj,pushmoney,added){
			var amount = $(obj).val();
			if(parseFloat(pushmoney) + parseFloat(added) + parseFloat(amount) < 0){
				top.layer.alert('营业额必须大于等于0', {icon: 0, title:'提醒'});
				flag=true;
				return;
			}
			flag = false;
		} */
		$(document).ready(function(){
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
<div class="wrapper wrapper-content">
	<div class="ibox">
		<div class="ibox-content">
			<div class="clearfix">
				<form:form id="inputForm" modelAttribute="mtmyTurnoverDetails" action="${ctx}/ec/returned/saveMtmyTurnoverDetails" method="post" class="form-horizontal">
					<input type="hidden" id="returnAmount" name="returnAmount" value="${returnAmount}"/>
					<!-- 存储需要字段 -->
					<input type="hidden" id="orderId" name="orderId" value="${turnOverDetails.orderId}"/>
					<input type="hidden" id="detailsId" name="detailsId" value="${turnOverDetails.detailsId}"/>
					<input type="hidden" id="mappingId" name="mappingId" value="${turnOverDetails.mappingId}"/>
					<input type="hidden" id="goodsId" name="goodsId" value="${turnOverDetails.goodsId}"/>
					
					<table class="table table-bordered  table-condensed dataTables-example dataTable no-footer">
						<thead>
							<tr>
								<th style="text-align: center;">时间</th>
								<th style="text-align: center;">类型</th>
								<th style="text-align: center;">售后金额</th>
								<th style="text-align: center;">店名</th>
								<th style="text-align: center;">店铺增减营业额</th>
								<th style="text-align: center;">营业额占比</th>
								<th style="text-align: center;">增减值</th>
							</tr>
						</thead>
						<tbody>
							${shopTurnover}
						</tbody>						
					</table>
					<p><font color="red">备注：<br/>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;正数是增加营业额，如：填写90，代表营业额增加90;<br/>
						&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;负数是减少营业额，如：填写-90，代表营业额扣减90</font></p>
				</form:form>
			</div>
		</div>
	</div>
</div>
</body>
</html>