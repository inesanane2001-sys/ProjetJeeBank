package tn.pi.BankApp.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tn.pi.BankApp.entities.Account;
import tn.pi.BankApp.entities.Operation;
import tn.pi.BankApp.repository.AccountRepository;
import tn.pi.BankApp.repository.OperationRepository;

import java.time.LocalDateTime;
import java.util.List;

@Service
@Transactional
public class OperationService {
    private final OperationRepository operationRepository;
    private final AccountRepository accountRepository;

    public OperationService(OperationRepository operationRepository,
                            AccountRepository accountRepository) {
        this.operationRepository = operationRepository;
        this.accountRepository = accountRepository;
    }

public Operation deposit(Long accountId, double amount) {
    Account account = accountRepository.findById(accountId)
            .orElseThrow(() -> new RuntimeException("Compte introuvable"));

    account.setBalance(account.getBalance() + amount);
    accountRepository.save(account);

    Operation operation = new Operation();
    operation.setAmount(amount); // Ceci est essentiel
    operation.setOperationType("DEPOSIT");
    operation.setDate(LocalDateTime.now());
    operation.setAccount(account);

    return operationRepository.save(operation);
}

    public Operation withdraw(Long accountId, double amount) {
        Account account = accountRepository.findById(accountId)
                .orElseThrow(() -> new RuntimeException("Compte introuvable"));

        if (account.getBalance() < amount) {
            throw new RuntimeException("Solde insuffisant");
        }

        account.setBalance(account.getBalance() - amount);
        accountRepository.save(account);

        Operation operation = new Operation();
        operation.setAmount(amount);
        operation.setOperationType("WITHDRAWAL");
        operation.setDate(LocalDateTime.now());
        operation.setAccount(account);

        return operationRepository.save(operation);
    }

    public Operation transfer(Long sourceAccountId, Long targetAccountId, double amount) {
        Account sourceAccount = accountRepository.findById(sourceAccountId)
                .orElseThrow(() -> new RuntimeException("Compte source introuvable"));
        Account targetAccount = accountRepository.findById(targetAccountId)
                .orElseThrow(() -> new RuntimeException("Compte cible introuvable"));

        if (sourceAccount.getBalance() < amount) {
            throw new RuntimeException("Solde insuffisant");
        }

        sourceAccount.setBalance(sourceAccount.getBalance() - amount);
        targetAccount.setBalance(targetAccount.getBalance() + amount);
        accountRepository.save(sourceAccount);
        accountRepository.save(targetAccount);

        Operation operation = new Operation();
        operation.setAmount(amount);
        operation.setOperationType("TRANSFER");
        operation.setDate(LocalDateTime.now());
        operation.setAccount(sourceAccount);
        operation.setTargetAccount(targetAccount);

        return operationRepository.save(operation);
    }

    public List<Operation> getAccountOperations(Long accountId) {
        return operationRepository.findByAccountId(accountId);
    }
}