# 消息

## 标准消息元素

作为一个simbot组件，OneBot组件理所当然的会支持部分simbot核心库所定义的标准消息元素。

<deflist>
<def id="Text" title="Text"></def>
<def id="At" title="At"></def>
<def id="Face" title="Face"></def>
<def id="Image" title="Image"></def>
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
<def id="OneBotAnonymous" title="OneBotAnonymous">

</def>
<def id="OneBotAt" title="OneBotAt">

</def>
<def id="OneBotContact" title="OneBotContact">

</def>
<def id="OneBotDice" title="OneBotDice">

</def>
<def id="OneBotFace" title="OneBotFace">

</def>
<def id="OneBotForward" title="OneBotForward.kt">

</def>
<def id="OneBotImage" title="OneBotImage">

</def>
<def id="OneBotJson" title="OneBotJson">

</def>
<def id="OneBotLocation" title="OneBotLocation">

</def>
<def id="OneBotMusic" title="OneBotMusic">

</def>
<def id="OneBotPoke" title="OneBotPoke">

</def>
<def id="OneBotRecord" title="OneBotRecord">

</def>
<def id="OneBotReply" title="OneBotReply">

</def>
<def id="OneBotRps" title="OneBotRps">

</def>
<def id="OneBotShake" title="OneBotShake">

</def>
<def id="OneBotShare" title="OneBotShare">

</def>
<def id="OneBotText" title="OneBotText">

</def>
<def id="OneBotVideo" title="OneBotVideo">

</def>
<def id="OneBotXml" title="OneBotXml">

</def>
<def id="OneBotUnknownSegment" title="OneBotUnknownSegment">

一个当出现了除上述其他已知类型以外的消息段类型时使用的包装类型。
它通过对 `SerializersModule` 的配置增加了 `OneBotMessageSegment`
类型的默认序列化/反序列化器来支持解析为此默认类型。

它只支持JSON结构，因为它使用了 `JsonObject` 作为 `data` 属性的类型。

<warning title="实验性">

`OneBotUnknownSegment` 是实验性的。可能不稳定或在未来发生变更、移除。

</warning>

</def>
</deflist>

