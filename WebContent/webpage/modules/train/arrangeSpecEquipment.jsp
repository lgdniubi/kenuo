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
	    .column { width: 100%; height: 500px; background: ; padding: 10px;}
	    #treeTable td{cursor:pointer;}
	    #treeTable .isNo{cursor:not-allowed;}
    </style>
    <script type="text/javascript" src="${ctxStatic}/jquery.scrollbar/optiscroll.js"></script>
    <script type="text/javascript">
    	function save(){
    		$("#searchForm").submit();
    	}
    </script>
</head>
<body>
	<div class="ibox-content">
		<sys:message content="${message}"/>
		<div class="warpper-content">
			<div class="ibox">
				<div class="ibox-title">
					<h5>特殊设备排班管理</h5>
				</div>
				<div class="ibox-content">
					<!-- 工具栏 -->
					<div class="row">
						<div class="col-sm-12">
							<div id='workState'>
								<input type="button" value='班' id='goWork' class="btn btn-default">&nbsp;
								<input type="button" value='修' id='holiday' class="btn btn-default">&nbsp;
								<input type="button" value='清' id='delWork' class="btn btn-default"> &nbsp;
								<shiro:hasPermission name="train:arrange:save">
									<input type="button" onclick="save()"  class="btn btn-default" value="保存">
								</shiro:hasPermission>
							</div>
						</div>
					</div>
					<form:form id="searchForm" action="${ctx}/train/arrange/saveEquipment" method="post" class="" style="padding-top: 10px;">
						<input type="hidden" value="${officeId }" id="nowOfficeId" name="nowOfficeId"> <!-- 用于重定向 -->
						<input type="hidden" value="${officeName }" id="nowOfficeName" name="nowOfficeName"> <!-- 用于重定向 -->
						<input id="searcTime" name="searcTime" value="${searcTime }" type="hidden">  <!-- 用于区分本月还是下月 -->
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
				                            	${ArrangeBeautician.equipmentName }
				                            	<input value="${ArrangeBeautician.equipmentId }" name="equipmentIds" type="hidden">
				                            </td>
				                            <c:forEach items="${ArrangeBeautician.arrangeEquipments}" var="arrangeEquipments" varStatus="status" end="31">
					                            <c:if test="${arrangeMaxDay < status.index+1 and ArrangeBeautician.delFlag == '0'}">
					                            	<c:choose>
					                            		<c:when test="${officeId eq arrangeEquipments.shopId }">
					                            			<td class="isYes">
						                            			<input id="${ArrangeBeautician.equipmentId }${status.index+1 }" name="${ArrangeBeautician.equipmentId }${status.index+1 }" value="${arrangeEquipments.shopId }" type="hidden">
						                            			<span>班</span>
						                            		</td>
					                            		</c:when>
					                            		<c:when test="${arrangeEquipments.shopId == '1' or arrangeEquipments.shopId == '2' }">
					                            			<td class="isYes">
						                            			<input id="${ArrangeBeautician.equipmentId }${status.index+1 }" name="${ArrangeBeautician.equipmentId }${status.index+1 }" value="${arrangeEquipments.shopId }" type="hidden">
						                            			<span>${arrangeEquipments.shopName }</span>
						                            		</td>
					                            		</c:when>
					                            		<c:when test="${arrangeEquipments.shopId != '1' and arrangeEquipments.shopId != '2' and arrangeEquipments.shopId != '' and arrangeEquipments.shopId != null}">
					                            			<td class="isNo">
						                            			<input id="${ArrangeBeautician.equipmentId }${status.index+1 }" name="${ArrangeBeautician.equipmentId }${status.index+1 }" value="${arrangeEquipments.shopId }" type="hidden">
						                            			<span>${arrangeEquipments.shopName }</span>
						                            		</td>
					                            		</c:when>
					                            		<c:otherwise>
					                            			<td class="isYes">
						                            			<input id="${ArrangeBeautician.equipmentId }${status.index+1 }" name="${ArrangeBeautician.equipmentId }${status.index+1 }" value="${arrangeEquipments.shopId }" type="hidden">
						                            			<span></span>
						                            		</td>
					                            		</c:otherwise>
					                            	</c:choose>
		                            			</c:if>
		                            			<c:if test="${arrangeMaxDay >= status.index+1 or ArrangeBeautician.delFlag == '1'}">
					                            	<c:choose>
					                            		<c:when test="${officeId eq arrangeEquipments.shopId }">
					                            			<td class="isNo">
						                            			<input id="${ArrangeBeautician.equipmentId }${status.index+1 }" name="${ArrangeBeautician.equipmentId }${status.index+1 }" value="${arrangeEquipments.shopId }" type="hidden">
						                            			<span>班</span>
						                            		</td>
					                            		</c:when>
					                            		<c:when test="${arrangeEquipments.shopId == '1' or arrangeEquipments.shopId == '2' }">
					                            			<td class="isNo">
						                            			<input id="${ArrangeBeautician.equipmentId }${status.index+1 }" name="${ArrangeBeautician.equipmentId }${status.index+1 }" value="${arrangeEquipments.shopId }" type="hidden">
						                            			<span>${arrangeEquipments.shopName }</span>
						                            		</td>
					                            		</c:when>
					                            		<c:when test="${arrangeEquipments.shopId != '1' and arrangeEquipments.shopId != '2' and arrangeEquipments.shopId != '' and arrangeEquipments.shopId != null}">
					                            			<td class="isNo">
						                            			<input id="${ArrangeBeautician.equipmentId }${status.index+1 }" name="${ArrangeBeautician.equipmentId }${status.index+1 }" value="${arrangeEquipments.shopId }" type="hidden">
						                            			<span>${arrangeEquipments.shopName }</span>
						                            		</td>
					                            		</c:when>
					                            		<c:otherwise>
					                            			<td class="isNo">
						                            			<input id="${ArrangeBeautician.equipmentId }${status.index+1 }" name="${ArrangeBeautician.equipmentId }${status.index+1 }" value="${arrangeEquipments.shopId }" type="hidden">
						                            			<span></span>
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
				        var os1 = new Optiscroll(document.getElementById('os1'), { maxTrackSize: 20, preventParentScroll: true });
				    </script>
					<script type="text/javascript">
					    var wr = new Optiscroll(document.getElementById('m-wrapper'), { forceScrollbars: true });
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
				/* if($state != 'cencleWork'){
					$('#treeTable').addClass('editTd');
				}else{
					$('#treeTable').removeClass('editTd');
				} */
			});
			
			$('#treeTable .isYes').click(function(){
				var $this = $(this);
				if($state == 'goWork'){
					$this.find('span').text('班');
					$this.find('input[type="hidden"]').val($("#nowOfficeId").val());
				}else if($state == 'holiday'){
					$this.find('span').text('修');
					$this.find('input[type="hidden"]').val('1');
				}else if($state == 'delWork'){
					$this.find('span').text('');
					$this.find('input[type="hidden"]').val('');
				} 
			});
		});
	</script>
</body>
</html>