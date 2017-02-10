<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/webpage/include/taglib.jsp"%>
<html>
<head>
<title>区域管理</title>
	<meta name="decorator" content="default" />
	<%@include file="/webpage/include/treeview.jsp" %>
	<link rel="stylesheet" href="${ctxStatic}/ec/css/loading.css">
	<style type="text/css">
		.ztree {overflow:auto;margin:0;_margin-top:10px;padding:10px 0 0 10px;}
		#left{width:100% !important;}
		 .ztree li{max-width:800px;}
		  .ztree li > a{width:100%;display:inline-block;height:22px;position:relative}
		  .ztree li a .btn{height:20px;line-height:20px;margin-left:5px;padding:0 3px;}
		  .ztree li .brn-group{position:absolute;top:0;right:0;height:20px; width:350px;}
	</style>
	<script type="text/javascript">
		//刷新
		function refresh(){
			window.location="${ctx}/sys/area/list";
		}
	</script>
</head>
<body>
	<div class="wrapper wrapper-content">
		<div class="ibox">
			<!-- 标题 -->
			<div class="ibox-title">
				<h5>区域列表</h5>
			</div>
			<!-- 主体内容 -->
			<div class="ibox-content">
				<sys:message content="${message}" />
				<!-- 查询条件 -->
				<div class="row">
					<div class="col-sm-12">
						<!-- 无查询条件 -->
					</div>
				</div>
				<!-- 工具栏 -->
				<div class="row">
					<div class="col-sm-12">
						<!-- <div class="pull-left">
							<button class="btn btn-white btn-sm " data-toggle="tooltip" data-placement="left" onclick="refresh()" title="刷新">
								<i class="glyphicon glyphicon-repeat"></i> 刷新
							</button>
						</div>
						<div id="ztree" class="ztree"></div>
						<div class="loading"></div> -->
					</div>
				</div>
				<div id="left" style="background-color:#e7eaec;" class="col-sm-1">
						<div style="padding-left: 10px;padding-top: 10px;width: 100%;height: 30px;">
							<input id="search_condition" type="text" placeholder="请输入区域名称" class="form-control pull-left" style="width: 200px;margin-top: 10px"/>
							<button class="btn btn-primary btn-rounded btn-outline btn-sm " onclick="searchNode()" style="margin-top: 13px;margin-left: 10px;">
								<i class="fa fa-search"></i> 查询
							</button>
							<button onclick="refresh()" class="btn btn-primary btn-rounded btn-outline btn-sm" style="margin-top: 13px;margin-left: 10px">
								<i class="fa fa-refresh"></i> 刷新
							</button>
						</div>
						<div id="ztree" class="ztree"></div>
						<div class="loading"></div>
					</div>
			</div>
		</div>
	</div>
	<!-- 用于异步加载时，操作权限设置 -->
	<shiro:hasPermission name="sys:area:view">
		<input type="hidden" id="shiroView" value="1">
	</shiro:hasPermission>
	<shiro:hasPermission name="sys:area:edit">
		<input type="hidden" id="shiroEdit" value="1">
	</shiro:hasPermission>
	<shiro:hasPermission name="sys:area:del">
		<input type="hidden" id="shiroDel" value="1">
	</shiro:hasPermission>
	<shiro:hasPermission name="sys:area:add">
		<input type="hidden" id="shiroAdd" value="1">
	</shiro:hasPermission>
	<script type="text/javascript">
		var lastValue = "";
		var tree, setting = {data:{simpleData:{enable:true,idKey:"id",rootPId:'0'}},
			view: {
				addDiyDom: addDiyDom
			}
		};
		$(document).ready(function(){
			$.getJSON("${ctx}/sys/area/newTreeData",function(data){
				tree = $.fn.zTree.init($("#ztree"), setting, data);
			});
		});
		
		
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
		
		function addDiyDom(treeId, treeNode) {
 			var aObj = $("#" + treeNode.tId + "_a");
			var editStr = "<span class='brn-group'>"+treeNode.code+"　　";
			if($("#shiroView").val() == 1){
				editStr = editStr + "<a href=\"#\" onclick=\"openDialogView('查看区域', '${ctx}/sys/area/form?id="+treeNode.id+"','800px', '380px')\" class=\"btn btn-info btn-xs\" ><i class=\"fa fa-search-plus\"></i> 查看</a>";
			}
			if($("#shiroEdit").val() == 1){
				editStr = editStr + "<a href=\"#\" onclick=\"openDialog('修改区域', '${ctx}/sys/area/form?id="+treeNode.id+"','800px', '380px')\" class=\"btn btn-success btn-xs\" ><i class=\"fa fa-edit\"></i> 修改</a>";
			}
			if($("#shiroDel").val() == 1){
				editStr = editStr + "<a href=\"${ctx}/sys/area/delete?id="+treeNode.id+"\" onclick=\"return confirmx('要删除该区域及所有子区域项吗？', this.href)\" class=\"btn btn-danger btn-xs\" ><i class=\"fa fa-trash\"></i> 删除</a>";
			}
			if($("#shiroAdd").val() == 1){
				editStr = editStr + "<a href=\"#\" onclick=\"openDialog('添加下级区域', '${ctx}/sys/area/form?parent.id="+treeNode.id+"','800px', '380px')\" class=\"btn btn-primary btn-xs\" ><i class=\"fa fa-plus\"></i> 添加下级区域</a>";
			}
			editStr+='</span>';
			aObj.append(editStr);
		}
	</script>
	<script src="${ctxStatic}/common/wsize.min.js" type="text/javascript"></script>
</body>
</html>