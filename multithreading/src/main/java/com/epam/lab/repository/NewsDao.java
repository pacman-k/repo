package com.epam.lab.repository;

import com.epam.lab.model.News;

import javax.validation.Valid;
import java.util.List;

public interface NewsDao {
    News save(@Valid News news);
    List<News> saveAll(@Valid List<News> news);
    long getCountOfNews();
}
