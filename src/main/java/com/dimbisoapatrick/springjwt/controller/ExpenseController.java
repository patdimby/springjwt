package com.dimbisoapatrick.springjwt.controller;

import java.text.ParseException;
import java.util.List;

import com.dimbisoapatrick.springjwt.dto.ExpenseDTO;
import com.dimbisoapatrick.springjwt.dto.ExpenseFilterDTO;
import com.dimbisoapatrick.springjwt.service.ExpenseService;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

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
		List<ExpenseDTO> list = expenseService.getAllExpenses();
		model.addAttribute("expenses", list);
		model.addAttribute("filter", new ExpenseFilterDTO());
		String totalExpenses = expenseService.totalExpenses(list);
		model.addAttribute("totalExpenses", totalExpenses);
		return "req/expenses-list";
	}
	@GetMapping("/createExpense")
	public String showExpenseForm(Model model) {
		model.addAttribute("expense", new ExpenseDTO());
		return "req/expense-form";
	}

	@PostMapping("/saveOrUpdateExpense")
	public String saveOrUpdateExpenseDetails(@ModelAttribute("expense") ExpenseDTO expneseDTO) throws ParseException {
		System.out.println("Printing the Expense DTO: "+expneseDTO);
		expenseService.saveExpenseDetails(expneseDTO);
		return "redirect:/expenses";
	}

	@GetMapping("/deleteExpense")
	public String deleteExpense(@RequestParam String id) {
		System.out.println("Printing the expense Id:"+id);
		expenseService.deleteExpense(id);
		return "redirect:/expenses";
	}

	@GetMapping("/updateExpense")
	public String updateExpense(@RequestParam String id, Model model) {
		System.out.println("Printing the expense Id inside update method:"+id);
		ExpenseDTO expense = expenseService.getExpenseById(id);
		model.addAttribute("expense", expense);
		return "req/expense-form";
	}
}






















