---
switcher-label: JavaAPI风格
---

<show-structure depth="2"/>

# 好友 OneBotFriend

<include from="snippets.md" element-id="to-main-doc" />

`OneBotFriend` 实现 `Contact`, `OneBotStrangerAware` 
和其他一些功能接口（后续 "更多能力" 中会介绍），
用于表示一个 OneBot11 协议中的 **好友**。

## Contact

`OneBotFriend` 实现来自 `Contact` 定义的抽象属性或函数。

<deflist>
<def title="id">QQ号。</def>
<def title="avatar">成员QQ头像。</def>
<def title="name">用户名。</def>
</deflist>

## SendSupport

`OneBotFriend` 拥有发送消息的能力。
使用 `send` 发送纯文本、消息或转发事件消息体。

<tabs group="code">
<tab title="Kotlin" group-key="Kotlin">

```Kotlin
val friend: OneBotFriend = ...
friend.send("text")
friend.send("text".toText() + Face(123.ID))
friend.send(messageContent)
```

</tab>
<tab title="Java" group-key="Java">

```Java
OneBotFriend friend = ...;
friend.sendAsync("text"); 
friend.sendAsync(
    Messages.builder()
        .add("text")
        .add(new Face(Identifies.of(123)))
        .build()
); 
friend.sendAsync(messageContent); 
```
{switcher-key=%ja%}

```Java
OneBotFriend friend = ...;
friend.sendBlocking("text");
friend.sendBlocking(
        Messages.builder()
            .add("text")
            .add(new Face(Identifies.of(123)))
            .build()
    );
friend.sendBlocking(messageContent);
```
{switcher-key=%jb%}

```Java
OneBotFriend friend = ...;
friend.sendReserve("text")
    .transform(SuspendReserves.mono())
    .subscribe();

friend.sendReserve(
    Messages.builder()
            .add("text")
            .add(new Face(Identifies.of(123)))
    .build()
    )
        .transform(SuspendReserves.mono())
    .subscribe();

friend.sendReserve(messageContent)
    .transform(SuspendReserves.mono())
    .subscribe();
```
{switcher-key=%jr%}

</tab>
</tabs>

## OneBotStrangerAware

`OneBotFriend` 实现 `OneBotStrangerAware`，
可以通过 `toStranger` 查询并得到一个对应的 `OneBotStranger` 类型。

<tabs group="code">
<tab title="Kotlin" group-key="Kotlin">

```Kotlin
val friend: OneBotFriend = ...
val stranger = friend.toStranger()
```

</tab>
<tab title="Java" group-key="Java">

```Java
OneBotFriend friend = ...;
friend.toStrangerAsync()
        .thenAccept(stranger -> {
            // ...
        });
```
{switcher-key=%ja%}

```Java
OneBotFriend friend = ...;
var stranger = friend.toStrangerBlocking();
```
{switcher-key=%jb%}

```Java
OneBotFriend friend = ...;
friend.toStrangerReserve()
        .transform(SuspendReserves.mono())
        .subscribe(stranger -> {
            // ... 
        });
```
{switcher-key=%jr%}

</tab>
</tabs>

## 更多能力

### SendLinkSupport

`OneBotFriend` 实现 `SendLinkSupport` 接口，
支持使用 `sendLike(Int)` 来点赞用户。

参数代表次数，一般来说一人一天最多共计10次赞，
但是代码内无校验，交给OneBot服务端处理。

<tabs group="code">
<tab title="Kotlin" group-key="Kotlin">

```Kotlin
val friend: OneBotFriend = ...
friend.sendLike(5)
```

</tab>
<tab title="Java" group-key="Java">

```Java
OneBotFriend friend = ...;
friend.sendLinkAsync(5);
```
{switcher-key=%ja%}

```Java
OneBotFriend friend = ...;
friend.sendLinkBlocking(5);
```
{switcher-key=%jb%}

```Java
OneBotFriend friend = ...;
friend.sendLinkReserve(5)
        .transform(SuspendReserves.mono())
        .subscribe();
```
{switcher-key=%jr%}

</tab>
</tabs>



## 获取 OneBotFriend

好友 `OneBotFriend` 通常来自
<a href="onebot11-OneBotBot.md" />
或与好友相关的事件。

### 来自事件

大多数跟好友相关的事件中都可以直接获取到 `OneBotFriend`。
通常来讲，如果事件主体与好友相关，那么就是 `content`，
如果侧面相关，例如某个好友消息事件中，
消息才是重点，而好友则为 `author`。
