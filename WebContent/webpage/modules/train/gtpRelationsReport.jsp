<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/webpage/include/taglib.jsp"%>
<html>
<head>
    <meta charset="utf-8">
    <meta name="decorator" content="default"/>
    <script>
	   function search(){//查询，页码清零
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
                    <form:form class="navbar-form navbar-left searcharea"  id="searchForm" modelAttribute="trainGtpRelations" action="${ctx}/train/gtpRelations/report" method="post">
                        <div class="form-group">
                            <span>归属机构：</span>
							<sys:treeselect id="office" name="office.id" value="${trainGtpRelations.office.id}" labelName="office.name" labelValue="${trainGtpRelations.office.name}" title="部门"
								url="/sys/office/treeData?type=2" cssClass=" form-control input-sm" allowClear="true" notAllowSelectRoot="false" notAllowSelectParent="false" />
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
                	<font color="red" size="3" style="font-weight:bold;">${trainGtpRelations.office.name}邀请总人数:${totalNum }人&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
                		<c:choose>
                			<c:when test="${not empty trainGtpRelations.beginDate and not empty trainGtpRelations.endDate}">
                				<fmt:formatDate value="${trainGtpRelations.beginDate}" pattern="yyyy-MM-dd"/>至<fmt:formatDate value="${trainGtpRelations.endDate}" pattern="yyyy-MM-dd"/>
                			</c:when>
                			<c:when test="${not empty trainGtpRelations.beginDate and empty trainGtpRelations.endDate}">
                				<fmt:formatDate value="${trainGtpRelations.beginDate}" pattern="yyyy-MM-dd"/>至今
                			</c:when>
                			<c:when test="${empty trainGtpRelations.beginDate and not empty trainGtpRelations.endDate}">
                				截止<fmt:formatDate value="${trainGtpRelations.endDate}" pattern="yyyy-MM-dd"/>
                			</c:when>
                		</c:choose>
                	</font>
					<table id="contentTable" class="table table-striped table-bordered  table-hover table-condensed  dataTables-example dataTable no-footer">
						<thead>
							<tr>
								<th style="text-align: center;">编码</th>
							    <th style="text-align: center;">所属机构</th>
							    <th style="text-align: center;">邀请注册总人数</th>
							</tr>
						</thead>
						<tbody style="text-align: center;">
							<c:forEach items="${list}" var="list" varStatus="status">
								<tr>
								  	<td>${status.index + 1 }</td>
								  	<td>${list.office.name }</td>
								  	<td>${list.gtpNnm }</td>
								</tr>
							</c:forEach>
						</tbody>
					</table>
				</div>
	        </div>
	    </div>
    </div>
    <div class="loading"></div>
</body>
</html>