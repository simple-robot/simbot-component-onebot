import io.ktor.client.*
import io.ktor.client.engine.java.*
import io.ktor.http.*
import love.forte.simbot.component.onebot.v11.core.api.GetLoginInfoApi
import love.forte.simbot.component.onebot.v11.core.api.GetStatusApi
import love.forte.simbot.component.onebot.v11.core.api.GetVersionInfoApi
import love.forte.simbot.component.onebot.v11.core.api.requestData

suspend fun main() {
    val host = Url("http://localhost:3000")
    val client = HttpClient(Java)

    println(
        GetLoginInfoApi
            .create()
            .requestData(
                client,
                host,
            )
    )

    println(
        GetStatusApi.create()
            .requestData(
                client,
                host,
            )
    )

    println(
        GetVersionInfoApi.create()
            .requestData(
                client,
                host
            )
    )

}
