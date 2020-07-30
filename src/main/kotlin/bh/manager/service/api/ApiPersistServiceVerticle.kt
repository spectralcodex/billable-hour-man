package bh.manager.service.api

import bh.manager.service.base.BaseMicroServiceVerticle
import bh.manager.util.ConfigHelper
import io.vertx.core.DeploymentOptions
import io.vertx.core.Future
import io.vertx.core.Promise
import io.vertx.core.json.JsonObject
import io.vertx.core.logging.LoggerFactory
import io.vertx.serviceproxy.ProxyHelper
import  bh.manager.service.api.PersistProxyService.SERVICE_ADDRESS
import  bh.manager.service.api.PersistProxyService.SERVICE_NAME

class ApiPersistServiceVerticle: BaseMicroServiceVerticle(){



  override fun start(startPromise: Promise<Void>?) {
    super.start()

    config().mergeIn(ConfigHelper.loadConfig()) //load configurations

    // create the service instance
    val persistService:PersistProxyService = ApiPersistServiceImpl(vertx, config())

    // register the service proxy on event bus
    ProxyHelper.registerService(PersistProxyService::class.java, vertx, persistService, SERVICE_ADDRESS)
    initBillableHourDatabase(persistService)
      .compose{publishEventBusService(SERVICE_NAME, SERVICE_ADDRESS, ApiPersistService::class.java)}
      .compose{ deployRestService(persistService)}
      .onComplete(startPromise)

  }

  private fun initBillableHourDatabase(service: PersistProxyService): Future<Void> {
     val initPromise: Promise<Void> = Promise.promise()
    service.initializePersistence(initPromise)
    return initPromise.future().map{null}
  }

  private fun deployRestService(service: PersistProxyService): Future<Void> {
    val promise: Promise<String> = Promise.promise()
    vertx.deployVerticle( RestVerticle(service),
       DeploymentOptions().setConfig(config()),
      promise)
    return promise.future().map{null}
  }
}
