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
		if((consignNum> (purchaseNum-takenNum))){
			alert("输入正确寄存数量");
			return false;
		}
		return true;
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
				  officeName: {
			            required : true,
			      },
			      goodsName: {
			        required: true,
			      },
			      purchaseNum: {
			    	 isNumber:true,  
			         required: true,
			         number:true,
			         min:1
			      },
			      takenNum: {
			    	   isNumber:true,  
			            required : true,
			            number:true,
			            min:0
			      },
			      consignNum:{
			    	   isNumber:true,  
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
			    	  isNumber:"非负整数",    
			        required: "这是必填字段",
			        number:"请输入数字",
			        min:"大于零的数字"
			      },
			      takenNum: {
			    	  isNumber:"非负整数",
			            required : "这是必填字段",
			            number:"请输入数字",
			            min:"输入正确数字"
			       },
			       consignNum:{
			    	   isNumber:"非负整数",
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
			    format: 'YYYY-MM-DD hh:mm:ss',
			    event: 'focus',
			    istime: true,				//是否显示时间
			    isclear: true,				//是否显示清除
			    istoday: true,				//是否显示今天
			    issure: true,				//是否显示确定
			    festival: true,				//是否显示节日
			    choose: function(datas){
			         
			    }
			};
		laydate(start);
    });
	function NumberCheck(t){
        var num = t.value;
        var re=/^\d*$/;
        if(!re.test(num)){
            isNaN(parseInt(num))?t.value=0:t.value=parseInt(num);
        }
    }
	
	//计算数量
	function compute(){
		var purchaseNum = $("#purchaseNum").val();//购买数量
		var takenNum = $("#takenNum").val();//取走数量
		if (purchaseNum == null || purchaseNum == undefined || purchaseNum == '') { 
			top.layer.alert('请先填写购买数量!', {icon: 0, title:'提醒'});
		}else{
			$("#purchaseNum").attr("readonly",true);
			$("#takenNum").attr("readonly",true);
			$("#consignNum").val(parseInt(purchaseNum)-parseInt(takenNum));//寄存数量
			$("#computType").val(0);
		}
	}
	//修改数量
	function updateCompute(){
		$("#purchaseNum").attr("readonly",false);
		$("#takenNum").attr("readonly",false);
		$("#consignNum").val(0);
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
										<sys:treeselect id="office" name="officeId" value="${consign.officeId}" labelName="officeName" 
								      		labelValue="${consign.officeName}" title="店铺" url="/sys/office/treeData?type=2" 
								      		cssClass="form-control input-sm" allowClear="true" notAllowSelectRoot="false" 
								      		notAllowSelectParent="false" />
								      	</td>
									<td class="width-15 active">
										<label class="pull-right"><font color="red">*</font>商品名称:</label>
									</td>
									<td>
										<sys:treeselect id="goodsId" name="goodsId" value="${consign.goodsId}" labelName="goodsName" labelValue="${consign.goodsName}"
											title="商品名称" url="/ec/goods/treeGoodsData?franchiseeId=${franchiseeId}" cssClass="form-control required" allowClear="true" notAllowSelectParent="true" />
									</td>
								</tr>
								<tr>
									<td class="width-15 active">
										<label class="pull-right"><font color="red">*</font>购买数量:</label>
									</td>
									<td class="width-20">
										<input name="purchaseNum" id="purchaseNum" value="${consign.purchaseNum}" maxlength="50" onblur="NumberCheck(this)" class="form-control required" />
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
										<input name="consignNum" id="consignNum" value="${consign.consignNum}" maxlength="50" readonly = "readonly" onblur="NumberCheck(this)" class="form-control" />
									</td>
								    <td class="width-15 active" >
								    	<label class="pull-right"><font color="red">*</font>寄存时间:</label>
								    </td>
									<td class="width-20" colspan="3">
										<input id="begtime" name="createDate" type="text" maxlength="50" class="laydate-icon form-control layer-date input-sm "
											value="<fmt:formatDate value="${consign.createDate}" pattern="yyyy-MM-dd HH:mm:ss"/>" style="width:185px;" placeholder="开始时间" required="required"/>
									</td>
								</tr>
								<tr>
									<td class="width-15 active">
										<label class="pull-right"><font color="red"></font>备注:</label>
									</td>
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