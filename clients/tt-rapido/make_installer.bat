IF EXIST build. RMDIR /S /Q build.
IF EXIST Rapido-win32-ia32. RMDIR /S /Q Rapido-win32-ia32.
IF EXIST Rapido-win32-x64. RMDIR /S /Q Rapido-win32-x64.
IF EXIST Rapido-linux-armv7l. RMDIR /S /Q Rapido-linux-armv7l.
IF EXIST Rapido-linux-ia32. RMDIR /S /Q Rapido-linux-ia32.
IF EXIST Rapido-linux-x64. RMDIR /S /Q Rapido-linux-x64.
IF EXIST Rapido-darwin-x64. RMDIR /S /Q Rapido-darwin-x64.
IF EXIST Rapido-mas-x64. RMDIR /S /Q Rapido-mas-x64.

CALL .\node_modules\.bin\electron-packager --platform=win32 --arch=ia32 --overwrite .
CALL .\node_modules\.bin\electron-packager --platform=win32 --arch=x64 --overwrite .
REM CALL .\node_modules\.bin\electron-packager --platform=linux --arch=ia32 --overwrite .
REM CALL .\node_modules\.bin\electron-packager --platform=linux --arch=x64 --overwrite .
REM CALL .\node_modules\.bin\electron-packager --platform=darwin --arch=x64 --overwrite .

CALL .\node_modules\.bin\electron-packager --win32metadata.ProductName=Rapido --win32metadata.InternalName=Rapido --win32metadata.FileDescriptioni="FTSafe teamtalk." --win32metadata.OriginalFilename=Rapido.exe --platform=win32 --arch=ia32 --overwrite .
CALL .\node_modules\.bin\electron-packager --win32metadata.ProductName=Rapido --win32metadata.InternalName=Rapido --win32metadata.FileDescriptioni="FTSafe teamtalk." --win32metadata.OriginalFilename=Rapido.exe --platform=win32 --arch=x64 --overwrite .
REM CALL .\node_modules\.bin\electron-packager --win32metadata.ProductName=Rapido --win32metadata.InternalName=Rapido --win32metadata.FileDescriptioni="FTSafe teamtalk." --win32metadata.OriginalFilename=Rapido.exe --platform=linux --arch=ia32 --overwrite .
REM CALL .\node_modules\.bin\electron-packager --win32metadata.ProductName=Rapido --win32metadata.InternalName=Rapido --win32metadata.FileDescriptioni="FTSafe teamtalk." --win32metadata.OriginalFilename=Rapido.exe --platform=linux --arch=x64 --overwrite .
REM CALL .\node_modules\.bin\electron-packager --win32metadata.ProductName=Rapido --win32metadata.InternalName=Rapido --win32metadata.FileDescriptioni="FTSafe teamtalk." --win32metadata.OriginalFilename=Rapido.exe --platform=darwin --arch=x64 --overwrite .

node build_installer.js
