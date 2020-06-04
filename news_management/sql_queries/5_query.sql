select id "author 1" ,
       case when lag(id) over (order by random()) is null then  lead(id, cast((select count(*) -1 from author) as int)) over (order by random())
           else lag(id) over (order by random())
       end "author 2"
from author
group by id
order by id;