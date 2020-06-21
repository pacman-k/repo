package com.chelcov.models;

import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.nio.file.FileSystems;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.Objects;
import java.util.regex.Pattern;


@Data
@NoArgsConstructor
@Builder
public class FilterRecordModel {
    private static final int DEFAULT_THREADS_COUNT = 1;
    private static final Path DEFAULT_PATH_OF_OUTPUT_FILES = Paths.get("output_logs.txt");

    private String username;
    private long periodTime;
    private String messagePattern;

    private TimeUnit timeUnit;
    private boolean byUsername;

    private int countOfProcessedThreads;
    private Path pathOfOutputFile;

    {
        this.countOfProcessedThreads = DEFAULT_THREADS_COUNT;
        this.pathOfOutputFile = DEFAULT_PATH_OF_OUTPUT_FILES;
    }

    public FilterRecordModel(String username, long periodTime, String messagePattern,
                             TimeUnit timeUnit, boolean byUsername, int countOfProcessedThreads, Path pathOfOutputFile) {
        this.username = username;
        this.periodTime = periodTime;
        this.messagePattern = messagePattern;
        this.timeUnit = timeUnit;
        this.byUsername = byUsername;
        this.countOfProcessedThreads = countOfProcessedThreads;
        this.pathOfOutputFile = pathOfOutputFile;
    }

    public List<RecordValidator> getRecordsValidators() {
        List<RecordValidator> recordValidators = new LinkedList<>();
        if (Objects.nonNull(username)) {
            recordValidators.add(logRecord ->
                    username.equalsIgnoreCase(logRecord.getUsername()));
        }
        if (Objects.nonNull(messagePattern)) {
            recordValidators.add(logRecord ->
                    Pattern.matches(messagePattern, logRecord.getCustomMessage()));
        }
        if (periodTime != 0) {
            recordValidators.add(logRecord ->
                    new Date().getTime() - logRecord.getDate().getTime() <= periodTime);
        }
        return recordValidators;
    }

    public GroupingModel getGroupingModel(LogRecord logRecord) {
        if (byUsername && Objects.nonNull(timeUnit)) {
            return new GroupingModel(logRecord.getUsername(), timeUnit.getDateFormat().format(logRecord.getDate()));
        } else if (byUsername) {
            return new GroupingModel(logRecord.getUsername(), "");
        } else {
            return new GroupingModel("", Objects.nonNull(timeUnit)
                    ? timeUnit.getDateFormat().format(logRecord.getDate())
                    : "");
        }
    }
}
