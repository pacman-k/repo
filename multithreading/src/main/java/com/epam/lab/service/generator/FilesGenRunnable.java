package com.epam.lab.service.generator;

import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.springframework.core.env.Environment;

import java.io.IOException;
import java.nio.file.Path;
import java.util.Objects;

public class FilesGenRunnable implements Runnable {

    private Environment environment;
    private Path path;
    private Logger logger;

    FilesGenRunnable(Environment environment, Path path, Logger logger) {
        this.environment = environment;
        this.path = path;
        this.logger = logger;
    }

    @Override
    public void run() {
        int filesCount = Integer.parseInt(Objects.requireNonNull(environment.getProperty("FILES_COUNT")));
        int validFilesCount = getIntValue(environment, "VALID_FILES");
        int notValidJsonCount = getIntValue(environment, "NOT_VALID_JSON_FORMAT_FILES");
        int notValidFieldNameCount = getIntValue(environment, "NOT_VALID_FIELD_NAMES_FILES");
        int notValidBeanCount = getIntValue(environment, "NOT_VALID_BEAN_FILES");
        int notValidDBConstraintsCount = getIntValue(environment, "NOT_VALID_DB_CONSTRAINTS_FILES");

        int totalSum = validFilesCount + notValidJsonCount + notValidFieldNameCount + notValidBeanCount + notValidDBConstraintsCount;

        try {
            for (int i = 0; i < filesCount / totalSum; i++) {
                FilesGenerators.VALID.generate(path, validFilesCount);
                FilesGenerators.NOT_VALID_JSON.generate(path, notValidJsonCount);
                FilesGenerators.NOT_VALID_FIELD_NAME.generate(path, notValidFieldNameCount);
                FilesGenerators.NOT_VALID_BEAN.generate(path, notValidBeanCount);
                FilesGenerators.NOT_VALID_DB_CONSTR.generate(path, notValidDBConstraintsCount);
                logger.log(Level.INFO, Thread.currentThread().getName() + " has completed " + (i + 1) * totalSum + " files");
            }
        } catch (IOException e) {
            logger.log(Level.ERROR, "error in " + path, e);
        }

    }

    private static int getIntValue(Environment env, String name) {
        return Integer.parseInt(Objects.requireNonNull(env.getProperty(name)));
    }


}
