package io.piveau

import io.piveau.MainVerticle
import io.piveau.pipe.model.loadPipe
import io.piveau.pipe.model.prettyPrint
import io.vertx.core.Vertx
import io.vertx.core.json.JsonObject
import io.vertx.ext.web.client.WebClient
import io.vertx.junit5.Timeout
import io.vertx.junit5.VertxExtension
import io.vertx.junit5.VertxTestContext
import org.junit.jupiter.api.BeforeAll
import org.junit.jupiter.api.Disabled
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInstance
import org.junit.jupiter.api.extension.ExtendWith
import org.slf4j.LoggerFactory
import java.util.concurrent.TimeUnit


@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@ExtendWith(VertxExtension::class)
class ImportingJsonTest {

    private val log = LoggerFactory.getLogger(this.javaClass)

    @BeforeAll
    fun setup(vertx: Vertx, testContext: VertxTestContext) {
        vertx.deployVerticle(MainVerticle::class.java.name) {
            if (it.succeeded()) {
                testContext.completeNow()
            } else {
                testContext.failNow(it.cause())
            }
        }
    }

    @Test
    @Disabled
    @Timeout(value = 8, timeUnit = TimeUnit.MINUTES)
    fun `Send a pipe`(vertx: Vertx, testContext: VertxTestContext) {
        val checkpoint = testContext.checkpoint(2)
        loadPipe("pipe.yaml")?.let { pipe ->
            WebClient.create(vertx).postAbs("http://localhost:8080/pipe")
                .putHeader("Content-type", "application/json")
                .sendJsonObject(JsonObject(pipe.prettyPrint())) {
                    if (it.succeeded()) {
                        checkpoint.flag()
                    } else {
                        testContext.failNow(it.cause())
                    }
                }
        }
    }

}