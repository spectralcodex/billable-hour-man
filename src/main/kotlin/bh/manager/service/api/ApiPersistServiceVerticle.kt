package bh.manager.service.api


import bh.manager.service.base.BaseMicroServiceVerticle
import bh.manager.service.base.InitializerHelper
import bh.manager.util.ConfigHelper
import io.vertx.core.DeploymentOptions
import io.vertx.core.Future
import io.vertx.core.Promise
import io.vertx.serviceproxy.ProxyHelper
import  bh.manager.service.api.PersistProxyService.SERVICE_ADDRESS
import bh.manager.service.api.PersistProxyService.SERVICE_NAME

class ApiPersistServiceVerticle: BaseMicroServiceVerticle(){

  override fun start(startPromise: Promise<Void>?) {
    super.start()

    config().mergeIn(ConfigHelper.loadConfig()) //load configurations

    // create the service instance
    val persistService: PersistProxyService = PersistProxyImpl(vertx, config())

    // register the service proxy on event bus
    ProxyHelper.registerService(PersistProxyService::class.java, vertx, persistService, SERVICE_ADDRESS)
    //initBillableHourDatabase(persistService)
    publishEventBusService(SERVICE_NAME, SERVICE_ADDRESS, ApiPersistService::class.java)
      .compose{deployRestService(persistService)}
      .onComplete(startPromise)

  }

  /*private fun deployInitializePeristence(): Future<Void>{
    val promise: Promise<String> = Promise.promise()
    vertx.deployVerticle(InitializerHelper(),
      DeploymentOptions().setConfig(config()),
      promise)
    return promise.future().map{null}
  }*/

  private fun deployRestService(service: PersistProxyService): Future<Void> {
    val promise: Promise<String> = Promise.promise()
    vertx.deployVerticle(RestVerticle(service),
       DeploymentOptions().setConfig(config()),
      promise)
    return promise.future().map{null}
  }
}
