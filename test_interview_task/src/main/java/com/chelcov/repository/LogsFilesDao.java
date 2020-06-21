package com.chelcov.repository;

import java.nio.file.Path;
import java.util.List;
import java.util.Optional;

public interface LogsFilesDao {
    Optional<Path> getFile(String fileName);
    List<Path> getAllFiles();
}
