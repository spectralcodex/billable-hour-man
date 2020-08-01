package bh.manager.service.base

import io.vertx.core.AbstractVerticle
import io.vertx.core.Future
import io.vertx.core.Promise
import io.vertx.core.json.JsonObject
import io.vertx.core.logging.LoggerFactory
import io.vertx.servicediscovery.Record
import io.vertx.servicediscovery.ServiceDiscovery
import io.vertx.servicediscovery.ServiceDiscoveryOptions
import io.vertx.servicediscovery.types.EventBusService
import io.vertx.servicediscovery.types.HttpEndpoint

open class BaseMicroServiceVerticle :  AbstractVerticle(){
  private val logger = LoggerFactory.getLogger(javaClass)
  var discovery: ServiceDiscovery? = null
  protected var registeredRecords = hashSetOf<Record>()


  override fun start() {
    // init service discovery instance
    discovery = ServiceDiscovery.create(vertx, ServiceDiscoveryOptions().setBackendConfiguration(config()))
  }

  /**
   * Helper methods for adding resources to the discovery service
   *
   * @param name resource name
   * @param host resource host
   * @param port resource port
   * @return async result
   */
   fun publishHttpEndpoint(name:String,  host: String, port:Int):Future<Void> {
    var record:Record = HttpEndpoint.createRecord(name, host, port, "/",
     JsonObject().put("api.name", config().getString("api.name", "")))
    return publish(record)
  }

  fun publishEventBusService(name:String,  address: String,  serviceClass: Class<Any>):Future<Void> {
    var record: Record = EventBusService.createRecord(name, address, serviceClass)
    return publish(record)
  }
     fun <T:Any> publishEventBusService(name:String,  address: String,    serviceClass: T):Future<T> {
     var record: Record = EventBusService.createRecord(name, address, serviceClass::class.java)
    return publish(record).map(serviceClass)
  }

  /**
   * Publish a service with record.
   *
   * @param record service record
   * @return async result
   */
  private fun publish(record:Record): Future<Void> {
    if (discovery == null) try {
      start()
    } catch (e:Exception) {
      throw  IllegalStateException("Cannot create discovery service")
    }

     val promise: Promise<Void> = Promise.promise()
    // publish the service
    discovery?.publish(record) { ar ->
      if (ar.succeeded()) {
        registeredRecords.add(record)
        logger.info("Service <" + ar.result().name + "> published")
        promise.complete()
      } else {
        promise.fail(ar.cause())
      }
    }

    return promise.future()
  }


}
