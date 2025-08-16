package com.dimbisoapatrick.springjwt.service;

import java.util.Optional;
import java.util.UUID;

import com.dimbisoapatrick.springjwt.mapper.UserConverter;
import com.dimbisoapatrick.springjwt.dto.UserDTO;
import com.dimbisoapatrick.springjwt.repository.UserRepository;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import com.dimbisoapatrick.springjwt.entity.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
@NoArgsConstructor
public class UserService implements UserDetailsService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private UserConverter userconverter;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        Optional<User> user = userRepository.findByUsername(username);
        if (user.isPresent()) {
            var userObj = user.get();
            return org.springframework.security.core.userdetails.User.builder()
                    .username(userObj.getUsername())
                    .password(userObj.getPassword())
                    .build();
        } else {
            throw new UsernameNotFoundException(username);
        }
    }

    public void save(UserDTO userDTO) {
        User user = mapToEntity(userDTO);
        user.setUserId(UUID.randomUUID().toString());
        userRepository.save(user);
    }

    private User mapToEntity(UserDTO userDTO) {
        return userconverter.convertDtoToModel(userDTO);
    }

}
