<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/webpage/include/taglib.jsp"%>
<html>
<head>
	<title>直播/回放信息表</title>
	<meta name="decorator" content="default"/>
	<script type="text/javascript">
		//重置表单
		function resetnew(){
			$("#name").val("");
			reset();
		}
		function page(n,s){
			$("#pageNo").val(n);
			$("#pageSize").val(s);
			$("#searchForm").submit();
	    	return false;
	    }
		
		//刷新
		function refresh(){
			window.location="${ctx}/forms/show/byidshow";
		}
		
		//二级分类
		function opentwo(){
			$("#showLiveTwo").empty();
			$("#showLiveTwo").html(showLiveTwo).val();
			$("#showLiveThree").html(showLiveThree).val();
			$("#showLiveTwo").prepend("<option value='' selected='selected'>请选择二级分类</option>");
			$("#showLiveThree").prepend("<option value='' selected='selected'>请选择三级分类</option>");
		 	var categorytwo = $("#categoryTwo").val();
			var showId=$("#showLiveOne").val();
			$.ajax({
				type : "POST",   
				url : "${ctx}/forms/show/twoclass",
				data:{showId:showId},
				dataType: 'json',
				async: false,
				success: function(data) {
					$.each(data, function(index,item){
						if(categorytwo == item.showId){
							$("#showLiveTwo").prepend("<option value='"+item.showId+"' selected>"+item.showLiveTwo+"</option>");
						}else{
							$("#showLiveTwo").prepend("<option value="+item.showId+">"+item.showLiveTwo+"</option>");
						}
					});
				}
			});    
		}
		
		//三级分类
		function openthree(){
			$("#showLiveThree").empty();
			$("#showLiveThree").prepend("<option value='' selected='selected'>请选择三级分类</option>");
			var showIdThree=$("#showLiveTwo").val();
			var categoryThree = $("#categoryThree").val();
			$.ajax({
				type : "POST",   
				url : "${ctx}/forms/show/twoclass",
				data:{showId:showIdThree},
				dataType: 'json',
				async: false,
				success: function(data) {
					$.each(data, function(index,item){
						if(categoryThree == item.showId){
							$("#showLiveThree").prepend("<option value='"+item.showId+"' selected>"+item.showLiveTwo+"</option>");
						}else{
							$("#showLiveThree").prepend("<option value="+item.showId+">"+item.showLiveTwo+"</option>");
						}
					});
				}
			}); 
		}
		
		//就绪函数
		$(document).ready(function() {
			var start = {
				    elem: '#begtime',
				    format: 'YYYY-MM-DD',
				    event: 'focus',
				    max: $("#endtime").val(),   //最大日期
				    istime: false,				//是否显示时间
				    isclear: false,				//是否显示清除
				    istoday: false,				//是否显示今天
				    issure: true,				//是否显示确定
				    festival: true,				//是否显示节日
				    choose: function(datas){
				         end.min = datas; 		//开始日选好后，重置结束日的最小日期
				         end.start = datas 		//将结束日的初始值设定为开始日
				    }
				};
			var end = {
					    elem: '#endtime',
					    format: 'YYYY-MM-DD',
					    event: 'focus',
					    min: $("#begtime").val(),
					    istime: false,
					    isclear: false,
					    istoday: false,
					    issure: true,
					    festival: true,
					    choose: function(datas){
					        start.max = datas; //结束日选好后，重置开始日的最大日期
					    }
					};
			laydate(start);
			laydate(end);
			
			var categoryOne = $("#categoryOne").val();
			if(null != categoryOne && '' != categoryOne){
				opentwo();
			}
			
			var categoryTwo = $("#categoryTwo").val();
			if(null != categoryTwo && '' != categoryTwo){
				openthree();
			}
			
			var pay = $("#pay").val();
			if(null != pay && '' != pay){
				$("#pay").empty();
			}
	   });
	</script>
</head>

