<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/webpage/include/taglib.jsp"%>
<html>
<head>
<title>B级用户</title>
<meta name="decorator" content="default" />
<link rel="stylesheet" type="text/css" href="${ctxStatic}/common/training.css">
<script type="text/javascript">
	function nowReset(){
		$("#pageNo").val(0);
		$("#searchForm div.form-group input").val("");
		$("#searchForm div.form-group select").val("");
		$("#searchForm").submit();
	}
	function move(userId,parentId){
		$("#userId").val(userId);
		$("#parentId").val(parentId);
		$('#modal').modal("show");
	}
	$(document).ready(function() {
	    var startTime = {
		    elem: '#startTime',
		    format: 'YYYY-MM-DD',
		    event: 'focus',
		    max: $("#endTime").val(),   //最大日期
		    istime: false,				//是否显示时间
		    isclear: false,				//是否显示清除
		    istoday: false,				//是否显示今天
		    issure: true,				//是否显示确定
		    festival: true,				//是否显示节日
		    choose: function(datas){
		    	endTime.min = datas; 		//开始日选好后，重置结束日的最小日期
		    	endTime.start = datas 		//将结束日的初始值设定为开始日
		    }
		};
		var endTime = {
		    elem: '#endTime',
		    format: 'YYYY-MM-DD',
		    event: 'focus',
		    min: $("#startTime").val(),
		    istime: false,
		    isclear: false,
		    istoday: false,
		    issure: true,
		    festival: true,
		    choose: function(datas){
		    	startTime.max = datas; //结束日选好后，重置开始日的最大日期
		    }
		};
		laydate(startTime);
		laydate(endTime);
    })
</script>
<style type="text/css">
	.modal-content{
		margin:0 auto;
		width: 350px;
	}
