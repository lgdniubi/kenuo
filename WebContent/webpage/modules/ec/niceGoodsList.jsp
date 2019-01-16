<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/webpage/include/taglib.jsp"%>
<html>
<head>
    <meta charset="utf-8">
    <meta name="decorator" content="default"/>
    <meta name="viewport" content="width=device-width, initial-scale=1.0, minimum-scale=1.0, maximum-scale=1.0,user-scalable=no">
    <link rel="stylesheet" href="${ctxStatic}/train/css/exam.css">
    <script>
		function page(n,s){//翻页
			$("#pageNo").val(n);
			$("#pageSize").val(s);
			$("#searchForm").submit();
			return false;
		}
		function nowReset(){
			$("#pageNo").val(0);
			$("#searchForm div.form-group input").val("");
			$("#categoryId").val(0);
			$("#articleType").val("");
			$("#searchForm").submit();
		}
		$(document).ready(function() {
		    var start = {
			    elem: '#beginDate',
			    format: 'YYYY-MM-DD',
			    event: 'focus',
			    max: $("#endDate").val(),   //最大日期
			    istime: false,				//是否显示时间
			    isclear: false,				//是否显示清除
			    istoday: false,				//是否显示今天
			    issure: false,				//是否显示确定
			    festival: true,				//是否显示节日
			    choose: function(datas){
			         end.min = datas; 		//开始日选好后，重置结束日的最小日期
			         end.start = datas 		//将结束日的初始值设定为开始日
			    }
			};
			var end = {
			    elem: '#endDate',
			    format: 'YYYY-MM-DD',
			    event: 'focus',
			    min: $("#beginDate").val(),
			    istime: false,
			    isclear: false,
			    istoday: false,
			    issure: false,
			    festival: true,
			    choose: function(datas){
			        start.max = datas; //结束日选好后，重置开始日的最大日期
			    }
			};
			laydate(start);
			laydate(end);
		})
		
		$(function(){
			$(".user_photo img").each(function(){
				var $this = $(this),
					$src = $this.attr('data-src');  
				$this.attr({'src':$src})
			});
			
		});
    </script>
    <title>有好货推荐</title>
