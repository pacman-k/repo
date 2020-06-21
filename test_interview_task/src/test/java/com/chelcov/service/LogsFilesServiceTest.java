package com.chelcov.service;

import com.chelcov.Config;
import com.chelcov.models.FilterRecordModel;
import com.chelcov.models.LogRecord;
import com.chelcov.models.TimeUnit;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.log4j.Level;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.junit.After;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import static org.junit.Assert.*;


@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = {Config.class})
public class LogsFilesServiceTest {
    private static final Logger LOGGER = LogManager.getLogger(LogsFilesServiceTest.class);
    private static final Path OUTPUT_PATH = Paths.get("output_logs.txt");

    @Autowired
    LogsFilesService logsFilesService;
    @Autowired
    ObjectMapper objectMapper;

    @Test
    public void getAllLogsGroupedByDayAndUsername() throws IOException {
        LOGGER.log(Level.INFO, "-------------getAllLogsGroupedByDayAndUsername---------------");
        int countOfThreads = 5;
        FilterRecordModel filterRecordModel = FilterRecordModel.builder()
                .timeUnit(TimeUnit.MONTH)
                .byUsername(true)
                .countOfProcessedThreads(countOfThreads)
                .pathOfOutputFile(OUTPUT_PATH)
                .build();

        Path fileOfSpecifiedLogs = logsFilesService.getFileOfSpecifiedLogs(filterRecordModel);

        List<LogRecord> logRecords = getLogRecords(fileOfSpecifiedLogs);
        assertEquals(logRecords.size(), 25);
        LOGGER.log(Level.INFO, "-------------END---------------");
    }

    @Test
    public void getAllLogsGroupedByUsername() throws IOException {
        LOGGER.log(Level.INFO, "-------------getAllLogsGroupedByUsername---------------");
        int countOfThreads = 5;
        FilterRecordModel filterRecordModel = FilterRecordModel.builder()
                .byUsername(true)
                .countOfProcessedThreads(countOfThreads)
                .pathOfOutputFile(OUTPUT_PATH)
                .build();

        Path fileOfSpecifiedLogs = logsFilesService.getFileOfSpecifiedLogs(filterRecordModel);

        List<LogRecord> logRecords = getLogRecords(fileOfSpecifiedLogs);
        assertEquals(logRecords.size(), 25);
        LOGGER.log(Level.INFO, "-------------END---------------");

    }

    @Test
    public void getLogsSpecifiedByUsernameGroupedByHour() throws IOException {
        LOGGER.log(Level.INFO, "-------------getLogsSpecifiedByUsernameGroupedByHour---------------");
        int countOfThreads = 2;
        String username = "pacman_k";
        FilterRecordModel filterRecordModel = FilterRecordModel.builder()
                .timeUnit(TimeUnit.HOUR)
                .username(username)
                .countOfProcessedThreads(countOfThreads)
                .pathOfOutputFile(OUTPUT_PATH)
                .build();

        Path fileOfSpecifiedLogs = logsFilesService.getFileOfSpecifiedLogs(filterRecordModel);
        List<LogRecord> logRecords = getLogRecords(fileOfSpecifiedLogs);
        assertTrue(logRecords.stream().allMatch(logRecord ->
                username.equalsIgnoreCase(logRecord.getUsername())));
        assertEquals(logRecords.size(), 5);
        LOGGER.log(Level.INFO, "-------------END---------------");
    }

    @Test
    public void getLogsSpecifiedByPeriodGroupedByMonth() throws IOException {
        LOGGER.log(Level.INFO, "-------------getLogsSpecifiedByPeriodGroupedByMonth---------------");
        int countOfThreads = 1;
        long period = 946080000000L;//last 30 year
        FilterRecordModel filterRecordModel = FilterRecordModel.builder()
                .timeUnit(TimeUnit.MONTH)
                .periodTime(period)
                .countOfProcessedThreads(countOfThreads)
                .pathOfOutputFile(OUTPUT_PATH)
                .build();

        Path fileOfSpecifiedLogs = logsFilesService.getFileOfSpecifiedLogs(filterRecordModel);
        List<LogRecord> logRecords = getLogRecords(fileOfSpecifiedLogs);
        assertTrue(logRecords.stream().allMatch(logRecord ->
                new Date().getTime() - logRecord.getDate().getTime() <= period));
        LOGGER.log(Level.INFO, "-------------END---------------");
    }



    @After
    public void clean() throws IOException {
        Files.deleteIfExists(OUTPUT_PATH);
    }

    private List<LogRecord> getLogRecords(Path path) throws IOException {
        String allRecords = new String(Files.readAllBytes(path), StandardCharsets.UTF_8);
        String splitter = "}";
        String[] split = allRecords.split(splitter);
        return Arrays.stream(split).map(note -> note = note + splitter)
                .map(json -> {
                    try {
                        return objectMapper.readValue(json, LogRecord.class);
                    } catch (JsonProcessingException e) {
                        throw new RuntimeException(e);
                    }
                })
                .collect(Collectors.toList());
    }

}