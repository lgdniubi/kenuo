<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/webpage/include/taglib.jsp"%>

<html>
<head>
<title>直播列表</title>
<meta name="decorator" content="default" />

<script type="text/javascript">
	function page(n, s) {
		$("#pageNo").val(n);
		$("#pageSize").val(s);
		$("#searchForm").submit();
		return false;
	}
	
	//正式数据
	function changeTestVal(flag,id,isTest){
		$(".loading").show();//打开展示层
		$.ajax({
			type : "POST",
			url : "${ctx}/train/live/updateIsTest?id="+id+"&isTest="+isTest,
			dataType: 'json',
			success: function(data) {
				$(".loading").hide(); //关闭加载层
				var STATUS = data.STATUS;
				var ISTEST = data.ISTEST;
				if("OK" == STATUS){
	            	$("#"+flag+id).html("");//清除DIV内容	
					if(ISTEST == '1'){
						$("#"+flag+id).append("<img width='20' height='20' src='${ctxStatic}/ec/images/cancel.png' onclick=\"changeTestVal('"+flag+"','"+id+"','0')\">");
					}else if(ISTEST == '0'){
						$("#"+flag+id).append("<img width='20' height='20' src='${ctxStatic}/ec/images/open.png' onclick=\"changeTestVal('"+flag+"','"+id+"','1')\">");
					} 
				}else if("ERROR" == STATUS){
					alert(data.MESSAGE);
				}
			}
		});   
	}
</script>
</head>

