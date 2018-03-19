说明

* 这是用来替代TeamTalk的业务服务器，当然不仅仅是“翻译”
* 此外，把原来的一部分逻辑重新组织过了
* 对上层服务，提供Rest接口调用，使用了Spring Eureka和 Feign处理调用的问题
* 至于接口的安全性，之后会追加Spring-security等方式进行限制
