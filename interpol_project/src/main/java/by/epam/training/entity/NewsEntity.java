package by.epam.training.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Timestamp;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class NewsEntity {
    private Long newsId;
    private String newsTopic;
    private String newsHeading;
    private String newsText;
    private Timestamp dateOfPost;
}
