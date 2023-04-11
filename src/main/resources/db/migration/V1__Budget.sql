create table budget
(
    id     serial primary key,
    year   int  not null,
    month  int  not null,
    amount int  not null,
    type   text not null
);

update budget
set type = 'Расход'
where type = 'Комиссия';