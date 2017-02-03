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
	    		<form id="searchForm" action="${ctx}/ec/mtmyMnappointment/findListLog" method="post" class="navbar-form navbar-left searcharea">
					<!-- 翻页隐藏文本框 -->
					<input id="pageNo" name="pageNo" type="hidden" value="${page.pageNo}"/>
					<input id="pageSize" name="pageSize" type="hidden" value="${page.pageSize}"/>
					<input id="reservationId" name="reservationId" type="hidden" value="${reservationLog.reservationId}"/>
					<table id="contentTable" class="table table-striped table-bordered  table-hover table-condensed  dataTables-example dataTable no-footer">
						<thead>
							<tr>
								 <th style="text-align: center;width:100px;" class="active">操作者</th>
							     <th style="text-align: center;width:80px;" class="active">操作时间</th>
							     <th style="text-align: center;" class="active">相关内容</th>
							</tr>
						</thead>
						<tbody>
							<c:forEach items="${page.list }" var="reservationLog">
								<tr>
								  	<td align="center">${reservationLog.createBy.name }</td>
								  	<td align="center"><fmt:formatDate value="${reservationLog.createDate }" pattern="yyyy-MM-dd HH:mm:ss"/></td>
								  	<td>																					
								  		店铺名:${reservationLog.officeName }|美容师:${reservationLog.userName }|服务状态:${fns:getDictLabel(reservationLog.reservationStatus, 'reservation_status', '')}<br>
								  		备注：${reservationLog.remarks }
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