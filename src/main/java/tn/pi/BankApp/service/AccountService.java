package tn.pi.BankApp.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tn.pi.BankApp.entities.Account;
import tn.pi.BankApp.entities.Client;
import tn.pi.BankApp.entities.Operation;
import tn.pi.BankApp.repository.AccountRepository;
import tn.pi.BankApp.repository.ClientRepository;
import tn.pi.BankApp.repository.OperationRepository;


import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Random;

@Service
@RequiredArgsConstructor
public class AccountService {
    private final AccountRepository accountRepository;
    private final ClientRepository clientRepository;
    private final OperationRepository operationRepository;
    private static final Random RANDOM = new Random();

    @Transactional
    public Account saveAccount(Account account) {
    //public Account saveAccount(Account account, Long clientId) {
        if (account.getAccountNumber() == null || !account.getAccountNumber().matches("\\d{9}")) {
            throw new IllegalArgumentException("Le numéro de compte doit contenir 9 chiffres");
        }

        return accountRepository.save(account);
    }

    @Transactional(readOnly = true)
    public List<Account> getAllAccounts() {
        return accountRepository.findAll();
    }

    @Transactional(readOnly = true)
    public List<Account> getAllAccountsByClient(Client client) {
        List<Account> allAccounts = accountRepository.findAll();
        List<Account> clientAccounts = new ArrayList<>(); ;
        for (Account account : allAccounts) {
            if (client.getId().equals(account.getClient().getId()) ){
                clientAccounts.add(account);
            }
        }

        return clientAccounts;
    }

    @Transactional(readOnly = true)
    public Optional<Account> getAccountById(Long id) {
        return accountRepository.findById(id);
    }

    @Transactional
    public void deleteAccount(Long id) {
        if (!accountRepository.existsById(id)) {
            throw new RuntimeException("Aucun compte trouvé avec l'ID: " + id);
        }
        accountRepository.deleteById(id);
    }

    @Transactional
    public Account createAccount(Account account, Long clientId) {
        return this.saveAccount(account);
    }

    @Transactional
    public void deposit(Long accountId, double amount) {
        Account account = accountRepository.findById(accountId)
                .orElseThrow(() -> new RuntimeException("Compte introuvable"));

        account.setBalance(account.getBalance() + amount);
        accountRepository.save(account);

        Operation operation = new Operation();
        operation.setOperationType("DEPOT"); // Utilisation directe de String
        operation.setAmount(amount);
        operation.setAccount(account);
        operation.setDate(LocalDateTime.now());
        operationRepository.save(operation);
    }

    @Transactional
    public void withdraw(Long accountId, double amount) {
        Account account = accountRepository.findById(accountId)
                .orElseThrow(() -> new RuntimeException("Compte introuvable"));

        if (account.getBalance() < amount) {
            throw new RuntimeException("Solde insuffisant");
        }

        account.setBalance(account.getBalance() - amount);
        accountRepository.save(account);

        Operation operation = new Operation();
        operation.setOperationType("RETRAIT"); // Utilisation directe de String
        operation.setAmount(amount);
        operation.setAccount(account);
        operation.setDate(LocalDateTime.now());
        operationRepository.save(operation);
    }

    @Transactional(readOnly = true)
    public List<Operation> getAccountOperations(Long accountId) {
        return operationRepository.findByAccountId(accountId);
    }

    public String generateUniqueAccountNumber() {
        String accountNumber;
        do {
            accountNumber = generateRandomAccountNumber();
        } while (accountRepository.existsByAccountNumber(accountNumber));

        return accountNumber;
    }
    private String generateRandomAccountNumber() {
        // 9-digit number with leading zeros if needed
        return String.format("%09d", RANDOM.nextInt(1_000_000_000));
    }
}