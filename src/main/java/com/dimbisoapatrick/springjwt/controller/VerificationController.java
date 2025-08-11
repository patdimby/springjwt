package com.dimbisoapatrick.springjwt.controller;

import com.dimbisoapatrick.springjwt.entity.User;
import com.dimbisoapatrick.springjwt.repository.UserRepository;
import com.dimbisoapatrick.springjwt.util.JwtTokenUtil;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/req")
@AllArgsConstructor
@Tag(name = "verification")
public class VerificationController {
    
    @Autowired
    private UserRepository userRepository;
    
    @Autowired
    private JwtTokenUtil jwtUtil;
     
    @GetMapping("/signup/verify")
    public ResponseEntity verifyEmail(@RequestParam("token") String token) {
        String emailString = jwtUtil.extractEmail(token);
        User user = userRepository.findByEmail(emailString);
        if (user == null || user.getVerificationToken() == null) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Token Expired!");
        }
        
        if (!jwtUtil.validateToken(token) || !user.getVerificationToken().equals(token)) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Token Expired!");
        }
        user.setVerificationToken(null);
        user.setVerified(true);  
        userRepository.save(user);
        
        return ResponseEntity.status(HttpStatus.CREATED).body("Email successfully verified!");
    }
    
}

