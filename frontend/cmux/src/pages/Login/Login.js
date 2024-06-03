import React, { useContext,useState } from "react";
import {AuthContext} from '../../components/AuthProvider';
import { Link, useHistory } from "react-router-dom";
import Logo from "../../components/icons/Logo";
import TextInput from "../../components/TextInput/TextInput";
import "./Login.css";

function Login() {
  const [userName, setUserName] = useState('');
  const [password, setPassword] = useState('');
  const [errorMessage, setErrorMessage] = useState('');

  const { signIn } = useContext(AuthContext);

  const history = useHistory();

  const handleUserNameChange = (e) => {
    console.log('Username change:', e.target.value);
    setUserName(e.target.value);
  };

  const handlePasswordChange = (e) => {
    setPassword(e.target.value);
  };

  const handleLogin = async () => {
    console.log(userName, password);
    try {
      const response = await fetch(`${process.env.REACT_APP_URL}auth/signin`, {
        method: 'POST',
        headers: {
          'Content-Type': 'application/json',
        },
        body: JSON.stringify({
          username: userName,
          password: password
        }),
      });

      const json = await response.json();
    
      if (response.ok) {
        console.log(json.userName);
        signIn({
          accessToken: json.accessToken,
          refreshToken: json.refreshToken,
          userId: json.userId,
          username: json.username,
        });
        history.push('/home');
      } else {
        setErrorMessage(json.message || 'Login failed');
      }
    } catch (error) {
      console.error('Login error:', error);
      setErrorMessage('An error occurred. Please try again later.');
    }
  };

  return (
    <div className="container">
      <div className="panel">
        <div className="panelHeader">
          <Logo width={39} fill="white" />
          <span className="panelHeaderText">Login to CMUX</span>
        </div>
        <div className="inputs">
          <TextInput type="text" value={userName} onChange={handleUserNameChange} text = "Username" />
          <TextInput type="password" value={password} onChange={handlePasswordChange} text="Password" />
        </div>
        <button onClick={handleLogin} className="loginBtn">
          <span className="loginText">Login</span>
        </button>
        {errorMessage && <div className="error-message">{errorMessage}</div>}
        <div className="loginLinks">
          <Link to="/signup">
            <span className="link">Sign up on CMUX</span>
          </Link>
        </div>
      </div>
    </div>
  );
}

export default Login;
