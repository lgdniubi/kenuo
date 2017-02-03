<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/webpage/include/taglib.jsp"%>
<html>
<head>
	<title>学生课程预约管理</title>
	<meta name="decorator" content="default" />
	<link rel="stylesheet" href="${ctxStatic}/train/css/addcourse.css">
	<link rel="stylesheet" href="${ctxStatic}/bootstrap-switch/lc_switch.css">
  	<script type="text/javascript" src="${ctxStatic}/bootstrap-switch/js/lc_switch.js"></script>
	<%-- <script type="text/javascript" src="${ctxStatic}/train/js/jquery-1.11.3.js"></script> --%>
	<script type="text/javascript">
		//页面加载时间
		$(document).ready(function() {
			$(".pd").lc_switch("终止","发布");
		});
		
		//分页按钮
		function page(n,s){
			$("#pageNo").val(n);
			$("#pageSize").val(s);
			$("#searchForm").submit();
	    	return false;
	    }
		//刷新
		function refresh(){
			$("#searchForm").submit();
			return false;
		}
		
		//预约时间开关控制
		function switchChange(v){
			var switchval = $("#switchval_"+v).val(); 
			$.ajax({
				url : "${ctx}/train/subscribe/subscribeupdatestatus?subscribeId="+v+"&status="+switchval,
				type : "post",
				dataType : "json",
				success :function(data) {
					if(data.STATUS == 'SUCCESS'){
						if(switchval == '1'){
							$("#"+v).html("终止");
							$("#switchval_"+v).val("0");
						}else if(switchval == '0'){
							$("#"+v).html("发布");
							$("#switchval_"+v).val("1");
						}
					}else if(data.STATUS == 'ERROR'){
						alert(data.MESSAGE);
					}
				}
			});
		}
	</script>
</head>
<body>
	<div class="ibox-content">
		<sys:message content="${message}"/>
		<div class="warpper-content">	
			<div class="ibox">
				<div class="ibox-title">
					<h5>${trainOfflineTestSubscribe.trainCategorys.name}</h5>-线下预约列表
				</div>
				<div class="ibox-content" style="width: 85%;">
					<form:form id="searchForm" action="${ctx}/train/studentsubscribe/findsetlist" modelAttribute="trainOfflineTestSubscribe" method="post">
						<input type="hidden" id="categoryId" name="categoryId" value="${trainOfflineTestSubscribe.categoryId}">
						<!-- 分页必要字段 -->
						<input id="pageNo" name="pageNo" type="hidden" value="${page.pageNo}"/>
						<input id="pageSize" name="pageSize" type="hidden" value="${page.pageSize}"/>
					</form:form>
					<!-- 工具栏 -->
					<div class="row" style="padding-bottom: 5px;">
						<div class="col-sm-12">
							<div class="pull-left">
								<c:if test="${findStatusList.size() < 4}">
									<shiro:hasPermission name="train:subscribe:savesubscribe">
										<table:addRow url="${ctx}/train/subscribe/addsubscribe?categoryId=${trainOfflineTestSubscribe.categoryId}" width="500px" height="550px" title="课程线下预约"></table:addRow><!-- 增加按钮 -->
									</shiro:hasPermission>
								</c:if>
								<button class="btn btn-white btn-sm " data-toggle="tooltip" data-placement="left" onclick="refresh()" title="刷新"><i class="glyphicon glyphicon-repeat"></i> 刷新</button>
							</div>
						</div>
					</div>
					<table id="treeTable" class="table table-bordered table-hover table-striped">
						<thead>
							<tr>
								<th width="120" style="text-align: center;">课程分类</th>
								<th width="200" style="text-align: center;">预约时间</th>
								<th width="200" style="text-align: center;">状态</th>
								<th width="200" style="text-align: center;">预约用户数量</th>
								<th width="200" style="text-align: center;">操作</th>
							</tr>
						</thead>
						<tbody>
							<c:forEach items="${page.list}" var="trainOfflineTestSubscribe">
								<c:if test="${trainOfflineTestSubscribe.trainOfflineSubscribeTime.status == 0}">
									<tr>
										<td>${trainOfflineTestSubscribe.trainCategorys.name }</td>
										<td><fmt:formatDate value="${trainOfflineTestSubscribe.subscribeTime }" pattern="yyyy-MM-dd HH:mm"/></td>
										<td id="${trainOfflineTestSubscribe.subscribeId}">
											发布
										</td>
										<td>
											<c:if test="${trainOfflineTestSubscribe.userNum  != 0}">
												<a href="#" onclick='top.openTab("${ctx}/train/studentsubscribe/findlist?categoryId=${trainOfflineTestSubscribe.categoryId }&subscribeId=${trainOfflineTestSubscribe.subscribeId }","学生预约详情", false)'>${trainOfflineTestSubscribe.userNum }</a>
											</c:if>
											<c:if test="${trainOfflineTestSubscribe.userNum  == 0}">
												0
											</c:if>
										</td>
										<td>
											<input type="hidden" id="switchval_${trainOfflineTestSubscribe.subscribeId}" value="1">
											<a href="javascript:switchChange('${trainOfflineTestSubscribe.subscribeId}');">
	    							 			<input type="checkbox" checked="checked" name="check" class="pd" autocomplete="off"/>
	    							 		</a>
										</td>
									</tr>
								</c:if>
								<c:if test="${trainOfflineTestSubscribe.trainOfflineSubscribeTime.status == 1}">
									<tr style="color: red;">
										<td>${trainOfflineTestSubscribe.trainCategorys.name }</td>
										<td><fmt:formatDate value="${trainOfflineTestSubscribe.subscribeTime }" pattern="yyyy-MM-dd HH:mm"/></td>
										<td id="${trainOfflineTestSubscribe.subscribeId}">终止</td>
										<td>
											<c:if test="${trainOfflineTestSubscribe.userNum != 0}">
												<a style="color: red;" href="#" onclick='top.openTab("${ctx}/train/studentsubscribe/findlist?categoryId=${trainOfflineTestSubscribe.categoryId }&subscribeId=${trainOfflineTestSubscribe.subscribeId }","学生预约详情", false)'>${trainOfflineTestSubscribe.userNum }</a>
											</c:if>
											<c:if test="${trainOfflineTestSubscribe.userNum == 0}">
												0
											</c:if>
										</td>
										<td>
											<%-- 
											<input type="hidden" id="switchval_${trainOfflineTestSubscribe.subscribeId}" value="0">
											<a href="javascript:switchChange('${trainOfflineTestSubscribe.subscribeId}');">
	    							 			<input type="checkbox" name="check" class="pd" autocomplete="off"/>
	    							 		</a> 
	    							 		--%>
										</td>
									</tr>
								</c:if>
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
	</div>
</body>
</html>