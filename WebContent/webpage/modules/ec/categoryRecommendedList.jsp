<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/webpage/include/taglib.jsp"%>
<%@ page import="com.training.modules.sys.utils.ParametersFactory"%>
<%
	String uploadURL = ParametersFactory.getMtmyParamValues("uploader_url");
%>
<!DOCTYPE html>
<html>
<head>
    <meta charset="utf-8">
   	<meta name="decorator" content="default"/>
    <meta name="viewport" content="width=device-width, initial-scale=1.0"> 
    <link rel="stylesheet" href="${ctxStatic}/ec/css/loading.css">
    <!-- 图片上传 引用-->
	<link rel="stylesheet" type="text/css" href="${ctxStatic}/ec/css/custom_uploadImg.css">
	<link rel="stylesheet" type="text/css" href="${ctxStatic}/train/uploadify/uploadify.css">
	<script type="text/javascript" src="${ctxStatic}/train/uploadify/lang-cn.js"></script>
	<script type="text/javascript" src="${ctxStatic}/train/uploadify/jquery.uploadify.min.js"></script>
    <script>
	    function page(n,s){
			$("#pageNo").val(n);
			$("#pageSize").val(s);
			$("#searchForm").submit();
	    	return false;
	    }
		$(function(){
			$(".imgUrl img").each(function(){
				var $this = $(this),
					$src = $this.attr('data-src');  
				$this.attr({'src':$src})
			});
			
		});
		
		//是否显示
		function updateType(id,flag,franchiseeId){
			$(".loading").show();//打开展示层
			$.ajax({
				type : "POST",
				url : "${ctx}/ec/franchiseeBanner/updateType?ID="+id+"&flag="+flag+"&franchiseeId="+franchiseeId,
				dataType: 'json',
				success: function(data) {
					$(".loading").hide(); //关闭加载层
					var status = data.STATUS;
					if("OK" == status){
						window.location="${ctx}/ec/franchiseeBanner/list";
					}else if("ERROR" == status){
						alert(data.MESSAGE);
					}
				}
			}); 
		}
		
		//确认是否删除
		function  delFranchiseeBanner(id,franchiseeId,isShow){
			if(isShow == 1){
				top.layer.alert('此数据启用中,请重新选择!', {icon: 0, title:'提醒'});
				return;
			}else{
				if(confirm("确认要删除吗？","提示框")){
					isDelete(id,franchiseeId);			
				}
			}
		}
		//不启用的数据可以被删除
		function isDelete(id,franchiseeId){
			$(".loading").show();//打开展示层
			$.ajax({
				type : "POST",
				url : "${ctx}/ec/franchiseeBanner/delete?id="+id+"&franchiseeId="+franchiseeId,
				dataType: 'json',
				success: function(data) {
					$(".loading").hide(); //关闭加载层
					var status = data.STATUS;
					if("OK" == status){
						window.location="${ctx}/ec/franchiseeBanner/list";
					}else if("ERROR" == status){
						top.layer.alert(data.MESSAGE, {icon: 0, title:'提醒'});
					}
				}
			}); 
		}
    </script>
    <title>品类推荐</title>
