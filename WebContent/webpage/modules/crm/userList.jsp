<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/webpage/include/taglib.jsp"%>
<html>
<head>
<title>会员管理</title>
<meta name="decorator" content="default" />
<meta name="viewport"
	content="width=device-width, initial-scale=1.0, minimum-scale=1.0, maximum-scale=1.0,user-scalable=no">
<script type="text/javascript">
	
	function addVirtualOrder(userId){
		var url = "/kenuo/a/crm/orders/createOrder?userId="+userId;
		openDialog("新增虚拟订单添加",url,"900px", "650px");
	}
	function addKindOrder(userId){
		var url = "/kenuo/a/crm/orders/createKindOrder?userId="+userId;
		openDialog("新增实物订单添加",url,"900px", "650px");
	}
	function newSearch() {
		$("#searchForm").submit();
	}
	function reset() {//重置，页码清零
		$("#pageNo").val(0);
		$("#bazaarId").val("");
		$("#bazaarName").val("");
		$("#shopId").val("");
		$("#shopName").val("");
		$("#searchForm div.form-control select").val("");
		return false;
	}
	$(document).ready( function(){
		$("#shopButton").click(function() {
			var bazaarId = $("#bazaarId").val();
			// 是否限制选择，如果限制，设置为disabled
			if ($("#shopButton").hasClass("disabled")) {
				return true;
		     }
			if (bazaarId == null|| bazaarId == "") {
				top.layer.alert('请先选择市场!', {
					icon : 0,
					title : '提醒'
					});
			} else {
				// 正常打开	
			   top.layer.open({
				   		type : 2,
						area : [ '300px','420px' ],
						title : "选择所在店铺",
						ajaxData : {
							selectIds : $("#shopId").val()
						},
						content : "/kenuo/a/tag/treeselect?url="+ encodeURIComponent("/ec/specEquipment/treeDataForShop?id="+ bazaarId)
								  + "&module=&checked=&extId=&isAll=",
						btn : [ '确定', '关闭' ],
						yes : function(index,layero) { 
							    //或者使用btn1
								var tree = layero.find("iframe")[0].contentWindow.tree;//h.find("iframe").contents();
								var ids = [], names = [], nodes = [];
									if ("" == "true") {
										nodes = tree.getCheckedNodes(true);
									} else {
										nodes = tree.getSelectedNodes();
									}
									for (var i = 0; i < nodes.length; i++) {//
										ids.push(nodes[i].id);
										names.push(nodes[i].name);//
										break; // 如果为非复选框选择，则返回第一个选择  
									}
									$("#shopId").val(ids.join(",").replace(/u_/ig,""));
									$("#shopName").val(names.join(","));
									$("#shopName").focus();
									if ("shop" == "officeIdbeaut") {
											findBeauty();
									}
										top.layer.close(index);
									},
						cancel : function(index) { //或者使用btn2
																//按钮【按钮二】的回调
								 }
					});
				}
			});
	$("#bazaarButton").click(function() {
			$("#shopId").val("");
			$("#shopName").val("");
			// 是否限制选择，如果限制，设置为disabled
			if ($("#bazaarButton").hasClass("disabled")) {
				return true;
			}
			// 正常打开	
			top.layer.open({
				type : 2,
				area : [ '300px','420px' ],
				title : "选择市场",
				ajaxData : {
					selectIds : $("#bazaarId").val()
			    },
				content : "/kenuo/a/tag/treeselect?url="+ encodeURIComponent("/sys/office/treeData?isGrade=true")
							+ "&module=&checked=&extId=&isAll=",
				btn : [ '确定', '关闭' ],
				yes : function(index,layero) { //或者使用btn1
						var tree = layero.find("iframe")[0].contentWindow.tree;//h.find("iframe").contents();
						var ids = [], names = [], nodes = [];
						if ("" == "true") {
							nodes = tree.getCheckedNodes(true);
							} else {
							nodes = tree.getSelectedNodes();
							}
						for (var i = 0; i < nodes.length; i++) {//
							if (nodes[i].isParent) {
							//top.$.jBox.tip("不能选择父节点（"+nodes[i].name+"）请重新选择。");
							//layer.msg('有表情地提示');
							top.layer.msg("不能选择父节点（"+ nodes[i].name+ "）请重新选择。",{icon : 0});
							return false;
							}//
							ids.push(nodes[i].id);
							names.push(nodes[i].name);//
							break; // 如果为非复选框选择，则返回第一个选择  
						}
						$("#bazaarId").val(ids.join(",").replace(/u_/ig,""));
						$("#bazaarName").val(names.join(","));
						$("#bazaarName").focus();
						//预约管理-->> 修改预约地址时异步加载美容师   2016-06-29  咖啡
						if ("bazaar" == "officeIdbeaut") {
							findBeauty();
							}
						top.layer.close(index);
					},
				cancel : function(index) { //或者使用btn2
											   //按钮【按钮二】的回调
					}
			});

		});
	});
