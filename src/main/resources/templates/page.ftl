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
            <a class="nav-link" href="/timesheet">
              <span data-feather="bar-chart-2"></span>
              Timesheet
            </a>
          </li>
          <li class="nav-item">
            <a class="nav-link active" href="/project">
              <span data-feather="file"></span>
              Projects
            </a>
          </li>
        </ul>
      </div>
    </nav>

    <main role="main" class="col-md-9 ml-sm-auto col-lg-10 px-md-4">
      <div class="d-flex justify-content-between flex-wrap flex-md-nowrap align-items-center pt-3 pb-2 mb-3 border-bottom">
        <h1 class="h2">Project Creation</h1>
        <div class="btn-toolbar mb-2 mb-md-0">
          <div class="btn-group mr-2">
            <button type="button" class="btn btn-sm btn-outline-secondary" onclick="location.href='/project'"><span
                      data-feather="file"></span>
              Add
            </button>
            <!--<button type="button" class="btn btn-sm btn-outline-secondary"><span data-feather="download"></span>
              Export
            </button>-->
          </div>
          <button type="button" class="btn btn-sm btn-outline-secondary" onclick="location.href='/timesheet'">
            <span data-feather="bar-chart-2"></span>
            Timesheet
          </button>
        </div>
      </div>

      <!--<canvas class="my-4 w-100" id="myChart" width="900" height="380"></canvas>-->

      <h6>Enter Billable details</h6>

      <div class="table-responsive">
        <div class="col-md-8 order-md-1">
          <#if msg?has_content>
                <#if msg == "success">
                  <div class="alert alert-success" role="alert">
                    Project added
                  </div>
                </#if>
          </#if>
          <div id="error_submit">

          </div>
          <form class="needs-validation" id="form-project" novalidate>
            <div class="form-row">
              <div class="col-md-6 mb-3">
                <label for="txt-emp">Employee ID</label>
                <input type="text" class="form-control" id="txt-emp" required>
                <div class="valid-feedback">
                  Looks good!
                </div>
              </div>
              <div class="col-md-6 mb-3">
                <label for="txt-bill-rate">Billable Rate (Per Hour)</label>
                <input type="number" class="form-control" id="txt-bill-rate" required>
                <div class="valid-feedback">
                  Looks good!
                </div>
              </div>
            </div>
            <div class="form-row">
              <div class="col-md-6 mb-3">
                <label for="txt-project-name">Project Name</label>
                <input type="text" class="form-control" id="txt-project-name" required>
                <div class="invalid-feedback">
                  Please provide a valid Project Name.
                </div>
              </div>
              <div class="col-md-3 mb-3">
                <label for="validationCustom04">Start Date</label>
                <input type="time" class="form-control" id="txt-start-date" required>
                <div class="invalid-feedback">
                  Please select a valid time.
                </div>
              </div>
              <div class="col-md-3 mb-3">
                <label for="validationCustom05">End Date</label>
                <input type="time" class="form-control" id="txt-end-date" required>
                <div class="invalid-feedback">
                  Please provide a valid end time.
                </div>
              </div>
            </div>
            <div class="form-row">
              <div class="col-md-6 mb-3">
                <label for="validationCustom01">Project Date</label>
                <input type="date" class="form-control" id="txt-project-date" required>
                <div class="valid-feedback">
                  Looks good!
                </div>
              </div>
              <div class="col-md-6 mb-3">

              </div>
            </div>

            <button class="btn btn-primary" type="submit">Submit</button>
          </form>

          <script>
            // Example starter JavaScript for disabling form submissions if there are invalid fields
            (function () {
              'use strict';
              window.addEventListener('load', function () {
                // Fetch all the forms we want to apply custom Bootstrap validation styles to
                var forms = document.getElementsByClassName('needs-validation');
                // Loop over them and prevent submission
                var validation = Array.prototype.filter.call(forms, function (form) {
                  form.addEventListener('submit', function (event) {
                    if (form.checkValidity() === false) {
                      event.preventDefault();
                      event.stopPropagation();
                    }
                    form.classList.add('was-validated');
                  }, false);
                });
              }, false);
            })();
          </script>
        </div>
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

