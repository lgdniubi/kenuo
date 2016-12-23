<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/webpage/include/taglib.jsp"%>
<html>
<head>
	<title>体验报名管理</title>
	<meta name="decorator" content="default"/>
    <meta name="viewport" content="width=device-width, initial-scale=1.0, minimum-scale=1.0, maximum-scale=1.0,user-scalable=no">
    <script type="text/javascript">
    $(document).ready(function() {
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
   function nowReset(){//重置，页码清零
		$("#beginDate,#endDate").val("");
		$("#searchForm").submit();
 	}
    </script>
</head>
<body class="gray-bg">
	<div class="wrapper wrapper-content">
		<div class="ibox">
			<div class="ibox-title">
				<h5>体验报名管理</h5>
			</div>
			<sys:message content="${message}"/>
			<!-- 查询条件 -->
	    	<div class="ibox-content">
				<div class="clearfix">
					<form id="searchForm" modelAttribute="" action="${ctx}/ec/mtmyArticleList/applyList" method="post" class="navbar-form navbar-left searcharea">
						<div class="form-group">
							<!-- 翻页隐藏文本框 -->
							<input id="pageNo" name="pageNo" type="hidden" value="${page.pageNo}"/>
							<input id="pageSize" name="pageSize" type="hidden" value="${page.pageSize}"/>
							<label>报名时间：</label>
							<input id="beginDate" name="beginDate" type="text" maxlength="20" class="laydate-icon form-control layer-date input-sm" value="<fmt:formatDate value="${mtmyArticle.beginDate }" pattern="yyyy-MM-dd"/>" placeholder="开始时间" readonly="readonly"/>
							<label>--</label>
							<input id="endDate" name="endDate" type="text" maxlength="20" class=" laydate-icon form-control layer-date input-sm" value="<fmt:formatDate value="${mtmyArticle.endDate }" pattern="yyyy-MM-dd"/>" placeholder="结束时间" readonly="readonly"/>
						</div>	
						<div class="pull-right">
							<button  class="btn btn-primary btn-rounded btn-outline btn-sm " onclick="search()" ><i class="fa fa-search"></i> 查询</button>
							<button  class="btn btn-primary btn-rounded btn-outline btn-sm " onclick="nowReset()" ><i class="fa fa-refresh"></i> 重置</button>
						</div>
					</form>
				</div>
				<table id="contentTable" class="table table-striped table-bordered  table-hover table-condensed  dataTables-example dataTable no-footer">
					<thead> 
                		<tr>
                			<th style="text-align: center;">报名来源</th>
                			<th style="text-align: center;">报名信息</th>
                			<th style="text-align: center;">真实姓名</th>
                			<th style="text-align: center;">用户名</th>
                			<th style="text-align: center;">用户手机</th>
                		</tr>
                	</thead>
                    <tbody>
                    	<c:forEach items="${page.list}" var="apply">
							<tr>
								<td style="text-align: center;">${apply.title}</td>
								<td style="text-align: center;">${apply.contents}</td>
								<td style="text-align: center;">${apply.users.name}</td>
								<td style="text-align: center;">${apply.users.nickname}</td>
								<td style="text-align: center;">${apply.users.mobile}</td>
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
</body>
</html>