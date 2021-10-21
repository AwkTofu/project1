package reimburstment.daos;

import java.util.ArrayList;

import com.fasterxml.jackson.annotation.JsonIgnore;
import reimburstment.models.Account;
import reimburstment.models.AccountType;

public interface AccountDao {
    public ArrayList<Account> getAll();
    public ArrayList<Account> getAllEmployee();
    public Account getAccountById(int id);
    public Account getAccountByUsernameAndPassword(String username, String password);
    public boolean createNewAccount(String username, String password, String firstName, String lastName, String email, AccountType accountType);
    public boolean accountExistByUsername(String username);

}
