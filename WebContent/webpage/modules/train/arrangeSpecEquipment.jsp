<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/webpage/include/taglib.jsp"%>
<html>
<head>
	<title>特殊设备排班管理</title>
	<meta name="decorator" content="default"/>
	<link rel="stylesheet" href="${ctxStatic}/train/css/bootstrap.min.css">
	<link rel="stylesheet" href="${ctxStatic}/train/css/exam.css">
	<link rel="stylesheet" href="${ctxStatic}/ec/css/loading.css">
	<link rel="stylesheet" href="${ctxStatic}/jquery.scrollbar/optiscroll.css">
	<style type="text/css">
	    .column { width: 100%; height: 100%; background: #f6f2f2; padding: 10px;}
	    .optiscroll-content{height: 100%;}
		#treeTable td,#treeTable th{padding:8px 0;}
		#treeTable div{ word-wrap: break-word;}
	    #treeTable td{cursor:pointer;}
	    #treeTable .isNo{cursor:not-allowed;}
    </style>
    <script type="text/javascript" src="${ctxStatic}/jquery.scrollbar/optiscroll.js"></script>
    <script type="text/javascript">
    	function save(){
    		$(".loading").show();
    		$("#searchForm").submit();
    	}
    </script>
</head>
<body>
	<div class="ibox-content" style="height: 100%;">
		<sys:message content="${message}"/>
		<div class="warpper-content" style="height: 100%;">
			<div class="ibox" style="height: 100%;">
				<div class="ibox-title">
					<h5>特殊设备排班管理</h5>
				</div>
				<div class="ibox-content" style="height:100%">
					<!-- 工具栏 -->
					<div class="row">
						<div class="col-sm-12">
							<div id='workState'>
								<input type="button" value='班' id='goWork' class="btn btn-default" style="color: #5ec4ff">&nbsp;
								<input type="button" value='修' id='holiday' class="btn btn-default" style="color: #ff3000">&nbsp;
								<input type="button" value='清' id='delWork' class="btn btn-default"> &nbsp;
								<shiro:hasPermission name="train:arrange:save">
									<input type="button" onclick="save()"  class="btn btn-default" value="保存">
								</shiro:hasPermission>
							</div>
						</div>
					</div>
					<form:form id="searchForm" action="${ctx}/train/arrange/saveEquipment" method="post" class="" style="padding-top: 10px;height:100%;" enctype="multipart/form-data">
						<input type="hidden" value="${officeId }" id="nowOfficeId" name="nowOfficeId"> <!-- 用于重定向 -->
						<input type="hidden" value="${officeName }" id="nowOfficeName" name="nowOfficeName"> <!-- 用于重定向 -->
						<input id="searcTime" name="searcTime" value="${searcTime }" type="hidden">  <!-- 用于区分本月还是下月 -->
						<div id='os1' style="padding-top: 33px;height:calc(100% - 100px);position: relative;overflow: scroll;">
							<c:if test="${not empty calendarStr }">
								<table id="treeTable" class="osTop table table-bordered table-hover table-striped" style="width:100px;position:absolute;left:0;top:0;z-index:10;">
									<thead>
										<tr>
									 		<th style="text-align:center;"><div style="width: 100px;max-width: 100px;overflow: hidden;height: 17px;line-height:17px;" title="${officeName }">${officeName }</div></th>
									 	</tr>
									</thead>
								</table>
								<div id="osLeft" style="height:auto;width:100px;position:absolute;left:0;top:0;z-index:4;background:#fff;">
									<table id="treeTable" class="table table-bordered table-hover table-striped" style="word-break:keep-all;">
										<thead>
										 	<tr>
										 		<th style="text-align:center;"><div style="width: 100px;max-width: 100px;overflow: hidden;height: 17px;line-height:17px;" title="${officeName }">${officeName }</div></th>
										 	</tr>
										 </thead>
										 <tbody>
										  	<c:forEach items="${lists}" var="ArrangeBeautician">
						                        <tr style="text-align: center;">
						                            <td><div style="width: 100px;max-width: 100px;overflow: hidden;height: 17px;line-height:17px;" title="${ArrangeBeautician.equipmentName }">${ArrangeBeautician.equipmentName }</div></td>
						                        </tr>
					                        </c:forEach>
										 </tbody>
									</table>
								</div>
							</c:if>
							<table id="treeTable" class="table table-bordered table-hover table-striped" style="word-break:keep-all">
			                    <thead style="position:absolute;top:0;z-index:2;">
			                       <c:if test="${not empty calendarStr }">
							    		${calendarStr }
							    	</c:if>
			                    </thead>
			                    <tbody>
			                    	<c:forEach items="${lists}" var="ArrangeBeautician">
				                        <tr style="text-align: center;">
				                            <td>
				                            	<div style="width: 100px;max-width: 100px;overflow: hidden;height: 17px;line-height:17px;">
					                            	${ArrangeBeautician.equipmentName }
					                            	<input value="${ArrangeBeautician.equipmentId }" name="equipmentIds" type="hidden">
				                            	</div>
				                            </td>
				                            <c:forEach items="${ArrangeBeautician.arrangeEquipments}" var="arrangeEquipments" varStatus="status">
					                            <c:if test="${arrangeMaxDay < status.index+1 and ArrangeBeautician.delFlag == '0'}">
					                            <%-- <c:if test="${ArrangeBeautician.delFlag == '0'}"> --%>
					                            	<c:choose>
					                            		<c:when test="${officeId eq arrangeEquipments.shopId }">
					                            			<td class="isYes">
					                            				<div style="width: 100px;max-width: 100px;overflow: hidden;height: 17px;line-height:17px;">
							                            			<input id="${ArrangeBeautician.equipmentId }_${status.index+1 }" name="${ArrangeBeautician.equipmentId }_${status.index+1 }" value="${arrangeEquipments.shopId }" type="hidden" class="shopId">
							                            			<input id="isUpdate${ArrangeBeautician.equipmentId }_${status.index+1 }" name="isUpdate${ArrangeBeautician.equipmentId }_${status.index+1 }" value="0" type="hidden" class="isUpdate">
							                            			<span style="color: #5ec4ff">班</span>
						                            			</div>
						                            		</td>
					                            		</c:when>
					                            		<c:when test="${arrangeEquipments.shopId == '1'}">
					                            			<td class="isYes">
					                            				<div style="width: 100px;max-width: 100px;overflow: hidden;height: 17px;line-height:17px;" title="${arrangeEquipments.shopName }">
							                            			<input id="${ArrangeBeautician.equipmentId }_${status.index+1 }" name="${ArrangeBeautician.equipmentId }_${status.index+1 }" value="${arrangeEquipments.shopId }" type="hidden" class="shopId">
							                            			<input id="isUpdate${ArrangeBeautician.equipmentId }_${status.index+1 }" name="isUpdate${ArrangeBeautician.equipmentId }_${status.index+1 }" value="0" type="hidden" class="isUpdate">
							                            			<c:if test="${arrangeEquipments.shopId == '1'}">
							                            				<span style="color: #ff3000">${arrangeEquipments.shopName }</span>
							                            			</c:if>
						                            			</div>
						                            		</td>
					                            		</c:when>
					                            		<c:when test="${arrangeEquipments.shopId != '1' and arrangeEquipments.shopId != '' and arrangeEquipments.shopId != null}">
					                            			<td class="isNo">
					                            				<div style="width: 100px;max-width: 100px;overflow: hidden;height: 17px;line-height:17px;" title="${arrangeEquipments.shopName }">
							                            			<input id="${ArrangeBeautician.equipmentId }_${status.index+1 }" name="${ArrangeBeautician.equipmentId }_${status.index+1 }" value="${arrangeEquipments.shopId }" type="hidden" class="shopId">
							                            			<input id="isUpdate${ArrangeBeautician.equipmentId }_${status.index+1 }" name="isUpdate${ArrangeBeautician.equipmentId }_${status.index+1 }" value="0" type="hidden" class="isUpdate">
							                            			<span style="color: #999999">${arrangeEquipments.shopName }</span>
						                            			</div>
						                            		</td>
					                            		</c:when>
					                            		<c:otherwise>
					                            			<td class="isYes">
					                            				<div style="width: 100px;max-width: 100px;overflow: hidden;height: 17px;line-height:17px;">
							                            			<input id="${ArrangeBeautician.equipmentId }_${status.index+1 }" name="${ArrangeBeautician.equipmentId }_${status.index+1 }" value="${arrangeEquipments.shopId }" type="hidden" class="shopId">
							                            			<input id="isUpdate${ArrangeBeautician.equipmentId }_${status.index+1 }" name="isUpdate${ArrangeBeautician.equipmentId }_${status.index+1 }" value="0" type="hidden" class="isUpdate">
							                            			<span></span>
						                            			</div>
						                            		</td>
					                            		</c:otherwise>
					                            	</c:choose>
		                            			</c:if>
		                            			<c:if test="${arrangeMaxDay >= status.index+1 or ArrangeBeautician.delFlag == '1'}">
		                            			<%-- <c:if test="${ArrangeBeautician.delFlag == '1'}"> --%>
					                            	<c:choose>
					                            		<c:when test="${officeId eq arrangeEquipments.shopId }">
					                            			<td class="isNo">
					                            				<div style="width: 100px;max-width: 100px;overflow: hidden;height: 17px;line-height:17px;">
							                            			<input id="${ArrangeBeautician.equipmentId }_${status.index+1 }" name="${ArrangeBeautician.equipmentId }_${status.index+1 }" value="${arrangeEquipments.shopId }" type="hidden" class="shopId">
							                            			<input id="isUpdate${ArrangeBeautician.equipmentId }_${status.index+1 }" name="isUpdate${ArrangeBeautician.equipmentId }_${status.index+1 }" value="0" type="hidden" class="isUpdate">
							                            			<span style="color: #5ec4ff">班</span>
						                            			</div>
						                            		</td>
					                            		</c:when>
					                            		<c:when test="${arrangeEquipments.shopId == '1'}">
					                            			<td class="isNo">
					                            				<div style="width: 100px;max-width: 100px;overflow: hidden;height: 17px;line-height:17px;" title="${arrangeEquipments.shopName }">
							                            			<input id="${ArrangeBeautician.equipmentId }_${status.index+1 }" name="${ArrangeBeautician.equipmentId }_${status.index+1 }" value="${arrangeEquipments.shopId }" type="hidden" class="shopId">
							                            			<input id="isUpdate${ArrangeBeautician.equipmentId }_${status.index+1 }" name="isUpdate${ArrangeBeautician.equipmentId }_${status.index+1 }" value="0" type="hidden" class="isUpdate">
							                            			<c:if test="${arrangeEquipments.shopId == '1'}">
							                            				<span style="color: #ff3000">${arrangeEquipments.shopName }</span>
							                            			</c:if>
							                            		</div>
						                            		</td>
					                            		</c:when>
					                            		<c:when test="${arrangeEquipments.shopId != '1' and arrangeEquipments.shopId != '' and arrangeEquipments.shopId != null}">
					                            			<td class="isNo">
					                            				<div style="width: 100px;max-width: 100px;overflow: hidden;height: 17px;line-height:17px;" title="${arrangeEquipments.shopName }">
							                            			<input id="${ArrangeBeautician.equipmentId }_${status.index+1 }" name="${ArrangeBeautician.equipmentId }_${status.index+1 }" value="${arrangeEquipments.shopId }" type="hidden" class="shopId">
							                            			<input id="isUpdate${ArrangeBeautician.equipmentId }_${status.index+1 }" name="isUpdate${ArrangeBeautician.equipmentId }_${status.index+1 }" value="0" type="hidden" class="isUpdate">
							                            			<span style="color: #999999">${arrangeEquipments.shopName }</span>
						                            			</div>
						                            		</td>
					                            		</c:when>
					                            		<c:otherwise>
					                            			<td class="isNo">
					                            				<div style="width: 100px;max-width: 100px;overflow: hidden;height: 17px;line-height:17px;">
							                            			<input id="${ArrangeBeautician.equipmentId }_${status.index+1 }" name="${ArrangeBeautician.equipmentId }_${status.index+1 }" value="${arrangeEquipments.shopId }" type="hidden" class="shopId">
							                            			<input id="isUpdate${ArrangeBeautician.equipmentId }_${status.index+1 }" name="isUpdate${ArrangeBeautician.equipmentId }_${status.index+1 }" value="0" type="hidden" class="isUpdate">
							                            			<span></span>
						                            			</div>
						                            		</td>
					                            		</c:otherwise>
					                            	</c:choose>
		                            			</c:if>
				                            </c:forEach>
				                        </tr>
			                        </c:forEach>
			                    </tbody>
			                </table>
	                	</div>
                	</form:form>
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
	<div class="loading"></div>
	<script>
		$(function(){
			var $state = '';
			$('#workState input').click(function(){
				$state = $(this).attr('id');
				//高亮显示
				var $this = $(this);
		        $this.addClass('active').siblings().removeClass('active');
			});
			
			$('#treeTable .isYes').click(function(){
				var $this = $(this);
				if($state == 'goWork'){
					$this.find('span').text('班');
					$this.find('span').attr('style','color:#5ec4ff');
					$this.find('input[class="shopId"]').val($("#nowOfficeId").val());
					$this.find('input[class="isUpdate"]').val('1');
				}else if($state == 'holiday'){
					$this.find('span').text('修');
					$this.find('span').attr('style','color:#ff3000');
					$this.find('input[class="shopId"]').val('1');
					$this.find('input[class="isUpdate"]').val('1');
				}else if($state == 'delWork'){
					$this.find('span').text('');
					$this.find('input[class="shopId"]').val('');
					$this.find('input[class="isUpdate"]').val('1');
				} 
			});
			//自动清除提示
            setTimeout(() => {
                    $('#messageBox').remove();
            }, 2000);
		});
	</script>
</body>
</html>