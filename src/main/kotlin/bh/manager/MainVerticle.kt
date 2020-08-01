package bh.manager

import bh.manager.service.api.FinanceServiceVerticle
import bh.manager.service.api.ApiPersistServiceVerticle
import bh.manager.service.base.InitializerHelper
import io.vertx.core.AbstractVerticle
import io.vertx.core.Promise
import io.vertx.core.logging.LoggerFactory

class MainVerticle : AbstractVerticle() {
  private val logger = LoggerFactory.getLogger(this::class.java)

  override fun start(startPromise: Promise<Void>) {

    logger.info("Starting up main server!!!")

    val initPromise: Promise<String> = Promise.promise()
    vertx.deployVerticle(InitializerHelper(), initPromise)

    initPromise.future().compose {
      logger.info("Database Ok id-->$it")

      val apiPromise: Promise<String> = Promise.promise()
      vertx.deployVerticle(ApiPersistServiceVerticle(), apiPromise)
      return@compose apiPromise.future()
    }.compose {

      logger.info("Api Ok id-->$it")

      val adminPromise = Promise.promise<String>()
      vertx.deployVerticle(FinanceServiceVerticle(),
        adminPromise)
      return@compose adminPromise.future()
    }.onComplete { ar ->
      if (ar.succeeded()) {
        logger.info("Hurray we are up \uD83D\uDE80")
        startPromise.complete()
      } else {
        logger.info("\uD83E\uDD23 OOps server failed to start -->${ar.cause()}")
        startPromise.fail(ar.cause())
      }
    }

  }
}
