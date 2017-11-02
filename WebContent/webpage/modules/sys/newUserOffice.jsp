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
			var setting = {check:{enable:false,autoCheckTrigger: false},view:{selectedMulti:false},
					data:{simpleData:{enable:true,idKey: "id",pIdKey: "pId"}},callback:{onClick: function (event, treeId, treeNode) { 
						$("#offtreeId").val(treeNode.id);
						$("#offtreeName").val(treeNode.name);
					} }};
			/* 加载权限树 */
			var compId = "${compId}";
			$.post("${ctx}/sys/office/newOfficeTreeData",{compId:compId},function(data){
				// 初始化树结构
				var tree = $.fn.zTree.init($("#newUserOffice"), setting, data);
				// 不选择父节点
				//tree.setting.check.chkboxType = { "Y" : "", "N" : "" };
				//默认战开第一级菜单
				var nodes = tree.getNodes();
				tree.expandNode(nodes[0], true);
			},"json")
		})
		</script>
	</head>
	<body>
		<form action="" method="post">
			<input type="hidden" id="offtreeId">
			<input type="hidden" id="offtreeName">
		</form>
		<div class="ztree" id="newUserOffice"></div>
	</body>
</html>