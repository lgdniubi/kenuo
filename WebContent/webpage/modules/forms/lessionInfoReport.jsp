<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/webpage/include/taglib.jsp"%>
<html>
<head>
	<title>视频文档日报表</title>
	<meta name="decorator" content="default"/>
	<script type="text/javascript">
		function page(n,s){
			$("#pageNo").val(n);
			$("#pageSize").val(s);
			$("#searchForm").submit();
	    	return false;
	    }
		
		//刷新
		function refresh(){
			window.location="${ctx}/forms/document/document";
		}
		
		$(document).ready(function() {
			var start = {
				    elem: '#begtime',
				    format: 'YYYY-MM-DD',
				    event: 'focus',
				    max: $("#endtime").val(),   //最大日期
				    istime: false,				//是否显示时间
				    isclear: false,				//是否显示清除
				    istoday: false,				//是否显示今天
				    issure: true,				//是否显示确定
				    festival: true,				//是否显示节日
				    choose: function(datas){
				    end.min = datas; 			//开始日选好后，重置结束日的最小日期
				    end.start = datas 			//将结束日的初始值设定为开始日
				    }
				};
			var end = {
					    elem: '#endtime',
					    format: 'YYYY-MM-DD',
					    event: 'focus',
					    min: $("#begtime").val(),
					    istime: false,
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
	   	});
	</script>
</head>

<body class="gray-bg">
	<div class="wrapper wrapper-content">
		<div class="ibox">
			<div class="ibox-title">
				<h5>视频文档日报表</h5>
			</div>
			 <sys:message content="${message}"/>
			 	<!-- 查询条件 -->
				<div class="ibox-content">
				<div class="clearfix">
				<form:form id="searchForm" modelAttribute="goodsReport" action="${ctx}/forms/document/document" method="post" class="form-inline">
					<input id="pageNo" name="pageNo" type="hidden" value="${page.pageNo}"/>
					<input id="pageSize" name="pageSize" type="hidden" value="${page.pageSize}"/>
					<table:sortColumn id="orderBy" name="orderBy" value="${page.orderBy}" callback="sortOrRefresh();"/><!-- 支持排序 -->
					<div class="form-group">
						<label>查询日期：</label>
						<input id="begtime" name="begtime" type="text" maxlength="20" class="laydate-icon form-control layer-date input-sm"
							value="<fmt:formatDate value="${lessionTimeReport.begtime}" pattern="yyyy-MM-dd"/>" style="width:185px;" placeholder="查询时间" readonly="readonly"/>
							一
						<input id="endtime" name="endtime" type="text" maxlength="20" class=" laydate-icon form-control layer-date input-sm" 
						value="<fmt:formatDate value="${lessionTimeReport.endtime}" pattern="yyyy-MM-dd"/>"  style="width:185px;" placeholder="结束时间" readonly="readonly"/>&nbsp;&nbsp;
					</div>
					<!-- 查询按钮 -->	
					<button  class="btn btn-primary btn-rounded btn-outline btn-sm " onclick="search()" ><i class="fa fa-search"></i> 查询</button>
					<button  class="btn btn-primary btn-rounded btn-outline btn-sm " onclick="reset()" ><i class="fa fa-refresh"></i> 重置</button>
				</form:form>
					<!-- 工具栏 -->
					<div class="row" style="padding-top:10px;">
					<div class="col-sm-12">
						<div class="pull-left">
							<shiro:hasPermission name="sys:user:export">
								<!-- 导出按钮 -->
								<table:exportExcel url="${ctx}/forms/document/exportdate"></table:exportExcel>
							</shiro:hasPermission>
							<button class="btn btn-white btn-sm " data-toggle="tooltip" data-placement="left" onclick="refresh()" title="刷新">
								<i class="glyphicon glyphicon-repeat"></i> 刷新
							</button>
						</div>
					</div>
				</div>
				</div>
				<table id="contentTable" class="table table-striped table-bordered  table-hover table-condensed  dataTables-example dataTable no-footer">
					<thead>
						<tr>
							<th style="text-align: center;">时间</th>
							<th style="text-align: center;">视频文档ID</th>
							<th style="text-align: center;">视频文档名称</th>
							<th style="text-align: center;">下载次数</th>
							<th style="text-align: center;">课程ID</th>
							<th style="text-align: center;">课程名称</th>
							<th style="text-align: center;">一级分类</th>
							<th style="text-align: center;">二级分类</th>
							<th style="text-align: center;">课程收藏量(当天)</th>
							<th style="text-align: center;">属性</th>
						</tr>
					</thead>
					<tbody style="text-align: center;">
						<c:forEach items="${page.list}" var="lession" varStatus="status">
							<tr>
								<td>${lession.time}</td>						
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
</body>
</html>