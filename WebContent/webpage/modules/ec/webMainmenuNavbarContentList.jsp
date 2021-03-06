<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/webpage/include/taglib.jsp"%>

<html>
<head>
<title>主菜单导航栏内容图列表</title>
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
	
	function  delWebMainmenuNavbarContent(id,mainmenuId,type){
		if(confirm("确认要删除吗？","提示框")){
			newDelete(id,mainmenuId,type);			
		}
	}
		
	function newDelete(id,mainmenuId,type){
		$.ajax({
			type:"post",
			url:"${ctx}/ec/webMainmenuNavbarContent/del?webMainmenuNavbarContentId="+id,
			success:function(date){
				if(date=="success"){
					top.layer.alert('删除成功!', {icon: 1, title:'提醒'});
					window.location="${ctx}/ec/webMainmenuNavbarContent/list?webMainmenuNavbarId=${mainmenuId}&type="+type;
				}
				if(date=="error"){
					top.layer.alert('删除失败!', {icon: 2, title:'提醒'});
					window.location="${ctx}/ec/webMainmenuNavbarContent/list?webMainmenuNavbarId=${mainmenuId}&type="+type;
				}
							
			},
			error:function(XMLHttpRequest,textStatus,errorThrown){
						    
			}
						 
	});
	}
	
	function addwebMainmenuNavbarContent(id,mainmenuId,flag,type){
		var categoryIds = $("#categoryIds").val();
		categoryIds = "," + categoryIds + ",";
		top.layer.open({
		    type: 2, 
		    area: ['900px', '550px'],
		    title:"添加主菜单导航栏内容图",
		    content: "${ctx}/ec/webMainmenuNavbarContent/form?webMainmenuNavbarContentId="+id+"&mainmenuId="+mainmenuId+"&flag="+flag+"&type="+type,
		    btn: ['确定', '关闭'],
		    yes: function(index, layero){
		        var obj =  layero.find("iframe")[0].contentWindow;
				var name = obj.document.getElementById("name");
				var imgUrl = obj.document.getElementById("imgUrl");
				var redirectUrl = obj.document.getElementById("redirectUrl");
				var sort = obj.document.getElementById("sort");
				var categoryId = obj.document.getElementById("categoryId");
		        var arr = new Array(); //数组定义标准形式，不要写成Array arr = new Array();
		        var all = new Array(); //定义变量全部保存
		        
				if($(name).val()== null || $(name).val()==""){
					top.layer.alert('名称不能为空!', {icon: 0, title:'提醒'}); 
					return;
				}
				if($(imgUrl).val()== null || $(imgUrl).val()==""){
					top.layer.alert('图片不能为空!', {icon: 0, title:'提醒'}); 
					return;
				}
				if($(redirectUrl).val()== null || $(redirectUrl).val()==""){
					top.layer.alert('链接地址不能为空!', {icon: 0, title:'提醒'}); 
					return;
				}
				if(!/^[1-9]*[1-9][0-9]*$/.test($(sort).val())){
					top.layer.alert('排序必须为正整数!', {icon: 0, title:'提醒'});
					return;
				}
		        if(type == 3){
		        	if(flag == 'add'){
		        		if(obj.document.getElementById("kind").value == 2){
		        			
		        			if($(categoryId).val()== null || $(categoryId).val()=="" || $(categoryId).val() == 0){
								top.layer.alert('分类id不能为空!', {icon: 0, title:'提醒'}); 
								return;
							}
		        			
		        			if(categoryIds.length > 0){
			        			if(categoryIds.indexOf(","+categoryId.value+",") >= 0){
			        				top.layer.alert('该分类已添加，请重新选择!', {icon: 0, title:'提醒'}); 
									return;
			        			}
			        		}
		        			
		        		}
		        	}
		        }
				
		        //异步添加商品
				$.ajax({
					type:"post",
					url:"${ctx}/ec/webMainmenuNavbarContent/save",
					data:{
						webMainmenuNavbarContentId:id,							
						mainmenuId:mainmenuId,
						name:$(name).val(),
						imgUrl:$(imgUrl).val(),
						redirectUrl:$(redirectUrl).val(),
						sort:$(sort).val(),
						categoryId:$(categoryId).val()
					 },
					success:function(date){
						if(date=="success"){
							top.layer.alert('保存成功!', {icon: 1, title:'提醒'});
							window.location="${ctx}/ec/webMainmenuNavbarContent/list?webMainmenuNavbarId=${mainmenuId}&type="+type;
						}
						if(date=="error"){
							top.layer.alert('保存失败!', {icon: 2, title:'提醒'});
							window.location="${ctx}/ec/webMainmenuNavbarContent/list?webMainmenuNavbarId=${mainmenuId}&type="+type;
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
					<form id="searchForm" action="${ctx}/ec/webMainmenuNavbarContent/list?webMainmenuNavbarId=${mainmenuId}" method="post" class="form-inline">
						<!-- 翻页隐藏文本框 -->
						<input id="pageNo" name="pageNo" type="hidden" value="${page.pageNo}"/>
						<input id="pageSize" name="pageSize" type="hidden" value="${page.pageSize}"/>
						<input id="categoryIds" name="categoryIds" type="hidden" value="${categoryIds}"/>
					</form>
					<shiro:hasPermission name="ec:webMainmenuNavbarContent:add">
						<a href="#" onclick="addwebMainmenuNavbarContent(0,${mainmenuId},'add',${type})" class="btn btn-primary btn-xs" ><i class="fa fa-plus"></i>添加主菜单导航栏内容图</a>
					</shiro:hasPermission>
				</div>
				<p></p>
				<table id="contentTable" class="table table-striped table-bordered  table-hover table-condensed  dataTables-example dataTable no-footer">
					<thead>
						<tr>
							<th style="text-align: center;">ID</th>
							<th style="text-align: center;">名称</th>
							<th style="text-align: center;">图片</th>
							<th style="text-align: center;">排序</th>
							<th style="text-align: center;">创建时间</th>
							<th style="text-align: center;">操作</th>
						</tr>
					</thead>
					<tbody style="text-align: center;">
						<c:forEach items="${page.list}" var="page">
							<tr>
								<td style="text-align: center;">${page.webMainmenuNavbarContentId}</td>
								<td style="text-align: center;">
									${page.name}
								</td>
								<td style="text-align: center;" class="imgUrl">
									<img alt="" src="${ctxStatic}/images/lazylode.png" data-src="${page.imgUrl}" style="width: 100px;height: 80px;">
								</td>
								<td style="text-align: center;">${page.sort}</td>
								<td style="text-align: center;">
									<fmt:formatDate value="${page.createDate}"  pattern="yyyy-MM-dd HH:mm:ss" />
								</td>
								<td style="text-align: center;">
									<shiro:hasPermission name="ec:webMainmenuNavbarContent:view">
										<a href="#" onclick="openDialogView('查看', '${ctx}/ec/webMainmenuNavbarContent/form?webMainmenuNavbarContentId=${page.webMainmenuNavbarContentId}&type=${type}','600px', '550px')" class="btn btn-info btn-xs"><i class="fa fa-search-plus"></i> 查看</a>
									</shiro:hasPermission>
									<shiro:hasPermission name="ec:webMainmenuNavbarContent:edit">
										<a href="#" onclick="addwebMainmenuNavbarContent(${page.webMainmenuNavbarContentId},${page.mainmenuId},'update',${type})" class="btn btn-success btn-xs"><i class="fa fa-edit"></i>修改</a>
									</shiro:hasPermission>
									<shiro:hasPermission name="ec:webMainmenuNavbarContent:del">
										<a href="#" onclick="delWebMainmenuNavbarContent(${page.webMainmenuNavbarContentId},${page.mainmenuId},${type})" class="btn btn-danger btn-xs"><i class="fa fa-trash"></i>删除</a>
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