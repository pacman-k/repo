package com.epam.lab.controller;

import com.epam.lab.dto.UserDto;
import com.epam.lab.exception.*;
import com.epam.lab.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.util.List;

@RestController
@CrossOrigin(origins = {"http://localhost:8083", "http:/EPBYMINW8113.minsk.epam.com:8083"})
@RequestMapping(value = "/users", produces = MediaType.APPLICATION_JSON_VALUE)
public class UserController {

   private UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping
    public ResponseEntity<UserDto> saveUser(@RequestBody UserDto userDto) throws UserControllerException {
        try {
            if (userService.loadUserByUsername(userDto.getLogin()) != null){
                throw new UserControllerException(userDto, "user with such id is presented", HttpServletResponse.SC_CONFLICT);
            }
                return new ResponseEntity<>(userService.save(userDto), HttpStatus.OK);
        } catch (ServiceException e) {
            throw new UserControllerException(userDto
                    , ControllerMessager.getMessage("controller.user.save")
                    , e.getMessage());
        }
    }

    @PutMapping
    @Secured({"ROLE_ADMIN"})
    public UserDto updateUser(@RequestBody UserDto userDto) throws UserControllerException {
        try {
            return userService.update(userDto);
        } catch (ServiceException e) {
            throw new UserControllerException(userDto
                    , ControllerMessager.getMessage("controller.user.update")
                    , e.getMessage(), HttpServletResponse.SC_NOT_FOUND);
        }
    }

    @DeleteMapping("/{id}")
    @Secured({"ROLE_ADMIN"})
    public boolean deleteUser(@PathVariable long id) throws UserControllerException {
        try {
            return userService.delete(id);
        } catch (ServiceException e) {
            throw new UserControllerException(
                    ControllerMessager.getMessage("controller.user.delete") + ": "
                            + e.getMessage(), e, HttpServletResponse.SC_NOT_FOUND);
        }
    }

    @GetMapping("/{id}")
    @Secured({"ROLE_ADMIN"})
    public UserDto getUserById(@PathVariable long id) {
        return userService.getById(id);
    }

    @GetMapping
    @Secured({"ROLE_ADMIN"})
    public List<UserDto> getAllTags() {
        return userService.getAll();
    }

}
