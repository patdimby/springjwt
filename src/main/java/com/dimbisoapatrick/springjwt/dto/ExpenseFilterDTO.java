package com.dimbisoapatrick.springjwt.dto;

import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class ExpenseFilterDTO {

    private String keyword;

    private String sortBy;

    private String startDate;

    private String endDate;
}
