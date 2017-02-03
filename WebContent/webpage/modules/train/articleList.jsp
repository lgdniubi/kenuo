<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/webpage/include/taglib.jsp"%>
<%@ include file="/webpage/include/icheck.jsp"%>
<html>
<head>
    <meta charset="utf-8">
    <meta name="decorator" content="default"/>
    <meta name="viewport" content="width=device-width, initial-scale=1.0, minimum-scale=1.0, maximum-scale=1.0,user-scalable=no">
     <link rel="stylesheet" href="${ctxStatic}/train/css/exam.css">
      <script>
	    $(document).ready(function () {
	    	if($('#nowcateId').val()!=''){
	       		document.getElementById("cateId").value=$('#nowcateId').val();
	    	}
	    });
	   function search(){//查询，页码清零
			$("#pageNo").val(0);
			$("#searchForm").submit();
	   		return false;
	   }
		function reset(){//重置，页码清零
			$("#pageNo").val(0);
		//	$("#searchForm div.form-control input").val("");
		//	$("#searchForm div.form-control text").val("");
			$("#title").val("");
			$("#searchForm div.form-control select").val("");
			$("#searchForm").submit();
	  		return false;
	 	 }
		function page(n,s){//翻页
			$("#pageNo").val(n);
			$("#pageSize").val(s);
			$("#searchForm").submit();
			return false;
		}
    </script>
    <title>文章管理</title>
</head>
<body>
	<div class="wrapper-content">
        <div class="ibox">
            <div class="ibox-title">
                <h5>文章管理</h5>
            </div>
            <sys:message content="${message}"/>
            <div class="ibox-content">
                <div class=" clearfix">
                    <form:form class="navbar-form navbar-left searcharea"  id="searchForm" modelAttribute="exercise" action="${ctx}/train/articlelist/articlelist" method="post">
                    	<!-- 分页隐藏文本框 -->
						<input id="pageNo" name="pageNo" type="hidden" value="${page.pageNo}"/>
						<input id="pageSize" name="pageSize" type="hidden" value="${page.pageSize}"/>
                        <div class="form-group">
                            <label>文章标题：<input id="title" name="title" type="text" value="${article.title}" class="form-control" placeholder="搜索文章标题" maxlength="10"></label> 
                            <select class="form-control" id="cateId" name="cateId">
								   <option value="null">请选择分类</option>
								   <c:forEach items="${categoryList}" var="categoryList">
										<option value="${categoryList.categoryId}">${categoryList.name}</option>
								   </c:forEach>
							</select>
                        </div>
                        <div class="pull-right">
							<button  class="btn btn-primary btn-rounded btn-outline btn-sm " onclick="search()" ><i class="fa fa-search"></i> 查询</button>
							<button  class="btn btn-primary btn-rounded btn-outline btn-sm " onclick="reset()" ><i class="fa fa-refresh"></i> 重置</button>
						</div>
                    </form:form>
                    <!-- 工具栏 -->
					<div class="row" style="padding-bottom: 5px;">
						<div class="col-sm-12">
	                        <div class="pull-left">
		                        <shiro:hasPermission name="train:articlelist:sendArticle">
		                        	<button class="btn btn-white btn-sm" title="添加文章" onclick='top.openTab("${ctx}/train/articlelist/sendArticle","添加文章", false)' data-placement="left" data-toggle="tooltip">
										<i class="fa fa-plus"></i>添加文章
									</button>
								</shiro:hasPermission>
								<shiro:hasPermission name="train:articlelist:articleCategory">
									<button class="btn btn-white btn-sm" title="分类管理" onclick='top.openTab("${ctx}/train/articlelist/articleCategory","添加分类", false)' data-placement="left" data-toggle="tooltip">
										<i class="fa fa-plus"></i>分类管理
									</button>
								</shiro:hasPermission>
								<shiro:hasPermission name="train:articlelist:deleteAll">
	                             	<table:delRow url="${ctx}/train/articlelist/deleteAll" id="treeTable"></table:delRow><!-- 删除按钮 -->
	                             </shiro:hasPermission>
	                        </div>
						</div>
					</div>
                </div>
                <table id="treeTable" class="table table-bordered table-hover table-striped">
	                 <thead> 
	                   	 <tr>
                   			<th style="text-align: center;"><label for="i-checks"><input type="checkbox" name="" id="i-checks" class="i-checks"></label></th>
                   			<th style="text-align: center;">标题</th>
                   			<th style="text-align: center;">所属类别</th>
                   			<th style="text-align: center;">创建者</th>
                   			<th style="text-align: center;">评论人数</th>
                   			<th style="text-align: center;">时间</th>
                   			<th style="text-align: center;">操作</th>
                   		</tr>
	                 </thead>
                     <tbody>
                       	<c:forEach items="${page.list}" var="article">
							<tr style="text-align: center;">
								<td style="text-align: center;">
	                            	<input type="checkbox" id="${article.articleId}" name="ids" class="i-checks">
                            	</td>
                                <td style="text-align: center;">
                               		${article.title}
                               	</td>
                               	<td style="text-align: center;">
                               		${article.name}
                               	</td>
                               	<td style="text-align: center;">
                               		${article.uName}
                               	</td>
                               	<td style="text-align: center;"><a onclick="top.openTab('${ctx}/train/articlelist/findListComment?articleId=${article.articleId}','查看文章评论', false)">
                               		${article.num}</a>
                               	</td>
                                <td style="text-align: center;">${fns:formatDateTime(article.createtime)}</td>
								<td style="text-align: center;">
									<a class="btn btn-info btn-xs" onclick="top.openTab('${ctx}/train/articlelist/lookdetail?articleId=${article.articleId}','查看文章', false)"><i class="fa fa-search-plus"></i>查看</a>
									<shiro:hasPermission name="train:articlelist:updatedetail">
										<a class="btn btn-success btn-xs"  onclick="top.openTab('${ctx}/train/articlelist/updatedetail?articleId=${article.articleId}','修改文章', false)"><i class="fa fa-edit"></i>修改</a> 
									</shiro:hasPermission>
									<shiro:hasPermission name="train:articlelist:deleteOne">
										<a href="${ctx}/train/articlelist/deleteOne?articleId=${article.articleId}" onclick="return confirmx('确认要删除此文章吗？', this.href)"   class="btn btn-info btn-xs btn-danger"><i class="fa fa-trash"></i> 删除</a>
									</shiro:hasPermission>
								</td>
							</tr>
						</c:forEach>
                     </tbody>
                     <tfoot>
                        <tr>
                            <td colspan="20">
                                <div class="tfoot">
                                </div>
                                <!-- 分页代码 --> 
                               	<table:page page="${page}"></table:page>
                            </td>	
                        </tr>
                 	</tfoot>
                </table>
	        </div>
	    </div>
    </div>
    <div style="display:none">
   		<input type="text" id="nowcateId" value="${article.cateId}">
    </div>
    <script type="text/javascript">
	        $('#i-checks').click(function(){
	        	 if(this.checked){
	 	            $("#treeTable :checkbox").prop("checked", true);
	 	        }else{
	 	            $("#treeTable :checkbox").prop("checked", false);
	 	        }
	        });
	</script>
</body>
</html>