<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/webpage/include/taglib.jsp"%>
<html>
<head>
<title>用户寄存记录</title>
<meta name="decorator" content="default" />
<script type="text/javascript">
	var validateForm;
	var msg = "您真的确定要提交吗？\n\n请确认！"; 
	var newconsignNum;//新的寄存数量
	var consignNum;//寄存数量
	var takenNum;//取走数量
	function doSubmit(){//回调函数，在编辑和保存动作时，供openDialog调用提交表单。
		if(validateForm.form()){
			//先计算数量才能提交
			if($("#computType").val() == 1){
				top.layer.alert('请先计算数量!', {icon: 0, title:'提醒'});
				return false;
			}
			var result = number();
			if(result==true){
		 		if (confirm(msg)==true){ 
					$("#inputForm").submit();
				    return true;
			    }
			}
		 }else{ 
			 top.layer.alert('请输入正确取走数量', {icon: 0, title:'提醒'});
			  return false; 
		 } 
	}
	//提交的时候判断错误
	function number(){
		var takenNum = parseInt($("#takenNum").val());//取走数量
		var purchaseNum = parseInt($("#purchaseNum").val());//购买数量
		
		if((takenNum > purchaseNum)|| (takenNum > consignNum)){
			top.layer.alert('请输入正确取走数量', {icon: 0, title:'提醒'});
			return false;
		}
		return true;
	}
	
	function NumberCheck(t){
        var num = t.value;
        var re=/^\d*$/;
        if(!re.test(num)){
            isNaN(parseInt(num))?t.value=0:t.value=parseInt(num);
        }
    }
	
	function isnumber(num) {
		 var regu = /^[1-9]\d*|0$/; 
		 return regu.test(num);
	}
	
	jQuery.validator.addMethod("isNumber", function(value, element) {
	    var length = value.length;
	    var temp = /^[1-9]\d*|0$/;   
	    return this.optional(element) || (length >0 && temp.test(value));
	}, "输入正确数量");
	
	$(document).ready(function() {
		validateForm = $("#inputForm").validate({
			rules: {
			      takenNum: {
			    	  isNumber:true,
			            required : true,
			            number:true,
			            min:1
			      }
			  },
			  messages: {
			      takenNum: {
			    	  	isNumber:"请输入正确的数字",
			            required : "这是必填字段",
			            number:"请输入数字",
			            min:"输入正确数字"
			       }
			  }
		 });
	});
	//计算数量
	function compute(){
		consignNum = "${consign.consignNum}";//寄存数量
		takenNum = $("#takenNum").val();//取走数量
		newconsignNum = parseInt(consignNum)-parseInt(takenNum);//新的寄存数量
		
		if(takenNum == 0 || newconsignNum < 0){
			top.layer.alert('请正确填写取走数量', {icon: 0, title:'提醒'});
		}else{
			$("#takenNum").attr("readonly",true);
			$("#consignNum").val(newconsignNum);
			$("#computType").val(0);
		}
	}
	//修改数量
	function updateCompute(){
		$("#takenNum").attr("readonly",false);
		$("#consignNum").val(consignNum);
		$("#computType").val(1);
	}
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
						<input type="hidden" name="franchiseeId" value="${franchiseeId}" />
						<table
							class="table table-bordered  table-condensed dataTables-example dataTable no-footer">
							<tbody>
								<tr>
									<td class="width-15 active">
										<label class="pull-right"><font color="red">*</font>所属店铺:</label>
									</td>
									<td>
										<sys:treeselect id="office" name="officeId" value="${consign.officeId}" labelName="officeName" labelValue="${consign.officeName}" title="店铺"
											url="/sys/office/treeData?type=2" hideBtn="true" cssClass="form-control input-sm" allowClear="true" notAllowSelectRoot="false" notAllowSelectParent="false" />
									</td>
									<td class="width-15 active">
										<label class="pull-right"><font color="red">*</font>商品名称:</label>
									</td>
									<td>
										<sys:treeselect id="goodsId" name="goodsId" value="${consign.goodsId}" labelName="goodsName" labelValue="${consign.goodsName}" title="商品名称"
											url="/ec/goods/treeAllData" cssClass="form-control required" allowClear="true" hideBtn="true" notAllowSelectParent="true" />
									</td>
								</tr>
								<tr>
									<td class="width-15 active">
										<label class="pull-right"><font color="red">*</font>购买数量:</label>
									</td>
									<td class="width-20">
										<input name="purchaseNum" id="purchaseNum" value="${consign.purchaseNum}" maxlength="50" class="form-control"  readonly = "readonly"  />
									</td>
									<td class="width-15 active">
										<label class="pull-right"><font color="red">*</font>取走数量:</label>
									</td>
									<td class="width-20">
										<input name="takenNum" id="takenNum" value="0" maxlength="50" class="form-control required" onblur="NumberCheck(this)" style="width: 150px" />
										<a href="#" id="compute" onclick="compute()">计算数量</a>
										<input type="hidden" id="computType" name="computType" value="1"/>
										<div id="updateCompute">
											<a href="#" id="compute" onclick="updateCompute()">修改数量</a>
										</div>
									</td>
								</tr>
								<tr>
									<td class="width-15 active">
										<label class="pull-right"><font color="red">*</font>寄存数量:</label>
									</td>
									<td class="width-20">
										<input name="consignNum" id="consignNum" value="${consign.consignNum}" maxlength="50" class="form-control required"  readonly = "readonly"  />
									</td>
									<td class="width-15 active">
										<label class="pull-right"><font color="red">*</font>寄存时间:</label>
									</td>
									<td class="width-20" colspan="3">
										<input id="begtime" name="createDate" type="text" maxlength="50" class="laydate-icon form-control layer-date input-sm "
											value="<fmt:formatDate value="${consign.createDate}" pattern="yyyy-MM-dd HH:mm:ss"/>" style="width: 185px;" placeholder="开始时间" required="required" readonly = "readonly"  />
									</td>
								</tr>
								<tr>
									<td class="width-15 active">
										<label class="pull-right"><font color="red"></font>备注:</label>
									</td>
									<td class="width-20" colspan="3">
										<textarea id="remark" name="remark" rows="10" cols="40">${consign.remark}</textarea>
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