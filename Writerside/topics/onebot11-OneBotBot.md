---
switcher-label: JavaAPI风格
---

<show-structure depth="2"/>

# OneBotBot

表示一个 OneBot 的 bot 客户端实例，实现 `Bot`，由 `OneBotBotManager` 注册产生。

## Bot

`OneBot` 实现simbot标准接口 `Bot`，提供可支持的属性或能力。

<deflist>
<def id="prop-id" title="id">

配置中提供的 `botUniqueId` ，
用与 `OneBotBotManager` 中作为唯一标识。
</def>
<def id="prop-isMe" title="isMe(ID)">

判断提供的 `ID` 是否为自己。
首先依据 `id` 属性，如果内部的 `userId` 已经初始化，
则也会依据 `userId` 进行判断。

</def>
<def id="prop-name" title="name">

Bot自己的用户名。需要 `start` 后才会初始化，
否则获取会抛出异常。

</def>
<def id="prop-contactRelation" title="contactRelation">

联系人相关操作，即好友相关的关系操作。

</def>
<def id="prop-groupRelation" title="groupRelation">

与群聊相关的操作。

</def>
<def id="prop-guildRelation" title="guildRelation">

OneBot11协议不支持 `Guild` (即频道) 相关的操作，始终得到 `null`。

</def>
<def id="prop-start" title="start()">

启动Bot。在 Spring Boot 环境默认配置下会自动启动扫描注册的所有Bot。

</def>
</deflist>

除了上述的属性和API，`OneBotBot` 还提供了一些额外的内容：

<deflist>
<def id="prop-decoderJson" title="decoderJson">
Bot内部在进行一些API调用时使用的JSON序列化器。
</def>
<def id="prop-configuration" title="configuration">
注册Bot时使用的配置类信息。
</def>
<def id="prop-apiClient" title="apiClient">
Bot进行API请求时使用的HttpClient。
</def>
<def id="prop-apiHost" title="apiHost">
Bot进行API请求时使用的服务地址的host，来自配置信息。
</def>
<def id="prop-accessToken" title="accessToken">
Bot进行API请求时使用的accessToken，来自配置信息。
</def>
<def id="prop-userId" title="userId">

Bot自己的信息，在使用 `start` 之后会通过API查询获取。
如果尚未 `start` 或调用 `queryLoginInfo()` 就获取会得到异常。
</def>
<def id="prop-queryLoginInfo" title="queryLoginInfo()">

通过API查询当前Bot的信息，并同时更新 `userId` 和 `name`。
</def>
<def id="prop-getCookies" title="getCookies(...)">

使用API查询 Cookies。
</def>
<def id="prop-getCredentials" title="getCredentials(...)">

使用API查询 Credentials。
</def>
<def id="prop-getCsrfToken" title="getCsrfToken()">

使用API查询 CsrfToken。
</def>
</deflist>

## 关系对象

即对好友和群的相关操作，通过 `contactRelation` 和 `groupRelation` 进行。

### OneBotBotFriendRelation

通过 `contactRelation` 获取，
代表对联系人(也就是好友 `OneBotFriend`)和陌生人(`OneBotStranger`)的查询操作。

<tabs group="code">
<tab title="Kotlin" group-key="Kotlin">

```Kotlin
val bot: OneBotBot = ...
val relation = bot.contactRelation
// 获取所有好友信息并遍历
relation.contacts
    .asFlow()
    .collect { friend -> println("Friend: $friend") }

// 根据ID寻找指定的好友
val friend = relation.contact(123L.ID)

// 根据ID寻找指定的陌生人
val stranger = relation.stranger(456L.ID)
```

</tab>
<tab title="Java" group-key="Java">

```Java
final var relation = bot.getContactRelation();
// 在异步中遍历好友列表，使用bot作为 CoroutineScope
relation.getContacts().collectAsync(
        bot,
        friend -> System.out.println("Friend: " + friend)
);

// 也可以使用 transform 或 Collectables 提供的能力转化为某种异步结果
final var friendCollectable = relation.getContacts();
Collectables.toListAsync(friendCollectable)
        .thenAccept(friendList -> {
            // ...
        });

// 根据ID寻找指定的好友
relation.getContactAsync(Identifies.of(123L))
        .thenAccept(friend -> { // Warn: nullable
            // ...
        });

// 根据ID寻找指定的陌生人
relation.getStrangerAsync(Identifies.of(123L))
        .thenAccept(stranger -> { // Warn: nullable
            // ...
        });
```
{switcher-key=%ja%}

