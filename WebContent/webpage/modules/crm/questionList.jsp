<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/webpage/include/taglib.jsp"%>

<html>
<head>
	<title>投诉咨询</title>
	<meta name="decorator" content="default"/>
	<script type="text/javascript">
		//分页按钮
		function page(n, s) {
			$("#pageNo").val(n);
			$("#pageSize").val(s);
			$("#searchForm").submit();
			return false;
		}
	</script>
</head>
<body>
	<div class="ibox-content">
		<sys:message content="${message}"/>
		<div class="warpper-content">
			<div class="ibox">
				<div class="ibox-title">
					<h5>投诉咨询</h5>
				</div>
				<div class="ibox-content">
					<div class="clearfix">
						<form:form id="searchForm" action="${ctx}/crm/store/list" modelAttribute="complain" method="post" class="form-inline">
							<input id="pageNo" name="pageNo" type="hidden" value="${page.pageNo}"/>
							<input id="pageSize" name="pageSize" type="hidden" value="${page.pageSize}"/>
							<form:hidden path="stamp" id="stamp"/>
							<form:hidden path="mobile" id="mobile"/>
							<form:hidden path="member" id="member"/>
							<div class="form-group">						
								<label>商品品牌：</label>
			                    <select class="form-control" id="brandType" name="brandType" style="text-align: center;width: 150px;">
			                        <option value="">所有品牌</option>
									<c:forEach items="${goodsBrandList}" var="goodsBrand">
										<option ${(goodsBrand.id == complain.brandType)?'selected="selected"':''} value="${goodsBrand.id}">${goodsBrand.name}</option>
									</c:forEach>
	                            </select>                              
								<label>处理结果：</label>
								<form:select path="status"  class="form-control" style="width:185px;">
									<form:option value="">全部</form:option>
									<form:option value="1">未处理</form:option>
									<form:option value="2">已处理</form:option>
								</form:select>
								<label>用户名：</label>
								<input id="name" name="name" maxlength="10" type="text" class="form-control" placeholder="昵称/姓名">	
							</div>
						</form:form>
					</div><p></p>
					<!-- 工具栏 -->
					<div class="row">
						<div class="col-sm-12">		
						    <c:if test="${stamp != '1'}">
				                                                      说明：
				        		<img alt="" src="${ctxStatic}/ec/images/u655.png" style="width: 16px;height: 16px">别人给我的&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
					            <img alt="" src="${ctxStatic}/ec/images/u657.png" style="width: 16px;height: 16px">自己的&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
					            <img alt="" src="${ctxStatic}/ec/images/u730.png" style="width: 16px;height: 16px">我给别人的&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;	
					        </c:if>              				         
								<table:addRow url="${ctx}/crm/store/from?mobile=${mobile}" width="800px" height="680px" title="投诉咨询"></table:addRow><!-- 增加按钮 -->
							<div class="pull-right">
								<button  class="btn btn-primary btn-rounded btn-outline btn-sm " onclick="search()" ><i class="fa fa-search"></i> 查询</button>
								<button  class="btn btn-primary btn-rounded btn-outline btn-sm " onclick="reset()" ><i class="fa fa-refresh"></i> 重置</button>
							</div>
						</div>
					</div>
					<p></p>
					<table id="treeTable" class="table table-striped table-bordered table-hover table-condensed dataTables-example dataTable">
						<thead>
							<tr>
								<th style="text-align: center;">ID</th>
								<th style="text-align: center;">投诉主题</th>
								<th style="text-align: center;">分类</th>
								<th style="text-align: center;">品牌</th>
								<th style="text-align: center;">客户昵称</th>
								<th style="text-align: center;">真实姓名</th>							
								<th style="text-align: center;">客户类型</th>
								<th style="text-align: center;">日期</th>							
								<th style="text-align: center;">时间</th>
								<th style="text-align: center;">紧急程度</th>							
								<th style="text-align: center;">首次接待人</th>					
								<th style="text-align: center;">最后处理人</th>
								<th style="text-align: center;">状态</th>
								<th style="text-align: center;">转交次数
								<th style="text-align: center;">操作</th>
							</tr>
						</thead>
						<tbody>
							<c:forEach items="${page.list}" var="complain">
							<tr style="text-align: center;">
								<td>${complain.id}</td>
								<td style="text-align:left;">
								  <c:if test="${stamp != '1'}">
								    <c:choose>
									    <c:when test="${complain.questionSource == 3}">
									       <img alt="" src="${ctxStatic}/ec/images/u655.png" style="width: 16px;height: 16px">
									    </c:when>
									    <c:when test ="${complain.questionSource == 1}">
									        <img alt="" src="${ctxStatic}/ec/images/u657.png" style="width: 16px;height: 16px">
									    </c:when>
										<c:when test ="${complain.questionSource == 2}">
									        <img alt="" src="${ctxStatic}/ec/images/u730.png" style="width: 16px;height: 16px">
									    </c:when>
								    </c:choose>
								  </c:if>${complain.theme}</td>								   
								<td><c:if test="${complain.questionType== '1'}">投诉</c:if>
								    <c:if test="${complain.questionType== '2'}">咨询</c:if>
									<c:if test="${complain.questionType== '3'}">销售机会</c:if>
									<c:if test="${complain.questionType=='4'}">后台管理</c:if>
					            </td>										
								<td>${complain.brandType}</td>
								<td>${complain.nickName}</td>
								<td>${complain.name}</td>
								<td>
								    <c:if test="${complain.member== '1'}">会员</c:if>
								    <c:if test="${complain.member== '2'}">非会员</c:if>
					            </td>	
								<td><fmt:formatDate value="${complain.creatDate}"
										pattern="yyyy-MM-dd HH:mm:ss" /> </td>
                                   <td><c:if test="${complain.recordTime== '1'}">0-5分钟</c:if>
								    <c:if test="${complain.recordTime== '2'}">6-15分钟</c:if>
									<c:if test="${complain.recordTime== '3'}">15-30分钟</c:if>
									<c:if test="${complain.recordTime=='4'}">30~~分钟</c:if>
					            </td>
								<td><c:if test="${complain.degree== '1'}">非常紧急</c:if>
								    <c:if test="${complain.degree== '2'}">紧急</c:if>
									<c:if test="${complain.degree== '3'}">普通</c:if>
					            </td>														
								<td>${complain.creatBy}</td>
								<td>${complain.handler}</td>
								<td><c:if test="${complain.status== '1'}">未处理</c:if>
								    <c:if test="${complain.status== '2'}">已处理</c:if>
					            </td>								
								<td>${complain.changeTimes}</td>
								<td> <a href="#" onclick="openDialog('详情', '${ctx}/crm/store/detailed?id=${complain.id}&stamp=${stamp}','800px','680px')"  class="btn btn-success btn-xs" ><i class="fa fa-search-plus"></i>详情</a></td>
 							   </tr>
							</c:forEach>
						</tbody>
					</table>
					<!-- 分页代码 -->
					<table:page page="${page}"></table:page>
				</div>
			</div>
		</div>
	</div>
</body>
</html>