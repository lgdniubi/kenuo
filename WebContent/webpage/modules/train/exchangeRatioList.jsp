<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/webpage/include/taglib.jsp"%>

<html>
<head>
<title>添加兑换比例列表</title>
<meta name="decorator" content="default" />
<link rel="stylesheet" href="${ctxStatic}/ec/css/loading.css">
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
	
	//是否显示
	function changeTableVal(id,isShow){
		$(".loading").show();//打开展示层
		$.ajax({
			type : "POST",
			url : "${ctx}/train/ratio/changeIsShow?isShow="+isShow+"&exchangeRatioId="+id,
			dataType: 'json',
			success: function(data) {
				$(".loading").hide(); //关闭加载层
				var status = data.STATUS;
				var isShow = data.ISSHOW;
				if("OK" == status){
					$("#isShow"+id).html("");//清除DIV内容
					if(isShow == '1'){
						$("#isShow"+id).append("<img width='20' height='20' src='${ctxStatic}/ec/images/cancel.png' onclick=\"changeTableVal('"+id+"','0')\">");
					}else if(isShow == '0'){
						$("#isShow"+id).append("<img width='20' height='20' src='${ctxStatic}/ec/images/open.png' onclick=\"changeTableVal('"+id+"','1')\">");
					}
				}else if("ERROR" == status){
					top.layer.alert(data.MESSAGE, {icon: 2, title:'提醒'});
				}
			}
		});   
	}
	
	function delExchangeRatio(id){
		if(confirm("确认要删除吗？","提示框")){
			newDelete(id);			
		}
	}
		
	function newDelete(id){
		$.ajax({
			type:"post",
			url:"${ctx}/train/ratio/delExchangeRatio?exchangeRatioId="+id,
			success:function(date){
				if(date=="success"){
					top.layer.alert('删除成功!', {icon: 1, title:'提醒'});
					window.location="${ctx}/train/ratio/exchangeRatioList?trainLiveActivityRatioId=${trainLiveExchangeRatio.activityId}";
				}
				if(date=="error"){
					top.layer.alert('删除失败!', {icon: 2, title:'提醒'});
					window.location="${ctx}/train/ratio/exchangeRatioList?trainLiveActivityRatioId=${trainLiveExchangeRatio.activityId}";
				}
							
			},
			error:function(XMLHttpRequest,textStatus,errorThrown){
						    
			}
						 
	});
	}
	
	function addExchangeRatio(id,flag){
		top.layer.open({
		    type: 2, 
		    area: ['600px', '550px'],
		    title:"编辑兑换比例",
		    content: "${ctx}/train/ratio/addExchangeRatioForm?id="+id+"&flag="+flag,
		    btn: ['确定', '关闭'],
		    yes: function(index, layero){
		        var obj =  layero.find("iframe")[0].contentWindow;
		        var name = obj.document.getElementById("name");
				var exchangeType = obj.document.getElementById("exchangeType");
		    	var exchangePrice1 = obj.document.getElementById("exchangePrice11");
		    	var exchangePrice2 = obj.document.getElementById("exchangePrice22");
				var integrals = obj.document.getElementById("integrals");
				var sort = obj.document.getElementById("sort");
		       	

				if(!$(name).val()){
					top.layer.alert('名称不能为空!', {icon: 0, title:'提醒'});
					return;
				}
				
				if($(exchangeType).val() == '0'){
					var exchangePrice = $(exchangePrice1).val();
					if(!/^\d+(\.\d{1,2})?$/.test($(exchangePrice1).val())){
						top.layer.alert('人民币的小数点后不可以超过2位!', {icon: 0, title:'提醒'});
						return;
					}
				}else if($(exchangeType).val() == '1'){
					var exchangePrice = $(exchangePrice2).val();
					if(!/^[1-9]*[1-9][0-9]*$/.test($(exchangePrice2).val())){
						top.layer.alert('佣金必须为整数!', {icon: 0, title:'提醒'});
						return;
					}
				}
				
				if(!/^[1-9]*[1-9][0-9]*$/.test($(integrals).val())){
					top.layer.alert('云币必须为正整数!', {icon: 0, title:'提醒'});
					return;
				}
				
				if(!/^[1-9]*[1-9][0-9]*$/.test($(sort).val())){
					top.layer.alert('排序必须为正整数!', {icon: 0, title:'提醒'});
					return;
				}
				
		        //异步添加或者更新兑换比例
				$.ajax({
				type:"post",
				data:{
					name:$(name).val(),
					exchangeType:$(exchangeType).val(),
					exchangePrice:exchangePrice,
					integrals:$(integrals).val(),
					sort:$(sort).val()
				 },
				url:"${ctx}/train/ratio/saveExchangeRatio?id="+id+"&flag="+flag,
				success:function(date){
					if(date=="success"){
						top.layer.alert('保存成功!', {icon: 1, title:'提醒'});
						window.location="${ctx}/train/ratio/exchangeRatioList?trainLiveActivityRatioId=${trainLiveExchangeRatio.activityId}";
					}
					if(date=="error"){
						top.layer.alert('保存失败!', {icon: 2, title:'提醒'});
						window.location="${ctx}/train/ratio/exchangeRatioList?trainLiveActivityRatioId=${trainLiveExchangeRatio.activityId}";
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
				</div>
				<div>
					<shiro:hasPermission name="train:ratio:add">
						<a href="#" onclick="addExchangeRatio(${trainLiveExchangeRatio.activityId},'add')" class="btn btn-primary btn-xs" ><i class="fa fa-plus"></i>添加兑换比例</a>
					</shiro:hasPermission>
				</div>
				<p></p>
				
				<table id="contentTable" class="table table-striped table-bordered  table-hover table-condensed  dataTables-example dataTable no-footer">
					<thead>
						<tr>
							<th style="text-align: center;">名称</th>
							<th style="text-align: center;">人民币(元)</th>
							<th style="text-align: center;">兑换云币</th>
							<th style="text-align: center;">排序</th>
							<th style="text-align: center;">是否显示</th>
							<th style="text-align: center;">操作</th>
						</tr>
					</thead>
					<tbody style="text-align: center;">
						<c:forEach items="${page0}" var="page">
						<tr>
							<td style="text-align: center;">${page.name}</td>
							<td style="text-align: center;">${page.exchangePrice}</td>
							<td style="text-align: center;">${page.integrals}</td>
							<td style="text-align: center;">${page.sort}</td>
							<td style="text-align: center;" id="isShow${page.exchangeRatioId}">
								<c:if test="${page.isShow==1}">
									<img width="20" height="20" src="${ctxStatic}/ec/images/cancel.png" onclick="changeTableVal('${page.exchangeRatioId}','0')">
								</c:if>
								<c:if test="${page.isShow==0}">
									<img width="20" height="20" src="${ctxStatic}/ec/images/open.png" onclick="changeTableVal('${page.exchangeRatioId}','1')">
								</c:if>
							</td>
							<td style="text-align: center;">
								<shiro:hasPermission name="train:ratio:edit">
									<a href="#" onclick="addExchangeRatio(${page.exchangeRatioId},'update')" class="btn btn-success btn-xs"><i class="fa fa-edit"></i>修改</a>
								</shiro:hasPermission>
								<shiro:hasPermission name="train:ratio:del">
									<a href="#" onclick="delExchangeRatio(${page.exchangeRatioId})" class="btn btn-danger btn-xs"><i class="fa fa-trash"></i>删除</a>
								</shiro:hasPermission> 
							</td>
						</tr>
						</c:forEach>
					</tbody>
				</table>
				<p></p>
				<table id="contentTable" class="table table-striped table-bordered  table-hover table-condensed  dataTables-example dataTable no-footer">
					<thead>
						<tr>
							<th style="text-align: center;">名称</th>
							<th style="text-align: center;">佣金数量</th>
							<th style="text-align: center;">兑换云币</th>
							<th style="text-align: center;">排序</th>
							<th style="text-align: center;">是否显示</th>
							<th style="text-align: center;">操作</th>
						</tr>
					</thead>
					<tbody style="text-align: center;">
						<c:forEach items="${page1}" var="page">
						<tr>
							<td style="text-align: center;">${page.name}</td>
							<td style="text-align: center;">${page.exchangePrice}</td>
							<td style="text-align: center;">${page.integrals}</td>
							<td style="text-align: center;">${page.sort}</td>
							<td style="text-align: center;" id="isShow${page.exchangeRatioId}">
								<c:if test="${page.isShow==1}">
									<img width="20" height="20" src="${ctxStatic}/ec/images/cancel.png" onclick="changeTableVal('${page.exchangeRatioId}','0')">
								</c:if>
								<c:if test="${page.isShow==0}">
									<img width="20" height="20" src="${ctxStatic}/ec/images/open.png" onclick="changeTableVal('${page.exchangeRatioId}','1')">
								</c:if>
							</td>
							<td style="text-align: center;">
								<shiro:hasPermission name="train:ratio:edit">
									<a href="#" onclick="addExchangeRatio(${page.exchangeRatioId},'update')" class="btn btn-success btn-xs"><i class="fa fa-edit"></i>修改</a>
								</shiro:hasPermission>
								<shiro:hasPermission name="train:ratio:del">
									<a href="#" onclick="delExchangeRatio(${page.exchangeRatioId})" class="btn btn-danger btn-xs"><i class="fa fa-trash"></i>删除</a>
								</shiro:hasPermission> 
							</td>
						</tr>
						</c:forEach>
					</tbody>
				</table>
			</div>
		</div>
		<div class="loading"></div>
	</div>
</body>
</html>