package bh.manager.service

import io.vertx.core.*
import io.vertx.core.json.JsonArray
import io.vertx.core.json.JsonObject
import io.vertx.core.logging.LoggerFactory
import io.vertx.ext.jdbc.JDBCClient
import io.vertx.ext.sql.SQLConnection

/**
 * Helper and wrapper class for Jooq repository services
 *
 * @author spectralcodex
 */

open class JdbcRepositoryWrapper(vertx: Vertx, config: JsonObject) {
  val client: JDBCClient = JDBCClient.createShared(vertx, config).also { print(config) }
  private val logger = LoggerFactory.getLogger(this::class.java)

  /**
   * A helper methods that generates jooq.async handler for SQLConnection
   *
   * @return generated handler
   */
  fun <T> connHandler(h1: Handler<AsyncResult<T>>,
                      h2: Handler<SQLConnection>): Handler<AsyncResult<SQLConnection>> {
    return Handler { conn ->
      if (conn.succeeded()) {
        val connection: SQLConnection = conn.result()
        h2.handle(connection)
      } else {
        h1.handle(Future.failedFuture(conn.cause()))
      }
    }
  }

  private fun getConnection(): Future<SQLConnection> {
    val promise: Promise<SQLConnection> = Promise.promise()
    client.getConnection(promise)
    return promise.future()
  }

  /**
   * @param params        JsonArray containing query parameters
   * @param sql           query
   * @param resultHandler results
   */
  protected fun executeNoResult(params: JsonArray, sql: String, resultHandler: Handler<AsyncResult<Void>>) {
    client.getConnection(connHandler(resultHandler, Handler { con ->
      con.queryWithParams(sql, params) { r ->  //if returns then fix return json array
        if (r.succeeded()) {
          resultHandler.handle(Future.succeededFuture())
        } else {
          resultHandler.handle(Future.failedFuture(r.cause()))
        }
        con.close()
      }
    }))
  }

  protected fun <T> executeNoResult(t: T, sql: String, resultHandler: Handler<AsyncResult<Void>>) {
    client.getConnection(connHandler(resultHandler, Handler { con ->
      con.execute(sql) { r ->  //if returns then fix return json array
        if (r.succeeded()) {
          resultHandler.handle(Future.succeededFuture())
          logger.info("Persist OK --> id:$t")
        } else {
          resultHandler.handle(Future.failedFuture(r.cause()))
        }
        con.close()
      }
    }))
  }

  protected fun removeAll(sql: String, resultHandler: Handler<AsyncResult<Unit>>) {
    client.getConnection(connHandler(resultHandler, Handler { con ->
      con.query(sql) { r ->
        if (r.succeeded()) {
          resultHandler.handle(Future.succeededFuture())
        } else {
          resultHandler.handle(Future.failedFuture(r.cause()))
        }
        con.close()
      }
    }))
  }

  /**
   * @param param Json array param
   * @param sql   callable statement of pgsql function
   * @return a list of json objects
   */
  protected fun retrieveMany(param: JsonArray, sql: String): Future<List<JsonObject>> {
    return getConnection().compose { con ->
      val promise: Promise<List<JsonObject>> = Promise.promise()
      //new JsonArray().add(t), new JsonArray().add("VARCHAR")
      con.queryWithParams(sql, param) { r ->
        if (r.succeeded()) {
          promise.complete(r.result().getRows())
        } else {
          promise.fail(r.cause())
        }
        con.close()
      }
      return@compose promise.future()
    }
  }

  /**
   * @param sql
   * @return future list of JsonObjects
   */
  protected fun retrieveAll(sql: String): Future<List<JsonObject>> {
    return getConnection().compose { con ->
      val promise: Promise<List<JsonObject>> = Promise.promise()
      con.query(sql) { r ->
        if (r.succeeded()) {
          promise.complete(r.result().getRows())
        } else {
          promise.fail(r.cause())
        }
        con.close()
      }
      return@compose promise.future()
    }
  }

  protected fun <T> retrieveNone(t: T, sql: String): Future<Void> {
    return getConnection().compose { con ->
      logger.info("Creating db:::::Init!!!!!!$sql")
      val promise: Promise<Void> = Promise.promise()
      con.execute(sql) { ar ->
        if (ar.succeeded()) {
          logger.info("Persist OK --> id:$t")
          promise.complete()
        } else {
          promise.fail(ar.cause())
        }
        con.close()
      }
      return@compose promise.future()
    }
  }

}
