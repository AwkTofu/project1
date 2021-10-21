package reimburstment.daos;

import reimburstment.models.Account;
import reimburstment.models.Reimbursement;
import reimburstment.models.Status;

import java.math.BigDecimal;
import java.util.ArrayList;

public interface ReimbursementDao {
    public ArrayList<Reimbursement> getAll();
    public ArrayList<Reimbursement> getAllByOwner(Account owner);
    public Reimbursement getByID(int id);
    public void setStatus(Reimbursement reimbursement, Account manager, Status status);
    public boolean createNewReimbursement(int owner_id, BigDecimal amount);
}
