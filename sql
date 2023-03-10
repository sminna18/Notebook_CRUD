DROP TABLE person;

create table if not exists Person(
                                     id int GENERATED BY DEFAULT AS IDENTITY PRIMARY KEY ,
                                     name varchar NOT NULL ,
                                     age int CHECK ( age > 0 ),
                                     email varchar UNIQUE
);
insert into Person(name, age, email) values('Tom', 28, 'tomker@mail.ru');
insert into Person(name, age, email) values('Kat', 18, 'kitikaty@mail.ru');
insert into Person(name, age, email) values('Jeni', 37, 'jeni1985@mail.ru');
insert into Person(name, age, email) values('Karl', 24, 'karlitos22@mail.ru');
insert into Person(name, age, email) values('Dmitriy', 21, 'dimka547@mail.ru');

SELECT * FROM person