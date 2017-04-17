<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/webpage/include/taglib.jsp"%>
<html>
<head>
	<title>技能标签管理</title>
	<meta name="decorator" content="default"/>
	<link rel="stylesheet" href="${ctxStatic}/ec/css/loading.css">
	<script type="text/javascript">
		//刷新
		function refresh(){
			window.location="${ctx}/ec/equipmentLabel/list";
		}
		
		function promptx(title, lable, href, closed) {
			top.layer.prompt({
				title : title,
				maxlength : 100,
				formType : 2
			//prompt风格，支持0-2  0 文本框  1 密码框 2 多行文本
			}, function(pass) {
				var nowhref = href + '&remarks=' + pass;
				confirmx(lable, nowhref, closed);
			});
			return false;
		}
		
		//是否显示
		function changeTableVal(id,isyesno){
			$(".loading").show();//打开展示层
			$.ajax({
				type : "POST",
				url : "${ctx}/ec/equipmentLabel/changeIsShow?isShow="+isyesno+"&equipmentLabelId="+id,
				dataType: 'json',
				success: function(data) {
					$(".loading").hide(); //关闭加载层
					var status = data.STATUS;
					var isyesno = data.ISYESNO;
					if("OK" == status){
						$("#isShow"+id).html("");//清除DIV内容
						if(isyesno == '1'){
							$("#isShow"+id).append("<img width='20' height='20' src='${ctxStatic}/ec/images/cancel.png' onclick=\"changeTableVal('"+id+"','0')\">");
						}else if(isyesno == '0'){
							$("#isShow"+id).append("<img width='20' height='20' src='${ctxStatic}/ec/images/open.png' onclick=\"changeTableVal('"+id+"','1')\">");
						}
					}else if("ERROR" == status){
						alert(data.MESSAGE);
					}
				}
			});   
		}
	</script>
</head>
<body>
	<div class="wrapper wrapper-content">
		<div class="ibox">
			<!-- 标题 -->
			<div class="ibox-title">
				<h5>设备标签管理</h5>
			</div>
			<!-- 主体内容 -->
			<div class="ibox-content">
				<sys:message content="${message}" />
				<!-- 查询条件 -->
				<div class="row">
					<div class="col-sm-12">
						<form:form id="searchForm" modelAttribute="equipmentLabel" action="${ctx}/ec/equipmentLabel/list" method="post" class="form-inline">
							<input id="pageNo" name="pageNo" type="hidden" value="${page.pageNo}"/>
							<input id="pageSize" name="pageSize" type="hidden" value="${page.pageSize}"/>
							<table:sortColumn id="orderBy" name="orderBy" value="${page.orderBy}" callback="sortOrRefresh();"/><!-- 支持排序 -->
						</form:form>
						<br />
					</div>
				</div>
				<!-- 工具栏 -->
				<div class="row">
					<div class="col-sm-12">
						<div class="pull-left">
							<shiro:hasPermission name="ec:equipmentLabel:add">
								<table:addRow url="${ctx}/ec/equipmentLabel/form?flag=1" title="设备标签" width="600px" height="400px" ></table:addRow><!-- 增加按钮 -->
							</shiro:hasPermission>
							<button class="btn btn-white btn-sm " data-toggle="tooltip" data-placement="left" onclick="refresh()" title="刷新">
								<i class="glyphicon glyphicon-repeat"></i> 刷新
							</button>
						</div>
					</div>
				</div>
				<!-- 主要内容展示 -->
				<table id="contentTable" class="table table-striped table-bordered table-hover table-condensed dataTables-example dataTable no-footer">
					<thead>
						<tr>
							<th style="text-align: center;">ID</th>
							<th style="text-align: center;">标签名称</th>
							<th style="text-align: center;">标签编号</th>
							<th style="text-align: center;">标签类型</th>
							<th style="text-align: center;">标签描述</th>
							<th style="text-align: center;">是否显示</th>
							<th style="text-align: center;">操作</th>
						</tr>
					</thead>
					<c:forEach items="${page.list}" var="equipmentLabel">
						<tr>
							<td style="text-align: center;">${equipmentLabel.equipmentLabelId }</td>
							<td style="text-align: center;">${equipmentLabel.name}</td>
							<td style="text-align: center;">${equipmentLabel.no}</td>
							<td style="text-align: center;">
								<c:if test="${equipmentLabel.type == 1}" >特殊</c:if>
								<c:if test="${equipmentLabel.type == 2}" >通用</c:if>
							</td>
							<td align="center">
								<div style="width:200px;white-space:nowrap; overflow:hidden; text-overflow:ellipsis;">${equipmentLabel.description}</div>
							</td>
							<td style="text-align: center;" id="isShow${equipmentLabel.equipmentLabelId}">
								<c:if test="${equipmentLabel.isShow == '1'}">
									<img width="20" height="20" src="${ctxStatic}/ec/images/cancel.png" onclick="changeTableVal('${equipmentLabel.equipmentLabelId}','0')">
								</c:if>
								<c:if test="${equipmentLabel.isShow == '0'}">
									<img width="20" height="20" src="${ctxStatic}/ec/images/open.png" onclick="changeTableVal('${equipmentLabel.equipmentLabelId}','1')">
								</c:if>
							</td>
							<td style="text-align: center;">
								<shiro:hasPermission name="ec:equipmentLabel:view">
									<a href="#" onclick="openDialogView('查看设备标签', '${ctx}/ec/equipmentLabel/form?equipmentLabelId=${equipmentLabel.equipmentLabelId}&flag=2','600px','400px')" class="btn btn-info btn-xs" ><i class="fa fa-search-plus"></i> 查看</a>
								</shiro:hasPermission>
								<c:if test="${equipmentLabel.equipmentLabelId != 100000}">
								<%-- <shiro:hasPermission name="ec:equipmentLabel:edit">
									<a href="#" onclick="openDialog('编辑设备标签', '${ctx}/ec/equipmentLabel/form?equipmentLabelId=${equipmentLabel.equipmentLabelId}','600px','400px')" class="btn btn-success btn-xs" ><i class="fa fa-edit"></i> 编辑</a>
								</shiro:hasPermission> --%>
								<shiro:hasPermission name="ec:equipmentLabel:del">
									<a href="${ctx}/ec/equipmentLabel/delete?equipmentLabelId=${equipmentLabel.equipmentLabelId}" onclick="return promptx('请填写删除备注信息！不可为空！','确定要删除标签吗？',this.href)" class="btn btn-danger btn-xs"><i class="fa fa-trash"></i> 删除</a>
								</shiro:hasPermission>
								</c:if>
							</td>
						</tr>
					</c:forEach>
				</table>
				<!-- 分页table -->
				<table:page page="${page}"></table:page>
			</div>
		</div>
		<div class="loading"></div>
	</div>
</body>
</html>