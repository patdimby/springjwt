package com.dimbisoapatrick.springjwt.service;

import java.math.BigDecimal;
import java.text.NumberFormat;
import java.text.ParseException;
import java.util.Locale;
import java.sql.Date;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import com.dimbisoapatrick.springjwt.dto.ExpenseFilterDTO;
import com.dimbisoapatrick.springjwt.util.DateTimeUtil;
import com.dimbisoapatrick.springjwt.dto.ExpenseDTO;
import com.dimbisoapatrick.springjwt.entity.Expense;
import com.dimbisoapatrick.springjwt.mapper.ExpenseConverter;
import com.dimbisoapatrick.springjwt.repository.ExpenseRepository;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Service
@AllArgsConstructor
@Getter
@Setter
@Data
public class ExpenseService {

    @Autowired
    private final ExpenseRepository expenseRepository;

    @Autowired
    private ExpenseConverter expenseConverter;

    public List<ExpenseDTO> getAllExpenses() {
        List<Expense> list = expenseRepository.findAll();
        List<ExpenseDTO> expenseList = list.stream().map(this::mapToDTO).collect(Collectors.toList());
        return expenseList;
    }

    private ExpenseDTO mapToDTO(Expense expense) {
        ExpenseDTO expenseDTO = expenseConverter.convertModelToDTO(expense);
        expenseDTO.setDateString(DateTimeUtil.convertDateToString(expenseDTO.getDate()));
        return expenseDTO;
    }

    public ExpenseDTO saveExpenseDetails(ExpenseDTO expenseDTO) throws ParseException {
        //map the DTO to entity
        Expense expense = mapToEntity(expenseDTO);
        //Save the entity to database
        expense = expenseRepository.save(expense);
        //map the entity to DTO
        return mapToDTO(expense);
    }

    private Expense mapToEntity(ExpenseDTO expenseDTO) throws ParseException {
        //map the DTO to entity
        Expense expense = expenseConverter.convertDtoToModel(expenseDTO);
        //TODO: generate the expense id
        if (expense.getId() == null) {
            expense.setExpenseId(UUID.randomUUID().toString());
        }
        //TODO: set the expense date
        expense.setDate(DateTimeUtil.convertStringToDate(expenseDTO.getDateString()));
        //return the expense entity
        return expense;
    }

    public void deleteExpense(String id) {
        Expense exitingExpense = getExpense(id);
        expenseRepository.delete(exitingExpense);
    }

    public ExpenseDTO getExpenseById(String id) {
        Expense exitingExpense = getExpense(id);
        return mapToDTO(exitingExpense);
    }

    public Expense getExpense(String id) {
        return expenseRepository.findByExpenseId(id).orElseThrow(() -> new RuntimeException("Expense not found for the id:" + id));
    }

    public List<ExpenseDTO> getFilteredExpenses(ExpenseFilterDTO expenseFilterDTO) throws ParseException {
        String keyword = expenseFilterDTO.getKeyword();
        String sortBy = expenseFilterDTO.getSortBy();
        String startDateString = expenseFilterDTO.getStartDate();
        String endDateString = expenseFilterDTO.getEndDate();

        Date startDate = !startDateString.isEmpty() ? DateTimeUtil.convertStringToDate(startDateString) : new Date(0);
        Date endDate = !endDateString.isEmpty() ? DateTimeUtil.convertStringToDate(endDateString) : new Date(System.currentTimeMillis());

        List<Expense> list = expenseRepository.findByNameContainingAndDateBetween(keyword, startDate, endDate);
        List<ExpenseDTO> filteredList = list.stream().map(this::mapToDTO).collect(Collectors.toList());
        if (sortBy.equals("date")) {
            //sort it by expense date
            filteredList.sort((o1, o2) -> o2.getDate().compareTo(o1.getDate()));
        } else {
            //sort it by expense amount
            filteredList.sort((o1, o2) -> o2.getAmount().compareTo(o1.getAmount()));
        }
        return filteredList;
    }

    public String totalExpenses(List<ExpenseDTO> expenses) {
        BigDecimal sum = new BigDecimal(0);
        BigDecimal total = expenses.stream().map(x -> x.getAmount().add(sum))
                .reduce(BigDecimal.ZERO, BigDecimal::add);
        NumberFormat format = NumberFormat.getCurrencyInstance(new Locale.Builder()
                .setLanguage("en").setRegion("US").setVariant("POSIX").build());
        return format.format(total).substring(2);
    }
}





















