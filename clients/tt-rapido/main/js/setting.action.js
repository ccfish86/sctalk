var ipcRender = nodeRequire('electron').ipcRenderer;

$(function(){
  ipcRender.on('setting-load', (event) => {
    $("#closeWindow").click(function(){
      ipcRender.send('settingWin-close');
    });

    $("#closeButton").click(function(){
      ipcRender.send('settingWin-close');
    });
  });
});

