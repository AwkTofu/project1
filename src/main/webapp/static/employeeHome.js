const loginURL = "http://localhost:8082/expenseReimbursement/static/login.html";

// Create our number formatter.
let formatter = new Intl.NumberFormat('en-US', {
  style: 'currency',
  currency: 'USD',

  // These options are needed to round to whole numbers if that's what you want.
  //minimumFractionDigits: 0, // (this suffices for whole numbers, but will print 2500.10 as $2,500.1)
  //maximumFractionDigits: 0, // (causes 2500.99 to be printed as $2,501)
});

let displayReimbursement = reimbursement => {
    let reimburseTable = document.getElementById("ReimburseTable");

    reimburseTable.querySelectorAll('*').forEach(n => n.remove());

    //recreating the header of the table
    let head = document.createElement('tr');
    head.innerHTML = '<tr><th> Reimburse ID </th>' +
                '<th> Owner</th>' +
                '<th> Amount </th>' +
                '<th> Status </th>' +
                '<th> Manager </th></tr>';
    reimburseTable.appendChild(head);


    reimbursement.forEach (reimburse => {
        let tr = document.createElement('tr');
        tr.innerHTML = '<td>' + reimburse.id + "</td>" +
                        '<td>' + reimburse.owner_id + "</td>" +
                        '<td>' + formatter.format(reimburse.amount) + "</td>" +
                        '<td>' + reimburse.status + "</td>" +
                        '<td>' + reimburse.manager_id + "</td>";

        reimburseTable.appendChild(tr);
    })


}

function getAllReimbursementsSelf() {
    let token = sessionStorage.token;

    fetch("http://localhost:8082/expenseReimbursement/reimburstment/create", {headers: {"Authorization" : token}})
    .then(response => response.json()) //converts JSON response body into JS objects
    .then(displayReimbursement);
}

let createReimbursement = () => {
    let amount = document.getElementById("amount").value;

    const errorDiv = document.getElementById("error-div");
    errorDiv.hidden = true;

    if (amount && amount > 0) {
        //console.log(reimburseID, statusValue);
            const requestBody = `amount=${amount}`;
            fetch("http://localhost:8082/expenseReimbursement/reimburstment/create", {
                method: "POST",
                headers: {
                    'Authorization' : sessionStorage.token,
                    'Content-Type' :  'application/x-www-form-urlencoded'
                },
                body: requestBody
            }).then((r) => {
                console.log(r);
                getAllReimbursementsSelf();
            })
    }
    else {
        errorDiv.hidden = false;
        errorDiv.innerText = "Amount MUST be a positive number";
    }
}

let logoutOnClick = () => {
    sessionStorage.removeItem('token');
    window.location.href=loginURL;
}

//Check if token exist and if it's even manager
if (!sessionStorage.token || sessionStorage.token.split(":")[1] !== "EMPLOYEE")
{   //If your role isn't right, logs you out and send to login screen
    logoutOnClick();
}

getAllReimbursementsSelf();

//Give logoutButton onclick
let logoutButton = document.getElementById("logoutButton");
logoutButton.addEventListener("click", logoutOnClick);

//give
let reimburseButton = document.getElementById("reimburseButton");
reimburseButton.addEventListener("click", createReimbursement);