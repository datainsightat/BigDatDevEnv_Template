
create table if not exists newschema.newtable(course_id varchar,course_name varchar,author_name varchar,no_of_reviews varchar);

insert into newschema.newtable VALUES(1,'Java','FutureX',45);
insert into newschema.newtable VALUES (2,'Java','FutureXSkill',56);
insert into newschema.newtable VALUES (3,'Big Data','Future',100);
insert into newschema.newtable VALUES (4,'Linux','Future',100);
insert into newschema.newtable VALUES (5,'Microservices','Future',100);
insert into newschema.newtable VALUES (6,'CMS','',100);
insert into newschema.newtable VALUES (7,'Python','FutureX','');