/**
 * Created by zhenkui on 2017/3/7.
 */

var electronInstaller = require('electron-winstaller');

var platform_archs = [
  'win32-ia32'
  , 'win32-x64'
// , 'linux-ia32'
// , 'linux-x64'
// , 'linux-armv7l'
// , 'darwin-x64'
// , 'mas-x64'
]

let promises = []
for (platform_arch of platform_archs) {
    let promise = electronInstaller.createWindowsInstaller({
        appDirectory: './Rapido-' + platform_arch,
        outputDirectory: './build/Installer-' + platform_arch,
        authors: 'FTSafe Inc.',
        exe: platform_arch.startsWith('win32') ? 'Rapido.exe' : 'Rapido'
    });
    promise.platform_arch = platform_arch;
    promises.push(promise);
}

Promise.all(promises).then(() => console.log("It worked!"), (e) => console.log("No dice: " + e));
