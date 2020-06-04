package com.epam.lab.repository;

import com.epam.lab.exception.DAOException;
import com.epam.lab.model.News;

import javax.validation.constraints.NotNull;
import java.util.List;

public interface NewsDao extends DaoCrud<News,Long> {
    long getNumberOfNews();
    boolean addTagsToNews(@NotNull News news) throws DAOException;
    List<News> searchBySpecification(@NotNull(message = "specification must not be null") Specification<News> specification);
}
