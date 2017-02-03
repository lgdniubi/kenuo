<%@ page import="com.training.modules.sys.utils.ParametersFactory"%>
<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/webpage/include/taglib.jsp"%>
<%@ include file="/webpage/include/icheck.jsp"%>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="decorator" content="default"/>
    <title>商品退货期列表</title>
</head>
<body>
    <div class="wrapper-content">
        <div class="ibox">
            <div class="ibox-title">
                <h5>商品退货期列表</h5>
            </div>
            <sys:message content="${message}"/>
            <div class="ibox-content">
                <div class="searcharea clearfix">
                    <form:form class="navbar-form navbar-left searcharea" id="searchForm" modelAttribute="mtmyRuleParam" action="${ctx}/ec/returnGoodsDate/list">
                    	<!-- 分页隐藏文本框 -->
	                    <input id="pageNo" name="pageNo" type="hidden" value="${page.pageNo}"/>
		 				<input id="pageSize" name="pageSize" type="hidden" value="${page.pageSize}"/>
                    </form:form>
	                <!-- 工具栏 -->
					<div class="row" style="padding-bottom: 5px;">
						<div class="col-sm-12">
	                        <div class="pull-left">
	                        	<shiro:hasPermission name="ec:returnGoodsDate:add">
	                        		<table:addRow url="${ctx}/ec/returnGoodsDate/form" title="添加" width="600px" height="350px"></table:addRow><!-- 增加按钮 -->
		                         </shiro:hasPermission>
		                         <shiro:hasPermission name="ec:returnGoodsDate:del">
		                         	<table:delRow url="${ctx}/ec/returnGoodsDate/deleteAll" id="treeTable"></table:delRow><!-- 批量删除按钮 -->
		                         </shiro:hasPermission>
	                        </div>
						</div>
					</div>
                </div>
                <table id="treeTable" class="table table-bordered table-hover table-striped">
                    <thead>
                        <tr>
                            <th width="120" style="text-align: center;"><label for="i-checks"><input type="checkbox" name="" id="i-checks" class="i-checks"></label></th>
                            <th width="120" style="text-align: center;">商品分类</th>
                            <th width="120" style="text-align: center;">退货期限</th>
                            <th width="200" style="text-align: center;">创建人</th>
                            <th width="200" style="text-align: center;">时间</th>
                            <th width="200" style="text-align: center;">备注</th>
                            <th width="200" style="text-align: center;">操作</th>
                        </tr>
                    </thead>
                    <tbody>
                    	<c:forEach items="${page.list}" var="returnGoodsDate">
	                        <tr style="text-align: center;">
	                            <td><input type="checkbox" id="${returnGoodsDate.id }" name="ids" class="i-checks" ></td>
	                            <td>${returnGoodsDate.goodsCategory.name }</td>
	                            <td>${returnGoodsDate.dateVal }</td>
	                            <td>${returnGoodsDate.createBy.name }</td>
	                            <td><fmt:formatDate value="${returnGoodsDate.createDate}" pattern="yyyy-MM-dd HH:mm:ss"/></td>
	                            <td>${returnGoodsDate.remarks }</td>
	                            <td>
	                            	<shiro:hasPermission name="ec:returnGoodsDate:view">
										<a href="#" onclick="openDialogView('查看系统参数', '${ctx}/ec/returnGoodsDate/form?id=${returnGoodsDate.id}','860px', '350px')" class="btn btn-info btn-xs" ><i class="fa fa-search-plus"></i> 查看</a>
									</shiro:hasPermission>
									<shiro:hasPermission name="ec:returnGoodsDate:edit">
			    						<a href="#" onclick="openDialog('修改系统参数', '${ctx}/ec/returnGoodsDate/form?id=${returnGoodsDate.id}','600px', '350px')" class="btn btn-success btn-xs" ><i class="fa fa-edit"></i> 修改</a>
				    				</shiro:hasPermission>
		                            <!--删除按钮  -->
		                            <shiro:hasPermission name="ec:returnGoodsDate:del">
		                            	<a href="${ctx}/ec/returnGoodsDate/del?id=${returnGoodsDate.id}" onclick="return confirmx('要删除该问题吗？', this.href)" class="btn btn-danger btn-xs"><i class="fa fa-trash"></i> 删除</a>
		                            </shiro:hasPermission>
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
    </div>
    <div class="loading"></div> 
</body>
</html>