<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/webpage/include/taglib.jsp"%>

<html>
<head>
<title>邀请明细列表</title>
<meta name="decorator" content="default" />

<script type="text/javascript">

		function page(n,s){
			$("#pageNo").val(n);
			$("#pageSize").val(s);
			$("#searchForm").submit();
	    	return false;
	    }
	
		$(document).ready(function() {
			
		
			var start = {
				    elem: '#begtime',
				    format: 'YYYY-MM-DD',
				    event: 'focus',
				    max: $("#endtime").val(),   //最大日期
				    istime: false,				//是否显示时间
				    isclear: false,				//是否显示清除
				    istoday: false,				//是否显示今天
				    issure: true,				//是否显示确定
				    festival: true,				//是否显示节日
				    choose: function(datas){
				         end.min = datas; 		//开始日选好后，重置结束日的最小日期
				         end.start = datas 		//将结束日的初始值设定为开始日
				    }
				};
			var end = {
				    elem: '#endtime',
				    format: 'YYYY-MM-DD',
				    event: 'focus',
				    min: $("#begtime").val(),
				    istime: false,
				    isclear: false,
				    istoday: false,
				    issure: true,
				    festival: true,
				    choose: function(datas){
				        start.max = datas; //结束日选好后，重置开始日的最大日期
				    }
				};
				laydate(start);
				laydate(end);
			
			
	       
	    });
</script>
</head>




<body class="gray-bg">
	<div class="wrapper wrapper-content">
		<div class="ibox">
			<div class="ibox-title">
				<h5>受邀人明细</h5>
			</div>
			<div class="ibox-content">
				<div class="clearfix">
					<form:form id="searchForm" modelAttribute="invitationUser" action="${ctx}/ec/invitation/form" method="post"
						class="form-inline">
						
						<div class="form-group">
							<label>发起人：</label>
							${invitationUser.userName}&nbsp;&nbsp;&nbsp;&nbsp;
							<label>邀请人数：</label>
							${invitationUser.sendNum}&nbsp;&nbsp;&nbsp;&nbsp;
							<label>消费人数：</label>
							${invitationUser.consume}&nbsp;&nbsp;&nbsp;&nbsp;
							<label>总消费金额：</label>
							${invitationUser.consumePrice}&nbsp;元&nbsp;&nbsp;&nbsp;&nbsp;
							<label>总提成：</label>
							${invitationUser.tiPrice}&nbsp;元&nbsp;&nbsp;&nbsp;&nbsp;
							<label>账户余额：</label>
							${invitationUser.price}&nbsp;元&nbsp;&nbsp;&nbsp;&nbsp;
							
						</div>
					</form:form>
					<!-- 工具栏 -->
					
				</div>
				<table id="contentTable"
					class="table table-striped table-bordered  table-hover table-condensed  dataTables-example dataTable no-footer">
					<thead>
						<tr>
							<th style="text-align: center;">编号</th>
							<th style="text-align: center;">受邀人</th>
							<th style="text-align: center;">手机号</th>
							<th style="text-align: center;">注册时间</th>
							<th style="text-align: center;">消费时间</th>
							<th style="text-align: center;">消费订单ID</th>
							<th style="text-align: center;">消费金额</th>
							<th style="text-align: center;">获得提成</th>	
						</tr>
					</thead>
					<tbody style="text-align: center;">
						<c:forEach items="${page}" var="list" varStatus="status">
							<tr>
								<td>${status.index+1}</td>
								<td>${list.userName}</td>
								<td>${list.mobile}</td>
								<td><fmt:formatDate value="${list.registerTime}" pattern="yyyy-MM-dd HH:mm:ss" /></td>
								<td><fmt:formatDate value="${list.consumeTime}" pattern="yyyy-MM-dd HH:mm:ss" /></td>
								<td>${list.orderId}</td>
								<td>${list.orderPrice}元</td>
								<td>${list.price}元</td>
								</tr>
						</c:forEach>
					</tbody>
					
				</table>
			</div>
		</div>
	</div>


</body>
</html>