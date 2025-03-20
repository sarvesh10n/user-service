-- Setup mysql on local --
create user 'sarvesh'@'localhost';
create database userservice;
grant all privileges on userservice.* to 'sarvesh'@'localhost';