/*
 * Initializes PostgreSQL database with the following tables
 */

-- Table 1
CREATE TABLE test (
    col1 int,
    col2 varchar
);

insert into test
select
  generate_series(1, 100000)
  , (generate_series(1, 100000) * 76)::text
;

-- Table 2
CREATE TABLE test2 (
    col1 int,
    col2 varchar
);

insert into test2
select
  generate_series(1, 1000000)
  , (generate_series(1, 1000000) * 76)::text
;

-- Table 3
create table sales_transaction (
  id bigserial primary key,
  created_at timestamp without time zone default current_timestamp,
  updated_at timestamp without time zone default current_timestamp,
  posted_date timestamp,
  description text,
  amount_cents int,
  status varchar(255)
)
;

insert into sales_transaction
  (created_at, updated_at, posted_date, description, amount_cents, status)

with base as (
  select
    generate_series(1, 1000000)
    , current_timestamp - (left((random() * 10)::text, 10) || ' days')::interval as created_at
    , random() as random_base
)

, base2 as (
  select
    created_at
    , created_at + (left((random_base * 10)::text, 10) || ' days')::interval as updated_at
    , created_at - '1 day'::interval as posted_date
    , md5(random_base::text) as description
    , (random_base * 100000)::int as amount_cents
  from base
)

select
  *
  , substring(description, 1, 10) as status
from base2
;

insert into sales_transaction
  (created_at, updated_at, posted_date, description, amount_cents, status)
select created_at, updated_at, posted_date, description, amount_cents, status
from sales_transaction
;
