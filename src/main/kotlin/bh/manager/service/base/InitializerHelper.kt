package bh.manager.service.base

import bh.manager.service.SqlQuery
import bh.manager.util.ConfigHelper
import io.vertx.core.AbstractVerticle
import io.vertx.core.AsyncResult
import io.vertx.core.Handler
import io.vertx.core.Promise
import io.vertx.core.json.JsonObject
import io.vertx.core.logging.LoggerFactory
import io.vertx.ext.jdbc.JDBCClient
import io.vertx.ext.sql.ResultSet
import io.vertx.ext.sql.SQLConnection
import java.util.HashMap

/*
* Schema Initialization  verticle
* */
class InitializerHelper : AbstractVerticle() {
  private val logger = LoggerFactory.getLogger(this::class.java)
  private val sqlQueries: HashMap<SqlQuery, String> = ConfigHelper.loadSqlQueries()



  override fun start(initPromise: Promise<Void>) {
    val schemaCreation = listOf(sqlQueries[SqlQuery.CREATE])


    //Obtain a connection
    var dbClient: JDBCClient = JDBCClient.createShared(vertx, ConfigHelper.loadConfig())

    dbClient.getConnection { ar ->
      if (ar.succeeded()) {
        val connection: SQLConnection = ar.result()
        connection.execute(schemaCreation[0]) { create -> //initialize schema
          if (create.failed()) {
            connection.close()
            logger.error("Schema creation failed", create.cause())
            initPromise.fail(create.cause())
          }

          connection.query(sqlQueries[SqlQuery.COUNT]) { count ->
            if (count.failed()) {
              connection.close()
              logger.error("Schema creation failed", create.cause())
              initPromise.fail(create.cause())
            }

            connection.close()
            logger.info("Schema created successfully")
            initPromise.complete()


          }
        }
      } else {
        logger.error("Cannot obtain a database connection", ar.cause())
        initPromise.fail(ar.cause())
      }
    }
    //reactiveX
  }

}
