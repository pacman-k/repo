package com.epam.lab.service;

import com.epam.lab.dto.Dto;

import java.util.Comparator;

public interface ComparatorFactory <D extends Dto> {
    Comparator<D> createComparator(String param, boolean trans);
}
