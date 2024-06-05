# 事件

## 原始事件

我们根据 OneBot11 协议中定义的所有事件结构，
在模块 `simbot-component-onebot-v11-event` 中提供了所有的实现类型。
它们是最基础的可序列化数据类，不含有任何功能。

<deflist>
<def id="MetaEvent" title="元事件 MetaEvent">
<deflist>
<def id="HeartbeatEvent" title="HeartbeatEvent"></def>
<def id="LifecycleEvent" title="LifecycleEvent"></def>
</deflist>
</def>
<def id="MessageEvent" title="消息事件 MessageEvent">
<deflist>
<def id="GroupMessageEvent" title="GroupMessageEvent"></def>
<def id="PrivateMessageEvent" title="PrivateMessageEvent"></def>
</deflist>
</def>
<def id="RequestEvent" title="请求事件 RequestEvent">
<deflist>
<def id="FriendRequestEvent" title="FriendRequestEvent"></def>
<def id="GroupRequestEvent" title="GroupRequestEvent"></def>
</deflist>
</def>
<def id="NoticeEvent" title="通知事件 NoticeEvent">
<deflist>
<def id="FriendAddEvent" title="FriendAddEvent"></def>
<def id="FriendRecallEvent" title="FriendRecallEvent"></def>
<def id="GroupAdminEvent" title="GroupAdminEvent"></def>
<def id="GroupBanEvent" title="GroupBanEvent"></def>
<def id="GroupDecreaseEvent" title="GroupDecreaseEvent"></def>
<def id="GroupIncreaseEvent" title="GroupIncreaseEvent"></def>
<def id="GroupRecallEvent" title="GroupRecallEvent"></def>
<def id="GroupUploadEvent" title="GroupUploadEvent"></def>
<def id="NotifyEvent" title="NotifyEvent"></def>
</deflist>
</def>
<def id="UnknownEvent" title="未知事件 UnknownEvent">

一个特殊的事件类型。
它是当遇到无法被上述这些类型所解析时使用的兜底类型，
它不可序列化，除 `time`、`selfId`、`postType` 
之外直接提供 `raw` 属性(也就是原始的JSON字符串)。

</def>
</deflist>

## 组件事件

组件实现是基于simbot标准API中定义的事件类型、对上述**原始事件**的包装，
并提供相对应的功能性API实现。

### 消息事件

<deflist>
<def id="OneBotMessageEvent" title="OneBotMessageEvent">
与消息相关的事件。

<deflist>
<def id="OneBotGroupMessageEvent" title="OneBotGroupMessageEvent">
与群消息相关的事件。

<deflist>
<def id="OneBotNormalGroupMessageEvent" title="OneBotNormalGroupMessageEvent">
与普通群消息相关的事件。
</def>
<def id="OneBotAnonymousGroupMessageEvent" title="OneBotAnonymousGroupMessageEvent">
与匿名群消息相关的事件。
</def>
<def id="OneBotNoticeGroupMessageEvent" title="OneBotNoticeGroupMessageEvent">
与群系统消息通知相关的事件。
</def>
</deflist>

</def>
<def id="OneBotPrivateMessageEvent" title="OneBotPrivateMessageEvent">
与私聊消息相关的事件。

<deflist>
<def id="OneBotFriendMessageEvent" title="OneBotFriendMessageEvent">
好友私信消息事件。
</def>
<def id="OneBotGroupPrivateMessageEvent" title="OneBotGroupPrivateMessageEvent">
群成员临时会话消息事件。
</def>
</deflist>

</def>
</deflist>

</def>
</deflist>



### 未知事件

`OneBotUnknownEvent`

是对上述原始事件中的 `UnknownEvent` 类型的包装。

### 未支持事件

`OneBotUnsupportedEvent`

是当出现了原始事件中有(除了 `UnknownEvent`)、
但是尚未提供对应的**组件事件**实现的事件类型时用来兜底的类型。

这些原始事件类型会被统一装入此事件中。
