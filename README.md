# Giphy_Connector

instruction to connect with DataBase:


show tables; 
create database exercise;
use exercise;
create table giphy (id varchar(30) primary key, likes int(10), dislikes int(10));
drop table giphy;
desc giphy;
select * from giphy;


For main application open:
 	src/main/java/justyna/giphy -> GiphyWindow.java
  
  
  Please enter the password for your database in 37th line: 
  
      private static final String PASSWORD = "PLEASE ENTER THE PASSWORD FOR THE DB";"
  
  Please enter the url for the database in line 32: 
  
      static {
        URL = "jdbc:mysql://localhost:3306/exercise?autoReconnect=true&useSSL=false";
      }
