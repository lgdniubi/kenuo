<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/webpage/include/taglib.jsp"%>
<html>
<head>
<title>用户详情</title>
<meta name="decorator" content="default" />
<meta name="viewport"
	content="width=device-width, initial-scale=1.0, minimum-scale=1.0, maximum-scale=1.0,user-scalable=no">
<!-- 引入layui.css -->
<link href="${ctxStatic}/layer-v2.0/layer/skin/layui.css" type="text/css" rel="stylesheet">
	
<style>
input[type="number"] {
   -moz-appearance: textfield;
}
input::-webkit-outer-spin-button,           
input::-webkit-inner-spin-button{
 -webkit-appearance: none !important;            
}
.col-intems{float:left;width:255px;height:35px;line-height:35px;margin:10px 10px 0 0;}
.col-intems-5{float:left;width:500px;height:35px;line-height:35px;margin:10px 10px 0 0;}
.col-item-1{float:left;width:95px;margin:0;}

</style>	
<script type="text/javascript">
	$(document).ready(function() {
		var officeId = $("#officeId").val(); 
		var beautyId =$("#beautyId").val();
		if(officeId !=''){
		    $("#officeButton").attr("disabled","disabled");//添加disabled属性
		    $("#beautyButton").attr("disabled","disabled");         
		}
	})
	function save() {
		//loading("正在提交，请稍候...");
		if($("#fieldset").attr("disabled")) {
			alert("未编辑");
		}else{
			if(validateForm.form()){
				$("#inputForm").submit();
				return true;
			}	
		}
	}
	function enableEdit()  {
		if($("#fieldset").attr("disabled")) {
			$("#fieldset").removeAttr("disabled");
		}
	}
	$(document).ready(function() {
		
		var weddingDay = {
			elem : '#weddingDay',
			format : 'YYYY-MM-DD',
			istime : false, //是否显示时间
			isclear : true, //是否显示清除
			istoday : true, //是否显示今天
			issure : true, //是否显示确定
			festival : true, //是否显示节日
			choose : function(datas) {
			}
		};
		var menstrualDate = {
			elem : '#menstrualDate',
			format : 'YYYY-MM-DD',
			istime : false, //是否显示时间
			isclear : true, //是否显示清除
			istoday : true, //是否显示今天
			issure : true, //是否显示确定
			festival : true, //是否显示节日
			choose : function(datas) {
			}
		};
		var birthday = {
			elem : '#birthday',
			format : 'YYYY-MM-DD',
			istime : false,
			isclear : true,
			istoday : true,
			issure : true,
			festival : true,
			choose : function(datas) {
			}
		};
		laydate(weddingDay);
		laydate(birthday);
		laydate(menstrualDate);
	});
	
	jQuery.validator.addMethod("isIdCardNo", function(value, element) {
	    var length = value.length;
	    var idCard = /^(\d{15}$|^\d{18}$|^\d{17}(\d|X|x))$/;       
	    return this.optional(element) || ((length == 15 || length == 18) && idCard.test(value));
	}, "请正确填写您的身份证号码");
	$(document).ready(function() {
		validateForm = $("#inputForm").validate({
			  rules: {
			      idCard: {
			    	  isIdCardNo:true,
			      }
			  },
			  messages: {
				  idCard: {
					  isIdCardNo: "请输入正确身份证号码",
			       }
			  }
	 	});
		
		//点击事件   所属店铺
		$("#officeButton").click(function() {
			// 是否限制选择，如果限制，设置为disabled
			if ($("#officeButton").hasClass("disabled")) {
				return true;
			}
			// 正常打开	
			top.layer.open({
						type : 2,
						area : [ '300px','420px' ],
						title : "选择店铺",
						ajaxData : {
							selectIds : $("#officeId").val()
						},
						content : "/kenuo/a/tag/treeselect?url="
								+ encodeURIComponent("/sys/office/treeData?type=2")
								+ "&module=&checked=&extId=&isAll=",
						btn : [ '确定', '关闭' ],
						yes : function(index,layero) { //或者使用btn1
							var tree = layero.find("iframe")[0].contentWindow.tree;//h.find("iframe").contents();
							var ids = [], names = [], nodes = [];
							if ("" == "true") {
								nodes = tree.getCheckedNodes(true);
							} else {
								nodes = tree.getSelectedNodes();
							}
							for (var i = 0; i < nodes.length; i++) {//
								ids.push(nodes[i].id);
								names.push(nodes[i].name);//
								break; // 如果为非复选框选择，则返回第一个选择  
							}
							$("#officeId").val(ids.join(",").replace(/u_/ig,""));
							$("#officeName").val(names.join(","));
							$("#officeName").focus();
							top.layer.close(index);
						},
						cancel : function(index) { //或者使用btn2
							//按钮【按钮二】的回调
						}
					});
	
		});
		//所属美容师美容师
		$("#beautyButton").click(function() {
			var officeId = $("#officeId").val();
			// 是否限制选择，如果限制，设置为disabled
			if ($("#beautyButton").hasClass("disabled")) {
				return true;
			}
			if (officeId == null|| officeId == "") {
				top.layer.alert('请先选择市场!', {icon : 0,title : '提醒'});
			} else {
				// 正常打开
				top.layer.open({
							type : 2,
							area : [ '300px','420px' ],
							title : "选择美容师",
							ajaxData : {
								selectIds : $("#beautyId").val()
							},
							content : "/kenuo/a/tag/treeselect?url="
									+ encodeURIComponent("/crm/user/beautyTree?officeId="
											+ officeId),
							btn : [ '确定', '关闭' ],
							yes : function(index,layero) {
								//或者使用btn1
								var tree = layero.find("iframe")[0].contentWindow.tree;//h.find("iframe").contents();
								var ids = [], names = [], nodes = [];
								if ("" == "true") {
									nodes = tree.getCheckedNodes(true);
								} else {
									nodes = tree.getSelectedNodes();
								}
								for (var i = 0; i < nodes.length; i++) {//
									if (nodes[i].isParent) {
										//top.$.jBox.tip("不能选择父节点（"+nodes[i].name+"）请重新选择。");
										//layer.msg('有表情地提示');
										top.layer.msg("不能选择父节点（"+ nodes[i].name+ "）请重新选择。",{icon : 0});
										return false;
									}//
									ids.push(nodes[i].id);
									names.push(nodes[i].name);//
									break; // 如果为非复选框选择，则返回第一个选择  
								}
								$("#beautyId").val(ids.join(",").replace(/u_/ig,""));
								$("#beautyName").val(names.join(","));
								$("#beautyName").focus();
								top.layer.close(index);
							},
							cancel : function(
									index) { //或者使用btn2
								//按钮【按钮二】的回调
							}
						});
				}
		});
	});
