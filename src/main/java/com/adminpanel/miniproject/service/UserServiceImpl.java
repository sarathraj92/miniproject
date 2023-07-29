package com.adminpanel.miniproject.service;


import com.adminpanel.miniproject.dto.UserDto;
import com.adminpanel.miniproject.entity.User;
import com.adminpanel.miniproject.repository.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class UserServiceImpl implements UserService {

    private UserRepository userRepository;
    private PasswordEncoder passwordEncoder;


    public UserServiceImpl(UserRepository userRepository,PasswordEncoder passwordEncoder
                           ) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;

    }

    @Override
    public void saveUser(UserDto userDto) {
        User user = new User();
        user.setName(userDto.getFirstName() + " " + userDto.getLastName());
        user.setEmail(userDto.getEmail());
        user.setPhoneNumber(userDto.getPhoneNumber());
        user.setGender(userDto.getGender());
        user.setPassword(passwordEncoder.encode(userDto.getPassword()));
        user.setRole("USER");


        userRepository.save(user);
    }

    @Override
    public User findByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    @Override
    public UserDto findById(int id) {

        User existing= userRepository.findById(id);
        UserDto userDto=new UserDto();
        userDto.setId(existing.getId());
        String[] name = existing.getName().split(" ");
        userDto.setFirstName(name[0]);
        userDto.setLastName(name[1]);
        userDto.setEmail(existing.getEmail());
        userDto.setPhoneNumber(existing.getPhoneNumber());
        userDto.setGender(existing.getGender());
        userDto.setRole(existing.getRole());


        return userDto;
    }

    @Override
    public List<UserDto> findAllByRole(String role) {
        List<User> users = userRepository.findAllByRole(role);
        return users.stream().map((user) -> convertEntityToDto(user))
                .collect(Collectors.toList());
    }

    @Override
    public User updateUser(UserDto user) {
        int id=user.getId();
        User existing=userRepository.findById(id);
        existing.setName(user.getFirstName() + " " + user.getLastName());
        existing.setEmail(user.getEmail());
        existing.setPhoneNumber(user.getPhoneNumber());
        existing.setGender(user.getGender());


        return userRepository.save(existing);
    }

    @Override
    public User deleteUserById(int id) {

        return userRepository.deleteById(id);
    }
    public List<UserDto> searchUser(String role,String name) {
        List<User> users = userRepository.findAllByRoleAndNameStartingWithIgnoreCase(role,name);
        return users.stream().map((user) -> convertEntityToDto(user))
                .collect(Collectors.toList());
    }


    private UserDto convertEntityToDto(User user) {
        UserDto userDto = new UserDto();
        userDto.setId(user.getId());
        String[] name = user.getName().split(" ");
        userDto.setFirstName(name[0]);
        userDto.setLastName(name[1]);
        userDto.setEmail(user.getEmail());
        userDto.setPhoneNumber(user.getPhoneNumber());
        userDto.setGender(user.getGender());
        userDto.setPassword(user.getPassword());
        return userDto;
    }

}

