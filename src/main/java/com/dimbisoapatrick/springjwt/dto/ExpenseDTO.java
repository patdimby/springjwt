package com.dimbisoapatrick.springjwt.dto;

import java.math.BigDecimal;
import java.sql.Date;

import lombok.*;

@Getter
@Setter
@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ExpenseDTO {
	
	private Long id;
	
	private String expenseId;
	
	private String name;
	
	private String description;
	
	private BigDecimal amount;
	
	private Date date;
	
	private String dateString; 

}
