//Appends all customers to customer div
var customerData;
var pageNo;
function loadCustomers() {
    pageNo=0;
        $.ajax({
        type: "GET",
        dataType: 'json',
        url: "/customers",
        success: function (customers) {
            customerData=customers;
            if(customers.length){
                document.getElementById("curr-page").style.display = "inline";
                populateCustomer(customers);
            }else {
                document.getElementById("next-page").style.display = "none";
                document.getElementById("prev-page").style.display = "none";
                document.getElementById("curr-page").style.display = "none";
                document.getElementById("customers-div").innerHTML=   " <div id=\"alert-load-data\" className='alert alert-info' role='alert'>\"+\n" +
                    "                    <h3 className='alert-heading'>Welcome to Customers Management System</h3>\n" +
                    "                    <h4>If you are seeing this message, it means data is not yet loaded to the database. </h4> <hr>\n" +
                    "                    <h4 className='mb-0'>Click on the button below to extract data from the CSV files and load it to the database.</h4>\n" +
                    "                    <button type='button' class='btn btn-primary btn-lg' onClick='loadData()'>Load Data</button>\n" +
                    "                </div>"
            }
        }
        });    }

//    loading one customer to profile model
function getCustomer(customerId) {
    $.ajax({
        type: "GET",
        dataType: 'json',
        url: "/customer/"+customerId,
        success: function (customer) {
            if (customer!=null) {
                document.getElementById("customer-name").innerHTML="<h3>" + customer.lastName +" "+ customer.firstName+"</h3>"
                document.getElementById("customer-bio-data").innerHTML=
                    "<h4>Customer Details</h4>"+
                    "<table style='width: 100%;'><p><tr><th>Email Address :</th><td> " +customer.email+"</td></tr>"+
                    "<p><tr><th>Location :</th><td> " + customer.latitude +", "+customer.longitude +"</td></tr>"+
                    "<p><tr><th>IP Address : </th><td>" +customer.ip+"</td></tr>"+
                    "<p><tr><th>Since:</th><td> " + customer.createdAt+"</td></tr>"+
                    "</table>";
                document.getElementById("customer-interests").innerHTML=
                    "<h4>Interests</h4>" +
                    "<ul>" +
                        "<li>Photography</li>"+
                        "<li>Speed Cubes </li>"+
                        "<li>Fortnite</li>"+
                        "<li>Mother of Dragons</li>"+
                    "</ul>";
                    var content=
                    "<h4>Orders</h4>"+
                    "<table class='table'>"+
                        "<tr>" +
                            "<th>SN</th>" +
                            "<th>Date</th>" +
                            "<th>Status</th>" +
                            "<th>Actions</th>" +
                        "</tr>";
                for(var i=0;i<3;i++)
                {//Should come from DB once we have the order system implemented
                    content+="<tr>" +
                        "<td>789</td>"+
                        "<td>2018-06-15T16:00:00Z</td>"+
                        "<td>Processing</td>"+
                        "<td><a href='/order/789'>View</a></td>"+
                          "</tr>";
                }
                content+="</table>";
                  document.getElementById("customer-orders").innerHTML=content;
                }
            }
    });
}

//Delete customer by ID
function myDelFunction(customerId) {
    if (confirm("Alert!!!!!!\n\nAre you sure you want to delete the customer record?" )) {
        $.ajax({
            type: "DELETE",
            dataType: 'json',
            url: "/customer/"+customerId,
            success: function (result) {
                if(result){
                    loadCustomers();
                    $(".alert").removeClass("in").show();
                    $(".alert").delay(400).addClass("in").fadeOut(6000);
                }else
                    window.alert('There was an error deleting the customer.');
            }});
    }
}

function search() {
  clearTimeout($.data(this, 'timer'));
    if($("#search-bar").val()==="")
        loadCustomers();//loads data from database if search box empty
       else{
        $(this).data('timer', setTimeout(searchData, 500));//sets the timer between the key press from keyboard and the search
    }
}

function searchData(){
    var searchValue = $("#search-bar").val();        //fetching the value from the textbox
    pageNo=0
        $.ajax({
        type: "GET",
        dataType: 'json',
        url: "/customer/search/"+searchValue,
        success: function (customersData) {
            customerData=customersData;
            populateCustomer(customerData);
        }
});
}


