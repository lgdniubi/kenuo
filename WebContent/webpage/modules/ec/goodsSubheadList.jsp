<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/webpage/include/taglib.jsp"%>
<%@ page import="com.training.modules.sys.utils.ParametersFactory"%>
<!DOCTYPE html>
<html>
<head>
    <meta charset="utf-8">
   	<meta name="decorator" content="default"/>
    <meta name="viewport" content="width=device-width, initial-scale=1.0"> 
    <link rel="stylesheet" href="${ctxStatic}/ec/css/loading.css">
    <script>
	    function page(n,s){
			$("#pageNo").val(n);
			$("#pageSize").val(s);
			$("#searchForm").submit();
	    	return false;
	    }
		
	$(document).ready(function() {
			var start = {
				    elem: '#startDate',
				    format: 'YYYY-MM-DD hh:mm:ss',
				    event: 'focus',
				    max: $("#endDate").val(),   //最大日期
				    istime: true,				//是否显示时间
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
				    elem: '#endDate',
				    format: 'YYYY-MM-DD hh:mm:ss',
				    event: 'focus',
				    min: $("#startDate").val(),
				    istime: true,
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
    <title>商品副标题管理</title>
</head>
<body>
	<div class="wrapper-content">
        <div class="ibox">
            <sys:message content="${message}"/>
            <div class="ibox-content">
                <div class=" clearfix">
					<form:form id="searchForm" modelAttribute="goodsSubhead" action="${ctx}/ec/goodsSubhead/list" method="post" class="form-inline">
						<!-- 翻页隐藏文本框 -->
						<input id="pageNo" name="pageNo" type="hidden" value="${page.pageNo}"/>
						<input id="pageSize" name="pageSize" type="hidden" value="${page.pageSize}"/>
						<div class="form-group">
							<span>副标题名称：</span>
							<form:input path="name" htmlEscape="false" maxlength="50" class=" form-control input-sm" />
							<span>副标题文案：</span>
							<form:input path="subheading" htmlEscape="false" maxlength="50" class=" form-control input-sm" />
							<label>活动日期：</label>
							<input id="startDate" name="startDate" type="text" maxlength="20" class="laydate-icon form-control layer-date input-sm" value="<fmt:formatDate value="${goodsSubhead.startDate}" pattern="yyyy-MM-dd"/>" style="width:185px;" readonly="readonly"/>
							一
							<input id="endDate" name="endDate" type="text" maxlength="20" class=" laydate-icon form-control layer-date input-sm" value="<fmt:formatDate value="${goodsSubhead.endDate}" pattern="yyyy-MM-dd"/>"  style="width:185px;" readonly="readonly"/>&nbsp;&nbsp;
							<label>活动状态：</label>
							<select id="status" name="status" class="form-control" style="width:185px;">
								<option value="">全部</option>
								<option value="0" ${(goodsSubhead.status == '0')?'selected="selected"':''}>开启</option>
								<option value="1" ${(goodsSubhead.status == '1')?'selected="selected"':''}>关闭</option>
							</select>
							<label>开启状态：</label>
							<select id="openStatus" name="openStatus" class="form-control" style="width:185px;">
								<option value="">全部</option>
								<option value="1" ${(goodsSubhead.openStatus == '1')?'selected="selected"':''}>未开始</option>
								<option value="2" ${(goodsSubhead.openStatus == '2')?'selected="selected"':''}>生效中</option>
								<option value="3" ${(goodsSubhead.openStatus == '3')?'selected="selected"':''}>已结束</option>
							</select>
						</div>
					</form:form>
					<!-- 工具栏 -->
					<div class="row" style="padding-top: 10px;">
						<div class="col-sm-12">
							<div class="pull-left">
								<shiro:hasPermission name="ec:goodsSubhead:add">
									<a href="#" onclick="openDialog('创建副标题活动', '${ctx}/ec/goodsSubhead/form?flag=add','800px','650px')" class="btn btn-white btn-sm"><i class="fa fa-plus"></i>创建副标题活动</a>
								</shiro:hasPermission>
								<p></p>
							</div>
							<div class="pull-right">
								<button  class="btn btn-primary btn-rounded btn-outline btn-sm " onclick="search()" ><i class="fa fa-search"></i> 查询</button>
								<button  class="btn btn-primary btn-rounded btn-outline btn-sm " onclick="reset()" ><i class="fa fa-refresh"></i> 重置</button>
							</div>
						</div>
					</div>
                <table id="treeTable" class="table table-bordered table-hover table-striped">
                	<thead> 
                		<tr>
                			<th style="text-align: center;">序号</th>
                			<th style="text-align: center;">副标题名称</th>
                			<th style="text-align: center;">副标题文案</th>
                			<th style="text-align: center;">生效时间</th>
                			<th style="text-align: center;">失效时间</th>
                			<th style="text-align: center;">活动状态</th>
                			<th style="text-align: center;">备注</th>
                			<th style="text-align: center;">操作</th>
                		</tr>
                	</thead>
                    <tbody>
                    	<c:forEach items="${page.list}" var="goodsSubhead">
						<tr>
							<td style="text-align: center;">${goodsSubhead.goodsSubheadId}</td>
							<td style="text-align: center;">${goodsSubhead.name}</td>
							<td style="text-align: center;">${goodsSubhead.subheading}</td>
							<td style="text-align: center;">
								<fmt:formatDate value="${goodsSubhead.startDate}"  pattern="yyyy-MM-dd HH:mm:ss" />
							</td>	
							<td style="text-align: center;">
								<fmt:formatDate value="${goodsSubhead.endDate}"  pattern="yyyy-MM-dd HH:mm:ss" />
							</td>	
							<td style="text-align: center;">
								<c:if test="${goodsSubhead.status == '0'}">开启</c:if>
								<c:if test="${goodsSubhead.status == '1'}">关闭</c:if>
							</td>
							<td style="text-align: center;">
								<!-- 当文本内容大于5个字符时，只显示其前五个字符 -->
							  	<c:choose>  
							         <c:when test="${fn:length(goodsSubhead.remarks) > 10}">  
							             <c:out value="${fn:substring(goodsSubhead.remarks, 0, 10)}..." />  
							         </c:when>  
							        <c:otherwise>  
							           <c:out value="${goodsSubhead.remarks}" />  
							        </c:otherwise>  
							    </c:choose>   
							</td>
							<td style="text-align: center;">
								<shiro:hasPermission name="ec:goodsSubhead:open">
									<c:if test="${goodsSubhead.status==0}">
										<a href="${ctx}/ec/goodsSubhead/open?status=1&goodsSubheadId=${goodsSubhead.goodsSubheadId}"  class="btn btn-primary btn-xs">
										<i class="fa fa-file"></i>开启</a>
									</c:if>
									<c:if test="${goodsSubhead.status==1}">
										<a href="${ctx}/ec/goodsSubhead/open?status=0&goodsSubheadId=${goodsSubhead.goodsSubheadId}"  class="btn btn-danger btn-xs">
										<i class="fa fa-close"></i>关闭</a>
									</c:if>
								</shiro:hasPermission>
								<shiro:hasPermission name="ec:goodsSubhead:view">
									<a href="#" onclick="openDialogView('查看', '${ctx}/ec/goodsSubhead/form?goodsSubheadId=${goodsSubhead.goodsSubheadId}&flag=view','800px', '650px')" class="btn btn-info btn-xs"><i class="fa fa-search-plus"></i> 查看</a>
								</shiro:hasPermission>
								<shiro:hasPermission name="ec:goodsSubhead:edit">
									<a href="#" onclick="openDialog('修改', '${ctx}/ec/goodsSubhead/form?goodsSubheadId=${goodsSubhead.goodsSubheadId}&flag=edit','800px', '650px')" class="btn btn-success btn-xs"><i class="fa fa-edit"></i> 修改</a>
								</shiro:hasPermission>
								<shiro:hasPermission name="ec:goodsSubhead:goodsSubheadGoodsList">
									<a href="#" onclick="openDialogView('添加商品', '${ctx}/ec/goodsSubhead/goodsSubheadGoodsList?goodsSubheadId=${goodsSubhead.goodsSubheadId}','700px', '650px')" class="btn btn-info btn-xs"><i class="fa fa-edit"></i> 添加商品</a>
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
                                </div>
                               	<table:page page="${page}"></table:page>
                            </td>	
                        </tr>
                    </tfoot>
                </table>
            </div>
        </div>
   </div>
   </div>
   <div class="loading"></div>
</body>
</html>