<body class="gray-bg">
	<div class="wrapper wrapper-content">
		<div class="ibox">
			<div class="ibox-title">
				<h5>直播/回放信息表</h5>
			</div>
			 <sys:message content="${message}"/>
			 	<!-- 查询条件 -->
				<div class="ibox-content">
				<div class="clearfix">
				<form:form id="searchForm" action="${ctx}/forms/show/byidshow" method="post" class="form-inline">
					<input id="pageNo" name="pageNo" type="hidden" value="${page.pageNo}"/>
					<input id="pageSize" name="pageSize" type="hidden" value="${page.pageSize}"/>
					<table:sortColumn id="orderBy" name="orderBy" value="${page.orderBy}" callback="sortOrRefresh();"/><!-- 支持排序 -->
					<div class="form-group">
						<label>查询日期：</label>
						<input id="begtime" name="begtime" type="text" maxlength="20" class="laydate-icon form-control layer-date input-sm"
							value="<fmt:formatDate value="${tvShowReport.begtime}" pattern="yyyy-MM-dd"/>" style="width:185px;" placeholder="查询时间" readonly="readonly"/>
					 			一
						<input id="endtime" name="endtime" type="text" maxlength="20" class=" laydate-icon form-control layer-date input-sm" 
							value="<fmt:formatDate value="${tvShowReport.endtime}" pattern="yyyy-MM-dd"/>"  style="width:185px;" placeholder="结束时间" readonly="readonly"/>
						<p></p>
						<label>直播ID：<input id="showLiveId" name="showLiveId" maxlength="100" type="text" class="form-control" value="${tvShowReport.showLiveId}" ></label> 
						<label>主播姓名：<input id="showManName" name="showManName" maxlength="100" type="text" class="form-control" value="${tvShowReport.showManName}" ></label> 
						<label>直播标题：<input id="showLiveTitle" name="showLiveTitle" maxlength="100" type="text" class="form-control" value="${tvShowReport.showLiveTitle}" ></label> 
					 	<select class="form-control" id="isPay" name="isPay">
									<option value="">是否付费</option>
									<option value="1" <c:if test="${pay == '1'}">selected </c:if>>是</option>
									<option value="2" <c:if test="${pay == '2'}">selected </c:if>>否</option>
						</select> 
						
						<select class="form-control" id="showLiveOne" name="showLiveOne" onchange="opentwo()">
							<option value="" selected="selected">请选择一级分类</option>
							<c:forEach items="${listone}" var="listone">
								<option value="${listone.showId}" <c:if test="${categoryOne == listone.showId}">selected</c:if>>${listone.showLiveOne}</option>
							</c:forEach>
						</select>
						<input type="hidden" id="categoryOne" name="categoryOne" value="${categoryOne}"/>
						<input type="hidden" id="categoryTwo" name="categoryTwo" value="${categoryTwo}"/>
						<input type="hidden" id="categoryThree" name="categoryThree" value="${categoryThree}"/>
						<input type="hidden" id="pay" name="pay" value="${pay}"/>
						<select class="form-control" id="showLiveTwo" name="showLiveTwo" onchange="openthree()" >
							<option value="" selected="selected" >请选择二级分类</option>
						</select>
						<select class="form-control" id="showLiveThree" name="showLiveThree">
							<option value="" selected="selected">请选择三级分类</option>
						</select>
					 	<!-- 查询，重置按钮 -->
						<button type="button" class="btn btn-primary btn-rounded btn-outline btn-sm" onclick="search()">
							<i class="fa fa-search"></i> 搜索
						</button>
						<button type="button" class="btn btn-primary btn-rounded btn-outline btn-sm" onclick="resetnew()" >
							<i class="fa fa-refresh"></i> 重置
						</button>
					</div>
				</form:form>
					<!-- 工具栏 -->
					<div class="row" style="padding-top:10px;">
					<div class="col-sm-12">
						<div class="pull-left">
							<!-- 导出按钮 -->
							<table:exportExcel url="${ctx}/forms/show/exportinfo"></table:exportExcel>
							<button class="btn btn-white btn-sm " data-toggle="tooltip" data-placement="left" onclick="refresh()" title="刷新">
								<i class="glyphicon glyphicon-repeat"></i> 刷新
							</button>
						</div>
					</div>
				</div>
				</div>
				<table id="contentTable" class="table table-striped table-bordered  table-hover table-condensed  dataTables-example dataTable no-footer">
					<thead>
						<tr>
							<th style="text-align: center;">直播ID</th>
							<th style="text-align: center;">主播姓名</th>
							<th style="text-align: center;">直播标题</th>
							<th style="text-align: center;">直播播放次数</th>
							<th style="text-align: center;">直播观看人数</th>
							<th style="text-align: center;">收藏量</th>
							<th style="text-align: center;">点赞量</th>
							<th style="text-align: center;">是否付费</th>
							<th style="text-align: center;">商家</th>
							<th style="text-align: center;">区域</th>
							<th style="text-align: center;">集团军</th>
							<th style="text-align: center;">市场</th>
							<th style="text-align: center;">门店</th>
							<th style="text-align: center;">职位</th>
							<th style="text-align: center;">角色</th>
							<th style="text-align: center;">直播一级分类</th>
							<th style="text-align: center;">直播二级分类</th>
							<th style="text-align: center;">直播三级分类</th>
							<th style="text-align: center;">开始直播时间</th>
							<th style="text-align: center;">结束直播时间</th>
						</tr>
					</thead>
					<tbody style="text-align: center;">
						<c:forEach items="${page.list}" var="tv" varStatus="status">
							<tr>
								<td>${tv.showLiveId}</td>
								<td>${tv.showManName}</td>
								<td>${tv.showLiveTitle}</td>
								<td>${tv.showLiveNum}</td>
								<td>${tv.showLiveLookNum}</td>
								<td>${tv.collectNum}</td>
								<td>${tv.praiseNum}</td>
								<td>${tv.isPay}</td>
								<td>${tv.stairOne}</td>
								<td>${tv.stairTwo}</td>
								<td>${tv.stairThree}</td>
								<td>${tv.stairFour}</td>
								<td>${tv.stairFive}</td>
								<td>${tv.job}</td>
								<td>${tv.role}</td>
								<td>${tv.showLiveOne}</td>
								<td>${tv.showLiveTwo}</td>
								<td>${tv.showLiveThree}</td>
								<td>${tv.showLivebegtime}</td>
								<td>${tv.showLiveendtime}</td>
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