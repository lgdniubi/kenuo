<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/webpage/include/taglib.jsp"%>
<html>
<head>
	<title>视频文档信息表</title>
	<meta name="decorator" content="default"/>
	<link rel="stylesheet" href="${ctxStatic}/train/css/addcourse.css">
	<link rel="stylesheet" href="${ctxStatic}/ec/css/loading.css">
	<%@include file="/webpage/include/treetable.jsp" %>
	<script type="text/javascript">
		//重置表单
		function resetnew(){
			$("#name").val("");
			reset();
		}
		
		//刷新
		function refresh(){
			window.location="${ctx}/forms/document/documentbyid";
		}
		
		//一级分类改变事件，联动二级分类
		function opentwo(){
			$("#twoClassify").empty();
			var categoryId=$("#oneClassify").val();
			var category=$("#categoryone").val();
			$.ajax({
				type : "POST",   
				url : "${ctx}/forms/document/twoclass",
				data:{categoryId:categoryId},
				dataType: 'json',
				async: false,
				success: function(data) {
					$("#twoClassify").prepend("<option value='' selected='selected'>请选择二级分类</option>");
					$.each(data, function(index,item){
						if(category == item.categoryId){
							$("#twoClassify").prepend("<option value='"+item.categoryId+"' selected>"+item.twoClassify+"</option>");
						}else{
							$("#twoClassify").prepend("<option value="+item.categoryId+">"+item.twoClassify+"</option>");
						}
					});
				}
			});  
		}
		
		//就绪函数
		$(document).ready(function() {
			var category=$("#categoryone").val();
			if(null != category && '' != category){
				opentwo();
			}
		})	
	</script>
</head>
<body>
	<div class="ibox-content">
		<sys:message content="${message}"/>
		<div class="warpper-content">
			<div class="ibox">
				<div class="ibox-title">
					<h5>视频文档信息表</h5>
				</div>
				<div class="ibox-content">
					<div class="searcharea clearfix">
						<form:form id="searchForm" modelAttribute="goodsReport" action="${ctx}/forms/document/documentbyid" method="post" class="form-inline">
							<input id="pageNo" name="pageNo" type="hidden" value="${page.pageNo}"/>
							<input id="pageSize" name="pageSize" type="hidden" value="${page.pageSize}"/>
							<table:sortColumn id="orderBy" name="orderBy" value="${page.orderBy}" callback="sortOrRefresh();"/><!-- 支持排序 -->
							<div class="form-group">
								<label>视频文档名称：<input id="contentName" name="contentName" maxlength="100" type="text" class="form-control" value="${lessionInfoReport.contentName}" ></label> 
								<label>课程ID：<input id="lessionId" name="lessionId" maxlength="100" type="text" class="form-control" value="${lessionInfoReport.lessionId}" ></label>
								<label>课程名称：<input id="lessionName" name="lessionName" maxlength="100" type="text" class="form-control" value="${lessionInfoReport.lessionName}" ></label>
								<select class="form-control" id="oneClassify" name="oneClassify" onchange="opentwo()">
									<option value="" selected='selected'>请选择一级分类</option>
									<c:forEach items="${lession}" var="lession">
										<option value="${lession.categoryId}" <c:if test="${one == lession.categoryId}">selected</c:if>>${lession.oneClassify}</option>
									</c:forEach>
								</select>
								<input type="hidden" id="categoryone" name="categoryone" value="${one}"/>
								<input type="hidden" id="category" name="category" value="${two}"/>
								<select class="form-control" id="twoClassify" name="twoClassify">
									<option value="" selected='selected'>请选择二级分类</option>
								</select>
							</div>
							<shiro:hasPermission name="train:categorys:findalllist">
								<button type="button" class="btn btn-primary btn-rounded btn-outline btn-sm" onclick="search()">
									<i class="fa fa-search"></i> 搜索
								</button>
								<button type="button" class="btn btn-primary btn-rounded btn-outline btn-sm" onclick="resetnew()" >
									<i class="fa fa-refresh"></i> 重置
								</button>
							</shiro:hasPermission>
						</form:form>
					</div>
					<!-- 工具栏 -->
					<div class="row" style="padding-top:10px;">
						<div class="col-sm-12">
							<div class="pull-left">
								<shiro:hasPermission name="sys:user:export">
								<!-- 导出按钮 -->
									<table:exportExcel url="${ctx}/forms/document/exportinfo"></table:exportExcel>
								</shiro:hasPermission>
								<button class="btn btn-white btn-sm " data-toggle="tooltip" data-placement="left" onclick="refresh()" title="刷新">
									<i class="glyphicon glyphicon-repeat"></i> 刷新
								</button>
							</div>
						</div>
					</div>
					<table id="treeTable" class="table table-striped table-bordered table-hover table-condensed dataTables-example dataTable">
						<thead>
							<tr>
								<th style="text-align: center;">视频文档ID</th>
								<th style="text-align: center;">视频文档名称</th>
								<th style="text-align: center;">下载次数</th>
								<th style="text-align: center;">课程ID</th>
								<th style="text-align: center;">课程名称</th>
								<th style="text-align: center;">一级分类</th>
								<th style="text-align: center;">二级分类</th>
								<th style="text-align: center;">课程收藏总量</th>
								<th style="text-align: center;">属性</th>
							</tr>
						</thead>
						<tbody>
							<c:forEach items="${page.list}" var="lession" varStatus="status">
								<tr>						
									<td>${lession.contentId}</td>
									<td>${lession.contentName}</td>
									<td>${lession.downNum}</td>
									<td>${lession.lessionId}</td>
									<td>${lession.lessionName}</td>
									<td>${lession.oneClassify}</td>
									<td>${lession.twoClassify}</td>
									<td>${lession.collect}</td>
									<td>${lession.status}</td>			
								</tr>
							</c:forEach>
						</tbody>
						<tfoot>
	 						<tr>
	                            <td colspan="20">
	                                <!-- 分页代码 --> 
	                                <div class="tfoot">
	                                	 <table:page page="${page}"></table:page>
	                                </div>
	                            </td>	
	                        </tr>
	                   	 </tfoot>
					</table>
				</div>
			</div>
		</div>
	</div>
</body>
</html>