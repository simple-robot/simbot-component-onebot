# 贡献指南

♥ 首先，非常感谢您愿意花时间阅读本指南并了解如何为 Simple Robot (下文简称 `simbot`) 的 [OneBot组件][OB-GH] 做出贡献！ ♥

本指南会主要介绍 [OneBot组件][OB-GH] (下文简称 `OB组件`) 的主要结构、规范以及贡献方式~

## 贡献方式

你可以通过创建 [拉取请求(pull request)][OB-PR] 的方式提供代码方面的贡献（例如某些功能的实现或问题的修复、某些错误文档注释的修正等），
或者通过 [议题反馈(issues)][OB-IS] 的方式以问题反馈的方式做出贡献。

### 拉取请求

**议题讨论**

如果你打算进行代码贡献，那么首先，你需要先考虑创建一个[议题][OB-IS]并描述你希望贡献的内容。
这有助于我们和其他人了解到你打算进行的工作，以免出现不必要的重复劳动或误会等。

当议题的内容经讨论后不是一个已完成的任务、不需要的任务、或者他人已经在进行的任务（换言之，它是一个可行的任务内容），
你便可以开始着手准备编码了。

**拉取、构建项目**

如果是首次贡献，你需要将[OB组件][OB-GH]fork至你的账户中，然后克隆你fork后的项目并在其中做出更改。
否则，你需要查看并确保你要进行开发的内容主分支是最新的。

> 最终合并的目标分支可以在前文所述的议题中讨论，但是一般来讲它是项目的默认分支。
> 早期开发阶段，它可能是最原始的 `master` 或 `main`；在早期开发阶段结束后，它通常是 `dev/main` 等开发分支。（以实际情况为准）

