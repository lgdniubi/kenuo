<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/webpage/include/taglib.jsp"%>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="decorator" content="default"/>
    <link rel="stylesheet" href="${ctxStatic}/train/css/bootstrap.min.css">
    <link rel="stylesheet" href="${ctxStatic}/train/css/exam.css">
    <script src="${ctxStatic}/train/js/jquery-1.11.3.js"></script>
	<script type="text/javascript">
		//一级分类改变事件，联动二级分类
	function nowcategorychange(v){
		$.ajax({
			type : "POST",   
			url : "${ctx}/train/categorys/listtow?categoryId="+v,
			dataType: 'json',
			success: function(data) {
				$("#categoryId").empty();
				$.each(data.listtow, function(index,item){
					$("#categoryId").prepend("<option value='"+item.categoryId+"'>"+item.name+"</option>");
				});
			}
		});   
	}
	//显示后台传过来的值
	function categorychange(v,n){
		$.ajax({
			type : "POST",   
			url : "${ctx}/train/categorys/listtow?categoryId="+n,
			dataType: 'json',
			success: function(data) {
				$("#categoryId").empty();
				$.each(data.listtow, function(index,item){
					$("#categoryId").prepend("<option value='"+item.categoryId+"'>"+item.name+"</option>");
					document.getElementById("categoryId").value=v;
				});
			}
		});   
		document.getElementById("s1").value=n;
	}
	//页面加载事件
	$(document).ready(function() {
		//默认加载显示后台传过来的值
		categorychange($("#categoryid1").val(),$("#parentId").val());
		update($("#type").val(),$("#exerciseResult").val());	
	});
	// 显示相应的div
    function update(num,result) {
           if(num==3){
                 $('#tab-inner1').css('display', 'block');
                 $('#tab-inner2,#tab-inner3').css('display', 'none');
                 $('#but1').css("background","#5BC0DE");
                 $('#but2,#but3').css("background","");
                 document.getElementById("exercisetype").value="3";
                 $("input[type=radio][name=exerciseResult1][value="+result+"]").attr("checked",'checked');
           }
           if(num==1){
          		 $('#tab-inner1,#tab-inner3').css('display', 'none');
                 $('#tab-inner2').css('display', 'block');
                 $('#but1,#but3').css("background","");
                 $('#but2').css("background","#5BC0DE");
                 document.getElementById("exercisetype").value="1";
                 $("input[type=radio][name=exerciseResult2][value="+result+"]").attr("checked",'checked');
           }
           if(num==2){
           	     $('#tab-inner1,#tab-inner2').css('display', 'none');
                 $('#tab-inner3').css('display', 'block');
                 $('#but1,#but2').css("background","");
                 $('#but3').css("background","#5BC0DE");
                 document.getElementById("exercisetype").value="2";
                 var arr=new Array();
                 arr=result.split(',');//注split可以用字符或字符串分割
                 for(var i=0;i<arr.length;i++){
               	  $("input[type=checkbox][name=exerciseResult3][value="+arr[i]+"]").attr("checked",'checked');
                 }
           }
       }
  	   function doSubmit(){//回调函数，在编辑和保存动作时，供openDialog调用提交表单。
  			if($("#exercisetype").val()==3){
  				if(document.getElementById("categoryId").value==""){
					$("#lei").show();
				}else if($("#exerciseTitle1").val()==""){
					$("#bt1").show();
				}else{
				 	$("#inputForm").submit();
			  	    return true;
				}
			}
			if($("#exercisetype").val()==1){
				if(document.getElementById("categoryId").value==""){
					$("#lei").show();
				}else if($("#exerciseTitle2").val()==""){
					$("#bt2").show();
				}else if($("#exerciseContent21").val()==""||$("#exerciseContent22").val()==""||$("#exerciseContent23").val()==""||$("#exerciseContent24").val()==""){
					$("#da2").show();
				}else{
				    $("#inputForm").submit();
			  		return true;
				}
			}
			if($("#exercisetype").val()==2){
				if(document.getElementById("categoryId").value==""){
					$("#lei").show();
				}else if($("#exerciseTitle3").val()==""){
					$("#bt3").show();
				}else if($("#exerciseContent31").val()==""||$("#exerciseContent32").val()==""||$("#exerciseContent33").val()==""||$("#exerciseContent34").val()==""){
					$("#da3").show();
				}else if(document.querySelectorAll("input[type=checkbox]:checked").length<2){
					$("#da").show();
				}else{
				    $("#inputForm").submit();
			  		return true;
				}
			}
		}
	  //使验证文字隐藏
		function a(){
			$("p").hide();
		}
	</script>
	<style type="text/css">
	.tab-inner ul{
		padding:20px 0 0 10px;
	}
	</style>
    <title>试题详情</title>
