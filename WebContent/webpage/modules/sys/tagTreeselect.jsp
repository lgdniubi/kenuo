<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/webpage/include/taglib.jsp"%>
<html>
<head>
	<title>数据选择</title>
	<meta name="decorator" content="blank"/>
	<link rel="stylesheet" href="${ctxStatic}/train/css/exam.css">
	<%@include file="/webpage/include/treeview.jsp" %>
	<script type="text/javascript">
		var key, lastValue = "", nodeList = [], type = getQueryString("type", "${url}");
		var nowurl;
		var nowparam;
		var pd;		//异步加载
		if(type==4){
			pd=true;
			nowurl="${ctx}/train/categorys/treeDataLesson";
			nowparam="id=categoryId";
		}else if(type==3){
			pd=true;
			nowurl="${ctx}/sys/user/treeData";
			nowparam="id=officeId";
		}else{
			pd=false;
		}
		var tree, setting = {view:{selectedMulti:false,dblClickExpand:false},check:{enable:"${checked}",nocheckInherit:true},
				async:{
					enable:pd,url:nowurl,autoParam:[nowparam]  
				},
				data:{simpleData:{enable:true}},callback:{<%--
					beforeClick: function(treeId, treeNode){
						if("${checked}" == "true"){
							//tree.checkNode(treeNode, !node.checked, true, true);
							tree.expandNode(treeNode, true, false, false);
						}
					}, --%>
					onClick:function(event, treeId, treeNode){
						tree.expandNode(treeNode);
					},onCheck: function(e, treeId, treeNode){
						var nodes = tree.getCheckedNodes(true);
						for (var i=0, l=nodes.length; i<l; i++) {
							tree.expandNode(nodes[i], true, false, false);
						}
						return false;
					},onAsyncSuccess: function(event, treeId, treeNode, msg){
						var nodes = tree.getNodesByParam("pId", treeNode.id, null);
						for (var i=0, l=nodes.length; i<l; i++) {
							try{tree.checkNode(nodes[i], treeNode.checked, true);}catch(e){}
							//tree.selectNode(nodes[i], false);
						}
						selectCheckNode();
					},onDblClick: function(){//<c:if test="${!checked}">
						top.$.jBox.getBox().find("button[value='ok']").trigger("click");
						//$("input[type='text']", top.mainFrame.document).focus();//</c:if>
					}
				}
			};
		function expandNodes(nodes) {
			if (!nodes) return;
			for (var i=0, l=nodes.length; i<l; i++) {
				tree.expandNode(nodes[i], true, false, false);
				if (nodes[i].isParent && nodes[i].zAsync) {
					expandNodes(nodes[i].children);
				}
			}
		}
		$(document).ready(function(){
			$.get("${ctx}${url}${fn:indexOf(url,'?')==-1?'?':'&'}&extId=${extId}&isAll=${isAll}&module=${module}&t="
					+ new Date().getTime(), function(zNodes){
				// 初始化树结构
				tree = $.fn.zTree.init($("#tree"), setting, zNodes);
				
				// 默认展开一级节点
				// 咖啡 注释 2017年4月5日17:39:27
				/* var nodes = tree.getNodesByParam("level", 0);
				for(var i=0; i<nodes.length; i++) {
					tree.expandNode(nodes[i], true, false, false);
				} */
				//异步加载子节点（加载用户）
				var nodesOne = tree.getNodesByParam("isParent", true);
				for(var j=0; j<nodesOne.length; j++) {
					tree.reAsyncChildNodes(nodesOne[j],"!refresh",true);
				}
				selectCheckNode();
			});
			key = $("#key");
			key.bind("focus", focusKey).bind("blur", blurKey).bind("change cut input propertychange", searchNode);
			key.bind('keydown', function (e){if(e.which == 13){searchNode();}});
			/* setTimeout("search();", "300"); 2017年6月27日16:16:54 新增弹出等待动画时注释   */
		});
		
		// 默认选择节点
		function selectCheckNode(){
			var ids = "${selectIds}".split(",");
			for(var i=0; i<ids.length; i++) {
				var node = tree.getNodeByParam("id", (type==3?"u_":"")+ids[i]);
				if("${checked}" == "true"){
					try{tree.checkNode(node, true, true);}catch(e){}
					tree.selectNode(node, false);
				}else{
					tree.selectNode(node, true);
				}
			}
		}
	  	function focusKey(e) {
			if (key.hasClass("empty")) {
				key.removeClass("empty");
			}
		}
		function blurKey(e) {
			if (key.get(0).value === "") {
				key.addClass("empty");
			}
			searchNode(e);
		}
		
		//搜索节点
		function searchNode() {
			$(".loading").show();
			console.info($("#d"));
			console.info($(".loading"));
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
			
			var nodes = tree.getNodes();
			// 如果要查空字串，就退出不查了。
			if (value == "") {
				showAllNode(nodes);
				//等待样式隐藏
				$(".loading").hide();
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
			$(".loading").hide();
		}
	</script>
</head>
<body>
	<input id="search_condition" type="text" placeholder="请输入名称" class="form-control" style="width: 180px;margin:0 auto; "/>
	<button class="btn btn-primary btn-rounded btn-outline btn-sm " onclick="searchNode()">
		<i class="fa fa-search"></i> 查询
	</button>
	<div id="tree" class="ztree" style="padding:15px 20px;"></div>
	<div class="loading"></div>
</body>