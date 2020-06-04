package com.epam.lab.service.impl;

import com.epam.lab.dto.AuthorDto;
import com.epam.lab.dto.NewsDto;
import com.epam.lab.dto.NewsSearchCriteriaDto;
import com.epam.lab.dto.TagDto;
import com.epam.lab.exception.*;
import com.epam.lab.model.Author;
import com.epam.lab.model.News;
import com.epam.lab.model.NewsSearchCriteriaModel;
import com.epam.lab.model.Tag;
import com.epam.lab.service.ComparatorFactory;
import com.epam.lab.service.converter.DtoModelConverter;
import com.epam.lab.service.ServiceMessager;
import com.epam.lab.service.NewsService;
import com.epam.lab.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;

import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

@Service
@Validated
public class NewsServiceImpl implements NewsService {

    private NewsDao newsDao;
    private TagDao tagDao;
    private AuthorDao authorDao;

    private DtoModelConverter<Author, AuthorDto> authorConverter;
    private DtoModelConverter<News, NewsDto> newsConverter;
    private DtoModelConverter<Tag, TagDto> tagConverter;
    private DtoModelConverter<NewsSearchCriteriaModel, NewsSearchCriteriaDto> searchCriteriaConverter;


    private ComparatorFactory<NewsDto> comparatorFactory;
    private NewsSpecificationFactory newsSpecificationFactory;

    @Autowired
    public NewsServiceImpl(NewsDao newsDao, TagDao tagDao, AuthorDao authorDao,
                           DtoModelConverter<Author, AuthorDto> authorConverter, DtoModelConverter<News, NewsDto> newsConverter,
                           DtoModelConverter<Tag, TagDto> tagConverter, DtoModelConverter<NewsSearchCriteriaModel,
            NewsSearchCriteriaDto> searchCriteriaConverter, ComparatorFactory<NewsDto> comparatorFactory,
                           NewsSpecificationFactory newsSpecificationFactory) {
        this.newsDao = newsDao;
        this.tagDao = tagDao;
        this.authorDao = authorDao;
        this.authorConverter = authorConverter;
        this.newsConverter = newsConverter;
        this.tagConverter = tagConverter;
        this.searchCriteriaConverter = searchCriteriaConverter;
        this.comparatorFactory = comparatorFactory;
        this.newsSpecificationFactory = newsSpecificationFactory;
    }

    @Override
    @Transactional
    public NewsDto save(NewsDto newsDto) throws ServiceException {
        try {
            News news = newsConverter.toModel(newsDto);
            if (authorDao.getById(news.getAuthorId()) == null) {
                throw new ServiceException(ServiceMessager.getMessage("service.common.save")
                        + ServiceMessager.getMessage("service.author.id")
                        + news.getAuthorId());
            }
            news.setCreationDate(new Date());
            news.setModificationDate(new Date());
            List<Tag> tagList = newsDto.getTagList().stream().map(tagConverter::toModel).collect(Collectors.toList());
            for (Tag tag : tagList) {
                news.getTagIdList().add(tagDao.save(tag));
            }
            news.setAuthorId(newsDto.getAuthorDto().getId());
            Long newsId = newsDao.save(news);
            return getById(newsId);
        } catch (DAOException e) {
            throw new ServiceException(ServiceMessager.getMessage("service.common.save") + e.getMessage(), e);
        }
    }

    @Override
    @Transactional
    public NewsDto update(NewsDto newsDto) throws ServiceException {
        try {
            NewsDto newsFromDB = getById(newsDto.getId());
            if (newsFromDB == null) {
                throw new ServiceException(ServiceMessager.getMessage("service.common.update")
                        + ServiceMessager.getMessage("service.news.id")
                        + newsDto.getId());
            }
            replaceEmptyFields(newsFromDB, newsDto);
            newsDto.setModificationDate(LocalDate.now());
            newsDao.update(newsConverter.toModel(newsDto));
            return getById(newsDto.getId());
        } catch (DAOException e) {
            throw new ServiceException(ServiceMessager.getMessage("service.common.update") + e.getMessage(), e);
        }
    }

