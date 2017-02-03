<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/webpage/include/taglib.jsp"%>
<html>
<head>
    <meta charset="utf-8">
    <meta name="decorator" content="default"/>
    <meta name="viewport" content="width=device-width, initial-scale=1.0, minimum-scale=1.0, maximum-scale=1.0,user-scalable=no">
    <link rel="stylesheet" href="${ctxStatic}/train/css/exam.css">
   	<!-- 日期控件 -->
    <script type="text/javascript" src="${ctxStatic}/My97DatePicker/WdatePicker.js"></script>
    
    <script>
	   function search(){//查询，页码清零
			$("#pageNo").val(0);
			$("#searchForm").submit();
	   		return false;
	   }
		function nowReset(){//重置，页码清零
			$("#pageNo").val(0);
			$("#title,#beginDate,#endDate").val("");
			$("#categoryId").val(0);
			$("#searchForm").submit();
	 	 }
		function page(n,s){//翻页
			$("#pageNo").val(n);
			$("#pageSize").val(s);
			$("#searchForm").submit();
			return false;
		}
		function changeflag(num){
			$("#flag").val(num);
			$("#myForm").submit();
		}
		$(document).ready(function() {
			var num = $("#flag").val();
			$(".task").hide();
			$("#tab" + num).addClass('active').siblings().removeClass('active');
			
		  
		})
		
		$(function(){
			$(".user_photo img").each(function(){
				var $this = $(this),
					$src = $this.attr('data-src');  
				$this.attr({'src':$src})
			});
			
		});
		
		
    </script>
    <title>评论管理</title>
</head>
<body>
	<div class="wrapper-content">
        <div class="ibox">
            <div class="ibox-title">
                <h5>评论管理</h5>
            </div>
            <sys:message content="${message}"/>
            <div class="ibox-content">
                <div class=" clearfix">
                	<div class="form-group">
                		<ul class="nav nav-tabs">
			                <li class="active" id="tab0"><a href="#" onclick="changeflag('0')" data-toggle="tab">评论</a></li>
			                <li id="tab2"><a href="#" onclick="changeflag('2')" data-toggle="tab">已删除</a></li>                        
			            </ul>
                	</div>
                	<form id="myForm" action="${ctx}/ec/mtmyArticleList/mtmyArticleCommentList" method="post">
                		<input id="flag" name="flag" value="${mtmyArticleComment.flag }" type="hidden">
                	</form>
                    <form:form class="navbar-form navbar-left searcharea"  id="searchForm" modelAttribute="exercise" action="${ctx}/ec/mtmyArticleList/mtmyArticleCommentList" method="post">
                    	<!-- 分页隐藏文本框 -->
						<input id="pageNo" name="pageNo" type="hidden" value="${page.pageNo}"/>
						<input id="pageSize" name="pageSize" type="hidden" value="${page.pageSize}"/>
						<input id="flag" name="flag" value="${mtmyArticleComment.flag }" type="hidden"><!-- 隐藏文章类型 -->
                        <div class="form-group">
                            <label>文章标题：<input id="title" name="title" type="text" value="${mtmyArticleComment.title}" class="form-control" placeholder="搜索文章标题" maxlength="10"></label> 
                            <select class="form-control" id="categoryId" name="categoryId">
								   <option value=0>请选择分类</option>
								   <c:forEach items="${categoryList}" var="categoryList">
								   	   <c:choose>
											<c:when test="${mtmyArticleComment.categoryId eq categoryList.id}">
												<option value="${categoryList.id}" selected="selected">${categoryList.name}</option>
											</c:when>
											<c:otherwise>
												<option value="${categoryList.id}">${categoryList.name}</option>
											</c:otherwise>
										</c:choose>
								   </c:forEach>
							</select>
				 时间范围：<input id="beginDate" name="beginDate" class="Wdate form-control layer-date input-sm required" style="height: 30px;width: 200px" type="text" 
							value="<fmt:formatDate value="${mtmyArticleComment.beginDate}" pattern="yyyy-MM-dd HH:mm:ss"/>" onclick="WdatePicker({dateFmt:'yyyy-MM-dd HH:mm:ss',maxDate:'#F{$dp.$D(\'endDate\')}'})" readonly="readonly"/>
						<label>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;--&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</label>
						<input id="endDate" name="endDate" class="Wdate form-control layer-date input-sm required" style="height: 30px;width: 200px" type="text" 
							value="<fmt:formatDate value="${mtmyArticleComment.endDate}" pattern="yyyy-MM-dd HH:mm:ss"/>" onclick="WdatePicker({dateFmt:'yyyy-MM-dd HH:mm:ss',minDate:'#F{$dp.$D(\'beginDate\')}'})" readonly="readonly"/>
                        </div>
                        <div class="pull-right">
							<button  class="btn btn-primary btn-rounded btn-outline btn-sm " onclick="search()" ><i class="fa fa-search"></i> 查询</button>
							<button  class="btn btn-primary btn-rounded btn-outline btn-sm " onclick="nowReset()" ><i class="fa fa-refresh"></i> 重置</button>
						</div>
                    </form:form>
                </div>
                 <div class="ibox-content">
				<div class="" id="reviewlists">
					<form id="searchForm" action="${ctx}/ec/mtmyArticleList/findArticleComment" method="post">
						<!-- 隐藏文本框  隐藏问题ID -->
						<input id="articlesId" name="articlesId" type="hidden" value="${articlesId}"/>
						<c:forEach items="${page.list}" var="ArticleComment">
							<!-- 分页隐藏文本框 -->
			                    <input id="pageNo" name="pageNo" type="hidden" value="${page.pageNo}"/>
				 				<input id="pageSize" name="pageSize" type="hidden" value="${page.pageSize}"/>
							<div class="comment_areas clearfix">
								<div class="user_photo"><img src="${ArticleComment.createPhoto }" alt="" class="img-responsive"></div>
								<div class="comments_con">
									<span class="usersname">${ArticleComment.createName}</span><span class="times"><fmt:formatDate value="${ArticleComment.createDate}" pattern="yyyy-MM-dd HH:mm:ss"/></span><span class="times">点赞：${ArticleComment.likeNum}</span>
									<c:if test="${mtmyArticleComment.flag == 0}">
										<shiro:hasPermission name="ec:mtmyArticleList:deleteComment">
											<a href="${ctx}/ec/mtmyArticleList/deleteManagerComment?id=${ArticleComment.id}" onclick="return confirmx('要删除该评论吗？', this.href)"  class="btn btn-default del-btn"><i class="glyphicon glyphicon-remove"></i> 删除</a>
										</shiro:hasPermission>
									</c:if>
									<p>${ArticleComment.contents}</p>
								</div>
							</div>
						</c:forEach>
					</form>
				</div>
                <table:page page="${page}"></table:page>
	        </div>
	    </div>
	    <div class="loading"></div>
    </div>
</div>
</body>
</html>