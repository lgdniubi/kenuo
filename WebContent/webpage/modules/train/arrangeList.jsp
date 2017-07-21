<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/webpage/include/taglib.jsp"%>
<html>
<head>
	<title>市场美容师排班管理</title>
	<meta name="decorator" content="default"/>
	<link rel="stylesheet" href="${ctxStatic}/ec/css/loading.css">
	<link rel="stylesheet" href="${ctxStatic}/jquery.scrollbar/optiscroll.css">
	<style type="text/css">
		.column { width: 100%; height: 100%; background: #f6f2f2; padding: 10px;}
	    .optiscroll-content{height: 100%;}
		#treeTable td,#treeTable th{padding:8px 0;}
		#treeTable div{ word-wrap: break-word;}
		.ibox .open > .dropdown-menu {
		    left: 0!important;
		    right: 0;
		}
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
	<div class="ibox-content" style="height: 100%;">
		<sys:message content="${message}"/>
		<div class="warpper-content" style="height: 100%;">
			<div class="ibox" style="height: 100%;">
				<div class="ibox-title">
					<h5>市场排班管理</h5>
				</div>
				<div class="ibox-content" style="height:100%">
				<input id="month" value="${month }" type="hidden">
				<c:if test="${empty nextMonth}">
					<div class="searcharea clearfix">
						<form:form id="searchForm" action="${ctx}/train/arrange/list" method="post" class="navbar-form navbar-left searcharea">
							<div class="form-group">
								<label>请选择市场：<sys:treeselect id="office" name="searchOfficeId" value="${arrangeShop.searchOfficeId}" labelName="searchOfficeName" labelValue="${arrangeShop.searchOfficeName}"
									title="市场" url="/sys/office/treeData?isGrade=true" cssClass="form-control" notAllowSelectRoot="false" notAllowSelectParent="true"/></label>
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
				<div id='os1' style="padding-top: 33px;height:calc(100% - 100px);position: relative;overflow: scroll;">
					<c:if test="${not empty calendarStr }">
						<table id="treeTable" class="osTop table table-bordered table-hover table-striped" style="width:200px;position:absolute;left:0;top:0;z-index:10;">
							<thead>
								<tr>
									<th style="text-align:center;"><div style="width: 100px;max-width: 100px;overflow: hidden;height: 17px;line-height:17px;">操作</div></th>
							 		<th style="text-align:center;"><div style="width: 100px;max-width: 100px;overflow: hidden;height: 17px;line-height:17px;" title="${bazaarName }">${bazaarName }</div></th>
							 	</tr>
							</thead>
						</table>
						<div id="osLeft" style="height:auto;width:200px;position:absolute;left:0;top:0;z-index:4;background:#fff;">
							<table id="treeTable" class="table table-bordered table-hover table-striped" style="word-break:keep-all;">
								<thead>
								 	<tr>
								 		<th style="text-align:center;"><div style="width: 100px;max-width: 100px;overflow: hidden;height: 17px;line-height:17px;">操作</div></th>
								 		<th style="text-align:center;"><div style="width: 100px;max-width: 100px;overflow: hidden;height: 17px;line-height:17px;" title="${bazaarName }">${bazaarName }</div></th>
								 	</tr>
								 </thead>
								 <tbody>
								  	<c:forEach items="${lists}" var="ArrangeBeautician">
				                        <tr style="text-align: center;">
			                        		<td>
												<div class="btn-group" style="height: 17px;line-height:17px;">
													<button type="button" class="btn btn-success dropdown-toggle btn-xs" data-toggle="dropdown">操作<span class="caret"></span>
													</button>
													<ul class="dropdown-menu" role="menu">
														<shiro:hasPermission name="train:arrange:ArrangeBeautician">
															<li><a href="#" onclick='top.openTab("${ctx }/train/arrange/ArrangeBeautician?officeId=${ArrangeBeautician.officeId }&officeName=${ArrangeBeautician.name}&searcTime=${fns:formatDateTime(arrangeShop.searcTime)}","市场美容师排班", false)' class="btn" >美容师排班</a></li>
														</shiro:hasPermission>
														<shiro:hasPermission name="train:arrange:ArrangeSpecEquipment">
															<li><a href="#" onclick='top.openTab("${ctx }/train/arrange/ArrangeSpecEquipment?officeId=${ArrangeBeautician.officeId }&officeName=${ArrangeBeautician.name}&searcTime=${fns:formatDateTime(arrangeShop.searcTime)}","市场设备排班", false)' class="btn" >设备排班</a></li>
														</shiro:hasPermission>
													</ul>
												</div>
			                            	</td>
				                            <td><div style="width: 100px;max-width: 100px;overflow: hidden;height: 17px;line-height:17px;" title="${ArrangeBeautician.name }">${ArrangeBeautician.name }</div></td>
				                        </tr>
			                        </c:forEach>
								 </tbody>
							</table>
						</div>
					</c:if>
					<table id="treeTable" class="table table-bordered table-hover table-striped" style="word-break:keep-all;">
						<thead style="position:absolute;top:0;z-index:2;">
							<c:if test="${not empty calendarStr }">
								${calendarStr }
							</c:if>
						</thead>
						<tbody>
							<c:forEach items="${lists}" var="ArrangeBeautician">
								<tr style="text-align: center;">
									<td>
										<div class="btn-group" style="width: 100px;height: 17px;line-height:17px;"></div>
	                            	</td>
									<td><div style="width:100px;max-width: 100px;overflow: hidden;height: 17px;line-height:17px;">${ArrangeBeautician.name }</div></td>
									<c:forEach items="${ArrangeBeautician.arrangeShops}" var="arrangeShops" varStatus="status">
										<td>
											<div style="width: 100px;max-width: 100px;overflow: hidden;height: 17px;line-height:17px;">
												<c:if test="${arrangeShops.status == 1}">
													<a href="#" onclick="show('${ArrangeBeautician.officeId }',${status.index+1 })">班</a>
												</c:if>
											</div>
										</td>
									</c:forEach>
								</tr>
							</c:forEach>
						</tbody>
					</table>
				</div>
                <script type="text/javascript">
			      $(function(){
						$('#os1').scroll(function(){
			    		   var $this = $(this);
			    		   $(this).find('thead').css({'top':''+$this.scrollTop()+'px'})
			    		   $(this).find('#osLeft').css({'left':''+$this.scrollLeft()+'px'})
			    		   $(this).find('.osTop').css({'left':''+$this.scrollLeft()+'px','top':''+$this.scrollTop()+'px'})
			    	   })
			    	   
			       })
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