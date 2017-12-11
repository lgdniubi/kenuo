<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/webpage/include/taglib.jsp"%>
<%@ include file="/webpage/include/icheck.jsp"%>
<html>
	<head>
		<title>添加角色及权限</title>
		<meta name="decorator" content="default" />
		<%@include file="/webpage/include/treeview.jsp" %>
		<link rel="stylesheet" type="text/css" href="${ctxStatic}/train/css/exam.css">
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
				tree = $.fn.zTree.init($("#newUserOffice"), setting, data);
				// 不选择父节点
				//tree.setting.check.chkboxType = { "Y" : "", "N" : "" };
				//默认战开第一级菜单
				var nodes = tree.getNodes();
				tree.expandNode(nodes[0], true);
			},"json")
		})
		
		lastValue = "";
		function searchNode() {
			// 取得输入的关键字的值
			//var value = $.trim(key.get(0).value);
		    var value = $('#search_condition').val();
			// 按名字查询
			var keyType = "name";<%--
			if (key.hasClass("empty")) {
				value = "";
			}--%>
			
			// 如果和上次一次，就退出不查了。
			if (lastValue === value) {
				//等待样式隐藏
				//$(".loading").hide();
				return;
			}
			
			// 保存最后一次
			lastValue = value;
			
			var nodes = tree.getNodes();
			// 如果要查空字串，就退出不查了。
			if (value == "") {
				showAllNode(nodes);
				//等待样式隐藏
				//$(".loading").hide();
				return;
			}
			hideAllNode(nodes);
			nodeList = tree.getNodesByParamFuzzy(keyType, value);
			//等待0.1秒    展示等待效果
			setTimeout(function(){
				updateNodes(nodeList)
				}, 100);
		}
		
		//隐藏所有节点
		function hideAllNode(nodes){
			nodes = tree.transformToArray(nodes);
			for(var i=nodes.length-1; i>=0; i--) {
				tree.hideNode(nodes[i]);
			}
		}
		
		//显示所有节点
		function showAllNode(nodes){			
			nodes = tree.transformToArray(nodes);
			for(var i=nodes.length-1; i>=0; i--) {
				/* if(!nodes[i].isParent){
					tree.showNode(nodes[i]);
				}else{ */
					if(nodes[i].getParentNode()!=null){
						tree.expandNode(nodes[i],false,false,false,false);
					}else{
						tree.expandNode(nodes[i],true,true,false,false);
					}
					tree.showNode(nodes[i]);
					showAllNode(nodes[i].children);
				/* } */
			}
		}
		
		//更新节点状态
		function updateNodes(nodeList) {
			tree.showNodes(nodeList);
			for(var i=0, l=nodeList.length; i<l; i++) {
				
				//2017年8月29日09:24:26   小叶添加   搜索上级条件时，若有子类则能看到子类
				nodes = tree.transformToArray(nodeList[i].children);
				for(var j=nodes.length-1; j>=0; j--) {
					tree.showNode(nodes[j]);
				}
				
				//展开当前节点的父节点
				tree.showNode(nodeList[i].getParentNode()); 
				//tree.expandNode(nodeList[i].getParentNode(), true, false, false);
				//显示展开符合条件节点的父节点
				while(nodeList[i].getParentNode()!=null){
					tree.expandNode(nodeList[i].getParentNode(), true, false, false);
					nodeList[i] = nodeList[i].getParentNode();
					tree.showNode(nodeList[i].getParentNode());
				}
				//显示根节点
				tree.showNode(nodeList[i].getParentNode());
				//展开根节点
				tree.expandNode(nodeList[i].getParentNode(), true, false, false);
			}
		}
		</script>
	</head>
	<body>
		<input id="search_condition" type="text" placeholder="请输入名称" class="form-control" style="width: 180px;margin:0 auto; "/>
		<button class="btn btn-primary btn-rounded btn-outline btn-sm " onclick="searchNode()">
			<i class="fa fa-search"></i> 查询
		</button>
		<form action="" method="post">
			<input type="hidden" id="offtreeId">
			<input type="hidden" id="offtreeName">
		</form>
		<div class="ztree" id="newUserOffice"></div>
	</body>
</html>