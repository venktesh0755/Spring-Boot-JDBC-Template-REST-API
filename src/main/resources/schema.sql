create table if not exists employee(
    id int primary key auto_increment,
    name varchar(255) not null,
    department varchar(255) not null
);