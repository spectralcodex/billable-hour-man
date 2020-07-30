package bh.manager

import bh.manager.service.admin.FinanceServiceVerticle
import bh.manager.service.api.ApiPersistServiceVerticle
import io.vertx.core.AbstractVerticle
import io.vertx.core.DeploymentOptions
import io.vertx.core.Promise
import io.vertx.core.logging.LoggerFactory

class MainVerticle : AbstractVerticle() {
  val logger = LoggerFactory.getLogger(this::class.java)

  override fun start(startPromise: Promise<Void>) {
    /* vertx
       .createHttpServer()
       .requestHandler { req ->
         req.response()
           .putHeader("content-type", "text/plain")
           .end("Hello from Vert.x!")
       }
       .listen(8888) { http ->
         if (http.succeeded()) {
           startPromise.complete()
           println("HTTP server started on port 8888")
         } else {
           startPromise.fail(http.cause());
         }
       }*/
    logger.info("Starting up main server!!!")

    val apiPromise: Promise<String> = Promise.promise()
    vertx.deployVerticle(ApiPersistServiceVerticle(), apiPromise)

    apiPromise.future().compose {
      logger.info("ApiOk result -->$it")

      val adminPromise = Promise.promise<String>()
      vertx.deployVerticle(FinanceServiceVerticle(), DeploymentOptions().setInstances(2),
        adminPromise)
      return@compose adminPromise.future()
    }.onComplete { ar ->
      if (ar.succeeded()) {
        logger.info("Hurray server up \uD83E\uDD23")
        startPromise.complete()
      } else {
        logger.info("\uD83E\uDD23 OOps server failed to start -->${ar.cause()}")
        startPromise.fail(ar.cause())
      }
    }

  }
}
