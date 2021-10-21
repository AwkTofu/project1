package reimburstment.servlets;

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

public class ReimbursementUpdateServlet extends HttpServlet {
    private AuthService authService= new AuthService();
    private ReimbursementService reimbursementService = new ReimbursementService();

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String authToken = req.getHeader("authorization");
        String reimburseID = req.getParameter("reimburseid");
        String status = req.getParameter("status");

        boolean tokenIsValidFormat = authService.validateToken(authToken);
        if (!tokenIsValidFormat) {
            resp.sendError(400, "Improper token format.");
        } else if (status == null || !doesStatusExist(status)) {
            resp.sendError(400, "Bad information");
        } else {

            Account manager = authService.findAccountByToken(authToken);
            if (manager == null || !manager.getAccountType().equals(AccountType.MANAGER))
                resp.sendError(403, "Token invalid");
            else {
                Reimbursement realReimburseID = reimbursementService.getReimbursementByID(reimburseID);
                if (realReimburseID == null)
                    resp.sendError(404, "Can't find reimbursement on server");
                else
                {
                    System.out.println(realReimburseID);
                    reimbursementService.updateReimbursement(realReimburseID, manager, status);
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
