package com.chelcov.service;

import com.chelcov.models.FilterRecordModel;
import com.chelcov.models.GroupingModel;
import com.chelcov.models.LogRecord;

import java.io.PrintStream;
import java.util.*;
import java.util.stream.Collectors;

class StatisticPrinter {
    private static final int TABLE_WIDTH = 90;
    private static final String NAME_OF_TABLE = "LogRecords Statistic";
    private static final String DATE_COLUMN = "Date";
    private static final String USERNAME_COLUMN = "Username";
    private static final String COUNT_OF_RECORDS_COLUMN = "Count of records";


    private PrintStream printStream;
    private FilterRecordModel filterRecordModel;
    private boolean isTitlePrinted;
    private int columnWidth;
    private boolean byUsername;
    private boolean byTimeUnit;

    StatisticPrinter(PrintStream printStream, FilterRecordModel filterRecordModel) {
        byUsername = filterRecordModel.isByUsername();
        byTimeUnit = Objects.nonNull(filterRecordModel.getTimeUnit());
        this.printStream = printStream;
        this.filterRecordModel = filterRecordModel;
        columnWidth = byUsername && byTimeUnit
                ? TABLE_WIDTH / 3
                : TABLE_WIDTH / 2;
    }

    void printStatistic(Collection<LogRecord> logRecords) {
        if (!(byUsername || byTimeUnit)) {
            printError("At least one grouping parameter need to be initialised");
            return;
        }
        if (logRecords.isEmpty()) {
            printError("no logRecords for statistic, " + new Date());
            return;
        }

        Map<GroupingModel, List<LogRecord>> gropedMap = getGropedMap(logRecords, filterRecordModel);
        gropedMap.entrySet().forEach(this::printSingleStat);

    }

    private static Map<GroupingModel, List<LogRecord>> getGropedMap(Collection<LogRecord> logRecords, FilterRecordModel model) {
        return logRecords.stream()
                .collect(Collectors.groupingBy(model::getGroupingModel));
    }

    private void printSingleStat(Map.Entry<GroupingModel, List<LogRecord>> groupingModelListEntry) {
        if (!isTitlePrinted) {
            printTitle();
            isTitlePrinted = true;
        }
        GroupingModel groupingModel = groupingModelListEntry.getKey();

        printDataLine(groupingModel.getUsername(), groupingModel.getDate(), Integer.toString(groupingModelListEntry.getValue().size()));

    }

    private void printTitle() {
        printStream.println(NAME_OF_TABLE + ", " + new Date());
        printHorizontalLine();
        printDataLine(USERNAME_COLUMN, DATE_COLUMN, COUNT_OF_RECORDS_COLUMN);
        printHorizontalLine();
    }

    private void printDataLine(String... values) {
        if (byUsername) {
            printStream.printf("%-" + columnWidth + "s%s", values[0], "|");
        }
        if (byTimeUnit) {
            printStream.printf("%-" + columnWidth + "s%s", values[1], "|");
        }
        printStream.printf("%-" + columnWidth + "s%n", values[2]);

    }

    private void printHorizontalLine() {
        for (int i = 1; i <= TABLE_WIDTH; i++) {
            printStream.print("-");
        }
        printStream.println();
    }

    private void printError(String message){
        printStream.println(message);
    }

}
