# 使用OneBot11协议实现

## 前期准备

### Java

如果你打算使用 Java，请确保你的Java版本至少为 **Java11**，因为
<tooltip term="simbot4">simbot4</tooltip>
在JVM平台上最低要求Java11。

如果你打算配合使用Spring Boot v3.x版本，确保Java版本至少为 **Java17**。

### Kotlin

如果打算使用Kotlin，建议使用 Kotlin 2.0 以上版本。
具体的最低推荐版本以最新的 [simbot4版本](%simbot4-release%) 中的Kotlin版本为准。

### Kotlin multiplatform

如果打算使用Kotlin的其他非JVM平台或多平台，需要注意组件库中由于引入了Ktor(v2.x)，
因此暂时无法使用Ktor Client不支持的平台(例如 `wasm-js`)。

> Ktor Client 支持平台可参考 [官方文档](https://ktor.io/docs/client-supported-platforms.html)

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
<tab title="核心库">

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
<tab title="Spring Boot starter">

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

<warning>TODO</warning>

### 创建Application

### 注册Bot

### 事件监听

