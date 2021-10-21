package reimburstment.models;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Objects;

public class Reimbursement implements Serializable {
    private int id;
    private int owner_id;
    private BigDecimal amount;
    private Status status;
    private int manager_id;


    // ------------------------ Constructors ------------------------
    public Reimbursement(){super();}

    public Reimbursement( int owner_id, BigDecimal amount, Status status)
    {
        this.owner_id = owner_id;
        this.amount = amount;
        this.status = status;
    }
    public Reimbursement(int id, int owner_id, BigDecimal amount, Status status)
    {
        this.id = id;
        this.owner_id = owner_id;
        this.amount = amount;
        this.status = status;
    }
    public Reimbursement(int id, int owner_id, BigDecimal amount, Status status, int manager_id)
    {
        this.id = id;
        this.owner_id = owner_id;
        this.amount = amount;
        this.status = status;
        this.manager_id = manager_id;
    }

    // ------------------------ Getters and Setters ------------------------
    public int getId() {
        return id;
    }

    public int getOwner_id() {
        return owner_id;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public Status getStatus() {
        return status;
    }

    public int getManager_id() {
        return manager_id;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public void setManager_id(int manager_id) {
        this.manager_id = manager_id;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Reimbursement that = (Reimbursement) o;
        return id == that.id && owner_id == that.owner_id && manager_id == that.manager_id && Objects.equals(amount, that.amount) && status == that.status;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, owner_id, amount, status, manager_id);
    }

    @Override
    public String toString() {
        return "Reimbursement{" +
                "id=" + id +
                ", owner_id=" + owner_id +
                ", amount=" + amount +
                ", status=" + status +
                ", manager_id=" + manager_id +
                '}';
    }
}
