<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/webpage/include/taglib.jsp"%>
<%@ include file="/webpage/include/icheck.jsp"%>
<html>
<head>
	<title>处理预约金</title>
	<meta name="decorator" content="default"/>
	<link rel="stylesheet" href="${ctxStatic}/train/css/exam.css">
	<style>
		#ichecks{display: inline-block;width: 17px;height: 17px;background: none;vertical-align: middle;margin-top: -1px;margin-right: 2px;}
	</style>
	<script type="text/javascript">
	$(document).ready(function(){
		var accountBalance = $("#accountBalance").val();
		var debt = $("#debt").val();
		var advance = $("#advance").val();
		var singleRealityPrice = $("#singleRealityPrice").val();
		//账户余额小于欠款，则无法使用账户的余额
		if(accountBalance - debt < 0){
			$("#ichecks").attr("disabled",true);
		}
		//若订金大于等于服务单次价，则也就不使用账户的余额
		if(advance - singleRealityPrice >= 0){
			$("#ichecks").attr("disabled",true);
		}
	}); 
	
	function sum(){
		if($("input[type='checkbox']").is(':checked')){
			$("#sum").val(0);
		}else{
			$("#sum").val(1);
		}
	}
	</script>
</head>
<body class="gray-bg">
	<div class="wrapper wrapper-content">
		<div class="ibox">
			<sys:message content="${message}"/>
	    	<div class="ibox-content">
				<div class="tab-content" id="tab-content">
	                <div class="tab-inner">
	                	<p></p>
						<label class="active">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;预约金：</label>
						<input type="text" id="advance" name="advance" readonly="readonly" value="${orderGoods.advance}" class="form-control required" style="width:150px;"  />
						<c:if test="${servicetimes != 999}">
							<p></p>
							<label class="active">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;服务单次价：</label>
							<input type="text" id="singleRealityPrice" name="singleRealityPrice" readonly="readonly" value="${orderGoods.singleRealityPrice}" class="form-control required" style="width:150px;"  />
							<p></p>
							<label class="active">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;服务次数：</label>
							<input type="text" id="advanceServiceTimes" name="advanceServiceTimes" readonly="readonly" value="${orderGoods.advanceServiceTimes}" class="form-control required" style="width:150px;"  />	
						</c:if>
						<p></p>
						<c:if test="${orderGoods.advanceServiceTimes == 0}">
							<label class="active">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;欠款：</label>
							<input type="text" id="debt" name="debt" readonly="readonly" value="${orderGoods.debt}" class="form-control required" style="width:150px;"  />
							<p></p>
							&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<input type="checkbox" id="ichecks" onchange="sum()" name="ichecks"/><label class="active">是否使用</label>
							（账户可用余额：${orderGoods.accountBalance}）
							<input type="hidden" id="accountBalance" name="accountBalance" value="${orderGoods.accountBalance}" class="form-control required" style="width:150px;"  />
						</c:if>
						<input type="hidden" id="sum" name="sum" value="1"/>
						<c:if test="${orderGoods.advanceServiceTimes != 0}">
							<label class="active">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;余额：</label>
							<input type="text" id="advanceBalance" name="advanceBalance" readonly="readonly" value="${orderGoods.advanceBalance}" class="form-control required" style="width:150px;"  />
						</c:if>
					</div>
				</div>
			</div>
		</div>
	</div>
	<div class="loading"></div>
</body>
</html>