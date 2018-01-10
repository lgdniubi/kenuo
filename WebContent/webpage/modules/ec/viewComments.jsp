<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/webpage/include/taglib.jsp"%>
<html>
<head>
	<meta name="decorator" content="default"/>
	<link rel="stylesheet" href="${ctxStatic}/train/css/exam.css">
	<link href="${ctxStatic}/train/css/imgbox.css" rel="stylesheet" />
	<style>
		.modal-dialog{width:600px;}
	</style>

	<script src="${ctxStatic}/train/js/jquery.min.js"></script>
	<script type="text/javascript">
		var jq = $.noConflict();
	</script>
	<script src="${ctxStatic}/train/js/jquery.imgbox.pack.js"></script>
	<script type="text/javascript">
	function lookBeauty(num){
		 loading("正在查询，请稍后 . . .");
		 $("#newComment").empty();
		 $("#photo").empty();
		 $("#parentId").val(num); 
		 $.ajax({
			 type : 'POST',
			 url : '${ctx}/ec/mtmycomment/oneBeautyComment?commentId='+num,
			 dataType: 'json',
			 success:function(data){
				var a;
				$.each(data.nowcomment, function(index,item){
					if(item.parentId == 0){
						$("#connent").html(item.contents);
						$("#myModalLabel").html("用户名："+item.users.name);
						$("#pullTime").html("发表时间："+item.newTime);
						$("input[type=radio][name=isShow][value="+item.isShow+"]").attr("checked",'checked');
						if(null != item.img){
							cutimg(item.img);
						}
						$("input[type=radio][name=isShow][value="+item.isShow+"]").attr("checked",'checked');
						a=item.userId;
					}else{
						if(item.userId == a){
							$("#newComment").append("<div class='comment_areas clearfix' id="+item.commentId+"><h5>追加评论：</h5><p>"+item.contents+"</p></div>");
							if(null != item.img){
								 var arr=new Array();
					             arr=item.img.split(',');		//注split可以用字符或字符串分割
						         for(var i=0;i<arr.length;i++){
						        	 if(arr[i] != ''){
						            	  $("#"+item.commentId).append("<a class='img' href="+arr[i]+"><img width='150px' height='150px' style='padding:3px' src='"+arr[i]+"'></a>");
						        	  }
						          }
							}
						}else{
							$("#newComment").append("<div class='comment_areas clearfix'><h5>回复内容：</h5>回复用户："+item.user.name+"<p>"+item.contents+"</p></div>");
						}
					}
				});
				$('#commentModal').modal('show');
				closeTip();
				
				jq("a[class='img']").imgbox({
					'speedIn'		: 0,
					'speedOut'		: 0,
					'alignment'		: 'center',
					'overlayShow'	: true,
					'allowMultiple'	: false
				});
			 }
		 })
	}
	function cutimg(img){
		var arr=new Array();
       arr=img.split(',');		//注split可以用字符或字符串分割
      for(var i=0;i<arr.length;i++){
   	   if(arr[i] != ''){
       		$("#photo").append("<a class='img' href="+arr[i]+"><img width='150px' height='150px' style='padding:3px' src='"+arr[i]+"'></a>");
   	   }
       }
	}
	function tijiao(reservationId){
		if($("#contents").val()!=''){
			loading('正在提交，请稍等...');
			
    		$("#inputForm").attr("action","${ctx}/ec/mtmyMnappointment/replybeautyComment?parentId="+$("#parentId").val()+"&contents="+$('#contents').val()+"&isShow="+$('input:radio[name="isShow"]:checked').val()+"&reservationId="+reservationId);
			$("#inputForm").submit();
		}else{
			$("#bt").show();
		} 
	}
	
	function lookShop(num,id){
		 loading("正在查询，请稍后 . . .");
		 $("#newComment2").empty();
		 $("#photo2").empty();
		 $("#parentId2").val(num); 
		 $("#shopId").val(id); 
		 $.ajax({
			 type : 'POST',
			 url : '${ctx}/ec/mtmycomment/oneShopComment?commentId='+num,
			 dataType: 'json',
			 success:function(data){
				var a;
				$.each(data.nowcomment, function(index,item){
					if(item.parentId == 0){
						$("#connent2").html(item.contents);
						$("#myModalLabel2").html("用户名："+item.users.name);
						$("#pullTime2").html("发表时间："+item.newTime);
						$("input[type=radio][name=isShow2][value="+item.isShow+"]").attr("checked",'checked');
						if(null != item.img){
							//有照片时  显示照片
							cutimg2(item.img);
						}
						a=item.userId;
					}else{
						//当用户名相同的时候则为用户追加评论   else则为客服回复评论
						if(item.userId == a){
							$("#newComment2").append("<div class='comment_areas clearfix' id="+item.commentId+"><h5>追加评论：</h5><p>"+item.contents+"</p></div>");
							if(null != item.img){
								//有照片时  显示照片
								 var arr=new Array();
					             arr=item.img.split(',');		//注split可以用字符或字符串分割
						         for(var i=0;i<arr.length;i++){
						        	 if(arr[i] != ''){
						            	  $("#"+item.commentId).append("<a class='img' href="+arr[i]+"><img width='150px' height='150px' style='padding:3px' src='"+arr[i]+"'></a>");
						        	  }
						         }
							}
						}else{
							$("#newComment2").append("<div class='comment_areas clearfix'><h5>回复内容：</h5>回复用户："+item.user.name+"<p>"+item.contents+"</p></div>");
						}
					} 
				 });
				$('#commentModal2').modal('show');
				closeTip(); 
				
				jq("a[class='img']").imgbox({
					'speedIn'		: 0,
					'speedOut'		: 0,
					'alignment'		: 'center',
					'overlayShow'	: true,
					'allowMultiple'	: false
				});
			 }
		 })
	}
	function cutimg2(img){
		var arr=new Array();
       arr=img.split(',');		//注split可以用字符或字符串分割
      for(var i=0;i<arr.length;i++){
   	   if(arr[i] != ''){
       		$("#photo2").append("<a class='img' href="+arr[i]+"><img width='150px' height='150px' style='padding:3px' src='"+arr[i]+"'></a>");
   	   }
       }
	}
	function tijiao2(reservationId){
		if($("#contents2").val()!=''){
			loading('正在提交，请稍等...');
			$("#inputForm").attr("action","${ctx}/ec/mtmyMnappointment/replyShopComment?parentId="+$("#parentId2").val()+"&contents="+$('#contents2').val()+"&isShow="+$('input:radio[name="isShow2"]:checked').val()+"&shopId="+$('#shopId').val()+"&reservationId="+reservationId);
			$("#inputForm").submit();
			
		}else{
			$("#bt2").show();
		} 
	}
	</script>
