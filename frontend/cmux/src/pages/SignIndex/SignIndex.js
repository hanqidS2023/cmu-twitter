import React from "react";
import { Link } from "react-router-dom";
import ChatIcon from "../../components/icons/ChatIcon";
import Logo from "../../components/icons/Logo";
import SearchIcon from "../../components/icons/SearchIcon";
import UsersIcon from "../../components/icons/UsersIcon";
import "./SignIndex.css";

function SignIndex() {
  return (
    <div className="container">
      <div className="col1">
        <div className="logo">
          <Logo />
        </div>

      </div>
      <div className="col2">
        <div className="menu">
          <span className="header">
            See what's happening in CMU right now
          </span>
          <span className="join">Join CMUX today.</span>
          <div className="buttons">
            <Link to="/login" className="login">
              <div className="loginItem">
                <span className="loginText">Login</span>
              </div>
            </Link>
          </div>
        </div>
      </div>
    </div>
  );
}

export default SignIndex;
