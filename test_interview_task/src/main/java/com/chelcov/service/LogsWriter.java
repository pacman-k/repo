package com.chelcov.service;

import com.chelcov.models.LogRecord;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.log4j.Level;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.util.Queue;

public class LogsWriter implements Target {

    private static final Logger LOGGER = LogManager.getLogger(LogsWriter.class);

    private Path outputPath;
    private Queue<LogRecord> logRecordsTreasure;
    private ObjectMapper objectMapper;

    LogsWriter(Path outputPath, Queue<LogRecord> logRecordsTreasure, ObjectMapper objectMapper) {
        this.outputPath = outputPath;
        this.logRecordsTreasure = logRecordsTreasure;
        this.objectMapper = objectMapper;
    }

    @Override
    public void execute(LogRecord logRecord) {
        try {
            Files.write(outputPath, objectMapper.writeValueAsBytes(logRecord), StandardOpenOption.APPEND);
            logRecordsTreasure.add(logRecord);
        } catch (IOException e) {
            LOGGER.log(Level.ERROR, String.format("record \" %s \" cant be save in file", logRecord), e);
        }
    }
}
