<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/webpage/include/taglib.jsp"%>
<html>
<head>
	<title>异常用户详情</title>
	<meta name="decorator" content="default"/>
    <meta name="viewport" content="width=device-width, initial-scale=1.0, minimum-scale=1.0, maximum-scale=1.0,user-scalable=no">
</head>
<body class="gray-bg">
	<div class="wrapper wrapper-content">
		<div class="ibox">
			<div class="ibox-title">
				<h5>异常用户详情</h5>
			</div>
			<sys:message content="${message}"/>
			<!-- 查询条件 -->
	    	<div class="ibox-content">
				<div class="clearfix">
					<form id="searchForm" modelAttribute="" action="${ctx}/ec/mtmyuser/finduserexception" method="post" class="navbar-form navbar-left searcharea">
						<div class="form-group">
							<input id="mobile" name="mobile" type="text" value="${mobile }" class="form-control" placeholder="手机号码" onkeyup="this.value=this.value.replace(/\D/g,'')"> 
						</div>	
						<div class="pull-right">
							<button  class="btn btn-primary btn-rounded btn-outline btn-sm " onclick="search()" ><i class="fa fa-search"></i> 查询</button>
						 </div>
					</form>
					<!-- 工具栏 -->
					<div class="row" style="padding-bottom: 5px;">
						<div class="col-sm-12">
							<div class="pull-left">
							</div>
							<div class="pull-right">
							 </div>
						</div>
					</div>
				</div>
				
				<!-- 妃子校用户 -->
				<c:if test="${null != train_user }">
					妃子校用户信息：
					<table id="contentTable" class="table table-striped table-bordered  table-hover table-condensed  dataTables-example dataTable no-footer">
						<thead>
							<tr>
								 <th style="text-align: center;">妃子校关联ID</th>
								 <th style="text-align: center;">姓名</th>
								 <th style="text-align: center;">手机号码</th>
								 <th style="text-align: center;">注册时间</th>
							</tr>
						</thead>
						<tbody style="text-align: center;">
							<tr>
								<td>${train_user.userid }</td>
								<td>${train_user.name }</td>
								<td>${train_user.mobile }</td>
								<td><fmt:formatDate value="${train_user.regtime}" pattern="yyyy-MM-dd HH:mm:ss" /></td>
							</tr>
						</tbody>
					</table>
				</c:if>
				
				<!-- mtmy用户 -->
				<c:if test="${null != mtmy_user }">
					每天美耶用户信息：
					<table id="contentTable" class="table table-striped table-bordered  table-hover table-condensed  dataTables-example dataTable no-footer">
						<thead>
							<tr>
								 <th style="text-align: center;">妃子校关联ID</th>
								 <th style="text-align: center;">姓名</th>
								 <th style="text-align: center;">手机号码</th>
								 <th style="text-align: center;">身份</th>
								 <th style="text-align: center;">注册时间</th>
							</tr>
						</thead>
						<tbody style="text-align: center;">
							<tr>
								<td>${mtmy_user.userid }</td>
								<td>${mtmy_user.name }</td>
								<td>${mtmy_user.mobile }</td>
								<td>${mtmy_user.layer }</td>
								<td><fmt:formatDate value="${mtmy_user.regtime}" pattern="yyyy-MM-dd HH:mm:ss" /></td>
							</tr>
						</tbody>
					</table>
				</c:if>
				
				<!-- mtmy用户订单列表 -->
				<c:if test="${null != mtmyOrders }">
					每天美耶用户订单列表：
					<table id="contentTable" class="table table-striped table-bordered  table-hover table-condensed  dataTables-example dataTable no-footer">
						<thead>
							<tr>
								 <th style="text-align: center;">订单号</th>
							     <th style="text-align: center;">用户ID</th>
							     <th style="text-align: center;">订单状态</th>
							     <th style="text-align: center;">支付金额</th>
							     <th style="text-align: center;">创建时间</th>
							</tr>
						</thead>
						<tbody style="text-align: center;">
							<c:forEach items="${mtmyOrders}" var="orders">
								<tr>
									<td>${orders.orderid}</td>
									<td>${orders.userid}</td>	
									<td>${fns:getDictLabel(orders.orderstatus, 'order_status', '')}</td>
									<td>${orders.orderamount}</td>
									<td><fmt:formatDate value="${orders.addtime}" pattern="yyyy-MM-dd HH:mm:ss" /></td>
								</tr>
							</c:forEach>
						</tbody>
					</table>
				</c:if>
				
				当条件满足：非妃子校用户+（身份为（Z+B）+无成功订单时，方可处理用户。<br>
				处理方案：<br>
				1：非妃子校用户+Z身份+无成功订单，做物理删除每天美耶用户。<br>
				2：非妃子校用户+B身份+无成功订单，先做删除关系操作（关系、邀请码），再物理删除每天美耶用户。<br>
				
				<c:if test="${null == train_user }">
					<c:if test="${null != mtmy_user }">
						<c:if test="${null != mtmy_user && (mtmy_user.layer == 'Z' || mtmy_user.layer == 'B')}">
							<c:if test="${null == mtmyOrders }">
								<!-- 展示处理按钮 -->
								<a href="${ctx}/ec/mtmyuser/userexceptionhandling?mobile=${mtmy_user.mobile}" style="padding-left: 30%;">
									<button class="btn btn-white btn-sm " data-toggle="tooltip" data-placement="left" >
										<i class="fa fa-plus"></i>处理用户
									</button>
								</a>
							</c:if>
						</c:if>
					</c:if>
				</c:if>
			</div>
			
		</div>
	</div>
	<div class="loading"></div>
</body>
</html>