<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/webpage/include/taglib.jsp"%>

<html>
<head>
	<title>营业执照</title>
	<meta name="decorator" content="default" />
	<!-- 凭证图片 -->
	<link rel="stylesheet" type="text/css" href="${ctxStatic}/train/css/normalize.css" /><!--CSS RESET-->
	<link href="${ctxStatic}/train/css/lightgallery.css" rel="stylesheet">
</head>
<body>
	<div class="htmleaf-container">
		
		<div class="demo-gallery">
            <ul id="lightgallery" class="list-unstyled row">
               <c:if test="${not empty imgUrl}">
					<li class="col-xs-6 col-sm-4 col-md-3" data-src="${imgUrl}"> 
					<a href="#"><img class="img-responsive" src="${imgUrl}" alt=""></a> 
					</li>
				</c:if>
            </ul>
        </div>
		
	</div>
	
	<script src="${ctxStatic}/train/imgZoom/lightgallery-all.min.js"></script>
	<script type="text/javascript">
       $(document).ready(function(){
           $('#lightgallery').lightGallery({


           });
           $('#lightgallery').find('li').first().click()
       });
    </script>

<!--  -->
	
	
	<script type="text/javascript" src="${ctxStatic}/train/imgZoom/jqueryPhoto.js"></script>
</body>
</html>