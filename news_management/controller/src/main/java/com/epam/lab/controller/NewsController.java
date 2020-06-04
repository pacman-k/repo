package com.epam.lab.controller;

import com.epam.lab.dto.NewsDto;
import com.epam.lab.dto.NewsSearchCriteriaDto;
import com.epam.lab.exception.*;
import com.epam.lab.exception.ServiceException;
import com.epam.lab.service.NewsService;
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
@RequestMapping(value = "/news", produces = MediaType.APPLICATION_JSON_VALUE)
public class NewsController {
    private static final Logger LOGGER = LogManager.getLogger(NewsController.class);

   private NewsService newsService;

    @Autowired
    public NewsController(NewsService newsService) {
        this.newsService = newsService;
    }

    @PostMapping
    @Secured({"ROLE_USER", "ROLE_ADMIN"})
    public NewsDto saveNews(@RequestBody NewsDto newsDto) throws NewsControllerException {
        try {
            return newsService.save(newsDto);
        } catch (ServiceException e) {
            throw new NewsControllerException(newsDto
                    , ControllerMessager.getMessage("controller.news.save")
                    , e.getMessage(), e);
        }
    }

    @PutMapping
    @Secured({"ROLE_USER", "ROLE_ADMIN"})
    public NewsDto updateNews(@RequestBody NewsDto newsDto) throws NewsControllerException {
        try {
            return newsService.update(newsDto);
        } catch (ServiceException e) {
            throw new NewsControllerException(newsDto
                    , ControllerMessager.getMessage("controller.news.update")
                    , e.getMessage(), e);
        }
    }

    @DeleteMapping("/{id}")
    @Secured({"ROLE_ADMIN"})
    public boolean deleteNews(@PathVariable long id, HttpServletResponse response) throws NewsControllerException {
        try {
            return newsService.delete(id);
        } catch (ServiceException e) {
            response.setStatus(HttpServletResponse.SC_NOT_FOUND);
            throw new NewsControllerException(
                    ControllerMessager.getMessage("controller.news.delete") + ": "
                            + e.getMessage(), e);
        }
    }

    @GetMapping("/{id}")
    public NewsDto getNewsById(@PathVariable long id) {
        return newsService.getById(id);
    }

    @GetMapping
    public List<NewsDto> getAllNews() {
        return newsService.getAll();
    }

    @GetMapping(value = "/count")
    public long getNumberOfNews() {
        return newsService.getNumberOfNews();
    }

    @GetMapping(value = "/searchByCriteria")
    public List<NewsDto> getNewsByCriteria(NewsSearchCriteriaDto newsSearchCriteriaDto) {
        return newsService.getByCriteria(newsSearchCriteriaDto);
    }

    @GetMapping(value = "/sort")
    public List<NewsDto> getSortedAllNews(@RequestParam(value = "param") String param,
                                          @RequestParam(value = "trans", required = false) boolean trans) {
        return newsService.getAll(param, trans);
    }

    @PutMapping(value = "/addTags")
    @Secured({"ROLE_USER", "ROLE_ADMIN"})
    public NewsDto addTagsToNews(@RequestBody NewsDto newsDto) throws NewsControllerException {
        try {
            return newsService.addTagsToNews(newsDto);
        } catch (ServiceException e) {
            throw new NewsControllerException(newsDto
                    , ControllerMessager.getMessage("controller.news.addTags")
                    , e.getMessage());
        }
    }

}
