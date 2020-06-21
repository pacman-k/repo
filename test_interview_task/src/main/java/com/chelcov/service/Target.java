package com.chelcov.service;

import com.chelcov.models.LogRecord;

public interface Target {
    void execute(LogRecord logRecord);
}
