package bh.manager.service.api

import bh.manager.service.pojo.BillableDetail
import io.vertx.codegen.annotations.Fluent
import io.vertx.codegen.annotations.ProxyGen
import io.vertx.codegen.annotations.VertxGen
import io.vertx.core.AsyncResult
import io.vertx.core.Handler
import io.vertx.core.json.JsonObject

@VertxGen
@ProxyGen
interface ApiPersistService {

  /**
   * The name of the event bus service.
   */
  val SERVICE_NAME: String get() = "administration-eb-service"

  /**
   * The address on which the service is published.
   */
  val  SERVICE_ADDRESS: String get() = "service.administration"

  @Fluent
  fun initializePersistence(resultHandler:Handler<AsyncResult<Void>>): ApiPersistService

  @Fluent
  fun fetchAllBillable(handler: Handler<AsyncResult<List<JsonObject>>>): ApiPersistService

  @Fluent
  fun findBillable(empId: String, handler: Handler<AsyncResult<JsonObject>>): ApiPersistService

  @Fluent
  fun addBillable(detail: BillableDetail, handler: Handler<AsyncResult<JsonObject>>): ApiPersistService
}
