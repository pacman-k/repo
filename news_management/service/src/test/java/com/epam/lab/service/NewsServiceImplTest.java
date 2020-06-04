package com.epam.lab.service;

import com.epam.lab.configuration.ServiceTestConfig;
import com.epam.lab.dto.AuthorDto;
import com.epam.lab.dto.NewsDto;
import com.epam.lab.dto.TagDto;
import com.epam.lab.exception.DAOException;
import com.epam.lab.exception.ServiceException;
import com.epam.lab.model.Author;
import com.epam.lab.model.News;
import com.epam.lab.model.Tag;
import com.epam.lab.repository.AuthorDao;
import com.epam.lab.repository.NewsDao;
import com.epam.lab.repository.TagDao;
import com.epam.lab.service.converter.AuthorModelDtoConverter;
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

import static org.junit.Assert.*;

@RunWith(JUnit4.class)
public class NewsServiceImplTest {
    private static final Logger LOGGER = LogManager.getLogger(NewsServiceImplTest.class);
    private static NewsService newsService;
    private static DtoModelConverter<Author, AuthorDto> authorConverter;
    private static DtoModelConverter<Tag, TagDto> tagConverter;
    private static DtoModelConverter<News, NewsDto> newsConverter;
    private static NewsDao mockNewsDao;
    private static List<NewsDto> newsDtoList;
    private static List<NewsDto> newsDtoTestList;

    static {
        NewsDto newsNature = new NewsDto(1L, "nature", "nature nature",
                "nature nature nature", new ArrayList<>(), new AuthorDto(1L));

        NewsDto newsSport = new NewsDto(2L, "sport", "sport sport",
                "sport sport sport", new ArrayList<>(), new AuthorDto(2L, "Jony", "Boy"));

        NewsDto newsCriminal = new NewsDto(3L, "criminal", "criminal criminal",
                "criminal criminal criminal",
                Arrays.asList(new TagDto(1L, "criminal"), new TagDto(2L, "bad_guy"), new TagDto(3L, "guns")),
                new AuthorDto(3L, "Jud", "Low"));

        NewsDto newsPolicy = new NewsDto(4L, "policy", "policy policy",
                "policy policy policy", new ArrayList<>(), new AuthorDto(4L));

        newsDtoList = Arrays.asList(newsNature, newsSport, newsCriminal, newsPolicy);
    }

    @BeforeClass
    public static void init() throws DAOException {
        LOGGER.log(Level.INFO, "----------INIT METHOD FOR TEST NEWS SERVICE HAS STARTED----------");
        ApplicationContext ap = new AnnotationConfigApplicationContext(ServiceTestConfig.class);

        newsService = ap.getBean(NewsService.class);
        mockNewsDao = ap.getBean(NewsDao.class);
        Mockito.when(mockNewsDao.getById(Mockito.anyLong())).thenAnswer(invocationOnMock ->
        {
            Long id = invocationOnMock.getArgumentAt(0, Long.class);
            NewsDto newsDto = newsDtoTestList.stream().
                    filter(news -> news.getId().equals(id)).
                    findFirst().orElse(null);
            News news = newsConverter.toModel(newsDto);
            newsDto.getTagList().forEach(tag -> news.getTagIdList().add(tag.getId()));
            return news;
        });
        newsConverter = ap.getBean(NewsModelDtoConverter.class);

        AuthorDao mockAuthorDao = ap.getBean(AuthorDao.class);
        Mockito.when(mockAuthorDao.getById(Mockito.anyLong())).thenAnswer(invocationOnMock ->
        {
            Long id = invocationOnMock.getArgumentAt(0, Long.class);
            return authorConverter.toModel(newsDtoTestList.stream().
                    map(NewsDto::getAuthorDto).
                    filter(author -> author.getId().equals(id)).
                    findFirst().orElse(null));
        });
        authorConverter = ap.getBean(AuthorModelDtoConverter.class);

        TagDao mockTagDao = ap.getBean(TagDao.class);
        Mockito.when(mockTagDao.getById(Mockito.anyLong())).thenAnswer(invocationOnMock ->
        {
            Long id = invocationOnMock.getArgumentAt(0, Long.class);
            return tagConverter.toModel(newsDtoTestList.stream().
                    map(NewsDto::getTagList)
                    .map(list -> list.stream().
                            filter(tag -> tag.getId().equals(id)).
                            findFirst().orElse(null))
                    .filter(Objects::nonNull).findFirst().orElse(null));
        });
        tagConverter = ap.getBean(TagModelDtoConverter.class);
        Mockito.when(mockTagDao.save(Mockito.any(Tag.class))).thenAnswer(invocationOnMock -> invocationOnMock.getArgumentAt(0, Tag.class).getId());
        LOGGER.log(Level.INFO, "----------INIT METHOD HAS ENDED----------");
    }

    @Before
    public void insertList() {
        newsDtoTestList = new ArrayList<>(newsDtoList);
    }

