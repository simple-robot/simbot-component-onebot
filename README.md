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
<a href="https://repo1.maven.org/maven2/love/forte/simbot/component/simbot-component-onebot-api/" target="_blank">
  <img alt="release" src="https://img.shields.io/maven-central/v/love.forte.simbot.component/simbot-component-onebot-api" /></a>
   <hr>
   <img alt="stars" src="https://img.shields.io/github/stars/simple-robot/simbot-component-onebot" />
   <img alt="forks" src="https://img.shields.io/github/forks/simple-robot/simbot-component-onebot" />
   <img alt="watchers" src="https://img.shields.io/github/watchers/simple-robot/simbot-component-onebot" />
   <img alt="repo size" src="https://img.shields.io/github/repo-size/simple-robot/simbot-component-onebot" />
   <img alt="issues" src="https://img.shields.io/github/issues-closed/simple-robot/simbot-component-onebot?color=green" />
   <img alt="last commit" src="https://img.shields.io/github/last-commit/simple-robot/simbot-component-onebot" />
   <a href="./COPYING"><img alt="copying" src="https://img.shields.io/github/license/simple-robot/simbot-component-onebot" /></a>

</div>

Simple Robot OneBot 组件是一个将
[OneBot11](https://github.com/botuniverse/onebot-11)
协议在
[Simple Robot](http://github.com/simple-robot/simpler-robot) 标准API下实现的组件库，
并由此提供simbot中的各项能力。

借助simbot核心库提供的能力，它可以支持很多高级功能和封装，比如组件协同、Spring支持等，
祝你快速开发 OneBot 客户端应用！

序列化和网络请求相关分别基于 [Kotlin serialization](https://github.com/Kotlin/kotlinx.serialization)
和 [Ktor](https://ktor.io/)。

> [!caution]
> WIP now.

## 文档与引导

- OneBot组件手册: *待施工*
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

## 配置文件

使用spring的时候用的。需要注意实际上是不允许使用注释的，这里只是方便展示。

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
  config: { 
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

## License

```
This program is free software: you can redistribute it and/or modify it under the terms of the GNU Lesser General 
Public License as published by the Free Software Foundation, either version 3 of the License, or (at your option) 
any later version.

This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied 
warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU Lesser General Public License for more 
details.

You should have received a copy of the GNU Lesser General Public License along with this program. 
If not, see <https://www.gnu.org/licenses/>.
```
