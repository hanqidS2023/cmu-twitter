import React, { useState } from 'react';
import { Dialog, DialogContent, DialogTitle, TextField, DialogActions, Button, Checkbox, FormControlLabel } from '@mui/material';

const PostForm = ({ open, onClose, onSubmit }) => {
  const [isFindTeammatePost, setIsFindTeammatePost] = useState(false);
  const [postData, setPostData] = useState({
    username: '',
    title: '',
    content: '',
    authorid: null,
    created_Date: null,
    findTeammatePost: isFindTeammatePost,
    semester: '',
    teamMembers: '',
    instructorName: '',
    courseNumber: '',
  });

  const handleChange = (e) => {
    const { name, value } = e.target;
    setPostData({ ...postData, [name]: value });
  };


  const handleCheckboxChange = (e) => {
    setIsFindTeammatePost(e.target.checked);
    setPostData({ ...postData, findTeammatePost: e.target.checked });
  };

  const handleSubmit = () => {
    // Prepare the postData for submission
    const submitData = {
      ...postData,
      teamMembers: postData.teamMembers ? [postData.teamMembers] : [],
    };
  
    onSubmit(submitData);
    onClose(); // Close the modal after submit
  };

  return (
    <Dialog open={open} onClose={onClose}>
      <DialogTitle>Create a New Post</DialogTitle>
      <DialogContent>
        <TextField
          autoFocus
          margin="dense"
          name="title"
          label="Title"
          type="text"
          fullWidth
          variant="outlined"
          value={postData.title}
          onChange={handleChange}
        />
        <TextField
          margin="dense"
          name="content"
          label="Content"
          type="text"
          fullWidth
          multiline
          rows={4}
          variant="outlined"
          value={postData.content}
          onChange={handleChange}
        />
        <FormControlLabel
          control={
            <Checkbox
              checked={isFindTeammatePost}
              onChange={handleCheckboxChange}
            />
          }
          label="Is this a Find Teammate Post?"
        />
        {isFindTeammatePost && (
          <>
            <TextField
              margin="dense"
              name="semester"
              label="Semester"
              type="text"
              fullWidth
              variant="outlined"
              value={postData.semester}
              onChange={handleChange}
            />
            <TextField
              margin="dense"
              name="teamMembers"
              label="Team Members (Your name)"
              type="text"
              fullWidth
              variant="outlined"
              value={postData.teamMembers}
              onChange={handleChange}
            />
            <TextField
              margin="dense"
              name="instructorName"
              label="Instructor Name"
              type="text"
              fullWidth
              variant="outlined"
              value={postData.instructorName}
              onChange={handleChange}
            />
            <TextField
              margin="dense"
              name="courseNumber"
              label="Course Number"
              type="text"
              fullWidth
              variant="outlined"
              value={postData.courseNumber}
              onChange={handleChange}
            />
          </>
        )}
      </DialogContent>
      <DialogActions>
        <Button onClick={onClose}>Cancel</Button>
        <Button onClick={handleSubmit}>Post</Button>
      </DialogActions>
    </Dialog>
  );
};

export default PostForm;
