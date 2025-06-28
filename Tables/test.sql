SELECT version();
DROP TABLE contracts;
--DROP TABLE servicehistory;
--DROP TABLE users cascade;
--drop table clients cascade;
--drop table employees cascade;
--drop table services cascade;
--drop database legalfirm;


SELECT column_name, is_nullable
FROM information_schema.columns
WHERE table_name = 'users';
