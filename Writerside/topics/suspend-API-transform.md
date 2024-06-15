# API桥接

<tip>

有关更多Java友好的内容，
请参考 [simbot4应用手册](https://simbot.forte.love/java-friendly.html)
。

</tip>

当使用Java时，挂起API会被桥接为三种风格的API：

<deflist>
<def title="阻塞API">

通常是 `xxxBlocking` 或 `getXxx`。
返回值类型与源挂起API的类型一致。
</def>
<def title="异步API">

通常是 `xxxAsync`。
调用时即启动异步任务，并返回 `CompletableFuture`。

</def>
<def title="预处理API">

通常是 `xxxReserve`。

返回类型为 `SuspendReserve`，
需要通过 `transform(...)` 提供一个转化器来真正执行逻辑并处理结果。
`SuspendReserves` 中的静态API提供了一些预设的转化器。

通常用于转化为响应式的结果，例如 `Mono`：

```Java
var reserve = xxxReserve(...);
// 转化为 Mono。
reserve.transform(
    SuspendReserve.mono()
).subscribe();
```

</def>
</deflist>

<warning title="注意异常处理">

在使用异步API或响应式API时，注意异步结果的处理，例如异常处理。
在搜索引擎中查询它们的结果类型
(例如`CompletableFuture`) 了解更多，
或参阅 [核心库说明](https://simbot.forte.love/java-friendly.html#阻塞与异步)
中的小贴士。
</warning>
