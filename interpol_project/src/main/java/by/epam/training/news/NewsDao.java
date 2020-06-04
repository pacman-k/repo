package by.epam.training.news;

import by.epam.training.dao.CrudDao;
import by.epam.training.dao.DAOException;

import java.util.List;

public interface NewsDao extends CrudDao<NewsDto, Long> {
 List<NewsDto> findNewsByTopic (String newsTopic) throws DAOException;
}
