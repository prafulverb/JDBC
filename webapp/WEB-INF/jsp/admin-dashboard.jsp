<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="java.util.List" %>
<%@ page import="com.gardening.platform.model.User" %>
<!DOCTYPE html>
<html>
<head>
    <title>Admin Dashboard</title>
    <style>
        * {
            margin: 0;
            padding: 0;
            box-sizing: border-box;
        }
        
        body {
            font-family: 'Arial', sans-serif;
            background: #f5f5f5;
        }
        
        .header {
            background: linear-gradient(135deg, #e74c3c 0%, #c0392b 100%);
            color: white;
            padding: 20px;
            display: flex;
            justify-content: space-between;
            align-items: center;
            box-shadow: 0 2px 5px rgba(0,0,0,0.1);
        }
        
        .header h1 {
            font-size: 24px;
        }
        
        .logout-btn {
            background: rgba(255,255,255,0.2);
            color: white;
            padding: 10px 20px;
            border: none;
            border-radius: 5px;
            cursor: pointer;
            text-decoration: none;
            display: inline-block;
        }
        
        .logout-btn:hover {
            background: rgba(255,255,255,0.3);
        }
        
        .container {
            max-width: 1200px;
            margin: 30px auto;
            padding: 0 20px;
        }
        
        .users-section {
            background: white;
            padding: 30px;
            border-radius: 10px;
            box-shadow: 0 2px 5px rgba(0,0,0,0.1);
        }
        
        .users-section h2 {
            color: #333;
            margin-bottom: 20px;
        }
        
        table {
            width: 100%;
            border-collapse: collapse;
        }
        
        th, td {
            padding: 12px;
            text-align: left;
            border-bottom: 1px solid #ddd;
        }
        
        th {
            background: #e74c3c;
            color: white;
            font-weight: bold;
        }
        
        tr:hover {
            background: #f5f5f5;
        }
        
        .stats {
            display: grid;
            grid-template-columns: repeat(auto-fit, minmax(200px, 1fr));
            gap: 20px;
            margin-bottom: 30px;
        }
        
        .stat-card {
            background: white;
            padding: 20px;
            border-radius: 10px;
            box-shadow: 0 2px 5px rgba(0,0,0,0.1);
            text-align: center;
        }
        
        .stat-card h3 {
            color: #e74c3c;
            font-size: 36px;
            margin-bottom: 10px;
        }
        
        .stat-card p {
            color: #666;
        }
    </style>
</head>
<body>
    <div class="header">
        <h1>🛠️ Admin Dashboard - Welcome, <%= request.getAttribute("adminName") %>!</h1>
        <a href="${pageContext.request.contextPath}/logout" class="logout-btn">Logout</a>
    </div>
    
    <div class="container">
        <div class="stats">
            <%
                List<User> users = (List<User>) request.getAttribute("users");
                int totalUsers = users != null ? users.size() : 0;
                long gardenerCount = users != null ? users.stream().filter(u -> "GARDENER".equalsIgnoreCase(u.getRole())).count() : 0;
                long adminCount = users != null ? users.stream().filter(u -> "ADMIN".equalsIgnoreCase(u.getRole())).count() : 0;
            %>
            <div class="stat-card">
                <h3><%= totalUsers %></h3>
                <p>Total Users</p>
            </div>
            <div class="stat-card">
                <h3><%= gardenerCount %></h3>
                <p>Gardeners</p>
            </div>
            <div class="stat-card">
                <h3><%= adminCount %></h3>
                <p>Admins</p>
            </div>
        </div>
        
        <div class="users-section">
            <h2>User Management</h2>
            <table>
                <thead>
                    <tr>
                        <th>ID</th>
                        <th>Name</th>
                        <th>Email</th>
                        <th>Role</th>
                    </tr>
                </thead>
                <tbody>
                    <%
                        if (users != null && !users.isEmpty()) {
                            for (User user : users) {
                    %>
                        <tr>
                            <td><%= user.getId() %></td>
                            <td><%= user.getName() %></td>
                            <td><%= user.getEmail() %></td>
                            <td><%= user.getRole() %></td>
                        </tr>
                    <%
                            }
                        } else {
                    %>
                        <tr>
                            <td colspan="4" style="text-align: center;">No users found</td>
                        </tr>
                    <%
                        }
                    %>
                </tbody>
            </table>
        </div>
    </div>
</body>
</html>
