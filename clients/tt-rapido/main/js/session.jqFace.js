(function($){
	//鼠标悬停，标签图片放大
	$.fn.facEenlarge = function(option){
		//对facepic这个图片元素样式处理
		$('#facepic').css('display','block');
		$('#facepic').css('background-color','#FFF');
		$('#facepic').css('border','1px solid #2D88D1');
		$('#facepic').attr('src',option.src);
		//获取表情窗口总宽度的2/1
		var faceParentbox = $('#facebox').width()/2;
		//获取当前鼠标指向的表情图片相对于表情窗口左边的相对距离
		var facebox = $('#'+option.id).position().left;
		if(facebox < faceParentbox){
			//右上角图片放大区域
			$('#facepic').css('right','0px');
			$('#facepic').css('left','');
		}else{
			//左上角图片放大区域
			$('#facepic').css('right','');
			$('#facepic').css('left','0px');
		}
		//如果原始尺寸小于25px,添加padding值
		var imgSrc = $('#facepic').attr("src"); 
		var img = new Image(); 
		img.src = imgSrc;
		if(img.width < 25){
			$('#facepic').css('padding','20px');
		}else{
			$('#facepic').css('padding','0px');
		}	
	}
	//鼠标离开放大图片隐藏
	$.fn.faceHide = function(){
		$('#facepic').css('display','none');
	}

	$.fn.qqFace = function(option){
		var assign = $('#'+option.assign);
		var id = option.id;
		var path = option.path;
		var faceJson = option.faceJson;

		if(assign.length<=0){
			alert('缺少表情赋值对象。');
			return false;
		}

		$(this).click(function(e){
			var strFace, labFace;
			if($('#'+id).length<=0){
				strFace = '<div id="'+id
				+'" style="position:absolute;display:none;z-index:1000;border:1px solid #ccc" class="qqFace">'
				+'<img id="facepic" style="position:absolute;display:none;" /><table border="0" cellspacing="0" cellpadding="0"><tr>';
				let i=1;
				for(let item in faceJson){
					labFace = ''+item+'';
					strFace += '<td style="padding:2px; border:1px solid #ccc"><img id="facepic_list_'+i+'" width="22px" height="22px" onmouseover="$.fn.facEenlarge(this);" onmouseout="$.fn.faceHide();" title="'+item+'" src="'+path+faceJson[item]+'" onclick="$(\'#'+option.assign+'\').setCaret();$(\'#'+option.assign+'\').insertAtCaret(\''+ labFace + '\');" /></td>';
					if( i % 17 == 0 ) strFace += '</tr><tr>';
					i = i+1;
				}
				strFace += '</tr></table></div>';
			}
			$(this).parent().before(strFace);
			var offset = $(this).position();
			$('#'+id).css('left',offset.left);
			$('#'+id).show();
			e.stopPropagation();
		});

		$(document).click(function(){
			$('#'+id).hide();
			$('#'+id).remove();
		});
	};

})(jQuery);

jQuery.fn.extend({
	setCaret: function(){
		if(!$.browser.msie) return;
		var initSetCaret = function(){
			var textObj = $(this).get(0);
			textObj.caretPos = document.selection.createRange().duplicate();
		};
		$(this).click(initSetCaret).select(initSetCaret).keyup(initSetCaret);
	},

	insertAtCaret: function(textFeildValue){
		var textObj = $(this).get(0);
		if(document.all && textObj.createTextRange && textObj.caretPos){
			var caretPos=textObj.caretPos;
			caretPos.text = caretPos.text.charAt(caretPos.text.length-1) == '' ?
			textFeildValue+'' : textFeildValue;
		} else if(textObj.setSelectionRange){
			var rangeStart=textObj.selectionStart;
			var rangeEnd=textObj.selectionEnd;
			var tempStr1=textObj.value.substring(0,rangeStart);
			var tempStr2=textObj.value.substring(rangeEnd);
			textObj.value=tempStr1+textFeildValue+tempStr2;
			textObj.focus();
			var len=textFeildValue.length;
			textObj.setSelectionRange(rangeStart+len,rangeStart+len);
			textObj.blur();
		}else{
			textObj.value+=textFeildValue;
		}
	}
});
