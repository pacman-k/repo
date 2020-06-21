package com.chelcov.service;

import com.chelcov.models.LogRecord;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.log4j.Level;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.List;

public class FileHandler implements Runnable {
    private static final Logger LOGGER = LogManager.getLogger(FileHandler.class);

    private Path pathToFile;
    private FilterChain filterChain;
    private ObjectMapper objectMapper;

    FileHandler(Path pathToFile, FilterChain filterChain, ObjectMapper objectMapper) {
        this.pathToFile = pathToFile;
        this.filterChain = filterChain;
        this.objectMapper = objectMapper;
    }

    @Override
    public void run() {
        File file = new File(pathToFile.toString());
        try {
            List<LogRecord> logRecords = Arrays.asList(objectMapper.readValue(file, LogRecord[].class));
            logRecords.forEach(filterChain::doFilter);
        } catch (IOException e) {
            LOGGER.log(Level.ERROR, String.format("file \"%s\" cant be read", file), e);
        }


    }
}
