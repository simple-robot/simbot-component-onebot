# 消息

<include from="snippets.md" element-id="to-main-doc" />

## 标准消息元素

作为一个simbot组件，OneBot组件理所当然的会支持部分simbot核心库所定义的标准消息元素。

<deflist>
<def id="Text" title="Text">
纯文本消息。
</def>
<def id="At" title="At">
At某人。
</def>
<def id="AtAll" title="AtAll">
At全体。
</def>
<def id="Face" title="Face">
一个表情。
</def>
<def id="Image" title="Image">
图片类型的接口类型。

发送时可以使用simbot标准API中的 `Image` 实现类型，
例如 `OfflineImage` 的某个实现。

<warning title="本地图片与base64">

参考 [OneBotImage](#OneBotImage) 的说明，
如果发送本地图片文件，那么这里面有些注意事项需要你留意。

因为默认情况下，你直接发送 `FileResource`、`PathResource`
或其他可以表示**本地文件**的资源时，会优先默认直接使用它们的
**绝对路径** 而不是转化为 `base64`。如果你的OneBot服务端并非本地服务，
那么便会产生问题。
</warning>

</def>
</deflist>

## 消息段

在OneBot组件中，我们选择使用消息段的方式进行消息交互。
除了直接使用部分上述的simbot标准消息元素以外，
我们还提供了OneBot11协议中定义的所有消息段类型的实现。

当想要发送它们的时候，我们统一使用 `OneBotMessageSegmentElement` 对其进行包装，
表示这是一个OneBot的消息段元素。

<tabs group="code">
<tab title="Kotlin" group-key="Kotlin">

```Kotlin
val segment = ...
val element = segment.toElement()
```

</tab>
<tab title="Java" group-key="Java">

```Java
var segment = ...;
var element = OneBotMessageSegments.toElement(segment);
```

</tab>
</tabs>

### 消息段定义

<deflist>
<def id="OneBotText" title="OneBotText">

[纯文本](https://github.com/botuniverse/onebot-11/blob/master/message/segment.md#纯文本)。

> 实现了simbot标准消息类型 `PlainText` 接口。

</def>
<def id="OneBotAt" title="OneBotAt">

[@某人或@全体](https://github.com/botuniverse/onebot-11/blob/master/message/segment.md#某人)。


> 因为与simbot标准消息类型 `At` 或 `AtAll` 的表达能力相同，
> 因此在接收的消息中会直接解析为 `At` 或 `AtAll`，
> 不会收到内容为 `OneBotAt` 的消息段元素。

</def>
<def id="OneBotFace" title="OneBotFace">

[QQ表情](https://github.com/botuniverse/onebot-11/blob/master/message/segment.md#qq-表情)。

> 因为与simbot标准消息类型 `Face` 的表达能力相同，
> 因此在接收的消息中会直接解析为 `Face` ，
> 不会收到内容为 `OneBotFace` 的消息段元素。

</def>
<def id="OneBotAnonymous" title="OneBotAnonymous">

[匿名发消息](https://github.com/botuniverse/onebot-11/blob/master/message/segment.md#匿名发消息-)。

</def>
<def id="OneBotContact" title="OneBotContact">

[推荐好友/推荐群](https://github.com/botuniverse/onebot-11/blob/master/message/segment.md#推荐好友)。

</def>
<def id="OneBotDice" title="OneBotDice">

[掷骰子魔法表情](https://github.com/botuniverse/onebot-11/blob/master/message/segment.md#掷骰子魔法表情)。

</def>
<def id="OneBotForward" title="OneBotForward">

[消息转发/合并转发](https://github.com/botuniverse/onebot-11/blob/master/message/segment.md#合并转发-)。

`OneBotForward` 是通过事件接收到的元素类型。
</def>
<def id="OneBotForwardNode" title="OneBotForwardNode">

[消息转发节点/合并转发节点](https://github.com/botuniverse/onebot-11/blob/master/message/segment.md#合并转发节点-)。

<warning>
自定义合并转发节点的结构定义有些...迷惑。
它似乎一个节点只能描述一个用户所发送的消息内容，但是又没说如何聚合发送。

不过其实按照实际情况来看，如果你打算发送合并转发消息，
那么消息元素链中只能包含 `OneBotForwardNode` 内容的元素。
</warning>

</def>
<def id="OneBotImage" title="OneBotImage">

[图片](https://github.com/botuniverse/onebot-11/blob/master/message/segment.md#图片)。

**发送时:**

如果你发送的是一个本地图片文件（例如使用 `File` 或 `Path`），
而你希望发送时使用 `base64` 而不是此文件的**绝对路径**（默认是绝对路径），
那么在构建 `OneBotImage` 时请注意额外的配置：

<tabs group="code">
<tab title="Kotlin" group-key="Kotlin">

```Kotlin
val obimg = OneBotImage.create(
    Path("xxx.png").toResource(),
) {
    localFileToBase64 = true
}

val emement = obimg.toElement()
```

</tab>
<tab title="Java" group-key="Java">

```Java
var params = new OneBotImage.AdditionalParams();
params.setLocalFileToBase64(true);

var obimg = OneBotImage.create(
    Resources.valueOf(Path.of("xxx.png")),
    params
);

var element = obimg.toElement();
// 或使用 OneBotMessageSegments.toElement(obimg);
```

</tab>
</tabs>

当然，你也可以选择直接使用 `ByteArrayResource` 进行发送。

**接收时:** 

会直接转为 `OneBotImage.Element` 而不是 `DefaultOneBotMessageSegmentElement`，
不过它们都实现 `OneBotMessageSegmentElement`。

`OneBotImage.Element` 实现 `Image` 
并提供了一些辅助属性或API，比如获取 `Resource` 或 `url` 字符串。 
</def>
<def id="OneBotVideo" title="OneBotVideo">

[短视频](https://github.com/botuniverse/onebot-11/blob/master/message/segment.md#短视频)。

**发送时:**

如果你发送的是一个本地文件（例如使用 `File` 或 `Path`），
而你希望发送时使用 `base64` 而不是此文件的**绝对路径**（默认是绝对路径），
那么在构建 `OneBotVideo` 时请注意额外的配置：

<tabs group="code">
<tab title="Kotlin" group-key="Kotlin">

```Kotlin
val obvideo = OneBotVideo.create(
    Path("xxx.mp4").toResource(),
    OneBotVideo.AdditionalParams().apply {
        localFileToBase64 = true
    }
)

val emement = obvideo.toElement()
```

</tab>
<tab title="Java" group-key="Java">

```Java
var params = new OneBotVideo.AdditionalParams();
params.setLocalFileToBase64(true);

var video = OneBotVideo.create(
    Resources.valueOf(Path.of("xxx.mp4")),
    params
);

var element = OneBotMessageSegments.toElement(video);
```

</tab>
</tabs>

当然，你也可以选择直接使用 `ByteArrayResource` 进行发送。

</def>
<def id="OneBotLocation" title="OneBotLocation">

[位置](https://github.com/botuniverse/onebot-11/blob/master/message/segment.md#位置)。

</def>
<def id="OneBotMusic" title="OneBotMusic">

[音乐分享](https://github.com/botuniverse/onebot-11/blob/master/message/segment.md#音乐分享-)。

</def>
<def id="OneBotPoke" title="OneBotPoke">

[戳一戳](https://github.com/botuniverse/onebot-11/blob/master/message/segment.md#戳一戳)。

</def>
<def id="OneBotRecord" title="OneBotRecord">

[语音](https://github.com/botuniverse/onebot-11/blob/master/message/segment.md#语音)。

</def>
<def id="OneBotReply" title="OneBotReply">

[引用回复](https://github.com/botuniverse/onebot-11/blob/master/message/segment.md#回复)

如果使用 `reply` API 发送消息，且消息链中没有其他的内容为 `OneBotReply` 的元素，
则会自动附加一个。

</def>
<def id="OneBotRps" title="OneBotRps">

[猜拳魔法表情](https://github.com/botuniverse/onebot-11/blob/master/message/segment.md#猜拳魔法表情)

</def>
<def id="OneBotShake" title="OneBotShake">

[窗口抖动（戳一戳）](https://github.com/botuniverse/onebot-11/blob/master/message/segment.md#窗口抖动戳一戳-)

</def>
<def id="OneBotShare" title="OneBotShare">

[链接分享](https://github.com/botuniverse/onebot-11/blob/master/message/segment.md#链接分享)

</def>
<def id="OneBotXml" title="OneBotXml">

[XML 消息](https://github.com/botuniverse/onebot-11/blob/master/message/segment.md#xml-消息)。


</def>
<def id="OneBotJson" title="OneBotJson">

[JSON 消息](https://github.com/botuniverse/onebot-11/blob/master/message/segment.md#json-消息)。

</def>
<def id="OneBotUnknownSegment" title="OneBotUnknownSegment">

一个当出现了除上述其他已知类型以外的消息段类型时使用的包装类型。
它通过对 `SerializersModule` 的配置增加了 `OneBotMessageSegment`
类型的默认序列化/反序列化器来支持解析为此默认类型。

它只支持使用JSON序列化器，因为它使用了 `JsonElement` 作为 `data` 属性的类型。

<warning title="实验性">

`OneBotUnknownSegment` 是实验性的。可能不稳定或在未来发生变更、移除。

</warning>

</def>
</deflist>

