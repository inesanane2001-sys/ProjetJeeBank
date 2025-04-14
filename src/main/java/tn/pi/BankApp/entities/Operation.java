package tn.pi.BankApp.entities;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;

@Entity
@Data
public class Operation {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false) // Assure que le montant est obligatoire
    private double amount;

    @Column(name = "operation_type", nullable = false)
    private String operationType;

    @Column(name = "date", nullable = false)
    private LocalDateTime date;

    @ManyToOne
    @JoinColumn(name = "account_id", nullable = false)
    private Account account;

    @ManyToOne
    @JoinColumn(name = "target_account_id")
    private Account targetAccount;

    // Constructeur par défaut
    public Operation() {
    }

    // Constructeur avec paramètres
    public Operation(double amount, String operationType, LocalDateTime date,
                     Account account, Account targetAccount) {
        this.amount = amount;
        this.operationType = operationType;
        this.date = date;
        this.account = account;
        this.targetAccount = targetAccount;

    }
}