<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/webpage/include/taglib.jsp"%>

<html>
<head>
<title>红包活动列表</title>
<meta name="decorator" content="default" />

<script type="text/javascript">
	function page(n, s) {
		$("#pageNo").val(n);
		$("#pageSize").val(s);
		$("#searchForm").submit();
		return false;
	}
	
	function editCoupon(num,couponId){
		if(num>0){
			top.layer.alert('该红包已经有人领取，不可以修改!', {icon: 0, title:'提醒'}); 
		}else{
			openDialog('编辑红包', '${ctx}/ec/coupon/form?couponId='+couponId,'900px','600px');
		}
		
		
	}
	
	
</script>
</head>




<body class="gray-bg">
	<div class="wrapper wrapper-content">
		<div class="ibox">
			<div class="ibox-title">
				<h5>红包活动列表</h5>
			</div>
			<sys:message content="${message}" />
			<!-- 查询条件 -->
			<div class="ibox-content">
				<div class="clearfix">
					<form:form id="searchForm" modelAttribute="coupon"
						action="${ctx}/ec/coupon/list" method="post" class="form-inline">
						<input id="pageNo" name="pageNo" type="hidden" value="${page.pageNo}"/>
						<input id="pageSize" name="pageSize" type="hidden" value="${page.pageSize}"/>
					</form:form>
					<!-- 工具栏 -->
					<div class="row" style="padding-top: 10px;">
						<div class="col-sm-12">
							<div class="pull-left">
								<shiro:hasPermission name="ec:coupon:add">
									<%-- <table:addRow url="${ctx}/ec/orders/createOrder" title="订单添加" width="750px" height="650px" title="创建红包"></table:addRow><!-- 增加按钮 --> --%>
									<a href="#" onclick="openDialog('创建红包活动', '${ctx}/ec/coupon/form','900px','600px')" class="btn btn-white btn-sm"><i class="fa fa-plus"></i>创建红包活动</a>
								</shiro:hasPermission>
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
							<th style="text-align: center;">名称</th>
							<th style="text-align: center;">所属商家</th>
							<th style="text-align: center;">类型</th>
							<th style="text-align: center;">创建人</th>
							<th style="text-align: center;">发放时间</th>
							<th style="text-align: center;">使用范围</th>
							<th style="text-align: center;">有效期</th>
							<th style="text-align: center;">红包状态</th>
							<th style="text-align: center;">操作</th>
						</tr>
					</thead>
					<tbody style="text-align: center;">
						<c:forEach items="${page.list}" var="coupon">
							<tr>
								<td>${coupon.couponId}</td>
								<td>${coupon.couponName}</td>
								<td>${coupon.franchiseeName}</td>
								<c:if test="${coupon.couponType==1}">
									<td>商品页领取</td>
								</c:if>
								<c:if test="${coupon.couponType==2}">
									<td>活动页领取</td>
								</c:if>
								<c:if test="${coupon.couponType==3}">
									<td>新注册</td>
								</c:if>
								<c:if test="${coupon.couponType==4}">
									<td>内部红包</td>
								</c:if>
								<td>${coupon.createBy.name}</td>
								<td><fmt:formatDate value="${coupon.getBegintime}"
										pattern="yyyy-MM-dd HH:mm:ss" /> — <fmt:formatDate
										value="${coupon.getEndtime}" pattern="yyyy-MM-dd HH:mm:ss" /></td>

								<c:if test="${coupon.goodsUseType==1}">
									<td>全部商品</td>
								</c:if>
								<c:if test="${coupon.goodsUseType==2}">
									<td>指定分类</td>
								</c:if>
								<c:if test="${coupon.goodsUseType==3}">
									<td>指定商品</td>
								</c:if>
								<td><fmt:formatDate value="${coupon.expirationDate}"
										pattern="yyyy-MM-dd HH:mm:ss" /></td>
								<c:if test="${coupon.status==0}">
									<td>发放中</td>
								</c:if>
								<c:if test="${coupon.status==1}">
									<td>已关闭</td>
								</c:if>
								<c:if test="${coupon.status==2}">
									<td>已过期</td>
								</c:if>
								<td>
									 <shiro:hasPermission name="ec:coupon:edit">
										<c:if test="${coupon.status==0}">
											<a href="${ctx}/ec/coupon/couponStatus?status=3&couponId=${coupon.couponId}" onclick="return confirmx('确定关闭正在发放中的红包吗？', this.href)" class="btn btn-danger btn-xs">
											<i class="fa fa-close"></i>关闭</a>
										</c:if>
										<c:if test="${coupon.status==1}">
											<a href="#" onclick="openDialog('开启红包', '${ctx}/ec/coupon/timeform?couponId=${coupon.couponId}','600px','400px')"
												class="btn btn-primary btn-xs"><i class="fa fa-file"></i>开启</a>
										</c:if>
										<c:if test="${coupon.status==2}">
											<a href="#" style="background: #C0C0C0; color: #FFF"
												class="btn  btn-xs"><i class="fa fa-file"></i>开启</a>
										</c:if>
									</shiro:hasPermission>
									<shiro:hasPermission name="ec:coupon:view">
										<a href="#" onclick="openDialogView('查看红包', '${ctx}/ec/coupon/form?couponId=${coupon.couponId}','900px','600px')"
											class="btn btn-info btn-xs"><i class="fa fa-search-plus"></i>查看</a>
									</shiro:hasPermission> 
									<shiro:hasPermission name="ec:coupon:edit">
										<c:if test="${coupon.status==1}">
											<a href="#" onclick="editCoupon(${coupon.usedNum},${coupon.couponId})"
												class="btn btn-success btn-xs"><i class="fa fa-edit"></i>修改</a>
										</c:if>
										<c:if test="${coupon.status==0}">
											<a href="#" style="background: #C0C0C0; color: #FFF"
												class="btn  btn-xs"><i class="fa fa-edit"></i>修改</a>
										</c:if>
										<c:if test="${coupon.status==2}">
											<a href="#" style="background: #C0C0C0; color: #FFF"
												class="btn  btn-xs"><i class="fa fa-edit"></i>修改</a>
										</c:if>
									</shiro:hasPermission>
									<shiro:hasPermission name="ec:coupon:edit">
										<c:if test="${coupon.status==1}">
											<a href="#" onclick="openDialogView('面值列表', '${ctx}/ec/coupon/addCoupon?couponId=${coupon.couponId}','900px','500px')"
												class="btn btn-success btn-xs"><i class="fa fa-plus"></i>面值列表</a>
										</c:if>
										<c:if test="${coupon.status==0}">
											<a href="#" style="background: #C0C0C0; color: #FFF"
												class="btn  btn-xs"><i class="fa fa-plus"></i>面值列表</a>
										</c:if>
										<c:if test="${coupon.status==2}">
											<a href="#" style="background: #C0C0C0; color: #FFF"
												class="btn  btn-xs"><i class="fa fa-plus"></i>面值列表</a>
										</c:if>
										<a href="#" onclick="openDialogView('查看面值列表','${ctx}/ec/coupon/couponAmountList?couponId=${coupon.couponId}','900px','500px')"
												class="btn btn-info btn-xs"><i class="fa fa-search-plus"></i>查看面值</a>
									</shiro:hasPermission>
									<shiro:hasPermission name="ec:coupon:edit">
										<c:if test="${coupon.couponType==4}">
											<c:if test="${coupon.status==1}">	
												<a href="#" style="background: #C0C0C0; color: #FFF" class="btn  btn-xs"><i class="fa fa-plus"></i>发放红包</a>
											</c:if>
											<c:if test="${coupon.status==0}">
												<a href="#" onclick="openDialog('发放红包', '${ctx}/ec/coupon/sendform?couponId=${coupon.couponId}','800px','600px')"
														class="btn btn-success btn-xs"><i class="fa fa-file"></i>发放红包</a>
											</c:if>
											<c:if test="${coupon.status==2}">
												<a href="#" style="background: #C0C0C0; color: #FFF" class="btn  btn-xs"><i class="fa fa-plus"></i>发放红包</a>
											</c:if>
										</c:if>
										<c:if test="${coupon.couponType!=4}">
											<a href="#" style="background: #C0C0C0; color: #FFF" class="btn  btn-xs"><i class="fa fa-plus"></i>发放红包</a>
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