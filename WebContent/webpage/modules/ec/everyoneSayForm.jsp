<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/webpage/include/taglib.jsp"%>
<html>
<head>
    <meta charset="utf-8">
    <meta name="decorator" content="default"/>
    <meta name="viewport" content="width=device-width, initial-scale=1.0, minimum-scale=1.0, maximum-scale=1.0,user-scalable=no">
    <link rel="stylesheet" href="${ctxStatic}/train/css/exam.css">
   	<!-- 日期控件 -->
    <script type="text/javascript" src="${ctxStatic}/My97DatePicker/WdatePicker.js"></script>
    <style>
		.talk_wrap{padding: 20px 25px;font-size:14px;background: #f6f6f6;border: 1px solid #c6c6c6;margin-bottom: 2px;}
		.talk_area .talk_inner{display:-moz-flex;display:flex;margin-bottom:10px; overflow:hidden;}
		.talk_area .talk_head{width:100px;line-height:30px;-moz-flex:1;flex:1;text-align:right;}
		.talk_area .talk_con{line-height:30px;-moz-flex:4;flex:4;text-align:left;}
		
	</style>
    <script>
	   function search(){//查询，页码清零
			$("#pageNo").val(0);
			$("#searchForm").submit();
	   		return false;
	   }
		function nowReset(){//重置，页码清零
			$("#pageNo").val(0);
			$("#title,#beginDate,#endDate").val("");
			$("#categoryId").val(0);
			$("#searchForm").submit();
	 	 }
		function page(n,s){//翻页
			$("#pageNo").val(n);
			$("#pageSize").val(s);
			$("#searchForm").submit();
			return false;
		}
		
		function delResponse(parentId,mtmyEveryoneSayId){
			if(confirm('确认要删除吗？')){
				$.ajax({
					type:"post",
					url:"${ctx}/ec/everyoneSay/delResponse?parentId="+parentId+"&mtmyEveryoneSayId="+mtmyEveryoneSayId,
					success:function(date){
						if(date=="success"){
							top.layer.alert('删除成功!', {icon: 1, title:'提醒'});
							window.location="${ctx}/ec/everyoneSay/form?mtmyEveryoneSayId="+parentId;
						}
						if(date=="error"){
							top.layer.alert('删除失败!', {icon: 2, title:'提醒'});
							window.location="${ctx}/ec/everyoneSay/form?mtmyEveryoneSayId="+parentId;
						}
									
					},
					error:function(XMLHttpRequest,textStatus,errorThrown){
								    
					}
				});
			 } 
		}
    </script>
</head>
<body>
	<div class="wrapper-content">
        <div class="ibox">
            <sys:message content="${message}"/>
				<div class="talk_wrap">
					<div class="talk_area">
						<div class="talk_inner">
							<div class="talk_head">说说用户：</div>
							<div class="talk_con">${mtmyEveryoneSayQuest.userName}　<fmt:formatDate value="${mtmyEveryoneSayQuest.createDate}" pattern="yyyy-MM-dd HH:mm:ss"/></div>
						</div>
						<div class="talk_inner">
							<div class="talk_head">说：</div>
							<div class="talk_con">${mtmyEveryoneSayQuest.content}</div>
						</div>
					</div>
				</div>
				<c:if test="${page.count != 0}"> 
					<div class="talk_wrap">
						<p></p>
						<form id="searchForm" action="${ctx}/ec/everyoneSay/form" method="post">
							<!-- 分页隐藏文本框 -->
			                <input id="pageNo" name="pageNo" type="hidden" value="${page.pageNo}"/>
				 			<input id="pageSize" name="pageSize" type="hidden" value="${page.pageSize}"/>
				 			<input id="mtmyEveryoneSayId" name="mtmyEveryoneSayId" type="hidden" value="${mtmyEveryoneSayQuest.mtmyEveryoneSayId}"/>
						</form>
						<c:forEach items="${page.list}" var="mtmyEveryoneSay">
				 			<div class="talk_area" style="border-bottom: 1px solid #000;">
					 			<div class="talk_inner">
									<div class="talk_head">回复用户：</div>
									<div class="talk_con">${mtmyEveryoneSay.userName}　<fmt:formatDate value="${mtmyEveryoneSay.createDate}" pattern="yyyy-MM-dd HH:mm:ss"/></div>
										<shiro:hasPermission name="ec:everyoneSay:delResponse">
											<button onclick="delResponse(${mtmyEveryoneSay.parentId},${mtmyEveryoneSay.mtmyEveryoneSayId})" class="btn btn-danger btn-xs"><i class="fa fa-trash"></i> 删除</button>
										</shiro:hasPermission>
								</div>
								<div class="talk_inner">
									<div class="talk_head">回：</div>
									<div class="talk_con">${mtmyEveryoneSay.content}</div>
								</div>	
							</div>
						</c:forEach>
					</div>
					<table:page page="${page}"></table:page>
				</c:if>
	    <div class="loading"></div>
    </div>
    </div>
</body>
</html>