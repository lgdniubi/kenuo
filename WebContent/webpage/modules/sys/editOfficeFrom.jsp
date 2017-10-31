<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/webpage/include/taglib.jsp"%>
<%@ include file="/webpage/include/icheck.jsp"%>
<html>
	<head>
		<title>修改权限</title>
		<meta name="decorator" content="default" />
		<%@include file="/webpage/include/treeview.jsp" %>
		<link rel="stylesheet" type="text/css" href="${ctxStatic}/common/training.css">
		<script type="text/javascript">
			$(function() {
				var setting = {check:{enable:true,autoCheckTrigger: false},view:{selectedMulti:true},
						data:{simpleData:{enable:true,idKey: "id",pIdKey: "pId"}},callback:{onClick: function (event, treeId, treeNode) { 
						} }};
				/* 加载权限树 */
				$.post("${ctx}/sys/office/treeData",null,function(officeData){
					// 初始化树结构
					var tree = $.fn.zTree.init($("#editOffice"), setting, officeData);
					// 不选择父节点
					tree.setting.check.chkboxType = { "Y" : "", "N" : "" };
					var ids = "${officeIds}".split(",");
					for(var i=0; i<ids.length; i++) {
						var node = tree.getNodeByParam("id",ids[i]);
						try{tree.checkNode(node, true, false);}catch(e){}
						if (node.pId != '0') {
							// 展开选中的节点
							var nodeP = tree.getNodeByParam("id",node.pId);
						tree.expandNode(nodeP,true,false); 
						}
					}
				},"json")
			})
			
			/* function doSubmit(){
				var officeTree = $.fn.zTree.getZTreeObj("editOffice");
				var officeTreeIds = officeTree.getCheckedNodes(true);
				var officeIds = [];
				for (var i = 0; i < officeTreeIds.length; i++) {
					officeIds.push(officeTreeIds[i].id); 
				}
				alert("aa");
				if (officeIds != "") {
					var id = "${id}";
					var userId = "${userId}";
					$("#officeIds").val(officeIds);	
					alert($("#officeIds").val());
					alert($("#id").val());
					$("#editForm").submit();
					//Thread.sleep (1000); 
					//loading('正在提交，请稍等...');
					//window.location="${ctx}/sys/user/updateOffice?id="+id+"&userId="+userId+"&officeIds="+officeIds
				}else{
					alert("请先选择权限！");
					return false;
				}
					return true;
				
			} */
		</script>
	</head>
	<body>
	<%-- <form:form id="inputForm" modelAttribute="role" autocomplete="off" action="${ctx}/sys/role/save" method="post" class="form-horizontal" > --%>
		<%-- <form action="${ctx}/sys/user/updateOffice" id="editForm" method="post"> --%>
		<form:form id="editForm" modelAttribute="role" action="${ctx}/sys/user/updateOffice" method="post">
			<input type="hidden" id="userId" name="userId" value="${userId }">
			<input type="hidden" id="id" name="id" value="${id }">
			<input type="hidden" id="officeIds" name="officeIds" value="${officeIds }">
		</form:form>
			<div class="ztree" id="editOffice"></div>
	</body>
</html>