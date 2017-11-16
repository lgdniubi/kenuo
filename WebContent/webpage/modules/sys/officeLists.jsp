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
		#left{width:100% !important;}
		 .ztree li{max-width:800px;}
		  .ztree li > a{width:100%;display:inline-block;height:22px;position:relative}
		  .ztree li a .btn{height:20px;line-height:20px;margin-left:5px;padding:0 3px;}
		  .ztree li .brn-group{position:absolute;top:0;right:0;height:20px; width:350px;}
	</style>
	<script type="text/javascript">
		function refresh(){//刷新
			window.location="${ctx}/sys/office/lists";
		}
		
		function delConfirm(id){
			$.ajax({
				type:"post",
				url:"${ctx}/sys/office/delConfirm?id="+id,
				success:function(data){
					if(data == 'success'){
						if(confirm('要删除该机构及所有子机构项吗？')){
							window.location="${ctx}/sys/office/delete?id="+id;
						}
					}else if(data == 'error'){
						top.layer.alert('删除失败!店铺中仍有员工，无法删除', {icon: 2, title:'提醒'});
					}
				},
				error:function(XMLHttpRequest,textStatus,errorThrown){
					    
				}
			});
		}
	</script>
</head>
<body class="gray-bg">
	<div class="wrapper wrapper-content">
		<div class="ibox">
			<div class="ibox-title">
				<h5>机构管理</h5>
			</div>
			<div class="ibox-content">
				<sys:message content="${message}"/>
				<div id="content" class="row">
					<!-- 工具栏 -->
					<div class="row" style="padding-bottom: 10px;">
						<div class="col-sm-12">
							<div class="pull-left">
								<!-- update 导入数据 -->
								<shiro:hasPermission name="sys:office:importPage">
									<a href="#" onclick="openDialog('导入数据', '${ctx}/sys/office/importPage','400px', '220px')" class="btn btn-white btn-sm"><i class="fa fa-folder-open-o"></i> 导入</a>
								</shiro:hasPermission>
								<shiro:hasPermission name="sys:office:export">
									<!-- 导出按钮 -->
									<table:exportExcel url="${ctx}/sys/office/export"></table:exportExcel>
								</shiro:hasPermission>
								<!-- 店铺标签管理  -->
								<shiro:hasPermission name="train:shopSpeciality:list">
									<button class="btn btn-white btn-sm " data-toggle="tooltip" data-placement="left" onclick='top.openTab("${ctx}/train/shopSpeciality/list","店铺标签管理", false)'><i class="fa fa-plus"></i>店铺标签管理</button>	
								</shiro:hasPermission>
							</div>
						</div>
					</div>
					<div id="left" style="background-color:#e7eaec;" class="col-sm-1">
						<div style="padding-left: 10px;padding-top: 10px;width: 100%;height: 30px;">
							<input id="search_condition" type="text" placeholder="请输入机构名称" class="form-control pull-left" style="width: 200px;margin-top: 10px"/>
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
	</div>
	<!-- 操作权限设置 -->
	<shiro:hasPermission name="sys:office:view">
		<input type="hidden" id="shiroView" value="1">
	</shiro:hasPermission>
	<shiro:hasPermission name="sys:office:edit">
		<input type="hidden" id="shiroEdit" value="1">
	</shiro:hasPermission>
	<shiro:hasPermission name="sys:office:del">
		<input type="hidden" id="shiroDel" value="1">
	</shiro:hasPermission>
	<shiro:hasPermission name="sys:office:add">
		<input type="hidden" id="shiroAdd" value="1">
	</shiro:hasPermission>
	<shiro:hasPermission name="train:arrange:ArrangeOrdinary">
		<input type="hidden" id="arrangeOrdinary" value="1">
	</shiro:hasPermission>
	<shiro:hasPermission name="ec:specEquipment:shopComEquipmentList">
		<input type="hidden" id="specEquipment" value="1">
	</shiro:hasPermission>
	<script type="text/javascript">
		var lastValue = "";
		var tree, setting = {data:{simpleData:{enable:true,idKey:"id",pIdKey:"pId",rootPId:'0'}},
			/* callback:{onClick:function(event, treeId, treeNode){
					var id = treeNode.id == '0' ? '' :treeNode.id;
					$('#officeContent').attr("src","${ctx}/sys/office/list?id="+id+"&parentIds="+treeNode.pIds);
				}
			}, */
			view: {
				addDiyDom: addDiyDom
			}
		};
		$(document).ready(function(){
			$.getJSON("${ctx}/sys/office/newTreeData",function(data){
				tree = $.fn.zTree.init($("#ztree"), setting, data);
			});
		});
		function refreshTree(){
			$.getJSON("${ctx}/sys/office/newTreeData",function(data){
				$.fn.zTree.init($("#ztree"), setting, data).expandAll(true);
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
				
				//2017年11月8日15:38:41   小叶添加   搜索上级条件时，若有子类则能看到子类
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
			$(".loading").hide();
		}
		function addDiyDom(treeId, treeNode) {
			var aObj = $("#" + treeNode.tId + "_a");
			var editStr = "<span class='brn-group'>"+treeNode.code+"　　";
			if(treeNode.id != 1 && treeNode.grade != 1){
				if($("#shiroAdd").val() == 1){
					editStr = editStr + "<a href=\"#\" onclick=\"openDialog('添加下级机构', '${ctx}/sys/office/form?parent.id="+treeNode.id+"','800px', '620px')\" class=\"btn  btn-primary btn-xs\"><i class=\"fa fa-plus\"></i> 添加下级机构</a>";
				}
			}
			if(treeNode.id.length > 7){
				//查看权限
				if($("#shiroView").val() == 1){
					editStr = editStr + "<a href=\"#\" onclick=\"openDialogView('查看机构', '${ctx}/sys/office/form?id="+treeNode.id+"&&num="+treeNode.num+"','800px', '620px')\" class=\"btn btn-info btn-xs\"><i class=\"fa fa-search-plus\"></i> 查看</a>";
				}
				if($("#shiroEdit").val() == 1){
					editStr = editStr + "<a href=\"#\" onclick=\"openDialog('修改机构', '${ctx}/sys/office/form?id="+treeNode.id+"&&num="+treeNode.num+"','800px', '620px')\" class=\"btn btn-success btn-xs\"><i class=\"fa fa-edit\"></i> 修改</a>";
				}
				if($("#shiroDel").val() == 1){
					editStr = editStr + "<a href=\"#\" onclick=\"delConfirm('"+treeNode.id +"')\" class=\"btn btn-danger btn-xs\"><i class=\"fa fa-trash\"></i> 删除</a>";
				}
			}
			if(treeNode.grade == 1){
				if($("#specEquipment").val() == 1){
					editStr = editStr + "<a href=\"#\" onclick=\"openDialogView('添加通用设备', '${ctx}/ec/specEquipment/shopComEquipmentList?shopId="+treeNode.id+"','800px', '620px', 'officeContent')\" class=\"btn  btn-primary btn-xs\"><i class=\"fa fa-plus\"></i> 设备</a>";
				}
				if($("#arrangeOrdinary").val() == 1){
					editStr = editStr + "<a href=\"#\" onclick='top.openTab(\"${ctx }/train/arrange/ArrangeOrdinary?officeId="+treeNode.id+"&officeName="+treeNode.name+"\",\"店铺美容师排班\", false)' class=\"btn btn-success btn-xs\" >美容师排班</a>";
				}
			}
			editStr+='</span>';
			aObj.append(editStr);
		};
	</script>
	<script src="${ctxStatic}/common/wsize.min.js" type="text/javascript"></script>
</body>
</html>