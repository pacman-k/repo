select tablename "name" , pg_size_pretty( pg_total_relation_size ((select oid from pg_class where relname = tablename))) total_size
from pg_tables t
where schemaname = 'public';