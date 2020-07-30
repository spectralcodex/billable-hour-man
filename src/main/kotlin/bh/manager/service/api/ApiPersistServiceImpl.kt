package bh.manager.service.api

import bh.manager.service.JdbcRepositoryWrapper
import bh.manager.service.SqlQuery
import bh.manager.util.ConfigHelper
import io.vertx.core.AsyncResult
import io.vertx.core.Handler
import io.vertx.core.Vertx
import io.vertx.core.json.JsonObject
import io.vertx.core.logging.LoggerFactory
import java.util.*

class ApiPersistServiceImpl: JdbcRepositoryWrapper, ApiPersistService {
  private val sqlQueries = ConfigHelper.loadSqlQueries()
  //private val logger = LoggerFactory.getLogger(javaClass)


  constructor(vertx: Vertx, config: JsonObject): super(vertx, config)

  override fun initializePersistence(resultHandler: Handler<AsyncResult<Void>>): ApiPersistService {
    var uuid = UUID.randomUUID().toString()
    sqlQueries[SqlQuery.CREATE_BILLABLE_TABLE]?.let { retrieveNone(uuid, it) }
    return this
  }

  override fun fetchAllBillable(handler: Handler<AsyncResult<List<JsonObject>>>): ApiPersistService {
    TODO("Not yet implemented")
  }

  override fun findBillable(empId: String, handler: Handler<AsyncResult<JsonObject>>): ApiPersistService {
    TODO("Not yet implemented")
  }

  override fun addBillable(detailJson: JsonObject, handler: Handler<AsyncResult<JsonObject>>): ApiPersistService {
    TODO("Not yet implemented")
  }

}
