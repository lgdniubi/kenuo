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
		var returnNum;//商品原始的售后数量
		var returnAmount;//商品原始的售后金额
		var goodsNum;//商品可售后数量
		var amount;//商品的可售后金额
		var applyType;//申请类型
		function doSubmit(){//回调函数，在编辑和保存动作时，供openDialog调用提交表单。
		  if(validateForm.form()){
			  //判断是同意还是拒绝,需要清空数据
			  var isConfirm = $("#isConfirm:checked").val();
			  if(isConfirm == 12 || isConfirm == 22){//同意,清空拒绝售后原因
				  $("#refusalCause").val("");
			  }else{//拒绝,清空仓库信息
				  $("#warehouseId").val(0);
			  }
			  returnNum = $("#returnNum").val();//售后次数
			  returnAmount = $("#returnAmount").val();//售后金额
			  if(applyType==1){//仅换货  换货数量不能为0.
				  if(parseInt(returnNum) <= 0){
					  top.layer.alert('售后次数必须大于0，小于等于可售后数量!', {icon: 0, title:'提醒'});
					  return;
				  }else if(parseInt(goodsNum)<parseInt(returnNum)){
					  top.layer.alert('售后次数必须大于0，小于等于可售后数量!', {icon: 0, title:'提醒'});
					  return;
				  }
			  }else{//退货并退款和仅退款   售后数量和售后金额不能为0.
				  //校验售后次数
				  if(parseInt(returnNum)<0){
					  top.layer.alert('售后次数必须大于等于0，小于等于可售后次数!', {icon: 0, title:'提醒'});
					  return;
				  }else if(parseInt(goodsNum)<parseInt(returnNum)){
					  top.layer.alert('售后次数必须大于等于0，小于等于可售后次数!', {icon: 0, title:'提醒'});
					  return;
				  }
				  //校验退款金额
				  if(parseFloat(returnAmount)<0){
					  top.layer.alert('退款金额必须大于等于0，小于等于可售后金额!', {icon: 0, title:'提醒'});
					  return;
				  }else if(parseFloat(amount) < parseFloat(returnAmount)){
					  top.layer.alert('退款金额必须大于等于0，小于等于可售后金额!', {icon: 0, title:'提醒'});
					  return;
				  }
   			  	  //当售后数量和售后金额都为0,不能审核通过
				  if(parseInt(returnNum) == 0 && parseFloat(returnAmount) == 0){
					  top.layer.alert('售后次数和退款金额不能都为0!', {icon: 0, title:'提醒'});
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
			  $("#inputForm").submit();
			  return true;
		  }
	
		  return false;
		}
		//同意时,显示地址,隐藏原因
		function seletAddreess(){
			applyType="${returnedGoods.applyType}";
			if(applyType == 2){
				$("#refusal").hide();
				$("#addreess").hide();
			}else{
				$("#refusal").hide();
				$("#addreess").show();
			}
		}
		//拒绝时,隐藏地址,显示原因
		function shuoRefusal(){
			$("#addreess").hide();
			$("#refusal").show();
		}
		//售后次数校验
		function returnChangeNum(){
			returnNum=$("#returnNum").val();
			goodsNum = $("#goodsNum").val();//商品的可售后数量
			if(parseInt(returnNum)<0){
				top.layer.alert('售后次数必须大于等于0，小于等于可申请的售后次数!', {icon: 0, title:'提醒'});
				return;
			}else if(parseInt(goodsNum)<parseInt(returnNum)){
				top.layer.alert('售后次数必须大于等于0，小于等于可申请的售后次数!', {icon: 0, title:'提醒'});
				return;
			}
		}
		//退款金额校验
		function returnChangeAmount(){
			returnAmount=$("#returnAmount").val();
			amount = $("#amount").val();//商品的可售后金额
			if(parseFloat(returnAmount)<0){
				top.layer.alert('退款金额必须大于等于0，小于等于可售后金额!', {icon: 0, title:'提醒'});
				return;
			}else if(parseFloat(amount)<parseFloat(returnAmount)){
				top.layer.alert('退款金额必须大于等于0，小于等于可售后金额!', {icon: 0, title:'提醒'});
				return;
			}
		}
		$(document).ready(function(){
			jq("a[class='img']").imgbox({
				'speedIn'		: 0,
				'speedOut'		: 0,
				'alignment'		: 'center',
				'overlayShow'	: true,
				'allowMultiple'	: false
			});
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
			applyType = "${returnedGoods.applyType}";//申请类型
			if(applyType == 1){//仅换货
				$("#returnNum").attr("readonly",false);//当仅换货,数量时可以输入的,即使存在欠款
				$("#returnAmountIsShow").hide();
				$("#returnTypeIsShow").hide();
			}
			var returnType = $("#returnType").val();
			if(returnType == 4){
				$("#selectReceive").show();
			}else{
				$("#selectReceive").hide();
			}
			var oldReturnNum = "${returnedGoods.returnNum}";
			$("#oldReturnNum").val(oldReturnNum)//记录原始的售后次数
		});
		//选择退款方式,只有选择"银行卡账户",需要填写收款人和收款账号
		function selectReturnType(o){
			var type=$("#returnType").val();
			if(type==4){
				$("#selectReceive").show();
			}else if(type!=4){
				$("#selectReceive").hide();
			}
		}
</script>
</head>
<body>
	<div class="wrapper wrapper-content">
		<div class="ibox">
			<div class="ibox-content">
				<div class="clearfix">
				<form:form id="inputForm" modelAttribute="returnedGoods" action="${ctx}/ec/returned/saveReturned" method="post" class="form-horizontal">
					<input type="hidden" id="amount" value="${amount}"/>
					<input type="hidden" id="goodsNum" value="${times}"/>
					<input type="hidden" id="oldReturnNum" name="oldReturnNum"/>
					<form:hidden path="id"/>
			        <label>退货单号：</label><label>${returnedGoods.id}</label>&nbsp;&nbsp;
			        <label>原订单号：</label><label>${returnedGoods.orderId}</label>
			   		<p></p>
			      	<label>用户名：</label><label>${returnedGoods.userName}</label>&nbsp;&nbsp;
			        <label>手机号：</label><label>${returnedGoods.mobile}</label>
			        <table class="table table-bordered  table-condensed dataTables-example dataTable no-footer">
			        	<thead>
			        		<tr>
			        			<th style="text-align: center;">商品名称</th>
			        			<th style="text-align: center;">商品类型</th>
								<th style="text-align: center;">规格</th>
								<th style="text-align: center;">价格</th>
								<th style="text-align: center;">数量</th>
								<th style="text-align: center;">实付金额</th>
			        		</tr>
			        	</thead>
			        	<tbody>
			        		<tr style="text-align: center;">
			        			<td>${returnedGoods.goodsName}</td>
			        			<td>
			        				<c:if test="${returnedGoods.isReal==0}">
			        					实物
			        				</c:if>
			        				<c:if test="${returnedGoods.isReal==1}">
			        					虚拟
			        				</c:if>
			        			</td>
			        			<td>${returnedGoods.specName}</td>
			        			<td>${returnedGoods.goodsPrice}</td>
			        			<td>${returnedGoods.goodsNum}</td>
			        			<td>${returnedGoods.totalAmount}</td>
			        		</tr>
			        	</tbody>
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
			        <label>申请类型：</label>
			        <label>
				        <c:if test="${returnedGoods.applyType==0}">
							退货并退款
						</c:if>		
						<c:if test="${returnedGoods.applyType==1}">
							仅换货
						</c:if>
						<c:if test="${returnedGoods.applyType==2}">
							仅退款
						</c:if>
					</label>
					<p></p>
					<p></p>
					<label>入库状态：</label>
					<label>
						<c:if test="${returnedGoods.isStorage==0}">
							未入库
						</c:if>		
						<c:if test="${returnedGoods.isStorage==1}">
							已入库
						</c:if>
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
					<p></p>
					<label>支付金额：</label>
			        <form:input path="totalAmount" htmlEscape="false" maxlength="10" style="width: 180px;height:30px;" class="form-control" readonly="true"/>
					<p></p>
					<c:if test="${serviceTimes != 999}">
				        <label>售后商品次数：</label>
       					<form:input path="returnNum" htmlEscape="false" maxlength="10" style="width: 180px;height:30px;" class="form-control"
					        onkeyup="this.value=this.value.replace(/[^\d.]/g,'')" 
							onpaste="this.value=this.value.replace(/[^\d.]/g,'')"
							onfocus="if(value == '0'){value=''}"
							onblur="if(value == ''){value='0'}"
					        onchange="returnChangeNum()"/>
       				</c:if>
					<p></p>
					<div id="returnAmountIsShow" style="display: display">
						<label>退款金额：</label>
				        <form:input path="returnAmount" htmlEscape="false" maxlength="10"  style="width:180px;" class="form-control required"
							onkeyup="this.value=this.value.replace(/[^\d.]/g,'')" 
							onpaste="this.value=this.value.replace(/[^\d.]/g,'')"
							onfocus="if(value == '0.0'){value=''}"
							onblur="if(value == ''){value='0.0'}"
							onchange="returnChangeAmount()"/>
				        <p></p>
				    </div>
				    <div id="returnTypeIsShow" style="display: display">
				        <label>退款方式：</label>
			        	<form:select path="returnType" class="form-control" style="width:185px;" onchange="selectReturnType(this)">
							<form:option value="0">原路退回</form:option>
							<form:option value="1">现金</form:option>
							<form:option value="2">微信</form:option>
							<form:option value="3">支付宝</form:option>
							<form:option value="4">银行卡账户</form:option>
							<form:option value="5">充值到每天美耶账户</form:option>
						</form:select>
						<p></p>
						<div id="selectReceive" style="display: display">
							<label>收&nbsp;&nbsp;款&nbsp;&nbsp;人：</label>
							<form:input path="receiveName" htmlEscape="false" maxlength="10" style="width: 180px;height:30px;" class="form-control"/>
				        	<p></p>
							<label>收款账号：</label>
							<form:input path="receiveAccount" htmlEscape="false" maxlength="19" style="width: 180px;height:30px;" class="form-control"
							onkeyup="this.value=this.value.replace(/[^\d.]/g,'')" 
							onpaste="this.value=this.value.replace(/[^\d.]/g,'')"/>
						</div>
						<p></p>
					</div>
					<c:if test="${flag == 'edit'}">
				        <c:if test="${returnedGoods.returnStatus==11}">
				        		<label>是否同意申请：</label>
								<label><input id="isConfirm" name="isConfirm" type="radio" value="12"  class="form required" onclick="seletAddreess()"/>同意退货 </label>
								<label><input id="isConfirm" name="isConfirm" type="radio" value="-10"  class="form required"  onclick="shuoRefusal()"/>拒绝退货</label>
						</c:if> 
						<c:if test="${returnedGoods.returnStatus==21}">
								<label>是否同意申请：</label>
								<label><input id="isConfirm" name="isConfirm" type="radio" value="22"  class="form required" onclick="seletAddreess()"/>同意换货</label>
								<label><input id="isConfirm" name="isConfirm" type="radio" value="-20"  class="form required"  onclick="shuoRefusal()"/>拒绝换货</label>
						</c:if>
					</c:if>
					<div id="refusal" style="display:none;">
						<label>拒绝原因：</label>
						<form:textarea path="refusalCause" htmlEscape="false" rows="3" maxlength="30" style="width:300px;" class="form-control required"/>
						<label><font color="red">如果拒绝请说明原因</font></label>
					</div>
					<div id="addreess" style="display:none;">
						<hr>
						<label>仓库地址选择：</label>
						<table class="table table-bordered  table-condensed dataTables-example dataTable no-footer">
							<thead>
				        		<tr>
				        			<th>选择</th>
				        			<th>仓库名称</th>
									<th>管理员</th>
									<th>地址</th>
				        		</tr>
				        	</thead>
							<tbody>
								<c:forEach items="${pdlist}" var="pdlist">
									<tr>
										<td><input id="warehouseId" name="warehouseId" type="radio" value="${pdlist.wareHouseId}"  class="form required" /></td>
										<td>${pdlist.name}</td>
										<td>${pdlist.governor}</td>
										<td>${pdlist.address}</td>
									</tr>
								</c:forEach>
							</tbody>
						</table>			
					</div>
			        <p></p>
			        <label>客服备注：</label>
			        <form:textarea path="remarks" htmlEscape="false" rows="3" maxlength="200" style="width:300px;" class="form-control"/>
			        <p></p>
			        <c:if test="${returnedGoods.returnStatus > 11 and returnedGoods.returnStatus != 21}">
						<hr>
						<label>退货仓库地址：</label>
						<table class="table table-bordered  table-condensed dataTables-example dataTable no-footer">
							<thead>
				        		<tr>
				        			<th style="text-align: center;">仓库名称</th>
									<th style="text-align: center;">管理员</th>
									<th style="text-align: center;">地址</th>
				        		</tr>
				        	</thead>
							<tbody>
								<tr>
									<td style="text-align: center;">${returnedGoods.hoseName}</td>
									<td style="text-align: center;">${returnedGoods.governor}</td>
									<td style="text-align: center;">${returnedGoods.address}</td>
								</tr>
							</tbody>
						</table>
					</c:if>
					<c:if test="${returnedGoods.returnStatus > 12 and returnedGoods.returnStatus != 21 and returnedGoods.returnStatus != 22}">
						<hr>
						<label>用户退货物流信息：</label>
						<table class="table table-bordered  table-condensed dataTables-example dataTable no-footer">
							<thead>
				        		<tr>
				        			<th style="text-align: center;">发货人</th>
									<th style="text-align: center;">发货地址</th>
									<th style="text-align: center;">快递公司</th>
									<th style="text-align: center;">快递单号</th>
									<th style="text-align: center;">发货时间</th>
									<th style="text-align: center;">发货物流单图片</th>
				        		</tr>
				        	</thead>
							<tbody>
								<tr>
									<td style="text-align: center;">${returnedGoods.consignerName}</td>
									<td style="text-align: center;">${returnedGoods.consignerAddress}</td>
									<td style="text-align: center;">${returnedGoods.consignerShippingName}</td>
									<td style="text-align: center;">${returnedGoods.consignerShippingCode}</td>
									<td style="text-align: center;"><fmt:formatDate value="${returnedGoods.consignerShippingTime}" pattern="yyyy-MM-dd HH:mm:ss" /></td>
									<td style="text-align: center;"><img alt="退货物流单号" src=" ${returnedGoods.consignerShippingImg}" style="width: 180px;height:100px;"></td>
								</tr>
							</tbody>
						</table>
					</c:if>
				</form:form>
				</div>
			</div>
		</div>
	</div>
</body>
</html>