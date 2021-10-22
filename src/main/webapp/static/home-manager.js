const loginURL = "http://localhost:8082/expenseReimbursement/static/login.html";

// Create our number formatter.
let formatter = new Intl.NumberFormat('en-US', {
  style: 'currency',
  currency: 'USD',

  // These options are needed to round to whole numbers if that's what you want.
  //minimumFractionDigits: 0, // (this suffices for whole numbers, but will print 2500.10 as $2,500.1)
  //maximumFractionDigits: 0, // (causes 2500.99 to be printed as $2,501)
});


let displayEmployee = employees => {
    let employeeTable = document.getElementById("EmployeeTable");

    employees.forEach( employee => {
        //console.log(employee);
        let tr = document.createElement('tr');
        tr.innerHTML = '<td>' + employee.id + "</td>" +
                        '<td>' + employee.firstName + "</td>" +
                        '<td>' + employee.lastName + "</td>" +
                        '<td>' + employee.email + "</td>";

        employeeTable.appendChild(tr);
    })
}

let displayReimbursement = reimbursement => {
    let reimburseTable = document.getElementById("ReimburseTable");
    let selectReimburse = document.getElementById("listOfReimburseID");

    reimburseTable.querySelectorAll('*').forEach(n => n.remove());
    selectReimburse.querySelectorAll('*').forEach(n => n.remove());

    //recreating the header of the table
    let head = document.createElement('tr');
    head.innerHTML = '<tr><th scope="col"> Reimburse ID </th>' +
                '<th scope="col" data-sortable="true" data-order="desc"> Owner</th>' +
                '<th scope="col"> Amount </th>' +
                '<th scope="col"> Status </th>' +
                '<th scope="col"> Manager </th></tr>';
    reimburseTable.appendChild(head);


    reimbursement.forEach (reimburse => {
        let tr = document.createElement('tr');
        tr.innerHTML = '<td>' + reimburse.id + "</td>" +
                        '<td>' + reimburse.owner_id + "</td>" +
                        '<td>' + formatter.format(reimburse.amount)  + "</td>" +
                        '<td>' + reimburse.status + "</td>" + 
                        '<td>' + reimburse.manager_id + "</td>";

        reimburseTable.appendChild(tr);

        let option = document.createElement('option')
        option.value = option.textContent = reimburse.id;
        selectReimburse.appendChild(option);
    })
}

function getAllEmployee(){
    let token = sessionStorage.token;

    fetch("http://localhost:8082/expenseReimbursement/accounts", {headers: {"Authorization" : token}})
    .then(response => response.json()) //converts JSON response body into JS objects 
    .then(displayEmployee);
}



function getAllReimbursements() {
    let token = sessionStorage.token;

    fetch("http://localhost:8082/expenseReimbursement/reimburstment", {headers: {"Authorization" : token}})
    .then(response => response.json()) //converts JSON response body into JS objects 
    .then(displayReimbursement);
}

let statusChangeOnClick = () => {
    let reimburseID = document.getElementById("listOfReimburseID").value;
    let statusValue = document.getElementById("status").value;

    //console.log(reimburseID, statusValue);
    const requestBody = `reimburseid=${reimburseID}&status=${statusValue}`
    fetch("http://localhost:8082/expenseReimbursement/reimburstment/update", {
        method: "POST",
        headers: {
            'Authorization' : sessionStorage.token,
            'Content-Type' :  'application/x-www-form-urlencoded'
        },
        body: requestBody
    }).then((r) => {
        console.log(r);
        getAllReimbursements();
    })


}

let logoutOnClick = () => {
    sessionStorage.removeItem('token');
    window.location.href=loginURL;
}

//Get Personal Information
let getPersonalInfo = () => {
    let firstName = document.getElementById("firstName");
    let lastName = document.getElementById("lastName");
    let email = document.getElementById("email");
    let role = document.getElementById("role");

    //console.log("Before Fetch", role);

    fetch("http://localhost:8082/expenseReimbursement/login", {headers: {"Authorization" : sessionStorage.token}})
    .then(response => response.json()) //converts JSON response body into JS objects
    .then(r => {
        //console.log(r);
        firstName.value = r.firstName;
        lastName.value = r.lastName;
        r.email ? email.value = r.email : email.placeholder = r.email;
        role.innerHTML = r.accountType;
    });
}

//Check if token exist and if it's even manager
if (!sessionStorage.token || sessionStorage.token.split(":")[1] !== "MANAGER")
{   //If your role isn't right, logs you out and send to login screen
    logoutOnClick();
}

getPersonalInfo();
getAllEmployee();
getAllReimbursements();

//Give statusChangeButton onClick
let statusChangeButton = document.getElementById("statusChangeButton");
statusChangeButton.addEventListener("click", statusChangeOnClick);

//Give logoutButton onclick
let logoutButton = document.getElementById("logoutButton");
logoutButton.addEventListener("click", logoutOnClick);