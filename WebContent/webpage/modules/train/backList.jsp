<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/webpage/include/taglib.jsp"%>

<html>
<head>
<title>回看列表</title>
<meta name="decorator" content="default" />

<!-- <script type="text/javascript">
	function page(n, s) {
		alert($("#pageNo").val());
		alert($("#pageSize").val());
		$("#searchForm").submit();
		return false;
	}
</script> -->
</head>
<body class="gray-bg">
	<div class="wrapper wrapper-content">
		<div class="ibox">
<!-- 			<div class="ibox-title"> -->
<!-- 				<h5>回看列表</h5> -->
<!-- 			</div> -->
			<sys:message content="${message}" />
			<!-- 查询条件 -->
			<div class="ibox-content">
				<div class="clearfix">
					<form:form id="searchForm" modelAttribute="trainLivePlayback" action="${ctx}/train/live/backform" method="post" class="form-inline">
							<input id="pageNo" name="pageNo" type="hidden" value="${page.pageNo}" />
							<input id="pageSize" name="pageSize" type="hidden" value="${page.pageSize}" />
							<input id="userId" name="userId" type="hidden" value="${trainLivePlayback.userId }">
					</form:form>
<%-- <!-- 					工具栏 -->
<!-- 					<div class="row" style="padding-top: 10px;"> -->
<!-- 						<div class="col-sm-12"> -->
<!-- 							<div class="pull-right"> -->
<!-- 								<button  class="btn btn-primary btn-rounded btn-outline btn-sm " onclick="search()" ><i class="fa fa-search"></i> 查询</button> -->
<!-- 								<button  class="btn btn-primary btn-rounded btn-outline btn-sm " onclick="reset()" ><i class="fa fa-refresh"></i> 重置</button> -->
<!-- 							</div> -->
<!-- 						</div> -->
<!-- 					</div> -->
<!-- 				</div> --><--%>
				<p></p>
				<table id="contentTable" class="table table-striped table-bordered  table-hover table-condensed  dataTables-example dataTable no-footer">
					<thead>
						<tr>
							<th style="text-align: center;">编号</th>
							<th style="text-align: center;">回看Id</th>
							<th style="text-align: center;">回看标题</th>
							<th style="text-align: center;">回看描述</th>
							<th style="text-align: center;">直播时间</th>
							<th style="text-align: center;">结束时间</th>
							<th style="text-align: center;">收藏数</th>
							<th style="text-align: center;">点赞数</th>
							<th style="text-align: center;">观看次数</th>
						</tr>
					</thead>
					<tbody style="text-align: center;">
						<c:forEach items="${page.list}" var="back">
							<tr>
								<td>${back.id}</td>
								<td>${back.playbackId}</td>
								<td>${back.name}</td>
								<td>${back.desc}</td>
								<td><fmt:formatDate value="${back.bengtime}" pattern="yyyy-MM-dd HH:mm:ss"/></td>
								<td><fmt:formatDate value="${back.endtime}" pattern="yyyy-MM-dd HH:mm:ss"/></td>
								<td>${back.collect}</td>
								<td>${back.thumbup}</td>
								<td>${back.playNum}</td>
							</tr>	
						</c:forEach>
					</tbody>
					 <%-- <tfoot> 
 						<tr> 
							<td colspan="20"> 
 								<!-- 分页代码 --> 
 								<div class="tfoot">
									<table:page page="${page}"></table:page>
 								</div> 
 							</td> 
 						</tr>
 					</tfoot> --%>
				</table>
				<table:page page="${page}"></table:page>
			</div>
		</div>
	</div>
</div>

</body>
</html>