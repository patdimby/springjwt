package com.dimbisoapatrick.springjwt.mapper;

import com.dimbisoapatrick.springjwt.dto.ExpenseDTO;

import com.dimbisoapatrick.springjwt.entity.Expense;

import lombok.*;
import org.springframework.stereotype.Component;

@Getter
@Setter
@Builder
@Data
@AllArgsConstructor
@Component
public class ExpenseConverter {

    public Expense convertDtoToModel(ExpenseDTO expenseDto) {
        Expense expense = new Expense();
        expense.setId(expenseDto.getId());
		expense.setName(expenseDto.getName());
		expense.setDescription(expenseDto.getDescription());
		expense.setAmount(expenseDto.getAmount());
		expense.setDate(expenseDto.getDate());
        return expense;
    }

    public ExpenseDTO convertModelToDTO(Expense expense) {
        ExpenseDTO expenseDto = new ExpenseDTO();
        expenseDto.setId(expense.getId());
        expenseDto.setName(expense.getName());
        expenseDto.setDescription(expense.getDescription());
        expenseDto.setAmount(expense.getAmount());
        expenseDto.setDate(expense.getDate());
        return expenseDto;
    }
}
