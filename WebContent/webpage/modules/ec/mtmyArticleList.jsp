<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/webpage/include/taglib.jsp"%>
<html>
<head>
    <meta charset="utf-8">
    <meta name="decorator" content="default"/>
    <meta name="viewport" content="width=device-width, initial-scale=1.0, minimum-scale=1.0, maximum-scale=1.0,user-scalable=no">
    <link rel="stylesheet" href="${ctxStatic}/train/css/exam.css">
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
		
		//是否推荐/是否显示 改变事件
		function changeTableVal(parameter,id,flag){
			$(".loading").show();//打开展示层
			$.ajax({
				type : "POST",
				url : "${ctx}/ec/mtmyArticleList/change?id="+id+"&flag="+flag+"&parameter="+parameter,
				dataType: 'json',
				success: function(data) {
					$(".loading").hide(); //关闭加载层
					var status = data.STATUS;
					var flag = data.FLAG;
					if("OK" == status){
						$("#"+parameter+id).html("");//清除DIV内容	
						if(flag == '0'){
							//当前状态为【是】，则取消
							$("#"+parameter+id).append("<img width='20' height='20' src='${ctxStatic}/ec/images/open.png' onclick=\"changeTableVal('"+parameter+"','"+id+"','1')\">");
						}else if(flag == '1'){
							//当前状态为【否】，则打开
							$("#"+parameter+id).append("<img width='20' height='20' src='${ctxStatic}/ec/images/cancel.png' onclick=\"changeTableVal('"+parameter+"','"+id+"','0')\">");
						}
						if(parameter == "isShow"){
							$("#oldTASK"+id).hide();
							if(flag == '1'){
								$("#TASK"+id).show();
								$("#TASKDATE"+id).show();
							}else{
								$("#TASK"+id).hide();
								$("#TASKDATE"+id).hide();
							}
						}
					}else if("ERROR" == status){
						alert(data.MESSAGE);
					}
				}
			});   
		}
		
		//是否头条
		function newChangeTableVal(id,isTopline){
			$(".loading").show();//打开展示层
			$.ajax({
				type : "POST",
				url : "${ctx}/ec/mtmyArticleList/updateIsTopline?isTopline="+isTopline+"&id="+id,
				dataType: 'json',
				success: function(data) {
					$(".loading").hide(); //关闭加载层
					var status = data.STATUS;
					var isTopline = data.ISTOPLINE;
					if("OK" == status){
						$("#isTopline"+id).html("");//清除DIV内容
						if(isTopline == '1'){
							$("#isTopline"+id).append("<img width='20' height='20' src='${ctxStatic}/ec/images/open.png' onclick=\"newChangeTableVal('"+id+"','0')\">");
						}else if(isTopline == '0'){
							$("#isTopline"+id).append("<img width='20' height='20' src='${ctxStatic}/ec/images/cancel.png' onclick=\"newChangeTableVal('"+id+"','1')\">");
						}
					}else if("ERROR" == status){
						top.layer.alert(data.MESSAGE, {icon: 2, title:'提醒'});
					}
				}
			});   
		}
    </script>
    <title>文章管理</title>
