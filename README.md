# LicznikSlowWpliku

CONTENTS OF THIS FILE
---------------------

 * Introduction
 * Requirements
 * Installation
 * Configuration
 * Troubleshooting

INTRODUCTION
------------

The  LicznikSlowWpliku is a program where you can upload the txt file and it will count the words and show you in the Console how much each of the word occurs. Then it sort the words number Descending and Ascending. Then the LicznikSlowWpliku will create for you a Excel file with the sorted results (IMPORTANT prerequisite: you need first to create a Directory “Wyniki” on your Disk C: partition), then the program will create the ZIP file and put there the excel file.

 * To submit bug reports and feature suggestions, or track changes please write directly to the Repository owner

REQUIREMENTS
------------

•	IntelliJ IDEA or Eclipse IDE
•	JDK 11+
•	Maven compiler v3.8.1
•	Maven Jar v2.4

INSTALLATION
------------
 
 * Install as you would normally install a simple Java “one page” application. Please put the code into the IDE and run mvn clean install. Then run the main class and put the Absolute Patch of the text file.

CONFIGURATION
-------------

The program has no menu or modifiable settings. 

TROUBLESHOOTING
---------------

 Got message "Za gruby, nie przejdzie przez wrota"?
- The progam will not suport the large text files, they need to have less than 5 Mb size

  Got message Niestety najwidoczniej pliku nie ma w domu"?
- Program supports only .txt files, so you can use only Notepad file to count the words
