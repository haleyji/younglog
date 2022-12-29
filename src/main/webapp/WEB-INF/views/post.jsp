<%@ page language="java" contentType="text/html; charset=UTF-8"
         pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<!DOCTYPE html>
<html lang="ko">
<body>

<div class="container">
    <table class="table table-hover table table-striped">
        <tr>
            <th>번호</th>
            <th>제목</th>
            <th>내용</th>
        </tr>

        <c:forEach items="${list}" var="post">
            <tr>
                <th>${post.getId()}</th>
                <th>${post.getTitle()}</th>
                <th>${post.getContent()}</th>
            </tr>
        </c:forEach>
    </table>
</div>
<a href="/write">write</a>
</body>
</html>