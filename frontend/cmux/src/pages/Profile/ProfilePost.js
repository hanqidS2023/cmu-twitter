import React from "react";
import { Avatar } from "@material-ui/core";
import "./ProfilePost.css"; // Create a separate CSS file for styling

function ProfilePost({
  userImage,
  username,
  title,
  content,
  likes,
  comments,
  created_Date,
}) {
  return (
    <div className="profile-post">
      <div>
        <Avatar src={userImage} />
      </div>
      <div className="post-content-col">
        <div className="post-header">
          <span className="post-header-displayname">{username}</span>
          <span className="post-header-date">{created_Date}</span>
        </div>
        <div style={{fontWeight:"bolder", fontSize:"25px", justifyContent:"center"}}>{title}</div>
        <div className="post-content" style={{color:"white"}}>{content}</div>
        {/* Display other post details like title, likes, comments as needed */}
      </div>
    </div>
  );
}

export default ProfilePost;
