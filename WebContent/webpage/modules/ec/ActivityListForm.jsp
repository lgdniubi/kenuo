<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/webpage/include/taglib.jsp"%>

<html>
<head>
<title>创建活动</title>
<meta name="decorator" content="default" />
<!-- 内容上传 引用-->
<style type="text/css">
#one {
	width: 200px;
	height: 180px;
	float: left
}

#two {
	width: 50px;
	height: 180px;
	float: left
}

#three {
	width: 200px;
	height: 180px;
	float: left
}

.fabtn {
	width: 50px;
	height: 30px;
	margin-top: 10px;
	cursor: pointer;
}
</style>

</head>
<body>
	<div class="wrapper wrapper-content">
		<div class="ibox">
			<div class="ibox-content">
				<div class="clearfix">
					<form:form id="inputForm" modelAttribute="activity" action="${ctx}/ec/activity/save" method="post" class="form-horizontal">
						<table class="table table-bordered  table-condensed dataTables-example dataTable no-footer">
							<tr>
								<td><label class="pull-right" ><font color="red">*</font>活动名称：</label></td>
								<td>
									<form:input path="name" htmlEscape="false" maxlength="200" style="width:200px;" class="form-control required" />
								</td>
							</tr>
							<tr>
								<td><label class="pull-right" ><font color="red">*</font>所属商家：</label></td>
								<td>
									<form:select path="franchiseeId" class="form-control" style="width:200px;">
										<form:option value="0">所属商家</form:option>
										<c:forEach items="${franList}" var="list" varStatus="status">
												<form:option value="${list.id}">${list.name}</form:option>
										</c:forEach>
									</form:select>
								</td>
							</tr>
							<tr>
								<td><label class="pull-right"><font color="red">*</font>活动类型：</label></td>
								<td><form:select path="actionType"  class="form-control" style="width:200px;" >
										<form:option value="1">营销红包</form:option>
										<form:option value="2">生日红包</form:option>
										<form:option value="3">内部红包</form:option>
										<form:option value="4">团购红包</form:option>
										<form:option value="5">其它</form:option>
									</form:select></td>
							</tr>
							<tr>
								<td><label class="pull-right"><font color="red">*</font>领取时间：</label></td>
								<td><input id="startTime" name="startTime" type="text" maxlength="20" class="laydate-icon form-control layer-date input-sm required"
									value="<fmt:formatDate value="${activity.startTime}" pattern="yyyy-MM-dd HH:mm:ss"/>" style="width: 200px;" placeholder="领取时间" readonly="readonly" /> </td>
							</tr>
							<tr>
								<td><label class="pull-right"><font color="red">*</font>截止时间：</label></td>
								<td>
									<input id="endTime" name="endTime" type="text" maxlength="20" class=" laydate-icon form-control layer-date input-sm required"
							value="<fmt:formatDate value="${activity.endTime}" pattern="yyyy-MM-dd HH:mm:ss"/>" style="width: 200px;" placeholder="截止时间" readonly="readonly" />
								</td>
							</tr>
							<tr>
								<td><label class="pull-right"><font color="red">*</font>有效期：</label></td>
								<td>
									<c:if test="${activity.expirationType == '0'}">
										<input id="expirationDate" name="expirationDate" type="text" maxlength="20" class="laydate-icon form-control layer-date input-sm required"
										value="<fmt:formatDate value="${activity.expirationDate}" pattern="yyyy-MM-dd HH:mm:ss"/>" style="width: 200px;" placeholder="有效期" readonly="readonly" />
									</c:if>
									<c:if test="${activity.expirationType == '1'}">
										<form:input path="expirationDay" htmlEscape="false" style="width:190px;" class="form-control required" placeholder="领取后到期天数"/>天
									</c:if>
								</td>
							</tr>
						</table>
					
					</form:form>
				</div>
				
			</div>
			<table id="contentTable" class="table table-striped table-bordered  table-hover table-condensed  dataTables-example dataTable no-footer">
					<thead>
						<tr>
							<th style="text-align: center;">ID</th>
							<th style="text-align: center;">红包名称</th>
							<th style="text-align: center;">红包类型</th>
							<th style="text-align: center;">满减金额</th>
							<th style="text-align: center;">红包金额</th>
							<th style="text-align: center;">红包数量</th>
							<th style="text-align: center;">剩余数量</th>
							<th style="text-align: center;">使用范围</th>
							<th style="text-align: center;">领取上限</th>
							<th style="text-align: center;">创建时间</th>
							<th style="text-align: center;">创建者</th>
							<th style="text-align: center;">状态</th>
							<th style="text-align: center;">操作</th>
						</tr>
					</thead>
					<tbody style="text-align: center;">
						<c:forEach items="${list}" var="list">
							<tr>
								<td>${list.id}</td>
								<td>${list.couponName}</td>
								<c:if test="${list.couponType==1}">
									<td>商品详情页</td>
								</c:if>
								<c:if test="${list.couponType==2}">
									<td>活动页</td>
								</c:if>
								<c:if test="${list.couponType==3}">
									<td>新注册</td>
								</c:if>
								<c:if test="${list.couponType==4}">
									<td>手工红包</td>
								</c:if>
								<td>${list.baseAmount}</td>
								<td>${list.couponMoney}</td>
								<td>${list.totalNumber}</td>
								<td>${list.couponNumber}</td>
								<c:if test="${list.usedType==1}">
									<td>全部商品</td>
								</c:if>
								<c:if test="${list.usedType==2}">
									<td>指定分类</td>
								</c:if>
								<c:if test="${list.usedType==3}">
									<td>指定商品</td>
								</c:if>
								<td>${list.ceiling}</td>
							 	<td><fmt:formatDate value="${list.createDate}" pattern="yyyy-MM-dd HH:mm:ss" /></td>
								<td>${list.createBy.name}</td>
								<c:if test="${list.status==1}">
									<td>发放</td>
								</c:if>
								<c:if test="${list.status==2}">
									<td>关闭</td>
								</c:if>	
								<td>
								<a href="#" onclick="openDialogView('信息列表', '${ctx}/ec/activity/CateGoodsList?id=${list.id}&usedType=${list.usedType}','300px','400px')"
												class="btn btn-info btn-xs"><i class="fa fa-search-plus"></i>查看</a>
								</td>
							</tr>
						</c:forEach>
					</tbody>
				</table>
		</div>
	</div>
	 <div class="loading"></div>
</body>
</html>