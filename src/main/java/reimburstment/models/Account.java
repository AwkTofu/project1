package reimburstment.models;

import com.fasterxml.jackson.annotation.JsonIgnore;

import java.io.Serializable;
import java.util.Objects;

public class Account implements Serializable {

    private int id;

    private String username;

    @JsonIgnore
    private String password;

    private String firstName;
    private String lastName;

    private String email;
    private AccountType accountType;

    // ------------------------ Constructors ------------------------
    public Account() {super(); }

    public Account(int id, String username, String password, String firstName, String lastName, AccountType accountType)
    {
        super();
        this.id = id;
        this.username = username;
        this.password = password;
        this.firstName = firstName;
        this.lastName = lastName;
        this.accountType = accountType;
    }

    public Account(int id, String username, String password, String firstName, String lastName, String email, AccountType accountType)
    {
        super();
        this.id = id;
        this.username = username;
        this.password = password;
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.accountType = accountType;
    }

    // ------------------------ Getters and Setters ------------------------
    public int getId() {
        return id;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public AccountType getAccountType() {
        return accountType;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Account account = (Account) o;
        return id == account.id && Objects.equals(username, account.username) && Objects.equals(password, account.password) && Objects.equals(firstName, account.firstName) && Objects.equals(lastName, account.lastName) && Objects.equals(email, account.email) && accountType == account.accountType;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, username, password, firstName, lastName, email, accountType);
    }

    @Override
    public String toString() {
        return "Account{" +
                "id=" + id +
                ", username='" + username + '\'' +
                ", password='" + password + '\'' +
                ", firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", email='" + email + '\'' +
                ", accountType=" + accountType +
                '}';
    }


}
