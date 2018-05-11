<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/webpage/include/taglib.jsp"%>
<html>
<head>
<title>会员管理</title>
<link href="${ctxStatic}/layer-v2.0/layer/skin/layui.css" type="text/css" rel="stylesheet">
<meta name="decorator" content="default" />
<meta name="viewport" content="width=device-width, initial-scale=1.0, minimum-scale=1.0, maximum-scale=1.0,user-scalable=no">
<script type="text/javascript">
	$(document).ready(function(){
		$("#enableEdit").click(function(){
			if($("#fieldset").attr("disabled")) {
				$("#fieldset").removeAttr("disabled");
			}
		})
		$('#edit').click(function(){
			if($("#fieldset").attr("disabled")) {
				alert("请先填入数据");
			}else{
				$("#inputForm").submit();
				return true;
			}
		})
	});
</script>
</head>
<body class="gray-bg">
	<div class="wrapper wrapper-content">
		<div class="ibox">
			<sys:message content="${message}" />
			<!-- 查询条件 -->
			<div class="ibox-content">
				<div class="clearfix">
					<div class="nav">
						<!-- 翻页隐藏文本框 -->
						<div class="text-danger" style="margin:8px">
							<p class="text-primary">
								<span >${userDetail.nickname}</span>的客户档案--请注意保密
							</p>
						</div>
						<div class="layui-tab">
							<ul class="layui-tab-title">
								<li role="presentation">
									<a href="${ctx}/crm/user/userDetail?userId=${userId}&franchiseeId=${franchiseeId}">基本资料</a>
								</li>
								<li role="presentation" class="layui-this">
									<a href="${ctx}/crm/physical/skin?userId=${userId}&franchiseeId=${franchiseeId}">身体状况</a>
								</li>
								<li role="presentation">
									<a href="${ctx}/crm/schedule/list?userId=${userId}&franchiseeId=${franchiseeId}">护理时间表</a>
								</li>
								<li role="presentation">
									<a href="${ctx}/crm/orders/list?userId=${userId}&franchiseeId=${franchiseeId}">客户订单</a>
								</li>
								<li role="presentation">
									<a href="${ctx}/crm/coustomerService/list?userId=${userId}&franchiseeId=${franchiseeId}">售后</a>
								</li>
								<li role="presentation">
									<a href="${ctx}/crm/consign/list?userId=${userId}&franchiseeId=${franchiseeId}">物品寄存</a>
								</li>
								<li role="presentation">
									<a href="${ctx}/crm/goodsUsage/list?userId=${userId}&franchiseeId=${franchiseeId}">产品使用记录</a>
								</li>
								<li role="presentation">
									<a href="${ctx}/crm/user/account?userId=${userId}&franchiseeId=${franchiseeId}">账户总览</a>
								</li>
								<li role="presentation">
									<a href="${ctx}/crm/invitation/list?userId=${userId}&franchiseeId=${franchiseeId}">邀请明细</a>
								</li>
								<li role="presentation">
									<shiro:hasPermission name="crm:store:list">	
										<a href="${ctx}/crm/store/questionCrmList?mobile=${userDetail.mobile}&userId=${userId}&franchiseeId=${franchiseeId}&stamp=1">投诉咨询</a>
									</shiro:hasPermission>
							 	</li>
							</ul>
						</div>
					</div>
					<!-- 工具栏 -->
				</div>
			</div>
			<div class="ibox-content">
				<div class="ibox-title">
					<div class="layui-tab layui-tab-brief">
						<ul class="layui-tab-title">
							<li role="presentation" class="layui-this">
								<a href="${ctx}/crm/physical/skin?userId=${userId}&franchiseeId=${franchiseeId}">皮肤档案</a>
						 	</li>
							<li role="presentation" >
								<a href="${ctx}/crm/physical/shape?userId=${userId}&franchiseeId=${franchiseeId}">形体档案</a>
							</li>
						</ul>
					</div>
				</div>
			</div>
			<div class="ibox-content">
				<div class="clearfix">
					<form action="${ctx}/crm/physical/saveSkinFile" method="post" id="inputForm">
						<fieldset id="fieldset" disabled="disabled">
							<div class="row">
								<div class="col-xs-12 col-md-8">
									<input type="hidden" value="${userDetail.userId}" name="userId">
									<input type="hidden" value="${franchiseeId}" name="franchiseeId">
									<c:forEach items="${dictList}" var="row">
										<c:if test="${row.actionType=='1'}">  
											<div class="row" style="margin:40px">
											<span>
												${row.description}:
											</span>	
											<c:forEach items="${row.crmDictList}" var="item">	
												<label class="radio-inline"><span style="float:right;text-align:center;">${item.label}</span>
													<input type="radio" name="${item.type}" style="float:left;text-align:center;"
													<c:if test="${fn:contains(row.multiValue,item.value)}">checked="checked"</c:if>
													value="${item.value}#${item.label}">
												</label>
											</c:forEach> 
											</div>
										 </c:if>
									</c:forEach>
									<c:forEach items="${dictList}" var="row">
										<c:if test="${row.actionType=='2'}">  
											<div class="row" style="margin:40px">
											<span>
												${row.description}:
											</span>	
											<c:forEach items="${row.crmDictList}" var="item">
													<label class="radio-inline"> <input type="checkbox"
														name="${item.type}"
														<c:if test="${ fn:contains(row.multiValue,item.value)}">checked="checked"</c:if>
														value="${item.value}#${item.label}"> ${item.label}
													</label>
											 </c:forEach> 
											</div>
										 </c:if>
									</c:forEach>
									<div class="" style="margin:40px;">
										<span style="align:center;float:left">其他问题:</span>	
										<textarea style="width:500px;height:200px;resize:none; rows:50;cols:400;margin-left:40px" 
										name="remark" class="form-control">${skinFile.remark}</textarea>		
									</div>
								</div>
							</div>
					</fieldset>
					</form>
					<div class="row">
					<shiro:hasPermission name="crm:userInfo:edit">
						 <div class="col-md-2">
							<button style="float:right" class="btn btn-success" id="enableEdit">点此编辑</button>
						</div>
						<div class="col-md-2">
							<button type="submit" class="btn btn-info" id="edit">保存</button>
						</div>
					</shiro:hasPermission>
					</div>
				</div>
			</div>
		</div>
	</div>
</body>
</html>