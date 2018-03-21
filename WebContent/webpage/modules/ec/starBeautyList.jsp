<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/webpage/include/taglib.jsp"%>
<%@ page import="com.training.modules.sys.utils.ParametersFactory"%>
<%
	String uploadURL = ParametersFactory.getMtmyParamValues("uploader_url");
%>
<!DOCTYPE html>
<html>
<head>
    <meta charset="utf-8">
   	<meta name="decorator" content="default"/>
    <meta name="viewport" content="width=device-width, initial-scale=1.0"> 
    <link rel="stylesheet" href="${ctxStatic}/ec/css/loading.css">
    <!-- 图片上传 引用-->
	<link rel="stylesheet" type="text/css" href="${ctxStatic}/ec/css/custom_uploadImg.css">
	<link rel="stylesheet" type="text/css" href="${ctxStatic}/train/uploadify/uploadify.css">
	<script type="text/javascript" src="${ctxStatic}/train/uploadify/lang-cn.js"></script>
	<script type="text/javascript" src="${ctxStatic}/train/uploadify/jquery.uploadify.min.js"></script>
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
		//是否启用
		function updateType(id,isShow,isOpen,franchiseeId){
			$(".loading").show();//打开展示层
			$.ajax({
				type : "POST",
				url : "${ctx}/ec/starBeauty/updateType?id="+id+"&isShow="+isShow+"&isOpen="+isOpen+"&franchiseeId="+franchiseeId,
				dataType: 'json',
				success: function(data) {
					$(".loading").hide(); //关闭加载层
					var status = data.STATUS;
					if("OK" == status){
						window.location="${ctx}/ec/starBeauty/list";
					}else if("ERROR" == status){
						alert(data.MESSAGE);
					}
				}
			}); 
		}
		
		//确认是否删除
		function  del(id,isShow){
			if(isShow == 1){
				top.layer.alert('此数据启用中,请重新选择!', {icon: 0, title:'提醒'});
				return;
			}else{
				if(confirm("确认要删除吗？","提示框")){
					isDelete(id);			
				}
			}
		}
		//不启用的数据可以被删除
		function isDelete(id){
			$(".loading").show();//打开展示层
			$.ajax({
				type : "POST",
				url : "${ctx}/ec/starBeauty/del?id="+id,
				dataType: 'json',
				success: function(data) {
					$(".loading").hide(); //关闭加载层
					var status = data.STATUS;
					if("OK" == status){
						window.location="${ctx}/ec/starBeauty/list";
					}else if("ERROR" == status){
						top.layer.alert(data.MESSAGE, {icon: 0, title:'提醒'});
					}
				}
			}); 
		}
    </script>
    <title>明星技师管理</title>
</head>
<body>
	<div class="wrapper-content">
        <div class="ibox">
            <div class="ibox-title">
                <h5>明星技师管理</h5>
            </div>
            <sys:message content="${message}"/>
            <div class="ibox-content">
                <div class=" clearfix">
                	<form id="searchForm" action="${ctx}/ec/starBeauty/list" method="post" class="navbar-form navbar-left searcharea">
						<!-- 翻页隐藏文本框 -->
						<input id="pageNo" name="pageNo" type="hidden" value="${page.pageNo}"/>
						<input id="pageSize" name="pageSize" type="hidden" value="${page.pageSize}"/>
					</form>
                    <!-- 工具栏 -->
					<div class="row" style="padding-bottom: 5px;">
						<div class="col-sm-12">
	                        <div class="pull-left">
	                        	<shiro:hasPermission name="ec:starBeauty:add">
	                        		<table:addRow url="${ctx}/ec/starBeauty/form" title="明星技师组" width="700px" height="550px"></table:addRow>
	                        	</shiro:hasPermission>
	                        </div>
						</div>
					</div>
                </div>
                <table id="treeTable" class="table table-bordered table-hover table-striped">
                	<thead> 
                		<tr>
                			<th style="text-align: center;">ID</th>
                			<th style="text-align: center;">明星技师组名称</th>
                			<th style="text-align: center;">是否显示</th>
                			<th style="text-align: center;">商家</th>
                			<th style="text-align: center;">排序</th>
                			<th style="text-align: center;">创建者</th>
                			<th style="text-align: center;">创建时间</th>
                			<th style="text-align: center;">操作</th>
                		</tr>
                	</thead>
                    <tbody>
                    	<c:forEach items="${page.list}" var="starBeauty">
						<tr>
							<td style="text-align: center;">${starBeauty.id}</td>
							<td style="text-align: center;">${starBeauty.name}</td>
							<td style="text-align: center;">
								<c:if test="${starBeauty.isShow  == 0}">
									<img width="20" height="20" src="${ctxStatic}/ec/images/cancel.png" onclick="updateType('${starBeauty.id}',1,'${starBeauty.isOpen}','${starBeauty.franchiseeId}')">
								</c:if>
								<c:if test="${starBeauty.isShow == 1}">
									<img width="20" height="20" src="${ctxStatic}/ec/images/open.png" onclick="updateType('${starBeauty.id}',0,'${starBeauty.isOpen}','${starBeauty.franchiseeId}')">
								</c:if>
							</td>
							<c:if test="${starBeauty.isOpen == 0}">
								<td style="text-align: center;">公开</td>
							</c:if>
							<c:if test="${starBeauty.isOpen == 1}">
								<td style="text-align: center;">${starBeauty.franchiseeName}</td>
							</c:if>
							<td style="text-align: center;">${starBeauty.sort}</td>
							<td style="text-align: center;">${starBeauty.createBy.name}</td>
							<td style="text-align: center;"><fmt:formatDate value="${starBeauty.createDate}"  pattern="yyyy-MM-dd HH:mm:ss" /></td>
							<td style="text-align: center;">
								<shiro:hasPermission name="ec:starBeauty:view">
									<a href="#" onclick="openDialogView('查看', '${ctx}/ec/starBeauty/form?id=${starBeauty.id}','600px', '550px')" class="btn btn-info btn-xs"><i class="fa fa-search-plus"></i> 查看</a>
								</shiro:hasPermission>
								<shiro:hasPermission name="ec:starBeauty:edit">
									<a href="#" onclick="openDialog('修改', '${ctx}/ec/starBeauty/form?id=${starBeauty.id}','600px', '550px')" class="btn btn-success btn-xs"><i class="fa fa-edit"></i> 修改</a>
								</shiro:hasPermission>
								<shiro:hasPermission name="ec:starBeauty:del">
									<a href="#" onclick="del('${starBeauty.id}','${starBeauty.isShow}')" class="btn btn-danger btn-xs"><i class="fa fa-trash"></i>删除</a>
								</shiro:hasPermission>
								<shiro:hasPermission name="ec:starBeautyMapping:add">
									<a href="#" onclick="openDialogView('添加明星技师', '${ctx}/ec/starBeauty/starBeautyMappingList?starId=${starBeauty.id}&isShow=${starBeauty.isShow}&franchiseeId=${starBeauty.franchiseeId}','700px', '550px')" class="btn btn-info btn-xs"><i class="fa fa-edit"></i> 添加明星技师</a>
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