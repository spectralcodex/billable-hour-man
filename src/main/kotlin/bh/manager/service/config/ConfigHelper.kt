package bh.manager.service.config

import bh.manager.service.SqlQuery
import java.util.*

class ConfigHelper {

  companion object {
    fun loadSqlQueries(): HashMap<SqlQuery, String> {
      val queriesInputStream = javaClass.getResourceAsStream("/db-queries.properties")
      val queriesProps = Properties()
      queriesProps.load(queriesInputStream)
      queriesInputStream.close()
      return hashMapOf(
        SqlQuery.CREATE_BILLABLE_TABLE to queriesProps.getProperty("create-billable-table"),
        SqlQuery.ALL_PAGES to queriesProps.getProperty("all-pages"),
        SqlQuery.CREATE_PAGE to queriesProps.getProperty("create-page")
      )
    }
  }
}
