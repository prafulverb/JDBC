# Online Gardening Community Platform

## Overview
This is a Java-based application for an online gardening community. It supports two user roles: Admin and Gardener.

## Features
- **Admin**: User Management, Content Moderation, System Settings.
- **Gardener**: Share Tips, Participate in Discussions, Manage Projects.
- **Technical**: JDBC for database, Swing for GUI, Multithreading for data loading.

## Prerequisites
- Java Development Kit (JDK) 8 or higher.
- SQLite JDBC Driver (e.g., `sqlite-jdbc-3.x.x.jar`).

## Setup
1.  Ensure the `sqlite-jdbc` jar is in your classpath.
2.  Compile the project.
3.  Run `com.gardening.platform.ui.MainApp`.

## Database
The application uses SQLite (`gardening.db`). The database and tables are automatically created on the first run.

## Usage
1.  **Login**:
    - You can register a new user (feature pending in UI, but DB supports it) or use the default admin if configured.
    - *Note*: Since registration UI is simplified, you might need to manually insert a user into the DB or use a test script to create the first user.
    
    To create a test user, you can modify `MainApp.java` temporarily to insert a user on startup if the table is empty.

## Project Structure
- `src/com/gardening/platform/model`: Entity classes.
- `src/com/gardening/platform/dao`: Data Access Objects.
- `src/com/gardening/platform/service`: Business logic.
- `src/com/gardening/platform/ui`: Swing GUI classes.
- `src/com/gardening/platform/util`: Database connection and setup.
