package bh.manager.service.api

import bh.manager.service.base.BaseRestVerticle
import io.vertx.core.Handler
import io.vertx.core.Promise
import io.vertx.core.json.DecodeException
import io.vertx.core.json.JsonObject
import io.vertx.core.logging.LoggerFactory
import io.vertx.ext.web.Router
import io.vertx.ext.web.RoutingContext
import io.vertx.ext.web.handler.BodyHandler

class RestVerticle (service: ApiPersistService): BaseRestVerticle(){
  private val logger = LoggerFactory.getLogger(this::class.java)
  private val service: ApiPersistService = service
  private val  serviceName:String = "billable-rest-api"



  override fun start(startPromise: Promise<Void>?) {
    super.start()

    val router: Router = Router.router(vertx)
    router.route().handler(BodyHandler.create())

    //router.get("/")
    router.get("/").handler { ctx -> ctx.response().putHeader("Content-Type", "application/json")
      ctx.response().end(JsonObject().put("message", "Billable Hour Rest version 0.1").encode())
    }

    val apiRouter: Router = Router.router(vertx)
    apiRouter.post("/add").handler(this::serviceAdd)
    apiRouter.post("/find")
    apiRouter.post(":/empId")
    router.mountSubRouter("/api", apiRouter)

    val host: String = config().getString("service.http.address", "0.0.0.0")
    val port: Int = config().getInteger("service.http.port", 8082)

    //Create Http Server and publish endpoint for discovery
    createHttpServer(router, host, port)
      .compose{publishHttpEndpoint(serviceName, host, port)}
      .onComplete(startPromise)

  }

  private fun serviceAdd(context: RoutingContext) {
    try {
      logger.info(context.bodyAsJson)
      service.addBillable(context.bodyAsJson, resultHandler(context, Handler{
        val result:String = JsonObject().put("message", "billables_details_added").encodePrettily()
        context.response().setStatusCode(201)
          .putHeader("content-type", "application/json")
          .end(result)
      }))
    } catch (e: DecodeException) {
      badRequest(context, e)
    }
  }
}
