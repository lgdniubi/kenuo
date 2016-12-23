<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/webpage/include/taglib.jsp"%>
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
	    function nowreset(){
			 $("#typeName").val(""); //清空
		}
	</script>
    <title>培训规则类型列表</title>
</head>
<body>
    <div class="wrapper-content">
        <div class="ibox">
            <div class="ibox-title">
                <h5>培训规则类型列表</h5>
            </div>
            <sys:message content="${message}"/>
            <div class="ibox-content">
                <div class="searcharea clearfix">
                    <form:form class="navbar-form navbar-left searcharea" id="searchForm" modelAttribute="trainRuleParamType" action="${ctx}/train/ruleparam/typelist">
                    	<!-- 分页隐藏文本框 -->
	                    <input id="pageNo" name="pageNo" type="hidden" value="${page.pageNo}"/>
		 				<input id="pageSize" name="pageSize" type="hidden" value="${page.pageSize}"/>
                        <div class="form-group">
                            <label>关键字：<input type="text" id="typeName" name="typeName" class="form-control" placeholder="搜索" value="${trainRuleParamType.typeName }" maxlength="10"></label>
                        </div>
                        <div class="pull-right" style="margin-left: 50px;">
							<button  class="btn btn-primary btn-rounded btn-outline btn-sm " onclick="search()" ><i class="fa fa-search"></i> 查询</button>
							<button  class="btn btn-primary btn-rounded btn-outline btn-sm " onclick="nowreset()" ><i class="fa fa-refresh"></i> 重置</button>
						</div>
                    </form:form>
	                <!-- 工具栏 -->
					<div class="row" style="padding-bottom: 5px;">
						<div class="col-sm-12">
	                        <div class="pull-left">
	                        	<shiro:hasPermission name="train:ruletype:typeadd">
	                        		<table:addRow url="${ctx}/train/ruleparam/typeform" title="添加" width="800px" height="350px"></table:addRow><!-- 增加按钮 -->
		                         </shiro:hasPermission>
		                         <shiro:hasPermission name="train:ruletype:typedel">
		                         	<table:delRow url="${ctx}/train/ruleparam/typedeleteAll" id="treeTable"></table:delRow><!-- 批量删除按钮 -->
		                         </shiro:hasPermission>
	                        </div>
						</div>
					</div>
                </div>
                <table id="treeTable" class="table table-bordered table-hover table-striped">
                    <thead>
                        <tr>
                            <th width="120" style="text-align: center;"><label for="i-checks"><input type="checkbox" name="" id="i-checks"></label></th>
                            <th width="120" style="text-align: center;">类型名称</th>
                            <th width="120" style="text-align: center;">别名</th>
                            <th width="200" style="text-align: center;">备注</th>
                            <th width="200" style="text-align: center;">操作</th>
                        </tr>
                    </thead>
                    <tbody>
                    	<c:forEach items="${page.list}" var="trainRuleParamType">
	                        <tr style="text-align: center;">
	                            <td><input type="checkbox" id="${trainRuleParamType.id }" name="ids" class="i-checks" ></td>
	                            <td>${trainRuleParamType.typeName }</td>
	                            <td>${trainRuleParamType.aliaName }</td>
	                            <td>${trainRuleParamType.remarks }</td>
	                            <td>
	                            	<shiro:hasPermission name="train:ruletype:typeview">
										<a href="#" onclick="openDialogView('查看系统参数', '${ctx}/train/ruleparam/typeform?id=${trainRuleParamType.id}&opflag=VIEW','800px', '350px')" class="btn btn-info btn-xs" ><i class="fa fa-search-plus"></i> 查看</a>
									</shiro:hasPermission>
									<shiro:hasPermission name="train:ruletype:typeedit">
			    						<a href="#" onclick="openDialog('修改系统参数', '${ctx}/train/ruleparam/typeform?id=${trainRuleParamType.id}&opflag=UPDATE','800px', '350px')" class="btn btn-success btn-xs" ><i class="fa fa-edit"></i> 修改</a>
				    				</shiro:hasPermission>
		                            <!--删除按钮  -->
		                            <shiro:hasPermission name="train:ruletype:typedel">
		                            	<a href="${ctx}/train/ruleparam/typedelete?id=${trainRuleParamType.id}" onclick="return confirmx('要删除该问题吗？', this.href)" class="btn btn-danger btn-xs"><i class="fa fa-trash"></i> 删除</a>
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
    <script type="text/javascript">
	        $('#i-checks').click(function(){
	        	 if(this.checked){
	 	            $("#treeTable :checkbox").prop("checked", true);
	 	        }else{
	 	            $("#treeTable :checkbox").prop("checked", false);
	 	        }
	        });
	</script>
</body>
</html>