<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/webpage/include/taglib.jsp"%>

<html>
<head>
<title>红包明细列表</title>
<meta name="decorator" content="default" />

<script type="text/javascript">
		function page(n,s){
			$("#pageNo").val(n);
			$("#pageSize").val(s);
			$("#searchForm").submit();
	    	return false;
	    }
		$(document).ready(function() {
			var beg = {
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
				        beg.max = datas; //结束日选好后，重置开始日的最大日期
				    }
				};
				var start = {
				    elem: '#startTime',
				    format: 'YYYY-MM-DD',
				    event: 'focus',
				    max: $("#lastTime").val(),   //最大日期
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
				var last = {
				    elem: '#lastTime',
				    format: 'YYYY-MM-DD',
				    event: 'focus',
				    min: $("#startTime").val(),
				    istime: false,
				    isclear: false,
				    istoday: false,
				    issure: true,
				    festival: true,
				    choose: function(datas){
				    	start.max = datas; //结束日选好后，重置开始日的最大日期
				    }
				};
				laydate(beg);
				laydate(end);
				laydate(start);
				laydate(last);

	    });
</script>
</head>
<body class="gray-bg">
	<div class="wrapper wrapper-content">
		<div class="ibox">
			<div class="ibox-title">
				<h5>红包明细列表</h5>
			</div>
			<sys:message content="${message}" />
			<!-- 查询条件 -->
			<div class="ibox-content">
				<div class="clearfix">
					<form:form id="searchForm" modelAttribute="activityCouponUser" action="${ctx}/ec/activity/couponUserlist" method="post"
						class="form-inline">
						<input id="pageNo" name="pageNo" type="hidden" value="${page.pageNo}" />
						<input id="pageSize" name="pageSize" type="hidden" value="${page.pageSize}" />

						<div class="form-group">
							<form:input path="orderId" htmlEscape="false" maxlength="60" style="width:200px;" class="form-control" placeholder="订单号"/>
							<form:input path="couponId" htmlEscape="false" maxlength="50" style="width:200px;" class="form-control" placeholder="红包ID"/>
							<form:input path="name" htmlEscape="false" maxlength="50" style="width:200px;" class="form-control" placeholder="用户名"/>
							<form:input path="mobile" htmlEscape="false" maxlength="50" class=" form-control input-sm" placeholder="手机号码"/>
							<label>是否使用：</label>
							<form:select path="status" class="form-control"
								style="width:185px;">
								<form:option value="">全部</form:option>
								<form:option value="0">未使用</form:option>
								<form:option value="1">已使用</form:option>
								<form:option value="2">已过期</form:option>
							</form:select>
							<p></p>
							<label>领取日期：</label>
							<input id="begtime" name="begtime" type="text" maxlength="20" class="laydate-icon form-control layer-date input-sm"
							value="<fmt:formatDate value="${activityCouponUser.begtime}" pattern="yyyy-MM-dd"/>" style="width:185px;" placeholder="开始时间" readonly="readonly"/>
							一
							<input id="endtime" name="endtime" type="text" maxlength="20" class=" laydate-icon form-control layer-date input-sm" 
							value="<fmt:formatDate value="${activityCouponUser.endtime}" pattern="yyyy-MM-dd"/>"  style="width:185px;" placeholder="结束时间" readonly="readonly"/>&nbsp;&nbsp;
							<label>使用日期：</label>
							<input id="startTime" name="startTime" type="text" maxlength="20" class="laydate-icon form-control layer-date input-sm"
							value="<fmt:formatDate value="${activityCouponUser.startTime}" pattern="yyyy-MM-dd"/>" style="width:185px;" placeholder="开始时间" readonly="readonly"/>
							一
							<input id="lastTime" name="lastTime" type="text" maxlength="20" class=" laydate-icon form-control layer-date input-sm" 
							value="<fmt:formatDate value="${activityCouponUser.lastTime}" pattern="yyyy-MM-dd"/>"  style="width:185px;" placeholder="结束时间" readonly="readonly"/>&nbsp;&nbsp;
						</div>
					</form:form>
					<p></p>
					<!-- 工具栏 -->
					<div class="row" >
						<div class="col-sm-12">
							<div class="pull-left">
								<shiro:hasPermission name="ec:activity:importPage">
									<!-- 导出按钮 -->
									<table:exportExcel url="${ctx}/ec/activity/export"></table:exportExcel>
								</shiro:hasPermission>
							</div>
							<div class="pull-right">
								<button class="btn btn-primary btn-rounded btn-outline btn-sm " onclick="search()"><i class="fa fa-search"></i> 查询
								</button>
								<button class="btn btn-primary btn-rounded btn-outline btn-sm " onclick="reset()"><i class="fa fa-refresh"></i> 重置
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
							<th style="text-align: center;">用户名</th>
							<th style="text-align: center;">活动ID</th>
							<th style="text-align: center;">活动名称</th>
							<th style="text-align: center;">红包ID</th>
							<th style="text-align: center;">红包名称</th>
							<th style="text-align: center;">红包类型</th>
							<th style="text-align: center;">红包金额</th>
							<th style="text-align: center;">领取时间</th>
							<th style="text-align: center;">订单金额</th>
							<th style="text-align: center;">使用时间</th>
							<th style="text-align: center;">订单号</th>
						</tr>
					</thead>
					<tbody style="text-align: center;">
						<c:forEach items="${page.list}" var="coupon" varStatus="status">
							<tr>
								<td>${coupon.id}</td>
								<td>${coupon.name}</td>
								<td>${coupon.actionId}</td>
								<td>${coupon.actionName}</td>
								<td>${coupon.couponId}</td>
								<td>${coupon.couponName}</td>
								<td>
								 	<c:if test="${coupon.type==1}">
										商品页领取
									</c:if>
									<c:if test="${coupon.type==2}">
										活动页领取
									</c:if>
				
									<c:if test="${coupon.type==3}">
										新注册
									</c:if>	
									<c:if test="${coupon.type==4}">
										内部红包
									</c:if>
								</td>
								<td>${coupon.couponMoney}</td>
								<td><fmt:formatDate value="${coupon.addTime}" pattern="yyyy-MM-dd HH:mm:ss" /></td>
								<td>
									<c:if test="${coupon.status==0}">
										未使用
									</c:if>
									<c:if test="${coupon.status==1}">
										${coupon.orderAmount}
									</c:if>
									<c:if test="${coupon.status==2}">
										已过期
									</c:if>
								</td>	
								<td><fmt:formatDate value="${coupon.usedTime}" pattern="yyyy-MM-dd HH:mm:ss" /></td>
								<td>${coupon.orderId}</td>
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