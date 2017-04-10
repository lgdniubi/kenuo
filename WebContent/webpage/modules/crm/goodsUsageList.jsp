<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/webpage/include/taglib.jsp"%>
<html>
<head>
<title>会员管理</title>
<meta name="decorator" content="default" />
<meta name="viewport"
	content="width=device-width, initial-scale=1.0, minimum-scale=1.0, maximum-scale=1.0,user-scalable=no">
	<link href="${ctxStatic}/layer-v2.0/layer/skin/layui.css" type="text/css" rel="stylesheet">
<script type="text/javascript">
	function edit() {
		$("userid").removeAttr("disabled");
		$("sex").attr("disabled", false);
		$("userid").removeAttr("disabled")
	}
	function save() {
		$("#inputForm").submit();
		return true;
	}
	$(document).ready(function() {
		var start = {
			    elem: '#startDate',
			    format: 'YYYY-MM-DD',
			    event: 'focus',
			    max: $("#endDate").val(),   //最大日期
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
			    elem: '#endDate',
			    format: 'YYYY-MM-DD',
			    event: 'focus',
			    min: $("#startDate").val(),
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
<style type="text/css">
.modal-content {
	margin: 0 auto;
	width: 400px;
}
</style>
</head>
<body class="gray-bg">
	<div class="wrapper wrapper-content">
		<div class="ibox">
			<sys:message content="${message}" />
			<!-- 查询条件 -->
			<div class="ibox-content">
				<div class="clearfix">
					<div class="nav">
						<!-- 翻页隐藏文本框 -->
						<div class="text-danger" style="margin:8px">
									<p class="text-primary">
										<span >${userDetail.nickname}</span>的客户档案--请注意保密
									</p>
						</div>
					 <ul class="layui-tab-title">
						<li role="presentation"><a
									href="${ctx}/crm/user/userDetail?userId=${userId}">基本资料</a></li>
						<li role="presentation"><a
									href="${ctx}/crm/physical/skin?userId=${userId}">身体状况</a></li>
						<li role="presentation"><a
									href="${ctx}/crm/schedule/list?userId=${userId}">护理时间表</a></li>
						<li role="presentation"><a
									href="${ctx}/crm/orders/list?userId=${userId}">客户订单</a></li>
						<li role="presentation"><a
									href="${ctx}/crm/coustomerService/list?userId=${userId}">售后</a></li>
						<li role="presentation"><a
									href="${ctx}/crm/consign/list?userId=${userId}">物品寄存</a></li>
						<li role="presentation" class="layui-this"><a
									href="${ctx}/crm/goodsUsage/list?userId=${userId}">产品使用记录</a></li>
						<li role="presentation"><a
									href="${ctx}/crm/user/account?userId=${userId}">账户总览</a></li>
						<li role="presentation"><a
									href="${ctx}/crm/invitation/list?userId=${userId}">邀请明细</a></li>
						<li role="presentation">
							<shiro:hasPermission name="crm:store:list">	
							<a onclick='top.openTab("${ctx}/crm/store/list?mobile=${userDetail.mobile}&stamp=1","会员投诉", false)'
								>投诉咨询</a>
							</shiro:hasPermission>
						</li>
					</div>
				</div>
			 <div>
			   	<!-- 工具栏 -->
					<div class="from-group">
						<div class="pull-left col-md-1">
							<shiro:hasPermission name="crm:goodsUsage:exe">
								<table:addRow url="${ctx}/crm/goodsUsage/add?userId=${userId}" title="用户使用记录"></table:addRow><!-- 增加按钮 -->
						    </shiro:hasPermission>
						</div>
						<div class="row">
						     <form action="${ctx}/crm/goodsUsage/list" method="post"> 
								<input name="userId" value="${userDetail.userId}" type="hidden"	>
								<input id="startDate" name="startDate"
								type="text" maxlength="20"
								class="laydate-icon form-control layer-date input-sm"
								value="<fmt:formatDate value="${returnedGoods.begtime}" pattern="yyyy-MM-dd"/>"
								placeholder="开始时间" readonly="readonly" style="width: 185px;" />
							— <input id="endDate" name="endDate" type="text" maxlength="20"
								class=" laydate-icon form-control layer-date input-sm"
								value="<fmt:formatDate value="${returnedGoods.endtime}" pattern="yyyy-MM-dd"/>"
								placeholder="结束时间" readonly="readonly" style="width: 185px;" />
								<button class="btn btn-primary btn-rounded btn-outline btn-sm "
									type="submit">
									<i class="fa fa-search"></i> 查询
								</button>
							</form>	
						</div>
					</div>
				<table id="contentTable"
					class="table table-striped table-bordered  table-hover table-condensed  dataTables-example dataTable no-footer">
					<thead>
						<tr>
							<!-- 							 <th style="text-align: center;">编号</th> -->
							<th style="text-align: center;">产品名称</th>
							<th style="text-align: center;">购买数量</th>
							<th style="text-align: center;">开始使用日期</th>
							<th style="text-align: center;">结束使用日期</th>
							<th style="text-align: center;">使用效果</th>
							<th style="text-align: center;">用户反馈</th>
							<th style="text-align: center;">备注</th>
							<th style="text-align: center;">操作</th>
						</tr>
					</thead>
					<tbody style="text-align: center;">
						<c:forEach items="${page.list}" var="item">
							<tr>
								<td>${item.goodsName}</td>
								<td>${item.usageNum}</td>
								<td><fmt:formatDate value="${item.startDate}"
 										pattern="yyyy-MM-dd " /></td>
 								<td><fmt:formatDate value="${item.endDate}"
 										pattern="yyyy-MM-dd " /></td>
								<td>${item.effection}</td>
								<td>${item.feedback}</td>
								<td>${item.remark}</td> 
								<td>
									<shiro:hasPermission name="crm:goodsUsage:exe">
 									<a href="#" onclick="openDialog('修改使用记录', '${ctx}/crm/goodsUsage/update?usageId=${item.usageId}&userId=${userId}','800px', '500px')"
 								  		 class="btn btn-success btn-xs" ><i class="fa fa-search-plus"></i>修改</a> 
								    </shiro:hasPermission>
								</td> 
 							</tr> 
 						</c:forEach>
					</tbody>
					<tfoot>
						<tr>
							<td colspan="20">
								<!-- 分页代码 -->
								<div class="tfoot"></div> <table:page page="${page}"></table:page>
							</td>
						</tr>
					</tfoot>
				</table>
			</div>
		</div>
	</div>
	</div>
</body>
</html>