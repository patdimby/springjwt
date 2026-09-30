package com.dimbisoapatrick.springjwt.entity;

import lombok.*;
import jakarta.validation.constraints.*;
import jakarta.persistence.*;


@Entity
@Data
@Setter
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "users", uniqueConstraints = @UniqueConstraint(columnNames = "email"))
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;    

    @Column(name = "name")
    @NotEmpty
    private String username;

    @Column(name = "email")
    @Email @NotBlank
    private String email;

    @Column(name = "password")
    @NotBlank
    private String password;
	
	@Column(name = "reset_token")
    private String resetToken;

    @Column(length = 1024)
    private String verificationToken;

	private boolean isVerified;

    private String userId;
}
