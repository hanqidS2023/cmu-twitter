import React from "react";
import "./Reward.css";
import Widgets from "../../components/Widgets/Widgets";
import HomeBox from "../../components/HomeBox/HomeBox";
import Loading from "../../components/Loading/Loading";
import { useFetchWithTokenRefresh } from "../../utils/ApiUtilsDynamic";
import { useEffect, useState, useContext } from "react";
import { AuthContext } from "../../components/AuthProvider";
import ShopIcon from "../../components/ShopIcon/ShopIcon";
import Table from "@mui/material/Table";
import TableBody from "@mui/material/TableBody";
import TableCell from "@mui/material/TableCell";
import TableContainer from "@mui/material/TableContainer";
import TableHead from "@mui/material/TableHead";
import TableRow from "@mui/material/TableRow";
import Paper from "@mui/material/Paper";
import Modal from "@mui/material/Modal";
import Box from "@mui/material/Box";
import Button from "@mui/material/Button";
import Typography from "@mui/material/Typography";
import { useHistory } from 'react-router-dom';

function Reward() {
  const navigate = useHistory();
  const [isAll, setIsAll] = React.useState(true);
  const [loading, setLoading] = React.useState(true);
  const [images, setImages] = useState([]);
  const [creditValue, setCreditValue] = useState({
    userId: undefined,
    username: undefined,
    coins: undefined,
    points: undefined,
  });
  const [userProfile, setUserProfile] = useState();
  const [chosenProductId, setChosenProductId] = useState();
  const { userId, username } = useContext(AuthContext);
  const [availableImages, setAvailableImages] = useState([]);
  const [creditHistory, setCreditHistory] = useState([]);
  const [openModal, setOpenModal] = useState(false);

  const fetchHelper = useFetchWithTokenRefresh();

  setTimeout(() => {
    setLoading(false);
  }, 2000);

  const formatTime = (timestamp) => {
    const date = new Date(timestamp);
    const year = date.getFullYear();
    const month = (date.getMonth() + 1).toString().padStart(2, "0");
    const day = date.getDate().toString().padStart(2, "0");
    const hours = date.getHours().toString().padStart(2, "0");
    const minutes = date.getMinutes().toString().padStart(2, "0");

    return `${year}-${month}-${day} ${hours}:${minutes}`;
  };

  const handleOpenModal = (productId) => {
    setChosenProductId(productId);
    setOpenModal(true);
  };

  const handleCloseModal = () => {
    setChosenProductId(null);
    setOpenModal(false);
  };

  const filterProduct = () => {
    const filteredProducts = images.filter(
      (product) => !userProfile.unlockedImageIds.includes(product.id)
    );
    setAvailableImages(filteredProducts);
  };

  const handleButton = (product) => {
    const purchasable = product.purchasable;
    if (purchasable) {
      return product.price > creditValue.coins;
    }
    return product.price > creditValue.points;
  };

  const handlePurchase = async () => {
    try {
      const url = `${process.env.REACT_APP_URL}shop/purchaseProduct`;
      const response = await fetchHelper(url, {
        method: "POST",
        headers: {
          "Content-Type": "application/json",
        },
        body: JSON.stringify({
          userId: userId,
          productId: chosenProductId,
        }),
      });

      const json = await response.json();

      if (response.ok) {
        handleCloseModal();
        navigate.push('/reward-success');
      }
    } catch (error) {
      console.error(error);
    }
  };

  const fetchAllProducts = async () => {
    const url = `${process.env.REACT_APP_URL}shop/allProducts`;
    try {
      const response = await fetchHelper(url, {
        method: "GET",
      });
      if (response.ok) {
        const data = await response.json();
        setImages(data);
      }
    } catch (error) {
      console.error(error);
    } finally {
      setLoading(false);
    }
  };

  const fetchUserCredit = async () => {
    const url = `${process.env.REACT_APP_URL}shop/userCredit/${userId}`;
    try {
      const response = await fetchHelper(url, {
        method: "GET",
      });
      if (response.ok) {
        const credit = await response.json();
        setCreditValue(credit);
      }
    } catch (error) {
      console.error(error);
    } finally {
      setLoading(false);
    }
  };

  const fetchUserProfile = async () => {
    const url = `${process.env.REACT_APP_URL}user/${userId}`;
    try {
      const response = await fetchHelper(url, {
        method: "GET",
      });
      if (response.ok) {
        const profile = await response.json();
        setUserProfile(profile);
      }
    } catch (error) {
      console.error(error);
    } finally {
      setLoading(false);
    }
  };

  const fetchCreditHistory = async () => {
    const url = `${process.env.REACT_APP_URL}shop/creditHistory/${userId}`;
    try {
      const response = await fetchHelper(url, {
        method: "GET",
      });
      if (response.ok) {
        const history = await response.json();
        setCreditHistory(history);
      }
    } catch (error) {
      console.error(error);
    } finally {
      setLoading(false);
    }
  };

  const handleHistory = async () => {
    setIsAll(false);
    setLoading(true);
    try {
      await fetchCreditHistory();
    } catch (error) {
      console.error(error);
    } finally {
      setLoading(false);
    }
  };

  const fetchData = async () => {
    setLoading(true);
    try {
      await fetchUserProfile();
      await fetchUserCredit();
    } catch (error) {
      console.error(error);
    } finally {
      setLoading(false);
    }
  };

  useEffect(() => {
    fetchData();
  }, []);

  useEffect(() => {
    if (userProfile) {
      fetchAllProducts();
    }
  }, [userProfile]);

  useEffect(() => {
    if (userProfile && images) {
      filterProduct();
    }
  }, [images]);

  if (loading) {
    return <Loading />;
  }
  return (
    <HomeBox>
      <div className="feed">
        <div>
          <Modal
            open={openModal}
            onClose={handleCloseModal}
            aria-labelledby="purchase-confirmation"
            aria-describedby="purchase-confirmation-description"
          >
            <Box className="modalBox">
              <Typography
                id="purchase-confirmation"
                component="h2"
                className="modalTitle"
              >
                Confirm Purchase
              </Typography>
              <Typography id="purchase-confirmation-description">
                Are you sure you want to make this purchase?
              </Typography>
              <div className="modalButtons">
                <Button onClick={() => handlePurchase()}>Yes</Button>
                <Button onClick={handleCloseModal}>No</Button>
              </div>
            </Box>
          </Modal>

          <div className="rewardTitle">
            {creditValue && (
              <>
                <p>Welcome to Reward System, {username}</p>
                <br />
                <br />
                <p>
                  You have {creditValue.coins} coins and {creditValue.points}{" "}
                  points
                </p>
              </>
            )}
          </div>
          <div className="rewardCategory">
            <div
              className={isAll && "rewardActive"}
              onClick={() => setIsAll(true)}
            >
              <span>Icon Shop</span>
            </div>
            <div
              className={!isAll && "rewardActive"}
              onClick={async () => await handleHistory()}
            >
              <span>View Credit History</span>
            </div>
          </div>
          <div>
            {isAll && (
              <div className="productList">
                {availableImages.map((product, index) => (
                  <ShopIcon
                    key={index}
                    product={product}
                    handleButton={handleButton(product)}
                    unit={product.purchasable ? "coins" : "points"}
                    handleModal={() =>handleOpenModal(product.id)}
                  ></ShopIcon>
                ))}
              </div>
            )}
            {!isAll && (
              <TableContainer component={Paper}>
                <Table sx={{ minWidth: 500 }} aria-label="credit history table">
                  <TableHead>
                    <TableRow className="tableRow">
                      <TableCell className="tableCell">Name</TableCell>
                      <TableCell align="right" className="tableCell">
                        Time
                      </TableCell>
                      <TableCell align="right" className="tableCell">
                        Coins
                      </TableCell>
                      <TableCell align="right" className="tableCell">
                        Points
                      </TableCell>
                    </TableRow>
                  </TableHead>
                  <TableBody>
                    {creditHistory.reverse().map((history, index) => (
                      <TableRow key={index} className="tableRow">
                        <TableCell
                          className="tableCell"
                          component="th"
                          scope="row"
                        >
                          {username}
                        </TableCell>
                        <TableCell align="right" className="tableCell">
                          {formatTime(history.timestamp)}
                        </TableCell>
                        <TableCell align="right" className="tableCell">
                          {history.coins}
                        </TableCell>
                        <TableCell align="right" className="tableCell">
                          {history.points}
                        </TableCell>
                      </TableRow>
                    ))}
                  </TableBody>
                </Table>
              </TableContainer>
            )}
          </div>
        </div>
      </div>
      <Widgets />
    </HomeBox>
  );
}

export default Reward;
