<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/webpage/include/taglib.jsp"%>

<html>
<head>
<title>首页广告图对应商品列表</title>
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
	
	function  deleteGoods(goodsId,adId){
		if(confirm("确认要删除吗？","提示框")){
			newDelete(goodsId,adId);			
		}
	}
		
	function newDelete(goodsId,adId){
		$.ajax({
			type:"post",
			url:"${ctx}/ec/webAd/delGoods?goodsId="+goodsId+"&adId="+adId,
			success:function(date){
				if(date=="success"){
					top.layer.alert('删除成功!', {icon: 1, title:'提醒'});
					window.location="${ctx}/ec/webAd/mtmyWebAdGoodsList?mtmyWebAdId="+adId;
				}
				if(date=="error"){
					top.layer.alert('删除失败!', {icon: 2, title:'提醒'});
					window.location="${ctx}/ec/webAd/mtmyWebAdGoodsList?mtmyWebAdId="+adId;
				}
							
			},
			error:function(XMLHttpRequest,textStatus,errorThrown){
						    
			}
						 
	});
	}
	
	function addGoods(id,flag){
		top.layer.open({
		    type: 2, 
		    area: ['900px', '550px'],
		    title:"添加商品",
		    content: "${ctx}/ec/webAd/mtmyWebAdGoodsForm?adId="+id+"&flag="+flag,
		    btn: ['确定', '关闭'],
		    yes: function(index, layero){
		        var obj =  layero.find("iframe")[0].contentWindow;
				var select2 = obj.document.getElementById("select2");
		        var arr = new Array(); //数组定义标准形式，不要写成Array arr = new Array();
		        var all = new Array(); //定义变量全部保存
		        
		     /*     $(select2).each(function () {
		    	  	all = $(this).text(); //获取单个text
		    	  	arr = $(this).val(); //获取单个value
		    	 });  */
		       	for(i=0;i<select2.length;i++){
		        	arr.push(select2[i].value);
		        } 
				if(arr.length <= 0){
					top.layer.alert('商品不能为空!', {icon: 0, title:'提醒'}); 
					return;
				}
		        
		        //异步添加商品
				$.ajax({
					type:"post",
					url:"${ctx}/ec/webAd/saveMtmyWebAdGoods?goodsIds="+arr+"&adId="+id,
					success:function(date){
						if(date=="success"){
							top.layer.alert('保存成功!', {icon: 1, title:'提醒'});
							window.location="${ctx}/ec/webAd/mtmyWebAdGoodsList?mtmyWebAdId="+id;
						}
						if(date=="error"){
							top.layer.alert('保存失败!', {icon: 2, title:'提醒'});
							window.location="${ctx}/ec/webAd/mtmyWebAdGoodsList?mtmyWebAdId="+id;
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
				<form id="searchForm" action="${ctx}/ec/webAd/mtmyWebAdGoodsList" method="post" class="navbar-form navbar-left searcharea">
					<!-- 翻页隐藏文本框 -->
					<input id="pageNo" name="pageNo" type="hidden" value="${page.pageNo}"/>
					<input id="pageSize" name="pageSize" type="hidden" value="${page.pageSize}"/>
					<input id="mtmyWebAdId" type="hidden" name="mtmyWebAdId" value="${mtmyWebAd.mtmyWebAdId}">
				</form>
				</div>
				<div>
					<a href="#" onclick="addGoods(${mtmyWebAd.mtmyWebAdId},'add')" class="btn btn-primary btn-xs" ><i class="fa fa-plus"></i>添加商品</a>
				</div>
				<p></p>
				<table id="contentTable" class="table table-striped table-bordered  table-hover table-condensed  dataTables-example dataTable no-footer">
					<thead>
						<tr>
							<th style="text-align: center;">ID</th>
							<th style="text-align: center;">商品名称</th>
							<th style="text-align: center;">市场价</th>
							<th style="text-align: center;">商品图片</th>
							<th style="text-align: center;">操作</th>
						</tr>
					</thead>
					<tbody style="text-align: center;">
						<c:forEach items="${page.list}" var="page">
							<tr>
								<td style="text-align: center;">${page.goodsId}</td>
								<td style="text-align: center;">${page.goodsName}</td>
								<td style="text-align: center;">${page.marketPrice}</td>
								<td style="text-align: center;" class="imgUrl" ><img alt="" src="${ctxStatic}/images/lazylode.png"  data-src="${page.originalImg}" style="width: 150px;height: 100px;border:1px solid black; "></td>
								<td style="text-align: center;">
									<shiro:hasPermission name="ec:webAd:deleteGoods">
										<a href="#" onclick="deleteGoods(${page.goodsId},${mtmyWebAd.mtmyWebAdId})" class="btn btn-danger btn-xs"><i class="fa fa-trash"></i>删除</a>
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