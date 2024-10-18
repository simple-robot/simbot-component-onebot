package love.forte.simbot.component.onebot.v11.core.dialect

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.asFlow
import kotlinx.serialization.SerializationException
import kotlinx.serialization.json.JsonObject
import kotlinx.serialization.json.jsonPrimitive
import kotlinx.serialization.json.long
import love.forte.simbot.annotations.FragileSimbotAPI
import love.forte.simbot.common.id.ID
import love.forte.simbot.common.id.LongID.Companion.ID
import love.forte.simbot.component.onebot.v11.core.OneBot11
import love.forte.simbot.component.onebot.v11.core.api.*
import love.forte.simbot.component.onebot.v11.core.bot.OneBotBot
import love.forte.simbot.component.onebot.v11.core.bot.OneBotBotManager.Factory.logger
import love.forte.simbot.component.onebot.v11.core.event.OneBotUnknownEvent
import love.forte.simbot.component.onebot.v11.core.event.OneBotUnsupportedEvent
import love.forte.simbot.component.onebot.v11.core.event.internal.message.*
import love.forte.simbot.component.onebot.v11.core.event.internal.meta.OneBotHeartbeatEventImpl
import love.forte.simbot.component.onebot.v11.core.event.internal.meta.OneBotLifecycleEventImpl
import love.forte.simbot.component.onebot.v11.core.event.internal.notice.*
import love.forte.simbot.component.onebot.v11.core.event.internal.request.OneBotFriendRequestEventImpl
import love.forte.simbot.component.onebot.v11.core.event.internal.request.OneBotGroupRequestEventImpl
import love.forte.simbot.component.onebot.v11.event.RawEvent
import love.forte.simbot.component.onebot.v11.event.UnknownEvent
import love.forte.simbot.component.onebot.v11.event.message.RawGroupMessageEvent
import love.forte.simbot.component.onebot.v11.event.message.RawPrivateMessageEvent
import love.forte.simbot.component.onebot.v11.event.meta.RawHeartbeatEvent
import love.forte.simbot.component.onebot.v11.event.meta.RawLifecycleEvent
import love.forte.simbot.component.onebot.v11.event.notice.*
import love.forte.simbot.component.onebot.v11.event.request.RawFriendRequestEvent
import love.forte.simbot.component.onebot.v11.event.request.RawGroupRequestEvent
import love.forte.simbot.component.onebot.v11.event.resolveEventSerializer
import love.forte.simbot.component.onebot.v11.event.resolveEventSubTypeFieldName
import love.forte.simbot.event.Event

/**
 * 实验性的OneBot方言API，可能会随时发生变更或被移除。
 */
@RequiresOptIn("实验性的OneBot方言API，可能会随时发生变更或被移除。")
@Retention(AnnotationRetention.BINARY)
public annotation class ExperimentalOneBotDialect

/**
 * 一个 [OneBotBot] 内部使用的方言。
 *
 * 一个 [OneBotDialect] 为一个 [bot] 服务，
 * 通过 [OneBotDialectFactory] 进行构建。
 *
 * [OneBotDialect] 供于 [OneBotBot] 内部使用，
 * 不对外提供挂起函数的Java友好函数，
 * 也不建议在外部调用任何方言函数。
 *
 * ### 重写扩展
 *
 * 可以通过继承 [OneBotDialect]
 * 并重写部分或全部可重写函数来支持自定义方言。
 * 建议使用 **Kotlin** 实现，因为 Java 几乎无法实现 `suspend` 函数。
 *
 * @see OneBotDialectFactory
 *
 * @since 1.5.0
 *
 * @author ForteScarlet
 */
@ExperimentalOneBotDialect
public open class OneBotDialect(protected open val bot: OneBotBot) {
    /**
     * 查询当前bot的登录信息
     */
    public open suspend fun queryLoginInfo(): GetLoginInfoResult {
        return bot.executeData(GetLoginInfoApi.create())
    }

