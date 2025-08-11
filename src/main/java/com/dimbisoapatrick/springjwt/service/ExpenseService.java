package com.dimbisoapatrick.springjwt.service;

import java.util.List;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import com.dimbisoapatrick.springjwt.dto.ExpenseDTO;
import com.dimbisoapatrick.springjwt.entity.Expense;
import com.dimbisoapatrick.springjwt.repository.ExpenseRepository;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


import lombok.RequiredArgsConstructor;

@Service
@AllArgsConstructor
public class ExpenseService {

	@Autowired
	private final ExpenseRepository expenseRepository;
	private final ModelMapper modelMapper;


	public List<ExpenseDTO> getAllExpenses() {
		List<Expense> list = expenseRepository.findAll();
		List<ExpenseDTO> expenseList = list.stream().map(this::mapToDTO).collect(Collectors.toList());
		return expenseList;
	}


	private ExpenseDTO mapToDTO(Expense expense) {
		ExpenseDTO expenseDTO = modelMapper.map(expense, ExpenseDTO.class);
		expenseDTO.setDateString(DateTimeUtil.convertDateToString(expenseDTO.getDate()));
		return expenseDTO;
	}
}





















