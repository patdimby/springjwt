package com.dimbisoapatrick.springjwt.controller;

import com.dimbisoapatrick.springjwt.service.ExpenseService;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;

import org.springframework.web.bind.annotation.RequestMapping;

@CrossOrigin
@Controller
@RequestMapping("/req")
@AllArgsConstructor
@Tag(name = "expenses")
public class ExpenseController {

	@Autowired
	private final ExpenseService expenseService;
	
	@GetMapping("/expenses")
	public String showExpenseList(Model model) {
		model.addAttribute("expenses", expenseService.getAllExpenses());
		return "req/expenses-list";
	}
}






















