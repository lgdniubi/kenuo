<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/webpage/include/taglib.jsp"%>
<html>
<head>
	<title>市场美容师排班管理</title>
	<meta name="decorator" content="default"/>
	<link rel="stylesheet" href="${ctxStatic}/ec/css/loading.css">
	<link rel="stylesheet" href="${ctxStatic}/jquery.scrollbar/optiscroll.css">
	<style type="text/css">
	    .column { width: 100%; height: 500px; background: #f6f2f2; padding: 10px;}
    </style>
    <script type="text/javascript" src="${ctxStatic}/jquery.scrollbar/optiscroll.js"></script>
	<script type="text/javascript">
		//重置表单
		function resetnew(){
			$("#name").val("");
			reset();
		}
		function show(officeId,day){
			$(".loading").show();
			$("#equipment").empty("");
			$("#beautician").empty("");
			if($("#month").val().length != 0 && officeId.length != 0 && day.length != 0){
				$.ajax({
					type : "POST",   
					url : "${ctx}/train/arrange/arrangeDetails?officeId="+officeId+"&day="+day+"&month="+$("#month").val(),
					dataType: 'json',
					success: function(data) {
						$(".loading").hide();//隐藏展示层
						if(data.STATUS == "OK"){
							//    需要修改
							var listB = "";
							var listE = "";
							$.each(data.LISTB,function(index,item){
								listB = listB+"<tr><td>"+item.name+"</td></tr>";
	    	    			});
							$.each(data.LISTE,function(index,item){
	    						 listE = listE+"<tr><td>"+item.equipmentName+"</td></tr>";
	    	    			});
							$("#equipment").append(listE);
							$("#beautician").append(listB);
							$('#myReply').modal('show');
						}else{
							$(".loading").hide();//隐藏展示层
							top.layer.alert('数据加载失败！', {icon: 0, title:'提醒'});
						}
					}
				});   
			}else{
				$(".loading").hide();//隐藏展示层
				top.layer.alert('数据加载失败！', {icon: 0, title:'提醒'});
			}
		}
		function arrange(){
			top.openTab("${ctx }/train/arrange/list?searchOfficeId="+$('#officeId').val()+"&searchOfficeName="+$('#officeName').val()+"&nextMonth=nextMonth","下月排班", false);
		}
	</script>
</head>
<body>
	<div class="ibox-content">
		<sys:message content="${message}"/>
		<div class="warpper-content">
			<div class="ibox">
				<div class="ibox-title">
					<h5>市场排班管理</h5>
				</div>
				<div class="ibox-content">
				<input id="month" value="${month }" type="hidden">
				<c:if test="${empty nextMonth}">
					<div class="searcharea clearfix">
						<form:form id="searchForm" action="${ctx}/train/arrange/list" method="post" class="navbar-form navbar-left searcharea">
							<div class="form-group">
								<label>请选择市场：<sys:treeselect id="office" name="searchOfficeId" value="${arrangeShop.searchOfficeId}" labelName="searchOfficeName" labelValue="${arrangeShop.searchOfficeName}"
									title="市场" url="/sys/office/treeData?isGrade=true" cssClass="form-control" notAllowSelectRoot="true" notAllowSelectParent="true"/></label>
								<input id="searcTime" name="searcTime" value="${arrangeShop.searcTime }" type="hidden"> <!-- 用于区分当前月还是下一个月 -->
							</div>
							<button type="button" class="btn btn-primary btn-rounded btn-outline btn-sm" onclick="search()">
								<i class="fa fa-search"></i> 搜索
							</button>
							<button type="button" class="btn btn-primary btn-rounded btn-outline btn-sm" onclick="resetnew()" >
								<i class="fa fa-refresh"></i> 重置
							</button>
						</form:form>
					</div>
					<!-- 工具栏 -->
					<div class="row">
						<div class="col-sm-12">
							<div class="pull-left">
								<shiro:hasPermission name="train:arrange:list">
									<c:if test="${arrangeShop.searchOfficeId != null && arrangeShop.searchOfficeId != ''}">
										<a href="#" onclick="arrange()" class="btn btn-white btn-sm" ><i class="fa fa-plus"></i> 下月排班</a>
									</c:if>
								</shiro:hasPermission>
							</div>
						</div>
					</div>
				</c:if>
					<div style="padding-top: 10px;">
						<div id="os1" class="optiscroll column mid-50">
							<table id="treeTable" class="table table-bordered table-hover table-striped" style="word-break:keep-all">
			                    <thead>
			                       <c:if test="${not empty calendarStr }">
							    		${calendarStr }
							    	</c:if>
			                    </thead>
			                    <tbody>
		                    	<c:forEach items="${lists}" var="ArrangeBeautician">
			                        <tr style="text-align: center;">
		                        		<td>
		                        			<shiro:hasPermission name="train:arrange:ArrangeBeautician">
		                        				<a href="#" onclick='top.openTab("${ctx }/train/arrange/ArrangeBeautician?officeId=${ArrangeBeautician.officeId }&officeName=${ArrangeBeautician.name}&searcTime=${fns:formatDateTime(arrangeShop.searcTime)}","市场美容师排班", false)' class="btn btn-success btn-xs" >美容师排班</a>
		                        			</shiro:hasPermission>
		                        			<shiro:hasPermission name="train:arrange:ArrangeSpecEquipment">
		                        				<a href="#" onclick='top.openTab("${ctx }/train/arrange/ArrangeSpecEquipment?officeId=${ArrangeBeautician.officeId }&officeName=${ArrangeBeautician.name}&searcTime=${fns:formatDateTime(arrangeShop.searcTime)}","市场设备排班", false)' class="btn btn-success btn-xs" >设备排班</a>
		                        			</shiro:hasPermission>
		                            	</td>
			                            <td>${ArrangeBeautician.name }</td>
			                            <c:forEach items="${ArrangeBeautician.arrangeShops}" var="arrangeShops" varStatus="status" end="31">
			                            	<td>
			                            		<c:if test="${arrangeShops.flag == 1}">
			                            			<a href="#" onclick="show('${ArrangeBeautician.officeId }',${status.index+1 })">班</a>
			                            		</c:if>
			                            	</td>
			                            </c:forEach>
			                        </tr>
		                        </c:forEach>
		                    </tbody>
		                </table>
	                </div>
                </div>
                <script type="text/javascript">
			        var os1 = new Optiscroll(document.getElementById('os1'), { maxTrackSize: 20, preventParentScroll: true });
			    </script>
				<script type="text/javascript">
				    var wr = new Optiscroll(document.getElementById('m-wrapper'), { forceScrollbars: true });
				</script>
                </div>
			</div>
		</div>
	</div>
	<!-- 弹出框 -->
    <div class="modal fade" id="myReply" tabindex="-1" role="dialog" aria-labelledby="myModalLabel">
        <div class="modal-dialog">
            <div class="modal-content">
                <div class="modal-header">
                    <button type="button" class="close" data-dismiss="modal" aria-label="Close"><span aria-hidden="true">&times;</span></button>
                    <h4 class="modal-title" id="myModalLabel">排班详情</h4>
                </div>                            
                <div class="modal-body">
                	<table id="treeTable" class="table table-bordered table-hover table-striped">
                		<thead>
	                   		<tr>
								<td style="text-align: center;">
									设备
								</td>	
								<td style="text-align: center;">
									美容师
								</td>                   		
	                   		</tr>
                   		</thead>
                   		<tbody style="text-align: center;">
	                   		<tr>
								<td>
									<table id="equipment" class="table">
									</table>
								</td>	
								<td>
									<table id="beautician" class="table">
									</table>
								</td>                   		
	                   		</tr>
                   		</tbody>
                	</table>
                </div>
                <div class="modal-footer">
                    <button type="button" class="btn btn-default" data-dismiss="modal">取消</button>
                </div>
            </div>
       </div>
    </div>
	<div class="loading"></div>
</body>
</html>