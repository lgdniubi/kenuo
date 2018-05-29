<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/webpage/include/taglib.jsp"%>
<html>
<head>
	<title>用户权限管理</title>
	<meta name="decorator" content="default"/>
	<%@include file="/webpage/include/treeview.jsp" %>
	<script type="text/javascript">
		var validateForm;
		var tree2;
		var oldRoleNames;
		var a = true;
		function doSubmit(){//回调函数，在编辑和保存动作时，供openDialog调用提交表单。
			$("#isUpdateRole").val(0);
			$("#isPB").val(0);
			if(validateForm.form()){
				var roles = $("#roleName").val();
				if((oldRoleNames.indexOf('排班') != -1) && (roles.indexOf('排班') == -1) && ($("#isSpecBeautician").val() != '0')){
					top.layer.alert('该用户为特殊美容师，无法将其排班角色删除', {icon: 0, title:'提醒'});
					return false;						
				}
				if((oldRoleNames.indexOf('排班') == -1) && (roles.indexOf('排班') != -1)){
					$("#isPB").val(1);						
				}
				var ids2 = [], nodes2 = tree2.getCheckedNodes(true);
				for (var i=0; i<nodes2.length; i++) {
					var halfCheck = nodes2[i].getCheckStatus();
					if (!halfCheck.half){//不为半选
						ids2.push(nodes2[i].id);
					}
				}
				if(oldRoleNames == $("#roleName").val()){	// 用户校验用户角色是否修改过 修改过则清除用户缓存 用户TOKEN失效
					$("#isUpdateRole").val(0);	
				}else{
					$("#isUpdateRole").val(1);	
				}
				
				//当自媒体权限  选择 是,下面的字段都是必填;选择 否,下面的字段全部为空
				var isLogin = $("[name='mediaLoginAuth.isLogin']:checked").val();
				var userType = $("[name='mediaLoginAuth.userType']").val();
				var userTag = $("[name='mediaLoginAuth.userTag']").val();
				if(isLogin == 1){
					if(userType == 1){//用户类型:写手,平台必选
						var platform = $("[name='mediaLoginAuth.platform']:checked").val();
						if(platform == undefined){
							top.layer.alert('平台为必选', {icon: 0, title:'提醒'});
							return;
						}
					}else if(userType == 2){//用户类型:管理员,默认平台不展示
						$("input[name='mediaLoginAuth.platform']").each(function(){ 
							$(this).attr("checked",false); 
						});
					}
					if($("[name='mediaLoginAuth.userTag']").val() == null || $("[name='mediaLoginAuth.userTag']").val() == ""){
						top.layer.alert('标签为必填', {icon: 0, title:'提醒'});
						return;
					}
				}
				
				if($("#dataScope").val()==2){
					  if(ids2.length > 0){
						  $("#officeIds").val(ids2);
						  loading('正在提交，请稍等...');
						  $("#inputForm").submit();
						  return true;
					  }else{
						  top.layer.alert('按明细设置权限时,明细不可为空', {icon: 0, title:'提醒'});
						  return false;
					  }
				 }else{
				    loading('正在提交，请稍等...');
				    $("#inputForm").submit();
				    return true;
				 }
			} 
		  return false;
		}
		$(document).ready(function(){
			oldRoleNames = $("#roleName").val();

			validateForm= $("#inputForm").validate({
				submitHandler: function(form){
					 loading('正在提交，请稍等...');
					 form.submit();
				}
			});
			
			var setting = {check:{enable:true,autoCheckTrigger: true},view:{selectedMulti:false},
					data:{simpleData:{enable:true}},callback:{beforeClick:function(id, node){
						tree.checkNode(node, !node.checked, true, true);
						return false;
					}}};
					
			// 用户-机构
			var zNodes2=[
					<c:forEach items="${officeList}" var="office">{id:"${office.id}", pId:"${not empty office.parent?office.parent.id:0}", name:"${office.name}"},
		            </c:forEach>];
			// 初始化树结构
			tree2 = $.fn.zTree.init($("#officeTree"), setting, zNodes2);
			// 不选择父节点
			tree2.setting.check.chkboxType = { "Y" : "s", "N" : "s" };
			// 默认选择节点
			var ids2 = "${user.officeIds}".split(",");
			for(var i=0; i<ids2.length; i++) {
				var node = tree2.getNodeByParam("id", ids2[i]);
				try{tree2.checkNode(node, true, false);}catch(e){}
			}
			// 默认展开全部节点
			//tree2.expandAll(true);
			tree2.selectNode(node,true,false);//  展开选中的节点
			
			// 刷新（显示/隐藏）机构
			refreshOfficeTree();
			$("#dataScope").change(function(){
				refreshOfficeTree();
			});
			
			//自媒体权限下面的数据暂时不显示
			var login = $("[name='mediaLoginAuth.isLogin']:checked").val();
			if(login == 0){
				$("#user_type").hide();
				$("#platform").hide();
				$("#userTag").hide();
			}
			//自媒体权限 用户类型是:管理员  平台不显
			var _userType = $("#_userType").val();
			if(_userType == 2){
				$("#platform").hide();
			}
			
			/* var checkeds = $("#_platform").val();
			//自媒体权限    是(为了回显)
			var checkArray  = checkeds.split(",");
			var checkBoxAll = $("input[name='mediaLoginAuth.platform']");
			if(checkArray.length > 1){
				for(var i=0;i<checkArray.length;i++){
					//获取所有复选框对象的value属性，然后，用checkArray[i]和他们匹配，如果有，则说明他应被选中
					$.each(checkBoxAll,function(j,checkbox){
				        //获取复选框的value属性
				        var checkValue=$(checkbox).val();
				        if(checkArray[i]==checkValue){
				            $(checkbox).attr("checked",true);
				        }
				    })
				}
			} */
		});
		function refreshOfficeTree(){
			if($("#dataScope").val()==2){
				$("#officeTree").show();
			}else{
				$("#officeTree").hide();
			}
		}
		//自媒体权限 是:用户类型.平台.标签才会显示  否:全部隐藏
		var updateIsLogin = function (val){
			if(val == 1){
				var _userType = $("[name='mediaLoginAuth.userType']").val();
				if(_userType == 2){
					$("#platform").hide();
				}else{
					$("#platform").show();
				}
				$("#user_type").show();
				$("#userTag").show();
			}else if(val == 0){//自媒体权限下面的数据暂时不显示
				$("#user_type").hide();
				$("#platform").hide();
				$("#userTag").hide();
			}
		}
		//用户类型  选择  管理员 默认平台全选
		var updateUserType = function (obj){
			var type = $(obj).val();
			if(type == 2){
				$("#platform").hide();
			}else{
				$("#platform").show();
			}
		}
	</script>
