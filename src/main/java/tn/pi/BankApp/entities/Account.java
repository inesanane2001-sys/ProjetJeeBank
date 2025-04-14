package tn.pi.BankApp.entities;

import jakarta.persistence.*;
import lombok.Data;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Entity
@Table(name = "accounts")
@Data

public class Account {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false, length = 24)
    private String accountNumber;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private AccountType type;  // COURANT, EPARGNE, BUSINESS

    @Column(nullable = false, columnDefinition = "DECIMAL(15,2) DEFAULT 0.0")
    private Double balance = 0.0;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(nullable = false, updatable = false)
    private Date creationDate = new Date();

    // Côté enfant
    @ManyToOne
    @JoinColumn(name = "client_id")
    private Client client;


    @OneToMany(mappedBy = "account", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Credit> credits = new ArrayList<>();

    @OneToMany(mappedBy = "account", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Carte> cartes = new ArrayList<>();

    // Enumération des types de compte
    public enum AccountType {
        COURANT("Compte Courant"),
        EPARGNE("Compte Épargne"),
        BUSINESS("Compte Professionnel");

        private final String displayName;

        AccountType(String displayName) {
            this.displayName = displayName;
        }

        public String getDisplayName() {
            return displayName;
        }
    }


    // Exemple de méthode métier
    public void deposit(Double amount) {
        if (amount > 0) {
            this.balance += amount;
        }
    }
    @Override
    public String toString() {
        return "Account{" +
                "id=" + id +
                ", accountNumber='" + accountNumber + '\'' +
                ", type=" + type +
                ", balance=" + balance +
                ", clientId=" + (client != null ? client.getId() : "null") +
                '}';
    }
    private String email;  // Ajoutez ce champ

    // Ajoutez getter et setter
    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }


}