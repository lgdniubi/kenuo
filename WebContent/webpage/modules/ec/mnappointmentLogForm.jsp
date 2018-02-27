<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/webpage/include/taglib.jsp"%>
<html>
<head>
	<title>日志查询</title>
	<meta name="decorator" content="default"/>
	<script type="text/javascript">
		function page(n,s){
			$("#pageNo").val(n);
			$("#pageSize").val(s);
			$("#searchForm").submit();
	    	return false;
	    }
	</script>
</head>
<body>
	<div class="wrapper wrapper-content">
		<div class="ibox">
	    	<div class="ibox-content">
	    		<form id="searchForm" action="${ctx}/ec/mtmyMnappointment/findReservationLog" method="post" class="navbar-form navbar-left searcharea">
					<!-- 翻页隐藏文本框 -->
					<input id="pageNo" name="pageNo" type="hidden" value="${page.pageNo}"/>
					<input id="pageSize" name="pageSize" type="hidden" value="${page.pageSize}"/>
					<input id="reservationId" name="reservationId" type="hidden" value="${reservationLog.reservationId}"/>
					<table id="contentTable" class="table table-striped table-bordered  table-hover table-condensed  dataTables-example dataTable no-footer">
						<thead>
							<tr>
								<th style="text-align: center;">时间</th>
								<th style="text-align: center;">操作人</th>
								<th style="text-align: center;">标题</th>
								<th style="text-align: center;">操作内容</th>
								<th style="text-align: center;">操作渠道</th>
								<th style="text-align: center;">操作平台</th>
							</tr>
						</thead>
						<tbody>
							<c:forEach items="${page.list}" var="reservationLog">
								<tr>
									<td align="center"><fmt:formatDate value="${reservationLog.createDate}" pattern="yyyy-MM-dd HH:mm:ss"/></td>
									<td align="center">${reservationLog.createBy.name}</td>
									<td align="center">${reservationLog.title}</td>
									<td align="center">${reservationLog.content}</td>
									<td align="center">
										<c:if test="${reservationLog.channelFlag== 'wap'}">
											wap端
										</c:if>
										<c:if test="${reservationLog.channelFlag== 'ios'}">
											苹果手机
										</c:if>
										<c:if test="${reservationLog.channelFlag=='android'}">
											安卓手机
										</c:if>
										<c:if test="${reservationLog.channelFlag=='bm'}">
											后台管理
										</c:if>
									</td>
									<td align="center">
										<c:if test="${reservationLog.platformFlag== 'mtmy'}">
											每天美耶
										</c:if>
										<c:if test="${reservationLog.platformFlag== 'fzx'}">
											妃子校
										</c:if>
									</td>
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
				</form>
			</div>
		</div>
	</div>
</body>
</html>