</script>
<style type="text/css">
.modal-content {
	margin: 0 auto;
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
				<div class="text">
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
					<div class="row">
						<div class="col-sm-12">
							<form:form id="searchForm" modelAttribute="userDetail"
								action="${ctx}/crm/user/userList" method="post" class="form-inline">
								<input id="pageNo" name="pageNo" type="hidden"
									value="${page.pageNo}" />
								<input id="pageSize" name="pageSize" type="hidden"
									value="${page.pageSize}" />
								<table:sortColumn id="orderBy" name="orderBy"
									value="${page.orderBy}" callback="sortOrRefresh();" />
								<!-- 支持排序 -->

								<div class="form-group">
									<shiro:hasPermission name="crm:office:choose">
									<span>选择市场：</span> <input id="bazaarId"
										class=" form-control input-sm" name="bazaarId"
										value="${userDetail.bazaarId}" type="hidden">
									<div class="input-group">
										<input id="bazaarName" class=" form-control input-sm"
											name="bazaarName" readonly="readonly"
											value="${userDetail.bazaarName}" data-msg-required=""
											style="" type="text"> <span class="input-group-btn">
											<button id="bazaarButton" class="btn btn-sm btn-primary "
												type="button">
												<i class="fa fa-search"></i>
											</button>
										</span>
									</div>
									<label id="bazaarName-error" class="error" for="bazaarName"
										style="display: none"></label>
									</shiro:hasPermission>	
									<%-- <sys:treeselect id="bazaar" name="bazaarId" value="${equipment.bazaarId}" labelName="bazaarName" labelValue="${equipment.bazaarName}" title="市场" url="/sys/office/treeData?isGrade=true" cssClass=" form-control input-sm" allowClear="true" notAllowSelectRoot="true" notAllowSelectParent="true"/> --%>
									<shiro:hasPermission name="crm:office:choose">
									<span>选择店铺：</span> <input id="shopId"
										class=" form-control input-sm" name="shopId"
										value="${userDetail.shopId}" type="hidden">
									<div class="input-group">
										<input id="shopName" class=" form-control input-sm"
											name="shopName" readonly="readonly"
											value="${userDetail.shopName}" data-msg-required="" style=""
											type="text"> <span class="input-group-btn">
											<button id="shopButton" class="btn btn-sm btn-primary "
												type="button">
												<i class="fa fa-search"></i>
											</button>
										</span>
									</div>
									</shiro:hasPermission>
									<label id="shopName-error" class="error" for="shopName"
										style="display: none"></label> <span>请选等级</span>
									<form:select path="level" name="level"
										value="${userDetail.level}" class="form-control"
										style="width:185px;">
										<form:option value="">等级</form:option>
										<form:option value="0">L0</form:option>
										<form:option value="1">L1</form:option>
										<form:option value="2">L2</form:option>
										<form:option value="3">L3</form:option>
										<form:option value="4">L4</form:option>
										<form:option value="5">L5</form:option>
										<form:option value="6">L6</form:option>
										<form:option value="7">L7</form:option>
										<form:option value="8">L8</form:option>
										<form:option value="9">L9</form:option>
										<form:option value="10">L10</form:option>
									</form:select>
									<form:input neme="keyword" path="keyword"
										value="${userDetail.keyword}" htmlEscape="false"
										maxlength="50" class=" form-control input-sm"
										placeholder ="姓名、手机号码、昵称、美容师等" style="width:250px" />
									<div class="pull-right">
									<button class="btn btn-primary btn-rounded btn-outline btn-sm "
									onclick="newSearch()"><i class="fa fa-search"></i>查询</button>
							</div>	
								</div>
							</form:form>
						</div>
						<div class="row" style="padding-top: 10px;">
						<div class="col-sm-12">
							<div class="pull-left">
							<shiro:hasPermission name="crm:userSecret:view">
								<button class="btn btn-primary btn-rounded btn-outline"
									onclick="openDialog('添加会员', '${ctx}/crm/user/adduserindex','650px', '500px')">
									 新建用户
								</button>
							</shiro:hasPermission>	
								<p></p>
							</div>
						</div>
						</div>
					</div>
				</div>
				<table id="contentTable"
					class="table table-striped table-bordered  table-hover table-condensed  dataTables-example dataTable no-footer">
					<thead>
						<tr>
							<!-- 							 <th style="text-align: center;">编号</th> -->
							
							<th style="text-align: center;">ID</th>
							<th style="text-align: center;">昵称</th>
							<th style="text-align: center;">姓名</th>
							<th style="text-align: center;">性别</th>
							<th style="text-align: center;">手机</th>
							<th style="text-align: center;">等级</th>
							<th style="text-align: center;">颜值</th>
							<th style="text-align: center;">欠款</th>
							<th style="text-align: center;">美容师</th>
							<th style="text-align: center;">注册日期</th>
							<th style="text-align: center;">首次消费</th>
							<th style="text-align: center;">最近消费</th>
							<th style="text-align: center;">所属市场</th>
							<th style="text-align: center;">操作</th>
						</tr>
					</thead>
					<tbody style="text-align: center;">
						<c:forEach items="${page.list}" var="users">
							<tr>
								<td>${users.userId}</td>
								<td>${users.nickname}</td>
								<td>${users.name}</td>
								<!-- 							  	<td> -->
								<%-- <%-- 							  		<shiro:hasPermission name="ec:mtmyuser:finduseraccounts"> --%>
								<%-- 							  			<a href="#" onclick="openDialogView('用户账户详情', '${ctx}/ec/mtmyuser/finduseraccounts?userId=${users.userid }','650px', '320px')">查看</a> --%>
								<%-- <%-- 							  		</shiro:hasPermission> --%>
								<!-- 							  	</td> -->
								<c:if test="${users.sex==0}">
									<td>保密</td>
								</c:if>
								<c:if test="${users.sex==1}">
									<td>男</td>
								</c:if>
								<c:if test="${users.sex==2}">
									<td>女</td>
								</c:if>
								<td>${users.mobile}</td>
								<td>${users.level}</td>
								<td>${users.levelvalue }</td>
								<td>${users.accountArrearage }</td>
								<td>${users.beautyName }</td>
								<td><fmt:formatDate value="${users.regTime}"
										pattern="yyyy-MM-dd " /></td>
								<td><fmt:formatDate value="${users.firstDate}"
										pattern="yyyy-MM-dd " /></td>
								<td><fmt:formatDate value="${users.lastDate}"
										pattern="yyyy-MM-dd " /></td>
								<td>${users.bazaarName }</td>

								<td>
									<a class="btn btn-info btn-xs"
									href="${ctx}/crm/user/userDetail?userId=${users.userId}">详情</a> 
									<shiro:hasPermission name="crm:userSecret:view">
									<button class="btn btn-info btn-xs btn-danger" onclick="addKindOrder(${users.userId})">实物</button> 
									<button class="btn btn-info btn-xs btn-danger" onclick="addVirtualOrder(${users.userId})">虚拟</button>
								    </shiro:hasPermission>
								</td>
							</tr>
						</c:forEach>
					</tbody>
					<tfoot>
						<tr>
							<td colspan="20">
								<!-- 分页代码 -->
								<div class="tfoot"></div> <table:page page="${page}"></table:page>
							</td>
						</tr>
					</tfoot>
				</table>
			</div>
		</div>
	</div>
</body>
</html>