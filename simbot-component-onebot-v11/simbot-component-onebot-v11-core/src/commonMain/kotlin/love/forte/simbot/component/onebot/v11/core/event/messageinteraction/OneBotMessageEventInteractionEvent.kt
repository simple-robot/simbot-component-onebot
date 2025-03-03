/*
 * Copyright (c) 2025. ForteScarlet.
 *
 * This file is part of simbot-component-onebot.
 *
 * simbot-component-onebot is free software: you can redistribute it and/or modify it under the terms
 * of the GNU Lesser General Public License as published by the Free Software Foundation,
 * either version 3 of the License, or (at your option) any later version.
 *
 * simbot-component-onebot is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY;
 * without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.
 * See the GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License along with simbot-component-onebot.
 * If not, see <https://www.gnu.org/licenses/>.
 */

package love.forte.simbot.component.onebot.v11.core.event.messageinteraction

import love.forte.simbot.component.onebot.v11.core.bot.OneBotBot
import love.forte.simbot.component.onebot.v11.core.event.message.*
import love.forte.simbot.event.*


/**
 * OneBot组件中针对 [MessageEvent] 的拦截或通知事件。
 *
 * @see love.forte.simbot.component.onebot.v11.core.event.message.OneBotMessageEvent
 *
 * @since 1.6.0
 *
 * @author ForteScarlet
 */
@SubclassOptInRequired(FuzzyEventTypeImplementation::class)
public interface OneBotMessageEventInteractionEvent :
    MessageEventInteractionEvent,
    OneBotInternalMessageInteractionEvent {
    override val bot: OneBotBot
    override val content: OneBotMessageEvent
}

/**
 * OneBot组件中针对 [MessageEvent.reply] 的拦截事件。
 * 可以对其中的参数进行修改。
 *
 * @since 1.6.0
 */
@OptIn(FuzzyEventTypeImplementation::class)
public interface OneBotMessageEventPreReplyEvent : OneBotMessageEventInteractionEvent, MessageEventPreReplyEvent {
    override val content: OneBotMessageEvent
}

/**
 * OneBot组件中针对 [MessageEvent.reply] 的通知事件。
 *
 * @since 1.6.0
 */
@OptIn(FuzzyEventTypeImplementation::class)
public interface OneBotMessageEventPostReplyEvent : OneBotMessageEventInteractionEvent, MessageEventPostReplyEvent {
    override val content: OneBotMessageEvent
    override val message: OneBotSegmentsInteractionMessage
}

//region Group

/**
 * OneBot组件中针对 [ChatGroupMessageEvent] 的拦截或通知事件。
 *
 * @since 1.6.0
 * @see love.forte.simbot.component.onebot.v11.core.event.message.OneBotGroupMessageEvent
 */
@OptIn(FuzzyEventTypeImplementation::class)
public interface OneBotGroupMessageEventInteractionEvent :
    OneBotMessageEventInteractionEvent {
    override val content: OneBotGroupMessageEvent
}

/**
 * OneBot组件中针对 [ChatGroupMessageEvent.reply] 的拦截事件。
 * 可以对其中的参数进行修改。
 *
 * @since 1.6.0
 */
@OptIn(FuzzyEventTypeImplementation::class)
public interface OneBotGroupMessageEventPreReplyEvent :
    OneBotGroupMessageEventInteractionEvent,
    OneBotMessageEventPreReplyEvent {
    override val content: OneBotGroupMessageEvent
}

/**
 * OneBot组件中针对 [ChatGroupMessageEvent.reply] 的通知事件。
 *
 * @since 1.6.0
 */
@OptIn(FuzzyEventTypeImplementation::class)
public interface OneBotGroupMessageEventPostReplyEvent :
    OneBotGroupMessageEventInteractionEvent,
    OneBotMessageEventPostReplyEvent {
    override val content: OneBotGroupMessageEvent
    override val message: OneBotSegmentsInteractionMessage
}

// OneBotNormalGroupMessageEvent

