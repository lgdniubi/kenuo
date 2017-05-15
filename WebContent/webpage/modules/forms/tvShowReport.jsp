<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/webpage/include/taglib.jsp"%>
<html>
<head>
	<title>直播/回放日报表</title>
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
			window.location="${ctx}/forms/show/show";
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
				         end.min = datas; 		//开始日选好后，重置结束日的最小日期
				         end.start = datas 		//将结束日的初始值设定为开始日
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
				<h5>直播/回放日报表</h5>
			</div>
			 <sys:message content="${message}"/>
			 	<!-- 查询条件 -->
				<div class="ibox-content">
				<div class="clearfix">
				<form:form id="searchForm" modelAttribute="goodsReport" action="${ctx}/forms/show/show" method="post" class="form-inline">
					<input id="pageNo" name="pageNo" type="hidden" value="${page.pageNo}"/>
					<input id="pageSize" name="pageSize" type="hidden" value="${page.pageSize}"/>
					<table:sortColumn id="orderBy" name="orderBy" value="${page.orderBy}" callback="sortOrRefresh();"/><!-- 支持排序 -->
					<div class="form-group">
						<label>查询日期：</label>
						<input id="begtime" name="begtime" type="text" maxlength="20" class="laydate-icon form-control layer-date input-sm"
							value="<fmt:formatDate value="${tvShowTimeReport.begtime}" pattern="yyyy-MM-dd"/>" style="width:185px;" placeholder="查询时间" readonly="readonly"/>
					 			一
						<input id="endtime" name="endtime" type="text" maxlength="20" class=" laydate-icon form-control layer-date input-sm" 
							value="<fmt:formatDate value="${tvShowTimeReport.endtime}" pattern="yyyy-MM-dd"/>"  style="width:185px;" placeholder="结束时间" readonly="readonly"/>&nbsp;&nbsp;
					 	<!-- 查询，重置按钮 -->
					 	<button  class="btn btn-primary btn-rounded btn-outline btn-sm " onclick="search()" ><i class="fa fa-search"></i> 查询</button>
						<button  class="btn btn-primary btn-rounded btn-outline btn-sm " onclick="reset()" ><i class="fa fa-refresh"></i> 重置</button>
					</div>
				</form:form>
					<!-- 工具栏 -->
					<div class="row" style="padding-top:10px;">
					<div class="col-sm-12">
						<div class="pull-left">
							<shiro:hasPermission name="sys:user:export">
								<!-- 导出按钮 -->
								<table:exportExcel url="${ctx}/forms/show/exportdate"></table:exportExcel>
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
							<th style="text-align: center;">直播ID</th>
							<th style="text-align: center;">主播姓名</th>
							<th style="text-align: center;">直播标题</th>
							<th style="text-align: center;">直播播放次数</th>
							<th style="text-align: center;">直播观看人数</th>
							<th style="text-align: center;">收藏量</th>
							<th style="text-align: center;">点赞量</th>
							<th style="text-align: center;">是否付费</th>
							<th style="text-align: center;">商家</th>
							<th style="text-align: center;">区域</th>
							<th style="text-align: center;">集团军</th>
							<th style="text-align: center;">市场</th>
							<th style="text-align: center;">门店</th>
							<th style="text-align: center;">职位</th>
							<th style="text-align: center;">角色</th>
							<th style="text-align: center;">直播一级分类</th>
							<th style="text-align: center;">直播二级分类</th>
							<th style="text-align: center;">直播三级分类</th>
							<th style="text-align: center;">开始直播时间</th>
							<th style="text-align: center;">结束直播时间</th>
						</tr>
					</thead>
					<tbody style="text-align: center;">
						<c:forEach items="${page.list}" var="tv" varStatus="status">
							<tr>
								<td>${tv.showLiveId}</td>
								<td>${tv.showManName}</td>
								<td>${tv.showLiveTitle}</td>
								<td>${tv.showLiveNum}</td>
								<td>${tv.showLiveLookNum}</td>
								<td>${tv.collectNum}</td>
								<td>${tv.praiseNum}</td>
								<td>${tv.isPay}</td>
								<td>${tv.stairOne}</td>
								<td>${tv.stairTwo}</td>
								<td>${tv.stairThree}</td>
								<td>${tv.stairFour}</td>
								<td>${tv.stairFive}</td>
								<td>${tv.job}</td>
								<td>${tv.role}</td>
								<td>${tv.showLiveOne}</td>
								<td>${tv.showLiveTwo}</td>
								<td>${tv.showLiveThree}</td>
								<td>${tv.showLivebegtime}</td>
								<td>${tv.showLiveendtime}</td>
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