---
switcher-label: JavaAPI风格
---

# 使用核心库或Spring

## 前期准备

参考
<a href="onebot11-quick-start.md"/>
。

## 安装
### 安装组件库

首先，先添加OneBot11组件库的依赖。

<tabs group="build">
<tab title="Gradle(Kotlin DSL)" group-key="kts">

```Kotlin
implementation("%d-group%:%d-ob11-id%-core:%version%")
```

<tip>

如果使用 Java 而不配合使用 Gradle 的 [Kotlin插件](https://kotlinlang.org/docs/gradle-configure-project.html), 
那么你需要指定依赖的后缀为 `-jvm`。

```Kotlin
implementation("%d-group%:%d-ob11-id%-core-jvm:%version%")
```

</tip>
</tab>
<tab title="Gradle(Groovy)" group-key="groovy">

```Groovy
implementation '%d-group%:%d-ob11-id%-core:%version%'
```

<tip>

如果使用 Java 而不配合使用 Gradle 的 `kotlin` 插件, 那么你需要指定依赖的后缀为 `-jvm`。

```Groovy
implementation '%d-group%:%d-ob11-id%-core-jvm:%version%'
```

</tip>


</tab>
<tab title="Maven" group-key="maven">

```xml

<dependency>
    <groupId>%d-group%</groupId>
    <artifactId>%d-ob11-id%-core-jvm</artifactId>
    <version>%version%</version>
</dependency>
```

</tab>
</tabs>

### 安装simbot4实现库

接下来，选择一个你打算使用的simbot4核心实现库。

实现库是一个实现了simbot标准API中定义的 `Application` 的库。
我们提供了几个默认的实现，以针对不同的需求或场景。

> 有关simbot4本体的更多内容可参考 [**simbot4应用手册**](https://simbot.forte.love)。
> 
> 此处的实现库版本仅供参考，建议在
> [simbot4版本](%simbot4-release%)
> 中选择较新的版本。

<tabs group="simbot4impl">
<tab group-key="core" title="核心库">

使用核心库实现 (`simbot-core`)。
核心库是一个轻量级实现库，实现了simbot标准API的绝大多数内容。

<tabs group="build">
<tab title="Gradle(Kotlin DSL)" group-key="kts">

```Kotlin
implementation("love.forte.simbot:simbot-core:%minimum-core-version%")
```

<tip>

如果使用 Java 而不配合使用 Gradle 的 [Kotlin插件](https://kotlinlang.org/docs/gradle-configure-project.html),
那么你需要指定依赖的后缀为 `-jvm`。

```Kotlin
implementation("love.forte.simbot:simbot-core-jvm:%minimum-core-version%")
```

</tip>
</tab>
<tab title="Gradle(Groovy)" group-key="groovy">

```Groovy
implementation 'love.forte.simbot:simbot-core:%minimum-core-version%'
```

<tip>

如果使用 Java 而不配合使用 Gradle 的 `kotlin` 插件, 那么你需要指定依赖的后缀为 `-jvm`。

```Groovy
implementation 'love.forte.simbot:simbot-core-jvm:%minimum-core-version%'
```

</tip>


</tab>
<tab title="Maven" group-key="maven">

```xml

<dependency>
    <groupId>love.forte.simbot</groupId>
    <artifactId>simbot-core-jvm</artifactId>
    <version>%minimum-core-version%</version>
</dependency>
```

</tab>
</tabs>


</tab>
<tab group-key="spring" title="Spring Boot starter">

使用核心库的Spring Boot启动器实现 (`simbot-core-spring-boot-starter`)。
它基于**核心库**，针对Spring Boot进行了启动器整合。

<tabs group="build">
<tab title="Gradle(Kotlin DSL)" group-key="kts">

```Kotlin
implementation("love.forte.simbot:simbot-core-spring-boot-starter:%minimum-core-version%")
```

</tab>
<tab title="Gradle(Groovy)" group-key="groovy">

```Groovy
implementation 'love.forte.simbot:simbot-core-spring-boot-starter:%minimum-core-version%'
```

</tab>
<tab title="Maven" group-key="maven">

```xml

<dependency>
    <groupId>love.forte.simbot</groupId>
    <artifactId>simbot-core-spring-boot-starter</artifactId>
    <version>%minimum-core-version%</version>
</dependency>
```

</tab>
</tabs>

<warning>

`simbot-core-spring-boot-starter` 基于 **Spring Boot 3**，
因此最低要求 **Java17**。
如果你想要使用 Spring Boot 2.x 的实现版本，请修改坐标为
`simbot-core-spring-boot-starter-v2`。

**注意**：这个 `-v2` 版本是一个临时性的兼容版本，
不保证未来会继续提供支持，也不保证其稳定性。

我们强烈建议你更新Spring Boot到3.x版本。
</warning>

</tab>
</tabs>

### 安装Ktor引擎

OneBot11组件使用 [Ktor](https://ktor.io) 作为 HTTP 客户端实现，
但是默认不会依赖任何具体的**引擎**。

<include from="snippets.md" element-id="engine-choose" />

## 使用

### 创建Application

<tabs group="simbot4impl">
<tab group-key="core" title="核心库">

在 `simbot-core` 中，提供了一个基础的 `Application` 实现类型：`SimpleApplication`。

<tabs group="code">
<tab title="Kotlin" group-key="Kotlin">

```Kotlin
val app = launchSimpleApplication {
    // 使用OneBot组件相关的内容。
    useOneBot11()
    // 其他配置...
}

app.join() // 挂起app直到cancel它
```

</tab>
<tab title="Java" group-key="Java">

```Java
final var applicationAsync = Applications.launchApplicationAsync(
    Simple.INSTANCE,
    configurer -> {
        // 安装OneBot组件相关内容
        configurer.install(OneBot11Component.Factory);
        configurer.install(OneBotBotManager.Factory);
        // 其他...
    }
);

applicationAsync
        .asFuture()
        // Application 转化为 Future
        .thenCompose(Application::asFuture)
        // 阻塞这个Future直到Application被cancel
        .join();
```
{switcher-key=%ja%}

```Java
final var application = Applications.launchApplicationBlocking(
    Simple.INSTANCE,
    configurer -> {
        // 安装OneBot组件相关内容
        configurer.install(OneBot11Component.Factory);
        configurer.install(OneBotBotManager.Factory);
        // 其他...
    }
);

// 阻塞直到被cancel
application.joinBlocking();
```
{switcher-key=%jb%}

```Java
var applicationAsync = Applications.launchApplicationAsync(
        Simple.INSTANCE,
        configurer -> {
            // 安装OneBot组件相关内容
            configurer.install(OneBot11Component.Factory);
            configurer.install(OneBotBotManager.Factory);
            // 其他...
        }
);

// 例如：转化为 reactor.core.publisher.Mono;
var mono = Mono.fromFuture(applicationAsync.asFuture())
        .flatMap(app ->
                app.joinReserve().transform(
                        SuspendReserves.mono()
                )
        );

// 使用这个响应式类型，例如阻塞它。
/// 阻塞直到被cancel
mono.block();
```
{switcher-key=%jr%}

</tab>
</tabs>

</tab>
<tab group-key="spring" title="Spring">

在 Spring Boot 中，不需要你手动构建 `Application`。
你只需要在你的启动类上标记 `@EnableSimbot` 来启用 simbot 即可。

<tabs group="code">
<tab title="Kotlin" group-key="Kotlin">

```Kotlin
@EnableSimbot
@SpringBootApplication
open class BotApplication

fun main(vararg args: String) {
    runApplication<BotApplication>(*args)
}
```

</tab>
<tab title="Java" group-key="Java">

```Java
@EnableSimbot
@SpringBootApplication
public class BotApplication {
    public static void main(String[] args) {
        SpringApplication.run(BotApplication.class, args);
    }
}
```

</tab>
</tabs>

</tab>
</tabs>


### 注册Bot

<tabs group="simbot4impl">
<tab group-key="core" title="核心库">

在 Application 构建完成后，即可从 `Application.botManagers` 
中寻找你所需的管理器（比如OneBot的Bot管理器：`OneBotBotManager`）
并注册你的Bot。

<tabs group="code">
<tab title="Kotlin" group-key="Kotlin">

```Kotlin
suspend fun main() {
    val app = launchSimpleApplication {
        useOneBot11()
        // ...
    }

    app.configure()
    app.join()
}

suspend fun Application.configure() {
    // 寻找、获得所需的BotManager
    val botManager = botManagers.firstOneBotBotManager()
    // 注册你所需的bot
    val bot = botManager.register(
        OneBotBotConfiguration().apply {
            // 这几个是必选属性
            /// 在OneBot组件中用于区分不同Bot的唯一ID， 建议可以直接使用QQ号。
            botUniqueId = "11112222"
            apiServerHost = Url("http://127.0.0.1:3000")
            eventServerHost = Url("ws://127.0.0.1:3001")
            // 其他配置, 一般都是可选属性
            /// token
            accessToken = null
            /// ...
        }
    )
    
    // 启动你的bot
    bot.start()
}
```

</tab>
<tab title="Java" group-key="Java">

```Java
public static void main(String[] args) {
    final var applicationAsync = Applications.launchApplicationAsync(
        Simple.INSTANCE,
        configurer -> {
            // 安装OneBot组件相关内容
            configurer.install(OneBot11Component.Factory);
            configurer.install(OneBotBotManager.Factory);
            // 其他...
        }
    );

    applicationAsync
        .asFuture()
        // Application 转化为 Future
        .thenApply(app -> {
            // 配置
            configure(app);
            return app;
        })
        .thenCompose(Application::asFuture)
        // 阻塞这个Future直到Application被cancel
        .join();
}

public static void configure(Application application) {
    // 寻找所需的BotManager
    // 可以选择遍历寻找
    final var found = application.getBotManagers().stream()
        .filter(it -> it instanceof OneBotBotManager)
        .map(it -> (OneBotBotManager) it)
        .findFirst()
        .orElseThrow();

    // 也可以选择使用辅助API
    final var oneBotBotManager = OneBotBotManagerUsageKt
        .firstOneBotBotManager(application.getBotManagers());

    // 注册Bot
    /// 准备配置类
    final var botConfiguration = new OneBotBotConfiguration();
    // 这几个是必选属性
    /// 在OneBot组件中用于区分不同Bot的唯一ID， 建议可以直接使用QQ号。
    botConfiguration.setBotUniqueId("11112222");
    /// API 的服务地址。默认localhost:3000
    botConfiguration.setApiServerHost(URLUtilsKt.Url("http://localhost:3000"));
    /// 连接事件的服务地址。默认localhost:3001
    botConfiguration.setEventServerHost(URLUtilsKt.Url("ws://localhost:3001"));
    // 其他配置, 一般都是可选属性
    /// token
    botConfiguration.setAccessToken(null);
    /// ...

    // 注册
    final var bot = oneBotBotManager.register(botConfiguration);
    // 启动
    final var future = bot.startAsync().whenComplete((r, ex) -> {
        // 在使用 Future 的时候，记得处理异常
        if (ex != null) {
            Logger.getGlobal().log(
                Level.SEVERE,
                "bot启动出现异常！",
                ex
            );
        }
    });
    // 你可以选择处理等待这个future，也可以选择不管他，直接在异步中处理。
}
```
{switcher-key=%ja%}

```Java
public static void main(String[] args) {
    final var application = Applications.launchApplicationBlocking(
            Simple.INSTANCE,
            configurer -> {
                // 安装OneBot组件相关内容
                configurer.install(OneBot11Component.Factory);
                configurer.install(OneBotBotManager.Factory);
                // 其他...
            }
    );

    configure(application);
    application.joinBlocking();
}

public static void configure(Application application) {
    // 寻找所需的BotManager
    // 可以选择遍历寻找
    final var found = application.getBotManagers().stream()
            .filter(it -> it instanceof OneBotBotManager)
            .map(it -> (OneBotBotManager) it)
            .findFirst()
            .orElseThrow();

    // 也可以选择使用辅助API
    final var oneBotBotManager = OneBotBotManagerUsageKt
        .firstOneBotBotManager(application.getBotManagers());

    // 注册Bot
    /// 准备配置类
    final var botConfiguration = new OneBotBotConfiguration();
    // 这几个是必选属性
    /// 在OneBot组件中用于区分不同Bot的唯一ID， 建议可以直接使用QQ号。
    botConfiguration.setBotUniqueId("11112222");
    /// API 的服务地址。默认localhost:3000
    botConfiguration.setApiServerHost(URLUtilsKt.Url("http://localhost:3000"));
    /// 连接事件的服务地址。默认localhost:3001
    botConfiguration.setEventServerHost(URLUtilsKt.Url("ws://localhost:3001"));
    // 其他配置, 一般都是可选属性
    /// token
    botConfiguration.setAccessToken(null);
    /// ...

    // 注册
    final var bot = oneBotBotManager.register(botConfiguration);
    // 启动
    bot.startBlocking();
}
```
{switcher-key=%jb%}

```Java
public static void main(String[] args) {
    var applicationAsync = Applications.launchApplicationAsync(
        Simple.INSTANCE,
        configurer -> {
            // 安装OneBot组件相关内容
            configurer.install(OneBot11Component.Factory);
            configurer.install(OneBotBotManager.Factory);
            // 其他...
        }
    );

    // 例如：转化为 reactor.core.publisher.Mono;
    var mono = Mono.fromFuture(applicationAsync.asFuture())
        // 配置 Application
        .doOnNext(CoreMain::configure)
        .flatMap(app ->
            app.joinReserve().transform(
                SuspendReserves.mono()
            )
        );

    // 使用这个响应式类型，例如阻塞它。
    /// 阻塞直到被cancel
    mono.block();
}

public static void configure(Application application) {
    // 寻找所需的BotManager
    // 可以选择遍历寻找
    final var found = application.getBotManagers().stream()
        .filter(it -> it instanceof OneBotBotManager)
        .map(it -> (OneBotBotManager) it)
        .findFirst()
        .orElseThrow();

    // 也可以选择使用辅助API
    final var oneBotBotManager = OneBotBotManagerUsageKt
        .firstOneBotBotManager(application.getBotManagers());

    // 注册Bot
    /// 准备配置类
    final var botConfiguration = new OneBotBotConfiguration();
    // 这几个是必选属性
    /// 在OneBot组件中用于区分不同Bot的唯一ID， 建议可以直接使用QQ号。
    botConfiguration.setBotUniqueId("11112222");
    /// API 的服务地址。默认localhost:3000
    botConfiguration.setApiServerHost(URLUtilsKt.Url("http://localhost:3000"));
    /// 连接事件的服务地址。默认localhost:3001
    botConfiguration.setEventServerHost(URLUtilsKt.Url("ws://localhost:3001"));
    // 其他配置, 一般都是可选属性
    /// token
    botConfiguration.setAccessToken(null);
    /// ...

    // 注册
    final var bot = oneBotBotManager.register(botConfiguration);
    // 启动
    bot.startReserve()
        .transform(SuspendReserves.mono())
        // 在使用响应式编程的时候，记得处理异常
        .doOnError(ex -> {
            Logger.getGlobal().log(
                Level.SEVERE,
                "bot启动出现异常！",
                ex
            );
        })
        // 你也可以选择返回Mono然后做进一步处理
        // 此处直接使其在异步中运行。
        .subscribe();
}
```
{switcher-key=%jr%}
</tab>
</tabs>

</tab>
<tab group-key="spring" title="Spring">

在 Spring 中，通常可以选择使用 `.bot.json` 格式的配置文件来快速、自动地批量注册bot。

默认情况下，在你的项目的资源目录 
<path>resource</path> 
中创建目录 
<path>/simbot-bots/</path> ，
然后前往参考
<b>
<a href="onebot11-bot-config.md" />
</b> 
并配置你的 JSON 格式的配置文件，例如 `abc.bot.json`。

默认情况下starter会自动扫描上述资源目录并加载、自动启动它们，这一切是在**异步**中进行的。

<tip>

上述的 _资源目录 `/simbot-bots/`_ 、_自动启动_、以及 _在异步中启动_ 这些都是可配置的。

有关Spring Boot环境下可用的**配置信息**，可前往
[simbot应用手册](https://simbot.forte.love/start-use-spring-boot-3.html)
参考更多。
</tip>

</tab>
</tabs>


### 事件监听

<tabs group="simbot4impl">
<tab group-key="core" title="核心库">

从 `Application` 中获取 `EventDispatcher` 即可注册事件监听函数。

<tabs group="code">
<tab title="Kotlin" group-key="Kotlin">

Kotlin 中，可以使用 `Application.listeners {}` 扩展函数。

```Kotlin
// 省略构建Application相关内容...

suspend fun Application.configure() {
    // bot相关内容省略....

    // Kotlin 中，可以使用 Application.listeners 扩展函数。
    listeners {
        // 使用 listen 监听一个事件
        // 此处是一个标准库中通用的类型：聊天群消息事件
        listen<ChatGroupMessageEvent> { event ->
            println("Event: $event")
            if (event.messageContent.plainText?.trim() == "你好") {
                event.reply("你也好")
            }

            // 使用listen时必须返回一个EventResult类型的结果
            EventResult.empty()
        }

        // 使用 process 监听一个事件
        // 此处监听的是OneBot组件中的专属类型：OneBot的好友消息事件
        process<OneBotFriendMessageEvent> { event ->
            println("Event: $event")
            if (event.messageContent.plainText?.trim() == "你好") {
                event.reply("你也好")
            }

            // 使用 process 不需要返回 EventResult，默认视为返回 EventResult.empty()
        }
    }
}
```

</tab>
<tab title="Java" group-key="Java">

```Java
// 省略构建Application相关内容...

public static void configure(Application application) {
    // bot相关内容省略....

    final var eventDispatcher = application.getEventDispatcher();

    // 监听一个OneBot组件中的专属类型：OneBot的好友消息事件
    eventDispatcher.register(
        EventListeners.async(
            OneBotFriendMessageEvent.class,
            (context, event) -> {
                System.out.println("Event: " + event);
                if ("你好".equals(event.getMessageContent().getPlainText())) {
                    return event.replyReserve("你也好")
                        .transform(SuspendReserves.mono())
                        .thenReturn(EventResult.empty())
                        // 返回一个异步的EventResult类型的结果
                        .toFuture();
                }

                // 返回一个异步的EventResult类型的结果
                return Mono.just(
                    EventResult.empty()
                ).toFuture();
            }
        )
    );
}
```
{switcher-key=%ja%}

```Java
// 省略构建Application相关内容...

public static void configure(Application application) {
    // bot相关内容省略....
    
    final var eventDispatcher = application.getEventDispatcher();

    // 监听一个OneBot组件中的专属类型：OneBot的好友消息事件
    eventDispatcher.register(
        EventListeners.block(
            OneBotFriendMessageEvent.class,
            (context, event) -> {
                System.out.println("Event: " + event);
                if ("你好".equals(event.getMessageContent().getPlainText())) {
                    event.replyBlocking("你也好");
                }

                // 返回一个EventResult类型的结果
                return EventResult.empty();
            }
        )
    );
}
```
{switcher-key=%jb%}

```Java
// 省略构建Application相关内容...

public static void configure(Application application) {
    // bot相关内容省略....

    final var eventDispatcher = application.getEventDispatcher();

    // 监听一个OneBot组件中的专属类型：OneBot的好友消息事件
    eventDispatcher.register(
        EventListeners.async(
            OneBotFriendMessageEvent.class,
            (context, event) -> {
                System.out.println("Event: " + event);
                if ("你好".equals(event.getMessageContent().getPlainText())) {
                    return event.replyReserve("你也好")
                        .transform(SuspendReserves.mono())
                        .thenReturn(EventResult.empty())
                        // 返回一个异步的EventResult类型的结果
                        .toFuture();
                }

                // 返回一个异步的EventResult类型的结果
                return Mono.just(
                    EventResult.empty()
                ).toFuture();
            }
        )
    );
}
```
{switcher-key=%jr%}

</tab>
</tabs>


</tab>
<tab group-key="spring" title="Spring">

在 Spring 中，使用注解 `@Listener` 注册一个监听函数。
可监听到的事件即为参数中的事件类型。
也因此，参数中的事件类型的参数应当**最多**只有1个。

<tip>

`@Listener` 需要在被Spring管理的bean中使用，例如需要在类上标记 `@Component`。

</tip>

<tabs group="code">
<tab title="Kotlin" group-key="Kotlin">

```Kotlin
@Component
class MyHandles {
    /**
     * 此处是一个标准库中通用的类型：聊天群消息事件
     */
    @Listener
    suspend fun onGroupMessage(event: ChatGroupMessageEvent) {
        println("ChatGroupMessageEvent: $event")
    }
    
    /**
     * 此处监听的是OneBot组件中的专属类型：OneBot的好友消息事件
     * 并且过滤消息：消息中的文本消息去除前后空字符后，等于 '你好'
     */
    @Listener
    @ContentTrim
    @Filter("你好")
    suspend fun onFriendMessage(event: OneBotFriendMessageEvent) {
        println("OneBotFriendMessageEvent: $event")
        // 回复消息
        event.reply("你也好")
    }
}


```

</tab>
<tab title="Java" group-key="Java">

```Java
@Component
public class MyHandles {
    /**
     * 此处是一个标准库中通用的类型：聊天群消息事件
     */
    @Listener
    public void onGroupMessage(ChatGroupMessageEvent event) {
        System.out.println("ChatGroupMessageEvent: " + event);
    }

    /**
     * 此处监听的是OneBot组件中的专属类型：OneBot的好友消息事件
     * 并且过滤消息：消息中的文本消息去除前后空字符后，等于 '你好'
     */
    @Listener
    @ContentTrim
    @Filter("你好")
    public CompletableFuture<?> onFriendMessage(OneBotFriendMessageEvent event) {
        System.out.println("OneBotFriendMessageEvent: " + event);
        return event.replyAsync("你也好");
        // 可以直接返回任意 CompletableFuture 类型
    }
}
```
{switcher-key=%ja%}

```Java
@Component
public class MyHandles {
    /**
     * 此处是一个标准库中通用的类型：聊天群消息事件
     */
    @Listener
    public void onGroupMessage(ChatGroupMessageEvent event) {
        System.out.println("ChatGroupMessageEvent: " + event);
    }

    /**
     * 此处监听的是OneBot组件中的专属类型：OneBot的好友消息事件
     * 并且过滤消息：消息中的文本消息去除前后空字符后，等于 '你好'
     */
    @Listener
    @ContentTrim
    @Filter("你好")
    public void onFriendMessage(OneBotFriendMessageEvent event) {
        System.out.println("OneBotFriendMessageEvent: " + event);
        event.replyBlocking("你也好");
    }
}
```
{switcher-key=%jb%}

```Java
@Component
public class MyHandles {
    /**
     * 此处是一个标准库中通用的类型：聊天群消息事件
     */
    @Listener
    public void onGroupMessage(ChatGroupMessageEvent event) {
        System.out.println("ChatGroupMessageEvent: " + event);
    }

    /**
     * 此处监听的是OneBot组件中的专属类型：OneBot的好友消息事件
     * 并且过滤消息：消息中的文本消息去除前后空字符后，等于 '你好'
     */
    @Listener
    @ContentTrim
    @Filter("你好")
    public Mono<?> onFriendMessage(OneBotFriendMessageEvent event) {
        System.out.println("OneBotFriendMessageEvent: " + event);
        return event.replyReserve("你也好")
            .transform(SuspendReserves.mono());
        // 可以直接返回任意(kotlinx.coroutines支持的)响应式类型，
        // 不过需要注意，确保运行时环境中有对应的依赖，
        // 以Mono为例，需要添加 [[[kotlinx-coroutines-reactor|https://github.com/Kotlin/kotlinx.coroutines/tree/master/reactive]]]
    }
}
```
{switcher-key=%jr%}


</tab>
</tabs>

</tab>
</tabs>

## 运行

运行你的程序，观察效果或进行测试即可。
