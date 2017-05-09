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
    </script>
    <title>地推管理</title>
</head>
<body>
	<div class="wrapper-content">
        <div class="ibox">
            <div class="ibox-title">
                <h5>地推管理</h5>
            </div>
            <sys:message content="${message}"/>
            <div class="ibox-content">
                <div class=" clearfix">
                    <form:form class="navbar-form navbar-left searcharea"  id="searchForm" modelAttribute="trainGtpRelations" action="${ctx}/train/gtpRelations/list" method="post">
                    	<!-- 分页隐藏文本框 -->
						<input id="pageNo" name="pageNo" type="hidden" value="${page.pageNo}"/>
						<input id="pageSize" name="pageSize" type="hidden" value="${page.pageSize}"/>
                        <div class="form-group">
                            <label><form:input path="user.name" class="form-control" placeholder="地推人员名称"/></label>  
                            <label><form:input path="user.mobile" class="form-control" placeholder="电话号码"/></label>  
                            <span>归属机构：</span>
							<sys:treeselect id="office" name="office.id" value="${trainGtpRelations.office.id}" labelName="office.name" labelValue="${trainGtpRelations.office.name}" title="部门"
								url="/sys/office/treeData?type=2" cssClass=" form-control input-sm" allowClear="true" notAllowSelectRoot="false" notAllowSelectParent="false" />
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
								<th style="text-align: center;">地推姓名</th>
								<th style="text-align: center;">电话号码</th>
							    <th style="text-align: center;">所属机构</th>
							    <th style="text-align: center;">邀请总人数</th>
						    	<th style="text-align: center;">操作</th>
							</tr>
						</thead>
						<tbody style="text-align: center;">
							<c:forEach items="${page.list}" var="list">
								<tr>
								  	<td>${list.user.name }</td>
								  	<td>${list.user.mobile }</td>
								  	<td>${list.user.office.name }</td>
								  	<td>${list.gtpNnm }</td>
								    <td>
							    		<shiro:hasPermission name="train:gtpRelations:view">
							    			<button class="btn btn-info btn-xs" onclick='top.openTab("${ctx}/train/gtpRelations/form?mtmyUserId=${list.user.mtmyUserId}","邀请详情", false)'>查看</button>
							    		</shiro:hasPermission>
								    </td>
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