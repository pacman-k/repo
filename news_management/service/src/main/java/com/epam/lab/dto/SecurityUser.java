package com.epam.lab.dto;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;

import java.util.stream.Collectors;

public class SecurityUser extends User {
    private static String ROLE_PREFIX = "ROLE_";
    public SecurityUser(UserDto userDto){
        super(userDto.getLogin(), userDto.getPassword(), userDto.getRoles().stream().map(str-> (GrantedAuthority) () -> ROLE_PREFIX + str).collect(Collectors.toList()));
    }

}
