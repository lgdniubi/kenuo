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
			$("#tab" + num).addClass('active').siblings().removeClass('active');
			$(".task").hide();
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
		function changeTableVal(flag,id,isyesno){
			$(".loading").show();//打开展示层
			$.ajax({
				type : "POST",
				url : "${ctx}/train/articleslist/updateArticleIS?articleId="+id+"&FLAG="+flag+"&ISYESNO="+isyesno,
				dataType: 'json',
				success: function(data) {
					$(".loading").hide(); //关闭加载层
					var status = data.STATUS;
					var isyesno = data.ISYESNO;
					if("OK" == status){
						$("#"+flag+id).html("");//清除DIV内容	
						if(isyesno == 1){
							//当前状态为【否】，则打开
							$("#"+flag+id).append("<img width='20' height='20' src='${ctxStatic}/ec/images/cancel.png' onclick=\"changeTableVal('"+flag+"','"+id+"',0)\">");
						}else if(isyesno == 0){
							//当前状态为【是】，则取消
							$("#"+flag+id).append("<img width='20' height='20' src='${ctxStatic}/ec/images/open.png' onclick=\"changeTableVal('"+flag+"','"+id+"',1)\">");
						}
						if(flag == "ISSHOW"){
							$("#oldTASK"+id).hide();
							if(isyesno == 1){
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
                	<form id="myForm" action="${ctx}/train/articleslist/list" method="post">
                		<input id="flag" name="flag" value="${articles.flag }" type="hidden">
                	</form>
                    <form:form class="navbar-form navbar-left searcharea"  id="searchForm" modelAttribute="articles" action="${ctx}/train/articleslist/list" method="post">
                    	<!-- 分页隐藏文本框 -->
						<input id="pageNo" name="pageNo" type="hidden" value="${page.pageNo}"/>
						<input id="pageSize" name="pageSize" type="hidden" value="${page.pageSize}"/>
						<input id="flag" name="flag" value="${articles.flag }" type="hidden"><!-- 隐藏文章类型 -->
                        <div class="form-group">
                            <label>文章标题：<form:input path="title" class="form-control" placeholder="搜索文章标题"/></label>  
                            <select class="form-control" id="categoryId" name="categoryId">
								   <option value=0>请选择分类</option>
								   <c:forEach items="${categoryList}" var="categoryList">
								   	   <c:choose>
											<c:when test="${articles.categoryId eq categoryList.categoryId}">
												<option value="${categoryList.categoryId}" selected="selected">${categoryList.name}</option>
											</c:when>
											<c:otherwise>
												<option value="${categoryList.categoryId}">${categoryList.name}</option>
											</c:otherwise>
										</c:choose>
								   </c:forEach>
							</select>
				  时间范围：<input id="beginDate" name="beginDate" type="text" maxlength="20" class="laydate-icon form-control layer-date input-sm"
								value="<fmt:formatDate value="${articles.beginDate}" pattern="yyyy-MM-dd"/>" readonly="readonly"/>
							<label>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;--&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</label>
							<input id="endDate" name="endDate" type="text" maxlength="20" class=" laydate-icon form-control layer-date input-sm"
								value="<fmt:formatDate value="${articles.endDate}" pattern="yyyy-MM-dd"/>" readonly="readonly"/>
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
								<shiro:hasPermission name="train:articleslist:categoryList">
									<button class="btn btn-white btn-sm" title="文章分类" onclick='top.openTab("${ctx}/train/articleslist/categoryList","文章分类", false)' data-placement="left" data-toggle="tooltip">
										<i class="fa fa-plus"></i>文章分类
									</button>
								</shiro:hasPermission>
	                        </div>
						</div>
					</div>
                </div>
                <div class="" id="reviewlists">
					<table id="contentTable" class="table table-striped table-bordered  table-hover table-condensed  dataTables-example dataTable no-footer">
						<thead>
							<tr>
								<th style="text-align: center;">ID</th>
								<th style="text-align: center;">文章标题</th>
							    <th style="text-align: center;">类别</th>
							    <th style="text-align: center;">作者</th>
							    <th style="text-align: center;">评论数</th>
							    <c:if test="${articles.flag == 0}">
								    <th style="text-align: center;">定时发布时间</th>
							    	<th style="text-align: center;">排序</th>
							    	<th style="text-align: center;">是否显示</th>
								    <th style="text-align: center;">是否推荐</th>
								    <th style="text-align: center;">是否置顶</th>
							    </c:if>
							    <c:if test="${articles.flag != 2}">
							    	<th style="text-align: center;">操作</th>
							    </c:if>
							</tr>
						</thead>
						<tbody style="text-align: center;">
							<c:forEach items="${page.list}" var="articlesList">
								<tr>
								  	<td>${articlesList.articleId }</td>
								  	<td>${articlesList.title }</td>
								  	<td>${articlesList.category.name }</td>
								  	<td>${articlesList.authorName }</td>
								  	<td>
								  		<a href="#" onclick='top.openTab("${ctx}/train/articleslist/findArticleComment?articleId=${articlesList.articleId}","文章评论", false)'>${articlesList.commentNum }</a>
								  	</td>
								  	<c:if test="${articles.flag == 0}">
									  	<td>
									  		<c:if test="${articlesList.isShow == 1 && articlesList.isTask == 1}">
									  			<div id="TASKDATE${articlesList.articleId}"><fmt:formatDate value="${articlesList.taskDate }" pattern="yyyy-MM-dd HH:mm:ss"/></div>
									  		</c:if>
									  	</td>
								  		<td>${articlesList.sort }</td>
								    	<td style="text-align: center;" id="ISSHOW${articlesList.articleId}">
								    		<c:if test="${articlesList.isShow == 1}">
												<img width="20" height="20" src="${ctxStatic}/ec/images/cancel.png" onclick="changeTableVal('ISSHOW','${articlesList.articleId}',0)">
											</c:if>
											<c:if test="${articlesList.isShow == 0}">
												<img width="20" height="20" src="${ctxStatic}/ec/images/open.png" onclick="changeTableVal('ISSHOW','${articlesList.articleId}',1)">
											</c:if>
								    	</td>
									    <td style="text-align: center;" id="ISRECOMMENT${articlesList.articleId}">
									    	<c:if test="${articlesList.isRecommend == 1}">
												<img width="20" height="20" src="${ctxStatic}/ec/images/cancel.png" onclick="changeTableVal('ISRECOMMENT','${articlesList.articleId}',0)">
											</c:if>
											<c:if test="${articlesList.isRecommend == 0}">
												<img width="20" height="20" src="${ctxStatic}/ec/images/open.png" onclick="changeTableVal('ISRECOMMENT','${articlesList.articleId}',1)">
											</c:if>
									    </td>
									    <td style="text-align: center;" id="ISTOP${articlesList.articleId}">
									    	<c:if test="${articlesList.isTop == 1}">
												<img width="20" height="20" src="${ctxStatic}/ec/images/cancel.png" onclick="changeTableVal('ISTOP','${articlesList.articleId}',0)">
											</c:if>
											<c:if test="${articlesList.isTop == 0}">
												<img width="20" height="20" src="${ctxStatic}/ec/images/open.png" onclick="changeTableVal('ISTOP','${articlesList.articleId}',1)">
											</c:if>
									    </td>
								    </c:if>
								    <c:if test="${articles.flag != 2}">
									    <td>
									    	<c:if test="${articles.flag == 1}">
									    		<shiro:hasPermission name="train:articleslist:auditArticles">
									    			<a href="${ctx}/train/articleslist/auditArticles?articleId=${articlesList.articleId}&flag=${articles.flag }&key=NO" class="btn btn-success btn-xs"><i class="fa fa-edit"></i> 审核通过</a>
									    		</shiro:hasPermission>
									    	</c:if>
									    	<c:if test="${articles.flag == 0}">
									    		<shiro:hasPermission name="train:articleslist:form">
									    			<a href="#" onclick="openDialog('编辑', '${ctx}/train/articleslist/form?articleId=${articlesList.articleId}&flag=${articles.flag }&key=NO','600px', '550px')" class="btn btn-success btn-xs"><i class="fa fa-edit"></i> 编辑</a>
									    		</shiro:hasPermission>
									    		<shiro:hasPermission name="train:articleslist:taskSend">
										    		<c:if test="${articlesList.isShow == 1}">
										    			<a id="oldTASK${articlesList.articleId}" href="#" onclick="openDialog('定时发布', '${ctx}/train/articleslist/form?articleId=${articlesList.articleId}&flag=${articles.flag }&key=YEStask','600px', '550px')" class="btn btn-success btn-xs"><i class="fa fa-edit"></i> 定时发布</a>
										    		</c:if>
										    		<a id="TASK${articlesList.articleId}" href="#" onclick="openDialog('定时发布', '${ctx}/train/articleslist/form?articleId=${articlesList.articleId}&flag=${articles.flag }&key=YEStask','600px', '550px')" class="task btn btn-success btn-xs"><i class="fa fa-edit"></i> 定时发布</a>
									    		</shiro:hasPermission>
									    	</c:if>
									    	<shiro:hasPermission name="train:articleslist:del">
									    		<a href="${ctx}/train/articleslist/deleteArticle?articleId=${articlesList.articleId}" onclick="return confirmx('确认要删除该文章吗？', this.href)" class="btn btn-danger btn-xs"><i class="fa fa-trash"></i> 删除</a>
								    		</shiro:hasPermission>
									    </td>
							    	</c:if>
								</tr>
							</c:forEach>
						</tbody>
					</table>
				</div>
                <table:page page="${page}"></table:page>
	        </div>
	    </div>
    </div>
    <div class="loading"></div>
</body>
</html>