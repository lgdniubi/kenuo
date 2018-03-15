<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/webpage/include/taglib.jsp"%>

<html>
<head>
<title>直播编辑</title>
<meta name="decorator" content="default" />
<!-- 内容上传 引用-->

<script type="text/javascript">
		var validateForm;
		function doSubmit(){//回调函数，在编辑和保存动作时，供openDialog调用提交表单。
		  if(validateForm.form()){
			  //公开到每天美耶,才可以选择可见范围
			  if($('input[name=isOpen]:checked').val() == 1){
				  //每天美耶可见范围
				  if($('input[name=isOpenLabel]:checked').length <= 0){
						top.layer.alert('|每天美耶可见范围|必选！', {icon: 0, title:'提醒'});
						return false;
				  }else{
					  var str=document.getElementsByName("isOpenLabel");
	
					  var ids = "";
					  for (i=0;i<str.length;i++){
					      if(str[i].checked == true){
					    	  ids = ids + str[i].value + ",";
					      }
					  }
					  $("#isOpenRole").val(ids);
				  }
			  }else{//非公开,需要默认可见范围的值为'0,'
				  var isRecommend = "${trainLiveAudit.isRecommend}";
				  //当该直播公开修改为非公开时,isRecommend=1是"已推荐",要修改为未推荐
				  if(isRecommend == 1){
					  $("#isRecommend").val("0");
				  }
				  $("#isOpenRole").val("0,");
			  }
			  $("#inputForm").submit();
			  return true;
		  }
	
		  return false;
		}
		
		//是否公开到每天美耶
		function isShow(show){
			if(show == 0){//非公开
				$("#isOpenRoleShow").hide();
				$("#isRecommendShow").hide();
			}else{//公开
				$("#isOpenRoleShow").show();
				$("#isRecommendShow").show();
			}
		}
		//每天美耶可见范围
		function queryIsOpen(value){
			var isOpen = $('input[name=isOpen]:checked').val();
			if($('input[name=isOpenLabel]:checked').length == 0){
				$("input[type=checkbox][name=isOpenLabel]").attr("disabled",false);
			}else{
				if(value == '0'){
					$("input[type=checkbox][name=isOpenLabel][id=notOpen]").attr("disabled",true);
				}else{
					$("input[type=checkbox][name=isOpenLabel][id=open]").attr("disabled",true);
				}				
			}
		}
	
		$(document).ready(function(){
			
			/* $("#reason").focus(); */
			validateForm = $("#inputForm").validate({
				submitHandler: function(form){
					//loading('正在提交，请稍等...');
					form.submit();
				},
				errorContainer: "#messageBox",
				errorPlacement: function(error, element) {
					$("#messageBox").text("输入有误，请先更正。");
					error.appendTo(element.parent());
					/* if (element.is(":checkbox")||element.is(":radio")||element.parent().is(".input-append")){
						error.appendTo(element.parent().parent());
					} else {
						error.insertAfter(element);
					} */
				}
			});
		
			//在ready函数中预先调用一次远程校验函数，是一个无奈的回避案。(刘高峰）
			//否则打开修改对话框，不做任何更改直接submit,这时再触发远程校验，耗时较长，
			//submit函数在等待远程校验结果然后再提交，而layer对话框不会阻塞会直接关闭同时会销毁表单，因此submit没有提交就被销毁了导致提交表单失败。
			/* $("#inputForm").validate().element($("#reason")); */
			
// 			laydate({
// 	            elem: '#userinfo.birthday', //目标元素。由于laydate.js封装了一个轻量级的选择器引擎，因此elem还允许你传入class、tag但必须按照这种方式 '#id .class'
// 	            event: 'focus' //响应事件。如果没有传入event，则按照默认的click
// 	        });
			
			var earningsRatio = $("#earningsRatio").val();
			var afterEarningsRatio = parseInt(earningsRatio);
			$("#earningsRatio").val(afterEarningsRatio);
			
			var isOpen = "${trainLiveAudit.isOpen}";//是否公开到每天美耶
			if(isOpen == 0){//非公开到每天美耶,可见范围不能选择
				$("#isOpenRoleShow").hide();
				$("#isRecommendShow").hide();
			}else{
				$("#isOpenRoleShow").show();
				$("#isRecommendShow").show();
				//每天美耶可见范围
				var isOpenIds = "${trainLiveAudit.isOpenRole}";
				if(isOpenIds.length > 0){
					var isOpenId = isOpenIds.split(",");
					for(q=0;q<isOpenId.length-1;q++){
						$("input[type=checkbox][name=isOpenLabel][value="+isOpenId[q]+"]").attr("checked",true);
						queryIsOpen(isOpenId[q]);
					}
				}
			}
		});
			
		//推荐直播按钮	
		function addRecommend(id,isRecommend) {
			//防止可见范围没选择,直接推荐
			if($('input[name=isOpenLabel]:checked').length <= 0){
				top.layer.alert('|每天美耶可见范围|必选！', {icon: 0, title:'提醒'});
				return false;
		    }
			$(".loading").show();//打开展示层
			$.ajax({
				type : "POST",
				url : "${ctx}/train/live/addRecommend?id="+id+"&isRecommend="+isRecommend,
				success: function(data) {
					$(".loading").hide(); //关闭加载层
					if(data=="OK"){
						top.layer.alert('推荐成功!', {icon: 1, title:'提醒'});
						window.location="${ctx}/train/live/liveFormMtmy?id="+id;
					}
					if(data=="ERROR"){
						top.layer.alert('推荐失败!', {icon: 2, title:'提醒'});
						window.location="${ctx}/train/live/liveFormMtmy?id="+id;
					}
				}
			});   
		}
			
