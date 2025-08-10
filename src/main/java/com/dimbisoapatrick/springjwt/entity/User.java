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

    @Email(message = "Invalid email address")
    @NotBlank(message = "Email is mandatory")
    @Column(name = "email")
    private String email;

    @NotBlank(message = "Password cannot be blank")
    @Column(name = "password")
    private String password;
	
	@Column(name = "reset_token")
    private String resetToken;

    private String verificationToken;

	private boolean isVerified;

    public @NotEmpty String getUsername() {
        return username;
    }

    public @NotBlank(message = "Password cannot be blank") String getPassword() {
        return password;
    }

    public void setPassword(@NotBlank(message = "Password cannot be blank") String password) {
        this.password = password;
    }

    public @Email(message = "Invalid email address") @NotBlank(message = "Email is mandatory") String getEmail() {
        return email;
    }

    public boolean isVerified() {
        return isVerified;
    }

    public void setVerified(boolean verified) {
        isVerified = verified;
    }

    public String getVerificationToken() {
        return verificationToken;
    }

    public void setVerificationToken(String verificationToken) {
        this.verificationToken = verificationToken;
    }
}
