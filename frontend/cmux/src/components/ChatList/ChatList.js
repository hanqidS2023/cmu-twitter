import { Avatar } from "@material-ui/core";
import React from "react";
import { Link } from "react-router-dom";
import "./ChatList.css";

const ChatList = ({
  chatId,
  chatName,
  lastMessage,
  lastMessageTime,
}) => {
  return (
    <Link className="lastChat" to={`/Messages/${chatId}`}>
      <div>
        <Avatar src={''} />
      </div>
      <div>
        <div>
          <span>
            {chatName}
          </span>
          <span>
            {lastMessageTime}
          </span>
        </div>
        <span>{lastMessage}</span>
      </div>
    </Link>
  );
};

export default ChatList;
