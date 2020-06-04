package com.epam.lab.controller;

import com.epam.lab.dto.AuthorDto;
import com.epam.lab.exception.*;
import com.epam.lab.exception.ServiceException;
import com.epam.lab.service.AuthorService;
import org.apache.log4j.Level;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.http.MediaType;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.ConstraintViolationException;
import java.util.Arrays;
import java.util.List;


@RestController
@CrossOrigin(origins = {"http://localhost:8083", "http:/EPBYMINW8113.minsk.epam.com:8083"})
@RequestMapping(value = "/authors", produces = MediaType.APPLICATION_JSON_VALUE)
public class AuthorController {
    private static final Logger LOGGER = LogManager.getLogger(AuthorController.class);

   private AuthorService authorService;

    @Autowired
    public AuthorController(AuthorService authorService) {
        this.authorService = authorService;
    }

    @PostMapping
    @Secured({"ROLE_USER", "ROLE_ADMIN"})
    public AuthorDto saveAuthor(@RequestBody AuthorDto authorDto) throws AuthorControllerException {
        try {
            return authorService.save(authorDto);
        } catch (ServiceException e) {
            throw new AuthorControllerException(authorDto
                    , ControllerMessager.getMessage("controller.author.save")
                    , e.getMessage());
        }
    }

    @PutMapping
    @Secured({"ROLE_USER", "ROLE_ADMIN"})
    public AuthorDto updateAuthor(@RequestBody AuthorDto authorDto) throws AuthorControllerException {
        try {
            return authorService.update(authorDto);
        } catch (ServiceException e) {
            throw new AuthorControllerException(authorDto
                    , ControllerMessager.getMessage("controller.author.update")
                    , e.getMessage());
        }
    }

    @DeleteMapping("/{id}")
    @Secured({"ROLE_ADMIN"})
    public boolean deleteAuthor(@PathVariable long id) throws AuthorControllerException {
        try {
            return authorService.delete(id);
        } catch (ServiceException e) {
            throw new AuthorControllerException(
                    ControllerMessager.getMessage("controller.author.delete") + ": "
                    + e.getMessage(), e, HttpServletResponse.SC_NOT_FOUND);
        }
    }

    @GetMapping("/{id}")
    @Secured({"ROLE_USER", "ROLE_ADMIN"})
    public AuthorDto getAuthorById(@PathVariable long id) {
        return authorService.getById(id);
    }

    @GetMapping
    @Secured({"ROLE_USER", "ROLE_ADMIN"})
    public List<AuthorDto> getAllAuthors() {
        return authorService.getAll();
    }

}
