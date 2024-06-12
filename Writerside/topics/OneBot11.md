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