function populateCustomer(data){
    document.getElementById("customers-div").innerHTML="";

       if (data.length>0) {
           var content="<table class='table table-striped'>";
            content+="<tr>" +
                "<th>SN</th>" +
                "<th>Last Name</th>" +
                "<th>First Name</th>" +
                "<th>Email Address</th>" +
                "<th>IP Address</th>" +
                "<th>Latitude</th>" +
                "<th>Longitude</th>" +
                "<th>Created on</th>" +
                "<th>Updated on</th>" +
                "<th colspan='3' style='text-align: center'>Admin Operations</th>" +
                "</tr>";
        for(var i=pageNo*10; i<pageNo*10+10 & i<data.length;i++)
        {
            content+="<tr id='row"+i+"' style='text-align: left'>" +
                "<td>"+(i+1)+"</td>" +
                "<td>"+data[i].lastName+"</td>" +
                "<td>"+data[i].firstName+"</td>" +
                "<td>"+data[i].email+"</td>" +
                "<td>"+data[i].ip+"</td>" +
                "<td>"+data[i].latitude+"</td>" +
                "<td>"+data[i].longitude+"</td>" +
                "<td>"+data[i].createdAt+"</td>" +
                "<td>"+((data[i].updatedAt!=null)?data[i].updatedAt:data[i].createdAt)+"</td>" +
                "<td>" +
                    "<button class='btn btn-primary btn-sm  glyphicon glyphicon-user' data-toggle='modal' " +
                    "onclick='getCustomer("+data[i].customerId+")' " +
                    "data-target='#profileModel'>&nbsp;Profile</button>" +
                "</td>"+
                "<td><a href='/home/new-customer/"+data[i].customerId+"'> <button class='btn btn-warning btn-sm  glyphicon glyphicon-pencil' >&nbsp;Update</button></a></td>"+
                "<td><button class='btn btn-danger btn-sm  glyphicon glyphicon-trash ' onclick='myDelFunction("+data[i].customerId+")'>&nbsp;Delete</button></td>"+
                "</tr>";
            if(i===(data.length-1)){
                   document.getElementById("next-page").style.display = "none";

            }else  document.getElementById("next-page").style.display = "inline";
            if(i<10){
                document.getElementById("prev-page").style.display = "none";

            }else
                document.getElementById("prev-page").style.display = "inline";

            document.getElementById("curr-page").style.display = "inline";
            document.getElementById("curr-page").innerText="Page "+ (pageNo+1) +" of " + ((data.length%10==0)?data.length/10:Math.trunc(data.length/10+1));
        }
        content+="</table>";
        $('#customers-div').append(content);  //this is to append the data fetched from json to the table

    }else {
           document.getElementById("customers-div").innerHTML="";
           content="" +
               "<div  style='background-color: Lightgray;width: 40%;margin: 0 auto;padding: 5%;'>" +
               "<h3>We couldn't locate the customer you're looking for!</h3><h4>Try again with different name, email, or IP Address.</h4></div>";
           $('#customers-div').append(content);
       }
}


function loadData(){
document.getElementById("alert-load-data").innerHTML="" +

    "<div className='d-flex align-items-center'>"+
        "<strong><h1>Loading...</h1></strong>"+
        "<div className='spinner-border ml-auto' role='status' aria-hidden='true'></div></div>";

    pageNo=0;
    $.ajax({
        type: "GET",
        dataType: 'json',
        url: "/customers/load-data",
        success: function (customers) {
            customerData=customers;
            if(customers.length)
                populateCustomer(customers);
                    }
    });
}

function updateCustomer(customerId) {
    $.ajax({
        type: "GET",
        dataType: 'json',
        url: "/customer/" + customerId,
        success: function (customer) {
            if (customer != null) {
                document.getElementById("customerId").value = customer.customerId;
                document.getElementById("createdAt").value = customer.createdAt;
                document.getElementById("firstName").value = customer.firstName;
                document.getElementById("lastName").value = customer.lastName;
                document.getElementById("email").value = customer.email;
                document.getElementById("ip").value = customer.ip;
                document.getElementById("latitude").value = customer.latitude;
                document.getElementById("longitude").value = customer.longitude;
            }

        }
    });
}

function saveCustomer() {
var id="77";//document.getElementById("customerId").value.toString();

    var customer = {
        "customerId":""+id+"",
        "createdAt": document.getElementById("createdAt").innerText ,
        "firstName": document.getElementById("firstName").value,
        "lastName": document.getElementById("lastName").value,
        "email": document.getElementById("email").value,
        "ip" :document.getElementById("ip").value,
        "latitude": document.getElementById("latitude").value,
        "longitude":document.getElementById("longitude").value
            };
       $.ajax({
        type: "POST",
        contentType: 'application/json; charset=utf-8',
        dataType: 'json',
        url: "/customer",
        data:customer,
        success: function (customer) {
            if (customer != null) {

                alert("successful")
            }

        }
    });
}

