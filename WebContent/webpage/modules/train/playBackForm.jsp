<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/webpage/include/taglib.jsp"%>

<html>
<head>
<title>回看查看</title>
<meta name="decorator" content="default" />
<!-- 内容上传 引用-->

<script type="text/javascript">
		var validateForm;
		function doSubmit(){//回调函数，在编辑和保存动作时，供openDialog调用提交表单。
		  if(validateForm.form()){
			  
			  $("#inputForm").submit();
			  return true;
		  }
	
		  return false;
		}
		
	
		$(document).ready(function(){
	
			
			$("#reason").focus();
			validateForm = $("#inputForm").validate({
				rules: {
					
				},
				messages: {
					
					
				},
				submitHandler: function(form){
				//	loading('正在提交，请稍等...');
					form.submit();
				},
				errorContainer: "#messageBox",
				errorPlacement: function(error, element) {
					$("#messageBox").text("输入有误，请先更正。");
					if (element.is(":checkbox")||element.is(":radio")||element.parent().is(".input-append")){
						error.appendTo(element.parent().parent());
					} else {
						error.insertAfter(element);
					}
				}
			});
		
			//在ready函数中预先调用一次远程校验函数，是一个无奈的回避案。(刘高峰）
			//否则打开修改对话框，不做任何更改直接submit,这时再触发远程校验，耗时较长，
			//submit函数在等待远程校验结果然后再提交，而layer对话框不会阻塞会直接关闭同时会销毁表单，因此submit没有提交就被销毁了导致提交表单失败。
			$("#inputForm").validate().element($("#reason"));
			
// 			laydate({
// 	            elem: '#userinfo.birthday', //目标元素。由于laydate.js封装了一个轻量级的选择器引擎，因此elem还允许你传入class、tag但必须按照这种方式 '#id .class'
// 	            event: 'focus' //响应事件。如果没有传入event，则按照默认的click
// 	        });
			
			
		});

		
</script>
</head>
<body>
<div class="wrapper wrapper-content">
	<div class="ibox">
	<form:form id="inputForm" modelAttribute="trainLiveAudit" action="${ctx}/train/live/save" method="post" class="form-horizontal">
		<table class="table table-bordered  table-condensed dataTables-example dataTable no-footer">
			<tbody>
				<tr>
					<td><label class="pull-right">申请人姓名:</label></td>
					<td><label>${trainLivePlayback.userName}</label>
					</td>
				</tr>
				<tr>
					<td><label class="pull-right">申请人描述:</label></td>
					<td><label>${trainLivePlayback.label}</label></td>
				</tr>
				<tr>
					<td><label class="pull-right">直播申请编码:</label></td>
					<td><label>${trainLivePlayback.auditId}</label></td>
				</tr>
				<tr>
					<td><label class="pull-right">回看编码:</label></td>
					<td><label>${trainLivePlayback.playbackId}</label></td>
				</tr>
				<tr>
					<td><label class="pull-right">回看主题:</label></td>
					<td><label>${trainLivePlayback.name}</label></td>
				</tr>
				<tr>
					<td><label class="pull-right">回看描述:</label></td>
					<td><label>${trainLivePlayback.desc}</label></td>
				</tr>
				<tr>
					<td><label class="pull-right">房间密码:</label></td>
					<td><label>${trainLivePlayback.playpass}</label></td>
				</tr>
				<tr>
					<td><label class="pull-right">直播时间:</label></td>
					<td><label>开始时间：<fmt:formatDate value="${trainLivePlayback.bengtime}" pattern="yyyy-MM-dd HH:mm:ss" />	结束时间：<fmt:formatDate value="${trainLivePlayback.endtime}" pattern="yyyy-MM-dd HH:mm:ss" /></label></td>
				</tr>
				<tr>
					<td><label class="pull-right">是否付费:</label></td>
					<td>
						<c:if test="${trainLivePlayback.isPay==1}">
							<label><input id="isPay" name="isPay" type="radio" value="1" checked="checked" class="form" />免费</label>
						</c:if> 
						<c:if test="${trainLivePlayback.isPay==2}">
							<label><input id="isPay" name="isPay" type="radio" value="2" checked="checked" class="form" />收费</label>
						</c:if> 
					</td>
				</tr>
				<tr>
					<td><label class="pull-right">直播封面图片:</label></td>
					<td><img alt="封面图片" src="${trainLivePlayback.imgurl}" style="height: 300px; width: 450px"></td>
				</tr>
				<tr>
					<td><label class="pull-right">收藏次数:</label></td>
					<td><label>${trainLivePlayback.collect}</label></td>
				</tr>
				<tr>
					<td><label class="pull-right">点赞次数:</label></td>
					<td><label>${trainLivePlayback.thumbup}</label></td>
				</tr>
				<tr>
					<td><label class="pull-right">播放次数:</label></td>
					<td><label>${trainLivePlayback.playNum}</label></td>
				</tr>
				<tr>
					<td><label class="pull-right">视频资源路径:</label></td>
					<td><label>${trainLivePlayback.downUrl}</label></td>
				</tr>
			</tbody>
		</table>

	</form:form>
	</div>
</div>	
</body>
</html>