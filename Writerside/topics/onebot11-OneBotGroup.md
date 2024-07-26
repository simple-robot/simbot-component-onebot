---
switcher-label: JavaAPI风格
---

<show-structure depth="2"/>

# 群 OneBotGroup

<include from="snippets.md" element-id="to-main-doc" />

`OneBotGroup` 实现 `ChatGroup` 和 `DeleteSupport`，
用于表示一个 OneBot11 协议中的 **群聊**。

## ChatGroup

`OneBotGroup` 实现来自 `ChatGroup` 定义的抽象属性或函数。

<deflist>
<def title="id">群号。</def>
<def title="name">群名称。</def>
<def title="roles">

群可用角色列表。此处将始终得到枚举类型 `OneBotMemberRole`
的元素列表。
</def>
<def title="members">获取群成员列表。</def>
<def title="member(...)">
根据参数（例如ID）寻找指定的群成员。
</def>
<def title="botAsMember()">
将当前Bot作为在群中的成员。
</def>
<def title="memberCount">成员数。</def>
<def title="maxMemberCount">最大成员数（群容量）。</def>
</deflist>

## SendSupport

`OneBotGroup` 拥有发送消息的能力。
使用 `send` 发送纯文本、消息或转发事件消息体。

<tabs group="code">
<tab title="Kotlin" group-key="Kotlin">

```Kotlin
val group: OneBotGroup = ...
group.send("text")
group.send("text".toText() + At(123.ID))
group.send(messageContent)
```

</tab>
<tab title="Java" group-key="Java">

```Java
OneBotGroup group = ...;
group.sendAsync("text"); 
group.sendAsync(
    Messages.builder()
        .add("text")
        .add(new At(Identifies.of(123)))
        .build()
); 
group.sendAsync(messageContent); 

```
{switcher-key=%ja%}

```Java
OneBotGroup group = ...;
group.sendBlocking("text");
group.sendBlocking(
        Messages.builder()
            .add("text")
            .add(new At(Identifies.of(123)))
            .build()
    );
group.sendBlocking(messageContent);
```
{switcher-key=%jb%}

```Java
OneBotGroup group = ...;
group.sendReserve("text")
    .transform(SuspendReserves.mono())
    .subscribe();

group.sendReserve(
    Messages.builder()
            .add("text")
            .add(new At(Identifies.of(123)))
    .build()
    )
        .transform(SuspendReserves.mono())
    .subscribe();

group.sendReserve(messageContent)
    .transform(SuspendReserves.mono())
    .subscribe();
```
{switcher-key=%jr%}

</tab>
</tabs>

## DeleteSupport

`OneBotGroup` 实现接口 `DeleteSupport`，代表其支持"删除"能力。
在这里，删除即表示使Bot离开/退出这个群。

<tabs group="code">
<tab title="Kotlin" group-key="Kotlin">

```Kotlin
val group: OneBotGroup = ...
group.delete()
group.delete(OneBotGroupDeleteOption.Dismiss)
```

</tab>
<tab title="Java" group-key="Java">

```Java
OneBotGroup group = ...;
group.deleteAsync(); 
group.deleteAsync(OneBotGroupDeleteOption.dismiss()); 
group.deleteAsync(
    OneBotGroupDeleteOption.dismiss(),
    StandardDeleteOption.IGNORE_ON_FAILURE
); 
```
{switcher-key=%ja%}

```Java
OneBotGroup group = ...;
group.deleteBlocking("text");
group.deleteBlocking(OneBotGroupDeleteOption.dismiss());
group.deleteBlocking(
    OneBotGroupDeleteOption.dismiss(),
    StandardDeleteOption.IGNORE_ON_FAILURE
);
```
{switcher-key=%jb%}

```Java
OneBotGroup group = ...;
group.deleteReserve()
    .transform(SuspendReserves.mono())
    .subscribe();

group.deleteReserve(OneBotGroupDeleteOption.dismiss())
    .transform(SuspendReserves.mono())
    .subscribe();

group.deleteReserve(
        OneBotGroupDeleteOption.dismiss(),
        StandardDeleteOption.IGNORE_ON_FAILURE
    )
    .transform(SuspendReserves.mono())
    .subscribe();
```
{switcher-key=%jr%}

</tab>
</tabs>

可以注意到，`delete` 支持可变参数 `options`。
在 `OneBotGroup` 中，它支持如下的可选属性：

<deflist>
<def title="StandardDeleteOption.IGNORE_ON_FAILURE">
忽略调用过程中可能会产生的任何异常。
</def>
<def title="OneBotGroupDeleteOption.*">

`OneBotGroupDeleteOption` 的所有子类型。

<deflist>
<def title="Dismiss">

是否为解散群。如果bot为群主，
则需要提供此参数来使 `delete` 解散群，
否则无法解散或退出。
</def>
</deflist>

</def>
</deflist>

## 更多能力

### 全群禁言

可以通过 `ban(Boolean)` 来设置群名称。
通常需要bot拥有管理权限。

<tabs group="code">
<tab title="Kotlin" group-key="Kotlin">

```Kotlin
val group: OneBotGroup = ...
group.ban(true) // true开启，false关闭
```

</tab>
<tab title="Java" group-key="Java">

```Java
OneBotGroup group = ...;
group.banAsync(true); // true开启，false关闭
```
{switcher-key=%ja%}