    private void replaceEmptyFields(NewsDto newsFromDb, NewsDto updatedNews) {
        String title = isEmpty(updatedNews.getTitle()) ? newsFromDb.getTitle() : updatedNews.getTitle();
        String shortText = isEmpty(updatedNews.getShortText()) ? newsFromDb.getShortText() : updatedNews.getShortText();
        String fullText = isEmpty(updatedNews.getFullText()) ? newsFromDb.getFullText() : updatedNews.getFullText();
        updatedNews.setTitle(title);
        updatedNews.setShortText(shortText);
        updatedNews.setFullText(fullText);
        updatedNews.setCreationDate(newsFromDb.getCreationDate());
    }

    @Override
    @Transactional
    public boolean delete(Long id) throws ServiceException {
        try {
            if (getById(id) == null) {
                throw new ServiceException(ServiceMessager.getMessage("service.common.delete")
                        + ServiceMessager.getMessage("service.news.id")
                        + id);
            }
            return newsDao.delete(id);
        } catch (DAOException e) {
            throw new ServiceException(ServiceMessager.getMessage("service.common.delete")
                    + e.getMessage(), e);
        }
    }

    @Override
    public NewsDto getById(Long id) {
        News news = newsDao.getById(id);
        return news == null ? null : parseNewsToDto(news);
    }

    @Override
    public List<NewsDto> getAll() {
        return newsDao.getAll().stream()
                .map(this::parseNewsToDto)
                .collect(Collectors.toList());
    }

    @Override
    public long getNumberOfNews() {
        return newsDao.getNumberOfNews();
    }

    @Override
    public List<NewsDto> getByCriteria(NewsSearchCriteriaDto newsSearchCriteriaDto) {
        Specification<News> specification =
                newsSpecificationFactory.getNewsComplexSpecification(searchCriteriaConverter.toModel(newsSearchCriteriaDto));
        try {
            return newsDao.searchBySpecification(specification).stream().map(this::parseNewsToDto).collect(Collectors.toList());
        } catch (NotImplementedException e) {
            return getAll();
        }
    }

    @Override
    public List<NewsDto> getAll(String param, boolean trans) {
        List<NewsDto> newsList = getAll();
        newsList.sort(comparatorFactory.createComparator(param, trans));
        return newsList;
    }

    @Override
    @Transactional
    public NewsDto addTagsToNews(NewsDto newsDto) throws ServiceException {
        try {
            News news = newsConverter.toModel(newsDto);
            news.setModificationDate(new Date());
            List<Tag> tagList = newsDto.getTagList().stream().map(tagConverter::toModel).collect(Collectors.toList());
            for (Tag tag : tagList) {
                news.getTagIdList().add(tagDao.save(tag));
            }
            newsDao.addTagsToNews(news);
            return getById(news.getId());
        } catch (DAOException e) {
            throw new ServiceException(ServiceMessager.getMessage("service.news.addTags")
                    + e.getMessage(), e);
        }
    }

    private NewsDto parseNewsToDto(News news) {
        NewsDto newsDto = newsConverter.toDto(news);
        if (news.getTags().isEmpty() && news.getAuthors().isEmpty()) {

            newsDto.setAuthorDto(authorConverter.toDto(authorDao.getById(news.getAuthorId())));
            news.getTagIdList().stream()
                    .map(tagDao::getById)
                    .map(tagConverter::toDto)
                    .forEach(newsDto.getTagList()::add);
            return newsDto;
        }
        newsDto.setAuthorDto(news.getAuthors().stream()
                .map(authorConverter::toDto)
                .findFirst()
                .orElse(null));
        newsDto.setTagList(news.getTags().stream()
                .map(tagConverter::toDto)
                .collect(Collectors.toList()));
        return newsDto;
    }

    private static boolean isEmpty(String str) {
        return str == null || str.trim().equals("");
    }
}
