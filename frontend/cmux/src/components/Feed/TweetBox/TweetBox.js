import React, { useState } from "react";
import "./TweetBox.css";
import { Avatar } from "@material-ui/core";
import PhotoIcon from "../../icons/PhotoIcon";
import GifIcon from "../../icons/GifIcon";
import SurveyIcon from "../../icons/SurveyIcon";
import EmojiIcon from "../../icons/EmojiIcon";
import PlanIcon from "../../icons/PlanIcon";
import { useDispatch } from "react-redux";
import { addTweetAction } from "../../../store/actions/postActions";

function TweetBox({setPostFormOpen}) {
  const [tweet, setTweet] = useState({
    id: Date.now(),
    userimage:"",
    username: "mucahitsah",
    displayName: "Test User",
    text: "",
    shareImage: "",
    date: Date.now(),
  });

  const dispatch = useDispatch();
  const tweetSubmit = (e) => {
    e.preventDefault();
    if (tweet.text.trim() === "") return;
    console.log(tweet);
    dispatch(addTweetAction(tweet));
    setTweet({ ...tweet, text: "" });
  };
  return (
    <>
      <form className="tweetbox" onSubmit={(e) => tweetSubmit(e)}>
        <div className="tweetboxRow">
          <div style={{ flex: 0.1 }}></div>
          <div style={{justifyContent:"center"}} className="tweetboxOptions">
            <button type="submit" className="tweetbox-button" onClick={()=>{setPostFormOpen(true)}}>
              Post
            </button>
          </div>
        </div>
      </form>
      <div className="bottomBorder"></div>
    </>
  );
}

export default TweetBox;
