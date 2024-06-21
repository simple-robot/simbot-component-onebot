# OneBot11

OneBot组件是一个对
[OneBot11协议](https://github.com/botuniverse/onebot-11)
的**客户端**实现。

## 交互方式

OneBot组件选择使用 
[正向 HTTP](https://github.com/botuniverse/onebot-11/blob/master/communication/http.md)
作为API交互方式、
[正向 WebSocket](https://github.com/botuniverse/onebot-11/blob/master/communication/ws.md)
作为事件订阅的方式。

简单来说，就是不论是API交互还是事件订阅，都由OneBot组件作为**主动方**：主动发起HTTP请求、主动发起WebSocket连接。

## 反向？

如果你真的想要通过反向HTTP来接收事件推送，那么你需要自行搭建 HTTP 服务端，然后使用 `OneBotBot.push` 手动推送原始事件的JSON字符串。
你可以前往参考 
`OneBotBot` 的
<a href="onebot11-OneBotBot.md#外部事件" /> 。
