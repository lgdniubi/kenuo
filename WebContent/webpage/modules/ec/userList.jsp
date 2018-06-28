<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/webpage/include/taglib.jsp"%>
<%@ include file="/webpage/include/icheck.jsp"%>
<html>
<head>
	<title>会员管理</title>
	<meta name="decorator" content="default"/>
    <meta name="viewport" content="width=device-width, initial-scale=1.0, minimum-scale=1.0, maximum-scale=1.0,user-scalable=no">
	<script type="text/javascript">
		function page(n,s){
			$("#pageNo").val(n);
			$("#pageSize").val(s);
			$("#searchForm").submit();
	    	return false;
	    }
	//  $(".loading").show();
		$.validator.setDefaults({
		    submitHandler: function() {
		    	mobileFrom();
		    }
		});
		
	    $(document).ready(function() {
	    	 $("#mobileFrom").validate({
	    		 rules: {
	    			 mobile: {
				            required : true,
				            minlength : 7,
				            remote: {
				            	url:"${ctx}/ec/mtmyuser/verifymobile",
			            		type: "get",
			                    data: {
			                        'oldMobile': function(){return $("input[name='oldMobile']").val();},
				            		'mobile': function(){return $("#newMobile").val();}
			                    }
				            }
				      }
				  },
				  messages: {
					  mobile: {
				            required : "请输入手机号",
				            minlength : "请输入正确手机号",
				            remote: "此手机号已存在"
				       }
				  }		 
	    	 });
	    	 $("#pwdForm").validate({
	    		 rules: {
				      password: {
				        required: true,
				        minlength: 6
				      }
				  },
				  messages: {
				      password: {
				        required: "请输入密码",
				        minlength: "密码长度不能小于 6 个字符"
				      }
				  }
	    	 });
		    var startRegtime = {
			    elem: '#startRegtime',
			    format: 'YYYY-MM-DD',
			    event: 'focus',
			    max: $("#endRegtime").val(),   //最大日期
			    istime: false,				//是否显示时间
			    isclear: false,				//是否显示清除
			    istoday: false,				//是否显示今天
			    issure: true,				//是否显示确定
			    festival: true,				//是否显示节日
			    choose: function(datas){
			    	endRegtime.min = datas; 		//开始日选好后，重置结束日的最小日期
			    	endRegtime.start = datas 		//将结束日的初始值设定为开始日
			    }
			};
			var endRegtime = {
			    elem: '#endRegtime',
			    format: 'YYYY-MM-DD',
			    event: 'focus',
			    min: $("#startRegtime").val(),
			    istime: false,
			    isclear: false,
			    istoday: false,
			    issure: true,
			    festival: true,
			    choose: function(datas){
			    	startRegtime.max = datas; //结束日选好后，重置开始日的最大日期
			    }
			};
			 var startLastlogin = {
					    elem: '#startLastlogin',
					    format: 'YYYY-MM-DD',
					    event: 'focus',
					    max: $("#endLastlogin").val(),   //最大日期
					    istime: false,				//是否显示时间
					    isclear: false,				//是否显示清除
					    istoday: false,				//是否显示今天
					    issure: true,				//是否显示确定
					    festival: true,				//是否显示节日
					    choose: function(datas){
					    	endLastlogin.min = datas; 		//开始日选好后，重置结束日的最小日期
					    	endLastlogin.start = datas 		//将结束日的初始值设定为开始日
					    }
					};
			var endLastlogin = {
			    elem: '#endLastlogin',
			    format: 'YYYY-MM-DD',
			    event: 'focus',
			    min: $("#startLastlogin").val(),
			    istime: false,
			    isclear: false,
			    istoday: false,
			    issure: true,
			    festival: true,
			    choose: function(datas){
			    	startLastlogin.max = datas; //结束日选好后，重置开始日的最大日期
			    }
			};
			laydate(startRegtime);
			laydate(endRegtime);
			laydate(startLastlogin);
			laydate(endLastlogin);
	    })
	    function lookmobile(num){
	    	loading("正在查询，请稍后 . . .");
	    	$("#userid").val(num);
	    	$("#oldMobile").val("");
	    	$.ajax({
	    		type : 'post',
	    		url : '${ctx}/ec/mtmyuser/lookmobile?userid='+num,
	    		dateType: 'text',
	    		success:function(date){
	    			$("#newPhone").val(date.phone);
	    			$("#newMobile").val(date.mobile);
	    			$("#oldMobile").val(date.mobile);
	    			$('#telphone').modal("show");
	    			closeTip();
	    		}
	    	})
	    }
	    function mobileFrom(){
	    	loading('正在提交，请稍等...');
			$.ajax({
				 type : 'POST',
				 url : '${ctx}/ec/mtmyuser/updateUser',
				 data:{'userid':$("#userid").val(),'phone':$("#newPhone").val(),'mobile':$('#newMobile').val(),'password':$('#password').val()},
				 success:function(index,data){
					closeTip();
					showTip('修改用户成功','success');
					//	页面刷新等待0.1秒 弹出成功框
					setTimeout(function(){
						sortOrRefresh()
						}, 100);
				 }
			 })
		}
	    function updatepwd(num){
	    	$("#password").val("");
	    	$("#userid").val(num);
	    	$('#resetpsd').modal('show');
	    }
	    
	</script>
	<style type="text/css">
		.modal-content{
			margin:0 auto;
			width: 400px;
		}
	</style>
