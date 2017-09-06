<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/webpage/include/taglib.jsp"%>
<html>
<head>
	<title>提成人员查询页面</title>
	<meta name="decorator" content="default"/>
	<link rel="stylesheet" href="${ctxStatic}/train/css/exam.css">
	<script type="text/javascript">
		function sum(){
			var rechargeAmount = parseFloat($("#rechargeAmount").val());
			var  accountBalance = parseFloat($("#accountBalance").val());
			var  topUpTotalAmount = (rechargeAmount+accountBalance).toFixed(2);
			$("#topUpTotalAmount").val(topUpTotalAmount);
		}
		
		function selectUser(){
			
			$("#sysName").val("");
			$("#sysMobile").val("");
			$("#sysUserId").val("");
			$("#pushMoney").val("");
			
			var searchMobile = $("#searchMobile").val();
			if(searchMobile == ""){
				return;
			}
			$.ajax({
				type:"get",
				dataType:"json",
				data:{
					mobile:searchMobile
				},
				url:"${ctx}/ec/orders/getSysUser",
				success:function(date){
					$("#sysName").val(date.user.name);
					$("#sysMobile").val(date.user.mobile);
					$("#sysUserId").val(date.user.id);
					$("#searchMobile").val(date.user.mobile);
				},
				error:function(XMLHttpRequest,textStatus,errorThrown){
					top.layer.alert('请确定手机号的正确性以及该手机号是否属于妃子校', {icon: 0, title:'提醒'});
				}
			});
		}
		
		function empty(){
			$("#searchMobile").val("");
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
		                	<label class="active"><font color="red">*</font>业务员手机号：</label>
		                	<input type="text" id="searchMobile" name="searchMobile" class="form-control required" />
		                	<div class="pull-right">
			                	<button  class="btn btn-primary btn-rounded btn-outline btn-sm " onclick="selectUser()" ><i class="fa fa-search"></i> 查询</button>
			                	<button  class="btn btn-primary btn-rounded btn-outline btn-sm " onclick="empty()" ><i class="fa fa-refresh"></i> 重置</button>
		                	</div>
		                	<p></p>
		                	<label class="active">业务员姓名：</label>
		                	<input id="sysName" name="sysName" class="form-control" readonly="readonly" type="text" value="" aria-required="true">
		                	<input id="sysUserId" name="sysUserId" class="form-control" readonly="readonly" type="hidden" value="" aria-required="true">
		                	<p></p>
		                	<label class="active">业务员手机：</label>
		                	<input id="sysMobile" name="sysMobile" class="form-control" readonly="readonly" type="text" value="" aria-required="true">
		                	<p></p>
		                	<label class="active"><font color="red">*</font>提成金额：</label>
		                	<input id="pushMoney" name="pushMoney" class="form-control" type="text" value="" aria-required="true">
					</div>
				</div>
			</div>
		</div>
	</div>
	<div class="loading"></div>
</body>
</html>