alter table test_service_schema.attempt add column if not exists pass_percentage DOUBLE PRECISION default 0;
CREATE OR REPLACE FUNCTION update_attempt_pass_percentage() RETURNS trigger AS
'
DECLARE
    number_of_testcases double precision;
    passed_tests double precision;
BEGIN
    select COUNT(*) into passed_tests from test_service_schema.test_case where attempt_id = old.attempt_id and verdict = ''1'';
    select COUNT(*) into number_of_testcases from test_service_schema.test_case where attempt_id = old.attempt_id;
    IF (TG_OP = ''UPDATE'') THEN
        UPDATE test_service_schema.attempt
        SET pass_percentage = (passed_tests/number_of_testcases)*100
        WHERE id = OLD.attempt_id;
    END IF;
    RETURN NULL;
END;
'
LANGUAGE PLPGSQL;

CREATE TRIGGER update_attempt_pass_percentage_trigger
    AFTER UPDATE ON test_service_schema.test_case
    FOR EACH ROW EXECUTE PROCEDURE update_attempt_pass_percentage();
