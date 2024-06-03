import * as React from "react";
import Card from "@mui/material/Card";
import CardActions from "@mui/material/CardActions";
import CardContent from "@mui/material/CardContent";
import "./ShopIcon.css";
import Button from "@mui/material/Button";
import Typography from "@mui/material/Typography";
import { Avatar } from "@material-ui/core";

export default function ShopIcon({ product, handleModal, handleButton, unit }) {
  return (
    <div className="avatar">
      <Card sx={{ width: 300 }}>
        <div className="avatar-icon">
          <Avatar
            alt="example"
            src={product.imageUrl}
            style={{ width: "100px", height: "100px" }}
          />
        </div>
        <CardContent>
          <Typography gutterBottom variant="h5" component="div">
            name: {product.name}
          </Typography>
          <Typography gutterBottom variant="h5" component="div">
            price: {product.price}
          </Typography>
        </CardContent>
        <CardActions>
          <Button size="small" onClick={handleModal} disabled={handleButton}>
            {!handleButton ? "BUY" : `Require ${product.price} ${unit}`}
          </Button>
        </CardActions>
      </Card>
    </div>
  );
}
