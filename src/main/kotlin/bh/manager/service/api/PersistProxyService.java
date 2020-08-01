package bh.manager.service.api;

import io.vertx.codegen.annotations.Fluent;
import io.vertx.codegen.annotations.ProxyGen;
import io.vertx.codegen.annotations.VertxGen;
import io.vertx.core.AsyncResult;
import io.vertx.core.Handler;
import io.vertx.core.json.JsonObject;

import java.util.List;

@VertxGen
@ProxyGen
interface PersistProxyService { //This a modified library and should not be touched
  /**
   * The name of the event bus service.
   */
  String SERVICE_NAME = "rest-eb-service";

  /**
   * The address on which the service is published.
   */
  String SERVICE_ADDRESS = "service.rest";



  @Fluent
  PersistProxyService initializePersistence(Handler<AsyncResult<Void>> resultHandler);


  @Fluent
  PersistProxyService fetchAllBillable(Handler<AsyncResult<List<JsonObject>>> handler);

  @Fluent
  PersistProxyService findBillable(String empId, Handler<AsyncResult<List<JsonObject>>> handler);

  @Fluent
  PersistProxyService addBillable(JsonObject json,  Handler<AsyncResult<Void>> handler);

  @Fluent
  PersistProxyService findBillableByCompany(String company,  Handler<AsyncResult<List<JsonObject>>> handler);

}
