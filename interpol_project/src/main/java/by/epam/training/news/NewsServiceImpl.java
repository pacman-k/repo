package by.epam.training.news;

import by.epam.training.core.Bean;
import by.epam.training.dao.DAOException;
import by.epam.training.dao.TransactionSupport;
import by.epam.training.dao.Transactional;
import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;

@Log4j
@Bean
@TransactionSupport
@AllArgsConstructor
public class NewsServiceImpl implements NewsService {
    NewsDao newsDao;

    @Transactional
    @Override
    public boolean createNews(NewsDto newsDto) {
        try {
            newsDao.save(newsDto);
            return true;
        } catch (DAOException e) {
            log.error("cant create news : " + newsDto, e);
            return false;
        }
    }

    @Override
    public List<NewsDto> getAllNews() {
        try {
            return newsDao.getAll();
        } catch (DAOException e) {
            log.error("cant get all news ", e);
            return new ArrayList<>();
        }
    }

    @Override
    public List<NewsDto> findByTopic(String topic) {
        try {
            return newsDao.findNewsByTopic(topic);
        } catch (DAOException e) {
            log.error("cant find news with such topic :" + topic, e);
            return new ArrayList<>();
        }
    }

    @Override
    public List<NewsDto> getSortedNews(Comparator<NewsDto> comp) {
        try {
            List<NewsDto> list = getAllNews();
            list.sort(comp);
            return list;
        } catch (Exception e){
            log.error("cant get sorted list of news");
            return new ArrayList<>();
        }
    }

    @Override
    public Optional<NewsDto> findById(Long id) {
        try {
            return Optional.of(newsDao.getById(id));
        }catch (DAOException e){
            log.error("cant get news by such id : " +id);
            return Optional.empty();
        }
    }

    @Override
    public boolean updateNews(NewsDto newsDto) {
        try {
           return newsDao.update(newsDto);
        } catch (DAOException e){
            log.error("Cant update news : " + newsDto);
            return false;
        }
    }

    @Override
    public void deleteNews(NewsDto newsDto) {
        try {
            newsDao.delete(newsDto);
        }catch (DAOException e){
            log.error("cant delete news : " + newsDto.getNewsId());
        }
    }
}
