package bh.manager.service.admin

import io.vertx.core.AbstractVerticle
import io.vertx.core.Promise

class FinanceServiceVerticle : AbstractVerticle(){

  override fun start(startPromise: Promise<Void>) {
    vertx
      .createHttpServer()
      .requestHandler { req ->
        req.response()
          .putHeader("content-type", "text/plain")
          .end("Hello from Vert.x!")
      }
      .listen(8888, "0.0.0.0") { http ->
        if (http.succeeded()) {
          startPromise.complete()
          println("Admin server started on port 8888")
        } else {
          startPromise.fail(http.cause())
        }
      }
    /*vertx.createHttpServer().requestHandler { req -> req.response()
      .putHeader("content-type", "text/plain")
      .end("Hello from Vert.x!")}.listen(8888,"0.0.0.0")*/
  }
}
