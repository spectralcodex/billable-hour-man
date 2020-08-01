package bh.manager.util

import bh.manager.service.SqlQuery
import io.vertx.core.json.JsonObject
import java.util.*

class ConfigHelper {

  companion object {
    var uuid =  UUID.randomUUID().toString()

    fun loadSqlQueries(): HashMap<SqlQuery, String> {
      val queriesInputStream = this::class.java.getResourceAsStream("/db-queries.properties")
      val queriesProps = Properties()
      queriesProps.load(queriesInputStream)
      queriesInputStream.close()
      return hashMapOf(
        SqlQuery.CREATE to queriesProps.getProperty("create-billable-table"),
        SqlQuery.ALL to queriesProps.getProperty("all-billable"),
        SqlQuery.GET_BY_COMPANY to queriesProps.getProperty("get-billable-byCompany"),
        SqlQuery.COUNT to queriesProps.getProperty("count-billable"),
        SqlQuery.GET_BY_ID to queriesProps.getProperty("get-billable"),
        SqlQuery.SAVE to queriesProps.getProperty("save-billable")
      )
    }

    fun loadConfig():JsonObject{
      return JsonObject()
        .put("url", "jdbc:hsqldb:file:db/wiki?shutdown=true")
        .put("driver_class", "org.hsqldb.jdbcDriver")
        .put("max_pool_size", 30)
    }
  }
}
