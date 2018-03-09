<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/webpage/include/taglib.jsp"%>
<%@ include file="/webpage/include/icheck.jsp"%>
<html>
<head>
<title>热门主题对应文章列表</title>
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
	
	function deleteAll(themeId){
		var str=document.getElementsByName("box");
		var objarray=str.length;
		var chestr="";     var ids="";
		for (i=0;i<objarray;i++){
		    if(str[i].checked == true){
		   		chestr+=str[i].id+",";
		    }
		}
		if(chestr.substr(chestr.length-1)== ','){
		    ids = chestr.substr(0,chestr.length-1);
		}
		if(ids == ""){
			top.layer.alert('请至少选择一条数据!', {icon: 0, title:'警告'});
		}else{
			top.layer.confirm('确认要彻底删除数据吗?', {icon: 3, title:'系统提示'}, function(index){
				
				$.ajax({
					type:"post",
					url:"${ctx}/ec/theme/deleteArticles?ids="+ids+"&themeId="+themeId,
					success:function(date){
						if(date=="success"){
							top.layer.alert('删除成功!', {icon: 1, title:'提醒'});
							window.location="${ctx}/ec/theme/themeArticlesList?themeId="+themeId;
						}
						if(date=="error"){
							top.layer.alert('删除失败!', {icon: 2, title:'提醒'});
							window.location="${ctx}/ec/theme/themeArticlesList?themeId="+themeId;
						}
									
					},
					error:function(XMLHttpRequest,textStatus,errorThrown){
								    
					}
								 
			});
				
	   		 top.layer.close(index);
		});
		}
	}
	
	function deleteArticles(ids,themeId){
		if(confirm("确认要删除吗？","提示框")){
			newDelete(ids,themeId);			
		}
	}
		
	function newDelete(ids,themeId){
		$.ajax({
			type:"post",
			url:"${ctx}/ec/theme/deleteArticles?ids="+ids+"&themeId="+themeId,
			success:function(date){
				if(date=="success"){
					top.layer.alert('删除成功!', {icon: 1, title:'提醒'});
					window.location="${ctx}/ec/theme/themeArticlesList?themeId="+themeId;
				}
				if(date=="error"){
					top.layer.alert('删除失败!', {icon: 2, title:'提醒'});
					window.location="${ctx}/ec/theme/themeArticlesList?themeId="+themeId;
				}
							
			},
			error:function(XMLHttpRequest,textStatus,errorThrown){
						    
			}
						 
	});
	}
	
	function addArticles(id,isOpen){
		var articlesIds = $("#articlesIds").val();
		articlesIds = "," + articlesIds + ",";
		top.layer.open({
		    type: 2, 
		    area: ['600px', '500px'],
		    title:"添加文章",
		    content: "${ctx}/ec/theme/themeArticlesForm?themeId="+id+"&isOpen="+isOpen,
		    btn: ['确定', '关闭'],
		    yes: function(index, layero){
		        var obj =  layero.find("iframe")[0].contentWindow;
				var articleId = obj.document.getElementById("articleId");
				
		        if(articleId.value == ''){
		        	top.layer.alert('请选择文章！', {icon: 0, title:'提醒'});
					return;
		        }
		        if(articlesIds.length > 0){
		        	if(articlesIds.indexOf(","+articleId.value+",") >= 0){
		        		top.layer.alert('该文章已添加,请重新选择！', {icon: 0, title:'提醒'});
						return;
		        	}
		        }
		        
		        //异步添加文章
				$.ajax({
					type:"post",
					url:"${ctx}/ec/theme/saveThemeSomeThing?someIds="+articleId.value+"&themeId="+id+"&type=0",
					success:function(data){
						if(data=="success"){
							top.layer.alert('保存成功!', {icon: 1, title:'提醒'});
							window.location="${ctx}/ec/theme/themeArticlesList?themeId="+id;	
						}
						if(data=="error"){
							top.layer.alert('保存失败!', {icon: 2, title:'提醒'});
							window.location="${ctx}/ec/theme/themeArticlesList?themeId="+id;
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
	
	$(document).ready(function() {
	    $('#contentTable thead tr th input.i-checks').on('ifChecked', function(event){ //ifCreated 事件应该在插件初始化之前绑定 
	    	 $('#contentTable tbody tr td input.i-checks').iCheck('check');
	    });
	    $('#contentTable thead tr th input.i-checks').on('ifUnchecked', function(event){ //ifCreated 事件应该在插件初始化之前绑定 
	    	 $('#contentTable tbody tr td input.i-checks').iCheck('uncheck');
	    });
	});
	
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
							<div class="pull-left">
									<a href="#" onclick="addArticles(${themeMapping.themeId},'${isOpen}')" class="btn btn-primary btn-xs" ><i class="fa fa-plus"></i>添加文章</a>
									<shiro:hasPermission name="ec:theme:deleteAllArticles">
										<!-- 删除按钮 -->
										<button class="btn btn-white btn-sm" onclick="deleteAll(${themeMapping.themeId})" data-toggle="tooltip" data-placement="top"><i class="fa fa-trash-o"> ${label==null?'删除':label}</i></button>
									</shiro:hasPermission>
							</div>
						</div>
					</div>
				<table id="contentTable" class="table table-striped table-bordered  table-hover table-condensed  dataTables-example dataTable no-footer">
					<thead>
						<tr>
							
							<th style="text-align: center;"><input type="checkbox" class="i-checks" ></th>
							<th style="text-align: center;">文章ID</th>
							<th style="text-align: center;">文章名称</th>
							<th style="text-align: center;">操作</th>
						</tr>
					</thead>
					<tbody style="text-align: center;">
						<c:forEach items="${page.list}" var="page">
							<tr>
								<td ><input type="checkbox" id="${page.articleRepository.articleId}" class="i-checks" name="box"></td>
								<td style="text-align: center;">${page.articleRepository.articleId}</td>
								<td style="text-align: center;">${page.articleRepository.title}</td>
								<td style="text-align: center;">
									<shiro:hasPermission name="ec:theme:deleteArticles">
										<a href="#" onclick="deleteArticles(${page.articleRepository.articleId},${themeMapping.themeId})" class="btn btn-danger btn-xs"><i class="fa fa-trash"></i>删除</a>
									</shiro:hasPermission> 
								</td>
							</tr>
						</c:forEach>
					</tbody>
				</table>
			</div>
			</div>
		</div>
	</div>
	
</body>
</html>