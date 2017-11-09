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
				var setting = {check:{enable:true,autoCheckTrigger: false},view:{selectedMulti:true,nameIsHTML: true},
						data:{simpleData:{enable:true,idKey: "id",pIdKey: "pId"}},callback:{onCheck: function (event, treeId, treeNode) { 
							var treeObj = $.fn.zTree.getZTreeObj("editOffice");
							var checked = treeNode.checked;
							if (checked) {
								if (treeNode.isParent) {
									//获得此父节点下的所有子节点
									var nodes = treeNode.children;
									for (var i = 0; i < nodes.length; i++) {
										nodes[i].checked = false;
										nodes[i].chkDisabled = true;
										setFontCss(checked,nodes[i],treeObj);
										treeObj.updateNode(nodes[i]);
										if (nodes[i].isParent) {
											screenOffice(nodes[i],treeObj);
										}
									}
								}
							}
							if(!checked){ 
									setFontCss(false,treeNode,treeObj);
									//取消节点禁止选中
									treeNode.chkDisabled = false;
									//如果是取消选中则更新节点状态
									treeObj.updateNode(treeNode);
									var nodes = treeNode.children;
									if (nodes != null) {
										for (var i = 0; i < nodes.length; i++) {
											setFontCss(false,nodes[i],treeObj);
											nodes[i].chkDisabled = false;
											treeObj.updateNode(nodes[i]);//更新改变的节点
											if (nodes[i].isParent) {
											recoveryOffice(treeNode,treeObj);
											}
										}
									} 
									
							}
							
							
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
						if (node != null) {
							try{tree.checkNode(node, true, false);}catch(e){}
							if (node.pId != '0') {
								// 展开选中的节点
								var nodeP = tree.getNodeByParam("id",node.pId);
								tree.expandNode(nodeP,true,false); 
							}
							initOffice(node,tree);
						}
					}
				},"json")
			})
			
			
			
			
			//初始化权限树时让其父节点下的子节点禁止选中
			function initOffice(node,treeObj) {
				if (node.checked) {
					if (node.isParent) {
						var nodes = node.children;
						for (var j = 0; j < nodes.length; j++) {
							nodes[j].checked = false;
							setFontCss(true,nodes[j],treeObj);
							nodes[j].chkDisabled = true;
							treeObj.updateNode(nodes[j]);
							if (nodes[j].isParent) {
								initOffice(nodes[j],treeObj);
							}
							
						}
					}
				} 
			}
			
			//递归遍历取消勾选的父节点下的所有的子节点并更新所有的节点让其可勾选并将字体颜色恢复
			function recoveryOffice(parentTree,treeObj) {
				parentTree.chkDisabled = false;
				setFontCss(false,parentTree,treeObj);
				treeObj.updateNode(parentTree);
				var nodes = parentTree.children;
				for (var i = 0; i < nodes.length; i++) {
					nodes[i].chkDisabled = false;
					setFontCss(false,nodes[i],treeObj);
					treeObj.updateNode(nodes[i]);
					if (nodes[i].isParent) {
						recoveryOffice(nodes[i],treeObj);
					}
				}
			}
			
			//递归遍历得到勾选的父节点下的所有的子节点并让其取消勾选禁止选中以及字体变灰色
			function screenOffice(parentTree,treeObj) {
				parentTree.checked = false;
				parentTree.chkDisabled = true;
				setFontCss(true,parentTree,treeObj);
				treeObj.updateNode(parentTree);
				var nodes = parentTree.children;
				for (var i = 0; i < nodes.length; i++) {
					nodes[i].checked = false;
					nodes[i].chkDisabled = true;
					setFontCss(true,nodes[i],treeObj);
					treeObj.updateNode(nodes[i]);
					if (nodes[i].isParent) {
						screenOffice(nodes[i],treeObj);
					}
				}
			}
			
			//改变节点状态框
			function setFontCss(checked,node,treeObj) {
				if (checked) {
					node.nocheck = true;
				}else{
					node.nocheck = false;
				}
			}
			
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