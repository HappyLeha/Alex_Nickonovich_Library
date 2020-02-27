Create database if not exists Library;
use Library;
create table if not exists Authors (
firstName varchar(100),
lastName varchar(100),
year INT,
gender varchar(100),
primary key(firstName,lastName)
);
create table if not exists Publishings (
title varchar(100) primary key,
country varchar(100),
address varchar(100),
postIndex INT,
email varchar(100)
);
create table if not exists Books (
title varchar(100) primary key,
year int not null,
publishing varchar(100),
date DATE,
countOfInstances int,
image Blob,
constraint publishing_books 
foreign key (publishing)
references publishings (title)
);
create table if not exists Books_has_Authors(
book varchar(100),
author_firstName varchar(100),
author_lastName varchar(100),
constraint books_connection foreign key(book) 
references Books(title),
constraint authors_connection 
foreign key (author_firstName,author_lastName)
references Authors(firstName,lastName)
);
create table if not exists Readers(
firstName varchar(100),
lastName varchar(100),
year INT,
email varchar(100) not null,
phone INT,
image Blob,
primary key(firstName,lastName)
);
create table if not exists Rent(
reader_firstName varchar(100),
reader_lastName varchar(100),
book varchar(100) not null,
startDate Date not null,
endDate Date not null,
primary key(reader_firstName,reader_lastName),
constraint Reader_Rent foreign key(reader_firstName,reader_lastName)
references Readers(firstName,lastName),
constraint Book_Rent foreign key(book)
references Books(title)
);



