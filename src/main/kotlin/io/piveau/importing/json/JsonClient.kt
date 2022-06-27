package io.piveau.importing.json

import io.piveau.json.putIfNotEmpty
import io.vertx.core.json.JsonArray
import io.vertx.core.json.JsonObject
import io.vertx.core.json.pointer.JsonPointer
import io.vertx.ext.web.client.WebClient
import io.vertx.ext.web.client.predicate.ResponsePredicate
import io.vertx.ext.web.codec.BodyCodec
import io.vertx.kotlin.ext.web.client.sendAwait
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.flow

typealias JsonResponse = JsonObject


class JsonClient(private val client: WebClient){

    suspend fun get(address:String): JsonResponse =
    client.getAbs(address)
        .`as`(BodyCodec.jsonObject() as BodyCodec<JsonResponse>)
        .expect(ResponsePredicate.SC_SUCCESS)
        .sendAwait()
        .body() 

}