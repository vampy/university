<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
  <meta charset="utf-8" />
  <title>Library Client Web</title>
  <link href="/static/main.css" media="all" rel="stylesheet" type="text/css" />
</head>
<body>

<h1>Library Client Web</h1>

<form action="/login/" method="post">
  <div class="block">
    <label>Username: <input type="text" name="user"></label><br/>
  </div>

  <div class="block">
    <label>Password: <input type="password" name="password"></label><br/>
  </div>

  <div class="block">
    <input type="submit" value="Login">
  </div>
</form>

</body>
</html>
