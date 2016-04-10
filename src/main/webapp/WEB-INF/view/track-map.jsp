<!DOCTYPE html>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>

<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>

<html>
<head>
    <meta name="viewport" content="initial-scale=1.0, user-scalable=no" charset="utf-8">
    <link href="<c:url value="/resources/css/map.css"/>" rel="stylesheet">
    <title>Track</title>
</head>
<body>

<c:url var="downloadUrl" value="/api/track/${trackId}/map/download"/>
<c:url var="geotagsUrl" value="/api/track/${trackId}/geotags"/>
<c:url var="imgUrl" value="/api/track/${trackId}/image/"/>

<div id="map"></div>

<script src="https://ajax.googleapis.com/ajax/libs/jquery/1.12.0/jquery.min.js">

</script>

<script src="https://maps.googleapis.com/maps/api/js?key=AIzaSyBIEtFvEUP60aSI4AjwQAbW7TG5xlEburY&signed_in=true&callback=initMap" async defer>

</script>

<script>

    function initMap() {
        var map = new google.maps.Map(document.getElementById('map'));
        $.ajax({
            type: "GET",
            url: "${downloadUrl}",
            dataType: "xml",
            success: function(xml) {
                var points = [];
                var bounds = new google.maps.LatLngBounds ();
                $(xml).find("trkpt").each(function() {
                    var lat = $(this).attr("lat");
                    var lon = $(this).attr("lon");
                    var p = new google.maps.LatLng(lat, lon);
                    points.push(p);
                    bounds.extend(p);
                });

                var poly = new google.maps.Polyline({
                    path: points,
                    strokeColor: "#FF00AA",
                    strokeOpacity: .7,
                    strokeWeight: 4
                });

                poly.setMap(map);
                map.fitBounds(bounds);
            }
        });
        $.ajax({
            type: "GET",
            url: "${geotagsUrl}",
            dataType: 'json',
            contentType: 'application/json',
            success: function(json) {

                var infowindow = new google.maps.InfoWindow();

                for (i in json) {
                    var imgsrc = "<div><img width='320' height='180' src='${imgUrl}" + json[i].imageId + "'/></div>";
                    createMarker(imgsrc, json[i].lat, json[i].lng);
                }

                function createMarker(add,lat,lng) {
                    var contentString = add;
                    var marker = new google.maps.Marker({
                        position: new google.maps.LatLng(lat,lng),
                        map: map,
                    });

                    google.maps.event.addListener(marker, 'click', function() {
                        infowindow.setContent(contentString);
                        infowindow.open(map,marker);
                    });

                }

            }
        });
    }

</script>

</body>
</html>
