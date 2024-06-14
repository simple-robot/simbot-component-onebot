<!--suppress HtmlDeprecatedAttribute -->
<div align="center">
<picture>
  <source media="(prefers-color-scheme: dark)" srcset=".simbot/logo-dark.svg">
  <source media="(prefers-color-scheme: light)" srcset=".simbot/logo.svg">
  <img alt="simbot logo" src=".simbot/logo.svg" width="260" />
</picture>
<h2>
    ~ Simple Robot ~ <br/> <small>OneBot Component</small>
</h2>
<a href="https://github.com/simple-robot/simbot-component-onebot/releases/latest"><img alt="release" src="https://img.shields.io/github/v/release/simple-robot/simbot-component-onebot" /></a>
<a href="https://repo1.maven.org/maven2/love/forte/simbot/component/simbot-component-onebot11-core/" target="_blank">
  <img alt="release" src="https://img.shields.io/maven-central/v/love.forte.simbot.component/simbot-component-onebot11-core" /></a>
   <hr>
<p>
   <img alt="stars" src="https://img.shields.io/github/stars/simple-robot/simbot-component-onebot" />
   <img alt="forks" src="https://img.shields.io/github/forks/simple-robot/simbot-component-onebot" />
   <img alt="watchers" src="https://img.shields.io/github/watchers/simple-robot/simbot-component-onebot" />
   <img alt="repo size" src="https://img.shields.io/github/repo-size/simple-robot/simbot-component-onebot" />
   <img alt="issues" src="https://img.shields.io/github/issues-closed/simple-robot/simbot-component-onebot?color=green" />
   <img alt="last commit" src="https://img.shields.io/github/last-commit/simple-robot/simbot-component-onebot" />
   <a href="./COPYING"><img alt="copying" src="https://img.shields.io/github/license/simple-robot/simbot-component-onebot" /></a>
</p>
<p>
<img src="https://img.shields.io/badge/OneBot-11-black?logo=data:image/png;base64,iVBORw0KGgoAAAANSUhEUgAAAHAAAABwCAMAAADxPgR5AAAAGXRFWHRTb2Z0d2FyZQBBZG9iZSBJbWFnZVJlYWR5ccllPAAAAAxQTFRF////29vbr6+vAAAAk1hCcwAAAAR0Uk5T////AEAqqfQAAAKcSURBVHja7NrbctswDATQXfD//zlpO7FlmwAWIOnOtNaTM5JwDMa8E+PNFz7g3waJ24fviyDPgfhz8fHP39cBcBL9KoJbQUxjA2iYqHL3FAnvzhL4GtVNUcoSZe6eSHizBcK5LL7dBr2AUZlev1ARRHCljzRALIEog6H3U6bCIyqIZdAT0eBuJYaGiJaHSjmkYIZd+qSGWAQnIaz2OArVnX6vrItQvbhZJtVGB5qX9wKqCMkb9W7aexfCO/rwQRBzsDIsYx4AOz0nhAtWu7bqkEQBO0Pr+Ftjt5fFCUEbm0Sbgdu8WSgJ5NgH2iu46R/o1UcBXJsFusWF/QUaz3RwJMEgngfaGGdSxJkE/Yg4lOBryBiMwvAhZrVMUUvwqU7F05b5WLaUIN4M4hRocQQRnEedgsn7TZB3UCpRrIJwQfqvGwsg18EnI2uSVNC8t+0QmMXogvbPg/xk+Mnw/6kW/rraUlvqgmFreAA09xW5t0AFlHrQZ3CsgvZm0FbHNKyBmheBKIF2cCA8A600aHPmFtRB1XvMsJAiza7LpPog0UJwccKdzw8rdf8MyN2ePYF896LC5hTzdZqxb6VNXInaupARLDNBWgI8spq4T0Qb5H4vWfPmHo8OyB1ito+AysNNz0oglj1U955sjUN9d41LnrX2D/u7eRwxyOaOpfyevCWbTgDEoilsOnu7zsKhjRCsnD/QzhdkYLBLXjiK4f3UWmcx2M7PO21CKVTH84638NTplt6JIQH0ZwCNuiWAfvuLhdrcOYPVO9eW3A67l7hZtgaY9GZo9AFc6cryjoeFBIWeU+npnk/nLE0OxCHL1eQsc1IciehjpJv5mqCsjeopaH6r15/MrxNnVhu7tmcslay2gO2Z1QfcfX0JMACG41/u0RrI9QAAAABJRU5ErkJggg==" />
</p>
</div>

