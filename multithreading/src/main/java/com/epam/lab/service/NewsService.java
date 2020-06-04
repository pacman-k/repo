package com.epam.lab.service;

import com.epam.lab.model.News;
import com.epam.lab.repository.NewsDao;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.log4j.Level;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.persistence.PersistenceException;
import javax.validation.ConstraintViolationException;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.Arrays;

@Service
public class NewsService {

    private static final Logger LOGGER = LogManager.getLogger(NewsService.class);

    private ObjectMapper objectMapper;
    private NewsDao newsDao;

    @Autowired
    public NewsService(NewsDao newsDao) {
        this.newsDao = newsDao;
        this.objectMapper = new ObjectMapper();
    }

    public boolean saveNews(File newsFile) {

        try (BufferedReader bufferedReader = new BufferedReader(new FileReader(newsFile))) {
            News[] news = objectMapper.readValue(bufferedReader, News[].class);
            newsDao.saveAll(Arrays.asList(news));
            LOGGER.log(Level.INFO, String.format("%s was saved in data base", newsFile.getName()));
            return true;
        } catch (JsonMappingException | ConstraintViolationException | PersistenceException e) {
            LOGGER.log(Level.INFO, String.format("%s is marked as invalid", newsFile.getName()));
        } catch (IOException e) {
            LOGGER.log(Level.ERROR, e);
        }
        return false;

    }

    public long getCountOfNews() {
        return newsDao.getCountOfNews();
    }
}
