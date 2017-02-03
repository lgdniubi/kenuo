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
			var mtmyMobile = $("#mtmyMobile").val();
			if(mtmyMobile == ""){
				return;
			}
			$.ajax({
				type:"get",
				dataType:"json",
				data:{
					mobile:mtmyMobile
				},
				url:"${ctx}/ec/orders/getUser",
				success:function(date){
					$("#mtmyUserName").val(date.users.name);
					$("#mtmyUserMobile").val(date.users.mobile);
					$("#mtmyUserId").val(date.users.userid);
					$("#mtmyMobile").val(date.users.mobile);
				},
				error:function(XMLHttpRequest,textStatus,errorThrown){
				}
			});
		}
		
		function empty(){
			$("#mtmyMobile").val("");
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
	                	<label class="active">备注信息：</label>
	                	<textarea rows="10" cols="65" id="remark" name="remark" maxlength="250"></textarea>
					</div>
				</div>
			</div>
		</div>
	</div>
	<div class="loading"></div>
</body>
</html>