# 行为对象

行为对象，指的是实现了simbot标准API中 `Actor` 接口的类型，
例如 `OneBotMember`、`OneBotGroup` 等，
它们与 `OneBotBot` 存在某种关系、并具有很多行为性的API。

它们通常被定义在 `simbot-component-onebot-v11-core` 
模块中的 `love.forte.simbot.component.onebot.v11.core.actor`
包路径下。

## CoroutineScope

所有行为对象均实现 `Actor`，而 `Actor` 则实现 `CoroutineScope`，
因此所有的行为对象均可作为一个协程作用域。

例如：

```kotlin
val group: OneBotGroup = ...

// 异步任务
group.launch { ... }
```

它们的协程上下文来自其各自所属的 `OneBotBot`，但是其中不包含 `Job`。

## 异步API&响应式/预处理API

参考
<a href="suspend-API-transform.md" />
。
