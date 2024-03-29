#!/bin/bash
mysql <<EOFMYSQL
use rwgoss;
CREATE TABLE SUPPLIER(ID int Primary Key, NAME char(50) NOT NULL, PHONE char(25) NOT NULL, EMAIL varchar(50) NOT NULL);
CREATE TABLE ITEM(ID int Primary Key, NAME char(50) NOT NULL, SUPPLIER_ID int NOT NULL, QUANTITY int CHECK( 0<=QUANTITY), UNIT_PRICE float NOT NULL);
CREATE TABLE EMPLOYEE(ID int Primary Key, NAME char(50) NOT NULL, ROLE char(50) NOT NULL, PHONE char(25) NOT NULL, ADDRESS char(50) NOT NULL, START_DATE date NOT NULL);
CREATE TABLE SALES(ID int Primary Key, EMPLOYEE_ID int NOT NULL, GRATUITY float, TOTAL float NOT NULL, CREATE_AT datetime NOT NULL, UPDATED_AT datetime);
CREATE TABLE SALE_ITEM(SALE_ID int, ITEM_ID int, QUANTITY int CHECK(1<=QUANTITY), TOTAL float CHECK(0<=TOTAL), FOREIGN KEY (SALE_ID) REFERENCES SALES(ID), FOREIGN KEY (ITEM_ID) REFERENCES ITEM(ID));
DESC SUPPLIER;
DESC ITEM;
DESC EMPLOYEE;
DESC SALES;
DESC SALE_ITEM;
INSERT INTO SUPPLIER VALUES(1,'Coco Fresh Tea & Juice','7183532709','marketing@cocobubbletea.com');
INSERT INTO SUPPLIER VALUES(3,'Sharetea','4052122339','service@1992sharetea.com');
INSERT INTO SUPPLIER VALUES(4,'Boba Guys','4159672622','support@bobaguys.zendesk.com');
INSERT INTO SUPPLIER VALUES(8,'Kung Fu Tea','8555389888','info@kfteausa.com');
INSERT INTO SUPPLIER VALUES(15,'Fat Straws','4695472195','smile@fatstraws.net');
INSERT INTO ITEM VALUES(2,'Classic Black Tea',3,68,3.00);
INSERT INTO ITEM VALUES(3,'Classic,Oolong Tea',3,36,3.00);
INSERT INTO ITEM VALUES(10,'Honey Milk Tea',1,59,4.50);
INSERT INTO ITEM VALUES(11,'Kung Fu Milk Green Tea',8,5,4.50);
INSERT INTO ITEM VALUES(15,'Mango & Passion Fruit Tea',3,15,5.00);
INSERT INTO ITEM VALUES(20,'Matcha Milk Tea',4,1,4.00);
INSERT INTO ITEM VALUES(21,'Taro Milk Tea',15,3,4.00);
INSERT INTO EMPLOYEE VALUES(1,'Alice Atkins','Manager','479-555-0171','4240 Arlington Ave, Jonesboro',STR_TO_DATE('2021-10-12' ,'%Y-%m-%d'));
INSERT INTO EMPLOYEE VALUES(2,'Albert Bullock','Cashier','479-555-0149','383 Rosebud Ave, Hatfield',STR_TO_DATE('2021-10-12' ,'%Y-%m-%d'));
INSERT INTO EMPLOYEE VALUES(3,'Juan Scott','Cashier','479-555-0152','5 Barrington Court, Little Rock',STR_TO_DATE('2021-10-12','%Y-%m-%d'));
INSERT INTO EMPLOYEE VALUES(4,'Christine Jarrett','Cashier','479-555-0110','1484 Mulberry Avenue, Conway',STR_TO_DATE('2021-10-12' ,'%Y-%m-%d'));
INSERT INTO SALES VALUES(1,4,NULL,16.50,STR_TO_DATE('2022-01-17 14:01:24' ,'%Y-%m-%d %T'),NULL);
INSERT INTO SALES VALUES(2,4,NULL,4.00,STR_TO_DATE('2022-01-17 14:15:06' ,'%Y-%m-%d %T'),NULL );
INSERT INTO SALES VALUES(3,3,NULL,17.50,STR_TO_DATE('2022-01-17 15:12:40' ,'%Y-%m-%d %T'),NULL);
INSERT INTO SALES VALUES(4,2,NULL,6.00,STR_TO_DATE('2022-01-17 16:47:11' ,'%Y-%m-%d %T'),NULL);
INSERT INTO SALES VALUES(5,1,NULL,7.50,STR_TO_DATE('2022-01-17 16:58:00' ,'%Y-%m-%d %T'),NULL);
INSERT INTO SALES VALUES(6,2,NULL,4.00,STR_TO_DATE('2022-01-17 17:03:33' ,'%Y-%m-%d %T'),NULL);
INSERT INTO SALE_ITEM VALUES(1,20,2,8.00);
INSERT INTO SALE_ITEM VALUES(1,21,1,4.00);
INSERT INTO SALE_ITEM VALUES(1,11,1,4.50);
INSERT INTO SALE_ITEM VALUES(2,20,1,4.00);
INSERT INTO SALE_ITEM VALUES(3,20,1,4.00);
INSERT INTO SALE_ITEM VALUES(3,21,1,4.00);
INSERT INTO SALE_ITEM VALUES(3,10,1,4.50);
INSERT INTO SALE_ITEM VALUES(3,15,1,5.00);
INSERT INTO SALE_ITEM VALUES(4,2,1,3.00);
INSERT INTO SALE_ITEM VALUES(4,3,1,3.00);
INSERT INTO SALE_ITEM VALUES(5,3,1,3.00);
INSERT INTO SALE_ITEM VALUES(5,10,1,4.50);
INSERT INTO SALE_ITEM VALUES(6,20,1,4.00);
SELECT * FROM SUPPLIER;
SELECT * FROM ITEM;
SELECT * FROM EMPLOYEE;
SELECT * FROM SALES;
SELECT * FROM SALE_ITEM;
SELECT i.NAME AS Item,s.NAME as Supplier FROM ITEM i, SUPPLIER s, SALE_ITEM si WHERE i.ID = si.ITEM_ID AND s.ID = i.SUPPLIER_ID GROUP BY i.NAME,s.NAME ORDER BY SUM(si.QUANTITY) DESC LIMIT 1;
SELECT * FROM SALES;
UPDATE SALES SET GRATUITY = TOTAl * .15, UPDATED_AT = NOW();
SELECT * FROM SALES;
SELECT CONCAT('$',TRUNCATE(SUM(TOTAL - GRATUITY),2)) AS SALE FROM SALES WHERE CREATE_AT BETWEEN '2022-01-17 00:00:00' AND '2022-01-17 23:59:59';
SELECT e.NAME as Name, SUM(si.QUANTITY) as Count FROM SALES s, EMPLOYEE e, SALE_ITEM si WHERE s.EMPLOYEE_ID = e.ID AND s.ID = si.SALE_ID AND si.ITEM_ID = 21 GROUP BY e.NAME;
SELECT e.NAME, SUM(s.TOTAL) as Highest_Sale FROM EMPLOYEE e, SALES s WHERE e.ID = s.EMPLOYEE_ID GROUP BY e.NAME ORDER BY Highest_Sale DESC Limit 1;
