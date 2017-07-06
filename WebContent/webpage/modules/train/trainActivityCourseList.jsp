<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/webpage/include/taglib.jsp"%>
<html>
<head>
    <meta charset="utf-8">
    <meta name="decorator" content="default"/>
    <link rel="stylesheet" href="${ctxStatic}/ec/css/loading.css">
    <script>
	   function search(){//查询，页码清零
			$("#pageNo").val(0);
			$("#searchForm").submit();
	   		return false;
	   }
		function page(n,s){//翻页
			$("#pageNo").val(n);
			$("#pageSize").val(s);
			$("#searchForm").submit();
			return false;
		}
		$(document).ready(function() {
		    var start = {
			    elem: '#beginDate',
			    format: 'YYYY-MM-DD',
			    event: 'focus',
			    max: $("#lastDate").val(),   //最大日期
			    istime: false,				//是否显示时间
			    isclear: true,				//是否显示清除
			    istoday: false,				//是否显示今天
			    issure: true,				//是否显示确定
			    festival: true,				//是否显示节日
			    choose: function(datas){
			         last.min = datas; 		//开始日选好后，重置结束日的最小日期
			         last.start = datas 		//将结束日的初始值设定为开始日
			    }
			};
			var last = {
			    elem: '#lastDate',
			    format: 'YYYY-MM-DD',
			    event: 'focus',
			    min: $("#beginDate").val(),
			    istime: false,
			    isclear: true,
			    istoday: false,
			    issure: true,
			    festival: true,
			    choose: function(datas){
			        start.max = datas; //结束日选好后，重置开始日的最大日期
			    }
			};
			laydate(start);
			laydate(last);
	    })
	    //是否推荐/是否显示 改变事件
		function changeTableVal(flag,id,isyesno){
			$(".loading").show();//打开展示层
			$.ajax({
				type : "POST",
				url : "${ctx}/train/trainActivityCourse/updateFlag?acId="+id+"&FLAG="+flag+"&ISYESNO="+isyesno,
				dataType: 'json',
				success: function(data) {
					$(".loading").hide(); //关闭加载层
					var status = data.STATUS;
					var isyesno = data.ISYESNO;
					if("OK" == status){
						$("#"+flag+id).html("");//清除DIV内容	
						if(isyesno == 1){
							//当前状态为【否】，则打开
							$("#"+flag+id).append("<img width='20' height='20' src='${ctxStatic}/ec/images/cancel.png' onclick=\"changeTableVal('"+flag+"','"+id+"',0)\">");
						}else if(isyesno == 0){
							//当前状态为【是】，则取消
							$("#"+flag+id).append("<img width='20' height='20' src='${ctxStatic}/ec/images/open.png' onclick=\"changeTableVal('"+flag+"','"+id+"',1)\">");
						}
					}else if("ERROR" == status){
						alert(data.MESSAGE);
					}
				}
			});   
		}
    </script>
    <title>活动管理</title>
