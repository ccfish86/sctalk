const nativeImage = require('electron').nativeImage
// let trayImgSrc = 'file://' + __dirname + '/images/sendicon.png';
// let trayImg = nativeImage.createFromDataURL(trayImgSrc);
var isLogoRed = true;
let isTray = false
var trayInterval = null;
function startTray(localSessionId){
  if (!isTray){
    isTray = true;
    let logoImgSrc =  global.maindir + '/images/logo_red.png';
    let logoImg = nativeImage.createFromPath(logoImgSrc);
    trayInterval = setInterval(function() {
      if (isLogoRed){
        global.tray.setImage(logoImg);
        isLogoRed = false;
      }else{
        global.tray.setImage(nativeImage.createEmpty());
        isLogoRed = true;
      }
    }, 800);
  }
}

function stopTray(localSessionId){
  if (isTray){
    clearInterval(trayInterval);
    isTray = false;
    let logoImgSrc =  global.maindir + '/images/logo.png';
    let logoImg = nativeImage.createFromPath(logoImgSrc);
    global.tray.setImage(logoImg);
  }
}

function offLineTray(){
  let logoImgSrc =  global.maindir + '/images/gray_logo.png';
  let logoImg = nativeImage.createFromPath(logoImgSrc);
  global.tray.setImage(logoImg);
}

exports.startTray = startTray;
exports.stopTray = stopTray;
exports.offLineTray = offLineTray;
