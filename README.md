
# Event Scheduling and Reminder System

## Overview
The **Event Scheduling and Reminder System** is a Java application designed to help users manage their tasks, appointments, and important deadlines. With this app, users can set up reminders, schedule events, and get notified before deadlines or appointments. It’s a simple, effective tool for managing your daily tasks and keeping track of important dates.

## Features
- **Schedule Events**: Add appointments, deadlines, and tasks to your calendar.
- **Set Reminders**: Receive notifications before an event starts or a task is due.
- **Event Management**: Search, update, or delete scheduled events easily.
- **Data Persistence**: All event data is stored in a SQLite database.

## Tools & Technologies
- **Programming Language**: Java
- **GUI**: AWT (Abstract Window Toolkit)
- **Database**: SQLite
- **Notifications**: Java Notification APIs

## How to Run
1. Clone the repository:
   ```bash
   git clone https://github.com/yourusername/event-scheduling-reminder-system.git
   ```

2. Make sure SQLite is installed and properly configured on your machine.

3. Open the `DatabaseConnection.java` file and ensure the SQLite database path is correct.

4. Create the necessary table in the SQLite database using the following SQL command:
   ```sql
   CREATE TABLE events (
       id INTEGER PRIMARY KEY AUTOINCREMENT,
       event_name VARCHAR(100),
       event_date DATE,
       reminder_time TIME
   );
   ```

5. Open the project in your preferred IDE (like VS Code or IntelliJ), and run the `Main.java` file to start the app.

## Requirements
- Java 8 or above
- SQLite Database
- Java Notification APIs for setting reminders

## Future Enhancements
- **Recurring Events**: Add support for scheduling recurring tasks.
- **Improved Notifications**: Include desktop notifications or email reminders.
- **Integration with Calendars**: Sync events with Google Calendar or other calendar platforms.

## Contributing
Feel free to fork the project, submit issues, or open pull requests. Contributions are welcome!
