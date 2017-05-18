<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/webpage/include/taglib.jsp"%>

<html>
<head>
<title>创建供应商联系人列表</title>
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
	
	function  delGoodsSupplierContacts(id,goodsSupplierId){
		if(confirm("确认要删除吗？","提示框")){
			newDelete(id,goodsSupplierId);			
		}
	}
		
	function newDelete(id,goodsSupplierId){
		$.ajax({
			type:"post",
			url:"${ctx}/ec/goodsSupplierContacts/del?goodsSupplierContactsId="+id,
			success:function(date){
				if(date=="success"){
					top.layer.alert('删除成功!', {icon: 1, title:'提醒'});
					window.location="${ctx}/ec/goodsSupplierContacts/newgoodsSupplierContactsList?goodsSupplierId="+goodsSupplierId;
				}
				if(date=="error"){
					top.layer.alert('删除失败!', {icon: 2, title:'提醒'});
					window.location="${ctx}/ec/goodsSupplierContacts/newgoodsSupplierContactsList?goodsSupplierId="+goodsSupplierId;
				}
			},
			error:function(XMLHttpRequest,textStatus,errorThrown){
						    
			}
						 
		});
	}
	
	function addGoodsSupplierContacts(id,goodsSupplierId){
		top.layer.open({
		    type: 2, 
		    area: ['600px', '550px'],
		    title:"编辑供应商联系人",
		    content: "${ctx}/ec/goodsSupplierContacts/form?goodsSupplierContactsId="+id+"&goodsSupplierId="+goodsSupplierId,
		    btn: ['确定', '关闭'],
		    yes: function(index, layero){
		        var obj =  layero.find("iframe")[0].contentWindow;
				var name = obj.document.getElementById("name");
				var mobile = obj.document.getElementById("mobile");
				var status = obj.document.getElementById("status");
		        var arr = new Array(); //数组定义标准形式，不要写成Array arr = new Array();
		        var all = new Array(); //定义变量全部保存
				
		        if($(name).val()== null || $(name).val()==""){
					top.layer.alert('姓名不能为空!', {icon: 0, title:'提醒'}); 
					return;
				}
		        var mob = $(mobile).val();
		        var regtitle = /^(133[0-9]{8})|(153[0-9]{8})|(180[0-9]{8})|(181[0-9]{8})|(189[0-9]{8})|(177[0-9]{8})|(130[0-9]{8})|(131[0-9]{8})|(132[0-9]{8})|(155[0-9]{8})|(156[0-9]{8})|(185[0-9]{8})|(186[0-9]{8})|(145[0-9]{8})|(170[0-9]{8})|(176[0-9]{8})|(134[0-9]{8})|(135[0-9]{8})|(136[0-9]{8})|(137[0-9]{8})|(138[0-9]{8})|(139[0-9]{8})|(150[0-9]{8})|(151[0-9]{8})|(152[0-9]{8})|(157[0-9]{8})|(158[0-9]{8})|(159[0-9]{8})|(182[0-9]{8})|(183[0-9]{8})|(184[0-9]{8})|(187[0-9]{8})|(188[0-9]{8})|(147[0-9]{8})|(178[0-9]{8})|(149[0-9]{8})|(173[0-9]{8})|(175[0-9]{8})|(171[0-9]{8})$/;        
		        if($(mobile).val() == null || $(mobile).val() == "" || !regtitle.test(mob) || mob.length != 11){
					top.layer.alert('请输入正确的手机号码！', {icon: 0, title:'提醒'});
					return;
				}
		        //异步添加或者更新goodsSupplierContacts
				$.ajax({
				type:"post",
				data:{
					goodsSupplierContactsId:id,
					goodsSupplierId:goodsSupplierId,
					name:$(name).val(),
					mobile:mob,
					status:$(status).val()
				 },
				url:"${ctx}/ec/goodsSupplierContacts/save",
				success:function(date){
					if(date == "success"){
						top.layer.alert('保存成功!', {icon: 1, title:'提醒'});
						window.location="${ctx}/ec/goodsSupplierContacts/newgoodsSupplierContactsList?goodsSupplierId="+goodsSupplierId;
					}
					if(date=="error"){
						top.layer.alert('保存失败!', {icon: 2, title:'提醒'});
						window.location="${ctx}/ec/goodsSupplierContacts/newgoodsSupplierContactsList?goodsSupplierId="+goodsSupplierId;
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
	//供应商状态  改变事件
	function changeStatus(id,status){
		$(".loading").show();//打开展示层
		$.ajax({
			type : "POST",   
			url : "${ctx}/ec/goodsSupplierContacts/updateStatus",
			data:{
				goodsSupplierContactsId:id,
				status:status
			},
			dataType: 'json',
			success: function(data) {
				$(".loading").hide(); //关闭加载层
				var yesOrNo = data.yesOrNo;
				var status = data.status;
				if("SUCCESS" == yesOrNo){
					$("#"+id).html("");//清除DIV内容	
					if(status == '0'){
						//status==0, 要商用
						$("#"+id).append("<img width='20' height='20' src='${ctxStatic}/ec/images/open.png' onclick=\"changeStatus('"+id+"','1')\">");
					}else if(status == '1'){
						//status==1, 要暂停
						$("#"+id).append("<img width='20' height='20' src='${ctxStatic}/ec/images/cancel.png' onclick=\"changeStatus('"+id+"','0')\">");
					}
				}else if("ERROR" == status){
					alert(data.MESSAGE);
				}
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
				</div>
				<div>
					<shiro:hasPermission name="ec:goodsSupplierContacts:add">
						<a href="#" onclick="addGoodsSupplierContacts(0,${supplierId})" class="btn btn-primary btn-xs" ><i class="fa fa-plus"></i>添加供应商联系人</a>
					</shiro:hasPermission>
				</div>
				<p></p>
				<table id="contentTable" class="table table-striped table-bordered  table-hover table-condensed  dataTables-example dataTable no-footer">
					<thead>
						<tr>
							<th style="text-align: center;">联系人ID</th>
							<th style="text-align: center;">供应商ID</th>
							<th style="text-align: center;">姓名</th>
							<th style="text-align: center;">电话</th>
							<th style="text-align: center;">联系人状态</th>
                			<th style="text-align: center;">操作</th>
						</tr>
					</thead>
					<tbody style="text-align: center;">
						<c:forEach items="${page.list}" var="page">
							<tr>
								<td style="text-align: center;">${page.goodsSupplierContactsId}</td>
								<td style="text-align: center;">${page.goodsSupplierId}</td>
								<td style="text-align: center;">${page.name}</td>
								<td style="text-align: center;">${page.mobile}</td>
								<td style="text-align: center;" id="${page.goodsSupplierContactsId}">
									<shiro:hasPermission name="ec:goodsSupplierContacts:update">
										<c:if test="${page.status == 1}">
											<img width="20" height="20" src="${ctxStatic}/ec/images/cancel.png" onclick="changeStatus('${page.goodsSupplierContactsId}','0')">
										</c:if>
										<c:if test="${page.status == 0}">
											<img width="20" height="20" src="${ctxStatic}/ec/images/open.png" onclick="changeStatus('${page.goodsSupplierContactsId}','1')">
										</c:if>
									</shiro:hasPermission>
								</td>
								<td style="text-align: center;">
									<shiro:hasPermission name="ec:goodsSupplierContacts:view">
										<a href="#" onclick="openDialogView('查看', '${ctx}/ec/goodsSupplierContacts/form?goodsSupplierContactsId=${page.goodsSupplierContactsId}','600px', '550px')" class="btn btn-info btn-xs"><i class="fa fa-search-plus"></i> 查看</a>
									</shiro:hasPermission>
									<shiro:hasPermission name="ec:goodsSupplierContacts:edit">
										<a href="#" onclick="addGoodsSupplierContacts(${page.goodsSupplierContactsId},${page.goodsSupplierId})" class="btn btn-success btn-xs"><i class="fa fa-edit"></i>修改</a>
									</shiro:hasPermission>
									<shiro:hasPermission name="ec:goodsSupplierContacts:del">
										<a href="#" onclick="delGoodsSupplierContacts(${page.goodsSupplierContactsId},${page.goodsSupplierId})" class="btn btn-danger btn-xs"><i class="fa fa-trash"></i>删除</a>
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