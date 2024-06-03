import React, { useState, useEffect, useContext } from "react";
import { Avatar } from "@material-ui/core";
import { useFetchWithTokenRefresh } from '../../utils/ApiUtilsDynamic';
import { AuthContext } from '../AuthProvider';
import './UserList.css';

const UserList = ({ onSelectUser, onCloseUserList }) => {
    const [allUsers, setAllUsers] = useState([]);
    const [selectedUser, setSelectedUser] = useState(null);
    const { userId } = useContext(AuthContext);

    const fetchWithTokenRefresh = useFetchWithTokenRefresh();

    const fetchAllUsers = async () => {
        try {
            const response = await fetchWithTokenRefresh(`${process.env.REACT_APP_URL}user/all`, {
                method: 'GET'
            });
            if (response.ok) {
                const data = await response.json();
                console.log('All users:', data);
                setAllUsers(data);
            } else {
                throw new Error('Failed to fetch users');
            }
        } catch (error) {
            console.error('Error fetching users:', error);
        }
    };

    const handleSelectUser = (user) => {
        console.log('Selected user:', user);
        onSelectUser(user);
        setSelectedUser(user);
    };

    useEffect(() => {
        fetchAllUsers();
    }, []);

    return (
        <div className="userListContainer">
            <div className="userListHeader">
                <h3>Select User To Talk</h3>
                <button onClick={onCloseUserList}>Close</button>
            </div>
            <ul className="userList">
                {allUsers.filter(user => user.id != userId)
                    .map((user) => (
                        <li
                            key={user.id}
                            onClick={() => handleSelectUser(user)}
                            className={selectedUser?.id === user.id ? 'selected' : ''}
                        >
                            <div className="userListItem">
                                <Avatar src={user.avatar} />
                                <span>{user.username}</span>
                            </div>
                        </li>
                    ))}
            </ul>
        </div>
    );
};

export default UserList;