</script>
</head>
<body>
	<form:form id="inputForm" modelAttribute="trainLiveAudit" action="${ctx}/train/live/saveFormMtmy" method="post" class="form-horizontal">
		<table class="table table-bordered  table-condensed dataTables-example dataTable no-footer">
			<tbody>
				<form:hidden path="id"/>
				<form:hidden path="isRecommend"/>
				<tr>
					<td width="30%"><label class="pull-right">是否公开到每天美耶:</label></td>
					<td>
						<c:if test="${trainLiveAudit.auditStatus==4}">
							<label><input id="isOpen" name="isOpen" type="radio" value="1" ${(trainLiveAudit.isOpen==1)?'checked="checked"':''} class="form" disabled="disabled" onclick="isShow(1)"/>是</label>
							<label><input id="isOpen" name="isOpen" type="radio" value="0" ${(trainLiveAudit.isOpen==0)?'checked="checked"':''} class="form" disabled="disabled" onclick="isShow(0)"/>否</label>
						</c:if> 
						<c:if test="${trainLiveAudit.auditStatus!=4}">
							<label><input id="isOpen" name="isOpen" type="radio" value="1" ${(trainLiveAudit.isOpen==1)?'checked="checked"':''} class="form" onclick="isShow(1)"/>是</label>
							<label><input id="isOpen" name="isOpen" type="radio" value="0" ${(trainLiveAudit.isOpen==0)?'checked="checked"':''} class="form" onclick="isShow(0)"/>否</label>
						</c:if> 
					</td>
				</tr>
				<tr id="isOpenRoleShow">
					<td><label class="pull-right">每天美耶可见范围:</label></td>	
					<td>
						<label><input type="checkbox" id="open" name="isOpenLabel" value="0" onclick="queryIsOpen('0')">公开</label>
						<c:forEach items="${list}" var="franchisee">
							<label><input type="checkbox" id="notOpen" name="isOpenLabel" value="${franchisee.id}" onclick="queryIsOpen('1')">${franchisee.name}</label>
						</c:forEach>
						<input id="isOpenRole" value="${trainLiveAudit.isOpenRole}" name="isOpenRole" type="hidden">
					</td>			
				</tr>
				<tr id="isRecommendShow">
					<td><label class="pull-right">是否推荐:</label></td>
					<td>
						<c:if test="${trainLiveAudit.isRecommend == 0}">
							<a href="#" onclick="addRecommend(${trainLiveAudit.id},${trainLiveAudit.isRecommend})" class="btn btn-success btn-xs"><i class="fa fa-edit"></i>推荐</a>
						</c:if>
						<c:if test="${trainLiveAudit.isRecommend == 1}">
							<a href="#" onclick="addRecommend(${trainLiveAudit.id},${trainLiveAudit.isRecommend})" class="btn btn-info btn-xs"><i class="fa fa-search-plus"></i>已推荐</a>
						</c:if>
					</td>
				</tr>
			</tbody>
		</table>
	</form:form>
</body>
</html>