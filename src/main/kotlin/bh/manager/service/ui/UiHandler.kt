package bh.manager.service.ui

import io.vertx.core.AsyncResult
import io.vertx.core.Handler
import io.vertx.core.Vertx
import io.vertx.core.buffer.Buffer
import io.vertx.core.json.JsonObject
import io.vertx.ext.web.RoutingContext
import io.vertx.ext.web.templ.freemarker.FreeMarkerTemplateEngine

internal class UiHandler(vertx:Vertx) {
  private val  templateEngine: FreeMarkerTemplateEngine = FreeMarkerTemplateEngine.create(vertx)

  fun indexHandler(context: RoutingContext) {
     val ctx = JsonObject()
     ctx.put("title", "Wiki home")
     ctx.put("page", "index")
     ctx.put("username","Kotlin")
     templateEngine.render(ctx, "templates/index.ftl", redirect(context))
  }



  fun projectHandler(context: RoutingContext) {
     var msg = context.request().getParam("msg").orEmpty()

    val ctx = JsonObject()
     ctx.put("msg", msg)
     ctx.put("page", "project")
     templateEngine.render(ctx, "templates/page.ftl", redirect(context))
  }

  fun timeSheetHandler(context: RoutingContext) {
    val ctx = JsonObject()
    ctx.put("page", "timesheet")
    templateEngine.render(ctx, "templates/timesheet.ftl", redirect(context))
  }

   private fun  redirect(context: RoutingContext): Handler<AsyncResult<Buffer>> {
    return Handler {ar ->
      when {
          ar.succeeded() -> {
            context.response().putHeader("Content-Type", "text/html")
            context.response().end(ar.result())
          }
          else -> {
            context.fail(ar.cause())
          }
      }
    }
  }

   fun financeHandler(context: RoutingContext) {
    //service.fetchAllBillable(rawResultHandler(context))
    val ctx = JsonObject()
    ctx.put("title", "Wiki home")
    ctx.put("page", "index")
    ctx.put("username","Kotlin")
    templateEngine.render(ctx, "templates/finance.ftl") {
      when {
        it.succeeded() -> {
          context.response().putHeader("Content-Type", "text/html")
          context.response().end(it.result())
        }
        else -> {
          context.fail(it.cause())
        }
      }
    }
   }

  fun invoiceHandler(context: RoutingContext) {
    templateEngine.render(JsonObject(), "templates/invoice.ftl") {
      when {
        it.succeeded() -> {
          context.response().putHeader("Content-Type", "text/html")
          context.response().end(it.result())
        }
        else -> {
          context.fail(it.cause())
        }
      }
    }
   }
}
