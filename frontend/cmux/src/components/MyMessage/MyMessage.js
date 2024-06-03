import React from "react";
import { Avatar } from "@material-ui/core";
import "./MyMessage.css";

const MyMessage = ({ message }) => {
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
      return <a href={message.fileUrl} download className="myMessageContent chat-file">Download File</a>;
    } else {
      return <span className="myMessageContent">{message.content}</span>;
    }
  };

  return (
    <div className="myMessageContainer">
      <div className="messageTimestamp">{formatDate(message.timestamp)}</div>
      <div className="myMessage">
        {renderContent()}
        <div className="avatarContainer">
          <Avatar src={message.sender.userImage} />
        </div>
      </div>
    </div>
  );
};

export default MyMessage;