</head>
<body>
	<div class="wrapper-content">
        <div class="ibox">
            <div class="ibox-title">
                <h5>活动管理</h5>
            </div>
            <sys:message content="${message}"/>
            <div class="ibox-content">
                <div class=" clearfix">
                    <form:form class="navbar-form navbar-left searcharea"  id="searchForm" modelAttribute="trainActivityCourse" action="${ctx}/train/trainActivityCourse/list" method="post">
                    	<!-- 分页隐藏文本框 -->
						<input id="pageNo" name="pageNo" type="hidden" value="${page.pageNo}"/>
						<input id="pageSize" name="pageSize" type="hidden" value="${page.pageSize}"/>
						<div class="form-group">
							<form:input path="name" htmlEscape="false" placeholder="活动名称" class=" form-control input-sm" />
							  报名时间：<input id="beginDate" name="beginDate" type="text" maxlength="20" class="laydate-icon form-control layer-date input-sm"
											value="<fmt:formatDate value="${trainActivityCourse.beginDate}" pattern="yyyy-MM-dd"/>" readonly="readonly"/>
										<label>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;--&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</label>
										<input id="lastDate" name="lastDate" type="text" maxlength="20" class=" laydate-icon form-control layer-date input-sm"
											value="<fmt:formatDate value="${trainActivityCourse.lastDate}" pattern="yyyy-MM-dd"/>" readonly="readonly"/>
							  活动状态：<select class="form-control" style="text-align: center;width: 150px;" id="status" name="status">
											<option value="0" ${(trainActivityCourse.status == '0')?'selected="selected"':''}>全部</option>
											<option value="1" ${(trainActivityCourse.status == '1')?'selected="selected"':''}>未开始</option>
											<option value="2" ${(trainActivityCourse.status == '2')?'selected="selected"':''}>报名中</option>
											<option value="3" ${(trainActivityCourse.status == '3')?'selected="selected"':''}>已结束</option>
										</select>
						</div>
                    </form:form>
                </div>
                <!-- 工具栏 -->
				<div class="row">
					<div class="col-sm-12">
						<div class="pull-left">
							<shiro:hasPermission name="train:trainActivityCourse:add">
								<!-- 增加按钮 -->
								<table:addRow url="${ctx}/train/trainActivityCourse/form" title="活动" width="800px" height="650px"></table:addRow>
							</shiro:hasPermission>
						</div>
						<div class="pull-right">
							<button class="btn btn-primary btn-rounded btn-outline btn-sm " onclick="search()">
								<i class="fa fa-search"></i> 查询
							</button>
							<button class="btn btn-primary btn-rounded btn-outline btn-sm " onclick="reset()">
								<i class="fa fa-refresh"></i> 重置
							</button>
						</div>
					</div>
				</div>
				<table id="contentTable" class="table table-striped table-bordered  table-hover table-condensed  dataTables-example dataTable no-footer">
					<thead>
						<tr>
							<th style="text-align: center;">活动名称</th>
							<th style="text-align: center;">报名开始时间</th>
						    <th style="text-align: center;">报名结束时间</th>
						    <th style="text-align: center;">报名费</th>
						    <th style="text-align: center;">排序</th>
						    <th style="text-align: center;">上架</th>
						    <th style="text-align: center;">活动状态</th>
					    	<th style="text-align: center;">操作</th>
						</tr>
					</thead>
					<tbody style="text-align: center;">
						<c:forEach items="${page.list}" var="list">
							<tr>
							  	<td>${list.name }</td>
								<td><fmt:formatDate value="${list.startDate }" pattern="yyyy-MM-dd HH:mm:ss"/></td>
							  	<td><fmt:formatDate value="${list.endDate }" pattern="yyyy-MM-dd HH:mm:ss"/></td>
							  	<td>${list.amount }</td>
							  	<td>${list.sort }</td>
							  	<td id="ISONSALE${list.acId}">
							  		<c:if test="${list.isOnSale == 1}">
										<img width="20" height="20" src="${ctxStatic}/ec/images/cancel.png" onclick="changeTableVal('ISONSALE','${list.acId}',0)">
									</c:if>
									<c:if test="${list.isOnSale == 0}">
										<img width="20" height="20" src="${ctxStatic}/ec/images/open.png" onclick="changeTableVal('ISONSALE','${list.acId}',1)">
									</c:if>
							  	</td>
							  	<td>
							  		<c:if test="${list.status == 1 }">未开始</c:if>
							  		<c:if test="${list.status == 2 }">报名中</c:if>
							  		<c:if test="${list.status == 3 }">已结束</c:if>
							  	</td>
							    <td>
						    		<shiro:hasPermission name="	train:trainActivityCourse:view">
										<a href="#" onclick="openDialogView('查看活动', '${ctx}/train/trainActivityCourse/form?acId=${list.acId}','800px', '650px')" class="btn btn-info btn-xs" ><i class="fa fa-search-plus"></i> 查看</a>
									</shiro:hasPermission>
							    </td>
							</tr>
						</c:forEach>
					</tbody>
				</table>
                <table:page page="${page}"></table:page>
	        </div>
	    </div>
    </div>
    <div class="loading"></div>
</body>
</html>