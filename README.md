## 索引
- [概述](#概述)
- [开源及环境说明](#开源及环境说明)
- [项目更新](#项目更新)
- [项目说明](#项目说明)
- [关联项目](#关联项目)
- [关联QQ群](#关联QQ群)

## 概述

当前项目是由蘑菇街的TeamTalk协议，完全用JAVA生态重构而成。以用于线上业务使用，并适当进行了调整，基本上还是与原Teamtalk协议保持兼容。

大家在开发过程中遇到问题，请提交到[issues](https://github.com/ccfish86/sctalk/issues/)。
具体文档见doc目录下,安装之前请仔细阅读相关文档。

### 线上测试地址：
* 登录服务器`http://tt.hifipi.com/msg_server` 用户名密码 10~99
* [管理后台](http://tt.hifipi.com/admin/)

### 联系方式：
* qq:88624053
* EMAIL: ccfish@ccfish.net
* 亦可加入[QQ群](#关联QQ群)与其他Teamtalk用户进行交流

## 开源及环境说明

### 免责声明

该项目是个人维护，尽量继续维护下去，此外代码借鉴了部分[开源项目](#关联项目)。

本项目完全免费使用，相关问题皆免费提供解答（任何收费行为皆为假冒），暂不接受定制开发。

本人不对使用此代码所造成的损失承担连带责任。

当然如果引用的第三方开源代码，侵犯了您的权益，请及时联系，会在第一时间删除。

### 环境

本项目中的服务器环境，使用JDK8 + PostgreSQL/MySQL + Redis + minio(图片及语音消息使用) + nginx。

内存2G以上即可，对系统其他组件的版本无要求。

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

## 关联项目

[蘑菇街Teamtalk] (https://github.com/meili/TeamTalk)

[Teamtalk之Java版管理后台] (https://github.com/Seeyouenough/TeamTalkOverwrite)

[Teamtalk之Web版前台] (https://github.com/xiaominfc/teamtalk_websocket_client)

[Teamtalk之Node版前台] (https://gitee.com/mayuehehe/rapido/tree/master/)

## 关联QQ群
TeamTalk交流群: 462424781
TeamTalk第一小队: 419850589

## 关于打赏
尽管你是否打赏并不影响我更新及相关问题的解答，如果你的确任性+土豪，那就来吧。
![支付宝](https://jvue.ccfish.net/1527212947124.jpg)
