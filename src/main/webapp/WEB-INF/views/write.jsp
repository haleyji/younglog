<%@ page language="java" contentType="text/html; charset=UTF-8"
         pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<!DOCTYPE html>
<html lang="ko">
<header>
    <script   src="https://code.jquery.com/jquery-3.6.3.min.js"   integrity="sha256-pvPw+upLPUjgMXY0G+8O0xUf+/Im1MZjXxxgOcBQBXU="   crossorigin="anonymous"></script>
</header>
<body>

<div class="container">

    <table class="table table-hover table table-striped">
        <tr>
            <th>제목</th>
            <th>내용</th>
        </tr>
        <tr>
            <td><input type="text" name="title"></td>
            <td><textarea name="content"></textarea></td>
        </tr>
    </table>
    <button type="button" onclick="submitForm();">write</button>
</div>
</body>
<script>

    const submitForm = () => {
       let title = document.querySelector('input[name="title"]').value;
       let content = document.querySelector('textarea[name="content"]').value;

       let param = {
           'title': title,
           'content': content
       }

       $.ajax({
           url:'/posts',
           method:'POST',
           contentType:'application/json; charset=utf-8;',
           dataType:'json',
           data: JSON.stringify(param),
           success: function(res){
               window.location.href='/post';
           }
       })

    }

</script>
</html>