
# Event Scheduling Reminder System

## Overview
The **Event Scheduling Reminder System** is a Java application designed to help users manage their tasks, appointments, and important deadlines. With this app, users can set up reminders, schedule events, and get notified before deadlines or appointments.

## Features
- **Schedule Events**: Add appointments, deadlines, and tasks to your calendar.
- **Set Reminders**: Receive notifications before an event starts or a task is due.
- **Event Management**: Search, update, or delete scheduled events easily.
- **Data Persistence**: All event data is stored in a SQLite database.

## Tools & Technologies
- **Programming Language**: Java
- **GUI**: AWT (Abstract Window Toolkit)
- **Database**: MYSQL
- **Notifications**: Java Notification APIs

## How to Run
1. Clone the repository:
   ```bash
   git clone https://github.com/crepopcorn/event-scheduling-reminder-system.git
   ```

2. Make sure SQLite/Mysql is installed and properly configured on your machine.

3. Open the `DatabaseConnection.java` file and ensure the SQLite database path is correct.

4. Create the necessary table in the SQLite/Mysql database using the following SQL command (mysql for example):
   ```sql
   CREATE TABLE events (
    id INT AUTO_INCREMENT PRIMARY KEY,
    event_name VARCHAR(100),
    event_date DATE,
    reminder_time TIME
   );
   ```

5. Open the project in your preferred IDE (like VS Code or IntelliJ), and run the `Main.java` file to start the app.

## Requirements
- Java 8 or above
- SQLite/MySQL Database
- Java Notification APIs for setting reminders


