package tn.pi.BankApp.entities;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "cartes")
@Data
public class Carte {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String carteNumber;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "client_id", nullable = false)
    private Client client;


    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "account_id", nullable = false)
    private Account account;

    private String accountNumber;


    @Enumerated(EnumType.STRING)
    private typesCarte type;

    public enum typesCarte {
        CLASSIQUE  , TECHNOLOGIQUE , VOYAGE
    }
}
