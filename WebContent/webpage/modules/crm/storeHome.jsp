<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/webpage/include/taglib.jsp"%>
<%@ include file="/webpage/include/icheck.jsp"%>

<html>
<head>
	<title>客服首页</title>
	<meta name="decorator" content="default"/>
	<link rel="stylesheet" href="${ctxStatic}/ec/css/loading.css">
	<link rel="stylesheet" href="${ctxStatic}/ec/css/base.css">
	<script type="text/javascript">
		function search(){
			var mobile = $("#mobile").val();	
			if(mobile == ""){
				return;
			}
			$.ajax({
				type:"post",
				data:{
					mobile:mobile
				},
				url:"${ctx}/crm/store/member",
				dataType: 'json',
				success:function(date){
					if(date != null && date.userId !=null){	
						  window.location.href = '${ctx}/crm/user/userDetail?userId='+date.userId; 
					}else{
						if(date != null && date.mobile != null){
						    window.location.href = '${ctx}/crm/store/list?mobile='+mobile+'&stamp='+"1"; 
						}else{
							top.layer.alert('此号码不是会员,且无投诉记录', {icon: 0, title:'提醒'}); 
						}
					}
				},
				error:function(XMLHttpRequest,textStatus,errorThrown){
				}
			});
		}
	</script>
</head>

