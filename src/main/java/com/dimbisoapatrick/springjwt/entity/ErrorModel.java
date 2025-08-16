package com.dimbisoapatrick.springjwt.entity;

import lombok.*;

@Data
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ErrorModel {
    public String code;
    public String message;
}
