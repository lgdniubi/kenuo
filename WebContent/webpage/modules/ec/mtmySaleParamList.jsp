<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/webpage/include/taglib.jsp"%>
<%@ include file="/webpage/include/icheck.jsp"%>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="decorator" content="default"/>
    <meta name="viewport" content="width=device-width, initial-scale=1.0, minimum-scale=1.0, maximum-scale=1.0,user-scalable=no">
    <script type="text/javascript">
		function page(n,s){
			$("#pageNo").val(n);
			$("#pageSize").val(s);
			$("#searchForm").submit();
	    	return false;
	    }
	    function nowReset(){
			 $("#paramKey").val(""); //清空
			 $("#searchForm").submit();
		}
	</script>
    <title>分销参数列表</title>
</head>
<body>
    <div class="wrapper-content">
        <div class="ibox">
            <div class="ibox-title">
                <h5>分销参数列表</h5>
            </div>
            <sys:message content="${message}"/>
            <div class="ibox-content">
                <div class="searcharea clearfix">
                    <form:form class="navbar-form navbar-left searcharea" id="searchForm" modelAttribute="mtmyRuleParam" action="${ctx}/ec/saleParam/list">
                    	<!-- 分页隐藏文本框 -->
	                    <input id="pageNo" name="pageNo" type="hidden" value="${page.pageNo}"/>
		 				<input id="pageSize" name="pageSize" type="hidden" value="${page.pageSize}"/>
                        <div class="form-group">
                            <label>关键字：<input type="text" id="paramKey" name="paramKey" class="form-control" placeholder="搜索参数Key" value="${mtmyRuleParam.paramKey }" maxlength="50"></label>
                        </div>
                        <div class="pull-right" style="margin-left: 50px;">
							<button  class="btn btn-primary btn-rounded btn-outline btn-sm " onclick="search()" ><i class="fa fa-search"></i> 查询</button>
							<button  class="btn btn-primary btn-rounded btn-outline btn-sm " onclick="nowReset()" ><i class="fa fa-refresh"></i> 重置</button>
						</div>
                    </form:form>
	                <!-- 工具栏 -->
					<div class="row" style="padding-bottom: 5px;">
						<div class="col-sm-12">
	                        <div class="pull-left">
	                        	<shiro:hasPermission name="ec:saleParam:add">
	                        		<table:addRow url="${ctx}/ec/saleParam/form" title="添加" width="800px" height="550px"></table:addRow><!-- 增加按钮 -->
		                         </shiro:hasPermission>
		                         <shiro:hasPermission name="ec:saleParam:del">
		                         	<table:delRow url="${ctx}/ec/saleParam/deleteAll" id="treeTable"></table:delRow><!-- 批量删除按钮 -->
		                         </shiro:hasPermission>
	                        </div>
						</div>
					</div>
                </div>
                <table id="treeTable" class="table table-bordered table-hover table-striped">
                    <thead>
                        <tr>
                            <th width="120" style="text-align: center;"><label for="i-checks"><input type="checkbox" name="" id="i-checks" class="i-checks"></label></th>
                            <th width="120" style="text-align: center;">参数Key</th>
                            <th width="120" style="text-align: center;">参数值</th>
                            <th width="200" style="text-align: center;">参数说明</th>
                            <th width="200" style="text-align: center;">创建人</th>
                            <th width="200" style="text-align: center;">时间</th>
                            <th width="200" style="text-align: center;">排序</th>
                            <th width="200" style="text-align: center;">备注</th>
                            <th width="200" style="text-align: center;">操作</th>
                        </tr>
                    </thead>
                    <tbody>
                    	<c:forEach items="${page.list}" var="mtmyRuleParam">
	                        <tr style="text-align: center;">
	                            <td><input type="checkbox" id="${mtmyRuleParam.id }" name="ids" class="i-checks" ></td>
	                            <td>${mtmyRuleParam.paramKey }</td>
	                            <td>${mtmyRuleParam.paramValue }</td>
	                            <td>${mtmyRuleParam.paramExplain }</td>
	                            <td>${mtmyRuleParam.createBy.name }</td>
	                            <td><fmt:formatDate value="${mtmyRuleParam.createDate}" pattern="yyyy-MM-dd HH:mm:ss"/></td>
	                            <td>${mtmyRuleParam.sort }</td>
	                            <td>${mtmyRuleParam.remarks }</td>
	                            <td>
	                            	<shiro:hasPermission name="ec:saleParam:view">
										<a href="#" onclick="openDialogView('查看系统参数', '${ctx}/ec/saleParam/form?id=${mtmyRuleParam.id}&opflag=VIEW','800px', '550px')" class="btn btn-info btn-xs" ><i class="fa fa-search-plus"></i> 查看</a>
									</shiro:hasPermission>
									<shiro:hasPermission name="ec:saleParam:edit">
			    						<a href="#" onclick="openDialog('修改系统参数', '${ctx}/ec/saleParam/form?id=${mtmyRuleParam.id}&opflag=UPDATE','800px', '550px')" class="btn btn-success btn-xs" ><i class="fa fa-edit"></i> 修改</a>
				    				</shiro:hasPermission>
		                            <!--删除按钮  -->
		                            <shiro:hasPermission name="ec:saleParam:del">
		                            	<a href="${ctx}/ec/saleParam/del?id=${mtmyRuleParam.id}" onclick="return confirmx('要删除该问题吗？', this.href)" class="btn btn-danger btn-xs"><i class="fa fa-trash"></i> 删除</a>
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