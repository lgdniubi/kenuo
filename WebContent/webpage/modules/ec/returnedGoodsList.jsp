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
				<h5>售后列表</h5>
			</div>
			 <sys:message content="${message}"/>
					<!-- 查询条件 -->
				<div class="ibox-content">
				<div class="clearfix">
				<form:form id="searchForm" modelAttribute="returnedGoods" action="${ctx}/ec/returned/list" method="post" class="form-inline">
					<input id="pageNo" name="pageNo" type="hidden" value="${page.pageNo}"/>
					<input id="pageSize" name="pageSize" type="hidden" value="${page.pageSize}"/>
					<table:sortColumn id="orderBy" name="orderBy" value="${page.orderBy}" callback="sortOrRefresh();"/><!-- 支持排序 -->
					<div class="form-group">
						<form:input path="userName" htmlEscape="false" maxlength="50" class=" form-control input-sm" placeholder="用户名"/>&nbsp;&nbsp;
						<form:input path="mobile" htmlEscape="false" maxlength="50" class=" form-control input-sm" placeholder="手机号"/>&nbsp;&nbsp;
						<form:input path="returnedId" htmlEscape="false" maxlength="50" class=" form-control input-sm" placeholder="退货单号"/>&nbsp;&nbsp;
						<form:input path="orderId" htmlEscape="false" maxlength="50" class=" form-control input-sm" placeholder="原订单号"/>&nbsp;&nbsp;
						<p></p>
						<label>入库状态：</label>	
						<form:select path="isStorage"  class="form-control" style="width:185px;">
								<form:option value=" ">全部</form:option>
			      		 		<form:option value="0">未入库</form:option>
			      		 		<form:option value="1">已入库</form:option> 
		      			</form:select>
		      			<label>退货状态：</label>
		      			<form:select path="returnStatus"  class="form-control" style="width:185px;">
								<form:option value=" ">全部</form:option>
			      		 		<form:option value="-10">拒绝退货</form:option>
			      		 		<form:option value="11">申请退货</form:option>
			      		 		<form:option value="12">同意退货</form:option>
			      		 		<form:option value="13">退货中</form:option>
			      		 		<form:option value="14">退货完成</form:option>
			      		 		<form:option value="15">退款中</form:option>
			      		 		<form:option value="16">已退款</form:option>
			      		 		<form:option value="-20">拒绝换货</form:option>
			      		 		<form:option value="21">申请换货</form:option>	
			      		 		<form:option value="22">同意换货</form:option>	
			      		 		<form:option value="23">换货退货中</form:option>	
			      		 		<form:option value="24">换货退货完成</form:option>	
			      		 		<form:option value="25">换货中</form:option>	
			      		 		<form:option value="26">换货完成</form:option>	
			      		 		
		      			</form:select>
		      			<p></p>
						<label>创建时间：</label>
						<input id="begtime" name="begtime" type="text" maxlength="20" class="laydate-icon form-control layer-date input-sm"
							value="<fmt:formatDate value="${returnedGoods.begtime}" pattern="yyyy-MM-dd"/>" placeholder="开始时间" readonly="readonly" style="width:185px;"/>
						—
						<input id="endtime" name="endtime" type="text" maxlength="20" class=" laydate-icon form-control layer-date input-sm" 
						value="<fmt:formatDate value="${returnedGoods.endtime}" pattern="yyyy-MM-dd"/>" placeholder="结束时间" readonly="readonly" style="width:185px;"/>
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
				<p></p>
				</div>		
					<table id="contentTable" class="table table-striped table-bordered  table-hover table-condensed  dataTables-example dataTable no-footer">
						<thead>
							<tr>
								<th style="text-align: center;">退货单号</th>
								<th style="text-align: center;">用户名</th>
								<th style="text-align: center;">退货状态</th>
								<th style="text-align: center;">支付金额</th>
								<th style="text-align: center;">申请服务</th>
								<th style="text-align: center;">申请时间</th>
								<th style="text-align: center;">退货原因</th>
								<th style="text-align: center;">入库状态</th>
								<th style="text-align: center;">退款金额</th>
								<th style="text-align: center;">审核人员</th>
								<th style="text-align: center;">收货人员</th>
								<th style="text-align: center;">发货人员</th>
								<th style="text-align: center;">退款人员</th>
								<th style="text-align: center;">客服备注</th>
								<th style="text-align: center;">操作</th>
							</tr>
						</thead>
						<tbody>
						<c:forEach items="${page.list}" var="returnGoods">
							<tr>
								<td style="text-align: center;">${returnGoods.id}</td>
								<td style="text-align: center;">${returnGoods.userName}</td>	
								<td style="text-align: center;">
									<c:if test="${returnGoods.returnStatus==-10}">
										拒绝退货
									</c:if>
									<c:if test="${returnGoods.returnStatus==-20}">
										拒绝换货
									</c:if>		
									<c:if test="${returnGoods.returnStatus==11}">
										申请退货
									</c:if>
									<c:if test="${returnGoods.returnStatus==12}">
										同意退货
									</c:if>
									<c:if test="${returnGoods.returnStatus==13}">
										退货中
									</c:if>
									<c:if test="${returnGoods.returnStatus==14}">
										退货完成
									</c:if>
									<c:if test="${returnGoods.returnStatus==15}">
										退款中
									</c:if>
									<c:if test="${returnGoods.returnStatus==16}">
										已退款
									</c:if>
									<c:if test="${returnGoods.returnStatus==21}">
										申请换货
									</c:if>
									<c:if test="${returnGoods.returnStatus==22}">
										同意换货
									</c:if>
									<c:if test="${returnGoods.returnStatus==23}">
										换货退货中
									</c:if>
									<c:if test="${returnGoods.returnStatus==24}">
										换货退货完成
									</c:if>
									<c:if test="${returnGoods.returnStatus==25}">
										换货中
									</c:if>
									<c:if test="${returnGoods.returnStatus==26}">
										换货完成
									</c:if>
								</td>
								<td style="text-align: center;">${returnGoods.orderAmount}</td>
								<td style="text-align: center;">
									<c:if test="${returnGoods.applyType==0}">
										退货并退款
									</c:if>
									<c:if test="${returnGoods.applyType==1}">
										仅换货
									</c:if>
								</td>
								<td style="text-align: center;">
									<fmt:formatDate value="${returnGoods.applyDate}" pattern="yyyy-MM-dd HH:mm:ss" /> 
								</td>
								<td style="text-align: center;">
									<!-- 当文本内容大于5个字符时，只显示其前五个字符 -->
								  	<c:choose>  
								         <c:when test="${fn:length(returnGoods.returnReason) > 20}">  
								             <c:out value="${fn:substring(returnGoods.returnReason, 0, 20)}..." />  
								         </c:when>  
								        <c:otherwise>  
								           <c:out value="${returnGoods.returnReason}" />  
								        </c:otherwise>  
								    </c:choose>
								</td>
								<td  style="text-align: center;">
									<c:if test="${returnGoods.isStorage==0}">
										未入库
									</c:if>
									<c:if test="${returnGoods.isStorage==1}">
										已入库
									</c:if>
								</td>
								<td style="text-align: center;">${returnGoods.returnAmount}</td>
								<td style="text-align: center;">${returnGoods.auditBy}</td>
								<td style="text-align: center;">${returnGoods.receiptBy}</td>
								<td style="text-align: center;">${returnGoods.shipperBy}</td>
								<td style="text-align: center;">${returnGoods.financialBy}</td>
								<td style="text-align: center;">${returnGoods.remarks}</td>
								<td style="text-align: center;">
									<!-- 查看详情 -->
				 					<shiro:hasPermission name="ec:returned:view"> 
				 						<a href="#" onclick="openDialogView('售后详情', '${ctx}/ec/returned/returnform?id=${returnGoods.id}','850px','650px')" class="btn btn-info btn-xs" ><i class="fa fa-search-plus"></i> 查看详情</a>
				 					</shiro:hasPermission>
				 					<!-- 审核 -->
									<shiro:hasPermission name="ec:returned:audit">
										<c:if test="${returnGoods.returnStatus==11 or returnGoods.returnStatus==21}">
											<a href="#" onclick="openDialog('审核售后申请', '${ctx}/ec/returned/returnform?id=${returnGoods.id}','850px','650px')"  class="btn btn-success btn-xs" ><i class="fa fa-edit"></i>审核</a>
										</c:if>
										<c:if test="${returnGoods.returnStatus !=11 and returnGoods.returnStatus !=21}">
											<a href="#" style="background:#C0C0C0;color:#FFF" class="btn  btn-xs" ><i class="fa fa-edit"></i>审核</a>
										</c:if>
									</shiro:hasPermission>
									<!-- 确认收货 -->
									<shiro:hasPermission name="ec:returned:take">
										<c:if test="${returnGoods.returnStatus==12 or returnGoods.returnStatus==13 or returnGoods.returnStatus==22 or returnGoods.returnStatus==23}">
											<a href="${ctx}/ec/returned/confirmTake?id=${returnGoods.id}" onclick="return confirmx('确认商品入库了吗？', this.href)" class="btn btn-info btn-xs"><i class="fa fa-edit"></i>收货入库</a>
										</c:if>
										<c:if test="${returnGoods.returnStatus !=12 and returnGoods.returnStatus !=13 and returnGoods.returnStatus !=22 and returnGoods.returnStatus !=23}">
											<a href="#" style="background:#C0C0C0;color:#FFF" class="btn  btn-xs" ><i class="fa fa-edit"></i>收货入库</a>
										</c:if>
									</shiro:hasPermission>
									<!-- 申请退款 -->
									<shiro:hasPermission name="ec:returned:apply">
										<c:if test="${returnGoods.returnStatus==14}">
											<a href="${ctx}/ec/returned/confirmApply?id=${returnGoods.id}" onclick="return confirmx('确认申请退款吗？', this.href)" class="btn btn-success btn-xs"><i class="fa fa-edit"></i>申请退款</a>
										</c:if>
										<c:if test="${returnGoods.applyType==0}">
											<c:if test="${returnGoods.returnStatus !=14}">
												<a href="#" style="background:#C0C0C0;color:#FFF" class="btn  btn-xs" ><i class="fa fa-edit"></i>申请退款</a>
											</c:if>
										</c:if>
									</shiro:hasPermission>
									<!-- 重新发货 -->
									<shiro:hasPermission name="ec:returned:send">
										<c:if test="${returnGoods.returnStatus==24}">
											<a href="${ctx}/ec/returned/againSendApply?id=${returnGoods.id}" onclick="return confirmx('确认重新发货吗？', this.href)" class="btn btn-success btn-xs"><i class="fa fa-edit"></i>重新发货</a>
										</c:if>
										<c:if test="${returnGoods.applyType==1}">
											<c:if test="${returnGoods.returnStatus !=24}">
												<a href="#" style="background:#C0C0C0;color:#FFF" class="btn  btn-xs" ><i class="fa fa-edit"></i>重新发货</a>
											</c:if>
										</c:if>
									</shiro:hasPermission>
									<shiro:hasPermission name="ec:returned:sendGoods">
										<c:if test="${returnGoods.returnStatus==25}">
											<a href="#" onclick="openDialog('物流录入', '${ctx}/ec/returned/againSend?id=${returnGoods.id}','500px','400px')"  class="btn btn-success btn-xs" ><i class="fa fa-edit"></i>确认发货</a>
										</c:if>
										<c:if test="${returnGoods.applyType==1}">
											<c:if test="${returnGoods.returnStatus !=25}">
												<a href="#" style="background:#C0C0C0;color:#FFF" class="btn  btn-xs" ><i class="fa fa-edit"></i>确认发货</a>
											</c:if>
										</c:if>
									</shiro:hasPermission>
									<!-- 确认退款 -->
									<shiro:hasPermission name="ec:returned:confirm"> 
										<c:if test="${returnGoods.returnStatus==15}">
											<a href="${ctx}/ec/returned/confirm?id=${returnGoods.id}" onclick="return confirmx('确认退款吗？', this.href)" class="btn btn-danger btn-xs"><i class="fa fa-save"></i>确认退款</a>
										</c:if>
										<c:if test="${returnGoods.applyType==0}">
											<c:if test="${returnGoods.returnStatus!=15}">
												<a href="#" style="background:#C0C0C0;color:#FFF" class="btn  btn-xs" ><i class="fa fa-save"></i>确认退款</a>
											</c:if>
										</c:if>
									</shiro:hasPermission>
									<!-- 查看物流 -->
									<shiro:hasPermission name="ec:returned:logistics">
										<c:if test="${returnGoods.applyType==1}">
											<c:if test="${returnGoods.returnStatus==26}">
												<a href="#" onclick="openDialogView('物流信息', '${ctx}/ec/returned/shippingList?id=${returnGoods.id}','600px','500px')"  class="btn btn-info btn-xs" ><i class="fa fa-search-plus"></i>查看物流</a>
											</c:if>
											<c:if test="${returnGoods.returnStatus!=26}">
												<a href="#" style="background:#C0C0C0;color:#FFF" class="btn  btn-xs" ><i class="fa fa-search-plus"></i>查看物流</a>
											</c:if>
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