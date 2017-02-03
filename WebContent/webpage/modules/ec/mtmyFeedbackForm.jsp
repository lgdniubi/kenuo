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
				<div class="comments_con">
					<span class="usersname">${feedback.users.nickname}</span>
					<p><span class="times"><fmt:formatDate value="${feedback.msgTime}" pattern="yyyy-MM-dd HH:mm:ss"/></span></p>
				</div>
			</div>
        	<div class="ibox-title">
                <h5>反馈详情
                <c:if test="${feedback.msgStatus==0}">
                	(未解决)
                </c:if>
                <c:if test="${feedback.msgStatus==1}">
                	(已解决)
                </c:if>
                </h5>
            </div>
            <div class="ibox-content">
			        <ul class="artical-content" >
			       		<li>${feedback.msgContent }</li>
			       		<li>
				       		<c:if test="${feedback.messageImg != null}">
				       			<c:forEach items="${photo }" var="photo">
				       				<img alt="" width="150px" height="150px" style="padding:3px" src="${photo }">
				       			</c:forEach>
				       		</c:if>
			       		</li>
			        </ul>
			    </div>
			</div>
		</div>
	</body>
</html>