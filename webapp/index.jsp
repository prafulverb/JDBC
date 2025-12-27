<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html>
<head>
    <title>Welcome - Gardening Community Platform</title>
    <style>
        * {
            margin: 0;
            padding: 0;
            box-sizing: border-box;
        }
        
        body {
            font-family: 'Arial', sans-serif;
            background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
            height: 100vh;
            display: flex;
            flex-direction: column;
            justify-content: center;
            align-items: center;
            color: white;
        }
        
        .welcome-container {
            text-align: center;
            background: rgba(255,255,255,0.1);
            padding: 60px;
            border-radius: 20px;
            backdrop-filter: blur(10px);
        }
        
        h1 {
            font-size: 48px;
            margin-bottom: 20px;
        }
        
        p {
            font-size: 20px;
            margin-bottom: 40px;
        }
        
        .button-group {
            display: flex;
            gap: 20px;
            justify-content: center;
        }
        
        .btn {
            padding: 15px 40px;
            font-size: 18px;
            border: none;
            border-radius: 8px;
            cursor: pointer;
            text-decoration: none;
            transition: transform 0.3s;
        }
        
        .btn:hover {
            transform: translateY(-2px);
        }
        
        .btn-primary {
            background: white;
            color: #667eea;
        }
        
        .btn-secondary {
            background: rgba(255,255,255,0.2);
            color: white;
            border: 2px solid white;
        }
    </style>
</head>
<body>
    <div class="welcome-container">
        <h1>🌱 Gardening Community Platform</h1>
        <p>Connect with fellow gardeners and share your green thumb expertise!</p>
        <div class="button-group">
            <a href="${pageContext.request.contextPath}/login" class="btn btn-primary">Login</a>
            <a href="${pageContext.request.contextPath}/register" class="btn btn-secondary">Register</a>
        </div>
    </div>
</body>
</html>
