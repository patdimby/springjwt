package com.dimbisoapatrick.springjwt.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;

@Builder
@Getter
@Setter
@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserDTO {

    private Long id;

    @NotBlank(message = "User name should not be null")
    private String username;

    @Email(message = "Invalid email address")
    private String email;

    @NotBlank(message = "Password should not be null")
    @Size(min = 5, message = "Password should be minimum {min} characters")
    private String password;

     private String resetToken;

    private String verificationToken;

	private boolean isVerified;

    
}
