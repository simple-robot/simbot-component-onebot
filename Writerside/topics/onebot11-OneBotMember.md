---
switcher-label: JavaAPI风格
---

<show-structure depth="2"/>

#  群成员 OneBotMember

`OneBotMember` 实现 `Member`, `OneBotStrangerAware`
以及其他一些功能接口（后续 "更多能力" 中会介绍），
用于表示一个 OneBot11 协议中的 **群成员**。

## Member

`OneBotMember` 实现来自 `Member` 定义的抽象属性或函数。

<deflist>
<def title="id">QQ号。</def>
<def title="avatar">成员QQ头像。</def>
<def title="name">用户名。</def>
<def title="nick">
在群内的昵称，
一些地方（比如协议中）也会称其为 <code>card</code> 。
</def>
</deflist>

## SendSupport

`OneBotMember` 拥有发送消息的能力。
使用 `send` 发送纯文本、消息或转发事件消息体。

<warning>

由bot主动发起一个临时会话可能会有一些问题，
有一些OneBot服务端实现可能根本不支持。

建议优先**不考虑**使用临时会话。

</warning>

<tabs group="code">
<tab title="Kotlin" group-key="Kotlin">

```Kotlin
val member: OneBotMember = ...
member.send("text")
member.send("text".toText() + Face(123.ID))
member.send(messageContent)
```

</tab>
<tab title="Java" group-key="Java">

```Java
OneBotMember member = ...;
member.sendAsync("text"); 
member.sendAsync(
    Messages.builder()
        .add("text")
        .add(new Face(Identifies.of(123)))
        .build()
); 
member.sendAsync(messageContent); 
```
{switcher-key=%ja%}

```Java
OneBotMember member = ...;
member.sendBlocking("text");
member.sendBlocking(
        Messages.builder()
            .add("text")
            .add(new Face(Identifies.of(123)))
            .build()
    );
member.sendBlocking(messageContent);
```
{switcher-key=%jb%}

```Java
OneBotMember member = ...;
member.sendReserve("text")
    .transform(SuspendReserves.mono())
    .subscribe();

member.sendReserve(
    Messages.builder()
            .add("text")
            .add(new Face(Identifies.of(123)))
    .build()
    )
        .transform(SuspendReserves.mono())
    .subscribe();

member.sendReserve(messageContent)
    .transform(SuspendReserves.mono())
    .subscribe();
```
{switcher-key=%jr%}

</tab>
</tabs>

## DeleteSupport

`OneBotMember` 实现接口 `DeleteSupport`，代表其支持"删除"能力。
在这里，删除即表示将这个成员踢出群。

> 这通常需要bot拥有管理权限。

<tabs group="code">
<tab title="Kotlin" group-key="Kotlin">

```Kotlin
val member: OneBotMember = ...
member.delete()
member.delete(OneBotMemberDeleteOption.RejectRequest)
```

</tab>
<tab title="Java" group-key="Java">

```Java
OneBotMember member = ...;
member.deleteAsync(); 
member.deleteAsync(OneBotMemberDeleteOption.rejectRequest()); 
member.deleteAsync(
    OneBotMemberDeleteOption.rejectRequest(),
    StandardDeleteOption.IGNORE_ON_FAILURE
); 
```
{switcher-key=%ja%}

```Java
OneBotMember member = ...;
member.deleteBlocking("text");
member.deleteBlocking(OneBotMemberDeleteOption.rejectRequest());
member.deleteBlocking(
    OneBotMemberDeleteOption.rejectRequest(),
    StandardDeleteOption.IGNORE_ON_FAILURE
);
```
{switcher-key=%jb%}

```Java
OneBotMember member = ...;
member.deleteReserve()
    .transform(SuspendReserves.mono())
    .subscribe();

member.deleteReserve(OneBotMemberDeleteOption.rejectRequest())
    .transform(SuspendReserves.mono())
    .subscribe();

member.deleteReserve(
        OneBotMemberDeleteOption.rejectRequest(),
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
<def title="OneBotMemberDeleteOption.*">

`OneBotMemberDeleteOption` 的所有子类型。

<deflist>
<def title="RejectRequest">
拒绝此人的加群请求，也就是踢出后将其屏蔽。
</def>
</deflist>

</def>
</deflist>

## OneBotStrangerAware

`OneBotMember` 实现 `OneBotStrangerAware`，
可以通过 `toStranger` 查询并得到一个对应的 `OneBotStranger` 类型。

<tabs group="code">
<tab title="Kotlin" group-key="Kotlin">

```Kotlin
val member: OneBotMember = ...
val stranger = member.toStranger()
```

</tab>
<tab title="Java" group-key="Java">

```Java
OneBotMember member = ...;
member.toStrangerAsync()
        .thenAccept(stranger -> {
            // ...
        });
```
{switcher-key=%ja%}

```Java
OneBotMember member = ...;
var stranger = member.toStrangerBlocking();
```
{switcher-key=%jb%}

```Java
OneBotMember member = ...;
member.toStrangerReserve()
        .transform(SuspendReserves.mono())
        .subscribe(stranger -> {
            // ... 
        });
