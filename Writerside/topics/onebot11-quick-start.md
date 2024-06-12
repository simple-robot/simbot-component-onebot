# 开始使用

开始使用OneBot11协议组件。

## 前期准备

### OneBot11协议服务端

OneBot组件是一个OneBot协议的**客户端**实现，
因此在使用之前，你需要安装、下载、启动一个支持OneBot11协议的**服务端**。

<warning>

鉴于OneBot协议的主要应用场景的特殊性，
大多数服务端的实现库都**不建议、不允许**公开讨论、宣传有关它们的信息(尤其是影响力较大的简中互联网网站)，
因此此处将 **不做列举**。

你可以选择去GitHub等开源网站搜索各种实现库，
或者可以前往 [社群](https://simbot.forte.love/communities.html)
向其他人寻求帮助。

</warning>


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