</style>
</head>
<body>
	<div class="wrapper wrapper-content">
		<div class="ibox">
			<!-- 标题 -->
			<div class="ibox-title">
				<h5>B级用户</h5>
			</div>
			<!-- 主体内容 -->
			<div class="ibox-content">
				<sys:message content="${message}" />
				<!-- 查询条件 -->
				<div class="row">
					<div class="col-sm-12">
						<form:form id="searchForm" modelAttribute="mtmySaleRelieve" action="${ctx}/ec/mtmySale/findOtherUsers" method="post" class="form-inline">
							<input id="pageNo" name="pageNo" type="hidden" value="${page.pageNo}" />
							<input id="pageSize" name="pageSize" type="hidden" value="${page.pageSize}" />
							<table:sortColumn id="orderBy" name="orderBy" value="${page.orderBy}" callback="sortOrRefresh();" />
							<!-- 支持排序 -->
							<div class="form-group">
								<form:input path="newusers.nickname" htmlEscape="false" maxlength="50" placeholder="邀请人/邀请人手机号" class=" form-control input-sm" />
								<form:input path="tag" htmlEscape="false" maxlength="50" placeholder="用户姓名/手机号" class=" form-control input-sm" />
								<label>申请时间:</label>
								<input id="startTime" name="startTime" type="text" maxlength="20" class="laydate-icon form-control layer-date input-sm" value="<fmt:formatDate value="${mtmySaleRelieve.startTime }" pattern="yyyy-MM-dd"/>" placeholder="开始时间" readonly="readonly"/>
								<label>--</label>
								<input id="endTime" name="endTime" type="text" maxlength="20" class=" laydate-icon form-control layer-date input-sm" value="<fmt:formatDate value="${mtmySaleRelieve.endTime }" pattern="yyyy-MM-dd"/>" placeholder="结束时间" readonly="readonly"/>
							</div>
							<div class="pull-right" style="margin-left: 50px;">
								<button  class="btn btn-primary btn-rounded btn-outline btn-sm " onclick="search()" ><i class="fa fa-search"></i> 查询</button>
								<button  class="btn btn-primary btn-rounded btn-outline btn-sm " onclick="nowReset()" ><i class="fa fa-refresh"></i> 重置</button>
							</div>
						</form:form>
					</div>
				</div>
				<p></p>
				<!-- 主要内容展示 -->
				<table id="contentTable" class="table table-striped table-bordered table-hover table-condensed dataTables-example dataTable">
					<thead>
						<tr>
							<th>级别</th>
							<th>用户名</th>
							<th>注册手机号</th>
							<th>邀请人</th>
							<th>邀请人手机号</th>
							<th>邀请人级别</th>
							<th class="sort-column r.create_date">受邀成功时间</th>
							<th class="sort-column rc.ABnum">AB级用户</th>
							<th>剩余AB级用户</th>
							<th class="sort-column uu.user_balance">总余额</th>
							<th class="sort-column uu.user_integral">总云币</th>
							<th>用户状态</th>
							<th>操作</th>
						</tr>
					</thead>
					<tbody>
						<c:forEach items="${page.list}" var="mtmySaleRelieve">
							<tr>
								<td>${mtmySaleRelieve.users.layer}</td>
								<td>${mtmySaleRelieve.users.nickname}</td>
								<td>${mtmySaleRelieve.users.mobile}</td>
								<td>
									<!-- 每天美耶用户: -->${mtmySaleRelieve.newusers.nickname}
									<%-- <c:if test="${mtmySaleRelieve.user.name != null && mtmySaleRelieve.user.name != ''}">
										<br>妃子校用户:${mtmySaleRelieve.user.name}
									</c:if> --%>
								</td>
								<td>${mtmySaleRelieve.newusers.mobile}</td>
								<td>${mtmySaleRelieve.newusers.layer}</td>
								<th><fmt:formatDate value="${mtmySaleRelieve.createDate}" pattern="yyyy-MM-dd HH:mm:ss"/></th>
								<td>${mtmySaleRelieve.ABnum }</td>
								<td>
									<c:if test="${BCmtmyRuleParam.paramValue - mtmySaleRelieve.ABnum < 0}">
										0
									</c:if>
									<c:if test="${BCmtmyRuleParam.paramValue - mtmySaleRelieve.ABnum >= 0}">
										${BCmtmyRuleParam.paramValue - mtmySaleRelieve.ABnum }
									</c:if>
								</td>
								<td><fmt:formatNumber type="number" value="${mtmySaleRelieve.users.usermoney}" maxFractionDigits="2"/></td>
								<td><fmt:formatNumber type="number" value="${mtmySaleRelieve.users.paypoints}" maxFractionDigits="2"/></td>
								<th>
									<c:if test="${mtmySaleRelieve.status == 1}">
										申请解除中
									</c:if>
									<c:if test="${mtmySaleRelieve.status != 1}">
										正常
									</c:if>
								</th>
								<td>
									<a href="#" onclick='top.openTab("${ctx}/ec/mtmySale/otherUserFrom?users.userid=${mtmySaleRelieve.users.userid}","B级用户详情", false)' class="btn btn-info btn-xs" ><i class="fa fa-search-plus"></i> 查看</a>
									<a href="#" onclick="move(${mtmySaleRelieve.users.userid},${mtmySaleRelieve.newusers.userid})" class="btn btn-info btn-xs btn-danger"><i class="fa fa-share"></i> 移动到</a>
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
	<div class="modal fade bs-example-modal-lg in" id="modal" tabindex="-1" role="dialog" aria-labelledby="myLargeModalLabel" style="display: none; padding-right: 17px;">
   		<form id="" class="modal-dialog modal-lg" action="${ctx}/ec/mtmySale/move">
   			<input name="parentId" id="parentId" type="hidden">
   			<input name="userId" id="userId" type="hidden">
   			<input name="moveType" id="moveType" value="BList" type="hidden">
     		<div class="modal-content">
       			<div class="modal-header">
       				<span>选择转移后用户上级</span>
         			<button type="button" class="close" data-dismiss="modal" aria-label="Close"><span aria-hidden="true">×</span></button>
       			</div>
       			<div class="modal-body">
       				<table>
       					<c:forEach items="${SNmtmyRuleParam }" var="SNmtmyRuleParam" varStatus="status">
							<tr>
						    	<td><input type="radio" id="newParentId" name="newParentId"  ${(status.index == '0')?'checked="checked"':''} value="${SNmtmyRuleParam.paramValue }">&nbsp;&nbsp;${SNmtmyRuleParam.paramExplain }<td>
						    </tr>
       					</c:forEach>
					</table>
       			</div>
       			<div class="modal-footer">
       				<input type="submit" class="btn btn-success" value="保 存">
					<a href="#" class="btn btn-primary" data-dismiss="modal">关   闭</a>
       			</div>
     		</div>
   		</form>
 	</div>
</body>
</html>