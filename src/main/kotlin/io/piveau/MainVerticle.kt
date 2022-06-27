package io.piveau

import io.vertx.core.json.JsonObject
import io.piveau.importing.ImportingJsonVerticle
import io.piveau.pipe.connector.PipeConnector
import io.vertx.core.DeploymentOptions
import io.vertx.core.Launcher
import io.vertx.kotlin.coroutines.CoroutineVerticle
import io.vertx.kotlin.coroutines.await

class MainVerticle : CoroutineVerticle() {
    
    override suspend fun start() {
        vertx.deployVerticle(ImportingJsonVerticle::class.java, DeploymentOptions().setWorker(true)).await()
        PipeConnector.create(vertx, DeploymentOptions()).await().publishTo(ImportingJsonVerticle.ADDRESS)
        
    }
    

}

fun main(args: Array<String>) {
    Launcher.executeCommand("run", *(args.plus(MainVerticle::class.java.name)))
}
