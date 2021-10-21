package reimburstment.services;

import reimburstment.daos.ReimbursementDao;
import reimburstment.daos.ReimbursementDaoImpl;
import reimburstment.models.Account;
import reimburstment.models.Reimbursement;
import reimburstment.models.Status;

import java.math.BigDecimal;
import java.util.ArrayList;

public class ReimbursementService {
    private ReimbursementDao reimbursementDao = new ReimbursementDaoImpl();

    public ArrayList<Reimbursement> getAllReimbursement(){ return reimbursementDao.getAll(); }

    public Reimbursement getReimbursementByID(String reimbursementID) {
        int realID = Integer.parseInt(reimbursementID);
        return reimbursementDao.getByID(realID);
    }

    public ArrayList<Reimbursement> getAllReimbursementByOwner(Account owner){ return reimbursementDao.getAllByOwner(owner); }

    //Assume that status is always valid and checked beforehand
    public void updateReimbursement(Reimbursement reimbursement, Account manager, String status) {
        Status realStatus = Status.valueOf(status);
        reimbursementDao.setStatus(reimbursement, manager, realStatus);
    }

    //Assume amount is BigDecimal and is always valid and status is always valid
    public boolean createNewReimbursement(Account owner, String amount) {
        BigDecimal realAmount = new BigDecimal(amount);
        return reimbursementDao.createNewReimbursement(owner.getId(), realAmount);
    }
}
