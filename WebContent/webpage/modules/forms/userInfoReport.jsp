<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/webpage/include/taglib.jsp"%>
<%@ include file="/webpage/include/treetable.jsp" %>
<%@ include file="/webpage/include/icheck.jsp"%>
<html>
<head>
	<title>妃子校用户信息表</title>
	<meta name="decorator" content="default"/>
	<link rel="stylesheet" href="${ctxStatic}/train/css/addcourse.css">
	<script type="text/javascript">
		function page(n,s){
			$("#pageNo").val(n);
			$("#pageSize").val(s);
			$("#searchForm").submit();
	    	return false;
	    }
		
		//刷新
		function refresh(){
			window.location="${ctx}/forms/collect/info";
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
				    start.max = datas; 			//结束日选好后，重置开始日的最大日期
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
				<h5>妃子校用户信息表</h5>
			</div>
			 <sys:message content="${message}"/>
			 	<!-- 查询条件 -->
				<div class="ibox-content">
				<div class="clearfix">
				<form:form id="searchForm" modelAttribute="goodsReport" action="${ctx}/forms/collect/info" method="post" class="form-inline">
					<input id="pageNo" name="pageNo" type="hidden" value="${page.pageNo}"/>
					<input id="pageSize" name="pageSize" type="hidden" value="${page.pageSize}"/>
					<table:sortColumn id="orderBy" name="orderBy" value="${page.orderBy}" callback="sortOrRefresh();"/><!-- 支持排序 -->
					<div class="form-group">
						<label>用户ID：<input id="userId" name="userId" maxlength="100" type="text" class="form-control" value="${userInfoReport.userId}" ></label> 
						<label>用户昵称：<input id="userNmae" name="userNmae" maxlength="100" type="text" class="form-control" value="${userInfoReport.userNmae}" ></label>
						<label>注册手机号：<input id="mobile" name="mobile" maxlength="100" type="text" class="form-control" value="${userInfoReport.mobile}" ></label>
						<label>创建时间：</label>
						<input id="begtime" name="begtime" type="text" maxlength="20" class="laydate-icon form-control layer-date input-sm"
							value="<fmt:formatDate value="${userInfoReport.begtime}" pattern="yyyy-MM-dd"/>" style="width:185px;" placeholder="查询时间" readonly="readonly"/>
							一
						<input id="endtime" name="endtime" type="text" maxlength="20" class=" laydate-icon form-control layer-date input-sm" 
						value="<fmt:formatDate value="${userInfoReport.endtime}" pattern="yyyy-MM-dd"/>"  style="width:185px;" placeholder="结束时间" readonly="readonly"/>&nbsp;&nbsp;
					 	<!-- 查询 重置按钮 -->
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
									<table:exportExcel url="${ctx}/forms/collect/exportinfo"></table:exportExcel>
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
							<th style="text-align: center;">用户ID</th>
							<th style="text-align: center;">用户昵称</th>
							<th style="text-align: center;">学分(总)</th>
							<th style="text-align: center;">被预约次数(总)</th>
							<th style="text-align: center;">云币额</th>
							<th style="text-align: center;">商家</th>
							<th style="text-align: center;">区域</th>
							<th style="text-align: center;">集团军</th>
							<th style="text-align: center;">市场</th>
							<th style="text-align: center;">门店</th>
							<th style="text-align: center;">职位</th>
							<th style="text-align: center;">角色</th>
							<th style="text-align: center;">创建时间</th>
							<th style="text-align: center;">注册手机号</th>
						</tr>
					</thead>
					<tbody style="text-align: center;">
					<c:forEach items="${page.list}" var="userinfo" varStatus="status">
						<tr>		
							<td>${userinfo.userId}</td>				
							<td>${userinfo.userNmae}</td>
							<td>${userinfo.score}</td>
							<td>${userinfo.bespeak}</td>
							<td>${userinfo.gold}</td>				
							<td>${userinfo.oneClass}</td>
							<td>${userinfo.twoClass}</td>
							<td>${userinfo.threeClass}</td>	
							<td>${userinfo.foreClass}</td>				
							<td>${userinfo.fiveClass}</td>
							<td>${userinfo.job}</td>	
							<td>${userinfo.role}</td>
							<td>${userinfo.createTime}</td>	
							<td>${userinfo.mobile}</td>			
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