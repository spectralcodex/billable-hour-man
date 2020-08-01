<#include "header.ftl">
<div class="container-fluid">
  <div class="row">
    <nav id="sidebarMenu" class="col-md-3 col-lg-2 d-md-block bg-light sidebar collapse">
      <div class="sidebar-sticky pt-3">
        <ul class="nav flex-column">
          <li class="nav-item">
            <a class="nav-link active" href="/">
              <span data-feather="home"></span>
              Dashboard <span class="sr-only">(current)</span>
            </a>
          </li>
          <li class="nav-item">
            <a class="nav-link" href="/invoice">
              <span data-feather="shopping-cart"></span>
              Invoice
            </a>
          </li>
          <li class="nav-item">
            <a class="nav-link" href="/#">
              <span data-feather="bar-chart-2"></span>
              Report
            </a>
          </li>
        </ul>
      </div>
    </nav>

    <main role="main" class="col-md-9 ml-sm-auto col-lg-10 px-md-4">
      <div class="d-flex justify-content-between flex-wrap flex-md-nowrap align-items-center pt-3 pb-2 mb-3 border-bottom">
        <h3 class="h3">Billable Insight</h3>
        <div class="btn-toolbar mb-2 mb-md-0">
          <div class="btn-group mr-2">
            <button type="button" class="btn btn-sm btn-outline-secondary" id="fpdf"><span data-feather="file"></span>
              JSON
            </button>
            <button type="button" class="btn btn-sm btn-outline-secondary" id="fcsv"> <span data-feather="download"></span>
              CSV
            </button>
          </div>
          <button type="button" class="btn btn-sm btn-outline-secondary" onclick="location.href='/invoice'">
            <span data-feather="shopping-cart"></span>
            Invoice
          </button>
        </div>
      </div>

      <!--<canvas class="my-4 w-100" id="myChart" width="900" height="380"></canvas>-->


      <div class="table-responsive">
        <table class="table table-striped table-sm" id="tb-finance" >
          <thead>
          <tr>
            <th>Employee ID</th>
            <th><div style="display: flex; justify-content: flex-end"><div>Billable Rate (hour)</div></div></th>
            <th>&nbsp;&nbsp;&nbsp;&nbsp;Project</th>
            <th>Date</th>
            <th>Start Time</th>
            <th>End Time</th>
            <th><div style="display: flex; justify-content: flex-end"><div>Duration (Hours)</div></div></th>
            <th><div style="display: flex; justify-content: flex-end"><div>Amount (GHS)</div></div></th>
          </tr>
          </thead>
          <tbody>

          </tbody>
          <tfoot>
          <tr>
            <th>Total</th>
            <th><div style="display: flex; justify-content: flex-end"><div id="sumRate"></div></div></th>
            <th></th>
            <th></th>
            <th></th>
            <th></th>
            <th><div style="display: flex; justify-content: flex-end"><div id="sumDuration"></div></div></th>
            <th><div style="display: flex; justify-content: flex-end"><div id="sumTotal"></div></div></th>
          </tr>
          </tfoot>
        </table>
      </div>
    </main>

  </div>
</div>
<footer class="footer mt-auto py-3">
  <div class="container">
    <span class="text-muted">#Spectralcodex</span>
  </div>
</footer>

<#include "footer.ftl">
