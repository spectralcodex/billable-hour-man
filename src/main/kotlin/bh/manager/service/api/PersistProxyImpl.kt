package bh.manager.service.api

import bh.manager.service.JdbcRepositoryWrapper
import bh.manager.service.*
import bh.manager.service.SqlQuery
import bh.manager.util.ConfigHelper
import io.vertx.core.AsyncResult
import io.vertx.core.Handler
import io.vertx.core.Vertx
import io.vertx.core.json.JsonArray
import io.vertx.core.json.JsonObject
import java.text.DateFormat
import java.text.SimpleDateFormat
import java.util.*


/*
* ProxyService Implementation
* */
internal class PersistProxyImpl : JdbcRepositoryWrapper, PersistProxyService {
  private val sqlQueries = ConfigHelper.loadSqlQueries()

  constructor(vertx: Vertx, config: JsonObject) : super(vertx, config)

  override fun findBillable(empId: String?, handler: Handler<AsyncResult<List<JsonObject>>>?): PersistProxyService {
    retrieveMany(JsonArray().add(empId), sqlQueries[SqlQuery.GET_BY_ID]!!).onComplete(handler)
    return this
  }

  override fun fetchAllBillable(handler: Handler<AsyncResult<List<JsonObject>>>): PersistProxyService {
    retrieveAll(sqlQueries[SqlQuery.ALL]).onComplete(handler)
    return this
  }

  override fun findBillableByCompany(company: String?, handler: Handler<AsyncResult<List<JsonObject>>>?): PersistProxyService {
    retrieveMany(JsonArray().add(company), sqlQueries[SqlQuery.GET_BY_COMPANY]!!).onComplete(handler)
    return this
  }

  override fun initializePersistence(resultHandler: Handler<AsyncResult<Void>>): PersistProxyService {
    executeNoResult(ConfigHelper.uuid, sqlQueries[SqlQuery.COUNT], resultHandler)
    return this
  }

  override fun addBillable(payload: JsonObject, handler: Handler<AsyncResult<Void>>): PersistProxyService {
    var params: JsonArray = JsonArray()
      .add(ConfigHelper.uuid)
      .add(payload.getString("employeeId"))
      .add(payload.getString("billableRate").toDouble())
      .add(payload.getString("projectName"))
      .add(payload.getString("projectDate"))


    val startTime = payload.getString("startTime")
    val endTime = payload.getString("endTime")

    val df: DateFormat = SimpleDateFormat("HH:mm")
    val d1: Date = df.parse(startTime)
    val d2: Date = df.parse(endTime)

    val hoursDifference: Long = (d2.time - d1.time) / 3600000L
    params.add(startTime)
      .add(endTime)
      .add(hoursDifference.toInt())

    executeNoResult(params, sqlQueries[SqlQuery.SAVE], handler)
    return this
  }

}
