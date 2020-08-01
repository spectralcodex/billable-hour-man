package bh.manager.service.base

import io.vertx.core.AsyncResult
import io.vertx.core.Future
import io.vertx.core.Handler
import io.vertx.core.Promise
import io.vertx.core.http.CookieSameSite
import io.vertx.core.http.HttpMethod
import io.vertx.core.http.HttpServer
import io.vertx.core.http.HttpServerOptions
import io.vertx.core.json.JsonObject
import io.vertx.core.logging.Logger
import io.vertx.core.logging.LoggerFactory
import io.vertx.core.net.JksOptions
import io.vertx.ext.web.Router
import io.vertx.ext.web.RoutingContext
import io.vertx.ext.web.handler.CorsHandler
import io.vertx.ext.web.handler.SessionHandler
import io.vertx.ext.web.sstore.LocalSessionStore


open class BaseRestVerticle : BaseMicroServiceVerticle() {
  private val logger: Logger = LoggerFactory.getLogger(this::class.java)

  /**
   * @param router microservice router
   * @param host   http host
   * @param port   http port
   * @return Unit httpServer
   */
  fun createHttpServer(router: Router, host: String, port: Int): Future<Unit> {
    val httpServerPromise: Promise<HttpServer> = Promise.promise()
    vertx.createHttpServer(HttpServerOptions().setSsl(true)
      .setKeyStoreOptions(JksOptions().setPath("server-keystore.jks").setPassword("secr3t")))
      .requestHandler(router)
      .listen(port, host, httpServerPromise)
    return httpServerPromise.future().map { null }
  }

  /**
   * @param router router instance
   */
  fun enableCorsSupport(router: Router) {
    val allowHeaders = hashSetOf<String>()
    allowHeaders.add("x-requested-with")
    allowHeaders.add("Access-Control-Allow-Origin")
    allowHeaders.add("origin")
    allowHeaders.add("Content-Type")
    allowHeaders.add("accept")

    val allowMethods = hashSetOf<HttpMethod>()
    allowMethods.add(HttpMethod.GET)
    allowMethods.add(HttpMethod.PUT)
    allowMethods.add(HttpMethod.OPTIONS)
    allowMethods.add(HttpMethod.POST)
    allowMethods.add(HttpMethod.DELETE)
    allowMethods.add(HttpMethod.PATCH)

    router.route().handler(CorsHandler.create("*")
      .allowedHeaders(allowHeaders)
      .allowedMethods(allowMethods))
  }

  /**
   * Enable local session support for session storage
   *
   * @param router router instance
   */
  fun enableLocalSession(router: Router) {
    //router.route().handler(CookieHandler.create()); //deprecated
    router.route().handler(SessionHandler.create(
      LocalSessionStore.create(vertx, "erp.user.session"))
      .setCookieSameSite(CookieSameSite.STRICT) //help prevent csrf
      .setCookieSecureFlag(true))
  }

  //Helper methods for REST API {PUT,GET,POST} calls
  fun <T> resultHandler(context: RoutingContext, handler: Handler<T>): Handler<AsyncResult<T>> {
    return Handler { ar ->
      if (ar.succeeded()) {
        handler.handle(ar.result())
      } else {
        logger.error(ar.cause())
        internalError(context, ar.cause())
        ar.cause().printStackTrace()
      }
    }
  }


  fun <T> rawResultHandler(context: RoutingContext): Handler<AsyncResult<T>> {
    return Handler { ar ->
      if (ar.succeeded()) {
        //logger.info("Fetch Result::::${ar.result()}")
        var res: T = ar.result()
        if (res == null) serviceUnavailable(context, "invalid_result")
        else {
          context.response()
            .putHeader("content-type", "application/json")
            .end(res?.toString())
        }
      } else {
        internalError(context, ar.cause())
        ar.cause().printStackTrace()
      }
    }
  }

  /**
   * This method generates handler for async methods in REST APIs.
   * The result requires non-empty. If empty, return <em>404 Not Found</em> status.
   * The content type is JSON.
   *
   * @param context routing context instance
   * @param <T>     result type
   * @return generated handler
   */
  protected fun <T> resultHandlerNonEmpty(context: RoutingContext): Handler<AsyncResult<T>> {
    return Handler { ar ->
      if (ar.succeeded()) {
        val res = ar.result()
        if (res == null) notFound(context) else {
          context.response()
            .putHeader("content-type", "application/json")
            .end(res?.toString())
        }
      } else {
        internalError(context, ar.cause())
        ar.cause().printStackTrace()
      }
    };
  }


  // helper method dealing with failure

  fun internalError(context: RoutingContext, ex: Throwable) {
    context.response().setStatusCode(500)
      .putHeader("content-type", "application/json")
      .end(JsonObject().put("error", ex.message).encodePrettily())
  }

  fun notImplemented(context: RoutingContext) {
    context.response().setStatusCode(501)
      .putHeader("content-type", "application/json")
      .end(JsonObject().put("message", "not_implemented").encodePrettily())
  }

  protected fun badGateway(ex: Throwable, context: RoutingContext) {
    logger.info(ex.printStackTrace())
    context.response()
      .setStatusCode(502)
      .putHeader("content-type", "application/json")
      .end(JsonObject().put("error", "bad_gateway")
        //.put("message", ex.getMessage())
        .encodePrettily())
  }

  protected fun serviceUnavailable(context: RoutingContext) {
    context.fail(503)
  }

  protected fun serviceUnavailable(context: RoutingContext, ex: Throwable) {
    context.response().setStatusCode(503)
      .putHeader("content-type", "application/json")
      .end(JsonObject().put("error", ex.message).encodePrettily())
  }

  protected fun serviceUnavailable(context: RoutingContext, cause: String) {
    context.response().setStatusCode(503)
      .putHeader("content-type", "application/json")
      .end(JsonObject().put("error", cause).encodePrettily())
  }

  protected fun badRequest(context: RoutingContext, ex: Throwable) {
    context.response().setStatusCode(400)
      .putHeader("content-type", "application/json")
      .end(JsonObject().put("error", ex.message).encodePrettily())
  }

  protected fun notFound(context: RoutingContext) {
    context.response().setStatusCode(404)
      .putHeader("content-type", "application/json")
      .end(JsonObject().put("message", "not_found").encodePrettily())
  }
}
