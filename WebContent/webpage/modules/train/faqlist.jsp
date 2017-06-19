<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/webpage/include/taglib.jsp"%>
<%@ include file="/webpage/include/icheck.jsp"%>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="decorator" content="default"/>
    <meta name="viewport" content="width=device-width, initial-scale=1.0, minimum-scale=1.0, maximum-scale=1.0,user-scalable=no">
    <link rel="stylesheet" href="${ctxStatic}/train/css/exam.css">
    <script type="text/javascript">
		function page(n,s){
			$("#pageNo").val(n);
			$("#pageSize").val(s);
			$("#searchForm").submit();
	    	return false;
	    }
		function test(num){
			document.getElementById("askId").value=num;
		}
	 //不能判定前后两个日期的大小
	 //	$(document).ready(function() {
	 //  外部js调用
	 //       laydate({
	 //           elem: '#beginDate', //目标元素。由于laydate.js封装了一个轻量级的选择器引擎，因此elem还允许你传入class、tag但必须按照这种方式 '#id .class'
	 //           event: 'focus' //响应事件。如果没有传入event，则按照默认的click
	 //       });
	 //       laydate({
	 //           elem: '#endDate', //目标元素。由于laydate.js封装了一个轻量级的选择器引擎，因此elem还允许你传入class、tag但必须按照这种方式 '#id .class'
	 //           event: 'focus' //响应事件。如果没有传入event，则按照默认的click
	 //       });    
	 //   })
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
	    })
	    function nowreset(){
			 $("#title").val(""); //清空
			 $("#beginDate").val(""); //清空
			 $("#endDate").val(""); //清空
		}
	    function huifu(){
	    	if($("#reviewText").val()!=''){
		    	loading('正在提交，请稍等...');
				$.ajax({
					 type : 'POST',
					 url : '${ctx}/train/faqlist/comment',
					 data:{'askId':$("#askId").val(),'content':$('#reviewText').val()},
					 success:function(index,data){
						//top.layer.close(index);//关闭对话框。
						closeTip();
						showTip('快速回复成功','success');
						//	页面刷新等待1秒 弹出回复成功框
						setTimeout(function(){
							sortOrRefresh()
							}, 1000);
					 }
				 })
	    	}else{
	    		$("#bt").show();
	    	}
		}
	  //使验证文字隐藏
		function a(){
			$("p").hide();
		}
	  
	  
		function changeTableVal(id,istop){
			$(".loading").show();//打开展示层
			$.ajax({
				type : "POST",
				url : "${ctx}/train/faqlist/updateistop?id="+id+"&istop="+istop,
				dataType: 'json',
				success: function(data) {
					$(".loading").hide(); //关闭加载层
					var status = data.STATUS;
					var istop = data.ISTOP;
					if("OK" == status){
						$("#ISTOP"+id).html("");//清除DIV内容	
						if(istop == '0'){
							//当前状态为【否】，则打开
							$("#ISTOP"+id).append("<img width='20' height='20' src='${ctxStatic}/ec/images/cancel.png' onclick=\"changeTableVal('"+id+"','1')\">");
						}else if(istop == '1'){
							//当前状态为【是】，则取消
							$("#ISTOP"+id).append("<img width='20' height='20' src='${ctxStatic}/ec/images/open.png' onclick=\"changeTableVal('"+id+"','0')\">");
						}
					}else if("ERROR" == status){
						alert(data.MESSAGE);
					}
				}
			});   
		}
	</script>
    <title>问答列表</title>
