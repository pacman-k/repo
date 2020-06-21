package com.chelcov.service;

import com.chelcov.models.LogRecord;

public interface FilterChain {
    void doFilter(LogRecord logRecord);
}
