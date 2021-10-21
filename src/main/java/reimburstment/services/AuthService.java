package reimburstment.services;

import reimburstment.models.Account;
import reimburstment.models.AccountType;

public class AuthService {
    private AccountService accountService = new AccountService();

    public boolean validateToken(String authToken){
        if(authToken == null)
            return false;

        String[] tokenArr = authToken.split(":");
        if(tokenArr.length != 2)
            return false;

        String idString = tokenArr[0];
        if(!idString.matches("^\\d+$"))// if it does not match a number regular expression, we return false
            return false;

        String accountTypeString = tokenArr[1];
        AccountType[] types = AccountType.values();
        for (AccountType t: types)
        {
            if (t.toString().equals(accountTypeString))
                return true;
        }
        return false;

    }

    public Account findAccountByToken(String authToken)
    {
        String[] tokenArr = authToken.split(":");
        int id = Integer.parseInt(tokenArr[0]);
        return accountService.getAccountById(id);
    }

    public boolean isTokenManager(String authToken)
    {
        String[] tokenArr = authToken.split(":");
        AccountType accType = AccountType.valueOf(tokenArr[1]);
        return accType.equals(AccountType.MANAGER) ? true : false;
    }
}