<body class="gray-bg">
	<div class="wrapper wrapper-content">
		<div class="ibox">
			<div class="ibox-title">
				<h5>直播列表</h5>
			</div>
			<!-- 页面加载等待 -->
			<div class="loading"></div>
			<sys:message content="${message}" />
			<!-- 查询条件 -->
			<div class="ibox-content">
				<div class="clearfix">
					<form:form id="searchForm" modelAttribute="trainLiveAudit" action="${ctx}/train/live/list" method="post" class="form-inline">
						<input id="pageNo" name="pageNo" type="hidden" value="${page.pageNo}"/>
						<input id="pageSize" name="pageSize" type="hidden" value="${page.pageSize}"/>
						<div class="form-group">
							<label>用户名：</label><form:input path="userName" htmlEscape="false" maxlength="50" class=" form-control input-sm" />
							<label>手机号：</label><form:input path="mobile" htmlEscape="false" maxlength="50" class=" form-control input-sm" />
							<span>归属机构：</span>
							<sys:treeselect id="organization" name="organization.id" value="${trainLiveAudit.organization.id}" labelName="organization.name" labelValue="${trainLiveAudit.organization.name}" title="部门" url="/sys/office/treeData?type=2" cssClass=" form-control input-sm" allowClear="true" notAllowSelectRoot="false" notAllowSelectParent="false" />
							<label>职位：</label><form:input path="position" htmlEscape="false" maxlength="50" class=" form-control input-sm" />
							<p></p>
							<label>直播主题：</label><form:input path="title" htmlEscape="false" maxlength="50" class=" form-control input-sm" />
							<label>审核状态：</label>
							<form:select path="auditStatus" class="form-control">
								<form:option value=" ">全部</form:option>
								<form:option value="0">审核失败</form:option>
								<form:option value="1">请求审核</form:option>
								<form:option value="2">审核通过</form:option>
								<form:option value="3">已完成</form:option>
								<form:option value="4">正在直播</form:option>
								<form:option value="5">异常</form:option>
							</form:select>
							<label>是否公开到每天美耶：</label>
							<form:select path="isOpen" class="form-control">
								<form:option value=" ">全部</form:option>
								<form:option value="0">不公开</form:option>
								<form:option value="1">公开</form:option>
							</form:select>
							<label>是否推荐：</label>
							<form:select path="isRecommend" class="form-control">
								<form:option value=" ">全部</form:option>
								<form:option value="0">未推荐</form:option>
								<form:option value="1">推荐</form:option>
							</form:select>
						</div>
					</form:form>
					<!-- 工具栏 -->
					<div class="row" style="padding-top: 10px;">
						<div class="col-sm-12">
							<div class="pull-right">
								<button  class="btn btn-primary btn-rounded btn-outline btn-sm " onclick="search()" ><i class="fa fa-search"></i> 查询</button>
								<button  class="btn btn-primary btn-rounded btn-outline btn-sm " onclick="reset()" ><i class="fa fa-refresh"></i> 重置</button>
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
							<th style="text-align: center;">申请人</th>
							<!-- <th style="text-align: center;">申请人描述</th> -->
							<th style="text-align: center;">直播id</th>
							<th style="text-align: center;">直播主题</th>
							<!-- <th style="text-align: center;">直播描述</th> -->
							<th style="text-align: center;">是否付费</th>
							<th style="text-align: center;">直播时间</th>
							<th style="text-align: center;">申请时间</th>
							<th style="text-align: center;">正式数据</th>
							<th style="text-align: center;">审核状态</th>
							<th style="text-align: center;">审核人</th>
							<th style="text-align: center;">操作</th>
						</tr>
					</thead>
					<tbody style="text-align: center;">
						<c:forEach items="${page.list}" var="live">
							<tr>
								<td>${live.id}</td>
								<td>${live.userName}</td>
								<%-- <td>${live.remarks}</td> --%>
								<td>${live.roomId}</td>
								<td>${live.title}</td>
								<%-- <td>${live.desc}</td> --%>
								<td>
									<c:if test="${live.isPay==1}">
										免费
									</c:if>
									<c:if test="${live.isPay==2}">
										线下收费
									</c:if>
									<c:if test="${live.isPay==3}">
										线上收费
									</c:if>
								</td>
								<td><fmt:formatDate value="${live.bengTime}" pattern="yyyy-MM-dd HH:mm:ss"/></td>
								<td><fmt:formatDate value="${live.createDate}" pattern="yyyy-MM-dd HH:mm:ss"/></td>
								<td style="text-align: center;" id="isTest${live.id}">
									<c:if test="${live.isTest == 1}">
										<img width="20" height="20" src="${ctxStatic}/ec/images/cancel.png" onclick="changeTestVal('isTest','${live.id}','0')">
									</c:if>
									<c:if test="${live.isTest == 0}">
										<img width="20" height="20" src="${ctxStatic}/ec/images/open.png" onclick="changeTestVal('isTest','${live.id}','1')">
									</c:if>
								</td>
								<td>
									<c:if test="${live.auditStatus==0}">
										审核失败
									</c:if>
									<c:if test="${live.auditStatus==1}">
										请求审核
									</c:if>
									<c:if test="${live.auditStatus==2}">
										审核通过
									</c:if>
									<c:if test="${live.auditStatus==3}">
										已完成
									</c:if>
									<c:if test="${live.auditStatus==4}">
										正在直播
									</c:if>
									<c:if test="${live.auditStatus==5}">
										异常
									</c:if>
								</td>	
								<td>${live.auditUser}</td>
								<td>
									<shiro:hasPermission name="train:live:view">
										<a href="#" onclick="openDialogView('查看直播', '${ctx}/train/live/form?id=${live.id}','800px','650px')"
											class="btn btn-info btn-xs"><i class="fa fa-search-plus"></i>查看</a>
									</shiro:hasPermission> 
									<shiro:hasPermission name="train:live:edit">
										<c:if test="${live.auditStatus!=3}">
											<a href="#" onclick="openDialog('编辑直播','${ctx}/train/live/form?id=${live.id}','800px','650px')"
												class="btn btn-success btn-xs"><i class="fa fa-edit"></i>修改</a>
										</c:if>
										<c:if test="${live.auditStatus==3}">
											<a href="#" style="background: #C0C0C0; color:#FFF" class="btn  btn-xs"><i class="fa fa-edit"></i>修改</a>
										</c:if>
									</shiro:hasPermission>
									<shiro:hasPermission name="train:live:view">
										<a href="#" onclick="openDialogView('查看回看列表', '${ctx}/train/live/backform?userId=${live.userId}','900px','600px')"
											class="btn btn-info btn-xs"><i class="fa fa-search-plus"></i>查看回看</a>
									</shiro:hasPermission> 
									<shiro:hasPermission name="train:live:view">
										<a href="#" onclick="openDialogView('会员列表', '${ctx}/train/live/liveUserform?auditId=${live.id}','800px','500px')"
												class="btn btn-info btn-xs"><i class="fa fa-search-plus"></i>会员列表</a>
									</shiro:hasPermission> 
									<shiro:hasPermission name="train:live:sku">
										<a href="#" onclick="openDialogView('配置列表', '${ctx}/train/live/liveSkuForm?auditId=${live.id}','800px','500px')"
											 class="btn btn-info btn-xs"><i class="fa fa-search-plus"></i>查看配置</a>
									</shiro:hasPermission>
									<shiro:hasPermission name="train:live:cloudContribution">
										<a href="#" onclick="openDialogView('云币贡献榜', '${ctx}/train/live/cloudContribution?auditId=${live.id}','800px','500px')"
											 class="btn btn-info btn-xs"><i class="fa fa-search-plus"></i>云币贡献榜</a>
									</shiro:hasPermission>
									<shiro:hasPermission name="train:live:edit">
										<a href="#" onclick="openDialog('编辑直播','${ctx}/train/live/liveFormMtmy?id=${live.id}','700px','400px')"
											class="btn btn-success btn-xs"><i class="fa fa-edit"></i>每天美耶权限按钮</a>
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