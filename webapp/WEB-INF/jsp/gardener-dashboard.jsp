<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="java.util.List" %>
<%@ page import="com.gardening.platform.model.Tip" %>
<!DOCTYPE html>
<html>
<head>
    <title>Gardener Dashboard</title>
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
            background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
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
        
        .add-tip-section {
            background: white;
            padding: 30px;
            border-radius: 10px;
            box-shadow: 0 2px 5px rgba(0,0,0,0.1);
            margin-bottom: 30px;
        }
        
        .add-tip-section h2 {
            color: #333;
            margin-bottom: 20px;
        }
        
        .form-group {
            margin-bottom: 15px;
        }
        
        label {
            display: block;
            margin-bottom: 5px;
            color: #555;
            font-weight: bold;
        }
        
        input[type="text"],
        textarea {
            width: 100%;
            padding: 10px;
            border: 1px solid #ddd;
            border-radius: 5px;
            font-size: 14px;
        }
        
        textarea {
            resize: vertical;
            min-height: 100px;
        }
        
        button {
            background: #667eea;
            color: white;
            padding: 12px 30px;
            border: none;
            border-radius: 5px;
            cursor: pointer;
            font-size: 16px;
        }
        
        button:hover {
            background: #5568d3;
        }
        
        .tips-section {
            background: white;
            padding: 30px;
            border-radius: 10px;
            box-shadow: 0 2px 5px rgba(0,0,0,0.1);
        }
        
        .tips-section h2 {
            color: #333;
            margin-bottom: 20px;
        }
        
        .tip-card {
            background: #f9f9f9;
            padding: 20px;
            border-radius: 8px;
            margin-bottom: 15px;
            border-left: 4px solid #667eea;
        }
        
        .tip-card h3 {
            color: #333;
            margin-bottom: 10px;
        }
        
        .tip-card p {
            color: #666;
            line-height: 1.6;
        }
        
        .tip-meta {
            margin-top: 10px;
            font-size: 12px;
            color: #999;
        }
        
        .success-message {
            background: #d5f4e6;
            color: #27ae60;
            padding: 15px;
            border-radius: 5px;
            margin-bottom: 20px;
        }
    </style>
</head>
<body>
    <div class="header">
        <h1>🌱 Gardener Dashboard - Welcome, <%= request.getAttribute("userName") %>!</h1>
        <a href="${pageContext.request.contextPath}/logout" class="logout-btn">Logout</a>
    </div>
    
    <div class="container">
        <% if (request.getParameter("success") != null) { %>
            <div class="success-message">✓ Tip added successfully!</div>
        <% } %>
        
        <div class="add-tip-section">
            <h2>Share a Gardening Tip</h2>
            <form method="post" action="${pageContext.request.contextPath}/gardener/addTip">
                <div class="form-group">
                    <label for="title">Title:</label>
                    <input type="text" id="title" name="title" required placeholder="Enter tip title">
                </div>
                
                <div class="form-group">
                    <label for="description">Description:</label>
                    <textarea id="description" name="description" required placeholder="Enter detailed description"></textarea>
                </div>
                
                <div class="form-group">
                    <label for="photoUrl">Photo URL (optional):</label>
                    <input type="text" id="photoUrl" name="photoUrl" placeholder="Enter image URL">
                </div>
                
                <button type="submit">Share Tip</button>
            </form>
        </div>
        
        <div class="tips-section">
            <h2>Community Tips</h2>
            <%
                List<Tip> tips = (List<Tip>) request.getAttribute("tips");
                if (tips != null && !tips.isEmpty()) {
                    for (Tip tip : tips) {
            %>
                <div class="tip-card">
                    <h3><%= tip.getTitle() %></h3>
                    <p><%= tip.getDescription() %></p>
                    <div class="tip-meta">
                        Posted on <%= tip.getDateShared() %>
                    </div>
                </div>
            <%
                    }
                } else {
            %>
                <p>No tips available yet. Be the first to share!</p>
            <%
                }
            %>
        </div>
    </div>
</body>
</html>