</script>
</head>
<body class="gray-bg">
	<div class="wrapper wrapper-content">
		<div class="ibox">
			<form id="inputForm" action="${ctx}/crm/user/saveDetail" method="post" class="form-horizontal">
				<fieldset id="fieldset" disabled="disabled">
					<sys:message content="${message}" />
					<!-- 查询条件 -->
					<div class="ibox-content">
						<div class="clearfix">
							<div class="nav">
								<div class="text-danger" style="margin:8px">
									<p class="text-primary">
										<span >${userDetail.nickname}</span>的客户档案--请注意保密
									</p>
								</div>
								<ul class="layui-tab-title">
									<li role="presentation" class="layui-this">
										<a href="${ctx}/crm/user/userDetail?userId=${userId}&franchiseeId=${franchiseeId}">基本资料</a>
									</li>
									<li role="presentation">
										<a href="${ctx}/crm/physical/skin?userId=${userId}&franchiseeId=${franchiseeId}">身体状况</a>
									</li>
									<li role="presentation">
										<a href="${ctx}/crm/schedule/list?userId=${userId}&franchiseeId=${franchiseeId}">护理时间表</a>
									</li>
									<li role="presentation">
										<a href="${ctx}/crm/orders/list?userId=${userId}&franchiseeId=${franchiseeId}">客户订单</a>
									</li>
									<li role="presentation">
										<a href="${ctx}/crm/coustomerService/list?userId=${userId}&franchiseeId=${franchiseeId}">售后</a>
									</li>
									<li role="presentation">
										<a href="${ctx}/crm/consign/list?userId=${userId}&franchiseeId=${franchiseeId}">物品寄存</a>
									</li>
									<li role="presentation">
										<a href="${ctx}/crm/goodsUsage/list?userId=${userId}&franchiseeId=${franchiseeId}">产品使用记录</a>
									</li>
									<li role="presentation">
										<a href="${ctx}/crm/user/account?userId=${userId}&franchiseeId=${franchiseeId}">账户总览</a>
									</li>
									<li role="presentation">
										<a href="${ctx}/crm/invitation/list?userId=${userId}&franchiseeId=${franchiseeId}">邀请明细</a>
									</li>
									<li role="presentation">
										<shiro:hasPermission name="crm:store:list">	
											<a href="${ctx}/crm/store/questionCrmList?mobile=${userDetail.mobile}&userId=${userId}&franchiseeId=${franchiseeId}&stamp=1">投诉咨询</a>
										</shiro:hasPermission>
									</li>
								</ul>
							</div>
						</div>
					</div>
					<div class="ibox-content">
						<div class="clearfix">
							<div class="row">
								<div class="col-md-8">
									<h3 style="color:blue">用戶基本资料</h3>
								</div>
							</div>
							<!--開始展示用戶基本資料 -->
							<input name="userId" value="${userId}" type="hidden" class="form-control">
							<input type="hidden" id="franchiseeId" name="franchiseeId" value="${franchiseeId}">
							<div class="col-intems">
								<label>客户昵称:</label> 
								<input value="${userDetail.nickname}" maxlength="50" name="nickname" style="width: 150px;" class="form-control " disabled="disabled"/>
							</div>
							<div class="col-intems">
								<label>客户性别:</label> 
								<select id="sex" name="sex" style="width: 150px;" class="form-control" disabled="disabled">
									<option value="0" <c:if test="${userDetail.sex=='0'}">selected="true"</c:if>>保密</option>
									<option value="1" <c:if test="${userDetail.sex=='1'}">selected="true"</c:if>>男</option>
									<option value="2" <c:if test="${userDetail.sex=='2'}">selected="true"</c:if>>女</option>
								</select>
							</div>
							<div class="col-intems">
								<label>出生日期:</label>
								<input id="birthday" name="birthday" type="text" style="width: 150px" value="<fmt:formatDate value='${userDetail.birthday}' pattern='yyyy-MM-dd'/>" class="form-control" disabled="disabled">
							</div>
							<div class="col-intems">
								<label>阴历生日:</label>
								<input id="lunar" name="lunarBirthday" value="${detail.lunarBirthday}" maxlength="50" style="width: 150px;" class="form-control">
							</div>
							<div class="col-intems">
								<label>客户星座:</label>
								<select id="constellation" name="constellation" style="width: 150px;" class="form-control">
									<option></option>
									<c:forEach items="${constellation}" var="con">
										<option value="${con.value}" <c:if test="${con.value eq detail.constellation}">selected="true"</c:if>>${con.label}</option>
									</c:forEach>
								</select>
							</div>
							<div class="col-intems">
								<label>真实姓名:</label>
								<!--隐藏userId的input框 -->
								<input name="remarkname" value="${userDetail.name}" maxlength="50" style="width: 150px;" class="form-control" readonly="readonly">
							</div>
							<div class="col-intems">
								<label>备   注   名:</label>
								<input name="name" value="${detail.name}" maxlength="50" style="width: 150px;" class="form-control" readonly="readonly">
							</div>
							<div class="col-intems">
								<label>客户性格:</label>
								<select id="character" name="character" style="width: 150px;" class="form-control">
									<option></option>
									<c:forEach items="${character}" var="cha">
										<option value="${cha.value}" <c:if test="${cha.value eq detail.character}">selected="true"</c:if>>${cha.label}</option>
									</c:forEach>
								</select>
							</div>
							<div class="col-intems">
								<label>客户年龄:</label>
								<input value="${detail.age}" maxlength="50" style="width: 150px;" class="form-control " readonly="readonly"/>
							</div>
							<div class="col-intems">
								<label>客户等级:</label>
								<input value="${userDetail.userLevel}" maxlength="50" style="width: 150px;" class="form-control " readonly />
							</div>
							<div class="col-intems">
								<label>婚姻状况:</label>
								<select id="isMarrige" name="isMarrige" style="width: 150px;" class="form-control">
									<option></option>
									<c:forEach items="${isMarrige}" var="con">
										<option value="${con.value}" <c:if test="${con.value eq detail.isMarrige}">selected="true"</c:if>>${con.label}</option>
									</c:forEach>
								</select>
							</div>
							<div class="col-intems">
								<label>结婚纪念:</label>
								<input id="weddingDay" name="weddingDay" type="text" class="form-control" value='${detail.weddingDay}' style="width: 150px;">
							</div>
							<div class="col-intems">
								<label>房产情况:</label>
								<select id="isEstate" name="isEstate" style="width: 150px;" class="form-control">
									<c:forEach items="${isEstate}" var="con">
										<option value="${con.value}" <c:if test="${con.value eq detail.isEstate}">selected="true"</c:if>>${con.label}</option>
									</c:forEach>
								</select>
							</div>
							<div class="col-intems">
								<label>客户类型:</label>
								<select id="isMember" name="isMember" style="width: 150px;" class="form-control">
									<option value="1" <c:if test="${detail.isMember=='1'}">selected="true"</c:if>>会员</option>
									<option value="0" <c:if test="${detail.isMember=='0'}">selected="true"</c:if>>非会员</option>
								</select>
							</div>
							<div class="col-intems">
								<label>汽车品牌:</label>
								<select id="carBrand" name="carBrand" style="width: 150px;" class="form-control">
									<option></option>
									<c:forEach items="${carBrand}" var="con">
										<option value="${con.value}" <c:if test="${con.value eq detail.carBrand}">selected="true"</c:if>>${con.label}</option>
									</c:forEach>
								</select>
							</div>
							<div class="col-intems">
								<label>子女人数:</label>
								<select id="children" name="children" style="width: 150px;" class="form-control">
									<option value="0" <c:if test="${detail.children=='0'}">selected="true"</c:if>>无</option>
									<option value="1" <c:if test="${detail.children=='1'}">selected="true"</c:if>>1</option>
									<option value="2" <c:if test="${detail.children=='2'}">selected="true"</c:if>>2</option>
									<option value="3" <c:if test="${detail.children=='3'}">selected="true"</c:if>>3</option>
									<option value="4" <c:if test="${detail.children=='4'}">selected="true"</c:if>>4</option>
								</select>
							</div>
							<div class="col-intems">
								<label>客户职业:</label>
								<select id="occupation" name="occupation" style="width: 150px;" class="form-control">
									<option></option>
									<c:forEach items="${occupation}" var="con">
										<option value="${con.value}" <c:if test="${con.value eq detail.occupation}">selected="true"</c:if>>${con.label}</option>
									</c:forEach>
								</select>
							</div>
							<div class="col-intems">
								<label>月 收 入:</label>
								<select id="income" name="income" style="width: 150px;" class="form-control">
									<option></option>
									<c:forEach items="${income}" var="con">
										<option value="${con.value}" <c:if test="${con.value eq detail.income}">selected="true"</c:if>>${con.label}</option>
									</c:forEach>
								</select>
							</div>
							<div class="col-intems">
								<label>例假日期:</label> 
								<input id="menstrualDate" name="menstrualDate" style="width: 150px;" value="<fmt:formatDate value="${detail.menstrualDate}" pattern="yyyy-MM-dd"/>" maxlength="50" class="form-control" />
							</div>
							<div class="col-intems">
								<label>例假天数:</label>
								<input value="${detail.menstrualPeroid}" style="width: 150px;" type="number" name="menstrualPeroid" class="form-control" />
							</div>
							<div class="col-intems">
								<label>身　　高:</label>
								<input name="height" value="${detail.height}" maxlength="50" type="number" min="0.0" step="0.01" style="width: 150px;" class="form-control " />
							</div>
							<div class="col-intems">
								<label>体　　重:</label>
								<input name="weight" value="${detail.weight}" maxlength="50" type="number" min="0.0" step="0.01" style="width: 150px;" class="form-control " /><br />
							</div>
							<div class="col-intems">
								<label style="float: left">所属店铺:</label>
								<input id="officeId" class="form-control" name="officeId" value="${detail.officeId}" type="hidden">
								<div class="input-group" style="width: 148px;margin-right: 10px">
									<input id="officeName" class=" form-control" name="officeName" readonly="readonly" value="${detail.officeName}" type="text">
									<span class="input-group-btn">
										<button id="officeButton" class="btn btn-primary " type="button"><i class="fa fa-search"></i></button>
									</span>
								</div>
							</div>
							<div class="col-intems">
								<label style="margin-left: -10px; float: left">所属美容师:</label>
								<div class="input-group" style="width: 148px; margin-right: 10px">
									<input id="beautyName" name="beautyName" readonly="readonly" type="text" class="form-control" value="${detail.beautyName}">
									<span class="input-group-btn">
										<button type="button" id="beautyButton" class="btn btn-primary  "><i class="fa fa-search"></i></button>
									</span>
									<input id="beautyId" name="beautyId" type="hidden" value="${detail.beautyId}">
								</div>
							</div>
							<div class="col-intems-5">
								<shiro:hasPermission name="crm:userSecret:view">
									<label style="float: left;">身份证号:</label>
									<input value="${detail.idCard}" name="idCard" id="idCard" style="width: 413px; float: left" type="text" class="form-control "/>
								</shiro:hasPermission>
							</div>
							<div class="col-intems-5">
								<label style="float: left">重大疾病:</label>
								<input value="${detail.criticalDiseases}" maxlength="50" name="criticalDiseases" style="width: 413px; float: left" class="form-control " /><br />
							</div>
						</div>
					</div>
					<div class="ibox-content">
						<div class="clearfix">
							<div class="row">
								<div class="col-md-8">
									<h3 style="color: blue">其他信息</h3><br>
								</div>
							</div>
							<div class="row" style="margin: 10px">
								<div class="col-item-1"><label>现用美容品牌:</label></div>
								<div class="col-md-6">
									<input value="${detail.usingBrand}" maxlength="50" style="width: 300px;" name="usingBrand" class="form-control " />
								</div>
							</div>
							<div class="row" style="margin: 10px">
								<div class="col-item-1"><label>兴 趣 爱 好:</label></div>
								<div class="col-md-6">
									<input value="${detail.intrest}" maxlength="50" style="width: 300px;" name="intrest" class="form-control " />
								</div>
							</div>
							<div class="row" style="margin: 10px">
								<div class="col-item-1"><label>客 户 忌 讳:</label></div>
								<div class="col-md-3">
									<input value="${detail.taboo}" maxlength="50" style="width: 300px;" name="taboo" class="form-control " />
								</div>
							</div>
							<div class="row" style="margin: 10px">
								<div class="col-item-1"><label>客 户 讨 厌:</label></div>
								<div class="col-md-6">
									<input name="hate" value="${detail.hate}" maxlength="50" style="width: 300px;" class="form-control " />
								</div>
							</div>
							<div class="row" style="margin: 10px">
								<div class="col-item-1"><label>备　　　注:</label></div>
								<div class="col-md-6">
									<input value="${detail.remark}" maxlength="80" name="remark"style="width: 300px;" class="form-control " />
								</div>
							</div>
						</div>
					</div>
					<div class="ibox-content">
						<div class="clearfix">
							<div class="row">
								<div class="col-md-8">
									<h3 style="color: blue">用户联系信息</h3>
								</div>
							</div>
							<div class="row" style="padding:10px;">
								<div class="col-intems">
									<label>手机号码:</label>
									<input value="${userDetail.mobile}" maxlength="50" style="width: 150px;" type="number"	class="form-control " readonly="readonly" />
								</div>
								<div class="col-intems" >
									<label>QQ号码:</label>
									<input name="qq" value="${info.qq}" maxlength="50" style="width: 150px;" type="number" min="0.0" step="1" class="form-control " />
								</div>
								<div class="col-intems" >
									<label>微信号码:</label>
									<input name="wechat" value="${info.wechat}" maxlength="50" style="width: 150px;" class="form-control " />
								</div>
								<div class="col-intems" >
									<label>邮件地址:</label>
									<input name="email" value="${info.email}" maxlength="50" style="width: 150px;" class="form-control " />
								</div>
									<div class="col-intems-5">
										<label>工作单位:</label>
										<input value="${info.companyName}" style="width: 320px;" name="companyName" class="form-control " />
									</div>
								<shiro:hasPermission name="crm:userSecret:view">
									<div class="col-intems-5">
										<label>客户住址:</label>
										<input value="${info.address}" style="width: 320px;" name="address" class="form-control " />
									</div>
								</shiro:hasPermission>
							</div>
						</div>
					</div>
				</fieldset>
			</form>
		</div>
		<div style="text-align:center;max-width:1400px;">
			<shiro:hasPermission name="crm:userInfo:edit">
				<button onclick="enableEdit()" type="button" style="margin-right:20px;" class="btn btn-info" id="enableEdit">编辑</button>
				<button onclick="save()" type="button" class="btn btn-success">保存</button>
			</shiro:hasPermission>
		</div>
	</div>

	<div class="wrapper wrapper-content">
		<div class="ibox">
			<!--用户联系信息 -->
			<div class="ibox-content">
				<div class="clearfix">
					<shiro:hasPermission name="crm:userInfo:edit">
						<a href="#" onclick="openDialogView('操作日志', '${ctx}/crm/user/logDetail?userId=${userId}&franchiseeId=${franchiseeId}&operatorType=1','850px','650px')" class="btn btn-info btn-xs" ><i class="fa fa-search-plus"></i> 操作日志</a>
					</shiro:hasPermission>
				</div>
			</div>
		</div>
	</div>
</body>
</html>
