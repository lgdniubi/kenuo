<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/webpage/include/taglib.jsp"%>
<html>
<head>
	<title>会员管理</title>
	<meta name="decorator" content="default"/>
    <meta name="viewport" content="width=device-width, initial-scale=1.0, minimum-scale=1.0, maximum-scale=1.0,user-scalable=no">
		<link href="${ctxStatic}/layer-v2.0/layer/skin/layui.css" type="text/css" rel="stylesheet">
	
	<script type="text/javascript">
// 	    function edit(){
// 	    	$("userid").removeAttr("disabled"); 
// 	    	$("sex").attr("disabled",false);
// 	    	$("userid").removeAttr("disabled")
// 	    }
	    function save(){
	    	$("#inputForm").submit();
			return true;
	    }
	    $('#edit').click(function() {
			if($('#fieldset').is(':disabled')) {
				$('#fieldset').removeAttr('disabled');
			}
		});
	</script>
	<style type="text/css">
		.modal-content{
			margin:0 auto;
			width: 400px;
		}
	</style>
</head>
<body class="gray-bg">
	<div class="wrapper wrapper-content">
		<div class="ibox">
		<div class="ibox-title">
			<div class="text">
				<h5>首页&nbsp;&nbsp;</h5>
			</div>
			<div class="text active">
				<h5>&nbsp;&nbsp;客户管理&nbsp;&nbsp;</h5>
			</div>
			<div class="text">
				<h5>&nbsp;&nbsp;订单管理&nbsp;&nbsp;</h5>
			</div>
			<div class="text">
				<h5>&nbsp;&nbsp;综合报表&nbsp;&nbsp;</h5>
			</div>
		</div>
			<sys:message content="${message}"/>
			<!-- 查询条件 -->
	    	<div class="ibox-content">
				<div class="clearfix">
					<div class="nav" >
						<!-- 翻页隐藏文本框 -->
						<div class="text-danger" style="margin:8px">
									<p class="text-primary">
										<span >${userDetail.nickname}</span>的客户档案--请注意保密
									</p>
						</div>
				      	 <ul class="layui-tab-title">
				   
							<li role="presentation"><a
										href="${ctx}/crm/user/userDetail?userId=${userId}">基本资料</a></li>
							<li role="presentation"><a
										href="${ctx}/crm/physical/skin?userId=${userId}">身体状况</a></li>
							<li role="presentation" class="layui-this"><a
										href="${ctx}/crm/schedule/list?userId=${userId}">护理时间表</a></li>
							<li role="presentation"><a
										href="${ctx}/crm/orders/list?userId=${userId}">客户订单</a></li>
							<li role="presentation"><a
										href="${ctx}/crm/coustomerService/list?userId=${userId}">售后</a></li>
							<li role="presentation"><a
										href="${ctx}/crm/consign/list?userId=${userId}">物品寄存</a></li>
							<li role="presentation"><a
										href="${ctx}/crm/goodsUsage/list?userId=${userId}">产品使用记录</a></li>
							<li role="presentation"><a
										href="${ctx}/crm/user/account?userId=${userId}">账户总览</a></li>
							<li role="presentation"><a
										href="${ctx}/crm/invitation/list?userId=${userId}">邀请明细</a></li>
							<li role="presentation"><a href="#">投诉咨询</a></li>
						  </ul>
					</div>
					<!-- 工具栏 -->
				</div>
			</div>
			<sys:message content="${message}"/>
			<!-- 查询条件 -->
	    	<div class="ibox-content">
				<table id="contentTable" class="table table-striped table-bordered  table-hover table-condensed  dataTables-example dataTable no-footer">
					<thead>
						<tr>
<!-- 							 <th style="text-align: center;">编号</th> -->
						     <th style="text-align: center;">产品编号</th>
						     <th style="text-align: center;">美容师编号</th>
						     <th style="text-align: center;">产品名称</th>
						     <th style="text-align: center;">预约时间</th>
						     <th style="text-align: center;">预约状态</th>
						     <th style="text-align: center;">护理时间</th>
						     <th style="text-align: center;">美容师</th>
						     <th style="text-align: center;">星级</th>
						     <th style="text-align: center;">操作</th>
						</tr>
					</thead>
					<tbody style="text-align: center;">
						<c:forEach items="${page.list}" var="item">
							<tr>
								<td>${item.goods.goodsSn}</td>
							  	<td>${item.user.no}</td>
							  	<td>${item.goods.goodsName}</td>
							    <td><fmt:formatDate value="${item.apptDate}" pattern="yyyy-MM-dd "/></td>
							    <td>
								    <c:if test="${item.apptStatus=='0'}">等待服务</c:if>
								    <c:if test="${item.apptStatus=='1'}">已完成</c:if>
								    <c:if test="${item.apptStatus=='2'}">已评价</c:if>
								    <c:if test="${item.apptStatus=='3'}">已取消</c:if>
								    <c:if test="${item.apptStatus=='4'}">客户爽约</c:if>
							    </td>
							    <td>${item.apptStartTime}</td>
							  	<td>${item.user.name}</td>						
							    <td>${item.teachersStarLevel}</td>	
								<td>
									<a href="#"
									onclick="openDialog('用户评论', 
									'${ctx}/crm/schedule/comment?userId=${userId}&reservationId=${item.reservationId}','800px', '500px')"
									class="btn btn-success btn-xs"><i class="fa fa-search-plus"></i>评论</a>
<!-- 									<a href="#" -->
<%-- 									onclick="openDialog('美容师护理日志', '${ctx}/crm/schedual/record?beauticianId=${beauticianId}','800px', '500px')" --%>
<!-- 									class="btn btn-success btn-xs"><i class="fa fa-search-plus"></i>日志</a> -->
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
</body>
</html>