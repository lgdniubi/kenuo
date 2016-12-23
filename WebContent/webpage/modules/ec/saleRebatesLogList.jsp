<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/webpage/include/taglib.jsp"%>

<html>
<head>
<title>订单日志列表</title>
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
				    elem: '#startTime',
				    format: 'YYYY-MM-DD',
				    event: 'focus',
				    max: $("#endTime").val(),   //最大日期
				    istime: false,				//是否显示时间
				    isclear: true,				//是否显示清除
				    istoday: false,				//是否显示今天
				    issure: true,				//是否显示确定
				    festival: true,				//是否显示节日
				    choose: function(datas){
				         end.min = datas; 		//开始日选好后，重置结束日的最小日期
				         end.start = datas 		//将结束日的初始值设定为开始日
				    }
				};
			var end = {
				    elem: '#endTime',
				    format: 'YYYY-MM-DD',
				    event: 'focus',
				    min: $("#startTime").val(),
				    istime: false,
				    isclear: true,
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
				<h5>订单日志</h5>
			</div>
			<sys:message content="${message}" />
			<!-- 查询条件 -->
			<div class="ibox-content">
				<div class="clearfix">
					<form:form id="searchForm" modelAttribute="saleRebatesLog" action="${ctx}/ec/salerelog/list" method="post"
						class="form-inline">
						<input id="pageNo" name="pageNo" type="hidden" value="${page.pageNo}" />
						<input id="pageSize" name="pageSize" type="hidden" value="${page.pageSize}" />

						<div class="form-group">
							<label>返利人：</label>
							<form:input path="rebateName" htmlEscape="false" maxlength="50" style="width:200px;" class="form-control" placeholder="返利人用户名"/>
							<label>收利人：</label>
							<form:input path="receiveName" htmlEscape="false" maxlength="50" style="width:200px;" class="form-control" placeholder="收利人用户名"/>
							<label>订单日期：</label>
							<input id="startTime" name="startTime" type="text" maxlength="20" class="laydate-icon form-control layer-date input-sm"
							value="<fmt:formatDate value="${saleRebatesLog.startTime}" pattern="yyyy-MM-dd"/>" style="width:185px;" placeholder="开始时间" readonly="readonly"/>
							一
							<input id="endTime" name="endTime" type="text" maxlength="20" class=" laydate-icon form-control layer-date input-sm" 
							value="<fmt:formatDate value="${saleRebatesLog.endTime}" pattern="yyyy-MM-dd"/>"  style="width:185px;" placeholder="结束时间" readonly="readonly"/>&nbsp;&nbsp;

						</div>
					</form:form>
					<!-- 工具栏 -->
					<div class="row" >
						<div class="col-sm-12">
							<div class="pull-left">
							</div>
							<div class="pull-right">
								<button class="btn btn-primary btn-rounded btn-outline btn-sm "
									onclick="search()">
									<i class="fa fa-search"></i> 查询
								</button>
								<button class="btn btn-primary btn-rounded btn-outline btn-sm "
									onclick="reset()">
									<i class="fa fa-refresh"></i> 重置
								</button>
							</div>
						</div>
					</div>
				</div>
				<p></p>
				<table id="contentTable"
					class="table table-striped table-bordered  table-hover table-condensed  dataTables-example dataTable no-footer">
					<thead>
						<tr>
							<th style="text-align: center;">编号</th>
							<th style="text-align: center;">返利人</th>
							<th style="text-align: center;">返利人级别</th>
							<th style="text-align: center;">收利人</th>
							<th style="text-align: center;">收利人级别</th>
							<!-- <th style="text-align: center;">关系类型</th> -->
							<th style="text-align: center;">订单号</th>
							<th style="text-align: center;">订单金额</th>
							<th style="text-align: center;">金额比例</th>
							<th style="text-align: center;">返利金额</th>
							<th style="text-align: center;">云币比例</th>
							<th style="text-align: center;">返利云币</th>
							<th style="text-align: center;">是否结算</th>
							<th style="text-align: center;">订单日期</th>
							<th style="text-align: center;">处理日期</th>
						</tr>
					</thead>
					<tbody style="text-align: center;">
						<c:forEach items="${page.list}" var="account" varStatus="status">
							<tr>
								<td>${account.id}</td>
								<td>${account.rebateName}</td>
								<td>${account.rebateLayer}</td>
								<td>${account.receiveName}</td>
								<td>${account.receiveLayer}</td>
								<%-- <td>${account.depth}</td> --%>
								<td>${account.orderId}</td>
								<td>${account.orderAmount}</td>
								<td>${account.balancePercent}</td>
								<td>${account.balanceAmount}</td>
								<td>${account.integralPercent}</td>
								<td>${account.integralAmount}</td>
								<c:if test="${account.rebateFlag==0}">
									<td>未结算</td>
								</c:if>
								<c:if test="${account.rebateFlag==1}">
									<td>已日结算</td>
								</c:if>
								<c:if test="${account.rebateFlag==2}">
									<td>已总结算</td>
								</c:if>
								<td><fmt:formatDate value="${account.rabateDate}" pattern="yyyy-MM-dd" /></td>
								<td><fmt:formatDate value="${account.createDate}" pattern="yyyy-MM-dd HH:mm:ss" /></td>
								</tr>
						</c:forEach>
					</tbody>
					<tfoot>
						<tr>
							<td colspan="20">
								<!-- 分页代码 -->
								<div class="tfoot">
									<table:page page="${page}"></table:page>
								</div>

							</td>
						</tr>
					</tfoot>
				</table>
			</div>
		</div>
	</div>


</body>
</html>