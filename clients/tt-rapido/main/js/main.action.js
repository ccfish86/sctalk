function activNav(navItem){
	navList = [$("#sessNavDiv"),$("#deptNavDiv"),$("#groupNavDiv")];
	contentList = [$("#sessionDiv"),$("#deptDiv"),$("#groupDiv")];
	iconList = ["icon-session", "icon-dept", "icon-group"]
	iconActivList = ["icon-session-activ", "icon-dept-activ", "icon-group-activ"]
	for (key in  navList){
		if(navItem.id == navList[key].attr("id")){
			navList[key].toggleClass(iconList[key],false);
		 	navList[key].toggleClass(iconActivList[key],true);
			contentList[key].show();
		}else{
			navList[key].toggleClass(iconList[key],true);
			navList[key].toggleClass(iconActivList[key],false);
			contentList[key].hide();
		}
	}
}

function miniWindow(){
  ipcRenderer.send('mainWin-mini');
}

//点击自己的头像弹出自己的详细信息窗口
function myMessage(){
  ipcRenderer.send('user-info');
}

//阻止触发父级li的groupBacClick事件
$(".creatGroup").click(function(e){
		e.stopPropagation(); // 阻止冒泡
});
//直接创建讨论组
function makeGroup(){
  ipcRenderer.send('make-group');
}

function settingWindow(){
//  ipcRenderer.send('setting-Win');
}

//在线
function Online(){
	let Onlinimg = $("#Online-img")[0].src;
	$("#switch").attr('src',Onlinimg);
	ipcRenderer.send('client-online');
}

//离线
function Offline(){
	let Offlineimg = $("#Offline-img")[0].src;
	$("#switch").attr('src',Offlineimg);
	$("#myAvatImg").toggleClass("gray",true);
	$(".img-circle").toggleClass("gray",true);
	$(".user_title").toggleClass("grays", true);
	ipcRenderer.send('client-offline');
}
