<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/webpage/include/taglib.jsp"%>
<html>
<head>
	<title>免费体验</title>
	<meta name="decorator" content="default"/>
    <meta name="viewport" content="width=device-width, initial-scale=1.0, minimum-scale=1.0, maximum-scale=1.0,user-scalable=no">
	<script type="text/javascript">
		function page(n,s){
			$("#pageNo").val(n);
			$("#pageSize").val(s);
			$("#searchForm").submit();
	    	return false;
	    }
	    function updateType(a,b,c,d){
	    	// a id   b  type类型  c 美容师id  d  美容师名称
	    	$("#updateId").val(a);
	    	$("#updateType").val(b);
	    	$("#beautyIdId").val(c);
	    	$("#beautyIdName").val(d);
	    	$('#resetpsd').modal('show');
	    }
	    $(document).ready(function() {
		    $("#freeForm").validate({
		    	submitHandler: function(form){
					loading('正在提交，请稍等...');
					form.submit();
				}
		    });
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
				<h5>免费体验</h5>
			</div>
			<sys:message content="${message}"/>
			<!-- 查询条件 -->
	    	<div class="ibox-content">
				<div class="clearfix">
					<form id="searchForm" action="${ctx}/ec/freeorder/list" method="post" class="navbar-form navbar-left searcharea">
						<!-- 翻页隐藏文本框 -->
						<input id="pageNo" name="pageNo" type="hidden" value="${page.pageNo}"/>
						<input id="pageSize" name="pageSize" type="hidden" value="${page.pageSize}"/>
						<table:sortColumn id="orderBy" name="orderBy" value="${page.orderBy}" callback="sortOrRefresh();"/><!-- 支持排序 -->
						<div class="form-group">
							<span><label>实体店铺:</label></span>
                            <sys:treeselect id="shopId" name="shopId" value="${freeOrder.shopId}" labelName="office.name" labelValue="${freeOrder.office.name}" 
							     title="店铺" url="/sys/office/treeData" cssClass="area1 form-control required"  allowClear="true" notAllowSelectRoot="true"/>
							<select class="form-control" id="type" name="type">
								<option value="">服务状态</option>
								<c:forEach items="${fns:getDictList('freeOrder_type')}" var="freeOrderType">
									<c:choose>
										<c:when test="${freeOrderType.value eq freeOrder.type}">
											<option value="${freeOrderType.value }" selected="selected">${freeOrderType.label }</option>
										</c:when>
										<c:otherwise>
											<option value="${freeOrderType.value }">${freeOrderType.label }</option>
										</c:otherwise>
									</c:choose>
								</c:forEach>
							</select>
							<input id="keyword" name="keyword" type="text" value="${freeOrder.keyword }" class="form-control" placeholder="关键字"> 
						</div>	
					</form>
					<div class="pull-right">
						<button  class="btn btn-primary btn-rounded btn-outline btn-sm " onclick="search()" ><i class="fa fa-search"></i> 查询</button>
						<button  class="btn btn-primary btn-rounded btn-outline btn-sm " onclick="reset()" ><i class="fa fa-refresh"></i> 重置</button>
					 </div>
				</div>
				<table id="contentTable" class="table table-striped table-bordered  table-hover table-condensed  dataTables-example dataTable no-footer">
					<thead>
						<tr>
							 <th style="text-align: center;">免费体验号</th>
						     <th style="text-align: center;">姓名</th>
						     <th style="text-align: center;">体验项目</th>
						     <th style="text-align: center;">到店体验时间</th>
						     <th style="text-align: center;">体验店</th>
						     <th style="text-align: center;">手机号</th>
						     <th style="text-align: center;">体验状态</th>
						     <th style="text-align: center;">接待人</th>
						     <th style="text-align: center;">创建时间</th>
						     <th style="text-align: center;">操作</th>
						</tr>
					</thead>
					<tbody style="text-align: center;">
						<c:forEach items="${page.list}" var="freeOrders">
							<tr>
								<td>${freeOrders.id }</td>
							  	<td>${freeOrders.userName }</td>
							  	<td>
							  	<!-- 0：脸部护理     1经络养生    2：女葆护理    3：刮痧护理    4：中华神灸     5：美颜美体', -->
							  		<c:if test="${freeOrders.serverId == 0}">
							  			脸部护理
							  		</c:if>
							  		<c:if test="${freeOrders.serverId == 1}">
							  			经络养生
							  		</c:if>
							  		<c:if test="${freeOrders.serverId == 2}">
							  			女葆护理
							  		</c:if>
							  		<c:if test="${freeOrders.serverId == 3}">
							  			刮痧护理
							  		</c:if>
							  		<c:if test="${freeOrders.serverId == 4}">
							  			中华神灸
							  		</c:if>
							  		<c:if test="${freeOrders.serverId == 5}">
							  			美颜美体
							  		</c:if>
						  		</td>
							  	<td><fmt:formatDate value="${freeOrders.expTime }" pattern="yyyy-MM-dd HH:mm:ss"/></td>
							  	<td>${freeOrders.office.name }</td>
							  	<td>${freeOrders.mobile }</td>
							  	<td>${fns:getDictLabel(freeOrders.type, 'freeOrder_type', '')}</td>
							  	<td>${freeOrders.user.name }</td>
							  	<td><fmt:formatDate value="${freeOrders.addTime }" pattern="yyyy-MM-dd HH:mm:ss"/></td>
							    <td>
							    	<shiro:hasPermission name="ec:freeorder:save">
							     		<a class="btn btn-info btn-xs resetpsd"  href="#" onclick="updateType('${freeOrders.id }','${freeOrders.type }','${freeOrders.beautyId }','${freeOrders.user.name }')"><i class="fa fa-edit"></i>修改体验状态</a>
							     	</shiro:hasPermission>
							     	<shiro:hasPermission name="ec:freeorder:findRemark">
							     		<a class="btn btn-success btn-xs" href="#" onclick="openDialog('查看备注', '${ctx}/ec/freeorder/findRemark?divId=${freeOrders.id }','650px', '500px')"><i class="fa fa-edit"></i>备注</a>
							     	</shiro:hasPermission>
							    </td>
							</tr>
						</c:forEach>
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
	<div class="loading"></div>
	<!--修改体验状态-->
	<div class="modal fade bs-example-modal-lg in" id="resetpsd" tabindex="-1" role="dialog" aria-labelledby="myLargeModalLabel" style="display: none; padding-right: 17px;">
  		<form id="freeForm" class="modal-dialog modal-lg" action="${ctx }/ec/freeorder/save">
    		<div class="modal-content">
      			<div class="modal-header">
      				<span>修改体验状态</span>
        			<button type="button" class="close" data-dismiss="modal" aria-label="Close"><span aria-hidden="true">×</span></button>
      			</div>
      			<div class="modal-body">
      				<input type="hidden" id="updateId" name="id">
      				<table>
      					<tr>
					    	<td>请选择美容师</td>
					    	<td>
					    		<sys:treeselect id="beautyId" name="beautyId" value="" labelName="office.name" labelValue="" 
							     title="美容师" url="/sys/office/treeData?type=3" cssClass="area1 form-control required"  allowClear="true" notAllowSelectRoot="true"/>
					    	</td>
      					</tr>
      					<tr><td colspan="2" height="5"></td></tr>
					    <tr>
					    	<td>请选择体验状态<td>
				    		<select class="form-control required" id="updateType" name="type">
								<option value="">服务状态</option>
								<c:forEach items="${fns:getDictList('freeOrder_type')}" var="freeOrderType">
										<option value="${freeOrderType.value }">${freeOrderType.label }</option>
								</c:forEach>
							</select>
					    </tr>
					</table>
        			<!-- <input type="hidden" value="" id="pwduserid" name="userid"> -->
      			</div>
      			<div class="modal-footer">
      				<input type="submit" class="btn btn-success" value="保 存">
					<a href="#" class="btn btn-primary" data-dismiss="modal">关   闭</a>
      			</div>
    		</div>
  		</form>
	</div>
</body>
</html>