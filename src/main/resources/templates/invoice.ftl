<#include "header.ftl">
<div class="container-fluid">
  <div class="row">
    <nav id="sidebarMenu" class="col-md-3 col-lg-2 d-md-block bg-light sidebar collapse">
      <div class="sidebar-sticky pt-3">
        <ul class="nav flex-column">
          <li class="nav-item">
            <a class="nav-link" href="/">
              <span data-feather="home"></span>
              Dashboard <span class="sr-only">(current)</span>
            </a>
          </li>
          <li class="nav-item">
            <a class="nav-link active" href="/invoice">
              <span data-feather="shopping-cart"></span>
              Invoice
            </a>
          </li>
          <!--<li class="nav-item">
            <a class="nav-link" href="/#">
              <span data-feather="bar-chart-2"></span>
              Report
            </a>
          </li>-->
        </ul>
      </div>
    </nav>

    <main role="main" class="col-md-9 ml-sm-auto col-lg-10 px-md-4">
      <div class="d-flex justify-content-between flex-wrap flex-md-nowrap align-items-center pt-3 pb-2 mb-3 border-bottom">
        <h3 class="h3">Invoice</h3>
        <div class="btn-toolbar mb-2 mb-md-0">
          <div class="btn-group mr-2">
            <button type="button" class="btn btn-sm btn-outline-secondary" id="ijson"><span data-feather="shopping-cart"></span>
              JSON
            </button>
            <button type="button" class="btn btn-sm btn-outline-secondary" id="icsv"> <span data-feather="download"></span>
              CSV
            </button>
          </div>
          <!--<button type="button" class="btn btn-sm btn-outline-secondary">
            <span data-feather="bar-chart-2"></span>
            Report
          </button>-->
        </div>
      </div>

      <!--<canvas class="my-4 w-100" id="myChart" width="900" height="380"></canvas>-->

      <div class="col-md-8 order-md-1">
      <div class="table-responsive">
        <form class="needs-validation" id="form-project" novalidate>
          <div class="form-row">
            <div class="col-md-6 mb-3">
              <select class="custom-select d-block w-100" id="sel-company" required>
                <option value="">Choose a company...</option>

              </select>

            </div>
          </div>
        </form>

        <table class="table table-striped table-sm" id="tb-invoice" >

          <thead>
             <tr>
               <th>
                 <div class="pull-right"><div id="company-name"></div></div>
               </th>
               <th>&nbsp;</th>
               <th>&nbsp;</th>
               <th>&nbsp;</th>
             </tr>
             <tr>
               <th>Employee ID</th>
               <th><div style="display: flex; justify-content: flex-end"><div>Number of Hours</div></div></th>
               <th><div style="display: flex; justify-content: flex-end"><div>Unit Price</div></div></th>
               <th><div style="display: flex; justify-content: flex-end"><div>Cost</div></div></th>
             </tr>
          </thead>

          <tbody id="tbody-tb-invoice">

          </tbody>
          <tfoot>
          <tr>
            <th>&nbsp;</th>
            <th><div id="fsumDuration"></div></th>
            <th><div id="fsumRate"></div></th>
            <th><div style="display: flex; justify-content: flex-end"><div id="fsumTotal"></div></div></th>
          </tr>
          </tfoot>
        </table>
      </div>
      </div>
    </main>

  </div>
</div>
<!--<footer class="footer mt-auto py-3">
  <div class="container">
    <span class="text-muted">#Spectralcodex</span>
  </div>
</footer>-->

<#include "footer.ftl">
