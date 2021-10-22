package reimburstment.servlets;

import com.fasterxml.jackson.databind.ObjectMapper;
import reimburstment.models.Account;
import reimburstment.models.AccountType;
import reimburstment.models.Reimbursement;
import reimburstment.models.Status;
import reimburstment.services.AuthService;
import reimburstment.services.ReimbursementService;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;

public class ReimbursementEmployeeServlet extends HttpServlet {
    private AuthService authService= new AuthService();
    private ReimbursementService reimbursementService = new ReimbursementService();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String authToken = req.getHeader("authorization");

        System.out.println(authToken);

        boolean tokenIsValidFormat = authService.validateToken(authToken);
        if (!tokenIsValidFormat) {
            resp.sendError(400, "Improper token format.");
        } else {
            Account owner = authService.findAccountByToken(authToken);
            if (owner == null)
                resp.sendError(403, "Token invalid");
            else {
                resp.setStatus(200);
                try(PrintWriter pw = resp.getWriter();){
                    ArrayList<Reimbursement> allReimbursements = reimbursementService.getAllReimbursementByOwner(owner);
                    System.out.println(allReimbursements + " " + owner);
                    ObjectMapper om = new ObjectMapper();

                    String userJson = om.writeValueAsString(allReimbursements);
                    pw.write(userJson);
                }
            }
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String authToken = req.getHeader("authorization");
        String amount = req.getParameter("amount");


        System.out.println("Testing "+authToken+ " " + amount + " " );
        boolean tokenIsValidFormat = authService.validateToken(authToken);
        if (!tokenIsValidFormat) {
            resp.sendError(400, "Improper token format.");
        } else if (amount == null) {
            resp.sendError(400, "Bad information");
        } else {

            Account owner = authService.findAccountByToken(authToken);
            if (owner == null)
                resp.sendError(403, "Token invalid");
            else {
                if (reimbursementService.createNewReimbursement(owner, amount))
                    resp.setStatus(201);
                else {
                    resp.sendError(500, "Unknown error occured while creating new Reimbursement.");
                }
            }
        }
    }

    private boolean doesStatusExist(String a)
    {
        //Making sure account type param exist
        Status[] types = Status.values();
        for (Status t: types)
        {
            if (t.toString().equals(a))
                return true;
        }
        return false;
    }
}