/**
 * OneBot组件中针对 [OneBotNormalGroupMessageEvent] 的拦截或通知事件。
 * @since 1.6.0
 */
public interface OneBotNormalGroupMessageEventInteractionEvent :
    OneBotGroupMessageEventInteractionEvent,
    ChatGroupMessageEventInteractionEvent {
    override val content: OneBotNormalGroupMessageEvent
}

/**
 * OneBot组件中针对 [OneBotNormalGroupMessageEvent.reply] 的拦截事件。
 *
 * @since 1.6.0
 */
public interface OneBotNormalGroupMessageEventPreReplyEvent :
    OneBotNormalGroupMessageEventInteractionEvent,
    OneBotGroupMessageEventPreReplyEvent {
    override val content: OneBotNormalGroupMessageEvent
}

/**
 * OneBot组件中针对 [OneBotNormalGroupMessageEvent.reply] 的通知事件。
 * @since 1.6.0
 */
public interface OneBotNormalGroupMessageEventPostReplyEvent :
    OneBotNormalGroupMessageEventInteractionEvent,
    OneBotGroupMessageEventPostReplyEvent {
    override val content: OneBotNormalGroupMessageEvent
    override val message: OneBotSegmentsInteractionMessage
}

// OneBotAnonymousGroupMessageEvent

/**
 * OneBot组件中针对 [OneBotAnonymousGroupMessageEvent] 的拦截或通知事件。
 * @since 1.6.0
 */
public interface OneBotAnonymousGroupMessageEventInteractionEvent :
    OneBotGroupMessageEventInteractionEvent,
    ChatGroupMessageEventInteractionEvent {
    override val content: OneBotAnonymousGroupMessageEvent
}

/**
 * OneBot组件中针对 [OneBotAnonymousGroupMessageEvent.reply] 的拦截事件。
 *
 * @since 1.6.0
 */
public interface OneBotAnonymousGroupMessageEventPreReplyEvent :
    OneBotAnonymousGroupMessageEventInteractionEvent,
    OneBotGroupMessageEventPreReplyEvent {
    override val content: OneBotAnonymousGroupMessageEvent
}

/**
 * OneBot组件中针对 [OneBotAnonymousGroupMessageEvent.reply] 的通知事件。
 * @since 1.6.0
 */
public interface OneBotAnonymousGroupMessageEventPostReplyEvent :
    OneBotAnonymousGroupMessageEventInteractionEvent,
    OneBotGroupMessageEventPostReplyEvent {
    override val content: OneBotAnonymousGroupMessageEvent
    override val message: OneBotSegmentsInteractionMessage
}

// OneBotNoticeGroupMessageEvent

/**
 * OneBot组件中针对 [OneBotNoticeGroupMessageEvent] 的拦截或通知事件。
 * @since 1.6.0
 */
public interface OneBotNoticeGroupMessageEventInteractionEvent :
    OneBotGroupMessageEventInteractionEvent {
    override val content: OneBotNoticeGroupMessageEvent
}

/**
 * OneBot组件中针对 [OneBotNoticeGroupMessageEvent.reply] 的拦截事件。
 *
 * @since 1.6.0
 */
public interface OneBotNoticeGroupMessageEventPreReplyEvent :
    OneBotNoticeGroupMessageEventInteractionEvent,
    OneBotGroupMessageEventPreReplyEvent {
    override val content: OneBotNoticeGroupMessageEvent
}

/**
 * OneBot组件中针对 [OneBotNoticeGroupMessageEvent.reply] 的通知事件。
 * @since 1.6.0
 */
public interface OneBotNoticeGroupMessageEventPostReplyEvent :
    OneBotNoticeGroupMessageEventInteractionEvent,
    OneBotGroupMessageEventPostReplyEvent {
    override val content: OneBotNoticeGroupMessageEvent
    override val message: OneBotSegmentsInteractionMessage
}

//endregion

// Privates

//region Private

/**
 * OneBot组件中针对 [OneBotPrivateMessageEvent] 的拦截或通知事件。
 *
 * @since 1.6.0
 */
