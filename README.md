# Giphy_Connector
to create DataBase (MySQL):
create database exercise;

to create table:
create table giphy (id varchar(30) primary key, likes int(10), dislikes int(10));


For main application open:
 	src/main/java/justyna/giphy -> GiphyWindow.java
  
  
  Please enter the password for your database in 37th line: 
  
      private static final String PASSWORD = "PLEASE ENTER THE PASSWORD FOR THE DB";"
  
  Please enter the url for the database in line 32: 
  
      private static final String URL = "jdbc:mysql: PLEASE ENTER YOUR URL";
