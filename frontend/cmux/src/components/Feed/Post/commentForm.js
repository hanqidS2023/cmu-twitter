import React, { useState } from 'react';
import { Dialog, DialogContent, DialogTitle, TextField, DialogActions, Button } from '@mui/material';

const CommentForm = ({ open, onClose, onSubmit }) => {
  const [commentData, setcommentData] = useState({
    username: '',
    content: '',
    author_id: null,
    created: null,
    communityPostid: null,
  });

  const handleChange = (e) => {
    const { name, value } = e.target;
    setcommentData({ ...commentData, [name]: value });
  };

  const handleSubmit = () => {
    onSubmit(commentData);
    onClose(); // Close the modal after submit
  };

  return (
    <Dialog open={open} onClose={onClose}>
      <DialogTitle>Create a New comment</DialogTitle>
      <DialogContent>
        <TextField
          margin="dense"
          name="content"
          label="Content"
          type="text"
          fullWidth
          multiline
          rows={4}
          variant="outlined"
          value={commentData.content}
          onChange={handleChange}
        />
      </DialogContent>
      <DialogActions>
        <Button onClick={onClose}>Cancel</Button>
        <Button onClick={handleSubmit}>comment</Button>
      </DialogActions>
    </Dialog>
  );
};

export default CommentForm;
