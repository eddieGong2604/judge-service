SET search_path to test_service_schema;

CREATE EXTENSION IF NOT EXISTS "uuid-ossp" with schema public;

CREATE TABLE IF NOT EXISTS ATTEMPT (
    id uid;
    createdOn TIMESTAMP;
    isDeleted boolean;
   	code varchar;
    PRIMARY KEY(id)
);