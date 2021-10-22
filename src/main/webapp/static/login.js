// respond to clicking the login button by:
document.getElementById("login-btn").addEventListener("click", attemptLogin);
// attemptLogin is being used as a callback function
// there is a third optional parameter ("useCapture") which indicates nested how nested events propagate

//URL for this page is http://localhost:8082/expenseReimbursement/static/login.html

function attemptLogin(){
    const errorDiv = document.getElementById("error-div");
    errorDiv.hidden = true;

    // get input values from input fields
    const username = document.getElementById("username").value;
    const password = document.getElementById("password").value;
    // we're using const here, rather than var or let

    console.log(`username: ${username}, password: ${password}`); // this is a template literal

    // use XMLHttpRequest to create an http request - POST to http://localhost:8082/auth-demo/login
                                                    // + request body with credentials
    const xhr = new XMLHttpRequest();
    xhr.open("POST", "http://localhost:8082/expenseReimbursement/login"); // ready state changes 0->1

    // define onreadystatechange callback for the xhr object (check for readystate 4)
    xhr.onreadystatechange = function(){
        if(xhr.readyState===4){
            //console.log(xhr.status);
            // look at status code (either 401 or 200)
            // if the status code is 401 - indicate to the user that their credentials are invalid
            if(xhr.status===401){
                errorDiv.hidden = false;
                errorDiv.innerText = "invalid admin credentials";
            } else if (xhr.status===200){
                // if the status code is 200 - get auth token from response and store it in browser, navigate to another page
                
                const token = xhr.getResponseHeader("Authorization");
                //console.log(token); //"2:ADMIN"


                sessionStorage.setItem("token", token);
                const words = token.split(':');
                const url =`http://localhost:8082/expenseReimbursement/static/home-${words[1].toLowerCase()}.html`;
                //console.log(url);
                window.location.href=url;
            } else {
            //console.log(xhr.getResponseHeader("Authorization"));
                errorDiv.hidden = false;
                errorDiv.innerText = "unknown error";
            }
        }
    }

    xhr.setRequestHeader("Content-Type", "application/x-www-form-urlencoded");

    // send request, with the username and password in the request body
    const requestBody = `username=${username}&password=${password}`;
    xhr.send(requestBody); // ready state changes 1->2, 2->3, 3-4

}


function fetchLogin() {
    const errorDiv = document.getElementById("error-div");
    errorDiv.hidden = true;

    // get input values from input fields
    const username = document.getElementById("username").value;
    const password = document.getElementById("password").value;

    const data = {username: username, password: password}
    console.log(JSON.stringify(data));
    fetch('http://localhost:8082/expenseReimbursement/login', {
        method: 'POST',
        mode: 'cors',
        headers: {
            'Content-Type': 'application/json'
        },
        body: JSON.stringify(data)
    })
    .then(r => r.json())
    .then(respond => {
        console.log(respond)
    })
}