<!DOCTYPE html>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>

<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form" %>

<html>
<head>
    <link href='http://fonts.googleapis.com/css?family=Bitter' rel='stylesheet' type='text/css'>
    <link href="<c:url value="/resources/css/main.css"/>" rel="stylesheet">
    <title>Image</title>
</head>
<body>

<c:url var="saveUrl" value="/track/${trackId}/image/new"/>

<form:form class="desktop" method="POST" action="${saveUrl}" enctype="multipart/form-data">

    <div class="section">
        new Image
    </div>

    <div>
        <div class="file">
            <input type="file" name="file" accept="image/jpeg">
        </div>
        <input type="submit" value="Save"/>
    </div>

    <br/>
    <br/>

</form:form>

</body>
</html>
