<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/webpage/include/taglib.jsp"%>
<html lang="en">
<head>
 	<meta charset="UTF-8">
 	<meta name="decorator" content="default"/>
    <meta name="viewport" content="width=device-width, initial-scale=1.0, minimum-scale=1.0, maximum-scale=1.0,user-scalable=no">
    <link rel="stylesheet" href="${ctxStatic}/train/css/exam.css">
    <script src="${ctxStatic}/train/js/jquery-1.11.3.js"></script>
    <script type="text/javascript">
  //页面加载事件
	$(document).ready(function() {
		if($("#askType").val()==1){
		    $('#img').hide();
		    $('#video').hide();
		}
		if($("#askType").val()==2){
		    $('#img').show();
		    $('#video').hide();
		}
		if($("#askType").val()==3){
		    $('#video').show();
		    $('#img').hide();
		}
	});
    </script>
    <title>问答详情</title>
</head>
<body>
    <div class="wrapper-content">
        <div class="ibox">
           	<div class="comment_areas clearfix">
				<div class="user_photo"><img src="${lessonAskContent.photo }" alt="" class="img-responsive"></div>
				<div class="comments_con">
					<span class="usersname">${lessonAskContent.name}</span>
					<p><span class="times"><fmt:formatDate value="${lessonAskContent.createtime}" pattern="yyyy-MM-dd HH:mm:ss"/></span></p>
				</div>
			</div>
        	<div class="ibox-title">
                <h5>问答详情</h5>
            </div>
            <sys:message content="${message}"/>
            <div class="ibox-content">
				<div class="artical-top" id="artical-tit">${lessonAskContent.title }</div>
			        <ul class="artical-content" >
			         	<input id="askType" name="askType" type="hidden" value="${lessonAskContent.askType }"/>
			       		<li>${lessonAskContent.textcontent }</li>
				        <li id="video">
					          <c:forEach items="${contentList}" var="contentList">
								 <video width="320" height="240" controls="controls">
								   <source src="${contentList.content }" type="video/mp4" />
								 </video> 
							  </c:forEach>
						</li>
						<c:forEach items="${contentList}" var="contentList">
					        <li id="img">
						            <img src="${contentList.content }" alt="">
					        </li>
					    </c:forEach>
			        </ul>
			        <form action="${ctx}/train/faqlist/comment" class="reviewarea" id="reviewarea">
			        	<input id="askId" name="askId" type="hidden" value="${lessonAskContent.askId }"/>
				    	<textarea name="content" id="reviewText" required maxlength="500"></textarea>
				    	<shiro:hasPermission name="train:faqlist:add">
				    		<button class="btn btn-primary review-btn"  type="submit">确认回复</button>
				    	</shiro:hasPermission>
				    </form>
			    </div>
			</div>
		</div>
	</body>
	<script type="text/javascript">
	$.validator.setDefaults({
	    submitHandler: function() {
	    }
	});
	$().ready(function() {
	    $("#reviewarea").validate();
	});
	</script>
</html>