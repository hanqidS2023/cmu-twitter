# Documentation for Community Post Microservice

## Post a new community post: 
*POST* `http://localhost:9000/community`

### Parameters:
> ```
> private long communityPostid;
> private String title;
> private String content;
> private Date created;
> private String author_id;
> private long likes;
> private int commentsCount;
> private boolean is_published;
> private List<Comment> comments;
> ```

### Sample inputbody: 
> ```
> {
>    "title": "My First Community Post",
>    "content": "This is the content of my first post in the community.",
>    "created": "2023-10-28T14:30:00Z",
>    "author_id": "user123",
>    "likes": 10,
>    "commentsCount": 0,
>    "is_published": true
> }
> ```
