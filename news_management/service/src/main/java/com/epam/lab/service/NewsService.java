package com.epam.lab.service;

import com.epam.lab.dto.NewsDto;
import com.epam.lab.dto.NewsSearchCriteriaDto;
import com.epam.lab.exception.ServiceException;
import com.epam.lab.service.validator.ValidDtoParam;

import javax.validation.constraints.NotNull;
import java.util.List;

public interface NewsService {
    NewsDto save(@ValidDtoParam(paramNames = {"title", "shortText", "fullText"}, message ="title, short text and full text must be input") NewsDto newsDto) throws ServiceException;

    NewsDto update(@ValidDtoParam(paramNames = {"id"}, message ="id must be input") NewsDto newsDto) throws ServiceException;

    boolean delete(@NotNull(message = "id must be input") Long id) throws ServiceException;

    NewsDto getById(@NotNull(message = "id must be input") Long id);

    List<NewsDto> getAll();

    long getNumberOfNews();

    List<NewsDto> getByCriteria(NewsSearchCriteriaDto newsSearchCriteriaDto);

    List<NewsDto> getAll(@NotNull String param, boolean trans);

    NewsDto addTagsToNews(@ValidDtoParam(paramNames = {"id"}, message ="news_id must be input") NewsDto newsDto) throws ServiceException;
}
