package by.epam.training.news;

import lombok.*;

import java.util.Date;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
public class NewsDto {
    private Long newsId;
    private String newsTopic;
    private String newsHeading;
    private String newsText;
    private Date dateOfPost = new Date();
}
