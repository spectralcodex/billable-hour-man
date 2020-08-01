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

internal class RestVerticle (service: PersistProxyService): BaseRestVerticle(){
  private val logger = LoggerFactory.getLogger(this::class.java)
  private val service: PersistProxyService = service
  private val  serviceName:String = "billable-rest-api"



  override fun start(startPromise: Promise<Void>?) {
    super.start()

    val uiHandler = UiHandler(vertx)
    val router: Router = Router.router(vertx)
    router.route().handler(BodyHandler.create())

    enableCorsSupport(router)
    enableLocalSession(router)
    router.route().handler(StaticHandler.create().setWebRoot("static"))
    router.get("/").handler(uiHandler::indexHandler)
    router.get("/project").handler(uiHandler::projectHandler)
    router.get("/timesheet").handler(uiHandler::timeSheetHandler)
    router.get("/version").handler { ctx -> ctx.response().putHeader("Content-Type", "application/json")
      ctx.response().end(JsonObject().put("message", "Billable Hour Rest version 0.1").encode())
    }

    val apiRouter: Router = Router.router(vertx)
    apiRouter.post("/add").handler(this::apiAdd)
    apiRouter.post("/all").handler(this::apiRetrieveAll)
    apiRouter.post("/:empId").handler(this::apiRetrieveById)
    router.mountSubRouter("/api", apiRouter)

    val host: String = config().getString("service.http.address", "0.0.0.0")
    val port: Int = config().getInteger("service.http.port", 9999)

    //Create Http Server and publish endpoint for discovery
    createHttpServer(router, host, port)
      .compose{publishHttpEndpoint(serviceName, host, port)}
      .onComplete(startPromise)
  }

  private fun apiAdd(context: RoutingContext) {
    try {
      logger.info(context.bodyAsJson)
      val payload = context.bodyAsJson

      service.addBillable(payload, resultHandler(context, Handler{
        val result:String = JsonObject().put("message", "success").encodePrettily()
        context.response().setStatusCode(201)
          .putHeader("content-type", "application/json")
          .end(result)
      }))
    } catch (e: Exception) {
      badRequest(context, e)
    }
  }

  private fun apiRetrieveAll(context: RoutingContext){
    service.fetchAllBillable(rawResultHandler(context))
  }

  private fun apiRetrieveById(context: RoutingContext){
    var employeeId = context.request().getParam("empId")
    //logger.info("!!!!!!!!!!!!!!!!!!!$employeeId")
    service.findBillable(employeeId, resultHandlerNonEmpty(context))
  }


}
