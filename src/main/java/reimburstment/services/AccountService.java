package reimburstment.services;

import reimburstment.daos.AccountDao;
import reimburstment.daos.AccountDaoImpl;
import reimburstment.models.Account;
import reimburstment.models.AccountType;

import java.util.ArrayList;

public class AccountService {
    private AccountDao accountDao = new AccountDaoImpl();

    public ArrayList<Account> getAllAccounts() {return accountDao.getAll(); }

    public ArrayList<Account> getAllEmployee() {return accountDao.getAllEmployee(); }

    public Account getAccountByCredentials(String username, String password) {
        if (username == null || username.isEmpty() || password == null || password.isEmpty())
            return null;

        return accountDao.getAccountByUsernameAndPassword(username, password);
    }

    public Account getAccountById(int id) { return accountDao.getAccountById(id); }

    public boolean accountExistByUsername(String username) { return accountDao.accountExistByUsername(username); }

    //Assuming AccountType must exist in a valid form
    public boolean createNewAccount(String username, String password, String firstName, String lastName, String email, String accountType) {
        AccountType realAccType = AccountType.valueOf(accountType);
        return accountDao.createNewAccount(username,password, firstName, lastName, email, realAccType);
    }
}
