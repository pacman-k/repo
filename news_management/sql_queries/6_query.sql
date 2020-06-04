/*(LOGGING TABLE)*/
create table LOGGING (
insert_date DATE,
ref_table_name varchar(30),
description varchar(300)
);


/*(FUNCTION FOR TRIGGER)*/
CREATE or REPLACE FUNCTION insert_into_LOGGING()
RETURNS trigger as $$
BEGIN 
    IF TG_ARGV[0] = 'news'
        then insert into LOGGING values (now(), 'news', concat_ws(', ', concat( '{title - ' , NEW.title), concat( 'short_text - ' , NEW.short_text), concat( 'full_text - ',  NEW.full_text,'}')) );
    ELSIF TG_ARGV[0] = 'author'
        then insert into LOGGING values (now(), 'author', concat_ws(', ',  concat( '{name - ' , NEW.name), concat( 'surname - ' , NEW.surname, '}')));
    ELSIF  TG_ARGV[0] = 'tag'
        then insert into LOGGING values (now(), 'tag' ,concat_ws(', ',  concat( '{name - ' ,  NEW.name, '}')));
    ELSIF  TG_ARGV[0] = 'users'
        then insert into LOGGING values (now(),  'users' ,concat_ws(', ',  concat( '{name - ' ,  NEW.name), concat( 'surname - ' ,  NEW.surname),concat( 'login - ' ,  NEW.login), concat( 'password - ' ,  NEW.password, '}')));
    ELSIF  TG_ARGV[0] = 'news_author'
        then insert into LOGGING values (now(), 'news_author' ,concat_ws(', ',  concat( '{news_id - ' ,  NEW.news_id), concat( 'author_id - ' ,  NEW.author_id),'}' ));
    ELSIF  TG_ARGV[0] = 'news_tag'
        then insert into LOGGING values (now(), 'news_tag' ,concat_ws(', ',  concat( '{news_id - ' ,  NEW.news_id), concat( 'tag_id - ' ,  NEW.tag_id),'}' ));
    END IF;
    RETURN NULL;
 END;
$$ LANGUAGE plpgsql;


/*news trigger*/
CREATE TRIGGER news_insert 
AFTER INSERT
on public.news
for each row 
EXECUTE PROCEDURE insert_into_LOGGING('news');
/*author trigger*/
CREATE TRIGGER author_insert 
AFTER INSERT
on public.author
for each row 
EXECUTE PROCEDURE insert_into_LOGGING('author');
/*tag trigger*/
CREATE TRIGGER tag_insert 
AFTER INSERT
on public.tag
for each row 
EXECUTE PROCEDURE insert_into_LOGGING('tag');
/*users trigger*/
CREATE TRIGGER user_insert
AFTER INSERT
ON public.users
for each row
EXECUTE PROCEDURE insert_into_LOGGING('users');
/*news_author trigger*/
CREATE TRIGGER news_author_insert
AFTER INSERT
ON public.news_author
for each row
EXECUTE PROCEDURE insert_into_LOGGING('news_author');
/*news_tag trigger*/
CREATE TRIGGER news_tag_insert
AFTER INSERT
ON public.news_tag
for each row
EXECUTE PROCEDURE insert_into_LOGGING('news_tag');



