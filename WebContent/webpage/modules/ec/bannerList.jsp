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
		function updateType(fromid,id,isyesno){
			$(".loading").show();//打开展示层
			$.ajax({
				type : "POST",
				url : "${ctx}/ec/banner/updateType?ID="+id+"&FROMID="+fromid+"&ISYESNO="+isyesno,
				dataType: 'json',
				success: function(data) {
					$(".loading").hide(); //关闭加载层
					var status = data.STATUS;
					var FLAG = data.FLAG;
					if("OK" == status){
						$("#"+fromid+id).html("");//清除DIV内容	
						if(FLAG == '0'){
							$("#"+fromid+id).append("<img width='20' height='20' src='${ctxStatic}/ec/images/open.png' onclick=\"updateType('"+fromid+"','"+id+"','1')\"><input type='hidden' id='"+id+"_isShow' value='0'>");
						}else if(FLAG == '1'){
							$("#"+fromid+id).append("<img width='20' height='20' src='${ctxStatic}/ec/images/cancel.png' onclick=\"updateType('"+fromid+"','"+id+"','0')\"><input type='hidden' id='"+id+"_isShow' value='1'>");
						}
					}else if("ERROR" == status){
						alert(data.MESSAGE);
					}
				}
			}); 
		}
		//确认是否删除
		function  deleteBanner(bannerId){
			var isShow = $("#"+bannerId+"_isShow").val();
			if(isShow == 0){
				top.layer.alert('此数据启用中,请重新选择!', {icon: 0, title:'提醒'});
				return;
			}else{
				if(confirm("确认要删除吗？","提示框")){
					isDelete(bannerId);			
				}
			}
		}
		//不启用的数据可以被删除
		function isDelete(bannerId){
			$(".loading").show();//打开展示层
			$.ajax({
				type : "POST",
				url : "${ctx}/ec/banner/delete?bannerId="+bannerId,
				dataType: 'json',
				success: function(data) {
					$(".loading").hide(); //关闭加载层
					var status = data.STATUS;
					if("SUCCESS" == status){
						top.layer.alert(data.MESSAGE, {icon: 0, title:'提醒'});
						window.location="${ctx}/ec/banner/list";
					}else if("ERROR" == status){
						top.layer.alert(data.MESSAGE, {icon: 0, title:'提醒'});
						window.location="${ctx}/ec/banner/list";
					}
				}
			}); 
		}
    </script>
    <title>banner图管理</title>
