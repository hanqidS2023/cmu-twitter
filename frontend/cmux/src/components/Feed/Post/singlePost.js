import React, {useEffect, useState} from "react";
import { Dialog, DialogContent, DialogTitle, TextField, DialogActions, Button } from '@mui/material';
import { Avatar } from "@material-ui/core";
import "./Post.css";
import FavoriteIcon from "../../icons/FavoriteIcon";
import CommentIcon from "../../icons/CommentIcon";
import RetweetIcon from "../../icons/RetweetIcon";
import SharePostIcon from "../../icons/SharePostIcon";
import MoreHorizIcon from "@material-ui/icons/MoreHoriz";
import { MillToDate } from "../../../utils/MillToDate";
import {StompClientSingleton} from "../../../socketClient";
import ProfileCard from "../../ProfileCard/ProfileCard";
import DeleteIcon from "@material-ui/icons/Delete";
import { usePostApi } from "../../../apis/communitypostAPIs/postAPI";
import Comments from "./comments";

// communityPostid, userImage, username, title, content, likes, comments, retweets, onCommentClick, commentsCount, created_Date  
function SinlgePost({open, onClose, communityPostid}) {
  const [isVisibleProfileCard, setIsVisibleProfileCard] = useState(false);
  const [authorid, setauthorid] = useState(null);
  const [username, setUsername] = useState(null);
  const [title, setTitle] = useState(null);
    const [content, setContent] = useState(null);
    const [likes, setLikes] = useState(null);
    const [comments, setComments] = useState(null);
    const [commentsCount, setCommentsCount] = useState(null);
    const [created_Date, setCreated_Date] = useState(null);
    const [findTeammatePost, setFindTeammatePost] = useState(false);
    const [instructorName, setInstructorName] = useState(null);
    const [courseNumber, setCourseNumber] = useState(null);
    const [semester, setSemester] = useState(null);
    const [teamMembers, setTeamMembers] = useState(null);
    const { fetchPostsByAuthorIds, createPost, addLike, getPostById, deletePost, updatePost, markAsFindTeammatePost, addTeamMembers, searchPosts } = usePostApi();

  useEffect(() => {
    const fetchPost = async () => {
        try{
            const res = await getPostById(communityPostid);
            if(!res) return console.log("no post found");
            setauthorid(res.authorid);
            setUsername(res.username);
            setTitle(res.title);
            setContent(res.content);
            setLikes(res.likes);
            setComments(res.comments);
            setCommentsCount(res.commentsCount);
            setCreated_Date(res.created_Date);
            setFindTeammatePost(res.findTeammatePost);
            setInstructorName(res.instructorName);
            setCourseNumber(res.courseNumber);
            setSemester(res.semester);
            setTeamMembers(res.teamMembers);
        }catch(err){
            console.log(err);
        }
    }
    fetchPost();
  }, []);
  const onLikeClick = async (communityPostid) => {
    const res = await addLike(communityPostid);
  }
  
  return (
    <Dialog open={open} onClose={onClose} >
    <div className="post-with-comment" style={{minWidth:"25vw"}}>
        <div className="post" onMouseLeave={() => setIsVisibleProfileCard(false)}>
            <ProfileCard active={isVisibleProfileCard && true} />
            <div>
                <Avatar/>
            </div>
            <div className="post-content-col">
                <div className="post-header">
                <span
                    className="post-header-displayname"
                    onMouseLeave={() => {
                    setTimeout(function () {
                        setIsVisibleProfileCard(false);
                    }, 1000);
                    }}
                    style={{color:"black"}}
                >
                    {username}
                </span>
                <span className="post-header-date">{created_Date}</span>
                <MoreHorizIcon className="postMoreIcon" />
                </div>
                <div style={{fontWeight:"bolder", fontSize:"25px", justifyContent:"center"}}>{title}</div>
                <div className="post-content" style={{color:"black"}}>{content}</div>
                {findTeammatePost && (
          <div style={{marginTop:"10px", color:"black"}}>
            <div style={{display:"flex", justifyContent:"center"}}> FindTeammate Post</div>
            <div className="post-findTeammate-section" style={{ border:"1px solid white"}}>
              <div style={{display:"flex", justifyContent:"space-evenly"}}>
                <div className="post-findTeammate-course">
                  <span>Course: </span>
                  <span>{courseNumber}</span>
                </div>
                <div className="post-findTeammate-semester">
                  <span>Semester: </span>
                  <span>{semester}</span>
                </div>
              </div>
                <div className="post-findTeammate-teamMembers" style={{display:"flex", justifyContent:"center"}}>
                  <span>Team Members: </span>
                  <span>{teamMembers}</span>
                </div>
                <div className="post-findTeammate-instructor" style={{display:"flex", justifyContent:"center"}}>
                  <span>Instructor: </span>
                  <span>{instructorName}</span>
                </div>
              </div>
          </div>
        )}
                {/* {shareImage && (
                <div className="post-image">
                    <img src={shareImage} alt="shareimage" />
                </div>
                )} */}
                <div className="post-event">
                <div>
                    <CommentIcon className="postIcon" />
                    <span>{commentsCount}</span>
                </div>
                <div>
                    <FavoriteIcon className="postIcon" onClick={()=>{onLikeClick(communityPostid)}}/>
                    <span>{likes}</span>
                </div>
                </div>
            </div>
        </div>
    </div>
    {comments &&
        comments.map((comment) => (
            <Comments key={comment.commentid} comment={comment}/>
        ))
    }
    </Dialog>
  );
}

export default SinlgePost;
