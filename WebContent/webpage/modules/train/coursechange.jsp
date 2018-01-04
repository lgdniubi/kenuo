<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/webpage/include/taglib.jsp"%>
<%@ include file="/webpage/include/treetable.jsp" %>
<%@ include file="/webpage/include/icheck.jsp"%>
<html>
<head>
	<title>课程管理</title>
	<meta name="decorator" content="default"/>
	<link rel="stylesheet" href="${ctxStatic}/train/css/addcourse.css">
	<script type="text/javascript">
		//一级分类改变事件，联动二级分类
		function categorychange(v){
			$(".loading").show();//打开展示层
			$.ajax({
				type : "POST",   
				url : "${ctx}/train/categorys/listtow?categoryId="+v,
				dataType: 'json',
				success: function(data) {
					$(".loading").hide(); //关闭加载层
					$("#categoryId").html("");
					$("#categoryId").prepend("<option value='-1'>请选择分类</option>");
					var categoryIdval = $("#categoryIdval").val();
					$.each(data.listtow, function(index,item){
						if(item.categoryId == categoryIdval){
							$("#categoryId").prepend("<option value='"+item.categoryId+"' selected>"+item.name+"</option>");
						}else{
							$("#categoryId").prepend("<option value='"+item.categoryId+"'>"+item.name+"</option>");
						}
					});
				}
			});   
		}
		//页面加载
		$(document).ready(function() {
			
	    	var parentIdval = $("#parentIdval").val();
			if(null != parentIdval && '' != parentIdval){
				categorychange(parentIdval);
			}
			
			//课程总览统计
			$("#btnExport").click(function(){
				top.layer.confirm('确认要导出Excel吗?', {icon: 3, title:'系统提示'}, function(index){
				    //do something
				    	//导出之前备份
				    	var url =  $("#searchForm").attr("action");
				    	var pageNo =  $("#pageNo").val();
				    	var pageSize = $("#pageSize").val();
				    	//导出excel
				        $("#searchForm").attr("action","${ctx}/train/course/totalexport");
					    $("#pageNo").val(-1);
						$("#pageSize").val(-1);
						$("#searchForm").submit();

						//导出excel之后还原
						$("#searchForm").attr("action",url);
					    $("#pageNo").val(pageNo);
						$("#pageSize").val(pageSize);
				    top.layer.close(index);
				});
			});
			//收藏统计
			$("#btnCollExport").click(function(){
				top.layer.confirm('确认要导出Excel吗?', {icon: 3, title:'系统提示'}, function(index){
				    //do something
				    	//导出之前备份
				    	var url =  $("#searchForm").attr("action");
				    	var pageNo =  $("#pageNo").val();
				    	var pageSize = $("#pageSize").val();
				    	//导出excel
				        $("#searchForm").attr("action","${ctx}/train/course/collectionexport");
					    $("#pageNo").val(-1);
						$("#pageSize").val(-1);
						$("#searchForm").submit();

						//导出excel之后还原
						$("#searchForm").attr("action",url);
					    $("#pageNo").val(pageNo);
						$("#pageSize").val(pageSize);
				    top.layer.close(index);
				});
			});
			//评论统计
			$("#btnComExport").click(function(){
				top.layer.confirm('确认要导出Excel吗?', {icon: 3, title:'系统提示'}, function(index){
				    //do something
				    	//导出之前备份
				    	var url =  $("#searchForm").attr("action");
				    	var pageNo =  $("#pageNo").val();
				    	var pageSize = $("#pageSize").val();
				    	//导出excel
				        $("#searchForm").attr("action","${ctx}/train/course/commentexport");
					    $("#pageNo").val(-1);
						$("#pageSize").val(-1);
						$("#searchForm").submit();

						//导出excel之后还原
						$("#searchForm").attr("action",url);
					    $("#pageNo").val(pageNo);
						$("#pageSize").val(pageSize);
				    top.layer.close(index);
				});
			});
			//单元测试
			$("#btnUnitExport").click(function(){
				top.layer.confirm('确认要导出Excel吗?', {icon: 3, title:'系统提示'}, function(index){
				    //do something
				    	//导出之前备份
				    	var url =  $("#searchForm").attr("action");
				    	var pageNo =  $("#pageNo").val();
				    	var pageSize = $("#pageSize").val();
				    	//导出excel
				        $("#searchForm").attr("action","${ctx}/train/course/unitexport");
					    $("#pageNo").val(-1);
						$("#pageSize").val(-1);
						$("#searchForm").submit();

						//导出excel之后还原
						$("#searchForm").attr("action",url);
					    $("#pageNo").val(pageNo);
						$("#pageSize").val(pageSize);
				    top.layer.close(index);
				});
			});
			//单元测试统计
			$("#btnUnitTotalExport").click(function(){
				top.layer.confirm('确认要导出Excel吗?', {icon: 3, title:'系统提示'}, function(index){
				    //do something
				    	//导出之前备份
				    	var url =  $("#searchForm").attr("action");
				    	var pageNo =  $("#pageNo").val();
				    	var pageSize = $("#pageSize").val();
				    	//导出excel
				        $("#searchForm").attr("action","${ctx}/train/course/totalunitexport");
					    $("#pageNo").val(-1);
						$("#pageSize").val(-1);
						$("#searchForm").submit();

						//导出excel之后还原
						$("#searchForm").attr("action",url);
					    $("#pageNo").val(pageNo);
						$("#pageSize").val(pageSize);
				    top.layer.close(index);
				});
			});
			
			
	    });
		
		
		//分页按钮
		function page(n,s){
			$("#pageNo").val(n);
			$("#pageSize").val(s);
			$("#searchForm").submit();
	    	return false;
	    }
		
		//重置表单
		function resetnew(){
			$("#name").val("");
			$("#parentId").val("-1");
			categorychange($("#parentId").val());
			reset();
		}
		//刷新
		function refresh(){
			window.location="${ctx}/train/course/listcourse";
		}
		
		$(function(){
			$("img").each(function(){
				var $this = $(this),
					$src = $this.attr('data-src');  
				$this.attr({'src':$src})
			});
			
		});
		//是否推荐/是否显示 改变事件
		function changeVal(flag,id,isShow){
			$(".loading").show();//打开展示层
			$.ajax({
				type : "POST",
				url : "${ctx}/train/course/updateIsShow?lessonId="+id+"&isShow="+isShow,
				dataType: 'json',
				success: function(data) {
					$(".loading").hide(); //关闭加载层
					var STATUS = data.STATUS;
					var ISSHOW = data.ISSHOW;
					if("OK" == STATUS){
		            	$("#"+flag+id).html("");//清除DIV内容	
						if(ISSHOW == '1'){
							$("#"+flag+id).append("<img width='20' height='20' src='${ctxStatic}/ec/images/cancel.png' onclick=\"changeVal('"+flag+"','"+id+"','0')\">");
						}else if(ISSHOW == '0'){
							$("#"+flag+id).append("<img width='20' height='20' src='${ctxStatic}/ec/images/open.png' onclick=\"changeVal('"+flag+"','"+id+"','1')\">");
						} 
					}else if("ERROR" == STATUS){
						alert(data.MESSAGE);
					}
				}
			});   
		}
	</script>
