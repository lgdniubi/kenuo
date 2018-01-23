<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/webpage/include/taglib.jsp"%>

<html>
<head>
	<title>退货录入</title>
	<meta name="decorator" content="default"/>
	<!-- 内容上传 引用-->

<script type="text/javascript">
		var isReal=0;//判断是否为实物
		var goodNum=0;
		var returnAmount=0;//退款金额
		var orderArrearage=0;//订单欠款
		var orderAmount=0;//应付款
		var totalAmount=0;//实付款
		var returnedGoodsNum = 0;//后台查询出来 "实物" 中正在退货的商品数量
		var surplusReturnAmount = 0;//剩余退款可退款金额
		var validateForm;
		
		function doSubmit(){//回调函数，在编辑和保存动作时，供openDialog调用提交表单。
		  if(validateForm.form()){
			  var recid=$("#goodsMappingId").val();
			  if(recid==""){
				  top.layer.alert('请选择退货商品!', {icon: 0, title:'提醒'});
				  return;
			  }
			  //校验售后数量,售后商品数量 <=goodNum实际购买量-returnedGoodsNum(从后台查出来的退货中的数量)
			  var rn=$("#returnNum").val();
			  if(parseInt(rn)<0){
				  top.layer.alert('售后商品数量必须大于等于0，小于等于可售后数量!', {icon: 0, title:'提醒'});
				  return;
			  }else if(parseInt(rn)>(parseInt(goodNum)-parseInt(returnedGoodsNum))){
				  top.layer.alert('售后商品数量必须大于等于0，小于等于可售后数量!', {icon: 0, title:'提醒'});
				  return;
			  }
			  //校验退款金额
			  var ra=$("#returnAmount").val();
			  if(parseFloat(ra)<0){
				  top.layer.alert('退款金额必须大于等于0，小于等于可售后金额!', {icon: 0, title:'提醒'});
				  return;
			  }else if(parseFloat(surplusReturnAmount) < parseFloat(ra)){
				  top.layer.alert('退款金额必须大于等于0，小于等于可售后金额!', {icon: 0, title:'提醒'});
				  return;
			  }
			  //退货并退款:注意当售后数量和金额都为0,不能售后
			  //当是退货并退款或者仅退款,退款方式需要校验
			  var type=$("#applyType").val();
			  if(type == 1){//当选择仅换货
				  if(parseInt(rn)<=0){
					  top.layer.alert('售后商品数量必须大于0，小于等于可售后数量!', {icon: 0, title:'提醒'});
					  return;
				  }else if(parseInt(rn)>(parseInt(goodNum)-parseInt(returnedGoodsNum))){
					  top.layer.alert('售后商品数量必须大于0，小于等于可售后数量!', {icon: 0, title:'提醒'});
					  return;
				  }
				  //仅换货:收款人和收款账户默认空,且退款方式为'0'
				  $("#returnType").val(0);//默认选择原路返回
				  $("#receiveName").val("");//清空收款人信息
				  $("#receiveAccount").val("");//清空收款账号
			  }else {//退货并退款      仅退款
				  //当售后数量和售后金额都为0,商品不能售后
				  if(parseInt(rn) == 0 && parseFloat(ra) == 0){
					  top.layer.alert('售后商品数量和退款金额不能都为0!', {icon: 0, title:'提醒'});
					  return;
				  }
				  //退款方式是"银行卡账户"需要填写收款人和收款方式
				  var returnType=$("#returnType").val();
				  if(returnType == 4){
					  var receiveName = $("#receiveName").val();	
					  var receiveAccount = $("#receiveAccount").val();	
					  if(null == receiveName || "" == receiveName){
						  top.layer.alert('收款人或者收款账号不能为空!', {icon: 0, title:'提醒'});
						  return;
					  }
					  if(null == receiveAccount || "" == receiveAccount){
						  top.layer.alert('收款人或者收款账号不能为空!', {icon: 0, title:'提醒'});
						  return;
					  }
				  }else{
					  $("#receiveName").val("");//清空收款人信息
					  $("#receiveAccount").val("");//清空收款账号
				  }
			  }
			  $("#orderArrearage").val(orderArrearage);//当实物存在欠款,欠款金额存入
			  $("#inputForm").submit();
			  return true;
		  }
		  return false;
		}
		
		//选中某个商品时,查询数据
		function selectFunction(o){
			var id=$("#selectId:checked").val();
			var type="${orders.channelFlag}";
			var orderId=$("#orderId").val();
			isReal="${orders.isReal}";
			$("#goodsMappingId").val(id);
			goodNum=$("#"+id+"goodsnum").val();//购买的数量
			orderArrearage=$("#"+id+"orderArrearage").val();//订单欠款
			totalAmount=$("#"+id+"totalAmount").val();//实付款
			orderAmount=$("#"+id+"orderAmount").val();//应付款
			
			returnedGoodsNum = $("#"+id+"returnedGoodsNum").val();//实物中  从后台查出来的退货的数量(从申请售后到已退款,包括实物换货,但不包括已完成换货的)
			$("#returnNum").val(parseInt(goodNum)-parseInt(returnedGoodsNum));//为售后商品数量赋值 = 购买数量-已售后数量(除了已拒绝申请和完成换货的商品不能售后)
			
			//实物存在欠款,全部商品售后
			if(orderArrearage>0){
				$("#returnNum").attr("readonly",true);//实物存在欠款,售后数量全部写入
			}else{
				$("#returnNum").attr("readonly",false);
			}
			$("#totalAmount").val(totalAmount);//实付款金额
			$("#orderAmount").val(orderAmount);//应付款金额
			//售后金额 <= 实付金额-已售后
			$.ajax({
				type:"post",
				async:false,
				data:{
					goodsMappingId:id,
					orderId:orderId
				 },
				url:"${ctx}/ec/returned/getSurplusReturnAmount",
				success:function(obj){
					//计算商品剩余可退款金额(注意:拒绝售后,拒绝换货和换货完成的金额要排除)
					surplusReturnAmount = totalAmount - obj;
					$("#showReturn").text(surplusReturnAmount);
				},
				error:function(XMLHttpRequest,textStatus,errorThrown){
							    
				}
			});
		}
		
		//退款并退货 全部展示,仅退货 展示数量,仅退款 展示退款金额
		function selectType(o){
			var type=$("#applyType").val();
			if(type==1){//仅换货,不显示"退款金额"和"退款方式";
				$("#returnNum").attr("readonly",false);//仅换货,商品数量都可以输入
				$("#returnAmountIsShow").hide();//退款金额
				$("#returnTypeIsShow").hide();//退款方式
			}else{
				if(orderArrearage>0){
					$("#returnNum").attr("readonly",true);//实物存在欠款,售后数量全部写入
				}else{
					$("#returnNum").attr("readonly",false);
				}
				$("#returnAmountIsShow").show();//退款金额
				$("#returnTypeIsShow").show();//退款方式
			}
		}
		//选择退款方式,只有选择"银行卡账户",需要填写收款人和收款账号
		function selectReturnType(o){
			var type=$("#returnType").val();
			if(type==4){
				$("#selectReceive").show();
			}else{
				$("#selectReceive").hide();
			}
		}
		//售后商品数量改变校验
		function returnChangeNum(){
			var num=$("#returnNum").val();
			if(parseInt(goodNum) == 0){
				top.layer.alert('请先选择商品', {icon: 0, title:'提醒'});
				return;
			}
			if(parseInt(num)<0){
				top.layer.alert('退货数量必须大于等于0，小于等于可申请的售后数量!', {icon: 0, title:'提醒'});
				return;
			}else if(parseInt(num)>(parseInt(goodNum)-parseInt(returnedGoodsNum))){
				top.layer.alert('退货数量必须大于等于0，小于等于可申请的售后数量!', {icon: 0, title:'提醒'});
				return;
			}
		}
		
		//退款金额校验
		function returnChangeAmount(){
			var ra=$("#returnAmount").val();
			if(parseFloat(ra)<0){
				top.layer.alert('退款金额必须大于等于0，小于等于可售后金额!', {icon: 0, title:'提醒'});
				return;
			}else if(parseFloat(surplusReturnAmount)<parseFloat(ra)){
				top.layer.alert('退款金额必须大于等于0，小于等于可售后金额!', {icon: 0, title:'提醒'});
				return;
			}
		}
		$(document).ready(function(){
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
			
// 			laydate({
// 	            elem: '#userinfo.birthday', //目标元素。由于laydate.js封装了一个轻量级的选择器引擎，因此elem还允许你传入class、tag但必须按照这种方式 '#id .class'
// 	            event: 'focus' //响应事件。如果没有传入event，则按照默认的click
// 	        });
			$("#selectReceive").hide();//收款人和收款账户首先隐藏
		});