@OptIn(FuzzyEventTypeImplementation::class)
public interface OneBotPrivateMessageEventInteractionEvent :
    OneBotMessageEventInteractionEvent {
    override val content: OneBotPrivateMessageEvent
}

/**
 * OneBot组件中针对 [OneBotPrivateMessageEvent.reply] 的拦截事件。
 * 可以对其中的参数进行修改。
 *
 * @since 1.6.0
 */
@OptIn(FuzzyEventTypeImplementation::class)
public interface OneBotPrivateMessageEventPreReplyEvent :
    OneBotPrivateMessageEventInteractionEvent,
    OneBotMessageEventPreReplyEvent {
    override val content: OneBotPrivateMessageEvent
}

/**
 * OneBot组件中针对 [OneBotPrivateMessageEvent.reply] 的通知事件。
 *
 * @since 1.6.0
 */
@OptIn(FuzzyEventTypeImplementation::class)
public interface OneBotPrivateMessageEventPostReplyEvent :
    OneBotPrivateMessageEventInteractionEvent,
    OneBotMessageEventPostReplyEvent {
    override val content: OneBotPrivateMessageEvent
    override val message: OneBotSegmentsInteractionMessage
}
//region Group Private

/**
 * OneBot组件中针对 [OneBotPrivateMessageEvent] 的拦截或通知事件。
 *
 * @since 1.6.0
 */
@OptIn(FuzzyEventTypeImplementation::class)
public interface OneBotGroupPrivateMessageEventInteractionEvent :
    OneBotPrivateMessageEventInteractionEvent {
    override val content: OneBotGroupPrivateMessageEvent
}

/**
 * OneBot组件中针对 [OneBotPrivateMessageEvent.reply] 的拦截事件。
 * 可以对其中的参数进行修改。
 *
 * @since 1.6.0
 */
@OptIn(FuzzyEventTypeImplementation::class)
public interface OneBotGroupPrivateMessageEventPreReplyEvent :
    OneBotGroupPrivateMessageEventInteractionEvent,
    OneBotPrivateMessageEventPreReplyEvent {
    override val content: OneBotGroupPrivateMessageEvent
}

/**
 * OneBot组件中针对 [OneBotPrivateMessageEvent.reply] 的通知事件。
 *
 * @since 1.6.0
 */
@OptIn(FuzzyEventTypeImplementation::class)
public interface OneBotGroupPrivateMessageEventPostReplyEvent :
    OneBotGroupPrivateMessageEventInteractionEvent,
    OneBotPrivateMessageEventPostReplyEvent {
    override val content: OneBotGroupPrivateMessageEvent
    override val message: OneBotSegmentsInteractionMessage
}
//endregion

//region Friend Private

/**
 * OneBot组件中针对 [OneBotFriendMessageEvent] 的拦截或通知事件。
 *
 * @since 1.6.0
 */
@OptIn(FuzzyEventTypeImplementation::class)
public interface OneBotFriendMessageEventInteractionEvent :
    OneBotPrivateMessageEventInteractionEvent {
    override val content: OneBotFriendMessageEvent
}

/**
 * OneBot组件中针对 [OneBotFriendMessageEvent.reply] 的拦截事件。
 * 可以对其中的参数进行修改。
 *
 * @since 1.6.0
 */
@OptIn(FuzzyEventTypeImplementation::class)
public interface OneBotFriendMessageEventPreReplyEvent :
    OneBotFriendMessageEventInteractionEvent,
    OneBotPrivateMessageEventPreReplyEvent {
    override val content: OneBotFriendMessageEvent
}

/**
 * OneBot组件中针对 [OneBotFriendMessageEvent.reply] 的通知事件。
 *
 * @since 1.6.0
 */
@OptIn(FuzzyEventTypeImplementation::class)
public interface OneBotFriendMessageEventPostReplyEvent :
    OneBotFriendMessageEventInteractionEvent,
    OneBotPrivateMessageEventPostReplyEvent {
    override val content: OneBotFriendMessageEvent
    override val message: OneBotSegmentsInteractionMessage
}
//endregion
//endregion
