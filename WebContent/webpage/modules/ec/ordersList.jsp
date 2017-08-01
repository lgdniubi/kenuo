<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/webpage/include/taglib.jsp"%>

<html>
<head>
	<title>订单列表</title>
	<meta name="decorator" content="default"/>

	<script type="text/javascript">
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
				
				var payBeg = {
					    elem: '#payBegTime',
					    format: 'YYYY-MM-DD',
					    event: 'focus',
					    max: $("#payEndTime").val(),   //最大日期
					    istime: false,				//是否显示时间
					    isclear: true,				//是否显示清除
					    istoday: true,				//是否显示今天
					    issure: true,				//是否显示确定
					    festival: true,				//是否显示节日
					    choose: function(datas){
					    	payEnd.min = datas; 		//开始日选好后，重置结束日的最小日期
					    	payEnd.start = datas 		//将结束日的初始值设定为开始日
					    }
					};
				var payEnd = {
					    elem: '#payEndTime',
					    format: 'YYYY-MM-DD',
					    event: 'focus',
					    min: $("#payBegTime").val(),
					    istime: false,
					    isclear: true,
					    istoday: true,
					    issure: true,
					    festival: true,
					    choose: function(datas){
					    	payBeg.max = datas; //结束日选好后，重置开始日的最大日期
					    }
					};
					laydate(start);
					laydate(end);
					laydate(payBeg);
					laydate(payEnd);
	       
	    });
		
		function addVirtualOrder(){
			openDialog("新增"+'虚拟订单添加',"/kenuo/a/ec/orders/createOrder","900px", "650px","");
		}
		function addKindOrder(){
			openDialog("新增"+'实物订单&nbsp;&nbsp;&nbsp;&nbsp;<span style=\'color:red;\'>实物订单无法预约,如需创建服务项目,请添加虚拟订单</span>',"/kenuo/a/ec/orders/createKindOrder","900px", "650px","");
		}
		function addSuitCardOrder(){
			openDialog("新增"+'套卡订单添加',"/kenuo/a/ec/orders/createSuitCardOrder","900px", "650px","");
		}        
		function addCommonCardOrder(){
			openDialog("新增"+'通用卡订单添加',"/kenuo/a/ec/orders/createCommonCardOrder","900px", "650px","");
		}
		
		//退货列表111
		function returnedGoodsList(orderId,flag,isReal){
			 openDialog('退货商品列表','${ctx}/ec/orders/returnGoddsList?flag='+flag+'&orderid='+orderId+'&isReal='+isReal,'1000px','650px');
		}
		
		
</script>
</head>




