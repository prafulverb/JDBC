# Online Gardening Community Platform

## Overview
A comprehensive Java-based desktop application for an online gardening community, supporting two user roles: Admin and Gardener. Built with Java Swing GUI, JDBC for database operations, and demonstrates advanced OOP principles, multithreading, and modern software engineering practices.

## Features

### For Gardeners:
- **Tip Sharing**: Share gardening tips with photos
- **Search & Filter**: Search tips by keywords
- **Export Data**: Export tips to CSV files
- **Project Management**: Create and track gardening projects
- **Discussion Forum**: Participate in community discussions
- **Profile Management**: Update personal information and password
- **Statistics Dashboard**: View your contributions (tips, projects, discussions)

### For Admins:
- **User Management**: View, manage, and delete user accounts
- **Content Moderation**: Review and moderate tips and discussions
- **System Settings**: Configure platform settings
- **Statistics Dashboard**: View platform-wide analytics
- **Export Capabilities**: Export user lists and content

### Technical Highlights:
- **OOP Implementation**: Inheritance (User → Admin/Gardener), Polymorphism, Interfaces (DAO pattern)
- **Collections & Generics**: Extensive use of List<T>, ArrayList, Stream API
- **Multithreading**: Background data loading, thread pool for image downloads
- **Synchronization**: Thread-safe tip counter
- **JDBC**: CRUD operations with PreparedStatement (SQL injection prevention)
- **Transaction Management**: Atomic operations with rollback support
- **Exception Handling**: Comprehensive error handling with user-friendly messages

## Prerequisites
- Java Development Kit (JDK) 8 or higher
- SQLite JDBC Driver (`sqlite-jdbc-3.50.3.0.jar` included in `lib/` folder)

## Setup & Installation

1.  **Clone the Repository:**
    ```bash
    git clone https://github.com/prafulverb/JDBC.git
    cd JDBC
    ```

2.  **Verify Dependencies:**
    The SQLite JDBC driver is already included in the `lib/` folder.

3.  **Compile the Project:**
    ```bash
    javac -d bin -sourcepath src -cp "lib/sqlite-jdbc-3.50.3.0.jar" src/com/gardening/platform/ui/MainApp.java
    ```

4.  **Run the Application:**
    ```bash
    java -cp "bin;lib/sqlite-jdbc-3.50.3.0.jar" com.gardening.platform.ui.MainApp
    ```

## Usage

### First Time Setup:
1.  Launch the application
2.  Click **Register** to create an account
3.  Choose your role: **ADMIN** or **GARDENER**
4.  Login with your credentials

### Example Accounts to Create:
- **Admin**: `admin@garden.com` / `admin123`
- **Gardener**: `gardener@garden.com` / `password`

## Database
- Uses SQLite (`gardening.db`)
- Automatically created on first run
- Tables: `users`, `tips`, `projects`, `discussions`, `settings`

## Project Structure
```
JDBC/
├── src/com/gardening/platform/
│   ├── model/          # Entity classes (User, Tip, Project, Discussion)
│   ├── dao/            # Data Access Objects (Interfaces + Implementations)
│   ├── service/        # Business logic layer
│   ├── ui/             # Swing GUI components
│   └── util/           # Database utilities
├── lib/                # External libraries (SQLite JDBC)
├── bin/                # Compiled classes (auto-generated)
└── README.md
```

## Innovative Features (Review 2 Enhancements)

1.  **Smart Search**: Real-time keyword search for tips
2.  **Data Export**: Export tips and user lists to CSV/TXT files
3.  **Profile Management**: Users can update their own information
4.  **Statistics Dashboards**: Analytics for both users and admins
5.  **Input Validation**: Comprehensive form validation with helpful error messages
6.  **Thread Pool Management**: Efficient image loading with memory optimization
7.  **Enhanced Logging**: Console output for debugging and monitoring

## Code Quality Features

- **Javadoc Comments**: Comprehensive documentation for classes and methods
- **Error Handling**: Try-catch blocks with user-friendly dialogs
- **Input Validation**: Prevents invalid data entry
- **Resource Management**: Proper connection closing, thread pool shutdown
- **Consistent Formatting**: Clean, readable code with proper indentation

## Technologies Used
- **Language**: Java 8+
- **GUI**: Swing (JFrame, JPanel, JTable, etc.)
- **Database**: SQLite with JDBC
- **Design Patterns**: DAO, MVC, Singleton (DBConnection)
- **Concurrency**: ExecutorService, Thread pools

## Future Enhancements
- User profile pictures
- Rating/like system for tips
- Email notifications
- Advanced search with filters
- Dark mode theme

## Developer Notes
This project demonstrates:
- Clean architecture with separation of concerns (Model-DAO-Service-UI)
- Best practices for database operations (PreparedStatement, transactions)
- Effective use of Java Collections Framework
- Thread safety and concurrency patterns
- Professional error handling and validation

## License
Educational project for Java GUI and JDBC demonstration.

## Author
Student Project - Galgotias University
