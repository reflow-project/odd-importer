package io.piveau.importing

import java.io.File
import io.piveau.importing.json.*
import io.piveau.pipe.PipeContext
import io.vertx.core.eventbus.Message
import io.vertx.core.json.JsonArray
import io.vertx.core.json.JsonObject
import io.vertx.ext.web.client.WebClient
import io.vertx.ext.web.client.WebClientOptions
import io.vertx.kotlin.coroutines.CoroutineVerticle
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.*
import java.util.Base64

class ImportingJsonVerticle : CoroutineVerticle() {

    private lateinit var JsonClient: JsonClient

    @FlowPreview
    @ExperimentalCoroutinesApi
    override suspend fun start() {
        val options = WebClientOptions().addEnabledSecureTransportProtocol("TLSv1.3")
        JsonClient = JsonClient(WebClient.create(vertx, options))
        vertx.eventBus().consumer(ADDRESS, this::handlePipe)
    }

    
    @FlowPreview
    @ExperimentalCoroutinesApi
    private fun handlePipe(message: Message<PipeContext>) {
        GlobalScope.launch(Dispatchers.IO) {
            with(message.body()) {
                val address = config.getString("address")
                val tableName = config.getString("tableName")
                val replaceTable = config.getBoolean("replaceTable")
                var response = JsonClient.get(address)
                if(response != null){
                    delay(8000)
                    val dataInfo = JsonObject()
                    .put("tableName", tableName)
                    .put("replaceTable", replaceTable)
                    log.info("Importing finished")
                    setResult(response.encodePrettily(), "application/json", dataInfo).forward()
                    log.info("Dataset imported: $dataInfo")
                    log.debug(response.encodePrettily())
                }
    
            }
        }
    }

    companion object {
        const val ADDRESS: String = "io.piveau.pipe.importing.json.queue"
    }

}