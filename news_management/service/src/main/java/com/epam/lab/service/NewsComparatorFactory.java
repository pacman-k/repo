package com.epam.lab.service;


import com.epam.lab.dto.NewsDto;
import org.springframework.stereotype.Component;


import java.util.Comparator;

@Component
public class NewsComparatorFactory implements ComparatorFactory<NewsDto> {
    private static final String DATE_PARAM = "date";
    private static final String AUTHOR_PARAM = "author";
    private static final String TAGS_PARAM = "tags";

    @Override
    public Comparator<NewsDto> createComparator(String param, boolean trans) {
        switch (param.toLowerCase().trim()) {
            case DATE_PARAM:
                return trans ? Comparator.comparing(NewsDto::getCreationDate)
                        : Comparator.comparing(NewsDto::getCreationDate).reversed();

            case AUTHOR_PARAM:
                Comparator<NewsDto> compareByFirstName = Comparator.comparing(news -> news.getAuthorDto().getName());
                Comparator<NewsDto> compareByLastName = Comparator.comparing(news -> news.getAuthorDto().getSurname());
                return trans ? compareByFirstName.thenComparing(compareByLastName)
                        : compareByFirstName.thenComparing(compareByLastName).reversed();

            case TAGS_PARAM:
                return (news1, news2) -> trans ? Integer.compare(news1.getTagList().size(), news2.getTagList().size())
                        : Integer.compare(news2.getTagList().size(), news1.getTagList().size());
            default:
                return (news1, news2) -> 0;
        }
    }

}