</head>
<body>
	<div class="wrapper-content">
        <div class="ibox">
            <div class="ibox-title">
                <h5>banner图管理</h5>
            </div>
            <sys:message content="${message}"/>
            <div class="ibox-content">
                <div class=" clearfix">
					<div class="row">
						<div class="col-sm-12">
							<form:form id="searchForm" modelAttribute="banner" action="${ctx}/ec/banner/list" method="post" class="navbar-form navbar-left searcharea">
							<div class="pull-left">
								<input id="pageNo" name="pageNo" type="hidden" value="${page.pageNo}" />
								<input id="pageSize" name="pageSize" type="hidden" value="${page.pageSize}" />
								<div class="form-group">
									<form:input path="bannerName" htmlEscape="false" maxlength="50" class=" form-control input-sm" placeholder="banner图名称"/>
									<label>是否显示：</label>
									<form:select path="isShow" cssClass="form-control">
										<form:option value="-1">全部</form:option>
										<form:option value="0">显示</form:option>
										<form:option value="1">隐藏</form:option>
									</form:select>
									<label>是否分享：</label>
									<form:select path="isShare" cssClass="form-control">
										<form:option value="-1">全部</form:option>
										<form:option value="0">分享</form:option>
										<form:option value="1">不分享</form:option>
									</form:select>
									<label>位置类型：</label>
									<form:select path="bannerType" cssClass="form-control">
										<form:option value="-1">全部</form:option>
										<form:options items="${fns:getDictList('bannerType')}" itemLabel="label" itemValue="value" htmlEscape="false"/>
									</form:select>
									<label>选择商家：</label>
									<form:select path="franchiseeIds" cssClass="form-control" style="text-align: center;width: 130px;">
										<form:option value="-1">全部</form:option>
										<form:option value="0">公开</form:option>
										<c:forEach items="${franchiseeList}" var="list">
											<form:option value="${list.id}">${list.name}</form:option>
										</c:forEach>
									</form:select>
								</div>
							</div>	
							</form:form>
							<div class="pull-right">
								<button class="btn btn-primary btn-rounded btn-outline btn-sm " onclick="search()"><i class="fa fa-search"></i> 查询</button>
								<button class="btn btn-primary btn-rounded btn-outline btn-sm " onclick="reset()"><i class="fa fa-refresh"></i> 重置</button>
							</div>
							<br/>
						</div>
					</div>
                    <!-- 工具栏 -->
					<div class="row" style="padding-bottom: 5px;">
						<div class="col-sm-12">
	                        <div class="pull-left">
	                        	<shiro:hasPermission name="ec:banner:add">
	                        		<table:addRow url="${ctx}/ec/banner/form" title="banner图" width="600px" height="550px"></table:addRow>
	                        	</shiro:hasPermission>
	                        </div>
						</div>
					</div>
                </div>
                <table id="treeTable" class="table table-bordered table-hover table-striped">
                	<thead> 
                		<tr>
                			<th style="text-align: center;">ID</th>
                			<th style="text-align: center;">banner图名称</th>
                			<th style="text-align: center;">banner图</th>
                			<th style="text-align: center;">位置类型</th>
                			<th style="text-align: center;">跳转类型</th>
                			<th style="text-align: center;">是否显示</th>
                			<th style="text-align: center;">是否分享</th>
                			<th style="text-align: center;">排序</th>
                			<th style="text-align: center;">操作</th>
                		</tr>
                	</thead>
                    <tbody>
                    	<c:forEach items="${page.list}" var="banner">
						<tr>
							<td style="text-align: center;">${banner.bannerId}</td>
							<td style="text-align: center;"><c:out value="${banner.bannerName}"></c:out></td>
							<td style="text-align: center;" class="imgUrl">
								<img alt="" src="${ctxStatic}/images/lazylode.png" data-src="${banner.imgUrl}" style="width: 150px;height: 100px;">
							</td>
							<td style="text-align: center;">
								${fns:getDictLabel(banner.bannerType, 'bannerType', '')}
							</td>
							<td style="text-align: center;">
								${fns:getDictLabel(banner.pageType, 'pageType', '')}
							</td>
							<td style="text-align: center;" id="ISSHOW${banner.bannerId}">
								<shiro:hasPermission name="ec:banner:update">
									<c:if test="${banner.isShow  eq '1'}">
										<img width="20" height="20" src="${ctxStatic}/ec/images/cancel.png" onclick="updateType('ISSHOW','${banner.bannerId}','0')">
										<input type="hidden" id="${banner.bannerId}_isShow" value="${banner.isShow}">
									</c:if>
									<c:if test="${banner.isShow eq '0'}">
										<img width="20" height="20" src="${ctxStatic}/ec/images/open.png" onclick="updateType('ISSHOW','${banner.bannerId}','1')">
										<input type="hidden" id="${banner.bannerId}_isShow" value="${banner.isShow}">
									</c:if>
								</shiro:hasPermission>
							</td>
							<td style="text-align: center;" id="ISSHARE${banner.bannerId}">
								<shiro:hasPermission name="ec:banner:update">
									<c:if test="${banner.isShare eq '1'}">
										<img width="20" height="20" src="${ctxStatic}/ec/images/cancel.png" onclick="updateType('ISSHARE','${banner.bannerId}','0')">
									</c:if>
									<c:if test="${banner.isShare eq '0'}">
										<img width="20" height="20" src="${ctxStatic}/ec/images/open.png" onclick="updateType('ISSHARE','${banner.bannerId}','1')">
									</c:if>
								</shiro:hasPermission>
							</td>
							<td style="text-align: center;">${banner.sort}</td>
							<td style="text-align: center;">
								<shiro:hasPermission name="ec:banner:view">
									<a href="#" onclick="openDialogView('查看', '${ctx}/ec/banner/form?bannerId=${banner.bannerId}','600px', '550px')" class="btn btn-info btn-xs"><i class="fa fa-search-plus"></i> 查看</a>
								</shiro:hasPermission>
								<shiro:hasPermission name="ec:banner:edit">
									<a href="#" onclick="openDialog('修改', '${ctx}/ec/banner/form?bannerId=${banner.bannerId}','600px', '550px')" class="btn btn-success btn-xs"><i class="fa fa-edit"></i> 修改</a>
								</shiro:hasPermission>
								<shiro:hasPermission name="ec:banner:view">
									<a href="#" onclick="deleteBanner(${banner.bannerId})" class="btn btn-danger btn-xs" ><i class="fa fa-trash"></i> 删除</a>
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