<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/webpage/include/taglib.jsp"%>

<html>
<head>
<title>业务员日志记录列表</title>
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
					<form id="searchForm"  modelAttribute="orderPushmoneyRecord" action="${ctx}/ec/orders/getOrderPushmoneyRecordList" method="post" class="navbar-form navbar-left searcharea">
						<!-- 翻页隐藏文本框 -->
						<input id="pageNo" name="pageNo" type="hidden" value="${page.pageNo}"/>
						<input id="pageSize" name="pageSize" type="hidden" value="${page.pageSize}"/>
						<input id="subId" type="hidden" name="subId" value="${subject.subId}">
						<input type="hidden" id="orderId" name="orderId" value="${orderPushmoneyRecord.orderId}" />
					</form>
				</div>
				<p></p>
				<table id="contentTable" class="table table-striped table-bordered  table-hover table-condensed  dataTables-example dataTable no-footer">
					<thead>
						<tr>
							<th style="text-align: center;">业务员</th>
							<th style="text-align: center;">手机号</th>
							<th style="text-align: center;">营业额</th>
							<th style="text-align: center;">操作人</th>
							<th style="text-align: center;">操作时间</th>
						</tr>
					</thead>
					<tbody style="text-align: center;">
						<c:forEach items="${page.list}" var="orderPushmoneyRecord">
							<tr>
								<td>
									<input id="sysName" name="sysName" type="text" value="${orderPushmoneyRecord.pushmoneyUserName }" class='form-control' readonly='readonly'>
								</td>
								<td>
									<input id="sysMobile" name="sysMobile" type="text" value="${orderPushmoneyRecord.pushmoneyUserMobile }" class='form-control' readonly='readonly'>
								</td>
								<td>
									${orderPushmoneyRecord.pushMoney }
								</td>
								<td>
									${orderPushmoneyRecord.createBy.name }
								</td>
								<td>
									<fmt:formatDate value="${orderPushmoneyRecord.createDate }" pattern="yyyy-MM-dd HH:mm:ss" />
								</td>
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