<body>
	<div class="wrapper wrapper-content">
		<div class="ibox">
			<!-- 标题 -->
			<div class="ibox-title">
				<h5>主页</h5>
			</div>
			<!-- 主体内容 -->
			<div class="ibox-content">
               <div class="row"><div class="col-md-12">					 		   
			   <div class="row">
				   <div class="col-md-4">
					  <form:form id="searchForm" action="${ctx}/crm/store/member" modelAttribute="goodsType" method="post">			
							客户查询：<input id="mobile" name="mobile" maxlength="20" type="text" class="form-control" style="width:220px" placeholder="昵称、手机号">	
							<button type="button" id="pushType" class="btn btn-primary btn-rounded btn-outline btn-sm" onclick="search()">
								<i class="fa fa-search"></i> 查询
							</button>			
					  </form:form>					
				  </div>				  
				  <div class="col-md-2">
				    <table:addRow url="${ctx}/crm/store/from" width="800px" height="680px" title="投诉咨询"></table:addRow><!-- 增加按钮 -->
		          </div>
				  <div class="col-md-6">
				  	当前来电：
		          </div>			
		      </div><br/>	  
			  <div class="row">
		          <div class="col-md-6">
					未处理（处理中的投诉咨询）<font color="red">(&nbsp;${coun}&nbsp;)</font>&nbsp;&nbsp;&nbsp;&nbsp;<a href="${ctx}/crm/store/list?status=1&member=1 " class="btn-xs">更多>></a>				
					 <table id="treeTable" class="table table-striped table-bordered table-hover table-condensed dataTables-example dataTable">
						<tbody>	
							<thead >
							 <tr>
								<th >ID</th>
								<th style="text-align: center;">主题</th>
								<th style="text-align: center;">类别</th>
								<th style="text-align: center;">品牌</th>
								<th style="text-align: center;">时间</th>
								<th style="text-align: center;">紧急程度</th>
							 </tr>
							</thead>		
							<c:forEach items="${com}" var="com">
							 <tr>
								<td>${com.id}</td>
								<td style="text-align:left;"><c:choose>
									<c:when test="${com.questionSource == 3}">
									       <img alt="" src="${ctxStatic}/ec/images/u655.png" style="width: 16px;height: 16px">
									    </c:when>
									    <c:when test ="${com.questionSource == 1}">
									        <img alt="" src="${ctxStatic}/ec/images/u657.png" style="width: 16px;height: 16px">
									    </c:when>
										<c:when test ="${com.questionSource == 2}">
									        <img alt="" src="${ctxStatic}/ec/images/u730.png" style="width: 16px;height: 16px">
									    </c:when>
								</c:choose>${com.theme}</td>
								<td style="text-align: left;">
									<c:if test="${com.questionType== '1'}">投诉</c:if>
								    <c:if test="${com.questionType == '2'}">咨询</c:if>
									<c:if test="${com.questionType == '3'}">销售机会</c:if>
									<c:if test="${com.questionType =='4'}">后台管理</c:if>
					            </td>										
								<td>${com.brandType}</td>
	                                  <td>
	                                  	<c:if test="${com.recordTime == '1'}">0-5分钟</c:if>
								    <c:if test="${com.recordTime == '2'}">6-15分钟</c:if>
									<c:if test="${com.recordTime == '3'}">15-30分钟</c:if>
									<c:if test="${com.recordTime =='4'}">30~~分钟</c:if>
					            </td>
								<td>
									<c:if test="${com.degree== '1'}">非常紧急</c:if>
								    <c:if test="${com.degree== '2'}">紧急</c:if>
									<c:if test="${com.degree== '3'}">普通</c:if>
					            </td>																						
	  						 </tr>
					    </c:forEach>
					 </tbody>
				  </table> 
		          </div>         				           			       			           
		           <div class="col-md-6">
		         			未处理的快速来电记录&nbsp;<font color="red">(&nbsp;${count}&nbsp;)</font>&nbsp;&nbsp;&nbsp;&nbsp;<a href="${ctx}/crm/store/list?member=2&stamp=1" class="btn-xs">更多>></a>
					<table id="treeTable" class="table table-striped table-bordered table-hover table-condensed dataTables-example dataTable">
						<thead>
							 <tr>
								<th style="text-align: center;">ID</th>
								<th style="text-align: center;">主题</th>
								<th style="text-align: center;">类别</th>
								<th style="text-align: center;">品牌</th>
								<th style="text-align: center;">时间</th>
								<th style="text-align: center;">紧急程度</th>
							 </tr>
						</thead>		
						<tbody>
							<c:forEach items="${complan}" var="complan">
							    <tr>
								<td>${complan.id}</td>
								<td style="text-align: left;">${complan.theme}</td>
								<td><c:if test="${complan.questionType== '1'}">投诉</c:if>
								    <c:if test="${complan.questionType== '2'}">咨询</c:if>
									<c:if test="${complan.questionType== '3'}">销售机会</c:if>
									<c:if test="${complan.questionType=='4'}">后台管理</c:if>
					            </td>										
								<td>${complan.brandType}</td>
	                                  <td><c:if test="${complan.recordTime== '1'}">0-5分钟</c:if>
								    <c:if test="${complan.recordTime== '2'}">6-15分钟</c:if>
									<c:if test="${complan.recordTime== '3'}">15-30分钟</c:if>
									<c:if test="${complan.recordTime=='4'}">30~~分钟</c:if>
					            </td>
								<td><c:if test="${complan.degree== '1'}">非常紧急</c:if>
								    <c:if test="${complan.degree== '2'}">紧急</c:if>
									<c:if test="${complan.degree== '3'}">普通</c:if>
					            </td>																						
	  						</tr>
						</c:forEach>
					</tbody>
				</table> 
              </div>
		     </div><br/><br/>
		     <div class="row">
		           <div class="col-md-12">说明：
		        		<img alt="" src="${ctxStatic}/ec/images/u655.png" style="width: 16px;height: 16px">别人给我的&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
			            <img alt="" src="${ctxStatic}/ec/images/u657.png" style="width: 16px;height: 16px">自己的&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
			            <img alt="" src="${ctxStatic}/ec/images/u730.png" style="width: 16px;height: 16px">我给别人的		           
		           </div>
		     </div><p></p>
		     <div class="row">
		           <div class="col-md-6">
							已处理（处理中的投诉咨询）<font color="red">(&nbsp;${counts}&nbsp;)</font>&nbsp;&nbsp;&nbsp;&nbsp;<a href="${ctx}/crm/store/list?status=2&member=1" class="btn-xs">更多>></a>					
						<table id="treeTable" class="table table-striped table-bordered table-hover table-condensed dataTables-example dataTable">
							<thead>
							 <tr>
								<th style="text-align: center;">ID</th>
								<th style="text-align: center;">主题</th>
								<th style="text-align: center;">类别</th>
								<th style="text-align: center;">品牌</th>
								<th style="text-align: center;">时间</th>
								<th style="text-align: center;">紧急程度</th>
							 </tr>
							</thead>		
							<tbody>
							<c:forEach items="${complans}" var="complans">
							  <tr style="text-align: center;">
								<td>${complans.id}</td>
								<td style="text-align:left;"><c:choose>
									    <c:when test="${complans.questionSource == 3}">
									       <img alt="" src="${ctxStatic}/ec/images/u655.png" style="width: 16px;height: 16px">
									    </c:when>
									    <c:when test ="${complans.questionSource == 1}">
									        <img alt="" src="${ctxStatic}/ec/images/u657.png" style="width: 16px;height: 16px">
									    </c:when>
										<c:when test ="${complans.questionSource == 2}">
									        <img alt="" src="${ctxStatic}/ec/images/u730.png" style="width: 16px;height: 16px">
									    </c:when>
								</c:choose>${complans.theme}</td>
								<td><c:if test="${complans.questionType== '1'}">投诉</c:if>
								    <c:if test="${complans.questionType== '2'}">咨询</c:if>
									<c:if test="${complans.questionType== '3'}">销售机会</c:if>
									<c:if test="${complans.questionType=='4'}">后台管理</c:if>
					            </td>										
								<td>${complans.brandType}</td>
	                                  <td><c:if test="${complans.recordTime== '1'}">0-5分钟</c:if>
								    <c:if test="${complans.recordTime== '2'}">6-15分钟</c:if>
									<c:if test="${complans.recordTime== '3'}">15-30分钟</c:if>
									<c:if test="${complans.recordTime=='4'}">30~~分钟</c:if>
					            </td>
								<td><c:if test="${complans.degree== '1'}">非常紧急</c:if>
								    <c:if test="${complans.degree== '2'}">紧急</c:if>
									<c:if test="${complans.degree== '3'}">普通</c:if>
					            </td>																						
	  						 </tr>
						</c:forEach>
							</tbody>
						  </table>  
						 </div>
				         <div class="col-md-6">
				                                                   通知：&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<a href="#" class="btn-xs">更多>></a>			
						  </div>
				     </div>
			 		</div>
				</div>
			</div>
		</div>
	</div>
</body>
</html>