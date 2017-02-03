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
			<div class="ibox-title">
				<h5>运费列表</h5>
			</div>
			<sys:message content="${message}" />
			<!-- 查询条件 -->
			<div class="ibox-content">
				<div class="clearfix">
					<!-- 工具栏 -->
					<div class="row" style="padding-top: 10px;">
						<div class="col-sm-12">
							<div class="pull-left">
								<shiro:hasPermission name="ec:wareHouse:add">
									<a href="#"
										onclick="openDialog('新增运费模板', '${ctx}/ec/pdTemplate/form?houseId=${wareHouseId }','1200px','600px')"
										class="btn btn-white btn-sm"><i class="fa fa-plus"></i>新增运费模板</a>
								</shiro:hasPermission>
								<p></p>
							</div>
						</div>
					</div>
				</div>
				<c:forEach items="${page.list }" var="template">
					<table id="contentTable" style="text-align: center;margin-bottom:30px;max-width:1440px;" class="table table-striped table-bordered  table-hover table-condensed  dataTables-example dataTable no-footer">
						<tr style="background-color: #5553;">
							<td colspan="6">
								<label class="pull-left">${template.templateName }</label>
									<label class="pull-right">最后编辑时间：<fmt:formatDate value="${template.updateDate }" pattern="yyyy-MM-dd"/>&nbsp;
									<a href="${ctx}/ec/pdTemplate/copy?templateId=${template.templateId }&houseId=${wareHouseId }">复制模板</a>&nbsp;|&nbsp;
									<a href="#" onclick="openDialog('修改运费模板', '${ctx}/ec/pdTemplate/modifiedPage?templateId=${template.templateId }&houseId=${wareHouseId }','1200px','600px')">修改</a>&nbsp;|&nbsp;
									<a href="${ctx}/ec/pdTemplate/delete?templateId=${template.templateId }&houseId=${wareHouseId }" onclick="return promptx('请填写删除备注信息！不可为空！','确定要删除模板吗？',this.href)">
										<i class="fa fa-trash"></i> 删除
									</a>&nbsp;|&nbsp;
									<c:if test="${template.isDefault == 1}">
										通用模板
									</c:if>
									<c:if test="${template.isDefault == 0}">
										<a href="${ctx}/ec/pdTemplate/updateStatus?templateId=${template.templateId }&houseId=${wareHouseId }" >设为通用</a>
									</c:if>
								</label>
							</td>
						</tr>
						<tr>
							<th style="text-align: center;width:35%">运送到</th>
							<th style="text-align: center;">首重(g)</th>
							<th style="text-align: center;">运费(元)</th>
							<th style="text-align: center;">续重(g)</th>
							<th style="text-align: center;">运费(元)</th>
						</tr>
						<c:forEach items="${template.templatePrices }" var="templatePrice">
							<tr>
								<td>
									<c:forEach items="${templatePrice.templateAreas }" var="templateArea">
										${templateArea.name }
									</c:forEach>
								</td>
								<td>${templatePrice.firstWeight }</td>
								<td>${templatePrice.firstPrice }</td>
								<td>${templatePrice.addWeight }</td>
								<td>${templatePrice.addPrice }</td>
							</tr>
						</c:forEach>
					</table>
				</c:forEach>
			</div>
		</div>
	</div>
</body>
</html>