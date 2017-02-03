<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/webpage/include/taglib.jsp"%>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="decorator" content="default"/>
    <meta name="viewport" content="width=device-width, initial-scale=1.0, minimum-scale=1.0, maximum-scale=1.0,user-scalable=no">
    <link rel="stylesheet" href="${ctxStatic}/train/css/bootstrap.min.css">
    <link rel="stylesheet" href="${ctxStatic}/train/css/exam.css">
    <script src="${ctxStatic}/train/js/jquery-1.11.3.js"></script>
    <script type="text/javascript">
		//一级分类改变事件，联动二级分类
		function categorychange(v){
			$(".loading").show();//打开展示层
			$.ajax({
				type : "POST",   
				url : "${ctx}/train/categorys/listtow?categoryId="+v,
				dataType: 'json',
				success: function(data) {
					$(".loading").hide();//隐藏展示层
					$("#categoryId").empty();
					$.each(data.listtow, function(index,item){
						$("#categoryId").prepend("<option value='"+item.categoryId+"'>"+item.name+"</option>");
					});
				}
			});   
		}
		//页面加载事件
		$(document).ready(function() {
			//默认加载第一个分类
			categorychange($("#s1").val());
		});
		//使验证文字隐藏
		function a(){
			$("p").hide();
		}
		function add(num){ 
			if(num==3){
				if(document.getElementById("categoryId").value==""){
					$("#lei").show();
				}else if($("#exerciseTitle1").val()==""){
					$("#bt1").show();
				}else{
					huigun(num);
				}
			}
			if(num==1){
				if(document.getElementById("categoryId").value==""){
					$("#lei").show();
				}else if($("#exerciseTitle2").val()==""){
					$("#bt2").show();
				}else if($("#exerciseContent21").val()==""||$("#exerciseContent22").val()==""||$("#exerciseContent23").val()==""||$("#exerciseContent24").val()==""){
					$("#da2").show();
				}else{
					huigun(num);
				}
			}
			if(num==2){
				if(document.getElementById("categoryId").value==""){
					$("#lei").show();
				}else if(document.getElementById("exerciseTitle3").value==""){
					$("#bt3").show();
				}else if($("#exerciseContent31").val()==""||$("#exerciseContent32").val()==""||$("#exerciseContent33").val()==""||$("#exerciseContent34").val()==""){
					$("#da3").show();
				}else if(document.querySelectorAll("input[type=checkbox]:checked").length<2){
					$("#da").show();
				}else{
					huigun(num);
				}
			}
		}
		function huigun(shu){
			$.ajax({
				url : "${ctx}/train/examlist/addExam?exerciseType="+shu,
				type : "post",
				data : $("#judgment").serialize(),     //此处表单序列化
				dataType : "text",
				success : 
					//function(data) {
			//	if(confirm('是否继续添加试题?'))
			//		{
			//		    $("input[type='text']").val("");
			//		    $(".mtitle").val("");
			//		}
			//		else
			//		{
			//		   window.location.href="${ctx}/train/examlist/examlist";
			//		}
		function confirmx(mess, href, closed){
			top.layer.confirm('是否跳转到试题库?', {icon: 3, title:'系统提示'}, function(index){
				window.location.href="${ctx}/train/examlist/examlist";
				top.layer.close(index);
			});
				$("input[type='text']").val("");
			    $(".mtitle").val("");
				},
			});	
		}
	</script>
	<style type="text/css">
	.tab-inner ul{
		padding:20px 0 0 10px;
	}
	</style>
    <title>添加试题</title>
