<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/webpage/include/taglib.jsp"%>
<%@ include file="/webpage/include/icheck.jsp"%>
<html>
<head>
<title>选择项目</title>
<meta name="decorator" content="default" />
<link rel="stylesheet" type="text/css" href="${ctxStatic}/common/training.css">
<link rel="stylesheet" type="text/css" href="${ctxStatic}/ec/css/loading.css">
	<script type="text/javascript">
		$(document).ready(function(){
			var chooseRecId = "${chooseRecId}";
			if(chooseRecId.length > 0){
				$("input[type=radio][name=box][id="+chooseRecId+"]").attr("checked",true);
			}
		});
		
</script>
</head>
<body>
	<div class="wrapper wrapper-content">
		<div class="ibox">
			<!-- 主体内容 -->
			<div class="ibox-content">
				<sys:message content="${message}" />
				<!-- 主要内容展示 -->
				<table id="treeTable" class="table table-striped table-bordered table-hover table-condensed dataTables-example dataTable">
					<tbody>
						<c:forEach items="${lists}" var="orderGoods">
							<tr>
								<c:choose>
									<c:when test="${orderGoods.isreal == 2}">
									<td colspan="2">
									<table id="treeTable" class="table table-striped table-bordered  table-hover table-condensed  dataTables-example dataTable no-footer">
										<tr>
											<td colspan="2">
												${orderGoods.goodsname}
												<input id="sumPrice_${orderGoods.recid}" name="sumPrice" value="0" type="hidden"/>
												<input id="advanceFlag_${orderGoods.recid}" name="advanceFlag" value="${orderGoods.advanceFlag}" type="hidden"/>
											</td>
										</tr>
										<c:forEach items="${orderGoods.goodsCards}" var="goodsCard">
											<tr>
												<c:choose>
													<c:when test="${orderGoods.advanceFlag == 1}">
														<td style="text-align: center;" width="10px"><input type="radio" name="box" id="${goodsCard.recId}" value="${orderGoods.recid}"></td>
													</c:when> 
													<c:otherwise>
														<c:if test="${(goodsCard.serviceTimes - goodsCard.usedNum <= 0) || (orderGoods.surplusAmount - goodsCard.price < 0)}">
															<td style="text-align: center;" width="10px"><input type="radio" name="box1" id="${goodsCard.recId}" value="${orderGoods.recid}" disabled="disabled"></td>
														</c:if>
														<c:if test="${(goodsCard.serviceTimes - goodsCard.usedNum > 0) && (orderGoods.surplusAmount - goodsCard.price >= 0)}">
															<td style="text-align: center;" width="10px"><input type="radio" name="box" id="${goodsCard.recId}" value="${orderGoods.recid}" ></td>
														</c:if>
													</c:otherwise>
												</c:choose>
												<td >
													${goodsCard.goodsName}
													<input id="franchiseeId_${goodsCard.recId}" name="franchiseeId" value="${goodsCard.franchiseeId}" type="hidden"/>
													<input id="isreal_${goodsCard.recId}" name="isreal" value="${orderGoods.isreal}" type="hidden"/>
													<input id="remaintimes_${goodsCard.recId}" name="remaintimes" value="${orderGoods.remaintimes}" type="hidden"/>
													<input id="surplusAmount_${goodsCard.recId}" name="surplusAmount" value="${orderGoods.surplusAmount}" type="hidden"/>
													<input id="price_${goodsCard.recId}" name="price" value="${goodsCard.price}" type="hidden"/>
													<input id="serviceTimes_${goodsCard.recId}" name="serviceTimes" value="${goodsCard.serviceTimes}" type="hidden"/>
													<input id="usedNum_${goodsCard.recId}" name="usedNum" value="${goodsCard.usedNum}" type="hidden"/>
													<input id="goodsName_${goodsCard.recId}" name="goodsName" value="${goodsCard.goodsName}" type="hidden" />
													<input id="serviceMin_${goodsCard.recId}" name="serviceMin" value="${goodsCard.serviceMin}" type="hidden"/> 
													<input id="goodsId_${goodsCard.recId}" name="goodsId" value="${goodsCard.goodsId}" type="hidden"/>
													<input id="skillId_${goodsCard.recId}" name="skillId" value="${goodsCard.skillId}" type="hidden"/>
													<input id="labelId_${goodsCard.recId}" name="labelId" value="${goodsCard.labelId}" type="hidden"/>
													<input id="positionId_${goodsCard.recId}" name="positionId" value="${goodsCard.positionId}" type="hidden">
													<input id="positionIds_${goodsCard.recId}" name="positionIds" value="${goodsCard.positionIds}" type="hidden">
												</td>
											</tr>
										</c:forEach>
									</table>
									</td>
									</c:when>
									<c:when test="${orderGoods.isreal == 3}">
									<td colspan="2">
									<table id="treeTable" class="table table-striped table-bordered  table-hover table-condensed  dataTables-example dataTable no-footer">
										<tr>
											<td colspan="2">
												${orderGoods.goodsname}
											</td>
										</tr>
										<c:forEach items="${orderGoods.goodsCards}" var="goodsCard">
											<tr>
												<td style="text-align: center;" width="10px"><input type="radio" name="box" id="${goodsCard.recId}" value="${orderGoods.recid}"></td>
												<td>
													${goodsCard.goodsName}
													<input id="franchiseeId_${goodsCard.recId}" name="franchiseeId" value="${goodsCard.franchiseeId}" type="hidden"/>
													<input id="remaintimes_${goodsCard.recId}" name="remaintimes" value="${orderGoods.remaintimes}" type="hidden"/>												
													<input id="isreal_${goodsCard.recId}" name="isreal" value="${orderGoods.isreal}" type="hidden"/>
													<input id="sonsNum_${goodsCard.recId}" name="sonsNum" value="${fn:length(orderGoods.goodsCards)}" type="hidden"/>
													<input id="serviceTimes_${goodsCard.recId}" name="serviceTimes" value="${orderGoods.servicetimes}" type="hidden"/>
													<input id="goodsName_${goodsCard.recId}" name="goodsName" value="${goodsCard.goodsName}" type="hidden" />
													<input id="serviceMin_${goodsCard.recId}" name="serviceMin" value="${goodsCard.serviceMin}" type="hidden"/>
													<input id="goodsId_${goodsCard.recId}" name="goodsId" value="${goodsCard.goodsId}" type="hidden"/>
													<input id="skillId_${goodsCard.recId}" name="skillId" value="${goodsCard.skillId}" type="hidden"/>
													<input id="labelId_${goodsCard.recId}" name="labelId" value="${goodsCard.labelId}" type="hidden"/>
													<input id="positionId_${goodsCard.recId}" name="positionId" value="${goodsCard.positionId}" type="hidden">
													<input id="positionIds_${goodsCard.recId}" name="positionIds" value="${goodsCard.positionIds}" type="hidden">
												</td>
											</tr>
										</c:forEach>
									</table>
									</td>
									</c:when>
									<c:otherwise>
										<td style="text-align: center;" width="10px"><input type="radio" name="box" id="${orderGoods.recid}" value="${orderGoods.recid}"></td>
										<td>
											${orderGoods.goodsname}
											<input id="franchiseeId_${orderGoods.recid}" name="franchiseeId" value="${orderGoods.franchiseeId}" type="hidden"/>
											<input id="remaintimes_${orderGoods.recid}" name="remaintimes" value="${orderGoods.remaintimes}" type="hidden"/>
											<input id="isreal_${orderGoods.recid}" name="isreal" value="${orderGoods.isreal}" type="hidden"/>
											<input id="serviceTimes_${orderGoods.recid}" name="serviceTimes" value="${orderGoods.servicetimes}" type="hidden"/>
											<input id="goodsName_${orderGoods.recid}" name="goodsName" value="${orderGoods.goodsname}" type="hidden" />
											<input id="serviceMin_${orderGoods.recid}" name="serviceMin" value="${orderGoods.servicemin}" type="hidden"/>
											<input id="goodsId_${orderGoods.recid}" name="goodsId" value="${orderGoods.goodsid}" type="hidden"/>
											<input id="skillId_${orderGoods.recid}" name="skillId" value="${orderGoods.skillId}" type="hidden"/>
											<input id="labelId_${orderGoods.recid}" name="labelId" value="${orderGoods.labelId}" type="hidden"/>
											<input id="positionId_${orderGoods.recid}" name="positionId" value="${orderGoods.positionId}" type="hidden">
											<input id="positionIds_${orderGoods.recid}" name="positionIds" value="${orderGoods.positionIds}" type="hidden">
										</td>
									</c:otherwise>
								</c:choose>
							</tr>
						</c:forEach>
					</tbody>
				</table>
				<!-- 分页table -->
				<table:page page="${page}"></table:page>
				<div class="loading" id="loading"></div>
			</div>
		</div>
	</div>
</body>
</html>