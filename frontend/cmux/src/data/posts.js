import {getPostById} from "../apis/communitypostAPIs/postAPI.js";

const posts = [
  {
    communityPostId: 1,
    userImage: "url_to_user_image_1",
    username: "user1",
    title: "Post Title 1",
    content: "This is the content of Post 1.",
    likes: 10,
    comments: ["Comment 1", "Comment 2"],
    retweets: 5,
    commentsCount: 2,
    created_Date: 1638384000000, // Date in milliseconds (for example, January 1, 2022)
  },
  {
    communityPostId: 2,
    userImage: "url_to_user_image_2",
    username: "user2",
    title: "Post Title 2",
    content: "This is the content of Post 2.",
    likes: 15,
    comments: ["Comment 1", "Comment 2", "Comment 3"],
    retweets: 8,
    commentsCount: 3,
    created_Date: 1638488000000, // Date in milliseconds (for example, January 2, 2022)
  },
  // Add more posts as needed
];




export default posts;
