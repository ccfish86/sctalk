const {app, BrowserWindow, Tray, Menu, MenuItem, globalShortcut} = require('electron');

const os = require('os');

let autoUpdater = require('electron').autoUpdater;

var ipcInit = require('./service/main/ipcMainOn.js');
var taryAction = require("./service/main/trayAction.js");
var loginAction = require("./service/main/loginAction.js");
let fs = require('fs');
var config = require("./service/assistant/config.js");
const spawn = require('child_process').spawn;

// Keep a global reference of the window object, if you don't, the window will
// be closed automatically when the JavaScript object is garbage collected.
let mainWindow;

const shouldQuit = app.makeSingleInstance((commandLine, workingDirectory) => {
  // Someone tried to run a second instance, we should focus our window.
  if (mainWindow) {
    mainWindow.show()
  }
})

if (shouldQuit) {
  app.quit()
}

//let global.priorConn
function createWindow () {
  autoUpdater.setFeedURL("http://192.168.0.228:8765");
  autoUpdater.on("error", function(error){
      console.log(error);
  });
  autoUpdater.on("checking-for-update", function(){
      console.log("checking-for-update");
  });
  autoUpdater.on("update-available", function(){
      console.log("update-available");
  });
  autoUpdater.on("update-not-available", function(){
      console.log("update-not-available");
  });
  autoUpdater.on("update-downloaded", function(releaseName){
      console.log("update-downloaded");
      autoUpdater.quitAndInstall();
      app.quit();
  });

  autoUpdater.checkForUpdates();

  /* Create shortcut to Desktop and StartMenu; 2017/03/18: Only win32 supported now. */
  if (os.platform() == 'win32') {
      let create_shortcut_args = ['--createShortcut', 'Rapido.exe', '-l', 'Desktop,StartMenu'];
      let update_exe_name1 = 'Update.exe';
      let update_exe_name2 = '..\\Update.exe';
      fs.access(update_exe_name1, fs.constants.F_OK, (err) => {
          if (!err) {
              spawn(update_exe_name1, create_shortcut_args);
          } else {
              fs.access(update_exe_name2, fs.constants.F_OK, (err) => {
                  if (!err) {
                      spawn(update_exe_name2, create_shortcut_args);
                  }
              });
          }
      });
  }

  //创建登录窗口
  let mainWinStyle = {
    width: config.mainWinWidth,
    height: config.mainWinHeight,
    frame:false,
    maximizable:false,
    skipTaskbar:true,
    transparent:true
  };

  mainWindow = new BrowserWindow(mainWinStyle);
  //mainWindow.setMenu(null);
  // and load the index.html of the app.
  mainWindow.loadURL('file://' + __dirname + '/main/login.html');

  // Debug mode
  if (config.isDev){
    mainWindow.webContents.openDevTools();
  }

  const tray = new Tray( __dirname + '/images/logo.png');
  const menu = new Menu();

  // menu.append(new MenuItem({label: '注销', click(){
  //   app.relaunch()
  //   app.exit(0)
  // }}))

  menu.append(new MenuItem({label: '重新登录', click(){
    for (let item of BrowserWindow.getAllWindows()){
      item.close();
    }
    app.relaunch()
    app.exit(0)
  }}))

  menu.append(new MenuItem({label: '退出', click(){
    for (let item of BrowserWindow.getAllWindows()){
      item.close();
    }
  }}))

  tray.setToolTip('FTSafe Rapido version: ' + app.getVersion() + '.')
  tray.setContextMenu(menu)
  tray.on('click',() => {
    if(mainWindow && !mainWindow.isFocused()){
      mainWindow.show();
    }
    taryAction.stopTray(1);
  })

  // Emitted when the window is closed.
  mainWindow.on('closed', function () {
    // Dereference the window object, usually you would store windows
    // in an array if your app supports multi windows, this is the time
    // when you should delete the corresponding element.
    mainWindow = null
  })

  mainWindow.webContents.on('did-finish-load', function(){
    if (!global.myUserId){
      loginAction.checkAutoLogin();
    }
  })

  mainWindow.setMaximizable(false);

  mainWindow.setResizable(false); //设置窗口是否可以被用户改变size.

  global.maindir = __dirname;

  ipcInit.ipcInit()
  global.mainWindow = mainWindow;
  global.tray = tray;
  global.datadir = os.homedir()+"/AppData/Local/rapido";
  //文件发送默认存储位置
  global.files = os.homedir()+"/Documents/Files/";

  // Register a 'Alt+S' shortcut listener.
  // const ret = globalShortcut.register('Alt+S', () => {
  //   if(BrowserWindow.getFocusedWindow() && !BrowserWindow.getFocusedWindow().isDestroyed()){
  //     BrowserWindow.getFocusedWindow().send("sessMsg-shortCut");
  //   }
  // })
  //
  // if (!ret) {
  //   console.log('registration failed')
  // }
  //
}

// This method will be called when Electron has finished
// initialization and is ready to create browser windows.
// Some APIs can only be used after this event occurs.
app.on('ready', createWindow)

// Quit when all windows are closed.
app.on('window-all-closed', function () {
  // On OS X it is common for applications and their menu bar
  // to stay active until the user quits explicitly with Cmd + Q
  if (process.platform !== 'darwin') {
    app.quit()
  }
})

app.on('activate', function () {
  // On OS X it's common to re-create a window in the app when the
  // dock icon is clicked and there are no other windows open.
  if (mainWindow === null) {
    createWindow()
  }
})

app.on('will-quit', () => {
  // Unregister a shortcut.
  globalShortcut.unregister('Alt+S')

  // Unregister all shortcuts.
  globalShortcut.unregisterAll()
})


// In this file you can include the rest of your app's specific main process
// code. You can also put them in separate files and require them here.
