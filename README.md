# GPSTracker
In progress: server side for GPS tracking trips solution on android (Spring REST, JSON, Hibernate, file upload, JavaScript, jQuery, Goodle Maps API 3.0)

Prototype demo: http://tracker-rubinatrix.rhcloud.com/

Available requests:

1)	Create track	
http://tracker-rubinatrix.rhcloud.com/api/track/	
POST	
Content-Type: application/json	
{
  "name": value
}
	
Success status: 201 Created
Headers:
Location: url for getting info about created track
Body:
{
  "id": new id,
  "name": value
}

2)	Change track info	
http://tracker-rubinatrix.rhcloud.com/api/track/{id}	
PUT	
Content-Type: application/json	
{
  "name": new value
}

Success status: 200 OK
Body:
{
  "id": value,
  "name": new value
}
Fail status: 404 Not Found

3)	Get track info	
http://tracker-rubinatrix.rhcloud.com/api/track/{id}	
GET			

Success status: 200 OK
Body:
{
  "id": value,
  "name": value
}
Fail status: 404 Not Found

4)	Delete track	
http://tracker-rubinatrix.rhcloud.com/api/track/{id}	
DELETE			

Success status: 204 No Content
Fail status: 404 Not Found

5)	Add .gpx for track	
http://tracker-rubinatrix.rhcloud.com/api/track/{id}/gpx/	
POST	
Content-Type: multipart/form-data
form-data
file = file content	

Success status: 200 OK
Body:
{
  "id": value,
  "name": value
}
Fail status: 404 Not Found

6)	Add .jpeg for track	
http://tracker-rubinatrix.rhcloud.com/api/track/{id}/image/	
POST	
Content-Type: multipart/form-data
form-data
file = file content	

Success status: 200 OK
Headers:
Location: url for downloaded image
Body:
{
  "id": new image id,
  "name": image name
}
Fail status: 404 Not Found

7)	Delete .jpeg from track	
http://tracker-rubinatrix.rhcloud.com/api/track/{id}/image/{id}	
DELETE			

Success status: 204 No Content
Fail status: 404 Not Found
