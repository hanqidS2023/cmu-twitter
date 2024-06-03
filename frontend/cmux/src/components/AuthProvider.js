import React, { createContext, useState } from 'react';

const AuthContext = createContext(null);

const AuthProvider = ({ children }) => {
  const [authState, setAuthState] = useState({
    accessToken: localStorage.getItem('accessToken'),
    refreshToken: localStorage.getItem('refreshToken'),
    userId: localStorage.getItem('userId'),
    username: localStorage.getItem('username')
  });

  const signIn = ({ accessToken, refreshToken, userId, username }) => {
    localStorage.setItem('accessToken', accessToken);
    localStorage.setItem('refreshToken', refreshToken);
    localStorage.setItem('userId', userId);
    localStorage.setItem('username', username);
    setAuthState({ accessToken, refreshToken, userId, username });
  };

  const signOut = () => {
    localStorage.removeItem('accessToken');
    localStorage.removeItem('refreshToken');
    localStorage.removeItem('userId');
    localStorage.removeItem('username');
    setAuthState({});
  };

  const refreshToken = async () => {
    try {
      const response = await fetch(`${process.env.REACT_APP_URL}auth/refresh`, { 
        method: 'POST',
        headers: {
          'Content-Type': 'application/json'
        },
        body: JSON.stringify({ refreshToken: authState.refreshToken })
      });

      const data = await response.json();
      console.log('Refresh token response:', data);
      if (response.ok) {
        signIn({ 
          accessToken: data.accessToken, 
          refreshToken: data.refreshToken, 
          userId: data.userId, 
          username: data.username 
        });
      } else {
        signOut();
      }
    } catch (error) {
      console.error('Refresh token error:', error);
      signOut();
    }
  };

  return (
    <AuthContext.Provider value={{ ...authState, signIn, signOut, refreshToken }}>
      {children}
    </AuthContext.Provider>
  );
};

export {AuthContext, AuthProvider};