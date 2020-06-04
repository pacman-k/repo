package com.epam.lab.controller;

import com.epam.lab.dto.TagDto;
import com.epam.lab.exception.*;
import com.epam.lab.exception.ServiceException;
import com.epam.lab.service.TagService;
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
@RequestMapping(value = "/tags", produces = MediaType.APPLICATION_JSON_VALUE)
public class TagController {
    private static final Logger LOGGER = LogManager.getLogger(TagController.class);

   private TagService tagService;

    @Autowired
    public TagController(TagService tagService) {
        this.tagService = tagService;
    }

    @PostMapping
    @Secured({"ROLE_USER", "ROLE_ADMIN"})
    public TagDto saveTag(@RequestBody TagDto tagDto) throws TagControllerException {
        try {
            return tagService.save(tagDto);
        } catch (ServiceException e) {
            throw new TagControllerException(tagDto
                    , ControllerMessager.getMessage("controller.tag.save")
                    , e.getMessage());
        }
    }

    @PutMapping
    @Secured({"ROLE_USER", "ROLE_ADMIN"})
    public TagDto updateTag(@RequestBody TagDto tagDto) throws TagControllerException {
        try {
            return tagService.update(tagDto);
        } catch (ServiceException e) {
            throw new TagControllerException(tagDto
                    , ControllerMessager.getMessage("controller.tag.update")
                    , e.getMessage());
        }
    }

    @DeleteMapping("/{id}")
    @Secured({"ROLE_ADMIN"})
    public boolean deleteTag(@PathVariable long id, HttpServletResponse response) throws TagControllerException {
        try {
            return tagService.delete(id);
        } catch (ServiceException e) {
            response.setStatus(HttpServletResponse.SC_NOT_FOUND);
            throw new TagControllerException(
                     ControllerMessager.getMessage("controller.tag.delete") + ": "
                    + e.getMessage(), e);
        }
    }

    @GetMapping("/{id}")
    @Secured({"ROLE_USER", "ROLE_ADMIN"})
    public TagDto getTagById(@PathVariable long id) {
        return tagService.getById(id);
    }

    @GetMapping
    @Secured({"ROLE_USER", "ROLE_ADMIN"})
    public List<TagDto> getAllTags() {
        return tagService.getAll();
    }

}
