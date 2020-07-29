package bh.manager.service.pojo

data class BillableDetail(
  val employeeId: String,
  val billableRate: Int,
  val projectName: String,
  val projectDate: String,
  val startDate: String,
  val endDate: String
)
