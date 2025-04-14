package tn.pi.BankApp.entities;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;

@Entity
@Table(name = "credits")
@Data
public class Credit {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "client_id", nullable = false)
    private Client client;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "account_id", nullable = false)
    private Account account;

    private String accountNumber;

    private String email;

    @Column(nullable = false)
    private Double salary;

    private Double amountRequested;

    @Column(name = "request_date")
    private LocalDateTime requestDate = LocalDateTime.now();

    @Enumerated(EnumType.STRING)
    private CreditStatus status = CreditStatus.PENDING;

    public enum CreditStatus {
        PENDING, APPROVED, REJECTED
    }

    public void setCreditStatus(CreditStatus creditStatus) {
        this.status = creditStatus ;
    }

}



