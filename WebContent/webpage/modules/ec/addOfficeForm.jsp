<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/webpage/include/taglib.jsp"%>

<html>
<head>
<title>添加店铺</title>
<meta name="decorator" content="default" />
<!-- 内容上传 引用-->
<script type="text/javascript">
	$(document).ready(function(){
		if($("#mappingId").val() != 0){
			$("#choose").hide();
		}
		
		$("#officeButton").click(function(){
			
			// 是否限制选择，如果限制，设置为disabled
			if ($("#officeButton").hasClass("disabled")){
				return true;
			}
			
			// 正常打开	
			top.layer.open({
			    type: 2, 
			    area: ['300px', '420px'],
			    title:"选择所在店铺",
			    content: "${ctx}/tag/treeselect?url="+encodeURIComponent("/sys/office/treeData?type=2")+"&module=&checked=&extId=&isAll=" ,
			    	btn: ['确定', '关闭']
	    	       ,yes: function(index, layero){ //或者使用btn1
	    	    	   var tree = layero.find("iframe")[0].contentWindow.tree;//h.find("iframe").contents();
						var ids = [], names = [], nodes = [];
						if ("" == "true"){
							nodes = tree.getCheckedNodes(true);
						}else{
							nodes = tree.getSelectedNodes();
						}
						for(var i=0; i<nodes.length; i++) {//
							if (nodes[i].level == 0){
								//top.$.jBox.tip("不能选择根节点（"+nodes[i].name+"）请重新选择。");
								top.layer.msg("不能选择根节点（"+nodes[i].name+"）请重新选择。", {icon: 0});
								return false;
							}
							if (nodes[i].isParent){
								//top.$.jBox.tip("不能选择父节点（"+nodes[i].name+"）请重新选择。");
								//layer.msg('有表情地提示');
								top.layer.msg("不能选择父节点（"+nodes[i].name+"）请重新选择。", {icon: 0});
								return false;
							}
							if (nodes[i].grade == 2){
								//top.$.jBox.tip("不能选择父节点（"+nodes[i].name+"）请重新选择。");
								//layer.msg('有表情地提示');
								top.layer.msg("不能选择非店铺（"+nodes[i].name+"）请重新选择。", {icon: 0});
								return false;
							}
							ids.push(nodes[i].id);
							names.push(nodes[i].name);//
							break; // 如果为非复选框选择，则返回第一个选择  
						}
						
						$("#officeId").val(ids.join(",").replace(/u_/ig,""));
						$("#officeName").val(names.join(","));
						$("#officeName").focus();
						top.layer.close(index);
				   },
	    	cancel: function(index){ //或者使用btn2
	    	           //按钮【按钮二】的回调
	    	       }
			}); 
		});
	}); 
</script>
</head>
<body>
	<div class="wrapper wrapper-content">
		<div class="ibox">
			<div class="ibox-content">
				<div class="clearfix">
					<table class="table table-bordered  table-condensed dataTables-example dataTable no-footer">
						<tr id="choose">
							<td>
								<label class="pull-right" >选择店铺：</label>
							</td>
							<%-- <td>
								<sys:treeselect id="office" name="officeId" value="${officeRecommendMapping.officeId}" labelName="officeName" labelValue="${officeRecommendMapping.officeName}" title="店铺" url="/sys/office/treeData?type=2" cssClass=" form-control input-sm" allowClear="true" notAllowSelectRoot="false" notAllowSelectParent="true"/>
							</td> --%>
							
							<td>
							<input id="officeId" class=" form-control input-sm" name="officeId" value="" type="hidden">
								<div class="input-group">
									<input id="officeName" class=" form-control input-sm" name="officeName" readonly="readonly" value="${officeRecommendMapping.officeName}" data-msg-required="" style="" type="text">
										<span class="input-group-btn">
											<button id="officeButton" class="btn btn-sm btn-primary " type="button">
												<i class="fa fa-search"></i>
											</button>
										</span>
								</div>
								<label id="shopName-error" class="error" for="shopName" style="display:none"></label>
							</td>
					
						</tr>
						<tr>
							<td>
								<label class="pull-right" >排序：</label>
							</td>
							<td>
								<input class="form-control required" id="sort" name="sort" type="text" value="${officeRecommendMapping.sort }" style="width: 300px"/>
								<input id="mappingId" name="mappingId" type="hidden" value="${officeRecommendMapping.officeRecommendMappingId }"/>
							</td>
						</tr>
					</table>
				</div>
			</div>
		</div>
	</div>
	
</body>
</html>