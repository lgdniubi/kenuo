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
	    
	  //是否显示
		function changeTableVal(id,isShow){
			$(".loading").show();//打开展示层
			$.ajax({
				type : "POST",
				url : "${ctx}/train/liveGift/updateIsShow?isShow="+isShow+"&trainLiveGiftId="+id,
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
	  
		 //是否显示
		function changeIsBatter(id,isBatter){
			$(".loading").show();//打开展示层
			$.ajax({
				type : "POST",
				url : "${ctx}/train/liveGift/updateIsBatter?isBatter="+isBatter+"&trainLiveGiftId="+id,
				dataType: 'json',
				success: function(data) {
					$(".loading").hide(); //关闭加载层
					var status = data.STATUS;
					var isBatter = data.ISBATTER;
					if("OK" == status){
						$("#isBatter"+id).html("");//清除DIV内容
						if(isBatter == '1'){
							$("#isBatter"+id).append("<img width='20' height='20' src='${ctxStatic}/ec/images/cancel.png' onclick=\"changeIsBatter('"+id+"','0')\">");
						}else if(isBatter == '0'){
							$("#isBatter"+id).append("<img width='20' height='20' src='${ctxStatic}/ec/images/open.png' onclick=\"changeIsBatter('"+id+"','1')\">");
						}
					}else if("ERROR" == status){
						top.layer.alert(data.MESSAGE, {icon: 2, title:'提醒'});
					}
				}
			});   
		}
		
    </script>
    <title>直播送礼管理</title>
</head>
<body>
	<div class="wrapper-content">
        <div class="ibox">
            <sys:message content="${message}"/>
            <div class="ibox-content">
                <div class=" clearfix">
                	<form id="searchForm" action="${ctx}/train/liveGift/list" method="post" class="navbar-form navbar-left searcharea">
						<!-- 翻页隐藏文本框 -->
						<input id="pageNo" name="pageNo" type="hidden" value="${page.pageNo}"/>
						<input id="pageSize" name="pageSize" type="hidden" value="${page.pageSize}"/>
						<input id="trainLiveGiftId" type="hidden" name="trainLiveGiftId" value="${trainLiveGift.trainLiveGiftId}">
					</form>
                    <!-- 工具栏 -->
					<div class="row" style="padding-bottom: 5px;">
						<div class="col-sm-12">
	                        <div class="pull-left">
	                        	<shiro:hasPermission name="train:liveGift:add">
	                        		<table:addRow url="${ctx}/train/liveGift/form" title="直播送礼" width="700px" height="550px"></table:addRow>
	                        	</shiro:hasPermission>
	                        </div>
						</div>
					</div>
                </div>
                <table id="treeTable" class="table table-bordered table-hover table-striped">
                	<thead> 
                		<tr>
                			<th style="text-align: center;">名称</th>
                			<th style="text-align: center;">图片</th>
                			<th style="text-align: center;">英文名</th>
                			<th style="text-align: center;">云币</th>
                			<th style="text-align: center;">排序</th>
                			<th style="text-align: center;">是否连发</th>
                			<th style="text-align: center;">状态</th>
                			<th style="text-align: center;">操作</th>
                		</tr>
                	</thead>
                    <tbody>
                    	<c:forEach items="${page.list}" var="trainLiveGift">
						<tr>
							<td style="text-align: center;">${trainLiveGift.name}</td>
							<td style="text-align: center;"class="imgUrl" ><img alt="" src="${ctxStatic}/images/lazylode.png"  data-src="${trainLiveGift.imgUrl}" style="width: 150px;height: 100px;border:1px solid black; "></td>
							<td style="text-align: center;">${trainLiveGift.imgName}</td>
							<td style="text-align: center;">${trainLiveGift.integrals}</td>
							<td style="text-align: center;">${trainLiveGift.sort}</td>
							<td style="text-align: center;" id="isBatter${trainLiveGift.trainLiveGiftId}">
								<c:if test="${trainLiveGift.isBatter==1}">
									<img width="20" height="20" src="${ctxStatic}/ec/images/cancel.png" onclick="changeIsBatter('${trainLiveGift.trainLiveGiftId}','0')">
								</c:if>
								<c:if test="${trainLiveGift.isBatter==0}">
									<img width="20" height="20" src="${ctxStatic}/ec/images/open.png" onclick="changeIsBatter('${trainLiveGift.trainLiveGiftId}','1')">
								</c:if>
							</td>
							<td style="text-align: center;" id="isShow${trainLiveGift.trainLiveGiftId}">
								<c:if test="${trainLiveGift.isShow==1}">
									<img width="20" height="20" src="${ctxStatic}/ec/images/cancel.png" onclick="changeTableVal('${trainLiveGift.trainLiveGiftId}','0')">
								</c:if>
								<c:if test="${trainLiveGift.isShow==0}">
									<img width="20" height="20" src="${ctxStatic}/ec/images/open.png" onclick="changeTableVal('${trainLiveGift.trainLiveGiftId}','1')">
								</c:if>
							</td>
							<td style="text-align: center;">
								<shiro:hasPermission name="train:liveGift:edit">
									<a href="#" onclick="openDialog('修改', '${ctx}/train/liveGift/form?trainLiveGiftId=${trainLiveGift.trainLiveGiftId}','600px', '550px')" class="btn btn-success btn-xs"><i class="fa fa-edit"></i> 修改</a>
								</shiro:hasPermission>
								<shiro:hasPermission name="train:liveGift:del">
									<a href="${ctx}/train/liveGift/del?trainLiveGiftId=${trainLiveGift.trainLiveGiftId}" onclick="return confirmx('确认要删除吗？', this.href)" class="btn btn-danger btn-xs"><i class="fa fa-trash"></i> 删除</a>
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