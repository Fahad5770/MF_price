set global log_output = 'TABLE';
set global long_query_time=10;
set long_query_time=10;
set global slow_query_log = 'ON';
show variables like 'slow%';

select * from mysql.slow_log order by start_time desc;