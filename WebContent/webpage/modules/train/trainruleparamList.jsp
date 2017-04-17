<%@ page import="com.training.modules.sys.utils.ParametersFactory"%>
<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/webpage/include/taglib.jsp"%>
<%@ include file="/webpage/include/icheck.jsp"%>
<%
	String refreshparam_url = ParametersFactory.getMtmyParamValues("refreshparam_url");
%>
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
	    function nowreset(){
			 $("#paramKey").val(""); //清空
			 $("#beginDate").val(""); //清空
			 $("#endDate").val(""); //清空
		}
	    
	  //刷新后台系统缓存
		function refreshparam(){
			$(".loading").show();//打开展示层
			$.ajax({
				type : "POST",   
				url : "${ctx}/ec/ruleparam/refreshparam",
				dataType: 'json',
				success: function(data) {
					$(".loading").hide(); //关闭加载层
					if(data.STATUS == 'OK'){
						alert(data.MESSAGE);
					}else{
						alert("刷新出现异常，请与管理员联系");
					}
				}
			});  
		}
	    
	</script>
    <title>培训系统参数列表</title>
</head>
<body>
    <div class="wrapper-content">
        <div class="ibox">
            <div class="ibox-title">
                <h5>培训系统参数列表</h5>
            </div>
            <sys:message content="${message}"/>
            <div class="ibox-content">
                <div class="searcharea clearfix">
                    <form:form class="navbar-form navbar-left searcharea" id="searchForm" modelAttribute="trainRuleParam" action="${ctx}/train/ruleparam/list">
                    	<!-- 分页隐藏文本框 -->
	                    <input id="pageNo" name="pageNo" type="hidden" value="${page.pageNo}"/>
		 				<input id="pageSize" name="pageSize" type="hidden" value="${page.pageSize}"/>
                        <div class="form-group">
                            <label>关键字：<input type="text" id="paramKey" name="paramKey" class="form-control" placeholder="搜索参数Key" value="${trainRuleParam.paramKey }" maxlength="50"></label>
                            <label>类型：
	                            <select id="paramType" name="paramType" class="form-control required">
									<option value="-2">全部</option>
									<option ${trainRuleParam.paramType==-1?'selected="selected"':''} value="-1"></option>
									<c:forEach items="${trainRuleParam.ruleparamtypelist}" var="tspType">
										<option ${trainRuleParam.paramType==tspType.id?'selected="selected"':''} value="${tspType.id }">${tspType.typeName }</option>
									</c:forEach>
								</select>
                            </label>
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
	                        	<shiro:hasPermission name="train:ruleparam:add">
	                        		<table:addRow url="${ctx}/train/ruleparam/form" title="添加" width="800px" height="550px"></table:addRow><!-- 增加按钮 -->
		                         </shiro:hasPermission>
		                         <shiro:hasPermission name="train:ruleparam:del">
		                         	<table:delRow url="${ctx}/train/ruleparam/deleteAll" id="treeTable"></table:delRow><!-- 批量删除按钮 -->
		                         </shiro:hasPermission>
		                         <shiro:hasPermission name="train:ruletype:typelist">
	                        		<button class="btn btn-white btn-sm " data-toggle="tooltip" data-placement="left" onclick='top.openTab("${ctx}/train/ruleparam/typelist","添加规则分类", false)'><i class="fa fa-plus"></i> 添加规则分类</button>
		                         </shiro:hasPermission>
		                         <shiro:hasPermission name="ec:ruletype:refreshparam">
	                        		<button class="btn btn-white btn-sm " data-toggle="tooltip" data-placement="left" onclick="refreshparam()" title="刷新后台系统参数缓存"><i class="glyphicon glyphicon-repeat"></i> 刷新后台系统参数缓存</button>
		                         </shiro:hasPermission>
	                        </div>
						</div>
					</div>
                </div>
                <table id="treeTable" class="table table-bordered table-hover table-striped">
                    <thead>
                        <tr>
                            <th width="120" style="text-align: center;">
                            	<label for="i-checks">
                            		<input type="checkbox" name="" id="i-checks" class="i-checks">
                            	</label>
                            </th>
                            <th width="120" style="text-align: center;">参数Key</th>
                            <th width="120" style="text-align: center;">参数值</th>
                            <th width="120" style="text-align: center;">参数类型</th>
                            <th width="120" style="text-align: center;">上限</th>
                            <th width="200" style="text-align: center;">参数说明</th>
                            <th width="200" style="text-align: center;">创建人</th>
                            <th width="200" style="text-align: center;">时间</th>
                            <th width="200" style="text-align: center;">排序</th>
                            <th width="200" style="text-align: center;">备注</th>
                            <th width="200" style="text-align: center;">操作</th>
                        </tr>
                    </thead>
                    <tbody>
                    	<c:forEach items="${page.list}" var="trainRuleParam">
	                        <tr style="text-align: center;">
	                            <td><input type="checkbox" id="${trainRuleParam.id }" name="ids" class="i-checks" ></td>
	                            <td>${trainRuleParam.paramKey }</td>
	                            <td>${trainRuleParam.paramValue }</td>
	                            <td>${trainRuleParam.trainRuleParamType.typeName }</td>
	                            <td>${trainRuleParam.paramUplimit }</td>
	                            <td>${trainRuleParam.paramExplain }</td>
	                            <td>${trainRuleParam.createBy.name }</td>
	                            <td><fmt:formatDate value="${trainRuleParam.createDate}" pattern="yyyy-MM-dd HH:mm:ss"/></td>
	                            <td>${trainRuleParam.sort }</td>
	                            <td>${trainRuleParam.remarks }</td>
	                            <td>
	                            	<shiro:hasPermission name="train:ruleparam:view">
										<a href="#" onclick="openDialogView('查看系统参数', '${ctx}/train/ruleparam/form?id=${trainRuleParam.id}&opflag=VIEW','800px', '550px')" class="btn btn-info btn-xs" ><i class="fa fa-search-plus"></i> 查看</a>
									</shiro:hasPermission>
									<shiro:hasPermission name="train:ruleparam:edit">
			    						<a href="#" onclick="openDialog('修改系统参数', '${ctx}/train/ruleparam/form?id=${trainRuleParam.id}&opflag=UPDATE','800px', '550px')" class="btn btn-success btn-xs" ><i class="fa fa-edit"></i> 修改</a>
				    				</shiro:hasPermission>
		                            <!--删除按钮  -->
		                            <shiro:hasPermission name="train:ruleparam:del">
		                            	<a href="${ctx}/train/ruleparam/del?id=${trainRuleParam.id}" onclick="return confirmx('要删除该问题吗？', this.href)" class="btn btn-danger btn-xs"><i class="fa fa-trash"></i> 删除</a>
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
<script type="text/javascript" src="${ctxStatic}/js/bootstrap.js"></script>
<script type="text/javascript" src="${ctxStatic}/js/jquery.datetimepicker.js"></script>
</html>