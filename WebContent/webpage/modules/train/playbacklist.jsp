<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/webpage/include/taglib.jsp"%>

<html>
<head>
<title>回看列表</title>
<meta name="decorator" content="default" />

<script type="text/javascript">
	function page(n, s) {
		$("#pageNo").val(n);
		$("#pageSize").val(s);
		$("#searchForm").submit();
		return false;
	}
	
	function newReset(){
		window.location="${ctx}/train/playback/list";
	}
	
	
	
</script>
</head>




<body class="gray-bg">
	<div class="wrapper wrapper-content">
		<div class="ibox">
			<div class="ibox-title">
				<h5>回看列表</h5>
			</div>
			<sys:message content="${message}" />
			<!-- 查询条件 -->
			<div class="ibox-content">
				<div class="clearfix">
					<form:form id="searchForm" modelAttribute="trainLivePlayback" action="${ctx}/train/playback/list" method="post" class="form-inline">
						<input id="pageNo" name="pageNo" type="hidden" value="${page.pageNo}"/>
						<input id="pageSize" name="pageSize" type="hidden" value="${page.pageSize}"/>
						<div class="form-group">
							<label>用户名：</label><form:input path="userName" htmlEscape="false" maxlength="50" class=" form-control input-sm" />
							<label>回看主题：</label><form:input path="name" htmlEscape="false" maxlength="50" class=" form-control input-sm" />
							<label>回看编码：</label><form:input path="playbackId" htmlEscape="false" maxlength="50" class=" form-control input-sm"/>
							<label>直播申请编码：</label><form:input path="auditId" htmlEscape="false" maxlength="50" class=" form-control input-sm" onkeyup="this.value=this.value.replace(/\D/g,'')" onfocus="if(value == '0')value=''" onblur="if(this.value == '')this.value='0';"/>
						</div>
					</form:form>
					<!-- 工具栏 -->
					<div class="row" style="padding-top: 10px;">
						<div class="col-sm-12">
							<div class="pull-right">
								<button  class="btn btn-primary btn-rounded btn-outline btn-sm " onclick="search()" ><i class="fa fa-search"></i> 查询</button>
								<button  class="btn btn-primary btn-rounded btn-outline btn-sm " onclick="newReset()" ><i class="fa fa-refresh"></i> 重置</button>
							</div>
						</div>
					</div>
				</div>
				<p></p>
				<table id="contentTable"
					class="table table-striped table-bordered  table-hover table-condensed  dataTables-example dataTable no-footer">
					<thead>
						<tr>	
							<th style="text-align: center;">编号</th>
							<th style="text-align: center;">直播申请编号</th>
							<th style="text-align: center;">用户名</th>
							<th style="text-align: center;">用户职位</th>
							<th style="text-align: center;">回看编码</th>
							<th style="text-align: center;">回看主题</th>
							<th style="text-align: center;">回看描述</th>
							<th style="text-align: center;">是否付费</th>
							<th style="text-align: center;">开始时间</th>
							<th style="text-align: center;">结束时间</th>
							<th style="text-align: center;">收藏次数</th>
							<th style="text-align: center;">点赞次数</th>
							<th style="text-align: center;">播放次数</th>
							<th style="text-align: center;">视频是否下载</th>
							<th style="text-align: center;">是否显示</th>
							<th style="text-align: center;">操作</th>
						</tr>
					</thead>
					<tbody style="text-align: center;">
						<c:forEach items="${page.list}" var="playback">
							<tr>
								<td>${playback.id}</td>
								<td>${playback.auditId}</td>
								<td>${playback.userName}</td>
								<td>${playback.label}</td>
								<td>${playback.playbackId}</td>
								<td>${playback.name}</td>
								<td>${playback.desc}</td>
								<td>
									<c:if test="${playback.isPay==1}">
										免费
									</c:if>
									<c:if test="${playback.isPay==2}">
										收费
									</c:if>
								</td>
								<td><fmt:formatDate value="${playback.bengtime}" pattern="yyyy-MM-dd HH:mm:ss"/></td>
								<td><fmt:formatDate value="${playback.endtime}" pattern="yyyy-MM-dd HH:mm:ss"/></td>
								<td>${playback.collect}</td>
								<td>${playback.thumbup}</td>
								<td>${playback.playNum}</td>
								<td>
									<c:if test="${playback.downStatus==0}">
										否
									</c:if>
									<c:if test="${playback.downStatus==1}">
										是
									</c:if>
								</td>
								<td>
									<c:if test="${playback.isShow==0}">
										显示
									</c:if>
									<c:if test="${playback.isShow==1}">
										隐藏
									</c:if>
								</td>
								<td>
									<shiro:hasPermission name="train:playback:view">
										<a href="#" onclick="openDialogView('查看回看', '${ctx}/train/playback/form?id=${playback.id}','800px','650px')"
											class="btn btn-info btn-xs"><i class="fa fa-search-plus"></i>查看</a>
									</shiro:hasPermission> 
									<shiro:hasPermission name="train:playback:edit">
										<c:if test="${playback.isShow==1}">
											<a href="${ctx}/train/playback/pIsShow?isShow=0&id=${playback.id}"  class="btn btn-danger btn-xs">
											<i class="fa fa-close"></i>显示</a>
										</c:if>
										<c:if test="${playback.isShow==0}">
											<a href="${ctx}/train/playback/pIsShow?isShow=1&id=${playback.id}"  class="btn btn-primary btn-xs">
												<i class="fa fa-file"></i>隐藏</a>
										</c:if>
									</shiro:hasPermission>
									<shiro:hasPermission name="train:live:sku">
										<a href="#" onclick="openDialogView('配置列表', '${ctx}/train/live/liveSkuForm?auditId=${playback.auditId}','800px','500px')"
											 class="btn btn-info btn-xs"><i class="fa fa-search-plus"></i>查看配置</a>
									</shiro:hasPermission>

								</td>
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