# Bot配置文件

Bot配置文件通常情况下是配合Spring Boot starter的时候用的。

当使用Spring Boot starter时，
配置文件放在资源目录 <path>resources</path> 中的 <path>/simbot-bots/</path> 目录下，
以 `.bot.json` 格式结尾，例如 `myBot.bot.json`。

<warning title="记得清理注释">

实际上JSON配置文件是**不允许**使用注释的，这里只是方便展示。

</warning>

<tabs>
<tab title="较完整示例">

```json
{
    // 固定值
    "component": "simbot.onebot11",
    "authorization": {
        // 唯一ID，作为组件内 Bot 的 id，用于组件内去重。可以随便编，但建议是bot的qq号
        "botUniqueId": "123456",
        // api地址，是个http/https服务器的路径，默认localhost:3000
        "apiServerHost": "http://localhost:3000",
        // 订阅事件的服务器地址，是个ws/wss路径，默认 `null`
        // 如果为 `null` 则不会连接 ws 和订阅事件
        "eventServerHost": "ws://localhost:3001",
        // 配置的 token，可以是null, 代表同时配置 apiAccessToken 和 eventAccessToken
        "accessToken": null,
        // 用于API请求时用的 token，默认 null
        "apiAccessToken": null,
        // 用于连接事件订阅ws时用的 token，默认 null
        "eventAccessToken": null
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
        // 当使用非 [OneBotImage] 类型作为图片资源发送消息时，
        // 默认根据 [Resource] 得到一个可能存在的 [OneBotImage.AdditionalParams]。
        // 注意！这无法影响直接使用 [OneBotImage] 的情况。
        // defaultImageAdditionalParams 默认为 `null`。
        "defaultImageAdditionalParams": {
            // default: null
            "localFileToBase64": null,
            "type": null,
            "cache": null,
            "proxy": null,
            "timeout": null
        }
    }
}
```

</tab>
<tab title="简单示例">

```json
{
  "component": "simbot.onebot11",
  "authorization": {
    "botUniqueId": "123456",
    "apiServerHost": "http://localhost:3000",
    "eventServerHost":"ws://localhost:3001"
  }
}
```

</tab>
</tabs>

## 属性说明

<warning>TODO</warning>
