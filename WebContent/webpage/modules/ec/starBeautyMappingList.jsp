<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/webpage/include/taglib.jsp"%>

<html>
<head>
<title>明星技师列表</title>
<meta name="decorator" content="default" />
<!-- 内容上传 引用-->


<script type="text/javascript">
	var validateForm;
	
	function doSubmit() {//回调函数，在编辑和保存动作时，供openDialog调用提交表单。
		if (validateForm.form()) {
			
			$("#inputForm").submit();
			return true;
		}

		return false;
	}
	
	function  delStarBeautyMapping(mappingId,starId,isShow,franchiseeId){
		if(isShow == 1){
			top.layer.alert('数据启用中,不能删除!', {icon: 0, title:'提醒'});
			return;
		}else{
			if(confirm("确认要删除吗？","提示框")){
				newDelete(mappingId,starId,isShow,franchiseeId);			
			}
		}
	}
		
	function newDelete(mappingId,starId,isShow,franchiseeId){
		$.ajax({
			type:"post",
			url:"${ctx}/ec/starBeauty/delStarBeautyMapping?mappingId="+mappingId,
			success:function(date){
				if(date=="success"){
					top.layer.alert('删除成功!', {icon: 1, title:'提醒'});
					window.location="${ctx}/ec/starBeauty/starBeautyMappingList?starId="+starId+"&isShow="+isShow+"&franchiseeId="+franchiseeId;
				}
				if(date=="error"){
					top.layer.alert('删除失败!', {icon: 2, title:'提醒'});
					window.location="${ctx}/ec/starBeauty/starBeautyMappingList?starId="+starId+"&isShow="+isShow+"&franchiseeId="+franchiseeId;
				}
							
			},
			error:function(XMLHttpRequest,textStatus,errorThrown){
						    
			}
						 
		});
	}
	
	function addStarBeautyMapping(starId,isShow){
		var lock = true;
		var num = $("#num").val();
		var franchiseeId = $("#franchiseeId").val();//商家保护所选择的商家id
		if(num == 7){//明星技师只能有7个
			top.layer.alert('只能有7个明星技师!', {icon: 0, title:'提醒'}); 
			return;
		}else{
			top.layer.open({
			    type: 2, 
			    area: ['900px', '550px'],
			    title:"添加明星技师",
			    content: "${ctx}/ec/starBeauty/mappingform?starId="+starId+"&franchiseeId="+franchiseeId,
			    btn: ['确定', '关闭'],
			    yes: function(index, layero){
			    	if(lock){
				        var obj =  layero.find("iframe")[0].contentWindow;
				        
						var starId = obj.document.getElementById("starId");
						var userId = obj.document.getElementById("userId");
						var officeIds = obj.document.getElementById("officeIds");
						var starBeautyName = obj.document.getElementById("starBeautyName");
						var starBeautyPhoto = obj.document.getElementById("starBeautyPhoto");
						var sort = obj.document.getElementById("sort");
						var remarks = obj.document.getElementById("remarks");
				        
						if($(starBeautyName).val()== null || $(starBeautyName).val()==""){
							top.layer.alert('姓名不能为空!', {icon: 0, title:'提醒'}); 
							return;
						}
						if($(starBeautyPhoto).val()== null || $(starBeautyPhoto).val()==""){
							top.layer.alert('图片不能为空!', {icon: 0, title:'提醒'}); 
							return;
						}
						if($(sort).val()== null || $(sort).val()==""){
							top.layer.alert('排序不能为空!', {icon: 0, title:'提醒'}); 
							return;
						}
						
						//明星技师最多只能是7张,多了不能选择
						lock = false;//上锁
				        //异步添加商品
						$.ajax({
							type:"post",
							url:"${ctx}/ec/starBeauty/saveStarBeautyMapping",
							data:{
								starId:$(starId).val(),							
								userId:$(userId).val(),
								officeIds:$(officeIds).val(),
								starBeautyName:$(starBeautyName).val(),
								starBeautyPhoto:$(starBeautyPhoto).val(),
								sort:$(sort).val(),
								remarks:$(remarks).val()
							 },
							success:function(date){
								if(date=="success"){
									top.layer.alert('保存成功!', {icon: 1, title:'提醒'});
									window.location="${ctx}/ec/starBeauty/starBeautyMappingList?starId="+$(starId).val()+"&isShow="+isShow+"&franchiseeId="+franchiseeId;
								}
								if(date=="error"){
									top.layer.alert('保存失败!', {icon: 2, title:'提醒'});
									window.location="${ctx}/ec/starBeauty/starBeautyMappingList?starId="+$(starId).val()+"&isShow="+isShow+"&franchiseeId="+franchiseeId;
									lock = true;
								}
											
							},
							error:function(XMLHttpRequest,textStatus,errorThrown){
										    
							}
						});
					top.layer.close(index);
				    }
				},
				cancel: function(index){ //或者使用btn2
					    	           //按钮【按钮二】的回调
				}
			});
		}
	}
	
	//修改明星技师信息
	function editStarBeautyMapping(mappingId,isShow,franchiseeId){
		top.layer.open({
		    type: 2, 
		    area: ['900px', '550px'],
		    title:"修改明星技师",
		    content: "${ctx}/ec/starBeauty/starBeautyMappingform?mappingId="+mappingId,
		    btn: ['确定', '关闭'],
		    yes: function(index, layero){
		        var obj =  layero.find("iframe")[0].contentWindow;
		        
				var starId = obj.document.getElementById("starId");
				var userId = obj.document.getElementById("userId");
				var officeIds = obj.document.getElementById("officeIds");
				var starBeautyName = obj.document.getElementById("starBeautyName");
				var starBeautyPhoto = obj.document.getElementById("starBeautyPhoto");
				var sort = obj.document.getElementById("sort");
				var remarks = obj.document.getElementById("remarks");
		        
				if($(starBeautyName).val()== null || $(starBeautyName).val()==""){
					top.layer.alert('姓名不能为空!', {icon: 0, title:'提醒'}); 
					return;
				}
				if($(starBeautyPhoto).val()== null || $(starBeautyPhoto).val()==""){
					top.layer.alert('图片不能为空!', {icon: 0, title:'提醒'}); 
					return;
				}
				if($(sort).val()== null || $(sort).val()==""){
					top.layer.alert('排序不能为空!', {icon: 0, title:'提醒'}); 
					return;
				}
				
		        //异步添加商品
				$.ajax({
					type:"post",
					url:"${ctx}/ec/starBeauty/saveStarBeautyMapping",
					data:{
						mappingId:mappingId,
						starId:$(starId).val(),							
						userId:$(userId).val(),
						officeIds:$(officeIds).val(),
						starBeautyName:$(starBeautyName).val(),
						starBeautyPhoto:$(starBeautyPhoto).val(),
						sort:$(sort).val(),
						remarks:$(remarks).val()
					 },
					success:function(date){
						if(date=="success"){
							top.layer.alert('保存成功!', {icon: 1, title:'提醒'});
							window.location="${ctx}/ec/starBeauty/starBeautyMappingList?starId="+$(starId).val()+"&isShow="+isShow+"&franchiseeId="+franchiseeId;
						}
						if(date=="error"){
							top.layer.alert('保存失败!', {icon: 2, title:'提醒'});
							window.location="${ctx}/ec/starBeauty/starBeautyMappingList?starId="+$(starId).val()+"&isShow="+isShow+"&franchiseeId="+franchiseeId;
						}
									
					},
					error:function(XMLHttpRequest,textStatus,errorThrown){
								    
					}
				});
			top.layer.close(index);
			},
			cancel: function(index){ //或者使用btn2
				    	           //按钮【按钮二】的回调
			}
		}); 
	}
	
	$(function(){
		$(".imgUrl img").each(function(){
			var $this = $(this),
				$src = $this.attr('data-src');  
			$this.attr({'src':$src})
		});
	});
