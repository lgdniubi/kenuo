<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/webpage/include/taglib.jsp"%>
<%@ include file="/webpage/include/icheck.jsp"%>
<html>
<head>
<title>热门主题对应商品列表</title>
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
					url:"${ctx}/ec/theme/delGoods?ids="+ids,
					success:function(date){
						if(date=="success"){
							top.layer.alert('删除成功!', {icon: 1, title:'提醒'});
							window.location="${ctx}/ec/theme/themeGoodsList?themeId="+themeId+"&isOpen="+$("#isOpen").val();
						}
						if(date=="error"){
							top.layer.alert('删除失败!', {icon: 2, title:'提醒'});
							window.location="${ctx}/ec/theme/themeGoodsList?themeId="+themeId+"&isOpen="+$("#isOpen").val();
						}
									
					},
					error:function(XMLHttpRequest,textStatus,errorThrown){
								    
					}
								 
			});
				
	   		 top.layer.close(index);
		});
		}
	}
	
	function deleteGoods(themeMappingId,themeId){
		if(confirm("确认要删除吗？","提示框")){
			newDelete(themeMappingId,themeId);			
		}
	}
		
	function newDelete(themeMappingId,themeId){
		$.ajax({
			type:"post",
			url:"${ctx}/ec/theme/delGoods?ids="+themeMappingId,
			success:function(date){
				if(date=="success"){
					top.layer.alert('删除成功!', {icon: 1, title:'提醒'});
					window.location="${ctx}/ec/theme/themeGoodsList?themeId="+themeId+"&isOpen="+$("#isOpen").val();
				}
				if(date=="error"){
					top.layer.alert('删除失败!', {icon: 2, title:'提醒'});
					window.location="${ctx}/ec/theme/themeGoodsList?themeId="+themeId+"&isOpen="+$("#isOpen").val();
				}
							
			},
			error:function(XMLHttpRequest,textStatus,errorThrown){
						    
			}
						 
	});
	}
	
	function addGoods(id,isOpen){
		top.layer.open({
		    type: 2, 
		    area: ['900px', '550px'],
		    title:"添加商品",
		    content: "${ctx}/ec/theme/themeGoodsForm?themeId="+id+"&isOpen="+isOpen,
		    btn: ['确定', '关闭'],
		    yes: function(index, layero){
		        var obj =  layero.find("iframe")[0].contentWindow;
				var select2 = obj.document.getElementById("select2");
		        var arr = new Array(); //数组定义标准形式，不要写成Array arr = new Array();
		        var all = new Array(); //定义变量全部保存
		        
		       	for(i=0;i<select2.length;i++){
		        	arr.push(select2[i].value);
		        } 
		        //异步添加商品
				$.ajax({
					type:"post",
					url:"${ctx}/ec/theme/saveThemeSomeThing?someIds="+arr+"&themeId="+id+"&type=1",
					success:function(data){
						if(data=="success"){
							top.layer.alert('保存成功!', {icon: 1, title:'提醒'});
							window.location="${ctx}/ec/theme/themeGoodsList?themeId="+id+"&isOpen="+$("#isOpen").val();	
						}
						if(data=="error"){
							top.layer.alert('保存失败!', {icon: 2, title:'提醒'});
							window.location="${ctx}/ec/theme/themeGoodsList?themeId="+id+"&isOpen="+$("#isOpen").val();
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
	
	function newReset(){
		window.location = "${ctx}/ec/theme/themeGoodsList?themeId="+${themeMapping.themeId}+"&isOpen="+$("#isOpen").val();
	}
</script>
</head>
<body>
	<div class="wrapper wrapper-content">
		<div class="ibox">
			<div class="ibox-content">
				<div class="clearfix">
					<form id="searchForm" action="${ctx}/ec/theme/themeGoodsList?themeId=${themeMapping.themeId}" method="post" class="form-inline">
						<!-- 翻页隐藏文本框 -->
						<input id="pageNo" name="pageNo" type="hidden" value="${page.pageNo}"/>
						<input id="pageSize" name="pageSize" type="hidden" value="${page.pageSize}"/>
						<input id="isOpen" name="isOpen" type="hidden" value="${isOpen}"/>
							<table>
								<tr>
									<td>商品ID：</td>
									<td><input id="newGoodsId" name="newGoodsId" value="${themeMapping.newGoodsId}" class="form-control input-sm"/></td>
									<td>商品名称：</td>
									<td><input id="newGoodsName" name="newGoodsName" value="${themeMapping.newGoodsName}" class="form-control input-sm" /></td>
								</tr>
								<tr>
									<td>商品分类：</td><td><sys:treeselect id="newGoodsCategoryId" name="newGoodsCategoryId" value="${themeMapping.newGoodsCategoryId}" labelName="newGoodsCategoryName" labelValue="${themeMapping.newGoodsCategoryName}" title="商品分类" url="/ec/goodscategory/treeData" cssClass="form-control required" allowClear="true" notAllowSelectParent="true"/></td>
								</tr>
							</table>
					</form>
					<!-- 工具栏 -->
					<div class="row" style="padding-top: 10px;">
						<div class="col-sm-12">
							<div class="pull-left">
									<a href="#" onclick="addGoods(${themeMapping.themeId},'${isOpen}')" class="btn btn-primary btn-xs" ><i class="fa fa-plus"></i>添加商品</a>
									<shiro:hasPermission name="ec:theme:deleteAllGoods">
										<!-- 删除按钮 -->
										<button class="btn btn-white btn-sm" onclick="deleteAll(${themeMapping.themeId})" data-toggle="tooltip" data-placement="top"><i class="fa fa-trash-o"> ${label==null?'删除':label}</i></button>
									</shiro:hasPermission>
							</div>
							<div class="pull-right">
								<button  class="btn btn-primary btn-rounded btn-outline btn-sm " onclick="search()" ><i class="fa fa-search"></i> 查询</button>
								<button  class="btn btn-primary btn-rounded btn-outline btn-sm " onclick="newReset()" ><i class="fa fa-refresh"></i> 重置</button>
							</div>
						</div>
					</div>
				<table id="contentTable" class="table table-striped table-bordered  table-hover table-condensed  dataTables-example dataTable no-footer">
					<thead>
						<tr>
							
							<th style="text-align: center;"><input type="checkbox" class="i-checks" ></th>
							<th style="text-align: center;">序号</th>
							<th style="text-align: center;">商品ID</th>
							<th style="text-align: center;">商品名称</th>
							<th style="text-align: center;">操作</th>
						</tr>
					</thead>
					<tbody style="text-align: center;">
						<c:forEach items="${page.list}" var="page">
							<tr>
								<td ><input type="checkbox" id="${page.themeMappingId}" class="i-checks" name="box"></td>
								<td style="text-align: center;">${page.themeMappingId}</td>
								<td style="text-align: center;">${page.goods.goodsId}</td>
								<td style="text-align: center;">${page.goods.goodsName}</td>
								<td style="text-align: center;">
									<shiro:hasPermission name="ec:theme:deleteGoods">
										<a href="#" onclick="deleteGoods(${page.themeMappingId},${page.themeId})" class="btn btn-danger btn-xs"><i class="fa fa-trash"></i>删除</a>
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