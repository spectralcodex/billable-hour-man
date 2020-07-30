package bh.manager.util

import bh.manager.service.SqlQuery
import io.vertx.core.json.JsonObject
import java.util.*

class ConfigHelper {

  companion object {
    fun loadSqlQueries(): HashMap<SqlQuery, String> {
      val queriesInputStream = this::class.java.getResourceAsStream("/db-queries.properties")
      val queriesProps = Properties()
      queriesProps.load(queriesInputStream)
      queriesInputStream.close()
      return hashMapOf(
        SqlQuery.CREATE_BILLABLE_TABLE to queriesProps.getProperty("create-billable-table"),
        SqlQuery.ALL_PAGES to queriesProps.getProperty("all-pages"),
        SqlQuery.CREATE_PAGE to queriesProps.getProperty("create-page")
      )
    }

    fun loadConfig():JsonObject{
      return JsonObject()
        .put("url", "jdbc:hsqldb:file:db/wiki")
        .put("driver_class", "org.hsqldb.jdbcDriver")
        .put("max_pool_size", 30)
    }
  }
}
