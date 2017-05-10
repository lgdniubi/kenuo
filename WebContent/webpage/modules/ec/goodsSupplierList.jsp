<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/webpage/include/taglib.jsp"%>
<!DOCTYPE html>
<html>
<head>
    <meta charset="utf-8">
   	<meta name="decorator" content="default"/>
    <meta name="viewport" content="width=device-width, initial-scale=1.0"> 
    <link rel="stylesheet" href="${ctxStatic}/ec/css/loading.css">
    <script>
	    function page(n,s){
			$("#pageNo").val(n);
			$("#pageSize").val(s);
			$("#searchForm").submit();
	    	return false;
	    }
	    $(function(){
			$(".imgUrl img").each(function(){
				var $this = $(this),
					$src = $this.attr('data-src');  
				$this.attr({'src':$src})
			});
			
		});
	  	//重置表单
		function resetnew(){
			reset();
		}
		//供应商状态  改变事件
		function changeStatus(id,status){
			$(".loading").show();//打开展示层
			$.ajax({
				type : "POST",   
				url : "${ctx}/ec/goodsSupplier/updateStatus",
				data:{
					goodsSupplierId:id,
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
		//删除做验证
		function  delGoodsSupplier(id){
			//判断供应商下面是否还有联系人
			$.ajax({
				type:"post",
				url:"${ctx}/ec/goodsSupplierContacts/getCountGoodsSupplierContacts?goodsSupplierId="+id,
				success:function(date){
					if(date){
						if(confirm("供应商下面还存在联系人,确认要删除吗？","提示框")){
							newDelete(id);
						}
					}else{
						newDelete(id);
					}
				},
				error:function(XMLHttpRequest,textStatus,errorThrown){
							    
				}
			});
		}
			
		function newDelete(id){
			$.ajax({
				type:"post",
				url:"${ctx}/ec/goodsSupplier/del?goodsSupplierId="+id,
				success:function(date){
					if(date=="success"){
						top.layer.alert('删除成功!', {icon: 1, title:'提醒'});
						window.location="${ctx}/ec/goodsSupplier/list";
					}
					if(date=="error"){
						top.layer.alert('删除失败!', {icon: 2, title:'提醒'});
						window.location="${ctx}/ec/goodsSupplier/list";
					}
				},
				error:function(XMLHttpRequest,textStatus,errorThrown){
							    
				}
							 
			});
		}
    </script>
    <title>供应商管理</title>
</head>
<body>
	<div class="wrapper-content">
        <div class="ibox">
            <div class="ibox-title">
                <h5>供应商管理</h5>
            </div>
            <sys:message content="${message}"/>
            <div class="ibox-content">
                <div class=" clearfix">
                	<form:form id="searchForm" modelAttribute="goodsSupplier" action="${ctx}/ec/goodsSupplier/list" method="post" class="form-inline">
						<!-- 翻页隐藏文本框 -->
						<input id="pageNo" name="pageNo" type="hidden" value="${page.pageNo}"/>
						<input id="pageSize" name="pageSize" type="hidden" value="${page.pageSize}"/>
						<div class="form-group">
							<form:input path="name" htmlEscape="false" maxlength="50" class=" form-control input-sm" placeholder="供应商名称"/>&nbsp;&nbsp;&nbsp;&nbsp;
		                	<label>供应商状态：</label>
							<form:select path="status"  class="form-control" style="width:185px;">
									<form:option value="">全部</form:option>
									<form:option value="0">商用</form:option>
									<form:option value="1">暂停</form:option>
							</form:select>
							<shiro:hasPermission name="ec:goodsSupplier:list">
								<button type="button" class="btn btn-primary btn-rounded btn-outline btn-sm" onclick="search()">
									<i class="fa fa-search"></i> 搜索
								</button>
								<button type="button" class="btn btn-primary btn-rounded btn-outline btn-sm" onclick="resetnew()" >
									<i class="fa fa-refresh"></i> 重置
								</button>
							</shiro:hasPermission>
						</div>
					</form:form>
                    <!-- 工具栏 -->
					<div class="row" style="padding-bottom: 5px;">
						<div class="col-sm-12">
	                        <div class="pull-left">
	                        	<shiro:hasPermission name="ec:goodsSupplier:add">
	                        		<table:addRow url="${ctx}/ec/goodsSupplier/form" title="添加" width="600px" height="550px"></table:addRow>
	                        	</shiro:hasPermission>
	                        </div>
						</div>
					</div>
                </div>
                <table id="treeTable" class="table table-bordered table-hover table-striped">
                	<thead> 
                		<tr>
                			<th style="text-align: center;">ID</th>
                			<th style="text-align: center;">供应商名称</th>
                			<th style="text-align: center;">供应商状态</th>
                			<th style="text-align: center;">创建人</th>
                			<th style="text-align: center;">创建时间</th>
                			<th style="text-align: center;">修改人</th>
                			<th style="text-align: center;">修改时间</th>
                			<th style="text-align: center;">操作</th>
                		</tr>
                	</thead>
                    <tbody>
                    	<c:forEach items="${page.list}" var="goodsSupplier">
						<tr>
							<td style="text-align: center;">${goodsSupplier.goodsSupplierId}</td>
							<td style="text-align: center;">${goodsSupplier.name}</td>
							<td style="text-align: center;" id="${goodsSupplier.goodsSupplierId}">
								<shiro:hasPermission name="ec:goodsSupplier:update">
									<c:if test="${goodsSupplier.status == 1}">
										<img width="20" height="20" src="${ctxStatic}/ec/images/cancel.png" onclick="changeStatus('${goodsSupplier.goodsSupplierId}','0')">
									</c:if>
									<c:if test="${goodsSupplier.status == 0}">
										<img width="20" height="20" src="${ctxStatic}/ec/images/open.png" onclick="changeStatus('${goodsSupplier.goodsSupplierId}','1')">
									</c:if>
								</shiro:hasPermission>
							</td>
							<td style="text-align: center;">${goodsSupplier.createBy.id}</td>
							<td style="text-align: center;">
								<fmt:formatDate value="${goodsSupplier.createDate}"  pattern="yyyy-MM-dd HH:mm:ss" />
							</td>	
							<td style="text-align: center;">${goodsSupplier.updateBy.id}</td>
							<td style="text-align: center;">
								<fmt:formatDate value="${goodsSupplier.updateDate}"  pattern="yyyy-MM-dd HH:mm:ss" />
							</td>	
							<td style="text-align: center;">
								<shiro:hasPermission name="ec:goodsSupplier:view">
									<a href="#" onclick="openDialogView('查看', '${ctx}/ec/goodsSupplier/form?goodsSupplierId=${goodsSupplier.goodsSupplierId}','600px', '550px')" class="btn btn-info btn-xs"><i class="fa fa-search-plus"></i> 查看</a>
								</shiro:hasPermission>
								<shiro:hasPermission name="ec:goodsSupplier:edit">
									<a href="#" onclick="openDialog('修改', '${ctx}/ec/goodsSupplier/form?goodsSupplierId=${goodsSupplier.goodsSupplierId}','600px', '550px')" class="btn btn-success btn-xs"><i class="fa fa-edit"></i> 修改</a>
								</shiro:hasPermission>
								<shiro:hasPermission name="ec:goodsSupplier:del">
									<a href="#" onclick="delGoodsSupplier(${goodsSupplier.goodsSupplierId})" class="btn btn-danger btn-xs"><i class="fa fa-trash"></i>删除</a>									
								</shiro:hasPermission>
								<shiro:hasPermission name="ec:goodsSupplierContacts:add">
									<a href="#" onclick="openDialogView('增加供应商联系人', '${ctx}/ec/goodsSupplierContacts/list?goodsSupplierId=${goodsSupplier.goodsSupplierId}','800px', '600px')" class="btn btn-info btn-xs"><i class="fa fa-edit"></i> 增加供应商联系人</a>
								</shiro:hasPermission>
							</td>
						</tr>
						</c:forEach>
                    </tbody>
                    <tfoot>
 						<tr>
                            <td colspan="20">
                                <!-- 分页代码 --> 
                                <div class="tfoot">
                                </div>
                               	<table:page page="${page}"></table:page>
                            </td>	
                        </tr>
                    </tfoot>
                </table>
            </div>
        </div>
   </div>
   <div class="loading"></div>
</body>
</html>