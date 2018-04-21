<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/webpage/include/taglib.jsp"%>
<html>
<head>
<title>团购商品管理</title>
<meta name="decorator" content="default" />
<script type="text/javascript">
	function page(n,s) {
		$("#pageNo").val(n);
		$("#pageSize").val(s);
		$("#searchForm").submit();
		return false;
	}
        
	$(document).ready(function() {
		
    });
</script>
</head>
<body class="gray-bg">
	<div class="wrapper wrapper-content">
		<div class="ibox">
			<div class="ibox-title">
				<h5>团购商品列表</h5>
				<div class="ibox-tools">
					<a href="${ctx}/ec/groupActivity/list?parent.id=${mtmyGroupActivityGoods.mtmyGroupActivity.parent.id }" class="btn btn-info btn-xs" ><i class="fa fa-arrow-left"></i> 返回</a>
				</div>
			</div>
			<sys:message content="${message}" />
			<!-- 查询条件 -->
			<div class="ibox-content">
				<div class="clearfix">
					<form:form id="searchForm" modelAttribute="mtmyGroupActivityGoods" action="${ctx}/ec/groupActivity/goodsList" method="post" class="form-inline">
						<input id="mtmyGroupActivity.id" name="mtmyGroupActivity.id" type="hidden" value="${mtmyGroupActivityGoods.mtmyGroupActivity.id}"/>
						<div class="form-group">
							<input id="pageNo" name="pageNo" type="hidden" value="${page.pageNo}"/>
							<input id="pageSize" name="pageSize" type="hidden" value="${page.pageSize}"/>
							<label>项目类目：</label>
							<sys:treeselect id="goodsCategoryId" name="goods.goodsCategoryId" value="${mtmyGroupActivityGoods.goods.goodsCategoryId}" labelName="goods.goodsCategory.name" labelValue="${mtmyGroupActivityGoods.goods.goodsCategory.name}" 
						     		title="商品分类" url="/ec/goodscategory/treeData" cssClass="form-control required" allowClear="true" notAllowSelectParent="true"/>
					     	&nbsp;&nbsp;商品商家：
							<sys:treeselect id="franchisee" name="goods.franchisee.id" value="${mtmyGroupActivityGoods.goods.franchisee.id}" labelName="goods.franchisee.name" labelValue="${mtmyGroupActivityGoods.goods.franchisee.name}" 
						     		title="商品商家" url="/sys/franchisee/treeData" cssClass="form-control required" allowClear="true" notAllowSelectParent="true"/>
		                    &nbsp;&nbsp;上架/下架：
							<select class="form-control" style="text-align: center;width: 150px;" id="goods.isOnSale" name="goods.isOnSale">
								<option value="-1" ${(mtmyGroupActivityGoods.goods.isOnSale == '-1')?'selected="selected"':''}>全部</option>
								<option value="1" ${(mtmyGroupActivityGoods.goods.isOnSale == '1')?'selected="selected"':''}>上架</option>
								<option value="0" ${(mtmyGroupActivityGoods.goods.isOnSale == '0')?'selected="selected"':''}>下架</option>
							</select>
							&nbsp;&nbsp;商品名称/关键字：<input id="goods.querytext" name="goods.querytext" type="text" class="form-control" placeholder="商品名称/关键字" value="${mtmyGroupActivityGoods.goods.querytext }">
							&nbsp;&nbsp;商品ID：<input id="goodsId" name="goodsId" type="text" class="form-control" placeholder="商品ID" value="${mtmyGroupActivityGoods.goodsId }">
						</div>
					</form:form>
					<!-- 工具栏 -->
					<div class="row" style="padding-top: 10px;">
						<div class="col-sm-12">
							<div class="pull-left">
								<%-- <shiro:hasPermission name="ec:groupActivity:add"> --%>
									<a href="#" onclick="openDialog('新增团购商品', '${ctx}/ec/groupActivity/goodsForm?mtmyGroupActivity.id=${mtmyGroupActivityGoods.mtmyGroupActivity.id}','700px','500px')" class="btn btn-white btn-sm"><i class="fa fa-plus"></i>新增</a>
								<%-- </shiro:hasPermission> --%>
							</div>
							<div class="pull-right">
								<button  class="btn btn-primary btn-rounded btn-outline btn-sm " onclick="search()" ><i class="fa fa-search"></i> 查询</button>
								<button  class="btn btn-primary btn-rounded btn-outline btn-sm " onclick="reset()" ><i class="fa fa-refresh"></i> 重置</button>
							</div>
						</div>
					</div>
				</div>
				<table id="contentTable" class="table table-striped table-bordered  table-hover table-condensed  dataTables-example dataTable no-footer">
					<thead>
						<tr>
							<th style="text-align: center;">商品ID</th>
							<th style="text-align: center;">商品名称</th>
							<th style="text-align: center;">所属商家</th>
							<th style="text-align: center;">类别</th>
							<th style="text-align: center;">上/下架</th>
							<th style="text-align: center;">价格</th>
							<th style="text-align: center;">操作</th>
						</tr>
					</thead>
					<tbody style="text-align: center;">
						<c:forEach items="${page.list}" var="mtmyGroupActivityGoods">
							<tr>
								<td>${mtmyGroupActivityGoods.goods.goodsId}</td>
								<td>${mtmyGroupActivityGoods.goods.goodsName}</td>
								<td>${mtmyGroupActivityGoods.goods.franchisee.name}</td>
								<td>${mtmyGroupActivityGoods.goods.goodsCategory.name}</td>
								<td>${(mtmyGroupActivityGoods.goods.isOnSale == '1')?'上架':'下架'}</td>	
								<td>
									${mtmyGroupActivityGoods.groupActivityMarketPrice}
								</td>	
								<td>
									<a href="#" onclick="openDialog('修改团购商品', '${ctx}/ec/groupActivity/goodsForm?mtmyGroupActivity.id=${mtmyGroupActivityGoods.mtmyGroupActivity.id}&gId=${mtmyGroupActivityGoods.gId}','700px','500px')" class="btn btn-success btn-xs"><i class="fa fa-edit"></i> 修改</a>
									<a href="${ctx}/ec/groupActivity/deleteGoods?mtmyGroupActivity.id=${mtmyGroupActivityGoods.mtmyGroupActivity.id}&gId=${mtmyGroupActivityGoods.gId}" onclick="return confirmx('要删除该团购商品吗？', this.href)" class="btn btn-danger btn-xs" ><i class="fa fa-trash"></i> 删除</a>
									<a href="#" onclick="openDialog('添加商品库存', '${ctx}/ec/groupActivity/specstocks?mtmyGroupActivity.id=${mtmyGroupActivityGoods.mtmyGroupActivity.id}&goods.goodsId=${mtmyGroupActivityGoods.goods.goodsId}','600px', '400px')" class="btn btn-success btn-xs" ><i class="fa fa-edit"></i> 补仓</a>
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