</head>
<body class="gray-bg">
	<div class="wrapper wrapper-content">
		<div class="ibox">
			<sys:message content="${message}"/>
			<!-- 查询条件 -->
	    	<div class="ibox-content">
		    	<form:form id="inputForm" modelAttribute="articleRepository" action="" method="post" class="form-horizontal">
		    	</form:form>
				<!-- 工具栏 -->
				<table id="contentTable" class="table table-striped table-bordered  table-hover table-condensed  dataTables-example dataTable no-footer">
					<thead>
						<tr>
							<th style="text-align: center;">编号</th>
						    <th style="text-align: center;">用户名</th>
						    <th style="text-align: center;">评价时间</th>
						    <th style="text-align: center;">评价对象</th>
						    <th style="text-align: center;">评价内容</th>
						    <th style="text-align: center;">操作</th>
						</tr>
					</thead>
					<tbody style="text-align: center;">
						<c:forEach items="${beautyComment}" var="comment">
							<tr>
								<td>${comment.commentId }</td>
							  	<td>${comment.users.name }</td>
							    <td><fmt:formatDate value="${comment.addTime}" pattern="yyyy-MM-dd"/></td>
							  	<td>${comment.user.name}</td>
							  	<td>
							  		<!-- 当文本内容大于5个字符时，只显示其前五个字符 -->
								  	<c:choose>  
								         <c:when test="${fn:length(comment.contents) > 10}">  
								             <c:out value="${fn:substring(comment.contents, 0, 10)}..." />  
								         </c:when>  
								        <c:otherwise>  
								           <c:out value="${comment.contents}" />  
								        </c:otherwise>  
								    </c:choose>
							  	</td>
							  	<td>
							     	<button class="btn btn-info btn-xs" onclick="lookBeauty('${comment.commentId }')"><i class="fa fa-search-plus"></i> 查看详情</button>
							    </td>
							</tr>
						</c:forEach>
						<tbody style="text-align: center;">
						<c:forEach items="${shopComment}" var="comment">
							<tr>
								<td>${comment.commentId }</td>
							  	<td>${comment.users.name }</td>
							    <td><fmt:formatDate value="${comment.addTime}" pattern="yyyy-MM-dd"/></td>
							  	<td>${comment.shopName}</td>
							  	<td>
							  		<!-- 当文本内容大于5个字符时，只显示其前五个字符 -->
								  	<c:choose>  
								         <c:when test="${fn:length(comment.contents) > 10}">  
								             <c:out value="${fn:substring(comment.contents, 0, 10)}..." />  
								         </c:when>  
								        <c:otherwise>  
								           <c:out value="${comment.contents}" />  
								        </c:otherwise>  
								    </c:choose>
							  	</td>
							  	<td>
							     	<button class="btn btn-info btn-xs" onclick="lookShop('${comment.commentId }','${comment.shopId }')"><i class="fa fa-search-plus"></i> 查看详情</button>
							    </td>
							</tr>
						</c:forEach>
					</tbody>
				</table>
			</div>
		</div>
	</div>
	<!-- 美容师评论对话框 -->
	<div class="modal fade bs-example-modal-lg" tabindex="-1" role="dialog" id="commentModal" >
		<div class="modal-dialog modal-lg">
		    <div class="modal-content">
		    	<div class="modal-header">
		    		<button type="button" class="close" data-dismiss="modal" aria-label="Close"><span aria-hidden="true">&times;</span></button>
		        	<h4 class="modal-title" id="myModalLabel"></h4>
				</div>
				<div class="modal-body">
					<div class="comment_areas clearfix">
						<h5 id="pullTime"></h5>
						<h5>评论内容：</h5>
						<div class="comments_con">
				      		<p id="connent"></p>
				      		<div id="photo"></div>
						</div>
					</div>
		      		<div id="newComment"></div>
						<textarea name="contents" class="form-control" id="contents" rows="6" style="width: 100%;margin-top: 10px"></textarea>
						<p id="bt" style="display:none"><font color="red" size="2">*  内容不能为空</font></p>
				      	<!-- 回复评论 隐藏评论ID -->
						<input type="hidden" id="parentId" name="parentId">
			      		<h5>权限设置；<input type="radio" name="isShow" checked="checked" value="0"> 所有人可见　<input type="radio" name="isShow" value="1"> 用户本人可见</h5>
		      	</div>
				<div class="modal-footer">
					<button type="button" class="btn btn-success" onclick="tijiao('${reservationId}')">发表回复</button>
		      	</div>
		    </div>
 		</div>
	</div>
	<div class="loading"></div>
	
	<!-- 店铺评论对话框 -->
	<div class="modal fade bs-example-modal-lg" tabindex="-1" role="dialog" id="commentModal2">
 		<div class="modal-dialog modal-lg">
		    <div class="modal-content">
		    	<div class="modal-header">
		    		<button type="button" class="close" data-dismiss="modal" aria-label="Close"><span aria-hidden="true">&times;</span></button>
		        	<h4 class="modal-title" id="myModalLabel2"></h4>
				</div>
				<div class="modal-body">
					<div class="comment_areas clearfix">
						<h5 id="pullTime2"></h5>
						<h5>评论内容：</h5>
						<div class="comments_con">
				      		<p id="connent2"></p>
				      		<div id="photo2"></div>
						</div>
					</div>
		      		<div id="newComment2"></div>
					<textarea name="contents2" class="form-control" id="contents2" rows="6" style="width: 100%;margin-top: 10px"></textarea>
					<p id="bt2" style="display:none"><font color="red" size="2">*  内容不能为空</font></p>
			      	<!-- 回复评论 隐藏评论ID -->
					<input type="hidden" id="parentId2" name="parentId2">
					<input type="hidden" id="shopId" name="shopId">
		      		<h5>权限设置；<input type="radio" id="isShow2" name="isShow2" checked="checked" value="0"> 所有人可见　<input type="radio" id="isShow2" name="isShow2" value="1"> 用户本人可见</h5>
		      	</div>
				<div class="modal-footer">
					<button type="button" class="btn btn-success" onclick="tijiao2('${reservationId}')">发表回复</button>
		      	</div>
		    </div>
		</div>
	</div>
	<div class="loading"></div>
</body>
</html>