<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/webpage/include/taglib.jsp"%>
<html>
<head>
	<title>对账管理</title>
	<meta name="decorator" content="default"/>
	<script type="text/javascript">
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
	    })
	    function nowReset(){//重置，页码清零
			$("#resetNum").val("reset");
			$("#pageNo").val(0);
			$("#searchForm div.form-group input").val("");
			$("#searchForm div.form-group select").val("");
			$("#searchForm").submit();
			return false;
		 }
		function nowSearch(){//查询，页码清零
			$("#resetNum").val("reset");
			$("#pageNo").val(0);
			$("#searchForm").submit();
				return false;
		}
		function page(n,s){//翻页
			$("#resetNum").val("reset");
			$("#pageNo").val(n);
			$("#pageSize").val(s);
			$("#searchForm").submit();
			$("span.page-size").text(s);
			return false;
		}
	</script>
</head>
<body class="gray-bg">
	<div class="wrapper wrapper-content">
		<div class="ibox">
			<div class="ibox-title">
				<h5>对账管理 </h5>
			</div>
			<sys:message content="${message}"/>
		    <div class="ibox-content">
				<!-- 查询条件 -->
				<div class="clearfix">
					<form:form id="searchForm" modelAttribute="mtmyCheckAccount" action="${ctx}/ec/mtmyCheck/list" method="post" class="navbar-form navbar-left searcharea">
						<input id="pageNo" name="pageNo" type="hidden" value="${page.pageNo}"/>
						<input id="pageSize" name="pageSize" type="hidden" value="${page.pageSize}"/>
						<input id="resetNum" name="resetNum" type="hidden"><!-- 默认查询当前最新标示 -->
						<table:sortColumn id="orderBy" name="orderBy" value="${page.orderBy}" callback="sortOrRefresh();"/><!-- 支持排序 -->
						<div class="form-group">
							<select class="form-control" id="groupFlag" name="groupFlag">
								<option value="">请选择标示</option>
								<c:forEach items="${list }" var="list">
									<c:choose>
										<c:when test="${list.groupFlag eq mtmyCheckAccount.groupFlag}">
											<option value="${list.groupFlag }" selected="selected">${list.groupFlag }</option>
										</c:when>
										<c:otherwise>
											<option value="${list.groupFlag }">${list.groupFlag }</option>
										</c:otherwise>
									</c:choose>
								</c:forEach>
							</select>
							<input id="orderNo" name="orderNo" type="text" value="${mtmyCheckAccount.orderNo }" class="form-control" placeholder="订单号"> 
							<span>时间范围：</span>
							<input id="begtime" name="begtime" type="text" maxlength="20" class="laydate-icon form-control layer-date input-sm" value="<fmt:formatDate value="${mtmyCheckAccount.begtime }" pattern="yyyy-MM-dd"/>" placeholder="开始时间" readonly="readonly"/>
							<label>&nbsp;&nbsp;--&nbsp;&nbsp;</label>
							<input id="endtime" name="endtime" type="text" maxlength="20" class=" laydate-icon form-control layer-date input-sm" value="<fmt:formatDate value="${mtmyCheckAccount.endtime }" pattern="yyyy-MM-dd"/>" placeholder="结束时间" readonly="readonly"/>
						</div>	
					</form:form>
					<!-- 工具栏 -->
					<div class="row" style="padding-bottom: 5px;">
						<div class="col-sm-12">
							<div class="pull-left">
								<shiro:hasPermission name="ec:mtmyCheck:export">
									<!-- 导出按钮 -->
									<table:exportExcel url="${ctx}/ec/mtmyCheck/export"></table:exportExcel>
								</shiro:hasPermission>
								<shiro:hasPermission name="ec:mtmyCheck:importPage">
									<!-- 导入数据 -->
									<a href="#" onclick="openDialog('导入ping++数据', '${ctx}/ec/mtmyCheck/importPage','400px', '220px')" class="btn btn-white btn-sm"><i class="fa fa-folder-open-o"></i>导入ping++数据</a>
								</shiro:hasPermission>
							</div>
							<div class="pull-right">
								<button  class="btn btn-primary btn-rounded btn-outline btn-sm " onclick="nowSearch()" ><i class="fa fa-search"></i> 查询</button>
								<button  class="btn btn-primary btn-rounded btn-outline btn-sm " onclick="nowReset()" ><i class="fa fa-refresh"></i> 重置</button>
							</div>
						</div>
					</div>
				</div>
				<table id="contentTable" class="table table-striped table-bordered  table-hover table-condensed  dataTables-example dataTable no-footer">
					<thead>
						<tr>
							<th>商户订单号</th>
							<th>每天美耶订单号</th>
							<th>交易时间</th>
							<th>实收金额</th>
							<th>交易渠道</th>
							<th>标示</th>
							<th>用户ID</th>
							<th>昵称</th>
							<th>操作</th>
						</tr>
					</thead>
					<tbody>
						<c:forEach items="${page.list}" var="mtmyCheckAccount">
							<tr>
								<th>${mtmyCheckAccount.orderNo }</th>
								<th>${mtmyCheckAccount.orderId }</th>
								<th><fmt:formatDate value="${mtmyCheckAccount.payDate }" pattern="yyyy-MM-dd HH:mm:ss"/></th>
								<th>${mtmyCheckAccount.payAmount }</th>
								<th>${mtmyCheckAccount.payChannel }</th>
								<th>${mtmyCheckAccount.groupFlag }</th>
								<th>${mtmyCheckAccount.userId }</th>
								<th>${mtmyCheckAccount.nickname }</th>
								<th>
									<c:if test="${mtmyCheckAccount.orderId ne null}">
										<shiro:hasPermission name="ec:mtmyCheck:view"> 
					 						<a href="#" onclick="openDialogView('商品详情', '${ctx}/ec/mtmyCheck/goodslist?orderid=${mtmyCheckAccount.orderId }','800px','400px')" class="btn btn-info btn-xs" ><i class="fa fa-search-plus"></i> 商品详情</a>
					 						<a href="#" onclick="openDialogView('查看订单', '${ctx}/ec/orders/orderform?orderid=${mtmyCheckAccount.orderId }','800px','600px')" class="btn btn-info btn-xs" ><i class="fa fa-search-plus"></i> 订单详情</a>
					 					</shiro:hasPermission>
									</c:if>
								</th>
							</tr>
						</c:forEach>
					</tbody>
				</table>
				<!-- 分页代码 -->
				<table:page page="${page}"></table:page>
				<br/>
				<br/>
			</div>
		</div>
	</div>
</body>
</html>