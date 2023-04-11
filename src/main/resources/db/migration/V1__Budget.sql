create table budget
(
    id     serial primary key,
    year   int  not null,
    month  int  not null,
    amount int  not null,
    type   text not null
);

create table author
(
    id         serial primary key,
    full_name  text      not null,
    created_at timestamp not null default now()
);

update budget
set type = 'Расход'
where type = 'Комиссия';