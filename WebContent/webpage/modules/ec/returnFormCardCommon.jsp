<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/webpage/include/taglib.jsp"%>

<html>
<head>
	<title>退货录入</title>
	<meta name="decorator" content="default"/>
	<!-- 内容上传 引用-->

<script type="text/javascript">
		var validateForm;
		var goodsNum = 0;//实物售后数量校验用
		var remaintimes = 0;//剩余次数
		var totalAmount=0;//实付款
		var orderArrearage=0;//订单欠款
		var advanceFlag;//判断是否为预约金
		var flagNum = false;//实物售后数量校验用
		var flag = false;//校验通用卡子项实物输入的数量是否都为0
		var j = 0;//校验通用卡子项实物输入的数量是否都为0
		
		function doSubmit(){//回调函数，在编辑和保存动作时，供openDialog调用提交表单。
		  if(validateForm.form()){
			  var recid=$("#goodsMappingId").val();
			  if(recid==""){
				  top.layer.alert('请选择退货商品!', {icon: 0, title:'提醒'});
				  return;
			  }
			  
			  //有预约金的,都需要做处理
			  if(advanceFlag == 1){
				  top.layer.alert('当前订单有预约金,无法退款(请先处理预约金)', {icon: 0, title:'提醒'});
				  return;
			  }
			  
			  //校验通用卡子项实物售后数量的准确
			  for(var i=0;i<goodsNum;i++){
				  var newNum = $("#returnNums"+i).val();
				  var oldNum = $("#oldreturnNums"+i).val();
				  if(parseInt(newNum) < 0){
					  flagNum = true;
				  }else if(parseInt(newNum) > parseInt(oldNum)){
					  flagNum = true;
				  }else{
					  flagNum = false;
				  }
				  if(flagNum){
					  top.layer.alert('售后数量必须大于等于0，小于等于购买数量!', {icon: 0, title:'提醒'});
					  return;
				  }
				  //当实物售后数量=0,做记录
				  if(parseInt(newNum) == 0){
					  j++;
				  }
				  if(j == goodsNum){
					  flag = true;
				  }
			  }
			  
			  //校验申请类型
			  var type=$("#applyType").val();
			  if(type == 1){//仅换货展示 '实物数量'和'售后次数'   售后次数不能为0
				  //校验售后次数
				  var rn=$("#returnNum").val();
				  if(parseInt(rn)<0){
					  top.layer.alert('售后次数必须大于等于0，小于等于可售后数量!', {icon: 0, title:'提醒'});
					  return;
				  }else if(parseInt(rn)>parseInt(remaintimes)){
					  top.layer.alert('售后次数必须大于等于0，小于等于可售后数量!', {icon: 0, title:'提醒'});
					  return;
				  }
				  
				  if(flag){
					  if(parseInt(rn) ==0){
						  top.layer.alert('实物商品数量和售后次数不能都为0!', {icon: 0, title:'提醒'});
						  return;			  
					  }
				  }
				  //仅换货:收款人和收款账户默认空,且
				  $("#returnType").val(0);//清空收款人信息
				  $("#receiveName").val("");//清空收款人信息
				  $("#receiveAccount").val("");//清空收款账号
			  }else{//退货并退款     仅退款
				  //校验售后次数
				  var rn=$("#returnNum").val();
				  if(parseInt(rn)<0){
					  top.layer.alert('售后次数必须大于等于0，小于等于可售后数量!', {icon: 0, title:'提醒'});
					  return;
				  }else if(parseInt(rn)>parseInt(remaintimes)){
					  top.layer.alert('售后次数必须大于等于0，小于等于可售后数量!', {icon: 0, title:'提醒'});
					  return;
				  }
				  
				  //虚拟商品的退款金额校验
				  var ra=$("#returnAmount").val();
				  if(parseFloat(ra)<0){
					  top.layer.alert('售后金额必须大于等于0，小于等于可售后金额!', {icon: 0, title:'提醒'});
					  return;
				  }else if(parseFloat(surplusReturnAmount) < parseFloat(ra)){
					  top.layer.alert('售后金额必须大于等于0，小于等于可售后金额!', {icon: 0, title:'提醒'});
					  return;
				  }
				  
				  //通用卡子项实物售后数量,售后次数,售后金额都是0,不能售后
				  if(flag){
					  if(parseFloat(ra) == 0 && parseInt(rn) ==0){
						  top.layer.alert('售后商品数量、售后次数和售后金额不能都为0!', {icon: 0, title:'提醒'});
						  return;			  
					  }
				  }
				  
				  //退款方式是"银行卡账户"需要填写收款人和收款方式
				  var returnType=$("#returnType").val();
				  if(returnType == 4){
					  var receiveName = $("#receiveName").val();//收款人	
					  var receiveAccount = $("#receiveAccount").val();//收款账户	
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
			  $("#orderArrearage").val(orderArrearage);//存在欠款,欠款金额存入
			  $("#inputForm").submit();
			  return true;
		  }
		  return false;
		}
		
		//选中某个商品时,查询数据
		function selectFunction(o){
			var groupId;
			var recid=$("#selectId:checked").val();
			var orderId=$("#orderId").val();//订单id
			var orderAmount=$("#"+recid+"orderAmount").val();//应付款
			orderArrearage=$("#"+recid+"orderArrearage").val();//订单欠款
			advanceFlag=$("#"+recid+"advanceFlag").val();//是否预约金
			remaintimes=$("#"+recid+"remaintimes").val();//剩余次数
			totalAmount=$("#"+recid+"totalAmount").val();//实付款
			$("#goodsMappingId").val(recid);//为goodsMappingId赋值
			$("#returnNum").val(remaintimes);//最大可售后次数赋值
			$("#orderAmount").val(orderAmount);
			$("#totalAmount").val(totalAmount);
			//有预约金的,都需要做处理
			if(advanceFlag == 1){
				  top.layer.alert('当前订单有预约金,无法退款(请先处理预约金)', {icon: 0, title:'提醒'});
				  return;
			}
			
			//存在实物,添加实物的售后数量,不存在:不显示
			$("#addReal").empty();
			$.ajax({
				type:"post",
				async:false,
				data:{
					recid:recid,
					orderid:orderId
				},
				url:"${ctx}/ec/orders/getCardRealNum",
				success:function(date){
					if(date!=null && date!=""){
						for(var i in date){
							if(date[i].isreal == 0){
								$("<label><font color='red'>*</font>"+date[i].goodsname+"    售后数量：</label><input id='recIds' name='recIds' value='"+date[i].recid+"' type='hidden'/><input id='returnNums"+goodsNum+"' name='returnNums' value='"+date[i].goodsnum+"' style='width:180px;' class='form-control required' onblur='findReturnNum(this,"+date[i].goodsnum+")'/><input id='oldreturnNums"+goodsNum+"' value='"+date[i].goodsnum+"' type='hidden'/> <p></p>").appendTo($("#addReal"));
								goodsNum++;
							}
						}
					}
				},
				error:function(XMLHttpRequest,textStatus,errorThrown){
							    
				}
			});
			
			//售后金额 <= 实付金额-已售后
			$.ajax({
				type:"post",
				async:false,
				data:{
					goodsMappingId:recid,
					orderId:orderId
				 },
				url:"${ctx}/ec/returned/getSurplusReturnAmount",
				success:function(obj){
					//计算商品剩余可退款金额
					surplusReturnAmount = parseFloat(totalAmount*100-obj*100)/100;
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
				$("#returnAmountIsShow").hide();//退款金额
				$("#returnTypeIsShow").hide();//退款方式
			}else {
				$("#returnAmountIsShow").show();//退款金额
				$("#returnTypeIsShow").show();//退款方式
			}
		}
		//选择退款方式,只有选择"银行卡账户",需要填写收款人和收款账号
		function selectReturnType(o){
			var type=$("#returnType").val();
			if(type==4){
				$("#selectReceive").show();
			}else if(type!=4){
				$("#selectReceive").hide();
			}
		}
		
		//实物售后数量校验
		function findReturnNum (id,num){
			var num1=$(id).val();
			if(parseInt(num1)<0){
				top.layer.alert('售后数量必须大于等于0，小于等于购买数量!', {icon: 0, title:'提醒'});
				return;
			}else if(parseInt(num1) > parseInt(num)){
				top.layer.alert('售后数量必须大于等于0，小于等于购买数量!', {icon: 0, title:'提醒'});
				return;
			}
		}
		//虚拟商品的售后次数校验
		function returnChangeTimes(){
			var rn=$("#returnNum").val();
			if(parseInt(rn)<0){
				top.layer.alert('售后次数必须大于等于0，小于等于售后次数!', {icon: 0, title:'提醒'});
				return;
			}else if(parseInt(remaintimes) < parseInt(rn)){
				top.layer.alert('售后次数必须大于等于0，小于等于售后次数!', {icon: 0, title:'提醒'});
				return;
			}
		}
		//虚拟商品的退款金额校验
		function returnChangeAmount(){
			var ra=$("#returnAmount").val();
			if(parseFloat(ra)<0){
				top.layer.alert('退款金额必须大于等于0，小于等于支付金额!', {icon: 0, title:'提醒'});
				return;
			}else if(parseFloat(surplusReturnAmount) < parseFloat(ra)){
				top.layer.alert('退款金额必须大于等于0，小于等于支付金额!', {icon: 0, title:'提醒'});
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
			$("#selectReceive").hide();//收款人和收款账户首先隐藏
		});
</script>
</head>
<body>
<div class="wrapper wrapper-content">
	<div class="ibox">
		<div class="ibox-content">
			<div class="clearfix">
				<form:form id="inputForm" modelAttribute="returnedGoods" action="${ctx}/ec/orders/saveReturnCommon" method="post" class="form-horizontal">
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
								<th style="text-align: center;">商品类型</th>
								<th style="text-align: center;">商品规格</th>
								<th style="text-align: center;">子项名称</th>
								<th style="text-align: center;">子项类型</th>
								<th style="text-align: center;">市场价</th>
								<th style="text-align: center;">优惠价</th>
								<th style="text-align: center;">异价比例</th>
								<th style="text-align: center;">异价后价格</th>
								<th style="text-align: center;">成本价</th>
								<th style="text-align: center;">购买数量</th>
								<th style="text-align: center;">应付金额</th>
								<th style="text-align: center;">实付金额</th>
								<th style="text-align: center;">剩余次数</th>
								<th style="text-align: center;">欠款</th>
							</tr>
						</thead>
						<tbody>
							${suitCardSons}
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
					<div id="addReal"></div>
					<p></p>
					<form:hidden path="orderAmount"/>
			        <label><font color="red">*</font>支付金额：</label>
			        <form:input path="totalAmount" htmlEscape="false" maxlength="10" style="width: 180px;height:30px;" class="form-control" readonly="true"/>
					<p></p>
					<label><font color="red">*</font>售后次数：</label>
		        	<form:input path="returnNum" htmlEscape="false" maxlength="10" style="width: 180px;height:30px;" class="form-control" 
		        	onkeyup="this.value=this.value.replace(/[^\d.]/g,'')" 
					onpaste="this.value=this.value.replace(/[^\d.]/g,'')"
					onfocus="if(value == '0'){value=''}"
					onblur="if(value == ''){value='0'}"
		        	onchange="returnChangeTimes()"/>
					<p></p>
					<div id="returnAmountIsShow" style="display: display">
						<label><font color="red">*</font>退款金额：</label>
						<form:input path="returnAmount" htmlEscape="false" maxlength="10"  style="width:180px;" class="form-control required"
						onkeyup="this.value=this.value.replace(/[^\d.]/g,'')" 
						onpaste="this.value=this.value.replace(/[^\d.]/g,'')"
						onfocus="if(value == '0.0'){value=''}"
						onblur="if(value == ''){value='0.0'}"
						onchange="returnChangeAmount()"/><font color="red" id="changeIsShow">（最大可退¥<span id="showReturn"></span>元）</font>
						<p></p>
					</div>
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