Simple Robot OneBot 组件是一个将
[OneBot11](https://github.com/botuniverse/onebot-11)
协议在
[Simple Robot](http://github.com/simple-robot/simpler-robot) 标准API下实现的组件库，
并由此提供simbot中的各项能力。

它是一个 Kotlin multiplatform 库，Java 友好、异步高效！

借助simbot核心库提供的能力，它可以支持很多高级功能和封装，比如组件协同、Spring支持等，
祝你快速开发 OneBot 客户端应用！

序列化和网络请求相关分别基于 [Kotlin serialization](https://github.com/Kotlin/kotlinx.serialization)
和 [Ktor](https://ktor.io/)。

## 文档与引导

- OneBot组件手册(🚧建设中): [OneBot组件手册](https://simple-robot.github.io/simbot-component-onebot/)
- 了解simbot: [Simple Robot 应用手册](https://simbot.forte.love)
- [文档引导站&API文档](https://docs.simbot.forte.love)
- [**社群**](https://simbot.forte.love/communities.html) 文档中也有提供社群信息喔
- 前往 [组织首页](https://github.com/simple-robot/) 了解更多有关组件、文档、以及社群等相关信息！

---

我们欢迎并期望着您的
[反馈](https://github.com/simple-robot/simbot-component-onebot/issues)
或
[协助](https://github.com/simple-robot/simbot-component-onebot/pulls)，
感谢您的贡献与支持！

## 快速开始

> [!warning]
> 手册施工完成之前，此处临时提供快速开始。手册施工完成后会删除。

> [!note]
> 核心库的版本可前往 
> [此处](https://github.com/simple-robot/simpler-robot/releases) 
> 参考。

### Ktor 客户端引擎

首先，OneBot组件使用 [Ktor](https://ktor.io/)
作为HTTP客户端与WS客户端，但是默认情况下依赖中不会有任何具体的引擎实现。

因此你需要根据你的使用平台前往 [Ktor client Engines](https://ktor.io/docs/client-engines.html#limitations)
选择一个合适的引擎使用。

> [!tip]
> 注意，你需要选择一个支持HTTP和WebSocket的引擎。
> 除非你打算通过更详细的配置为两个场景分配不同的引擎实现。

以 Java11+ 的情况为例，我们选择使用 `Java` 引擎：

Gradle:

```kotlin
runtimeOnly("io.ktor:ktor-client-java:$KTOR_VERSION")
```

Maven:

```xml
<dependency>
    <groupId>io.ktor</groupId>
    <artifactId>ktor-client-java-jvm</artifactId>
    <version>${KTOR_VERSION}</version>
    <scope>runtime</scope>
</dependency>
```

### 普通核心库

Gradle:

```kotlin
// 指定核心库依赖
implementation("love.forte.simbot:simbot-core:$SIMBOT_VERSION")
// 引入OneBot组件库
implementation("love.forte.simbot.component:simbot-component-onebot-v11-core:$VERSION")
```

Maven:

```xml
<dependencies>
    <!-- 指定核心库依赖 -->
    <dependency>
        <groupId>love.forte.simbot</groupId>
        <artifactId>simbot-core-jvm</artifactId>
        <version>${SIMBOT_VERSION}</version>
    </dependency>
    <dependency>
        <groupId>love.forte.simbot.component</groupId>
        <artifactId>simbot-component-onebot-v11-core-jvm</artifactId>
        <version>${VERSION}</version>
    </dependency>
</dependencies>
```

```kotlin
suspend fun main() {
    val app = launchSimpleApplication {
        // 使用OneBot11协议组件
        useOneBot11()
    }

    // 注册、监听事件
    app.listeners {
        // 例如：监听OneBot的普通群消息
        process<OneBotNormalGroupMessageEvent> { event ->
            println("event: $event")
            if (event.messageContent.plainText?.trim() == "你好") {
                event.reply("你也好")
            }
        }
    }

    // 注册Bot
    // 先得到OneBot的BotManager
    app.oneBot11Bots {
        val bot = register(OneBotBotConfiguration().apply {
            botUniqueId = "123456"
            apiServerHost = Url("http://localhost:3000")
            eventServerHost = Url("ws://localhost:3001")
        })

        // 启动bot
        bot.start()
    }

    // 挂起直到被终止
    app.join()
}
```

### 配合 Spring Boot

Gradle:

```kotlin
// 你的其他SpringBoot依赖..

// 指定核心库依赖
implementation("love.forte.simbot:simbot-core-spring-boot-starter:$SIMBOT_VERSION")
// 引入OneBot组件库
implementation("love.forte.simbot.component:simbot-component-onebot-v11-core:$VERSION")
```

Maven:

```xml
<dependencies>
    <!-- 你的其他SpringBoot依赖.. -->
    
    
    <!-- 指定核心库依赖 -->
    <dependency>
        <groupId>love.forte.simbot</groupId>
        <artifactId>simbot-core-spring-boot-starter</artifactId>
        <version>${SIMBOT_VERSION}</version>
    </dependency>
    <dependency>
        <groupId>love.forte.simbot.component</groupId>
        <artifactId>simbot-component-onebot-v11-core-jvm</artifactId>
        <version>${VERSION}</version>
    </dependency>
</dependencies>
```


Kotlin:

```Kotlin
@SpringBootApplication
@EnableSimbot
class MyApp

fun main(vararg args: String) {
    runApplication<MyApp>(*args)
}

@Component
class MyHandlers {

    /**
     * 例如：监听OneBot的普通群消息
     */
    @Listener
    @Filter("你好")
    @ContentTrim
    suspend fun onGroupMessage(event: OneBotNormalGroupMessageEvent) {
        event.reply("你也好")
    }
}
```

or Java:

```java
@SpringBootApplication
@EnableSimbot
public class MyApp {
    public static void main(String[] args) {
        SpringApplication.run(MyApp.class, args);
    }

    @Component
    public static class MyHandlers {

        /**
         * 例如：监听OneBot的普通群消息
         */
        @Listener
        @Filter("你好")
        @ContentTrim
        public CompletableFuture<?> onGroupMessage(OneBotNormalGroupMessageEvent event) {
            return event.replyAsync("你也好");
        }
    }
}
```

#### 配置文件

使用spring的时候用的。需要注意实际上是不允许使用注释的，这里只是方便展示。

配置文件放在资源目录 `resources` 中的 `/simbot-bots/` 目录下，以 `.bot.json` 格式结尾，
例如 `myBot.bot.json`。

```json5
{
  // 固定值
  "component": "simbot.onebot11",
  "authorization": {
    // 唯一ID，作为组件内 Bot 的 id，用于组件内去重。可以随便编，但建议是bot的qq号
    "botUniqueId": "123456",
    // api地址，是个http/https服务器的路径，默认localhost:3000
    "apiServerHost": "http://localhost:3000",
    // 订阅事件的服务器地址，是个ws/wss路径，默认localhost:3001
    "eventServerHost":"ws://localhost:3001",
    // 配置的 token，可以是null
    "accessToken": null 
  },
  // 额外的可选配置
  // config本身以及其内的各项属性绝大多数都可省略或null
  "config": { 
    // API请求中的超时请求配置。整数数字，单位毫秒，默认为 `null`。
    "apiHttpRequestTimeoutMillis": null,
    // API请求中的超时请求配置。整数数字，单位毫秒，默认为 `null`。
    "apiHttpConnectTimeoutMillis": null,
    // API请求中的超时请求配置。整数数字，单位毫秒，默认为 `null`。
    "apiHttpSocketTimeoutMillis": null,
    // 每次尝试连接到 ws 服务时的最大重试次数，大于等于0的整数，默认为 2147483647
    "wsConnectMaxRetryTimes": null,
    // 每次尝试连接到 ws 服务时，如果需要重新尝试，则每次尝试之间的等待时长
    // 整数数字，单位毫秒，默认为 3500
    "wsConnectRetryDelayMillis": null,
  }
}
```

## 事件监听

简单列举一下原始事件与可能对应的组件事件之间的关系。

| 原始事件                                          | 组件事件                                   |
|-----------------------------------------------|----------------------------------------|
| `MetaEvent`                                   | `OneBotMetaEvent`                      |
| > `LifecycleEvent`                            | > `OneBotLifecycleEvent`               |
| > `HeartbeatEvent`                            | > `OneBotHeartbeatEvent`               |
| `MessageEvent`                                | `OneBotMessageEvent`                   |
| > `GroupMessageEvent`                         | > `OneBotGroupMessageEvent`            |
| > `GroupMessageEvent`                         | > > `OneBotNormalGroupMessageEvent`    |
| > `GroupMessageEvent`                         | > > `OneBotAnonymousGroupMessageEvent` |
| > `GroupMessageEvent`                         | > > `OneBotNoticeGroupMessageEvent`    |
| > `PrivateMessageEvent`                       | > `OneBotPrivateMessageEvent`          |
| > `PrivateMessageEvent`                       | > > `OneBotFriendMessageEvent`         |
| > `PrivateMessageEvent`                       | > > `OneBotGroupPrivateMessageEvent`   |
| `RequestEvent`                                | `OneBotRequestEvent`                   |
| > `FriendRequestEvent`                        | > `OneBotFriendRequestEvent`           |
| > `GroupRequestEvent`                         | > `OneBotGroupRequestEvent`            |
| `NoticeEvent`                                 | `OneBotNoticeEvent`                    |
| > `FriendAddEvent`                            | > `OneBotFriendAddEvent`               |
| > `FriendRecallEvent`                         | > `OneBotFriendRecallEvent`            |
| > `GroupAdminEvent`                           | > `OneBotGroupAdminEvent`              |
| > `GroupBanEvent`                             | > `OneBotGroupBanEvent`                |
| > `GroupIncreaseEvent` 或 `GroupDecreaseEvent` | > `OneBotGroupChangeEvent`             |
| > `GroupIncreaseEvent`                        | > > `OneBotGroupMemberIncreaseEvent`   |
| > `GroupDecreaseEvent`                        | > > `OneBotGroupMemberDecreaseEvent`   |
| > `GroupRecallEvent`                          | > `OneBotGroupRecallEvent`             |
| > `GroupUploadEvent`                          | > `OneBotGroupUploadEvent`             |
| > `NotifyEvent`                               | > `OneBotNotifyEvent`                  |
| > `NotifyEvent`                               | > > `OneBotHonorEvent`                 |
| > `NotifyEvent`                               | > > `OneBotLuckyKingEvent`             |
| > `NotifyEvent`                               | > > `OneBotPokeEvent`                  |
| > `NotifyEvent`                               | > > > `OneBotMemberPokeEvent`          |
| > `NotifyEvent`                               | > > > `OneBotBotSelfPokeEvent`         |
| `UnknownEvent`                                | > `UnknownEvent`                       |
| 无                                             | `OneBotBotStageEvent`                  |
| 无                                             | > `OneBotBotRegisteredEvent`           |
| 无                                             | > `OneBotBotStartedEvent`              |
| 任意未支持事件                                       | `OneBotUnsupportedEvent`               |

其中，可以通过 `OneBotUnsupportedEvent` 和 `OneBotUnknownEvent`
来间接地监听那些尚未提供组件事件类型的原始事件。

> [!note]
> OB11协议中的事件类型均有实现。

## License

```
This program is free software: you can redistribute it and/or 
modify it under the terms of the GNU Lesser General 
Public License as published by the Free Software Foundation, 
either version 3 of the License, or (at your option) 
any later version.

This program is distributed in the hope that it will be useful, 
but WITHOUT ANY WARRANTY; without even the implied 
warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. 
See the GNU Lesser General Public License for more details.

You should have received a copy of the GNU Lesser General Public 
License along with this program. 
If not, see <https://www.gnu.org/licenses/>.
```
