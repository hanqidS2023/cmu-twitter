import React from "react";
import { Avatar } from "@material-ui/core";
import "./FromMessage.css";

const FromMessage = ({ message }) => {
  const formatDate = (date) => {
    const d = new Date(date);
    const hours = d.getHours().toString().padStart(2, '0');
    const minutes = d.getMinutes().toString().padStart(2, '0');
    const day = d.getDate().toString().padStart(2, '0');
    const month = (d.getMonth() + 1).toString().padStart(2, '0');
    const year = d.getFullYear();
    return `${month}/${day}/${year} ${hours}:${minutes}`;
  };

  const renderContent = () => {
    if (message.messageType === 'IMAGE') {
      return <img src={message.fileUrl} alt="Uploaded Image" className="chat-image" />;
    } else if (message.messageType === 'FILE') {
      return <a href={message.fileUrl} download className="messageContent chat-file">Download File</a>;
    } else {
      return <span className="messageContent">{message.content}</span>;
    }
  };

  return (
    <div className="fromMessageContainer">
      <div className="fromMessage">
        <div className="avatarContainer">
          <Avatar src={message.sender.userImage} />
        </div>
        <div className="messageDetails">
          <div className="messageHeader">
            <span className="receiverName">{message.sender.username}</span>
            <div className="messageTimestamp">{formatDate(message.timestamp)}</div>
          </div>
          {renderContent()}
        </div>
      </div>
    </div>
  );
};

export default FromMessage;