</head>
<body class="gray-bg">
	<div class="wrapper wrapper-content">
		<div class="ibox">
			<div class="ibox-title">
				<h5>会员管理</h5>
			</div>
			<sys:message content="${message}"/>
			<!-- 查询条件 -->
	    	<div class="ibox-content">
				<div class="clearfix">
					<form id="searchForm" action="${ctx}/ec/mtmyuser/list" method="post" class="navbar-form navbar-left searcharea">
						<!-- 翻页隐藏文本框 -->
						<input id="pageNo" name="pageNo" type="hidden" value="${page.pageNo}"/>
						<input id="pageSize" name="pageSize" type="hidden" value="${page.pageSize}"/>
						<table:sortColumn id="orderBy" name="orderBy" value="${page.orderBy}" callback="sortOrRefresh();"/><!-- 支持排序 -->
						<div class="form-group">
							<input id="userid" name="userid" type="text" value="${users.userid }" class="form-control" placeholder="用户ID" onkeyup="this.value=this.value.replace(/\D/g,'')"> 
							<input id="name" name="nickname" type="text" value="${users.nickname }" class="form-control" placeholder="用户名"> 
							<input id="mobile" name="mobile" type="text" value="${users.mobile }" class="form-control" placeholder="手机号码" onkeyup="this.value=this.value.replace(/\D/g,'')"> 
							<input id="realName" name="name" type="text" value="${users.name }" class="form-control" placeholder="真实姓名"> 
							<span><label>选择区域:</label></span>
                            <sys:treeselect id="district" name="district" value="${users.district}" labelName="areaName" labelValue="${users.areaName}" 
							     title="地区" url="/sys/area/treeData" cssClass="area1 form-control required"  allowClear="true" notAllowSelectRoot="true"/>
						<p></p>
						<p>
							<label>注册时间：</label>
							<input id="startRegtime" name="startRegtime" type="text" maxlength="20" class="laydate-icon form-control layer-date input-sm" 
							value="<fmt:formatDate value="${users.startRegtime }" pattern="yyyy-MM-dd"/>" placeholder="开始时间" readonly="readonly"/>
							<label>--</label>
							<input id="endRegtime" name="endRegtime" type="text" maxlength="20" class=" laydate-icon form-control layer-date input-sm" value="<fmt:formatDate value="${users.endRegtime }" pattern="yyyy-MM-dd"/>" placeholder="结束时间" readonly="readonly"/>
							<label>登录时间：</label>
							<input id="startLastlogin" name="startLastlogin" type="text" maxlength="20" class="laydate-icon form-control layer-date input-sm" value="<fmt:formatDate value="${users.startLastlogin }" pattern="yyyy-MM-dd"/>" placeholder="开始时间" readonly="readonly"/>
							<label>--</label>
							<input id="endLastlogin" name="endLastlogin" type="text" maxlength="20" class=" laydate-icon form-control layer-date input-sm" value="<fmt:formatDate value="${users.endLastlogin }" pattern="yyyy-MM-dd"/>" placeholder="结束时间" readonly="readonly"/>
							&nbsp;&nbsp;<label><input type="checkbox" class="i-checks" id="noLogin" name="noLogin" value='1' ${(users.noLogin eq '1')?'checked="checked"':''} >只查询未登陆用户</label>
						</p>
						 </div>	
					</form>
					<!-- 工具栏 -->
					<div class="row" style="padding-bottom: 5px;">
						<div class="col-sm-12">
							<div class="pull-left">
								<!-- 添加会员 -->
								<shiro:hasPermission name="ec:mtmyuser:adduserindex">
									<button class="btn btn-white btn-sm" title="添加会员" onclick="openDialog('添加会员', '${ctx}/ec/mtmyuser/adduserindex','650px', '500px')" data-placement="left" data-toggle="tooltip">
										<i class="fa fa-plus"></i>添加会员
									</button>
								</shiro:hasPermission>
								
								<!-- 异常用户处理 -->
								<shiro:hasPermission name="ec:mtmyuser:userexceptionpage">
									<a href="#" onclick='top.openTab("${ctx}/ec/mtmyuser/userexceptionpage","异常用户处理", false)'><i class="fa fa-edit"></i>异常用户处理</a>
								</shiro:hasPermission>
							</div>
							<div class="pull-right">
								<button  class="btn btn-primary btn-rounded btn-outline btn-sm " onclick="search()" ><i class="fa fa-search"></i> 查询</button>
								<button  class="btn btn-primary btn-rounded btn-outline btn-sm " onclick="reset()" ><i class="fa fa-refresh"></i> 重置</button>
							 </div>
						</div>
					</div>
				</div>
				<table id="contentTable" class="table table-striped table-bordered  table-hover table-condensed  dataTables-example dataTable no-footer">
					<thead>
						<tr>
							 <th style="text-align: center;">用户名</th>
						     <th style="text-align: center;">真实姓名</th>
						     <th style="text-align: center;">账户信息</th>
						     <th style="text-align: center;">性别</th>
						     <th style="text-align: center;">电话</th>
						     <th style="text-align: center;">订单数</th>
						     <th style="text-align: center;">注册时间</th>
						     <th style="text-align: center;">最近登陆时间</th>
						     <th style="text-align: center;">地区</th>
						     <shiro:hasPermission name="ec:mtmyuser:isRealData">
						     	<th style="text-align: center;">是否正常数据</th>
						     </shiro:hasPermission>
						     <th style="text-align: center;">操作</th>
						</tr>
					</thead>
					<tbody style="text-align: center;">
						<c:forEach items="${page.list}" var="users">
							<tr>
								<td>${users.nickname }</td>
							  	<td>${users.name }</td>
							  	<td>
							  		<shiro:hasPermission name="ec:mtmyuser:finduseraccounts">
							  			<a href="#" onclick="openDialogView('用户账户详情', '${ctx}/ec/mtmyuser/finduseraccounts?userId=${users.userid }','650px', '320px')">查看</a>
							  		</shiro:hasPermission>
							  	</td>
							  	<c:if test="${users.sex==0}">
							  		<td>保密</td>
							  	</c:if>
							  	<c:if test="${users.sex==1}">
							  		<td>男</td>
							  	</c:if>
							  	<c:if test="${users.sex==2}">
							  		<td>女</td>
							  	</c:if>
							  	
							  	<td>
							  		<shiro:hasPermission name="ec:mtmyuser:lookmobile">
							  			<a href="#" class="infos" onclick="lookmobile(${users.userid })">查看</a>
							  		</shiro:hasPermission>
						  		</td>
							  	<td>
							  		<shiro:hasPermission name="ec:mtmyuser:lookOrderByUser">
							  			<a href="#" onclick='top.openTab("${ctx}/ec/mtmyuser/lookOrderByUser?userid=${users.userid }","个人订单", false)'>查看</a>
						  			</shiro:hasPermission>
					  			</td>
							  	<td><fmt:formatDate value="${users.regtime }" pattern="yyyy-MM-dd HH:mm:ss"/></td>
							  	<td><fmt:formatDate value="${users.lastlogin }" pattern="yyyy-MM-dd HH:mm:ss"/></td>
							  	<td>${users.province }${users.city }${users.district }</td>
							  	<shiro:hasPermission name="ec:mtmyuser:isRealData">
								  	<td>
									  	<c:if test="${users.isTest == 1}">
											<a href="${ctx}/ec/mtmyuser/updateIsRealData?ID=${users.userid}&ISYESNO=0" >
												<img width="20" height="20" src="${ctxStatic}/ec/images/cancel.png" >
											</a>
										</c:if>
										<c:if test="${users.isTest == 0}">
											<a href="${ctx}/ec/mtmyuser/updateIsRealData?ID=${users.userid}&ISYESNO=1" >
												<img width="20" height="20" src="${ctxStatic}/ec/images/open.png" >
											</a>
										</c:if>
								  	</td>
							  	</shiro:hasPermission>
							    <td>
							    	<!--  
							     	<a href="#" onclick='top.openTab("${ctx}/ec/mtmyuser/userarchives","健康档案", false)' class="btn btn-primary" >健康档案</a>
							     	-->
							     	<shiro:hasPermission name="ec:mtmyuser:lookmobile">
							     		<a class="btn btn-success btn-xs resetpsd"  href="#" onclick="updatepwd(${users.userid })"><i class="fa fa-edit"></i>重置密码</a>
							     	</shiro:hasPermission>
							     	<shiro:hasPermission name="ec:mtmyuser:lockUser">
								     	<c:if test="${users.islock == 0}">
								     		<a class="btn btn-info btn-xs btn-danger"  href="${ctx}/ec/mtmyuser/lockUser?userid=${users.userid }&&name=${users.name }&&islock=1" onclick="return confirmx('确定要冻结此用户吗？', this.href)"><i class="fa fa-trash"></i>冻结</a>
								     	</c:if>
								     	<c:if test="${users.islock != 0}">
								     		<a class="btn btn-info btn-xs" href="${ctx}/ec/mtmyuser/lockUser?userid=${users.userid }&&name=${users.name }&&islock=0" onclick="return confirmx('确定为此用户解冻吗？', this.href)"><i class="fa fa-edit"></i>解冻</a>
								     	</c:if>
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
	<!--查看电话号码-->
	<input type="hidden" value="" id="userid" name="userid">
	<input type="hidden"  id="oldMobile" name="oldMobile">
	<div class="modal fade bs-example-modal-lg in" id="telphone" tabindex="-1" role="dialog" aria-labelledby="myLargeModalLabel" style="display: none; padding-right: 17px;">
   		<form id="mobileFrom" class="modal-dialog modal-lg">
     		<div class="modal-content">
       			<div class="modal-header">
       				<span>查看电话</span>
         			<button type="button" class="close" data-dismiss="modal" aria-label="Close"><span aria-hidden="true">×</span></button>
       			</div>
       			<div class="modal-body">
       				<table>
					    <tr>
					    	<td>座机号码：<td>
					    	<td><label><input class="form-control" type="text" value="" id="newPhone" name="phone" onkeyup="this.value=this.value.replace(/\D/g,'')"/></label><td>
					    </tr>
					    <tr>
					        <td>手机号码：<td>
					        <td><label><input class="form-control" type="text" value="" id="newMobile" name="mobile" maxlength="11" onkeyup="this.value=this.value.replace(/\D/g,'')"/></label><td>
					    </tr>
					</table>
       			</div>
       			<div class="modal-footer">
       				<!-- <a href="#" class="btn btn-success">保  存</a> -->
       				<input type="submit" class="btn btn-success" value="保 存">
					<a href="#" class="btn btn-primary" data-dismiss="modal">关   闭</a>
       			</div>
     		</div>
   		</form>
 	</div>
 	<!--重置密码-->
 	<div class="modal fade bs-example-modal-lg in" id="resetpsd" tabindex="-1" role="dialog" aria-labelledby="myLargeModalLabel" style="display: none; padding-right: 17px;">
   		<form id="pwdForm" class="modal-dialog modal-lg">
     		<div class="modal-content">
       			<div class="modal-header">
       				<span>重置密码</span>
         			<button type="button" class="close" data-dismiss="modal" aria-label="Close"><span aria-hidden="true">×</span></button>
       			</div>
       			<div class="modal-body">
       				<table>
					    <tr>
					    	<td>输入新密码：<td>
					    	<td><label><input class="form-control" type="password" value="" id="password" name="password" maxlength="20"/></label><td>
					    </tr>
					</table>
         				<!-- <input type="hidden" value="" id="pwduserid" name="userid"> -->
       			</div>
       			<div class="modal-footer">
       				<input type="submit" class="btn btn-success" value="保 存">
					<a href="#" class="btn btn-primary" data-dismiss="modal">关   闭</a>
       			</div>
     		</div>
   		</form>
 	</div>
</body>
</html>