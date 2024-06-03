import React, { useContext } from 'react';
import { useLocation } from "react-router";
import { EmojiIcon, FileIcon, PhotoIcon, SendIcon } from "../icons";
import { AuthContext } from '../AuthProvider';
import { useFetchWithTokenRefresh } from '../../utils/ApiUtilsDynamic';
import { chatSocketClient } from "../../ChatSocketClient";
import "./ChatInputs.css";

const ChatInputs = ({ addMessageToHistory }) => {
  const { userId } = useContext(AuthContext);
  var chatId = useLocation().pathname.split("/")[2];
  const [isFocus, setIsFocus] = React.useState(false);
  const [message, setMessage] = React.useState("");

  const fetchWithTokenRefresh = useFetchWithTokenRefresh();

  const handleFileUpload = async (event, type) => {
    const file = event.target.files[0];
    if (file) {
      const formData = new FormData();
      formData.append('file', file);
      formData.append('chatId', chatId);
      formData.append('senderId', userId);
      formData.append('messageType', type);
      try {
        const response = await fetchWithTokenRefresh(`${process.env.REACT_APP_URL}api/chats/file`, {
          method: 'POST',
          body: formData,
        });

        if (response.ok) {
          const responseData = await response.json();
          const fileUrl = responseData.fileUrl;
          console.log('File uploaded url:', fileUrl);
          const newMessage = {
            chatId,
            senderId: userId,
            content: fileUrl,
            messageType: type,
          };
          addMessageToHistory(newMessage);
        } else {
          // handle error
        }
      } catch (error) {
        console.error('Error uploading file:', error);
      }
    }
  };

  const sendMessage = async () => {
    if (message.trim() !== '') {
      const newMessage = {
        chatId,
        senderId: userId,
        content: message,
      };
      chatSocketClient.sendMessage(`/app/chat.sendMessage`, newMessage);
      setMessage('');
    }
  };

  return (
    <div className="chatInputs">
      <PhotoIcon onClick={() => document.getElementById('image-upload').click()} />
      <FileIcon onClick={() => document.getElementById('file-upload').click()} />
      <input
        type="file"
        id="image-upload"
        style={{ display: 'none' }}
        accept="image/*"
        onChange={(e) => handleFileUpload(e, 'IMAGE')}
      />
      <input
        type="file"
        id="file-upload"
        style={{ display: 'none' }}
        onChange={(e) => handleFileUpload(e, 'FILE')}
      />
      <div
        className={
          isFocus ? "chatTextInput chatTextInputFocus" : "chatTextInput"
        }
      >
        <input
          type="text"
          placeholder="Start a new message"
          onFocus={() => setIsFocus(true)}
          onBlur={() => setIsFocus(false)}
          onKeyDown={(e) => {
            e.key === "Enter" && sendMessage();
          }}
          value={message}
          onChange={(e) => setMessage(e.target.value)}
        />
        <EmojiIcon />
      </div>
      <div onClick={() => sendMessage()}>
        <SendIcon />
      </div>
    </div>
  );
};

export default ChatInputs;
