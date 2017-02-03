//页面加载时间
$(document).ready(function() {
	//商品规格 控制选中 动态生成 价格和库存等
	$("#goods_spec_table").on('click','tr .btn',function(){
		var _this =$(this); 
		if (_this.hasClass('btn-success')){
			_this.find("input").prop("checked",false);
			_this.removeClass('btn-success').addClass('btn-default');
		}else{
			_this.find("input").prop("checked",true);
			_this.removeClass('btn-default').addClass('btn-success');
		};
		ajaxGetSpecInput();	 
	});
	
	//点击商品规格触发 下面输入框显示
	function ajaxGetSpecInput(){
		var spec_arr = {};// 用户选择的规格数组
		$("#goods_spec_table  span").each(function(){
			if($(this).hasClass('btn-success')){
				var spec_id = $(this).data('spec_id');
				var item_id = $(this).data('item_id');
				if(!spec_arr.hasOwnProperty(spec_id))
					spec_arr[spec_id] = [];
			    spec_arr[spec_id].push(item_id);
			}		
		});
		ajaxGetSpecInput2(spec_arr); // 显示下面的输入框
	}

	//根据用户选择的不同规格选项 
	//返回 不同的输入框选项
	function ajaxGetSpecInput2(spec_arr){	
		$(".loading").show();//打开展示层
		var specarr = JSON.stringify(spec_arr);
		$.ajax({
			type:'POST',
			url:ctx+"/ec/goods/getspeccontent?SPECARR="+specarr,
			dataType: 'json',
			success:function(data){
				$(".loading").hide(); //关闭加载层
				var status = data.STATUS;
				var tablecontent = data.TABLECONTENT;
				$("#goods_spec_table2").empty();//清空table里面的值
				if("OK" == status){
					$("#goods_spec_table2").append(tablecontent);//填写table
					$("#specarr").val(specarr);
					hbdyg();  // 合并单元格
				}else if("ERROR" == status){
					//失败
					alert(data.MESSAGE);
				}
			}
		});
	}
	
	//合并单元格
	var hbdyg = function(){
		var tab = document.getElementById("goods_spec_input_table"); //要合并的tableID
        var maxCol = 2, val, count, start;  //maxCol：合并单元格作用到多少列  
        if (tab != null) {
            for (var col = maxCol - 1; col >= 0; col--) {
                count = 1;
                val = "";
                for (var i = 0; i < tab.rows.length; i++) {
                    if (val == tab.rows[i].cells[col].innerHTML) {
                        count++;
                    } else {
                        if (count > 1) { //合并
                            start = i - count;
                            tab.rows[start].cells[col].rowSpan = count;
                            for (var j = start + 1; j < i; j++) {
                                tab.rows[j].cells[col].style.display = "none";
                            }
                            count = 1;
                        }
                        val = tab.rows[i].cells[col].innerHTML;
                    }
                }
                if (count > 1) { //合并，最后几行相同的情况下
                    start = i - count;
                    tab.rows[start].cells[col].rowSpan = count;
                    for (var j = start + 1; j < i; j++) {
                        tab.rows[j].cells[col].style.display = "none";
                    }
                }
            }
        }
    }
});
