<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/webpage/include/taglib.jsp"%>

<html>
<head>
	<title>商品属性</title>
	<meta name="decorator" content="default"/>
	<link rel="stylesheet" href="${ctxStatic}/ec/css/loading.css">
	<script type="text/javascript">
		//刷新
		function refresh(){
			window.location="${ctx}/ec/goodsattribute/list";
		}
		
		//是否推荐 改变事件
		function changeTableVal(id,attrIndex){
			$(".loading").show();//打开展示层
			$.ajax({
				type : "POST",   
				url : "${ctx}/ec/goodsattribute/updateattrindex?ID="+id+"&ATTRINDEX="+attrIndex,
				dataType: 'json',
				success: function(data) {
					$(".loading").hide(); //关闭加载层
					var status = data.STATUS;
					var attrIndex = data.ATTRINDEX;
					if("OK" == status){
						$("#attrIndex"+id).html("");//清除DIV内容	
						if(attrIndex == '0'){
							//当前状态为【是】，则取消
							$("#attrIndex"+id).append("<img width='20' height='20' src='${ctxStatic}/ec/images/cancel.png' onclick=\"changeTableVal('"+id+"','1')\">");
						}else if(attrIndex == '1'){
							//当前状态为【否】，则打开
							$("#attrIndex"+id).append("<img width='20' height='20' src='${ctxStatic}/ec/images/open.png' onclick=\"changeTableVal('"+id+"','0')\">");
						}
					}else if("ERROR" == status){
						alert(data.MESSAGE);
					}
				}
			});   
		}
		
		//重置表单
		function resetnew(){
			reset();
		}
		
		//分页按钮
		function page(n,s){
			$("#pageNo").val(n);
			$("#pageSize").val(s);
			$("#searchForm").submit();
	    	return false;
	    }
		
	</script>
</head>
<body>
	<div class="ibox-content">
		<sys:message content="${message}"/>
		<div class="warpper-content">
			<div class="ibox">
				<div class="ibox-title">
					<h5>商品属性管理</h5>
				</div>
				<div class="ibox-content">
					<div class="searcharea clearfix">
						<form:form id="searchForm" action="${ctx}/ec/goodsattribute/list" modelAttribute="goodsAttribute" method="post" class="navbar-form navbar-left searcharea">
							<div class="form-group" style="width: 200%">
								商品类型：
								<select class="form-control" style="width: 30%;text-align: center;" id="typeId" name="typeId">
									<option value="-1">所有类型</option>
									<c:forEach items="${goodsTypeList}" var="goodsType">
										<option ${(goodsType.id == goodsAttribute.typeId)?'selected="selected"':''} value="${goodsType.id}">${goodsType.name}</option>
									</c:forEach>
								</select>
								<shiro:hasPermission name="ec:goodsattribute:list">
									<button type="button" class="btn btn-primary btn-rounded btn-outline btn-sm" onclick="search()">
										<i class="fa fa-search"></i> 搜索
									</button>
									<button type="button" class="btn btn-primary btn-rounded btn-outline btn-sm" onclick="resetnew()" >
										<i class="fa fa-refresh"></i> 重置
									</button>
								</shiro:hasPermission>
								<!-- 分页必要字段 -->
								<input id="pageNo" name="pageNo" type="hidden" value="${page.pageNo}"/>
								<input id="pageSize" name="pageSize" type="hidden" value="${page.pageSize}"/>
							</div>
						</form:form>
					</div>
					<!-- 工具栏 -->
					<div class="row">
						<div class="col-sm-12">
							<shiro:hasPermission name="ec:goodsattribute:add">
								<table:addRow url="${ctx}/ec/goodsattribute/form?opflag=ADDPARENT" width="800px" height="500px" title="商品属性"></table:addRow><!-- 增加按钮 -->
							</shiro:hasPermission>
							<button class="btn btn-white btn-sm " data-toggle="tooltip" data-placement="left" onclick="refresh()" title="刷新"><i class="glyphicon glyphicon-repeat"></i> 刷新</button>
						</div>
					</div>
					<table id="treeTable" class="table table-striped table-bordered table-hover table-condensed dataTables-example dataTable">
						<thead>
							<th style="text-align: center;">ID</th>
							<th style="text-align: center;">属性名称</th>
							<th style="text-align: center;">商品类型</th>
							<th style="text-align: center;">属性值的输入方式</th>
							<th style="text-align: center;">可选值列表</th>
							<th style="text-align: center;">筛选</th>
							<th style="text-align: center;">排序</th>
							<th style="text-align: center;">操作</th>
						</thead>
						<tbody>
							<c:forEach items="${page.list}" var="goodsAttribute">
								<tr style="text-align: center;">
									<td>${goodsAttribute.id}</td>
									<td>${goodsAttribute.attrName}</td>
									<td>${goodsAttribute.goodsType.name}</td>
									<td>${goodsAttribute.attrInputTypeName}</td>
									<td>${goodsAttribute.attrValues}</td>
									<td style="text-align: center;" id="attrIndex${goodsAttribute.id}">
										<c:if test="${goodsAttribute.attrIndex == 0}">
											<img width="20" height="20" src="${ctxStatic}/ec/images/cancel.png" onclick="changeTableVal('${goodsAttribute.id}','1')">
										</c:if>
										<c:if test="${goodsAttribute.attrIndex == 1}">
											<img width="20" height="20" src="${ctxStatic}/ec/images/open.png" onclick="changeTableVal('${goodsAttribute.id}','0')">
										</c:if>
									</td>
									<td>${goodsAttribute.sort}</td>
									<td style="text-align: left;">
										<shiro:hasPermission name="ec:goodsattribute:view">
											<a href="#" onclick="openDialogView('查看商品属性', '${ctx}/ec/goodsattribute/form?id=${goodsAttribute.id}&opflag=VIEW','800px', '500px')" class="btn btn-info btn-xs" ><i class="fa fa-search-plus"></i> 查看</a>
										</shiro:hasPermission>
										<shiro:hasPermission name="ec:goodsattribute:edit">
				    						<a href="#" onclick="openDialog('修改商品属性', '${ctx}/ec/goodsattribute/form?id=${goodsAttribute.id}&opflag=UPDATE','800px', '500px')" class="btn btn-success btn-xs" ><i class="fa fa-edit"></i> 修改</a>
					    				</shiro:hasPermission>
					    				<shiro:hasPermission name="ec:goodsattribute:del">
											<a href="${ctx}/ec/goodsattribute/delete?id=${goodsAttribute.id}" onclick="return confirmx('要删除该商品属性吗？', this.href)" class="btn btn-danger btn-xs" ><i class="fa fa-trash"></i> 删除</a>
										</shiro:hasPermission>
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
		<div class="loading"></div>
	</div>
</body>
</html>