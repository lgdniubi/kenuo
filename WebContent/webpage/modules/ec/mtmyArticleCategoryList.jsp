<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/webpage/include/taglib.jsp"%>
<!DOCTYPE html>
<html>
<head>
    <meta charset="utf-8">
   	<meta name="decorator" content="default"/>
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <link rel="stylesheet" href="${ctxStatic}/train/css/exam.css">
    <script>
	    function page(n,s){
			$("#pageNo").val(n);
			$("#pageSize").val(s);
			$("#searchForm").submit();
	    	return false;
	    }
		function show(a,b,c,d){
			// 弹出框	a 判断为添加还是修改  b 若为修改时传类别ID  c 若为修改时传类别名称 d 若为修改时传排序
			if(a==1){
				$("#id").val(b);
				$("#name").val(c);
				$("#sort").val(d);
			}else{
				$("#id").val('');
				$("#name").val('');
				$("#sort").val('');
			}
	        $('#myReply').modal('show');           
		}
    </script>
    <style type="text/css">
	.modal-content{
		margin:0 auto;
		width: 400px;
	}
	</style>
    <title>文章类别管理</title>
</head>
<body>
	<div class="wrapper-content">
        <div class="ibox">
            <div class="ibox-title">
                <h5>文章类别管理</h5>
            </div>
            <sys:message content="${message}"/>
            <div class="ibox-content">
                <div class=" clearfix">
                	<form id="searchForm" action="${ctx}/ec/mtmyArticleList/categoryList" method="post" class="navbar-form navbar-left searcharea">
						<!-- 翻页隐藏文本框 -->
						<input id="pageNo" name="pageNo" type="hidden" value="${page.pageNo}"/>
						<input id="pageSize" name="pageSize" type="hidden" value="${page.pageSize}"/>
					</form>
                    <!-- 工具栏 -->
					<div class="row" style="padding-bottom: 5px;">
						<div class="col-sm-12">
	                        <div class="pull-left">
	                        	<shiro:hasPermission name="ec:mtmyArticleList:saveCategory">
		                        	<button class="btn btn-white btn-sm" title="添加文章" onclick='show(0,0,0,0)' data-placement="left" data-toggle="tooltip">
										<i class="fa fa-plus"></i>添加文章类别
									</button>
								</shiro:hasPermission>
	                        </div>
						</div>
					</div>
                </div>
                <table id="treeTable" class="table table-bordered table-hover table-striped">
                	<thead> 
                		<tr>
                			<th style="text-align: center;">分类名称</th>
                			<td style="text-align: center;">排序</td>
                			<th style="text-align: center;">创建时间</th>
                			<th style="text-align: center;">操作</th>
                		</tr>
                	</thead>
                    <tbody>
                    	<c:forEach items="${page.list}" var="categoryList">
						<tr>
							<td style="text-align: center;">${categoryList.name}</td>
							<td style="text-align: center;">${categoryList.sort}</td>
							<td style="text-align: center;"><fmt:formatDate value="${categoryList.createDate}" pattern="yyyy-MM-dd HH:mm:ss"/></td>
							<td style="text-align: center;">
								<shiro:hasPermission name="ec:mtmyArticleList:saveCategory">
			                        <a class="btn btn-success btn-xs" href="#" onclick="show(1,' ${categoryList.id}','${categoryList.name}','${categoryList.sort}')"><i class="fa fa-edit"></i>修改</a>
		                        </shiro:hasPermission>
		                        <shiro:hasPermission name="ec:mtmyArticleList:deleteCategory">
									<c:if test="${categoryList.id < 50 or categoryList.id > 52}">
										<a href="${ctx}/ec/mtmyArticleList/deleteCategory?id=${categoryList.id}" onclick="return confirmx('确认要删除该类别吗？', this.href)"   class="btn btn-info btn-xs btn-danger"><i class="fa fa-trash"></i> 删除</a>
									</c:if>
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
    <!-- 弹出框 -->
    <div class="modal fade" id="myReply" tabindex="-1" role="dialog" aria-labelledby="myModalLabel">
        <form class="modal-dialog" role="document" id="reviewarea" action="${ctx}/ec/mtmyArticleList/saveCategory">
        	<!-- 获取类别ID -->
        	<input id="id" name="id" type="hidden" />
            <div class="modal-content">
                <div class="modal-header">
                    <button type="button" class="close" data-dismiss="modal" aria-label="Close"><span aria-hidden="true">&times;</span></button>
                    <h4 class="modal-title" id="myModalLabel">类别管理</h4>
                </div>                            
                <div class="modal-body">
                   	文章类别：<input type="text" id="name" name="name" class="form-control" required size="26" placeholder="输入新添加文章类别" maxlength="10">
                   <p>&nbsp;</p>
                   	&nbsp;&nbsp;&nbsp;&nbsp;排&nbsp;&nbsp;&nbsp;序：<input type="text" id="sort" name="sort" class="form-control digits required" size="26" placeholder="输入排序数字" maxlength="4" onkeyup="this.value=this.value.replace(/\D/g,'')">
                </div>
                <div class="modal-footer">
                    <button type="button" class="btn btn-default" data-dismiss="modal">取消</button>
                    <button type="submit" class="btn btn-primary">确定</button>
                </div>
            </div>
        </form>
    </div>
    <script type="text/javascript">
		$.validator.setDefaults({
			submitHandler: function(form){
				form.submit();
		    }
		});
		$().ready(function(){
		    $("#reviewarea").validate();
		});
	</script>
</body>
</html>