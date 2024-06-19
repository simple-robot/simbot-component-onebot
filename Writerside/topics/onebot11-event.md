# 事件

## 原始事件

我们根据 OneBot11 协议中定义的所有事件结构，
在模块 `simbot-component-onebot-v11-event` 中提供了所有的实现类型。
它们是最基础的可序列化数据类，不含有任何功能。

<deflist>
<def id="RawMetaEvent" title="元事件 RawMetaEvent">
<deflist>
<def id="RawHeartbeatEvent" title="RawHeartbeatEvent"></def>
<def id="RawLifecycleEvent" title="RawLifecycleEvent"></def>
</deflist>
</def>
<def id="RawMessageEvent" title="消息事件 RawMessageEvent">
<deflist>
<def id="RawGroupMessageEvent" title="RawGroupMessageEvent"></def>
<def id="RawPrivateMessageEvent" title="RawPrivateMessageEvent"></def>
</deflist>
</def>
<def id="RawRequestEvent" title="请求事件 RawRequestEvent">
<deflist>
<def id="RawFriendRequestEvent" title="RawFriendRequestEvent"></def>
<def id="RawGroupRequestEvent" title="RawGroupRequestEvent"></def>
</deflist>
</def>
<def id="RawNoticeEvent" title="通知事件 RawNoticeEvent">
<deflist>
<def id="RawFriendAddEvent" title="RawFriendAddEvent"></def>
<def id="RawFriendRecallEvent" title="RawFriendRecallEvent"></def>
<def id="RawGroupAdminEvent" title="RawGroupAdminEvent"></def>
<def id="RawGroupBanEvent" title="RawGroupBanEvent"></def>
<def id="RawGroupDecreaseEvent" title="RawGroupDecreaseEvent"></def>
<def id="RawGroupIncreaseEvent" title="RawGroupIncreaseEvent"></def>
<def id="RawGroupRecallEvent" title="RawGroupRecallEvent"></def>
<def id="RawGroupUploadEvent" title="RawGroupUploadEvent"></def>
<def id="RawNotifyEvent" title="RawNotifyEvent"></def>
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
<def id="OneBotMetaEvent" title="OneBotMetaEvent">
元数据相关的事件
<deflist>
<def id="OneBotLifecycleEvent" title="OneBotLifecycleEvent">
生命周期事件
</def>
<def id="OneBotHeartbeatEvent" title="OneBotHeartbeatEvent">
心跳事件
</def>
</deflist>
</def>
<def id="OneBotRequestEvent" title="OneBotRequestEvent">
请求相关的事件
<deflist>
<def id="OneBotFriendRequestEvent" title="OneBotFriendRequestEvent">
好友添加申请
</def>
<def id="OneBotGroupRequestEvent" title="OneBotGroupRequestEvent">
群添加申请
</def>
</deflist>
</def>
<def id="OneBotNoticeEvent" title="OneBotNoticeEvent">
通知相关的事件。
<deflist>
<def id="OneBotFriendAddEvent" title="OneBotFriendAddEvent">好友已添加事件</def>
<def id="OneBotFriendRecallEvent" title="OneBotFriendRecallEvent">好友消息撤回事件</def>
<def id="OneBotGroupAdminEvent" title="OneBotGroupAdminEvent">管理员变动事件</def>
<def id="OneBotGroupBanEvent" title="OneBotGroupBanEvent">群禁言事件</def>
<def id="OneBotGroupChangeEvent" title="OneBotGroupChangeEvent">群成员变动事件</def>
<def id="OneBotGroupMemberIncreaseEvent" title="OneBotGroupMemberIncreaseEvent">群成员添加事件</def>
<def id="OneBotGroupMemberDecreaseEvent" title="OneBotGroupMemberDecreaseEvent">群成员离开事件</def>
<def id="OneBotGroupRecallEvent" title="OneBotGroupRecallEvent">群消息撤回事件</def>
<def id="OneBotGroupUploadEvent" title="OneBotGroupUploadEvent">群文件上传事件</def>
<def id="OneBotNotifyEvent" title="OneBotNotifyEvent">群荣耀事件、红包人气王事件或戳一戳事件</def>
<def id="OneBotHonorEvent" title="OneBotHonorEvent">群荣耀事件</def>
<def id="OneBotLuckyKingEvent" title="OneBotLuckyKingEvent">红包人气王事件</def>
<def id="OneBotPokeEvent" title="OneBotPokeEvent">戳一戳事件</def>
<def id="OneBotMemberPokeEvent" title="OneBotMemberPokeEvent">戳一戳(普通群成员被戳)事件</def>
<def id="OneBotBotSelfPokeEvent" title="OneBotBotSelfPokeEvent">戳一戳(Bot被戳)事件</def>
</deflist>
</def>

