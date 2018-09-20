<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/webpage/include/taglib.jsp"%>

<html>
<head>
	<title>凭证</title>
	<meta name="decorator" content="default" />
	<!-- 凭证图片 -->
	<link href="${ctxStatic}/train/css/jqueryPhoto.css" type="text/css" rel="stylesheet">
</head>
<body>
	<div class="htmleaf-container">
		<div class="mod18">
			<span id="prev" class="btn prev"></span>
			<span id="next" class="btn next"></span>
			<span id="prevTop" class="btn prev"></span>
			<span id="nextTop" class="btn next"></span>
			<div id="picBox" class="picBox">
				<ul class="cf">
					<c:if test="${not empty proofList}">
					<c:forEach items="${proofList}" var="proof">
					<li> <a href="#"><img src="${proof}" alt=""></a> </li>
					</c:forEach>
					</c:if>
				<!-- 	<li> <a href="#"><img src="http://10.10.8.22:9377/resource/images/2018/06/f19da03e-5f84-462c-84a3-59ab28c2f1b8.jpg" alt=""></a> </li>
					<li> <a href="#"><img src="http://10.10.8.22:9377/resource/images/2018/06/f19da03e-5f84-462c-84a3-59ab28c2f1b8.jpg" alt=""></a> </li>
					<li> <a href="#"><img src="http://10.10.8.22:9377/resource/images/2018/06/f19da03e-5f84-462c-84a3-59ab28c2f1b8.jpg" alt=""></a> </li> -->
				</ul>
			</div>
			<div id="listBox" class="listBox">
				<ul class="cf">
					<c:if test="${not empty proofList}">
					<c:forEach items="${proofList}" var="proof">
					<li class="on"> <img src="${proof}" alt=""> </li>
					</c:forEach>
					</c:if>
					<!-- <li class="on"> <img src="http://10.10.8.22:9377/resource/images/2018/06/f19da03e-5f84-462c-84a3-59ab28c2f1b8.jpg" alt=""> </li>
					<li class="on"> <img src="http://10.10.8.22:9377/resource/images/2018/06/f19da03e-5f84-462c-84a3-59ab28c2f1b8.jpg" alt=""> </li>
					<li class="on"> <img src="http://10.10.8.22:9377/resource/images/2018/06/f19da03e-5f84-462c-84a3-59ab28c2f1b8.jpg" alt=""> </li> -->
				</ul>
			</div>
			<div class="clear"></div>
		</div>
	</div>
	
	<script type="text/javascript" src="${ctxStatic}/train/imgZoom/jqueryPhoto.js"></script>
</body>
</html>