</head>
<body>
	<div class="wrapper-content">
        <div class="ibox">
            <div class="ibox-title">
                <h5>品类推荐</h5>
            </div>
            <sys:message content="${message}"/>
            <div class="ibox-content">
                <div class=" clearfix">
					<div class="row">
						<div class="col-sm-12">
							<%-- <form:form id="searchForm" modelAttribute="mtmyCategoryRecommended" action="${ctx}/ec/categoryRecommended/list" method="post" class="navbar-form navbar-left searcharea">
							<div class="pull-left">
								<input id="pageNo" name="pageNo" type="hidden" value="${page.pageNo}" />
								<input id="pageSize" name="pageSize" type="hidden" value="${page.pageSize}" />
								<div class="form-group">
									<label>是否启用：</label>
									<form:select path="isShow" cssClass="form-control">
										<form:option value="-1">全部</form:option>
										<form:option value="1">启用</form:option>
										<form:option value="0">隐藏</form:option>
									</form:select>
								</div>
							</div>	
							</form:form> --%>
							<div class="pull-right">
								<button class="btn btn-primary btn-rounded btn-outline btn-sm " onclick="search()"><i class="fa fa-search"></i> 查询</button>
								<button class="btn btn-primary btn-rounded btn-outline btn-sm " onclick="reset()"><i class="fa fa-refresh"></i> 重置</button>
							</div>
							<br/>
						</div>
					</div>
                    <!-- 工具栏 -->
					<%-- <div class="row" style="padding-bottom: 5px;">
						<div class="col-sm-12">
	                        <div class="pull-left">
	                        	<shiro:hasPermission name="ec:franchiseeBanner:add">
	                        		<table:addRow url="${ctx}/ec/franchiseeBanner/form" title="商家主页banner图" width="700px" height="650px"></table:addRow>
	                        	</shiro:hasPermission>
	                        </div>
						</div>
					</div> --%>
                </div>
                <table id="treeTable" class="table table-bordered table-hover table-striped">
                	<thead> 
                		<tr>
                			<th style="text-align: center;">分类ID</th>
                			<th style="text-align: center;">分类名称</th>
                			<th style="text-align: center;">品类名称</th>
                			<th style="text-align: center;">品类图片</th>
                			<th style="text-align: center;">商品数量</th>
                			<th style="text-align: center;">分类显示</th>
                			<th style="text-align: center;">商城显示</th>
                			<th style="text-align: center;">排序</th>
                			<th style="text-align: center;">操作</th>
                		</tr>
                	</thead>
                    <tbody>
                    	<c:forEach items="${page.list}" var="categoryRecommended">
						<tr>
							<td style="text-align: center;">${categoryRecommended.id}</td>
							<td style="text-align: center;">${categoryRecommended.categoryName}</td>
							<td style="text-align: center;">${categoryRecommended.name}</td>
							<td style="text-align: center;" class="imgUrl" ><img alt="" src="${ctxStatic}/images/lazylode.png"  data-src="${categoryRecommended.imgUrl}" style="width: 150px;height: 100px;border:1px solid black; "></td>
							<td style="text-align: center;">${categoryRecommended.id}</td>
							<td style="text-align: center;" id="${categoryRecommended.id}">
								<c:if test="${categoryRecommended.isCategoryShow  eq '0'}">
									<img width="20" height="20" src="${ctxStatic}/ec/images/cancel.png" onclick="updateType('${categoryRecommended.id}','1','${categoryRecommended.categoryId}')">
								</c:if>
								<c:if test="${categoryRecommended.isCategoryShow eq '1'}">
									<img width="20" height="20" src="${ctxStatic}/ec/images/open.png" onclick="updateType('${categoryRecommended.id}','0','${categoryRecommended.categoryId}')">
								</c:if>
							</td>
							<td style="text-align: center;" id="${categoryRecommended.id}">
								<c:if test="${categoryRecommended.isMallShow  eq '0'}">
									<img width="20" height="20" src="${ctxStatic}/ec/images/cancel.png" onclick="updateType('${categoryRecommended.id}','1','${categoryRecommended.categoryId}')">
								</c:if>
								<c:if test="${categoryRecommended.isMallShow eq '1'}">
									<img width="20" height="20" src="${ctxStatic}/ec/images/open.png" onclick="updateType('${categoryRecommended.id}','0','${categoryRecommended.categoryId}')">
								</c:if>
							</td>
							<td style="text-align: center;">${categoryRecommended.sort}</td>
							<%-- <td style="text-align: center;">
								<shiro:hasPermission name="ec:franchiseeBanner:view">
									<a href="#" onclick="openDialogView('查看', '${ctx}/ec/categoryRecommended/form?id=${franchiseeBanner.id}','600px', '550px')" class="btn btn-info btn-xs"><i class="fa fa-search-plus"></i> 查看</a>
								</shiro:hasPermission>
								<shiro:hasPermission name="ec:franchiseeBanner:edit">
									<a href="#" onclick="openDialog('修改', '${ctx}/ec/categoryRecommended/form?id=${franchiseeBanner.id}','600px', '550px')" class="btn btn-success btn-xs"><i class="fa fa-edit"></i> 修改</a>
								</shiro:hasPermission>
								<shiro:hasPermission name="ec:franchiseeBanner:del">
									<a href="#" onclick="delFranchiseeBanner('${categoryRecommended.id}','${categoryRecommended.franchiseeId}',${categoryRecommended.isCategoryShow})" class="btn btn-danger btn-xs"><i class="fa fa-trash"></i>删除</a>
								</shiro:hasPermission>
							</td> --%>
						</tr>
						</c:forEach>
                    </tbody>
                    <tfoot>
 						<tr>
                            <td colspan="20">
                                <!-- 分页代码 --> 
                                <div class="tfoot">
                                </div>
                               	<table:page page="${page}"></table:page>
                            </td>	
                        </tr>
                    </tfoot>
                </table>
            </div>
        </div>
   </div>
   <div class="loading"></div>
</body>
</html>