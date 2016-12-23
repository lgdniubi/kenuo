<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/webpage/include/taglib.jsp"%>
<html>
<head>
	<title>退货订单列表</title>
	<meta name="decorator" content="default"/>
	<script type="text/javascript">

		function page(n,s){
			$("#pageNo").val(n);
			$("#pageSize").val(s);
			$("#searchForm").submit();
	    	return false;
	    }
		
		$(document).ready(function() {
			
			
			var start = {
				    elem: '#begtime',
				    format: 'YYYY-MM-DD',
				    event: 'focus',
				    max: $("#endtime").val(),   //最大日期
				    istime: false,				//是否显示时间
				    isclear: true,				//是否显示清除
				    istoday: true,				//是否显示今天
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
				    isclear: true,
				    istoday: true,
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
				<h5>退货列表</h5>
			</div>
			 <sys:message content="${message}"/>
					<!-- 查询条件 -->
				<div class="ibox-content">
				<div class="clearfix">
				<form:form id="searchForm" modelAttribute="returnGoods" action="${ctx}/ec/returngoods/list" method="post" class="form-inline">
					<input id="pageNo" name="pageNo" type="hidden" value="${page.pageNo}"/>
					<input id="pageSize" name="pageSize" type="hidden" value="${page.pageSize}"/>
					<table:sortColumn id="orderBy" name="orderBy" value="${page.orderBy}" callback="sortOrRefresh();"/><!-- 支持排序 -->
					<div class="form-group">
						
						<form:input path="username" htmlEscape="false" maxlength="50" class=" form-control input-sm" placeholder="用户名"/>&nbsp;&nbsp;&nbsp;&nbsp;
						
						
						<form:input path="mobile" htmlEscape="false" maxlength="50" class=" form-control input-sm" placeholder="手机号"/>&nbsp;&nbsp;&nbsp;&nbsp;
						
						<form:input path="orderid" htmlEscape="false" maxlength="50" class=" form-control input-sm" placeholder="订单号"/>&nbsp;&nbsp;&nbsp;&nbsp;
						
						<label>入库状态：</label>	
						
						 <form:select path="storagestatus"  class="form-control" style="width:185px;">
								<form:option value="0">全部</form:option>
			      		 		<form:option value="1">未入库</form:option>
			      		 		<form:option value="2">已入库</form:option> 
		      				</form:select>
		      				<p></p>
		      			<label>退款状态：</label>
		      			<form:select path="returnstatus"  class="form-control" style="width:185px;">
								<form:option value="0">全部</form:option>
			      		 		<form:option value="1">未退款</form:option>
			      		 		<form:option value="2">已退款</form:option> 	
		      			</form:select>
						<label>创建时间：</label>
						<input id="begtime" name="begtime" type="text" maxlength="20" class="laydate-icon form-control layer-date input-sm"
							value="<fmt:formatDate value="${returnGoods.begtime}" pattern="yyyy-MM-dd"/>" placeholder="开始时间" readonly="readonly" style="width:185px;"/>
						—
						<input id="endtime" name="endtime" type="text" maxlength="20" class=" laydate-icon form-control layer-date input-sm" 
						value="<fmt:formatDate value="${returnGoods.endtime}" pattern="yyyy-MM-dd"/>" placeholder="结束时间" readonly="readonly" style="width:185px;"/>
					 </div>	
				</form:form>
					<!-- 工具栏 -->
					<div class="row">
					<div class="col-sm-12">
						<div class="pull-right">
							<button  class="btn btn-primary btn-rounded btn-outline btn-sm " onclick="search()" ><i class="fa fa-search"></i> 查询</button>
							<button  class="btn btn-primary btn-rounded btn-outline btn-sm " onclick="reset()" ><i class="fa fa-refresh"></i> 重置</button>
						</div>
					</div>
					</div>
				</div>
								
					<table id="contentTable" class="table table-striped table-bordered  table-hover table-condensed  dataTables-example dataTable no-footer">
						<thead>
							<tr>
								
								<th style="text-align: center;">订单号</th>
								<th style="text-align: center;">用户名</th>
								<th style="text-align: center;">订单状态</th>
								<th style="text-align: center;">支付金额</th>
								<th style="text-align: center;">创建时间</th>
								<th style="text-align: center;">退款原因</th>
								<th style="text-align: center;">入库状态</th>
								<th style="text-align: center;">退款金额</th>
								<th style="text-align: center;">退款状态</th>
								<th style="text-align: center;">录入人员</th>
								<th style="text-align: center;">退款人员</th>
								<th style="text-align: center;">客服备注</th>
								<th style="text-align: center;">操作</th>
							</tr>
						</thead>
						<tbody>
						<c:forEach items="${page.list}" var="returnGoods">
							<tr>
								<td style="text-align: center;">${returnGoods.orderid}</td>
								<td style="text-align: center;">${returnGoods.username}</td>	
											
								<c:if test="${returnGoods.orderstatus==-2}">
									<td  style="text-align: center;">取消</td>
								</c:if>
								<c:if test="${returnGoods.orderstatus==-1}">
									<td  style="text-align: center;">未支付</td>
								</c:if>
								<c:if test="${returnGoods.orderstatus==1}">
									<td  style="text-align: center;">已支付</td>
								</c:if>
								<c:if test="${returnGoods.orderstatus==2}">
									<td  style="text-align: center;">已发货</td>
								</c:if>
								<c:if test="${returnGoods.orderstatus==3}">
									<td  style="text-align: center;">已退款</td>
								</c:if>
								<c:if test="${returnGoods.orderstatus==4}">
									<td  style="text-align: center;">已完成</td>
								</c:if>
								<c:if test="${returnGoods.orderstatus==5}">
									<td  style="text-align: center;">申请退款</td>
								</c:if>
								<td style="text-align: center;">${returnGoods.orderamount}</td>
								<td style="text-align: center;"><fmt:formatDate value="${returnGoods.addtime}" pattern="yyyy-MM-dd HH:mm:ss" /> </td>
								
								<td style="text-align: center;">${returnGoods.reason}</td>
								
								<c:if test="${returnGoods.storagestatus==1}">
									<td  style="text-align: center;">未入库</td>
								</c:if>
								<c:if test="${returnGoods.storagestatus==2}">
									<td  style="text-align: center;">已入库</td>
								</c:if>
								<td style="text-align: center;">${returnGoods.returnmoney}</td>
								<c:if test="${returnGoods.returnstatus==1}">
									<td  style="text-align: center;">未退款</td>
								</c:if>
								<c:if test="${returnGoods.returnstatus==2}">
									<td  style="text-align: center;">已退款</td>
								</c:if>
								
								<td style="text-align: center;">${returnGoods.entryoperator}</td>
								<td style="text-align: center;">${returnGoods.financialoperator}</td>
								<td style="text-align: center;">${returnGoods.remark}</td>
								<td style="text-align: center;">
				 					<shiro:hasPermission name="ec:return:view"> 
				 						<a href="#" onclick="openDialogView('退货订单', '${ctx}/ec/returngoods/returnform?orderid=${returnGoods.orderid}','600px','500px')" class="btn btn-info btn-xs" ><i class="fa fa-search-plus"></i> 查看详情</a>
				 					</shiro:hasPermission>
									<shiro:hasPermission name="ec:return:edit">
										<c:if test="${returnGoods.returnstatus!=2}">
											<a href="#" onclick="openDialog('编辑', '${ctx}/ec/returngoods/returnform?orderid=${returnGoods.orderid}','600px','500px')"  class="btn btn-success btn-xs" ><i class="fa fa-edit"></i>修改</a>
										</c:if>
										<c:if test="${returnGoods.returnstatus==2}">
										<a href="#" style="background:#C0C0C0;color:#FFF"  class="btn  btn-xs" ><i class="fa fa-edit"></i>修改</a>
										</c:if>
									</shiro:hasPermission>
									<shiro:hasPermission name="ec:return:confirm"> 
										<c:if test="${returnGoods.returnstatus!=2}">
										    <c:if test="${returnGoods.storagestatus==1}">
												<a href="${ctx}/ec/returngoods/confirm?orderid=${returnGoods.orderid}" onclick="return confirmx('商品未入库确认退款吗？', this.href)" class="btn btn-danger btn-xs"><i class="fa fa-save"></i>确认退款</a>
											</c:if>
											<c:if test="${returnGoods.storagestatus!=1}">
												<a href="${ctx}/ec/returngoods/confirm?orderid=${returnGoods.orderid}" onclick="return confirmx('确认退款吗？', this.href)" class="btn btn-danger btn-xs"><i class="fa fa-save"></i>确认退款</a>
											</c:if>
										</c:if>
										<c:if test="${returnGoods.returnstatus==2}">
											<a href="#" style="background:#C0C0C0;color:#FFF" class="btn  btn-xs" ><i class="fa fa-save"></i>确认退款</a>
										</c:if>
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