如果是首次拉取项目，你可以参考下文 [构建并校验项目](#构建并校验项目) 中的内容来了解如何构建项目。

**编写代码**

当项目构建完毕、准备就绪时，便可以开始编写代码了。
你可以参考下文 [代码风格与内容格式](#代码风格与内容格式) 中的内容来了解我们约定好的代码风格与内容格式要求。

当功能实现完毕后，你可以参考下文的 [校验项目](#校验项目) 来了解如何在提交拉取请求前先行本地确保代码可靠性与质量。

然后，通过向我们的[OneBot组件仓库][OB-GH]创建[拉取请求][OB-PR]来提交你的更改。
我们会对你提交的内容进行一定程度的审查，在这过程中可能会伴随着我们之间的多次交流与进一步的代码建议、更改。

当一切就绪、没有意外后，我们会合并你的提交，至此一次代码贡献的流程便结束了，感谢你的贡献！

### 议题反馈

如果你发现了一些错误、Bug或不符合预期的情况，则可以创建一个[议题][OB-IS]来向我们反馈。
通常情况下，创建议题页面会将你引导至 [Simple Robot 核心库议题](https://github.com/simple-robot/simpler-robot/issues) 处，
在那里统一汇总各方面的问题，其中也包括组件的问题。

当然，如果你明确清楚你所要反馈的问题与OB组件本身强相关，不会涉及到核心库，你仍然可以直接创建一个OB组件仓库的议题。

## 模块

OB组件的主要模块目前有：

- `simbot-component-onebot11-core` 基于 [OneBot11][OB11] 的simbot组件实现模块。

与其他的一些组件 (比如KOOK组件、Telegram组件等) 不太一样的是，
OB组件没有提供类似于 `stdlib` 这样可以单独作为一个轻量级SDK使用的模块。

> [!note]
> 目前，我们暂时不考虑轻易接受**添加更多模块**的贡献请求。如果你认为有这方面的必要，可以创建一个议题进行讨论。

### simbot-component-onebot11-core

OB11模块的内容可以大致分为以下这么几个部分：

- 对消息段内容的实现
- 对 API 的实现
- 基于上述两条，对simbot标准API的实现（比如 `Bot`、事件等）。

### 消息元素与消息段

OB组件中对simbot的消息元素类型 `Message.Element` 的实现类型 `OneBotMessageElement`
定义在 `love.forte.simbot.component.onebot.v11.core.message` 中，它是OB组件中所有消息元素类型的父类。

我们选择使用具有明确结构的
[消息段](https://github.com/botuniverse/onebot-11/blob/master/message/segment.md)
的形式来作为OB11的消息元素的实现格式。
它们的封装实现类型在子包 `segment` 下，并统一实现父类型 `OneBotMessageSegment`。

消息段使用 Kotlin Serialization 支持序列化，并将消息段内容中的 `type` 以多态的形式实现。
以 [纯文本消息段](https://github.com/botuniverse/onebot-11/blob/master/message/segment.md#%E7%BA%AF%E6%96%87%E6%9C%AC) 为例：

```kotlin
@Serializable
@SerialName(OneBotText.TYPE)
public class OneBotText private constructor(override val data: Data) :
 OneBotMessageSegment,
 PlainText {
 override val text: String get() = ...

 public companion object Factory {
     public const val TYPE: String = "text"

     @JvmStatic
     public fun create(text: String): OneBotText = ...
 }

 @Serializable
 public data class Data internal constructor(val text: String)

 override fun equals(other: Any?): Boolean { ... }
  
 override fun hashCode(): Int { ... }

 override fun toString(): String { ... }
}
```

由于消息段的结构要求，所有消息段的实现都是内含一个 `data` 类型来囊括具体的属性内容。

```json
{
  "type": "...",
  "data": { "一些属性": "一些值" }
}
```

这个类型通常可以直接定义在消息段类型内，并直接命名为 `Data`。

为了统一风格，消息元素应当以**隐藏构造**并公开**工厂方法**的方式对外提供构建API，工厂方法通常命名为 `create`，
且应当在JVM被编译为静态 (标记 `@JvmStatic`)。

部分含义与simbot标准消息元素有重合的 (比如文本消息段，在simbot标准消息类型中可以对应 `PlainText`)，
需要考虑额外实现这些类型。

#### 不可变性

消息元素应当是一种不可变的数据载体类，不应存在可变状态，不应有可能会导致行为不一致的可变属性。

#### 序列化

消息元素 `Message.Element` 不保证消息的绝对可序列化，但是OB11中的消息段都是 JSON 格式的数据，
一般来讲不存在不可序列化的内容。
因此通常情况下OB组件中的消息段实现都应是可序列化的。

### API

OB组件中对
[OB11的API](https://github.com/botuniverse/onebot-11/tree/master/api)
进行实现的内容，它们被统一定义在
`love.forte.simbot.component.onebot.v11.core.api`
中，均实现 `OneBotApi`。

API的实现目标是提供对OB11的API的抽象，我们希望它能够做到：

- 可以通过 API 实现来获取到发起请求所需的全部信息（比如API的 `action`、请求体等）
- 可以便捷地使用 Ktor 作为客户端发起请求 (提供了对结果的解析器(基于Kotlin Serialization)，并提供额外的扩展API统一实现请求)

#### 不可变性

API的实现应当是一种不可变的数据载体类，不应存在可变状态，不应有可能会导致行为不一致的可变属性。

### simbot标准API

最重要也是最复杂的部分。基于上述两条，对simbot标准API中约定的功能进行实现。

实现simbot标准API时需要根据可提供的功能进行适当"收放":
- 不支持的功能，根据情况抛出 `UnsupportedOperationException` (常用) 或返回默认值。
- 可以额外支持的功能，定义时给出详细的说明内容。
- 可以扩展的返回值结果，需要确保同类型API的返回值类型定义相同。

对于第三条，举个例子，OB组件中Bot的实现类型可以获取到的**成员**类型被定义为 `OneBotMember`，
那么所有实现的可以获取到**成员**的API的返回值都应调整为 `OneBotMember`。


## 功能原则
### 公开抽象、隐藏实现

在提供组件实现时，应当首先考虑对外**公开接口或抽象**，内部**隐藏实现细节**。

### 确保兼容性

当对某个现存的功能进行修改或迭代时，应当尽可能确保公开内容的二进制**向下兼容**，至少优先保证JVM平台的向下兼容性。
在实在难以保证的情况下，版本号的minor数字应递增，且应当有详细的警告与说明。

## 代码风格与内容格式

代码的风格和潜在隐患的排查通过 `detekt` 完成。可参考下文 [detekt](#detekt) 来了解更多信息。

除了可以通过detekt进行检测的内容以外，还有一些其他要求。

所有公开API(比如类型定义、函数、属性等)都需要有较为清晰的文档注释来对其进行描述。
且在类型定义上，使用 `@author` 添加你的信息，例如:

```kotlin
/**
 * 描述
 *
 * @author Your name
 */
interface Foo

/**
 * 描述
 *
 * @author Your name
 */
class Bar
```

如果是继承的属性或方法，且没有对其的进一步说明，则可以酌情考虑省略文档注释。

其他的非公开内容(比如 `internal` 或 `private`)则在你认为必要的时候添加对逻辑的内容的说明，
以便于其他开发者的理解。除非是十分简单的逻辑，否则我们建议你同样留下对这些内容的说明。


## 构建并校验项目
### 构建项目

OB组件使用 [Gradle](https://gradle.org/) 构建，确保你的开发环境能够支持使用 Gradle 进行开发。

OB组件基于 Kotlin Multiplatform，且 JVM 平台目标最低使用 Java11，确保你的开发环境能够满足此要求。

当项目初始化完毕后，你可以运行Gradle命令 `gradle assemble build` 来构建并校验项目。
同时，项目中部分地方使用了 [KSP](https://kotlinlang.org/docs/ksp-overview.html) 生成代码，
使用命令也可以使得这些代码被生成出来，消除错误。

### 校验项目

OB组件使用单元测试和 [`detekt`](https://detekt.dev/) 来确保代码可靠性与质量。

#### 单元测试

在开发过程中，在对应的测试模块编写对应功能的单元测试，并使用 `gradle allTests` 来校验测试结果。

#### detekt

OB组件引用了 detekt 来校验、统一代码风格和部分潜在风险，并可以自动修复它们，
你可以在项目根目录的 `config/detekt` 中找到它的配置文件。

使用 `gradle detekt` 校验并自动修复代码中的风格与潜在风险。
根据配置，如果存在不符合规范且无法自动修复的内容，构建将会失败，此时需要根据错误提示修复对应的内容。

> [!TIP]
> 部分IDE(比如 `IntelliJ IDEA`) 会提供detekt插件，可以根据配置文件提供代码提示与警告。
> 你可以考虑借助这些插件更快地了解到这些潜在问题或更便捷地修复它们。

> [!NOTE]
> 在项目根目录中地 `build.gradle.kts` 中，对 `detekt` 进行了进一步的配置：
> `gradle detekt` 不会检测 `internal-processors` 目录内的内容以及测试模块内的内容。




[OB-GH]: https://github.com/Simple-robot/simbot-component-onebot/
[OB-PR]: https://github.com/Simple-robot/simbot-component-onebot/pulls/
[OB-IS]: https://github.com/Simple-robot/simbot-component-onebot/issues/
[OB11]: https://github.com/botuniverse/onebot-11