</script>
</head>
<body>
	<div class="wrapper wrapper-content">
		<div class="ibox">
			<div class="ibox-content">
				<div>
					<form id="searchForm" action="${ctx}/ec/starBeauty/starBeautyMappingList?starId=${starId}" method="post" class="form-inline">
						<!-- 翻页隐藏文本框 -->
						<input id="pageNo" name="pageNo" type="hidden" value="${page.pageNo}"/>
						<input id="pageSize" name="pageSize" type="hidden" value="${page.pageSize}"/>
						<input id="categoryIds" name="categoryIds" type="hidden" value="${categoryIds}"/>
					</form>
					<shiro:hasPermission name="ec:starBeauty:add">
						<a href="#" onclick="addStarBeautyMapping(${starId},${isShow})" class="btn btn-primary btn-xs" ><i class="fa fa-plus"></i>添加明星技师</a>
					</shiro:hasPermission>
				</div>
				<p></p>
				<input id="num" type="hidden" value="${num}"/>
				<input id="franchiseeId" type="hidden" value="${franchiseeId}"/>
				<table id="contentTable" class="table table-striped table-bordered  table-hover table-condensed  dataTables-example dataTable no-footer">
					<thead>
						<tr>
							<th style="text-align: center;">ID</th>
							<th style="text-align: center;">名称</th>
							<th style="text-align: center;">图片</th>
							<th style="text-align: center;">排序</th>
							<th style="text-align: center;">操作</th>
						</tr>
					</thead>
					<tbody style="text-align: center;">
						<c:forEach items="${page.list}" var="page">
							<tr>
								<td style="text-align: center;">${page.mappingId}</td>
								<td style="text-align: center;">
									${page.starBeautyName}
								</td>
								<td style="text-align: center;" class="imgUrl">
									<img alt="" src="${ctxStatic}/images/lazylode.png" data-src="${page.starBeautyPhoto}" style="width: 100px;height: 80px;">
								</td>
								<td style="text-align: center;">${page.sort}</td>
								<td style="text-align: center;">
									<shiro:hasPermission name="ec:starBeauty:view">
										<a href="#" onclick="openDialogView('查看', '${ctx}/ec/starBeauty/starBeautyMappingform?mappingId=${page.mappingId}','600px', '550px')" class="btn btn-info btn-xs"><i class="fa fa-search-plus"></i> 查看</a>
									</shiro:hasPermission>
									<shiro:hasPermission name="ec:starBeauty:edit">
										<a href="#" onclick="editStarBeautyMapping(${page.mappingId},${isShow},${franchiseeId})" class="btn btn-success btn-xs"><i class="fa fa-edit"></i>修改</a>
									</shiro:hasPermission>
									<shiro:hasPermission name="ec:starBeauty:del">
										<a href="#" onclick="delStarBeautyMapping(${page.mappingId},${starId},${isShow},${franchiseeId})" class="btn btn-danger btn-xs"><i class="fa fa-trash"></i>删除</a>
									</shiro:hasPermission>
								</td>
							</tr>
						</c:forEach>
					</tbody>
					<tfoot>
						<tr>
	                    	<td colspan="20">
	                        	<!-- 分页代码 --> 
	                            <table:page page="${page}"></table:page>
	                        </td>	
	                    </tr>
                   </tfoot>
				</table>
			</div>
		</div>
	</div>
	
</body>
</html>