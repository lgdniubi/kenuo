<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/webpage/include/taglib.jsp"%>

<html>
<head>
<title>机构管理</title>
	<meta name="decorator" content="default"/>
	<%@ include file="/webpage/include/treetable.jsp"%>
	<script type="text/javascript">
		//页面加载
		$(document).ready(function() {
			//树形table配置
			var option = {
                expandLevel : 2
            };
			//树形table
			$("#treeTable").treeTable(option);
	    });
	
		//刷新或者排序，页码不清零
		function refresh(){
			window.location="${ctx}/sys/office/list";
    	}    
	</script>
</head>
<body>
	<div class="wrapper wrapper-content">
		<div class="ibox">
			<!-- 标题 -->
			<div class="ibox-title">
				<h5>机构列表</h5>
			</div>
			<!-- 主体内容 -->
			<div class="ibox-content">
				<sys:message content="${message}" />
				<!-- 查询条件 -->
				<div class="row">
					<div class="col-sm-12">
						<!-- 无查询条件 -->
						<form:form id="searchForm" modelAttribute="" action="${ctx}/sys/office/export" method="post" class="form-inline"></form:form>
					</div>
				</div>
				<!-- 工具栏 -->
				<div class="row">
					<div class="col-sm-12">
						<div class="pull-left">
							<!-- update 导入数据 -->
							<shiro:hasPermission name="sys:office:importPage">
								<a href="#" onclick="openDialog('导入数据', '${ctx}/sys/office/importPage','400px', '220px')" class="btn btn-white btn-sm"><i class="fa fa-folder-open-o"></i> 导入</a>
							</shiro:hasPermission>
							<shiro:hasPermission name="sys:office:export">
								<!-- 导出按钮 -->
								<table:exportExcel url="${ctx}/sys/office/export"></table:exportExcel>
							</shiro:hasPermission>
							<button class="btn btn-white btn-sm " data-toggle="tooltip" data-placement="left" onclick="refresh()" title="刷新">
								<i class="glyphicon glyphicon-repeat"></i> 刷新
							</button>
						</div>
					</div>
				</div>
				<table id="treeTable" class="table table-striped table-bordered table-hover table-condensed dataTables-example dataTable">
					<thead>
						<tr>
							<th>机构名称</th>
							<th>归属区域</th>
							<th>机构编码</th>
							<th>机构类型</th>
							<th>备注</th>
							<th>操作</th>
						</tr>
					</thead>
					<tbody>
						<c:forEach items="${list}" var="row" varStatus="status">
							<c:if test="${row.num>0}">
								<tr id="${row.id}" pid="0">
							</c:if>
							<c:if test="${row.num == 0}">
								<tr id="${row.id}" pid="${row.parent.id}">
							</c:if>
								<td nowrap style="text-align: left;">
									<a href="#" onclick="openDialogView('查看机构', '${ctx}/sys/office/form?id=${row.id}','800px', '620px')">${row.name}</a>
								</td>
								<td>${row.area.name}</td>
								<td>${row.code}</td>
								<td>${fns:getDictLabel(row.type, 'sys_office_type', '')}</td>
								<td>${row.remarks}</td>
								<td>
									<shiro:hasPermission name="sys:office:view">
										<c:choose>
											<c:when test="${row.parent.id =='0' && row.code !='10001'}">
												<a href="#" onclick="openDialogView('查看机构', '${ctx}/sys/franchisee/form?id=${row.id}&opflag=VIEW','800px', '620px')" class="btn btn-info btn-xs"><i class="fa fa-search-plus"></i> 查看</a>
											</c:when>
											<c:otherwise>
												<a href="#" onclick="openDialogView('查看机构', '${ctx}/sys/office/form?id=${row.id}&&num=${row.num}','800px', '620px')" class="btn btn-info btn-xs"><i class="fa fa-search-plus"></i> 查看</a>
											</c:otherwise>
										</c:choose>
									</shiro:hasPermission> 
									<c:if test="${row.parent.id !='0'}">
										<shiro:hasPermission name="sys:office:edit">
											<a href="#" onclick="openDialog('修改机构', '${ctx}/sys/office/form?id=${row.id}&&num=${row.num}','800px', '620px', 'officeContent')" class="btn btn-success btn-xs"><i class="fa fa-edit"></i> 修改</a>
										</shiro:hasPermission>
										<shiro:hasPermission name="sys:office:del">
											<a href="${ctx}/sys/office/delete?id=${row.id}" onclick="return confirmx('要删除该机构及所有子机构项吗？', this.href)" class="btn btn-danger btn-xs"><i class="fa fa-trash"></i> 删除</a>
										</shiro:hasPermission>
									</c:if> 
									<c:if test="${row.grade != '1'}">
										<shiro:hasPermission name="sys:office:add">
											<a href="#" onclick="openDialog('添加下级机构', '${ctx}/sys/office/form?parent.id=${row.id}','800px', '620px', 'officeContent')" class="btn  btn-primary btn-xs"><i class="fa fa-plus"></i> 添加下级机构</a>
										</shiro:hasPermission>
									</c:if>
									<c:if test="${row.grade == '1'}">
										<a href="#" onclick="openDialogView('添加通用设备', '${ctx}/ec/specEquipment/shopComEquipmentList?shopId=${row.id}','800px', '620px', 'officeContent')" class="btn  btn-primary btn-xs"><i class="fa fa-plus"></i> 设备</a>
									</c:if> 
									<c:if test="${row.grade == '1'}">
										<shiro:hasPermission name="train:arrange:ArrangeOrdinary">
											<a href="#" onclick='top.openTab("${ctx }/train/arrange/ArrangeOrdinary?officeId=${row.id}&officeName=${row.name}","美容师排班", false)' class="btn btn-success btn-xs" >美容师排班</a>
										</shiro:hasPermission>
									</c:if>
								</td>
							</tr>
						</c:forEach>
					</tbody>
				</table>
			</div>
		</div>
	</div>
</body>
</html>