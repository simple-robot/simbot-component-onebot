/*
 * Copyright (c) 2024. ForteScarlet.
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

package love.forte.simbot.component.onebot.v11.core.utils

import love.forte.simbot.common.id.ID
import love.forte.simbot.component.onebot.common.annotations.InternalOneBotAPI
import love.forte.simbot.component.onebot.v11.core.api.OneBotMessageOutgoing
import love.forte.simbot.component.onebot.v11.core.api.SendMsgApi
import love.forte.simbot.component.onebot.v11.message.segment.OneBotMessageSegment
import love.forte.simbot.component.onebot.v11.message.segment.OneBotReply
import love.forte.simbot.component.onebot.v11.message.segment.OneBotText

/**
 * 构建一个用于发送的纯文本消息。
 *
 * @param messageType see [SendMsgApi]`.MESSAGE_TYPE_*`
 */
@InternalOneBotAPI
public fun sendTextMsgApi(
    messageType: String,
    target: ID,
    text: String,
    reply: ID? = null,
): SendMsgApi {
    return if (reply == null) {
        SendMsgApi.create(
            messageType = messageType,
            userId = target.takeIf { messageType == SendMsgApi.MESSAGE_TYPE_PRIVATE },
            groupId = target.takeIf { messageType == SendMsgApi.MESSAGE_TYPE_GROUP },
            message = OneBotMessageOutgoing.create(text),
            autoEscape = true
        )
    } else {
        SendMsgApi.create(
            messageType = messageType,
            userId = target.takeIf { messageType == SendMsgApi.MESSAGE_TYPE_PRIVATE },
            groupId = target.takeIf { messageType == SendMsgApi.MESSAGE_TYPE_GROUP },
            message = OneBotMessageOutgoing.create(
                listOf(
                    OneBotReply.create(reply),
                    OneBotText.create(text),
                )
            ),
        )
    }
}

@InternalOneBotAPI
public fun sendPrivateTextMsgApi(
    target: ID,
    text: String,
    reply: ID? = null,
): SendMsgApi = sendTextMsgApi(
    messageType = SendMsgApi.MESSAGE_TYPE_PRIVATE,
    target = target,
    text = text,
    reply = reply,
)

@InternalOneBotAPI
public fun sendGroupTextMsgApi(
    target: ID,
    text: String,
    reply: ID? = null,
): SendMsgApi = sendTextMsgApi(
    messageType = SendMsgApi.MESSAGE_TYPE_GROUP,
    target = target,
    text = text,
    reply = reply,
)


/**
 * 构建一个发送 segments 的纯文本消息。
 *
 * @param messageType see [SendMsgApi]`.MESSAGE_TYPE_*`
 */
@InternalOneBotAPI
public fun sendMsgApi(
    messageType: String,
    target: ID,
    message: List<OneBotMessageSegment>,
    reply: ID? = null,
): SendMsgApi {
    return if (reply == null) {
        SendMsgApi.create(
            messageType = messageType,
            userId = target.takeIf { messageType == SendMsgApi.MESSAGE_TYPE_PRIVATE },
            groupId = target.takeIf { messageType == SendMsgApi.MESSAGE_TYPE_GROUP },
            message = OneBotMessageOutgoing.create(message),
            autoEscape = true
        )
    } else {
        val newMessage = resolveReplyMessageSegmentList(message, reply)

        SendMsgApi.create(
            messageType = messageType,
            userId = target.takeIf { messageType == SendMsgApi.MESSAGE_TYPE_PRIVATE },
            groupId = target.takeIf { messageType == SendMsgApi.MESSAGE_TYPE_GROUP },
            message = OneBotMessageOutgoing.create(newMessage)
        )
    }
}

@InternalOneBotAPI
public fun resolveReplyMessageSegmentList(
    message: List<OneBotMessageSegment>,
    reply: ID,
): List<OneBotMessageSegment> {
    val newMessage = ArrayList<OneBotMessageSegment>(message.size + 1)
    var hasReply = false
    for (segment in message) {
        if (!hasReply && segment is OneBotReply) {
            hasReply = true
        }

        newMessage.add(segment)
    }

    if (!hasReply) {
        newMessage.add(0, OneBotReply.create(reply))
    }
    return newMessage
}

@InternalOneBotAPI
public fun sendPrivateMsgApi(
    target: ID,
    message: List<OneBotMessageSegment>,
    reply: ID? = null,
): SendMsgApi = sendMsgApi(
    messageType = SendMsgApi.MESSAGE_TYPE_PRIVATE,
    target = target,
    message = message,
    reply = reply,
)

@InternalOneBotAPI
public fun sendGroupMsgApi(
    target: ID,
    message: List<OneBotMessageSegment>,
    reply: ID? = null,
): SendMsgApi = sendMsgApi(
    messageType = SendMsgApi.MESSAGE_TYPE_GROUP,
    target = target,
    message = message,
    reply = reply,
)
