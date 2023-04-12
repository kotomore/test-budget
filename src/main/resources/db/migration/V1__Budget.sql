create table author
(
    id         serial primary key,
    full_name  text      not null,
    created_at timestamp not null default now()
);

create table budget
(
    id     serial primary key,
    year   int  not null,
    month  int  not null,
    amount int  not null,
    type   text not null,
    author_id int NULL,
    CONSTRAINT fk_author FOREIGN KEY (author_id) REFERENCES Author(id)
);

update budget
set type = 'Расход'
where type = 'Комиссия';