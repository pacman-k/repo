INSERT INTO news (title, short_text, full_text, modification_date, creation_date) VALUES ('Hello', 'some', 'something', '2020-02-07', '2020-02-07');
INSERT INTO news (title, short_text, full_text, modification_date, creation_date) VALUES ('Science', 'some amou', 'helloSinece', '2013-02-12', '2012-02-03');
INSERT INTO news (title, short_text, full_text, modification_date, creation_date) VALUES ('neighbor', 'tiny texy', 'looooong text', '2019-02-19', '2012-02-03');
INSERT INTO news (title, short_text, full_text, modification_date, creation_date) VALUES ('learn', 'short about learn', 'long about learn ', '2012-02-02', '2012-02-03');
INSERT INTO news (title, short_text, full_text, modification_date, creation_date) VALUES ('village', 'short about village', 'long about village', '2012-02-02', '2012-02-03');
INSERT INTO news (title, short_text, full_text, modification_date, creation_date) VALUES ('garbon', 'short about garbon', 'long about garbon', '2012-02-02', '2012-02-03');
INSERT INTO news (title, short_text, full_text, modification_date, creation_date) VALUES ('Serat', 'short about Serat', 'long about Serat', '2012-02-02', '2012-02-03');
INSERT INTO news (title, short_text, full_text, modification_date, creation_date) VALUES ('Uraz', 'short about Uraz', 'long about Uraz', '2012-02-02', '2012-02-03');
INSERT INTO news (title, short_text, full_text, modification_date, creation_date) VALUES ('Jacking', 'short about Jacking', 'long about Jacking', '2012-02-02', '2012-02-03');
INSERT INTO news (title, short_text, full_text, modification_date, creation_date) VALUES ('Holling', 'short about Holling', 'long about Holling', '2012-02-02', '2012-02-03');
INSERT INTO news (title, short_text, full_text, modification_date, creation_date) VALUES ('School', 'short about School', 'long about School', '2012-02-02', '2012-02-03');

INSERT INTO author (name, surname) VALUES ('Jon', 'Snow');--1
INSERT INTO author (name, surname) VALUES ('Sansa', 'Stark');--2
INSERT INTO author (name, surname) VALUES ('Robert', 'Buration');--3
INSERT INTO author (name, surname) VALUES ('Tirion', 'Lanistar');--4
INSERT INTO author (name, surname) VALUES ('Serseya', 'Lanistar');--5
INSERT INTO author (name, surname) VALUES ('Deyneris', 'Targarian');--6

INSERT INTO tag (name) VALUES ('science');--1
INSERT INTO tag (name) VALUES ('soft');--2
INSERT INTO tag (name) VALUES ('hard');--3
INSERT INTO tag (name) VALUES ('cars');--4
INSERT INTO tag (name) VALUES ('glass');--5
INSERT INTO tag (name) VALUES ('peniapple');--6
INSERT INTO tag (name) VALUES ('loop');--7
INSERT INTO tag (name) VALUES ('language');--8
INSERT INTO tag (name) VALUES ('queue');--9


INSERT INTO news_author (news_id, author_id) VALUES (1, 1);
INSERT INTO news_author (news_id, author_id) VALUES (2, 1);
INSERT INTO news_author (news_id, author_id) VALUES (3, 2);
INSERT INTO news_author (news_id, author_id) VALUES (4, 2);
INSERT INTO news_author (news_id, author_id) VALUES (5, 2);
INSERT INTO news_author (news_id, author_id) VALUES (6, 3);
INSERT INTO news_author (news_id, author_id) VALUES (7, 4);
INSERT INTO news_author (news_id, author_id) VALUES (8, 5);
INSERT INTO news_author (news_id, author_id) VALUES (9, 6);
INSERT INTO news_author (news_id, author_id) VALUES (10, 1);
INSERT INTO news_author (news_id, author_id) VALUES (11, 2);

INSERT INTO news_tag (news_id, tag_id) VALUES (1,1);
INSERT INTO news_tag (news_id, tag_id) VALUES (2,2);
INSERT INTO news_tag (news_id, tag_id) VALUES (2,3);
INSERT INTO news_tag (news_id, tag_id) VALUES (2,4);
INSERT INTO news_tag (news_id, tag_id) VALUES (3,5);
INSERT INTO news_tag (news_id, tag_id) VALUES (3,9);
INSERT INTO news_tag (news_id, tag_id) VALUES (4,7);
INSERT INTO news_tag (news_id, tag_id) VALUES (4,8);
INSERT INTO news_tag (news_id, tag_id) VALUES (4,1);
INSERT INTO news_tag (news_id, tag_id) VALUES (5,7);
INSERT INTO news_tag (news_id, tag_id) VALUES (6,8);
INSERT INTO news_tag (news_id, tag_id) VALUES (7,8);
INSERT INTO news_tag (news_id, tag_id) VALUES (8,8);
INSERT INTO news_tag (news_id, tag_id) VALUES (9,1);
INSERT INTO news_tag (news_id, tag_id) VALUES (10,2);
INSERT INTO news_tag (news_id, tag_id) VALUES (11,3);
INSERT INTO news_tag (news_id, tag_id) VALUES (7,1);
INSERT INTO news_tag (news_id, tag_id) VALUES (9,3);
INSERT INTO news_tag (news_id, tag_id) VALUES (7,5);


