<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/webpage/include/taglib.jsp"%>
<html>
<head>
    <meta charset="utf-8">
    <meta name="decorator" content="default"/>
    <script> 
	    function isDelete(id,type,isShop){
	  		var url = "${ctx}/train/handbook/delete?id="+id+"&type="+type+"&isShop="+isShop;
	  		var msg = "确认删除？";
			$.ajax({
				url:'${ctx}/train/handbook/isDelete',
				type:'post',
				data:{id:id},
			 	dataType:'json',
			 	success:function(data){
			 		if(data){
			 			top.layer.msg("分类下包含问题，无法删除，请把问题处理后在进行删除！", {icon: 0});
			 		}else{
				  		top.layer.confirm(msg, {icon: 3, title:'系统提示'}, function(index){
							top.layer.close(index);
							location.href = url;
						});
			 		}
			 	}
			});
	    }
    </script>
    <title>手册类型列表</title>
</head>
<body>
	<div class="wrapper-content">
	<sys:message content="${message}" />
        <div class="ibox">
            <div class="ibox-title">
                <h5>手册类型列表</h5>
            </div>
<%--             <sys:message content="${message}"/> --%>
			<!-- 工具栏 -->
			<div class="row">
				<div class="col-sm-12">
					<div class="pull-left">
<%-- 						<button class="btn btn-white btn-sm " data-toggle="tooltip" data-placement="left" onclick='top.openTab("${ctx}/sys/speciality/list","特长管理", false)'><i class="fa fa-plus"></i> 特长管理</button> --%>
							<table:addRow url="${ctx}/train/handbook/form?type=${type}&isShop=${isShop}" title="手册类型" width="800px" height="650px"></table:addRow>
					</div>
				</div>
			</div>
            <div class="ibox-content">
                <div class="" id="reviewlists">
					<table id="contentTable" class="table table-striped table-bordered  table-hover table-condensed  dataTables-example dataTable no-footer">
						<thead>
							<tr>
								<th style="text-align: center;">排序</th>
								<th style="text-align: center;">分类名称</th>
						    	<th style="text-align: center;">操作</th>
							</tr>
						</thead>
						<tbody style="text-align: center;">
							<c:forEach items="${typeList}" var="type">
								<tr>
									<td>${type.sort }</td>
								  	<td>${type.name }</td>
								    <td>
				    					<a href="#" onclick="isDelete('${type.id}','${type.type}','${type.isShop}')" class="btn btn-success btn-xs" ><i class="fa fa-edit"></i>删除</a>
				    					<a href="#" onclick="openDialog('修改', '${ctx}/train/handbook/form?id=${type.id}','850px', '550px')" class="btn btn-primary btn-xs" ><i class="fa fa-edit"></i>修改</a>
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