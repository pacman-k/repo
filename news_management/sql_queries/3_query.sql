/*(PL/pgSQL)*/
CREATE OR REPLACE FUNCTION get_tags(newsid bigint, separator character)
RETURNS varchar as $$
BEGIN
 RETURN ( select STRING_AGG( tag.name, separator)
from tag
join news_tag
on news_tag.tag_id = tag.id
where news_tag.news_id = newsid);
END;
$$ language plpgsql;

/*
(PL/SQL)
CREATE OR REPLACE FUNCTION get_tags(newsid IN NUMBER, separator IN CHARACTER)
RETURN VARCHAR IS
BEGIN
 RETURN ( select STRING_AGG( tag.name, separator)
from tag
join news_tag
on news_tag.tag_id = tag.id
where news_tag.news_id = newsid);
END;
*/