</head>
<body>
	<div class="ibox-content">
		<sys:message content="${message}"/>
		<div class="warpper-content">
			<div class="ibox">
				<div class="ibox-title">
					<h5>课程管理</h5>
				</div>
				<div class="ibox-content">
					<div class="searcharea clearfix">
						<form:form id="searchForm" action="${ctx}/train/course/listcourse" method="post" class="navbar-form navbar-left searcharea">
							<div class="form-group">
								<label>关键字：<input id="name" name="name" maxlength="10" type="text" class="form-control" value="${trainLessons.name}"></label> 
								<input type="hidden" id="parentIdval" name="parentIdval" value="${trainLessons.parentId}"/>
								<select class="form-control" id="parentId" name="parentId" onchange="categorychange(this.options[this.options.selectedIndex].value)">
									<option value="-1">请选择分类</option>
									<c:forEach items="${listone}" var="trainCategorys">
										<option ${trainCategorys.categoryId == trainLessons.parentId?'selected="selected"':'' } value="${trainCategorys.categoryId}">${trainCategorys.name}</option>
									</c:forEach>
								</select>
								<input type="hidden" id="categoryIdval" name="categoryIdval" value="${trainLessons.categoryId}"/>
								<select class="form-control" id="categoryId" name="categoryId">
									<option value='-1'>请选择分类</option>
								</select> 
								时间范围：
								<input id="beginDate" name="beginDate" type="text" class="datetimepicker form-control" value="<fmt:formatDate value="${trainLessons.beginDate}" pattern="yyyy-MM-dd HH:mm"/>" /> -- 
								<input id="endDate" name="endDate" type="text" class="datetimepicker form-control" value="<fmt:formatDate value="${trainLessons.endDate}" pattern="yyyy-MM-dd HH:mm"/>" />
							</div>
							<shiro:hasPermission name="train:course:listcourse">
								<button type="button" class="btn btn-primary btn-rounded btn-outline btn-sm" onclick="search()">
									<i class="fa fa-search"></i> 搜索
								</button>
								<button type="button" class="btn btn-primary btn-rounded btn-outline btn-sm" onclick="resetnew()" >
									<i class="fa fa-refresh"></i> 重置
								</button>
							</shiro:hasPermission>
							<!-- 分页必要字段 -->
							<input id="pageNo" name="pageNo" type="hidden" value="${page.pageNo}"/>
							<input id="pageSize" name="pageSize" type="hidden" value="${page.pageSize}"/>
						</form:form>
					</div>
					<!-- 工具栏 -->
					<div class="row" style="padding-bottom: 5px;">
						<div class="col-sm-12">
							<div class="pull-left">
								<shiro:hasPermission name="train:course:addcourse">
									<a href="${ctx}/train/course/addcourse">
										<button class="btn btn-white btn-sm " data-toggle="tooltip" data-placement="left" ><i class="fa fa-plus"></i> 添加课程</button>
									</a>
								</shiro:hasPermission>
								<shiro:hasPermission name="train:course:batchdeletecourse">
									<table:delRow url="${ctx}/train/course/batchdeletecourse" id="treeTable"></table:delRow><!-- 批量删除按钮 -->
								</shiro:hasPermission>
								<!-- 导出按钮 -->
								<shiro:hasPermission name="train:course:importPage">
									<button id="btnExport" class="btn btn-white btn-sm " data-toggle="tooltip" data-placement="left" title="导出">
										<i class="fa fa-file-excel-o"></i>课程总揽</button>
									<button id="btnCollExport" class="btn btn-white btn-sm " data-toggle="tooltip" data-placement="left" title="导出">
										<i class="fa fa-file-excel-o"></i>收藏统计</button>
									<button id="btnComExport" class="btn btn-white btn-sm " data-toggle="tooltip" data-placement="left" title="导出">
										<i class="fa fa-file-excel-o"></i>评论统计</button>
									<button id="btnUnitExport" class="btn btn-white btn-sm " data-toggle="tooltip" data-placement="left" title="导出">
										<i class="fa fa-file-excel-o"></i>单元测试</button>
									<button id="btnUnitTotalExport" class="btn btn-white btn-sm " data-toggle="tooltip" data-placement="left" title="导出">
										<i class="fa fa-file-excel-o"></i>单元测试统计</button>
								</shiro:hasPermission>
								<button class="btn btn-white btn-sm " data-toggle="tooltip" data-placement="left" onclick="refresh()" title="刷新">
									<i class="glyphicon glyphicon-repeat"></i> 刷新</button>
							</div>
						</div>
					</div>
					<table id="treeTable" class="table table-bordered table-hover table-striped">
						<thead>
							<tr>
								<th width="120" style="text-align: center;"><input type="checkbox" name="" id="i-checks" class="i-checks"></th>
								<th style="text-align: center;">名称</th>
								<th width="120" style="text-align: center;">课程类型</th>
								<th width="120" style="text-align: center;">分类</th>
								<th width="200" style="text-align: center;">图片</th>
								<th style="text-align: center;">是否显示</th>
								<th style="text-align: center;">排序</th>
								<th width="200" style="text-align: center">课程内容</th>
								<th width="120" style="text-align: center;">课程操作</th>
							</tr>
						</thead>
						<tbody>
							<c:forEach items="${page.list}" var="trainLessons">
								<tr>
									<td><input class="i-checks" type="checkbox" id="${trainLessons.lessonId}" name="ids"></td>
									<td><c:out value="${trainLessons.name}"></c:out></td>
									<td>
										<c:if test="${trainLessons.lessontype == 1}">
											线上课程
										</c:if>
										<c:if test="${trainLessons.lessontype == 2}">
											线上测试
										</c:if>
										<c:if test="${trainLessons.lessontype == 3}">
											线下预约
										</c:if>
									</td>
									<td>${trainLessons.trainCategorys.name}</td>
									<td><img alt="image" class="img-responsive" style="width: 220px;height: 100px;" src="${ctxStatic}/images/lazylode.png"  data-src="${trainLessons.coverPic }"/></td>
									<td style="text-align: center;" id="isShow${trainLessons.lessonId}">
										<c:if test="${trainLessons.isShow == 1}">
											<img width="20" height="20" src="${ctxStatic}/ec/images/cancel.png" onclick="changeVal('isShow','${trainLessons.lessonId}','0')">
										</c:if>
										<c:if test="${trainLessons.isShow == 0}">
											<img width="20" height="20" src="${ctxStatic}/ec/images/open.png" onclick="changeVal('isShow','${trainLessons.lessonId}','1')">
										</c:if>
									</td>
									<td>${trainLessons.sort}</td>
									<td>
										<c:if test="${trainLessons.lessontype == 1}">
											<shiro:hasPermission name="train:course:contentmanage">
												<a href="#" onclick='top.openTab("${ctx}/train/course/contentmanage?lessonId=${trainLessons.lessonId}","内容管理", false)'>内容管理</a>
											</shiro:hasPermission>
											<shiro:hasPermission name="train:exambank:index">
												<br/>
												<br/>
												<a href="#" onclick='top.openTab("${ctx}/train/exambank/exambank?lessonId=${trainLessons.lessonId}&lessontype=${trainLessons.lessontype}","添加课后习题", false)'>添加课后习题</a>
											</shiro:hasPermission>
											<shiro:hasPermission name="train:exambank:index">
												<br/>
												<br/>
												<a href="#" onclick='top.openTab("${ctx}/train/comments/listcomments?lessonId=${trainLessons.lessonId}","课程评论管理", false)'>课程评论管理</a>
											</shiro:hasPermission>
										</c:if>
										<c:if test="${trainLessons.lessontype == 2}">
											<shiro:hasPermission name="train:exambank:index">
												<a href="#" onclick='top.openTab("${ctx}/train/exambank/exambank?ziCategoryId=${trainLessons.trainCategorys.categoryId}&lessontype=${trainLessons.lessontype}","添加单元测试", false)'>添加单元测试</a>
											</shiro:hasPermission>
										</c:if>
										<c:if test="${trainLessons.lessontype == 3}">
											<shiro:hasPermission name="train:studentsubscribe:findsetlist">
												<a href="#" onclick='top.openTab("${ctx}/train/studentsubscribe/findsetlist?categoryId=${trainLessons.trainCategorys.categoryId}","课程线下预约", false)'>课程线下预约</a>
											</shiro:hasPermission>
										</c:if>
									</td>
									<td>
										<shiro:hasPermission name="train:course:getcoursebyid">
											<a class="btn btn-primary btn-circle btn-xg"  title="修改课程"
												onclick="openDialog('课程基本信息 ', '${ctx}/train/course/getcoursebyid?lessonId=${trainLessons.lessonId}','450px', '600px')">
												<i class="glyphicon glyphicon-edit"></i>
											</a> 
										</shiro:hasPermission>
										<shiro:hasPermission name="train:course:deletecourse">
											<a href="${ctx}/train/course/deletecourse?lessonId=${trainLessons.lessonId}" class="btn btn-danger btn-circle" 
												onclick="return confirmx('要删除该课程吗？', this.href)" title="删除课程">
												<i class="glyphicon glyphicon-trash"></i>
											</a>
										</shiro:hasPermission>
									</td>
								</tr>
							</c:forEach>
						</tbody>
					</table>
					<!-- 分页代码 -->
					<table:page page="${page}"></table:page>
					<br/>
					<br/>
				</div>
			</div>
		</div>
	</div>
	<div class="loading"></div>
	<script type="text/javascript" src="${ctxStatic}/train/js/jquery.datetimepicker.js"></script>
	<script>
	    // 选取时间
	    $('.datetimepicker').datetimepicker();
	</script>
</body>
</html>