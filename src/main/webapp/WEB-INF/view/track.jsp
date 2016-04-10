<!DOCTYPE html>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>

<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form" %>

<html>
<head>
    <link href='http://fonts.googleapis.com/css?family=Bitter' rel='stylesheet' type='text/css'>
    <link href="<c:url value="/resources/css/main.css"/>" rel="stylesheet">
    <title>Track</title>
</head>
<body>

<c:url var="addUrl" value="/track/${trackId}/image/new"/>
<c:url var="deleteImgUrl" value="/resources/img/delete.png"/>
<c:url var="viewImgUrl" value="/resources/img/view.png"/>

<form:form class="desktop" modelAttribute="track" method="POST" action="${saveUrl}" enctype="multipart/form-data">

    <div class="section">
        <c:choose>
            <c:when test="${type=='existing'}">
                <c:url var="saveUrl" value="/track/${track.id}"/>
                Track
            </c:when>
            <c:when test="${type=='new'}">
                <c:url var="saveUrl" value="/track/new"/>
                new Track
            </c:when>
        </c:choose>
    </div>

    <div class="form-elements">
        <form:label path="name">
            Name
            <form:input type="text" path="name"/>
        </form:label>
        <div class="file">
            <input type="file" name="file" accept=".gpx">
        </div>
        </br>
    </div>

    <div class="menu">
        <a href="${addUrl}">Add image</a>
    </div>

    <table class="list">
        <thead>
        <tr>
            <td>Name</td>
            <th></th>
            <th></th>
        </tr>
        </thead>
        <tbody>
        <c:forEach items="${images}" var="image">
            <c:url var="downloadUrl" value="/track/${trackId}/image/${image.id}"/>
            <c:url var="deleteUrl" value="/track/${trackId}/image/delete?id=${image.id}"/>
            <tr>
                <td><c:out value="${image.name}"/></td>
                <td class="button"><a href="${downloadUrl}"><img src="${viewImgUrl}"/></a></td>
                <td class="button"><a href="${deleteUrl}"><img src="${deleteImgUrl}"/></a></td>
            </tr>
        </c:forEach>
        </tbody>
    </table>
    <c:if test="${empty images}">
        No images available
    </c:if>

    <br/>

    <div>
        <input type="submit" value="Save"/>
    </div>

    <br/>
    <br/>

</form:form>

</body>
</html>
