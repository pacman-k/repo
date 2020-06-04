/*get_tags() -- from 3-query.sql*/
select id, title,get_tags(id, ', ') as tags
from news
order by news.id;



/*select news.id, news.title, STRING_AGG( tag.name,', ') as tags
from news
join news_tag
on news_tag.news_id = news.id
left join tag
on tag.id = news_tag.tag_id
group by news.id
order by news.id;
*/

