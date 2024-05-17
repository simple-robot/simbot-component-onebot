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

package love.forte.simbot.component.onebot.v11.core

import kotlinx.serialization.json.Json
import kotlinx.serialization.modules.SerializersModule
import kotlinx.serialization.modules.polymorphic
import love.forte.simbot.annotations.InternalSimbotAPI
import love.forte.simbot.component.onebot.v11.message.OneBotMessageElement
import love.forte.simbot.component.onebot.v11.message.includeAllComponentMessageElementImpls
import love.forte.simbot.component.onebot.v11.message.includeAllOneBotSegmentImpls
import love.forte.simbot.component.onebot.v11.message.segment.OneBotMessageSegment
import love.forte.simbot.message.messageElementPolymorphic
import kotlin.jvm.JvmField

/**
 * Some OneBot11 constants.
 *
 * @author ForteScarlet
 */
public object OneBot11 {
    /**
     * 一个默认的 [Json] 序列化器。
     * 会在部分内部API中使用。
     */
    @JvmField
    @OptIn(InternalSimbotAPI::class)
    public val DefaultJson: Json = Json {
        isLenient = true
        ignoreUnknownKeys = true
        allowSpecialFloatingPointValues = true
        prettyPrint = false
        serializersModule = SerializersModule {
            messageElementPolymorphic {
                includeAllComponentMessageElementImpls()
            }
            polymorphic(OneBotMessageElement::class) {
                includeAllComponentMessageElementImpls()
            }
            polymorphic(OneBotMessageSegment::class) {
                includeAllOneBotSegmentImpls()
            }
        }
    }

}
