# File Storage REST Service
This is an application that allows us to store files in the cloud, categorize them with tags and search through them.

We won't store the actual file content, only their name and size at the moment.

##More information

###1. Upload
```
POST /file

{
   "name": "file_name.ext"
   "size" : 121231                           # size in bytes
}
```
returns status 200 and body:
```
{
   "ID": "unique file ID"
}
```
or status 400 with error if one of the field is absent or has incorrect value (like negative file size)
```
{
  "success": false,
  "error": "error description"
}
```
###2. Delete file
```
DELETE  /file/{ID}
```
returns status 200 and body
```
{"success": true}
```
or 404 and body
```
{
  "success": false,
  "error": "file not found"
}
```
###3. Assign tags to file
```
POST /file/{ID}/tags

["tag1", "tag2", "tag3"]
```
returns status 200 and body
```
{"success": true}
```
###4. Remove tags from file
```
DELETE /file/{ID}/tags

["tag1", "tag3"]
```
returns status 200 if all OK and body
```
{"success": true}
```
returns status 400 if one of the tags is not present on the file and body
```
{
  "success": false,
  "error": "tag not found on file"
}
```
###5. List files with pagination optionally filtered by tags
```
GET /file?tags=tag1,tag2,tag3&page=2&size=3
```

returns status 200 with body:
```
{
   "total": 25,
   "page": [
       {
          "id": "ID1",
          "name": "presentation.pdf",
          "size": 123123,
          "tags": ["work"]
       },
       {
          "id": "ID2",
          "name": "file.mp3",
          "size": 123123,
          "tags": ["audio", "jazz"]
       },
       {
          "id": "ID3",
          "name": "film.mp4",
          "size": 123123,
          "tags": ["video"]
       }
   ]
}
```