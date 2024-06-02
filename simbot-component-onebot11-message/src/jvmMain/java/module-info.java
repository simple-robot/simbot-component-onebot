module simbot.component.onebot11v.message {
    requires kotlin.stdlib;
    requires static simbot.api;
    requires static simbot.common.annotations;
    requires transitive kotlinx.serialization.core;

    exports love.forte.simbot.component.onebot.v11.message;
    exports love.forte.simbot.component.onebot.v11.message.segment;
}
