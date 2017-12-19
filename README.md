当前项目是由蘑菇街的TeamTalk改造而来

开源促进人类进步，`公司`也比较支持，只是想请各位朋友多多提BUG。
具体文档见doc目录下,安装之前请仔细阅读相关文档。

交流：

建议大家在开发过程中遇到问题,提交issues到[https://github.com/ccfish86/sctalk/issues](https://github.com/ccfish86/sctalk/issues/)

由于该项目是个人维护，不一定有时间及时上传最新代码，另外代码借鉴了部分开源项目（可能没来得及修改注释，如果侵犯了你的利益。可联系我，我会及时改正）。

* 其他问题可加qq:88624053或Email进行交流。
* EMAL: ccfish@ccfish.net

线上测试地址：`http://tt.hifipi.com/msg_server` 用户名密码 10~99

---

版本[0.1.0-RELEASE]使用了router-server作为消息中转服务（这点跟原TT是相似的），0.2以后将使用hazelcast等分布式中间件取代router-server来处理消息的“漫游”。

取消“router-server”，更多的是为了解决路由服务的单点故障的问题，之后会追加zuul来对外提供服务。

2017-12-15 更新内容：

第一步是把router删除，把关键的msgServerManager，userClintInfoManager等功能（服务/用户在线管理）重新调整，用hazelcast的分布式map来修改了一下。

然后把router<>msg的处理分了几种情况，如：请求用户在线状态的，现在直接可以通过hazelcast#map本地处理。本地处理不了的，通过hazelcast#topic发布到其他msg-server上分别处理。

现在，已基本实现了功能，有条件的话，对于某些访问比较集中的消息类型还可以做以下优化：

 - 先通过map把需要处理该的msg服务给查出来，然后只发布到这些个msg服务。
 - 一些处理，可以通过hazelcast#ExecutorService直接处理

---

希望大家一起努力，把这个项目完善起来。

多提供意见也是对我们最大的支持。