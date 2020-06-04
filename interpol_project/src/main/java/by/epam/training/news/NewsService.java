package by.epam.training.news;


import by.epam.training.dao.Transactional;

import java.util.Comparator;
import java.util.List;
import java.util.Optional;

public interface NewsService {
    @Transactional
    boolean createNews (NewsDto newsDto);

    List<NewsDto> getAllNews();

    List<NewsDto> findByTopic(String topic);

    List<NewsDto> getSortedNews(Comparator<NewsDto> comp);

    Optional<NewsDto> findById (Long id);

    @Transactional
    boolean updateNews(NewsDto newsDto);

    void deleteNews(NewsDto newsDto);
}
