2018-02-24 升级protobuf版本为3.4

由于生成工具只支持`syntax = "proto3";`，这里只提供可以正常生成cs文件的protobuf定义，如果需要开发csharp下的程序，最好把服务器端及其他客户端一并升级到`proto3`；