```Java
final var relation = bot.getContactRelation();
// 查询所有角色列表并阻塞地收集为List
final var list = relation.getContacts()
        .transform(SuspendReserves.list());

for (var friend : list) {
    System.out.println("Friend: " + friend);
}

// 根据ID寻找指定的好友
final var friend = relation.getContact(Identifies.of(123L));

// 根据ID寻找指定的陌生人
final var stranger = relation.getStranger(Identifies.of(123L));
```
{switcher-key=%jb%}

```Java
final var relation = bot.getContactRelation();
// 查询所有的好友并转化为 Flux 后遍历
relation.getContacts()
        .transform(SuspendReserves.flux())
        .subscribe(friend -> System.out.println("Friend: " + friend));

// 根据ID寻找指定的好友
relation.getContactReserve(Identifies.of(123L))
        .transform(SuspendReserves.mono())
        .subscribe(friend -> {
            // ...
        });

// 根据ID寻找指定的陌生人
relation.getStrangerReserve(Identifies.of(123L))
        .transform(SuspendReserves.mono())
        .subscribe(friend -> {
            // ...
        });
```
{switcher-key=%jr%}

</tab>
</tabs>

### OneBotBotGroupRelation

通过 `contactRelation` 获取，
代表对群(`OneBotGroup`)的查询操作。

<tabs group="code">
<tab title="Kotlin" group-key="Kotlin">

```Kotlin
val bot: OneBotBot = ...
val relation = bot.groupRelation
// 获取所有群并遍历
relation.groups
    .asFlow()
    .collect { group -> println("Group: $group") }

// 根据ID寻找指定的群
val group = relation.group(123L.ID)

// 根据ID直接寻找指定的群内的群成员
val member = relation.member(123L.ID, 456L.ID)
```

</tab>
<tab title="Java" group-key="Java">

```Java
final var relation = bot.getGroupRelation();
// 在异步中遍历群列表，使用bot作为 CoroutineScope
relation.getGroups().collectAsync(
        bot,
        group -> System.out.println("Group: " + group)
);

// 也可以使用 transform 或 Collectables 提供的能力转化为某种异步结果
final var groupCollectable = relation.getGroups();
Collectables.toListAsync(groupCollectable)
        .thenAccept(groupList -> {
            // ...
        });

// 根据ID寻找指定的群
relation.getGroupAsync(Identifies.of(123L))
        .thenAccept(group -> { // Warn: nullable
            // ...
        });

// 根据ID寻找指定的群内成员
relation.getMemberAsync(Identifies.of(123L), Identifies.of(456L))
        .thenAccept(member -> { // Warn: nullable
            // ...
        });
```
{switcher-key=%ja%}

```Java
final var relation = bot.getGroupRelation();
// 查询所有角色列表并阻塞地收集为List
final var list = relation.getContacts()
        .transform(SuspendReserves.list());

for (var friend : list) {
    System.out.println("Friend: " + friend);
}

// 根据ID寻找指定的好友
final var friend = relation.getContact(Identifies.of(123L));

// 根据ID寻找指定的陌生人
final var stranger = relation.getStranger(Identifies.of(123L));
```
{switcher-key=%jb%}

```Java
final var relation = bot.getGroupRelation();
// 查询所有的群并转化为 Flux 后遍历
relation.getGroup()
        .transform(SuspendReserves.flux())
        .subscribe(group -> System.out.println("Group: " + group));

// 根据ID寻找指定的群
relation.getGroupReserve(Identifies.of(123L))
        .transform(SuspendReserves.mono())
        .subscribe(group -> {
            // ...
        });

// 根据ID寻找指定的群内成员
relation.getMemberReserve(Identifies.of(123L), Identifies.of(456L))
        .transform(SuspendReserves.mono())
        .subscribe(group -> {
            // ...
        });
```
{switcher-key=%jr%}

</tab>
</tabs>

## OneBotBotManager

`OneBotBotManager` 实现simbot标准API的 `BotManager`，作为一个Bot管理器，
它用于注册生产与管理 `OneBotBot`。

### 获取 OneBotBotManager

