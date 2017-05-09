<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/webpage/include/taglib.jsp"%>
<html>
<head>
    <meta charset="utf-8">
    <meta name="decorator" content="default"/>
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
			    max: $("#endDate").val(),   //最大日期
			    istime: false,				//是否显示时间
			    isclear: false,				//是否显示清除
			    istoday: false,				//是否显示今天
			    issure: false,				//是否显示确定
			    festival: true,				//是否显示节日
			    choose: function(datas){
			         end.min = datas; 		//开始日选好后，重置结束日的最小日期
			         end.start = datas 		//将结束日的初始值设定为开始日
			    }
			};
			var end = {
			    elem: '#endDate',
			    format: 'YYYY-MM-DD',
			    event: 'focus',
			    min: $("#beginDate").val(),
			    istime: false,
			    isclear: false,
			    istoday: false,
			    issure: false,
			    festival: true,
			    choose: function(datas){
			        start.max = datas; //结束日选好后，重置开始日的最大日期
			    }
			};
			laydate(start);
			laydate(end);
		})
    </script>
    <title>邀请详情</title>
</head>
<body>
	<div class="wrapper-content">
        <div class="ibox">
            <div class="ibox-title">
                <h5>邀请详情</h5>
            </div>
            <sys:message content="${message}"/>
            <div class="ibox-content">
                <div class=" clearfix">
                    <form:form class="navbar-form navbar-left searcharea"  id="searchForm" modelAttribute="trainGtpRelations" action="${ctx}/train/gtpRelations/form" method="post">
                    	<!-- 分页隐藏文本框 -->
						<input id="pageNo" name="pageNo" type="hidden" value="${page.pageNo}"/>
						<input id="pageSize" name="pageSize" type="hidden" value="${page.pageSize}"/>
						<input id="mtmyUserId" name="mtmyUserId" type="hidden" value="${trainGtpRelations.mtmyUserId}"/>
                        <div class="form-group">
                        	时间范围：<input id="beginDate" name="beginDate" type="text" maxlength="20" class="laydate-icon form-control layer-date input-sm"
								value="<fmt:formatDate value="${trainGtpRelations.beginDate}" pattern="yyyy-MM-dd"/>" readonly="readonly"/>
								<label>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;--&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</label>
								<input id="endDate" name="endDate" type="text" maxlength="20" class=" laydate-icon form-control layer-date input-sm"
									value="<fmt:formatDate value="${trainGtpRelations.endDate}" pattern="yyyy-MM-dd"/>" readonly="readonly"/>
                        </div>
                    </form:form>
                    <div class="pull-right">
						<button  class="btn btn-primary btn-rounded btn-outline btn-sm " onclick="search()" ><i class="fa fa-search"></i> 查询</button>
						<button  class="btn btn-primary btn-rounded btn-outline btn-sm " onclick="reset()" ><i class="fa fa-refresh"></i> 重置</button>
					</div>
                </div>
                <div class="" id="reviewlists">
					<table id="contentTable" class="table table-striped table-bordered  table-hover table-condensed  dataTables-example dataTable no-footer">
						<thead>
							<tr>
								<th style="text-align: center;">用户昵称</th>
								<th style="text-align: center;">电话号码</th>
							    <th style="text-align: center;">注册时间</th>
							    <th style="text-align: center;">邀请人姓名</th>
							</tr>
						</thead>
						<tbody style="text-align: center;">
							<c:forEach items="${page.list}" var="list">
								<tr>
								  	<td>${list.mtmyUserName }</td>
								  	<td>${list.mobile }</td>
								  	<td><fmt:formatDate value="${list.users.regtime}" pattern="yyyy-MM-dd HH:mm:ss"/></td>
								  	<td>${list.user.name }</td>
								</tr>
							</c:forEach>
						</tbody>
					</table>
				</div>
                <table:page page="${page}"></table:page>
	        </div>
	    </div>
    </div>
    <div class="loading"></div>
</body>
</html>