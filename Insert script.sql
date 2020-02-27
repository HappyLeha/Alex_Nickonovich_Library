use library;
insert into authors values('Herbert','Schildt',null,'MAN');
insert into publishings values('Williams','Russia',null,null,null);
insert into books values('Java the complete reference',2013,'Williams',null,1,null),('Philosophy of java',2011,null,null,2,null);
insert into books_has_authors values('Java the complete reference','Herbert','Schildt');
insert into readers values ('Alex','Novak',1995,'alexnovak@gmail.com',null,null),('Martin','Stolz',1998,'martinstolz@gmail.com',null,null),('Anna','Polonskaya',2001,'annapolonskaya@gmail.com',null,null);
insert into rents values ('Martin','Stolz','Java the complete reference','2020.02.28','2020.09.09');