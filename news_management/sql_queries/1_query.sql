select  * 
from public.query_1;

/*create view public.query_1 as
select author.id as author_id, 
                        (select count(*) from
                            (select * from news 
                                join news_author on 
                                news_author.news_id = news.id 
                                where news_author.author_id = author.id)
                        foo) as news_count,
                         ( select tag.name from tag 
                                join news_tag 
                                on news_tag.tag_id = tag.id 
                                join news 
                                on news.id = news_tag.news_id 
                                join news_author 
                                on news_author.news_id = news.id 
                                where news_author.author_id = author.id 
                                group by tag.id order by count(tag) 
                                desc limit 1)  as top_tag
 from author
 order by id ;*/