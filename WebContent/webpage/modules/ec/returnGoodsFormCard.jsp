<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/webpage/include/taglib.jsp"%>

<html>
<head>
	<title>退货审核</title>
	<meta name="decorator" content="default"/>
	<!-- 内容上传 引用-->
	<link href="${ctxStatic}/train/css/imgbox.css" rel="stylesheet" />
	<script src="${ctxStatic}/train/js/jquery.min.js"></script>
	<script type="text/javascript">
		var jq = $.noConflict();
	</script>
	<script src="${ctxStatic}/train/js/jquery.imgbox.pack.js"></script>
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
	
			jq("a[class='img']").imgbox({
				'speedIn'		: 0,
				'speedOut'		: 0,
				'alignment'		: 'center',
				'overlayShow'	: true,
				'allowMultiple'	: false
			});
			/* $("#reason").focus(); */
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
		
			//在ready函数中预先调用一次远程校验函数，是一个无奈的回避案。(刘高峰）
			//否则打开修改对话框，不做任何更改直接submit,这时再触发远程校验，耗时较长，
			//submit函数在等待远程校验结果然后再提交，而layer对话框不会阻塞会直接关闭同时会销毁表单，因此submit没有提交就被销毁了导致提交表单失败。
			/* $("#inputForm").validate().element($("#reason")); */
			
// 			laydate({
// 	            elem: '#userinfo.birthday', //目标元素。由于laydate.js封装了一个轻量级的选择器引擎，因此elem还允许你传入class、tag但必须按照这种方式 '#id .class'
// 	            event: 'focus' //响应事件。如果没有传入event，则按照默认的click
// 	        });
			
			
		});
