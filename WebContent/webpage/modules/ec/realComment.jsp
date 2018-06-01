<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/webpage/include/taglib.jsp"%>
<html>
<head>
	<title>商品评论管理</title>
	<meta name="decorator" content="default"/>
	<link rel="stylesheet" href="${ctxStatic}/train/css/exam.css">
	<link href="${ctxStatic}/train/css/imgbox.css" rel="stylesheet" />
	<script src="${ctxStatic}/train/js/jquery.min.js"></script>
	<script type="text/javascript">
		var jq = $.noConflict();
	</script>
	<script src="${ctxStatic}/train/js/jquery.imgbox.pack.js"></script>
	<script type="text/javascript">
		function page(n,s){
			$("#pageNo").val(n);
			$("#pageSize").val(s);
			$("#searchForm").submit();
	    	return false;
	    }
		$(document).ready(function() {
		    var start = {
			    elem: '#beginDate',
			    format: 'YYYY-MM-DD hh:mm:ss',
			    event: 'focus',
			    max: $("#endDate").val(),   //最大日期
			    istime: true,				//是否显示时间
			    isclear: false,				//是否显示清除
			    istoday: false,				//是否显示今天
			    issure: true,				//是否显示确定
			    festival: true,				//是否显示节日
			    choose: function(datas){
			         end.min = datas; 		//开始日选好后，重置结束日的最小日期
			         end.start = datas 		//将结束日的初始值设定为开始日
			    }
			};
			var end = {
			    elem: '#endDate',
			    format: 'YYYY-MM-DD hh:mm:ss',
			    event: 'focus',
			    min: $("#beginDate").val(),
			    istime: true,
			    isclear: false,
			    istoday: false,
			    issure: true,
			    festival: true,
			    choose: function(datas){
			        start.max = datas; //结束日选好后，重置开始日的最大日期
			    }
			};
			laydate(start);
			laydate(end);
	    })
		function look(num){
			 loading("正在查询，请稍后 . . .");
			 $("#newComment").empty();
			 $("#contents").val('');
			 $("#photo").empty();
			 $("#parentId").val(num); 
			 $.ajax({
				 type : 'POST',
				 url : '${ctx}/ec/mtmycomment/oneRealComment?commentId='+num,
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
								//有照片时  显示照片
								cutimg(item.img);
							}
							a=item.userId;
						}else{
							//当用户名相同的时候则为用户追加评论   else则为客服回复评论
							if(item.userId == a){
								$("#newComment").append("<div class='comment_areas clearfix' id="+item.commentId+"><h5>追加评论：</h5><p>"+item.contents+"</p></div>");
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
		function tijiao(){
			if($("#contents").val()!=''){
				loading('正在提交，请稍等...');
				$.ajax({
					 type : 'POST',
					 url : '${ctx}/ec/mtmycomment/replyRealComment',
					 data:{'parentId':$("#parentId").val(),'contents':$('#contents').val(),'isShow':$('input:radio:checked').val()},
					 success:function(index,data){
						//top.layer.close(index);//关闭对话框。
						closeTip();
						showTip('回复评论成功','success');
						//	页面刷新等待1秒 弹出回复成功框
						setTimeout(function(){
							sortOrRefresh()
							}, 1000);
					 },
					 error: function(XMLHttpRequest, textStatus, errorThrown) {
						 closeTip();
						 showTip('回复评论失败','error');
					}
				 })
			}else{
				$("#bt").show();
			} 
		}
		function nowreset(){
			 overShade();
			 $("#usersName").val(""); //清空
			 $("#userName").val(""); //清空
			 $("#beginDate").val(""); //清空
			 $("#endDate").val(""); //清空
			 $("#whetherImg").val(""); //清空
			 $("#isReplay").val(""); //清空
			 $("#pageNo").val("");
			 $("#pageSize").val("");
		}
	</script>
</head>
<body class="gray-bg">
	<div class="wrapper wrapper-content">
		<div class="ibox">
			<div class="ibox-title">
				<h5>商品评论管理</h5>
			</div>
			<sys:message content="${message}"/>
			<!-- 查询条件 -->
	    	<div class="ibox-content">
				<div class="clearfix">
					<form id="searchForm" action="${ctx}/ec/mtmycomment/realComment" method="post" class="navbar-form navbar-left searcharea">
						<!-- 翻页隐藏文本框 -->
						<input id="pageNo" name="pageNo" type="hidden" value="${page.pageNo}"/>
						<input id="pageSize" name="pageSize" type="hidden" value="${page.pageSize}"/>
						<table:sortColumn id="orderBy" name="orderBy" value="${page.orderBy}" callback="sortOrRefresh();"/><!-- 支持排序 -->
						<div class="form-group">
							<input id="usersName" name="usersName" type="text" value="${comment.usersName }" class="form-control" placeholder="用户名"> 
							<input id="userName" name="userName" type="text" value="${comment.userName }" class="form-control" placeholder="商品名"> 
							<label>评论时间：</label>
							<input id="beginDate" name="beginDate" type="text" maxlength="20" class="laydate-icon form-control layer-date input-sm" value="<fmt:formatDate value="${comment.beginDate }" pattern="yyyy-MM-dd HH:mm:ss"/>" placeholder="开始时间" readonly="readonly"/>
							<label>&nbsp;&nbsp;--&nbsp;&nbsp;</label>
							<input id="endDate" name="endDate" type="text" maxlength="20" class=" laydate-icon form-control layer-date input-sm" value="<fmt:formatDate value="${comment.endDate }" pattern="yyyy-MM-dd HH:mm:ss"/>" placeholder="结束时间" readonly="readonly"/>
							<label>是否有图：</label>
							<select id ="whetherImg" name="whetherImg" class="form-control" style="width:185px;">
								<option value="" <c:if test="${comment.whetherImg == ''}">selected</c:if>>全部</option>
								<option value="0" <c:if test="${comment.whetherImg == '0'}">selected</c:if>>有图</option>
								<option value="1" <c:if test="${comment.whetherImg == '1'}">selected</c:if>>无图</option>
							</select>	
							<label>是否回复：</label>
							<select id ="isReplay" name="isReplay" class="form-control" style="width:185px;">
								<option value="" <c:if test="${comment.isReplay == ''}">selected</c:if>>全部</option>
								<option value="0" <c:if test="${comment.isReplay == '0'}">selected</c:if>>未回复</option>
								<option value="1" <c:if test="${comment.isReplay == '1'}">selected</c:if>>已回复</option>
							</select>	
						</div>
						<div class="pull-right">
							<button  class="btn btn-primary btn-rounded btn-outline btn-sm " onclick="search()" ><i class="fa fa-search"></i> 查询</button>
							<button  class="btn btn-primary btn-rounded btn-outline btn-sm " onclick="nowreset()" ><i class="fa fa-refresh"></i> 重置</button>
						</div>
					</form>
				</div>
				<!-- 工具栏 -->
				<div class="row">
					<div class="col-sm-12">
						<div class="pull-left">
							<!-- 导出 -->
							<shiro:hasPermission name="ec:mtmycomment:export">
								<table:exportExcel url="${ctx}/ec/mtmycomment/export"></table:exportExcel>
							</shiro:hasPermission>
						</div>
					</div>
				</div>
				<table id="contentTable" class="table table-striped table-bordered  table-hover table-condensed  dataTables-example dataTable no-footer">
					<thead>
						<tr>
							<th style="text-align: center;">编号</th>
						    <th style="text-align: center;">用户名</th>
						    <th style="text-align: center;">相关商品</th>
						    <th style="text-align: center;">评论时间</th>
						    <th style="text-align: center;">星级</th>
						    <th style="text-align: center;">评论内容</th>
						    <th style="text-align: center;">操作</th>
						</tr>
					</thead>
					<tbody style="text-align: center;">
						<c:forEach items="${page.list }" var="realComment">
							<tr>
							  	<td>${realComment.commentId }</td>
							  	<td>${realComment.users.name }</td>
							  	<td>${realComment.goods.goodsName }</td>
							  	<td><fmt:formatDate value="${realComment.addTime }" pattern="yyyy-MM-dd HH:mm:ss"/></td>
							  	<td>${realComment.goodsRank }</td>
							  	<td>
							  		<!-- 当文本内容大于5个字符时，只显示其前五个字符 -->
								  	<c:choose>  
								         <c:when test="${fn:length(realComment.contents) > 10}">  
								             <c:out value="${fn:substring(realComment.contents, 0, 10)}..." />  
								         </c:when>  
								        <c:otherwise>  
								           <c:out value="${realComment.contents}" />  
								        </c:otherwise>  
								    </c:choose>   
							  	</td>
							    <td>
							     	<button class="btn btn-info btn-xs" onclick="look('${realComment.commentId }')"><i class="fa fa-search-plus"></i> 查看详细</button>
							    </td>
							</tr>
						</c:forEach>
					</tbody>
					<tfoot>
 						<tr>
                            <td colspan="20">
                                <!-- 分页代码 --> 
                                <div class="tfoot">
                                </div>
                               	<table:page page="${page}"></table:page>
                            </td>	
                        </tr>
                    </tfoot>
				</table>
			</div>
		</div>
	</div>
	<div class="loading"></div>
	<!-- 评论对话框 -->
	<div class="modal fade bs-example-modal-lg" tabindex="-1" role="dialog" id="commentModal">
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
		      		<h5>权限设置:<input type="radio" id="isShow" name="isShow" checked="checked" value="0"> 所有人可见　<input type="radio" id="isShow" name="isShow" value="1"> 用户本人可见</h5>
		      	</div>
				<div class="modal-footer">
					<button type="button" class="btn btn-success" onclick="tijiao()">发表回复</button>
		      	</div>
		    </div>
		</div>
	</div>
</body>
</html>