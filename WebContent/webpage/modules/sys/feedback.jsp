<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/webpage/include/taglib.jsp"%>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="decorator" content="default"/>
    <meta name="viewport" content="width=device-width, initial-scale=1.0, minimum-scale=1.0, maximum-scale=1.0,user-scalable=no">
    <link rel="stylesheet" href="${ctxStatic}/train/css/exam.css">
	<link rel="stylesheet" href="${ctxStatic}/bootstrap-switch/lc_switch.css">
  	<script src="${ctxStatic}/bootstrap-switch/js/lc_switch.js" type="text/javascript"></script>
    <script type="text/javascript">
		function page(n,s){
			$("#pageNo").val(n);
			$("#pageSize").val(s);
			$("#searchForm").submit();
	    	return false;
		}
		$(document).ready(function() {
		    var start = {
			    elem: '#beginDate',
			    format: 'YYYY-MM-DD',
			    event: 'focus',
			    max: $("#endDate").val(),   //最大日期
			    istime: false,				//是否显示时间
			    isclear: false,				//是否显示清除
			    istoday: false,				//是否显示今天
			    issure: false,				//是否显示确定
			    festival: true,				//是否显示节日
			    choose: function(datas){
			         end.min = datas; 		//开始日选好后，重置结束日的最小日期
			         end.start = datas 		//将结束日的初始值设定为开始日
			    }
			};
			var end = {
			    elem: '#endDate',
			    format: 'YYYY-MM-DD',
			    event: 'focus',
			    min: $("#beginDate").val(),
			    istime: false,
			    isclear: false,
			    istoday: false,
			    issure: false,
			    festival: true,
			    choose: function(datas){
			        start.max = datas; //结束日选好后，重置开始日的最大日期
			    }
			};
			laydate(start);
			laydate(end);
			$(".pd").lc_switch("否","是");
	    })
	    function nowreset(){
			 $("#content").val(""); //清空
			 $("#beginDate").val(""); //清空
			 $("#endDate").val(""); //清空
		}
		//弹窗修改
		/* function xiugai(mess,a,b,closed){
			top.layer.confirm(mess, {icon: 3, title:'系统提示'}, function(index){
				$.ajax({
					url : "${ctx}/sys/feedback/OneFeedback",
					type : "post",
					data:{fbId:a,fbStatus:b},
					dataType : "text",
					success :function(data) {
					//	window.location.reload();
						top.layer.close(index);
						}
		
				});	
			});
				
		} */
		function nowxiugai(a){
			$(".loading").show();//打开展示层
			$.ajax({
				url : "${ctx}/sys/feedback/update",
				type : "post",
				data:{fbId:a},
				dataType : "text",
				success :function(data) {
					$(".loading").hide();//隐藏展示层
					}
			});	
		}
	</script>
    <title>反馈管理</title>
</head>
<body>
    <div class="wrapper-content">
        <div class="ibox">
            <div class="ibox-title">
                <h5>反馈管理</h5>
            </div>
            <sys:message content="${message}"/>
            <div class="ibox-content">
                <div class="searcharea clearfix">
                    <form class="navbar-form navbar-left searcharea" id="searchForm" action="${ctx}/sys/feedback/feedback" role="search">
                    	<!-- 分页隐藏文本框 -->
	                    <input id="pageNo" name="pageNo" type="hidden" value="${page.pageNo}"/>
		 				<input id="pageSize" name="pageSize" type="hidden" value="${page.pageSize}"/>
                    	<table:sortColumn id="orderBy" name="orderBy" value="${page.orderBy}" callback="sortOrRefresh();"/><!-- 支持排序 -->
                        <div class="form-group">
                            <label>关键字：<input id="content" type="text" class="form-control" placeholder="Search" name="content" value="${content }" maxlength="10"></label>
                           时间范围：<input id="beginDate" name="beginDate" type="text" maxlength="20" class="laydate-icon form-control layer-date input-sm"
								value="<fmt:formatDate value="${feedback.beginDate}" pattern="yyyy-MM-dd"/>" readonly="readonly"/>
							<label>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;--&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</label>
							<input id="endDate" name="endDate" type="text" maxlength="20" class=" laydate-icon form-control layer-date input-sm"
								value="<fmt:formatDate value="${feedback.endDate}" pattern="yyyy-MM-dd"/>" readonly="readonly"/>
							<!--  
	                         &nbsp;&nbsp;&nbsp;未解决<label class="checked"><input type="radio" id="fbStatus" name="fbStatus" value="0" ></label>	
							 &nbsp;&nbsp;&nbsp;已解决<label class="checked"><input type="radio" id="fbStatus" name="fbStatus" value="1" ></label>
							 -->
                        </div>
                        <div class="pull-right">
							<button  class="btn btn-primary btn-rounded btn-outline btn-sm " onclick="search()" ><i class="fa fa-search"></i> 查询</button>
							<button  class="btn btn-primary btn-rounded btn-outline btn-sm " onclick="nowreset()" ><i class="fa fa-refresh"></i> 重置</button>
						</div>
                    </form>
                </div>
                <table id="contentTable" class="table table-striped table-bordered  table-hover table-condensed  dataTables-example dataTable no-footer">
                    <thead>
                        <tr>
                            <th width="120" style="text-align: center;">反馈用户</th>
                            <th style="text-align: center;">反馈内容</th>
                            <th width="200" style="text-align: center;">反馈时间</th>
                            <!--  
                            <th width="100">是否解决</th>
                            -->
                            <th width="100" style="text-align: center;">是否解决</th>
                        </tr>
                    </thead>
                    <tbody style="text-align: center;">
                    	<c:forEach items="${page.list}" var="feedback">
                    	<tr>
                    		<td>${feedback.name}</td>
                            <td>
                            <a onclick="openDialogView('查看反馈基本信息 ', '${ctx}/sys/feedback/feedbackForm?fbId=${feedback.fbId}','750px', '600px')">
								${feedback.content}
							</a> 
                            </td>
                            <td><fmt:formatDate value="${feedback.createtime}" pattern="yyyy-MM-dd HH:mm:ss"/></td>
                            <td>
                             	<shiro:hasPermission name="sys:feedback:update">
    							 <c:if test="${feedback.fbStatus == 0}">
									 <a href="javascript:nowxiugai('${feedback.fbId}');">
	    							 <input type="checkbox"  name="check" class="pd" checked="checked"  autocomplete="off"/>
	    							 </a>
								</c:if>
								<c:if test="${feedback.fbStatus  == 1}">
	                            	 <a href="javascript:nowxiugai('${feedback.fbId}');">
	    							 <input type="checkbox"  name="check" class="pd"  autocomplete="off"/>
	    							 </a>
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