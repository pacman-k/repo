package com.chelcov.service;

import com.chelcov.models.LogRecord;
import com.chelcov.models.RecordValidator;

import java.util.List;

public class FilterChainImpl implements FilterChain {
    private List<RecordValidator> recordValidators;
    private Target target;

    FilterChainImpl(List<RecordValidator> recordValidators, Target target) {
        this.recordValidators = recordValidators;
        this.target = target;
    }

    @Override
    public void doFilter(LogRecord logRecord) {
        if (recordValidators.stream()
                .allMatch(recordValidator -> recordValidator.isValid(logRecord))) {
            target.execute(logRecord);
        }
    }
}
