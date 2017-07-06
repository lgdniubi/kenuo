$(document).ready(function(){
	
	$("#newGoodsCategoryIdButton").click(function(){
		// 是否限制选择，如果限制，设置为disabled
		if ($("#newGoodsCategoryIdButton").hasClass("disabled")){
			return true;
		}
		// 正常打开	
		top.layer.open({
		    type: 2, 
		    area: ['300px', '420px'],
		    title:"选择商品分类",
		    ajaxData:{selectIds: $("#newGoodsCategoryIdId").val()},
		    content: "/kenuo/a/tag/treeselect?url="+encodeURIComponent("/ec/goodscategory/treeData")+"&module=&checked=&extId=&isAll=" ,
		    btn: ['确定', '关闭']
    	       ,yes: function(index, layero){ //或者使用btn1
						var tree = layero.find("iframe")[0].contentWindow.tree;//h.find("iframe").contents();
						var ids = [], names = [], nodes = [];
						if ("" == "true"){
							nodes = tree.getCheckedNodes(true);
						}else{
							nodes = tree.getSelectedNodes();
						}
						for(var i=0; i<nodes.length; i++) {//
							if (nodes[i].isParent){
								//top.$.jBox.tip("不能选择父节点（"+nodes[i].name+"）请重新选择。");
								//layer.msg('有表情地提示');
								top.layer.msg("不能选择父节点（"+nodes[i].name+"）请重新选择。", {icon: 0});
								return false;
							}//
							ids.push(nodes[i].id);
							names.push(nodes[i].name);//
							break; // 如果为非复选框选择，则返回第一个选择  
						}
						$("#newGoodsCategoryIdId").val(ids.join(",").replace(/u_/ig,""));
						$("#newGoodsCategoryIdName").val(names.join(","));
						$("#newGoodsCategoryIdName").focus();
						
						cateid=$("#newGoodsCategoryIdId").val();
						
						$("#goodselectId").val("");
						$("#goodselectName").val("");
						$("#goodsdetails").empty();
						top.layer.close(index);
				    	       },
    	cancel: function(index){ //或者使用btn2
    	           //按钮【按钮二】的回调
    	       }
		}); 
	});
		
	$("#goodselectButton").click(function(){
		// 是否限制选择，如果限制，设置为disabled
		if ($("#goodselectButton").hasClass("disabled")){
			return true;
		}
		if(cateid == "0" || cateid == ""){
			top.layer.alert('请先选择商品分类!', {icon: 0, title:'提醒'});
		}else{
			// 正常打开	
			top.layer.open({
			    type: 2, 
			    area: ['300px', '420px'],
			    title:"商品选择",
			    ajaxData:{selectIds: $("#goodselectId").val()},
			    content: "/kenuo/a/tag/treeselect?url="+encodeURIComponent("/ec/goods/treeGoodsData?goodsCategory="+cateid+"&isAppshow=1")+"&module=&checked=&extId=&isAll=",
			    btn: ['确定', '关闭']
	    	       ,yes: function(index, layero){ //或者使用btn1
							var tree = layero.find("iframe")[0].contentWindow.tree;//h.find("iframe").contents();
							var ids = [], names = [], nodes = [];
							if ("" == "true"){
								nodes = tree.getCheckedNodes(true);
							}else{
								nodes = tree.getSelectedNodes();
							}
							for(var i=0; i<nodes.length; i++) {//
								if (nodes[i].isParent){
									//top.$.jBox.tip("不能选择父节点（"+nodes[i].name+"）请重新选择。");
									//layer.msg('有表情地提示');
									top.layer.msg("不能选择父节点（"+nodes[i].name+"）请重新选择。", {icon: 0});
									return false;
								}//
								ids.push(nodes[i].id);
								names.push(nodes[i].name);//
								break; // 如果为非复选框选择，则返回第一个选择  
							}
							$(".loading").show();
							$("#goodselectId").val(ids.join(",").replace(/u_/ig,""));
							$("#goodselectName").val(names.join(","));
							$("#goodselectName").focus();
							loadGoods($("#goodselectId").val());
							top.layer.close(index);
					    	       },
	    	cancel: function(index){ //或者使用btn2
	    	           //按钮【按钮二】的回调
	    	       }
			}); 
		}
	});
	
	$("#close,#newClose,#closeShopTag,#newCloseShopTag,#closeCode,#newCloseCode").click(function(){
		$("#imgsrc").attr('src',null); 
		$("#img,#w_httpImg,#h_httpImg").val('');
		$("#httpImg").val('http://');
		$(".t3").html("");
		$('#ke-dialog').hide();
		
		$("#goodsCategoryIdId,#goodsCategoryIdName,#goodselectId,#goodselectName").val("");
		$("#goodsdetails").empty();
		$('#ke-dialog-shoptag').hide();
		
		$("#styleCss").val("");
		$('#ke-dialog-code').hide();
	});
	
	$('.ke-dalog-addpic .ke-tabs-ul li').click(function(){
		$(this).addClass('ke-tabs-li-on ke-tabs-li-selected').siblings().removeClass('ke-tabs-li-on ke-tabs-li-selected')
		if ($(this).index() == '0'){
			$('.ke-dalog-addpic .tab1').show()
			$('.ke-dalog-addpic .tab2').hide();
			$("#ke-dialog-num").val("1");
			
		}else{
			console.log($(this).index())
			$('.ke-dalog-addpic .tab2').show()
			$('.ke-dalog-addpic .tab1').hide();
			$("#ke-dialog-num").val("2");
		};
	});
	
	//富文本框上传照片
	$("#file_img_upload").uploadify({
		'buttonText' : '&nbsp;&nbsp;&nbsp;请选择上传图片',
		'method' : 'post',
		'swf' : ctxStatic+'/train/uploadify/uploadify.swf',
		'uploader' : newUploadURL,
		'fileObjName' : 'file_img_upload',//<input type="file"/>的name
		'queueID' : 'file_img_queue',//与下面HTML的div.id对应
		'method' : 'post',
		'fileTypeDesc' : '支持的格式：*.BMP;*.JPG;*.PNG;*.GIF;',
		'fileTypeExts' : '*.BMP;*.JPG;*.PNG;*.GIF;', //控制可上传文件的扩展名，启用本项时需同时声明fileDesc 
		'fileSizeLimit' : '10MB',//上传文件的大小限制
		'multi' : true,//设置为true时可以上传多个文件
		'auto' : true,//点击上传按钮才上传(false)
		'onFallback' : function() {
			//没有兼容的FLASH时触发
			alert("您未安装FLASH控件，无法上传图片！请安装FLASH控件后再试。");
		},
		'onUploadSuccess' : function(file, data, response) {
			var jsonData = $.parseJSON(data);//text 转 json
			if (jsonData.result == '200') {
				$(".t3").append("<input type='hidden' readonly='readonly' id='img' name='img' value='"+jsonData.file_url+"' class='form-control' style='width: 350px;' > <img id='imgsrc' src='"+jsonData.file_url+"' alt='' style='width: 100px;height: 100px;'/> ");
				/*$("#img").val(jsonData.file_url);
				$('.img').attr('href', jsonData.file_url);
				$("#imgsrc").attr('src', jsonData.file_url);*/
			}
		}
	});
	
});

	function loadGoods(num){
		$("#goodsdetails").empty();
		if(num != ""){
			$.ajax({
        		type : 'post',
        		url : ctx+'/ec/mtmyArticleList/loadGoods?goodsId='+num,
        		dateType: 'text',
        		success:function(data){
    				/* $("#goodsdetails").append('<dl class="mt-item" data-id="'+data.goodsId+'" action_type="'+data.actionType+'"><dt>'
    					+ '<img src="'+data.originalImg+'" alt="商品图片" style="width: 80px;height: 80px;"></dt>'
    					+ '<dd><h3>'+data.goodsName+'</h3>'
    					+ '<p class="clearfix">'
    					+ '<span class="cost"><i>¥</i>'+data.shopPrice+'</span>'
    					+ '<span class="link-btn">查看详情>> </span>'
    					+ '</p></dd></dl><br>'); */
    				$("#goodsdetails").append('<div class="hot-items" data-id="'+data.goodsId+'" action_type="'+data.actionType+'">'
    						+ '<dl>'
    						+ '<dt><img src="'+data.originalImg+'" alt=""></dt>'
    						+ '<dd>'
    						+ '<h3>'+data.goodsName+'</h3>'
    						+ '<p class="prise"><span>¥<i>'+data.shopPrice+'</i></span><del>¥'+data.marketPrice+'</del></p>'
    						+ '<p class="features">'
    						+ '<span class="score"><span class="score-inner" style="width:'+data.rank+'"></span></span>'
    						+ '<span class="sales">'+data.buyCount+'</span>'
    						+ '<span class="comment">'+data.commentNum+'</span>'
    						+ '	</p>'
    						+ '</dd>'
    						+ '</dl>'
    						+ '</div>');
    				$(".loading").hide();
        		}
        	})
		}
		$(".loading").hide();
	}
	function choose(num){
		$("#styleCss").val("");
		if(num == 1){
			$("#styleCss").val("<style>\n"
							+ "	p{margin:0;padding:0 12px;line-height:26px;color:#1a1a1a;font-size:16px;}\n"
							+ "	img{max-width:100%;}\n"
							+ "	.mt-item{margin:12px;padding:10px;overflow:hidden;box-shadow:0 1px 4px 2px #f0f0f0}\n"
							+ "	.mt-item dt{float:left;width:80px;height:80px;position:relative;overflow:hidden;}\n"
							+ "	.mt-item dt img{margin:0 !important;}\n"
							+ "	.mt-item dd{margin-left:90px;}\n"
							+ "	.mt-item h3{height:30px;line-height:30px; margin-top:6px;font-size:16px;color:#1a1a1a;white-space:nowrap;overflow:hidden;text-overflow:ellipsis;}\n"
							+ "	.mt-item p{height:30px;line-height:30px;padding:0; font-size:14px;color:#ebbd30;margin-top:10px;}\n"
							+ "	.mt-item p .cost{font-size:20px;}\n"
							+ "	.mt-item p .cost i{font-size:12px;}\n"
							+ "	.mt-item p .link-btn{float:right;}\n"
							+ "strong{font-weight:bold}\n"
							+ "</style>");
		}else if(num == 2){
			$("#styleCss").val("<style>\n"
					+ "	h1{margin:0;padding:0;line-height:40px;font-size:16px;color:#1a1a1a;}\n"
					+ "	p{margin:0;padding:0;line-height:26px;font-size:14px;color:#b2b2b2}\n"
					+ "</style>");
		}
	}
	
	/**
	 * 自定义上传照片
	 */
	KindEditor.plugin('image', function(K) {
	    var editor = this, self = this, name = 'image';
	    //点击图标时执行
	    editor.clickToolbar(name, function() {
	    //  editor.insertHtml('');
		    	$('#ke-dialog').show();
		});
	});
    //自定义插入标签
	KindEditor.plugin('shoptag', function(K) {
	    var editor = this, self = this, name = 'shoptag';
	    //点击图标时执行
	    editor.clickToolbar(name, function() {
	    //  editor.insertHtml('');
    		$("#goodsCategoryIdId,#goodsCategoryIdName,#goodselectId,#goodselectName").val("");
    		$("#goodsdetails").empty();
	    	$('#ke-dialog-shoptag').show();
		});
	});
	KindEditor.lang({
		shoptag : '商品卡片'
	});
	//自定义插入代码
	KindEditor.plugin('code', function(K) {
	    var editor = this, self = this, name = 'code';
	    //点击图标时执行
	    editor.clickToolbar(name, function() {
	    //  editor.insertHtml('');
	    	$("#selectOption").val("");
	    	$("#styleCss").val("");
	    	$('#ke-dialog-code').show();
		});
	});

	
	function saveImg(){
		if("1" == $("#ke-dialog-num").val()){
			if($("#httpImg").val() != "http://" && $("#httpImg").val() != ""){
				editor.insertHtml('<img alt="" src="'+$("#httpImg").val()+'" style="width:'+$("#w_httpImg").val()+'px;height:'+$("#h_httpImg").val()+'px;">');
			}
			$("#imgsrc").attr('src',null); 
			$("#img,#w_httpImg,#h_httpImg").val('');
			$("#httpImg").val('http://');
			$(".t3").html("");
			$('#ke-dialog').hide();
		}else if("2" == $("#ke-dialog-num").val()){
			$("input[name='img']").each(function(index,item){
				if($(this).val() != ""){
					editor.insertHtml('<img alt="" src="'+$(this).val()+'">');
				}
			});
			$("#imgsrc").attr('src',null); 
			$("#img,#w_httpImg,#h_httpImg").val('');
			$("#httpImg").val('http://');
			$(".t3").html("");
			$('#ke-dialog').hide();
		}else{
			$("#imgsrc").attr('src',null); 
			$("#img,#w_httpImg,#h_httpImg").val('');
			$("#httpImg").val('http://');
			$(".t3").html("");
			$('#ke-dialog').hide();
		}
	}	
	function saveTag(){
		if($("#goodselectId").val() != ""){
			$("#goodsdetails img").removeAttr("style");
			editor.insertHtml($("#goodsdetails").html());
		}
		$('#ke-dialog-shoptag').hide();
	}
	function saveCode(){
		if($("#styleCss").val() != ""){
			var content = $("#styleCss").val();
			if(content.indexOf("style") >=0){
				content = content.replace("<style>","&lt;style&gt;");
				content = content.replace("</style>","&lt;/style&gt;");
			}
			editor.insertHtml(content);
		}
		$('#ke-dialog-code').hide();
	}
