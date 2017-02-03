<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/webpage/include/taglib.jsp"%>
<html>
<head>
	<title>市场美容师排班管理</title>
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
					<h5>${officeName }_美容师排班管理</h5>
				</div>
				<div class="ibox-content">
					<div class="row">
						<div class="col-sm-12">
							<div id='workState'>
								<input type="button" value='班' id='goWork' class="btn btn-default">&nbsp;
								<input type="button" value='假' id='holiday' class="btn btn-default">&nbsp;
								<input type="button" value='休' id='weekWork' class="btn btn-default">&nbsp;
								<input type="button" value='清' id='delWork' class="btn btn-default">&nbsp;
								<input type="button" onclick="save()"  class="btn btn-default" value="保存">
							</div>
						</div>
					</div>
					<!-- 工具栏 -->
					<div class="row">
						<div class="col-sm-12">
							<div class="pull-left">
								<c:if test="${nextMonth != 'nextMonth' }">
									<a href="#" onclick='top.openTab("${ctx }/train/arrange/ArrangeOrdinary?officeId=${officeId }&officeName=${officeName }&nextMonth=nextMonth","美容师排班", false)' class="btn btn-white btn-sm" ><i class="fa fa-plus"></i> 下月排班</a>
								</c:if>
							</div>
						</div>
					</div>
					<form:form id="searchForm" action="${ctx}/train/arrange/saveOrdinary" method="post" class="" style="padding-top: 10px;">
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
				                            	<c:choose>
				                            		<c:when test="${ArrangeBeautician.userOfficeId == officeId }">
						                            	${ArrangeBeautician.name }
				                            		</c:when>
				                            		<c:otherwise>
				                            			${ArrangeBeautician.name }<font color="red">(特)</font>
				                            		</c:otherwise>
				                            	</c:choose>
				                            	<input value="${ArrangeBeautician.userId }" name="userids" type="hidden">
				                            </td>
				                            <c:forEach items="${ArrangeBeautician.arrangeShops}" var="arrangeShops" varStatus="status" end="31">
				                            	<!-- 当前时间+2  <  日    可进行排班（1.当前时间之前排班不可操作  2.预约存在三天缓存） -->
					                            <c:if test="${arrangeMaxDay < status.index+1 and ArrangeBeautician.userOfficeId == officeId and ArrangeBeautician.delFlag == '0'}">
					                            	<c:choose>
					                            		<c:when test="${officeId eq arrangeShops.shopId }">
					                            			<td class="isYes">
						                            			<input id="${ArrangeBeautician.userId }${status.index+1 }" name="${ArrangeBeautician.userId }${status.index+1 }" value="${arrangeShops.shopId }" type="hidden">
						                            			<span>班</span>
						                            		</td>
					                            		</c:when>
					                            		<c:when test="${arrangeShops.shopId == '1' or arrangeShops.shopId == '2' }">
					                            			<td class="isYes">
						                            			<input id="${ArrangeBeautician.userId }${status.index+1 }" name="${ArrangeBeautician.userId }${status.index+1 }" value="${arrangeShops.shopId }" type="hidden">
						                            			<span>${arrangeShops.shopName }</span>
						                            		</td>
					                            		</c:when>
					                            		<c:when test="${arrangeShops.shopId != '1' and arrangeShops.shopId != '2' and arrangeShops.shopId != '' and arrangeShops.shopId != null}">
					                            			<td class="isNo">
						                            			<input id="${ArrangeBeautician.userId }${status.index+1 }" name="${ArrangeBeautician.userId }${status.index+1 }" value="${arrangeShops.shopId }" type="hidden">
						                            			<span>${arrangeShops.shopName }</span>
						                            		</td>
					                            		</c:when>
					                            		<c:otherwise>
					                            			<td class="isYes">
						                            			<input id="${ArrangeBeautician.userId }${status.index+1 }" name="${ArrangeBeautician.userId }${status.index+1 }" value="${arrangeShops.shopId }" type="hidden">
						                            			<span></span>
						                            		</td>
					                            		</c:otherwise>
					                            	</c:choose>
		                            			</c:if>
		                            			<!-- 当前时间+2  >=  日    可进行排班（1.当前时间之前排班不可操作  2.预约存在三天缓存） -->
		                            			<c:if test="${arrangeMaxDay >= status.index+1 or ArrangeBeautician.userOfficeId != officeId or ArrangeBeautician.delFlag == '1'}">
					                            	<c:choose>
					                            		<c:when test="${officeId eq arrangeShops.shopId }">
					                            			<td class="isNo">
						                            			<input id="${ArrangeBeautician.userId }${status.index+1 }" name="${ArrangeBeautician.userId }${status.index+1 }" value="${arrangeShops.shopId }" type="hidden">
						                            			<span>班</span>
						                            		</td>
					                            		</c:when>
					                            		<c:when test="${arrangeShops.shopId == '1' or arrangeShops.shopId == '2' }">
					                            			<td class="isNo">
						                            			<input id="${ArrangeBeautician.userId }${status.index+1 }" name="${ArrangeBeautician.userId }${status.index+1 }" value="${arrangeShops.shopId }" type="hidden">
						                            			<%-- <span>${arrangeShops.shopName }</span> --%>
						                            			<span></span>
						                            		</td>
					                            		</c:when>
					                            		<c:when test="${arrangeShops.shopId != '1' and arrangeShops.shopId != '2' and arrangeShops.shopId != '' and arrangeShops.shopId != null}">
					                            			<td class="isNo">
						                            			<input id="${ArrangeBeautician.userId }${status.index+1 }" name="${ArrangeBeautician.userId }${status.index+1 }" value="${arrangeShops.shopId }" type="hidden">
						                            			<%-- <span>${arrangeShops.shopName }</span> --%><!-- 当用户为特殊美容师时  除在本店铺上班外   在其他店铺上班不展示 -->
						                            			<span></span>
						                            		</td>
					                            		</c:when>
					                            		<c:otherwise>
					                            			<td class="isNo">
						                            			<input id="${ArrangeBeautician.userId }${status.index+1 }" name="${ArrangeBeautician.userId }${status.index+1 }" value="${arrangeShops.shopId }" type="hidden">
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
					$this.find('span').text('假');
					$this.find('input[type="hidden"]').val('2');
				}else if($state == 'weekWork'){
					$this.find('span').text('休');
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