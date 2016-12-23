<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/webpage/include/taglib.jsp"%>
<%@ include file="/webpage/include/treetable.jsp" %>
<html>
<head>
	<title>课程线下预约管理</title>
	<meta name="decorator" content="default" />
	<link rel="stylesheet" href="${ctxStatic}/train/css/addcourse.css">
	<script type="text/javascript" src="${ctxStatic}/train/js/jquery-1.11.3.js"></script>
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
					$("#categoryId").empty();
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
			if(null == parentIdval || '' == parentIdval){
				categorychange(-1);
			}else{
				categorychange(parentIdval);
			}
			
			//一级分类默认
	    	var parentIdval = $("#parentIdval").val();
			$("#parentId option").each(function(){
				$this = $(this).val();
				if($this == parentIdval){
					$(this).attr("selected",true);
				}
			});
	    });
		//分页按钮
		function page(n,s){
			$("#pageNo").val(n);
			$("#pageSize").val(s);
			$("#searchForm").submit();
	    	return false;
	    }
		//刷新
		function refresh(){
			window.location="${ctx}/train/subscribe/listsubscribe";
		}
		//重置表单
		function resetnew(){
			$("#parentId").val("-1");
			categorychange($("#parentId").val());
			reset();
		}
	</script>
</head>
<body>
	<div class="ibox-content">
		<sys:message content="${message}"/>
		<div class="warpper-content">	
			<div class="ibox">
				<div class="ibox-title">
					<h5>课程线下预约列表</h5>
				</div>
				<div class="ibox-content" style="width: 85%;">
					<div class="searcharea clearfix">
						<form:form id="searchForm" action="${ctx}/train/subscribe/listsubscribe" method="post" class="navbar-form navbar-left searcharea">
							<div class="form-group">
								<input type="hidden" id="parentIdval" name="parentIdval" value="${trainOfflineSubscribeTime.parentId}"/>
								选择分类：
								<select class="form-control" id="parentId" name="parentId" onchange="categorychange(this.options[this.options.selectedIndex].value)">
									<option value="-1">请选择分类</option>
									<c:forEach items="${listone}" var="trainCategorys">
										<option value="${trainCategorys.categoryId}">${trainCategorys.name}</option>
									</c:forEach>
								</select>
								<input type="hidden" id="categoryIdval" name="categoryIdval" value="${trainOfflineSubscribeTime.categoryId}"/>
								<select class="form-control" id="categoryId" name="categoryId"></select> 
								时间范围：
								<input id="beginDate" name="beginDate" type="text" class="datetimepicker form-control" value="<fmt:formatDate value="${trainOfflineSubscribeTime.beginDate}" pattern="yyyy-MM-dd HH:mm"/>" /> -- 
								<input id="endDate" name="endDate" type="text" class="datetimepicker form-control" value="<fmt:formatDate value="${trainOfflineSubscribeTime.endDate}" pattern="yyyy-MM-dd HH:mm"/>" />
							</div>
							<shiro:hasPermission name="train:subscribe:listsubscribe">
								<button type="button" class="btn btn-primary btn-rounded btn-outline btn-sm" onclick="search()">
									<i class="fa fa-search"></i>  搜索
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
								<shiro:hasPermission name="train:subscribe:savesubscribe">
									<table:addRow url="${ctx}/train/subscribe/addsubscribe" width="500px" height="550px" title="课程线下预约"></table:addRow><!-- 增加按钮 -->
								</shiro:hasPermission>
								<shiro:hasPermission name="train:subscribe:batchdeletesubscribe">
									<table:delRow url="${ctx}/train/subscribe/batchdeletesubscribe" id="treeTable"></table:delRow><!-- 删除按钮 -->
								</shiro:hasPermission>
								<button class="btn btn-white btn-sm " data-toggle="tooltip" data-placement="left" onclick="refresh()" title="刷新"><i class="glyphicon glyphicon-repeat"></i> 刷新</button>
							</div>
						</div>
					</div>
					<table id="treeTable" class="table table-bordered table-hover table-striped">
						<thead>
							<tr>
								<th width="120" style="text-align: center;"><input type="checkbox" name="" id="checkall"></th>
								<th width="200" style="text-align: center;">课程分类</th>
								<th width="200" style="text-align: center;">用户名称</th>
								<th width="200" style="text-align: center;">预约时间</th>
								<th width="200" style="text-align: center;">操作</th>
							</tr>
						</thead>
						<tbody>
							<c:forEach items="${page.list}" var="trainOfflineSubscribeTime">
								<tr>
									<td><input type="checkbox" class="i-checks" id="${trainOfflineSubscribeTime.subscribeId }"></td>
									<td>${trainOfflineSubscribeTime.trainCategorys.name }</td>
									<td>${trainOfflineSubscribeTime.user.name }</td>
									<td><fmt:formatDate value="${trainOfflineSubscribeTime.subscribeTime }" pattern="yyyy-MM-dd HH:mm"/></td>
									<td>
										<shiro:hasPermission name="train:subscribe:deletesubscribe">
											<a href="${ctx}/train/subscribe/deletesubscribe?subscribeId=${trainOfflineSubscribeTime.subscribeId }" class="btn btn-danger btn-circle" 
												onclick="return confirmx('要删除该线下预约吗？', this.href)">
												<i class=" glyphicon glyphicon-trash"></i>
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
	<script type="text/javascript">
	    $(function(){
	        // 全选
	        $('#checkall').click(function(){
	            if ($(this).is(':checked')) {
	                $(":checkbox").prop('checked',true); 
	            } else {
	                $(":checkbox").prop('checked',false);
	            }
	        });
		    // 选取时间
		    $('.datetimepicker').datetimepicker();
	    });
	</script>
</body>
</html>