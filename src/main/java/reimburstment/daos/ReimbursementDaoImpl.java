package reimburstment.daos;

import reimburstment.models.Account;
import reimburstment.models.Reimbursement;
import reimburstment.models.Status;
import reimburstment.services.ConnectionService;

import java.math.BigDecimal;
import java.sql.*;
import java.util.ArrayList;

public class ReimbursementDaoImpl implements ReimbursementDao{

    private ArrayList<Reimbursement> reimbursements= new ArrayList<>();
    private ConnectionService connectionService = new ConnectionService();

    public ReimbursementDaoImpl() { reimbursements = getAll(); }

    public ArrayList<Reimbursement> getAll()
    {
        ArrayList<Reimbursement> allReimbursements = new ArrayList<>();
        String sql = "select * from p1_reimbursements";

        try {
            Connection c = connectionService.establishConnection();
            Statement stmt = c.createStatement();
            ResultSet rs = stmt.executeQuery(sql);

            while (rs.next())
            {
                int id = rs.getInt("reimburse_id");
                int owner_id = rs.getInt("owner_id");
                BigDecimal amount = rs.getBigDecimal("amount");
                Status status = Status.valueOf(rs.getString("status"));
                int manager_id = rs.getInt("manager_id");

                Reimbursement r = new Reimbursement(id, owner_id, amount, status, manager_id);
                allReimbursements.add(r);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return allReimbursements;
    }
    public ArrayList<Reimbursement> getAllByOwner(Account owner)
    {
        ArrayList<Reimbursement> allReimbursements = new ArrayList<>();
        String sql = "select * from p1_reimbursements where owner_id = ?";

        try {
            Connection c = connectionService.establishConnection();
            PreparedStatement pstmt = c.prepareStatement(sql);
            pstmt.setInt(1, owner.getId());

            ResultSet rs = pstmt.executeQuery();

            while (rs.next())
            {
                int id = rs.getInt("reimburse_id");
                int owner_id = rs.getInt("owner_id");
                BigDecimal amount = rs.getBigDecimal("amount");
                Status status = Status.valueOf(rs.getString("status"));
                int manager_id = rs.getInt("manager_id");

                Reimbursement r = new Reimbursement(id, owner_id, amount, status, manager_id);
                allReimbursements.add(r);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return allReimbursements;
    }
    public void setStatus(Reimbursement reimbursement, Account manager, Status status)
    {
        String sql = "update p1_reimbursements set status = ?::p1_status, manager_id = ? where reimburse_id = ?";

        try {
            Connection c = connectionService.establishConnection();
            PreparedStatement pstmt = c.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            pstmt.setString(1, status.toString());
            pstmt.setInt(2, manager.getId());
            pstmt.setInt(3, reimbursement.getId());
            pstmt.executeUpdate();

            //Changing the files locally
            reimbursement.setStatus(status);
            reimbursement.setManager_id(manager.getId());
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public boolean createNewReimbursement(int owner_id, BigDecimal amount){
        String sql =  "insert into p1_reimbursements (owner_id, amount, status) values (?, ?, ?::p1_status)";;

        try {
            Connection c = connectionService.establishConnection();
            PreparedStatement pstmt = c.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            pstmt.setInt(1, owner_id);
            pstmt.setBigDecimal(2, amount);
            pstmt.setString(3, Status.PENDING.toString());
            pstmt.executeUpdate();

            ResultSet rs = pstmt.getGeneratedKeys();
            rs.next();
            //System.out.println("Test Key: " + rs.getInt(1));
            int newID = rs.getInt(1);
            Reimbursement newReimbursement = new Reimbursement(newID, owner_id, amount, Status.PENDING);
            reimbursements.add(newReimbursement);
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public Reimbursement getByID(int id) {
        for (Reimbursement r: reimbursements)
        {
            if (r.getId() == id)
                return r;
        }
        return null;
    }
}