    /**
     * 判断 [id] 是否可以指代当前bot。
     *
     * @param currentLoginInfo 如果至少执行并缓存过一次 [GetLoginInfoResult] 信息则有值，
     * 否则会是 `null`。
     */
    public open fun checkIsMe(id: ID, currentLoginInfo: GetLoginInfoResult?): Boolean {
        if (id == bot.id) return true

        return currentLoginInfo?.let { it.userId == bot.id }
            ?: true
    }

    /**
     * 解析数据包字符串为 [Event]。
     */
    @OptIn(FragileSimbotAPI::class)
    public open fun resolveRawEvent(raw: String): RawEvent {
        val obj = OneBot11.DefaultJson.decodeFromString(
            JsonObject.serializer(),
            raw
        )

        val postType = requireNotNull(obj["post_type"]?.jsonPrimitive?.content) {
            "Missing required event property 'post_type'"
        }

        val subTypeFieldName = resolveEventSubTypeFieldName(postType) ?: "${postType}_type"
        val subType = obj[subTypeFieldName]?.jsonPrimitive?.content

        fun toUnknown(reason: Throwable? = null): UnknownEvent {
            val time = obj["time"]?.jsonPrimitive?.long ?: -1L
            val selfId = obj["self_id"]?.jsonPrimitive?.long?.ID ?: 0L.ID
            return UnknownEvent(time, selfId, postType, raw, obj, reason)
        }

        if (subType == null) {
            // 一个不规则的 unknown event
            return toUnknown()
        }

        resolveEventSerializer(postType, subType)?.let {
            return try {
                OneBot11.DefaultJson.decodeFromJsonElement(it, obj)
            } catch (serEx: SerializationException) {
                logger.error(
                    "Received raw event '{}' decode failed because of serialization: {}" +
                            "It will be pushed as an UnknownEvent",
                    raw,
                    serEx.message,
                    serEx
                )

                toUnknown(serEx)
            } catch (argEx: IllegalArgumentException) {
                logger.error(
                    "Received raw event '{}' decode failed because of illegal argument: {}" +
                            "It will be pushed as an UnknownEvent",
                    raw,
                    argEx.message,
                    argEx
                )

                toUnknown(argEx)
            }
        } ?: run {
            return toUnknown()
        }
    }

