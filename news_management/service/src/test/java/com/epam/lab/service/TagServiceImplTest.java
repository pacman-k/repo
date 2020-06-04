package com.epam.lab.service;

import com.epam.lab.configuration.ServiceTestConfig;
import com.epam.lab.dto.NewsDto;
import com.epam.lab.dto.TagDto;
import com.epam.lab.exception.DAOException;
import com.epam.lab.exception.ServiceException;
import com.epam.lab.model.News;
import com.epam.lab.model.Tag;
import com.epam.lab.repository.NewsDao;
import com.epam.lab.repository.TagDao;
import com.epam.lab.service.converter.DtoModelConverter;
import com.epam.lab.service.converter.NewsModelDtoConverter;
import com.epam.lab.service.converter.TagModelDtoConverter;
import org.apache.log4j.Level;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.junit.After;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;
import org.mockito.Mockito;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import java.util.*;
import java.util.stream.Collectors;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

@RunWith(JUnit4.class)
public class TagServiceImplTest {

    private static final Logger LOGGER = LogManager.getLogger(TagServiceImplTest.class);
    private static TagService tagService;
    private static DtoModelConverter<Tag, TagDto> tagConverter;
    private static DtoModelConverter<News, NewsDto> newsConverter;
    private static TagDao mockTagDao;
    private static List<TagDto> tagDtoList;
    private static List<TagDto> tagDtoTestList;

    static {
        TagDto tagSport = new TagDto(1L, "sport");

        TagDto tagCriminal = new TagDto(2L, "criminal");

        TagDto tagNature = new TagDto(3L, "nature");

        TagDto tagPolicy = new TagDto(4L, "policy", Arrays.asList(new NewsDto(1L), new NewsDto(2L)));

        tagDtoList = Arrays.asList(tagSport, tagCriminal, tagNature, tagPolicy);
    }

    @BeforeClass
    public static void init() {
        LOGGER.log(Level.INFO, "----------INIT METHOD FOR TEST TAG SERVICE HAS STARTED----------");
        ApplicationContext ap = new AnnotationConfigApplicationContext(ServiceTestConfig.class);

        tagService = ap.getBean(TagService.class);
        mockTagDao = ap.getBean(TagDao.class);
        Mockito.when(mockTagDao.getById(Mockito.anyLong())).thenAnswer(invocationOnMock ->
        {
            Long id = invocationOnMock.getArgumentAt(0, Long.class);
            TagDto tagDto = tagDtoTestList.stream().
                    filter(tag -> tag.getId().equals(id)).
                    findFirst().orElse(null);
            Tag tag = tagConverter.toModel(tagDto);
            tagDto.getNewsList().forEach(news -> tag.getNewsIdList().add(news.getId()));
            return tag;
        });
        tagConverter = ap.getBean(TagModelDtoConverter.class);

        NewsDao mockNewsDao = ap.getBean(NewsDao.class);
        Mockito.when(mockNewsDao.getById(Mockito.anyLong())).thenAnswer(invocationOnMock ->
        {
            Long id = invocationOnMock.getArgumentAt(0, Long.class);
            return newsConverter.toModel(tagDtoTestList.stream().
                    map(TagDto::getNewsList)
                    .map(list -> list.stream().
                            filter(news -> news.getId().equals(id)).
                            findFirst().orElse(null))
                    .filter(Objects::nonNull).findFirst().orElse(null));
        });
        newsConverter = ap.getBean(NewsModelDtoConverter.class);

        LOGGER.log(Level.INFO, "----------INIT METHOD HAS ENDED----------");
    }

    @Before
    public void insertList() {
        tagDtoTestList = new ArrayList<>(tagDtoList);
    }

    @After
    public void cleanList() {
        tagDtoTestList.clear();
    }

    @Test
    public void saveTag() throws DAOException, ServiceException {
        LOGGER.log(Level.INFO, "----------SAVE_TAG METHOD HAS STARTED----------");
        Mockito.when(mockTagDao.save(Mockito.any(Tag.class)))
                .thenAnswer(invocationOnMock -> invocationOnMock.getArgumentAt(0, Tag.class).getId());
        for (TagDto tagDto : tagDtoTestList) {
            TagDto savedTag = tagService.save(tagDto);
            assertNotNull(savedTag);
            assertEquals(tagDto, savedTag);
        }
        LOGGER.log(Level.INFO, "----------SAVE_TAG METHOD HAS ENDED----------");
    }

    @Test
    public void updateTag() throws DAOException, ServiceException {
        LOGGER.log(Level.INFO, "----------UPDATE_TAG METHOD HAS STARTED----------");
        Mockito.when(mockTagDao.update(Mockito.any(Tag.class))).thenAnswer(invocationOnMock -> {
            TagDto tagForUpdateDto = tagConverter.toDto((Tag) invocationOnMock.getArguments()[0]);
            TagDto oldTag = tagDtoTestList.stream()
                    .filter(tagDto -> tagDto.getId().equals(tagForUpdateDto.getId()))
                    .findFirst().orElseThrow(DAOException::new);
            oldTag.setName(tagForUpdateDto.getName());
            return true;
        });
        TagDto tagForUpdateSport = new TagDto(1L, "bloody sport");
        TagDto updatedTag = tagService.update(tagForUpdateSport);
        assertNotNull(updatedTag);
        assertEquals(tagForUpdateSport.getName(), updatedTag.getName());
        LOGGER.log(Level.INFO, "----------UPDATE_TAG METHOD HAS ENDED----------");
    }

    @Test
    public void deleteTag() throws DAOException, ServiceException {
        LOGGER.log(Level.INFO, "----------DELETE_TAG METHOD HAS STARTED----------");
        List<TagDto> deletedTags = new ArrayList<>();
        Mockito.when(mockTagDao.delete(Mockito.any(Long.class))).thenAnswer(invocationOnMock -> {
            Long tagId = (Long) invocationOnMock.getArguments()[0];
            tagDtoTestList.stream()
                    .filter(tag -> tag.getId().equals(tagId))
                    .forEach(deletedTags::add);

            return true;
        });
        int counter = 0;
        for (TagDto tagDto : tagDtoTestList) {
            tagService.delete(tagDto.getId());
            assertEquals(++counter, deletedTags.size());
        }
        LOGGER.log(Level.INFO, "----------DELETE_TAG METHOD HAS ENDED----------");
    }

    @Test
    public void getById() {
        LOGGER.log(Level.INFO, "----------GET_BY_ID METHOD HAS STARTED----------");
        List<Long> listId = tagDtoTestList.stream()
                .map(TagDto::getId)
                .collect(Collectors.toList());
        List<TagDto> gettedTags = listId.stream()
                .map(tagService::getById)
                .collect(Collectors.toList());
        assertEquals(tagDtoTestList, gettedTags);
        LOGGER.log(Level.INFO, "----------GET_BY_ID METHOD HAS ENDED----------");
    }

    @Test
    public void getAll() {
        LOGGER.log(Level.INFO, "----------GET_ALL METHOD HAS STARTED----------");
        Mockito.when(mockTagDao.getAll()).thenReturn(tagDtoTestList.stream()
                .map(tagDto -> {
                    Tag tag = tagConverter.toModel(tagDto);
                    tagDto.getNewsList().forEach(news -> tag.getNewsIdList().add(news.getId()));
                    return tag;
                }).collect(Collectors.toList()));
        assertEquals(tagDtoTestList, tagService.getAll());
        LOGGER.log(Level.INFO, "----------GET_ALL METHOD HAS ENDED----------");

    }
}