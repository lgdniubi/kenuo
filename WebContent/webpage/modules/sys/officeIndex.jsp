<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/webpage/include/taglib.jsp"%>
<html>
<head>
	<title>机构管理</title>
	<meta name="decorator" content="default"/>
	<link rel="stylesheet" href="${ctxStatic}/ec/css/loading.css">
	<%@include file="/webpage/include/treeview.jsp" %>
	<style type="text/css">
		.ztree {overflow:auto;margin:0;_margin-top:10px;padding:10px 0 0 10px;}
	</style>
	<script type="text/javascript">
		function refresh(){//刷新
			window.location="${ctx}/sys/office/";
		}
	</script>
</head>
<body class="gray-bg">
	<div class="wrapper wrapper-content">
	<div class="ibox">
	<div class="ibox-content">
	<sys:message content="${message}"/>
	<div id="content" class="row">
		<div id="left" style="background-color:#e7eaec" class="col-sm-1">
			<input id="search_condition" type="text" placeholder="请输入机构名称" class="form-control pull-left" style="width: 200px;margin-top: 10px" onblur="searchNode()"/>
			<a onclick="refresh()" class="pull-right" style="margin-top: 18px">
				<i class="fa fa-refresh"></i>
			</a>
			<div id="ztree" class="ztree"></div>
			<div class="loading"></div>
		</div>
		<div id="right"  class="col-sm-11  animated fadeInRight">
			<iframe id="officeContent" name="officeContent" src="${ctx}/sys/office/list?id=&parentIds=" width="100%" height="91%" frameborder="0"></iframe>
		</div>
	</div>
	</div>
	</div>
	</div>
	<script type="text/javascript">
		var lastValue = "";
		var tree, setting = {data:{simpleData:{enable:true,idKey:"id",pIdKey:"pId",rootPId:'0'}},
			callback:{onClick:function(event, treeId, treeNode){
					var id = treeNode.id == '0' ? '' :treeNode.id;
					$('#officeContent').attr("src","${ctx}/sys/office/list?id="+id+"&parentIds="+treeNode.pIds);
				}
			}
		};
		$(document).ready(function(){
			$.getJSON("${ctx}/sys/office/treeData",function(data){
				tree = $.fn.zTree.init($("#ztree"), setting, data);
			});
		});
		function refreshTree(){
			$.getJSON("${ctx}/sys/office/treeData",function(data){
				$.fn.zTree.init($("#ztree"), setting, data).expandAll(false);
			});
		}
		//refreshTree();
		 
		var leftWidth = 220; // 左侧窗口大小
		var htmlObj = $("html"), mainObj = $("#main");
		var frameObj = $("#left, #openClose, #right, #right iframe");
		function wSize(){
			var strs = getWindowSize().toString().split(",");
			htmlObj.css({"overflow-x":"hidden", "overflow-y":"hidden"});
			mainObj.css("width","auto");
			frameObj.height(strs[0] - 120);
			var leftWidth = ($("#left").width() < 0 ? 0 : $("#left").width());
			$("#right").width($("#content").width()- leftWidth - $("#openClose").width() -60);
			$(".ztree").width(leftWidth - 10).height(frameObj.height() - 46);
		}
		
		//搜索节点
		function searchNode() {
			$(".loading").show();
			// 取得输入的关键字的值
			//var value = $.trim(key.get(0).value);
		    var value = $('#search_condition').val();
			// 按名字查询
			var keyType = "name";
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
		//隐藏所有节点
		function hideAllNode(nodes){
			nodes = tree.transformToArray(nodes);
			for(var i=nodes.length-1; i>=0; i--) {
				tree.hideNode(nodes[i]);
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
	<script src="${ctxStatic}/common/wsize.min.js" type="text/javascript"></script>
</body>
</html>