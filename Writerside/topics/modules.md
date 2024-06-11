# 模块概述

## 公共模块

OneBot组件为所有协议实现的模块提供了一些共享内容的模块，
命名为 `simbot-component-onebot-common`。

此模块中会定义一些通用的类型或注解等。
对于普通开发者来讲可以不用过多关注，此模块由其他组件模块引用并使用。

## OneBot11

在OneBot组件中，我们提供了针对 [OneBot11](https://github.com/botuniverse/onebot-11)
协议的组件实现模块，它们的坐标以 `simbot-component-onebot-v11` 作为开头：

<list>
<li><control>simbot-component-onebot-v11-common</control>

在OneBot11协议的实现模块中进行共享的模块。
对于普通开发者来讲可以不用过多关注，此模块由其他组件模块引用并使用。
</li>
<li><control>simbot-component-onebot-v11-core</control>

OneBot11协议作为一个simbot组件的实现模块。通常会是你**真正使用**的模块。
</li>
<li><control>simbot-component-onebot-v11-event</control>

对OneBot11协议中的[原始事件](https://github.com/botuniverse/onebot-11/tree/master/event)
类型提供定义的模块，
被 `simbot-component-onebot-v11-core` 引用并依赖。

<tip>

需要注意的是这里的事件并不是simbot中的**事件**，而仅仅是一种数据类实现，
是对原始事件的JSON结构的基本映射。
</tip>
</li>
<li><control>simbot-component-onebot-v11-message</control>

对OneBot11协议中的[原始消息段](https://github.com/botuniverse/onebot-11/blob/master/message/segment.md)
类型提供定义的模块。

这里定义的大部分类型都是针对消息段的数据类实现，
是对它们的JSON结构的基本映射，
被 `simbot-component-onebot-v11-core` 引用并依赖。

</li>
</list>



