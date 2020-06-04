package com.epam.lab.service.generator;

import org.apache.log4j.Level;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.nio.file.*;

@Component
public class MainGenerator {
    private static final Logger LOGGER = LogManager.getLogger(MainGenerator.class);

    @Value("${SUBFOLDERS_COUNT}")
    private int subfoldersCount;
    @Value("${AVERAGE_DEPTH}")
    private int averageDepth;

    private FileVisitor<Path> pathFileVisitor;

    @Autowired
    public MainGenerator(FileVisitor<Path> pathFileVisitor) {
        this.pathFileVisitor = pathFileVisitor;
    }

    public Path generate(String sourceDirectoryName) {
        Path path = Paths.get(System.getProperty("user.dir") + FileSystems.getDefault().getSeparator() + sourceDirectoryName);
        try {
            Files.walkFileTree(path, new FilesDropperVisitor());
        } catch (NoSuchFileException e) {
            LOGGER.log(Level.INFO, "no source files directory for dropping");
        } catch (IOException e) {
            LOGGER.log(Level.ERROR, e);
        }
        try {
            FoldersGenerator.generateFoldersTree(path, subfoldersCount, averageDepth);
            Files.walkFileTree(path, pathFileVisitor);
        } catch (IOException e) {
            LOGGER.log(Level.ERROR, e);
        }
        return path;
    }


}
