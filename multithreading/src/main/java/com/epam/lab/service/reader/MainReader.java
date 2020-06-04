package com.epam.lab.service.reader;

import com.epam.lab.service.NewsService;
import org.apache.log4j.Level;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

@Component
public class MainReader {

    private static final String ERROR_FOLDER_NAME = "error";
    private static final Logger LOGGER = LogManager.getLogger(MainReader.class);

    private NewsService newsService;

    @Value("${SCAN_DELAY}")
    private double scanDelay;
    @Value("${FILES_COUNT}")
    private int filesCount;
    @Value("${SUBFOLDERS_COUNT}")
    private int subfoldersCount;
    @Value("${MONITORING_TIME_MIN}")
    private int monitoringTime;

    private ScheduledExecutorService executorService;

    @Autowired
    public MainReader(NewsService newsService) {
        this.newsService = newsService;
    }

    @PostConstruct
    private void init() {
        executorService = Executors.newScheduledThreadPool(filesCount * (subfoldersCount + 1));
    }

    public Path read(Path rootPath) {
        Path errorFolder = rootPath;
        try {
            errorFolder = createErrorFolder(rootPath);
            executorService.scheduleWithFixedDelay(new ReaderRunnable(rootPath, newsService, Thread.currentThread(), errorFolder)
                    , 0
                    , (long) (scanDelay * 1000)
                    , TimeUnit.MILLISECONDS);
            TimeUnit.MINUTES.sleep(monitoringTime);
            LOGGER.log(Level.INFO,"monitoring time is over");
        } catch (InterruptedException e) {
            LOGGER.log(Level.INFO, e.getMessage());
        } catch (IOException e) {
            LOGGER.log(Level.ERROR, e);
        }
        executorService.shutdown();
        return errorFolder;
    }

    private static Path createErrorFolder(Path path) throws IOException {
        return Files.createDirectory(Paths.get(path + FileSystems.getDefault().getSeparator() + ERROR_FOLDER_NAME));
    }
}