</head>
<body>
	<div class="wrapper-content">
        <div class="ibox">
            <div class="ibox-title">
                <h5>文章管理</h5>
            </div>
            <sys:message content="${message}"/>
            <div class="ibox-content">
                <div class=" clearfix">
                	<div class="form-group">
                		<ul class="nav nav-tabs">
			                <li class="active" id="tab0"><a href="#" onclick="changeflag('0')" data-toggle="tab">已发布</a></li>
			                <li id="tab1"><a href="#" onclick="changeflag('1')" data-toggle="tab">待审核</a></li>
			                <li id="tab2"><a href="#" onclick="changeflag('2')" data-toggle="tab">已删除</a></li>                        
			            </ul>
                	</div>
                	<form id="myForm" action="${ctx}/ec/mtmyArticleList/list" method="post">
                		<input id="flag" name="flag" value="${mtmyArticle.flag }" type="hidden">
                	</form>
                    <form:form class="navbar-form navbar-left searcharea"  id="searchForm" modelAttribute="exercise" action="${ctx}/ec/mtmyArticleList/list" method="post">
                    	<!-- 分页隐藏文本框 -->
						<input id="pageNo" name="pageNo" type="hidden" value="${page.pageNo}"/>
						<input id="pageSize" name="pageSize" type="hidden" value="${page.pageSize}"/>
						<input id="flag" name="flag" value="${mtmyArticle.flag }" type="hidden"><!-- 隐藏文章类型 -->
                        <div class="form-group">
                            <label>文章标题：<input id="title" name="title" type="text" value="${mtmyArticle.title}" class="form-control" placeholder="搜索文章标题" maxlength="10"></label> 
                            <select class="form-control" id="categoryId" name="categoryId">
								   <option value=0>请选择分类</option>
								   <c:forEach items="${categoryList}" var="categoryList">
								   	   <c:choose>
											<c:when test="${mtmyArticle.categoryId eq categoryList.id}">
												<option value="${categoryList.id}" selected="selected">${categoryList.name}</option>
											</c:when>
											<c:otherwise>
												<option value="${categoryList.id}">${categoryList.name}</option>
											</c:otherwise>
										</c:choose>
								   </c:forEach>
							</select>
				  时间范围：<input id="beginDate" name="beginDate" type="text" maxlength="20" class="laydate-icon form-control layer-date input-sm"
								value="<fmt:formatDate value="${mtmyArticle.beginDate}" pattern="yyyy-MM-dd"/>" readonly="readonly"/>
							<label>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;--&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</label>
							<input id="endDate" name="endDate" type="text" maxlength="20" class=" laydate-icon form-control layer-date input-sm"
								value="<fmt:formatDate value="${mtmyArticle.endDate}" pattern="yyyy-MM-dd"/>" readonly="readonly"/>
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
	                        	<%-- <shiro:hasPermission name="ec:mtmyArticleList:sendMtmyArticle">
		                        	<a href="${ctx}/ec/mtmyArticleList/sendMtmyArticle"><button class="btn btn-white btn-sm" title="添加文章" data-placement="left" data-toggle="tooltip">
										<i class="fa fa-plus"></i>添加文章
									</button></a>
								</shiro:hasPermission> --%>
								<shiro:hasPermission name="ec:mtmyArticleList:categoryList">
									<button class="btn btn-white btn-sm" title="文章分类" onclick='top.openTab("${ctx}/ec/mtmyArticleList/categoryList","文章分类", false)' data-placement="left" data-toggle="tooltip">
										<i class="fa fa-plus"></i>文章分类
									</button>
								</shiro:hasPermission>
	                             	<%-- <table:delRow url="${ctx}/train/articlelist/deleteAll" id="treeTable"></table:delRow> --%><!-- 删除按钮 -->
	                        </div>
						</div>
					</div>
                </div>
                <table id="treeTable" class="table table-bordered table-hover table-striped">
                	<thead> 
                		<tr>
                			<th style="text-align: center;">ID</th>
                			<th style="text-align: center;">文章标题</th>
                			<th style="text-align: center;">类别</th>
                			<th style="text-align: center;">作者</th>
                			<th style="text-align: center;">文章点赞数</th>
                			<th style="text-align: center;">评论数</th>
                			<c:if test="${mtmyArticle.flag == 0}">
	                			<th style="text-align: center;">定时发布时间</th>
	                			<th style="text-align: center;">排序</th>
	                			<th style="text-align: center;">是否显示</th>
	                			<th style="text-align: center;">是否推荐</th>
	                			<th style="text-align: center;">是否置顶</th>
	                			<th style="text-align: center;">是否头条</th>
							</c:if>
							<c:if test="${mtmyArticle.flag != 2}">
                				<th style="text-align: center;">操作</th>
                			</c:if>
                		</tr>
                	</thead>
                	<tbody>
                		<c:forEach items="${page.list}" var="MtmyArticle">
                		<tr>
                			<td style="text-align: center;">${MtmyArticle.id}</td>
                			<td style="text-align: center;">${MtmyArticle.title}</td>
                			<td style="text-align: center;">${MtmyArticle.categoryName}</td>
                			<td style="text-align: center;">${MtmyArticle.authorId}</td>
                			<td style="text-align: center;">${MtmyArticle.likeNum}</td>
                			<td style="text-align: center;"><a href="#" onclick='top.openTab("${ctx}/ec/mtmyArticleList/findArticleComment?articlesId=${MtmyArticle.id}","文章评论", false)'>${MtmyArticle.comNum}</a></td>
                			<c:if test="${mtmyArticle.flag == 0}">
                			
	                			<td style="text-align: center;">
	                				<c:if test="${MtmyArticle.isShow == 1}">
	                					<div id="TASKDATE${MtmyArticle.id}"><fmt:formatDate value="${MtmyArticle.taskDate }" pattern="yyyy-MM-dd HH:mm:ss"/></div>
	                				</c:if>
	                			</td>
	                			<td style="text-align: center;">${MtmyArticle.sort}</td>
	                			<td style="text-align: center;" id="isShow${MtmyArticle.id}">
									<c:if test="${MtmyArticle.isShow == 1}">
										<img width="20" height="20" src="${ctxStatic}/ec/images/cancel.png" onclick="changeTableVal('isShow','${MtmyArticle.id}','0')">
									</c:if>
									<c:if test="${MtmyArticle.isShow == 0}">
										<img width="20" height="20" src="${ctxStatic}/ec/images/open.png" onclick="changeTableVal('isShow','${MtmyArticle.id}','1')">
									</c:if>
								</td>
								<td style="text-align: center;" id="isRecommend${MtmyArticle.id}">
									<c:if test="${MtmyArticle.isRecommend == 1}">
										<img width="20" height="20" src="${ctxStatic}/ec/images/cancel.png" onclick="changeTableVal('isRecommend','${MtmyArticle.id}','0')">
									</c:if>
									<c:if test="${MtmyArticle.isRecommend == 0}">
										<img width="20" height="20" src="${ctxStatic}/ec/images/open.png" onclick="changeTableVal('isRecommend','${MtmyArticle.id}','1')">
									</c:if>
								</td>
								<td style="text-align: center;" id="isTop${MtmyArticle.id}">
									<c:if test="${MtmyArticle.isTop == 1}">
										<img width="20" height="20" src="${ctxStatic}/ec/images/cancel.png" onclick="changeTableVal('isTop','${MtmyArticle.id}','0')">
									</c:if>
									<c:if test="${MtmyArticle.isTop == 0}">
										<img width="20" height="20" src="${ctxStatic}/ec/images/open.png" onclick="changeTableVal('isTop','${MtmyArticle.id}','1')">
									</c:if>
								</td>
								<td style="text-align: center;" id="isTopline${MtmyArticle.id}">
									<c:if test="${MtmyArticle.isTopline == 1}">
										<img width="20" height="20" src="${ctxStatic}/ec/images/open.png" onclick="newChangeTableVal('${MtmyArticle.id}','0')">
									</c:if>
									<c:if test="${MtmyArticle.isTopline == 0}">
										<img width="20" height="20" src="${ctxStatic}/ec/images/cancel.png" onclick="newChangeTableVal('${MtmyArticle.id}','1')">
									</c:if>
								</td>
							</c:if>
							<c:if test="${mtmyArticle.flag != 2}">
								<td style="text-align: center;">
									<c:if test="${mtmyArticle.flag == 1}">
							    		<shiro:hasPermission name="ec:mtmyArticleList:auditArticle">
											<span class="btn del-btn"><a href="${ctx}/ec/mtmyArticleList/auditArticle?id=${MtmyArticle.id}" class="btn btn-success btn-xs"><i class="fa fa-edit"></i> 审核通过</a></span>
										</shiro:hasPermission>
							    	</c:if>
							    	<c:if test="${mtmyArticle.flag == 0}">
		                				<shiro:hasPermission name="ec:mtmyArticleList:editArticle">
		                					<a href="#" onclick="openDialog('编辑', '${ctx}/ec/mtmyArticleList/editArticle?id=${MtmyArticle.id}&flag=${MtmyArticle.flag }&key=NO','600px', '550px')" class="btn btn-success btn-xs"><i class="fa fa-edit"></i> 编辑</a>
										</shiro:hasPermission>
										<c:if test="${MtmyArticle.isShow == 1}">
							    			<a id="oldTASK${MtmyArticle.id}" href="#" onclick="openDialog('定时发布', '${ctx}/ec/mtmyArticleList/editArticle?id=${MtmyArticle.id}&flag=${MtmyArticle.flag }&key=YEStask','600px', '550px')" class="btn btn-success btn-xs"><i class="fa fa-edit"></i> 定时发布</a>
							    		</c:if>
							    		<a id="TASK${MtmyArticle.id}" href="#" onclick="openDialog('定时发布', '${ctx}/ec/mtmyArticleList/editArticle?id=${MtmyArticle.id}&flag=${MtmyArticle.flag }&key=YEStask','600px', '550px')" class="task btn btn-success btn-xs"><i class="fa fa-edit"></i> 定时发布</a>
									</c:if>
										<shiro:hasPermission name="ec:mtmyArticleList:deleteArticle">
											<a href="${ctx}/ec/mtmyArticleList/deleteArticle?id=${MtmyArticle.id}" onclick="return confirmx('要删除该文章吗？', this.href)" class="btn btn-danger btn-xs"><i class="fa fa-trash"></i>删除</a>
										</shiro:hasPermission>
	                			</td>
	                		</c:if>
                		</tr>
                		</c:forEach>
                	</tbody>
                </table>
                <table:page page="${page}"></table:page>
	        </div>
	    </div>
	    <div class="loading"></div>
    </div>
</body>
</html>