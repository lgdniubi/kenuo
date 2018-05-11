<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/webpage/include/taglib.jsp"%>
<html>
<head>
<title>会员管理</title>
<meta name="decorator" content="default" />
<meta name="viewport"
	content="width=device-width, initial-scale=1.0, minimum-scale=1.0, maximum-scale=1.0,user-scalable=no">
<link href="${ctxStatic}/layer-v2.0/layer/skin/layui.css"
	type="text/css" rel="stylesheet">

<script type="text/javascript">
	// 	    function edit(){
	// 	    	$("userid").removeAttr("disabled"); 
	// 	    	$("sex").attr("disabled",false);
	// 	    	$("userid").removeAttr("disabled")
	// 	    }
	function save() {
		$("#inputForm").submit();
		return true;
	}
	$('#edit').click(function() {
		if ($('#fieldset').is(':disabled')) {
			$('#fieldset').removeAttr('disabled');
		}
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
						<div class="text-danger" style="margin:8px">
							<p class="text-primary">
								<span >${detail.nickname}</span>的客户档案--请注意保密
							</p>
						</div>
						<!-- 翻页隐藏文本框 -->
						<ul class="layui-tab-title">
							<li role="presentation">
								<a href="${ctx}/crm/user/userDetail?userId=${userId}&franchiseeId=${franchiseeId}">基本资料</a>
							</li>
							<li role="presentation">
								<a href="${ctx}/crm/physical/skin?userId=${userId}&franchiseeId=${franchiseeId}">身体状况</a>
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
							<li role="presentation">
								<a href="${ctx}/crm/user/account?userId=${userId}&franchiseeId=${franchiseeId}">账户总览</a>
							</li>
							<li role="presentation">
								<a href="${ctx}/crm/invitation/list?userId=${userId}&franchiseeId=${franchiseeId}">邀请明细</a>
							</li>
							<li role="presentation" class="layui-this">
								<shiro:hasPermission name="crm:store:list">	
									<a href="${ctx}/crm/store/questionCrmList?mobile=${mobile}&userId=${userId}&franchiseeId=${franchiseeId}&stamp=1">投诉咨询</a>
								</shiro:hasPermission>
							</li>
						</ul>
					</div>
				</div>
			</div>
			<!-- 查询条件 -->
			<div class="ibox-content">
				<div class="clearfix">
					<form:form id="searchForm" action="${ctx}/crm/store/questionCrmList" modelAttribute="complain" method="post" class="form-inline">
						<input id="pageNo" name="pageNo" type="hidden" value="${page.pageNo}"/>
						<input id="pageSize" name="pageSize" type="hidden" value="${page.pageSize}"/>
						<input id="stamp" class="stamp" name="stamp" value="${stamp}" type="hidden">
						<input id="userId" name="userId" value="${userId}" type="hidden">
						<input id="franchiseeId" name="franchiseeId" value="${franchiseeId}" type="hidden">
						<form:hidden path="mobile" id="mobile"/>
						<form:hidden path="member" id="member"/>
						<div class="form-group">						
							<label>商品品牌：</label>
		                    <select class="form-control" id="brandType" name="brandType" style="text-align: center;width: 150px;">
		                        <option value="">所有品牌</option>
								<c:forEach items="${goodsBrandList}" var="goodsBrand">
									<option ${(goodsBrand.id == complain.brandType)?'selected="selected"':''} value="${goodsBrand.id}">${goodsBrand.name}</option>
								</c:forEach>
                            </select>                              
							<label>处理结果：</label>
							<form:select path="status"  class="form-control" style="width:185px;">
								<form:option value="">全部</form:option>
								<form:option value="1">未处理</form:option>
								<form:option value="2">已处理</form:option>
							</form:select>
							<p></p>
						</div>
					</form:form>
					<!-- 工具栏 -->
					<div class="row">
						<div class="col-sm-12">		
							<table:addRow url="${ctx}/crm/store/from?mobile=${mobile}&userId=${userId}&franchiseeId=${franchiseeId}&stamp=${stamp}&tab=1" width="800px" height="680px" title="投诉咨询"></table:addRow><!-- 增加按钮 -->
							<div class="pull-right">
								<button  class="btn btn-primary btn-rounded btn-outline btn-sm " onclick="search()" ><i class="fa fa-search"></i> 查询</button>
								<button  class="btn btn-primary btn-rounded btn-outline btn-sm " onclick="reset()" ><i class="fa fa-refresh"></i> 重置</button>
							</div>
						</div>
					</div>
					<p></p>
				</div>
				<table id="treeTable" class="table table-striped table-bordered table-hover table-condensed dataTables-example dataTable">
					<thead>
						<tr>
							<th style="text-align: center;">ID</th>
							<th style="text-align: center;">投诉主题</th>
							<th style="text-align: center;">分类</th>
							<th style="text-align: center;">品牌</th>
							<th style="text-align: center;">客户昵称</th>
							<th style="text-align: center;">真实姓名</th>							
							<th style="text-align: center;">客户类型</th>
							<th style="text-align: center;">日期</th>							
							<th style="text-align: center;">时间</th>
							<th style="text-align: center;">紧急程度</th>							
							<th style="text-align: center;">首次接待人</th>					
							<th style="text-align: center;">最后处理人</th>
							<th style="text-align: center;">状态</th>
							<th style="text-align: center;">转交次数
							<th style="text-align: center;">操作</th>
						</tr>
					</thead>
					<tbody>
						<c:forEach items="${page.list}" var="complain">
							<tr style="text-align: center;">
								<td>${complain.id}</td>
								<td style="text-align:left;">${complain.theme}</td>								   
								<td>
									<c:if test="${complain.questionType== '1'}">投诉</c:if>
								    <c:if test="${complain.questionType== '2'}">咨询</c:if>
									<c:if test="${complain.questionType== '3'}">销售机会</c:if>
									<c:if test="${complain.questionType=='4'}">后台管理</c:if>
					            </td>										
								<td>${complain.brandType}</td>
								<td>${complain.nickName}</td>
								<td>${complain.name}</td>
								<td>
								    <c:if test="${complain.member== '1'}">会员</c:if>
								    <c:if test="${complain.member== '2'}">非会员</c:if>
					            </td>	
								<td><fmt:formatDate value="${complain.creatDate}" pattern="yyyy-MM-dd HH:mm:ss" /></td>
                                <td>
                                	<c:if test="${complain.recordTime== '1'}">0-5分钟</c:if>
								    <c:if test="${complain.recordTime== '2'}">6-15分钟</c:if>
									<c:if test="${complain.recordTime== '3'}">15-30分钟</c:if>
									<c:if test="${complain.recordTime=='4'}">30~~分钟</c:if>
					            </td>
								<td>
									<c:if test="${complain.degree== '1'}">非常紧急</c:if>
								    <c:if test="${complain.degree== '2'}">紧急</c:if>
									<c:if test="${complain.degree== '3'}">普通</c:if>
					            </td>														
								<td>${complain.creatBy}</td>
								<td>${complain.handler}</td>
								<td>
									<c:if test="${complain.status== '1'}">未处理</c:if>
								    <c:if test="${complain.status== '2'}">已处理</c:if>
					            </td>								
								<td>${complain.changeTimes}</td>
								<td>
									<a href="#" onclick="openDialogView('详情', '${ctx}/crm/store/detailed?id=${complain.id}&stamp=${stamp}','800px','680px')"  class="btn btn-info btn-xs" ><i class="fa fa-search-plus"></i>详情</a>
								</td>
 							</tr>
						</c:forEach>
					</tbody>
				</table>
				<!-- 分页代码 -->
				<table:page page="${page}"></table:page>
			</div>
		</div>
	</div>
</body>
</html>