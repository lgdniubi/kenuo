<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/webpage/include/taglib.jsp"%>

<html>
<head>
	<title>商品品牌</title>
	<meta name="decorator" content="default"/>
	<link rel="stylesheet" href="${ctxStatic}/ec/css/loading.css">
	<script type="text/javascript">
		//刷新
		function refresh(){
			window.location="${ctx}/ec/goodsbrand/list";
		}
		
		//是否推荐 改变事件
		function changeTableVal(id,isHot){
			$(".loading").show();//打开展示层
			$.ajax({
				type : "POST",   
				url : "${ctx}/ec/goodsbrand/updateishot?ID="+id+"&ISHOT="+isHot,
				dataType: 'json',
				success: function(data) {
					$(".loading").hide(); //关闭加载层
					var status = data.STATUS;
					var isHot = data.ISHOT;
					if("OK" == status){
						$("#isHot"+id).html("");//清除DIV内容	
						if(isHot == '0'){
							//当前状态为【是】，则取消
							$("#isHot"+id).append("<img width='20' height='20' src='${ctxStatic}/ec/images/open.png' onclick=\"changeTableVal('"+id+"','1')\">");
						}else if(isHot == '1'){
							//当前状态为【否】，则打开
							$("#isHot"+id).append("<img width='20' height='20' src='${ctxStatic}/ec/images/cancel.png' onclick=\"changeTableVal('"+id+"','0')\">");
						}
					}else if("ERROR" == status){
						alert(data.MESSAGE);
					}
				}
			});   
		}
		
		//重置表单
		function resetnew(){
			$("#name").val("");
			reset();
		}
		
		//分页按钮
		function page(n,s){
			$("#pageNo").val(n);
			$("#pageSize").val(s);
			$("#searchForm").submit();
	    	return false;
	    }
		
		$(function(){
			$("img").each(function(){
				var $this = $(this),
					$src = $this.attr('data-src');  
				$this.attr({'src':$src})
			});
			
		});
	
		
	</script>
</head>
<body>
	<div class="ibox-content">
		<sys:message content="${message}"/>
		<div class="warpper-content">
			<div class="ibox">
				<div class="ibox-title">
					<h5>商品品牌管理</h5>
				</div>
				<div class="ibox-content">
					<div class="searcharea clearfix">
						<form:form id="searchForm" action="${ctx}/ec/goodsbrand/list" method="post" class="navbar-form navbar-left searcharea">
							<div class="form-group">
								<label>关键字：<input id="name" name="name" maxlength="10" type="text" class="form-control" value="${goodsBrand.name}"></label> 
							</div>
							<shiro:hasPermission name="ec:goodsbrand:list">
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
						</form:form>
					</div>
				
					<!-- 工具栏 -->
					<div class="row">
						<div class="col-sm-12">
							<shiro:hasPermission name="ec:goodsbrand:add">
								<table:addRow url="${ctx}/ec/goodsbrand/form?opflag=ADDPARENT" width="800px" height="500px" title="商品品牌"></table:addRow><!-- 增加按钮 -->
							</shiro:hasPermission>
							<button class="btn btn-white btn-sm " data-toggle="tooltip" data-placement="left" onclick="refresh()" title="刷新"><i class="glyphicon glyphicon-repeat"></i> 刷新</button>
						</div>
					</div>
					<table id="treeTable" class="table table-striped table-bordered table-hover table-condensed dataTables-example dataTable">
						<thead>
							<th style="text-align: center;">ID</th>
							<th style="text-align: center;">品牌名称</th>
							<th style="text-align: center;">LOGO</th>
							<th style="text-align: center;">产地</th>
							<th style="text-align: center;">品牌分类</th>
							<th style="text-align: center;">是否推荐</th>
							<th style="text-align: center;">排序</th>
							<th style="text-align: center;">操作</th>
						</thead>
						<tbody>
							<c:forEach items="${page.list}" var="goodsBrand">
								<tr style="text-align: center;">
									<td>${goodsBrand.id}</td>
									<td>${goodsBrand.name}</td>
									<td><img width="80px" height="40px" src="${ctxStatic}/images/lazylode.png" data-src="${goodsBrand.logo}"></td>
									<td>${goodsBrand.area}</td>
									<td>${goodsBrand.goodsCategory.name}</td>
									<td style="text-align: center;" id="isHot${goodsBrand.id}">
										<c:if test="${goodsBrand.isHot == 0}">
											<img width="20" height="20" src="${ctxStatic}/ec/images/open.png" onclick="changeTableVal('${goodsBrand.id}','1')">
										</c:if>
										<c:if test="${goodsBrand.isHot == 1}">
											<img width="20" height="20" src="${ctxStatic}/ec/images/cancel.png" onclick="changeTableVal('${goodsBrand.id}','0')">
										</c:if>
									</td>
									<td>${goodsBrand.sort}</td>
									<td style="text-align: left;">
										<shiro:hasPermission name="ec:goodsbrand:view">
											<a href="#" onclick="openDialogView('查看商品品牌', '${ctx}/ec/goodsbrand/form?id=${goodsBrand.id}&opflag=VIEW','800px', '500px')" class="btn btn-info btn-xs" ><i class="fa fa-search-plus"></i> 查看</a>
										</shiro:hasPermission>
										<shiro:hasPermission name="ec:goodsbrand:edit">
				    						<a href="#" onclick="openDialog('修改商品品牌', '${ctx}/ec/goodsbrand/form?id=${goodsBrand.id}&opflag=UPDATE','800px', '500px')" class="btn btn-success btn-xs" ><i class="fa fa-edit"></i> 修改</a>
					    				</shiro:hasPermission>
					    				<shiro:hasPermission name="ec:goodsbrand:del">
											<a href="${ctx}/ec/goodsbrand/delete?id=${goodsBrand.id}" onclick="return confirmx('要删除该商品品牌吗？', this.href)" class="btn btn-danger btn-xs" ><i class="fa fa-trash"></i> 删除</a>
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