    /**
     * 根据 [raw] 和 [event] 解析为一个推送的 [Event].
     */
    @OptIn(FragileSimbotAPI::class)
    public open fun resolveRawEventToEvent(raw: String, event: RawEvent): Event {
        fun unsupported(): OneBotUnsupportedEvent =
            OneBotUnsupportedEvent(raw, event)

        return when (event) {
            //region 消息事件
            // 群消息、匿名消息、系统消息
            is RawGroupMessageEvent -> when (event.subType) {
                RawGroupMessageEvent.SUB_TYPE_NORMAL ->
                    OneBotNormalGroupMessageEventImpl(raw, event, bot)

                RawGroupMessageEvent.SUB_TYPE_ANONYMOUS ->
                    OneBotAnonymousGroupMessageEventImpl(raw, event, bot)

                RawGroupMessageEvent.SUB_TYPE_NOTICE ->
                    OneBotNoticeGroupMessageEventImpl(raw, event, bot)

                else -> OneBotDefaultGroupMessageEventImpl(raw, event, bot)
            }

            // 好友私聊消息、成员临时会话
            is RawPrivateMessageEvent -> when (event.subType) {
                RawPrivateMessageEvent.SUB_TYPE_FRIEND ->
                    OneBotFriendMessageEventImpl(raw, event, bot)

                RawPrivateMessageEvent.SUB_TYPE_GROUP ->
                    OneBotGroupPrivateMessageEventImpl(raw, event, bot)

                else -> OneBotDefaultPrivateMessageEventImpl(raw, event, bot)
            }
            //endregion

            //region 元事件
            is RawLifecycleEvent -> OneBotLifecycleEventImpl(raw, event, bot)
            is RawHeartbeatEvent -> OneBotHeartbeatEventImpl(raw, event, bot)
            //endregion

            //region 申请事件
            is RawFriendRequestEvent -> OneBotFriendRequestEventImpl(raw, event, bot)
            is RawGroupRequestEvent -> OneBotGroupRequestEventImpl(raw, event, bot)
            //endregion

            //region notice events
            is RawFriendAddEvent -> OneBotFriendAddEventImpl(raw, event, bot)
            is RawFriendRecallEvent -> OneBotFriendRecallEventImpl(raw, event, bot)
            is RawGroupAdminEvent -> OneBotGroupAdminEventImpl(raw, event, bot)
            is RawGroupBanEvent -> OneBotGroupBanEventImpl(raw, event, bot)
            is RawGroupIncreaseEvent -> OneBotGroupMemberIncreaseEventImpl(raw, event, bot)
            is RawGroupDecreaseEvent -> OneBotGroupMemberDecreaseEventImpl(raw, event, bot)
            is RawGroupRecallEvent -> OneBotGroupRecallEventImpl(raw, event, bot)
            is RawGroupUploadEvent -> OneBotGroupUploadEventImpl(raw, event, bot)
            is RawNotifyEvent -> when (event.subType) {
                RawNotifyEvent.SUB_TYPE_HONOR -> OneBotHonorEventImpl(raw, event, bot)
                RawNotifyEvent.SUB_TYPE_LUCKY_KING -> OneBotLuckyKingEventImpl(raw, event, bot)
                RawNotifyEvent.SUB_TYPE_POKE -> when {
                    event.groupId == null -> OneBotPrivatePokeEventImpl(raw, event, bot)
                    event.selfId.value == event.targetId?.value ->
                        OneBotBotSelfPokeEventImpl(raw, event, bot)

                    else -> OneBotMemberPokeEventImpl(raw, event, bot)
                }

                // Unsupported
                else -> unsupported()
            }

            //endregion
            is UnknownEvent -> OneBotUnknownEvent(raw, event)
            else -> unsupported()
        }
    }

    //region Contact relation

    /**
     * 获取好友列表
     */
    public open suspend fun getFriends(): Flow<GetFriendListResult> {
        return bot.executeData(GetFriendListApi.create()).asFlow()
    }

    /**
     * 获取指定好友信息
     */
    public open suspend fun getFriend(id: ID): GetFriendListResult? {
        return bot.executeData(GetFriendListApi.create()).firstOrNull { it.userId == id }
    }

    /**
     * 获取好友数量
     */
    public open suspend fun getFriendCount(): Int {
        return bot.executeData(GetFriendListApi.create()).size
    }

    /**
     * 获取指定陌生人信息
     */
    public open suspend fun getStrangerInfo(id: ID): GetStrangerInfoResult {
        return bot.executeData(GetStrangerInfoApi.create(userId = id))
    }
    //endregion

    //region Group relation

    /**
     * 获取所有群的流
     */
    public open suspend fun getGroups(): Flow<GetGroupInfoResult> {
        return bot.executeData(GetGroupListApi.create()).asFlow()
    }

    /**
     * 获取指定id的群信息
     */
    public open suspend fun getGroup(id: ID): GetGroupInfoResult? {
        // TODO 如何检测不存在？
        return bot.executeData(GetGroupInfoApi.create(id))
    }

    /**
     * 获取群数量
     */
    public open suspend fun getGroupCount(): Int {
        return bot.executeData(GetGroupListApi.create()).size
    }

    /**
     * 获取群指定成员
     */
    public open suspend fun getGroupMember(groupId: ID, memberId: ID): GetGroupMemberInfoResult {
        // TODO 如何检测不存在？
        return bot.executeData(GetGroupMemberInfoApi.create(groupId, memberId))
    }
    //endregion


    // TODO
}