    @After
    public void cleanList() {
        newsDtoTestList.clear();
    }


    @Test
    public void saveNews() throws DAOException, ServiceException {
        LOGGER.log(Level.INFO, "----------SAVE_NEWS METHOD HAS STARTED----------");
        Mockito.when(mockNewsDao.save(Mockito.any(News.class))).thenAnswer(invocationOnMock -> invocationOnMock.getArgumentAt(0, News.class).getId());
        for (NewsDto newsDto : newsDtoTestList) {
            NewsDto savedNews = newsService.save(newsDto);
            assertNotNull(savedNews);
            assertEquals(newsDto, savedNews);
        }
        LOGGER.log(Level.INFO, "----------SAVE_NEWS METHOD HAS ENDED----------");
    }

    @Test
    public void updateNews() throws DAOException, ServiceException {
        LOGGER.log(Level.INFO, "----------UPDATE_NEWS METHOD HAS STARTED----------");
        Mockito.when(mockNewsDao.update(Mockito.any(News.class))).thenAnswer(invocationOnMock -> {
            NewsDto newsForUpdateDto = newsConverter.toDto((News) invocationOnMock.getArguments()[0]);
            NewsDto oldNews = newsDtoTestList.stream()
                    .filter(newsDto -> newsDto.getId().equals(newsForUpdateDto.getId()))
                    .findFirst().orElseThrow(DAOException::new);
            oldNews.setTitle(newsForUpdateDto.getTitle());
            oldNews.setShortText(newsForUpdateDto.getShortText());
            oldNews.setFullText(newsForUpdateDto.getFullText());
            oldNews.setModificationDate(newsForUpdateDto.getModificationDate());
            return true;
        });
        NewsDto newsForUpdateNature = newsDtoTestList.get(3);
        NewsDto updatedNews = newsService.update(newsForUpdateNature);
        assertNotNull(updatedNews);
        assertEquals(newsForUpdateNature.getTitle(), updatedNews.getTitle());
        assertEquals(newsForUpdateNature.getFullText(), updatedNews.getFullText());
        assertEquals(newsForUpdateNature.getShortText(), updatedNews.getShortText());
        LOGGER.log(Level.INFO, "----------UPDATE_NEWS METHOD HAS ENDED----------");
    }

    @Test
    public void deleteNews() throws DAOException, ServiceException {
        LOGGER.log(Level.INFO, "----------DELETE_NEWS METHOD HAS STARTED----------");
        List<NewsDto> deletedNews = new ArrayList<>();
        Mockito.when(mockNewsDao.delete(Mockito.any(Long.class))).thenAnswer(invocationOnMock -> {
            Long newsId = (Long) invocationOnMock.getArguments()[0];
            newsDtoTestList.stream()
                    .filter(news -> news.getId().equals(newsId))
                    .forEach(deletedNews::add);

            return true;
        });
        int counter = 0;
        for (NewsDto newsDto : newsDtoTestList) {
            newsService.delete(newsDto.getId());
            assertEquals(++counter, deletedNews.size());
        }
        LOGGER.log(Level.INFO, "----------DELETE_NEWS METHOD HAS ENDED----------");
    }

    @Test
    public void getById() {
        LOGGER.log(Level.INFO, "----------GET_BY_ID METHOD HAS STARTED----------");
        List<Long> listId = newsDtoTestList.stream()
                .map(NewsDto::getId)
                .collect(Collectors.toList());
        List<NewsDto> gettedNews = listId.stream()
                .map(newsService::getById)
                .collect(Collectors.toList());
        assertEquals(newsDtoTestList, gettedNews);
        LOGGER.log(Level.INFO, "----------GET_BY_ID METHOD HAS ENDED----------");
    }

    @Test
    public void getAll() {
        LOGGER.log(Level.INFO, "----------GET_ALL METHOD HAS STARTED----------");
        Mockito.when(mockNewsDao.getAll()).thenReturn(newsDtoTestList.stream()
                .map(newsDto -> {
                    News news = newsConverter.toModel(newsDto);
                    newsDto.getTagList().forEach(tag -> news.getTagIdList().add(tag.getId()));
                    return news;
                }).collect(Collectors.toList()));
        assertEquals(newsDtoTestList, newsService.getAll());
        LOGGER.log(Level.INFO, "----------GET_ALL METHOD HAS ENDED----------");
    }

    @Test
    public void getNumberOfNews() {
        LOGGER.log(Level.INFO, "----------GET_NUMBER_OF_NEWS METHOD HAS STARTED----------");
        Mockito.when(mockNewsDao.getNumberOfNews()).thenReturn((long)newsDtoTestList.size());
        assertEquals(newsDtoTestList.size(), newsService.getNumberOfNews());
        LOGGER.log(Level.INFO, "----------GET_NUMBER_OF_NEWS METHOD HAS ENDED----------");
    }



}