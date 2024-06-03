import React, { useState, useEffect, useContext } from 'react';
import { chatSocketClient } from "../../ChatSocketClient";
import ChatInputs from '../ChatInputs/ChatInputs';
import { AuthContext } from '../../components/AuthProvider';
import MyMessage from '../MyMessage/MyMessage';
import FromMessage from '../FromMessage/FromMessage';
import { UsersIcon } from '../../components/icons';
import { useFetchWithTokenRefresh } from '../../utils/ApiUtilsDynamic';
import { useHistory } from 'react-router-dom';
import NotSelectedMessage from '../NotSelectedMessage/NotSelectedMessage';
import './Chat.css';


const Chat = ({ chat }) => {
  const [messageHistory, setMessageHistory] = useState([]);
  const { userId } = useContext(AuthContext);
  const [isUserListOpen, setIsUserListOpen] = useState(false);
  const [userList, setUserList] = useState([]);
  const [allUsers, setAllUsers] = useState([]);
  const [selectedUsers, setSelectedUsers] = useState([]);

  const history = useHistory();

  const addMessageToHistory = async (newMessage) => {
    const senderInfo = await fetchSender(newMessage.senderId);
    const messageWithSender = { ...newMessage, sender: senderInfo };
    setMessageHistory(prevHistory => [...prevHistory, messageWithSender]);
  };

  useEffect(() => {
    if (!chat) {
      history.push('/Messages');
      return <NotSelectedMessage />;
    }

    console.log('Chat changed:', chat);

    const establishConnection = async () => {
      console.log('Establishing websocket connection ...');
      await chatSocketClient.ensureConnection();
      chatSocketClient.subscribeToTopic(`/topic/chat.${chat.chatId}`, async (message) => {
        console.log('Received message:', message);
        const senderInfo = await fetchSender(message.senderId);
        const messageWithSender = { ...message, sender: senderInfo };
        setMessageHistory(prevHistory => [...prevHistory, messageWithSender]);
      });
    };

    establishConnection();

    return () => {
      chatSocketClient.unsubscribeFromTopic(`/topic/chat.${chat.chatId}`);
      // chatSocketClient.disconnect();
    };
  }, [chat]);


  const fetchWithTokenRefresh = useFetchWithTokenRefresh();

  const fetchAllUsers = async () => {
    try {
      // const url = `${process.env.REACT_APP_URL}user/all`;
      const url = `${process.env.REACT_APP_URL}followers/mutual?userId=${userId}`;
      const response = await fetchWithTokenRefresh(url, { method: 'GET' });
      if (response.ok) {
        const users = await response.json();
        setAllUsers(users);
      } else {
        console.error('Failed to fetch all users');
      }
    } catch (error) {
      console.error('Error fetching all users:', error);
    }
  };

  const fetchMessages = async () => {
    try {
      const url = `${process.env.REACT_APP_URL}api/chats/history/${chat.chatId}`;
      const response = await fetchWithTokenRefresh(url, { method: 'GET' });
      if (response.ok) {
        let data = await response.json();

        const messagesWithSender = await Promise.all(data.map(async (message) => {
          const sender = await fetchSender(message.senderId);
          return { ...message, sender };
        }));

        setMessageHistory(messagesWithSender);
        console.log('Chat history:', messagesWithSender);
      } else {
        console.error('Failed to fetch chat history');
      }
    } catch (error) {
      console.error('Error fetching chat history:', error);
    }
  };


  const fetchSender = async (userId) => {
    try {
      const url = `${process.env.REACT_APP_URL}user/${userId}`;
      const response = await fetchWithTokenRefresh(url, { method: 'GET' });
      if (response.ok) {
        const data = await response.json();
        return data;
      } else {
        throw new Error('Failed to fetch user');
      }
    } catch (error) {
      console.error('Error fetching user:', error);
      return null;
    }
  }

  const fetchUsers = async (userIds) => {
    try {
      const url = `${process.env.REACT_APP_URL}user/users`;
      const response = await fetchWithTokenRefresh(url, {
        method: 'POST',
        headers: {
          'Content-Type': 'application/json'
        },
        body: JSON.stringify(userIds)
      });
      if (response.ok) {
        const users = await response.json();
        return users;
      } else {
        console.error('Failed to fetch users');
        return [];
      }
    } catch (error) {
      console.error('Error fetching users:', error);
      return [];
    }
  };

  const fetchGroupUsers = async () => {
    try {
      const url = `${process.env.REACT_APP_URL}api/chats/groupusers/${chat.chatId}`;
      const response = await fetchWithTokenRefresh(url, { method: 'GET' });
      if (response.ok) {
        const data = await response.json();
        console.log('Group users:', data);
        return data;
      } else {
        console.error('Failed to fetch group users');
        return [];
      }
    } catch (error) {
      console.error('Error fetching group users:', error);
      return [];
    }
  };

  const addUserToGroup = async (selectedUserIds, chatId) => {
    try {
      const payload = selectedUserIds.map((userId) => parseInt(userId));

      if (payload.some(isNaN)) {
        console.error('Invalid user ID');
        return;
      }

      console.log('Adding users to group:', payload);
      const response = await fetchWithTokenRefresh(`${process.env.REACT_APP_URL}api/chats/groupusers/${chatId}`, {
        method: 'POST',
        headers: {
          'Content-Type': 'application/json'
        },
        body: JSON.stringify(payload)
      });

      if (response.ok) {
        return true;
      } else {
        console.error('Failed to add users to group');
        return false;
      }
    } catch (error) {
      console.error('Error adding users to group:', error);
      return false;
    }
  };

  const handleAddGroupUser = async (selectedUserIds) => {
    const addedSuccessfully = await addUserToGroup(selectedUserIds, chat.chatId);

    if (addedSuccessfully) {
      loadUserList();
    } else {
      console.error('Failed to add users to group');
    }
  };

  const handleDeleteGroupUser = async (userIdToDelete) => {
    try {
      const response = await fetchWithTokenRefresh(`${process.env.REACT_APP_URL}api/chats/groupusers/${chat.chatId}/${userIdToDelete}`, {
        method: 'DELETE'
      });

      if (response.ok) {
        if (userIdToDelete == userId) {
          history.push('/Messages');
        }
        loadUserList();
      } else {
        console.error('Failed to delete user from group');
      }
    } catch (error) {
      console.error('Error deleting user from group:', error);
    }
  };

  const handleUserListOpen = () => {
    setIsUserListOpen(true);
  };

  const handleUserListClose = () => {
    setIsUserListOpen(false);
  };

  const loadUserList = async () => {
    try {
      const userIdsInGroup = await fetchGroupUsers(chat.chatId);
      const usersInGroup = await fetchUsers(userIdsInGroup);

      const filteredUsers = allUsers.filter((user) => {
        return !usersInGroup.some((groupUser) => groupUser.id === user.id);
      });

      setUserList(usersInGroup);
      setAllUsers(filteredUsers);
    } catch (error) {
      console.error('Error loading user list:', error);
    }
  };

  const handleUserSelection = (userId) => {
    if (selectedUsers.includes(userId)) {
      setSelectedUsers(selectedUsers.filter((id) => id !== userId));
    } else {
      setSelectedUsers([...selectedUsers, userId]);
    }
  };

  const handleUserListToggle = () => {
    setIsUserListOpen(!isUserListOpen);
  };


  useEffect(() => {
    console.log('isUserListOpen changed:', isUserListOpen);
    if (isUserListOpen) {
      console.log('Loading user list ...');
      loadUserList();
    }
  }, [isUserListOpen]);


  useEffect(() => {
    fetchMessages();
    console.log('messageHistory changed:', messageHistory);
  }, [chat]);

  useEffect(() => {
    fetchAllUsers();
  }, []);


  if (!chat) {
    return <div className="chat">No chat selected</div>;
  }

  return (
    <div className="chat">
      <div className="chatHeader">
        <span>{chat.chatName}</span>
        {chat.chatType === 'GROUP' && <UsersIcon onClick={handleUserListOpen} />}
      </div>
      {isUserListOpen ? (
        <div className="userList">
          <button onClick={handleUserListClose}>Close</button>
          <h3>Group User List</h3>
          <ul>
            {userList.map((user, index) => (
              <React.Fragment key={user.id}>
                <li className="userListItem">
                  <div className="userAvatar">

                    <img src={user.userImage} alt={user.username} />
                  </div>
                  <div className="userName">
                    {user.username}
                  </div>
                  <button onClick={() => handleDeleteGroupUser(user.id)}>Delete</button>
                </li>
                {index < userList.length - 1 && <div className="userListSeparator"></div>}
              </React.Fragment>
            ))}
          </ul>
          <div className="userListSeparator"></div>
          <h3>Add User</h3>
          <div className="userList">
            <ul>
              {allUsers.map((user) => (
                <li className="userListItem" key={user.id}>
                  <div className="userAvatar">
                    <img src={user.userImage} alt={user.username} />
                  </div>
                  <div className="userName">{user.username}</div>
                  <button onClick={() => handleUserSelection(user.id)}>
                    {selectedUsers.includes(user.id) ? 'Unselect' : 'Select'}
                  </button>
                </li>
              ))}
            </ul>
          </div>
          <button onClick={() => handleAddGroupUser(selectedUsers)}>Add User</button>
        </div>
      ) : (<div className="chatRoom">
        <div className="chatMessages">
          {messageHistory.map((message) => {
            const isMyMessage = message.senderId == userId;

            return isMyMessage ? (
              <MyMessage message={message} />
            ) : (
              <FromMessage message={message} />
            );
          })}
        </div>
        <ChatInputs addMessageToHistory={addMessageToHistory} />
      </div>)}
    </div>
  );
};

export default Chat;
