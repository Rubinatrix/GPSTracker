<!DOCTYPE html>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>

<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<html>
<head>
    <link href='http://fonts.googleapis.com/css?family=Bitter' rel='stylesheet' type='text/css'>
    <link href="<c:url value="/resources/css/main.css"/>" rel="stylesheet">
    <title>Track list</title>
</head>
<body>

<c:url var="addUrl" value="/track/new"/>
<c:url var="deleteImgUrl" value="/resources/img/delete.png"/>
<c:url var="viewImgUrl" value="/resources/img/view.png"/>

<div class="desktop">

    <div class="info">
        <div class="error">${param.contentError}</div>
    </div>

    <div class="menu">
        <a href="${addUrl}">Add track</a>
    </div>

    <table class="list">
        <thead>
        <tr>
            <th>Name</th>
            <th>Map</th>
            <th></th>
        </tr>
        </thead>
        <tbody>
        <c:forEach items="${tracks}" var="track">
            <c:url var="editUrl" value="/track/${track.id}"/>
            <c:url var="viewUrl" value="/track/${track.id}/map"/>
            <c:url var="deleteUrl" value="/track/delete?id=${track.id}"/>
            <tr>
                <td><a href="${editUrl}"><c:out value="${track.name}"/></a></td>
                <td class="button"><a href="${viewUrl}"><img src="${viewImgUrl}"/></a></td>
                <td class="button"><a href="${deleteUrl}"><img src="${deleteImgUrl}"/></a></td>
            </tr>
        </c:forEach>
        </tbody>
    </table>
    <c:if test="${empty tracks}">
        No tracks available
    </c:if>
</div>

</body>
</html>
