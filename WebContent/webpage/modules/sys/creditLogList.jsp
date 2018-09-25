<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/webpage/include/taglib.jsp"%>
<html>
<head>
    <meta charset="utf-8">
    <meta name="decorator" content="default"/>
    <!-- 引入layui.css -->
	<link href="${ctxStatic}/layer-v2.0/layer/skin/layui.css" type="text/css" rel="stylesheet">
    <title>额度日志</title>
</head>
<body>
    <div class="ibox-content">
       <div class="clearfix">
			<div class="nav">
				<ul class="layui-tab-title">
					<li>
						<a href="${ctx}/sys/officeCredit/toEditCredit?office_id=${officeAcount.officeId}">额度调整</a>
					</li>
					<li class="layui-this">
						<a href="${ctx}/sys/officeCredit/creditLogList?officeId=${officeAcount.officeId}">额度日志</a>
					</li>
					
				</ul>
			</div>
		</div>
	</div>
	<div class="wrapper-content">
        <div class="ibox">
            <sys:message content="${message}"/>
              <div class=" clearfix">
                  <form:form class="navbar-form navbar-left searcharea"  id="searchForm" modelAttribute="officeAcount" action="${ctx}/sys/officeCredit/creditLogList?officeId=${officeAcount.officeId}" method="post">
                  	<!-- 分页隐藏文本框 -->
				<input id="pageNo" name="pageNo" type="hidden" value="${page.pageNo}"/>
				<input id="pageSize" name="pageSize" type="hidden" value="${page.pageSize}"/>
				
                  </form:form>
              </div>
              <table id="contentTable" class="table table-bordered table-hover table-striped">
                <thead> 
                  	 <tr>
                 			<th style="text-align: center;">时间</th>
                 			<th style="text-align: center;">操作人</th>
                 			<th style="text-align: center;">额度</th>
                 			<th style="text-align: center;">内容</th>
                 		</tr>
                </thead>
                   <tbody>
                     	<c:forEach items="${page.list}" var="officeAcount">
					<tr style="text-align: center;">
                              <td style="text-align: center;">
                             		<fmt:formatDate value="${officeAcount.createDate}" pattern="yyyy-MM-dd HH:mm:ss"/>
                             	</td>
                             	<td style="text-align: center;">
                             		${officeAcount.createBy.name}
                             	</td>
                             	<td style="text-align: center;">
                             		${officeAcount.creditLimit - officeAcount.oldCreditLimit}
                             	</td>
                             	<td style="text-align: center;">
                             		总额度由【${officeAcount.oldCreditLimit}】变更为【${officeAcount.creditLimit}】
                             	</td>
					</tr>
				</c:forEach>
                   </tbody>
                   <tfoot>
                      <tr>
                          <td colspan="20">
                              <div class="tfoot">
                              </div>
                              <!-- 分页代码 --> 
                             	<table:page page="${page}"></table:page>
                          </td>	
                      </tr>
               	</tfoot>
              </table>
	    </div>
    </div>
</body>
</html>