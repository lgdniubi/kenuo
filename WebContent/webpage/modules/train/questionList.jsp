<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/webpage/include/taglib.jsp"%>
<html>
<head>
    <meta charset="utf-8">
    <meta name="decorator" content="default"/>
    <script> 
	  	//是否启用
		function changeOpenVal(flag,id,isOpen){
			$(".loading").show();//打开展示层
			$.ajax({
				type : "POST",
				url : "${ctx}/train/question/updateIsOpen?qId="+id+"&isOpen="+isOpen,
				dataType: 'json',
				success: function(data) {
					$(".loading").hide(); //关闭加载层
					var STATUS = data.STATUS;
					var ISOPEN = data.ISOPEN;
					if("OK" == STATUS){
		            	$("#"+flag+id).html("");//清除DIV内容	
						if(ISOPEN == '2'){
							$("#"+flag+id).append("<img width='20' height='20' src='${ctxStatic}/ec/images/cancel.png' onclick=\"changeOpenVal('"+flag+"','"+id+"','1')\">");
						}else if(ISOPEN == '1'){
							$("#"+flag+id).append("<img width='20' height='20' src='${ctxStatic}/ec/images/open.png' onclick=\"changeOpenVal('"+flag+"','"+id+"','2')\">");
						} 
					}else if("ERROR" == STATUS){
						alert(data.MESSAGE);
					}
				}
			});   
		}
    </script>
    <title>手册列表</title>
</head>
<body>
	<div class="wrapper-content">
	<sys:message content="${message}" />
        <div class="ibox">
            <div class="ibox-title">
                <h5>手册列表</h5>
            </div>
<%--             <sys:message content="${message}"/> --%>
			<!-- 工具栏 -->
			<div class="row">
				<div class="col-sm-12">
					<div class="pull-left">
						<table:addRow url="${ctx}/train/question/form?type=${type}" title="手册" width="800px" height="650px"></table:addRow>
						<c:choose>
							<c:when test="${type eq '3'}">
								<button class="btn btn-white btn-sm " data-toggle="tooltip" data-placement="left" onclick='top.openTab("${ctx}/train/handbook/list?type=${type}&isShop=0","查看分类", false)'><i class="fa fa-plus"></i> 查看管理端分类</button>
								<button class="btn btn-white btn-sm " data-toggle="tooltip" data-placement="left" onclick='top.openTab("${ctx}/train/handbook/list?type=${type}&isShop=1","查看分类", false)'><i class="fa fa-plus"></i> 查看店铺端分类</button>
							</c:when>
							<c:otherwise>
								<button class="btn btn-white btn-sm " data-toggle="tooltip" data-placement="left" onclick='top.openTab("${ctx}/train/handbook/list?type=${type}","查看分类", false)'><i class="fa fa-plus"></i> 查看分类</button>
							</c:otherwise>
						</c:choose>
					</div>
				</div>
			</div>
            <div class="ibox-content">
                <div class="" id="reviewlists">
					<table id="contentTable" class="table table-striped table-bordered  table-hover table-condensed  dataTables-example dataTable no-footer">
						<thead>
							<tr>
								<th style="text-align: center;">ID</th>
								<th style="text-align: center;">标题</th>
								<th style="text-align: center;">分类名称</th>
								<th style="text-align: center;">启用</th>
						    	<th style="text-align: center;">操作</th>
							</tr>
						</thead>
						<tbody style="text-align: center;">
							<c:forEach items="${questionList}" var="q">
								<tr>
									<td>${q.id }</td>
								  	<td>${q.name }</td>
								  	<td>${q.typeName }</td>
								  	<td style="text-align: center;" id="isOpen${q.id}">
								  		<c:if test="${q.status == 2}">
											<img width="20" height="20" src="${ctxStatic}/ec/images/cancel.png" onclick="changeOpenVal('isOpen','${q.id}','1')">
										</c:if>
										<c:if test="${q.status == 1}">
											<img width="20" height="20" src="${ctxStatic}/ec/images/open.png" onclick="changeOpenVal('isOpen','${q.id}','2')">
										</c:if>
								  	</td>
								    <td>
				    					<a href="${ctx}/train/question/delete?id=${q.id}"  class="btn btn-success btn-xs" ><i class="fa fa-edit"></i>删除</a>
				    					<a href="#" onclick="openDialog('修改', '${ctx}/train/question/form?id=${q.id}&type=${type}','850px', '650px')" class="btn btn-primary btn-xs" ><i class="fa fa-edit"></i>修改</a>
								    </td>
								</tr>
							</c:forEach>
						</tbody>
					</table>
				</div>
	        </div>
	    </div>
    </div>
    <div class="loading"></div>
</body>
</html>