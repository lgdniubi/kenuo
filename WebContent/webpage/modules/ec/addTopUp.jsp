<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/webpage/include/taglib.jsp"%>
<%@ include file="/webpage/include/icheck.jsp"%>
<html>
<head>
	<title>充值</title>
	<meta name="decorator" content="default"/>
	<link rel="stylesheet" href="${ctxStatic}/train/css/exam.css">
	<style>
		#ichecks{display: inline-block;width: 17px;height: 17px;background: none;vertical-align: middle;margin-top: -1px;margin-right: 2px;}
	</style>
</head>
<body class="gray-bg">
	<div class="wrapper wrapper-content">
		<div class="ibox">
			<sys:message content="${message}"/>
	    	<div class="ibox-content">
				<div class="tab-content" id="tab-content">
	                <div class="tab-inner">
	                	<input type="hidden" id="userid" name="userid" value="${userid }" />
	                	<c:if test="${isReal == 1 }"> <!-- 0实物 -->
		                	<label class="active">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<font color="red">*</font>服务单次价：</label>
		                	${singleRealityPrice }<font color="red">&nbsp;&nbsp;充值金额要满足单次价的倍数！</font>
	                	</c:if>
	                	<p></p>
	                	<label class="active">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<font color="red">*</font>商品余额：</label>
	                	<input type="text" id="goodsBalance" value=${goodsBalance} name="goodsBalance" class="form-control required" style="width:150px;" readonly="readonly"/>
	                	<p></p>
						<label class="active">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<font color="red">*</font>充值金额：</label>
						<input type="text" id="rechargeAmount" value="0" name="rechargeAmount" class="form-control required" style="width:150px;"  value="0" onkeyup="this.value=this.value.replace(/[^\d.]/g,&quot;&quot;)" onpaste="this.value=this.value.replace(/[^\d.]/g,&quot;&quot;)" onfocus="if(value == '0')value=''" onblur="if(this.value == '')this.value='0';"/>
						<p></p>
						&nbsp;<input type="checkbox" id="ichecks" /><label class="active">账户余额：</label>
						<input type="text" id="accountBalance" name="accountBalance" style="width:150px;" value="0" class="form-control required" value="0" onkeyup="this.value=this.value.replace(/[^\d.]/g,&quot;&quot;)" onpaste="this.value=this.value.replace(/[^\d.]/g,&quot;&quot;)" onfocus="if(value == '0')value=''" onblur="if(this.value == '')this.value='0';"/>（可用余额：<label id="ye"></label>）
						<p></p>
						<label class="active">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<font color="red">*</font>总付款：</label>
						<input type="hidden" id="topUpTotalAmount" name="topUpTotalAmount" readonly="readonly" class="form-control required" style="width:150px;" />
						<input type="text" id="newTopUpTotalAmount" name="newTopUpTotalAmount" readonly="readonly" class="form-control required" style="width:150px;" />
						&nbsp;&nbsp;<a href="#" onclick="sum()">计算金额</a>
						<a href="#" onclick="update()">修改金额</a>
						<input type="hidden" name="jsmoney" id="jsmoney" />
					</div>
				</div>
			</div>
		</div>
	</div>
	<div class="loading" id="loading"></div>
	<script type="text/javascript">
		function sum(){
			var rechargeAmount = parseFloat($("#rechargeAmount").val());
			var accountBalance = parseFloat($("#accountBalance").val());
			var goodsBalance = parseFloat($("#goodsBalance").val());
			var topUpTotalAmount = (rechargeAmount+accountBalance).toFixed(2);
			var newTopUpTotalAmount = (rechargeAmount+accountBalance+goodsBalance).toFixed(2);
			
			$("#rechargeAmount").attr("readonly",true);
			$("#accountBalance").attr("readonly",true);
			$("#topUpTotalAmount").val(topUpTotalAmount);
			$("#newTopUpTotalAmount").val(newTopUpTotalAmount);
			$("#jsmoney").val(1); //计算过费用
			$("#ichecks").attr("disabled",true);
		}
		function update(){
			$("#rechargeAmount").removeAttr("readonly");
			$("#accountBalance").removeAttr("readonly");
			$("#topUpTotalAmount").val("");
			$("#newTopUpTotalAmount").val("");
			$("#jsmoney").val(0); //未计算过
			$("#ichecks").attr("disabled",false);
		}
		$(document).ready(function() {
			$("#ichecks").change(function(){
				if($(this).is(":checked")){
					var userid = $("#userid").val();
					$.ajax({
						type:"post",
						data:{
							userid:userid
						 },
						url:"${ctx}/ec/orders/getAccount",
						success:function(date){
							$("#ye").html(date);
							$("#topUpTotalAmount").val("");
							$("#newTopUpTotalAmount").val("");
						},
						error:function(XMLHttpRequest,textStatus,errorThrown){}
					}); 
				}else{
					$("#accountBalance").val(0);
				}
			});
		});
	</script>
</body>
</html>