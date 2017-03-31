<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/webpage/include/taglib.jsp"%>
<html>
<head>
<title>会员管理</title>
<meta name="decorator" content="default" />
<meta name="viewport"
	content="width=device-width, initial-scale=1.0, minimum-scale=1.0, maximum-scale=1.0,user-scalable=no">
<link href="${ctxStatic}/layer-v2.0/layer/skin/layui.css"
	type="text/css" rel="stylesheet">

<script>
	$(document).ready(function(){
		$("#enableEdit").click(function(){
			if($("#fieldset").attr("disabled")) {
				$("#fieldset").removeAttr("disabled");
			}
		});
	})
	function save() {
		//loading("正在提交，请稍候...");
		$("#inputForm").submit();
		return true;
	}
</script>
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
						<ul class="layui-tab-title">
							<li role="presentation"><a
								href="${ctx}/crm/user/userDetail?userId=${userId}">基本资料</a></li>
							<li role="presentation" class="layui-this"><a
								href="${ctx}/crm/physical/skin?userId=${userId}">身体状况</a></li>
							<li role="presentation"><a
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
							<li role="presentation"><a 
								href="${ctx}/crm/store/list?mobile=${userDetail.mobile}&stamp=1">投诉咨询</a></li>
						</ul>
					</div>
					<!-- 工具栏 -->
				</div>
			</div>

			<div class="ibox-content">
				<div class="ibox-title">
					<div class="layui-tab layui-tab-brief">
							<ul class="layui-tab-title">
								<li role="presentation" ><a
									href="${ctx}/crm/physical/skin?userId=${userId}">皮肤档案</a></li>
								<li role="presentation" class="layui-this"><a
									href="${ctx}/crm/physical/shape?userId=${userId}">形体档案</a></li>
							</ul>
					</div>
				</div>
			</div>
			<div class="ibox-content">
				<div class="clearfix">
					<form id="inputForm" action="${ctx}/crm/physical/saveShapeFile"
						method="post">
						<input value="${userId}" name="userId" type="hidden">
						<fieldset id="fieldset" disabled="disabled">
							<div class="row" style="text-align: center; margin: 10px">
								<div class="col-sm-2 col-offset-sm-1">
									<label style="float: left; line-height: 30px"><font
										color="red"> </font>胸高标准:</label> <input name="breastHeightStandard"
										value="${shapeFile.breastHeightStandard}" maxlength="50"
										style="width: 150px; float: right" class="form-control">
								</div>
								<div class="col-sm-2 col-offset-sm-1">
									<label style="float: left; line-height: 30px"><font
										color="red"> </font>上胸围:</label> <input name="upperBust"
										value="${shapeFile.upperBust}" maxlength="50"
										style="width: 150px; float: right" class="form-control">
								</div>
								<div class="col-sm-2 col-offset-sm-1">
									<label style="float: left; line-height: 30px"><font
										color="red"> </font>腹围:</label> <input name="abdomen"
										value="${shapeFile.abdomen}" maxlength="50"
										style="width: 150px; float: right" class="form-control">
								</div>
								<div class="col-sm-2 col-offset-sm-1">
									<label style="float: left; line-height: 30px"><font
										color="red"> </font>臀围:</label> <input name="hip"
										value="${shapeFile.hip}" maxlength="50"
										style="width: 150px; float: right" class="form-control">
								</div>
							</div>

							<div class="row" style="text-align: center; margin: 10px">
								<div class="col-sm-2 col-offset-sm-1">
									<label style="float: left; line-height: 30px"><font
										color="red"> </font>左BP:</label> <input name="leftBp"
										value="${shapeFile.leftBp}" maxlength="50"
										style="width: 150px; float: right" class="form-control">
								</div>
								<div class="col-sm-2 col-offset-sm-1">
									<label style="float: left; line-height: 30px"><font
										color="red"> </font>下胸围:</label> <input name="lowerBust"
										value="${shapeFile.lowerBust}" maxlength="50"
										style="width: 150px; float: right" class="form-control">
								</div>
								<div class="col-sm-2 col-offset-sm-1">
									<label style="float: left; line-height: 30px"><font
										color="red"> </font>胸围:</label> <input name="bust"
										value="${shapeFile.bust}" maxlength="50"
										style="width: 150px; float: right" class="form-control">
								</div>
								<div class="col-sm-2 col-offset-sm-1">
									<label style="float: left; line-height: 30px"><font
										color="red"> </font>腰围:</label> <input name="waist"
										value="${shapeFile.waist}" maxlength="50"
										style="width: 150px; float: right" class="form-control">
								</div>
							</div>

							<div class="row" style="text-align: center; margin: 10px">
								<div class="col-sm-2 col-offset-sm-1">
									<label style="float: left; line-height: 30px"><font
										color="red"> </font>右BP:</label> <input name="rightBp"
										value="${shapeFile.rightBp}" maxlength="50"
										style="width: 150px; float: right"  class="form-control">
								</div>
								<div class="col-sm-2 col-offset-sm-1">
									<label style="float: left; line-height: 30px"><font
										color="red"> </font>BB:</label> <input name="bb"
										value="${shapeFile.bb}" maxlength="50"
										style="width: 150px; float: right" class="form-control">
								</div>
								<div class="col-sm-2 col-offset-sm-1">
									<label style="float: left; line-height: 30px"><font
										color="red"> </font>左大腿:</label> <input name="leftThign"
										value="${shapeFile.leftThign}" maxlength="50"
										style="width: 150px; float: right" class="form-control">
								</div>
								<div class="col-sm-2 col-offset-sm-1">
									<label style="float: left; line-height: 30px"><font
										color="red"> </font>右大腿:</label> <input name="rightThign"
										value="${shapeFile.rightThign}" maxlength="50"
										style="width: 150px; float: right " class="form-control">
								</div>
							</div>
						</fieldset>
					</form>
				</div>
				<div align="center">
				<shiro:hasPermission name="crm:userInfo:edit">
					<div align="center" class="col-md-3">
						<button id="enableEdit"  class="btn btn-default">编辑</button>
					</div>
					<div align="center" class="col-md-3">
						<button onclick="save()" type="button" class="btn btn-default">保存</button>
					</div>
				</shiro:hasPermission>
				</div>
			</div>
		</div>
	</div>
</body>
</html>