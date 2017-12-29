<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/webpage/include/taglib.jsp"%>
<!DOCTYPE html>
<html>
<head>
    <meta charset="utf-8">
   	<meta name="decorator" content="default"/>
    <meta name="viewport" content="width=device-width, initial-scale=1.0"> 
    <link rel="stylesheet" href="${ctxStatic}/ec/css/loading.css">
    <script>
	    function page(n,s){
			$("#pageNo").val(n);
			$("#pageSize").val(s);
			$("#searchForm").submit();
	    	return false;
	    }
	    
	    function newReset(){
	    	$("#cityIdsId").val("");
	    	$("#cityIdsName").val("");
	    	$("#newRatio").val("");
	    	$("#searchForm").submit();
	    }
    </script>
    <title>城市异价管理</title>
</head>
<body>
	<div class="wrapper-content">
        <div class="ibox">
            <sys:message content="${message}"/>
            <div class="ibox-content">
                <div class=" clearfix">
                	<form id="searchForm" action="${ctx}/ec/goodsPriceRatio/list" method="post" class="navbar-form navbar-left searcharea">
						<!-- 翻页隐藏文本框 -->
						<input id="pageNo" name="pageNo" type="hidden" value="${page.pageNo}"/>
						<input id="pageSize" name="pageSize" type="hidden" value="${page.pageSize}"/>
						<span>选择城市：</span>                                 
						<sys:treeselect id="cityIds" name="cityIds" value="${goodsPriceRatio.cityIds}" labelName="cityNames" labelValue="${goodsPriceRatio.cityNames}" title="城市" url="/sys/area/findListByPID" cssClass="form-control" notAllowSelectParent="true"/>
						<label>异价比例：</label>	
						<select id="newRatio" name="newRatio" class="form-control" style="width:185px;">
								<option value="">全部</option>
								<option <c:if test="${(goodsPriceRatio.minRatio == 0.01) && (goodsPriceRatio.maxRatio == 0.1)}">selected="selected"</c:if> value="0.01-0.1">1%-10%</option>
								<option <c:if test="${(goodsPriceRatio.minRatio == 0.11) && (goodsPriceRatio.maxRatio == 0.2)}">selected="selected"</c:if> value="0.11-0.2">11%-20%</option>
								<option <c:if test="${(goodsPriceRatio.minRatio == 0.21) && (goodsPriceRatio.maxRatio == 0.3)}">selected="selected"</c:if> value="0.21-0.3">21%-30%</option>
								<option <c:if test="${(goodsPriceRatio.minRatio == 0.31) && (goodsPriceRatio.maxRatio == 0.4)}">selected="selected"</c:if> value="0.31-0.4">31%-40%</option>
								<option <c:if test="${(goodsPriceRatio.minRatio == 0.41) && (goodsPriceRatio.maxRatio == 0.5)}">selected="selected"</c:if> value="0.41-0.5">41%-50%</option>
								<option <c:if test="${(goodsPriceRatio.minRatio == 0.51) && (goodsPriceRatio.maxRatio == 0.6)}">selected="selected"</c:if> value="0.51-0.6">51%-60%</option>
								<option <c:if test="${(goodsPriceRatio.minRatio == 0.61) && (goodsPriceRatio.maxRatio == 0.7)}">selected="selected"</c:if> value="0.61-0.7">61%-70%</option>
								<option <c:if test="${(goodsPriceRatio.minRatio == 0.71) && (goodsPriceRatio.maxRatio == 0.8)}">selected="selected"</c:if> value="0.71-0.8">71%-80%</option>
								<option <c:if test="${(goodsPriceRatio.minRatio == 0.81) && (goodsPriceRatio.maxRatio == 0.9)}">selected="selected"</c:if> value="0.81-0.9">81%-90%</option>
								<option <c:if test="${(goodsPriceRatio.minRatio == 0.91) && (goodsPriceRatio.maxRatio == 1)}">selected="selected"</c:if> value="0.91-1">91%-100%</option>
						</select>	
					</form>
                    <!-- 工具栏 -->
					<div class="row" style="padding-bottom: 5px;">
						<div class="col-sm-12">
	                        <div class="pull-left">
	                        	<shiro:hasPermission name="ec:goodsPriceRatio:add">
	                        		<table:addRow url="${ctx}/ec/goodsPriceRatio/form" title="城市异价" width="800px" height="650px"></table:addRow>
	                        	</shiro:hasPermission>
	                        </div>
	                        <div class="pull-right">
								<button class="btn btn-primary btn-rounded btn-outline btn-sm " onclick="search()">
									<i class="fa fa-search"></i> 查询
								</button>
								<button class="btn btn-primary btn-rounded btn-outline btn-sm " onclick="newReset()">
									<i class="fa fa-refresh"></i> 重置
								</button>
							</div>
						</div>
					</div>
                </div>
                <table id="treeTable" class="table table-bordered table-hover table-striped">
                	<thead> 
                		<tr>
                			<th style="text-align: center;">ID</th>
                			<th style="text-align: center;">异价比例</th>
                			<th style="text-align: center;">城市</th>
                			<th style="text-align: center;">商品</th>
							<th style="text-align: center;">操作人</th>
                			<th style="text-align: center;">操作时间</th>
                			<th style="text-align: center;">操作</th>
                		</tr>
                	</thead>
                    <tbody>
                    	<c:forEach items="${page.list}" var="goodsPriceRatio">
						<tr>
							<td style="text-align: center;">${goodsPriceRatio.goodsPriceRatioId}</td>
							<td style="text-align: center;">${goodsPriceRatio.ratio}</td>
							<td style="text-align: center;"><a href="#" onclick="openDialogView('查看城市', '${ctx}/ec/goodsPriceRatio/viewCity?goodsPriceRatioId=${goodsPriceRatio.goodsPriceRatioId}','700px', '350px')">查看</a></td>
							<td style="text-align: center;"><a href="#" onclick="openDialogView('查看商品', '${ctx}/ec/goodsPriceRatio/viewGoods?goodsPriceRatioId=${goodsPriceRatio.goodsPriceRatioId}','700px', '350px')">查看</a></td>
							<td style="text-align: center;">${goodsPriceRatio.createBy.name}</td>
							<td style="text-align: center;">
								<fmt:formatDate value="${goodsPriceRatio.createDate}"  pattern="yyyy-MM-dd HH:mm:ss" />
							</td>	
							<td style="text-align: center;">
								<shiro:hasPermission name="ec:goodsPriceRatio:view">
									<a href="#" onclick="openDialogView('查看', '${ctx}/ec/goodsPriceRatio/form?goodsPriceRatioId=${goodsPriceRatio.goodsPriceRatioId}','800px', '650px')" class="btn btn-info btn-xs"><i class="fa fa-search-plus"></i> 查看</a>
								</shiro:hasPermission>
								<shiro:hasPermission name="ec:goodsPriceRatio:edit">
									<a href="#" onclick="openDialog('修改', '${ctx}/ec/goodsPriceRatio/form?goodsPriceRatioId=${goodsPriceRatio.goodsPriceRatioId}','800px', '650px')" class="btn btn-success btn-xs"><i class="fa fa-edit"></i> 修改</a>
								</shiro:hasPermission>
								<shiro:hasPermission name="ec:goodsPriceRatio:del">
									<a href="${ctx}/ec/goodsPriceRatio/del?goodsPriceRatioId=${goodsPriceRatio.goodsPriceRatioId}" onclick="return confirmx('确认要删除吗？', this.href)" class="btn btn-danger btn-xs"><i class="fa fa-trash"></i> 删除</a>
								</shiro:hasPermission>
							</td>
						</tr>
						</c:forEach>
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