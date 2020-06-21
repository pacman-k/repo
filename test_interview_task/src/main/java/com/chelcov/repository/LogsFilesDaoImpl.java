package com.chelcov.repository;

import org.apache.log4j.Level;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Repository;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Repository
public class LogsFilesDaoImpl implements LogsFilesDao {
    private static final Logger LOGGER = LogManager.getLogger(LogsFilesDaoImpl.class);

    private Path logsSourceDirectory;

    public LogsFilesDaoImpl(@Value("${LOGS_SOURCE_DIRECTORY}") Path path) {
        this.logsSourceDirectory = path.toAbsolutePath();
        if (!Files.isDirectory(this.logsSourceDirectory)) {
            LOGGER.log(Level.ERROR, "Path need to be an existed directory '" + path + "'");
            throw new RepositoryException("Path need to be an existed directory");
        }
    }

    @Override
    public Optional<Path> getFile(String fileName) {
        try {
            return Files.find(logsSourceDirectory
                    , 1
                    , (path, basicFileAttributes) ->
                            Files.isRegularFile(path) && String.valueOf(path).endsWith(fileName))
                    .findFirst();
        } catch (IOException e) {
            LOGGER.log(Level.ERROR, "error during looking for file '" + fileName + "'", e);
            throw new RepositoryException();
        }
    }

    @Override
    public List<Path> getAllFiles() {
        try {
            return Files.find(logsSourceDirectory
                    , 1,
                    ((path, basicFileAttributes) ->
                            Files.isRegularFile(path)))
                    .collect(Collectors.toList());
        } catch (IOException e) {
            LOGGER.log(Level.ERROR, "error during getting all files", e);
            throw new RepositoryException();
        }
    }


}
