<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/webpage/include/taglib.jsp"%>

<html>
<head>
<title>商品服务费基础配置列表</title>
<meta name="decorator" content="default" />

<script type="text/javascript">
	function page(n,s) {
		$("#pageNo").val(n);
		$("#pageSize").val(s);
		$("#searchForm").submit();
		return false;
	}
	
	
	function  addGoods(show,close,actionId){
		var newDate=new Date();
		var showDate =new Date(show);
		var closeDate=new Date(close);
		if(showDate.getTime()<newDate.getTime()){
			top.layer.alert('活动已到开启时间不可以修改!',{icon: 0, title:'提醒'}); 
			 return;
		}
		if(newDate.getTime()>closeDate.getTime()){
			top.layer.alert('活动已经结束不能修改!', {icon: 0, title:'提醒'}); 
			 return;
		}
		if(newDate.getTime()<showDate.getTime()){
			 openDialogView('活动商品列表', '${ctx}/ec/action/addActionGoodsList?actionId='+actionId,'900px','700px');
		}
	
	}           
	
	function  actionEdit(show,close,actionId){
		var newDate=new Date();
		var showDate =new Date(show);
		var closeDate=new Date(close);
		if(showDate.getTime()<newDate.getTime()){
			top.layer.alert('活动已到开启时间不可以修改!', {icon: 0, title:'提醒'}); 
			 return;
		}
		if(newDate.getTime()>closeDate.getTime()){
			top.layer.alert('活动已经结束不能修改!', {icon: 0, title:'提醒'}); 
			 return;
		}
		if(newDate.getTime()<showDate.getTime()){
			openDialog('查看抢购活动', '${ctx}/ec/action/form?actionId='+actionId,'800px','550px');
		}
	
	}           
	
	
	
	$(document).ready(function() {
		
		
		var start = {
			    elem: '#startTime',
			    format: 'YYYY-MM-DD',
			    event: 'focus',
			    max: $("#endTime").val(),   //最大日期
			    istime: false,				//是否显示时间
			    isclear: true,				//是否显示清除
			    istoday: true,				//是否显示今天
			    issure: true,				//是否显示确定
			    festival: true,				//是否显示节日
			    choose: function(datas){
			         end.min = datas; 		//开始日选好后，重置结束日的最小日期
			         end.start = datas 		//将结束日的初始值设定为开始日
			    }
			};
		var end = {
			    elem: '#endTime',
			    format: 'YYYY-MM-DD',
			    event: 'focus',
			    min: $("#startTime").val(),
			    istime: false,
			    isclear: true,
			    istoday: true,
			    issure: true,
			    festival: true,
			    choose: function(datas){
			        start.max = datas; //结束日选好后，重置开始日的最大日期
			    }
			};
			laydate(start);
			laydate(end);
       
    });
	
	
</script>
</head>




<body class="gray-bg">
	<div class="wrapper wrapper-content">
		<div class="ibox">
			<div class="ibox-title">
				<h5>商品服务费基础配置列表</h5>
			</div>
			<sys:message content="${message}" />
			<!-- 查询条件 -->
			<div class="ibox-content">
				<div class="clearfix">
					<form:form id="searchForm" modelAttribute="goodsBeauticianFee" action="${ctx}/ec/beautician/list" method="post" class="form-inline">
						<input id="pageNo" name="pageNo" type="hidden" value="${page.pageNo}"/>
						<input id="pageSize" name="pageSize" type="hidden" value="${page.pageSize}"/>
						<div class="form-group">
							<label>商品名称：</label>
							<form:input path="goodsName" htmlEscape="false" maxlength="50" class=" form-control input-sm"/>&nbsp;&nbsp;
							<label>商品编号：</label>
							<form:input path="goodsNo" htmlEscape="false" maxlength="50" class=" form-control input-sm"/>&nbsp;&nbsp;
						</div>
					</form:form>
					<!-- 工具栏 -->
					<div class="row" style="padding-top: 10px;">
						<div class="col-sm-12">
							<div class="pull-left">
								<shiro:hasPermission name="ec:beautician:add">
									<a href="#" onclick="openDialog('创建商品服务费', '${ctx}/ec/beautician/form','650px','550px')" class="btn btn-white btn-sm"><i class="fa fa-plus"></i>创建商品服务费</a>
								</shiro:hasPermission>
								<p></p>
							</div>
							<div class="pull-right">
								<button  class="btn btn-primary btn-rounded btn-outline btn-sm " onclick="search()" ><i class="fa fa-search"></i> 查询</button>
								<button  class="btn btn-primary btn-rounded btn-outline btn-sm " onclick="reset()" ><i class="fa fa-refresh"></i> 重置</button>
							</div>
						</div>
					</div>
				</div>
				<table id="contentTable" class="table table-striped table-bordered  table-hover table-condensed  dataTables-example dataTable no-footer">
					<thead>
						<tr>
							<th style="text-align: center;">商品名称</th>
							<th style="text-align: center;">商品编号</th>
							<th style="text-align: center;">商品类型</th>
							<th style="text-align: center;">创建人</th>
							<th style="text-align: center;">创建时间</th>
							<th style="text-align: center;">操作</th>
						</tr>
					</thead>
					<tbody style="text-align: center;">
						<c:forEach items="${page.list}" var="beaut">
							<tr>
								<td>${beaut.goodsName}</td>
								<td>${beaut.goodsNo}</td>
								<td>
									<c:if test="${beaut.isReal==0}">
										实物
									</c:if>
									<c:if test="${beaut.isReal==1}">
										虚拟
									</c:if>
								</td>
								<td>${beaut.createBy.name}</td>
								<td><fmt:formatDate value="${beaut.createDate}" pattern="yyyy-MM-dd HH:mm:ss" /></td>
								<td>
									<shiro:hasPermission name="ec:beautician:view">
										<a href="#" onclick="openDialogView('查看商品服务费', '${ctx}/ec/beautician/editform?goodsId=${beaut.goodsId}','650px','550px')"
											class="btn btn-info btn-xs"><i class="fa fa-search-plus"></i>查看</a>
									</shiro:hasPermission>
									<shiro:hasPermission name="ec:beautician:edit">
										<a href="#" onclick="openDialog('编辑商品服务费', '${ctx}/ec/beautician/editform?goodsId=${beaut.goodsId}','650px','550px')"
										class="btn btn-success btn-xs"><i class="fa fa-file"></i>修改</a>
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