</script>
</head>
<body>
<div class="wrapper wrapper-content">
	<div class="ibox">
		<div class="ibox-content">
			<div class="clearfix">
				<form:form id="inputForm" modelAttribute="returnedGoods" action="${ctx}/ec/orders/saveReturnKind" method="post" class="form-horizontal">
					<form:hidden path="userId"/>
					<form:hidden path="orderId" value="${orders.orderid}"/>
					<form:hidden path="goodsMappingId"/>
					<form:hidden path="isReal" value="${orders.isReal}"/>
					<form:hidden path="orderArrearage"/>
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
								<th style="text-align: center;">异价比例</th>
								<th style="text-align: center;">异价后价格</th>
								<th style="text-align: center;">成交价</th>
								<th style="text-align: center;">购买数量</th>
								<th style="text-align: center;">实付金额</th>
								<th style="text-align: center;">欠款</th>
							</tr>
						</thead>
						<tbody>
							<c:forEach items="${orderGoodList}" var="orderGood">
								<tr>
									<td><input id="selectId" name="selectId" type="radio" value="${orderGood.recid}"  class="form required"  onchange="selectFunction(this)"/></td>
									<td style="text-align: center;">${orderGood.goodsname}</td>
									<td style="text-align: center;">${orderGood.speckeyname }</td>
									<td  style="text-align: center;">实物</td>
									<td  style="text-align: center;">${orderGood.marketprice}</td>
									<td  style="text-align: center;">${orderGood.goodsprice}</td>
									<td  style="text-align: center;">${orderGood.costprice}</td>
									<td  style="text-align: center;">${orderGood.ratio}</td>
									<td  style="text-align: center;">${orderGood.ratioPrice}</td>
									<td  style="text-align: center;">${orderGood.orderAmount}</td>
									<td  style="text-align: center;">${orderGood.goodsnum}</td>
									<td  style="text-align: center;">${orderGood.totalAmount}</td>
									<td  style="text-align: center;">${orderGood.orderArrearage}</td>
								</tr>
								<input type="hidden" id="${orderGood.recid}goodsnum" name="${orderGood.recid}goodsnum" value="${orderGood.goodsnum}" />
								<input type="hidden" id="${orderGood.recid}totalAmount" name="${orderGood.recid}totalAmount" value="${orderGood.totalAmount}" />
								<input type="hidden" id="${orderGood.recid}orderAmount" name="${orderGood.recid}orderAmount" value="${orderGood.orderAmount}" />
								<input type="hidden" id="${orderGood.recid}orderArrearage" name="${orderGood.recid}orderArrearage" value="${orderGood.orderArrearage}" />
								<input type="hidden" id="${orderGood.recid}returnedGoodsNum" value="${orderGood.returnNum}" />
							</c:forEach>
						</tbody>						
					</table>
					<p></p>					
				  	<label><font color="red">*</font>售后原因：</label>
			       	<form:input path="returnReason" htmlEscape="false" maxlength="50" style="width: 300px;height:30px;" class="form-control required"/>
			       	<p></p>
			        <label><font color="red">*</font>问题描述：</label>
			        <form:textarea path="problemDesc" htmlEscape="false" rows="3"  style="width:300px;" maxlength="200" class="form-control required"/>
			        <p></p>
			        <label><font color="red">*</font>申请类型：</label>
		        	<form:select path="applyType" class="form-control" style="width:185px;" onchange="selectType(this)">
						<form:option value="0">退货并退款</form:option>
						<form:option value="1">仅换货</form:option>
						<form:option value="2">仅退款</form:option>
					</form:select>
					<p></p>
					<form:hidden path="orderAmount"/>
			        <label><font color="red">*</font>支付金额：</label>
			        <form:input path="totalAmount" htmlEscape="false" maxlength="10" style="width: 180px;height:30px;" class="form-control" readonly="true"/>
					<p></p>
			        <label><font color="red">*</font>售后商品数量：</label>
			        <form:input path="returnNum" htmlEscape="false" maxlength="10" style="width: 180px;height:30px;" class="form-control"
			        onkeyup="this.value=this.value.replace(/[^\d.]/g,'')" 
					onpaste="this.value=this.value.replace(/[^\d.]/g,'')"
					onfocus="if(value == '0'){value=''}"
					onblur="if(value == ''){value='0'}"
			        onchange="returnChangeNum()"/>
					<p></p>
					<div id="returnAmountIsShow" style="display: display">
						<label><font color="red">*</font>售后金额：</label>
						<form:input path="returnAmount" htmlEscape="false" maxlength="10"  style="width:180px;" class="form-control required"
						onkeyup="this.value=this.value.replace(/[^\d.]/g,'')" 
						onpaste="this.value=this.value.replace(/[^\d.]/g,'')"
						onfocus="if(value == '0.0'){value=''}"
						onblur="if(value == ''){value='0.0'}"
						onchange="returnChangeAmount()"/><font color="red">（最大可退¥<span id="showReturn"></span>元）</font>
					</div>
					<p></p>
					<div id="returnTypeIsShow" style="display: display">
				        <label><font color="red">*</font>退款方式：</label>
						<form:select path="returnType" class="form-control" style="width:185px;" onchange="selectReturnType(this)">
							<form:options items="${fns:getDictList('returnType')}" itemLabel="label" itemValue="value" htmlEscape="false"/>
						</form:select><font color="red">所有退款方式仅用于记录!我们退款打款都是由人工手动操作,系统并没有自动打款功能!</font>
						<p></p>
						<div id="selectReceive" style="display: display">
							<label><font color="red">*</font>收&nbsp;&nbsp;款&nbsp;&nbsp;人：</label>
							<form:input path="receiveName" htmlEscape="false" maxlength="10" style="width: 180px;height:30px;" class="form-control"/>
				        	<p></p>
							<label><font color="red">*</font>收款账号：</label>
							<form:input path="receiveAccount" htmlEscape="false" maxlength="19" style="width: 180px;height:30px;" class="form-control"
							onkeyup="this.value=this.value.replace(/[^\d.]/g,'')" 
							onpaste="this.value=this.value.replace(/[^\d.]/g,'')"/>
						</div>
					</div>
			        <p></p>
					<label>客服备注：</label>
					<form:textarea path="remarks" htmlEscape="false" rows="3" maxlength="200" style="width:300px;" class="form-control"/>
				</form:form>
				<p></p>
				<font color="red">如需用户寄回所退商品,请选择"退货并退款"或者"仅换货";"仅退款"表示售后商品是不需要用户寄回所退商品</font>
			</div>
		</div>
	</div>
</div>
</body>
</html>