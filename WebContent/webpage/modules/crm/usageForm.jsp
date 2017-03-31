<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/webpage/include/taglib.jsp"%>
<html>
<head>
<title>产品使用记录</title>
<meta name="decorator" content="default" />
	<link href="${ctxStatic}/layer-v2.0/layer/skin/layui.css" type="text/css" rel="stylesheet">

<script type="text/javascript">
	var validateForm;
	function doSubmit() {//回调函数，在编辑和保存动作时，供openDialog调用提交表单。
		if(validateForm.form()){
			$("#inputForm").submit();
	    	return true;
    	}
    	return false;
	}
	$(document).ready(function() {
		validateForm = $("#inputForm").validate({
			rules: {
				  goodsName: {
			            required : true,
			      },
			      usageNum: {
			        required: true,
			        min:1,
			      }
			  },
			  messages: {
				  goodsName: {
			            required : "请选择商品",
			       },
			      usageNum: {
			        	 required: "请输入使用数量",
			      }
			  }
	 });
		
		var start = {
			elem : '#begtime',
			format : 'YYYY-MM-DD',
			max : $("#endtime").val(), //最大日期
			istime : false, //是否显示时间
			isclear : true, //是否显示清除
			istoday : true, //是否显示今天
			issure : true, //是否显示确定
			festival : true, //是否显示节日
			choose : function(datas) {
				end.min = datas; //开始日选好后，重置结束日的最小日期
				end.start = datas //将结束日的初始值设定为开始日
			}
		};
		var end = {
			elem : '#endtime',
			format : 'YYYY-MM-DD',			
			min : $("#begtime").val(),
			istime : false,
			isclear : true,
			istoday : true,
			issure : true,
			festival : true,
			choose : function(datas) {
				start.max = datas; //结束日选好后，重置开始日的最大日期
			}
		};
		laydate(start);
		laydate(end);
	});
</script>
</head>
<body>
	<div class="wrapper wrapper-content">
		<div class="ibox">
			<div class="ibox-title">
				<h5>产品使用记录</h5>
			</div>
			<div class="ibox-content">
				<sys:message content="${message}" />
				<div class="clearfix">
					<form id="inputForm" action="${ctx}/crm/goodsUsage/save" method="post" class="form-horizontal">
						<input type="hidden" name="usageId"
							value="${goodsUsage.usageId}" /> <input type="hidden"
							name="userId" value="${userId}" />
						<table
							class="table table-bordered  table-condensed dataTables-example dataTable no-footer">
							<tbody>
								<tr>
									<td class="width-15 active"><label class="pull-right"><font
											color="red">*</font>商品名称:</label></td>
									<td class="width-15" colspan="3"><sys:treeselect
											id="goodsId" name="goodsId" value="${goodsUsage.goodsId}"
											labelName="goodsName" labelValue="${goodsUsage.goodsName}"
											title="商品名称" url="/ec/goods/treeAllData"
											cssClass="form-control required" allowClear="true"
											notAllowSelectParent="true"/></td>
									<td class="width-15 active"><label class="pull-right"><font
											color="red">*</font>购买数量:</label></td>
									<td class="width-15" colspan="3"><input name="usageNum" id="usageNum"
										value="${goodsUsage.usageNum}" maxlength="50" 
										class="form-control required" /></td>
								</tr>
								<tr>
									<td class="width-15"><label class="pull-right"><font
											color="red">*</font>开始使用日期:</label></td>
									<td class="width-20" colspan="3"><input id="begtime"
										name="startDate" type="text" maxlength="50" 
										class="laydate-icon form-control layer-date input-sm required"
										value="<fmt:formatDate value="${goodsUsage.startDate}" pattern="yyyy-MM-dd"/>"
										style="width: 185px;" placeholder="开始时间" /></td>
									<td class="width-15"><label class="pull-right"><font
											color="red">*</font>结束使用日期:</label></td>
									<td class="width-20" colspan="3"><input id="endtime"
										name="endDate" type="text" maxlength="50" 
										class="laydate-icon form-control layer-date input-sm required"
										value="<fmt:formatDate value="${goodsUsage.endDate}" pattern="yyyy-MM-dd"/>"
										style="width: 185px;" placeholder="结束时间" /></td>
								</tr>
								<tr>
									<td class="width-15 active"><label class="pull-right"><font
											color="red"></font>使用效果:</label></td>
									<td class="width-20" colspan="6"><textarea id="effection"
											name="effection" rows="4" cols="60">${goodsUsage.effection}</textarea>
									</td>
								</tr>
								<tr>
									<td class="width-15 active"><label class="pull-right"><font
											color="red"></font>用户反馈:</label></td>
									<td class="width-20" colspan="6"><textarea id="feedback"
											name="feedback" rows="4" cols="60">${goodsUsage.feedback}</textarea>
									</td>
								</tr>
								<tr>
									<td class="width-15 active"><label class="pull-right"><font
											color="red"></font>备注:</label></td>
									<td class="width-20" colspan="6"><textarea id="remark"
											name="remark" rows="4" cols="60">${goodsUsage.remark}</textarea>
									</td>
								</tr>
							</tbody>
						</table>
					</form>
				</div>
			</div>
		</div>
	</div>
</body>
</html>