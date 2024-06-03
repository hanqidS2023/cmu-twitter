import React from "react";
import "./Reward.css";
import Widgets from "../../components/Widgets/Widgets";
import HomeBox from "../../components/HomeBox/HomeBox";
import Button from "@mui/material/Button";
import { useHistory } from "react-router-dom";

function Success() {
  const history = useHistory();
  const handleClick = () => {
    history.push("/reward");
  };
  return (
    <HomeBox>
      <div className="feed">
        <div className="successTitle">
          <span>Sucess!</span>
        </div>
        <Button variant="contained" onClick={handleClick}>
          Return to shop
        </Button>
      </div>
      <Widgets />
    </HomeBox>
  );
}

export default Success;
