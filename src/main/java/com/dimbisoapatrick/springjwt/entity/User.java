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
    @NotNull
    private Long id;    

    @Column(name = "name")
    @NotEmpty
    private String username;

    @Column(name = "email")
    private String email;

    @Column(name = "password")
    private String password;
	
	@Column(name = "reset_token")
    private String resetToken;

    private String verificationToken;

	private boolean isVerified;

    private String userId;
}
