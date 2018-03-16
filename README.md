## 索引
- [概述](#概述)
- [第三方说明及测试环境](#第三方说明及测试环境)
- [项目更新](#项目更新)
- [项目说明](#项目说明)
- [关联项目](#关联项目)
- [关联QQ群](#关联QQ群)

## 概述
当前项目是由蘑菇街的TeamTalk改造而来

开源促进人类进步，`公司`也比较支持，只是想请各位朋友多多提BUG。
具体文档见doc目录下,安装之前请仔细阅读相关文档。

线上测试地址：`http://tt.hifipi.com/msg_server` 用户名密码 10~99

## 第三方说明及测试环境

建议大家在开发过程中遇到问题,提交issues到[https://github.com/ccfish86/sctalk/issues](https://github.com/ccfish86/sctalk/issues/)

由于该项目是个人维护，不一定有时间及时上传最新代码，另外代码借鉴了部分开源项目。本处代码完全免费使用，相关问题皆免费提供解答（任何收费行为皆为假冒），暂不接受定制开发。

* 其他问题可加qq:88624053或Email进行交流。
* EMAIL: ccfish@ccfish.net

另外，推荐一个java版的管理后台：[Seeyouenough/TeamTalkOverwrite](https://github.com/Seeyouenough/TeamTalkOverwrite)
>> 测试地址：[http://tt.hifipi.com/admin/](http://tt.hifipi.com/admin/)

## 项目更新

2018-02-08 支持websocket，可与现APP互通。
web端地址:[teamtalk_websocket_client](http://tt.hifipi.com/teamtalk_websocket_client/)

websocket使用客户端：[xiaominfc/teamtalk_websocket_client](https://github.com/xiaominfc/teamtalk_websocket_client)

版本[0.1.0-RELEASE]使用了router-server作为消息中转服务（这点跟原TT是相似的），0.2以后将使用hazelcast等分布式中间件取代router-server来处理消息的“路由”。

取消“router-server”，更多的是为了解决路由服务的单点故障的问题，之后会追加zuul来对外提供服务。

2017-12-15 更新内容：

第一步是把router删除，把关键的msgServerManager，userClintInfoManager等功能（服务/用户在线管理）重新调整，用hazelcast的分布式map来修改了一下。

然后把router<>msg的处理分了几种情况，如：请求用户在线状态的，现在直接可以通过hazelcast#map本地处理。本地处理不了的，通过hazelcast#topic发布到其他msg-server上分别处理。

现在，已基本实现了功能，有条件的话，对于某些访问比较集中的消息类型还可以做以下优化：

 - 先通过map把需要处理该的msg服务给查出来，然后只发布到这些个msg服务。
 - 一些处理，可以通过hazelcast#ExecutorService直接处理

## 项目说明
项目主要有以下几部分：
 1. spring cloud相关的服务，如：eureka
 2. 底层基础设施，数据库(支持postgresql和mysql，或者其他)，redis
 3. message-server消息服务，DB-proxy服务。由于采用了hazelcast来处理了一些集群方面的东西，可以支持message-server多服务器，甚至message-server(socket)和message-server(websocket)互通
 4. 正在处理webrtc的视频通话，希望有生之年可以搞定:-)

签于本站坑爹的字数限制，再唠叨几句。

原Teamtalk（下面简称TT）的架构和功能就不详细介绍了，说点不一样的。

 - 开发第一版的时候，跟TT一样有message-server、router-server和db-proxy，并且达到了多message-server相通的效果。后续接触到了hazelcast，并试着把router-server用topic和excuteService给取代了。
 - 在开发db-proxy时，用了hibernate，然后顺便把字段名给调整了下。
 - 把一些表的主键（ID），由Integer改为了Long，以避免数据量大时会超过Integer的上限。
 - 同时支持socket和websocket连接，并能互发消息。
 - 整理了一些TT相关的第三方开源项目，并做了测试服务器。


希望大家一起努力，把这个项目完善起来。

多提供意见也是对我们最大的支持。

## 关联项目
[蘑菇街Teamtalk] (https://github.com/meili/TeamTalk)
[Teamtalk之Java版管理后台] (https://github.com/Seeyouenough/TeamTalkOverwrite)
[Teamtalk之Web版前台] (https://github.com/xiaominfc/teamtalk_websocket_client)
[Teamtalk之Node版前台] (https://gitee.com/mayuehehe/rapido/tree/master/)

##关联QQ群
TeamTalk交流群: 462424781
TeamTalk第一小队: 419850589