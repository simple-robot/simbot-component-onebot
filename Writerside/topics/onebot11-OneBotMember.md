---
switcher-label: JavaAPI风格
---

<show-structure depth="2"/>

#  群成员 OneBotMember

`OneBotMember` 实现 `Member`, `OneBotStrangerAware`
以及其他一些功能接口（后续 "更多能力" 中会介绍），
用于表示一个 OneBot11 协议中的 **群成员**。

<warning>TODO</warning>


## Member

`OneBotMember` 实现来自 `Member` 定义的抽象属性或函数。

<deflist>
<def title="id">QQ号。</def>
<def title="avatar">成员QQ头像。</def>
<def title="name">用户名。</def>
<def title="nick">
在群内的昵称，
一些地方（比如协议中）也会称其为 <code>card</code> 。
</def>
</deflist>

## DeleteSupport
## OneBotStrangerAware
## 更多能力
## 获取 OneBotMember
