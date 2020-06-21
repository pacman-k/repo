package com.chelcov.service;

import com.chelcov.models.FilterRecordModel;
import com.chelcov.models.LogRecord;
import com.chelcov.repository.LogsFilesDao;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.log4j.Level;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.io.PrintStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

@Service
public class LogsFilesService {
    private static final long WAITING_TIME = 3;
    private static final Logger LOGGER = LogManager.getLogger(LogsFilesService.class);

    private LogsFilesDao logsFilesDao;
    private ObjectMapper objectMapper;
    private PrintStream printStream;

    @Autowired
    public LogsFilesService(LogsFilesDao logsFilesDao, ObjectMapper objectMapper, PrintStream printStream) {
        this.logsFilesDao = logsFilesDao;
        this.objectMapper = objectMapper;
        this.printStream = printStream;
    }

    public Path getFileOfSpecifiedLogs(FilterRecordModel filterRecordModel) {
        ExecutorService executorService = Executors.newFixedThreadPool(filterRecordModel.getCountOfProcessedThreads());
        Path outputPath = generateOutputFile(filterRecordModel.getPathOfOutputFile().toAbsolutePath());
        Queue<LogRecord> validRecords = new ConcurrentLinkedQueue<>();

        Target target = new LogsWriter(outputPath, validRecords, objectMapper);
        FilterChain filterChain = new FilterChainImpl(filterRecordModel.getRecordsValidators(), target);

        logsFilesDao.getAllFiles().stream()
                .map(path -> new FileHandler(path, filterChain, objectMapper))
                .forEach(executorService::execute);
        executorService.shutdown();
        sleepUntilTerminated(executorService);
        new StatisticPrinter(printStream, filterRecordModel).printStatistic(validRecords);
        return outputPath;

    }

    private static Path generateOutputFile(Path path) {
        try {
            return Files.createFile(path);
        } catch (IOException e) {
            LOGGER.log(Level.ERROR, "cant generate file by path '" + path + "'", e);
            throw new ServiceException();
        }
    }

    private static void sleepUntilTerminated(ExecutorService executorService) {
        while (!executorService.isTerminated()) {
            try {
                TimeUnit.SECONDS.sleep(WAITING_TIME);
            } catch (InterruptedException e) {
                LOGGER.log(Level.INFO, "main thread waiting has been interrupted");
            }
        }
    }


}