</head>
<body>
    <div class="wrapper-content">
        <div class="ibox">
            <div class="ibox-title">
                <h5>修改试题</h5>
            </div>
            <form:form id="inputForm" modelAttribute="Exercises" action="${ctx}/train/examlist/save" method="post" class="form-horizontal" >
	            <div class="ibox-content">
	                <div class="sort">课程分类：
	                <!-- 隐藏文本框 方便传值  获取值进行判断 -->
	                <input id="exerciseId" name="exerciseId" value="${exerciseId}" type="hidden"/>
	                <input id="categoryid1" name="categoryid1" value="${categoryid}" type="hidden"/>
	                <input id="type" name="type" value="${exercisetype}" type="hidden"/>
	                <input id="exercisetype" name="exercisetype" value="${exercisetype}" type="hidden"/>
	                <input id="exerciseResult" name="exerciseResult" value="${exercisesCategorys.exerciseResult}" type="hidden"/>
	                <input id="parentId" name="parentId" value="${parentId}" type="hidden"/>
	                    <select class="form-control" id="s1" name="s1" onchange="nowcategorychange(this.options[this.options.selectedIndex].value)">
							<c:forEach items="${listone}" var="trainCategorys">
								<option value="${trainCategorys.categoryId}">${trainCategorys.name}</option>
							</c:forEach>
						</select>
						<select class="form-control" id="categoryId" name="name"></select>
                     	 <span class="btn-group tab-navs clearfix" role="group" id="tab-navs">
	                        <button type="button" class="btn btn-default" onclick="update(3)" id="but1">判断题</button>
	                        <button type="button" class="btn btn-default" onclick="update(1)" id="but2">单选题</button>
	                        <button type="button" class="btn btn-default"  onclick="update(2)" id="but3">多选题</button>
	                    </span>
	                    <p id="lei" style="display:none"><span class="col-sm-2"></span><font color="red" size="2">*  课程类别不能为空</font></p>
	                </div>
	                <div class="tab-content" id="tab-content">
					  	<div class="tab-inner" id="tab-inner1">
                            <ul>
                                <li>
                                    <span class="col-sm-2"><font color="red" size="2">*&nbsp;</font>添加题目：</span><textarea name="exerciseTitle1" id="exerciseTitle1" cols="46" class="mtitle col-sm-8" name="mtitle" onfocus="a()" maxlength="255">${exercisesCategorys.exerciseTitle}</textarea>
                                    <p id="bt1" style="display:none"><span class="col-sm-2"></span><font color="red" size="2">*  标题不能为空</font></p>
                                </li>
                                <li class="hr-line-dashed"></li>
                                <li>
                                	<span class="col-sm-2">关键字：</span><input type="text" class="mradio" id="tags1" name="tags1" value="${exercisesCategorys.tags}" maxlength="100">
                                </li>
                                <li>
                                    <span class="col-sm-2"><font color="red" size="2">*&nbsp;</font>正确答案： </span>
                                    <label class="checked">正确<input type="radio" name="exerciseResult1" id="exerciseResult1" value="1" checked="checked"></label>
                                    <label class="checked">错误<input type="radio" name="exerciseResult1" id="exerciseResult1" value="2"></label>
                                </li>
                            </ul>
	                    </div>
					  	<div class="tab-inner" id="tab-inner2">
                            <ul>
                                <li>
                                    <span class="col-sm-2"><font color="red" size="2">*&nbsp;</font>添加题目：</span><textarea name="exerciseTitle2" id="exerciseTitle2" cols="46" class="mtitle col-sm-8" name="mtitle" onfocus="a()" maxlength="255">${exercisesCategorys.exerciseTitle}</textarea>
                                    <p id="bt2" style="display:none"><span class="col-sm-2"></span><font color="red" size="2">*  标题不能为空</font></p>
                                </li>
                                <li class="hr-line-dashed"></li>
                                <li>
                                	<p id="da2" style="display:none"><span class="col-sm-2"></span><font color="red" size="2">*  答案内容不能为空</font></p>
                                    <span class="col-sm-2"><font color="red" size="2">*&nbsp;</font>选 项A：</span><input type="text" id="exerciseContent21" name="exerciseContent2" class="mradio" value="${exerciseContent0}" onfocus="a()" maxlength="120">
                                </li>
                                <li>
                                    <span class="col-sm-2"><font color="red" size="2">*&nbsp;</font>选 项B：</span><input type="text" id="exerciseContent22"  name="exerciseContent2" class="mradio" value="${exerciseContent2}" onfocus="a()" maxlength="120">
                                </li>
                                <li>
                                    <span class="col-sm-2"><font color="red" size="2">*&nbsp;</font>选 项C：</span><input type="text" id="exerciseContent23" name="exerciseContent2" class="mradio" value="${exerciseContent4}" onfocus="a()"  maxlength="120">
                                </li>
                                <li>
                                    <span class="col-sm-2"><font color="red" size="2">*&nbsp;</font>选 项D：</span><input type="text" id="exerciseContent24" name="exerciseContent2" class="mradio" value="${exerciseContent6}" onfocus="a()" maxlength="120">
                                </li>
                                <li class="hr-line-dashed"></li>
                                <li>
                                	<span class="col-sm-2">关键字：</span><input type="text" class="mradio" id="tags2" name="tags2" value="${exercisesCategorys.tags}" maxlength="100">
                                </li>
                                <li>
                                    <span class="col-sm-2"><font color="red" size="2">*&nbsp;</font>正确答案： </span>
                                    <label class="checked">A<input type="radio" name="exerciseResult2" id="exerciseResult2" value="1" checked="checked"></label>
                                    <label class="checked">B<input type="radio" name="exerciseResult2" id="exerciseResult2" value="2"></label>
                                    <label class="checked">C<input type="radio" name="exerciseResult2" id="exerciseResult2" value="3"></label>
                                    <label class="checked">D<input type="radio" name="exerciseResult2" id="exerciseResult2" value="4"></label>
                                </li>
                            </ul>
	                    </div>
				 		<div class="tab-inner" id="tab-inner3">
                            <ul>
                                <li>
                                    <span class="col-sm-2"><font color="red" size="2">*&nbsp;</font>添加题目：</span><textarea name="exerciseTitle3" id="exerciseTitle3" cols="46" class="mtitle col-sm-8" name="mtitle" onfocus="a()" maxlength="255">${exercisesCategorys.exerciseTitle}</textarea>
                                    <p id="bt3" style="display:none"><span class="col-sm-2"></span><font color="red" size="2">*  标题不能为空</font></p>
                                </li>
                                <li class="hr-line-dashed"></li>
                                <li>
                                	<p id="da3" style="display:none"><span class="col-sm-2"></span><font color="red" size="2">*  答案内容不能为空</font></p>
                                    <span class="col-sm-2"><font color="red" size="2">*&nbsp;</font>选 项A：</span><input type="text" name="exerciseContent3" id="exerciseContent31" class="mradio" value="${exerciseContent0}" onfocus="a()" maxlength="120">
                                </li>
                                <li>
                                    <span class="col-sm-2"><font color="red" size="2">*&nbsp;</font>选 项B：</span><input type="text" name="exerciseContent3" id="exerciseContent32" class="mradio" value="${exerciseContent2}" onfocus="a()" maxlength="120">
                                </li>
                                <li>
                                    <span class="col-sm-2"><font color="red" size="2">*&nbsp;</font>选 项C：</span><input type="text" name="exerciseContent3" id="exerciseContent33" class="mradio" value="${exerciseContent4}" onfocus="a()" maxlength="120">
                                </li>
                                <li>
                                    <span class="col-sm-2"><font color="red" size="2">*&nbsp;</font>选 项D：</span><input type="text" name="exerciseContent3" id="exerciseContent34" class="mradio" value="${exerciseContent6}" onfocus="a()" maxlength="120">
                                </li>
                                <li class="hr-line-dashed"></li>
                                <li>
                                	<span class="col-sm-2">关键字：</span><input type="text" class="mradio" id="tags3" name="tags3" value="${exercisesCategorys.tags}" maxlength="100">
                                </li>
                                <li>
                                	<p id="da" style="display:none"><span class="col-sm-2"></span><font color="red" size="2">*  答案不能少于两个</font></p>
                                    <span class="col-sm-2"><font color="red" size="2">*&nbsp;</font>正确答案： </span>
                                    <label class="checked">A<input type="checkbox" name="exerciseResult3" id="exerciseResult3" value="1" onfocus="a()"></label>
                                    <label class="checked">B<input type="checkbox" name="exerciseResult3" id="exerciseResult3" value="2" onfocus="a()"></label>
                                    <label class="checked">C<input type="checkbox" name="exerciseResult3" id="exerciseResult3" value="3" onfocus="a()"></label>
                                    <label class="checked">D<input type="checkbox" name="exerciseResult3" id="exerciseResult3" value="4" onfocus="a()"></label>
                                </li>
                            </ul>
	                   	</div>
	                </div>
	            </div>
            </form:form>
        </div>
    </div>
</body>
</html>