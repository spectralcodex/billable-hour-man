package bh.manager.service.api

import bh.manager.service.base.BaseMicroServiceVerticle
import bh.manager.util.ConfigHelper
import io.vertx.core.DeploymentOptions
import io.vertx.core.Future
import io.vertx.core.Promise
import io.vertx.core.logging.LoggerFactory
import io.vertx.serviceproxy.ProxyHelper

class ApiPersistServiceVerticle: BaseMicroServiceVerticle(){
  val config = ConfigHelper.loadConfig(config())
  private val logger = LoggerFactory.getLogger(this::class.java)



  override fun start(startPromise: Promise<Void>?) {
    super.start()

    // create the service instance
    val persistService:ApiPersistService = ApiPersistServiceImpl(vertx, config)

    // register the service proxy on event bus
    ProxyHelper.registerService(ApiPersistService::class.java, vertx, persistService, persistService.SERVICE_ADDRESS)
    initBillableHourDatabase(persistService)
      .compose{publishEventBusService(persistService.SERVICE_NAME, persistService.SERVICE_ADDRESS, ApiPersistService::class.java)}
      .compose{ deployRestService(persistService)}
      .onComplete(startPromise)

  }

  private fun initBillableHourDatabase(service: ApiPersistService): Future<Void> {
     val initPromise: Promise<Void> = Promise.promise()
    service.initializePersistence(initPromise)
    return initPromise.future().map{null}
  }

  private fun deployRestService(service: ApiPersistService): Future<Void> {
    val promise: Promise<String> = Promise.promise()
    vertx.deployVerticle( RestVerticle(service),
       DeploymentOptions().setConfig(config),
      promise)
    return promise.future().map{null}
  }
}
