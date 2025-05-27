Note:(تم عمل هذا المشروع ضمن مادة الرمجه المتقدمه بالجامعة )


# StudentMarksManager 
A simple Java GUI application for managing student marks per subject using MySQL.

## Features
- Add students with their marks
- Search students by name or mark
- View all students in a selected subject
- Supports multiple subjects:
  - البرمجة المتقدمة
  - الأنظمة المضمنة
  - الذكاء الاصطناعي

## Technologies
- Java Swing
- MySQL database
- JDBC driver

## Setup Instructions
1. Install MySQL and create a database named `finalproject`
2. Create the following tables:


```sql
CREATE TABLE advanced_programming (
    id INT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(100),
    mark FLOAT
);

CREATE TABLE embedded_systems (
    id INT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(100),
    mark FLOAT
);

CREATE TABLE ai (
    id INT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(100),
    mark FLOAT
);




## Author
Created by Eng.Azzam Saleh
