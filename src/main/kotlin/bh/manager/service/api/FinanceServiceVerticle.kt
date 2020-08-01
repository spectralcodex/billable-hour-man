package bh.manager.service.api

import bh.manager.util.ConfigHelper
import io.vertx.core.AbstractVerticle
import io.vertx.core.DeploymentOptions
import io.vertx.core.Future
import io.vertx.core.Promise
import io.vertx.serviceproxy.ProxyHelper

class FinanceServiceVerticle : AbstractVerticle(){

  override fun start(startPromise: Promise<Void>) {
    super.start()

    config().mergeIn(ConfigHelper.loadConfig()) //load configurations

    // create the service instance
    val persistService: PersistProxyService = PersistProxyImpl(vertx, config())

    // register the service proxy on event bus
    //ProxyHelper.registerService(PersistProxyService::class.java, vertx, persistService, PersistProxyService.SERVICE_ADDRESS)

    //No need to register service since they all use the same proxy
    initBillableHourDatabase(persistService)
      .compose{deployRestService(persistService)}
      .onComplete(startPromise)


  }

  private fun initBillableHourDatabase(service: PersistProxyService): Future<Void> {//Moved to InitialzerVerticle
    val initPromise: Promise<Void> = Promise.promise()
    service.initializePersistence(initPromise)
    return initPromise.future().map{null}
  }

  private fun deployRestService(service: PersistProxyService): Future<Void> {
    val promise: Promise<String> = Promise.promise()
    vertx.deployVerticle(AdminHttpServerVerticle(service),
      DeploymentOptions().setConfig(config()),
      promise)
    return promise.future().map{null}
  }
}
