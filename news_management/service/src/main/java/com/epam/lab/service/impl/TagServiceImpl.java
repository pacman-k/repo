package com.epam.lab.service.impl;

import com.epam.lab.dto.NewsDto;
import com.epam.lab.dto.TagDto;
import com.epam.lab.exception.*;
import com.epam.lab.model.News;
import com.epam.lab.model.Tag;
import com.epam.lab.repository.NewsDao;
import com.epam.lab.repository.TagDao;
import com.epam.lab.service.converter.DtoModelConverter;
import com.epam.lab.service.ServiceMessager;
import com.epam.lab.service.TagService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Validated
public class TagServiceImpl implements TagService {

    private TagDao tagDao;
    private NewsDao newsDao;

    private DtoModelConverter<News, NewsDto> newsConverter;
    private DtoModelConverter<Tag, TagDto> tagConverter;

    @Autowired
    public TagServiceImpl(TagDao tagDao, NewsDao newsDao, DtoModelConverter<News,
            NewsDto> newsConverter, DtoModelConverter<Tag, TagDto> tagConverter) {
        this.tagDao = tagDao;
        this.newsDao = newsDao;
        this.newsConverter = newsConverter;
        this.tagConverter = tagConverter;
    }

    @Override
    @Transactional
    public TagDto save(TagDto tagDto) throws ServiceException {
        try {
            tagDto.setName(tagDto.getName().toLowerCase());
            Long tagId = tagDao.save(tagConverter.toModel(tagDto));
            return getById(tagId);
        } catch (DAOException e) {
            throw new ServiceException(ServiceMessager.getMessage("service.common.save")
                    + e.getMessage());
        }
    }

    @Override
    @Transactional
    public TagDto update(TagDto tagDto) throws ServiceException {
        try {
            TagDto tagFromDB = getById(tagDto.getId());
            if (tagFromDB == null) {
                throw new ServiceException(ServiceMessager.getMessage("service.common.update")
                        + ServiceMessager.getMessage("service.tag.id")
                        + tagDto.getId());
            }
            replaceEmptyFields(tagFromDB, tagDto);
            tagDto.setName(tagDto.getName().toLowerCase());
            tagDao.update(tagConverter.toModel(tagDto));
            return tagDto;
        } catch (DAOException e) {
            throw new ServiceException(ServiceMessager.getMessage("service.common.update")
                    + e.getMessage());
        }
    }
    private void replaceEmptyFields(TagDto tagFromDb, TagDto updatedTag){
        String name = isEmpty(updatedTag.getName()) ? tagFromDb.getName() : updatedTag.getName();
        updatedTag.setName(name);
    }

    @Override
    @Transactional
    public boolean delete(Long id) throws ServiceException {
        try {
            if (getById(id) == null) {
                throw new ServiceException(ServiceMessager.getMessage("service.common.delete")
                        + ServiceMessager.getMessage("service.tag.id")
                        + id);
            }
            return tagDao.delete(id);
        } catch (DAOException e) {
            throw new ServiceException(ServiceMessager.getMessage("service.common.delete")
                    + e.getMessage());
        }
    }

    @Override
    public TagDto getById(Long id) {
        Tag tag = tagDao.getById(id);
        return tag == null ? null : parseTagToDto(tag);
    }

    @Override
    public List<TagDto> getAll() {
        return tagDao.getAll().stream().map(this::parseTagToDto).collect(Collectors.toList());
    }

    private TagDto parseTagToDto(Tag tag) {
        TagDto tagDto = tagConverter.toDto(tag);
        if (tag.getNewsList().isEmpty()) {
            tag.getNewsIdList().stream()
                    .map(newsDao::getById)
                    .map(newsConverter::toDto)
                    .forEach(tagDto.getNewsList()::add);
            return tagDto;
        }
        tag.getNewsList().stream().map(newsConverter::toDto).forEach(tagDto.getNewsList()::add);
        return tagDto;
    }

    private static boolean isEmpty(String str) {
        return str == null || str.trim().equals("");
    }
}
