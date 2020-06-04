package com.epam.lab.service.impl;


import com.epam.lab.dto.AuthorDto;
import com.epam.lab.dto.NewsDto;
import com.epam.lab.dto.TagDto;
import com.epam.lab.exception.*;
import com.epam.lab.model.Author;
import com.epam.lab.model.News;
import com.epam.lab.model.Tag;
import com.epam.lab.repository.AuthorDao;
import com.epam.lab.repository.NewsDao;
import com.epam.lab.repository.TagDao;
import com.epam.lab.service.AuthorService;
import com.epam.lab.service.converter.DtoModelConverter;
import com.epam.lab.service.ServiceMessager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;


import java.util.List;
import java.util.stream.Collectors;

@Service
@Validated
public class AuthorServiceImpl implements AuthorService {


    private AuthorDao authorDao;
    private NewsDao newsDao;
    private TagDao tagDao;

    private DtoModelConverter<Author, AuthorDto> authorConverter;
    private DtoModelConverter<News, NewsDto> newsConverter;
    private DtoModelConverter<Tag, TagDto> tagConverter;

    @Autowired
    public AuthorServiceImpl(AuthorDao authorDao, NewsDao newsDao, TagDao tagDao,
                             DtoModelConverter<Author, AuthorDto> authorConverter,
                             DtoModelConverter<News, NewsDto> newsConverter, DtoModelConverter<Tag, TagDto> tagConverter) {
        this.authorDao = authorDao;
        this.newsDao = newsDao;
        this.tagDao = tagDao;
        this.authorConverter = authorConverter;
        this.newsConverter = newsConverter;
        this.tagConverter = tagConverter;
    }


    @Override
    @Transactional
    public AuthorDto save(AuthorDto authorDto) throws ServiceException {
        try {
            Long userId = authorDao.save(authorConverter.toModel(authorDto));
            return getById(userId);
        } catch (DAOException e) {
            throw new ServiceException(ServiceMessager.getMessage("service.common.save") + e.getMessage(), e);
        }
    }

    @Override
    @Transactional
    public AuthorDto update(AuthorDto authorDto) throws ServiceException {
        try {
            AuthorDto authorFromDB = getById(authorDto.getId());
            if (authorFromDB == null) {
                throw new ServiceException(ServiceMessager.getMessage("service.common.update")
                        + ServiceMessager.getMessage("service.author.id")
                        + authorDto.getId());
            }
            replaceEmptyFields(authorFromDB, authorDto);
            authorDao.update(authorConverter.toModel(authorDto));
            return getById(authorDto.getId());
        } catch (DAOException e) {
            throw new ServiceException(ServiceMessager.getMessage("service.common.update") + e.getMessage(), e);
        }
    }

    private void replaceEmptyFields(AuthorDto authorFromDb, AuthorDto updatedAuthor) {
        String name = isEmpty(updatedAuthor.getName()) ? authorFromDb.getName() : updatedAuthor.getName();
        String surname = isEmpty(updatedAuthor.getSurname()) ? authorFromDb.getSurname() : updatedAuthor.getSurname();
        updatedAuthor.setName(name);
        updatedAuthor.setSurname(surname);
    }

    @Override
    @Transactional
    public boolean delete(Long id) throws ServiceException {

        try {
            if (getById(id) == null) {
                throw new ServiceException(ServiceMessager.getMessage("service.common.delete")
                        + ServiceMessager.getMessage("service.author.id")
                        + id);
            }
            Author author = authorDao.getById(id);
            deleteAllAuthorsNews(author);
            return authorDao.delete(id);

        } catch (DAOException e) {
            throw new ServiceException(ServiceMessager.getMessage("service.common.delete") + e.getMessage(), e);
        }
    }

    private void deleteAllAuthorsNews(Author author) throws DAOException {
        for (Long id : author.getNewsIdList()) {
            newsDao.delete(id);
        }
    }

    @Override
    public AuthorDto getById(Long id) {
        Author author = authorDao.getById(id);
        return author == null ? null : parseAuthorToDto(author);
    }

    @Override
    public List<AuthorDto> getAll() {
        return authorDao.getAll().stream().map(this::parseAuthorToDto).collect(Collectors.toList());
    }

    private AuthorDto parseAuthorToDto(Author author) {
        AuthorDto authorDto = authorConverter.toDto(author);
        if (author.getNewsList().isEmpty()) {
            List<News> newsList = author.getNewsIdList().stream()
                    .map(newsDao::getById)
                    .collect(Collectors.toList());
            for (News news : newsList) {
                NewsDto newsDto = newsConverter.toDto(news);
                news.getTagIdList().stream()
                        .map(tagDao::getById)
                        .map(tagConverter::toDto)
                        .forEach(newsDto.getTagList()::add);
                authorDto.getNewsList().add(newsDto);
            }
            return authorDto;
        }
        List<News> newsList = author.getNewsList();
        for (News news : newsList) {
            NewsDto newsDto = newsConverter.toDto(news);
            news.getTags().stream()
                    .map(tagConverter::toDto)
                    .forEach(newsDto.getTagList()::add);
            authorDto.getNewsList().add(newsDto);
        }
        return authorDto;
    }

    private static boolean isEmpty(String str) {
        return str == null || str.trim().equals("");
    }

}
