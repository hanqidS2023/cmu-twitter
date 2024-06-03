import React from 'react';
import { Dialog, DialogTitle, List } from '@mui/material';
import Post from '../../Feed/Post/Post';
import ProfileCard from '../../ProfileCard/ProfileCard';
import Button from '@mui/material/Button';
import ButtonGroup from '@mui/material/ButtonGroup';
import { flexbox } from '@material-ui/system';
function SearchResultsPopup({ open, onClose, searchResults, userSearchResults }) {
  const defualtColor = "gray";
  const activeColor = "#1da1f2";
  const [ userOrPost, setUserOrPost ] = React.useState('user');
  const [ isActivated, setIsActivated ] = React.useState(false);
  const [ isFollowing, setIsFollowing ] = React.useState(false);
  const onPostClick = (id) =>{
    console.log("")
  }
  const handleSearchType = () => {
    setIsActivated(!isActivated);
  }
  React.useEffect(() => {
    setIsFollowing(userSearchResults.isFollowing);
  }, [userSearchResults.isFollowing]);
  return (
    <Dialog open={ open } onClose={ onClose } fullWidth maxWidth="sm" PaperProps={ {
      style: { backgroundColor: '#15202b', opacity: 1, minHeight: '500px' } // Change as per your color preference
    } }>
      <div style={ { display: "flex", justifyContent: "space-around" } }>
        <DialogTitle color="white">Search Results</DialogTitle>
        <ButtonGroup style={ { margin: "15px" } } variant="contained" aria-label="outlined primary button group">
          <Button style={ { backgroundColor: (isActivated ? defualtColor : activeColor) } } onClick={ () => { setIsActivated(!isActivated); setUserOrPost('user') } }>User</Button>
          <Button style={ { backgroundColor: (!isActivated ? defualtColor : activeColor) } } onClick={ () => { setIsActivated(!isActivated); setUserOrPost('post') } }>Post</Button>
        </ButtonGroup>
      </div>
      { userOrPost === 'user' ?
        <List>
          {
            userSearchResults.map((user) => (
              <ProfileCard
                key={ user.userId }
                active={ true }
                username={ user.name }
                userId={ user.userId }
                initialIsFollowing={ user.isFollowing }
                following={ user.following }
                followers={ user.followers }
              />
            ))
          }
        </List>
       :
        <List>
          { searchResults.map((post) => (
            <Post
              key={ post.communityPostid }
              communityPostid={ post.communityPostid }
              username={ post.username }
              userimage={ post.userimage }
              created_Date={ post.created_Date }
              title={ post.title }
              content={ post.content }
              likes={ post.likes }
              comments={ post.comments }
              retweets={ post.retweets }
              commentsCount={ post.commentsCount }
              findTeammatePost={ post.findTeammatePost }
              instructorName={ post.instructorName }
              courseNumber={ post.courseNumber }
              semester={ post.semester }
              teamMembers={ post.teamMembers }
              onPostClick={onPostClick}
            />
          )) }
        </List>

      }


    </Dialog>
  );
}

export default SearchResultsPopup;
