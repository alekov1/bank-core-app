-- liquibase formatted sql

-- changeset polar:1
create table if not exists accounts
(
    id             bigint primary key,
    account_number varchar(255) not null unique,
    account_type   varchar(255) not null,
    balance        numeric(38, 2),
    closed_at      timestamp(6),
    created_at     timestamp(6),
    credit_limit   int check ( credit_limit >= 0 ),
    currency       varchar(255) not null,
    status         varchar(255),
    updated_ad     timestamp(6)
);

