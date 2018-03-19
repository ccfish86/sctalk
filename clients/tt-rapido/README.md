# <div style="text-align:center;">rapido</div>

## 介绍
teamtalk-nodejs 版本，该版为以后客户端的主要版本
> >针对当前服务器端调整

## 构建可执行文件
### 编译exe文件
1. 准备
    安装electron-packager
    ```
    npm install electron-packager
    ```
    安装(需要配置) sqlite3
    npm install -g windows-build-tools node-pre-gyp
    #sqlite3 for windows
    npm install sqlite3  --runtime=electron --target=1.8.2 --dist-url=https://atom.io/download/electron

2. 编译exe文件
    在应用的根目录执行以下命令:
    ```
    electron-packager --platform=<platform> --arch=<arch> .
    ```
    如果之前已经构建过可执行文件, 执行该命令时添加--overwrite参数:
    ```
    electron-packager --platform=<platform> --arch=<arch> --overwrite .
    ```
    > [11:21 2017/3/15] Rapido目前支持的platform为win32和linux, arch为ia32和x64.

3. 输出:
    编译的exe文件输出到Rapido-<平台名称>-<平台架构>目录下, win32平台的x64架构系统中, 输出目录为Rapido-win32-x64.

4. 备注:
    > electron-packager更详细的使用说明请参考: [electron-packager](https://github.com/electron-userland/electron-packager)

### 打包安装文件
1. 准备
    安装打包工具electron-winstaller
    ```
    npm install electron-winstaller
    ```
2. 清理不需要的文件

    ./build目录为打包输出目录, 如果要进行一次打包操作, 需要将之前的打包输出删除掉, 否则会被再次打包到安装文件中.
3. 打包安装文件
    执行打包脚本: build_installer.js
    ```
    node build_installer.js
    ```
    其中build_installer.js内容如下:
    ```
    var electronInstaller = require('electron-winstaller');
    resultPromise = electronInstaller.createWindowsInstaller({
        appDirectory: './Rapido-win32-x64',
        outputDirectory: './build/Installer-win32-x64',
        authors: 'FTSafe Inc.',
        exe: 'Rapido.exe'
    });
    resultPromise.then(() => console.log("It worked!"), (e) => console.log("No dice: ${e.message}"));
    ```
    > [11:21 2017/3/15] 目前已添加多平台安装包的打包脚本, 请参考build_installer.js内容, 原理是一样的.
* 脚本中参数说明如下:
    * appDirectory: Electron应用所在目录
    * outputDirectory: 安装包输出目录
    * authors: 作者名称
    * exe: 应用的主程序名称

4. 备注:
    > electron-winstaller更详细的使用说明请参考: [electron-winstaller](https://github.com/electron/windows-installer)
