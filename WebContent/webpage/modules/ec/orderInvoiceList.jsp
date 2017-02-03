<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/webpage/include/taglib.jsp"%>

<html>
<head>
<title>发票列表</title>
<meta name="decorator" content="default" />

<script type="text/javascript">
	function page(n, s) {
		$("#pageNo").val(n);
		$("#pageSize").val(s);
		$("#searchForm").submit();
		return false;
	}
	
	
	
	
</script>
</head>




<body class="gray-bg">
	<div class="wrapper wrapper-content">
		<div class="ibox">
			<div class="ibox-title">
				<h5>发票列表</h5>
			</div>
			<sys:message content="${message}" />
			<!-- 查询条件 -->
			<div class="ibox-content">
				<div class="clearfix">
					<form:form id="searchForm" modelAttribute="orderInvoice" action="${ctx}/ec/invoice/list" method="post" class="form-inline">
						<input id="pageNo" name="pageNo" type="hidden" value="${page.pageNo}"/>
						<input id="pageSize" name="pageSize" type="hidden" value="${page.pageSize}"/>
						<div class="form-group">
							<form:input path="userName" htmlEscape="false" maxlength="50" class=" form-control input-sm" placeholder="用户名"/>
							<form:input path="mobile" htmlEscape="false" maxlength="50" class=" form-control input-sm" placeholder="手机号"/>
							<label>发票类型：</label>
								<form:select path="invoiceType"  class="form-control" style="width:185px;">
									<form:option value="0">请选择类型</form:option>
									<form:option value="1">个人普票</form:option>
									<form:option value="2">公司普通</form:option>
									<form:option value="3">公司专票</form:option>
								</form:select>
						</div>
					</form:form>
					<!-- 工具栏 -->
					<div class="row" style="padding-top: 10px;">
						<div class="col-sm-12">
							<div class="pull-left">
								<shiro:hasPermission name="ec:invoice:add">
									<%-- <table:addRow url="${ctx}/ec/orders/createOrder" title="订单添加" width="750px" height="650px" title="创建红包"></table:addRow><!-- 增加按钮 --> --%>
									<a href="#" onclick="openDialog('创建发票', '${ctx}/ec/invoice/createForm','800px','600px')" class="btn btn-white btn-sm"><i class="fa fa-plus"></i>创建发票</a>
								</shiro:hasPermission>
								<shiro:hasPermission name="ec:invoice:export">
									<table:exportExcel url="${ctx}/ec/invoice/export"></table:exportExcel>
								</shiro:hasPermission>
							</div>
							<div class="pull-right">
								<button  class="btn btn-primary btn-rounded btn-outline btn-sm " onclick="search()" ><i class="fa fa-search"></i> 查询</button>
								<button  class="btn btn-primary btn-rounded btn-outline btn-sm " onclick="reset()" ><i class="fa fa-refresh"></i> 重置</button>
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
							<th style="text-align: center;">时间</th>
							<th style="text-align: center;">用户名</th>
							<th style="text-align: center;">手机号</th>
							<th style="text-align: center;">发票类型</th>
							<th style="text-align: center;">发票抬头</th>
							<th style="text-align: center;">发票内容</th>
							<th style="text-align: center;">发票金额</th>
							<th style="text-align: center;">发票状态</th>
							<th style="text-align: center;">邮寄地址</th>
							<th style="text-align: center;">操作</th>
						</tr>
					</thead>
					<tbody style="text-align: center;">
						<c:forEach items="${page.list}" var="Invoice">
							<tr>
								<td>${Invoice.id}</td>
								<td><fmt:formatDate value="${Invoice.createDate}" pattern="yyyy-MM-dd HH:mm:ss" /></td>
								<td>${Invoice.userName}</td>
								<td>${Invoice.mobile}</td>
								<td>
									<c:if test="${Invoice.invoiceType==1}">
										个人普票
									</c:if>
									<c:if test="${Invoice.invoiceType==2}">
										公司普通
									</c:if>
									<c:if test="${Invoice.invoiceType==3}"> 
										公司专票
									</c:if>
								</td>
								<td>${Invoice.headContent}</td>
								<td>${Invoice.invoiceContent}</td>
								<td>${Invoice.invoiceAmount}</td>
								<td>
									<c:if test="${Invoice.invoiceStatus==0}">
										已填写
									</c:if>
									<c:if test="${Invoice.invoiceStatus==1}">
										已发出
									</c:if>
								</td>
								<td>${Invoice.recipientsAddress}</td>
								<td>
									<shiro:hasPermission name="ec:invoice:view">
										<a href="#" onclick="openDialogView('发票信息', '${ctx}/ec/invoice/form?id=${Invoice.id}','800px','600px')"
											class="btn btn-info btn-xs"><i class="fa fa-search-plus"></i>查看</a>
									</shiro:hasPermission> 
									<shiro:hasPermission name="ec:invoice:edit">
										<a href="#" onclick="openDialog('编辑发票', '${ctx}/ec/invoice/form?id=${Invoice.id}','800px','600px')" class="btn btn-success btn-xs"><i class="fa fa-file"></i>修改发票</a>
									</shiro:hasPermission>
									<shiro:hasPermission name="ec:invoice:del">
										<a href="${ctx}/ec/invoice/delInvoic?id=${Invoice.id}" onclick="return confirmx('确认删除吗？', this.href)" class="btn btn-danger btn-xs"><i class="fa fa-save"></i>删除发票</a>
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