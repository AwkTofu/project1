package reimburstment.servlets;


import com.fasterxml.jackson.databind.ObjectMapper;
import reimburstment.models.Account;
import reimburstment.models.Reimbursement;
import reimburstment.services.AccountService;
import reimburstment.services.AuthService;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;

public class AuthServlet extends HttpServlet {
    private AccountService accService = new AccountService();
    private AuthService authService= new AuthService();

    //Get current Account information
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String authToken = req.getHeader("authorization");

        boolean tokenIsValidFormat = authService.validateToken(authToken);
        if (!tokenIsValidFormat) {
            resp.sendError(400, "Improper token format.");
        } else {
            Account currentAccount = authService.findAccountByToken(authToken);

            if (currentAccount == null)
                resp.sendError(401, "Auth Token Invalid, no user exist");
            else {
                resp.setStatus(200);
                try(PrintWriter pw = resp.getWriter();){
                    ObjectMapper om = new ObjectMapper();

                    String userJson = om.writeValueAsString(currentAccount);
                    pw.write(userJson);
                }
            }
        }
    }

    //Logging in
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String userParam = req.getParameter("username");
        String passParam = req.getParameter("password");

        System.out.println("Credentials received: "+userParam +" "+passParam);
        //System.out.println("Test: "+req.);

        Account account = accService.getAccountByCredentials(userParam, passParam);
        //System.out.println(account);
        //System.out.println(account.getId() + " "+ account.getAccountType());
        if (account == null)
        {
            resp.sendError(401, "User credentials provided can't find an account.");
        } else {
            resp.setStatus(200);

            String token = account.getId() + ":" + account.getAccountType();
            resp.setHeader("Authorization", token);
        }
    }
}
