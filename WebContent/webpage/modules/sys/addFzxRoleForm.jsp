<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/webpage/include/taglib.jsp"%>
<%@ include file="/webpage/include/icheck.jsp"%>
<html>
	<head>
	<title>添加角色及权限</title>
	<meta name="decorator" content="default" />
	<%@include file="/webpage/include/treeview.jsp" %>
	<link rel="stylesheet" type="text/css" href="${ctxStatic}/common/training.css">
	
	<script type="text/javascript">
		$(function() {
			var setting = {check:{enable:true,autoCheckTrigger: true,chkStyle: "radio"},view:{selectedMulti:false},
					data:{simpleData:{enable:true}},callback:{onClick: function (event, treeId, treeNode) { 
						tree1.checkNode(treeNode, !treeNode.checked, true);
					}}};
					
			/* 加载妃子校角色 */
			var fzxRoleIds = "${fzxRoleIds}";
			//alert(fzxRoleIds);
			$.post("${ctx}/train/fzxRole/newTreeData",{fzxRoleIds:fzxRoleIds},function(data){
				var tree1 = $.fn.zTree.init($("#fzxRoleTree"), setting, data);
				// 不选择父节点
				tree1.setting.check.chkboxType = { "Y" : "", "N" : "" };
				
			},"json");
			
			var setting2 = {check:{enable:true,autoCheckTrigger: false},view:{selectedMulti:false},
					data:{simpleData:{enable:true}},callback:{onCheck: function (event, treeId, treeNode) { 
						var treeObj = $.fn.zTree.getZTreeObj("officeTree");
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
						}else{ 
							setFontCss(false,treeNode,treeObj);
							//取消节点禁止选中
							treeNode.chkDisabled = false;
							//如果是取消选中则更新节点状态
							treeObj.updateNode(treeNode);
							var nodes = treeNode.children;
							if (nodes != null) {
								for (var i = 0; i < nodes.length; i++) {
									setFontCss(false,nodes[i],treeObj);
									treeObj.updateNode(nodes[i]);//更新改变的节点
									nodes[i].chkDisabled = false;
									if (nodes[i].isParent) {
										recoveryOffice(nodes[i],treeObj);
									}
								}
							}
						}
							
					}}};
			
			//改变字体颜色
			function setFontCss(checked,node,treeObj) {
				if (checked) {
					node.nocheck = true;
				}else{
					node.nocheck = false;
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
			
			/* 加载权限树 */
			$.post("${ctx}/sys/office/newTreeData",null,function(officeData){
				// 初始化树结构
				var tree = $.fn.zTree.init($("#officeTree"), setting2, officeData);
				// 不选择父节点
				tree.setting.check.chkboxType = { "Y" : "", "N" : "" };
				// 默认展开全部节点
				//tree.expandAll(true); 
				
			},"json")
		})
	</script>
	</head>
	<body>
		<table class="table table-striped table-bordered table-hover table-condensed dataTables-example dataTable">
			<thead>
				<tr>
					<th style="text-align: center;"><font color="red">*</font>角色</th>
					<th style="text-align: center;">权限</th>
				</tr>
			</thead>
			<tbody>
				<tr>
					<td id="fzxRoleTree" class="ztree" style="vertical-align: top;"></td>
					<td id="officeTree" class="ztree" style="vertical-align: top;"></td>
				</tr>
			</tbody>
		</table>
	</body>
</html>