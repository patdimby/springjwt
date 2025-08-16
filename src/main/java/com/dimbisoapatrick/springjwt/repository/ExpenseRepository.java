package com.dimbisoapatrick.springjwt.repository;

import com.dimbisoapatrick.springjwt.entity.Expense;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.sql.Date;
import java.util.List;
import java.util.Optional;

@Repository
public interface ExpenseRepository extends JpaRepository<Expense, Long> {
    //SELECT * FROM tbl_expenses WHERE expenseId=?
    Optional<Expense> findByExpenseId(String id);

    //SELECT * FROM tbl_expenses WHERE name LIKE %keyword%
    List<Expense> findByNameContainingAndDateBetween(String keyword, Date startDate, Date endDate);
}
