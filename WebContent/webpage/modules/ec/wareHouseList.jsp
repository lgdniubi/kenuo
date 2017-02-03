<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/webpage/include/taglib.jsp"%>

<html>
<head>
<title>仓库管理列表</title>
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
			elem : '#startTime',
			format : 'YYYY-MM-DD',
			event : 'focus',
			max : $("#endTime").val(), //最大日期
			istime : false, //是否显示时间
			isclear : true, //是否显示清除
			istoday : true, //是否显示今天
			issure : true, //是否显示确定
			festival : true, //是否显示节日
			choose : function(datas) {
				end.min = datas; //开始日选好后，重置结束日的最小日期
				end.start = datas //将结束日的初始值设定为开始日
			}
		};
		var end = {
			elem : '#endTime',
			format : 'YYYY-MM-DD',
			event : 'focus',
			min : $("#startTime").val(),
			istime : false,
			isclear : true,
			istoday : true,
			issure : true,
			festival : true,
			choose : function(datas) {
				start.max = datas; //结束日选好后，重置开始日的最大日期
			}
		};
		laydate(start);
		laydate(end);

	});
	
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
</script>
</head>




<body class="gray-bg">
	<div class="wrapper wrapper-content">
		<div class="ibox">
			<sys:message content="${message}" />
			<!-- 查询条件 -->
			<div class="ibox-content">
				<div class="clearfix">
					<form:form id="searchForm" modelAttribute="courier"
						action="${ctx}/ec/wareHouse/list" method="post" class="form-inline">
						<input id="pageNo" name="pageNo" type="hidden"
							value="${page.pageNo}" />
						<input id="pageSize" name="pageSize" type="hidden"
							value="${page.pageSize}" />
						<div class="form-group">
							<label>创建日期：</label>
							<input id="startTime" name="timeStart" type="text" maxlength="20" class="laydate-icon form-control layer-date input-sm"
								value="<fmt:formatDate value="${timeStart}" pattern="yyyy-MM-dd"/>" style="width:185px;" placeholder="开始时间" readonly="readonly"/>
							一
							<input id="endTime" name="timeEnd" type="text" maxlength="20" class=" laydate-icon form-control layer-date input-sm" 
							value="<fmt:formatDate value="${timeEnd}" pattern="yyyy-MM-dd"/>"  style="width:185px;" placeholder="结束时间" readonly="readonly"/>&nbsp;&nbsp;
						</div>
					</form:form>
					<!-- 工具栏 -->
					<div class="row" style="padding-top: 10px;">
						<div class="col-sm-12">
							<div class="pull-left">
								<shiro:hasPermission name="ec:wareHouse:add">
									<a href="#"
										onclick="openDialog('创建仓库', '${ctx}/ec/wareHouse/form','600px','600px')"
										class="btn btn-white btn-sm"><i class="fa fa-plus"></i>创建仓库</a>
								</shiro:hasPermission>
								<p></p>
							</div>
							<div class="pull-right">
								<button  class="btn btn-primary btn-rounded btn-outline btn-sm " onclick="search()" ><i class="fa fa-search"></i> 查询</button>
								<button  class="btn btn-primary btn-rounded btn-outline btn-sm " onclick="reset()" ><i class="fa fa-refresh"></i> 重置</button>
							</div> 
						</div>
					</div>
				</div>
				<table id="contentTable"
					class="table table-striped table-bordered  table-hover table-condensed  dataTables-example dataTable no-footer">
					<thead>
						<tr>
							<th style="text-align: center;">序号</th>
							<th style="text-align: center;">归属商家</th>
							<th style="text-align: center;">库房名称</th>
							<th style="text-align: center;">库房地址</th>
							<th style="text-align: center;">库房管理</th>
							<th style="text-align: center;">联系方式</th>
							<th style="text-align: center;">库房建立日期</th>
							<th style="text-align: center;">备注</th>
							<th style="text-align: center;">操作</th>
						</tr>
					</thead>
					<tbody style="text-align: center;">
						<c:forEach items="${page.list}" var="wareHouse">
							<tr>
								<td>${wareHouse.wareHouseId }</td>
								<td>${wareHouse.fName }</td>
								<td>${wareHouse.name}</td>
								<td>${wareHouse.address}</td>
								<td>${wareHouse.governor}</td>
								<td>${wareHouse.phone}</td>
								<td>
									<fmt:formatDate value="${wareHouse.createDate}" pattern="yyyy-MM-dd" />
								</td>
								<td align="center"><div style="width:100px;white-space:nowrap; overflow:hidden; text-overflow:ellipsis;">${wareHouse.remarks}</div></td>
								<td>
									<shiro:hasPermission name="ec:wareHouse:view">
										<a href="#" onclick='top.openTab("${ctx}/ec/pdTemplate/list?houseId=${wareHouse.wareHouseId}","运费模板", false)' class="btn btn-success btn-xs">
											<i class="fa fa-plus"></i>运费模板
										</a>
									</shiro:hasPermission> 
									<shiro:hasPermission name="ec:wareHouse:edit">
										<a href="#"
											onclick="openDialog('修改仓库', '${ctx}/ec/wareHouse/form?id=${wareHouse.wareHouseId}','900px','600px')"
											class="btn btn-success btn-xs"><i class="fa fa-file"></i>修改</a>
									</shiro:hasPermission> 
									<shiro:hasPermission name="ec:wareHouse:del">
										<a href="${ctx}/ec/wareHouse/delete?id=${wareHouse.wareHouseId}" class="btn btn-danger btn-xs"
											onclick="return promptx('请填写删除备注信息！不可为空！','确定要删除当前仓库吗？',this.href)">
											<i class="fa fa-trash"></i> 删除
										</a>
									</shiro:hasPermission>
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