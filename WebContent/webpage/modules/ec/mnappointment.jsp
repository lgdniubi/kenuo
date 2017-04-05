<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/webpage/include/taglib.jsp"%>
<html>
<head>
	<title>预约管理</title>
	<meta name="decorator" content="default"/>
	<!-- 日期控件 -->
    <script type="text/javascript" src="${ctxStatic}/My97DatePicker/WdatePicker.js"></script>
	<script type="text/javascript">
		function page(n,s){
			$("#pageNo").val(n);
			$("#pageSize").val(s);
			$("#searchForm").submit();
	    	return false;
	    }
	    function warning(){
	    	top.layer.msg("操作已超时！不可取消订单！", {icon: 0});
	    }
	</script>
</head>
<body class="gray-bg">
	<div class="wrapper wrapper-content">
		<div class="ibox">
			<div class="ibox-title">
				<h5>预约管理</h5>
			</div>
			<sys:message content="${message}"/>
			<!-- 查询条件 -->
	    	<div class="ibox-content">
				<div class="clearfix">
					<form id="searchForm" action="${ctx}/ec/mtmyMnappointment/mnappointment" method="post" class="navbar-form navbar-left searcharea">
						<!-- 翻页隐藏文本框 -->
						<input id="pageNo" name="pageNo" type="hidden" value="${page.pageNo}"/>
						<input id="pageSize" name="pageSize" type="hidden" value="${page.pageSize}"/>
						<table:sortColumn id="orderBy" name="orderBy" value="${page.orderBy}" callback="sortOrRefresh();"/><!-- 支持排序 -->
						<div class="form-group">
							<input id="keyword" name="keyword" type="text" value="${reservation.keyword }" class="form-control" placeholder="请输入关键字"> 
							<input id="orderId" name="orderId" type="text" value="${reservation.orderId }" class="form-control" placeholder="请输入订单号"> 
							<select class="form-control" id="apptStatus" name="apptStatus">
								<option value="">请选择预约状态</option>
								<c:forEach items="${fns:getDictList('reservation_status')}" var="reservation_status">
									<c:choose>
										<c:when test="${reservation_status.value eq reservation.apptStatus}">
											<option value="${reservation_status.value }" selected="selected">${reservation_status.label }</option>
										</c:when>
										<c:otherwise>
											<option value="${reservation_status.value }">${reservation_status.label }</option>
										</c:otherwise>
									</c:choose>
								</c:forEach>
							</select>
				 时间范围：<input id="beginDate" name="beginDate" class="Wdate form-control layer-date input-sm required" style="height: 30px;width: 200px" type="text" 
								value="<fmt:formatDate value="${reservation.beginDate}" pattern="yyyy-MM-dd HH:mm:ss"/>" onclick="WdatePicker({dateFmt:'yyyy-MM-dd HH:mm:ss',maxDate:'#F{$dp.$D(\'endDate\')}'})" readonly="readonly"/>
							<label>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;--&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</label>
							<input id="endDate" name="endDate" class="Wdate form-control layer-date input-sm required" style="height: 30px;width: 200px" type="text" 
								value="<fmt:formatDate value="${reservation.endDate}" pattern="yyyy-MM-dd HH:mm:ss"/>" onclick="WdatePicker({dateFmt:'yyyy-MM-dd HH:mm:ss',minDate:'#F{$dp.$D(\'beginDate\')}'})" readonly="readonly"/>		
						 </div>	
					</form>
					 <div class="pull-right">
						<button  class="btn btn-primary btn-rounded btn-outline btn-sm " onclick="search()" ><i class="fa fa-search"></i> 查询</button>
						<button  class="btn btn-primary btn-rounded btn-outline btn-sm " onclick="reset()" ><i class="fa fa-refresh"></i> 重置</button>
					 </div>
				</div>
				 <!-- 工具栏 -->
				<div class="row" style="padding-bottom: 5px;">
					<div class="col-sm-12">
                        <div class="pull-left">
                        	<shiro:hasPermission name="ec:mtmyMnappointment:addReservation">
	                        	<button class="btn btn-white btn-sm" title="添加预约" onclick="openDialog('添加预约', '${ctx}/ec/mtmyMnappointment/addReservation','750px', '600px')" data-placement="left" data-toggle="tooltip">
									<i class="fa fa-plus"></i>添加预约
								</button>
							</shiro:hasPermission>
                        </div>
					</div>
				</div>
				<table id="contentTable" class="table table-striped table-bordered  table-hover table-condensed  dataTables-example dataTable no-footer">
					<thead>
						<tr>
							<th style="text-align: center;">ID</th>
						    <th style="text-align: center;">预约日期</th>
						    <th style="text-align: center;">预约时间</th>
						    <th style="text-align: center;">服务名称</th>
						    <th style="text-align: center;">实体店名称</th>
						    <th style="text-align: center;">美容师</th>
						    <th style="text-align: center;">客户姓名</th>
						    <th style="text-align: center;">客户电话</th>
						    <th style="text-align: center;">状态</th>
						    <th style="text-align: center;">操作</th>
						</tr>
					</thead>
					<tbody style="text-align: center;">
						<c:forEach items="${page.list }" var="reservation">
							<tr>
								<td>${reservation.reservationId }</td>
							    <td><fmt:formatDate value="${reservation.apptDate }" pattern="yyyy-MM-dd"/></td>
							  	<td>
							  		${reservation.apptStartTime }
									<label>&nbsp;&nbsp;--&nbsp;&nbsp;</label>
									${reservation.apptEndTime }
							  	</td>
							  	<td>${reservation.goods.goodsName }</td>
							  	<td>${reservation.office.name }</td>
							  	<td>${reservation.user.name }</td>
							  	<td> 
							  		${reservation.userName }
						  		</td>
							  	<td>${reservation.userPhone }</td>
							  	<td>${fns:getDictLabel(reservation.apptStatus, 'reservation_status', '')}</td>
							  	<td style="text-align: left;">
							  		<a class="btn btn-info btn-xs" onclick="openDialogView('查看预约详情', '${ctx}/ec/mtmyMnappointment/oneMnappointment?reservationId=${reservation.reservationId }','750px', '600px')"><i class="fa fa-search-plus"></i>查看</a>
									<c:if test="${reservation.apptStatus == '0'}">
										<shiro:hasAnyPermissions name="ec:mtmyMnappointment:update">
								  			<a href="#" onclick="openDialog('修改预约', '${ctx}/ec/mtmyMnappointment/oneMnappointment?reservationId=${reservation.reservationId }','800px', '620px')" class="btn btn-success btn-xs"><i class="fa fa-edit"></i> 修改</a>
							  			</shiro:hasAnyPermissions>
								  		<shiro:hasAnyPermissions name="ec:mtmyMnappointment:cancel">
											<a class="btn btn-success btn-xs btn-danger" href="${ctx}/ec/mtmyMnappointment/cancel?reservationId=${reservation.reservationId }" onclick="return confirmx('确定要取消此预约吗？', this.href)"><i class="fa fa-close"></i>取消</a>
										</shiro:hasAnyPermissions>
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
			</div>
		</div>
	</div>
	<div class="loading"></div>
	<script src="${ctxStatic}/train/js/jquery.datetimepicker.js" type="text/javascript" charset="utf-8"></script>
	<script type="text/javascript">
		$('.datetimepicker').datetimepicker({lang:'ch'});
	</script>
</body>
</html>