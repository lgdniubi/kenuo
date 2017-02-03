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
			$("#tag").val("");
			$("#startTime").val("");
			$("#endTime").val("");
			$("#searchForm").submit();
		}
		function promptx(title, lable, href, closed) {
			top.layer.prompt({
				title : title,
				maxlength : 100,
				formType : 2
			//prompt风格，支持0-2  0 文本框  1 密码框 2 多行文本
			}, function(pass) {
				var nowhref = href + '&remark=' + pass;
				confirmx(lable, nowhref, closed);
			});
			return false;
		}
		$(document).ready(function() {
		    var startTime = {
			    elem: '#startTime',
			    format: 'YYYY-MM-DD',
			    event: 'focus',
			    max: $("#endTime").val(),   //最大日期
			    istime: false,				//是否显示时间
			    isclear: false,				//是否显示清除
			    istoday: false,				//是否显示今天
			    issure: true,				//是否显示确定
			    festival: true,				//是否显示节日
			    choose: function(datas){
			    	endTime.min = datas; 		//开始日选好后，重置结束日的最小日期
			    	endTime.start = datas 		//将结束日的初始值设定为开始日
			    }
			};
			var endTime = {
			    elem: '#endTime',
			    format: 'YYYY-MM-DD',
			    event: 'focus',
			    min: $("#startTime").val(),
			    istime: false,
			    isclear: false,
			    istoday: false,
			    issure: true,
			    festival: true,
			    choose: function(datas){
			    	startTime.max = datas; //结束日选好后，重置开始日的最大日期
			    }
			};
			laydate(startTime);
			laydate(endTime);
			
		    $('#treeTable thead tr th input.i-checks').on('ifChecked', function(event){ //ifCreated 事件应该在插件初始化之前绑定 
		    	 $('#treeTable tbody tr td input.i-checks').iCheck('check');
		    });
		    $('#treeTable thead tr th input.i-checks').on('ifUnchecked', function(event){ //ifCreated 事件应该在插件初始化之前绑定 
		    	 $('#treeTable tbody tr td input.i-checks').iCheck('uncheck');
		    });
	    })
	    function agreeAll(){
			var str="";
			var ids="";
			$("input[name='ids']:checked").each(function(){
			    if(true == $(this).is(':checked')){
			      str+=$(this).attr("id")+",";
			    }
			});
			if(str.substr(str.length-1)== ','){
			   ids = str.substr(0,str.length-1);
			}
			if(ids == ""){
				top.layer.alert('请至少选择一条数据！', {icon: 0, title:'提醒'});
			}else{
				$("#parentIds").val(ids);
				$('#modal').modal("show");
			}
		}
		function agree(num){
			$("#parentIds").val(num);
			$('#modal').modal("show");
		}
		function update(newUrl){
			var str="";
			var ids="";
			$("input[name='ids']:checked").each(function(){
			    if(true == $(this).is(':checked')){
			      str+=$(this).attr("id")+",";
			    }
			});
			if(str.substr(str.length-1)== ','){
			   ids = str.substr(0,str.length-1);
			}
			if(ids == ""){
				top.layer.alert('请至少选择一条数据!', {icon: 0, title:'警告'});
			}else{
				top.layer.confirm('确认要批量拒绝吗?', {icon: 3, title:'系统提示'}, function(index){
					var url =newUrl+"?ids="+ids;
					window.location = url;
			    	top.layer.close(index);
				});
			}
		}
	</script>
	<style type="text/css">
		.modal-content{
			margin:0 auto;
			width: 350px;
		}
	</style>
    <title>分销用户关系管理</title>
