package bh.manager.service.api

import bh.manager.service.base.BaseRestVerticle
import bh.manager.service.ui.UiHandler
import io.vertx.core.Handler
import io.vertx.core.Promise
import io.vertx.core.json.JsonObject
import io.vertx.core.logging.LoggerFactory
import io.vertx.ext.web.Router
import io.vertx.ext.web.RoutingContext
import io.vertx.ext.web.handler.BodyHandler
import io.vertx.ext.web.handler.StaticHandler

internal class AdminHttpServerVerticle(service: PersistProxyService): BaseRestVerticle() {
  private val service: PersistProxyService = service
  private val  serviceName:String = "billable-admin-api"

  override fun start(startPromise: Promise<Void>?) {
    super.start()

     val uiHandler = UiHandler(vertx)


    val router: Router = Router.router(vertx)
    router.route().handler(BodyHandler.create())

    enableCorsSupport(router)
    enableLocalSession(router)
    router.route().handler(StaticHandler.create().setWebRoot("static"))
    router.get("/").handler(uiHandler::financeHandler)
    router.post("/api/company").handler(this::apiRetrieveByCompany)
    router.post("/api/all").handler(this::apiRetrieveAll)
    router.get("/invoice").handler(uiHandler::invoiceHandler)





    val host: String = config().getString("service.http.address", "0.0.0.0")
    val port: Int = config().getInteger("service.http.port", 8888)

    //Create Http Server and publish endpoint for discovery
    createHttpServer(router, host, port)
      .compose{publishHttpEndpoint(serviceName, host, port)}
      .onComplete(startPromise)
  }

  private fun apiRetrieveAll(context: RoutingContext){
    service.fetchAllBillable(rawResultHandler(context))
  }

  private fun apiRetrieveByCompany(context: RoutingContext){
    var company = context.bodyAsJson!!.getString("company")
    service.findBillableByCompany(company, resultHandlerNonEmpty(context))
  }



}
