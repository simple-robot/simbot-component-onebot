---
switcher-label: JavaAPI风格
---

<show-structure depth="2"/>

# OneBotBot

表示一个 OneBot 的 bot 客户端实例，实现 `Bot`，由 `OneBotBotManager` 注册产生。

## Bot

`OneBot` 实现simbot标准接口 `Bot`，提供可支持的属性或能力。

<deflist>
<def id="prop-id" title="id">

配置中提供的 `botUniqueId` ，
用与 `OneBotBotManager` 中作为唯一标识。
</def>
<def id="prop-isMe" title="isMe(ID)">

判断提供的 `ID` 是否为自己。
首先依据 `id` 属性，如果内部的 `userId` 已经初始化，
则也会依据 `userId` 进行判断。

</def>
<def id="prop-name" title="name">

Bot自己的用户名。需要 `start` 后才会初始化，
否则获取会抛出异常。

</def>
<def id="prop-contactRelation" title="contactRelation">

联系人相关操作，即好友相关的关系操作。

</def>
<def id="prop-groupRelation" title="groupRelation">

与群聊相关的操作。

</def>
<def id="prop-guildRelation" title="guildRelation">

OneBot11协议不支持 `Guild` (即频道) 相关的操作，始终得到 `null`。

</def>
<def id="prop-start" title="start()">

</def>
</deflist>

除了上述的属性和API，`OneBotBot` 还提供了一些额外的内容：

<deflist>
<def id="prop-decoderJson" title="decoderJson"></def>
<def id="prop-configuration" title="configuration"></def>
<def id="prop-apiClient" title="apiClient"></def>
<def id="prop-apiHost" title="apiHost"></def>
<def id="prop-accessToken" title="accessToken"></def>
<def id="prop-userId" title="userId"></def>
<def id="prop-queryLoginInfo" title="queryLoginInfo()"></def>
<def id="prop-getCookies" title="getCookies(...)"></def>
<def id="prop-getCredentials" title="getCredentials(...)"></def>
<def id="prop-getCsrfToken" title="getCsrfToken()"></def>
</deflist>

### 关系对象

即对好友和群的相关操作，通过 `contactRelation` 和 `groupRelation` 进行。

#### OneBotBotFriendRelation
#### OneBotBotGroupRelation


<warning>TODO</warning>
