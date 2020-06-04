package com.epam.lab.service.generator;

import org.apache.log4j.Level;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.nio.file.FileVisitResult;
import java.nio.file.Path;
import java.nio.file.SimpleFileVisitor;
import java.util.concurrent.TimeUnit;

@Component
public class FilesFillerVisitor extends SimpleFileVisitor<Path> {

    private static final Logger LOGGER = LogManager.getLogger(FilesFillerVisitor.class);

    @Value("${PERIOD_TIME}")
    private double periodTime;

    private Environment environment;

    @Autowired
    public FilesFillerVisitor(Environment environment) {
        this.environment = environment;
    }


    @Override
    public FileVisitResult postVisitDirectory(Path dir, IOException exc) {
        try {
            TimeUnit.MILLISECONDS.sleep((long) (periodTime * 1000));
            new Thread(new FilesGenRunnable(environment, dir, LOGGER)).start();
        } catch (InterruptedException e) {
            LOGGER.log(Level.ERROR, e);
        }
        return FileVisitResult.CONTINUE;
    }

}