</head>
<body>
    <div class="wrapper-content">
        <div class="ibox">
            <div class="ibox-title">
                <h5>分销用户关系管理</h5>
            </div>
            <sys:message content="${message}"/>
            <div class="ibox-content">
                <div class="searcharea clearfix">
                    <form:form class="navbar-form navbar-left searcharea" id="searchForm" modelAttribute="mtmySaleRelieve" action="${ctx}/ec/mtmySale/list">
                    	<!-- 分页隐藏文本框 -->
	                    <input id="pageNo" name="pageNo" type="hidden" value="${page.pageNo}"/>
		 				<input id="pageSize" name="pageSize" type="hidden" value="${page.pageSize}"/>
                        <div class="form-group">
                            <input type="text" id="tag" name="tag" class="form-control" placeholder="搜索注册用户姓名/手机号" value="${mtmySaleRelieve.tag }">
                            <label>申请时间:</label>
							<input id="startTime" name="startTime" type="text" maxlength="20" class="laydate-icon form-control layer-date input-sm" value="<fmt:formatDate value="${mtmySaleRelieve.startTime }" pattern="yyyy-MM-dd"/>" placeholder="开始时间" readonly="readonly"/>
							<label>--</label>
							<input id="endTime" name="endTime" type="text" maxlength="20" class=" laydate-icon form-control layer-date input-sm" value="<fmt:formatDate value="${mtmySaleRelieve.endTime }" pattern="yyyy-MM-dd"/>" placeholder="结束时间" readonly="readonly"/>
							<form:select path="status" class="form-control" id="status">
								<form:option value="0">申请中</form:option>
								<form:option value="1">已同意</form:option>
							</form:select>
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
		                         <button onclick="update('${ctx}/ec/mtmySale/refuseAll')" class="btn btn-white btn-sm"><i class="fa fa-close"></i>批量拒绝</button>
		                         <button onclick="agreeAll()" class="btn btn-white btn-sm"><i class="fa fa-check"></i>批量同意</button>
	                        </div>
						</div>
					</div>
                </div>
                <table id="treeTable" class="table table-bordered table-hover table-striped">
                    <thead>
                        <tr>
                        	<th style="text-align: center;"><label for="i-checks"><input type="checkbox" name="" id="i-checks" class="i-checks"></label></th>
                        	<th style="text-align: center;">ID</th>
                            <th style="text-align: center;">申请时间</th>
                            <th style="text-align: center;">级别</th>
                            <th style="text-align: center;">姓名</th>
                            <th style="text-align: center;">注册手机号</th>
                            <th style="text-align: center;">邀请人</th>
                            <th style="text-align: center;">邀请人级别</th>
                            <th style="text-align: center;">邀请成功时间</th>
                            <th style="text-align: center;">申请状态</th>
                            <th style="text-align: center;">贡献余额</th>
                            <th style="text-align: center;">贡献云币</th>
                            <th style="text-align: center;">操作</th>
                        </tr>
                    </thead>
                    <tbody>
                    	<c:forEach items="${page.list}" var="mtmySaleRelieve">
	                        <tr style="text-align: center;">
	                        	<td>
	                        		<c:if test="${mtmySaleRelieve.status == 0}">
	                        			<input type="checkbox" id="${mtmySaleRelieve.id }" name="ids" class="i-checks" >
	                        		</c:if>
	                        	</td>
	                        	<td>${mtmySaleRelieve.id }</td>
	                            <td><fmt:formatDate value="${mtmySaleRelieve.createDate}" pattern="yyyy-MM-dd HH:mm:ss"/></td>
	                            <td>${mtmySaleRelieve.newusers.layer}</td>
	                            <td>${mtmySaleRelieve.name }</td>
	                            <td>${mtmySaleRelieve.mobile }</td>
	                            <td>
	                            	${mtmySaleRelieve.users.nickname }
									<%-- <c:if test="${mtmySaleRelieve.user.name != '' && mtmySaleRelieve.user.name != null}">
										<br>妃子校用户:${mtmySaleRelieve.user.name }
									</c:if>  妃子校用户  关联查询很慢 --%>
	                            </td>
	                            <td>${mtmySaleRelieve.users.layer}</td>
	                            <td><fmt:formatDate value="${mtmySaleRelieve.date}" pattern="yyyy-MM-dd HH:mm:ss"/></td>
	                             <td>
	                             	<c:if test="${mtmySaleRelieve.status == 0}">申请中</c:if>
	                             	<c:if test="${mtmySaleRelieve.status == 1}">已同意</c:if>
	                             </td>
	                            <td><fmt:formatNumber type="number" value="${mtmySaleRelieve.mtmySaleRelieveLog.balanceAmount}" maxFractionDigits="2"/></td>
	                            <td><fmt:formatNumber type="number" value="${mtmySaleRelieve.mtmySaleRelieveLog.integralAmount}" maxFractionDigits="2"/></td>
	                            <td>
	                            	<c:if test="${mtmySaleRelieve.status == 0}">
										<a href="#" onclick="agree(${mtmySaleRelieve.id})" class="btn btn-info btn-xs" ><i class="fa fa-check"></i>同意</a>
			    						<a href="${ctx}/ec/mtmySale/refuse?id=${mtmySaleRelieve.id}&userId=${mtmySaleRelieve.userId}&parentId=${mtmySaleRelieve.parentId}" onclick="return promptx('请填写拒绝原因！不可为空！','确定要拒绝吗？',this.href)" class="btn btn-danger btn-xs" ><i class="fa fa-close"></i>拒绝</a>
		    						</c:if>
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
    <div class="modal fade bs-example-modal-lg in" id="modal" tabindex="-1" role="dialog" aria-labelledby="myLargeModalLabel" style="display: none; padding-right: 17px;">
   		<form id="" class="modal-dialog modal-lg" action="${ctx}/ec/mtmySale/agreeAll">
   			<input name="parentIds" id="parentIds" type="hidden">
     		<div class="modal-content">
       			<div class="modal-header">
       				<span>选择解除后用户上级</span>
         			<button type="button" class="close" data-dismiss="modal" aria-label="Close"><span aria-hidden="true">×</span></button>
       			</div>
       			<div class="modal-body">
       				<table>
       					<c:forEach items="${SNmtmyRuleParam }" var="SNmtmyRuleParam" varStatus="status">
							<tr>
						    	<td><input type="radio" id="newParentId" name="newParentId"  ${(status.index == '0')?'checked="checked"':''} value="${SNmtmyRuleParam.paramValue }">&nbsp;&nbsp;${SNmtmyRuleParam.paramExplain }<td>
						    </tr>
       					</c:forEach>
					</table>
       			</div>
       			<div class="modal-footer">
       				<input type="submit" class="btn btn-success" value="保 存">
					<a href="#" class="btn btn-primary" data-dismiss="modal">关   闭</a>
       			</div>
     		</div>
   		</form>
 	</div>
    <div class="loading"></div> 
</body>
</html>