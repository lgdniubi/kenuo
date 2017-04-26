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
		var oldRoles;
		var a = true;
		function doSubmit(){//回调函数，在编辑和保存动作时，供openDialog调用提交表单。
			if(validateForm.form()){
				var roles = $("#roleId").val();
				if((oldRoles.indexOf('cc78d1a10a584999af86410c0a4fabdb') != -1)&&(roles.indexOf('cc78d1a10a584999af86410c0a4fabdb')== -1)){
						$.ajax({
						  async:false,
				          type:"get",  
				          url:"${ctx}/sys/user/isSpecBeautician",  
				          data:{'id':$('#id').val()},  
				          success: function(data){ 
				              if (data == 'false'){
				            	  top.layer.alert('该用户为特殊美容师，无法将其排班角色删除', {icon: 0, title:'提醒'});
				            	  a = false;
				              }
				          }
					 });
					if(a == false){
						return;						
					}						
				}
				var ids2 = [], nodes2 = tree2.getCheckedNodes(true);
				for (var i=0; i<nodes2.length; i++) {
					var halfCheck = nodes2[i].getCheckStatus();
					if (!halfCheck.half){//不为半选
						ids2.push(nodes2[i].id);
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
			oldRoles = $("#roleId").val();
			
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
		});
		function refreshOfficeTree(){
			if($("#dataScope").val()==2){
				$("#officeTree").show();
			}else{
				$("#officeTree").hide();
			}
		}
	</script>
</head>
<body>
	<form:form id="inputForm" modelAttribute="user" autocomplete="off" action="${ctx}/sys/user/saveAuth" method="post" class="form-horizontal" >
		<form:hidden path="id" id="id"/>
		<table class="table table-bordered  table-condensed dataTables-example dataTable no-footer">
		   <tbody>
		   	  <tr>
		         <td class="width-15 active"><label class="pull-right">用户角色</label></td>
		         <td class="width-35">
		         	<sys:treeselect id="role" name="roleIdList" value="${user.roleIds }" labelName="role.name" labelValue="${user.roleNames}" title="角色"
						url="/sys/role/treeData" cssClass=" form-control input-sm required" allowClear="true" notAllowSelectRoot="false" notAllowSelectParent="false" checked="true"/>
				 </td>
		      </tr>
		      <tr>
		         <td class="width-15 active" style="vertical-align: top;"><label class="pull-right">数据范围:</label></td>
		         <td class="width-35"><form:select path="dataScope" class="form-control ">
					<form:options items="${fns:getDictList('sys_data_scope2')}" itemLabel="label" itemValue="value" htmlEscape="false"/>
					</form:select>
					<span class="help-inline">特殊情况下，设置为“按明细设置”，可进行跨机构授权</span>
					<div class="controls" style="margin-top:3px;margin-left: 10px;">
						<div id="officeTree" class="ztree" style="margin-top:3px;margin-left: 10px;"></div>
						<form:hidden path="officeIds"/>
					</div></td>
		      </tr>
			</tbody>
			</table>
	</form:form>
</body>
</html>