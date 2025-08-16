package com.dimbisoapatrick.springjwt.entity;

import lombok.*;
import jakarta.persistence.*;

import java.math.BigDecimal;
import java.sql.Date;

@Entity
@Table(name = "tbl_expenses")
@Data
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Expense {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	@Column(unique = true)
	private String expenseId;

	@Column(name="name")
	private String name;

	@Column(name="description")
	private String description;

	@Column(name="amount")
	private BigDecimal amount;

	@Column(name="local_date")
	private Date date;

}
