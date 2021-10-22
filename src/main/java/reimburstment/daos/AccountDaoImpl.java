package reimburstment.daos;

import reimburstment.models.Account;
import reimburstment.models.AccountType;

import reimburstment.services.ConnectionService;

import java.sql.*;
import java.util.ArrayList;

public class AccountDaoImpl implements AccountDao{
    private ArrayList<Account> accounts = new ArrayList<>();
    private ConnectionService connectionService = new ConnectionService();

    public AccountDaoImpl() {
        accounts = getAll();
    }


    @Override
    public ArrayList<Account> getAll() {
        ArrayList<Account> allAccounts = new ArrayList<>();

        String sql = "select * from p1_accounts";
        try {
            Connection c = connectionService.establishConnection();
            Statement stmt = c.createStatement();
            ResultSet rs = stmt.executeQuery(sql);

            while (rs.next())
            {
                int id = rs.getInt("account_id");
                String username = rs.getString("username");
                String password = rs.getString("password");
                String firstName = rs.getString("first_name");
                String lastName = rs.getString("last_name");
                String email = rs.getString("email");
                AccountType accountType = AccountType.valueOf(rs.getString("account_type"));

                Account a = new Account(id, username, password, firstName, lastName,email, accountType);
                allAccounts.add(a);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return allAccounts;
    };

    @Override
    public ArrayList<Account> getAllEmployee() {
        ArrayList<Account> allAccounts = new ArrayList<>();

        String sql = "select * from p1_accounts where account_type = 'EMPLOYEE'";
        try {
            Connection c = connectionService.establishConnection();
            Statement stmt = c.createStatement();
            ResultSet rs = stmt.executeQuery(sql);

            while (rs.next())
            {
                int id = rs.getInt("account_id");
                String username = rs.getString("username");
                String password = rs.getString("password");
                String firstName = rs.getString("first_name");
                String lastName = rs.getString("last_name");
                String email = rs.getString("email");
                AccountType accountType = AccountType.valueOf(rs.getString("account_type"));

                Account a = new Account(id, username, password, firstName, lastName,email, accountType);
                allAccounts.add(a);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return allAccounts;
    };

    @Override
    public Account getAccountById(int id) {
        accounts = getAll();

        for(Account a: accounts) {
           if (a.getId() == id)
               return a;
        }
        return null;
    };

    @Override
    public Account getAccountByUsernameAndPassword(String username, String password){
        accounts = getAll();

        for (Account a: accounts)
        {
            if (a.getUsername() != null && a.getUsername().equals(username))
            {
                if (a.getPassword() != null && a.getPassword().equals(password))
                {
                    return a;
                }
            }
        }
        return  null;
    };

    @Override
    public boolean createNewAccount(String username, String password, String firstName, String lastName, String email, AccountType accountType) {
        String sql = "insert into p1_accounts (username, password, first_name, last_name, email, account_type) values (?, ?, ?, ?, ?, ?::p1_account_types)";

        try {
            Connection c = connectionService.establishConnection();
            PreparedStatement pstmt = c.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            pstmt.setString(1, username);
            pstmt.setString(2, password);
            pstmt.setString(3, firstName);
            pstmt.setString(4, lastName);
            pstmt.setString(5, email);
            pstmt.setString(6, accountType.toString());
            pstmt.executeUpdate();

            ResultSet rs = pstmt.getGeneratedKeys();
            rs.next();
            //System.out.println("Test Key: " + rs.getInt(1));
            int newID = rs.getInt(1);
            Account newAcc = new Account(newID, username, password, firstName, lastName, email, accountType);
            accounts.add(newAcc);

            //System.out.println(newAcc);
            //System.out.println(accounts);
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean accountExistByUsername(String username) {
        for (Account a: accounts)
        {
            if (a.getUsername().equals(username))
                return true;
        }
        return false;
    }
}
