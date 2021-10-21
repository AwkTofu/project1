package reimburstment.servlets;

import com.fasterxml.jackson.databind.ObjectMapper;
import reimburstment.models.Account;
import reimburstment.models.AccountType;
import reimburstment.services.AccountService;
import reimburstment.services.AuthService;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;

public class AccountServlet extends HttpServlet {

    private AuthService authService= new AuthService();
    private AccountService accountService= new AccountService();


    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String authToken = req.getHeader("authorization");

        //System.out.println(authToken);
        boolean tokenIsValidFormat = authService.validateToken(authToken);
        if (!tokenIsValidFormat) {
            resp.sendError(400, "Improper token format.");
        } else {
            Account currentAccount = authService.findAccountByToken(authToken);

            if (currentAccount == null)
                resp.sendError(401, "Auth Token Invalid, no user exist");
            else {
                if (currentAccount.getAccountType() == AccountType.MANAGER){
                    resp.setStatus(200);
                    try(PrintWriter pw = resp.getWriter();){
                        ArrayList<Account> accounts = accountService.getAllEmployee();
                        ObjectMapper om = new ObjectMapper();

                        String userJson = om.writeValueAsString(accounts);
                        pw.write(userJson);
                    }
                } else {
                    resp.sendError(403, "Invalid account role for current request");
                }
            }
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String userParam = req.getParameter("username");
        String passParam = req.getParameter("password");
        String firstNameParam = req.getParameter("firstname");
        String lastNameParam = req.getParameter("lastname");
        String emailParam = req.getParameter("email");
        String accountTypeParam = req.getParameter("accounttype");

        System.out.println("Credentials received: "+userParam +" "+passParam + " " +
                            firstNameParam + " " + lastNameParam + " " +
                            emailParam + " " + accountTypeParam);

        if (userParam == null || passParam == null || firstNameParam == null || lastNameParam == null ||
                accountTypeParam == null || !doesAccountTypeExist(accountTypeParam))
        {
            resp.sendError(400, "Bad information");
        } else if (accountService.accountExistByUsername(userParam))
        {
            resp.sendError(400, "Username taken");
        } else {
            if (accountService.createNewAccount(userParam, passParam, firstNameParam, lastNameParam, emailParam, accountTypeParam))
            {
                resp.setStatus(201);
            } else {
                resp.sendError(500, "Unknown error occured while creating new account.");
            }
        }
    }

    private boolean doesAccountTypeExist(String a)
    {
        //Making sure account type param exist
        AccountType[] types = AccountType.values();
        for (AccountType t: types)
        {
            if (t.toString().equals(a))
                return true;
        }
        return false;
    }
}