```Java
OneBotGroup group = ...;
group.banBlocking(true); // true开启，false关闭
```
{switcher-key=%jb%}

```Java
OneBotGroup group = ...;
group.banReserve("newName") // true开启，false关闭
    .transform(SuspendReserves.mono())
    .subscribe();
```
{switcher-key=%jr%}

</tab>
</tabs>

### 设置群名

可以通过 `setName(String)` 来设置群名称。
通常需要bot拥有管理权限。

<tabs group="code">
<tab title="Kotlin" group-key="Kotlin">

```Kotlin
val group: OneBotGroup = ...
group.setName("newName")
```

</tab>
<tab title="Java" group-key="Java">

```Java
OneBotGroup group = ...;
group.setNameAsync("newName"); 
```
{switcher-key=%ja%}

```Java
OneBotGroup group = ...;
group.setNameBlocking("newName");
```
{switcher-key=%jb%}

```Java
OneBotGroup group = ...;
group.setNameReserve("newName")
    .transform(SuspendReserves.mono())
    .subscribe();
```
{switcher-key=%jr%}

</tab>
</tabs>

### 设置bot群备注

可以通过 `setBotGroupNick(String?)` 来设置bot在群内的群备注。

<tabs group="code">
<tab title="Kotlin" group-key="Kotlin">

```Kotlin
val group: OneBotGroup = ...
group.setBotGroupNick("newNick")
```

</tab>
<tab title="Java" group-key="Java">

```Java
OneBotGroup group = ...;
group.setBotGroupNickAsync("newNick"); 
```
{switcher-key=%ja%}

```Java
OneBotGroup group = ...;
group.setBotGroupNickBlocking("newNick");
```
{switcher-key=%jb%}

```Java
OneBotGroup group = ...;
group.setBotGroupNickReserve("newNick")
    .transform(SuspendReserves.mono())
    .subscribe();
```
{switcher-key=%jr%}

</tab>
</tabs>

### 设置管理员

可以通过 `setAdmin(ID, Boolean)` 来设置群内的管理。
通常需要bot拥有群主权限。

<tabs group="code">
<tab title="Kotlin" group-key="Kotlin">

```Kotlin
val group: OneBotGroup = ...
group.setAdmin(memberId, true)
```

</tab>
<tab title="Java" group-key="Java">

```Java
OneBotGroup group = ...;
group.setAdminAsync(memberId, true); 
```
{switcher-key=%ja%}

```Java
OneBotGroup group = ...;
group.setAdminBlocking(memberId, true);
```
{switcher-key=%jb%}

```Java
OneBotGroup group = ...;
group.setAdminReserve(memberId, true)
    .transform(SuspendReserves.mono())
    .subscribe();
```
{switcher-key=%jr%}

</tab>
</tabs>

### 获取荣誉信息

可以通过 `getHonorInfo(String)` 和 `getAllHonorInfo()` 
来获取群内的荣誉信息。

<tabs group="code">
<tab title="Kotlin" group-key="Kotlin">

```Kotlin
val group: OneBotGroup = ...
group.getHonorInfo("talkative")
group.getAllHonorInfo()
```

</tab>
<tab title="Java" group-key="Java">

```Java
OneBotGroup group = ...;
group.getHonorInfoAsync("talkative"); 
group.getAllHonorInfoAsync(); 
```
{switcher-key=%ja%}

```Java
OneBotGroup group = ...;
group.getHonorInfoBlocking("talkative");
group.getAllHonorInfoBlocking();
```
{switcher-key=%jb%}

```Java
OneBotGroup group = ...;
group.getHonorInfoReserve("talkative")
    .transform(SuspendReserves.mono())
    .subscribe();

group.getAllHonorInfoReserve()
    .transform(SuspendReserves.mono())
    .subscribe();
```
{switcher-key=%jr%}

</tab>
</tabs>

`getHonorInfo` 的参数 `type` 的可选值通常有：

- `all`
- `talkative`
- `performer`
- `legend`
- `strong_newbie`
- `emotion`

### 设置匿名聊天

可以通过 `setAnonymous(Boolean)` 来设置是否允许匿名聊天，`true` 为开启。

> 这可能需要bot拥有管理权限。

<tabs group="code">
<tab title="Kotlin" group-key="Kotlin">

```Kotlin
val group: OneBotGroup = ...
group.setAnonymous(true)
```

</tab>
<tab title="Java" group-key="Java">

```Java
OneBotGroup group = ...;
group.setAnonymousAsync(true); 
```
{switcher-key=%ja%}

```Java
OneBotGroup group = ...;
group.setAnonymousBlocking(true);
```
{switcher-key=%jb%}

```Java
OneBotGroup group = ...;
group.setAnonymousReserve(true)
    .transform(SuspendReserves.mono())
    .subscribe();
```
{switcher-key=%jr%}

</tab>
</tabs>

## 获取 OneBotGroup

`OneBotGroup` 主要来自 `OneBotBot` 获取或与群相关的事件。

### 来自Bot

使用 `OneBotBot` 的 `GroupRelation` 获取群列表或寻找某个指定的群。

前往
<a href="onebot11-OneBotBot.md" />
了解更多。

### 来自事件

大多数跟群相关的事件中都可以直接获取到 `OneBotGroup`。
通常来讲，如果事件主体与群相关，那么就是 `content`，
如果侧面相关（例如某个群成员事件，这里群成员才是主体），
那么通常是 `source` 或 `group`。
