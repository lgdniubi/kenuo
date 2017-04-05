<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/webpage/include/taglib.jsp"%>
<%@ include file="/webpage/include/icheck.jsp"%>
<html>
<head>
<title>云币充值记录</title>
<meta name="decorator" content="default" />
<link rel="stylesheet" type="text/css" href="${ctxStatic}/common/training.css">
<script type="text/javascript">
	function page(n, s) {
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
			    isclear: true,				//是否显示清除
			    istoday: true,				//是否显示今天
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
			    isclear: true,
			    istoday: true,
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

<body>
	<div class="wrapper wrapper-content">
		<div class="ibox">
			<!-- 标题 -->
			<div class="ibox-title">
				<h5>云币充值记录</h5>
			</div>
			<!-- 主体内容 -->
			<div class="ibox-content">
				<sys:message content="${message}" />
				<!-- 查询条件 -->
				<div class="row">
					<div class="col-sm-12">
						<form:form id="searchForm" modelAttribute="trainLiveRecord" action="${ctx}/train/record/payRecordList" method="post" class="form-inline">
							<input id="pageNo" name="pageNo" type="hidden" value="${page.pageNo}" />
							<input id="pageSize" name="pageSize" type="hidden" value="${page.pageSize}" />
							<table:sortColumn id="orderBy" name="orderBy" value="${page.orderBy}" callback="sortOrRefresh();" />
							<!-- 支持排序 -->
							<div class="form-group">
								<span>姓&nbsp;&nbsp;&nbsp;名：</span>
								<form:input path="name" htmlEscape="false" maxlength="50" class=" form-control input-sm" />
								<span>手机号码：</span>
								<form:input path="phone" htmlEscape="false" maxlength="50" class=" form-control input-sm" />
								<span>归属机构：</span>
								<sys:treeselect id="organization" name="organization.id" value="${trainLiveRecord.organization.id}" labelName="organization.name" labelValue="${trainLiveRecord.organization.name}" title="部门" url="/sys/office/treeData?type=2" cssClass=" form-control input-sm" allowClear="true" notAllowSelectRoot="false" notAllowSelectParent="false" />
								<label>充值时间：</label>
								<input id="begtime" name="beginTime" type="text" maxlength="20" class="laydate-icon form-control layer-date input-sm"
									value="<fmt:formatDate value="${trainLiveRecord.beginTime}" pattern="yyyy-MM-dd"/>" style="width:185px;" placeholder="开始时间" readonly="readonly"/>
								一
								<input id="endtime" name="endTime" type="text" maxlength="20" class=" laydate-icon form-control layer-date input-sm" 
								value="<fmt:formatDate value="${trainLiveRecord.endTime}" pattern="yyyy-MM-dd"/>"  style="width:185px;" placeholder="结束时间" readonly="readonly"/>&nbsp;&nbsp;
								<span>充值单号：</span>
								<form:input path="orderId" htmlEscape="false" maxlength="150" class=" form-control input-sm" />
							</div>
						</form:form>
						<br />
					</div>
				</div>
				<!-- 工具栏 -->
				<div class="row">
					<div class="col-sm-12">
						<div class="pull-right">
							<button class="btn btn-primary btn-rounded btn-outline btn-sm " onclick="search()">
								<i class="fa fa-search"></i> 查询
							</button>
							<button class="btn btn-primary btn-rounded btn-outline btn-sm " onclick="reset()">
								<i class="fa fa-refresh"></i> 重置
							</button>
						</div>
					</div>
				</div>
				<!-- 主要内容展示 -->
				<table id="contentTable" class="table table-striped table-bordered table-hover table-condensed dataTables-example dataTable">
					<thead>
						<tr>
							<th style="text-align: center;">姓名</th>
							<th style="text-align: center;">手机号</th>
							<th style="text-align: center;">归属机构</th>
							<th style="text-align: center;">充值单号</th>
							<th style="text-align: center;">充值云币数量</th>
							<th style="text-align: center;">充值人民币</th>
							<th style="text-align: center;">充值时间</th>
							<th style="text-align: center;">支付方式</th>
						</tr>
					</thead>
					<tbody style="text-align: center;">
						<c:forEach items="${page.list}" var="live" varStatus="status">
							<tr>
								<td>${live.name}</td>
								<td>${live.phone}</td>
								<td>${live.organization.name}</td>
								<td>${live.orderId}</td>
								<td>${live.integrals}</td>
								<td>${live.specPrice}</td>
								<td><fmt:formatDate value="${live.payDate}" pattern="yyyy-MM-dd HH:mm:ss" /></td>
								<td>
									<c:if test="${live.payCode eq 'wx'}">微信</c:if>
									<c:if test="${live.payCode eq 'alipay'}">支付宝</c:if>
								</td>
							</tr>
						</c:forEach>
					</tbody>
				</table>
				<!-- 分页table -->
				<table:page page="${page}"></table:page>
			</div>
		</div>
	</div>
</body>
</html>