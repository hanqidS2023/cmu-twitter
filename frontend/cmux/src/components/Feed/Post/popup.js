import React from 'react';
import Dialog from '@mui/material/Dialog';
import DialogTitle from '@mui/material/DialogTitle';
import DialogContent from '@mui/material/DialogContent';
import DialogActions from '@mui/material/DialogActions';
import Button from '@mui/material/Button';

const CommonPopup = ({ isOpen, handleClose, text }) => {
  return (
    <Dialog open={isOpen} onClose={handleClose}>
      <DialogContent>
        <div>{text}</div>
      </DialogContent>
      <DialogActions>
        <Button onClick={handleClose}>Close</Button>
      </DialogActions>
    </Dialog>
  );
};

export default CommonPopup;
