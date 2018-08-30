<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/webpage/include/taglib.jsp"%>
<%@ page import="com.training.modules.sys.utils.ParametersFactory"%>
<%
	String uploadURL = ParametersFactory.getMtmyParamValues("uploader_url");
%>
<html>
<head>
	<title>菜单管理</title>
	<meta name="decorator" content="default"/>
	<%@include file="/webpage/include/treeview.jsp" %>
	<!-- 内容上传 引用-->
	<link rel="stylesheet" type="text/css" href="${ctxStatic}/train/uploadify/uploadify.css">
	<script type="text/javascript" src="${ctxStatic}/train/uploadify/lang-cn.js"></script>
	<script type="text/javascript" src="${ctxStatic}/train/uploadify/jquery.uploadify.min.js"></script>
	
	<link rel="stylesheet" href="${ctxStatic}/ec/css/moveImg.css">
	
	<!-- 图片上传 引用-->
	<link rel="stylesheet" type="text/css" href="${ctxStatic}/ec/css/custom_uploadImg.css">
	<link rel="stylesheet" type="text/css" href="${ctxStatic}/train/uploadify/uploadify.css">
	<script type="text/javascript" src="${ctxStatic}/train/uploadify/lang-cn.js"></script>
	<script type="text/javascript" src="${ctxStatic}/train/uploadify/jquery.uploadify.min.js"></script>
	
	<!-- 富文本框 -->
	<link rel="stylesheet" href="${ctxStatic}/kindEditor/themes/default/default.css" />
	<script src="${ctxStatic}/kindEditor/kindeditor-all.js" type="text/javascript"></script>
	
	<!-- 富文本框中新增的东西：上传照片，上传代码，商品分类，商品选择 -->
	<script src="${ctxStatic}/kindEditor/themes/editJs/editJs.js" type="text/javascript"></script>
	<!-- 富文本框上传图片样式引用 -->
	<link rel="stylesheet" type="text/css" href="${ctxStatic}/kindEditor/themes/editCss/edit.css">
	<script type="text/javascript">
		var validateForm;
		var tree2;
		function doSubmit(){//回调函数，在编辑和保存动作时，供openDialog调用提交表单。
			if($("#headImg").val() != null && $("#headImg").val() != ""){
				if(validateForm.form()){
					if($("#isOpen").val()==1){
						var ids2 = [], nodes2 = tree2.getCheckedNodes(true);
						for (var i=0; i<nodes2.length; i++) {
							var halfCheck = nodes2[i].getCheckStatus();
							if (!halfCheck.half){//不为半选
								ids2.push(nodes2[i].id);
							}
						}
						  if(ids2.length > 0){
							  $("#franchiseeId").val(ids2);
						  }else{
							  top.layer.alert('商家不可为空', {icon: 0, title:'提醒'});
							  return ;
						  }
					 }else{
						 $("#franchiseeId").val('');
					 }
					if(confirm("活动添加后不可修改！确定添加？")){
						creatContent();
						$("#inputForm").submit();
						return true;
					}
				}
			}else{
				top.layer.alert('封面照片不能为空！', {icon: 0});
			}
			return false;
		}
		jQuery.validator.addMethod("checkNumber",function(value, element){
            var returnVal = true;
            inputZ = value;
            var ArrMen= inputZ.split(".");    //截取字符串
            if(ArrMen.length==2){
                if(ArrMen[1].length>2){    //判断小数点后面的字符串长度
                    returnVal = false;
                    return false;
                }
            }
            return returnVal;
        },"小数点后最多为两位");         //验证错误信息
		$(document).ready(function(){
			
			var start = {
				    elem: '#startDate',
				    format: 'YYYY-MM-DD hh:mm:ss',
				    event: 'focus',
				    max: $("#endDate").val(),   //最大日期
				    istime: true,				//是否显示时间
				    isclear: true,				//是否显示清除
				    istoday: false,				//是否显示今天
				    issure: true,				//是否显示确定
				    festival: true,				//是否显示节日
				    choose: function(datas){
				         last.min = datas; 		//开始日选好后，重置结束日的最小日期
				         last.start = datas 		//将结束日的初始值设定为开始日
				    }
				};
			var last = {
			    elem: '#endDate',
			    format: 'YYYY-MM-DD hh:mm:ss',
			    event: 'focus',
			    min: $("#startDate").val(),
			    istime: true,
			    isclear: false,
			    istoday: false,
			    issure: true,
			    festival: true,
			    choose: function(datas){
			        start.max = datas; //结束日选好后，重置开始日的最大日期
			    }
			};
			laydate(start);
			laydate(last);
			
			validateForm = $("#inputForm").validate({
				//验证规则
	            rules: {
	            	amount: {
	                    required: true,    //要求输入不能为空
	                    number: true,     //输入必须是数字
	                    min:0,
	                    checkNumber: $("#amount").val()    //调用自定义验证
	                }
	            },
				messages:{
					enname:{remote:"英文名称已存在"}
				},
				submitHandler: function(form){
					loading('正在提交，请稍等...');
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
			
			$("#file_headImg_upload").uploadify({
				'buttonText' : '请选择封面',
				'method' : 'post',
				'swf' : '${ctxStatic}/train/uploadify/uploadify.swf',
				'uploader' : '<%=uploadURL%>',
				'fileObjName' : 'file_headImg_upload',//<input type="file"/>的name
				'queueID' : 'file_headImg_queue',//与下面HTML的div.id对应
				'method' : 'post',
				'fileTypeDesc': '支持的格式：*.BMP;*.JPG;*.PNG;*.GIF;',
				'fileTypeExts' : '*.BMP;*.JPG;*.PNG;*.GIF;', //控制可上传文件的扩展名，启用本项时需同时声明fileDesc 
				'fileSizeLimit' : '10MB',//上传文件的大小限制
				'multi' : false,//设置为true时可以上传多个文件
				'auto' : true,//点击上传按钮才上传(false)
				'onFallback' : function(){
					//没有兼容的FLASH时触发
					alert("您未安装FLASH控件，无法上传图片！请安装FLASH控件后再试。");
				},
				'onUploadSuccess' : function(file, data, response) { 
					var jsonData = $.parseJSON(data);//text 转 json
					if(jsonData.result == '200'){
						$("#headImg").val(jsonData.file_url);
						$("#headImgsrc").attr('src',jsonData.file_url); 
					}
				}
			});
			
			
			//-------------商家树开始----------------
			var setting = {check:{enable:true,autoCheckTrigger: true},view:{selectedMulti:false},
					data:{simpleData:{enable:true}},callback:{beforeClick:function(id, node){
						tree.checkNode(node, !node.checked, true, true);
						return false;
					}}};
					
			// 用户-机构
			var zNodes2=[
					<c:forEach items="${officeList}" var="office">{id:"${office.id}", pId:"${not empty office.parent?office.parent.id:0}", name:"${office.name}"},
		            </c:forEach>];
			// 初始化树结构
			tree2 = $.fn.zTree.init($("#officeTree"), setting, zNodes2);
			// 不选择父节点
			tree2.setting.check.chkboxType = { "Y" : "s", "N" : "s" };
			// 默认选择节点
			var ids2 = "${trainActivityCourse.franchiseeId}".split(",");
			for(var i=0; i<ids2.length; i++) {
				var node = tree2.getNodeByParam("id", ids2[i]);
				try{tree2.checkNode(node, true, false);}catch(e){}
			}
			// 默认展开全部节点
			//tree2.expandAll(true);
			tree2.selectNode(node,true,false);//  展开选中的节点
			
			// 刷新（显示/隐藏）机构
			refreshOfficeTree();
			$("#isOpen").change(function(){
				refreshOfficeTree();
			});
			//---------商家树结束----------
			
		});
		//上传图片，用户记录div的ID
		var imagecount = 0;
		//删除\左\右切换图片
		var updateImagesDiv = function(action,divname1,divname2){
			if('DEL' == action){
				//删除div
				$("#"+divname1).find("#"+divname2).remove();
			}else{
				//左右改变图片的位置
				var arr = $("#"+divname1).find('div').toArray();
				//判断当前div所属的位置
				var now;
				for(var i in arr){
					if(divname2 == arr[i].id){
						now = parseInt(i);
					}
				}
				var temp ;//临时保存div
				if('LEFT' == action){
					if(now-1 == -1){
						//当当前位置为第一个时,则与最后一个进行调换
						temp = arr[arr.length-1];
						arr[arr.length-1] = arr[now];
					}else{
						//左移动	
						temp = arr[now-1];//左移动减1
						arr[now-1] = arr[now];
					}
				}else if('RIGHT' == action){
					if(now+1 == arr.length){
						//当当前位置为最后一个时，则与第一个进行调换
						temp = arr[0];
						arr[0] = arr[parseInt(now)];
					}else{
						//右移动
						temp = arr[now+1];//右移动加1
						arr[now+1] = arr[now];
					}
				}
				arr[now] = temp;
				$("#"+divname1).html(arr);
			}
		}
		
		
		function refreshOfficeTree(){
			if($("#isOpen").val()==1){
				$("#officeTree").show();
			}else{
				$("#officeTree").hide();
			}
		}
	</script>
</head>
<body>
	<form:form id="inputForm" modelAttribute="trainActivityCourse" action="${ctx}/train/trainActivityCourse/save" method="post" class="form-horizontal">
		<form:hidden path="acId"/>
		<input type="hidden" id="contents" name="content" value="${trainActivityCourse.content}"><!-- 内容 不能删除-->
		<table class="table table-bordered  table-condensed dataTables-example dataTable no-footer">
		   <tbody>
		      <tr>
		         <td  class="width-15 active"><label class="pull-right"><font color="red">*</font> 活动名称:</label></td>
		         <td  class="width-35" colspan="3"><form:input path="name" htmlEscape="false" class="required form-control" maxlength="100"/></td>
		      </tr>
		      <tr>
		         <td  class="width-15 active"><label class="pull-right"><font color="red">*</font> 报名开始时间:</label></td>
		         <td  class="width-35" >
		         	<input id="startDate" name="startDate" type="text" maxlength="20" class="laydate-icon form-control layer-date input-sm required"
							value="<fmt:formatDate value="${trainActivityCourse.startDate}" pattern="yyyy-MM-dd HH:mm:ss"/>" readonly="readonly"/>
		         </td>
		         <td  class="width-15 active"><label class="pull-right"><font color="red">*</font> 报名截止时间:</label></td>
		         <td  class="width-35" >
		         	<input id="endDate" name="endDate" type="text" maxlength="20" class="laydate-icon form-control layer-date input-sm required"
							value="<fmt:formatDate value="${trainActivityCourse.endDate}" pattern="yyyy-MM-dd HH:mm:ss"/>" readonly="readonly"/>
		         </td>
		      </tr>
		      <tr>
		         <td  class="width-15 active"><label class="pull-right"><font color="red">*</font>报名费:</label></td>
		         <td class="width-35" ><form:input path="amount" htmlEscape="false" class="form-control required number"/>
		         <td  class="width-15 active"><label class="pull-right"><font color="red">*</font>排序:</label></td>
		         <td class="width-35" ><form:input path="sort" htmlEscape="false" class="form-control required digits"/>
		      </tr>
		      <tr>
		         <td  class="width-15 active"><label class="pull-right"><font color="red">*</font>公开:</label></td>
		         <td class="width-35" colspan="3">
		         	<form:select path="isOpen" id="isOpen" cssClass="form-control" >
						<form:option  value="0" label="公开"/>
						<form:option  value="1" label="不公开"/>
					</form:select>
					<div class="controls" style="margin-top:3px;margin-left: 10px;">
						<div id="officeTree" class="ztree" style="margin-top:3px;margin-left: 10px;"></div>
						<form:hidden path="franchiseeId"/>
					</div>
		         </td>
		         <%-- <td  class="width-15 active"><label class="pull-right"><font color="red">*</font>选择商家:</label></td>
		         <td class="width-35" ><form:input path="sort" htmlEscape="false" class="form-control required digits"/> --%>
		      </tr>
		      <tr>
		         <td  class="width-15 active"><label class="pull-right"><font color="red"></font>标签:</label></td>
		         <td class="width-35" colspan="3"><form:input path="label" htmlEscape="false" maxlength="4"  class="form-control"/>
		      </tr>
		      <tr>
		         <td  class="width-15 active"><label class="pull-right"><font color="red">*</font>封面照片:</label></td>
		         <td class="width-35" colspan="3">
			         <div style="float: left;">
			         	<img id="headImgsrc" src="${trainActivityCourse.headImg }" alt="images" style="width: 200px;height: 100px;"/>
						<input type="hidden" id="headImg" name="headImg" value="${trainActivityCourse.headImg }">
						<div class="upload">
							<input type="file" name="file_headImg_upload" id="file_headImg_upload">
						</div>
						<div id="file_headImg_queue"></div>
		         	</div>
		         </td>
		      </tr>
		      <tr>
		         <td  class="width-15 active"><label class="pull-right"><font color="red">*</font>活动介绍:</label></td>
		         <td colspan="3">
		         	<textarea name="contents" id="editor1" cols="30" class="form-control required" rows="20"></textarea>
		         </td>
		         
		      </tr>
		    </tbody>
		  </table>
	</form:form>
	<!-- 富文本框上传图片弹出框 -->
	<div class="ke-dialog-default ke-dialog ke-dalog-addpic" id="ke-dialog">
		<div class="ke-dialog-content">
			<div class="ke-dialog-header">图片<span class="ke-dialog-icon-close" id="close" title="关闭"></span></div>
			<div class="ke-dialog-body">
				<div class="ke-tabs" style="padding:20px;">
					<input type="hidden" id="ke-dialog-num">
					<ul class="ke-tabs-ul ke-clearfix">
						<li class="ke-tabs-li ke-tabs-li-on ke-tabs-li-selected">网络图片</li>
						<li class="ke-tabs-li">本地上传</li>
					</ul>
					<div class="tab1" style="display:block">
						<div class="form-group">
							图片地址：<input type="text" id="httpImg" class="" value="http://" style="width: 300px;height: 35px">
						</div>
						<div class="form-group">
							图片宽度：<input type="text" id="w_httpImg" class="" style="width: 300px;height: 35px">
						</div>
						<div class="form-group">
							图片高度：<input type="text" id="h_httpImg" class="" style="width: 300px;height: 35px">
						</div>
					</div>
					<div class="tab2">
				        <div class="upload" style="margin top:10px;">
							<input type="file" name="file_img_upload" id="file_img_upload"> 
						</div>
						<div id="file_img_queue" style="margin top:10px;"></div> 
						<div class="t3">
							<div id="file_img_queue" style="margin top:10px;"></div> 
						</div>
					</div>
				</div>
			</div>
			<div class="ke-dialog-footer">
			    <span class="ke-button-common ke-button-outer ke-dialog-yes" title="确定">
			        <input class="ke-button-common ke-button" type="button" value="确定" onclick="saveImg()">
			    </span>
			    <span class="ke-button-common ke-button-outer ke-dialog-no" title="取消">
			        <input class="ke-button-common ke-button" id="newClose" type="button" value="取消">
			    </span>
			</div>
			<div class="ke-dialog-shadow"></div>
			<div class="ke-dialog-mask ke-add-mask"></div>
		</div>
	</div>
	<script>
		var editor;
		KindEditor.ready(function(K) {
			editor = K.create('textarea[name="contents"]', {
				width : "100%",
				items : ['undo', 'redo', '|','plainpaste','image','link','fontname','fontsize','forecolor','hilitecolor','bold','italic','underline','|','justifyleft', 'justifycenter', 'justifyright','justifyfull','|','clearhtml','source','|','fullscreen']
			});
		});
		
		
		function creatContent(){//回调函数，在编辑和保存动作时，供openDialog调用提交表单。
		  var content = $(".ke-edit-iframe").contents().find(".ke-content").html();
			if(content.indexOf("style") >=0){
				content = content.replace("&lt;style&gt;","<style>");
				content = content.replace("&lt;/style&gt;","</style>");
			}
			$("#contents").val(content);
		}
		
		
		var newUploadURL = '<%=uploadURL%>';
		function LoadOver(){
			$("#ke-dialog-num").val("1");
			//给富文本框赋值
			var content = $("#contents").val();
			if(content.indexOf("style") >=0){
				content = content.replace("<style>","&lt;style&gt;");
				content = content.replace("</style>","&lt;/style&gt;");
			}
			$(".ke-edit-iframe").contents().find(".ke-content").html(content);
		}
		window.onload=LoadOver;
		// 发布文章弹出框
    </script>
</body>
</html>