<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/webpage/include/taglib.jsp"%>
<html>
<head>
    <meta charset="utf-8">
    <meta name="decorator" content="default"/>
    <script> 
		function changeOpenVal(flag,id,isOpen){
			if(isOpen == '3'){
//				$("#"+flag+id).append("<img width='20' height='20' src='${ctxStatic}/ec/images/cancel.png' onclick=\"changeOpenVal('"+flag+"','"+id+"','1')\">");
				cancelJsp(flag,id,isOpen);
			}else if(isOpen == '1'){
				openJsp(flag,id,isOpen);
	//			$("#"+flag+id).append("<img width='20' height='20' src='${ctxStatic}/ec/images/open.png' onclick=\"changeOpenVal('"+flag+"','"+id+"','3')\">");
			} 
		}
    	function cancelJsp(flag,id,isOpen){
			top.layer.open({
			    type: 2, 
			    area: ['300px', '300px'],
			    title:"是否启用",
			    content: "${ctx}/train/protocolModel/openProtocol?isOpen="+isOpen,
			    btn: ['确定', '取消']
    	    ,yes: function(index, layero){ //或者使用btn1
				changeTab(flag,id,isOpen);
				top.layer.close(index);
    	    },
	    	cancel: function(index){ //或者使用btn2 //按钮【按钮二】的回调
	    	     }
			}); 
    	}
    		
		
    	function openJsp(flag,id,isOpen){
			top.layer.open({
			    type: 2, 
			    area: ['300px', '300px'],
			    title:"是否启用",
			    content: "${ctx}/train/protocolModel/openProtocol?isOpen="+isOpen,
			    btn: ['是', '否']
    	    ,yes: function(index, layero){ //或者使用btn1
    	    	$.ajax({
    				type : "POST",
    				url : "${ctx}/train/protocolModel/deleteProtocolShop?id="+id,
    				dataType: 'json',
    				success: function(data) {
    					top.layer.msg("操作成功", {icon: 0});
    				}
    			}); 
				changeTab(flag,id,isOpen);
				top.layer.close(index);
    	    },
	    	btn2: function(index, layero){ //或者使用btn2 //按钮【按钮二】的回调
				changeTab(flag,id,isOpen);
				top.layer.close(index);
			},
	    	cancel: function(index){ //或者使用btn2 //按钮【按钮二】的回调
	    	     }
			}); 
    	}
    		
	  //是否启用
		function changeTab(flag,id,isOpen){
			$(".loading").show();//打开展示层
			$.ajax({
				type : "POST",
				url : "${ctx}/train/protocolModel/updateIsOpen?modelId="+id+"&isOpen="+isOpen,
				dataType: 'json',
				success: function(data) {
					$(".loading").hide(); //关闭加载层
					var STATUS = data.STATUS;
					var ISOPEN = data.ISOPEN;
					if("OK" == STATUS){
		            	$("#"+flag+id).html("");//清除DIV内容	
						if(ISOPEN == '3'){
							$("#"+flag+id).append("<img width='20' height='20' src='${ctxStatic}/ec/images/cancel.png' onclick=\"changeOpenVal('"+flag+"','"+id+"','1')\">");
						}else if(ISOPEN == '1'){
							$("#"+flag+id).append("<img width='20' height='20' src='${ctxStatic}/ec/images/open.png' onclick=\"changeOpenVal('"+flag+"','"+id+"','3')\">");
						} 
					}else if("ERROR" == STATUS){
						alert(data.MESSAGE);
					}
				}
			});   
		}
    </script>
    <title>协议列表</title>
</head>
<body>
	<div class="wrapper-content">
	<sys:message content="${message}" />
        <div class="ibox">
            <div class="ibox-title">
                <h5>协议列表</h5>
            </div>
<%--             <sys:message content="${message}"/> --%>
            <div class="ibox-content">
                <div class="" id="reviewlists">
					<table id="contentTable" class="table table-striped table-bordered  table-hover table-condensed  dataTables-example dataTable no-footer">
						<thead>
							<tr>
								<th style="text-align: center;">ID</th>
								<th style="text-align: center;">协议名称</th>
							    <!-- <th style="text-align: center;">状态</th> -->
							    <th style="text-align: center;">启用</th>
							    <th style="text-align: center;">创建时间</th>
							    <th style="text-align: center;">操作人</th>
						    	<th style="text-align: center;">操作</th>
							</tr>
						</thead>
						<tbody style="text-align: center;">
							<c:forEach items="${modelList}" var="model">
								<tr>
									<td>${model.id }</td>
								  	<td>${model.name }</td>
								  	<%-- <td>
									  	<c:if test="${model.status ==1}">启用</c:if>
									  	<c:if test="${model.status ==3}">停用</c:if>
									  	<c:if test="${model.status ==2}">变更</c:if>
									  	
								  	</td> --%>
								  	<td style="text-align: center;" id="isOpen${model.id}">
										<c:if test="${model.status == 3}">
											<img width="20" height="20" src="${ctxStatic}/ec/images/cancel.png" onclick="changeOpenVal('isOpen','${model.id}','1')">
										</c:if>
										<c:if test="${model.status == 1}">
											<img width="20" height="20" src="${ctxStatic}/ec/images/open.png" onclick="changeOpenVal('isOpen','${model.id}','3')">
										</c:if>
									</td>
								  	<td><fmt:formatDate value="${model.createDate}" pattern="yyyy-MM-dd HH:mm:ss"/></td>
								  	<td>${model.createBy.name}</td>
								    <td>
								    	<c:if test="${model.pid == 0}">
										<a href="#" onclick="openDialog('修改', '${ctx}/train/protocolModel/modelForm?id=${model.id}','850px', '550px')" class="btn btn-primary btn-xs" ><i class="fa fa-edit"></i>修改</a> 
										</c:if>
										<a href="#" onclick="openDialogView('查看', '${ctx}/train/protocolModel/modelForm?id=${model.id}','850px', '550px')" class="btn btn-primary btn-xs" ><i class="fa fa-edit"></i>查看</a> 
								    	<c:if test="${empty isShow || isShow !=1}">
										<a href="#" onclick="openDialogView('历史版本', '${ctx}/train/protocolModel/oldModelList?id=${model.id}','850px', '550px')" class="btn btn-primary btn-xs" ><i class="fa fa-edit"></i>历史版本</a> 
										</c:if>
								    </td>
								</tr>
							</c:forEach>
						</tbody>
					</table>
					<!-- 分页table -->
					<table:page page="${page}"></table:page>
				</div>
	        </div>
	    </div>
    </div>
    <div class="loading"></div>
</body>
</html>