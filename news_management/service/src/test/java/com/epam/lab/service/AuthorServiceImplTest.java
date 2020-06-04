package com.epam.lab.service;

import com.epam.lab.configuration.ServiceTestConfig;
import com.epam.lab.dto.AuthorDto;
import com.epam.lab.dto.NewsDto;
import com.epam.lab.exception.DAOException;
import com.epam.lab.model.Author;
import com.epam.lab.model.News;
import com.epam.lab.repository.AuthorDao;
import com.epam.lab.repository.NewsDao;
import com.epam.lab.exception.ServiceException;
import com.epam.lab.service.converter.AuthorModelDtoConverter;
import com.epam.lab.service.converter.DtoModelConverter;
import com.epam.lab.service.converter.NewsModelDtoConverter;
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
public class AuthorServiceImplTest {
    private static final Logger LOGGER = LogManager.getLogger(AuthorServiceImplTest.class);
    private static AuthorService authorService;
    private static DtoModelConverter<Author, AuthorDto> authorConverter;
    private static DtoModelConverter<News, NewsDto> newsConverter;
    private static AuthorDao mockAuthorDao;
    private static List<AuthorDto> authorDtoList;
    private static List<AuthorDto> authorDtoTestList;

    static {
        AuthorDto authorStepa = new AuthorDto(1L, "Stepan", "Stepanovich",
                Arrays.asList(new NewsDto(1L), new NewsDto(2L)));

        AuthorDto authorRuslan = new AuthorDto(2L, "Ruslan", "Diga");

        AuthorDto authorSasha = new AuthorDto(3L, "Sasha", "Shnic",
                Collections.singletonList(new NewsDto(3L)));

        AuthorDto authorKurt = new AuthorDto(4L, "Kurt", "Cobain",
                Arrays.asList(new NewsDto(4L), new NewsDto(5L)));

        authorDtoList = Arrays.asList(authorStepa, authorRuslan, authorSasha, authorKurt);
    }

