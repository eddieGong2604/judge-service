SET search_path to test_service_schema;

CREATE EXTENSION IF NOT EXISTS "uuid-ossp" with schema public;

create table if not exists attempt(
    id uuid PRIMARY KEY,
    created_on timestamp default CURRENT_TIMESTAMP,
    is_deleted BOOL default 'f',
    code varchar
);
create table if not exists test_case(
    id uuid PRIMARY KEY,
    created_on timestamp default CURRENT_TIMESTAMP,
    is_deleted BOOL default 'f',
    attempt_id uuid,
    stdout varchar,
    verdict varchar,
    expected_output varchar,
    input varchar,
    type varchar,
    size varchar,
    constraint fk_qualification_practitioner foreign key(attempt_id) references attempt(id)
);
