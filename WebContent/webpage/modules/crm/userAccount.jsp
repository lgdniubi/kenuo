<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/webpage/include/taglib.jsp"%>
<html>
<head>
	<title>会员管理</title>
	<meta name="decorator" content="default"/>
    <meta name="viewport" content="width=device-width, initial-scale=1.0, minimum-scale=1.0, maximum-scale=1.0,user-scalable=no">
		<link href="${ctxStatic}/layer-v2.0/layer/skin/layui.css" type="text/css" rel="stylesheet">
	
	<script type="text/javascript">
// 	    function edit(){
// 	    	$("userid").removeAttr("disabled"); 
// 	    	$("sex").attr("disabled",false);
// 	    	$("userid").removeAttr("disabled")
// 	    }
	    function save(){
	    	$("#inputForm").submit();
			return true;
	    }
	    $('#edit').click(function() {
			if($('#fieldset').is(':disabled')) {
				$('#fieldset').removeAttr('disabled');
			}
		});
	    function couponList(){
			openDialog("查看"+'用户可用红包',"/kenuo/a/crm/user/couponList?userId=${userId}","800px", "650px","");
		}
	    
	</script>
	<style type="text/css">
		.modal-content{
			margin:0 auto;
			width: 400px;
		}
	</style>
</head>
<body class="gray-bg">
	<div class="wrapper wrapper-content">
		<div class="ibox">
			<sys:message content="${message}"/>
			<!-- 查询条件 -->
	    	<div class="ibox-content">
				<div class="clearfix">
					<div class="nav" >
						<div class="text-danger" style="margin:8px">
							<p class="text-primary">
								<span >${userDetail.nickname}</span>的客户档案--请注意保密
							</p>
						</div>	
				        <ul class="layui-tab-title">
							<li role="presentation">
								<a href="${ctx}/crm/user/userDetail?userId=${userId}&franchiseeId=${franchiseeId}">基本资料</a>
							</li>
							<li role="presentation">
								<a href="${ctx}/crm/physical/skin?userId=${userId}&franchiseeId=${franchiseeId}}">身体状况</a>
							</li>
							<li role="presentation">
								<a href="${ctx}/crm/schedule/list?userId=${userId}&franchiseeId=${franchiseeId}">护理时间表</a>
							</li>
							<li role="presentation">
								<a href="${ctx}/crm/orders/list?userId=${userId}&franchiseeId=${franchiseeId}">客户订单</a>
							</li>
							<li role="presentation">
								<a href="${ctx}/crm/coustomerService/list?userId=${userId}&franchiseeId=${franchiseeId}">售后</a>
							</li>
							<li role="presentation">
								<a href="${ctx}/crm/consign/list?userId=${userId}&franchiseeId=${franchiseeId}">物品寄存</a>
							</li>
							<li role="presentation">
								<a href="${ctx}/crm/goodsUsage/list?userId=${userId}&franchiseeId=${franchiseeId}">产品使用记录</a>
							</li>
							<li role="presentation" class="layui-this">
								<a href="${ctx}/crm/user/account?userId=${userId}&franchiseeId=${franchiseeId}">账户总览</a>
							</li>
							<li role="presentation">
								<a href="${ctx}/crm/invitation/list?userId=${userId}&franchiseeId=${franchiseeId}">邀请明细</a>
							</li>
							<li role="presentation">
								<shiro:hasPermission name="crm:store:list">	
									<a href="${ctx}/crm/store/questionCrmList?mobile=${userDetail.mobile}&userId=${userId}&franchiseeId=${franchiseeId}&stamp=1">投诉咨询</a>
								</shiro:hasPermission>
							</li>
					  	</ul>
					</div>
					<!-- 工具栏 -->
				</div>
			</div>
			<sys:message content="${message}"/>
			<!-- 查询条件 -->
	    	<div class="ibox-content">
				<table id="contentTable" class="table table-striped table-bordered  table-hover table-condensed  dataTables-example dataTable no-footer">
					<thead>
						<tr>
						     <th style="text-align: center;">消费</th>
						     <th style="text-align: center;">余额</th>
						     <th style="text-align: center;">欠款</th>
						     <th style="text-align: center;">云币</th>
						     <th style="text-align: center;">可用红包</th>
						</tr>
					</thead>
					<tbody style="text-align: center;">
							<tr>
							  	<td>${account.totalBmount}</td>
							  	<td>${account.accountBalance}</td>
							    <td>${account.accountArrearage }</td>	
							  	<td>${account.userIntegral}</td>						
							    <td><a onclick="couponList()" style="color:red">${coupon}个</a></td>	
							</tr>
					</tbody>
				</table>
			</div>	 
		</div>
	</div>
</body>
</html>