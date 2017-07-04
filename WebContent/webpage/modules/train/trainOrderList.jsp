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
					<form:form class="navbar-form navbar-left searcharea"  id="searchForm" modelAttribute="trainOrder" action="${ctx}/train/trainActivityCourse/findTrainOrder" method="post">
                    	<!-- 分页隐藏文本框 -->
						<input id="pageNo" name="pageNo" type="hidden" value="${page.pageNo}"/>
						<input id="pageSize" name="pageSize" type="hidden" value="${page.pageSize}"/>
						<div class="form-group">
							<form:input path="goodsName" htmlEscape="false" placeholder="活动名称" class=" form-control input-sm" />
							<form:input path="user.name" htmlEscape="false" placeholder="用户名" class=" form-control input-sm" />
							<form:input path="user.mobile" htmlEscape="false" placeholder="电话号码" class=" form-control input-sm" />
							  报名时间：<input id="beginDate" name="beginDate" type="text" maxlength="20" class="laydate-icon form-control layer-date input-sm"
											value="<fmt:formatDate value="${trainOrder.beginDate}" pattern="yyyy-MM-dd"/>" readonly="readonly"/>
										<label>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;--&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</label>
										<input id="lastDate" name="lastDate" type="text" maxlength="20" class=" laydate-icon form-control layer-date input-sm"
											value="<fmt:formatDate value="${trainOrder.lastDate}" pattern="yyyy-MM-dd"/>" readonly="readonly"/>
						</div>
                    </form:form>
                </div>
                <!-- 工具栏 -->
				<div class="row">
					<div class="col-sm-12">
						<div class="pull-left">
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
							<th style="text-align: center;">用户名</th>
						    <th style="text-align: center;">电话号码</th>
						    <th style="text-align: center;">性别</th>
						    <th style="text-align: center;">报名时间</th>
						    <th style="text-align: center;">支付金额</th>
						    <th style="text-align: center;">报名状态</th>
						</tr>
					</thead>
					<tbody style="text-align: center;">
						<c:forEach items="${page.list}" var="list">
							<tr>
							  	<td>${list.goodsName }</td>
							  	<td>${list.user.name }</td>
							  	<td>${list.user.mobile }</td>
							  	<td>
								  	<c:if test="${list.user.sex == 1}">男</c:if>
								  	<c:if test="${list.user.sex == 2}">女</c:if>
							  	</td>
								<td><fmt:formatDate value="${list.payTime }" pattern="yyyy-MM-dd HH:mm:ss"/></td>
							  	<td>${list.amount }</td>
							  	<td>
							  		支付成功
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