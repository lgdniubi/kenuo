<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/webpage/include/taglib.jsp"%>
<html>
<head>
<title>用户寄存记录</title>
<meta name="decorator" content="default" />
<script type="text/javascript">	
	var validateForm;
	var msg = "您真的确定要提交吗？\n\n请确认！"; 
	function doSubmit(){//回调函数，在编辑和保存动作时，供openDialog调用提交表单。
		if(validateForm.form()){
			var result = number();
			if(result==true){
		 		if (confirm(msg)==true){ 
					$("#inputForm").submit();
				    return true;
			    }
			}
		 }else{ 
			  return false; 
		 } 
	}
	//提交的时候判断错误
	function number(){
		var takenNum = parseInt($("#takenNum").val()); 
		var purchaseNum = parseInt($("#purchaseNum").val()); 
		var consignNum = parseInt($("#consignNum").val());
		if((takenNum > purchaseNum)|| (takenNum > consignNum)){
			alert("输入正确取走数量");
			return false;
		}
		if((consignNum> purchaseNum)){
			alert("输入正确寄存数量");
			return false;
		}
		return true;
	}
	$(document).ready(function() {
		validateForm = $("#inputForm").validate({
			rules: {
				  officeName: {
			            required : true,
			      },
			      goodsName: {
			        required: true,
			      },
			      purchaseNum: {
			        required: true,
			        number:true,
			        min:1
			      },
			      takenNum: {
			            required : true,
			            number:true,
			            min:0
			      },
			      consignNum:{
			    	    required:true,
			    	    number:true,
			    	    min:0
			      },
			      begtime:{
			    	    required:true,
			    	    dateISO:true	
			      },
			  },
			  messages: {
				  officeName: {
			            required : "这是必填字段",
			       },
			       goodsName: {
			        required: "这是必填字段",
			      },
			      purchaseNum: {
			        required: "这是必填字段",
			        number:"请输入数字",
			        min:"大于零的数字"
			      },
			      takenNum: {
			            required : "这是必填字段",
			            number:"请输入数字",
			            min:"输入正确数字"
			       },
			       consignNum:{
			    	  required : "这是必填字段",
			    	    number:"请输入数字",
			    	    min:"不能为负数"
			      },
			      begtime:{
			    	  required : "这是必填字段 ",
			      },
			  }
	 });
		var start = {
			    elem: '#begtime',
			    format: 'YYYY-MM-DD',
			    event: 'focus',
			    istime: false,				//是否显示时间
			    isclear: true,				//是否显示清除
			    istoday: true,				//是否显示今天
			    issure: true,				//是否显示确定
			    festival: true,				//是否显示节日
			    choose: function(datas){
			         
			    }
			};
		laydate(start);
    });
</script>
</head>
<body>
	<div class="wrapper wrapper-content">
		<div class="ibox">
			<div class="ibox-title">
				<h5>用户寄存</h5>
			</div>
			<div class="ibox-content">
				<sys:message content="${message}" />
				<div class="clearfix">
					<form id="inputForm" action="${ctx}/crm/consign/save" method="post"
						class="form-horizontal">
						<input type="hidden" name="consignId" value="${consign.consignId}" />
						<input type="hidden" name="userId" value="${userId}" />
						<table
							class="table table-bordered  table-condensed dataTables-example dataTable no-footer">
							<tbody>
								<tr>
								    <td class="width-15 active"><label class="pull-right"><font
											color="red">*</font>所属店铺:</label></td>
									<td><sys:treeselect id="office" name="officeId" value="${consign.officeId}" labelName="officeName" 
								      labelValue="${consign.officeName}" title="店铺" url="/sys/office/treeData?type=2" 
								      cssClass="form-control input-sm" allowClear="true" notAllowSelectRoot="false" 
								      notAllowSelectParent="false" /></td>
									<td class="width-15 active"><label class="pull-right"><font
											color="red">*</font>商品名称:</label></td>
									<td><sys:treeselect id="goodsId"
											name="goodsId" value="${consign.goodsId}"
											labelName="goodsName" labelValue="${consign.goodsName}"
											title="商品名称" url="/ec/goods/treeAllData"
											cssClass="form-control required" allowClear="true"
											notAllowSelectParent="true" /></td>
								</tr>
								<tr>
									<td class="width-15 active"><label class="pull-right"><font
											color="red">*</font>购买数量:</label></td>
									<td class="width-20"><input name="purchaseNum" id="purchaseNum"
										value="${consign.purchaseNum}" maxlength="50"
										class="form-control required" /></td>
								    <td class="width-15 active"><label class="pull-right"><font
											color="red">*</font>取走数量:</label></td>
									<td class="width-20"><input name="takenNum" id="takenNum"
										value="${consign.takenNum}" maxlength="50"
										class="form-control required" /></td>
								</tr>
								<tr>
									<td class="width-15 active"><label class="pull-right"><font
											color="red">*</font>寄存数量:</label></td>
									<td class="width-20"><input name="consignNum" id="consignNum"
										value="${consign.consignNum}" maxlength="50"
										class="form-control required" /></td>
								    <td class="width-15 active" ><label class="pull-right"><font
											color="red">*</font>寄存时间:</label></td>
									<td class="width-20" colspan="3"><input id="begtime" name="createDate" type="text" maxlength="50" class="laydate-icon form-control layer-date input-sm "
							value="<fmt:formatDate value="${consign.createDate}" pattern="yyyy-MM-dd"/>" style="width:185px;" placeholder="开始时间" required="required"/>
									</td>
								</tr>
								<tr>
									<td class="width-15 active"><label class="pull-right"><font
											color="red"></font>备注:</label></td>
									<td class="width-20" colspan="3">
									  <textarea id="remark" name="remark" rows="10" cols="40" >${consign.remark}</textarea>
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