package com.dimbisoapatrick.springjwt.controller;

import com.dimbisoapatrick.springjwt.entity.User;
import com.dimbisoapatrick.springjwt.repository.UserRepository;
import com.dimbisoapatrick.springjwt.service.EmailService;
import com.dimbisoapatrick.springjwt.util.JwtTokenUtil;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/req")
@AllArgsConstructor
@Tag(name = "registration")
public class RegistrationController {

    @Autowired
    private final UserRepository userRepository;

    @Autowired
    private final PasswordEncoder passwordEncoder;

    @Autowired
    private final EmailService emailService;

    @PostMapping(value = "/signup", consumes = "application/json")
    public ResponseEntity<String> createUser(@RequestBody User user) {

        User existingAppUser = userRepository.findByEmail(user.getEmail());

        if (existingAppUser != null) {
            if (existingAppUser.isVerified()) {
                return new ResponseEntity<>("User Already exist and Verified.", HttpStatus.BAD_REQUEST);
            } else {
                String verificationToken = JwtTokenUtil.generateToken(existingAppUser.getEmail());
                existingAppUser.setVerificationToken(verificationToken);
                userRepository.save(existingAppUser);
                //Send Email Code
                emailService.sendVerificationEmail(existingAppUser.getEmail(), verificationToken);
                return new ResponseEntity<>("Verification Email resent. Check your inbox", HttpStatus.OK);
            }
        }
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        String verificationToken = JwtTokenUtil.generateToken(user.getEmail());
        user.setVerificationToken(verificationToken);
        userRepository.save(user);
        //Send Email Code
        emailService.sendVerificationEmail(user.getEmail(), verificationToken);

        return new ResponseEntity<>("Registration successfull! Please Verify your Email", HttpStatus.OK);
    }

}