<body class="gray-bg">
	<div class="wrapper wrapper-content">
		<div class="ibox">
			<div class="ibox-title">
				<h5>订单列表</h5>
			</div>
			 <sys:message content="${message}"/>
			 	<!-- 查询条件 -->
				<div class="ibox-content">
				<div class="clearfix">
				<form:form id="searchForm" modelAttribute="orders" action="${ctx}/ec/orders/list" method="post" class="form-inline">
					<input id="pageNo" name="pageNo" type="hidden" value="${page.pageNo}"/>
					<input id="pageSize" name="pageSize" type="hidden" value="${page.pageSize}"/>
					<table:sortColumn id="orderBy" name="orderBy" value="${page.orderBy}" callback="sortOrRefresh();"/><!-- 支持排序 -->
					<div class="form-group">
						<form:input path="username" htmlEscape="false" maxlength="50" class=" form-control input-sm" placeholder="用户名"/>&nbsp;&nbsp;&nbsp;&nbsp;
						<form:input path="mobile" htmlEscape="false" maxlength="50" class=" form-control input-sm" placeholder="手机号"/>&nbsp;&nbsp;&nbsp;&nbsp;
						<form:input path="orderid" htmlEscape="false" maxlength="50" class=" form-control input-sm" placeholder="订单号"/>&nbsp;&nbsp;&nbsp;&nbsp;
						<label>订单状态：</label>
							<form:select path="orderstatus"  class="form-control" style="width:185px;">
								<form:option value="0">全部</form:option>
								<form:option value="-2">取消订单</form:option>
								<form:option value="-1">待付款</form:option>
								<form:option value="1">待发货</form:option>
								<form:option value="2">待收货</form:option>
								<form:option value="3">已退款</form:option>
								<form:option value="4">已完成</form:option>
							</form:select>
						<%-- <label>是否有欠费：</label>
						<form:select path="orderArrearageType"  class="form-control" style="width:185px;">
								<form:option value="0">全部</form:option>
								<form:option value="1">无欠款</form:option>
								<form:option value="2">有欠款</form:option>
							</form:select> --%>
						<label>订单区分：</label>	
						<form:select path="searchIsReal"  class="form-control" style="width:185px;">
								<form:option value="">全部</form:option>
								<form:option value="0">实物</form:option>
								<form:option value="1">虚拟</form:option>
						</form:select>	
						<p></p>
						<label>订单类型：</label>	
						<form:select path="userDelFlag"  class="form-control" style="width:185px;">
								<form:option value="">全部</form:option>
								<form:option value="0">正常</form:option>
								<form:option value="1">用户删除</form:option>
						</form:select>	
						<label>创建类型：</label>
						<form:select path="channelFlag"  class="form-control" style="width:185px;">
								<form:option value="">全部</form:option>
								<form:option value="wap">wap端</form:option>
								<form:option value="ios">苹果手机</form:option>
								<form:option value="android">安卓手机</form:option>
								<form:option value="bm">后台管理</form:option>
						</form:select>
						<label>新老订单：</label>
						<form:select path="newIsNeworder"  class="form-control" style="width:185px;">
								<form:option value="">全部</form:option>
								<form:option value="0">新订单</form:option>
								<form:option value="1">老订单</form:option>
						</form:select>
						
						<label>商品分类：</label>
						<sys:treeselect id="cateid" name="cetaid" value="${orders.cetaid}" 
									labelName="catename" labelValue="${orders.catename}" 
						     		title="商品分类" url="/ec/goodscategory/treeData" cssClass="form-control required" allowClear="true" notAllowSelectParent="true"/>
						<label>商品名称：</label>
						<sys:treeselect id="goodsid" name="goodsids" value="${orders.goodsids}" 
									labelName="goodsname" labelValue="${orders.goodsname}" 
						     		title="商品名称" url="/ec/goods/treeAllData" cssClass="form-control required" allowClear="true" notAllowSelectParent="true"/>
						<p></p>
						<label>创建日期：</label>
						<input id="begtime" name="begtime" type="text" maxlength="20" class="laydate-icon form-control layer-date input-sm"
							value="<fmt:formatDate value="${orders.begtime}" pattern="yyyy-MM-dd"/>" style="width:185px;" placeholder="开始时间" readonly="readonly"/>
						一
						<input id="endtime" name="endtime" type="text" maxlength="20" class=" laydate-icon form-control layer-date input-sm" 
						value="<fmt:formatDate value="${orders.endtime}" pattern="yyyy-MM-dd"/>"  style="width:185px;" placeholder="结束时间" readonly="readonly"/>&nbsp;&nbsp;
						<label>支付时间：</label>
						<input id="payBegTime" name="payBegTime" type="text" maxlength="20" class="laydate-icon form-control layer-date input-sm"
							value="<fmt:formatDate value="${orders.payBegTime}" pattern="yyyy-MM-dd"/>" style="width:185px;" placeholder="开始时间" readonly="readonly"/>
						一
						<input id="payEndTime" name="payEndTime" type="text" maxlength="20" class=" laydate-icon form-control layer-date input-sm" 
						value="<fmt:formatDate value="${orders.payEndTime}" pattern="yyyy-MM-dd"/>"  style="width:185px;" placeholder="结束时间" readonly="readonly"/>&nbsp;&nbsp;
					 </div>	
				</form:form>
					<!-- 工具栏 -->
					<div class="row" style="padding-top:10px;">
					<div class="col-sm-12">
						<div class="pull-left">
							<shiro:hasPermission name="ec:orders:add">
								<button class="btn btn-white btn-sm" data-toggle="tooltip" data-placement="left" onclick="addVirtualOrder()" title="添加虚拟订单">
									<i class="fa fa-plus"></i>添加虚拟订单
								</button>
								<button class="btn btn-white btn-sm" data-toggle="tooltip" data-placement="left" onclick="addKindOrder()" title="添加实物订单">
									<i class="fa fa-plus"></i>添加实物订单<span style="color: red;font-weight:bold;">&nbsp;&nbsp;*&nbsp;实物订单无法预约！</span>
								</button>
								<button class="btn btn-white btn-sm" data-toggle="tooltip" data-placement="left" onclick="addSuitCardOrder()" title="添加套卡订单">
									<i class="fa fa-plus"></i>添加套卡订单
								</button>
								<button class="btn btn-white btn-sm" data-toggle="tooltip" data-placement="left" onclick="addCommonCardOrder()" title="添加通用卡订单">
									<i class="fa fa-plus"></i>添加通用卡订单
								</button>
							</shiro:hasPermission>
							<shiro:hasPermission name="ec:orders:export">
								<!-- 导出按钮 -->
								<table:exportExcel url="${ctx}/ec/orders/export"></table:exportExcel>
							</shiro:hasPermission>
							<shiro:hasPermission name="ec:orders:importPage">
								<!-- 导入数据 -->
								<a href="#" onclick="openDialog('导入物流数据', '${ctx}/ec/orders/importPage','400px', '220px')" class="btn btn-white btn-sm"><i class="fa fa-folder-open-o"></i>导入物流数据</a>
							</shiro:hasPermission>
							<shiro:hasPermission name="ec:orders:importVirtualOrdersPage">
								<!-- 导入虚拟订单数据 -->
								<a href="#" onclick="openDialog('导入虚拟订单订单', '${ctx}/ec/orders/importVirtualOrdersPage','400px', '220px')" class="btn btn-white btn-sm"><i class="fa fa-folder-open-o"></i>导入虚拟订单</a>
							</shiro:hasPermission> 
						</div>
						<div class="pull-right">
							<button  class="btn btn-primary btn-rounded btn-outline btn-sm " onclick="search()" ><i class="fa fa-search"></i> 查询</button>
							<button  class="btn btn-primary btn-rounded btn-outline btn-sm " onclick="reset()" ><i class="fa fa-refresh"></i> 重置</button>
						</div>
					</div>
				</div>
				</div>
				<p></p>
				<table id="contentTable" class="table table-striped table-bordered  table-hover table-condensed  dataTables-example dataTable no-footer">
					<thead>
						<tr>
							<th style="text-align: center;">订单号</th>
							<th style="text-align: center;">订单区分</th>
							<th style="text-align: center;">用户名</th>
							<th style="text-align: center;">订单状态</th>
							<th style="text-align: center;">取消类型</th>
							<!-- <th style="text-align: center;">订单欠款</th> -->
							<th style="text-align: center;">支付金额</th>
							<th style="text-align: center;">商品种类</th>
							<th style="text-align: center;">支付方式</th>
							<th style="text-align: center;">创建类型</th>
							<th style="text-align: center;">订单类型</th>
							<th style="text-align: center;">创建时间</th>
							<th style="text-align: center;">客户留言</th>
							<th style="text-align: center;">操作</th>
						</tr>
					</thead>
					<tbody style="text-align: center;">
					<c:forEach items="${page.list}" var="orders">
						<tr>
							<td>${orders.orderid}</td>
							<td>
								<c:if test="${orders.isReal==0}">实物订单</c:if>
								<c:if test="${orders.isReal==1}">虚拟订单</c:if>
								<input type="hidden" id="isReal" value="${orders.isReal==0}" />
							</td>
							<td>${orders.username}</td>		
							<td>	
								<c:if test="${orders.orderstatus==-2}">
									取消订单
								</c:if>
								<c:if test="${orders.orderstatus==-1}">
									待付款
								</c:if>
								<c:if test="${orders.orderstatus==1}">
									待发货
								</c:if>
								<c:if test="${orders.orderstatus==2}">
									待收货
								</c:if>
								<c:if test="${orders.orderstatus==3}">
									已退款
								</c:if>
								<c:if test="${orders.orderstatus==4}">
									已完成
								</c:if>
								<c:if test="${orders.orderstatus==5}">
									申请退款
								</c:if>
							</td>
							<td>	
								${orders.cancelType}
							</td>
							<%-- <td>${orders.orderArrearage}</td> --%>
							<td>${orders.orderamount}</td>
							<td>${orders.goodsnum}</td>
							<td>${orders.payname}</td>
							<td>
								<c:if test="${orders.channelFlag== 'wap'}">
									wap端
								</c:if>
								<c:if test="${orders.channelFlag== 'ios'}">
									苹果手机
								</c:if>
								<c:if test="${orders.channelFlag=='android'}">
									安卓手机
								</c:if>
								<c:if test="${orders.channelFlag=='bm'}">
									后台管理
								</c:if>
							</td>
							<td>
								<c:if test="${orders.delFlag == 0}">
									正常
								</c:if>
								<c:if test="${orders.delFlag == 1}">
									用户删除
								</c:if>
							</td>
							<td><fmt:formatDate value="${orders.addtime}" pattern="yyyy-MM-dd HH:mm:ss" /> </td>
							<td><div style="width:100px;white-space:nowrap; overflow:hidden; text-overflow:ellipsis;">${orders.userNote}</div></td>
							<td>
			 					<shiro:hasPermission name="ec:orders:view"> 
			 						<a href="#" onclick="openDialogView('查看订单', '${ctx}/ec/orders/orderform?orderid=${orders.orderid}&isReal=${orders.isReal}&type=view','1100px','650px')" class="btn btn-info btn-xs" ><i class="fa fa-search-plus"></i> 订单详情</a>
			 					</shiro:hasPermission>
								<shiro:hasPermission name="ec:orders:edit">
									<c:if test="${orders.channelFlag=='bm' || (orders.channelFlag != 'bm' && orders.isReal==1)}">
										<a href="#" onclick="openDialog('编辑订单', '${ctx}/ec/orders/orderform?orderid=${orders.orderid}&isReal=${orders.isReal}&type=edit','1100px','650px')"  class="btn btn-success btn-xs" ><i class="fa fa-edit"></i>修改</a>
									</c:if>
									<c:if test="${orders.channelFlag!='bm' && orders.isReal==0}">
										<c:if test="${orders.orderstatus==4 or orders.orderstatus==-2}">
											<a href="#" style="background:#C0C0C0;color:#FFF" class="btn  btn-xs" ><i class="fa fa-edit"></i>修改</a>
										</c:if>
										<c:if test="${orders.orderstatus!=4 and orders.orderstatus!=-2}">
											<a href="#" onclick="openDialog('编辑订单', '${ctx}/ec/orders/orderform?orderid=${orders.orderid}&isReal=${orders.isReal}&type=edit','1100px','650px')"  class="btn btn-success btn-xs" ><i class="fa fa-edit"></i>修改</a>
										</c:if>
									</c:if>
								</shiro:hasPermission>
								<shiro:hasPermission name="ec:orders:edit"> 
									<c:if test="${orders.orderstatus == -1}">
										<a href="#" style="background:#C0C0C0;color:#FFF" class="btn  btn-xs" ><i class="fa fa-edit"></i>物流</a>
									</c:if>
									<c:if test="${orders.orderstatus != -1}">
										<a href="#" onclick="openDialog('查看物流', '${ctx}/ec/orders/shipping?orderid=${orders.orderid}','600px','400px')" class="btn btn-info btn-xs" ><i class="fa fa-truck"></i> 物流</a>
									</c:if>
			 					</shiro:hasPermission>
			 				 	<shiro:hasPermission name="ec:orders:return">
									<c:if test="${orders.orderstatus==4 && orders.flag==1}">
										<a href="#" onclick="returnedGoodsList('${orders.orderid}','${orders.flag}','${orders.isReal}')"  class="btn btn-success btn-xs" ><i class="fa fa-edit"></i>售后服务</a>
									</c:if>
									<c:if test="${orders.orderstatus!=4 || orders.flag!=1}">
										<a href="#" style="background:#C0C0C0;color:#FFF" class="btn  btn-xs" ><i class="fa fa-edit"></i>售后服务</a>
									</c:if>
								</shiro:hasPermission>
								<%-- <c:if test="${orders.channelFlag=='bm' || (orders.channelFlag != 'bm' && orders.isReal==1)}">
									<a href="#" onclick="openDialogView('查看订单', '${ctx}/ec/orders/getOrderRechargeView?orderid=${orders.orderid}&orderType=order','800px','600px')" class="btn btn-info btn-xs" ><i class="fa fa-search-plus"></i>订单充值查看</a>
								</c:if>
								<c:if test="${orders.channelFlag!='bm' && orders.isReal==0}">
									<a href="#" style="background:#C0C0C0;color:#FFF" class="btn  btn-xs" ><i class="fa fa-search-plus"></i>订单充值查看</a>
								</c:if> --%>
								<c:if test="${orders.orderstatus == -1}">
									<a href="${ctx}/ec/orders/cancellationOrder?orderid=${orders.orderid}" onclick="return confirmx('确定取消订单吗？', this.href)" class="btn btn-danger btn-xs"><i class="fa fa-trash"></i>取消订单</a>
								</c:if>
								<c:if test="${orders.orderstatus != -1}">
									<a href="#" style="background:#C0C0C0;color:#FFF" class="btn  btn-xs" ><i class="fa fa-trash"></i>取消订单</a>
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