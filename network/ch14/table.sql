CREATE DATABASE myChat;

USE myChat;

CREATE TABLE tblRegister(
 id VARCHAR(20) PRIMARY KEY,
 pwd VARCHAR(20) NOT NULL,
 NAME VARCHAR(20) NOT NULL,
 regdate DATETIME DEFAULT NOW()
 )
 
 INSERT tblRegister VALUES ('aaa','1234','강호동',NOW());
 INSERT tblRegister VALUES ('bbb','1234','오지혁',NOW());
 INSERT tblRegister VALUES ('ccc','1234','강도장',NOW());
 
 SELECT * FROM tblRegistermyChat