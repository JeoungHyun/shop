<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>회원 정보</title>
</head>
<body>
<h2>환영합니다. ${sessionScope.loginUser.username }님</h2><hr>
<a href="mypage.shop?id=${loginUser.userid }">My Page</a><br>
<a href="logout.shop">로그아웃</a>
</body>
</html>