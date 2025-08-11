package com.dimbisoapatrick.springjwt.repository;

import com.dimbisoapatrick.springjwt.entity.Expense;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ExpenseRepository extends JpaRepository<Expense, Long> {

}
