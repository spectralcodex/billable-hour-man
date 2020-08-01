/* globals Chart:false, feather:false */
$(document).ready(function () {
  feather.replace()

  $.ajax({
    url: '/api/all',
    type: 'POST',
    success: function (response) {
      var trHTML = '';
      var sumHour = 0;
      var sumRate = 0;
      var comps = '';
      $.each(response, function (i, item) {
        trHTML += '<tr><td>' + item.EMPLOYEEID + '</td><td align="right">' + item.BILLABLERATE +
          '</td><td>&nbsp;&nbsp;&nbsp;&nbsp;' + item.PROJECT + '</td><td>' + item.PROJECTDATE + '</td>' +
          '<td>' + item.STARTTIME + '</td><td>' + item.ENDTIME + '</td><td align="right">' + item.WORKDURATION + '</td>' +
          '<td align="right">' + item.WORKDURATION * item.BILLABLERATE + '</td></tr>';
        comps += '<option value="'+item.PROJECT+'">'+ item.PROJECT + '</option>'
      sumRate += item.BILLABLERATE;
      sumHour += item.WORKDURATION;
      });
      //console.log(sumHour, sumRate)
      $('#tb-records').append(trHTML);
      $('#tb-timesheet').append(trHTML);
      $('#sumRate').text(sumRate);
      $('#sumTotal').text(sumRate * sumHour);
      $('#sumDuration').text(sumHour);
      $('#tb-finance').append(trHTML);
      $('#sel-company').append(comps)
    }
  });

  $("#sel-company").change(function(){
    var company = $(this).children("option:selected").val();
    $('#company-name').html('<p class="text-monospace">Company:<b>'+company +'</b></p>');

    $.ajax({
      url: '/api/company',
      data: JSON.stringify({"company": company}),
      type: 'POST',
      success: function(response){
        $('#tb-invoice tbody').empty();
        var trHTML = '';
        var sumHour = 0;
        var sumRate = 0;
        $.each(response, function (i, item) {
            trHTML += '<tr><td>' + item.EMPLOYEEID + '<td align="right">' + item.WORKDURATION + '</td>'
              +'</td><td align="right">' + item.BILLABLERATE + '</td><td align="right">' + item.WORKDURATION * item.BILLABLERATE + '</td></tr>';
          sumRate += item.BILLABLERATE;
          sumHour += item.WORKDURATION;
        })
        $('#tb-invoice').append(trHTML);
        //$('#fsumRate').text(sumRate);
        $('#fsumTotal').text("Total: "+sumRate * sumHour);
        //$('#fsumDuration').text(sumHour);
      },
      error: function(xhr, status, error){

      }
    })
  });


  $('#form-project').submit(function (e) {
    e.preventDefault(); // avoid to execute the actual submit of the form.
    $('#error_submit').empty()


    if (
      $('#txt-emp').val().length == 0 ||
      $('#txt-bill-rate').val().length == 0 ||
      $('#txt-project-name').val().length == 0 ||
      $('#txt-project-date').val().length == 0 ||
      $('#txt-start-date').val().length == 0 ||
      $('#txt-end-date').val().length == 0
    ) {
      $('#error_submit').append('<div class="alert alert-danger" role="alert" id="error_submit">'+ 'All fields are required' + '</div>');
      return;
    } else {

      var obj = {
        employeeId: $('#txt-emp').val(),
        billableRate: $('#txt-bill-rate').val(),
        projectName: $('#txt-project-name').val(),
        projectDate: $('#txt-project-date').val(),
        startTime: $('#txt-start-date').val(),
        endTime: $('#txt-end-date').val()

      };

      console.log(obj)

      $.ajax({
        type: "POST",
        url: '/api/add',
        data: JSON.stringify(obj), // serializes the form's elements.
        success: function (response) {
          console.log(response)
          window.location.href = '/project?msg=' + response.message
        },
        error: function (xhr, status, error) {

          console.log(error, status)

          $('#error_submit').append('<div class="alert alert-danger" role="alert" id="error_submit">' + error + '</div>')

        }
      });
    }
  });

  $('#csv').on('click',function(){
    $("#tb-records").tableHTMLExport({type:'csv',filename:'sample.csv'});
  })

  $('#time-csv').on('click',function(){
    $("#tb-timesheet").tableHTMLExport({type:'csv',filename:'timesheet.csv'});
  })

  $('#time-json').on('click',function(){
    $("#tb-timesheet").tableHTMLExport({type:'json',filename:'timesheet.json'});
  })

  $('#fcsv').on('click',function(){
    $("#tb-finance").tableHTMLExport({type:'csv',filename:'insight.csv'});
  })

  $('#icsv').on('click',function(){
    $("#tb-invoice").tableHTMLExport({type:'csv',filename:'invoice.csv'});//invoice csv
  })

  $('#ijson').on('click',function(){
    $("#tb-invoice").tableHTMLExport({type:'json',filename:'invoice.json'});//invoice csv
  })

  $('#fpdf').on('click',function(){
    $("#tb-finance").tableHTMLExport({type:'json',filename:'invoice.json'});
  })
});
