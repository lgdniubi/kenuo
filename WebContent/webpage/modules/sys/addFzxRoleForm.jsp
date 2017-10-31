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
			
			var setting2 = {check:{enable:true,autoCheckTrigger: true},view:{selectedMulti:false},
					data:{simpleData:{enable:true}},callback:{onClick: function (event, treeId, treeNode) { 
					}}};
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
					<th style="text-align: center;">角色</th>
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