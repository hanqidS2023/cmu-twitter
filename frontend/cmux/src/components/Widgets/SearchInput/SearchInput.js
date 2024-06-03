import React, { useState } from "react";
import "./SearchInput.css";
import SearchIcon from "@material-ui/icons/Search";
import SearchResultsPopup from "./SearchPopup";
import { useSubscriptionApi } from "../../../apis/communitypostAPIs/subscriptionAPI";
import { useContext } from 'react';
import { AuthContext } from '../../AuthProvider';
import { Alarm } from "@material-ui/icons";
import { usePostApi } from "../../../apis/communitypostAPIs/postAPI";


function SearchInput({ placeholder }) {
  const baseUrl = process.env.REACT_APP_SUBSCRIPTION_SERVICE_URL;
  const [ isFocus, setIsFocus ] = React.useState(false);
  const [ searchText, setSearchText ] = React.useState('');
  const [ isPopupOpen, setIsPopupOpen ] = useState(false);
  const [ results, setResults ] = useState([]);
  const [ user_results, setUserResults ] = useState([]);
  const user_url = baseUrl + 'subscriptions/users';
  const follower_number_url = baseUrl + 'followers/count';
  const subscription_number_url = baseUrl + 'subscriptions/count';
  const has_subscription_url = baseUrl + 'subscriptions/has';
  const { fetchUser, fetchFollowers, fetchSubscriptions, fetchHasSubscription } = useSubscriptionApi();
  const { searchPosts } = usePostApi();
  // Mock userId, should be replaced by the real userId
  const userId = useContext(AuthContext).userId;
  // const userId = 1;
  //========================================
  

  const handleSearch = async () => {
    console.log('Searching for:');
    try {
      if (searchText === '') {
        setIsPopupOpen(false);
        return;
      }
      
      const res = await searchPosts(searchText);
      let user_res = [];
      // if searchText contains only numbers, search by userId
      const params = { u: searchText };
      const query = new URLSearchParams(params).toString();
      user_res = await fetchUser(user_url, query);
      // console.log('User search results:', user_res);

      if (res.length === 0 && user_res.length === 0) {
        setIsPopupOpen(false);
        return;
      }
      // Iterate over user_res array to update each user object
      for (let i = 0; i < user_res.length; i++) {
        const user = user_res[i];
        // Use the actual user ID from the user object
        const followerParams = { userId: user.userId };
        const subscriptionParams = { userId: user.userId };
        const followerQuery = new URLSearchParams(followerParams).toString();
        const subscriptionQuery = new URLSearchParams(subscriptionParams).toString();
      
        const user_followers = await fetchFollowers(follower_number_url, followerQuery);
        const user_subscriptions = await fetchSubscriptions(subscription_number_url, subscriptionQuery);
      
        const has_subscription_params = { userId: userId, otherUserId: user.userId };
        const has_subscription_query = new URLSearchParams(has_subscription_params).toString();
      
        const user_has_subscription = await fetchHasSubscription(has_subscription_url, has_subscription_query);
      
        // Update the user object
        user.followers = user_followers;
        user.following = user_subscriptions;
        user.isFollowing = user_has_subscription;
        user.isSelf = userId === user.userId;
      }
  
      setResults(res);
      setUserResults(user_res);
      console.log("setting popup open");
      setIsPopupOpen(true);
      return;
    }
    catch (err) {
      console.log(err);
    }
  }
  

  return (
    <div
      className={ isFocus ? "widgetsSearch widgetsSearchFocus" : "widgetsSearch" }
    >
      <SearchIcon
        className={
          isFocus
            ? "widgetsSearchIcon widgetsSearchIconFocus"
            : "widgetsSearchIcon"
        }
        style={ { cursor: "pointer" } }
        onClick={ () => handleSearch(searchText) }
      />

      <input
        className="widgetsSearchInput"
        type="text"
        placeholder={ placeholder }
        onFocus={ () => setIsFocus(true) }
        onBlur={ () => setIsFocus(false) }
        onChange={ (e) => setSearchText(e.target.value) }
      />
      <SearchResultsPopup
        open={ isPopupOpen }
        onClose={ () => setIsPopupOpen(false) }
        searchResults={ results }
        userSearchResults={ user_results }
      />
    </div>
  );
}

export default SearchInput;