</head>
<body>
	<form:form id="inputForm" modelAttribute="user" autocomplete="off" action="${ctx}/sys/user/saveAuth" method="post" class="form-horizontal" >
		<form:hidden path="id" id="id"/>
		<input id="isSpecBeautician" name="isSpecBeautician" value="${isSpecBeautician }" type="hidden">
		<input id="isUpdateRole" name="isUpdateRole" type="hidden">
		<input id="isPB" name="isPB" type="hidden">
		<table class="table table-bordered  table-condensed dataTables-example dataTable no-footer">
		   <tbody>
		   	  <tr>
		         <td class="width-15 active"><label class="pull-right">用户后台角色</label></td>
		         <td class="width-35">
		         	<sys:treeselect id="role" name="roleIdList" value="${user.roleIds }" labelName="role.name" labelValue="${user.roleNames}" title="后台角色"
						url="/sys/role/treeData" cssClass=" form-control input-sm required" allowClear="true" notAllowSelectRoot="false" notAllowSelectParent="false" checked="true"/>
				 </td>
		      </tr>
		      <!-- 用户后台数据范围 -->
		      <tr>
		         <td class="width-15 active" style="vertical-align: top;"><label class="pull-right">后台数据范围:</label></td>
		         <td class="width-35"><form:select path="dataScope" class="form-control ">
					<form:options items="${fns:getDictList('sys_data_scope2')}" itemLabel="label" itemValue="value" htmlEscape="false"/>
					</form:select>
					<span class="help-inline">特殊情况下，设置为“按明细设置”，可进行跨机构授权</span>
					<div class="controls" style="margin-top:3px;margin-left: 10px;">
						<div id="officeTree" class="ztree" style="margin-top:3px;margin-left: 10px;"></div>
						<form:hidden path="officeIds"/>
					</div></td>
		      </tr>
		      <tr>
		         <td class="width-15 active" style="vertical-align: top;">
		         	<label class="pull-right">商家数据范围:</label>
		         </td>
		         <td class="width-35">
					<div class="controls" style="margin-top:3px;margin-left: 10px;">
						<sys:treeselect id="company" name="companyIds" value="${user.companyIds }" labelName="companyNames" labelValue="${user.companyNames }"
						title="公司" url="/sys/franchisee/treeData" cssClass="form-control required" checked="true" notAllowSelectParent="true" extId="1"/>
						<span class="help-inline">注:此权限仅适用于发现、直播、问答</span>
					</div>
				</td>
		      </tr>
		      <tr>
		         <td class="width-15 active" style="vertical-align: top;"><label class="pull-right">自媒体权限:</label></td>
		         <td class="width-35">
		         <sys:treeselect id="mdrole" name="mediaRoleIdList" value="${user.mediaRoleIds}" labelName="mediaRole.name" labelValue="${user.mediaRoleNames}" title="自媒体角色"
						url="/train/mdrole/treeData?modeid=8" cssClass=" form-control input-sm required" allowClear="true" notAllowSelectRoot="false" notAllowSelectParent="false"/>
		         	<%-- <c:if test="${not empty user.mediaLoginAuth.isLogin}">
				        <c:if test="${user.mediaLoginAuth.isLogin==1}">
							<label><input id="isLogin" name="mediaLoginAuth.isLogin" type="radio" value="1" class="form" onclick="updateIsLogin(1)" <c:if test="${user.mediaLoginAuth.isLogin==1}">checked="checked"</c:if>/>是</label>
							<label><input id="isLogin" name="mediaLoginAuth.isLogin" type="radio" value="0" class="form" onclick="updateIsLogin(0)" <c:if test="${user.mediaLoginAuth.isLogin==0}">checked="checked"</c:if>/>否</label>
						</c:if>
						<c:if test="${user.mediaLoginAuth.isLogin==0}">
							<label><input id="isLogin" name="mediaLoginAuth.isLogin" type="radio" value="1" class="form" onclick="updateIsLogin(1)" <c:if test="${user.mediaLoginAuth.isLogin==1}">checked="checked"</c:if>/>是</label>
							<label><input id="isLogin" name="mediaLoginAuth.isLogin" type="radio" value="0" class="form" onclick="updateIsLogin(0)" <c:if test="${user.mediaLoginAuth.isLogin==0}">checked="checked"</c:if>/>否</label>
						</c:if>
					</c:if>
					<c:if test="${empty user.mediaLoginAuth.isLogin}">
						<label><input id="isLogin" name="mediaLoginAuth.isLogin" type="radio" value="1" class="form" onclick="updateIsLogin(1)"/>是</label>
						<label><input id="isLogin" name="mediaLoginAuth.isLogin" type="radio" value="0" class="form" onclick="updateIsLogin(0)" checked="checked"/>否</label>
					</c:if> --%>
				</td>
		      </tr>
		      <%-- <tr id="user_type">
		         <td class="width-15 active" style="vertical-align: top;"><label class="pull-right"><font color="red">*</font>用户类型:</label></td>
		         <td class="width-35">
		         	<form:select path="mediaLoginAuth.userType" class="form-control" onchange="updateUserType(this)">
						<form:options items="${fns:getDictList('media_user_type')}" itemLabel="label" itemValue="value" htmlEscape="false"/>
					</form:select>
					<input type="hidden" id="_userType" name="_userType" value="${user.mediaLoginAuth.userType}" />
				 </td>
		      </tr>
		      <tr id="platform">
		         <td class="width-15 active" style="vertical-align: top;"><label class="pull-right"><font color="red">*</font>平台:</label></td>
		         <td class="width-35">
					<form:checkboxes items="${fns:getDictList('media_platform')}" itemLabel="label" itemValue="value" path="mediaLoginAuth.platform" />
					<input type="hidden" id="_platform" name="_platform" value="${user.mediaLoginAuth.platform}" />
				 </td>
		      </tr>
		      <tr id="userTag">
		         <td class="width-15 active" style="vertical-align: top;"><label class="pull-right"><font color="red">*</font>标签:</label></td>
		         <td class="width-35">
		         	<form:input path="mediaLoginAuth.userTag" htmlEscape="false" maxlength="6" class="form-control"/>
		         	<span class="help-inline">每人一个标签,最多6个字</span>
		         </td>
		      </tr> --%>
			</tbody>
		</table>
	</form:form>
</body>
</html>