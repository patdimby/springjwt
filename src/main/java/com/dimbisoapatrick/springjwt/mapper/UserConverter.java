package com.dimbisoapatrick.springjwt.mapper;

import com.dimbisoapatrick.springjwt.entity.User;
import com.dimbisoapatrick.springjwt.dto.UserDTO;


import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import lombok.Builder;
import org.springframework.stereotype.Component;

@Builder
@Getter
@Setter
@Data
@Component
public class UserConverter {

    public User convertDtoToModel(UserDTO userDto) {
        User user = new User();
        user.setId(userDto.getId());
        user.setUsername(userDto.getUsername());
        user.setEmail(userDto.getEmail());
        user.setPassword(userDto.getPassword());
        user.setResetToken(userDto.getResetToken());
        user.setVerificationToken(userDto.getVerificationToken());
        user.setVerified(userDto.isVerified());
        return user;
    }


}