</script>
</head>
<body>
	<div class="wrapper wrapper-content">
		<div class="ibox">
			<div class="ibox-content">
				<div class="clearfix">
				<form:form id="inputForm" modelAttribute="returnedGoods" action="${ctx}/ec/returned/save" method="post" class="form-horizontal">
					<form:hidden path="id"/>
					<form:hidden path="isReal"/>
			        <label>退货单号：</label><label>${returnedGoods.id}</label>&nbsp;&nbsp;
			        <label>原订单号：</label><label>${returnedGoods.orderId}</label>
			   		<p></p>
			      	<label>用户名：</label><label>${returnedGoods.userName}</label>&nbsp;&nbsp;
			        <label>手机号：</label><label>${returnedGoods.mobile}</label>
			        <table class="table table-bordered  table-condensed dataTables-example dataTable no-footer">
						<thead>
							<tr>
								<th style="text-align: center;">商品编号</th>
								<th style="text-align: center;">商品名称</th>
								<th style="text-align: center;">商品类型</th>
								<th style="text-align: center;">子项名称</th>
								<th style="text-align: center;">子项类型</th>
								<th style="text-align: center;">市场价</th>
								<th style="text-align: center;">优惠价</th>
								<th style="text-align: center;">成本价</th>
								<th style="text-align: center;">购买数量</th>
								<th style="text-align: center;">应付款</th>
								<th style="text-align: center;">实付款</th>
								<th style="text-align: center;">欠款</th>
							</tr>
						</thead>
						<c:if test="${not empty returnGoodsCard}">
							<tbody>
								${returnGoodsCard}
							</tbody>
						</c:if>						
					</table>
			        <label>售后商品图片：</label>
			        <c:forEach items="${returnedGoods.imgList}" var="imgList">
			        	<label>
			        		<a class='img' href="${imgList.returnImg}"><img alt="退货商品图片" src="${imgList.returnImg}" style="width: 180px;height:100px;"></a>
			        	</label>
			        </c:forEach>
			      	<p></p>
			      	<label>售后原因：</label>
			       	<form:input path="returnReason" htmlEscape="false" maxlength="50" style="width: 300px;height:30px;" class="form-control"/>
			       	<p></p>
			        <label>问题描述：</label>
			        <form:textarea path="problemDesc" htmlEscape="false" rows="3"  style="width:300px;" maxlength="200" class="form-control"/>
			        <p></p>
			        <table>
						<tr>
							<td width="70px"><label>归属机构：</label></td>
							<td width="300px">
								<input id="belongOfficeId" class=" form-control input-sm" name="belongOfficeId" value="${returnedGoods.belongOfficeId}" type="hidden">
								<div class="input-group">
									<input id="belongOfficeName" class=" form-control input-sm" name="belongOfficeName" readonly="readonly" value="${returnedGoods.belongOfficeName}" data-msg-required="" style="" type="text">
										<span class="input-group-btn">
											<button id="belongOfficeButton" class="btn btn-sm btn-primary " disabled="disabled" type="button">
												<i class="fa fa-search"></i>
											</button>
										</span>
								</div>
								<label id="belongOfficeName-error" class="error" for="belongOfficeName" style="display:none"></label>
							</td>
						</tr>
						<tr>
							<td width="70px"><label>归&nbsp;&nbsp;属&nbsp;&nbsp;人：</label></td>
							<td width="300px">
								<input id="belongUserId" class=" form-control input-sm" name="belongUserId" value="${returnedGoods.belongUserId}" type="hidden">
								<div class="input-group">
									<input id="belongUserName" class=" form-control input-sm" name="belongUserName" readonly="readonly" value="${returnedGoods.belongUserName}" data-msg-required="" style="" type="text">
										<span class="input-group-btn">
											<button id="belongUserButton" class="btn btn-sm btn-primary " disabled="disabled" type="button">
												<i class="fa fa-search"></i>
											</button>
										</span>
								</div>
								<label id="belongUserName-error" class="error" for="belongUserName" style="display:none"></label>
							</td>
						</tr>
					</table>
			       	<p></p>
			        <label>申请类型：</label>
			        <label>
						退货并退款
					</label>
					<p></p>
					<label>订单状态：</label>
			      	<label>
			      		<form:hidden path="returnStatus"/>
				      	<c:if test="${returnedGoods.returnStatus==-10}">
							拒绝退货
						</c:if>
						<c:if test="${returnedGoods.returnStatus==-20}">
							拒绝换货
						</c:if>		
						<c:if test="${returnedGoods.returnStatus==11}">
							申请退货
						</c:if>
						<c:if test="${returnedGoods.returnStatus==12}">
							同意退货
						</c:if>
						<c:if test="${returnedGoods.returnStatus==13}">
							退货中
						</c:if>
						<c:if test="${returnedGoods.returnStatus==14}">
							退货完成
						</c:if>
						<c:if test="${returnedGoods.returnStatus==15}">
							退款中
						</c:if>
						<c:if test="${returnedGoods.returnStatus==16}">
							已退款
						</c:if>
						<c:if test="${returnedGoods.returnStatus==21}">
							申请换货
						</c:if>
						<c:if test="${returnedGoods.returnStatus==22}">
							同意换货
						</c:if>
						<c:if test="${returnedGoods.returnStatus==23}">
							换货退货中
						</c:if>
						<c:if test="${returnedGoods.returnStatus==24}">
							换货退货完成
						</c:if>
						<c:if test="${returnedGoods.returnStatus==25}">
							换货中
						</c:if>
						<c:if test="${returnedGoods.returnStatus==26}">
							换货完成
						</c:if>
					</label>
					<c:if test="${not empty realnum}">
						<p></p>
						${realnum}
					</c:if>
					<p></p>
					<label>支付金额：</label>
			        <form:input path="totalAmount" htmlEscape="false" maxlength="10" style="width: 180px;height:30px;" class="form-control" readonly="true"/>
					<p></p>
					<label>退款金额：</label>
			        <form:input path="returnAmount" htmlEscape="false" maxlength="10"  style="width:180px;" class="form-control required" readonly="true"/>
					<p></p>
					<c:if test="${flag != 1}">
				        <p></p>
		        		<label>是否同意申请：</label>
						<label><input id="isConfirm" name="isConfirm" type="radio" value="12"  class="form required" />同意退货 </label>
						<label><input id="isConfirm" name="isConfirm" type="radio" value="-10"  class="form required" />拒绝退货</label>
					</c:if>
					<div id="refusal" style="display:none;">
						<label>拒绝原因：</label>
						<form:textarea path="refusalCause" htmlEscape="false" rows="3" maxlength="30" style="width:300px;" class="form-control required"/>
						<label><font color="red">如果拒绝请说明原因</font></label>
					</div>
			        <p></p>
			        <label>客服备注：</label>
			        <form:textarea path="remarks" htmlEscape="false" rows="3" maxlength="200" style="width:300px;" class="form-control"/>
			        <p></p>
				</form:form>
				</div>
			</div>
		</div>
	</div>
</body>
</html>