</head>
<body>
	<div class="wrapper-content">
        <div class="ibox">
            <div class="ibox-title">
                <h5>有好货推荐</h5>
            </div>
            <sys:message content="${message}"/>
            <div class="ibox-content">
                <div class=" clearfix">
                    <form:form class="navbar-form navbar-left searcharea"  id="searchForm" modelAttribute="articleRepository" action="${ctx}/ec/articles/list" method="post">
                    	<!-- 分页隐藏文本框 -->
						<input id="pageNo" name="pageNo" type="hidden" value="${page.pageNo}"/>
						<input id="pageSize" name="pageSize" type="hidden" value="${page.pageSize}"/>
                        <div class="form-group">
                            <label>文章标题：<form:input path="title" class="form-control" placeholder="搜索文章标题"/></label> 
                            <select class="form-control" id="categoryId" name="categoryId">
								  <option value=0>请选择分类</option>
								  <c:forEach items="${categoryList}" var="categoryList">
								   	  <c:choose>
											<c:when test="${articleRepository.categoryId == categoryList.categoryId}">
												<option value="${categoryList.categoryId}" selected="selected">${categoryList.name}</option>
											</c:when>
											<c:otherwise>
												<option value="${categoryList.categoryId}">${categoryList.name}</option>
											</c:otherwise>
									    </c:choose>
								  </c:forEach>
						   </select>
						   <select class="form-control" id="articleType" name="articleType">
								  <option value="">请选择文章类型</option>
								  <c:choose>
										<c:when test="${articleRepository.articleType == '0'}">
											<option value="0" selected="selected">已发布</option>
								  			<option value="1">草稿</option>
										</c:when>
										<c:when test="${articleRepository.articleType == '1'}">
											<option value="0">已发布</option>
								  			<option value="1" selected="selected">草稿</option>
										</c:when>
										<c:otherwise>
											<option value="0">已发布</option>
								  			<option value="1">草稿</option>
										</c:otherwise>
								    </c:choose>
						   </select>
				  时间范围：<input id="beginDate" name="beginDate" type="text" class="laydate-icon form-control layer-date input-sm"
								value="<fmt:formatDate value="${articleRepository.beginDate}" pattern="yyyy-MM-dd"/>" readonly="readonly"/>
							<label>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;--&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</label>
							<input id="endDate" name="endDate" type="text" class=" laydate-icon form-control layer-date input-sm"
								value="<fmt:formatDate value="${articleRepository.endDate}" pattern="yyyy-MM-dd"/>" readonly="readonly"/>
                        </div>
                        <div class="pull-right">
							<button  class="btn btn-primary btn-rounded btn-outline btn-sm " onclick="search()" ><i class="fa fa-search"></i> 查询</button>
							<button  class="btn btn-primary btn-rounded btn-outline btn-sm " onclick="nowReset()" ><i class="fa fa-refresh"></i> 重置</button>
						</div>
                    </form:form>
                    <!-- 工具栏 -->
					<div class="row" style="padding-bottom: 5px;">
						<div class="col-sm-12">
	                        <div class="pull-left">
                        		<shiro:hasPermission name="ec:articles:add">
		                        	<a href="${ctx}/ec/articles/articleForm"><button class="btn btn-white btn-sm" title="添加文章" data-placement="left" data-toggle="tooltip"><i class="fa fa-plus"></i>添加文章</button></a>
		                        </shiro:hasPermission>
		                        <shiro:hasPermission name="ec:articles:categoryList">	
									<button class="btn btn-white btn-sm" title="文章分类" onclick='top.openTab("${ctx}/ec/articles/categoryList","文章分类", false)' data-placement="left" data-toggle="tooltip"><i class="fa fa-plus"></i>文章分类</button>
								</shiro:hasPermission>
	                        </div>
						</div>
					</div>
                </div>
                <div class="" id="reviewlists">
	                <table id="contentTable" class="table table-striped table-bordered  table-hover table-condensed  dataTables-example dataTable no-footer">
						<thead>
							<tr>
								<th style="text-align: center;">ID</th>
								<th style="text-align: center;">文章标题</th>
							    <th style="text-align: center;">类别</th>
							    <th style="text-align: center;">作者</th>
							    <th style="text-align: center;">发布类型</th>
							    <th style="text-align: center;">发布时间</th>
							    <th style="text-align: center;">操作</th>
							</tr>
						</thead>
						<tbody style="text-align: center;">
							<c:forEach items="${page.list}" var="articleRepository">
								<tr>
									<td>${articleRepository.articleId }</td>
								  	<td>${articleRepository.title }</td>
								  	<td>${articleRepository.category.name }</td>
								  	<td>${articleRepository.authorName }</td>
								  	<td>
								  		<c:if test="${articleRepository.type == 0}">
								  			已发布
								  		</c:if>
								  		<c:if test="${articleRepository.type == 1}">
								  			草稿
								  		</c:if>
								  	</td>
								  	<td><fmt:formatDate value="${articleRepository.createDate }" pattern="yyyy-MM-dd HH:mm:ss"/></td>
								    <td>
								    	<shiro:hasPermission name="ec:articles:update">
								    		<a href="${ctx}/ec/articles/articleForm?articleId=${articleRepository.articleId}" class="btn btn-success btn-xs"><i class="fa fa-edit"></i> 编辑</a>
								    	</shiro:hasPermission>
								    	<shiro:hasPermission name="ec:articles:del">
								    		<a href="${ctx}/ec/articles/deleteArticle?articleId=${articleRepository.articleId}" onclick="return confirmx('确认要删除该文章吗？', this.href)" class="btn btn-danger btn-xs"><i class="fa fa-trash"></i> 删除</a>
								    	</shiro:hasPermission>
								    	<c:if test="${articleRepository.type == 0}">
									    	<shiro:hasPermission name="ec:articles:issue">
									    		<a href="#" onclick="openDialog('发布文章', '${ctx}/ec/articles/issueArticle?articleId=${articleRepository.articleId}','800px', '550px')" class="btn btn-info btn-xs"><i class="fa fa-search-plus"></i> 发布</a>
									    	</shiro:hasPermission>
								  		</c:if>
								    	<shiro:hasPermission name="ec:articles:findLogs">
									    	<button class="btn btn-primary btn-xs" title="发布日志" onclick="openDialog('发布日志', '${ctx}/ec/articles/findLogs?articleId=${articleRepository.articleId}','650px', '500px')" data-placement="left" data-toggle="tooltip">
												<i class="fa fa-calendar-o"></i> 发布日志
											</button>
										</shiro:hasPermission>
								    </td>
								</tr>
							</c:forEach>
						</tbody>
					</table>
				</div>
                <table:page page="${page}"></table:page>
	        </div>
	    </div>
    </div>
</body>
</html>