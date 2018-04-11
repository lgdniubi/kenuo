<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/webpage/include/taglib.jsp"%>

<html>
<head>
<title>添加文章</title>
<meta name="decorator" content="default" />
<!-- 内容上传 引用-->
<script type="text/javascript">
	//根据条件查询文章    搜索按键
	function findArticles(){
		$("#articleId").html("");
		var cateid=$("#categoryId").val();
		var title = $("#title").val();
		
		if(cateid == ''){
			top.layer.alert('请先选择文章分类!', {icon: 0, title:'警告'});
			return;
		}
		$.ajax({
			 type:"get",
			 dataType:"json",
			 url:"${ctx}/ec/theme/treeArticlesData?categoryId="+cateid+"&title="+title+"&isOpen="+$("#isOpen").val(),
			 success:function(date){
				var data=date;
				if(data.length < 1){
					top.layer.alert('文章不存在!', {icon: 0, title:'警告'});
				}
				if(data.length>0){
					for(var i=0;i<data.length;i++){
						$("#articleId").append("<option value='"+data[i].id+"'>"+data[i].name+"</option>");
					}
				}
			 },
			 error:function(XMLHttpRequest,textStatus,errorThrown) {
			    
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
					<input id="isOpen" name="isOpen" value="${isOpen}" type="hidden">
					<table class="table table-bordered  table-condensed dataTables-example dataTable no-footer">
						<tr>
							<td>
								<label class="pull-right" ><font color="red">*</font>选择文章分类：</label>
							</td>
							<td>
								<sys:treeselect id="category" name="categoryId" value="" labelName="categoryName" labelValue="" title="商品分类" url="/ec/theme/treeArticlesCategoryData" cssClass="form-control" allowClear="true" notAllowSelectParent="true"/>
							</td>
						</tr>
						<tr>
							<td>
								<label class="pull-right" >文章关键词：</label>
							</td>
							<td>
								<input id="title" name="title" type="text" value="" class="input-sm" />
								<a href="#"  class="btn btn-primary btn-rounded btn-outline btn-sm pull-right" onclick="findArticles()" ><i class="fa fa-search"></i> 查询</a>
							</td>
						</tr>
						<tr>
							<td><label class="pull-right"><font color="red">*</font>选择文章：</label></td>
							<td>
								<select id="articleId" name="articleId" class="form-control" style="width:185px;">
								</select>
							</td>
						</tr>
					</table>
				</div>
			</div>
		</div>
	</div>
	
</body>
</html>