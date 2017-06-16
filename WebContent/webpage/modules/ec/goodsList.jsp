<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/webpage/include/taglib.jsp"%>
<%@ include file="/webpage/include/icheck.jsp"%>
<html>
<head>
	<title>商品列表</title>
	<meta name="decorator" content="default"/>
	<link rel="stylesheet" href="${ctxStatic}/ec/css/loading.css">
	<script type="text/javascript">
		//刷新
		function refresh(){
			window.location="${ctx}/ec/goods/list";
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
		
		//是否推荐/是否显示 改变事件
		function changeTableVal(fromid,id,isyesno){
			$(".loading").show();//打开展示层
			$.ajax({
				type : "POST",
				url : "${ctx}/ec/goods/updateisyesno?ID="+id+"&FROMID="+fromid+"&ISYESNO="+isyesno,
				dataType: 'json',
				success: function(data) {
					$(".loading").hide(); //关闭加载层
					var status = data.STATUS;
					var isyesno = data.ISYESNO;
					if("OK" == status){
						$("#"+fromid+id).html("");//清除DIV内容	
						if(isyesno == '0'){
							//当前状态为【否】，则打开
							$("#"+fromid+id).append("<img width='20' height='20' src='${ctxStatic}/ec/images/cancel.png' onclick=\"changeTableVal('"+fromid+"','"+id+"','1')\">");
						}else if(isyesno == '1'){
							//当前状态为【是】，则取消
							$("#"+fromid+id).append("<img width='20' height='20' src='${ctxStatic}/ec/images/open.png' onclick=\"changeTableVal('"+fromid+"','"+id+"','0')\">");
						}
					}else if("ERROR" == status){
						alert(data.MESSAGE);
					}
				}
			});   
		}
		function update(newUrl){
			var str=document.getElementsByName("box");
			var objarray=str.length;
			var chestr="";     var ids="";
			for (i=0;i<objarray;i++){
			    if(str[i].checked == true){
			   		chestr+=str[i].id+",";
			    }
			}
			if(chestr.substr(chestr.length-1)== ','){
			    ids = chestr.substr(0,chestr.length-1);
			}
			if(ids == ""){
				top.layer.alert('请至少选择一条数据!', {icon: 0, title:'警告'});
			}else{
				top.layer.confirm('确认要编辑数据吗?', {icon: 3, title:'系统提示'}, function(index){
					var url =newUrl+"&ids="+ids;
					window.location = url;
			    top.layer.close(index);
				});
			}
		}
		$(document).ready(function() {
		    $('#treeTable thead tr th input.i-checks').on('ifChecked', function(event){ //ifCreated 事件应该在插件初始化之前绑定 
		    	 $('#treeTable tbody tr td input.i-checks').iCheck('check');
		    });
		    $('#treeTable thead tr th input.i-checks').on('ifUnchecked', function(event){ //ifCreated 事件应该在插件初始化之前绑定 
		    	 $('#treeTable tbody tr td input.i-checks').iCheck('uncheck');
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
					<h5>商品列表管理</h5>
				</div>
				<div class="ibox-content">
					<div class="searcharea clearfix">
						<form:form id="searchForm" action="${ctx}/ec/goods/list" style="width: 100%;" modelAttribute="goods" method="post" class="navbar-form navbar-left searcharea">
							<div class="form-group" style="width: 100%;margin-top: 5px;">
								商品分类：
								<sys:treeselect id="goodsCategoryId" name="goodsCategoryId" value="${goods.goodsCategoryId }" 
									labelName="goodsCategory.name" labelValue="${goodsCategory.name }" 
						     		title="商品分类" url="/ec/goodscategory/treeData" cssClass="form-control required" allowClear="true" notAllowSelectParent="true"/>
						     	&nbsp;&nbsp;商品品牌：
								<select class="form-control" id="goodsBrandId" name="goodsBrandId" style="text-align: center;width: 150px;">
			                        <option value="-1">所有品牌</option>
									<c:forEach items="${goodsBrandList}" var="goodsBrand">
										<option ${(goodsBrand.id == goods.goodsBrandId)?'selected="selected"':''} value="${goodsBrand.id}">${goodsBrand.name}</option>
									</c:forEach>
			                    </select>
								&nbsp;&nbsp;商品名称/关键字：<input id="querytext" name="querytext" maxlength="10" type="text" class="form-control" placeholder="商品名称/关键字" value="${goods.querytext }">
								&nbsp;&nbsp;活动名称：
								<select class="form-control" id="actionId" name="actionId" style="text-align: center;width: 150px;">
			                        <option value="0">全部活动</option>
									<c:forEach items="${actionList}" var="list">
										<option ${(list.actionId == goods.actionId)?'selected="selected"':''} value="${list.actionId}">${list.actionName}</option>
									</c:forEach>
			                    </select>
			                </div>
			                <div class="form-group" style="width: 100%;margin-top: 5px;">
			                	上架/下架：
								<select class="form-control" style="text-align: center;width: 150px;" id="isOnSale" name="isOnSale">
									<option value="-1" ${(goods.isOnSale == '-1')?'selected="selected"':''}>全部</option>
									<option value="1" ${(goods.isOnSale == '1')?'selected="selected"':''}>上架</option>
									<option value="0" ${(goods.isOnSale == '0')?'selected="selected"':''}>下架</option>
								</select>
								&nbsp;&nbsp;推荐：
								<select class="form-control" style="text-align: center;width: 150px;" id="isRecommend" name="isRecommend">
									<option value="-1" ${(goods.isRecommend == '-1')?'selected="selected"':''}>全部</option>
									<option value="1" ${(goods.isRecommend == '1')?'selected="selected"':''}>是</option>
									<option value="0" ${(goods.isRecommend == '0')?'selected="selected"':''}>否</option>
								</select>
								&nbsp;&nbsp;新品：
								<select class="form-control" style="text-align: center;width: 150px;" id="isNew" name="isNew">
									<option value="-1" ${(goods.isNew == '-1')?'selected="selected"':''}>全部</option>
									<option value="1" ${(goods.isNew == '1')?'selected="selected"':''}>是</option>
									<option value="0" ${(goods.isNew == '0')?'selected="selected"':''}>否</option>
								</select>
								&nbsp;&nbsp;热卖：
								<select class="form-control" style="text-align: center;width: 150px;" id="isHot" name="isHot">
									<option value="-1" ${(goods.isHot == '-1')?'selected="selected"':''}>全部</option>
									<option value="1" ${(goods.isHot == '1')?'selected="selected"':''}>是</option>
									<option value="0" ${(goods.isHot == '0')?'selected="selected"':''}>否</option>
								</select>
								&nbsp;&nbsp;app显示：
								<select class="form-control" style="text-align: center;width: 150px;" id="isHot" name="isAppshow">
									<option value="-1" ${(goods.isAppshow == '-1')?'selected="selected"':''}>全部</option>
									<option value="1" ${(goods.isAppshow == '1')?'selected="selected"':''}>是</option>
									<option value="0" ${(goods.isAppshow == '0')?'selected="selected"':''}>否</option>
								</select>
								&nbsp;&nbsp;实物：
								<select class="form-control" style="text-align: center;width: 150px;" id="isReal" name="isReal">
									<option value="-1" ${(goods.isReal == '-1')?'selected="selected"':''}>全部</option>
									<option value="0" ${(goods.isReal == '0')?'selected="selected"':''}>是</option>
									<option value="1" ${(goods.isReal == '1')?'selected="selected"':''}>否</option>
								</select>
								&nbsp;&nbsp;
								<shiro:hasPermission name="ec:goods:list">
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
							<shiro:hasPermission name="ec:goods:add">
								<a href="${ctx}/ec/goods/form?opflag=ADDPARENT">
									<button class="btn btn-white btn-sm " data-toggle="tooltip" data-placement="left" ><i class="fa fa-plus"></i> 添加商品</button>
								</a>
							</shiro:hasPermission>
							<shiro:hasPermission name="ec:goods:updateIsOnSale">
								<button onclick="update('${ctx}/ec/goods/updateIsOnSale?flag=1')" class="btn btn-white btn-sm"><i class="fa fa-file-text-o"></i> 上架</button>
								<button onclick="update('${ctx}/ec/goods/updateIsOnSale?flag=2')" class="btn btn-white btn-sm"><i class="fa fa-file-text-o"></i> 下架</button>
							</shiro:hasPermission>
							<shiro:hasPermission name="ec:goods:updateIsRecommend">
								<button onclick="update('${ctx}/ec/goods/updateIsOnSale?flag=3')" class="btn btn-white btn-sm"><i class="fa fa-file-text-o"></i> 是推荐</button>
								<button onclick="update('${ctx}/ec/goods/updateIsOnSale?flag=4')" class="btn btn-white btn-sm"><i class="fa fa-file-text-o"></i> 否推荐</button>
							</shiro:hasPermission>
							<shiro:hasPermission name="ec:goods:generateStaticIndex">
								<a href="#" onclick="openDialog('主页静态化', '${ctx}/ec/staticIndex/previews','600px', '500px')" class="btn btn-white btn-sm"><i class="fa fa-folder-open-o"></i>主页静态化</a>
							</shiro:hasPermission>
							<button class="btn btn-white btn-sm " data-toggle="tooltip" data-placement="left" onclick="refresh()" title="刷新"><i class="glyphicon glyphicon-repeat"></i> 刷新</button>
						</div>
					</div>
					<table id="treeTable" class="table table-striped table-bordered table-hover table-condensed dataTables-example dataTable">
						<thead>
							<tr>
								<th style="text-align: center;"><input type="checkbox" class="i-checks"></th>
								<th style="text-align: center;">ID</th>
								<th style="text-align: center;">商品名称</th>
								<th style="text-align: center;">活动名称</th>
								<th style="text-align: center;">货号</th>
								<th style="text-align: center;">商品分类</th>
								<th style="text-align: center;">实物</th>
								<th style="text-align: center;">价格</th>
								<th style="text-align: center;">总库存</th>
								<th style="text-align: center;">剩余库存</th>
								<th style="text-align: center;">上架</th>
								<th style="text-align: center;">推荐</th>
								<th style="text-align: center;">新品</th>
								<th style="text-align: center;">热卖</th>
								<th style="text-align: center;">app显示</th>
								<th style="text-align: center;">排序</th>
								<th style="text-align: center;">操作</th>
							</tr>
						</thead>
						<tbody>
							<c:forEach items="${page.list}" var="goods">
								<tr style="text-align: center;">
									<td><input type="checkbox" id="${goods.goodsId}" class="i-checks" name="box"></td>
									<td>${goods.goodsId}</td>
									<td>${goods.goodsName}</td>
									<td>${goods.actionName}</td>
									<td>${goods.goodsSn}</td>
									<td>${goods.goodsCategory.name}</td>
									<td>${goods.isReal==0?"是":"否"}</td>
									<td>${goods.shopPrice}</td>
									<td>${goods.totalStore}</td>
									<td>${goods.storeCount}</td>
									<td style="text-align: center;" id="ISONSALE${goods.goodsId}">
										<c:if test="${goods.isOnSale == 0}">
											<img width="20" height="20" src="${ctxStatic}/ec/images/cancel.png" onclick="changeTableVal('ISONSALE','${goods.goodsId}','1')">
										</c:if>
										<c:if test="${goods.isOnSale == 1}">
											<img width="20" height="20" src="${ctxStatic}/ec/images/open.png" onclick="changeTableVal('ISONSALE','${goods.goodsId}','0')">
										</c:if>
									</td>
									<td style="text-align: center;" id="ISRECOMMEND${goods.goodsId}">
										<c:if test="${goods.isRecommend == 0}">
											<img width="20" height="20" src="${ctxStatic}/ec/images/cancel.png" onclick="changeTableVal('ISRECOMMEND','${goods.goodsId}','1')">
										</c:if>
										<c:if test="${goods.isRecommend == 1}">
											<img width="20" height="20" src="${ctxStatic}/ec/images/open.png" onclick="changeTableVal('ISRECOMMEND','${goods.goodsId}','0')">
										</c:if>
									</td>
									<td style="text-align: center;" id="ISNEW${goods.goodsId}">
										<c:if test="${goods.isNew == 0}">
											<img width="20" height="20" src="${ctxStatic}/ec/images/cancel.png" onclick="changeTableVal('ISNEW','${goods.goodsId}','1')">
										</c:if>
										<c:if test="${goods.isNew == 1}">
											<img width="20" height="20" src="${ctxStatic}/ec/images/open.png" onclick="changeTableVal('ISNEW','${goods.goodsId}','0')">
										</c:if>
									</td>
									<td style="text-align: center;" id="ISHOT${goods.goodsId}">
										<c:if test="${goods.isHot == 0}">
											<img width="20" height="20" src="${ctxStatic}/ec/images/cancel.png" onclick="changeTableVal('ISHOT','${goods.goodsId}','1')">
										</c:if>
										<c:if test="${goods.isHot == 1}">
											<img width="20" height="20" src="${ctxStatic}/ec/images/open.png" onclick="changeTableVal('ISHOT','${goods.goodsId}','0')">
										</c:if>
									</td>
										<td style="text-align: center;" id="ISAPPSHOW${goods.goodsId}">
										<c:if test="${goods.isAppshow == 0}">
											<img width="20" height="20" src="${ctxStatic}/ec/images/cancel.png" onclick="changeTableVal('ISAPPSHOW','${goods.goodsId}','1')">
										</c:if>
										<c:if test="${goods.isAppshow == 1}">
											<img width="20" height="20" src="${ctxStatic}/ec/images/open.png" onclick="changeTableVal('ISAPPSHOW','${goods.goodsId}','0')">
										</c:if>
									</td>
									<td>${goods.sort}</td>
									<td style="text-align: left;">
										<shiro:hasPermission name="ec:goods:view">
											<a href="http://wap.idengyun.com/mtmy-wap/goods/queryGoodsDetail.do?user_id=1000014&goods_id=${goods.goodsId}" class="btn btn-info btn-xs" target="_bank"><i class="fa fa-search-plus"></i> 预览</a>
										</shiro:hasPermission> 
										<shiro:hasPermission name="ec:goods:edit">
				    						<a href="${ctx}/ec/goods/form?id=${goods.goodsId}&opflag=UPDATE" class="btn btn-success btn-xs" ><i class="fa fa-edit"></i> 修改</a>
					    				</shiro:hasPermission>
					    				<shiro:hasPermission name="ec:goods:del">
											<a href="${ctx}/ec/goods/delete?id=${goods.goodsId}" onclick="return confirmx('要删除该商品吗？', this.href)" class="btn btn-danger btn-xs" ><i class="fa fa-trash"></i> 删除</a>
										</shiro:hasPermission>
										<shiro:hasPermission name="ec:goods:del">
											<a href="#" onclick="openDialog('添加商品库存', '${ctx}/ec/goods/fromspecstocks?id=${goods.goodsId}&actionId=${goods.actionId}','600px', '400px')" class="btn btn-success btn-xs" ><i class="fa fa-edit"></i> 补仓</a>
										</shiro:hasPermission>
										<shiro:hasPermission name="ec:goods:edit">
											<a href="${ctx}/ec/goods/copyGood?goodsId=${goods.goodsId}&goodsName=${goods.goodsName}" onclick="return confirmx('确认复制该商品吗？', this.href)" class="btn btn-success btn-xs" ><i class="fa fa-file"></i>复制</a>
										</shiro:hasPermission>
										<shiro:hasPermission name="ec:goods:edit">
											<a href="#" onclick="openDialogView('商品规格', '${ctx}/ec/goods/goodsBySpecList?goodsId=${goods.goodsId}','800px', '500px')" class="btn btn-danger btn-xs" ><i class="fa fa-edit"></i> 商品规格</a>
										</shiro:hasPermission>
										<shiro:hasPermission name="ec:goods:refreshRedis">
											<a href="${ctx}/ec/goods/refreshRedis?goodsId=${goods.goodsId}" onclick="return confirmx('确认刷新该商品(详情)缓存吗？', this.href)" class="btn btn-success btn-xs" ><i class="fa fa-file"></i>刷新缓存</a>
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