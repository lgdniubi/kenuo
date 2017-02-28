<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/webpage/include/taglib.jsp"%>

<html>
<head>
	<title>退货录入</title>
	<meta name="decorator" content="default"/>
	<!-- 内容上传 引用-->

<script type="text/javascript">
		var goodNum=0;
		var returnAmount=0;
		var validateForm;
		function doSubmit(){//回调函数，在编辑和保存动作时，供openDialog调用提交表单。
		  if(validateForm.form()){
			  var recid=$("#goodsMappingId").val();
			  if(recid==""){
				  top.layer.alert('请选择退货商品!', {icon: 0, title:'提醒'});
				  return;
			  }
			  if(returnAmount==-1){
				 top.layer.alert('该商品欠款或者没有余款，无法退款!', {icon: 0, title:'提醒'});
				 return;
			  }else if(returnAmount==-2){
				 top.layer.alert('该订单还没有进行分销，请稍后再试!', {icon: 0, title:'提醒'});
				 return;
			  }
			  var num=$("#returnNum").val();
				if(num<=0){
					top.layer.alert('退货数量必须大于0，小于购买数量!', {icon: 0, title:'提醒'});
					return;
				}else if(num>goodNum){
					top.layer.alert('退货数量必须大于0，小于购买数量!', {icon: 0, title:'提醒'});
					return;
				}
			  $("#returnAmount").val(returnAmount*num);
			  $("#inputForm").submit();
			  return true;
		  }
	
		  return false;
		}
		
		//初始化
		function init(){
			var isReal="${orders.isReal}";
			if(isReal==1){
				$("#returnNum").attr("readonly",true);
			}
			
		}
		
		function selectFunction(o){
			var id=$("#selectId:checked").val();
			var type="${orders.channelFlag}";
			var isReal="${orders.isReal}";
			var orderId=$("#orderId").val();
			$("#goodsMappingId").val(id);
			goodNum=$("#"+id+"goodsnum").val();
			if("bm"==type){
				var orderA=$("#"+id+"orderAmount").val();
				var totalA=$("#"+id+"total").val();
				$("#totalAmount").val(totalA);
				$("#orderAmount").val(orderA);
				if(isReal==0){
					if(orderA==totalA){
						$("#totalAmount").val(totalA);
						returnAmount=totalA/goodNum;
						var retNum=$("#returnNum").val();
						var SumAmount=returnAmount*retNum;
						$("#returnAmount").val(SumAmount.toFixed(2));
						
					}else{
						$.ajax({
							type:"post",
							async:false,
							data:{
								id:id,
								isReal:isReal,
								orderid:orderId
							 },
							url:"${ctx}/ec/orders/getOrderGoodsIsFre",
							success:function(date){
								returnAmount=date;
								if(returnAmount==-1){
									top.layer.alert('该商品欠款或者没有余款，无法退款!', {icon: 0, title:'提醒'});
									return;
								}else if(returnAmount==-2){
									top.layer.alert('该订单还没有进行分销，请稍后再试!', {icon: 0, title:'提醒'});
									return;
								}else{
									$("#returnAmount").val(returnAmount*$("#returnNum").val());
								}
								
							},
							error:function(XMLHttpRequest,textStatus,errorThrown){
										    
							}
						});
						
					}
				}else if(isReal==1){
					$.ajax({
						type:"post",
						async:false,
						data:{
							id:id,
							isReal:isReal,
							orderid:orderId
						 },
						url:"${ctx}/ec/orders/getOrderGoodsIsFre",
						success:function(date){
							returnAmount=date;
							if(returnAmount==-1){
								top.layer.alert('该商品欠款或者没有余款，无法退款!', {icon: 0, title:'提醒'});
								return;
							}else if(returnAmount==-2){
								top.layer.alert('该订单还没有进行分销，请稍后再试!', {icon: 0, title:'提醒'});
								return;
							}else{
								$("#returnAmount").val(returnAmount)
							}
						},
						error:function(XMLHttpRequest,textStatus,errorThrown){
									    
						}
					});
				}

			}else{
				var totalA=$("#"+id+"total").val();
				var orderA=$("#"+id+"orderAmount").val();
				$("#totalAmount").val(totalA);
				returnAmount=totalA/goodNum;
				var retNum=$("#returnNum").val();
				var SumAmount=returnAmount*retNum;
				$("#returnAmount").val(SumAmount.toFixed(2));
				$("#orderAmount").val(orderA);
			}
			
		}
		
		//退款 换货 展示隐藏 退款数量
		function selectType(o){
			var type=$("#applyType").val();
			if(type==0){
				$("#hideandshow").show();
			}else if(type==1){
				$("#hideandshow").hide();
			}
		}
		
		//退款数量改变
		function returnChangeNum(){
			var num=$("#returnNum").val();
			if(num<=0){
				top.layer.alert('退货数量必须大于0，小于购买数量!', {icon: 0, title:'提醒'});
				return;
			}else if(num>goodNum){
				top.layer.alert('退货数量必须大于0，小于购买数量!', {icon: 0, title:'提醒'});
				return;
			}
			$("#returnAmount").val(returnAmount*num);
			
		}
	
		$(document).ready(function(){
	
			
			$("#reason").focus();
			validateForm = $("#inputForm").validate({
				rules: {
					returnmoney:{
						number:true,
						min:0
						},
					bankno:{
						digits:true
					}
				},
				messages: {
					returnmoney:{
						number:"输入合法的价格",
						min:"价格最小为0"
						},
						bankno:{
							digits:"输入合法的银行卡号"
						}
					
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
		
			//在ready函数中预先调用一次远程校验函数，是一个无奈的回避案。(刘高峰）
			//否则打开修改对话框，不做任何更改直接submit,这时再触发远程校验，耗时较长，
			//submit函数在等待远程校验结果然后再提交，而layer对话框不会阻塞会直接关闭同时会销毁表单，因此submit没有提交就被销毁了导致提交表单失败。
			$("#inputForm").validate().element($("#reason"));
			
// 			laydate({
// 	            elem: '#userinfo.birthday', //目标元素。由于laydate.js封装了一个轻量级的选择器引擎，因此elem还允许你传入class、tag但必须按照这种方式 '#id .class'
// 	            event: 'focus' //响应事件。如果没有传入event，则按照默认的click
// 	        });
			
			
		});

		window.onload = init;
</script>
</head>
<body>
<div class="wrapper wrapper-content">
	<div class="ibox">
		<div class="ibox-content">
			<div class="clearfix">
				<form:form id="inputForm" modelAttribute="returnedGoods" action="${ctx}/ec/orders/saveReturn" method="post" class="form-horizontal">
					<form:hidden path="userId"/>
					<form:hidden path="orderId" value="${orders.orderid}"/>
					<form:hidden path="goodsMappingId"/>
					<label>订单号：</label>${orders.orderid}
					<p></p>
					<label>选择售后商品</label>
					<table class="table table-bordered  table-condensed dataTables-example dataTable no-footer">
						<thead>
							<tr>
								<th style="text-align: center;">选择</th>
								<th style="text-align: center;">商品名称</th>
								<th style="text-align: center;">商品规格</th>
								<th style="text-align: center;">商品类型</th>
								<th style="text-align: center;">市场价</th>
								<th style="text-align: center;">优惠价</th>
								<th style="text-align: center;">系统价</th>
								<th style="text-align: center;">成交价</th>
								<th style="text-align: center;">购买数量</th>
								<th style="text-align: center;">实付款</th>
								<th style="text-align: center;">实际服务单价</th>
								<th style="text-align: center;">剩余次数</th>
								<th style="text-align: center;">余款</th>
								<th style="text-align: center;">欠款</th>
							</tr>
						</thead>
						<tbody>
							<c:forEach items="${orderGoodList}" var="orderGood">
								<tr>
									<td><input id="selectId" name="selectId" type="radio" value="${orderGood.recid}"  class="form required"  onchange="selectFunction(this)"/></td>
									<td style="text-align: center;">${orderGood.goodsname}</td>
									<td style="text-align: center;">${orderGood.speckeyname }</td>
									<td  style="text-align: center;">
										<c:if test="${orderGood.isreal==0}">
											实物
										</c:if>
										<c:if test="${orderGood.isreal==1}">
											虚拟
										</c:if>
									</td>
									<td  style="text-align: center;">${orderGood.marketprice}</td>
									<td  style="text-align: center;">${orderGood.goodsprice}</td>
									<td  style="text-align: center;">${orderGood.costprice}</td>
									<td  style="text-align: center;">${orderGood.orderAmount}</td>
									<td  style="text-align: center;">${orderGood.goodsnum}</td>
									<td  style="text-align: center;">${orderGood.totalAmount}</td>
									<td  style="text-align: center;">${orderGood.singleRealityPrice}</td>
									<td  style="text-align: center;">${orderGood.remaintimes}</td>
									<td  style="text-align: center;">${orderGood.orderBalance}</td>
									<td  style="text-align: center;">${orderGood.orderArrearage}</td>
								</tr>
								<input type="hidden" id="${orderGood.recid}total" name="${orderGood.recid}total" value="${orderGood.totalAmount}" />
								<input type="hidden" id="${orderGood.recid}goodsnum" name="${orderGood.recid}goodsnum" value="${orderGood.goodsnum}" />
								<input type="hidden" id="${orderGood.recid}orderAmount" name="${orderGood.recid}orderAmount" value="${orderGood.orderAmount}" />
							</c:forEach>
						</tbody>						
					</table>	
					<p></p>					
				  	<label><font color="red">*</font>售后原因：</label>
			       	<form:input path="returnReason" htmlEscape="false" maxlength="50" style="width: 300px;height:30px;" class="form-control"/>
			       	<p></p>
			        <label><font color="red">*</font>问题描述：</label>
			        <form:textarea path="problemDesc" htmlEscape="false" rows="3"  style="width:300px;" maxlength="200" class="form-control"/>
			        <p></p>
			        <label><font color="red">*</font>申请类型：</label>
			        <c:if test="${orders.isReal==0}">
			        	<form:select path="applyType" class="form-control" style="width:185px;" onchange="selectType(this)">
							<form:option value="0">退货并退款</form:option>
							<form:option value="1">仅换货</form:option>
						</form:select>
			        </c:if>
			        <c:if test="${orders.isReal==1}">
			        	<form:select path="applyType" class="form-control" style="width:185px;" >
							<form:option value="0">退货并退款</form:option>
						</form:select>
			        </c:if>
					<p></p>
					<form:hidden path="orderAmount"/>
			        <label><font color="red">*</font>支付金额：</label>
			        <form:input path="totalAmount" htmlEscape="false" maxlength="10" style="width: 180px;height:30px;" class="form-control" readonly="true"/>
					<p></p>
			        <label><font color="red">*</font>售后商品数量：</label>
			        <form:input path="returnNum" htmlEscape="false" maxlength="10" value="1" style="width: 180px;height:30px;" class="form-control" onchange="returnChangeNum()"/>
					<p></p>
					<div id="hideandshow" style="display: display">
						 <label><font color="red">*</font>退款金额：</label>
						 <form:input path="returnAmount" htmlEscape="false" maxlength="10"  style="width:180px;" class="form-control required"/>
					</div>
			       
			        <p></p>
					<label>客服备注：</label>
					<form:textarea path="remarks" htmlEscape="false" rows="3" maxlength="200" style="width:300px;" class="form-control"/>
				</form:form>
			</div>
		</div>
	</div>
</div>
</body>
</html>