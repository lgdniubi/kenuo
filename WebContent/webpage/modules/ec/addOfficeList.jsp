<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/webpage/include/taglib.jsp"%>
<%@ include file="/webpage/include/icheck.jsp"%>
<html>
<head>
<title>店铺推荐组添加店铺列表</title>
<meta name="decorator" content="default" />
<meta name="viewport" content="width=device-width, initial-scale=1.0">
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
	
	function delOffice(officeRecommendMappingId,officeRecommendId,franchiseeId){
		if(confirm("确认要删除吗？","提示框")){
			newDelete(officeRecommendMappingId,officeRecommendId,franchiseeId);			
		}
	}
		
	function newDelete(officeRecommendMappingId,officeRecommendId,franchiseeId){
		$.ajax({
			type:"post",
			url:"${ctx}/ec/officeRecommend/delOffice?officeRecommendMappingId="+officeRecommendMappingId,
			success:function(date){
				if(date=="success"){
					top.layer.alert('删除成功!', {icon: 1, title:'提醒'});
					window.location="${ctx}/ec/officeRecommend/addOffice?officeRecommendId="+officeRecommendId+"&franchiseeId="+franchiseeId;
				}
				if(date=="error"){
					top.layer.alert('删除失败!', {icon: 2, title:'提醒'});
					window.location="${ctx}/ec/officeRecommend/addOffice?officeRecommendId="+officeRecommendId+"&franchiseeId="+franchiseeId;
				}
							
			},
			error:function(XMLHttpRequest,textStatus,errorThrown){
						    
			}
						 
		});
	}
	
	function editOffice(id,mappingId,flag,franchiseeId){
		var oldOfficeIds = $("#oldOfficeIds").val();
		top.layer.open({
		    type: 2, 
		    area: ['500px', '300px'],
		    title:"添加店铺",
		    content: "${ctx}/ec/officeRecommend/addOfficeForm?officeRecommendMappingId="+mappingId+"&franchiseeId="+franchiseeId,
		    btn: ['确定', '关闭'],
		    yes: function(index, layero){
		        var obj =  layero.find("iframe")[0].contentWindow;
				var officeId = obj.document.getElementById("officeId");
				var sort = obj.document.getElementById("sort");
				
		        if(officeId.value == ''){
		        	top.layer.alert('请选择店铺！', {icon: 0, title:'提醒'});
					return;
		        }
		        
		        if(sort.value == ''){
		        	top.layer.alert('请填写排序值！', {icon: 0, title:'提醒'});
					return;
		        }
		        
		        if(flag == 'add'){
			        if(oldOfficeIds.length > 0){
			        	if(oldOfficeIds.indexOf(officeId.value) >= 0){
			        		top.layer.alert('该店铺已添加，请重新选择！', {icon: 0, title:'提醒'});
     	     	    		return;
			        	}
			        }
		        }
		        
				//异步添加店铺
				$.ajax({
					type:"post",
					url:"${ctx}/ec/officeRecommend/saveOffice?officeId="+officeId.value+"&recommendId="+id+"&sort="+sort.value+"&flag="+flag+"&officeRecommendMappingId="+mappingId,
					success:function(data){
						if(data=="success"){
							top.layer.alert('保存成功!', {icon: 1, title:'提醒'});
							window.location="${ctx}/ec/officeRecommend/addOffice?officeRecommendId="+id+"&franchiseeId="+franchiseeId;	
						}
						if(data=="error"){
							top.layer.alert('保存失败!', {icon: 2, title:'提醒'});
							window.location="${ctx}/ec/officeRecommend/addOffice?officeRecommendId="+id+"&franchiseeId="+franchiseeId;
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
</script>
</head>
<body>
	<div class="wrapper wrapper-content">
		<div class="ibox">
			<div class="ibox-content">
				<div class="clearfix">
					<!-- 工具栏 -->
					<div class="row" style="padding-top: 10px;">
						<div class="col-sm-12">
							<form id="searchForm" action="${ctx}/ec/officeRecommend/addOffice?officeRecommendId=${officeRecommendMapping.recommendId}" method="post" class="form-inline">
								<!-- 翻页隐藏文本框 -->
								<input id="pageNo" name="pageNo" type="hidden" value="${page.pageNo}"/>
								<input id="pageSize" name="pageSize" type="hidden" value="${page.pageSize}"/>
								<input id="oldOfficeIds" name="oldOfficeIds" type="hidden" value="${oldOfficeIds}"/>
							</form>
							<div class="pull-left">
								<a href="#" onclick="editOffice(${officeRecommendMapping.recommendId},0,'add','${franchiseeId}')" class="btn btn-primary btn-xs" ><i class="fa fa-plus"></i>添加店铺</a>
							</div>
						</div>
					</div>
				<table id="contentTable" class="table table-striped table-bordered  table-hover table-condensed  dataTables-example dataTable no-footer">
					<thead>
						<tr>
							<th style="text-align: center;">序号</th>
							<th style="text-align: center;">店铺ID</th>
							<th style="text-align: center;">店铺名称</th>
							<th style="text-align: center;">排序</th>
							<th style="text-align: center;">操作</th>
						</tr>
					</thead>
					<tbody style="text-align: center;">
						<c:forEach items="${page.list}" var="page">
							<tr>
								<td style="text-align: center;">${page.officeRecommendMappingId}</td>
								<td style="text-align: center;">${page.officeId}</td>
								<td style="text-align: center;">${page.officeName}</td>
								<td style="text-align: center;">${page.sort}</td>
								<td style="text-align: center;">
									<shiro:hasPermission name="ec:officeRecommend:editOffice">
										<a href="#" onclick="editOffice(${page.recommendId},${page.officeRecommendMappingId},'edit','${franchiseeId}')" class="btn btn-success btn-xs"><i class="fa fa-edit"></i>修改</a>
									</shiro:hasPermission> 
									<shiro:hasPermission name="ec:officeRecommend:delOffice">
										<a href="#" onclick="delOffice(${page.officeRecommendMappingId},${page.recommendId},'${franchiseeId}')" class="btn btn-danger btn-xs"><i class="fa fa-trash"></i>删除</a>
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
	</div>
	
</body>
</html>