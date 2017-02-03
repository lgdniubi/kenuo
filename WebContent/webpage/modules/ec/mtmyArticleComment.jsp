<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/webpage/include/taglib.jsp"%>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="decorator" content="default"/>
    <meta name="viewport" content="width=device-width, initial-scale=1.0, minimum-scale=1.0, maximum-scale=1.0,user-scalable=no">
    <link rel="stylesheet" href="${ctxStatic}/train/css/bootstrap.min.css">
    <link rel="stylesheet" href="${ctxStatic}/train/css/exam.css">
    <script src="${ctxStatic}/train/js/jquery-1.11.3.js"></script>
    <script type="text/javascript">
		function page(n,s){
			$("#pageNo").val(n);
			$("#pageSize").val(s);
			$("#searchForm").submit();
	    	return false;
	    }
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
									<shiro:hasPermission name="ec:mtmyArticleList:deleteComment">
										<a href="${ctx}/ec/mtmyArticleList/deleteComment?id=${ArticleComment.id}&articlesId=${articlesId}" onclick="return confirmx('要删除该评论吗？', this.href)"  class="btn btn-default del-btn"><i class="glyphicon glyphicon-remove"></i> 删除</a>
									</shiro:hasPermission>
									<!--  
									<button type="button"  class="btn btn-default del-btn"><i class="glyphicon glyphicon-remove"></i> 删除</button>
									-->
									<p>${ArticleComment.contents}</p>
								</div>
							</div>
						</c:forEach>
					</form>
				</div>
				<table:page page="${page}"></table:page>
            </div>
		</div>
	</div>
</body>
</html>