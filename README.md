# Online Gardening Community Platform

## Overview
A comprehensive Java application for an online gardening community with **both Desktop (Swing GUI) and Web (Servlet-based)** interfaces. Supports two user roles: Admin and Gardener. Demonstrates advanced OOP principles, multithreading, JDBC database operations, and complete servlet lifecycle management with session handling.

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
├── webapp/             # Web application files
│   ├── WEB-INF/
│   │   ├── jsp/       # JSP view files
│   │   │   ├── login.jsp
│   │   │   ├── register.jsp
│   │   │   ├── gardener-dashboard.jsp
│   │   │   └── admin-dashboard.jsp
│   │   └── web.xml    # Servlet deployment descriptor
│   └── index.jsp      # Welcome page
└── README.md
```

## Review 2 Requirements - All Three Implemented ✅

### 1. Servlet Implementation (10 points) ✅
**Servlets Created:**
- `LoginServlet.java` - Handles authentication with session management
- `LogoutServlet.java` - Manages session invalidation
- `RegisterServlet.java` - Form handling for user registration with validation
- `GardenerDashboardServlet.java` - Session-based access control for gardeners
- `AdminDashboardServlet.java` - Role-based authorization for admins
- `AddTipServlet.java` - Form processing for tip submission

**Key Features Demonstrated:**
- **Servlet Lifecycle**: init(), doGet(), doPost(), destroy() methods implemented
- **Session Management**: HttpSession creation, attribute storage, timeout configuration (30 minutes)
- **Form Handling**: POST request processing, parameter extraction, input validation
- **Request Forwarding**: RequestDispatcher for JSP rendering
- **Redirects**: sendRedirect() for navigation after form submission

**JSP Pages:**
- Modern, responsive UI with embedded styles
- Dynamic content rendering with JSTL-style expressions
- Form validation with error/success messages
- Session-aware navigation

### 2. Code Quality & Execution (5 points) ✅
**Coding Standards:**
- Clean, well-structured code with consistent naming conventions
- Comprehensive Javadoc comments for all classes and methods
- Proper exception handling with try-catch blocks
- Resource management with try-with-resources for database connections
- Input validation to prevent invalid data entry
- Separation of concerns (MVC architecture)

**Readability:**
- Meaningful variable and method names
- Logical code organization by package (model, dao, service, ui, servlet)
- Consistent indentation and formatting
- Comments explaining complex logic

**Output Correctness:**
- Desktop app: Fully functional GUI with all features working
- Web app: Complete servlet-based interface with session management
- Database operations execute correctly with proper error handling
- Both interfaces share the same backend (DAO/Service layers)

### 3. Innovation / Extra Effort (2 points) ✅
**Beyond Minimum Requirements:**

1. **Dual Interface Architecture**: Built both desktop (Swing) AND web (Servlet) interfaces using shared backend
2. **Smart Search**: Real-time keyword filtering for tips
3. **Data Export**: CSV/TXT export functionality for tips and user lists
4. **Profile Management**: Self-service user profile updates
5. **Statistics Dashboards**: Analytics for user contributions and platform metrics
6. **Thread Pool Management**: Optimized image loading with ExecutorService
7. **Transaction Management**: Atomic database operations with rollback
8. **Modern Web Design**: Gradient backgrounds, hover effects, responsive cards
9. **Enhanced Security**: Role-based access control, session validation
10. **Comprehensive Logging**: Console output for debugging and monitoring

## Technologies Used
- **Language**: Java 8+
- **Desktop GUI**: Swing (JFrame, JPanel, JTable, JTabbedPane)
- **Web Framework**: Java Servlets + JSP
- **Database**: SQLite with JDBC
- **Design Patterns**: DAO, MVC, Singleton (DBConnection)
- **Concurrency**: ExecutorService, Thread pools, synchronized methods

## Running the Web Application

### Option 1: Using Apache Tomcat
1. **Download Apache Tomcat** (v9.0 or higher)
2. **Package as WAR:**
   ```bash
   # Create WAR structure
   mkdir -p build/WEB-INF/classes build/WEB-INF/lib
   
   # Copy compiled classes
   cp -r bin/com build/WEB-INF/classes/
   
   # Copy library
   cp lib/sqlite-jdbc-3.50.3.0.jar build/WEB-INF/lib/
   
   # Copy web files
   cp -r webapp/* build/
   
   # Create WAR file
   cd build
   jar -cvf ../gardening.war *
   ```

3. **Deploy:**
   - Copy `gardening.war` to Tomcat's `webapps/` folder
   - Start Tomcat: `./bin/startup.sh` (or `startup.bat` on Windows)
   - Access at: `http://localhost:8080/gardening/`

### Option 2: Using Embedded Server (Jetty)
Add Jetty dependency and create a launcher class for development.

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
