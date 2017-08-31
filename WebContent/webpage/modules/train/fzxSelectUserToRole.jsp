<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/webpage/include/taglib.jsp"%>
<html>
<head>
	<title>分配角色</title>
	<meta name="decorator" content="blank"/>
	<link rel="stylesheet" href="${ctxStatic}/ec/css/loading.css">
	<%@include file="/webpage/include/treeview.jsp" %>
	<script type="text/javascript">
	
		var officeTree;
		var selectedTree;//zTree已选择对象
		var lastValue = "";
		
		// 初始化
		$(document).ready(function(){
			officeTree = $.fn.zTree.init($("#officeTree"), setting, officeNodes);
			selectedTree = $.fn.zTree.init($("#selectedTree"), setting, selectedNodes);
		});

		var setting = {view: {selectedMulti:false,nameIsHTML:true,showTitle:false,dblClickExpand:false},
				data: {simpleData: {enable: true}},
				callback: {onClick: treeOnClick}};
		
		var officeNodes=[
	            <c:forEach items="${officeList}" var="office">
	            {id:"${office.id}",
	             pId:"${not empty office.parent?office.parent.id:0}", 
	             name:"${office.name}"},
	            </c:forEach>];
	
		var pre_selectedNodes =[
   		        <c:forEach items="${userList}" var="user">
   		        {id:"${user.id}",
   		         pId:"0",
   		         name:"<font color='red' style='font-weight:bold;'>${user.name}</font>"},
   		        </c:forEach>];
		
		var selectedNodes =[
		        <c:forEach items="${userList}" var="user">
		        {id:"${user.id}",
		         pId:"0",
		         name:"<font color='red' style='font-weight:bold;'>${user.name}</font>"},
		        </c:forEach>];
		
		var pre_ids = "${selectIds}".split(",");
		var ids = "${selectIds}".split(",");
		
		//点击选择项回调
		function treeOnClick(event, treeId, treeNode, clickFlag){
			$.fn.zTree.getZTreeObj(treeId).expandNode(treeNode);
			if("officeTree"==treeId){
				$.get("${ctx}/sys/role/users?officeId=" + treeNode.id, function(userNodes){
					$.fn.zTree.init($("#userTree"), setting, userNodes);
				});
			}
			if("userTree"==treeId){
				//alert(treeNode.id + " | " + ids);
				//alert(typeof ids[0] + " | " +  typeof treeNode.id);
				if($.inArray(String(treeNode.id), ids)<0){
					selectedTree.addNodes(null, treeNode);
					ids.push(String(treeNode.id));
				}
			};
			if("selectedTree"==treeId){
				if($.inArray(String(treeNode.id), pre_ids)<0){
					selectedTree.removeNode(treeNode);
					ids.splice($.inArray(String(treeNode.id), ids), 1);
				}else{
					top.$.jBox.tip("角色原有成员不能清除！", 'info');
				}
			}
		};
		function clearAssign(){
			var submit = function (v, h, f) {
			    if (v == 'ok'){
					var tips="";
					if(pre_ids.sort().toString() == ids.sort().toString()){
						tips = "未给角色【${role.name}】分配新成员！";
					}else{
						tips = "已选人员清除成功！";
					}
					ids=pre_ids.slice(0);
					selectedNodes=pre_selectedNodes;
					$.fn.zTree.init($("#selectedTree"), setting, selectedNodes);
			    	top.$.jBox.tip(tips, 'info');
			    } else if (v == 'cancel'){
			    	// 取消
			    	top.$.jBox.tip("取消清除操作！", 'info');
			    }
			    return true;
			};
			tips="确定清除角色【${role.name}】下的已选人员？";
			top.$.jBox.confirm(tips, "清除确认", submit);
		};
		
		//搜索节点
		function searchNode() {
			$(".loading").show();
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
				$(".loading").hide();
				return;
			}
			
			// 保存最后一次
			lastValue = value;
			
			var nodes = officeTree.getNodes();
			// 如果要查空字串，就退出不查了。
			if (value == "") {
				showAllNode(nodes);
				//等待样式隐藏
				$(".loading").hide();
				return;
			}
			hideAllNode(nodes);
			nodeList = officeTree.getNodesByParamFuzzy(keyType, value);
			//等待0.1秒    展示等待效果
			setTimeout(function(){
				updateNodes(nodeList)
				}, 100);
		}
		
		//隐藏所有节点
		function hideAllNode(nodes){
			nodes = officeTree.transformToArray(nodes);
			for(var i=nodes.length-1; i>=0; i--) {
				officeTree.hideNode(nodes[i]);
			}
		}
		
		//显示所有节点
		function showAllNode(nodes){			
			nodes = officeTree.transformToArray(nodes);
			for(var i=nodes.length-1; i>=0; i--) {
				/* if(!nodes[i].isParent){
					tree.showNode(nodes[i]);
				}else{ */
					if(nodes[i].getParentNode()!=null){
						officeTree.expandNode(nodes[i],false,false,false,false);
					}else{
						officeTree.expandNode(nodes[i],true,true,false,false);
					}
					officeTree.showNode(nodes[i]);
					showAllNode(nodes[i].children);
				/* } */
			}
		}
		
		//更新节点状态
		function updateNodes(nodeList) {
			officeTree.showNodes(nodeList);
			for(var i=0, l=nodeList.length; i<l; i++) {
				
				//2017年8月29日09:24:26   小叶添加   搜索上级条件时，若有子类则能看到子类
				nodes = officeTree.transformToArray(nodeList[i].children);
				for(var j=nodes.length-1; j>=0; j--) {
					officeTree.showNode(nodes[j]);
				}
				
				//展开当前节点的父节点
				officeTree.showNode(nodeList[i].getParentNode()); 
				//tree.expandNode(nodeList[i].getParentNode(), true, false, false);
				//显示展开符合条件节点的父节点
				while(nodeList[i].getParentNode()!=null){
					officeTree.expandNode(nodeList[i].getParentNode(), true, false, false);
					nodeList[i] = nodeList[i].getParentNode();
					officeTree.showNode(nodeList[i].getParentNode());
				}
				//显示根节点
				officeTree.showNode(nodeList[i].getParentNode());
				//展开根节点
				officeTree.expandNode(nodeList[i].getParentNode(), true, false, false);
			}
			$(".loading").hide();
		}
	</script>
</head>
<body>
	
	<div id="assignRole" class="row wrapper wrapper-content">
		<div class="col-sm-4" style="border-right: 1px solid #A8A8A8;">
			<p>所在部门：</p>
			<p>
				<input id="search_condition" type="text" placeholder="请输入名称"/>
				<button onclick="searchNode()"> 查询</button>
			</p>
			<div id="officeTree" class="ztree"></div>
		</div>
		<div class="col-sm-4">
			<p>待选人员：</p>
			<div id="userTree" class="ztree"></div>
		</div>
		<div class="col-sm-4" style="padding-left:16px;border-left: 1px solid #A8A8A8;">
			<p>已选人员：</p>
			<div id="selectedTree" class="ztree"></div>
		</div>
	</div>
	<div class="loading"></div>
</body>
</html>
