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

class InitializerHelper : AbstractVerticle() {
  private val logger = LoggerFactory.getLogger(this::class.java)
  private val sqlQueries: HashMap<SqlQuery, String> = ConfigHelper.loadSqlQueries()


  override fun start(initPromise: Promise<Void>) {
    val schemaCreation = listOf(sqlQueries[SqlQuery.CREATE])
/*
* employee 1 data:
* employeeId integer, \
    billableRate double, \
    project varchar(255, \
    projectDate varchar(255), \
    startTime varchar(255), \
    endTime varchar(255),\
    workDuration integer,\
    createdOn timestamp default 'now');
*/
    val dataInit = listOf(
      "insert into tb_emp_project values ('23qwe', '1', 300, 'MTN', '2019-08-01', '09:00', '17:00', '8')",
      "insert into tb_emp_project values ('43wee', '1', 300, 'Fidelity', '2019-08-02', '09:00', '14:00', '5')")


    var dbClient: JDBCClient = JDBCClient.createShared(vertx, ConfigHelper.loadConfig())

    dbClient.getConnection { ar ->
      if (ar.succeeded()) {
        val connection: SQLConnection = ar.result()
        connection.execute(schemaCreation[0]) { create ->
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
            if (count.result().results[0].getInteger(0) == 0) {
              logger.info("Need to insert data")
              connection.batch(dataInit, batchInsertHandler(connection, initPromise))
            } else {
              logger.info("No need to insert data")
              connection.close()
              initPromise.complete()
            }

          }
        }
      } else {
        logger.error("Cannot obtain a database connection", ar.cause())
        initPromise.fail(ar.cause())
      }
    }
    //reactiveX
  }

  /*private fun schemaCreationHandler(dataInit: List<String>, connection: SQLConnection, ar: AsyncResult<Void>) {
    if (ar.succeeded()) {
      logger.info("Table creation success")
      connection.query(sqlQueries[SqlQuery.COUNT_BILLABLE], testQueryHandler(dataInit, connection))
    } else {
      connection.close()
      logger.error("Schema creation failed", ar.cause())
    }
  }

  private fun testQueryHandler(dataInit: List<String>, connection: SQLConnection): Handler<AsyncResult<ResultSet>> {
    return Handler { ar ->
      if (ar.succeeded()) {
        if (ar.result().results[0].getInteger(0) == 0) {
          logger.info("Need to insert data")
          connection.batch(dataInit, batchInsertHandler(connection))
        } else {
          logger.info("No need to insert data")
          connection.close()
        }
      } else {
        connection.close()
        logger.error("Could not check the number of records in the database", ar.cause())
      }
    }
  }*/


  private fun batchInsertHandler(connection: SQLConnection, initPromise: Promise<Void>): Handler<AsyncResult<List<Int>>> {
    return Handler { ar ->
      if (ar.succeeded()) {
        logger.info("Successfully inserted data")
        initPromise.complete()
      } else {
        logger.error("Could not insert data", ar.cause())
        initPromise.fail(ar.cause())
      }
      connection.close()
    }
  }
}