当 `Application` 注册完成后，即可通过其中的 `botManagers` 寻找所需的 `BotManager`。
在 OneBot 组件中，我们通常要寻找 `OneBotBotManager`。

<tabs group="code">
<tab title="Kotlin" group-key="Kotlin">

```Kotlin
val application: Application = ...
// 得到第一个 OneBotBotManager
val obManager = application.botManagers.firstOneBotBotManager()

obManager.all().forEach {
    // 遍历所有的bot。。。
}

// 通过你配置的 uniqueBotId 获取
val bot = obManager["123456789".ID]
```

</tab>
<tab title="Java" group-key="Java">

```Java
final Application application;

// 得到第一个 OneBotBotManager
var obManager = application.getBotManagers()
    .stream()
    .filter(it -> it instanceof OneBotBotManager)
    .map(it -> (OneBotBotManager) it)
    .findFirst()
    .orElseThrow();

// 遍历bot
obManager.all().iterator().forEachRemaining(bot -> {
    // ...
});

// 也可以转成List
final var list = SequencesKt.toList(obManager.all());

// 通过你配置的 uniqueBotId 获取
final var bot = obManager.get(Identifies.of("123456789"));
```

</tab>
</tabs>

### 注册 OneBotBot

如果你打算以编程的方式动态注册 `OneBotBot`，那么在获取到 `OneBotBotManager`
之后使用 `register` 即可。

<tabs group="code">
<tab title="Kotlin" group-key="Kotlin">

```Kotlin
val manager: OneBotBotManager = ...
val bot = manager.register {
    botUniqueId = ""
    apiServerHost = ...
    eventServerHost = ...
    // 上面是必填属性，不过两个Host有默认值
    // 其他可选参数可自行探索，此处省略
}
```

</tab>
<tab title="Java" group-key="Java">

```Java
final var configuration = new OneBotBotConfiguration();
configuration.setBotUniqueId(...);
configuration.setApiServerHost(...);
configuration.setEventServerHost(...);
// 上面是必填属性，不过两个Host有默认值
// 其他可选参数可自行探索，此处省略

final var bot = onManager.register(configuration);
```

</tab>
</tabs>

<note>

注册完Bot后记得使用 `start()` 启动它们!
</note>

## Spring Boot

在 Spring Boot starter 默认配置环境下，`OneBotBotManager` 会被自动注册，
并会扫描所有
<a href="onebot11-bot-config.md"><b>Bot配置文件</b></a>
并解析、注册为 <code>OneBotBot</code> 后自动在 <b>异步中</b> 启动。

在 Spring 中，你可以通过注入 `Application` 来获取到 `BotManager`。

<tabs group="code">
<tab title="Kotlin" group-key="Kotlin">

```Kotlin
@Component
class MyComponent(
    val application: Application
) {
    // 假设它会被定时任务或者HTTP接口等其他地方调用
    fun run() {
        val obManager = application.botManagers.firstOneBotBotManager()
        obManager.all().forEach {
            // 遍历所有的bot。。。
        }
        
        // 通过你配置的 uniqueBotId 获取
        val bot = obManager["123456789".ID]
    }
}
```

</tab>
<tab title="Java" group-key="Java">

```Java
@Component
public class MyComponent {
    private final Application application;
    public MyComponent(Application application) {
        this.application = application;
    }

    // 假设它会被定时任务或者HTTP接口等其他地方调用
    public void run() {
        var obManager = application.getBotManagers()
            .stream()
            .filter(it -> it instanceof OneBotBotManager)
            .map(it -> (OneBotBotManager) it)
            .findFirst()
            .orElseThrow();

        // 遍历bot
        obManager.all().iterator().forEachRemaining(bot -> {
            // ...
        });

        // 也可以转成List
        final var list = SequencesKt.toList(obManager.all());

        // 通过你配置的 uniqueBotId 获取
        final var bot = obManager.get(Identifies.of("123456789"));
    }
}
```

</tab>
</tabs>

<warning>

由于 `Bot` 都是在异步中注册、启动的，因此无法保证Spring启动完成后就可以
**立即** 获取到你的Bot。
请做好逻辑处理，当暂时获取不到时，跳过本次处理。

或者你也可以监听事件 `OneBotBotStartedEvent` 来得知哪些Bot启动了。
有关事件的更多内容参考 
<a href="onebot11-event.md" /> 。
</warning>
