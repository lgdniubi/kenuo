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
		
	$(document).ready(function() {
			var start = {
				    elem: '#begtime',
				    format: 'YYYY-MM-DD',
				    event: 'focus',
				    max: $("#endtime").val(),   //最大日期
				    istime: false,				//是否显示时间
				    isclear: true,				//是否显示清除
				    istoday: true,				//是否显示今天
				    issure: true,				//是否显示确定
				    festival: true,				//是否显示节日
				    choose: function(datas){
				         end.min = datas; 		//开始日选好后，重置结束日的最小日期
				         end.start = datas 		//将结束日的初始值设定为开始日
				    }
				};
			var end = {
				    elem: '#endtime',
				    format: 'YYYY-MM-DD',
				    event: 'focus',
				    min: $("#begtime").val(),
				    istime: false,
				    isclear: true,
				    istoday: true,
				    issure: true,
				    festival: true,
				    choose: function(datas){
				        start.max = datas; //结束日选好后，重置开始日的最大日期
				    }
				};
					laydate(start);
					laydate(end);
	    });
    </script>
    <title>说说管理</title>
</head>
<body>
	<div class="wrapper-content">
        <div class="ibox">
            <div class="ibox-title">
                <h5>说说管理</h5>
            </div>
            <sys:message content="${message}"/>
            <div class="ibox-content">
                <div class=" clearfix">
					<form:form id="searchForm" modelAttribute="mtmyEveryoneSay" action="${ctx}/ec/everyoneSay/list" method="post" class="form-inline">
						<!-- 翻页隐藏文本框 -->
						<input id="pageNo" name="pageNo" type="hidden" value="${page.pageNo}"/>
						<input id="pageSize" name="pageSize" type="hidden" value="${page.pageSize}"/>
						<div class="form-group">
							<label>说说时间：</label>
							<input id="begtime" name="begtime" type="text" maxlength="20" class="laydate-icon form-control layer-date input-sm" value="<fmt:formatDate value="${mtmyEveryoneSay.begtime}" pattern="yyyy-MM-dd"/>" style="width:185px;" placeholder="开始时间" readonly="readonly"/>
							一
							<input id="endtime" name="endtime" type="text" maxlength="20" class=" laydate-icon form-control layer-date input-sm" value="<fmt:formatDate value="${mtmyEveryoneSay.endtime}" pattern="yyyy-MM-dd"/>"  style="width:185px;" placeholder="结束时间" readonly="readonly"/>&nbsp;&nbsp;
							<label>位置类型：</label>
							<select id="positionType" name="positionType" class="form-control" style="width:185px;">
								<option value="">全部</option>
								<option value="1" ${(mtmyEveryoneSay.positionType == '1')?'selected="selected"':''}>店铺</option>
								<option value="2" ${(mtmyEveryoneSay.positionType == '2')?'selected="selected"':''}>技师</option>
								<option value="3" ${(mtmyEveryoneSay.positionType == '3')?'selected="selected"':''}>商品</option>
							</select>
							<form:input path="name" htmlEscape="false" maxlength="50" class=" form-control input-sm" placeholder="商品名或技师名或美容院名"/>
						</div>
					</form:form>
				<div class="pull-right">
							<button  class="btn btn-primary btn-rounded btn-outline btn-sm " onclick="search()" ><i class="fa fa-search"></i> 查询</button>
							<button  class="btn btn-primary btn-rounded btn-outline btn-sm " onclick="reset()" ><i class="fa fa-refresh"></i> 重置</button>
						</div>
                <table id="treeTable" class="table table-bordered table-hover table-striped">
                	<thead> 
                		<tr>
                			<th style="text-align: center;">序号</th>
                			<th style="text-align: center;">用户昵称</th>
                			<th style="text-align: center;">说说内容</th>
                			<th style="text-align: center;">点赞数</th>
                			<th style="text-align: center;">位置类型</th>
                			<th style="text-align: center;">时间</th>
                			<th style="text-align: center;">操作</th>
                		</tr>
                	</thead>
                    <tbody>
                    	<c:forEach items="${page.list}" var="mtmyEveryoneSay">
						<tr>
							<td style="text-align: center;">${mtmyEveryoneSay.mtmyEveryoneSayId}</td>
							<td style="text-align: center;">${mtmyEveryoneSay.userName}</td>
							<td style="text-align: center;">
								<!-- 当文本内容大于5个字符时，只显示其前五个字符 -->
							  	<c:choose>  
							         <c:when test="${fn:length(mtmyEveryoneSay.content) > 10}">  
							             <c:out value="${fn:substring(mtmyEveryoneSay.content, 0, 10)}..." />  
							         </c:when>  
							        <c:otherwise>  
							           <c:out value="${mtmyEveryoneSay.content}" />  
							        </c:otherwise>  
							    </c:choose>   
							</td>
							<td style="text-align: center;">${mtmyEveryoneSay.likeSum}</td>
							<td style="text-align: center;">
								<c:if test="${mtmyEveryoneSay.positionType == 1}">店铺</c:if>
								<c:if test="${mtmyEveryoneSay.positionType == 2}">技师</c:if>
								<c:if test="${mtmyEveryoneSay.positionType == 3}">商品</c:if>							
							</td>
							<td style="text-align: center;">
								<fmt:formatDate value="${mtmyEveryoneSay.createDate}"  pattern="yyyy-MM-dd HH:mm:ss" />
							</td>	
							<td style="text-align: center;">
								<shiro:hasPermission name="ec:everyoneSay:del">
									<a href="${ctx}/ec/everyoneSay/del?mtmyEveryoneSayId=${mtmyEveryoneSay.mtmyEveryoneSayId}" onclick="return confirmx('确认要删除吗？', this.href)" class="btn btn-danger btn-xs"><i class="fa fa-trash"></i> 删除</a>
								</shiro:hasPermission>
								<shiro:hasPermission name="ec:everyoneSay:view">
									<a href="#" onclick="openDialogView('${mtmyEveryoneSay.showName}', '${ctx}/ec/everyoneSay/form?mtmyEveryoneSayId=${mtmyEveryoneSay.mtmyEveryoneSayId}','600px', '550px')" class="btn btn-success btn-xs"><i class="fa fa-edit"></i> 查看详情</a>
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
   </div>
   <div class="loading"></div>
</body>
</html>