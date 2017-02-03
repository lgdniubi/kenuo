<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/webpage/include/taglib.jsp"%>

<html>
<head>
<title>创建tab_banner图列表</title>
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
	
	function  delTabBanner(id){
		if(confirm("确认要删除吗？","提示框")){
			newDelete(id);			
		}
	}
		
	function newDelete(id){
		$.ajax({
			type:"post",
			url:"${ctx}/ec/tab_banner/delTabBanner?tabBannerId="+id,
			success:function(date){
				if(date=="success"){
					top.layer.alert('删除成功!', {icon: 1, title:'提醒'});
					window.location="${ctx}/ec/tab_banner/newBannerList?id=${tabBanner.tabId}";
				}
				if(date=="error"){
					top.layer.alert('删除失败!', {icon: 2, title:'提醒'});
					window.location="${ctx}/ec/tab_banner/newBannerList?id=${tabBanner.tabId}";
				}
							
			},
			error:function(XMLHttpRequest,textStatus,errorThrown){
						    
			}
						 
	});
	}
	
		
	
	
	
	function addTabBanner(id,flag){
		top.layer.open({
		    type: 2, 
		    area: ['600px', '550px'],
		    title:"编辑tab_banner图",
		    content: "${ctx}/ec/tab_banner/tabBannerForm?id="+id+"&flag="+flag,
		    btn: ['确定', '关闭'],
		    yes: function(index, layero){
		        var obj =  layero.find("iframe")[0].contentWindow;
				var tabName = obj.document.getElementById("tabName");
		    	var tabIcon = obj.document.getElementById("tabIcon");
				var tabSelectIcon = obj.document.getElementById("tabSelectIcon");
				var newSort = obj.document.getElementById("newSort");
		        var arr = new Array(); //数组定义标准形式，不要写成Array arr = new Array();
		        var all = new Array(); //定义变量全部保存
				
		        if($(tabName).val()== null || $(tabName).val()==""){
					top.layer.alert('内容名称不能为空!', {icon: 0, title:'提醒'}); 
					return;
				}
		        if($(tabIcon).val() == null || $(tabIcon).val() == ""){
					top.layer.alert('图片不可为空！', {icon: 0, title:'提醒'});
					return;
				}
				if($(tabSelectIcon).val() == null || $(tabSelectIcon).val() == ""){
					top.layer.alert('图片不可为空！', {icon: 0, title:'提醒'});
					return;
				}
				if($(newSort).val()== null || $(newSort).val()==""){
					top.layer.alert('排序不能为空!', {icon: 0, title:'提醒'}); 
					return;
				}
				
		        //异步添加或者更新tab_banner
				$.ajax({
				type:"post",
				data:{
					tabName:$(tabName).val(),
					tabIcon:$(tabIcon).val(),
					tabSelectIcon:$(tabSelectIcon).val(),
					newSort:$(newSort).val()
				 },
				url:"${ctx}/ec/tab_banner/saveTabBanner?id="+id+"&flag="+flag,
				success:function(date){
					if(date=="success"){
						top.layer.alert('保存成功!', {icon: 1, title:'提醒'});
						window.location="${ctx}/ec/tab_banner/newBannerList?id=${tabBanner.tabId}";
					}
					if(date=="error"){
						top.layer.alert('保存失败!', {icon: 2, title:'提醒'});
						window.location="${ctx}/ec/tab_banner/newBannerList?id=${tabBanner.tabId}";
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
				<div class="clearfix">
				</div>
				<div>
					<a href="#" onclick="addTabBanner(${tabBanner.tabId},'add')" class="btn btn-primary btn-xs" ><i class="fa fa-plus"></i>添加tab_banner图</a>
				</div>
				<p></p>
				<table id="contentTable" class="table table-striped table-bordered  table-hover table-condensed  dataTables-example dataTable no-footer">
					<thead>
						<tr>
							<th style="text-align: center;">ID</th>
							<th style="text-align: center;">内容名称</th>
							<th style="text-align: center;">未选中样式图标</th>
							<th style="text-align: center;">选中样式图标</th>
							<th style="text-align: center;">排序</th>
							<th style="text-align: center;">操作</th>
						</tr>
					</thead>
					<tbody style="text-align: center;">
						<c:forEach items="${page.list}" var="page">
							<tr>
								<td style="text-align: center;">${page.tabBannerId}</td>
								<td style="text-align: center;">${page.tabName}</td>
								<td style="text-align: center;" class="imgUrl"><img alt="" src="${ctxStatic}/images/lazylode.png" data-src="${page.tabIcon}" style="width: 51px;height: 38px;"></td>
								<td style="text-align: center;" class="imgUrl"><img alt="" src="${ctxStatic}/images/lazylode.png" data-src="${page.tabSelectIcon}" style="width: 51px;height: 38px;"></td>
								<td style="text-align: center;">${page.newSort}</td>
								<td style="text-align: center;">
									<shiro:hasPermission name="ec:tab_banner:view">
										<a href="#" onclick="openDialogView('查看', '${ctx}/ec/tab_banner/newTabBannerForm?tabBannerId=${page.tabBannerId}','600px', '550px')" class="btn btn-info btn-xs"><i class="fa fa-search-plus"></i> 查看</a>
									</shiro:hasPermission>
									<a href="#" onclick="addTabBanner(${page.tabBannerId},'update')" class="btn btn-success btn-xs"><i class="fa fa-edit"></i>修改</a>
									<shiro:hasPermission name="ec:tab_banner:del">
										<a href="#" onclick="delTabBanner(${page.tabBannerId})" class="btn btn-danger btn-xs"><i class="fa fa-trash"></i>删除</a>
									</shiro:hasPermission> 
								</td>
							</tr>
						</c:forEach>
					</tbody>
				</table>
			</div>
		</div>
	</div>
	
</body>
</html>