    @BeforeClass
    public static void init() {
        LOGGER.log(Level.INFO, "----------INIT METHOD FOR TEST AUTHOR SERVICE HAS STARTED----------");
        ApplicationContext ap = new AnnotationConfigApplicationContext(ServiceTestConfig.class);

        authorService = ap.getBean(AuthorService.class);
        mockAuthorDao = ap.getBean(AuthorDao.class);
        Mockito.when(mockAuthorDao.getById(Mockito.anyLong())).thenAnswer(invocationOnMock ->
        {
            Long id = invocationOnMock.getArgumentAt(0, Long.class);
            AuthorDto authorDto = authorDtoTestList.stream().
                    filter(author -> author.getId().equals(id)).
                    findFirst().orElse(null);
            Author author = authorConverter.toModel(authorDto);
            if (author != null) {
                authorDto.getNewsList().forEach(news -> author.getNewsIdList().add(news.getId()));
            }
            return author;
        });
        authorConverter = ap.getBean(AuthorModelDtoConverter.class);

        NewsDao mockNewsDao = ap.getBean(NewsDao.class);
        Mockito.when(mockNewsDao.getById(Mockito.anyLong())).thenAnswer(invocationOnMock ->
        {
            Long id = invocationOnMock.getArgumentAt(0, Long.class);
            return newsConverter.toModel(authorDtoTestList.stream().
                    map(AuthorDto::getNewsList)
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
        authorDtoTestList = new ArrayList<>(authorDtoList);
    }

    @After
    public void cleanList() {
        authorDtoTestList.clear();
    }


    @Test
    public void saveAuthor() throws DAOException, ServiceException {
        LOGGER.log(Level.INFO, "----------SAVE_AUTHOR METHOD HAS STARTED----------");
            Mockito.when(mockAuthorDao.save(Mockito.any(Author.class)))
                    .thenAnswer(invocationOnMock -> invocationOnMock.getArgumentAt(0, Author.class).getId());
            for (AuthorDto authorDto : authorDtoTestList) {
                AuthorDto savedAuthor = authorService.save(authorDto);
                assertNotNull(savedAuthor);
                assertEquals(authorDto, savedAuthor);
            }
        LOGGER.log(Level.INFO, "----------SAVE_AUTHOR METHOD HAS ENDED----------");
    }


    @Test
    public void updateAuthor() throws DAOException, ServiceException {
        LOGGER.log(Level.INFO, "----------UPDATE_AUTHOR METHOD HAS STARTED----------");
        Mockito.when(mockAuthorDao.update(Mockito.any(Author.class))).thenAnswer(invocationOnMock -> {
            AuthorDto authorForUpdateDto = authorConverter.toDto((Author) invocationOnMock.getArguments()[0]);
            AuthorDto oldAuthor = authorDtoTestList.stream()
                    .filter(authorDto -> authorDto.getId().equals(authorForUpdateDto.getId()))
                    .findFirst().orElseThrow(DAOException::new);
            oldAuthor.setName(authorForUpdateDto.getName());
            oldAuthor.setSurname(authorForUpdateDto.getSurname());
            return true;
        });
        AuthorDto authorForUpdateStepan = new AuthorDto(1L, "Alex", "Petrovich");
        AuthorDto updatedAuthor = authorService.update(authorForUpdateStepan);
        assertNotNull(updatedAuthor);
        assertEquals(authorForUpdateStepan.getName(), updatedAuthor.getName());
        assertEquals(authorForUpdateStepan.getSurname(), updatedAuthor.getSurname());
        LOGGER.log(Level.INFO, "----------UPDATE_AUTHOR METHOD HAS ENDED----------");
    }


    @Test
    public void deleteAuthor() throws DAOException, ServiceException {
        LOGGER.log(Level.INFO, "----------DELETE_AUTHOR METHOD HAS STARTED----------");
        List<AuthorDto> deletedAuthors = new ArrayList<>();
        Mockito.when(mockAuthorDao.delete(Mockito.any(Long.class))).thenAnswer(invocationOnMock -> {
            Long authorId = (Long) invocationOnMock.getArguments()[0];
            authorDtoTestList.stream()
                    .filter(author -> author.getId().equals(authorId))
                    .forEach(deletedAuthors::add);

            return true;
        });
        int counter = 0;
        for (AuthorDto authorDto : authorDtoTestList) {
            authorService.delete(authorDto.getId());
            assertEquals(++counter, deletedAuthors.size());
        }
        LOGGER.log(Level.INFO, "----------DELETE_AUTHOR METHOD HAS ENDED----------");
    }


    @Test
    public void getById() {
        LOGGER.log(Level.INFO, "----------GET_BY_ID METHOD HAS STARTED----------");
        List<Long> listId = authorDtoTestList.stream()
                .map(AuthorDto::getId)
                .collect(Collectors.toList());
        List<AuthorDto> gettedAuthors = listId.stream()
                .map(authorService::getById)
                .collect(Collectors.toList());
        assertEquals(authorDtoTestList, gettedAuthors);
        LOGGER.log(Level.INFO, "----------GET_BY_ID METHOD HAS ENDED----------");
    }

    @Test
    public void getByWrongId() {
        LOGGER.log(Level.INFO, "----------GET_BY_WRONG_ID METHOD HAS STARTED----------");
        AuthorDto authorDto = authorService.getById(123L);
        assertNull(authorDto);
        LOGGER.log(Level.INFO, "----------GET_BY_WRONG_ID METHOD HAS ENDED----------");
    }

    @Test
    public void getAll() {
        LOGGER.log(Level.INFO, "----------GET_ALL METHOD HAS STARTED----------");
        Mockito.when(mockAuthorDao.getAll()).thenReturn(authorDtoTestList.stream()
                .map(authorDto -> {
                    Author author = authorConverter.toModel(authorDto);
                    authorDto.getNewsList().forEach(news -> author.getNewsIdList().add(news.getId()));
                    return author;
                }).collect(Collectors.toList()));
        assertEquals(authorDtoTestList, authorService.getAll());
        LOGGER.log(Level.INFO, "----------GET_ALL METHOD HAS ENDED----------");
    }
}