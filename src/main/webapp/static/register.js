const loginURL = "http://localhost:8082/expenseReimbursement/static/login.html";

let createNewAccount = () => {
    let username = document.getElementById("username").value;
    let password = document.getElementById("password").value;
    let firstName = document.getElementById("firstName").value;
    let lastName = document.getElementById("lastName").value;
    let email = document.getElementById("email").value;
    let role = document.getElementById("role").value;

    const errorDiv = document.getElementById("error-div");
    errorDiv.hidden = true;
    let error = false;

    //console.log(username, password, firstName, lastName, email, role);
    let errorString = "";

    if (!username) {
        errorString += "Username must exist.\n";
        error = true;
    }
    if (!password) {
        errorString += "Password must exist.\n";
        error = true;
    }
    if (!firstName) {
        errorString += "First name must exist.\n";
        error = true;
    }
    if (!lastName) {
        errorString += "Last name must exist.\n";
        error = true;
    }

    //console.log(error, errorString)
    if (error)
    {
        errorDiv.hidden = false;
        errorDiv.innerText = errorString;
    } else {
        errorDiv.hidden = true;

        const requestBody = `username=${username}&password=${password}&firstname=${firstName}&lastname=${lastName}&email=${email ? email : null}&accounttype=${role}`

        fetch("http://localhost:8082/expenseReimbursement/accounts", {
            method: "POST",
            headers: {
                'Content-Type' :  'application/x-www-form-urlencoded'
            },
            body: requestBody
        }).then(r => {
            console.log(r);
            if (r.status === 400) {
                errorDiv.hidden = false;
                errorDiv.innerText = "Username already exist";
                return false;
            } else if (r.status === 201) {
                loginOnClick();
            } else {
                errorDiv.hidden = false;
                errorDiv.innerText = "Unknown Error";
                return false;
            }

        })
    }
}

let loginOnClick = () => {
    window.location.href=loginURL;
    console.log("test")
}

//Give loginButton onclick
let loginButton = document.getElementById("loginButton");
loginButton.addEventListener("click", loginOnClick);

//Give createAccountButton onclick
let createAccountButton = document.getElementById("createAccountButton");
console.log(createAccountButton);
createAccountButton.addEventListener("click", createNewAccount);