<def id="OneBotBotStageEvent" title="OneBotBotStageEvent">
与OneBot协议本身无关的Bot的阶段事件。
<deflist>
<def id="OneBotBotRegisteredEvent" title="OneBotBotRegisteredEvent">
一个 `OneBotBot` 被注册了的事件
</def>
<def id="OneBotBotStartedEvent" title="OneBotBotStartedEvent">
一个 `OneBotBot` 被(首次)启动了的事件
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

## 事件关系

简单列举一下原始事件与可能对应的组件事件之间的关系。

| 原始事件类型                                              | 组件事件                                   |
|-----------------------------------------------------|----------------------------------------|
| `RawMetaEvent`                                      | `OneBotMetaEvent`                      |
| > `RawLifecycleEvent`                               | > `OneBotLifecycleEvent`               |
| > `RawHeartbeatEvent`                               | > `OneBotHeartbeatEvent`               |
| `RawMessageEvent`                                   | `OneBotMessageEvent`                   |
| > `RawGroupMessageEvent`                            | > `OneBotGroupMessageEvent`            |
| > `RawGroupMessageEvent`                            | > > `OneBotNormalGroupMessageEvent`    |
| > `RawGroupMessageEvent`                            | > > `OneBotAnonymousGroupMessageEvent` |
| > `RawGroupMessageEvent`                            | > > `OneBotNoticeGroupMessageEvent`    |
| > `RawPrivateMessageEvent`                          | > `OneBotPrivateMessageEvent`          |
| > `RawPrivateMessageEvent`                          | > > `OneBotFriendMessageEvent`         |
| > `RawPrivateMessageEvent`                          | > > `OneBotGroupPrivateMessageEvent`   |
| `RawRequestEvent`                                   | `OneBotRequestEvent`                   |
| > `RawFriendRequestEvent`                           | > `OneBotFriendRequestEvent`           |
| > `RawGroupRequestEvent`                            | > `OneBotGroupRequestEvent`            |
| `RawNoticeEvent`                                    | `OneBotNoticeEvent`                    |
| > `RawFriendAddEvent`                               | > `OneBotFriendAddEvent`               |
| > `RawFriendRecallEvent`                            | > `OneBotFriendRecallEvent`            |
| > `RawGroupAdminEvent`                              | > `OneBotGroupAdminEvent`              |
| > `RawGroupBanEvent`                                | > `OneBotGroupBanEvent`                |
| > `RawGroupIncreaseEvent` 或 `RawGroupDecreaseEvent` | > `OneBotGroupChangeEvent`             |
| > `RawGroupIncreaseEvent`                           | > > `OneBotGroupMemberIncreaseEvent`   |
| > `RawGroupDecreaseEvent`                           | > > `OneBotGroupMemberDecreaseEvent`   |
| > `RawGroupRecallEvent`                             | > `OneBotGroupRecallEvent`             |
| > `RawGroupUploadEvent`                             | > `OneBotGroupUploadEvent`             |
| > `RawNotifyEvent`                                  | > `OneBotNotifyEvent`                  |
| > `RawNotifyEvent`                                  | > > `OneBotHonorEvent`                 |
| > `RawNotifyEvent`                                  | > > `OneBotLuckyKingEvent`             |
| > `RawNotifyEvent`                                  | > > `OneBotPokeEvent`                  |
| > `RawNotifyEvent`                                  | > > > `OneBotMemberPokeEvent`          |
| > `RawNotifyEvent`                                  | > > > `OneBotBotSelfPokeEvent`         |
| `UnknownEvent`                                      | > `UnknownEvent`                       |
| 无                                                   | `OneBotBotStageEvent`                  |
| 无                                                   | > `OneBotBotRegisteredEvent`           |
| 无                                                   | > `OneBotBotStartedEvent`              |
| 任意未支持事件                                             | `OneBotUnsupportedEvent`               |
