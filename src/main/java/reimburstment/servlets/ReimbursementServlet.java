package reimburstment.servlets;

import com.fasterxml.jackson.databind.ObjectMapper;
import reimburstment.models.Account;
import reimburstment.models.Reimbursement;
import reimburstment.services.AuthService;
import reimburstment.services.ReimbursementService;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;

public class ReimbursementServlet extends HttpServlet {
    private AuthService authService= new AuthService();
    private ReimbursementService reimbursementService = new ReimbursementService();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String authToken = req.getHeader("authorization");

        //System.out.println(authToken);

        boolean tokenIsValidFormat = authService.validateToken(authToken);
        if (!tokenIsValidFormat) {
            resp.sendError(400, "Improper token format.");
        } else if (!authService.isTokenManager(authToken)) {
            resp.sendError(403, "Invalid account role for current request");
        } else {
            Account owner = authService.findAccountByToken(authToken);
            if (owner == null)
                resp.sendError(403, "Token invalid");
            else {
                resp.setStatus(200);
                try(PrintWriter pw = resp.getWriter();){
                    ArrayList<Reimbursement> allReimbursements = reimbursementService.getAllReimbursement();
                    //System.out.println(allReimbursements + " " + owner);
                    ObjectMapper om = new ObjectMapper();

                    String userJson = om.writeValueAsString(allReimbursements);
                    pw.write(userJson);
                }
            }
        }
    }
}
