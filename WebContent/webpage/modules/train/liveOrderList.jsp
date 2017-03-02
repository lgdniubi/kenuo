<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/webpage/include/taglib.jsp"%>

<html>
<head>
<title>直播订单列表</title>
<meta name="decorator" content="default" />

<script type="text/javascript">
	function page(n, s) {
		$("#pageNo").val(n);
		$("#pageSize").val(s);
		$("#searchForm").submit();
		return false;
	}
	
	$(document).ready(function() {
	    var start = {
		    elem: '#beginDate',
		    format: 'YYYY-MM-DD hh:mm:ss',
		    event: 'focus',
		    max: $("#endDate").val(),   //最大日期
		    istime: true,				//是否显示时间
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
		    elem: '#endDate',
		    format: 'YYYY-MM-DD hh:mm:ss',
		    event: 'focus',
		    min: $("#beginDate").val(),
		    istime: true,
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
    })
	
    function newReset(){
		window.location = "${ctx}/train/live/liveOrderList";
	}
	
	
</script>
</head>

<body class="gray-bg">
	<div class="wrapper wrapper-content">
		<div class="ibox">
			<div class="ibox-title">
				<h5>直播订单列表</h5>
			</div>
			<sys:message content="${message}" />
			<!-- 查询条件 -->
			<div class="ibox-content">
				<div class="clearfix">
					<form:form id="searchForm" modelAttribute="trainLiveOrder" action="${ctx}/train/live/liveOrderList" method="post" class="form-inline">
						<input id="pageNo" name="pageNo" type="hidden" value="${page.pageNo}"/>
						<input id="pageSize" name="pageSize" type="hidden" value="${page.pageSize}"/>
						<div class="form-group">
							<label>用户名：</label>
							<form:input path="userName" htmlEscape="false" maxlength="50" class=" form-control input-sm"/>&nbsp;&nbsp;&nbsp;&nbsp;
							<label>订单ID：</label>
							<form:input path="trainLiveOrderId" htmlEscape="false" maxlength="50" class=" form-control input-sm"/>&nbsp;&nbsp;&nbsp;&nbsp;
							<label>直播申请ID：</label>
							<form:input path="auditId" htmlEscape="false" maxlength="50" class=" form-control input-sm" onkeyup="this.value=this.value.replace(/\D/g,'')" onfocus="if(value == '0')value=''" onblur="if(this.value == '')this.value='0';"/>&nbsp;&nbsp;&nbsp;&nbsp;
							<label>类型：</label>
							<form:select path="type"  class="form-control" style="width:185px;">
								<form:option value="0">全部</form:option>
								<form:option value="1">直播</form:option>
								<form:option value="2">回放</form:option>
							</form:select>
							<p></p>
							<p>
								<label>支付状态：</label>
								<form:select path="orderStatus"  class="form-control" style="width:185px;">
									<form:option value="0">全部</form:option>
									<form:option value="1">取消订单</form:option>
									<form:option value="2">待支付</form:option>
									<form:option value="3">已付款</form:option>
									<form:option value="4">已退款</form:option>
								</form:select>
								<label>添加时间：</label>
								<input id="beginDate" name="beginDate" type="text" maxlength="20" class="laydate-icon form-control layer-date input-sm" value="<fmt:formatDate value="${trainLiveOrder.beginDate }" pattern="yyyy-MM-dd HH:mm:ss"/>" placeholder="开始时间" readonly="readonly"/>--
								<input id="endDate" name="endDate" type="text" maxlength="20" class=" laydate-icon form-control layer-date input-sm" value="<fmt:formatDate value="${trainLiveOrder.endDate }" pattern="yyyy-MM-dd HH:mm:ss"/>" placeholder="结束时间" readonly="readonly"/>
							</p>
							
						</div>
					</form:form>
					<div class="pull-right">
						<button class="btn btn-primary btn-rounded btn-outline btn-sm " onclick="search()">
							<i class="fa fa-search"></i> 查询
						</button>
						<button class="btn btn-primary btn-rounded btn-outline btn-sm " onclick="newReset()">
							<i class="fa fa-refresh"></i> 重置
						</button>
					</div>
				</div>
				<p></p>
				<table id="contentTable"
					class="table table-striped table-bordered  table-hover table-condensed  dataTables-example dataTable no-footer">
					<thead>
						<tr>
							<th style="text-align: center;">订单ID</th>
							<th style="text-align: center;">直播申请ID</th>
							<th style="text-align: center;">用户名</th>
							<th style="text-align: center;">直播规格ID</th>
							<th style="text-align: center;">天数</th>
							<th style="text-align: center;">实付金额</th>
							<th style="text-align: center;">类型</th>
							<th style="text-align: center;">添加时间</th>
							<th style="text-align: center;">支付时间</th>
							<th style="text-align: center;">有效时间</th>
							<th style="text-align: center;">支付状态</th>
						</tr>
					</thead>
					<tbody style="text-align: center;">
						<c:forEach items="${page.list}" var="order">
							<tr>
								<td>${order.trainLiveOrderId}</td>
								<td>${order.auditId}</td>
								<td>${order.userName}</td>
								<td>${order.specId}</td>
								<td>${order.specNum}</td>
								<td>${order.specPrice}</td>
								<td>
									<c:if test="${order.type == 1}">直播</c:if>
									<c:if test="${order.type == 2}">回放</c:if>
								</td>
								<td><fmt:formatDate value="${order.createDate}" pattern="yyyy-MM-dd HH:mm:ss"/></td>
								<td><fmt:formatDate value="${order.payDate}" pattern="yyyy-MM-dd HH:mm:ss"/></td>
								<td><fmt:formatDate value="${order.validDate}" pattern="yyyy-MM-dd HH:mm:ss"/></td>
								<td>
									<c:if test="${order.orderStatus == 1}">取消订单</c:if>
									<c:if test="${order.orderStatus == 2}">待支付</c:if>
									<c:if test="${order.orderStatus == 3}">已付款</c:if>
									<c:if test="${order.orderStatus == 4}">已退款</c:if>
								</td>
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