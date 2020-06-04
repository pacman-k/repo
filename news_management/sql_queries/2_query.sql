select name, surname
from author
where (
    select FLOOR(avg(len)/500) * FLOOR(sum(len)/3000)  from (
        select length(full_text) len
        from news 
        join news_author 
        on news_author.news_id = news.id 
        where news_author.author_id = author.id) t) 
> 0  ;