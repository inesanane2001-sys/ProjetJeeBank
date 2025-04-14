package tn.pi.BankApp.repository;  // 1. Package en minuscules


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import tn.pi.BankApp.entities.Operation;

import java.util.List;
public interface OperationRepository extends JpaRepository<Operation, Long> {
    List<Operation> findByAccountId(Long accountId);

    @Query("SELECT o FROM Operation o WHERE o.account.id = :accountId OR o.targetAccount.id = :accountId ORDER BY o.date DESC")
    List<Operation> findAllByAccount(@Param("accountId") Long accountId);
}