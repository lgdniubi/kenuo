<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/webpage/include/taglib.jsp"%>
<html>
<head>
	<title>健康档案</title>
	<meta name="decorator" content="default"/>
</head>
<body class="gray-bg">
	<div class="wrapper wrapper-content">
		<div class="ibox">
			<div class="ibox-title">
				<h5>健康档案</h5>
			</div>
			<sys:message content="${message}"/>
			<!-- 查询条件 -->
	    	<div class="ibox-content">
				<div class="clearfix">
					<form id="searchForm" modelAttribute="" action="" method="post" class="navbar-form navbar-left searcharea">
						<!-- 翻页隐藏文本框 -->
						<input id="pageNo" name="pageNo" type="hidden" value="${page.pageNo}"/>
						<input id="pageSize" name="pageSize" type="hidden" value="${page.pageSize}"/>
						<table:sortColumn id="orderBy" name="orderBy" value="${page.orderBy}" callback="sortOrRefresh();"/><!-- 支持排序 -->
					</form>
				</div>
				<table id="contentTable" class="table table-striped table-bordered  table-hover table-condensed  dataTables-example dataTable no-footer">
					<thead>
						<tr>
							<th style="text-align: center;">商品名称</th>
						    <th style="text-align: center;">总服务次数</th>
						    <th style="text-align: center;">已服务次数</th>
						    <th style="text-align: center;">剩余次数</th>
						    <th style="text-align: center;">服务时间</th>
						    <th style="text-align: center;">服务状态</th>
						    <th style="text-align: center;">服务店铺</th>
						    <th style="text-align: center;">操作</th>
						</tr>
					</thead>
					<tbody style="text-align: center;">
						<tr>
						  	<td>奥玛生机</td>
						  	<td>10</td>
						  	<td>1</td>
						  	<td>9</td>
						  	<td>/</td>
						  	<td>/</td>
						  	<td>北京万达店</td>
						  	<td>
						  		<a href="#" onclick='top.openTab("${ctx}/ec/mtmyuser/addrecord","服务记录", false)' class="btn btn-primary">增加记录</a>
						  	</td>
						</tr>
						<tr>
						  	<td>/</td>
						  	<td>/</td>
						  	<td>2</td>
						  	<td>8</td>
						  	<td>2016-1-1 10:10-- 2016-1-1 12:10 </td>
						  	<td>已完成</td>
						  	<td>北京万达店</td>
						  	<td>
						  		<button type="button" class="btn btn-primary" disabled="disabled">健康笔记</button>
						  	</td>
						</tr>
						<tr>
						  	<td>深海面膜</td>
						  	<td>10</td>
						  	<td>1</td>
						  	<td>9</td>
						  	<td>/</td>
						  	<td>/</td>
						  	<td>北京万达店</td>
						  	<td>
						  		<a href="#" onclick='top.openTab("${ctx}/ec/mtmyuser/addrecord","服务记录", false)' class="btn btn-primary">增加记录</a>
						  	</td>
						</tr>
						<tr>
						  	<td>/</td>
						  	<td>/</td>
						  	<td>2</td>
						  	<td>8</td>
						  	<td>2016-1-1 10:10-- 2016-1-1 12:10 </td>
						  	<td>已完成</td>
						  	<td>北京万达店</td>
						  	<td>
						  		<button type="button" class="btn btn-primary" disabled="disabled">健康笔记</button>
						  	</td>
						</tr>
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
	<div class="loading"></div>
</body>
</html>