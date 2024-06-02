module simbot.component.onebot11v.common {
    requires kotlin.stdlib;
    requires static simbot.common.annotations;
    requires transitive kotlinx.serialization.core;

    exports love.forte.simbot.component.onebot.v11.common.api;
    exports love.forte.simbot.component.onebot.v11.common.event;
    exports love.forte.simbot.component.onebot.v11.common.utils;
}