```
{switcher-key=%jr%}

</tab>
</tabs>

## 更多能力

### 设置昵称

可以通过 `setNick(String)` 设置此成员在群内的昵称。

> 这通常需要bot拥有管理权限。

<tabs group="code">
<tab title="Kotlin" group-key="Kotlin">

```Kotlin
val member: OneBotMember = ...
member.setNick("newNick")
```

</tab>
<tab title="Java" group-key="Java">

```Java
OneBotMember member = ...;
member.setNickAsync("newNick");
```
{switcher-key=%ja%}

```Java
OneBotMember member = ...;
member.setNickBlocking("newNick");
```
{switcher-key=%jb%}

```Java
OneBotMember member = ...;
member.setNickReserve("newNick")
        .transform(SuspendReserves.mono())
        .subscribe();
```
{switcher-key=%jr%}

</tab>
</tabs>

### 设置管理员

可以通过 `setAdmin(Boolean)` 设置此成员为管理或取消管理。

参数为 `true` 设置为管理，`false` 取消管理

> 这通常需要bot拥有群主权限。

<tabs group="code">
<tab title="Kotlin" group-key="Kotlin">

```Kotlin
val member: OneBotMember = ...
member.setAdmin(true)
```

</tab>
<tab title="Java" group-key="Java">

```Java
OneBotMember member = ...;
member.setAdminAsync(true);
```
{switcher-key=%ja%}

```Java
OneBotMember member = ...;
member.setAdminBlocking(true);
```
{switcher-key=%jb%}

```Java
OneBotMember member = ...;
membersetAdminReserve(true)
        .transform(SuspendReserves.mono())
        .subscribe();
```
{switcher-key=%jr%}

</tab>
</tabs>

### 获取原始类型

有些时候可能需要获取OneBot11协议中的 `Member` 类型的内容。
`OneBotMember` 的实现并不唯一，因此并不一定是来自API所获取的。

可以通过 `getSourceMemberInfo` 得到 `GetGroupMemberInfoApi` 接口的请求结果。

> `getSourceMemberInfo` 不一定会真的发起API请求，这取决于具体实现。
> 
> 如果会发起请求，则请求结果不会缓存，即每次使用 `getSourceMemberInfo` 都会发起请求。
> 
> 而如果不会实际请求，则始终得到一个内部属性。

<tabs group="code">
<tab title="Kotlin" group-key="Kotlin">

```Kotlin
val member: OneBotMember = ...
val info = member.getSourceMemberInfo()
```

</tab>
<tab title="Java" group-key="Java">

```Java
OneBotMember member = ...;
member.getSourceMemberInfoAsync()
        .thenAccept(info -> {
            // ...
        });
```
{switcher-key=%ja%}

```Java
OneBotMember member = ...;
var info = member.getSourceMemberInfoBlocking();
```
{switcher-key=%jb%}

```Java
OneBotMember member = ...;
member.getSourceMemberInfoReserve()
        .transform(SuspendReserves.mono())
        .subscribe(info -> {
            // ...
        });
```
{switcher-key=%jr%}

</tab>
</tabs>

### 禁言

可以使用 `ban(...)` 或 `unban()` 对成员禁言或解除禁言。

通常来讲禁言时间应该大于等于1分钟、小于30天。
但是代码内未作校验，这交给了OneBot服务端处理。

> 这通常需要bot拥有管理权限。

<tabs group="code">
<tab title="Kotlin" group-key="Kotlin">

> `unban()` 相当于 `ban(Duration.ZERO)`。

```Kotlin
val member: OneBotMember = ...
member.ban(10.minutes)
member.unban()
```

</tab>
<tab title="Java" group-key="Java">

> `unbanXxx()` 相当于 `banXxx(0L, TimeUnit.*)`。

```Java
OneBotMember member = ...;
member.banAsync(10L, TimeUnit.MINUTES);
member.unbanAsync();
```
{switcher-key=%ja%}

```Java
OneBotMember member = ...;
member.banBlocking(10L, TimeUnit.MINUTES);
member.unbanBlocking();
```
{switcher-key=%jb%}

```Java
OneBotMember member = ...;
member.banReserve(10L, TimeUnit.MINUTES)
        .transform(SuspendReserves.mono())
        .subscribe();

member.unbanReserve()
        .transform(SuspendReserves.mono())
        .subscribe();
```
{switcher-key=%jr%}

</tab>
</tabs>

### 设置头衔

可以通过 `setSpecialTitle(String?)` 设置此成员在群内的特殊头衔。

<tip>

想要获取头衔，可以通过
[获取原始类型](#获取原始类型)
取到 `title`。

</tip>

<tabs group="code">
<tab title="Kotlin" group-key="Kotlin">

```Kotlin
val member: OneBotMember = ...
member.setSpecialTitle("newTitle")
```

</tab>
<tab title="Java" group-key="Java">

```Java
OneBotMember member = ...;
member.setSpecialTitleAsync("newTitle");
```
{switcher-key=%ja%}

```Java
OneBotMember member = ...;
member.setSpecialTitleBlocking("newTitle");
```
{switcher-key=%jb%}

```Java
OneBotMember member = ...;
member.setSpecialTitleReserve("newTitle")
        .transform(SuspendReserves.mono())
        .subscribe();
```
{switcher-key=%jr%}

</tab>
</tabs>


## 获取 OneBotMember

群成员 `OneBotMember` 通常来自
<a href="onebot11-OneBotGroup.md" />
或与群成员相关的事件。

### 来自事件

大多数跟群成员相关的事件中都可以直接获取到 `OneBotMember`。
通常来讲，如果事件主体与群成员相关，那么就是 `content`，
如果侧面相关，例如某个群成员消息事件中，
消息才是重点，而群成员则为 `author`。
