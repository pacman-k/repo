package com.epam.lab.controller;

import com.epam.lab.exception.AuthenticationException;
import com.epam.lab.exception.ControllerException;
import com.epam.lab.model.AuthenticationRequest;
import com.epam.lab.model.AuthenticationResponse;
import com.epam.lab.service.UserService;
import com.epam.lab.util.JwtService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;


import javax.servlet.http.HttpServletResponse;


@RestController
@CrossOrigin(origins = {"http://localhost:8083", "http:/EPBYMINW8113.minsk.epam.com:8083"})
public class AuthenticationController {

    @Autowired
    private AuthenticationManager authenticationManager;
    @Autowired
    private UserService userService;
    @Autowired
    private JwtService jwtService;

    @PostMapping("/login")
    public ResponseEntity<AuthenticationResponse> createAuthenticationToken(@RequestBody AuthenticationRequest request) throws ControllerException {
        try {
            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(request.getLogin(), request.getPassword()));
        } catch (BadCredentialsException e) {
            throw new AuthenticationException(request, "wrong input", HttpServletResponse.SC_NOT_FOUND);
        }
        UserDetails userDetails = userService.loadUserByUsername(request.getLogin());
        String jwt = jwtService.generateToken(userDetails);
        return new ResponseEntity<>(new AuthenticationResponse(jwt), HttpStatus.OK);
    }

}