</head>
<body>
    <div class="wrapper-content">
        <div class="ibox">
            <div class="ibox-title">
                <h5>添加试题</h5>
            </div>
            <sys:message content="${message}"/>
            <div class="ibox-content">
                <form name="myform" action="" id="judgment" class="judgment" method="get">
	                <div class="sort">课程分类：
	                    <select class="form-control" id="s1" name="s1" onchange="categorychange(this.options[this.options.selectedIndex].value)">
							<c:forEach items="${listone}" var="trainCategorys">
								<option value="${trainCategorys.categoryId}">${trainCategorys.name}</option>
							</c:forEach>
						</select>
						<select class="form-control" id="categoryId" name="name"></select>
	                    <span class="btn-group tab-navs clearfix" role="group" id="tab-navs">
	                        <button type="button"  class="btn btn-default active">判断题</button>
	                        <button type="button"  class="btn btn-default">单选题</button>
	                        <button type="button"  class="btn btn-default">多选题</button>
	                    </span>
	                    <p id="lei" style="display:none"><span class="col-sm-2"></span><font color="red" size="2">*  课程类别不能为空</font></p>
	                </div>
	                <div class="tab-content" id="tab-content">
	                    <div class="tab-inner">
                            <ul>
                                <li>
                                    <span class="col-sm-2"><font color="red" size="2">*&nbsp;</font>添加题目：</span><textarea name="exerciseTitle1" id="exerciseTitle1" class="mtitle col-sm-8" name="mtitle" onfocus="a()" maxlength="255"></textarea>
                                    <p id="bt1" style="display:none" ><span class="col-sm-2"></span><font color="red" size="2">*  标题不能为空</font></p>
                                </li>
                                <li class="hr-line-dashed"></li>
                                <li>
                                	<span class="col-sm-2">关键字：</span><input type="text" class="mradio" id="tags1"   name="tags1"  maxlength="100">
                                </li>
                                <li>
                                    <span class="col-sm-2"><font color="red" size="2">*&nbsp;</font>正确答案： </span>
                                    <label class="checked">正确<input type="radio" checked="checked"  id="exerciseResult1" name="exerciseResult1" value="1" ></label>
                                    <label class="checked">错误<input type="radio" id="exerciseResult1" name="exerciseResult1" value="2" ></label>
                                    <span class="col-sm-7 sub-btn">
	                                <shiro:hasPermission name="train:addExam:add">    
	                                    <input type="button" onclick="add(3)"  class="btn btn-primary" value="确认添加">
	                                </shiro:hasPermission>  
                                    </span>
                                </li>
                            </ul>
	                    </div>
	                    <div class="tab-inner hidden">
                            <ul>
                                <li>
                                    <span class="col-sm-2"><font color="red" size="2">*&nbsp;</font>添加题目：</span><textarea name="exerciseTitle2" id="exerciseTitle2" class="mtitle col-sm-8" name="mtitle" onfocus="a()" maxlength="255"></textarea>
                                    <p id="bt2" style="display:none"><span class="col-sm-2"></span><font color="red" size="2">*  标题不能为空</font></p>
                                </li>
                                <li class="hr-line-dashed"></li>
                                <li>
                                	<p id="da2" style="display:none"><span class="col-sm-2"></span><font color="red" size="2">*  答案内容不能为空</font></p>
                                    <span class="col-sm-2"><font color="red" size="2">*&nbsp;</font>选 项A：</span><input type="text" class="mradio" id="exerciseContent21" name="exerciseContent2" onfocus="a()" maxlength="120">
                                </li>
                                <li>
                                    <span class="col-sm-2"><font color="red" size="2">*&nbsp;</font>选 项B：</span><input type="text" class="mradio" id="exerciseContent22" name="exerciseContent2" onfocus="a()"  maxlength="120">
                                </li>
                                <li>
                                    <span class="col-sm-2"><font color="red" size="2">*&nbsp;</font>选 项C：</span><input type="text" class="mradio" id="exerciseContent23" name="exerciseContent2" onfocus="a()"  maxlength="120">
                                </li>
                                <li>
                                    <span class="col-sm-2"><font color="red" size="2">*&nbsp;</font>选 项D：</span><input type="text" class="mradio" id="exerciseContent24" name="exerciseContent2" onfocus="a()"  maxlength="120">
                                </li>
                                <li class="hr-line-dashed"></li>
                                <li>
                                	<span class="col-sm-2">关键字：</span><input type="text" class="mradio" id="tags2" name="tags2"  maxlength="100">
                                </li>
                                <li>
                                    <span class="col-sm-2"><font color="red" size="2">*&nbsp;</font>正确答案： </span>
                                    <label class="checked">A<input type="radio" checked="checked" id="exerciseResult2" name="exerciseResult2" value="1" ></label>
                                    <label class="checked">B<input type="radio" id="exerciseResult2" name="exerciseResult2" value="2" ></label>
                                    <label class="checked">C<input type="radio" id="exerciseResult2" name="exerciseResult2" value="3" ></label>
                                    <label class="checked">D<input type="radio" id="exerciseResult2" name="exerciseResult2" value="4" ></label>
                                    <span class="col-sm-7 sub-btn">
                                    <shiro:hasPermission name="train:addExam:add">
                                    	<input type="button" onclick="add(1)" class="btn btn-primary" value="确认添加">
                                   	</shiro:hasPermission>
                                   	</span>
                                </li>
                            </ul>
	                    </div>
	                    <div class="tab-inner hidden">
                            <ul>
                                <li>
                                    <span class="col-sm-2"><font color="red" size="2">*&nbsp;</font>添加题目：</span><textarea name="exerciseTitle3" id="exerciseTitle3" class="mtitle col-sm-8" name="mtitle" onfocus="a()" maxlength="255"></textarea>
                                    <p id="bt3" style="display:none"><span class="col-sm-2"></span><font color="red" size="2">*  标题不能为空</font></p>
                                </li>
                                <li class="hr-line-dashed"></li>
                                <li>
                                	<p id="da3" style="display:none"><span class="col-sm-2"></span><font color="red" size="2">*  答案内容不能为空</font></p>
                                    <span class="col-sm-2"><font color="red" size="2">*&nbsp;</font>选 项A：</span><input type="text" class="mradio" id="exerciseContent31" name="exerciseContent3" onfocus="a()"   maxlength="120">
                                </li>
                                <li>
                                    <span class="col-sm-2"><font color="red" size="2">*&nbsp;</font>选 项B：</span><input type="text" class="mradio" id="exerciseContent32" name="exerciseContent3" onfocus="a()"   maxlength="120">
                                </li>
                                <li>
                                    <span class="col-sm-2"><font color="red" size="2">*&nbsp;</font>选 项C：</span><input type="text" class="mradio" id="exerciseContent33" name="exerciseContent3" onfocus="a()"   maxlength="120">
                                </li>
                                <li>
                                    <span class="col-sm-2"><font color="red" size="2">*&nbsp;</font>选 项D：</span><input type="text" class="mradio" id="exerciseContent34" name="exerciseContent3" onfocus="a()"   maxlength="120">
                                </li>
                                <li class="hr-line-dashed"></li>
                                <li>
                                	<span class="col-sm-2">关键字：</span><input type="text" class="mradio" id="tags3" name="tags3"  maxlength="100">
                                </li>
                                <li>
                                	<p id="da" style="display:none"><span class="col-sm-2"></span><font color="red" size="2">*  答案不能少于两个</font></p>
                                    <span class="col-sm-2"><font color="red" size="2">*&nbsp;</font>正确答案： </span>
                                    <label class="checked">A<input type="checkbox" id="exerciseResult3" name="exerciseResult3" value="1" onfocus="a()"></label>
                                    <label class="checked">B<input type="checkbox" id="exerciseResult3"  name="exerciseResult3" value="2" onfocus="a()"></label>
                                    <label class="checked">C<input type="checkbox" id="exerciseResult3"  name="exerciseResult3" value="3" onfocus="a()"></label>
                                    <label class="checked">D<input type="checkbox" id="exerciseResult3"  name="exerciseResult3" value="4" onfocus="a()"></label>
                                    <span class="col-sm-7 sub-btn">
                                    <shiro:hasPermission name="train:addExam:add">
                                    	<input type="button" onclick="add(2)" class="btn btn-primary" value="确认添加">
                                    </shiro:hasPermission>
                                    </span>
                                </li>
                            </ul>
	                    </div>
                     </div>
                  </form>
              </div>
          </div>
      </div>
      <div class="loading"></div> 
	<script type="text/javascript">
	    $(function(){
	        // tab
	        $('#tab-navs button').click(function(){
	           var $this = $(this);
	           $this.addClass('active').siblings().removeClass('active');
	           $('#tab-content .tab-inner').eq($this.index()).removeClass('hidden').siblings().addClass('hidden');
	        });
	    });
	</script>
</body>
</html>