<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/webpage/include/taglib.jsp"%>
<html>
<head>
 	<meta name="decorator" content="default"/>
    <link rel="stylesheet" href="${ctxStatic}/train/css/exam.css">
    <script src="${ctxStatic}/train/js/jquery-1.11.3.js"></script>
    <script type="text/javascript">

    </script>
    <title>反馈详情</title>
</head>
<body>
    <div class="wrapper-content">
        <div class="ibox">
           <div class="comment_areas clearfix">
				<div class="user_photo"><img src="${feedback.photo }" alt="" class="img-responsive"></div>
				<div class="comments_con">
					<span class="usersname">${feedback.name}</span>
					<p><span class="times"><fmt:formatDate value="${feedback.createtime}" pattern="yyyy-MM-dd HH:mm:ss"/></span></p>
				</div>
			</div>
        	<div class="ibox-title">
                <h5>反馈详情
                <c:if test="${feedback.fbStatus==0}">
                	(未解决)
                </c:if>
                <c:if test="${feedback.fbStatus==1}">
                	(已解决)
                </c:if>
                </h5>
            </div>
            <div class="ibox-content">
			        <ul class="artical-content" >
			       		<li>${feedback.content }</li>
			        </ul>
			    </div>
			</div>
		</div>
	</body>
</html>