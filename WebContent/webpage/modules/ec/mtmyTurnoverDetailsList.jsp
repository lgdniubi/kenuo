<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/webpage/include/taglib.jsp"%>

<html>
<head>
<title>店铺营业额记录列表</title>
<meta name="decorator" content="default" />
<!-- 内容上传 引用-->


<script type="text/javascript">
	var validateForm;
	
	function doSubmit() {//回调函数，在编辑和保存动作时，供openDialog调用提交表单。
		if (validateForm.form()) {
			
			$("#inputForm").submit();
			return true;
		}
		return false;
	}
</script>
</head>
<body>
	<div class="wrapper wrapper-content">
		<div class="ibox">
			<div class="ibox-content">
				<div class="clearfix">
					<form:form id="searchForm"  modelAttribute="turnOverDetails" action="${ctx}/ec/returned/findMtmyTurnoverDetailsList" method="post" class="navbar-form navbar-left searcharea">
						<!-- 翻页隐藏文本框 -->
						<input id="pageNo" name="pageNo" type="hidden" value="${page.pageNo}"/>
						<input id="pageSize" name="pageSize" type="hidden" value="${page.pageSize}"/>
						<input type="hidden" id="orderId" name="orderId" value="${turnOverDetails.orderId}" />
						<input type="hidden" id="detailsId" name="detailsId" value="${turnOverDetails.detailsId}" />
					</form:form>
				</div>
				<p></p>
				<table id="contentTable" class="table table-striped table-bordered  table-hover table-condensed  dataTables-example dataTable no-footer">
					<thead>
						<tr>
							<th style="text-align: center;">订单ID</th>
							<th style="text-align: center;">类型</th>
							<th style="text-align: center;">金额</th>
							<th style="text-align: center;">使用账户余额</th>
							<th style="text-align: center;">状态</th>
							<th style="text-align: center;">消费者</th>
							<th style="text-align: center;">消费者绑定店铺</th>
							<th style="text-align: center;">绑定技师</th>
							<th style="text-align: center;">创建者</th>
							<th style="text-align: center;">创建时间</th>
						</tr>
					</thead>
					<tbody style="text-align: center;">
						<c:forEach items="${page.list}" var="turnOverDetails">
							<tr>
								<td>${turnOverDetails.orderId}</td>
								<td>
									<c:if test="${turnOverDetails.type == 1}">
										下单
									</c:if>
									<c:if test="${turnOverDetails.type == 2}">
										充值/还欠款
									</c:if>
									<c:if test="${turnOverDetails.type == 3}">
										退款
									</c:if>
									<c:if test="${turnOverDetails.type == 4}">
										账户充值
									</c:if>
								</td>
								<td>${turnOverDetails.amount}</td>
								<td>${turnOverDetails.useBalance}</td>
								<td>
									<c:if test="${turnOverDetails.status == 0}">
										正常下单
									</c:if>
									<c:if test="${turnOverDetails.status == 1}">
										预约金
									</c:if>
									<c:if test="${turnOverDetails.status == 2}">
										处理预约金
									</c:if>
									<c:if test="${turnOverDetails.status == 3}">
										充值
									</c:if>
									<c:if test="${turnOverDetails.status == 4}">
										退款
									</c:if>
								</td>
								<td>${turnOverDetails.userName}</td>
								<td>${turnOverDetails.officeName}</td>
								<td>${turnOverDetails.beauticianName}</td>
								<td>${turnOverDetails.createName}</td>
								<td><fmt:formatDate value="${turnOverDetails.createDate }" pattern="yyyy-MM-dd HH:mm:ss" /></td>
							</tr>
						</c:forEach>
					</tbody>
					<tfoot>
						<tr>
	                    	<td colspan="20">
	                        	<!-- 分页代码 --> 
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