</head>
<body>
    <div class="wrapper-content">
        <div class="ibox">
            <div class="ibox-title">
                <h5>问答列表</h5>
            </div>
            <sys:message content="${message}"/>
            <div class="ibox-content">
                <div class="searcharea clearfix">
                    <form class="navbar-form navbar-left searcharea" id="searchForm" action="${ctx}/train/faqlist/faqlist" role="search">
                    
                    	<!-- 分页隐藏文本框 -->
	                    <input id="pageNo" name="pageNo" type="hidden" value="${page.pageNo}"/>
		 				<input id="pageSize" name="pageSize" type="hidden" value="${page.pageSize}"/>
                    	<table:sortColumn id="orderBy" name="orderBy" value="${page.orderBy}" callback="sortOrRefresh();"/><!-- 支持排序 -->
                    	
                        <div class="form-group">
                            <label>关键字：<input id="title" type="text" class="form-control" placeholder="搜索标题" name="title" value="${title }" maxlength="10"></label>
                             时间范围：<input id="beginDate" name="beginDate" type="text" maxlength="20" class="laydate-icon form-control layer-date input-sm"
								value="<fmt:formatDate value="${lessonAsks.beginDate}" pattern="yyyy-MM-dd"/>" readonly="readonly"/>
							<label>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;--&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</label>
							<input id="endDate" name="endDate" type="text" maxlength="20" class=" laydate-icon form-control layer-date input-sm"
								value="<fmt:formatDate value="${lessonAsks.endDate}" pattern="yyyy-MM-dd"/>" readonly="readonly"/>
                        </div>
                        <div class="pull-right">
							<button  class="btn btn-primary btn-rounded btn-outline btn-sm " onclick="search()" ><i class="fa fa-search"></i> 查询</button>
							<button  class="btn btn-primary btn-rounded btn-outline btn-sm " onclick="nowreset()" ><i class="fa fa-refresh"></i> 重置</button>
						</div>
                    </form>
	                <!-- 工具栏 -->
					<div class="row" style="padding-bottom: 5px;">
						<div class="col-sm-12">
	                        <div class="pull-left">
		                         <shiro:hasPermission name="train:faqlist:del">
		                                 	<table:delRow url="${ctx}/train/faqlist/deleteAll" id="treeTable"></table:delRow><!-- 删除按钮 -->
		                         </shiro:hasPermission>
	                        </div>
						</div>
					</div>
                </div>
                <table id="treeTable" class="table table-bordered table-hover table-striped">
                    <thead>
                        <tr>
                            <th width="120" style="text-align: center;"> <label for="i-checks"><input type="checkbox" name="" id="i-checks" class="i-checks"></label></th>
                            <th width="120" style="text-align: center;">提问者</th>
                            <th style="text-align: center;">问题标题</th>
                            <th style="text-align: center;">是否置顶</th>
                            <th width="120" style="text-align: center;">回答人数</th>
                            <th width="200" style="text-align: center;">发布时间</th>
                            <th width="200" style="text-align: center;">操作</th>
                        </tr>
                    </thead>
                    <tbody>
                    	<c:forEach items="${page.list}" var="LessonAsks">
	                        <tr style="text-align: center;">
	                            <td><input type="checkbox" id="${LessonAsks.askId }" name="ids" class="i-checks" ></td>
	                            <td>${LessonAsks.name }</td>
	                            <td><a href="#" onclick='top.openTab("${ctx}/train/faqlist/faqdetail?askId=${LessonAsks.askId}","问答详情", false)'>${LessonAsks.title }</a></td>
	                            <td style="text-align: center;" id="ISTOP${LessonAsks.askId}">
									<c:if test="${LessonAsks.isTop == 0}">
										<img width="20" height="20" src="${ctxStatic}/ec/images/cancel.png" onclick="changeTableVal('${LessonAsks.askId}','1')">
									</c:if>
									<c:if test="${LessonAsks.isTop == 1}">
										<img width="20" height="20" src="${ctxStatic}/ec/images/open.png" onclick="changeTableVal('${LessonAsks.askId}','0')">
									</c:if>
								</td>
	                            <td>
	                            	<!-- 2017年6月19日16:06:55 由于问答盖楼 后台注释回复评论 -->
	                            	<%-- <a href="#" onclick='top.openTab("${ctx}/train/reviewlist/reviewlist?askId=${LessonAsks.askId}","评论管理", false)'>${LessonAsks.num }人</a> --%>
									${LessonAsks.num }人
	                            </td>
	                            <td><fmt:formatDate value="${LessonAsks.createtime}" pattern="yyyy-MM-dd HH:mm:ss"/></td>
	                            <td>
	                            	<shiro:hasPermission name="train:faqlist:add">
	                                	<span data-toggle="modal" data-target="#myReply" class="btn btn-circle btn-xg"  onclick="test('${LessonAsks.askId }')"><i class="glyphicon glyphicon-edit"></i></span>
	                               	</shiro:hasPermission>
		                            <!--删除按钮  -->
		                            <shiro:hasPermission name="train:faqlist:del">
		                            	<a href="${ctx}/train/faqlist/deleteOneAsk?askId=${LessonAsks.askId }" onclick="return confirmx('要删除该问题吗？', this.href)" class="btn btn-circle btn-del"><i class=" glyphicon glyphicon-trash"></i></a>
		                            </shiro:hasPermission>
	                            </td>
	                        </tr>
                        </c:forEach>
                    </tbody>
                    <tfoot>
                        <tr>
                            <td colspan="20">
                                <div class="tfoot">
                                </div>
                                <!-- 分页代码 --> 
                               	<table:page page="${page}"></table:page>
                            </td>
                        </tr>
                    </tfoot>
                </table>
            </div>
            <!-- 弹出回复框 -->
            <div class="modal fade" id="myReply" tabindex="-1" role="dialog" aria-labelledby="myModalLabel">
                <form class="modal-dialog" role="document" id="reviewarea">
                	<!-- 获取问题ID -->
                	<input id="askId" name="askId" value="" type="hidden"/>
                    <div class="modal-content">
                        <div class="modal-header">
                            <button type="button" class="close" data-dismiss="modal" aria-label="Close"><span aria-hidden="true">&times;</span></button>
                            <h4 class="modal-title" id="myModalLabel">快速回复内容</h4>
                        </div>                            
                        <div class="modal-body">
                           <textarea name="content" id="reviewText"  class="reviewText" required  maxlength="500" onfocus="a()"></textarea>
                        </div>
                        <p id="bt" style="display:none"><font color="red" size="2">*  内容不能为空</font></p>
                        <div class="modal-footer">
                            <button type="button" class="btn btn-default" data-dismiss="modal">取消关闭</button>
                            <button type="button" onclick="huifu()" class="btn btn-primary">确定回复</button>
                        </div>
                    </div>
                </form>
            </div>
        </div>
    </div>
    <div class="loading"></div> 
    <script type="text/javascript">
	        $('#i-checks').click(function(){
	        	 if(this.checked){
	 	            $("#treeTable :checkbox").prop("checked", true);
	 	        }else{
	 	            $("#treeTable :checkbox").prop("checked", false);
	 	        }
	        });
	</script>
</body>
<script type="text/javascript" src="./js/bootstrap.js"></script>
<script type="text/javascript" src="./js/jquery.datetimepicker.js"></script>
<script type="text/javascript">
    $(function(){
        var $load = $('#loading');
        // 弹出框
        $('.btn-xg').click(function(){
            $('#myReply').modal('show');           
        });
    });
</script>
</html>